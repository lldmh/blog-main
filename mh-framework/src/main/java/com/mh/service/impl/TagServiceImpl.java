package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.TagByIdDto;
import com.mh.domain.dto.TagDto;
import com.mh.domain.dto.TagListDto;
import com.mh.domain.entity.Category;
import com.mh.domain.entity.Tag;
import com.mh.domain.vo.CategoryVo;
import com.mh.domain.vo.PageVo;
import com.mh.domain.vo.TagVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import com.mh.mapper.TagMapper;
import com.mh.service.TagService;
import com.mh.utils.BeanCopyUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-07-24 15:30:52
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    // 查询标签列表
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //创建查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        //比较
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        //去掉已删除的标签
        queryWrapper.eq(Tag::getDelFlag, 0);
        //分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    // 新增标签
    @Override
    public ResponseResult addTag(@RequestBody TagDto tagDto) {
        //当为空的时候，抛出异常
        if(Objects.isNull(tagDto)){
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        // 根据标签名判断标签是否存在
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Tag::getName, tagDto.getName());
        Tag getTag = getOne(queryWrapper);
        if (getTag != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_EXIST);
        }
        //实现bean拷贝
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        //保存标签
        save(tag);
        return ResponseResult.okResult();
    }

    //删除标签
    @Override
    public ResponseResult deleteTag(Integer id) {
        removeById(id);    //配置当中自动将DelFlag设置成1
        //返回
        return ResponseResult.okResult();
    }

    //获取标签信息
    @Override
    public ResponseResult getTag(Long id) {
        //查询条件
        Tag tag = getById(id);
        if(Objects.isNull(tag)){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_NO_EXIST);
        }
        //封装
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        //返回
        return ResponseResult.okResult(tagVo);
    }

    //修改标签
    @Override
    public ResponseResult updateTag(TagByIdDto tagByIdDto) {
        //判断数据是否为空
        if(!StringUtils.hasText(tagByIdDto.getName()) && !StringUtils.hasText(tagByIdDto.getRemark())){
            return ResponseResult.errorResult(AppHttpCodeEnum.CONTENT_IS_BLANK);
        }
        //将TagByIdDto对象转换为Tag对象
        Tag tag = BeanCopyUtils.copyBean(tagByIdDto , Tag.class);
        updateById(tag);
        //返回
        return ResponseResult.okResult();
    }

    //查询所有标签接口
    @Override
    public ResponseResult<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(queryWrapper);
        //拷贝bean
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);

        return ResponseResult.okResult(tagVos);
    }
}

