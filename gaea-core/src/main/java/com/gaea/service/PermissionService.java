package com.gaea.service;

import com.gaea.common.query.Pagination;
import com.gaea.dto.PermissionDto;
import com.gaea.entity.Permission;
import com.gaea.mapper.query.PermissionQuery;

/**
 * Created by tiantiea on 16/4/26.
 */
public interface PermissionService {

    Pagination<PermissionDto> listPermissionPage(PermissionQuery query);

    Permission getById(Long id);

    int insert(Permission permission);

    int update(Permission permission);

}
