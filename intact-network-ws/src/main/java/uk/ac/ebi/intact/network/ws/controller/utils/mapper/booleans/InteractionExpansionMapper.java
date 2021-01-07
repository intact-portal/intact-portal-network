package uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans;

import uk.ac.ebi.intact.network.ws.controller.model.shapes.EdgeShape;

public class InteractionExpansionMapper extends BooleanMapper<EdgeShape> {
    @Override
    public EdgeShape getStyleOf(Boolean key) {
        return key ? EdgeShape.DASHED_LINE : EdgeShape.SOLID_LINE;
    }

    @Override
    public String getTrueLabel() {
        return "spoke expansion";
    }

    @Override
    public String getFalseLabel() {
        return "no expansion";
    }
}
