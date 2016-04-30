package com.gaea.mapper;

import com.gaea.dto.RoleDto;
import com.gaea.entity.Role;
import com.gaea.mapper.query.RoleQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role>{

    List<String> listByStaffId(RoleQuery query);

    List<String> listAllRole(String appName);

    List<RoleDto> listByPage(RoleQuery query);

    int countByPage(RoleQuery query);

    List<Role> listRole(RoleQuery query);

    List<Role> listRoleByIds(List<Long> ids);

    Role queryByName(String name);

}
