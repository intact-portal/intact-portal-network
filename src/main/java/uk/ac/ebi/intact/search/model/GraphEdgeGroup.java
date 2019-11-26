package uk.ac.ebi.intact.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 05/09/19.
 */
public class GraphEdgeGroup {

    @JsonProperty("data")
    private GraphLink interaction;

    @JsonProperty("group")
    private String group = "edges";


    public GraphLink getInteraction() {
        return interaction;
    }

    public void setInteraction(GraphLink interaction) {
        this.interaction = interaction;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
