package com.fantacg.common.dto.video;

import com.fantacg.common.utils.QpGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoDto
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
public class VideoDto {

    /**
     * 视频id
     */
    private String vidoeId;
    /**
     * 视频标题
     */
    @Length(min = 2, max = 30, message = "视频标题只能在2~30位之间",groups = {QpGroup.Add.class})
    @NotEmpty(message = "视频标题不能为空",groups = {QpGroup.Add.class})
    private String title;

    /**
     * 分类id
     */
    private String cateId;
    /**
     *视频标签
     */
//    @NotEmpty(message = "视频标签不能为空",groups = {QpGroup.Add.class})
    private String tags;

    /**
     *视频描述
     */
//    @NotEmpty(message = "视频描述不能为空",groups = {QpGroup.Add.class})
    private String description;

    /**
     *模板组ID
     */
    private String templateGroupId;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     *预览url
     */
    private String videoUrl;

    /**
     * 售价
     */
    private String videoPrices;

    /**
     * 时长
     */
    private Integer readVideoTime;
    /**
     * 后缀名
     */
    private String suf;
    /**
     * 管理员id
     */
    private Long userId;
}
