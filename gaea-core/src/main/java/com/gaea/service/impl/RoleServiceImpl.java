package com.gaea.service.impl;

import com.gaea.common.query.Pagination;
import com.gaea.dto.RoleDto;
import com.gaea.entity.Role;
import com.gaea.mapper.RoleMapper;
import com.gaea.mapper.query.RoleQuery;
import com.gaea.service.RoleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by tiantiea on 16/4/22.
 */
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Pagination<RoleDto> listRolePage(RoleQuery query) {
        long count = roleMapper.countByPage(query);
        List<RoleDto> list = Collections.emptyList();
        if (count > 0) {
            list = roleMapper.listByPage(query);
            Map<Long, Role> roleMap = buildRoleMap(list);
            for (RoleDto tmp : list) {
                String pathName = "/";
                String[] ids = tmp.getPath().split("/");
                for (String id : ids) {
                    if (StringUtils.isNotBlank(id)) {
                        pathName += (roleMap.get(Long.valueOf(id)).getName() + "/");
                    }
                }
                tmp.setPathName(pathName);
            }
        }

        return new Pagination<RoleDto>(query, list, count);
    }

    @Override
    public Role getById(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Role role) {
        String path = "/";
        if (role.getParentId() != null) {
            Role parentRole = roleMapper.selectByPrimaryKey(role.getParentId());
            path = parentRole.getPath() + "/" + parentRole.getId();
        } else {
            role.setParentId(0L);
        }
        role.setPath(path);
        Date now = new Date();
        role.setModifyTime(now);
        role.setCreateTime(now);
        return roleMapper.insertSelective(role);
    }

    @Override
    public int update(Role role) {
        String path = "/";
        if (role.getParentId() != null) {
            Role parentRole = roleMapper.selectByPrimaryKey(role.getParentId());
            path = parentRole.getPath() + "/" + parentRole.getId();
        } else {
            role.setParentId(0L);
        }
        role.setPath(path);
        role.setModifyTime(new Date());
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public List<Role> listRole(RoleQuery query) {
        return roleMapper.listRole(query);
    }

    @Override
    public Role queryByName(String name) {
        return null;
    }


    private Map<Long, Role> buildRoleMap(List<RoleDto> list) {
        List<Long> roleIds = new ArrayList<>();
        Map<Long, Role> roleMap = new HashMap<>();
        for (RoleDto tmp : list) {
            String[] ids = tmp.getPath().split("/");
            for (String id : ids) {
                if (StringUtils.isNotBlank(id)) {
                    roleIds.add(Long.valueOf(id));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<Role> roleList = roleMapper.listRoleByIds(roleIds);
            for (Role tmp : roleList) {
                roleMap.put(tmp.getId(), tmp);
            }
        }
        return roleMap;
    }
}
