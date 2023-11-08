package com.mh.constants;

/**
 * @Author dmh
 * @Date 2023/7/16 16:46
 * @Version 1.0
 * @introduce  实际项目中都不允许直接在代码中使用字面值。都需要定义成常量来使用。这种方式有利于提高代码的可维护性。
 */
public class SystemConstants {
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 分类状态分布正常(后台菜单分类也用到了)
     */
    public static final String STATUS_NORMAL = "0";
    /**
     * 友联状态分布正常
     */
    public static final String LINK_STATUS_NORMAL = "0";
    /**
     * 评论类型：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型：友联评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * redis中存的article:viewCount的浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article:viewCount";

    /**
     * 菜单：C菜单类型、F按钮类型
     */
    public static final String MENU = "C";
    public static final String BUTTON = "F";
    /**
     * 标签：未删除\删除
     */
    public static final Integer TAG_DELETE_FLAG_NO = 0;
    public static final Integer TAG_DELETE_FLAG_YES = 1;

    /** 正常状态 */
    public static final String NORMAL = "0";

    /**
     * 后台用户admin
     */
    public static final String ADMAIN = "1";

    public static final String LINK_ID = "id";
    public static final String LINK_STATUS = "status";
}
