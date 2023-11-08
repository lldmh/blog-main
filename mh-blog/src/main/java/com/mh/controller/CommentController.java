package com.mh.controller;

import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.dto.AddCommentDto;
import com.mh.domain.entity.Comment;
import com.mh.service.CommentService;
import com.mh.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author dmh
 * @Date 2023/7/20 10:52
 * @Version 1.0
 * @introduce
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //查询对应articleId根评论
    @GetMapping("/commentList")
    public ResponseResult commentList(Integer articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);   //"0"代表type是文章评论
    }

    //发表评论接口
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        //将DTO转化为comment
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    //友联评论接口
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, pageSize); //"1"代表type是文章评论
    }
}
