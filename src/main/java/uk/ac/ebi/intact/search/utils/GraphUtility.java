package uk.ac.ebi.intact.search.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anjali on 13/06/19.
 */
public class GraphUtility {
    private static final Log logger = LogFactory.getLog(GraphUtility.class);

    public static final HashMap<Integer, List<Integer>> speciesDescendantsMap = new HashMap<>();
    public static final HashMap<Integer, Integer> descendantsParentMap = new HashMap<>();

    public static void initializeSpeciesDescendantsMapping() {

        speciesDescendantsMap.put(9606,null);// Homo sapiens
        speciesDescendantsMap.put(4932,null);// Mus musculus
        speciesDescendantsMap.put(10090,null);// Saccharomyces cerevisiae
        speciesDescendantsMap.put(3702,null);// Arabidopsis thaliana (Mouse-ear cress)
        speciesDescendantsMap.put(7227,null);// Drosophila melanogaster
        speciesDescendantsMap.put(6239,null);// Caenorhabditis elegans
        speciesDescendantsMap.put(562, null);// Escherichia coli
        speciesDescendantsMap.put(-2, null);// chemical synthesis

        for (Integer parentSpecie : speciesDescendantsMap.keySet()) {

            String jsonQuery = "https://www.ebi.ac.uk/ols/api/ontologies/ncbitaxon/terms/" +
                    "http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252FNCBITaxon_" + parentSpecie + "/descendants?size=1000";

            String jsonText = "";
            List<Integer> specieChildren = new ArrayList<>();
            try {
                boolean hasNext = true;
                while (hasNext) {
                    jsonText = getJsonForUrl(jsonQuery);// mainQry
                    if (jsonText.length() > 0) {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(jsonText);
                        JsonNode embedded = jsonNode.get("_embedded");
                        JsonNode termChildren = embedded.get("terms");

                        for (final JsonNode objNode : termChildren) {
                            String obo_id = objNode.get("obo_id").textValue();
                            Integer id = Integer.parseInt(obo_id.substring(obo_id.indexOf(":") + 1, obo_id.length()));
                            specieChildren.add(id);
                            descendantsParentMap.put(id,parentSpecie);
                        }

                        JsonNode links = jsonNode.get("_links");
                        JsonNode nextPage = links.get("next");
                        if (nextPage != null) {
                            jsonQuery = nextPage.get("href").textValue();
                        }else {
                            hasNext=false;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if(specieChildren.isEmpty()){
                specieChildren=null;
            }
            speciesDescendantsMap.put(parentSpecie, specieChildren);
        }
    }

    /**
     * Gets Json text from the given query url
     *
     * @param jsonQuery
     * @return
     */
    public static String getJsonForUrl(String jsonQuery) {
        String jsonText = "";
        try {
            URL url = new URL(jsonQuery);
            URLConnection olsConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(olsConnection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                //jsonText += inputLine;
                builder.append(inputLine);
                    /*in.close();
                    break;*/
            }
            jsonText = builder.toString();
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return jsonText;
    }

    public static String getColorForTaxId(Integer taxId){
        Integer parentSpecie = GraphUtility.descendantsParentMap.get(taxId);
        Integer specieTaxId=((parentSpecie!=null) ? parentSpecie:(taxId));

        String color = null;
        switch(specieTaxId) {
            case 9606 :
                color=ColourCodes.SPECIES_TAXID_9606;
                break;

            case 562 :
                color=ColourCodes.SPECIES_TAXID_562;
                break;

            case 3702 :
                color=ColourCodes.SPECIES_TAXID_3702;
                break;

            case 4932 :
                color=ColourCodes.SPECIES_TAXID_4932;
                break;

            case 6239 :
                color=ColourCodes.SPECIES_TAXID_6239;
                break;

            case 7227 :
                color=ColourCodes.SPECIES_TAXID_7227;
                break;

            case 10090 :
                color=ColourCodes.SPECIES_TAXID_10090;
                break;

            case -2 :
                color=ColourCodes.SPECIES_TAXID_2;
                break;

            default :
                color = ColourCodes.SPECIES_TAXID_OTHERS;
        }

        return color;
    }

    public static String getColorForInteractionType(String interactiontype){

        String color =null;

        switch(interactiontype) {
            case "physical association" :
                color=InteractionType.IT_PHYSICAL_ASSOCIATION.rgbColor();
                break;

            case "association" :
                color=InteractionType.IT_ASSOCIATION.rgbColor();
                break;

            case "direct interaction" :
                color=InteractionType.IT_DIRECT_INTERACTION.rgbColor();
                break;

            case "colocalization" :
                color=InteractionType.IT_COLOCALIZATION.rgbColor();
                break;

            case "phosphorylation reaction" :
                color=InteractionType.IT_PHOSPHORYLATION_REACTION.rgbColor();
                break;

            case "ubiquitination reaction" :
                color=InteractionType.IT_UBIQUITINATION_REACTION.rgbColor();
                break;

            case "dephosphorylation reaction" :
                color=InteractionType.IT_DEPHOSPHORYLATION_REACTION.rgbColor();
                break;

            case "enzymatic reaction" :
                color=InteractionType.IT_ENZYMATIC_REACTION.rgbColor();
                break;

            default :
                color = InteractionType.IT_OTHERS.rgbColor();
        }

        return color;
    }
}
