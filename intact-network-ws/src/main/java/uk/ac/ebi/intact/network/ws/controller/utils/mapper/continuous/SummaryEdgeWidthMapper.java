package uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous;

import uk.ac.ebi.intact.network.ws.controller.model.legend.SummaryEdgeWidthLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.Mapper;

public class SummaryEdgeWidthMapper implements Mapper<Integer, Integer> {
    @Override
    public Integer getStyleOf(Integer key) {
        return 2 + Math.min(key, 25);
    }

    public SummaryEdgeWidthLegend createLegend() {
        SummaryEdgeWidthLegend summaryEdgeWidthLegend = new SummaryEdgeWidthLegend();
        summaryEdgeWidthLegend.setMinWidth(3);
        summaryEdgeWidthLegend.setMaxWidth(28);
        summaryEdgeWidthLegend.setMinValue(1);
        summaryEdgeWidthLegend.setMaxValue(25);
        return summaryEdgeWidthLegend;
    }
}
