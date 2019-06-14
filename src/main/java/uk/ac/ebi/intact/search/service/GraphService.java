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
import uk.ac.ebi.intact.search.model.GraphJson;
import uk.ac.ebi.intact.search.model.GraphLink;
import uk.ac.ebi.intact.search.model.GraphNode;
import uk.ac.ebi.intact.search.utils.ColourCodes;
import uk.ac.ebi.intact.search.utils.GraphUtility;

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

        Page<SearchInteraction> interactions = this.interactionSearchService.findInteractionForGraphJson(query, detectionMethodFilter,
                interactionTypeFilter, interactionHostOrganism, isNegativeFilter, minMiScore, maxMiScore, speciesFilter, interSpecies,
                page, pageSize);

        // return toD3FormatAlternative(interactors.getContent(), interactions.getContent());

        return toD3FormatAlternative(interactions.getContent());
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
    private GraphJson toD3FormatAlternative(List<SearchInteraction> interactions) {
        GraphJson graphJson = new GraphJson();
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphLink> graphLinks = new ArrayList<>();
        HashSet<String> interactorSet = new HashSet<>();


        for (SearchInteraction searchInteraction : interactions) {
            try {
                GraphLink graphLink = new GraphLink();
                graphLink.setSource(searchInteraction.getInteractorAAc());
                graphLink.setTarget(searchInteraction.getInteractorBAc());
                graphLink.setInteractionAc(searchInteraction.getInteractionAc());
                graphLink.setInteractionType(searchInteraction.getInteractionType());
                graphLink.setInteractionDetectionMethod(searchInteraction.getInteractionDetectionMethod());

                if (!interactorSet.contains(searchInteraction.getInteractorAAc())) {
                    GraphNode graphNode = new GraphNode();
                    graphNode.setId(searchInteraction.getInteractorAAc());
                    graphNode.setSpeciesName(searchInteraction.getSpeciesA());
                    graphNode.setTaxId(searchInteraction.getTaxIdA());
                    graphNode.setInteractorId(searchInteraction.getIdA());
                    graphNode.setInteractorType(searchInteraction.getTypeA());
                    graphNode.setPreferredId(searchInteraction.getUniqueIdA());
                    graphNode.setInteractorName(searchInteraction.getMoleculeA());
                    graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                    graphNodes.add(graphNode);
                    interactorSet.add(searchInteraction.getInteractorAAc());
                }

                if (!interactorSet.contains(searchInteraction.getInteractorBAc())) {
                    GraphNode graphNode = new GraphNode();
                    graphNode.setId(searchInteraction.getInteractorBAc());
                    graphNode.setSpeciesName(searchInteraction.getSpeciesB());
                    graphNode.setTaxId(searchInteraction.getTaxIdB());
                    graphNode.setInteractorId(searchInteraction.getIdB());
                    graphNode.setInteractorType(searchInteraction.getTypeB());
                    graphNode.setPreferredId(searchInteraction.getUniqueIdB());
                    graphNode.setInteractorName(searchInteraction.getMoleculeB());
                    graphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                    graphNodes.add(graphNode);
                    interactorSet.add(searchInteraction.getInteractorBAc());
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

}
