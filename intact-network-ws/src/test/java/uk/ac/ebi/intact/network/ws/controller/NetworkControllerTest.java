package uk.ac.ebi.intact.network.ws.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.network.ws.controller.model.NetworkJson;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NetworkControllerTest {


    @Autowired
    private NetworkController controller;


    @Test
    public void getGraphJson() {
        ResponseEntity<NetworkJson> stat3 = controller.getGraphJson("lrrk2", false, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), false, 0, 1, false, false, 0, 100);
        assertTrue(stat3.hasBody());
    }
}