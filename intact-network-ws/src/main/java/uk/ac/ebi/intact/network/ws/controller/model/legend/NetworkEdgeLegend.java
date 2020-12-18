package uk.ac.ebi.intact.network.ws.controller.model.legend;

import java.util.Map;
import java.util.TreeMap;

public class NetworkEdgeLegend {

    private Map<String, String> edgeColor = new TreeMap<>();
    private Map<String, String> edgeShape = new TreeMap<>();

    public Map<String, String> getEdgeColor() {
        return edgeColor;
    }

    public void setEdgeColor(Map<String, String> edgeColor) {
        this.edgeColor = edgeColor;
    }

    public Map<String, String> getEdgeShape() {
        return edgeShape;
    }

    public void setEdgeShape(Map<String, String> edgeShape) {
        this.edgeShape = edgeShape;
    }
}
