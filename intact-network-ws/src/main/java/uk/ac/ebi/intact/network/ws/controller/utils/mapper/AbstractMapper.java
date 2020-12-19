package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import uk.ac.ebi.intact.network.ws.controller.utils.mapper.archetypes.Archetype;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.*;

/**
 * Map a key (K) to its closest archetype (A) thanks to an ontology tree
 *
 * @param <K> Key of mapping, faceted by solr and used as source for the mapping
 * @param <A> Archetype, an Enum describing specific nodes in an ontology to summarize all its closest children, and link them with their style
 */
public abstract class AbstractMapper<K, A extends Archetype<P>, P> {
    private final Map<K, A> keyToArchetype = new HashMap<>();

    protected abstract TreeNode<K> getRootOfOntology();

    public void indexTree() {
        indexRecursively(getRootOfOntology(), null);
    }


    /**
     * @param key An instance of key identifying an Archetype
     * @return null if key is not an Archetype, or the Archetype instance
     */
    protected abstract A matchArchetype(K key);

    private void indexRecursively(TreeNode<K> sourceNode, A archetype) {
        K key = sourceNode.getData();
        A currentArchetype = matchArchetype(key);
        if (currentArchetype == null) currentArchetype = archetype;
        if (currentArchetype != null) keyToArchetype.put(key, currentArchetype);
        for (TreeNode<K> child : sourceNode.getChildren()) {
            indexRecursively(child, currentArchetype);
        }
    }

    public A getArchetype(K key) {
        return keyToArchetype.get(key);
    }
    public P getStyleOf(K key) {
        return getArchetype(key).getVisualProperty();
    }

    protected Map<String, P> createLegend(Collection<K> facets) {
        return facets.stream()
                .map(this::getArchetype)
                .distinct()
                .collect(toMap(Archetype::getName, Archetype::getVisualProperty, (u,v) -> u, TreeMap::new));
    }
}
