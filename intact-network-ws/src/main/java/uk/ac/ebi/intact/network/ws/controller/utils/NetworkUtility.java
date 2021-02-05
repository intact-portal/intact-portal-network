package uk.ac.ebi.intact.network.ws.controller.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anjali on 13/06/19.
 */
public class NetworkUtility {
    public static final HashMap<Integer, List<Integer>> speciesDescendantsMap = new HashMap<>();
    public static final HashMap<Integer, Integer> descendantsParentMap = new HashMap<>();
    public static final HashMap<String, List<String>> interactorTypeDescendantsMap = new HashMap<>();
    public static final HashMap<String, String> interactorTypeParentMap = new HashMap<>();
    public static final HashMap<String, List<String>> interactionTypeDescendantsMap = new HashMap<>();
    public static final HashMap<String, String> interactionTypeParentMap = new HashMap<>();
    private static final Log logger = LogFactory.getLog(NetworkUtility.class);

    public static void initializeSpeciesDescendantsMapping() {

        speciesDescendantsMap.put(9606, null);// Homo sapiens
        speciesDescendantsMap.put(4932, null);// Mus musculus
        speciesDescendantsMap.put(10090, null);// Saccharomyces cerevisiae
        speciesDescendantsMap.put(3702, null);// Arabidopsis thaliana (Mouse-ear cress)
        speciesDescendantsMap.put(7227, null);// Drosophila melanogaster
        speciesDescendantsMap.put(6239, null);// Caenorhabditis elegans
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
                            descendantsParentMap.put(id, parentSpecie);
                        }

                        JsonNode links = jsonNode.get("_links");
                        JsonNode nextPage = links.get("next");
                        if (nextPage != null) {
                            jsonQuery = nextPage.get("href").textValue();
                        } else {
                            hasNext = false;
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
            if (specieChildren.isEmpty()) {
                specieChildren = null;
            }
            speciesDescendantsMap.put(parentSpecie, specieChildren);
        }
    }

    public static void initializeInteractorTypeDescendantsMapping() {
        interactorTypeDescendantsMap.put("MI:1100", null);// Bioactive entity
        interactorTypeDescendantsMap.put("MI:0320", null);// RNA
        interactorTypeDescendantsMap.put("MI:0319", null);// DNA
        interactorTypeDescendantsMap.put("MI:0250", null);// Gene
        interactorTypeDescendantsMap.put("MI:0326", null);// Protein
        interactorTypeDescendantsMap.put("MI:0327", null);// Peptide
        interactorTypeDescendantsMap.put("MI:0314", null);// Complex

        for (String parentInteractorType : interactorTypeDescendantsMap.keySet()) {

            String jsonQuery = "https://www.ebi.ac.uk/ols/api/ontologies/mi/terms/" +
                    "http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252F" + parentInteractorType.replaceAll(":", "_") + "/descendants?size=1000";

            String jsonText = "";
            List<String> typeChildren = new ArrayList<>();
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
                            typeChildren.add(obo_id);
                            interactorTypeParentMap.put(obo_id, parentInteractorType);
                        }

                        JsonNode links = jsonNode.get("_links");
                        JsonNode nextPage = links.get("next");
                        if (nextPage != null) {
                            jsonQuery = nextPage.get("href").textValue();
                        } else {
                            hasNext = false;
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
            if (typeChildren.isEmpty()) {
                typeChildren = null;
            }
            interactorTypeDescendantsMap.put(parentInteractorType, typeChildren);
        }
    }

    public static void initializeInteractionTypeDescendantsMapping() {

        interactionTypeDescendantsMap.put("MI:0407", null);// direct interaction
        interactionTypeDescendantsMap.put("MI:0217", null);// phosphorylation reaction
        interactionTypeDescendantsMap.put("MI:0203", null);// dephosphorylation reaction

        for (String parentInteractionType : interactionTypeDescendantsMap.keySet()) {

            String jsonQuery = "https://www.ebi.ac.uk/ols/api/ontologies/mi/terms/" +
                    "http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252F" + parentInteractionType.replaceAll(":", "_") + "/descendants?size=1000";

            String jsonText = "";
            List<String> typeChildren = new ArrayList<>();
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
                            typeChildren.add(obo_id);
                            interactionTypeParentMap.put(obo_id, parentInteractionType);
                        }

                        JsonNode links = jsonNode.get("_links");
                        JsonNode nextPage = links.get("next");
                        if (nextPage != null) {
                            jsonQuery = nextPage.get("href").textValue();
                        } else {
                            hasNext = false;
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
            if (typeChildren.isEmpty()) {
                typeChildren = null;
            }
            interactionTypeDescendantsMap.put(parentInteractionType, typeChildren);
        }

        interactionTypeDescendantsMap.put("MI:0914", null);// association
        interactionTypeDescendantsMap.put("MI:0915", null);// physical association
        interactionTypeDescendantsMap.put("MI:0403", null);// colocalization
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
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("hx-wwwcache.ebi.ac.uk", 3128));
            URL url = new URL(jsonQuery);
            URLConnection olsConnection = url.openConnection(proxy);
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

    public static void test(){
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            List<Proxy> proxy = ProxySelector.getDefault().select(
                    new URI("http://www.yahoo.com/"));
            if(proxy!=null) {
                Proxy proxy1=proxy.get(0);
                System.out.println("proxy hostname : " + proxy1.type());
                InetSocketAddress addr = (InetSocketAddress) proxy1.address();
                System.out.println("proxy hostname : " + addr.getHostName());
                System.out.println("proxy port : " + addr.getPort());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getColorForTaxId(Integer taxId) {
        Integer parentSpecie = NetworkUtility.descendantsParentMap.get(taxId);
        Integer specieTaxId = ((parentSpecie != null) ? parentSpecie : (taxId));

        String color = null;

        if (specieTaxId == null) {
            color = ColourCodes.SPECIES_TAXID_OTHERS;
        } else {
            switch (specieTaxId) {
                case 9606:
                    color = ColourCodes.SPECIES_TAXID_9606;
                    break;

                case 562:
                    color = ColourCodes.SPECIES_TAXID_562;
                    break;

                case 3702:
                    color = ColourCodes.SPECIES_TAXID_3702;
                    break;

                case 4932:
                    color = ColourCodes.SPECIES_TAXID_4932;
                    break;

                case 6239:
                    color = ColourCodes.SPECIES_TAXID_6239;
                    break;

                case 7227:
                    color = ColourCodes.SPECIES_TAXID_7227;
                    break;

                case 10090:
                    color = ColourCodes.SPECIES_TAXID_10090;
                    break;

                case -2:
                    color = ColourCodes.SPECIES_TAXID_2;
                    break;

                default:
                    color = ColourCodes.SPECIES_TAXID_OTHERS;
            }
        }

        return color;
    }

    public static String getColorForInteractionType(String interactionTypeMIIdentifier) {

        String interactionTypeIdentifier = null;
        String color = null;
        if (interactionTypeDescendantsMap.containsKey(interactionTypeMIIdentifier)) {
            interactionTypeIdentifier = interactionTypeMIIdentifier;
        } else {
            interactionTypeIdentifier = NetworkUtility.interactionTypeParentMap.get(interactionTypeMIIdentifier);
        }

        if (interactionTypeIdentifier == null) {
            color = EdgeColor.OTHERS;
        } else {
            switch (interactionTypeIdentifier) {
                case "MI:0915": // physical association
                    color = EdgeColor.PHYSICAL_ASSOCIATION;
                    break;

                case "MI:0914": // association
                    color = EdgeColor.ASSOCIATION;
                    break;

                case "MI:0407": // direct interaction
                    color = EdgeColor.DIRECT_INTERACTION;
                    break;

                case "MI:0403": // colocalization
                    color = EdgeColor.COLOCALIZATION;
                    break;

                case "MI:0217": // phosphorylation reaction
                    color = EdgeColor.PHOSPHORYLATION_REACTION;
                    break;

                case "MI:0203": // dephosphorylation reaction
                    color = EdgeColor.DEPHOSPHORYLATION_REACTION;
                    break;

                default:
                    color = EdgeColor.OTHERS;
            }
        }

        return color;
    }

    public static String getShapeForInteractorType(String interactorType) {
        String parentInteractorType = NetworkUtility.interactorTypeParentMap.get(interactorType);
        String interactorTypeMI = ((parentInteractorType != null) ? parentInteractorType : (interactorType));

        String shape = null;
        if (interactorTypeMI == null) {
            shape = NodeShape.TAG;
        } else {
            switch (interactorTypeMI) {
                case "MI:1100": //Bioactive entity
                    shape = NodeShape.TRIANGLE;
                    break;

                case "MI:0320": //RNA
                    shape = NodeShape.DIAMOND;
                    break;

                case "MI:0319": //DNA
                    shape = NodeShape.UPSIDE_DOWN_CUT_TRIANGLE;
                    break;

                case "MI:0250": // Gene
                    shape = NodeShape.ROUNDED_RECTANGLE;
                    break;

                case "MI:0326":// Protein
                case "MI:0327": // Peptide
                    shape = NodeShape.ELLIPSE;
                    break;
                case "MI:0314": //Complex
                    shape = NodeShape.HEXAGON;
                    break;
                default:
                    shape = NodeShape.TAG;
            }
        }

        return shape;
    }

    public static String getShapeForExpansionType(String expansionType) {

        String shape = null;
        if (expansionType == null) {
            shape = EdgeShape.SOLID_LINE;
        } else {
            switch (expansionType) {
                case "spoke expansion":
                    shape = EdgeShape.DASHED_LINE;
                    break;

                default:
                    shape = EdgeShape.SOLID_LINE;
            }
        }
        return shape;
    }

    public static String getColorForCollapsedEdge(double miScore) {

        Color start = EdgeColor.COLLAPSED_EDGE_COLOR_START;
        Color end = EdgeColor.COLLAPSED_EDGE_COLOR_END;
        float blending = (float) miScore;

        float inverse_blending = 1 - blending;

        float red = end.getRed() * blending + start.getRed() * inverse_blending;
        float green = end.getGreen() * blending + start.getGreen() * inverse_blending;
        float blue = end.getBlue() * blending + start.getBlue() * inverse_blending;

        //note that if i pass float values they have to be in the range of 0.0-1.0
        //and not in 0-255 like the ones i get returned by the getters.
        Color collapsedEdgeColor = new Color(red / 255, green / 255, blue / 255);
        return "rgb(" + collapsedEdgeColor.getRed() + "," + collapsedEdgeColor.getGreen() + "," + collapsedEdgeColor.getBlue() + ")";
    }

    public static String getColorForCollapsedEdgeDiscrete(double miScore) {

        int miScoreFloor = (int) Math.floor(miScore*10);
        return EdgeColor.YELLOW_ORANGE_BROWN_PALETTE[miScoreFloor];
    }

    public static String createNodeLabel(String preferredName, String preferredId, String interactorAc) {
        if (preferredName != null) {
            return preferredName + " (" + preferredId + ")";
        } else {
            return interactorAc;
        }
    }

}
