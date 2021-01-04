package uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes;


import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;

public enum InteractorType implements Archetype<NodeShape> {
    BIO_ACTIVE_ENTITY("bioactive entity", "MI_1100", NodeShape.TRIANGLE),
    PROTEIN("protein", "MI_0326", NodeShape.ELLIPSE),
    GENE("gene", "MI_0250", NodeShape.ROUNDED_RECTANGLE),
    NUCLEIC_ACID("nucleic acid", "MI_0318", NodeShape.PARALLELOGRAM),
    DNA("deoxyribonucleic acid", "MI_0319", NodeShape.VEE),
    DNA_S("dna", "", NodeShape.VEE),
    RNA("ribonucleic acid", "MI_0320", NodeShape.DIAMOND),
    RNA_S("rna", "", NodeShape.DIAMOND),
    PEPTIDE("peptide", "MI_0327", NodeShape.ELLIPSE),
    MOLECULE_SET("molecule set", "MI_1304", NodeShape.OCTAGON),
    COMPLEX("complex", "MI_0314", NodeShape.HEXAGON);

    public final String name;
    public final String MI_ID;
    public final NodeShape shape;

    InteractorType(String name, String MI_ID, NodeShape shape) {
        this.name = name;
        this.MI_ID = MI_ID;
        this.shape = shape;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public NodeShape getVisualProperty() {
        return shape;
    }
}
