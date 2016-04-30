package com.gaea.service.impl;

import com.gaea.common.query.Pagination;
import com.gaea.dto.UrlDto;
import com.gaea.entity.Url;
import com.gaea.mapper.UrlMapper;
import com.gaea.mapper.query.UrlQuery;
import com.gaea.service.UrlService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by tiantiea on 16/4/26.
 */
public class UrlServiceImpl implements UrlService {

    @Resource
    private UrlMapper urlMapper;
    
    @Override
    public Pagination<UrlDto> listUrlPage(UrlQuery query) {
        long count = urlMapper.countByPage(query);
        List<UrlDto> list = Collections.emptyList();
        if (count > 0) {
            list = urlMapper.listByPage(query);
        }

        return new Pagination<UrlDto>(query, list, count);
    }

    @Override
    public Url getById(Long id) {
        return urlMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Url url) {
        Date now = new Date();
        url.setModifyTime(now);
        url.setCreateTime(now);
        return urlMapper.insertSelective(url);
    }

    @Override
    public int update(Url url) {
        url.setModifyTime(new Date());
        return urlMapper.updateByPrimaryKeySelective(url);
    }
}
