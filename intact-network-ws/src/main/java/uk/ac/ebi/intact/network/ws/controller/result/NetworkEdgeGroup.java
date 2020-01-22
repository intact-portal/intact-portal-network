package uk.ac.ebi.intact.network.ws.controller.result;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 05/09/19.
 */
public class NetworkEdgeGroup {

    @JsonProperty("data")
    private NetworkLink interaction;

    @JsonProperty("group")
    private String group = "edges";


    public NetworkLink getInteraction() {
        return interaction;
    }

    public void setInteraction(NetworkLink interaction) {
        this.interaction = interaction;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
