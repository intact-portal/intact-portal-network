package uk.ac.ebi.intact.network.ws.controller.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.ac.ebi.intact.network.ws.controller.model.legend.Range;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class RangeSerializer extends JsonSerializer<Range> {

    private final static NumberFormat n = NumberFormat.getInstance(Locale.ENGLISH);
    static {
        n.setMinimumFractionDigits(1);
    }

    @Override
    public void serialize(Range value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(n.format(value.getStart()) + " - " + n.format(value.getStop()));
    }
}
