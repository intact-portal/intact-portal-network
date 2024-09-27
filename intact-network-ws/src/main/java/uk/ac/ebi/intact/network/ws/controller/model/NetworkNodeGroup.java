package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 05/09/19.
 */
public class NetworkNodeGroup {

    @JsonProperty("data")
    private NetworkNode interactor;

    @JsonProperty("group")
    private String group = "nodes";


    public NetworkNode getInteractor() {
        return interactor;
    }

    public void setInteractor(NetworkNode interactor) {
        this.interactor = interactor;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
