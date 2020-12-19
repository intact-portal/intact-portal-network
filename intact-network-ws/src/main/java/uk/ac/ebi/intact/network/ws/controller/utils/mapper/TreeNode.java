package uk.ac.ebi.intact.network.ws.controller.utils.mapper;

import java.util.Collection;

public interface TreeNode<T> {
    Collection<TreeNode<T>> getChildren();
    T getData();
}
