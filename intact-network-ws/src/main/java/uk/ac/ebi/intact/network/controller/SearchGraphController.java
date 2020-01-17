package uk.ac.ebi.intact.network.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.network.model.GraphJson;
import uk.ac.ebi.intact.network.service.GraphService;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/network")
public class SearchGraphController {

    private GraphService graphService;

    @Autowired
    public SearchGraphController(GraphService graphService) {
        this.graphService = graphService;
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
    public GraphJson getGraphJson(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "speciesFilter", required = false) Set<String> speciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "hostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiScore", defaultValue = "0", required = false) double minMiScore,
            @RequestParam(value = "maxMiScore", defaultValue = "1", required = false) double maxMiScore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "isCompound", required = false) boolean isCompound,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = Integer.MAX_VALUE + "", required = false) int pageSize) {

        return this.graphService.getGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter, minMiScore, maxMiScore, interSpecies, isCompound, page, pageSize);
    }

}
