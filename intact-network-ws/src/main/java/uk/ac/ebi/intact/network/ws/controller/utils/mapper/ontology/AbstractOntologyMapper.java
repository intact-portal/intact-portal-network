package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.Mapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.Archetype;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;

import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * Map a key to its closest archetype (A) thanks to an ontology tree
 *
 * @param <A> Archetype, an Enum describing specific nodes in an ontology to summarize all its closest children, and link them with their style
 */
public abstract class AbstractOntologyMapper<A extends Archetype<P>, P> implements Mapper<String, P> {
    private static final Log log = LogFactory.getLog(AbstractOntologyMapper.class);
    private Map<String, A> keyToArchetype = new HashMap<>();

    protected abstract Term getRootOfOntology();

    /**
     * Walk through the ontology to index keys to their related archetypes.
     * Must be called at service initiation.
     * Support reindexing.
     */
    public void indexTree() {
        if (keyToArchetype.isEmpty()) {
            indexRecursively(getRootOfOntology(), null, keyToArchetype);
        } else {
            keyToArchetype = indexRecursively(getRootOfOntology(), null, new HashMap<>());
        }
    }


    /**
     * @param key A key identifying an Archetype
     * @return null if key is not an Archetype, or the Archetype instance
     */
    protected abstract A matchArchetype(String key);

    private Map<String, A> indexRecursively(Term sourceNode, A archetype, Map<String, A> mapping) {
        String key = sourceNode.getId();
        A currentArchetype = matchArchetype(key);
        if (currentArchetype == null) {
            currentArchetype = archetype;
        }
        if (currentArchetype != null && !mapping.containsKey(key)) {
            mapping.put(key, currentArchetype);
        }
        for (Term child : sourceNode.getChildren()) {
            indexRecursively(child, currentArchetype, mapping);
        }
        return mapping;
    }

    public A getArchetype(String key) {
        A archetype = keyToArchetype.get(key);
        if (archetype != null) return archetype;
        A defaultArchetype = getDefaultArchetype();
        log.debug(key + " could not be mapped to any archetype. " + defaultArchetype.getName() + " will  be used as the default one.");
        return defaultArchetype;
    }

    public P getStyleOf(String key) {
        return getArchetype(key).getVisualProperty();
    }

    public abstract A getDefaultArchetype();

    public Map<String, P> createLegend(Collection<String> facets) {
        return facets.stream()
                .map(this::getArchetype)
                .distinct()
                .filter(Objects::nonNull)
                .sorted()
                .collect(toMap(Archetype::getName, Archetype::getVisualProperty, (u, v) -> u, LinkedHashMap::new));
    }
}
