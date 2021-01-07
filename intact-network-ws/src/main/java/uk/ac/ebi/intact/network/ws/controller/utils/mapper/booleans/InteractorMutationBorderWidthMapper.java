package uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans;

public class InteractorMutationBorderWidthMapper extends BooleanMapper<Integer> {
    public static final int MUTATED_BORDER_WIDTH = 4;
    public static final int NOT_MUTATED_BORDER_WIDTH = 0;

    @Override
    public Integer getStyleOf(Boolean key) {
        return key ? MUTATED_BORDER_WIDTH : NOT_MUTATED_BORDER_WIDTH;
    }

    @Override
    public String getTrueLabel() {
        return "mutated";
    }

    @Override
    public String getFalseLabel() {
        return "not mutated";
    }
}
