package com.fantacg.common.pojo.video;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @Classname VideoPrice 视频价格表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_video_price")
public class VideoPrice implements Serializable {

    @Id
    private Long id;
    /**
     * 视频id
     */
    private String videoId;
    /**
     * 数量  1 天
     */
    private Integer number;
    /**
     * 类型 1时长 2次数 3永久
     */
    private Integer type;

    /**
     * 价格
     */
    private String price;

    /**
     * 时长类型（1-时/ 2-天/ 3-月 /4-年）
     */
    private Integer timeType;

    /**
     * 创建人 id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long inUserName;

    /**
     * 创建时间
     */
    private Date inDate;

    /**
     * 编辑人 id
     */
    private Long editUserName;

    /**
     * 编辑时间
     */
    private Date editDate;

    /**
     * 是否删除 1删除
     */
    private Integer isDel;


}
