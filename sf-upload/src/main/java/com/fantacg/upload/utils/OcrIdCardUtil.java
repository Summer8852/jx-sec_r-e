package com.fantacg.upload.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20180916.CompareFacesRequest;
import com.aliyuncs.cloudauth.model.v20180916.CompareFacesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.HttpUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * 使用APPCODE进行云市场ocr服务接口调用
 *
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * 使用APPCODE进行云市场ocr服务接口调用
 * @author 智慧安全云
 * @Classname OcrIdCardUtil
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
public class OcrIdCardUtil {

    /**
     * 获取参数的json对象
     *
     * @param type
     * @param dataValue
     * @return
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
        return obj;
    }

    /**
     * 获取身份证信息
     *
     * @param inputStream
     * @param appcode
     * @param type
     * @return
     */
    public Map scanIdCard(InputStream inputStream, String appcode, String type) {
        //进入控制台 https://market.console.aliyun.com/imageconsole/index.htm?#/bizlist?_k=wkxk1i
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String method = "POST";
        String side = "face";
        if ("reverse".equals(type)) {
            side = "back";
        }
        Boolean is_old_format = false;
        JSONObject configObj = new JSONObject();
        configObj.put("side", side);
        String config_str = configObj.toString();

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();

        headers.put("Authorization", "APPCODE " + appcode);

        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if (is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, getImageStr(inputStream)));
                if (config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            } else {
                requestObj.put("image", getImageStr(inputStream));
                if (config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                log.error("Http code: " + stat);
                log.error("http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
                log.error("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return null;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if (is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
            } else {
                log.info("识别图片返回信息:" + res_obj);
            }
            return res_obj;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.UPLOAD_IDCARD_SCAN_ERROR);
        }

    }

    /**
     * 身份证对与图片对比
     * CompareFaces接口文档：https://help.aliyun.com/document_detail/59317.html
     *
     * @param facePic
     * @param idPic
     * @return
     */
    public static Result IAcsClient(String facePic, String idPic) {
        //创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        //创建API请求并设置参数
        CompareFacesRequest request = new CompareFacesRequest();
        request.setMethod(MethodType.POST);
        //用户人脸照 传入图片资料，请控制单张图片大小在 2M 内，避免拉取超时
        request.setSourceImageType("FacePic");
        //base64方式上传图片, 格式为"base64://图片base64字符串", 以"base64://"开头且图片base64字符串去掉头部描述(如"data:image/png;base64,"), 并注意控制接口请求的Body在8M以内
        request.setSourceImageValue("base64://" + facePic);
        //若为身份证芯片照则传 "IDPic"
        request.setTargetImageType("FacePic");
        //http方式上传图片, 此http地址须可公网访问
        request.setTargetImageValue(idPic);

        //发起请求并处理异常
        try {
            CompareFacesResponse response = client.getAcsResponse(request);
            if ("1".equals(response.getCode())) {
                log.info("相似度:" + response.getData().getSimilarityScore().intValue());
                return Result.success(response.getData().getSimilarityScore().intValue());
            } else {
                return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
            }
            //后续业务处理
        } catch (ServerException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.UPLOAD_IDCARD_SCAN_ERROR);
        } catch (ClientException e) {
            log.error("图片上传不正确,请上正确的图片");
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.UPLOAD_IDCARD_SCAN_ERROR);
        }
    }


    /**
     * 图片地址转换为base64编码字符串
     *
     * @param inputStream
     * @return
     */
    public static String getImageStr(InputStream inputStream) {
        byte[] bytes = new byte[1024];
        try {
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

}