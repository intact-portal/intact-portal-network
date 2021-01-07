package uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans;


import uk.ac.ebi.intact.network.ws.controller.model.legend.BooleanLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.Mapper;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class BooleanMapper<P> implements Mapper<Boolean, P> {

    public SortedMap<Boolean, BooleanLegend<P>> createLegend(Collection<Boolean> values) {
        return createLegend(values.stream().anyMatch(value -> value));
    }

    public SortedMap<Boolean, BooleanLegend<P>> createLegend(boolean containsTrueFacet) {
        SortedMap<Boolean, BooleanLegend<P>> legend = new TreeMap<>();
        if (containsTrueFacet) {
            BooleanLegend<P> falseLegend = new BooleanLegend<>();
            falseLegend.setLabel(getFalseLabel());
            falseLegend.setValue(getStyleOf(false));
            legend.put(false, falseLegend);

            BooleanLegend<P> trueLegend = new BooleanLegend<>();
            trueLegend.setLabel(getTrueLabel());
            trueLegend.setValue(getStyleOf(true));
            legend.put(true, trueLegend);
        }
        return legend;
    }

    public abstract String getTrueLabel();

    public abstract String getFalseLabel();
}
