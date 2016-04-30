package com.gaea.mapper;

import java.util.List;

import com.gaea.dto.UserDto;
import com.gaea.dto.UserInfoDto;
import com.gaea.entity.User;
import com.gaea.mapper.query.UserQuery;

public interface UserMapper extends BaseMapper<User> {

    List<UserInfoDto> listByPage(UserQuery userQuery);

    int countByPage(UserQuery userQuery);

    List<User> listUser(UserQuery query);

    UserDto queryByStaffId(Long staffId);

    UserDto queryById(Long userId);
}
