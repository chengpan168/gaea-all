package com.gaea.client;

import com.gaea.client.tree.Tree;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chengpanwang on 4/22/16.
 */
public class test {

    @Test
    public void testAntPath() {

    }

    @Test
    public void testTree() {
        List<String> strings = Arrays.asList("city:3:1", "city:4");

        Tree tree = new Tree();
        tree.resolve(strings);
        System.out.println(tree.getChildren("city:3"));

    }

    @Test
    public void testPermission() {
        WildcardPermission permission = new WildcardPermission("a:b,c,d");

        System.out.println(permission.implies(new WildcardPermission("a:b")));
    }
}
