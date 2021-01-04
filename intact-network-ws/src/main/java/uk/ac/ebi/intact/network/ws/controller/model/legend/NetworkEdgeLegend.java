package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.ac.ebi.intact.network.ws.controller.model.serializer.RangeSerializer;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.EdgeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.BooleanLegend;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class NetworkEdgeLegend {

    @JsonProperty("summary_colors")
    @JsonSerialize(keyUsing = RangeSerializer.class)
    private SortedMap<Range, Color> summaryColors = new TreeMap<>();
    @JsonProperty("evidence_colors")
    private SortedMap<String, Color> evidenceColors = new TreeMap<>();
    @JsonProperty
    private SortedMap<Boolean, BooleanLegend<Color>> mutation = new TreeMap<>();
    @JsonProperty
    private SortedMap<Boolean, BooleanLegend<EdgeShape>> expansion = new TreeMap<>();

    public SortedMap<String, Color> getEvidenceColors() {
        return evidenceColors;
    }

    public void setEvidenceColors(SortedMap<String, Color> evidenceColors) {
        this.evidenceColors = evidenceColors;
    }

    public SortedMap<Range, Color> getSummaryColors() {
        return summaryColors;
    }

    public void setSummaryColors(SortedMap<Range, Color> summaryColors) {
        this.summaryColors = summaryColors;
    }

    public SortedMap<Boolean, BooleanLegend<Color>> getMutation() {
        return mutation;
    }

    public void setMutation(SortedMap<Boolean, BooleanLegend<Color>> mutation) {
        this.mutation = mutation;
    }

    public SortedMap<Boolean, BooleanLegend<EdgeShape>> getExpansion() {
        return expansion;
    }

    public void setExpansion(SortedMap<Boolean, BooleanLegend<EdgeShape>> expansion) {
        this.expansion = expansion;
    }
}
