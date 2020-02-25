package uk.ac.ebi.intact.network.ws.controller.utils;

import java.awt.*;

/**
 * Created by anjali on 23/09/19.
 */
public class EdgeColor {

    public static final Color COLLAPSED_EDGE_COLOR_START = new Color(251, 231, 35);
    public static final Color COLLAPSED_EDGE_COLOR_END = new Color(68, 2, 86);

    public static final String[] YELLOW_ORANGE_BROWN_PALETTE  = new String[]{
            "rgb(255,255,229)",
            "rgb(255,247,188)",
            "rgb(254,227,145)",
            "rgb(254,196,79)",
            "rgb(254,153,41)",
            "rgb(236,112,20)",
            "rgb(204,76,2)",
            "rgb(153,52,4)",
            "rgb(102,19,5)",
            "rgb(54, 19, 3)"};

    public static final String ASSOCIATION = "rgb(153,153,255)";
    public static final String PHYSICAL_ASSOCIATION = "rgb(153,204,0)";
    public static final String DIRECT_INTERACTION = "rgb(255,165,0)";
    public static final String COLOCALIZATION = "rgb(255,222,62)";
    public static final String PHOSPHORYLATION_REACTION = "rgb(153,0,0)";
    public static final String DEPHOSPHORYLATION_REACTION = "rgb(153,153,0)";
    public static final String OTHERS = "rgb(153,153,153)";

}
