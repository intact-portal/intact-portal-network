package uk.ac.ebi.intact.search.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactor.service.InteractorSearchService;
import uk.ac.ebi.intact.search.model.*;
import uk.ac.ebi.intact.search.utils.ColourCodes;
import uk.ac.ebi.intact.search.utils.GraphUtility;
import uk.ac.ebi.intact.search.utils.NodeShape;

import java.util.*;

/**
 * Created by anjali on 16/05/19.
 */
@Service
public class GraphService {

    private static final Log log = LogFactory.getLog(GraphService.class);
    private InteractionSearchService interactionSearchService;
    private InteractorSearchService interactorSearchService;

    @Autowired
    public GraphService(InteractionSearchService interactionSearchService,
                        InteractorSearchService interactorSearchService) {
        this.interactorSearchService = interactorSearchService;
        this.interactionSearchService = interactionSearchService;
    }

    public GraphJson getGraphJson
            (String query,
             Set<String> speciesFilter,
             Set<String> interactorTypeFilter,
             Set<String> detectionMethodFilter,
             Set<String> interactionTypeFilter,
             Set<String> interactionHostOrganism,
             boolean isNegativeFilter,
             double minMiScore,
             double maxMiScore,
             boolean interSpecies,
             boolean isCompound,
             int page,
             int pageSize) {

        /*Page<SearchInteractor> interactors = this.interactorSearchService.findInteractorForGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganism,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);*/

        Page<SearchInteraction> interactions = this.interactionSearchService.findInteractionForGraphJson(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, interactionHostOrganism, isNegativeFilter, minMiScore, maxMiScore, interSpecies, page, pageSize);

        // return toD3FormatAlternative(interactors.getContent(), interactions.getContent());

        return toCytoscapeJsonFormat(interactions.getContent(), isCompound);
    }


    private GraphJson toCytoscapeJsonFormat(List<SearchInteraction> interactions, boolean isCompound) {
        GraphJson graphCompoundJson = new GraphJson();
        List<Object> edgesAndNodes = new ArrayList<>();
        HashMap<String, GraphNode> interactorAcAndNodeMap = new HashMap<String, GraphNode>();
        HashSet<String> specieSet = new HashSet<>();
        Integer interactionCounter = 1;


        for (SearchInteraction searchInteraction : interactions) {
            try {
                GraphEdgeGroup graphEdgeGroup = new GraphEdgeGroup();
                GraphLink graphLink = new GraphLink();
                graphLink.setId(searchInteraction.getBinaryInteractionId());
                graphLink.setSource(searchInteraction.getAcA());
                if (searchInteraction.getAcB() != null) {
                    graphLink.setTarget(searchInteraction.getAcB());
                } else {
                    graphLink.setTarget(graphLink.getSource());
                }
                graphLink.setInteractionAc(searchInteraction.getAc());
                graphLink.setInteractionType(searchInteraction.getType());
                graphLink.setInteractionDetectionMethod(searchInteraction.getDetectionMethod());
                graphLink.setColor(GraphUtility.getColorForInteractionType(searchInteraction.getTypeMIIdentifier()));
                graphLink.setCollapsedColor(GraphUtility.getColorForCollapsedEdge(searchInteraction.getIntactMiscore()));
                graphLink.setShape(GraphUtility.getShapeForExpansionType(searchInteraction.getExpansionMethod()));
                graphLink.setDisruptedByMutation(searchInteraction.isDisruptedByMutation());
                graphLink.setMiScore(searchInteraction.getIntactMiscore());
                graphEdgeGroup.setInteraction(graphLink);

                if (searchInteraction.getAcA() != null) {
                    if (!interactorAcAndNodeMap.keySet().contains(searchInteraction.getAcA())) {
                        GraphNode graphNode = new GraphNode();
                        GraphNodeGroup graphNodeGroup = new GraphNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdA() + "";
                        graphNode.setId(searchInteraction.getAcA());
                        graphNode.setSpeciesName(searchInteraction.getSpeciesA());
                        graphNode.setTaxId(searchInteraction.getTaxIdA());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesA()));
                            }
                            graphNode.setParent(parentTaxId);
                        }
                        graphNode.setInteractorId(GraphUtility.createNodeLabel(searchInteraction.getMoleculeA(), searchInteraction.getUniqueIdA(), searchInteraction.getAcA()));
                        graphNode.setPreferredId(searchInteraction.getIdA());
                        graphNode.setPreferredIdWithDB(searchInteraction.getIdA());
                        graphNode.setInteractorType(searchInteraction.getTypeA());
                        graphNode.setPreferredId(searchInteraction.getUniqueIdA());
                        graphNode.setInteractorName(searchInteraction.getMoleculeA());
                        graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                        graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIA()));
                        graphNode.setClusterId(searchInteraction.getTaxIdA());
                        graphNode.setMutation(searchInteraction.isMutationA());
                        graphNodeGroup.setInteractor(graphNode);

                        interactorAcAndNodeMap.put(searchInteraction.getAcA(), graphNode);
                        edgesAndNodes.add(graphNodeGroup);
                    } else if (searchInteraction.isMutationA()) {
                        GraphNode existingGraphNode = interactorAcAndNodeMap.get(searchInteraction.getAcA());
                        existingGraphNode.setMutation(searchInteraction.isMutationA());
                    }
                }

                if (searchInteraction.getAcB() != null) {
                    if (!interactorAcAndNodeMap.keySet().contains(searchInteraction.getAcB())) {
                        GraphNode graphNode = new GraphNode();
                        GraphNodeGroup graphNodeGroup = new GraphNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdB() + "";
                        graphNode.setId(searchInteraction.getAcB());
                        graphNode.setSpeciesName(searchInteraction.getSpeciesB());
                        graphNode.setTaxId(searchInteraction.getTaxIdB());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesB()));
                            }
                            graphNode.setParent(parentTaxId);
                        }
                        graphNode.setInteractorId(GraphUtility.createNodeLabel(searchInteraction.getMoleculeB(), searchInteraction.getUniqueIdB(), searchInteraction.getAcB()));
                        graphNode.setPreferredId(searchInteraction.getIdB());
                        graphNode.setPreferredIdWithDB(searchInteraction.getIdB());
                        graphNode.setInteractorType(searchInteraction.getTypeB());
                        graphNode.setPreferredId(searchInteraction.getUniqueIdB());
                        graphNode.setInteractorName(searchInteraction.getMoleculeB());
                        graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                        graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIB()));
                        graphNode.setClusterId(searchInteraction.getTaxIdB());
                        graphNode.setMutation(searchInteraction.isMutationB());
                        graphNodeGroup.setInteractor(graphNode);
                        interactorAcAndNodeMap.put(searchInteraction.getAcB(), graphNode);
                        edgesAndNodes.add(graphNodeGroup);
                    } else if (searchInteraction.isMutationB()) {
                        GraphNode existingGraphNode = interactorAcAndNodeMap.get(searchInteraction.getAcB());
                        existingGraphNode.setMutation(searchInteraction.isMutationB());
                    }
                }
                edgesAndNodes.add(graphEdgeGroup);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getAc() + "failed to process" +
                        "and therefore this interaction will not be in graph json");
                //TODO... Uncomment following
                //throw e;
            }
            interactionCounter++;
        }
        graphCompoundJson.setCompoundData(edgesAndNodes);
        return graphCompoundJson;
    }

    public GraphNodeGroup createMetaNode(String parentTaxId, String species) {
        GraphNode graphCompoundNode = new GraphNode();
        GraphNodeGroup graphCompoundNodeGroup = new GraphNodeGroup();
        graphCompoundNode.setId(parentTaxId);
        graphCompoundNode.setColor(ColourCodes.META_NODE);
        graphCompoundNode.setSpeciesName(species);
        graphCompoundNodeGroup.setInteractor(graphCompoundNode);
        graphCompoundNode.setShape(NodeShape.ELLIPSE);

        return graphCompoundNodeGroup;
    }

}
