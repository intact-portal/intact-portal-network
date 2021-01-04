package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkEdgeLegend;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkLegend;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.InteractionExpansionMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.InteractionMutationMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.booleans.InteractorMutationMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.continuous.MIScoreMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.InteractionTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.InteractorTypeMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.TaxonMapper;

import java.util.List;

public class LegendBuilder {
    private final TaxonMapper taxonMapper;
    private final InteractorTypeMapper interactorTypeMapper;
    private final InteractionTypeMapper interactionTypeMapper;
    private final MIScoreMapper miScoreMapper;
    private final InteractionMutationMapper interactionMutationMapper;
    private final InteractorMutationMapper interactorMutationMapper;
    private final InteractionExpansionMapper interactionExpansionMapper;


    public LegendBuilder(TaxonMapper taxonMapper,
                         InteractorTypeMapper interactorTypeMapper,
                         InteractionTypeMapper interactionTypeMapper,
                         MIScoreMapper miScoreMapper,
                         InteractionMutationMapper interactionMutationMapper,
                         InteractorMutationMapper interactorMutationMapper,
                         InteractionExpansionMapper interactionExpansionMapper) {
        this.taxonMapper = taxonMapper;
        this.interactorTypeMapper = interactorTypeMapper;
        this.interactionTypeMapper = interactionTypeMapper;
        this.miScoreMapper = miScoreMapper;
        this.interactionMutationMapper = interactionMutationMapper;
        this.interactorMutationMapper = interactorMutationMapper;
        this.interactionExpansionMapper = interactionExpansionMapper;
    }

    public NetworkLegend createLegend(List<String> taxIds, List<String> nodeTypes, boolean nodeMutated, List<String> edgeTypes, boolean edgeExpanded, boolean edgeAffectedByMutation) {
        NetworkLegend legend = new NetworkLegend();
        setupNodeLegend(legend.getNodeLegend(), taxIds, nodeTypes, nodeMutated);
        setupEdgeLegend(legend.getEdgeLegend(), edgeTypes, edgeExpanded, edgeAffectedByMutation);
        return legend;
    }

    private void setupNodeLegend(NetworkNodeLegend legend, List<String> taxIds, List<String> nodeTypes, boolean nodeMutated) {
        taxonMapper.setupNodeLegend(legend, taxIds);
        legend.setShape(interactorTypeMapper.createLegend(nodeTypes));
        legend.setBorderColors(interactorMutationMapper.createLegend(nodeMutated));
    }

    private void setupEdgeLegend(NetworkEdgeLegend legend, List<String> edgeTypes, boolean edgeExpanded, boolean edgeAffectedByMutation) {
        legend.setSummaryColors(miScoreMapper.createLegend());
        legend.setEvidenceColors(interactionTypeMapper.createLegend(edgeTypes));
        legend.setExpansion(interactionExpansionMapper.createLegend(edgeExpanded));
        legend.setMutation(interactionMutationMapper.createLegend(edgeAffectedByMutation));
    }
}
