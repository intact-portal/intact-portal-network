package uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.PropertySerializer;

import java.awt.*;

public class InteractorMutationMapper extends BooleanMapper<Color> {

    public static final Color MUTATION_COLOR = new Color(255, 0, 161);

    @Override
    public Color getStyleOf(Boolean key) {
        return key ? MUTATION_COLOR : Color.black;
    }

    @Override
    public String getTrueLabel() {
        return "Mutated protein";
    }

    @Override
    public String getFalseLabel() {
        return "Wild type protein";
    }
}
