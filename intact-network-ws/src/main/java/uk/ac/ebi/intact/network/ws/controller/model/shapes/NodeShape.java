package uk.ac.ebi.intact.network.ws.controller.model.shapes;

import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public final String title;

    NodeShape(String title) {
        this.title = title;
    }
}
