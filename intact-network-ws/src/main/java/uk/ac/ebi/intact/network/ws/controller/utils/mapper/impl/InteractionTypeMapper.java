package uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.AbstractMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.TreeNode;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.InteractionType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.InteractorType;

import java.awt.*;

public class InteractionTypeMapper extends AbstractMapper<String, InteractionType, Color> {
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

    @Override
    public Color extractProperty(InteractionType archetype) {
        return archetype.defaultColor;
    }
}
