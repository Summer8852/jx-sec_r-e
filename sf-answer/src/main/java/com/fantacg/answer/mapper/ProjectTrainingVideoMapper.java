package com.fantacg.answer.mapper;

import com.fantacg.common.pojo.answer.ProjectTrainingVideo;
import com.fantacg.common.vo.answer.VideoListVO;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 培训视频详情 DAO
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingVideoMapper 培训视频详情 DAO
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface ProjectTrainingVideoMapper extends Mapper<ProjectTrainingVideo> {

    /**
     * 查询工人项目培训观看视频
     * @param map
     * @return
     */
    List<VideoListVO> queryPTVByPTId(Map<String,Object> map);

    /**
     * 添加培训方案视频
     * @param list
     * @return
     */
    int insertProjectTrainingVideo(@Param("list") List<ProjectTrainingVideo> list);

}
