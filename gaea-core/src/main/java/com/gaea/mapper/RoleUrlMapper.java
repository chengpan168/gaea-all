package com.gaea.mapper;

import com.gaea.entity.RoleUrl;

public interface RoleUrlMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleUrl record);

    int insertSelective(RoleUrl record);

    RoleUrl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleUrl record);

    int updateByPrimaryKey(RoleUrl record);
}