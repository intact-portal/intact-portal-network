package uk.ac.ebi.intact.network.ws.controller.utils.mapper.impl;


import com.sun.tools.javac.util.List;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.AbstractMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.TreeNode;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.Taxon;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class TaxonMapper extends AbstractMapper<String, Taxon, Color> {

    @Override
    protected TreeNode<String> getRootOfOntology() {
        //TODO Download from OLS
        try {
            URL taxonomyUrl = new URL("https://www.ebi.ac.uk/ols/ontologies/ncbitaxon/download");
            OWLOntologyManager m = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = m.loadOntologyFromOntologyDocument(taxonomyUrl.openStream());
            OWLOntologyWalker walker = new OWLOntologyWalker(List.of(ontology));
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
    protected Map<String, Color> createLegend(Collection<String> facets) {
        return super.createLegend(facets);
    }

    public void setupNodeLegend(NetworkNodeLegend legend, Collection<String> taxIds) {
        taxIds.stream()
                .map(this::getArchetype)
                .distinct()
                .forEach(taxon -> {
                    if (taxon.isSpecies) legend.getNodeSpeciesColor().put(taxon.descriptor, taxon.defaultColor);
                    else legend.getNodeKingdomsColor().put(taxon.descriptor, taxon.defaultColor);
                });
    }

}
