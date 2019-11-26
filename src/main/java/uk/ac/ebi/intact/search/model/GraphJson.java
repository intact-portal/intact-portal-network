package uk.ac.ebi.intact.search.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

/**
 * Created by anjali on 05/09/19.
 */
public class GraphJson {

    @JsonValue
    private List<Object> compoundData;


    public List<Object> getCompoundData() {
        return compoundData;
    }

    public void setCompoundData(List<Object> compoundData) {
        this.compoundData = compoundData;
    }
}
