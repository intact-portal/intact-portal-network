package uk.ac.ebi.intact.network.ws.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.network.ws.controller.model.*;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.LegendBuilder;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.*;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous.MIScoreMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous.SummaryEdgeWidthMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.InteractionTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.InteractorTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.MIOntology;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.TaxonMapper;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@RestController
public class NetworkController {

    //TODO temporary
    public static final String UPLOADED_BATCH_FILE_PREFIX = "file_";
    private static final Log log = LogFactory.getLog(NetworkController.class);

    private static final TaxonMapper taxonMapper = new TaxonMapper();
    private static final MIOntology miOntology = new MIOntology();
    private static final InteractorTypeMapper interactorTypeMapper = new InteractorTypeMapper(miOntology);
    private static final InteractorMutationMapper interactorMutationMapper = new InteractorMutationMapper();
    private static final InteractorMutationBorderWidthMapper interactorMutationBorderWidthMapper = new InteractorMutationBorderWidthMapper();

    private static final MIScoreMapper miScoreMapper = new MIScoreMapper();
    private static final InteractionExpansionMapper interactionExpansionMapper = new InteractionExpansionMapper();
    private static final InteractionTypeMapper interactionTypeMapper = new InteractionTypeMapper(miOntology);
    private static final InteractionMutationColorMapper interactionMutationColorMapper = new InteractionMutationColorMapper();
    private static final InteractionMutationWidthMapper interactionMutationWidthMapper = new InteractionMutationWidthMapper();
    private static final SummaryEdgeWidthMapper summaryEdgeWidthMapper = new SummaryEdgeWidthMapper();

    private static final LegendBuilder legendBuilder = new LegendBuilder(taxonMapper, interactorTypeMapper,
            interactionTypeMapper, miScoreMapper, interactionMutationColorMapper,
            interactionMutationWidthMapper, interactorMutationMapper, interactorMutationBorderWidthMapper,
            interactionExpansionMapper, summaryEdgeWidthMapper);


    @Value("${server.upload.batch.file.path}")
    private String uploadBatchFilePath;


    private InteractionSearchService interactionSearchService;

    @Autowired
    public NetworkController(InteractionSearchService interactionSearchService) {
        this.interactionSearchService = interactionSearchService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/getInteractions",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<NetworkJson> getGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "interactionDetectionMethodFilter", required = false) Set<String> interactionDetectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiScore", defaultValue = "0", required = false) double minMiScore,
            @RequestParam(value = "maxMiScore", defaultValue = "1", required = false) double maxMiScore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "isCompound", required = false) boolean isCompound,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "", required = false) int pageSize) {

        HttpStatus httpStatus = HttpStatus.OK;
        NetworkJson networkJson = null;
        long interactionsCount = interactionSearchService.countInteractionsForGraphJson(
                extractSearchTerms(query),
                batchSearch,
                interactorSpeciesFilter,
                interactorTypeFilter,
                interactionDetectionMethodFilter,
                interactionTypeFilter,
                interactionHostOrganismFilter,
                isNegativeFilter,
                minMiScore,
                maxMiScore,
                interSpecies
        );

        if (interactionsCount > 1300) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            FacetPage<SearchInteraction> interactions = interactionSearchService.findInteractionForGraphJsonWithFacet(
                    extractSearchTerms(query),
                    batchSearch,
                    interactorSpeciesFilter,
                    interactorTypeFilter,
                    interactionDetectionMethodFilter,
                    interactionTypeFilter,
                    interactionHostOrganismFilter,
                    isNegativeFilter,
                    minMiScore,
                    maxMiScore,
                    interSpecies,
                    page,
                    pageSize);

            networkJson = toCytoscapeJsonFormat(interactions.getContent(), isCompound,
                    interactions.getFacetResultPage(SearchInteractionFields.SPECIES_A_B_STR).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()), //TODO Replace by taxids of A and B
                    interactions.getFacetResultPage(SearchInteractionFields.TYPE_MI_IDENTIFIER).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()),
                    interactions.getFacetResultPage(SearchInteractionFields.TYPE_MI_A).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()) //TODO replace by a faceting of both A and B
            );
        }
        //initialising empty json if request is forbidden
        networkJson = (networkJson == null) ? new NetworkJson() : networkJson;

        return new ResponseEntity<>(networkJson, httpStatus);
    }

    private NetworkJson toCytoscapeJsonFormat(List<SearchInteraction> interactions, boolean isCompound, Set<String> taxIdFacets, Set<String> interactionTypeFacets, Set<String> interactorTypeFacets) {
        List<Object> edgesAndNodes = new ArrayList<>();
        Map<String, NetworkNode> acToNode = new HashMap<>();
        boolean nodeMutated = false;
        boolean edgeExpanded = false;
        boolean edgeAffectedByMutation = false;

        // TODO: Remove when Facets uses Solr
        taxIdFacets = new HashSet<>();
        interactionTypeFacets = new HashSet<>();
        interactorTypeFacets = new HashSet<>();
        // TO SUPPRESS


        for (SearchInteraction searchInteraction : interactions) {
            try {
                NetworkEdgeGroup networkEdgeGroup = new NetworkEdgeGroup();
                NetworkLink networkLink = new NetworkLink();
                networkLink.setId(searchInteraction.getBinaryInteractionId());
                networkLink.setSource(searchInteraction.getAcA());
                if (searchInteraction.getAcB() != null) {
                    networkLink.setTarget(searchInteraction.getAcB());
                } else {
                    networkLink.setTarget(networkLink.getSource());
                }
                networkLink.setInteractionAc(searchInteraction.getAc());
                networkLink.setInteractionType(searchInteraction.getType());
                networkLink.setInteractionDetectionMethod(searchInteraction.getDetectionMethod());

                networkLink.setColor(interactionTypeMapper.getStyleOf(searchInteraction.getTypeMIIdentifier()));
                networkLink.setCollapsedColor(miScoreMapper.getStyleOf(searchInteraction.getIntactMiscore()));
                interactionTypeFacets.add(searchInteraction.getTypeMIIdentifier()); // TODO: Remove on Solr faceting
                boolean spokeExpanded = searchInteraction.getExpansionMethod() != null;
                if (spokeExpanded) edgeExpanded = true;
                networkLink.setShape(interactionExpansionMapper.getStyleOf(spokeExpanded));

                boolean affectedByMutation = searchInteraction.isDisruptedByMutation();
                if (affectedByMutation) edgeAffectedByMutation = true;
                networkLink.setAffectedByMutation(affectedByMutation);
                networkLink.setMiScore(searchInteraction.getIntactMiscore());
                networkEdgeGroup.setInteraction(networkLink);

                if (searchInteraction.getAcA() != null) {
                    boolean isMutated = searchInteraction.isMutationA();
                    registerNode(acToNode, edgesAndNodes,
                            searchInteraction.getAcA(), searchInteraction.getTaxIdA(), searchInteraction.getSpeciesA(), isCompound,
                            searchInteraction.getMoleculeA(), searchInteraction.getUniqueIdA(), searchInteraction.getIdA(),
                            searchInteraction.getTypeA(), searchInteraction.getTypeMIA(), isMutated, taxIdFacets, interactorTypeFacets);
                    if (isMutated) nodeMutated = true;
                }

                if (searchInteraction.getAcB() != null) {
                    boolean isMutated = searchInteraction.isMutationB();
                    registerNode(acToNode, edgesAndNodes,
                            searchInteraction.getAcB(), searchInteraction.getTaxIdB(), searchInteraction.getSpeciesB(), isCompound,
                            searchInteraction.getMoleculeB(), searchInteraction.getUniqueIdB(), searchInteraction.getIdB(),
                            searchInteraction.getTypeB(), searchInteraction.getTypeMIB(), isMutated, taxIdFacets, interactorTypeFacets);
                    if (isMutated) nodeMutated = true;
                }
                edgesAndNodes.add(networkEdgeGroup);
            } catch (Exception e) {
                log.info("Interaction with ac: " + searchInteraction.getAc() + " failed to process" +
                        " and therefore this interaction will not be in graph json");
                log.error(e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
                //TODO... Uncomment following
                throw e;
            }
        }

        return new NetworkJson(edgesAndNodes, legendBuilder.createLegend(taxIdFacets, interactorTypeFacets, nodeMutated, interactionTypeFacets, edgeExpanded, edgeAffectedByMutation));
    }

    public NetworkNodeGroup createMetaNode(String parentTaxId, String species) {
        NetworkNode graphCompoundNode = new NetworkNode();
        NetworkNodeGroup graphCompoundNodeGroup = new NetworkNodeGroup();
        graphCompoundNode.setId(parentTaxId);
        graphCompoundNode.setColor(new Color(220, 220, 220));
        graphCompoundNode.setSpeciesName(species);
        graphCompoundNodeGroup.setInteractor(graphCompoundNode);
        graphCompoundNode.setShape(NodeShape.ELLIPSE);

        return graphCompoundNodeGroup;
    }

    //TODO temporary
    private String extractSearchTerms(String query) {

        StringBuilder searchTerms = new StringBuilder();

        if (query.startsWith(UPLOADED_BATCH_FILE_PREFIX)) {
            File uploadedBatchFile = new File(uploadBatchFilePath + File.separator + query);
            if (uploadedBatchFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(uploadedBatchFile));
                    String line;
                    int count = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (count > 0) {
                            searchTerms.append(",").append(line);
                        } else {
                            searchTerms = new StringBuilder(line);
                        }
                        count++;
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            searchTerms = new StringBuilder(query);
        }

        return searchTerms.toString();
    }

    private String createNodeLabel(String preferredName, String preferredId, String interactorAc) {
        if (preferredName != null) {
            return preferredName + " (" + preferredId + ")";
        } else {
            return interactorAc;
        }
    }

    private void registerNode(Map<String, NetworkNode> acToNode, List<Object> edgesAndNodes,
                              String ac, Integer taxId, String species, boolean isCompound,
                              String molecule, String uniqueId, String id,
                              String type, String typeMI, boolean isMutated,
                              Set<String> taxIdFacets, Set<String> interactorTypeIdFacets) {
        if (!acToNode.containsKey(ac)) {
            NetworkNode networkNode = new NetworkNode();
            NetworkNodeGroup networkNodeGroup = new NetworkNodeGroup();
            String parentTaxId = taxId + "";
            networkNode.setId(ac);
            networkNode.setSpeciesName(species);
            networkNode.setTaxId(taxId);
            if (isCompound) {
                edgesAndNodes.add(createMetaNode(parentTaxId, species));
                networkNode.setParent(parentTaxId);
            }
            networkNode.setInteractorId(createNodeLabel(molecule, uniqueId, ac));
            networkNode.setPreferredIdWithDB(id);
            networkNode.setInteractorType(type);
            networkNode.setPreferredId(uniqueId);
            networkNode.setInteractorName(molecule);

            networkNode.setColor(taxonMapper.getStyleOf(taxId.toString()));
            networkNode.setColorArchetype(taxonMapper.getArchetype(taxId.toString()));
            networkNode.setShape(interactorTypeMapper.getStyleOf(typeMI));
            networkNode.setBorderColor(interactorMutationMapper.getStyleOf(isMutated));
            taxIdFacets.add(taxId.toString()); // TODO: Remove on Solr faceting
            interactorTypeIdFacets.add(typeMI); // TODO: Remove on Solr faceting

            networkNode.setClusterId(taxId);
            networkNode.setMutation(isMutated);
            networkNodeGroup.setInteractor(networkNode);

            acToNode.put(ac, networkNode);
            edgesAndNodes.add(networkNodeGroup);
        } else if (isMutated) {
            NetworkNode existingNetworkNode = acToNode.get(ac);
            existingNetworkNode.setMutation(true);
        }
    }
}
