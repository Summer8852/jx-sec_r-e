package com.fantacg.common.pojo.video;

import lombok.Data;

import java.io.Serializable;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname TokenInfo  Token信息，业务方可提供更多信息，这里仅仅给出示例
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class TokenInfo implements Serializable {

    /**
     * Token的有效使用次数，分布式环境需要注意同步修改问题
     */
    int useCount;

    /**
     * token内容
     */
    String token;

}
