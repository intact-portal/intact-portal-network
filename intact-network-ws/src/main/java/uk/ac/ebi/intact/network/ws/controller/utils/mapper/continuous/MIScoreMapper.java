package uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous;

import uk.ac.ebi.intact.network.ws.controller.model.legend.Range;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.Mapper;

import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class MIScoreMapper implements Mapper<Double, Color> {
    TreeMap<Range, Color> miScoreColors = new TreeMap<>();

    public static final Color[] colors = {
            new Color(255, 255, 221),
            new Color(255, 248, 171),
            new Color(254, 224, 121),
            new Color(254, 186, 51),
            new Color(254, 135, 13),
            new Color(231, 90, 0),
            new Color(193, 55, 0),
            new Color(135, 36, 0),
            new Color(83, 26, 0),
            new Color(41, 15, 2),
    };

    @Override
    public Color getStyleOf(Double key) {
        return colors[(int) Math.floor(key * 10)];
    }

    public SortedMap<Range, Color> createLegend() {
        SortedMap<Range, Color> legend = new TreeMap<>();
        for (int i = 0; i < colors.length; i++) {
            double start = i / 10.0;
            legend.put(new Range(start, start + 0.1), colors[i]);
        }
        return legend;
    }
}
