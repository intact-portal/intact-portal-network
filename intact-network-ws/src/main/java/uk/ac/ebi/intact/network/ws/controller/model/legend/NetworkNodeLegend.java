package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class NetworkNodeLegend {
    @JsonProperty
    private Map<String, NodeShape> shape = new LinkedHashMap<>();
    @JsonProperty("species_color")
    private Map<String, Color> speciesColor = new LinkedHashMap<>();
    @JsonProperty("kingdom_color")
    private Map<String, Color> kingdomColor = new LinkedHashMap<>();
    @JsonProperty("border_color")
    private Map<Boolean, BooleanLegend<Color>> borderColor = new LinkedHashMap<>();
    @JsonProperty("border_width")
    private Map<Boolean, BooleanLegend<Integer>> borderWidth= new LinkedHashMap<>();

    public Map<String, NodeShape> getShape() {
        return shape;
    }

    public void setShape(Map<String, NodeShape> shape) {
        this.shape = shape;
    }

    public Map<String, Color> getSpeciesColor() {
        return speciesColor;
    }

    public void setSpeciesColor(Map<String, Color> speciesColor) {
        this.speciesColor = speciesColor;
    }

    public Map<String, Color> getKingdomColor() {
        return kingdomColor;
    }

    public void setKingdomColor(Map<String, Color> kingdomColor) {
        this.kingdomColor = kingdomColor;
    }

    public Map<Boolean, BooleanLegend<Color>> getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Map<Boolean, BooleanLegend<Color>> borderColor) {
        this.borderColor = borderColor;
    }

    public Map<Boolean, BooleanLegend<Integer>> getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(Map<Boolean, BooleanLegend<Integer>> borderWidth) {
        this.borderWidth = borderWidth;
    }
}
