package com.gaea.service;

import com.gaea.common.query.Pagination;
import com.gaea.dto.PermissionDto;
import com.gaea.dto.UrlDto;
import com.gaea.entity.Permission;
import com.gaea.entity.Url;
import com.gaea.mapper.query.PermissionQuery;
import com.gaea.mapper.query.UrlQuery;

/**
 * Created by tiantiea on 16/4/26.
 */
public interface UrlService {

    Pagination<UrlDto> listUrlPage(UrlQuery query);

    Url getById(Long id);

    int insert(Url url);

    int update(Url url);
}
