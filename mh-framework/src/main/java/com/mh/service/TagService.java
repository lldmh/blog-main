package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.TagByIdDto;
import com.mh.domain.dto.TagDto;
import com.mh.domain.dto.TagListDto;
import com.mh.domain.entity.Tag;
import com.mh.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-07-24 15:30:52
 */
public interface TagService extends IService<Tag> {
    // 查询标签列表
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);
    // 新增标签
    ResponseResult addTag(TagDto tagDto);
    //删除标签
    ResponseResult deleteTag(Integer id);
    //获取标签信息
    ResponseResult getTag(Long id);
    //修改标签
    ResponseResult updateTag(TagByIdDto tagByIdDto);
    //查询所有标签接口
    ResponseResult listAllTag();
}

