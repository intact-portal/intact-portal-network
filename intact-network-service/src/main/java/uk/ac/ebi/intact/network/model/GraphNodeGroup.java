package uk.ac.ebi.intact.network.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 05/09/19.
 */
public class GraphNodeGroup {

    @JsonProperty("data")
    private GraphNode interactor;

    @JsonProperty("group")
    private String group = "nodes";


    public GraphNode getInteractor() {
        return interactor;
    }

    public void setInteractor(GraphNode interactor) {
        this.interactor = interactor;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
