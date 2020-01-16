package com.fantacg.project.mapper;

import com.github.pagehelper.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PandectMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface PandectMapper {

    /**
     * 查询项目 班组 人员数量
     */
    Map<String,Object> queryProTmPwCount(@Param("memberId") Long memberId);

    /**
     * 查询实时培训数据
     * @param memberId
     * @return
     */
    Page<HashMap<String,Object>> queryPtList(@Param("memberId") Long memberId);

    /**
     * 查询 及格 不及格人数
     * @param memberId
     * @return
     */
    Map<String, Object> queryPassRate(@Param("memberId") Long memberId);

    /**
     * 查询一周内每天答题人数
     * @param memberId
     * @return
     */
    List<Map<String, Object>> queryWeekAnswerNum(@Param("memberId") Long memberId);

    /**
     * 查询每个工种占比
     * @param memberId
     * @return
     */
    List<Map<String, Long>> queryWorkerProportion(@Param("memberId") Long memberId);
}
