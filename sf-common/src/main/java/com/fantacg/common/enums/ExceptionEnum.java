package com.fantacg.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ExceptionEnum
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    VIDEO_ID_NULL(300, "视频id参数为空！"),
    VIDEO_PRICE_ID_NULL(301, "视频价格id参数为空！"),
    VIDEO_PRICE_COUNT(302, "视频价格属性已存在！"),
    VIDEO_NULL(304, "视频不存在！"),
    VIDEO_PRICE_NULL(305, "视频价格不存在！"),
    VIDEO_PRICE_DATA_INCOMPLETE(306, "视频价格属性信息不完整,请补全完整后上架"),
    VIDEO_NOT_FOUND(307, "视频未查询到"),
    VIDEO_TITLE_REPEAT(308, "视频考试题目重复"),
    VIDEO_TITLE_NULL(309, "试题题目不能为空"),

    NO_PREMISSION(400, "没有权限！"),
    File_TYPE_ERROR(401, "请选择附件类型！"),
    DATA_PARAM_TYPE_BIND_ERROR(403, "参数错误"),
    UPLOAD_IDCARD_SCAN_ERROR(20016, "图片上传不正确,请上正确的图片"),
    SYSTEM_INNER_ERROR(500, "系统打个盹，马上回来哦！"),

    ORDER_STATUS_EXCEPTION(501, "订单状态异常"),
    ORDER_PAY_STATUS_EXCEPTION(501, "订单状态异常"),
    CREATE_PAY_URL_ERROR(502, "常见支付链接异常"),
    WX_PAY_SIGN_INVALID(503, "微信支付签名异常"),
    WX_PAY_NOTIFY_PARAM_ERROR(504, "微信支付回调参数异常"),
    INVALID_FILE_FORMAT(505, "文件格式错误"),
    INVALID_FILE_NO_NULL(506, "文件不存在"),
    ORDER_NOT_EXCEPTION(508, "订单异常"),
    UPLOAD_IMAGE_EXCEPTION(507, "文件上传异常");
    int value;
    String message;

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }


}
