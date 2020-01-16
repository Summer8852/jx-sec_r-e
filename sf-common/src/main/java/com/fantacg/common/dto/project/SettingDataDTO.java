package com.fantacg.common.dto.project;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname SettingDataDTO 数据设置实体类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingDataDTO {

    /**
     * 账号id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long memberId;

    /**
     * 平台编号
     */
    private Long equipmentNum;

    /**
     *
     */

}
