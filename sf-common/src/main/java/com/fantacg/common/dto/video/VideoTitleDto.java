package com.fantacg.common.dto.video;

import lombok.Data;

import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoTitleDto
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class VideoTitleDto {

    private String videoId;

    private List<Long> titleId;


}
