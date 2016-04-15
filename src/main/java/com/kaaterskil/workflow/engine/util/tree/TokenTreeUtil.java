package com.kaaterskil.workflow.engine.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class TokenTreeUtil {

    public static TokenTree buildTree(Collection<Token> executions) {
        final TokenTree tree = new TokenTree();

        final Map<Long, List<Token>> parents = new HashMap<>();
        for (final Token execution : executions) {
            final Long parentId = execution.getParentId();
            if (parentId != null) {
                if (!parents.containsKey(parentId)) {
                    parents.put(parentId, new ArrayList<Token>());
                }
                parents.get(parentId).add(execution);
            } else {
                tree.setRoot(new TokenTreeNode(execution));
            }
        }

        fillTree(tree, parents);
        return tree;
    }

    protected static void fillTree(TokenTree tree,
            Map<Long, List<Token>> parents) {
        if (tree.getRoot() == null) {
            throw new WorkflowException("Process instance execution is null");
        }

        final LinkedList<TokenTreeNode> executions = new LinkedList<>();
        executions.add(tree.getRoot());

        while (!executions.isEmpty()) {
            final TokenTreeNode parentNode = executions.pop();
            final Long parentId = parentNode.getToken().getId();
            if (parents.containsKey(parentId)) {
                final List<Token> children = parents.get(parentId);
                final List<TokenTreeNode> childNodes = new ArrayList<>(children.size());

                for (final Token each : children) {
                    final TokenTreeNode node = new TokenTreeNode(each);
                    node.setParent(parentNode);
                    childNodes.add(node);
                    executions.add(node);
                }
                parentNode.setChildren(childNodes);
            }
        }
    }
}
