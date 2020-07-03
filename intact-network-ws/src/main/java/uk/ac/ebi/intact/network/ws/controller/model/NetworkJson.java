package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

/**
 * Created by anjali on 05/09/19.
 */
public class NetworkJson {

    @JsonValue
    private List<Object> compoundData;

    public NetworkJson(List<Object> compoundData) {
        this.compoundData = compoundData;
    }

    public List<Object> getCompoundData() {
        return compoundData;
    }

    public void setCompoundData(List<Object> compoundData) {
        this.compoundData = compoundData;
    }
}
