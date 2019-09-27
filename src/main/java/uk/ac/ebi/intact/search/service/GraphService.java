package uk.ac.ebi.intact.search.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactor.model.SearchInteractor;
import uk.ac.ebi.intact.search.interactor.service.InteractorSearchService;
import uk.ac.ebi.intact.search.model.*;
import uk.ac.ebi.intact.search.utils.ColourCodes;
import uk.ac.ebi.intact.search.utils.GraphUtility;
import uk.ac.ebi.intact.search.utils.NodeShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
             int page,
             int pageSize) {

        /*Page<SearchInteractor> interactors = this.interactorSearchService.findInteractorForGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganism,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);*/

        Page<SearchInteraction> interactions = this.interactionSearchService.findInteractionForGraphJson(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, interactionHostOrganism, isNegativeFilter, minMiScore, maxMiScore, interSpecies, page, pageSize);

        // return toD3FormatAlternative(interactors.getContent(), interactions.getContent());

        return toCytoscapeJsonFormat(interactions.getContent());
    }

    public GraphCompoundJson getGraphCompoundJson
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

        return toCytoscapeCompoundJson(interactions.getContent(), isCompound);
    }

    private GraphJson toD3Format(List<SearchInteractor> interactors, List<SearchInteraction> interactions) {
        GraphJson graphJson = new GraphJson();
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphLink> graphLinks = new ArrayList<>();

        for (SearchInteractor searchInteractor : interactors) {
            GraphNode graphNode = new GraphNode();
            graphNode.setId(searchInteractor.getInteractorId());
            graphNode.setSpeciesName(searchInteractor.getSpecies());
            graphNode.setTaxId(searchInteractor.getTaxId());
            graphNode.setColor("rgb(255,0,0)");
            graphNodes.add(graphNode);
        }

        for (SearchInteraction searchInteraction : interactions) {
            GraphLink graphLink = new GraphLink();
            graphLink.setSource(searchInteraction.getInteractorAAc());
            graphLink.setTarget(searchInteraction.getInteractorBAc());
            graphLinks.add(graphLink);
        }

        graphJson.setInteractions(graphLinks);
        graphJson.setInteractors(graphNodes);

        return graphJson;
    }

    /*
    * Delete this code when not needed
    *
    * */
    private GraphJson toCytoscapeJsonFormat(List<SearchInteraction> interactions) {
        GraphJson graphJson = new GraphJson();
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphLink> graphLinks = new ArrayList<>();
        HashSet<String> interactorSet = new HashSet<>();


        for (SearchInteraction searchInteraction : interactions) {
            try {
                GraphLink graphLink = new GraphLink();
                graphLink.setSource(searchInteraction.getInteractorAAc());
                if (searchInteraction.getInteractorBAc() != null) {
                    graphLink.setTarget(searchInteraction.getInteractorBAc());
                } else {
                    graphLink.setTarget(graphLink.getSource());
                }
                graphLink.setInteractionAc(searchInteraction.getInteractionAc());
                graphLink.setInteractionType(searchInteraction.getInteractionType());
                graphLink.setInteractionDetectionMethod(searchInteraction.getInteractionDetectionMethod());
                graphLink.setColor(GraphUtility.getColorForInteractionType(searchInteraction.getInteractionType()));

                if (!interactorSet.contains(searchInteraction.getInteractorAAc())) {
                    GraphNode graphNode = new GraphNode();
                    graphNode.setId(searchInteraction.getInteractorAAc());
                    graphNode.setSpeciesName(searchInteraction.getSpeciesA());
                    graphNode.setTaxId(searchInteraction.getTaxIdA());
                    graphNode.setInteractorId(searchInteraction.getMoleculeA());
                    graphNode.setInteractorType(searchInteraction.getTypeA());
                    graphNode.setPreferredId(searchInteraction.getUniqueIdA());
                    graphNode.setInteractorName(searchInteraction.getMoleculeA());
                    graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                    graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIA()));
                    graphNode.setClusterId(searchInteraction.getTaxIdA());
                    graphNodes.add(graphNode);
                    interactorSet.add(searchInteraction.getInteractorAAc());
                }

                if (searchInteraction.getInteractorBAc() != null) {
                    if (!interactorSet.contains(searchInteraction.getInteractorBAc())) {
                        GraphNode graphNode = new GraphNode();
                        graphNode.setId(searchInteraction.getInteractorBAc());
                        graphNode.setSpeciesName(searchInteraction.getSpeciesB());
                        graphNode.setTaxId(searchInteraction.getTaxIdB());
                        graphNode.setInteractorId(searchInteraction.getMoleculeB());
                        graphNode.setInteractorType(searchInteraction.getTypeB());
                        graphNode.setPreferredId(searchInteraction.getUniqueIdB());
                        graphNode.setInteractorName(searchInteraction.getMoleculeB());
                        graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                        graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIB()));
                        graphNode.setClusterId(searchInteraction.getTaxIdB());
                        graphNodes.add(graphNode);
                        interactorSet.add(searchInteraction.getInteractorBAc());
                    }
                }

                graphLinks.add(graphLink);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getInteractionAc() + "failed to process" +
                        "and therefore this interaction will not be in graph json");
                //TODO... Uncomment following
                //throw e;
            }
        }


        graphJson.setInteractions(graphLinks);
        graphJson.setInteractors(graphNodes);

        return graphJson;
    }

    private GraphCompoundJson toCytoscapeCompoundJson(List<SearchInteraction> interactions, boolean isCompound) {
        GraphCompoundJson graphCompoundJson = new GraphCompoundJson();
        List<Object> edgesAndNodes = new ArrayList<>();
        HashSet<String> interactorSet = new HashSet<>();
        HashSet<String> specieSet = new HashSet<>();


        for (SearchInteraction searchInteraction : interactions) {
            try {
                GraphEdgeGroup graphEdgeGroup = new GraphEdgeGroup();
                GraphCompoundLink graphLink = new GraphCompoundLink();
                graphLink.setSource(searchInteraction.getInteractorAAc());
                if (searchInteraction.getInteractorBAc() != null) {
                    graphLink.setTarget(searchInteraction.getInteractorBAc());
                } else {
                    graphLink.setTarget(graphLink.getSource());
                }
                graphLink.setInteractionAc(searchInteraction.getInteractionAc());
                graphLink.setInteractionType(searchInteraction.getInteractionType());
                graphLink.setInteractionDetectionMethod(searchInteraction.getInteractionDetectionMethod());
                graphLink.setColor(GraphUtility.getColorForInteractionType(searchInteraction.getInteractionTypeMIIdentifier()));
                graphLink.setCollapsedColor(GraphUtility.getColorForCollapsedEdge(searchInteraction.getIntactMiscore()));
                graphLink.setShape(GraphUtility.getShapeForExpansionType(searchInteraction.getExpansionMethod()));
                graphLink.setDisruptedMutation(searchInteraction.isInteractionDisruptedByMutation());
                graphEdgeGroup.setInteraction(graphLink);

                if (!interactorSet.contains(searchInteraction.getInteractorAAc())) {
                    GraphCompoundNode graphNode = new GraphCompoundNode();
                    GraphNodeGroup graphNodeGroup = new GraphNodeGroup();
                    String parentTaxId = searchInteraction.getTaxIdA() + "";
                    graphNode.setId(searchInteraction.getInteractorAAc());
                    graphNode.setSpeciesName(searchInteraction.getSpeciesA());
                    graphNode.setTaxId(searchInteraction.getTaxIdA());
                    if (isCompound) {
                        if (!specieSet.contains(parentTaxId)) {
                            specieSet.add(parentTaxId);
                            edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesA()));
                        }
                        graphNode.setParent(parentTaxId);
                    }
                    graphNode.setInteractorId(searchInteraction.getMoleculeA());
                    graphNode.setInteractorType(searchInteraction.getTypeA());
                    graphNode.setPreferredId(searchInteraction.getUniqueIdA());
                    graphNode.setInteractorName(searchInteraction.getMoleculeA());
                    graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                    graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIA()));
                    graphNode.setClusterId(searchInteraction.getTaxIdA());
                    graphNode.setDisruptedByMutation(searchInteraction.isDisruptedByMutationA());
                    graphNodeGroup.setInteractor(graphNode);

                    interactorSet.add(searchInteraction.getInteractorAAc());
                    edgesAndNodes.add(graphNodeGroup);
                }

                if (searchInteraction.getInteractorBAc() != null) {
                    if (!interactorSet.contains(searchInteraction.getInteractorBAc())) {
                        GraphCompoundNode graphNode = new GraphCompoundNode();
                        GraphNodeGroup graphNodeGroup = new GraphNodeGroup();
                        String parentTaxId = searchInteraction.getTaxIdB() + "";
                        graphNode.setId(searchInteraction.getInteractorBAc());
                        graphNode.setSpeciesName(searchInteraction.getSpeciesB());
                        graphNode.setTaxId(searchInteraction.getTaxIdB());
                        if (isCompound) {
                            if (!specieSet.contains(parentTaxId)) {
                                specieSet.add(parentTaxId);
                                edgesAndNodes.add(createMetaNode(parentTaxId, searchInteraction.getSpeciesB()));
                            }
                            graphNode.setParent(parentTaxId);
                        }
                        graphNode.setInteractorId(searchInteraction.getMoleculeB());
                        graphNode.setInteractorType(searchInteraction.getTypeB());
                        graphNode.setPreferredId(searchInteraction.getUniqueIdB());
                        graphNode.setInteractorName(searchInteraction.getMoleculeB());
                        graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                        graphNode.setShape(GraphUtility.getShapeForInteractorType(searchInteraction.getTypeMIB()));
                        graphNode.setClusterId(searchInteraction.getTaxIdB());
                        graphNode.setDisruptedByMutation(searchInteraction.isDisruptedByMutationB());
                        graphNodeGroup.setInteractor(graphNode);
                        interactorSet.add(searchInteraction.getInteractorBAc());
                        edgesAndNodes.add(graphNodeGroup);
                    }
                }
                edgesAndNodes.add(graphEdgeGroup);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getInteractionAc() + "failed to process" +
                        "and therefore this interaction will not be in graph json");
                //TODO... Uncomment following
                //throw e;
            }
        }
        graphCompoundJson.setCompoundData(edgesAndNodes);
        return graphCompoundJson;
    }

    public GraphNodeGroup createMetaNode(String parentTaxId, String species) {
        GraphCompoundNode graphCompoundNode = new GraphCompoundNode();
        GraphNodeGroup graphCompoundNodeGroup = new GraphNodeGroup();
        graphCompoundNode.setId(parentTaxId);
        graphCompoundNode.setColor(ColourCodes.META_NODE);
        graphCompoundNode.setSpeciesName(species);
        graphCompoundNodeGroup.setInteractor(graphCompoundNode);
        graphCompoundNode.setShape(NodeShape.ELLIPSE);

        return graphCompoundNodeGroup;
    }

}
