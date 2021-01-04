package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Term  {
    private List<Term> children = new ArrayList<>();
    private List<Term> parents = new ArrayList<>();
    private String id;
    private String name;

    public Term() {
    }

    public Term(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<Term> getChildren() {
        return children;
    }

    public void setChildren(List<Term> children) {
        this.children = children;
    }

    public List<Term> getParents() {
        return parents;
    }

    public void setParents(List<Term> parents) {
        this.parents = parents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Term)) return false;
        Term term = (Term) o;
        return getId().equals(term.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return name;
    }
}
