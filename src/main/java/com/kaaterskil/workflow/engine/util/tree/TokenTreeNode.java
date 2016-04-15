package com.kaaterskil.workflow.engine.util.tree;

import java.util.Iterator;
import java.util.List;

import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class TokenTreeNode implements Iterable<TokenTreeNode> {

    private Token token;
    private TokenTreeNode parent;
    private List<TokenTreeNode> children;

    public TokenTreeNode(Token token) {
        this.token = token;
    }

    @Override
    public Iterator<TokenTreeNode> iterator() {
        return new TokenTreeIterator(this);
    }
    public TokenTreeIterator reverseIterator() {
        return new TokenTreeIterator(this, true);
    }

    /*---------- Getter/Setters ----------*/

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public TokenTreeNode getParent() {
        return parent;
    }

    public void setParent(TokenTreeNode parent) {
        this.parent = parent;
    }

    public List<TokenTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TokenTreeNode> children) {
        this.children = children;
    }

}
