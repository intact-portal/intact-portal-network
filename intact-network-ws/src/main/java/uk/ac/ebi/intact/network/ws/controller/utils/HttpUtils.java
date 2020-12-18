package uk.ac.ebi.intact.network.ws.controller.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.StyleMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtils {
    public static final Log logger = LogFactory.getLog(HttpUtils.class);

    public static String getRequestResultForUrl(String requestURL) {
        String jsonText = "";
        try {
            URL url = new URL(requestURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            jsonText = builder.toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return jsonText;
    }

    public static JsonNode getJsonForUrl(String jsonQuery) {
        String jsonText = getRequestResultForUrl(jsonQuery);
        if (jsonText.length() > 0) {
            try {
                return new ObjectMapper().readTree(jsonText);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
