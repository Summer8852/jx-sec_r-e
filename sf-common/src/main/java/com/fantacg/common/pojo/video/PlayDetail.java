package com.fantacg.common.pojo.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PlayDetail 已购买视频播放详情
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_play_detail")
public class PlayDetail implements Serializable {


    /**
     * 主键id
     */
    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 分类id
     */
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
     * 视频封面地址
     */
    private String coverUrl;

    /**
     * 视频时长
     */
    private Integer videoTime;

    /**
     * 预览视频url
     */
    private String videoUrl;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 剩余数量
     */
    private Integer number;

    /**
     * 类型 1时长 2次数 3永久
     */
    private Integer type;

    /**
     * 是否到期
     */
    private Integer isEnd;
    /**
     * 订单id
     */
    private String orderNo;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;


}
