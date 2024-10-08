package uk.ac.ebi.intact.network.ws.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.network.ws.controller.model.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;
import uk.ac.ebi.intact.style.model.shapes.NodeShape;
import uk.ac.ebi.intact.style.service.StyleService;

import java.awt.*;
import java.util.List;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@RestController
public class NetworkController {

    private static final Log log = LogFactory.getLog(NetworkController.class);

    public static final int NETWORK_LIMIT = 1500;

    private final InteractionSearchService interactionSearchService;
    private final StyleService styleService;

    @Autowired
    public NetworkController(InteractionSearchService interactionSearchService, StyleService styleService) {
        this.interactionSearchService = interactionSearchService;
        this.styleService = styleService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/getInteractions",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<NetworkJson> getGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "isCompound", required = false) boolean isCompound,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "", required = false) int pageSize) {

        HttpStatus httpStatus = HttpStatus.OK;
        NetworkJson networkJson = null;
        long interactionsCount = interactionSearchService.countInteractionsForGraphJson(
                query,
                batchSearch,
                advancedSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter.booleanValue,
                mutationFilter,
                expansionFilter,
                minMIScore,
                maxMIScore,
                intraSpeciesFilter
        );

        if (interactionsCount > NETWORK_LIMIT) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            FacetPage<SearchInteraction> interactions = interactionSearchService.findInteractionForGraphJsonWithFacet(
                    query,
                    batchSearch,
                    advancedSearch,
                    interactorSpeciesFilter,
                    interactorTypesFilter,
                    interactionDetectionMethodsFilter,
                    interactionTypesFilter,
                    interactionHostOrganismsFilter,
                    negativeFilter.booleanValue,
                    mutationFilter,
                    expansionFilter,
                    minMIScore,
                    maxMIScore,
                    intraSpeciesFilter,
                    page,
                    pageSize);

            networkJson = toCytoscapeJsonFormat(interactions.getContent(), isCompound, null, null, null);
//                    interactions.getFacetResultPage(SearchInteractionFields.SPECIES_A_B_STR).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()), //TODO Replace by taxids of A and B
//                    interactions.getFacetResultPage(SearchInteractionFields.TYPE_MI_IDENTIFIER).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()),
//                    interactions.getFacetResultPage(SearchInteractionFields.TYPE_MI_A).get().map(FacetFieldEntry::getValue).collect(Collectors.toSet()) //TODO replace by a faceting of both A and B
//            );
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
        boolean edgeNegative = false;
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

                networkLink.setColor(styleService.getInteractionColor(searchInteraction.getTypeMIIdentifier()));
                networkLink.setCollapsedColor(styleService.getSummaryInteractionColor(searchInteraction.getIntactMiscore()));
                interactionTypeFacets.add(searchInteraction.getTypeMIIdentifier()); // TODO: Remove on Solr faceting

                boolean spokeExpanded = searchInteraction.getExpansionMethod() != null;
                if (spokeExpanded) edgeExpanded = true;
                networkLink.setShape(styleService.getInteractionShape(spokeExpanded));

                boolean affectedByMutation = searchInteraction.isAffectedByMutation();
                if (affectedByMutation) edgeAffectedByMutation = true;
                networkLink.setAffectedByMutation(affectedByMutation);

                boolean negative = searchInteraction.isNegative();
                if (negative) edgeNegative = true;
                networkLink.setNegative(negative);

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

        return new NetworkJson(edgesAndNodes, styleService.createLegend(taxIdFacets, interactorTypeFacets, nodeMutated, interactionTypeFacets, edgeExpanded, edgeAffectedByMutation, edgeNegative));
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

            networkNode.setColor(styleService.getInteractorColor(taxId.toString()));
            networkNode.setShape(styleService.getInteractorShape(typeMI));
            networkNode.setBorderColor(styleService.getInteractorBorderColor(isMutated));
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