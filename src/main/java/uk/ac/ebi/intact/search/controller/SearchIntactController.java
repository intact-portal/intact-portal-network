package uk.ac.ebi.intact.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactor.model.SearchInteractor;
import uk.ac.ebi.intact.search.interactor.service.InteractorSearchService;
import uk.ac.ebi.intact.search.model.GraphJson;
import uk.ac.ebi.intact.search.service.GraphService;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/graph")
public class SearchIntactController {

    private InteractionSearchService interactionSearchService;
    private InteractorSearchService interactorSearchService;
    private GraphService graphService;

    @Autowired
    public SearchIntactController(InteractionSearchService interactionSearchService,
                                  InteractorSearchService interactorSearchServic, GraphService graphService) {
        this.interactorSearchService = interactorSearchService;
        this.interactionSearchService = interactionSearchService;
        this.graphService = graphService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

    @RequestMapping(value = "/findInteractorForGraphJson",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
    public Page<SearchInteractor> findInteractorForGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "speciesFilter", required = false) Set<String> speciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiScore", defaultValue = "0", required = false) double minMiScore,
            @RequestParam(value = "maxMiScore", defaultValue = "1", required = false) double maxMiScore,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "") int pageSize) {

        return this.interactorSearchService.findInteractorForGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);
    }

    @RequestMapping(value = "/findInteractionForGraphJson",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
    public Page<SearchInteraction> findInteractionForGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "hostOrganismFilter", required = false) Set<String> hostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore", defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore", defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "species", required = false) Set<String> species,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return this.interactionSearchService.findInteractionForGraphJson(query, detectionMethodFilter,
                interactionTypeFilter, hostOrganismFilter, isNegativeFilter, minMiscore, maxMiscore, species, interSpecies,
                page, pageSize);
    }

    @RequestMapping(value = "/getGraphJson",
            params = {
                    "query",
            },
            method = RequestMethod.GET)
    public GraphJson getGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "speciesFilter", required = false) Set<String> speciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiScore", defaultValue = "0", required = false) double minMiScore,
            @RequestParam(value = "maxMiScore", defaultValue = "1", required = false) double maxMiScore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "", required = false) int pageSize) {

        return this.graphService.getGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter, minMiScore, maxMiScore, interSpecies, page, pageSize);
    }

}
