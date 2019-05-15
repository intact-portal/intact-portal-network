package uk.ac.ebi.intact.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.intact.search.interactor.controller.SearchInteractorResult;
import uk.ac.ebi.intact.search.interactor.model.SearchInteractor;
import uk.ac.ebi.intact.search.interactor.service.InteractorIndexService;
import uk.ac.ebi.intact.search.interactor.service.InteractorSearchService;


import java.util.*;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/intact")
public class SearchIntactController {

    private InteractorIndexService interactorIndexService;
    private InteractorSearchService interactorSearchService;

    @Autowired
    public SearchIntactController(InteractorIndexService interactorIndexService,
                                  InteractorSearchService interactorSearchService) {
        this.interactorIndexService = interactorIndexService;
        this.interactorSearchService = interactorSearchService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

    @RequestMapping("/getAll")
    public List<SearchInteractor> getAllDocs() {
        List<SearchInteractor> documents = new ArrayList<>();
        // iterate all documents and add it to list
        for (SearchInteractor doc : this.interactorSearchService.findAll()) {
            documents.add(doc);
        }
        return documents;
    }

    @RequestMapping(value = "/findInteractorWithFields",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
    public Page<SearchInteractor> findInteractorWithFields(
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
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return this.interactorSearchService.findInteractorWithFields(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);
    }
}
