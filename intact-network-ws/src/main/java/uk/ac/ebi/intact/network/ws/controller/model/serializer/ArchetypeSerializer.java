package uk.ac.ebi.intact.network.ws.controller.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Archetype;

import java.io.IOException;

public class ArchetypeSerializer extends JsonSerializer<Archetype<?>> {
    @Override
    public void serialize(Archetype<?> archetype, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", archetype.getId());
        gen.writeObjectField("name", archetype.getName());
        gen.writeObjectField("property", archetype.getVisualProperty());
        gen.writeEndObject();
    }
}
