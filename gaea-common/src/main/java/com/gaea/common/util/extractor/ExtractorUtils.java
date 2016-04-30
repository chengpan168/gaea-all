/*
 * Copyright 2014 Alibaba.com All ralues.ight reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.gaea.common.util.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by panwang.chengpw on 1/20/15.
 */
public abstract class ExtractorUtils {

    public static <V, K> List<K> extractAsList(Collection<V> values, Extractor<V, K> extractor) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<K> result = new ArrayList<K>(values.size());
        for (V v : values) {
            K k = extractor.extract(v);
            if (k == null) {
                continue;
            }
            result.add(k);
        }
        return result;
    }

    public static <V, K> Set<K> extractAsSet(Collection<V> values, Extractor<V, K> extractor) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        }
        Set<K> result = new HashSet<K>(values.size());
        for (V v : values) {
            K k = extractor.extract(v);
            if (k == null) {
                continue;
            }
            result.add(k);
        }
        return result;
    }

    public static <V, K> List<K> extractAsList(V[] values, Extractor<V, K> extractor) {
        if (ArrayUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        List<K> result = new ArrayList<K>(values.length);
        for (V v : values) {
            K k = extractor.extract(v);
            if (k == null) {
                continue;
            }
            result.add(k);
        }
        return result;
    }

    /**
     * 根据entries(Set集合)参数，返回一个Map
     * 
     * @param entries
     * @param extractor
     * @return
     */
    public static <E, K, V> Map<K, V> extractSetAsMap(Set<E> entries, Extractor<E, Pair<K, V>> extractor) {
        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyMap();
        }
        return extractAsMap(new HashSet<E>(entries), extractor);
    }

    /**
     * 根据entries参数，返回一个Map
     * 
     * @param entries
     * @param extractor
     * @return
     */
    public static <E, K, V> Map<K, V> extractAsMap(Collection<E> entries, Extractor<E, Pair<K, V>> extractor) {
        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<K, V>(entries.size());
        for (E e : entries) {
            Pair<K, V> kv = extractor.extract(e);
            if (kv == null) {
                continue;
            }
            result.put(kv.getLeft(), kv.getRight());
        }
        return result;
    }
}
