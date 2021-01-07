package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.ac.ebi.intact.network.ws.controller.model.serializer.RangeSerializer;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.EdgeShape;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class NetworkEdgeLegend {

    @JsonProperty("summary_color")
    @JsonSerialize(keyUsing = RangeSerializer.class)
    private Map<Range, Color> summaryColors = new LinkedHashMap<>();
    @JsonProperty("summary_width")
    private SummaryEdgeWidthLegend summaryWidth = new SummaryEdgeWidthLegend();
    @JsonProperty("evidence_color")
    private Map<String, Color> evidenceColors = new LinkedHashMap<>();
    @JsonProperty("mutation_color")
    private Map<Boolean, BooleanLegend<Color>> mutationColor = new LinkedHashMap<>();
    @JsonProperty("mutation_width")
    private Map<Boolean, BooleanLegend<Integer>> mutationWidth = new LinkedHashMap<>();
    @JsonProperty
    private Map<Boolean, BooleanLegend<EdgeShape>> expansion = new LinkedHashMap<>();


    public Map<Range, Color> getSummaryColors() {
        return summaryColors;
    }

    public void setSummaryColors(Map<Range, Color> summaryColors) {        this.summaryColors = summaryColors;    }

    public Map<String, Color> getEvidenceColors() {
        return evidenceColors;
    }

    public void setEvidenceColors(Map<String, Color> evidenceColors) {
        this.evidenceColors = evidenceColors;
    }

    public Map<Boolean, BooleanLegend<Color>> getMutationColor() {
        return mutationColor;
    }

    public void setMutationColor(Map<Boolean, BooleanLegend<Color>> mutationColor) {
        this.mutationColor = mutationColor;
    }

    public Map<Boolean, BooleanLegend<Integer>> getMutationWidth() {
        return mutationWidth;
    }

    public void setMutationWidth(Map<Boolean, BooleanLegend<Integer>> mutationWidth) {
        this.mutationWidth = mutationWidth;
    }

    public Map<Boolean, BooleanLegend<EdgeShape>> getExpansion() {
        return expansion;
    }

    public void setExpansion(Map<Boolean, BooleanLegend<EdgeShape>> expansion) {
        this.expansion = expansion;
    }

    public SummaryEdgeWidthLegend getSummaryWidth() {
        return summaryWidth;
    }

    public void setSummaryWidth(SummaryEdgeWidthLegend summaryWidth) {
        this.summaryWidth = summaryWidth;
    }
}
