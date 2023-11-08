package com.mh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mh.constants.SystemConstants;
import com.mh.domain.ResponseResult;
import com.mh.domain.entity.Comment;
import com.mh.domain.vo.CommentVo;
import com.mh.domain.vo.PageVo;
import com.mh.enums.AppHttpCodeEnum;
import com.mh.exception.SystemException;
import com.mh.mapper.CommentMapper;
import com.mh.service.CommentService;
import com.mh.service.UserService;
import com.mh.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-07-19 21:58:35
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    //查询对应articleId根评论
    @Override
    public ResponseResult commentList(String commentType, Integer articleId, Integer pageNum, Integer pageSize) {
        //创建查询条件   查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据articleId查询文章(判断一下是文章评论时才需要这个，友联评论不需要这个)
//        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        //rootId为-1的根评论
        queryWrapper.eq(Comment::getRootId, -1);
        //倒序查询
        queryWrapper.orderByDesc(Comment::getCreateTime);   //按创建时间倒序

        //评论类型
        queryWrapper.eq(Comment::getType,commentType);

        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //将分页查询后的list进行vo封装(集合拷贝)
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        //封装返回
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
//        queryWrapper.orderByAsc(Comment::getCreateTime);   //按创建时间倒序
        queryWrapper.orderByDesc(Comment::getCreateTime);   //按创建时间倒序
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    /**
     *  list中的评论人名和子评论人名
     * @param list
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> list){
        //集合拷贝
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合,把评论人username放进去
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值（评论人的昵称，评论创建人的昵称）
            //当游客访问评论时（创建人是commentVo.getCreateBy()=-1），直接添加nickname为游客
//            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            String nickName = "";
//            System.out.println("commentVo.getCreateBy():" + commentVo.getCreateBy());
            if (commentVo.getCreateBy() > 0 && userService.getById(commentVo.getCreateBy())!=null){    //bug，当人被删除的时候，评论区中的立马变成观光游客
                nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            }else {
                nickName = "观光游客";
            }
            System.out.println("nickName: "+ nickName);
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询(只有有子评论才查询)
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }



    //发表评论接口
    @Override
    public ResponseResult addComment(Comment comment) {
        //因为创建了MyMetaObjectHandler，mybatisplus就可以自动填充，创建时间、人更新时间、人
        //评论内容不能为空（StringUtils：获取登录的token，这里不用前台传id，因为可能出现刷评论的问题）
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }
}

