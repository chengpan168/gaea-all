package com.gaea.mapper;

import java.util.List;

import com.gaea.dto.UrlDto;
import com.gaea.entity.Url;
import com.gaea.mapper.query.UrlQuery;
import org.apache.ibatis.annotations.Param;

public interface UrlMapper extends BaseMapper<Url> {

    List<String> listByStaffId(@Param("appName") String appName, @Param("staffId") Long staffId);

    List<UrlDto> listByPage(UrlQuery query);

    int countByPage(UrlQuery query);

}
