package com.fantacg.common.pojo.video;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoMemberRelationship
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Table(name="tb_video_member_relationship")
@Data
public class VideoMemberRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    /**
     * 阿里的视频id
     */
    private String videoId;

    /**
     * 剩余点播次数
     */
    private Integer theRestTimes;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 购买时间
     */
    private Date buyTime;
}
