package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import uk.ac.ebi.intact.network.ws.controller.model.shapes.NodeShape;

import java.awt.*;

public class PropertySerializer {
    public static String serialize(Object property) {
        if (property == null) {
            return "";
        } else if (property instanceof Color) {
            Color color = (Color) property;
            return String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
        } else if (property instanceof String) {
            return (String) property;
        } else if (property instanceof NodeShape) {
            return ((NodeShape) property).title;
        }
        return "";
    }
}
