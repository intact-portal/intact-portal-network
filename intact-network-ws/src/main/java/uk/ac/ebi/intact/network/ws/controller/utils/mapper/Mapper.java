package uk.ac.ebi.intact.network.ws.controller.utils.mapper;


public interface Mapper<K, P> {
    P getStyleOf(K key);
}
