package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;

import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.AbstractOntologyMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.InteractionType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.InteractorType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;

public class InteractorTypeMapper extends AbstractOntologyMapper<InteractorType, NodeShape> {
    private final MIOntology ontology;

    public InteractorTypeMapper(MIOntology ontology) {
        this.ontology = ontology;
        indexTree();
    }

    @Override
    protected Term getRootOfOntology() {
        return ontology.get().getTerm(InteractorType.getRootId());
    }

    @Override
    protected InteractorType matchArchetype(String id) {
        return InteractorType.getInteractorType(id);
    }

    @Override
    public InteractorType getDefaultArchetype() {
        return InteractorType.PROTEIN;
    }
}
