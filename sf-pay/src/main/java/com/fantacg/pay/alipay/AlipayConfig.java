package com.fantacg.pay.alipay;

/**
 * 类名：AlipayConfig
 * 功能：基础配置类
 * 详细：设置帐户有关信息及返回路径
 * 修改日期：2017-04-05
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *
 * @author Dupengfei
 */
public class AlipayConfig {

    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    public static final String app_id = "2019111969221640";

    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    // public static final String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBe8fQ9/JNvTMux18R9yaY9oDUe+YuOZQ4IMW0drxKvwaYG5CFhex0eW25Y/lN0Vcec9tAoDPqMafwJtlG+/k/ar1Uo38Asxwbq+w7MVe2rUvd/zXo0qGIdkoKLwNpFEZwZJU43KpEBfY3NNa0SXVWkylqF+DHbhBQV2e7EgApmZzCfdAUOAeQKdGF1c9HzdsMQziEWw3+7Uu5NvhIlh5cBn31XC/LzJ9DfyfDE1IdhmsUlytxvrcSCh538syHyrjG1uiy4JQfNyalWTmQXjvEHmIH2atRXT7gG5lihr0VbvMdNHWMYhX2XTDj0vNKZl718uTv0pav+XMqIqc6dzgbAgMBAAECggEBAJaEG6rF6FpvkCyY7q2ELiOayBgElslpA5Qycnr0T4B6YL/d2az1xT/K4v/w9RWDgBlxmaQJvLk4jeO15Rtik8Cw4b7BPeCmdXxVX32g4hJ7d+SoUCDiEsWc9/Y6IpYRLsbqiGdmypIGbUb/NPOlncGO1A3zq2qZhdlqVYji9PtmZnhj+z75HTAuYZ4LZqbEMCw60AWO0rOR0t8OUpNJ0paaSrSjueq/WoCvkioPprIMh/9rthqb5NIqHQZXwh9uVowLRCqsY1Eflw4flJuYdm/MjpeVQE17k/z1Fn1VUVjaFJZqm2vyN7ZPYF3yk7vHndXF/9NfpbonbLlcCzKMhXkCgYEA/nui6CGmXEBFkqjq9s0G8H9l4V0QbRok2jfVMXBx3DXx5+6SzxIK4iGZEk+2renOwu52gO/6r+a/eeR1+a6qqTCPEb5KxWnCHu8Bd+7y7yKMyesNZLQgWpcdUMYVDtFAUitD1B9rsrGGr8R1OK2VaTxrla6mRbBLmz5slJ4BaE0CgYEAwqMNuSZAUth81+OvmydiZodTgpDMqc876RpW2OXT4NqxBcPzgjSEBqdhDkRbzI8/zHFoek+fi9YKY70yCMkqUIAP+MoDEMlL9IRxcQsJUu0vKkGVq4Nfs7EGGXqv3GGE883EAyRbRaVNIj+xhAJlH+/KExZ0ZsMRARs72hax1gcCgYAieUEyTQyqRpKmMXfTAydDgR6eDutgvD/tx857qg3ilPQ2RR1iK8GJrJ992YqXuKqnwYIQKXI+TfX2XzqUHlNEJC3da3fZkmAXrKz4QEhsmLOVzt+7mqMfYoU4If1MSpB6ER5ZKACA7oeeW/C9QtaQsPyqBETGAAHd//DZmSX7qQKBgDa51/L2MWHZ4whkyYxrf3xqkm2EvzcDwMmjEUY9INz5QIrpdL9oerLQPzbeu50BriOSw5iYaLjNvvAaU0YrIc2upE8EpeYDY2rmDmuFeeKqM/fCpw2pi7AflyJ035usg9B1S6bCgzNK7iwyEnyWm85mAsBpMONv94kDGkoQ1YHjAoGAO9/4RnTqqyTHnbAmU2Jr1v5sAiiQjdBYQ1xQuso835JbMJq6DmmyfpfBqj21iuFbtCM+qFUKmT1iGotSyGLNND9aYsKhcv8joYsA4B9WnqU4Ez+iXA0jpNDJH92X9MAo1y9E9xzs7myGfBN1fNxfmKp4v7h7TKuWHDrWoYs7cSQ=";
    public static final String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCK5TTkOXXA5Vizbz0D2ZigjvbC9+KTU0qXH8SqB7ccghPWB4h1ZY1SIOaLgeelJlSQSUEgm1po+Fu88GMtoVnRUh8oyV9LVeGHmtBPbhonqHVCy/mwsLG1BXzXNIYci8FaBxNtnnl1nphRcc5vC1uHtzsO1NEm4XORIMBQXFM5RjahI46U1bNKOTSzGPtTG28flqxw/8w5ClcWESoBa6lmpSKqX/ldEEirOLLQy7S2Sg5UZ0zIfWO+BLSGJNCGbAkE0nu3Rkuewzb8TiDvAuwp1LWC8RZvpkkhA0rEYGSAwR+ZicCezAtGYYJBroHunAZSnNUXKrzLPQHP40tIJb4FAgMBAAECggEAKr/PHqtcxckRGvES23t+kXRPGMUI1IVfQNJDSn3jav/SMR7Sbieabnu32Zpa/Kamgl45kFqgYgyG18k5DVNK/LUE6B3KmbpOFtmcYcYmgKxFoxB6wlov5+JPgUEmQeT11sq0vRl4kq4EfZTEj8L0IfpQUAlKcmgTlMh6yIESEF9HGcbJmoLUs1og1S8dXTl8TGAiHbCKnHUViKNkE7s4Q1UOCkXh6QdMTC3V2MvfOxYwxUwOscqTHxA6MqLetb9+TE7CW4MFMusRDt3LanxPOKal6lU/pQd+t/lNw25L0+cdvsimlSSgJ9xSAQKawe451W4x5SADcW6sU4E0FYkXQQKBgQDzKoq11RMRrdK4mK1HKAKpJqdGmbfGAJBngyG8ox75dXicJ1Dns7OE0Hmz78Gb8IfaSazWfWDW1K5ZaQAmVZjFz66/uA7gTiSviTucNf8kuKyXMAk7eBbwUIrfQVl3Ngf20sW0PCQk1KcLolLfbP0z2bI7TSLSN3pGYAKSU3q1lQKBgQCSOdgloVvQmu4niS4UZBIdyVyqUFMMpHwX77lDeOJr6KDm0tAq6zQCMGL05UGvZUXHGsxH8i3JXZmA1a01SBYOp5c37QZYSoliqGDigl5jJ4W5QwOMZzfHY6p9wLt5BcvouUhNPdKQroO6CRVAMPdZKKRzZLQH88uyB8aIUATqsQKBgHQvtYtgPfZKic7uFI1VTsAn2fFf7XFMP7NV8r7BakFjobdrCbtMH9CsFBqnymiKKz9fyJfL3IOJp6zNAOdQKukJbMPqXCFM9TZyaesf2cNgAgdTSnYB0WQN9+zouqi9RlznJNQTkGmQuihS7SoYV3t43zJ6jtLG80PBCDmGbbFpAoGAHor4i/ulSvBhmF5+AoJYimI5+dUnU7ezt+g5wU8ptDJ2EWCtXJmeVmq28MJGMxWrOohtbQW37JyhaJi5QizZdpwo6OSYc661/aZ//jAO+RUTSHwn7JbOvtYhkXzFa+T7D5RL9t5X8iKRnzUwfCT+NWBY4UWCmTqiLICVRnm4XTECgYBeZng/dk7OPj5luLKV+06jaUfXlHbpZ/fJ0yFu/g7YkpS98+nb1N2b6pFzCLWaumltpyjxJ9jnYu8MMTNv5fTH5juTRYJLmD8AocsKaSbe0i9K9QBeFji1WdEB56BL6ahs4lMUbf9oH63t1lWq7xN306zSC1NmLThVEvLXDNip0A==";


    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    // public static final String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwXvH0PfyTb0zLsdfEfcmmPaA1HvmLjmUOCDFtHa8Sr8GmBuQhYXsdHltuWP5TdFXHnPbQKAz6jGn8CbZRvv5P2q9VKN/ALMcG6vsOzFXtq1L3f816NKhiHZKCi8DaRRGcGSVONyqRAX2NzTWtEl1VpMpahfgx24QUFdnuxIAKZmcwn3QFDgHkCnRhdXPR83bDEM4hFsN/u1LuTb4SJYeXAZ99Vwvy8yfQ38nwxNSHYZrFJcrcb63Egoed/LMh8q4xtbosuCUHzcmpVk5kF47xB5iB9mrUV0+4BuZYoa9FW7zHTR1jGIV9l0w49LzSmZe9fLk79KWr/lzKiKnOnc4GwIDAQAB";
    public static final String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiuU05Dl1wOVYs289A9mYoI72wvfik1NKlx/Eqge3HIIT1geIdWWNUiDmi4HnpSZUkElBIJtaaPhbvPBjLaFZ0VIfKMlfS1Xhh5rQT24aJ6h1Qsv5sLCxtQV81zSGHIvBWgcTbZ55dZ6YUXHObwtbh7c7DtTRJuFzkSDAUFxTOUY2oSOOlNWzSjk0sxj7UxtvH5ascP/MOQpXFhEqAWupZqUiql/5XRBIqziy0Mu0tkoOVGdMyH1jvgS0hiTQhmwJBNJ7t0ZLnsM2/E4g7wLsKdS1gvEWb6ZJIQNKxGBkgMEfmYnAnswLRmGCQa6B7pwGUpzVFyq8yz0Bz+NLSCW+BQIDAQAB";


    /**
     * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static final String notify_url = "https://sf.fatantacg.com/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public static final String return_url = "https://sf.fatantacg.com/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    /**
     * 签名方式
     */
    public static final String sign_type = "RSA2";

    /**
     * 字符编码格式
     */
    public static final String charset = "utf-8";

    /**
     * 支付宝网关
     */
    public static final String gatewayUrl = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝网关
     */
    public static final String log_path = "";


    /**
     * 销售产品码，与支付宝签约的产品码名称。 注：目前仅支持FAST_INSTANT_TRADE_PAY
     */
    public static final String pc_product_code = "FAST_INSTANT_TRADE_PAY";
    public static final String wap_product_code = "FAST_INSTANT_TRADE_PAY";

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
//    public static void logResult(String sWord) {
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
//            writer.write(sWord);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}

