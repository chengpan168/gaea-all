package com.gaea.service.impl;

import com.gaea.common.query.Pagination;
import com.gaea.dto.PermissionDto;
import com.gaea.entity.Permission;
import com.gaea.mapper.PermissionMapper;
import com.gaea.mapper.query.PermissionQuery;
import com.gaea.service.PermissionService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tiantiea on 16/4/26.
 */
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public Pagination<PermissionDto> listPermissionPage(PermissionQuery query) {
        long count = permissionMapper.countByPage(query);
        List<PermissionDto> list = Collections.emptyList();
        if (count > 0) {
            list = permissionMapper.listByPage(query);
        }

        return new Pagination<PermissionDto>(query, list, count);
    }

    @Override
    public Permission getById(Long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Permission permission) {
        Date now = new Date();
        permission.setModifyTime(now);
        permission.setCreateTime(now);
        return permissionMapper.insertSelective(permission);
    }

    @Override
    public int update(Permission permission) {
        permission.setModifyTime(new Date());
        return permissionMapper.updateByPrimaryKeySelective(permission);
    }
}
