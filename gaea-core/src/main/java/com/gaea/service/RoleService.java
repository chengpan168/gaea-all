package com.gaea.service;

import com.gaea.common.query.Pagination;
import com.gaea.dto.RoleDto;
import com.gaea.entity.Role;
import com.gaea.mapper.query.RoleQuery;

import java.util.List;

/**
 * Created by tiantiea on 16/4/22.
 */
public interface RoleService {

    Pagination<RoleDto> listRolePage(RoleQuery query);

    Role getById(Long id);

    int insert(Role role);

    int update(Role role);

    List<Role> listRole(RoleQuery query);

    Role queryByName(String name);
}
