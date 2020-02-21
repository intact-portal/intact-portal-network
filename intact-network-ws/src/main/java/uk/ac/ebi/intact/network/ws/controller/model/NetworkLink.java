package uk.ac.ebi.intact.network.ws.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("color")
    private String color;

    @JsonProperty("shape")
    private String shape;

    @JsonProperty("collapsed_color")
    private String collapsedColor;

    @JsonProperty("affected_by_mutation")
    private boolean affectedByMutation;

    @JsonProperty("mi_score")
    private double miScore;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getCollapsedColor() {
        return collapsedColor;
    }

    public void setCollapsedColor(String collapsedColor) {
        this.collapsedColor = collapsedColor;
    }

    public boolean isAffectedByMutation() {
        return affectedByMutation;
    }

    public void setAffectedByMutation(boolean affectedByMutation) {
        this.affectedByMutation = affectedByMutation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMiScore() {
        return miScore;
    }

    public void setMiScore(double miScore) {
        this.miScore = miScore;
    }
}
