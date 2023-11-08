package com.mh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mh.domain.ResponseResult;
import com.mh.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-07-19 21:58:35
 */
public interface CommentService extends IService<Comment> {

    //查询对应articleId根评论
    ResponseResult commentList(String commentType, Integer articleId, Integer pageNum, Integer pageSize);
    //发表评论接口
    ResponseResult addComment(Comment comment);
}

