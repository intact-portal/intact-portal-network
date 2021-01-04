package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;



import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.AbstractOntologyMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Taxon;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.OBOParser;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Ontology;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl.MIOntology;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.SortedMap;

public class TaxonMapper extends AbstractOntologyMapper<Taxon, Color> {
    private Ontology ontology;

    public TaxonMapper() {
        try {
            ontology = OBOParser.parseOntology(MIOntology.class.getResource("/ncbitaxon.obo").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Term getRootOfOntology() {
        return ontology.getRoot();
    }

    @Override
    protected Taxon matchArchetype(String key) {
        return Taxon.getTaxon(key);
    }


    @Override
    @Deprecated
    public SortedMap<String, Color> createLegend(Collection<String> facets) {
        return super.createLegend(facets);
    }

    public NetworkNodeLegend setupNodeLegend(NetworkNodeLegend legend, Collection<String> taxIds) {
        taxIds.stream()
                .map(this::getArchetype)
                .distinct()
                .forEach(taxon -> {
                    if (taxon.isSpecies) legend.getSpeciesColors().put(taxon.getName(), taxon.getVisualProperty());
                    else legend.getKingdomsColors().put(taxon.getName(), taxon.getVisualProperty());
                });
        return legend;
    }
}
