package com.fantacg.common.vo.answer;

import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.project.PlatformKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname ReceiveVO
 * @Created by Dupengfei 2020-01-16 14:29
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveVO {

    private SafetyEducation safetyEducation;

    private PlatformKey platformKey;
}
