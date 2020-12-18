package uk.ac.ebi.intact.network.ws.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.network.ws.controller.model.*;
import uk.ac.ebi.intact.network.ws.controller.utils.ColourCodes;
import uk.ac.ebi.intact.network.ws.controller.utils.NetworkUtility;
import uk.ac.ebi.intact.network.ws.controller.utils.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.StyleMapper;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@RestController
public class NetworkController {

    //TODO temporary
    public static final String UPLOADED_BATCH_FILE_PREFIX = "file_";
    private static final Log log = LogFactory.getLog(NetworkController.class);
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
            Page<SearchInteraction> interactions = interactionSearchService.findInteractionForGraphJson(
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

            networkJson = toCytoscapeJsonFormat(interactions.getContent(), isCompound);
        }
        //initialising empty json if request is forbidden
        networkJson = (networkJson == null) ? new NetworkJson(new ArrayList<Object>()) : networkJson;

        return new ResponseEntity<NetworkJson>(networkJson, httpStatus);
    }

    private NetworkJson toCytoscapeJsonFormat(List<SearchInteraction> interactions, boolean isCompound) {
        List<Object> edgesAndNodes = new ArrayList<>();
        HashMap<String, NetworkNode> interactorAcAndNodeMap = new HashMap<String, NetworkNode>();
        HashSet<String> specieSet = new HashSet<>();

        Set<String> taxIds = new HashSet<>();
        interactions.forEach(searchInteraction -> {
            Integer taxIdA = searchInteraction.getTaxIdA();
            if (taxIdA != null) taxIds.add(taxIdA.toString());
            Integer taxIdB = searchInteraction.getTaxIdB();
            if (taxIdB != null) taxIds.add(taxIdB.toString());
        });
        StyleMapper.harvestKingdomsOf(taxIds, true);

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
                networkLink.setColor(StyleMapper.getColorForInteractionType(searchInteraction.getTypeMIIdentifier()));
                networkLink.setCollapsedColor(NetworkUtility.getColorForCollapsedEdgeDiscrete(searchInteraction.getIntactMiscore()));
                networkLink.setShape(NetworkUtility.getShapeForExpansionType(searchInteraction.getExpansionMethod()));
                networkLink.setAffectedByMutation(searchInteraction.isDisruptedByMutation());
                networkLink.setMiScore(searchInteraction.getIntactMiscore());
                networkEdgeGroup.setInteraction(networkLink);

                if (searchInteraction.getAcA() != null) {
                    if (!interactorAcAndNodeMap.containsKey(searchInteraction.getAcA())) {
                        NetworkNode networkNode = new NetworkNode();
                        NetworkNodeGroup networkNodeGroup = new NetworkNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdA() + "";
                        networkNode.setId(searchInteraction.getAcA());
                        networkNode.setSpeciesName(searchInteraction.getSpeciesA());
                        networkNode.setTaxId(searchInteraction.getTaxIdA());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesA()));
                            }
                            networkNode.setParent(parentTaxId);
                        }
                        networkNode.setInteractorId(NetworkUtility.createNodeLabel(searchInteraction.getMoleculeA(),
                                searchInteraction.getUniqueIdA(), searchInteraction.getAcA()));
                        networkNode.setPreferredId(searchInteraction.getIdA());
                        networkNode.setPreferredIdWithDB(searchInteraction.getIdA());
                        networkNode.setInteractorType(searchInteraction.getTypeA());
                        networkNode.setPreferredId(searchInteraction.getUniqueIdA());
                        networkNode.setInteractorName(searchInteraction.getMoleculeA());
                        networkNode.setColor(StyleMapper.getColorForTaxId(searchInteraction.getTaxIdA()));
                        networkNode.setShape(StyleMapper.getShapeForInteractorType(searchInteraction.getTypeMIA()));
                        networkNode.setClusterId(searchInteraction.getTaxIdA());
                        networkNode.setMutation(searchInteraction.isMutationA());
                        networkNodeGroup.setInteractor(networkNode);

                        interactorAcAndNodeMap.put(searchInteraction.getAcA(), networkNode);
                        edgesAndNodes.add(networkNodeGroup);
                    } else if (searchInteraction.isMutationA()) {
                        NetworkNode existingNetworkNode = interactorAcAndNodeMap.get(searchInteraction.getAcA());
                        existingNetworkNode.setMutation(searchInteraction.isMutationA());
                    }
                }

                if (searchInteraction.getAcB() != null) {
                    if (!interactorAcAndNodeMap.keySet().contains(searchInteraction.getAcB())) {
                        NetworkNode networkNode = new NetworkNode();
                        NetworkNodeGroup networkNodeGroup = new NetworkNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdB() + "";
                        networkNode.setId(searchInteraction.getAcB());
                        networkNode.setSpeciesName(searchInteraction.getSpeciesB());
                        networkNode.setTaxId(searchInteraction.getTaxIdB());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesB()));
                            }
                            networkNode.setParent(parentTaxId);
                        }
                        networkNode.setInteractorId(NetworkUtility.createNodeLabel(searchInteraction.getMoleculeB(),
                                searchInteraction.getUniqueIdB(), searchInteraction.getAcB()));
                        networkNode.setPreferredId(searchInteraction.getIdB());
                        networkNode.setPreferredIdWithDB(searchInteraction.getIdB());
                        networkNode.setInteractorType(searchInteraction.getTypeB());
                        networkNode.setPreferredId(searchInteraction.getUniqueIdB());
                        networkNode.setInteractorName(searchInteraction.getMoleculeB());
                        networkNode.setColor(StyleMapper.getColorForTaxId(searchInteraction.getTaxIdB()));
                        networkNode.setShape(StyleMapper.getShapeForInteractorType(searchInteraction.getTypeMIB()));
                        networkNode.setClusterId(searchInteraction.getTaxIdB());
                        networkNode.setMutation(searchInteraction.isMutationB());
                        networkNodeGroup.setInteractor(networkNode);
                        interactorAcAndNodeMap.put(searchInteraction.getAcB(), networkNode);
                        edgesAndNodes.add(networkNodeGroup);
                    } else if (searchInteraction.isMutationB()) {
                        NetworkNode existingNetworkNode = interactorAcAndNodeMap.get(searchInteraction.getAcB());
                        existingNetworkNode.setMutation(searchInteraction.isMutationB());
                    }
                }
                edgesAndNodes.add(networkEdgeGroup);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getAcB() + " failed to process" +
                        " and therefore this interaction will not be in graph json");
                log.error(e.getMessage());
                //TODO... Uncomment following
                //throw e;
            }
        }

        return new NetworkJson(edgesAndNodes);
    }

    public NetworkNodeGroup createMetaNode(String parentTaxId, String species) {
        NetworkNode graphCompoundNode = new NetworkNode();
        NetworkNodeGroup graphCompoundNodeGroup = new NetworkNodeGroup();
        graphCompoundNode.setId(parentTaxId);
        graphCompoundNode.setColor(ColourCodes.META_NODE);
        graphCompoundNode.setSpeciesName(species);
        graphCompoundNodeGroup.setInteractor(graphCompoundNode);
        graphCompoundNode.setShape(NodeShape.ELLIPSE.title);

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
}
