package com.mh.enums;

/**
 * @Author dmh
 * @Date 2023/7/16 14:05
 * @Version 1.0
 * @introduce  返回状态枚举类
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CONTENT_NOT_NULL(506, "评论不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png图片"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    TAG_NOT_NULL(513, "标签不能为空"),
    TAG_IS_EXIST(514, "标签已存在"),
    TAG_NO_EXIST(515, "标签不存在"),
    CONTENT_IS_BLANK(516, "更新标签不能为空"),
    DELETE_ARTICLE_FAIL(517,"删除文章失败"),
    ADD_MENU_FAIL(518, "添加菜单失败"),
    CONTENT_MENU_IS_BLANK(516, "修改菜单'写博文'失败，上级菜单不能选择自己"),
    DELETE_MENU_REFUSE(516, "存在子菜单不允许删除"),
    ROLE_INFO_EXIST(517, "角色信息已存在"),
    ROLEKEY_INFO_EXIST(518, "角色值已存在"),
    DELETE_USER_REFUSE(519, "当前登录用户，不能删除"),
    CATEGORY_IS_EXIST(520, "类别已经存在"),
    DELETE_CATEGORY_FAIL(521, "删除文章类型失败"),
    LINK_IS_EXIST(522, "友联已经存在"),
    DELETE_LINK_FAIL(523, "删除友联失败")
    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
