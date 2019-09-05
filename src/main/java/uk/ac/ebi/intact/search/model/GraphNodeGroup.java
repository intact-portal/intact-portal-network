package uk.ac.ebi.intact.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 05/09/19.
 */
public class GraphNodeGroup {

    @JsonProperty("data")
    private GraphCompoundNode interactor;

    @JsonProperty("group")
    private String group = "nodes";


    public GraphCompoundNode getInteractor() {
        return interactor;
    }

    public void setInteractor(GraphCompoundNode interactor) {
        this.interactor = interactor;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
