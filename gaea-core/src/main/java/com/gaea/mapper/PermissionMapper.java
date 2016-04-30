package com.gaea.mapper;

import java.util.List;

import com.gaea.dto.PermissionDto;
import com.gaea.entity.Permission;
import com.gaea.mapper.query.PermissionQuery;

public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> listByStaffId(PermissionQuery query);

    List<String> listUrlPatternByStaffId(PermissionQuery query);

    List<PermissionDto> listByPage(PermissionQuery query);

    int countByPage(PermissionQuery query);

}
