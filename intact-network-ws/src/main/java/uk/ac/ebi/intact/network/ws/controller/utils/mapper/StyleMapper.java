package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.XML;
import uk.ac.ebi.intact.network.ws.controller.utils.EdgeColor;
import uk.ac.ebi.intact.network.ws.controller.utils.EdgeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.HttpUtils;
import uk.ac.ebi.intact.network.ws.controller.utils.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.definitions.InteractionType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.definitions.InteractorType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.definitions.SourceOntology;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.definitions.Taxons;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static uk.ac.ebi.intact.network.ws.controller.utils.mapper.definitions.Taxons.ARTIFICIAL;

public class StyleMapper {
    public static final Log logger = LogFactory.getLog(StyleMapper.class);


    public static Map<String, Color> speciesColors = Arrays.stream(Taxons.values())
            .filter(taxons -> taxons.isSpecies)
            .collect(toMap(
                    taxon -> taxon.taxId,
                    taxon -> taxon.defaultColor,
                    (u, v) -> u)
            );

    public static Map<String, Color> kingdomColors = Arrays.stream(Taxons.values())
            .filter(taxons -> !taxons.isSpecies)
            .collect(toMap(
                    taxon -> taxon.taxId,
                    taxon -> taxon.defaultColor,
                    (u, v) -> u)
            );

    public static Map<String, Color> originalKingdomColors = new HashMap<>(kingdomColors);

    public static Map<String, List<String>> taxIdToChildrenTaxIds = new HashMap<>();
    public static Map<String, String> taxIdToParentTaxId = new HashMap<>();

    public static final Map<String, Color> edgeTypeToColor = Arrays.stream(InteractionType.values())
            .collect(toMap(
                    type -> type.MI_ID.replace("_", ":"),
                    type -> type.defaultColor,
                    (u, v) -> u)
            );

    public static final Map<String, NodeShape> nodeTypeToShape = Arrays.stream(InteractorType.values())
            .collect(toMap(
                    type -> type.MI_ID.replace("_", ":"),
                    type -> type.shape,
                    (u, v) -> u)
            );

    public static final Map<String, List<String>> nodeTypeToParent = new HashMap<>();
    public static final Map<String, List<String>> edgeTypeToParent = new HashMap<>();

    public static final Map<String, String> typesToIds = new HashMap<>();

    static {
        Arrays.stream(InteractionType.values()).filter(type -> !type.MI_ID.isEmpty()).forEach(type -> typesToIds.put(type.name, type.MI_ID));
        Arrays.stream(InteractorType.values()).filter(type -> !type.MI_ID.isEmpty()).forEach(type -> typesToIds.put(type.name, type.MI_ID));
    }

    public static void initializeSpeciesAndKingdomColors() {
        for (String kingdomId : kingdomColors.keySet()) {
            taxIdToChildrenTaxIds.put(kingdomId, new ArrayList<>());
        }
        taxIdToChildrenTaxIds.get(ARTIFICIAL.taxId).add("-1");
        speciesColors.put("-1", kingdomColors.get(ARTIFICIAL.taxId));

        for (String parentSpecie : new ArrayList<>(speciesColors.keySet())) {
            Color paint = speciesColors.get(parentSpecie);
            addDescendantsColors(parentSpecie, paint);
        }
    }


    public static void addDescendantsColors(String parentSpecie, Color paint) {
        taxIdToChildrenTaxIds.put(parentSpecie, new ArrayList<>());

        String jsonQuery = "https://www.ebi.ac.uk/ols/api/ontologies/ncbitaxon/terms/" +
                "http%253A%252F%252Fpurl.obolibrary.org%252Fobo%252FNCBITaxon_" + parentSpecie + "/descendants?size=1000";

        try {
            boolean hasNext = true;
            while (hasNext) {
                JsonNode json = HttpUtils.getJsonForUrl(jsonQuery);
                if (json != null) {
                    if (json.get("page").get("totalElements").intValue() > 0) {

                        JsonNode termChildren = json.get("_embedded").get("terms");

                        for (final JsonNode objNode : termChildren) {
                            String obo_id = objNode.get("obo_id").asText();
                            String id = obo_id.substring(obo_id.indexOf(":") + 1);
                            speciesColors.put(id, paint);
                            taxIdToChildrenTaxIds.get(parentSpecie).add(id);
                        }
                    }
                    JsonNode nextPage = json.get("_links").get("next");
                    if (nextPage != null) {
                        jsonQuery = nextPage.get("href").textValue();
                    } else {
                        hasNext = false;
                    }
                } else {
                    hasNext = false;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public synchronized static Map<String, Color> harvestKingdomsOf(Set<String> taxIdsToCheckAndAdd, boolean setColor) {
        Map<String, Color> addedTaxIds = new HashMap<>();

        if (taxIdsToCheckAndAdd == null)
            return addedTaxIds;

        taxIdsToCheckAndAdd.removeAll(speciesColors.keySet());
        taxIdsToCheckAndAdd.removeAll(kingdomColors.keySet());

        if (taxIdsToCheckAndAdd.size() == 0)
            return addedTaxIds;

        try {
            String resultText = HttpUtils.getRequestResultForUrl("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=taxonomy&id=" + concatenateTaxIds(taxIdsToCheckAndAdd));
            JSONObject jObject = XML.toJSONObject(resultText);
            JsonNode taxons = new ObjectMapper().readTree(jObject.toString()).get("TaxaSet").get("Taxon");
            if (taxons.isArray()) {
                for (JsonNode taxon : taxons) {
                    addTaxon(addedTaxIds, taxon, setColor);
                }
            } else {
                addTaxon(addedTaxIds, taxons, setColor);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return addedTaxIds;
    }

    private static void addTaxon(Map<String, Color> addedTaxIds, JsonNode taxons, boolean setColor) {
        String taxId = taxons.get("TaxId").asText();
        ArrayNode lineage = (ArrayNode) taxons.get("LineageEx").get("Taxon");

        for (int i = lineage.size() - 1; i >= 0; i--) {
            String supTaxId = lineage.get(i).get("TaxId").asText();
            if (originalKingdomColors.containsKey(supTaxId)) {
                taxIdToChildrenTaxIds.get(supTaxId).add(taxId);
                taxIdToParentTaxId.put(taxId, supTaxId);
                if (setColor) {
                    Color paint = kingdomColors.get(supTaxId);
                    kingdomColors.put(taxId, paint);
                    addedTaxIds.put(taxId, paint);
                }
                break;
            }
        }
    }

    public static void initializeNodeTypeToShape() {
        for (String miType : new ArrayList<>(nodeTypeToShape.keySet())) {
            setChildrenValues(nodeTypeToShape, miType, nodeTypeToShape.get(miType), nodeTypeToParent);
        }


    }

    public static void initializeEdgeTypeToColor() {

        Map<String, Color> originalColors = new HashMap<>(edgeTypeToColor);

        Arrays.stream(InteractionType.values())
                .filter(type -> type.queryChildren)
                .forEach(type -> setChildrenValues(edgeTypeToColor, type.MI_ID.replace("_", ":"), type.defaultColor, edgeTypeToParent));

        edgeTypeToColor.putAll(originalColors);


    }

    private static <T> void setChildrenValues(Map<String, T> mapToFill, String parentId, T parentValue, Map<String, List<String>> parentToChildIdMap) {
        String jsonQuery = SourceOntology.MI.getDescendantsURL(parentId);

        try {
            boolean hasNext = true;
            while (hasNext) {
                JsonNode json = HttpUtils.getJsonForUrl(jsonQuery);
                if (json != null) {
                    if (json.get("page").get("totalElements").intValue() > 0) {

                        JsonNode termChildren = json.get("_embedded").get("terms");

                        List<String> children = new ArrayList<>();
                        for (final JsonNode objNode : termChildren) {
                            String childId = objNode.get("obo_id").textValue();
                            mapToFill.put(childId, parentValue);
                            mapToFill.put(childId, parentValue);
                            children.add(childId);
                        }
                        parentToChildIdMap.put(parentId, children);
                    }
                    JsonNode nextPage = json.get("_links").get("next");
                    if (nextPage != null) {
                        jsonQuery = nextPage.get("href").textValue();
                    } else {
                        hasNext = false;
                    }
                } else {
                    hasNext = false;
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static String concatenateTaxIds(Set<String> taxIds) {
        return taxIds.stream().filter(taxId -> !taxId.startsWith("-")).collect(toList()).toString().replaceAll("[\\[\\]]", "").replaceAll(" ", "%20");
    }

    private static String encodeColor(Color color) {
        return String.format("rgb(%s,%s,%s)", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String getColorForInteractionType(String interactionTypeMIIdentifier) {
        return encodeColor(edgeTypeToColor.get(interactionTypeMIIdentifier));
    }

    public static String getColorForTaxId(Integer taxonId) {
        String taxId = taxonId.toString();
        if (speciesColors.containsKey(taxId)) {
            return encodeColor(speciesColors.get(taxId));
        } else if (kingdomColors.containsKey(taxId)) {
            return encodeColor(kingdomColors.get(taxId));
        } else return "rgb(173, 188, 148)";
    }

    public static String getShapeForInteractorType(String interactorType) {
        NodeShape nodeShape = nodeTypeToShape.get(interactorType);
        if (nodeShape != null) return nodeShape.title;
        else return NodeShape.TAG.title;
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

    public static String getColorForCollapsedEdgeDiscrete(double miScore) {

        int miScoreFloor = (int) Math.floor(miScore * 10);
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
