package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.intact.style.model.shapes.EdgeShape;

import java.awt.*;

/**
 * Created by anjali on 17/05/19.
 */
public class NetworkLink {

    @JsonProperty("id")
    private int id;

    @JsonProperty("source")
    private String source;

    @JsonProperty("target")
    private String target;

    @JsonProperty("interaction_ac")
    private String interactionAc;

    @JsonProperty("interaction_type")
    private String interactionType;

    @JsonProperty("interaction_detection_method")
    private String interactionDetectionMethod;

    @JsonProperty("affected_by_mutation")
    private boolean affectedByMutation;

    @JsonProperty("mi_score")
    private double miScore;

    @JsonProperty("color")
    private Color color;

    @JsonProperty("collapsed_color")
    private Color collapsedColor;

    @JsonProperty("shape")
    private EdgeShape shape;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getInteractionAc() {
        return interactionAc;
    }

    public void setInteractionAc(String interactionAc) {
        this.interactionAc = interactionAc;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getInteractionDetectionMethod() {
        return interactionDetectionMethod;
    }

    public void setInteractionDetectionMethod(String interactionDetectionMethod) {
        this.interactionDetectionMethod = interactionDetectionMethod;
    }

    public boolean isAffectedByMutation() {
        return affectedByMutation;
    }

    public void setAffectedByMutation(boolean affectedByMutation) {
        this.affectedByMutation = affectedByMutation;
    }

    public double getMiScore() {
        return miScore;
    }

    public void setMiScore(double miScore) {
        this.miScore = miScore;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getCollapsedColor() {
        return collapsedColor;
    }

    public void setCollapsedColor(Color collapsedColor) {
        this.collapsedColor = collapsedColor;
    }

    public EdgeShape getShape() {
        return shape;
    }

    public void setShape(EdgeShape shape) {
        this.shape = shape;
    }
}
