package com.gaea.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gaea.mapper.query.PermissionQuery;
import com.gaea.mapper.query.RoleQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gaea.common.constant.Constant;
import com.gaea.common.query.Pagination;
import com.gaea.common.util.CryptoUtil;
import com.gaea.common.util.FastJson;
import com.gaea.common.util.Randoms;
import com.gaea.common.web.Cookies;
import com.gaea.dto.UserDto;
import com.gaea.dto.UserInfoDto;
import com.gaea.entity.App;
import com.gaea.entity.User;
import com.gaea.enums.UserStatusEnum;
import com.gaea.mapper.AppMapper;
import com.gaea.mapper.PermissionMapper;
import com.gaea.mapper.RoleMapper;
import com.gaea.mapper.UserMapper;
import com.gaea.mapper.query.UserQuery;
import com.gaea.service.UserService;
import com.gaea.service.helper.Authentication;
import com.gaea.service.helper.LoginHelper;

/**
 * Created by chengpan on 2016/2/22.
 */
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper       userMapper;
    @Resource
    private RoleMapper       roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private AppMapper        appMapper;
    @Resource
    private Authentication   authentication;

    @Override
    public Pagination<UserInfoDto> listUserPage(UserQuery query) {
        long count = userMapper.countByPage(query);
        List<UserInfoDto> list = Collections.emptyList();
        if (count > 0) {
            list = userMapper.listByPage(query);
        }

        return new Pagination<UserInfoDto>(query, list, count);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(User user) {
        Date now = new Date();
        user.setModifyTime(now);
        user.setCreateTime(now);
        return userMapper.insertSelective(user);
    }

    @Override
    public int update(User user) {
        user.setModifyTime(new Date());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User queryByStaffId(Long staffId) {
        return userMapper.queryByStaffId(staffId);
    }

    @Override
    public List<User> listUser(UserQuery query) {
        return userMapper.listUser(query);
    }

    @Override
    public boolean login(UserDto userDto) {

        // 从数据库验证用户
        UserDto existUser = userMapper.queryByStaffId(userDto.getStaffId());

        // 如果本地不正确, 从C# 验证, 并更新本地
        if (existUser == null || !StringUtils.equals(existUser.getPassword(), userDto.getPassword())) {
            String res = LoginHelper.login(authentication, userDto);
            if (StringUtils.isBlank(res)) {
                return false;
            }

            Map<String, Object> resMap = FastJson.fromJson(res);
            if (MapUtils.getIntValue(resMap, "code", -1) != 1) {
                return false;
            }

            // 更新数据库
            Map<String, Object> response = (Map<String, Object>) MapUtils.getMap(resMap, "response");
            String userName = MapUtils.getString(response, "username");
            userDto.setName(userName);

            if (existUser != null) {
                userDto.setId(existUser.getId());
                userMapper.updateByPrimaryKeySelective(userDto);
            }
        }

        if (existUser == null) {
            userMapper.insert(userDto);
        }

        String ticket = userDto.getTicket();
        if (StringUtils.isBlank(ticket)) {
            ticket = Cookies.getCookie(Constant.COOKIE_TICKET);
        }

        if (StringUtils.isBlank(ticket)) {
            ticket = Randoms.UUID();
            Cookies.setCookie(Constant.COOKIE_TICKET, ticket, -1);
        }

        userDto.setTicket(ticket);

        // 记住密码
        if (userDto.getRemember()) {
            Cookies.setCookie(Constant.COOKIE_NAME, String.valueOf(userDto.getStaffId()));
            Cookies.setCookie(Constant.COOKIE_PASS, CryptoUtil.encryptAES(userDto.getPassword()));

        }

        return true;
    }

    @Override
    public UserDto queryDetail(String appName, Long staffId) {
        UserDto userDto = userMapper.queryByStaffId(staffId);
        if (userDto == null || !UserStatusEnum.normal.getCode().equals(userDto.getStatus())) {
            return null;
        }

        // 如果是应用所有人, 则拥有所有权限
        App app = appMapper.queryByName(appName);
        if (ArrayUtils.contains(StringUtils.split(app.getOwnerId(), ","), String.valueOf(staffId))) {
            userDto.setUrlPatterns(Constant.ROOT_URI_PATTER);
            userDto.setPermissions(Constant.ROOT_PERMISSION);
            userDto.setRoles(roleMapper.listAllRole(appName));
            userDto.setOwner(true);
            return userDto;
        }

        RoleQuery query = new RoleQuery();
        query.setStaffId(staffId);
        List<String> roles = roleMapper.listByStaffId(query);

        PermissionQuery permissionQuery = new PermissionQuery();
        permissionQuery.setStaffId(staffId);
        permissionQuery.setAppName(appName);
        List<String> permissions = permissionMapper.listByStaffId(permissionQuery);
        List<String> urlPatterns = permissionMapper.listUrlPatternByStaffId(permissionQuery);

        userDto.setRoles(roles);
        userDto.setPermissions(permissions);
        userDto.setUrlPatterns(urlPatterns);
        userDto.setOwner(false);

        return userDto;
    }
}
