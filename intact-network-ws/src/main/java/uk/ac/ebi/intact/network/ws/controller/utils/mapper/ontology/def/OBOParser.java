package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

public class OBOParser {
    public static Ontology parseOntology(InputStream oboFile) throws FileNotFoundException {
        Ontology ontology = new Ontology();
        Map<String, Term> terms = ontology.getTerms();
        Scanner sc = new Scanner(oboFile);
        Term currentTerm = null;
        boolean isObsolete = false;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.startsWith("[Term]")) {
                currentTerm = new Term();
                isObsolete = false;
            } else if (currentTerm != null) {
                if (line.startsWith("id: ")) {
                    String id = line.substring(4);
                    if (terms.containsKey(id)) currentTerm = terms.get(id);
                    else currentTerm.setId(id);
                } else if (line.startsWith("name: ")) {
                    currentTerm.setName(line.substring(6));
                } else if (line.startsWith("is_obsolete: true")) {
                    isObsolete = true;
                } else if (line.startsWith("is_a: ")) {
                    String refString = line.substring(6);
                    addParent(terms, currentTerm, refString);
                } else if (line.startsWith("relationship: ")) {
                    String[] relationship = line.substring(14).split(" ", 2);
                    switch (relationship[0]) {
                        case "has_functional_parent":
                        case "derives_from":
                        case "part_of":
                            addParent(terms, currentTerm, relationship[1]);
                            break;
                        case "contains":
                            addChild(terms, currentTerm, relationship[1]);
                            break;
                    }
                } else if (line.isEmpty()) {
                    if (!isObsolete) terms.put(currentTerm.getId(), currentTerm);
                    currentTerm = null;
                }
            }
        }

        setRoot(ontology);
        return ontology;
    }

    private static void setRoot(Ontology ontology) {
        if (ontology.getTerms().isEmpty()) return;
        Term term = ontology.getTerms().values().iterator().next();
        while (!term.getParents().isEmpty()) {
            term = term.getParents().get(0);
        }
        ontology.setRoot(term);
    }

    private static void addParent(Map<String, Term> terms, Term currentTerm, String parentRef) {
        Term parent = getOrCreateTermFromRef(terms, parentRef);
        parent.getChildren().add(currentTerm);
        currentTerm.getParents().add(parent);
    }

    private static void addChild(Map<String, Term> terms, Term currentTerm, String childRef) {
        Term child = getOrCreateTermFromRef(terms, childRef);
        child.getParents().add(currentTerm);
        currentTerm.getChildren().add(child);
    }

    private static Term getOrCreateTermFromRef(Map<String, Term> terms, String childRef) {
        String[] refElements = childRef.split(" ! ");
        String id = refElements[0];
        Term child;
        if (terms.containsKey(id)) {
            child = terms.get(id);
        } else {
            child = new Term(refElements[0], refElements[1]);
            terms.put(id, child);
        }
        return child;
    }
}
