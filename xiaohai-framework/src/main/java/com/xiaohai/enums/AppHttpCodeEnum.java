package com.xiaohai.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "该邮箱已被注册"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTENT_NOT_NULL(506, "内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传jpg,jpeg,png格式的图片"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "该昵称已被注册"),
    EMAIL_ERROR(513, "邮箱格式错误"),
    PASSWORD_ERROR(514, "密码格式错误"),
    NAME_EXIST_ERROR(515, "该标签名称已被注册"),
    NAME_USED_ERROR(516, "该标签名称正在被使用"),
    TAG_NOT_EXIST(517, "该标签不存在"),
    CATEGORY_EXIST(518, "分类名已存在"),
    LINK_EXISTED(519, "链接已存在"),
    LINK_NOT_FOUND(520, "未找到该友链"),
    DELETE_USER_ERROR(521, "删除用户失败"),
    CANNOT_SET_CHILD_AS_PARENT(522, "不能将子级设置为父级"),
    HAVE_MENU_REFERENCED(523, "有菜单正在被引用");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
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