package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def;

import java.util.HashMap;
import java.util.Map;

public class Ontology {
    private Map<String, Term> terms = new HashMap<>();
    private Term root;

    public Map<String, Term> getTerms() {
        return terms;
    }

    public Term getTerm(String id) {
        return terms.get(id);
    }

    public Term getRoot() {
        return root;
    }

    void setRoot(Term root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return terms.toString();
    }
}
