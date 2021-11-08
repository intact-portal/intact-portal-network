package uk.ac.ebi.intact.network.ws.controller;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.network.ws.controller.model.NetworkJson;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NetworkControllerTest {

    @Autowired
    private NetworkController controller;

    @Test
    @Ignore
    public void getGraphJson() {
        ResponseEntity<NetworkJson> lrrk2 = controller.getGraphJson(
                "lrrk2",
                false,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                NegativeFilterStatus.POSITIVE_ONLY,
                false,
                false,
                0,
                1,
                false,
                false,
                0, 100);
        assertTrue(lrrk2.hasBody());
    }
}