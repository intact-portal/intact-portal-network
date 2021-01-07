package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes;

public interface Archetype<P> {
    String getId();
    String getName();
    P getVisualProperty();

}
