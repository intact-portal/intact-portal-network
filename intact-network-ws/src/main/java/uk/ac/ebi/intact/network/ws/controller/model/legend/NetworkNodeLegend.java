package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.BooleanLegend;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class NetworkNodeLegend {
    @JsonProperty
    private SortedMap<String, NodeShape> shape = new TreeMap<>();
    @JsonProperty("species_colors")
    private SortedMap<String, Color> speciesColors = new TreeMap<>();
    @JsonProperty("kingdoms_colors")
    private SortedMap<String, Color> kingdomsColors = new TreeMap<>();
    @JsonProperty("border_colors")
    private SortedMap<Boolean, BooleanLegend<Color>> borderColors = new TreeMap<>();


    public SortedMap<String, Color> getSpeciesColors() {
        return speciesColors;
    }

    public void setSpeciesColors(SortedMap<String, Color> speciesColors) {
        this.speciesColors = speciesColors;
    }

    public SortedMap<String, Color> getKingdomsColors() {
        return kingdomsColors;
    }

    public void setKingdomsColors(SortedMap<String, Color> kingdomsColors) {
        this.kingdomsColors = kingdomsColors;
    }

    public SortedMap<String, NodeShape> getShape() {
        return shape;
    }

    public void setShape(SortedMap<String, NodeShape> shape) {
        this.shape = shape;
    }

    public SortedMap<Boolean, BooleanLegend<Color>> getBorderColors() {
        return borderColors;
    }

    public void setBorderColors(SortedMap<Boolean, BooleanLegend<Color>> borderColors) {
        this.borderColors = borderColors;
    }
}
