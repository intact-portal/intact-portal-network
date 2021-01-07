package uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans;

public class InteractionMutationWidthMapper extends BooleanMapper<Integer> {

    public static final int AFFECTED_BY_MUTATION_EDGE_WIDTH = 4;
    public static final int NOT_AFFECTED_BY_MUTATION_EDGE_WIDTH = 1;

    @Override
    public Integer getStyleOf(Boolean key) {
        return key ? AFFECTED_BY_MUTATION_EDGE_WIDTH : NOT_AFFECTED_BY_MUTATION_EDGE_WIDTH;
    }

    @Override
    public String getTrueLabel() {
        return "affected by mutation";
    }

    @Override
    public String getFalseLabel() {
        return "not affected by mutation";
    }
}
