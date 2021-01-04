package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NetworkLegend {

    @JsonProperty("node_legend")
    private NetworkNodeLegend nodeLegend = new NetworkNodeLegend();

    @JsonProperty("edge_legend")
    private NetworkEdgeLegend edgeLegend = new NetworkEdgeLegend();

    public NetworkNodeLegend getNodeLegend() {
        return nodeLegend;
    }

    public void setNodeLegend(NetworkNodeLegend nodeLegend) {
        this.nodeLegend = nodeLegend;
    }

    public NetworkEdgeLegend getEdgeLegend() {
        return edgeLegend;
    }

    public void setEdgeLegend(NetworkEdgeLegend edgeLegend) {
        this.edgeLegend = edgeLegend;
    }
}
