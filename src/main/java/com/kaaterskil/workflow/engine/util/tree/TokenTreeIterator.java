package com.kaaterskil.workflow.engine.util.tree;

import java.util.Iterator;
import java.util.LinkedList;

import com.kaaterskil.workflow.util.CollectionUtil;

public class TokenTreeIterator implements Iterator<TokenTreeNode> {

    private final TokenTreeNode rootNode;
    private final boolean reverseOrder;
    private LinkedList<TokenTreeNode> flattenedList;
    private Iterator<TokenTreeNode> flattenedListIterator;

    public TokenTreeIterator(TokenTreeNode rootNode) {
        this(rootNode, false);
    }

    public TokenTreeIterator(TokenTreeNode rootNode, boolean reverseOrder) {
        this.rootNode = rootNode;
        this.reverseOrder = reverseOrder;
    }

    @Override
    public boolean hasNext() {
        if (flattenedList == null) {
            flattenTree();
        }
        return flattenedListIterator.hasNext();
    }

    @Override
    public TokenTreeNode next() {
        if (flattenedList == null) {
            flattenTree();
        }
        return flattenedListIterator.next();
    }

    @Override
    public void remove() {
        if (flattenedList == null) {
            flattenTree();
        }
        flattenedListIterator.remove();
    }

    private void flattenTree() {
        flattenedList = new LinkedList<TokenTreeNode>();

        final LinkedList<TokenTreeNode> nodes = new LinkedList<>();
        nodes.add(rootNode);
        while (!nodes.isEmpty()) {
            final TokenTreeNode currentNode = nodes.pop();
            if (reverseOrder) {
                flattenedList.addFirst(currentNode);
            } else {
                flattenedList.add(currentNode);
            }

            if (CollectionUtil.isNotEmpty(currentNode.getChildren())) {
                for (final TokenTreeNode childNode : currentNode.getChildren()) {
                    nodes.add(childNode);
                }
            }
        }
        flattenedListIterator = flattenedList.iterator();
    }

}
