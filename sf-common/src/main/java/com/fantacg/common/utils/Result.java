package com.fantacg.common.utils;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Result
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    private Object data;

    private String dateTime;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setDateTime(DateUtil.now());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        result.setDateTime(DateUtil.now());
        return result;
    }

    public static Result success(ResultCode resultCode) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg(resultCode.message());
        result.setDateTime(DateUtil.now());
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setDateTime(DateUtil.now());
        return result;
    }

    public static Result failure(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SYSTEM_INNER_ERROR.code());
        result.setMsg(JSON.toJSONString(data));
        result.setDateTime(DateUtil.now());
        return result;
    }

    public static Result failure(String data) {
        Result result = new Result();
        result.setCode(ResultCode.SYSTEM_INNER_ERROR.code());
        result.setMsg(data);
        result.setDateTime(DateUtil.now());
        return result;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +'\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}

