package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;


import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.AbstractOntologyMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.InteractionType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;

import java.awt.*;

public class InteractionTypeMapper extends AbstractOntologyMapper<InteractionType, Color> {
    private final MIOntology ontology;

    public InteractionTypeMapper(MIOntology ontology) {
        this.ontology = ontology;
        indexTree();
    }

    @Override
    protected Term getRootOfOntology() {
        return ontology.get().getTerm(InteractionType.getRootId());
    }

    @Override
    protected InteractionType matchArchetype(String MIId) {
        for (InteractionType type: InteractionType.values()) {
            if (type.id.equals(MIId)) return type;
        }
        return null;
    }

    @Override
    public InteractionType getDefaultArchetype() {
        return InteractionType.ASSOCIATION;
    }
}
