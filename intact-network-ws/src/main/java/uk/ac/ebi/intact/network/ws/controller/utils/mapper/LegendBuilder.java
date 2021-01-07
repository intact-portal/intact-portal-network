package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkEdgeLegend;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkLegend;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.*;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous.MIScoreMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous.SummaryEdgeWidthMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.InteractionTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.InteractorTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.TaxonMapper;

import java.util.Collection;

public class LegendBuilder {
    private final TaxonMapper taxonMapper;
    private final InteractorTypeMapper interactorTypeMapper;
    private final InteractionTypeMapper interactionTypeMapper;

    private final MIScoreMapper miScoreMapper;
    private final InteractionMutationColorMapper interactionMutationColorMapper;
    private final InteractionMutationWidthMapper interactionMutationWidthMapper;
    private final InteractorMutationMapper interactorMutationMapper;
    private final InteractorMutationBorderWidthMapper interactorMutationBorderWidthMapper;
    private final InteractionExpansionMapper interactionExpansionMapper;
    private final SummaryEdgeWidthMapper summaryEdgeWidthMapper;

    public LegendBuilder(TaxonMapper taxonMapper,
                         InteractorTypeMapper interactorTypeMapper,
                         InteractionTypeMapper interactionTypeMapper,
                         MIScoreMapper miScoreMapper,
                         InteractionMutationColorMapper interactionMutationColorMapper,
                         InteractionMutationWidthMapper interactionMutationWidthMapper, InteractorMutationMapper interactorMutationMapper,
                         InteractorMutationBorderWidthMapper interactorMutationBorderWidthMapper, InteractionExpansionMapper interactionExpansionMapper, SummaryEdgeWidthMapper summaryEdgeWidthMapper) {
        this.taxonMapper = taxonMapper;
        this.interactorTypeMapper = interactorTypeMapper;
        this.interactionTypeMapper = interactionTypeMapper;
        this.miScoreMapper = miScoreMapper;
        this.interactionMutationColorMapper = interactionMutationColorMapper;
        this.interactionMutationWidthMapper = interactionMutationWidthMapper;
        this.interactorMutationMapper = interactorMutationMapper;
        this.interactorMutationBorderWidthMapper = interactorMutationBorderWidthMapper;
        this.interactionExpansionMapper = interactionExpansionMapper;
        this.summaryEdgeWidthMapper = summaryEdgeWidthMapper;
    }

    public NetworkLegend createLegend(Collection<String> taxIds, Collection<String> nodeTypes, boolean nodeMutated, Collection<String> edgeTypes, boolean edgeExpanded, boolean edgeAffectedByMutation) {
        NetworkLegend legend = new NetworkLegend();
        setupNodeLegend(legend.getNodeLegend(), taxIds, nodeTypes, nodeMutated);
        setupEdgeLegend(legend.getEdgeLegend(), edgeTypes, edgeExpanded, edgeAffectedByMutation);
        return legend;
    }

    private void setupNodeLegend(NetworkNodeLegend legend, Collection<String> taxIds, Collection<String> nodeTypes, boolean nodeMutated) {
        taxonMapper.setupNodeLegend(legend, taxIds);
        legend.setShape(interactorTypeMapper.createLegend(nodeTypes));
        legend.setBorderColor(interactorMutationMapper.createLegend(nodeMutated));
        legend.setBorderWidth(interactorMutationBorderWidthMapper.createLegend(nodeMutated));
    }

    private void setupEdgeLegend(NetworkEdgeLegend legend, Collection<String> edgeTypes, boolean edgeExpanded, boolean edgeAffectedByMutation) {
        legend.setSummaryColors(miScoreMapper.createLegend());
        legend.setEvidenceColors(interactionTypeMapper.createLegend(edgeTypes));
        legend.setExpansion(interactionExpansionMapper.createLegend(edgeExpanded));
        legend.setMutationColor(interactionMutationColorMapper.createLegend(edgeAffectedByMutation));
        legend.setMutationWidth(interactionMutationWidthMapper.createLegend(edgeAffectedByMutation));
        legend.setSummaryWidth(summaryEdgeWidthMapper.createLegend());
    }
}
