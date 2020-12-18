package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NetworkLegend {

    @JsonProperty("node_legend")
    private NetworkNodeLegend nodeLegend;

    @JsonProperty("edge_legend")
    private NetworkEdgeLegend edgeLegend;
}
