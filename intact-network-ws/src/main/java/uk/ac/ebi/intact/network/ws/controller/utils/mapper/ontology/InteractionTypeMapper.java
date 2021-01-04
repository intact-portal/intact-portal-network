package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.InteractionType;

import java.awt.*;

public class InteractionTypeMapper extends AbstractOntologyMapper<String, InteractionType, Color> {
    @Override
    protected TreeNode<String> getRootOfOntology() {
        return null; //TODO Download from OLS
    }

    @Override
    protected InteractionType matchArchetype(String MIId) {
        for (InteractionType type: InteractionType.values()) {
            if (type.MI_ID.equals(MIId)) return type;
        }
        return null;
    }
}
