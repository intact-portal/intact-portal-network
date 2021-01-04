package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.OBOParser;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Ontology;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OBOParserTest {

    @Test
    public void testParseOntology() {
        try {
            InputStream file = OBOParser.class.getResource("/mi.obo").openStream();
            Ontology ontology = OBOParser.parseOntology(file);
            System.out.println(ontology);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

