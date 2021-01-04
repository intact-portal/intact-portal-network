package uk.ac.ebi.intact.network.ws.controller.model.legend;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Range implements Comparable<Range> {
    @JsonProperty
    private Double start;

    @JsonProperty
    private Double stop;

    public Range() {
    }

    public Range(Double start, Double stop) {
        if (stop <= start) throw new IllegalArgumentException("Stop must be greater than start");
        this.start = start;
        this.stop = stop;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        if (stop <= start) throw new IllegalArgumentException("Start must be lesser than stop");
        this.start = start;
    }

    public Double getStop() {
        return stop;
    }

    public void setStop(Double stop) {
        if (stop <= start) throw new IllegalArgumentException("Stop must be greater than start");
        this.stop = stop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;
        Range range = (Range) o;
        return Double.compare(range.getStart(), getStart()) == 0 && Double.compare(range.getStop(), getStop()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getStop());
    }

    @Override
    public int compareTo(Range o) {
        if (stop >= o.start) return 1;
        if (start <= o.stop) return -1;
        return 0;
    }

    public boolean isIncluded(Double value) {
        return start <= value && value <= stop;
    }
}
