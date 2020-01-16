package com.fantacg.common.pojo.video;

import com.fantacg.common.utils.QpGroup;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoCategory 视频分类管理
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Data
@Table(name = "tb_video_category")
public class VideoCategory implements Serializable {

    /**
     * 阿里云返回的分类id
     * 分类ID
     */
    @NotEmpty(message = "分类名不能为空", groups = {QpGroup.Del.class, QpGroup.Update.class})
    private Long id;

    /**
     * 类目名称
     */
    @NotEmpty(message = "分类名不能为空", groups = {QpGroup.Add.class, QpGroup.Update.class})
    private String name;

    /**
     * 父分类ID，若不填，则默认生成一级分类，根节点分类ID为-1
     * 父类目id,顶级类目填-1
     */
    private Long parentId;

    /**
     * 等级
     */
    private Long level;


    @Transient
    private List children;

    @Transient
    private Long value;

    @Transient
    private String label;


}
