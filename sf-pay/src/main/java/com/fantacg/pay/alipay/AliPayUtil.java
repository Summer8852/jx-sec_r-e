package com.fantacg.pay.alipay;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.FormatUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Dupengfei
 */
public class AliPayUtil {

    private static Log log = LogFactory.getLog(AliPayUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /***
     * H5
     * 请求阿里H5接口
     * @param model
     * @return
     */
    public String requestAliWapPay(AlipayTradeWapPayModel model) {
        String response = "";
        try {
            //用支付宝sdk生成公共请求参数
            DefaultAlipayClient client = this.defaultAlipayClient();
            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            // 设置异步通知地址
            request.setNotifyUrl(AlipayConfig.notify_url);
            //填充业务参数
            request.setBizModel(model);
            response = client.pageExecute(request).getBody();
            return response;
        } catch (Exception e) {
            log.error("生成支付需要请求的json数据失败");
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
//            return response;
        }
    }


    /***
     * pc
     * 请求阿里接口
     * @param model
     * @return
     */
    public AlipayTradePagePayResponse requestAliPcPay(AlipayTradePagePayModel model) {
        AlipayTradePagePayResponse response = new AlipayTradePagePayResponse();
        try {
            //用支付宝sdk生成公共请求参数
            DefaultAlipayClient client = this.defaultAlipayClient();
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            // 设置异步通知地址
            request.setNotifyUrl(AlipayConfig.notify_url);
            //支付完成后跳转页面
            request.setReturnUrl(AlipayConfig.return_url);
            //填充业务参数
            request.setBizModel(model);
            response  = client.pageExecute(request);
            return response;
        } catch (Exception e) {
            log.error("生成支付需要请求的json数据失败");
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /***
     * 校验签名
     * @return
     */
    public boolean checkSign(PublicResponsePojo publicResponsePojo) {

        Map<String, String> params = new HashMap<>();
        try {
            String s = objectMapper.writeValueAsString(publicResponsePojo);
            params = objectMapper.readValue(s, HashMap.class);
        } catch (Exception e) {

            log.error("将阿里响应参数转为map时出现错误");
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
        try {
            boolean b = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            return b;
        } catch (AlipayApiException e) {
            log.error("在对阿里返回参数进行签名校验时,出现错误");
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    private DefaultAlipayClient defaultAlipayClient(){
        DefaultAlipayClient client = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8, AlipayConfig.alipay_public_key,
                AlipayConstants.SIGN_TYPE_RSA2);
        return client;
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis() + (60 * 5 * 1000);
        Date date1 = new Date(l);
        String endTime = FormatUtils.formatTime(date1);
        System.out.println(endTime);
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo("12312354646789789");
        model.setProductCode(AlipayConfig.pc_product_code);
        model.setTotalAmount("0.01");
        model.setSubject("title");
        model.setTimeExpire(endTime);
        model.setBody("title");
        //生成请求接口的json数据
        AliPayUtil aliPayUtil = new AliPayUtil();
        AlipayTradePagePayResponse response = aliPayUtil.requestAliPcPay(model);
    }

}
