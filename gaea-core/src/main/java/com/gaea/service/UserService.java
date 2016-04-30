package com.gaea.service;

import com.gaea.common.query.Pagination;
import com.gaea.dto.UserDto;
import com.gaea.dto.UserInfoDto;
import com.gaea.entity.User;
import com.gaea.mapper.query.UserQuery;

import java.util.List;

/**
 * Created by chengpan on 2016/2/22.
 */
public interface UserService {

    Pagination<UserInfoDto> listUserPage(UserQuery query);

    User getById(Long id);

    int insert(User user);

    int update(User user);

    User queryByStaffId(Long staffId);

    List<User> listUser(UserQuery query);

    boolean login(UserDto userDto);

    UserDto queryDetail(String appName, Long staffId);
}
