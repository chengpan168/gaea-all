package com.gaea.mapper;

/**
 * Created by panwang.chengpw on 1/19/15.
 */

public interface BaseMapper<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
