package uk.ac.ebi.intact.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 17/05/19.
 */
public class GraphLink {

    @JsonProperty("source")
    private String source;

    @JsonProperty("target")
    private String target;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
