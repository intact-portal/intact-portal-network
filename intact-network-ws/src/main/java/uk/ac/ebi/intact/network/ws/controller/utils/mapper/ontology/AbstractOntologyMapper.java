package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.Mapper;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.Archetype;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * Map a key (K) to its closest archetype (A) thanks to an ontology tree
 *
 * @param <K> Key of mapping, faceted by solr and used as source for the mapping
 * @param <A> Archetype, an Enum describing specific nodes in an ontology to summarize all its closest children, and link them with their style
 */
public abstract class AbstractOntologyMapper<K, A extends Archetype<P>, P> implements Mapper<K, P> {
    private Map<K, A> keyToArchetype = new HashMap<>();

    protected abstract TreeNode<K> getRootOfOntology();

    /**
     * Walk through the ontology to index keys to their related archetypes.
     * Must be called at service initiation.
     * Support reindexing.
     */
    public void indexTree() {
        if (keyToArchetype.isEmpty()) {
            indexRecursively(getRootOfOntology(), null, keyToArchetype);
        } else  {
            keyToArchetype = indexRecursively(getRootOfOntology(), null, new HashMap<>());
        }
    }


    /**
     * @param key An instance of key identifying an Archetype
     * @return null if key is not an Archetype, or the Archetype instance
     */
    protected abstract A matchArchetype(K key);

    private Map<K, A> indexRecursively(TreeNode<K> sourceNode, A archetype, Map<K, A> mapping) {
        K key = sourceNode.getData();
        A currentArchetype = matchArchetype(key);
        if (currentArchetype == null) currentArchetype = archetype;
        if (currentArchetype != null) mapping.put(key, currentArchetype);
        for (TreeNode<K> child : sourceNode.getChildren()) {
            indexRecursively(child, currentArchetype, mapping);
        }
        return mapping;
    }

    public A getArchetype(K key) {
        return keyToArchetype.get(key);
    }
    public P getStyleOf(K key) {
        return getArchetype(key).getVisualProperty();
    }

    public SortedMap<String, P> createLegend(Collection<K> facets) {
        return facets.stream()
                .map(this::getArchetype)
                .distinct()
                .collect(toMap(Archetype::getName, Archetype::getVisualProperty, (u,v) -> u, TreeMap::new));
    }
}
