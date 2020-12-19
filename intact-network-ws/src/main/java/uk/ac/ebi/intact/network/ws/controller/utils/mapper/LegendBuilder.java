package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkEdgeLegend;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl.InteractionTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl.InteractorTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl.TaxonMapper;

import java.util.Collection;

public class LegendBuilder {
    private final TaxonMapper taxonMapper;
    private final InteractorTypeMapper interactorTypeMapper;
    private final InteractionTypeMapper interactionTypeMapper;

    public LegendBuilder(TaxonMapper taxonMapper, InteractorTypeMapper interactorTypeMapper, InteractionTypeMapper interactionTypeMapper) {
        this.taxonMapper = taxonMapper;
        this.interactorTypeMapper = interactorTypeMapper;
        this.interactionTypeMapper = interactionTypeMapper;
    }

    public NetworkNodeLegend createNodeLegend(Collection<String> taxIds, Collection<String> types) {
        NetworkNodeLegend legend = new NetworkNodeLegend();

        taxonMapper.setupNodeLegend(legend, taxIds);
        legend.setNodeShape(interactorTypeMapper.createLegend(types));

        return legend;
    }

    public NetworkEdgeLegend createEdgeLegend(Collection<String> types) {
        NetworkEdgeLegend legend = new NetworkEdgeLegend();

        legend.setEdgeColor(interactionTypeMapper.createLegend(types));

        return legend;
    }
}
