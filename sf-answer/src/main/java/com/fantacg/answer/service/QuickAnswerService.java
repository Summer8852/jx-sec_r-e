package com.fantacg.answer.service;

import com.fantacg.common.utils.Result;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname QuickAnswerService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public interface QuickAnswerService {


    /**
     * 查询全部
     * @return
     */
    Result queryQuickAnswerAll();

    /**
     *
     * @param integer
     * @return
     */
    Result queryQuickAnswerRandom(Integer integer);

}
