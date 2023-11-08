package com.mh.Controller;

import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddLinkDto;
import com.mh.domain.dto.LinkDto;
import com.mh.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/8/1 10:10
 * @Version 1.0
 * @introduce   友联接口
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 分页查询所有友链-Admin
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        return linkService.getAllLinkByAdmin(pageNum, pageSize, linkDto);
    }
    /**
     * 添加友链
     * @param addLinkDto 添加链接dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    /**
     * 删除链接
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
    /**
     * 根据id查询友链
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkOneById(@PathVariable Long id){
        return linkService.getLinkOneById(id);
    }


    /**
     * 修改友链
     * @param linkDto
     * @return {@link ResponseResult}
     */
    @PutMapping()
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }
}
