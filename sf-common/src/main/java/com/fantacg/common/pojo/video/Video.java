package com.fantacg.common.pojo.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Video 视频基本信息表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_video_info")
public class Video implements Serializable {

    /**
     * 视频ID（对应阿里）
     */
    @Id
    private String id;

    /**
     * 视频标题
     */
//    @Length(min = 2, max = 30, message = "视频标题只能在2~30位之间",groups = {QpGroup.Add.class})
//    @NotEmpty(message = "不能为空",groups = {QpGroup.Add.class})
    private String title;

    /**
     * 分类id
     */
//    @NotEmpty(message = "分类id不能为空",groups = {QpGroup.Add.class})
    private String cateId;

    /**
     * 视频标签
     */
    private String tags;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 模板组ID
     */
    private String templateGroupId;

    /**
     * 是否上传成功
     */
    private Integer isUpload;

    /**
     * 是否转码成功
     */
    private Integer isSubmit;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     * 视频时长
     */
    private Integer videoTime;

    /**
     * 文件后缀名
     */
    private String suf;

    /**
     * 预览url
     */
    private String videoUrl;

    /**
     * 状态  0未上架  1上架
     */
    private Integer status;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
