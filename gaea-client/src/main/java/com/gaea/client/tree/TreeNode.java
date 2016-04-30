package com.gaea.client.tree;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by chengpanwang on 4/27/16.
 */
public class TreeNode {

    private TreeNode              parent;
    private String                name;
    private Map<String, TreeNode> children = Maps.newHashMap();


    public TreeNode getChild(String key) {
        return children.get(key);
    }
    public TreeNode getChild(String key, boolean create) {
        TreeNode treeNode = children.get(key);
        if (treeNode == null) {
            treeNode = new TreeNode();
            treeNode.setParent(this);
            children.put(key, treeNode);
            return treeNode;
        }
        return treeNode;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }
}
