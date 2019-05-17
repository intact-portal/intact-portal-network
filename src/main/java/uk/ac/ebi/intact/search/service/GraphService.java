package uk.ac.ebi.intact.search.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by anjali on 16/05/19.
 */
@Service
public class GraphService {

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

        Page<SearchInteractor> interactors = this.interactorSearchService.findInteractorForGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganism,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);

        Page<SearchInteraction> interactions = this.interactionSearchService.findInteractionForGraphJson(query, detectionMethodFilter,
                interactionTypeFilter, interactionHostOrganism, isNegativeFilter, minMiScore, maxMiScore, speciesFilter, interSpecies,
                page, pageSize);

        return toD3Format(interactors.getContent(), interactions.getContent());
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

}
