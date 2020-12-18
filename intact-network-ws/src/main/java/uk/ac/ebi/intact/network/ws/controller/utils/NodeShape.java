package uk.ac.ebi.intact.network.ws.controller.utils;

/**
 * Created by anjali on 12/09/19.
 */
public enum NodeShape {
    TRIANGLE("triangle"), // Bioactive Entity
    DIAMOND("diamond"), // RNA
    ROUNDED_RECTANGLE("round-rectangle"), // Gene
    TAG("tag"),
    VEE("vee"), // DNA
    ELLIPSE("ellipse"), // Protein,
    PARALLELOGRAM("rhomboid"),
    OCTAGON("octagon"),
    HEXAGON("hexagon");

    public final String title;

    NodeShape(String title) {
        this.title = title;
    }
}
