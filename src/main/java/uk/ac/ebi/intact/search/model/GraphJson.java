package uk.ac.ebi.intact.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by anjali on 17/05/19.
 */
public class GraphJson {

    @JsonProperty("edges")
    private List<GraphLink> interactions;

    @JsonProperty("nodes")
    private List<GraphNode> interactors;


    public List<GraphLink> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<GraphLink> interactions) {
        this.interactions = interactions;
    }

    public List<GraphNode> getInteractors() {
        return interactors;
    }

    public void setInteractors(List<GraphNode> interactors) {
        this.interactors = interactors;
    }
}
