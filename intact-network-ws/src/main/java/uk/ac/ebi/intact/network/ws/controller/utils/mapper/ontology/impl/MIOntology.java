package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.OBOParser;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Ontology;

import java.io.IOException;

public class MIOntology {
    private Ontology ontology;
    public MIOntology() {
        try {
            ontology = OBOParser.parseOntology(MIOntology.class.getResource("/mi.obo").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Ontology get() {
        return ontology;
    }
}
