package com.fantacg.project.service.data;

import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.utils.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname ReceiveService
 * @Created by Dupengfei 2020-01-16 14:37
 * @Version 2.0
 */
public interface ReceiveService {


    Result receiveSafetyEducation(SafetyEducation safetyEducation, HttpServletRequest request);
}
