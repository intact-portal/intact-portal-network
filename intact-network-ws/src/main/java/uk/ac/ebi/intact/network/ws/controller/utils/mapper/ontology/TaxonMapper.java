package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;



import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.Taxon;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;

public class TaxonMapper extends AbstractOntologyMapper<String, Taxon, Color> {

    @Override
    protected TreeNode<String> getRootOfOntology() {
        //TODO Download from OLS
        try {
            URL taxonomyUrl = new URL("https://www.ebi.ac.uk/ols/ontologies/ncbitaxon/download");
            OWLOntologyManager m = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = m.loadOntologyFromOntologyDocument(taxonomyUrl.openStream());
            OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singletonList(ontology));
            OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
                @Override
                public void visit(OWLObjectSomeValuesFrom desc) {
                    System.out.println(desc);
                    System.out.println(" " + getCurrentAxiom());
                }
            };
// Have the walker walk...
            walker.walkStructure(visitor);
        } catch (IOException | OWLOntologyCreationException ignored) {

        }
        return null;
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
