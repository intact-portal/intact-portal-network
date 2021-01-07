package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Archetype;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Taxon;

import java.awt.*;

/**
 * Created by anjali on 17/05/19.
 */
public class NetworkNode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("species")
    private String speciesName;

    @JsonProperty("tax_id")
    private int taxId;

    @JsonProperty("color")
    private Color color;

    @JsonProperty("color_archetype")
    private Archetype<Color> colorArchetype;

    @JsonProperty("border_color")
    private Color borderColor;

    @JsonProperty("label")
    private String interactorId;

    @JsonProperty("interactor_type")
    private String interactorType;

    @JsonProperty("interactor_name")
    private String interactorName;

    @JsonProperty("preferred_id")
    private String preferredId;

    @JsonProperty("parent")
    private String parent;

    @JsonProperty("shape")
    private NodeShape shape;

    @JsonProperty("clusterID")
    private int clusterId;

    @JsonProperty("mutation")
    private boolean mutation;

    @JsonProperty("preferred_id_db")
    private String preferredIdWithDB;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Archetype<Color> getColorArchetype() {
        return colorArchetype;
    }

    public void setColorArchetype(Archetype<Color> colorArchetype) {
        this.colorArchetype = colorArchetype;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public String getInteractorId() {
        return interactorId;
    }

    public void setInteractorId(String interactorId) {
        this.interactorId = interactorId;
    }

    public String getInteractorType() {
        return interactorType;
    }

    public void setInteractorType(String interactorType) {
        this.interactorType = interactorType;
    }

    public String getInteractorName() {
        return interactorName;
    }

    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }

    public String getPreferredId() {
        return preferredId;
    }

    public void setPreferredId(String preferredId) {
        this.preferredId = preferredId;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public NodeShape getShape() {
        return shape;
    }

    public void setShape(NodeShape shape) {
        this.shape = shape;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public boolean isMutation() {
        return mutation;
    }

    public void setMutation(boolean mutation) {
        this.mutation = mutation;
    }

    public String getPreferredIdWithDB() {
        return preferredIdWithDB;
    }

    public void setPreferredIdWithDB(String preferredIdWithDB) {
        this.preferredIdWithDB = preferredIdWithDB;
    }
}
