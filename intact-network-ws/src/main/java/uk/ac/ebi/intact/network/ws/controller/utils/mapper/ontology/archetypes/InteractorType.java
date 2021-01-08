package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes;


import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum InteractorType implements Archetype<NodeShape> {
    BIO_ACTIVE_ENTITY("bioactive entity", "MI:1100", NodeShape.TRIANGLE),
    PROTEIN("protein", "MI:0326", NodeShape.ELLIPSE),
    GENE("gene", "MI:0250", NodeShape.ROUNDED_RECTANGLE),
    NUCLEIC_ACID("nucleic acid", "MI:0318", NodeShape.PARALLELOGRAM),
    DNA("deoxyribonucleic acid", "MI:0319", NodeShape.VEE),
    DNA_S("dna", "", NodeShape.VEE),
    RNA("ribonucleic acid", "MI:0320", NodeShape.DIAMOND),
    RNA_S("rna", "", NodeShape.DIAMOND),
    PEPTIDE("peptide", "MI:0327", NodeShape.ELLIPSE),
    MOLECULE_SET("molecule set", "MI:1304", NodeShape.OCTAGON),
    COMPLEX("complex", "MI:0314", NodeShape.HEXAGON);

    public final String name;
    public final String id;
    public final NodeShape shape;

    final static Map<String, InteractorType> interactorTypes = Arrays.stream(InteractorType.values()).filter(type -> !type.id.isEmpty()).collect(Collectors.toMap(type -> type.id, type -> type));
    InteractorType(String name, String id, NodeShape shape) {
        this.name = name;
        this.id = id;
        this.shape = shape;
    }

    public static InteractorType getInteractorType(String id) {
        return interactorTypes.get(id);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeShape getVisualProperty() {
        return shape;
    }


    public static String getRootId() {
        return "MI:0313";
    }
}
