package uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl;

import uk.ac.ebi.intact.network.ws.controller.utils.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.AbstractMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.TreeNode;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.InteractorType;

public class InteractorTypeMapper extends AbstractMapper<String, InteractorType, NodeShape> {
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
