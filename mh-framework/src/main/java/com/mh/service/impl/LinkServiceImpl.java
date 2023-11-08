package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddLinkDto;
import com.mh.domain.dto.LinkDto;
import com.mh.domain.dto.LinkStatusDto;
import com.mh.domain.entity.Link;
import com.mh.domain.vo.AdminLinkVo;
import com.mh.domain.vo.LinkVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.mapper.LinkMapper;
import com.mh.service.LinkService;
import com.mh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-07-17 19:28:08
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Autowired
    private LinkMapper linkMapper;

    //查询所有友联
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> vs = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(vs);
    }

    //分页查询所有友链-Admin
    @Override
    public ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        //        1.根据友链名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(linkDto.getStatus()), Link::getStatus, linkDto.getStatus());
        queryWrapper.like(StringUtils.hasText(linkDto.getName()), Link::getName, linkDto.getName());

//        2.分页查询
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);

//        3.将当前页中的Link对象转换为LinkVo对象
        List<Link> links = page.getRecords();
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
//        4.将LinkVo对象转换为LinkAdminVo对象
        AdminLinkVo adminLinkVo = new AdminLinkVo(linkVos, page.getTotal());
        return ResponseResult.okResult(adminLinkVo);
    }

    //添加友链
    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
//        1.首先根据友链名称查询要添加的友链是否存在
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Link::getName, addLinkDto.getName());
        Link link = getOne(queryWrapper);
        if (!Objects.isNull(link)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LINK_IS_EXIST);
        }
//        2.添加友链
//          2.1将AddLinkDto对象转为Link对象
        Link addLink = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(addLink);
        return ResponseResult.okResult();
    }

    //删除链接
    @Override
    public ResponseResult deleteLink(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_LINK_FAIL);
        }
        return ResponseResult.okResult();
    }

    //根据id查询友链
    @Override
    public ResponseResult getLinkOneById(Long id) {
//        1.根据id查询友链
        Link link = getById(id);
//        2.将Link对象封装为LinkVo对象
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    //修改友联
    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
//        1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(String.valueOf(linkDto.getStatus())) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getDescription())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
//        2.将LinkVo对象转换为Link对象
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    //更新友联状态
    @Override
    public ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto) {
//        1.根据友链id设置友链status
        UpdateWrapper<Link> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(SystemConstants.LINK_ID, linkStatusDto.getId());
        updateWrapper.set(SystemConstants.LINK_STATUS, linkStatusDto.getStatus());
        linkMapper.update(null, updateWrapper);
        return ResponseResult.okResult();
    }

}

