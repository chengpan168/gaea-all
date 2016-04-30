package com.gaea.client.tree;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chengpanwang on 4/27/16.
 */
public class Tree {

    private TreeNode tree = new TreeNode();

    public Collection<String> getChildren(String name) {
        if(StringUtils.isBlank(name)) {
            return Collections.emptyList();
        }

        TreeNode child = null;
        TreeNode treeInner = tree;
        String[] split = StringUtils.split(name, ":");
        for (String nameInner : split) {
            if (StringUtils.isBlank(nameInner)) {
                continue;
            }

            child = treeInner.getChild(nameInner);
            treeInner = child;
        }

        if (child != null) {
            return child.getChildren().keySet();
        }

        return Collections.emptyList();
    }

    public void resolve(Collection<String> permissions) {

        if (CollectionUtils.isEmpty(permissions)) {
            return;
        }

        for (String permission : permissions) {
            String[] permissionSplit = StringUtils.split(permission, ":");

            if (ArrayUtils.isEmpty(permissionSplit)) {
                continue;
            }

            TreeNode treeInner = tree;
            for (String permissionInner : permissionSplit) {
                TreeNode child = treeInner.getChild(permissionInner, true);
                child.setName(permissionInner);
                treeInner = child;
            }
        }
    }
}
