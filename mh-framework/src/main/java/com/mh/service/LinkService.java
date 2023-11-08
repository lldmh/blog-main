package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddLinkDto;
import com.mh.domain.dto.LinkDto;
import com.mh.domain.dto.LinkStatusDto;
import com.mh.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-07-17 19:28:08
 */
public interface LinkService extends IService<Link> {
    //查询所有友联
    ResponseResult getAllLink();
    //分页查询所有友链-Admin
    ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto);
    //添加友链
    ResponseResult addLink(AddLinkDto addLinkDto);
    //删除链接
    ResponseResult deleteLink(Long id);
    //根据id查询友链
    ResponseResult getLinkOneById(Long id);
    //修改友联
    ResponseResult updateLink(LinkDto linkDto);
    //更新友联状态
    ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto);
}

