package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BooleanLegend<P> {
    @JsonProperty
    private String label;

    @JsonProperty
    private P value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public P getValue() {
        return value;
    }

    public void setValue(P value) {
        this.value = value;
    }
}
