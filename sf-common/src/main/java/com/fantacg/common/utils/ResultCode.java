package com.fantacg.common.utils;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname ResultCode API 统一返回状态码
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public enum ResultCode {

    SUCCESS(200, "请求成功"),
    LOGIN_TOKEN_EXPIRE_ERROR(10001, "登录过期,请重新登录"),
    LOGIN_NOT_ACCOUNT_ERROR(10002, "账号不存在,请先注册!"),
    LOGIN_NOT_ACCOUNT_PWD_ERROR(10003, "账号或密码错误,请重新输入账号或密码!"),
    LOGIN_PHONE_EXIST_ERROR(10004, "手机号已存在,请更换其他手机号注册!"),
    CODE_ERROR(10005, "验证码不正确或已过期！"),
    NO_ACCESS_ERROR(10006, "无访问权限"),
    PARAMETER_ERROR(10007, "参数错误!"),
    PARAMETER_NULL_ERROR(10008, "参数为空"),
    DATA_EXIST_ERROR(10009, "数据已存在"),
    DATA_NONE_RESULE(10010, "暂无数据"),
    DATA_ADD_SUCCESS(10011, "添加成功"),
    DATA_ADD_ERROR(10012, "添加失败"),
    DATA_UPDATE_SUCCESS(10013, "修改成功"),
    DATA_UPDATE_ERROR(10014, "修改失败"),
    DATA_DELETE_SUCCESS(10015, "删除成功"),
    DATA_DELETE_ERROR(10016, "删除失败"),
    DATA_FOUND_SUCCESS(10017, "创建成功"),
    DATA_FOUND_ERROR(10018, "创建失败"),
    DATA_DELETE_CATEGORY_CHILDREN_FIRST(10019, "数据存在子分类，无法删除"),
    DATA_DELETE_CATEGORY_VIDEO_FIRST(10020, "该分类存在视频，无法删除"),
    REGISTER_EXPIRE(10021, "注册失败"),
    ACCOUNT_NOT_AUTH(10022, "用户未认证"),
    ACCOUNT_HAS(20023, "用户已认证"),
    CODE_SMS_NOT_LOSE_EFFICACY(10024, "验证码发送频繁，请稍后发送！"),
    PHONE_CODE_NULL_ERROR(10025, "手机号或验证码为空"),
    PHONE_PWD_NULL_ERROR(10026, "手机号或密码为空"),
    EMAIL_NULL_ERROR(10027, "邮箱格式不正确或邮箱不存在"),
    PHONE_FORMAT_ERROR(10028, "手机格式不正确"),
    UPLOAD_IDCARD_SCAN_ERROR(10029, "请上传正确的身份证照片"),
    ACCOUNT_NOT_AUTH_REGISTER(10030, "邀请人手机号用户未认证，请重新填写邀请人手机号"),
    ACCOUNT_NOT_AUTH_EXIST(10031, "邀请账号不存在"),
    PHONE_EMAIL_FORMAT_ERROR(10032, "手机号或邮箱输入格式不正确"),
    USER_OLD_PASSWORD_ERROR(10033, "原密码错误"),
    EMAIL_HAS_REGISTER(10034, "邮箱已注册"),
    PHONE_HAS_REGISTER(10035, "手机已注册"),
    VIDEO_DATA_INCOMPLETE(10036, "视频信息不完整,请补全完整后上架"),
    VIDEO_PRICE_DATA_INCOMPLETE(10037, "视频价格属性信息不完整,请补全完整后上架"),
    VIDEO_TO_SHELF_SUCCESS(10038, "视频上架成功"),
    VIDEO_TO_SHELF_ERROR(10039, "视频上架失败"),
    VIDEO_NULL_ERROR(10040, "视频信息不存在，无法加入购物车"),
    VIDEO_PRICE_NULL_ERROR(10041, "视频价格信息不存在，无法加入购物车"),
    VIDEO_PLAY_ERROR(10042, "视频在播放时间或次数范围内,无需重新购买,请前往我的视频查看"),
    VIDEO_PLAY_AUTH_ERROR(10034, "视频播放次数或时间以用完，请重新购买获取播放权限！"),
    CORPCODE_ERROR(10044, "企业统一社会信用代码已存在"),
    CORPNAME_ERROR(10045, "企业名称已存在"),
    PROJECT_CODE_REPEAT_ERROR(10046, "项目编码已存在！"),
    PROJECT_NAME_NULL_ERROR(10047, "项目名称不能为空！"),
    WX_PAY_SIGN_INVALID(10048, "微信支付签名异常"),
    WX_PAY_NOTIFY_PARAM_ERROR(10049, "微信支付回调参数异常"),
    INVALID_FILE_FORMAT(10050, "文件格式错误"),
    UPLOAD_IMAGE_EXCEPTION(10051, "文件上传异常"),
    IDCODE_NULL_EXCEPTION(10052, "无法识别身份证信息，请将身份证放置身份证阅读器上!"),
    REPEAT_ANSWER_EXCEPTION(10053, "无法重复答题!"),
    INTERFACE_ADDRESS_INVALID(10054, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(10055, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(10056, "接口负载过高"),
    SYSTEM_INNER_ERROR(500, "系统出小差了，马上回来哦！");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode video : ResultCode.values()) {
            if (video.name().equals(name)) {
                return video.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode video : ResultCode.values()) {
            if (video.name().equals(name)) {
                return video.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
