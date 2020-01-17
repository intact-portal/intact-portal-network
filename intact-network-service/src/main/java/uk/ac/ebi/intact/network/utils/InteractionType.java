package uk.ac.ebi.intact.network.utils;

/**
 * Created by anjali on 14/06/19.
 */
public enum InteractionType {

    IT_PHYSICAL_ASSOCIATION("physical association", "rgb(51, 94, 148)"),//Color: Navy blue
    IT_ASSOCIATION("association", "rgb(107, 13, 10)"),//Color: Brown
    IT_DIRECT_INTERACTION("direct interaction", "rgb(88, 115, 29)"),//Color: Green
    IT_COLOCALIZATION("colocalization", "rgb(97, 74, 124)"),//Color: Violet
    IT_PHOSPHORYLATION_REACTION("phosphorylation reaction", "rgb(47, 132, 156)"),//Color : Cyan
    IT_UBIQUITINATION_REACTION("ubiquitination reaction", "rgb(202, 115, 47)"),// Color : Orange
    IT_DEPHOSPHORYLATION_REACTION("dephosphorylation reaction", "rgb(144, 163, 198)"),//Color: Light blue
    IT_ENZYMATIC_REACTION("enzymatic reaction", "rgb(141, 102, 102)"),//Color: Light brown
    IT_OTHERS("others", "rgb(173, 188, 148)");//Color:Light green

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
