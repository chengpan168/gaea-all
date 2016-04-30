package com.gaea.common.util.extractor;

/**
 * Created by panwang.chengpw on 1/20/15.
 */
public interface Extractor<S, T> {

    public T extract(S value);
}
