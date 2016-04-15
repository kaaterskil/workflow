package com.kaaterskil.workflow.engine.util.tree;

import java.util.Iterator;
import java.util.List;

public class TokenTree implements Iterable<TokenTreeNode> {

    private TokenTreeNode root;

    @Override
    public Iterator<TokenTreeNode> iterator() {
        return new TokenTreeIterator(this.getRoot());
    }

    public Iterator<TokenTreeNode> firstIterator() {
        return new TokenTreeIterator(getRoot(), true);
    }

    public TokenTreeNode getNode(Long tokenId) {
        return getNode(tokenId, root);
    }

    private TokenTreeNode getNode(Long tokenId, TokenTreeNode currentNode) {
        if (currentNode.getToken().getId().equals(tokenId)) {
            return currentNode;
        }

        final List<TokenTreeNode> children = currentNode.getChildren();
        if (currentNode.getChildren() != null && !children.isEmpty()) {
            int idx = 0;
            while (idx < children.size()) {
                final TokenTreeNode node = getNode(tokenId, children.get(idx));
                if (node != null) {
                    return node;
                }
                idx++;
            }
        }

        return null;
    }

    /*---------- Getter/Setters ----------*/

    public TokenTreeNode getRoot() {
        return root;
    }

    public void setRoot(TokenTreeNode root) {
        this.root = root;
    }
}
