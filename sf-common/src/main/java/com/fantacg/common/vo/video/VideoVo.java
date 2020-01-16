package com.fantacg.common.vo.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 视频列表
 * @author Dupengfei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

    /**
     * 视频ID（对应阿里）
     */
    private String id;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 分类id
     */
    private String cateId;

    /**
     *视频标签
     */
    private String tags;

    /**
     *视频描述
     */
    private String description;

    /**
     *模板组ID
     */
    private String templateGroupId;

    /**
     *是否上传成功
     */
    private Integer isUpload;

    /**
     *是否转码成功
     */
    private Integer isSubmit;

    /**
     * 视频封面地址
     */
    private String coverUrl;

    /**
     *视频时长
     */
    private Integer videoTime;

    /**
     *文件后缀名
     */
    private String suf;

    /**
     *预览url
     */
    private String videoUrl;

    /**
     * 状态  0未上架  1上架
     */
    private Integer status;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     *最低价格
     */
    private String price;

    /**
     * 创建时间
     */
    private Date createTime;

    private List<HashMap<String, Object>> prices;
}
