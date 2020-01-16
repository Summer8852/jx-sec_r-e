package com.fantacg.common.pojo.answer;

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
 * @Classname TrainingContent 工种培训表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_training_content")
public class TrainingContent implements Serializable {

    /**
     * 培训内容Id
     */
    @Id
    private Long contentId;
    /**
     *  培训内容
     */
    private String content;

    /**
     * 内容图片
     */
    private String contentImg;

    /**
     * 培训内容所属工种
     */
    private String workTypeId;
    /**
     * 是否删除 1删除 默认0
     */
    private String isDel;
    /**
     * 创建时间
     */
    private Date creationTime;

}
