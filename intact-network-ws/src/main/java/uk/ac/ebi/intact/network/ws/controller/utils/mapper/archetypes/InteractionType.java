package uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes;

import java.awt.*;

public enum InteractionType implements Archetype<Color> {
    COLOCALIZATION("colocalization", "MI_0403", new Color(216, 216, 216), false),
    ASSOCIATION("association", "MI_0914", new Color(186, 228, 188), false),
    PHYSICAL_ASSOCIATION("physical association", "MI_0915",new Color(123, 204, 196), false),
    DIRECT_INTERACTION("direct interaction", "MI_0407", new Color(67, 162, 202) , true),
    ENZYMATIC_REACTION("enzymatic reaction", "MI_0414", new Color(8, 104, 172), true),
    PHOSPHORYLATION_R("phosphorylation reaction", "MI_0217", new Color(253, 141, 60), true),
    DEPHOSPHORYLATION_R("dephosphorylation reaction", "MI_0203",new Color(247, 104, 161), true);

    public final String name;
    public final String MI_ID;
    public final Color defaultColor;
    public final boolean queryChildren;

    InteractionType(String name, String MI_ID, Color defaultColor, boolean queryChildren) {
        this.name = name;
        this.MI_ID = MI_ID;
        this.defaultColor = defaultColor;
        this.queryChildren = queryChildren;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getVisualProperty() {
        return defaultColor;
    }
}
