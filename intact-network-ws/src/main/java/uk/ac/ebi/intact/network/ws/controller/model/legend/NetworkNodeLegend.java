package uk.ac.ebi.intact.network.ws.controller.model.legend;

import uk.ac.ebi.intact.network.ws.controller.utils.NodeShape;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class NetworkNodeLegend {

    private Map<String, Color> nodeSpeciesColor = new TreeMap<>();
    private Map<String, Color> nodeKingdomsColor = new TreeMap<>();
    private Map<String, NodeShape> nodeShape = new TreeMap<>();

    public Map<String, Color> getNodeSpeciesColor() {
        return nodeSpeciesColor;
    }

    public void setNodeSpeciesColor(Map<String, Color> nodeSpeciesColor) {
        this.nodeSpeciesColor = nodeSpeciesColor;
    }

    public Map<String, Color> getNodeKingdomsColor() {
        return nodeKingdomsColor;
    }

    public void setNodeKingdomsColor(Map<String, Color> nodeKingdomsColor) {
        this.nodeKingdomsColor = nodeKingdomsColor;
    }

    public Map<String, NodeShape> getNodeShape() {
        return nodeShape;
    }

    public void setNodeShape(Map<String, NodeShape> nodeShape) {
        this.nodeShape = nodeShape;
    }
}
