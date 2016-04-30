package com.gaea.service;

import com.gaea.common.query.Pagination;
import com.gaea.dto.AppDto;
import com.gaea.entity.App;
import com.gaea.mapper.query.AppQuery;

import java.util.List;

/**
 * Created by chengpanwang on 4/21/16.
 */
public interface AppService {
    App queryByName(String appName);

    Pagination<AppDto> listAppPage(AppQuery appQuery);

    App getById(Long id);

    int insert(App app);

    int update(App app);

    List<App> listApp(AppQuery query);
}
