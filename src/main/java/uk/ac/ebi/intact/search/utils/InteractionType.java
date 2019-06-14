package uk.ac.ebi.intact.search.utils;

/**
 * Created by anjali on 14/06/19.
 */
public enum InteractionType {

    IT_PHYSICAL_ASSOCIATION("physical association", "rgb(255,153,204)"),//Color: Pink
    IT_ASSOCIATION("association", "rgb(255, 127, 0)"),//Color:Orange
    IT_DIRECT_INTERACTION("direct interaction", "rgb(0, 153, 153)"),//Color: Cyan
    IT_COLOCALIZATION("colocalization", "rgb(0, 255, 0)"),//Color: Green
    IT_PHOSPHORYLATION_REACTION("phosphorylation reaction", "rgb(0, 0, 255)"),//Color : Blue
    IT_UBIQUITINATION_REACTION("ubiquitination reaction", "rgb(75, 0, 130)"),// Color : Indigo
    IT_DEPHOSPHORYLATION_REACTION("dephosphorylation reaction", "rgb(255,0,0)"),//Color: Red
    IT_ENZYMATIC_REACTION("enzymatic reaction", "rgb(148, 0, 211)"),//Color: Dark Violet
    IT_OTHERS("others", "rgb(128, 128, 128)");//Color:Grey

    private String shortLabel;
    private String rgbColor;

    private InteractionType(String shortLabel, String rgbColor) {
        this.shortLabel = shortLabel;
        this.rgbColor = rgbColor;
    }

    public String shortLabel() {
        return shortLabel;
    }

    public String rgbColor() {
        return rgbColor;
    }



}
