package uk.ac.ebi.intact.network.ws.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.network.ws.controller.result.*;
import uk.ac.ebi.intact.network.ws.controller.utils.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.util.*;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/network")
public class SearchGraphController {

    private static final Log log = LogFactory.getLog(SearchGraphController.class);

    private InteractionSearchService interactionSearchService;

    @Autowired
    public SearchGraphController(InteractionSearchService interactionSearchService) {
        this.interactionSearchService = interactionSearchService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getJson",
            params = {
                    "query",
            },
            method = RequestMethod.GET)
    public NetworkJson getGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "speciesFilter", required = false) Set<String> speciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "hostOrganismFilter", required = false) Set<String> hostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiScore", defaultValue = "0", required = false) double minMiScore,
            @RequestParam(value = "maxMiScore", defaultValue = "1", required = false) double maxMiScore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "isCompound", required = false) boolean isCompound,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "", required = false) int pageSize) {

        Page<SearchInteraction> interactions = interactionSearchService.findInteractionForGraphJson(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, page, pageSize);

        return toCytoscapeJsonFormat(interactions.getContent(), isCompound);
    }

    private NetworkJson toCytoscapeJsonFormat(List<SearchInteraction> interactions, boolean isCompound) {
        NetworkJson graphCompoundJson = new NetworkJson();
        List<Object> edgesAndNodes = new ArrayList<>();
        HashMap<String, NetworkNode> interactorAcAndNodeMap = new HashMap<String, NetworkNode>();
        HashSet<String> specieSet = new HashSet<>();
        Integer interactionCounter = 1;


        for (SearchInteraction searchInteraction : interactions) {
            try {
                NetworkEdgeGroup networkEdgeGroup = new NetworkEdgeGroup();
                NetworkLink networkLink = new NetworkLink();
                networkLink.setId(searchInteraction.getBinaryInteractionId());
                networkLink.setSource(searchInteraction.getInteractorAAc());
                if (searchInteraction.getInteractorBAc() != null) {
                    networkLink.setTarget(searchInteraction.getInteractorBAc());
                } else {
                    networkLink.setTarget(networkLink.getSource());
                }
                networkLink.setInteractionAc(searchInteraction.getInteractionAc());
                networkLink.setInteractionType(searchInteraction.getInteractionType());
                networkLink.setInteractionDetectionMethod(searchInteraction.getInteractionDetectionMethod());
                networkLink.setColor(NetworkUtility.getColorForInteractionType(searchInteraction.getInteractionTypeMIIdentifier()));
                networkLink.setCollapsedColor(NetworkUtility.getColorForCollapsedEdge(searchInteraction.getIntactMiscore()));
                networkLink.setShape(NetworkUtility.getShapeForExpansionType(searchInteraction.getExpansionMethod()));
                networkLink.setDisruptedByMutation(searchInteraction.isInteractionDisruptedByMutation());
                networkLink.setMiScore(searchInteraction.getIntactMiscore());
                networkEdgeGroup.setInteraction(networkLink);

                if (searchInteraction.getInteractorAAc() != null) {
                    if (!interactorAcAndNodeMap.keySet().contains(searchInteraction.getInteractorAAc())) {
                        NetworkNode networkNode = new NetworkNode();
                        NetworkNodeGroup networkNodeGroup = new NetworkNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdA() + "";
                        networkNode.setId(searchInteraction.getInteractorAAc());
                        networkNode.setSpeciesName(searchInteraction.getSpeciesA());
                        networkNode.setTaxId(searchInteraction.getTaxIdA());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesA()));
                            }
                            networkNode.setParent(parentTaxId);
                        }
                        networkNode.setInteractorId(NetworkUtility.createNodeLabel(searchInteraction.getMoleculeA(), searchInteraction.getUniqueIdA(), searchInteraction.getInteractorAAc()));
                        networkNode.setPreferredId(searchInteraction.getIdA());
                        networkNode.setPreferredIdWithDB(searchInteraction.getIdA());
                        networkNode.setInteractorType(searchInteraction.getTypeA());
                        networkNode.setPreferredId(searchInteraction.getUniqueIdA());
                        networkNode.setInteractorName(searchInteraction.getMoleculeA());
                        networkNode.setColor(NetworkUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                        networkNode.setShape(NetworkUtility.getShapeForInteractorType(searchInteraction.getTypeMIA()));
                        networkNode.setClusterId(searchInteraction.getTaxIdA());
                        networkNode.setMutation(searchInteraction.isMutationA());
                        networkNodeGroup.setInteractor(networkNode);

                        interactorAcAndNodeMap.put(searchInteraction.getInteractorAAc(), networkNode);
                        edgesAndNodes.add(networkNodeGroup);
                    } else if (searchInteraction.isMutationA()) {
                        NetworkNode existingNetworkNode = interactorAcAndNodeMap.get(searchInteraction.getInteractorAAc());
                        existingNetworkNode.setMutation(searchInteraction.isMutationA());
                    }
                }

                if (searchInteraction.getInteractorBAc() != null) {
                    if (!interactorAcAndNodeMap.keySet().contains(searchInteraction.getInteractorBAc())) {
                        NetworkNode networkNode = new NetworkNode();
                        NetworkNodeGroup networkNodeGroup = new NetworkNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdB() + "";
                        networkNode.setId(searchInteraction.getInteractorBAc());
                        networkNode.setSpeciesName(searchInteraction.getSpeciesB());
                        networkNode.setTaxId(searchInteraction.getTaxIdB());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesB()));
                            }
                            networkNode.setParent(parentTaxId);
                        }
                        networkNode.setInteractorId(NetworkUtility.createNodeLabel(searchInteraction.getMoleculeB(), searchInteraction.getUniqueIdB(), searchInteraction.getInteractorBAc()));
                        networkNode.setPreferredId(searchInteraction.getIdB());
                        networkNode.setPreferredIdWithDB(searchInteraction.getIdB());
                        networkNode.setInteractorType(searchInteraction.getTypeB());
                        networkNode.setPreferredId(searchInteraction.getUniqueIdB());
                        networkNode.setInteractorName(searchInteraction.getMoleculeB());
                        networkNode.setColor(NetworkUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                        networkNode.setShape(NetworkUtility.getShapeForInteractorType(searchInteraction.getTypeMIB()));
                        networkNode.setClusterId(searchInteraction.getTaxIdB());
                        networkNode.setMutation(searchInteraction.isMutationB());
                        networkNodeGroup.setInteractor(networkNode);
                        interactorAcAndNodeMap.put(searchInteraction.getInteractorBAc(), networkNode);
                        edgesAndNodes.add(networkNodeGroup);
                    } else if (searchInteraction.isMutationB()) {
                        NetworkNode existingNetworkNode = interactorAcAndNodeMap.get(searchInteraction.getInteractorBAc());
                        existingNetworkNode.setMutation(searchInteraction.isMutationB());
                    }
                }
                edgesAndNodes.add(networkEdgeGroup);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getInteractionAc() + "failed to process" +
                        "and therefore this interaction will not be in graph json");
                //TODO... Uncomment following
                //throw e;
            }
            interactionCounter++;
        }
        graphCompoundJson.setCompoundData(edgesAndNodes);
        return graphCompoundJson;
    }

    public NetworkNodeGroup createMetaNode(String parentTaxId, String species) {
        NetworkNode graphCompoundNode = new NetworkNode();
        NetworkNodeGroup graphCompoundNodeGroup = new NetworkNodeGroup();
        graphCompoundNode.setId(parentTaxId);
        graphCompoundNode.setColor(ColourCodes.META_NODE);
        graphCompoundNode.setSpeciesName(species);
        graphCompoundNodeGroup.setInteractor(graphCompoundNode);
        graphCompoundNode.setShape(NodeShape.ELLIPSE);

        return graphCompoundNodeGroup;
    }
}
