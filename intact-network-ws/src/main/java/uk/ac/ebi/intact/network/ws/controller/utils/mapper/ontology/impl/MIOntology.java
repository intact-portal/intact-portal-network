package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.network.ws.controller.NetworkController;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.OBOParser;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Ontology;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MIOntology {
    private static final Log log = LogFactory.getLog(NetworkController.class);

    private Ontology ontology;
    public MIOntology() {
        setupOntology();
    }

    private void setupOntology() {
        try {
            InputStream inputStream = MIOntology.class.getResource("/mi.obo").openStream();
            log.info("Start indexing of mi.obo");
            ontology = OBOParser.parseOntology(inputStream, null);
            log.info("Finished indexing of mi.obo");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            try {
                URL url = new URL("http://ontologies.berkeleybop.org/mi.obo");
                log.info("Start downloading of mi.obo");
                Files.copy(url.openStream(), Paths.get(MIOntology.class.getResource("/").getPath(),"mi.obo"), StandardCopyOption.REPLACE_EXISTING);
                log.info("Finished downloading of mi.obo");
                setupOntology();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public Ontology get() {
        return ontology;
    }
}
