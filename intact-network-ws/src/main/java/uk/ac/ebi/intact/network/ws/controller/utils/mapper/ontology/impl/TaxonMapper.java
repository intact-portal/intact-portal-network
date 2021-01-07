package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.network.ws.controller.model.legend.NetworkNodeLegend;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.AbstractOntologyMapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Taxon;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.OBOParser;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Ontology;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Map;

public class TaxonMapper extends AbstractOntologyMapper<Taxon, Color> {
    private static final Log log = LogFactory.getLog(TaxonMapper.class);
    private Ontology ontology;

    public TaxonMapper() {
       setupOntology();
       indexTree();
    }

    private void setupOntology() {
        try {
            InputStream inputStream = TaxonMapper.class.getResource("/ncbitaxon.obo").openStream();
            log.info("Start indexing of ncbitaxon.obo");
            ontology = OBOParser.parseOntology(inputStream, "NCBITaxon:");
            log.info("Finished indexing of ncbitaxon.obo");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            try {
                URL url = new URL("https://github.com/obophenotype/ncbitaxon/releases/download/current/ncbitaxon.obo");
                log.info("Start downloading of ncbitaxon.obo");
                Files.copy(url.openStream(), Paths.get(MIOntology.class.getResource("/").getPath(),"ncbitaxon.obo"), StandardCopyOption.REPLACE_EXISTING);
                log.info("Finished downloading of ncbitaxon.obo");
                setupOntology();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
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
    public Taxon getDefaultArchetype() {
        return Taxon.CHEMICAL_SYNTHESIS;
    }

    @Override
    @Deprecated
    public Map<String, Color> createLegend(Collection<String> facets) {
        return super.createLegend(facets);
    }

    public NetworkNodeLegend setupNodeLegend(NetworkNodeLegend legend, Collection<String> taxIds) {
        taxIds.stream()
                .map(this::getArchetype)
                .distinct()
                .sorted()
                .forEach(taxon -> {
                    if (taxon.isSpecies) legend.getSpeciesColor().put(taxon.getName(), taxon.getVisualProperty());
                    else legend.getKingdomColor().put(taxon.getName(), taxon.getVisualProperty());
                });
        return legend;
    }
}
