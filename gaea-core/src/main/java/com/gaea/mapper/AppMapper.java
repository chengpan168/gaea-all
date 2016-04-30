package com.gaea.mapper;

import com.gaea.dto.AppDto;
import com.gaea.entity.App;
import com.gaea.mapper.query.AppQuery;

import java.util.List;

public interface AppMapper {
    int deleteByPrimaryKey(Long id);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);

    List<AppDto> listByPage(AppQuery appQuery);

    int countByPage(AppQuery appQuery);

    App queryByName(String name);

    List<App> listApp(AppQuery appQuery);

}
