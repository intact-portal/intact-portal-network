package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;

import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.InteractorType;

public class InteractorTypeMapper extends AbstractOntologyMapper<String, InteractorType, NodeShape> {
    @Override
    protected TreeNode<String> getRootOfOntology() {
        return null; //TODO Download from OLS
    }

    @Override
    protected InteractorType matchArchetype(String MIId) {
        for (InteractorType type: InteractorType.values()) {
            if (type.MI_ID.equals(MIId)) return type;
        }
        return null;
    }
}
