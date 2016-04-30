package com.gaea.service.impl;

import com.gaea.common.query.Pagination;
import com.gaea.common.util.Randoms;
import com.gaea.dto.AppDto;
import com.gaea.entity.App;
import com.gaea.mapper.AppMapper;
import com.gaea.mapper.query.AppQuery;
import com.gaea.service.AppService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tiantiea on 16/4/21.
 */
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public Pagination<AppDto> listAppPage(AppQuery appQuery) {

        long count = appMapper.countByPage(appQuery);
        List<AppDto> list = Collections.emptyList();
        if (count > 0) {
            list = appMapper.listByPage(appQuery);
        }

        return new Pagination<AppDto>(appQuery, list, count);
    }

    @Override
    public App getById(Long id) {
        return appMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(App app) {
        Date now = new Date();
        app.setModifyTime(now);
        app.setCreateTime(now);
        app.setAppKey(Randoms.UUID());
        return appMapper.insertSelective(app);
    }

    @Override
    public int update(App app) {
        Date now = new Date();
        app.setModifyTime(now);
        return appMapper.updateByPrimaryKeySelective(app);
    }

    @Override
    public App queryByName(String name) {
        return appMapper.queryByName(name);
    }

    @Override
    public List<App> listApp(AppQuery query) {
        return appMapper.listApp(query);
    }
}
