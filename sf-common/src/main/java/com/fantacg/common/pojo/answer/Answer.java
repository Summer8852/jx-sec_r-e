package com.fantacg.common.pojo.answer;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Answer 培训试题表
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_answer")
public class Answer implements Serializable {

    @Id
    private Long answerId;

    /**
     * 视频Id
     */
    private String videoId;

    /**
     * 题目
     */
    @NotEmpty(message = "题目不能为空")
    private String problem;

    /**
     * 选项A
     */
    @NotEmpty(message = "选项A不能为空")
    private String A;
    /**
     * 选项B
     */
    @NotEmpty(message = "选项A不能为空")
    private String B;
    /**
     * 选项C
     */
    private String C;
    /**
     * 选项D
     */
    private String D;

    /**
     * 正确答案
     */
    @NotEmpty(message = "正确答案不能为空")
    private String rightKey;

    /**
     * 等级
     */
    private String levels;

    /**
     * 视频路径
     */
    private String mp4File;

    /**
     * 工人工种类型Id (对应工种见工种表)
     */
    @NotEmpty(message = "工种类型不能为空")
    private String workTypeId;


}
