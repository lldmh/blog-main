package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.TagByIdDto;
import com.mh.domain.dto.TagDto;
import com.mh.domain.dto.TagListDto;
import com.mh.domain.vo.PageVo;
import com.mh.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/7/26 10:49
 * @Version 1.0
 * @introduce  标签列表
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    // 查询标签列表
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    // 新增标签
    @PostMapping("")
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    //删除标签
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Integer id){
        return tagService.deleteTag(id);
    }

    //获取标签信息
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        return tagService.getTag(id);
    }

    //修改标签
    @PutMapping("")
    public ResponseResult updateTag(@RequestBody TagByIdDto tagByIdDto){
        return tagService.updateTag(tagByIdDto);
    }

    //查询所有标签接口
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

}
