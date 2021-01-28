package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.intact.style.model.legend.NetworkLegend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anjali on 05/09/19.
 */
public class NetworkJson {

    @JsonProperty
    private List<Object> data = new ArrayList<>();

    @JsonProperty
    private NetworkLegend legend = new NetworkLegend();

    public NetworkJson() {
    }

    public NetworkJson(List<Object> data, NetworkLegend legend) {
        this.data = data;
        this.legend = legend;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public NetworkLegend getLegend() {
        return legend;
    }

    public void setLegend(NetworkLegend legend) {
        this.legend = legend;
    }
}
