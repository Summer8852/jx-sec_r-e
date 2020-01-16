package com.fantacg.video.mapper;

import com.fantacg.common.pojo.video.PlayDetail;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PlayDetailMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface PlayDetailMapper extends Mapper<PlayDetail> {

    Page<HashMap<String,Object>> selectPlayDetailList(HashMap<String, Object> params);

    /**
     * 查询播放次数
     * @param params
     * @return
     */
    Integer selectPlayDetailNumber(Map<String, Object> params);

    /**
     * 查询播放时长
     * @param params
     * @return
     */
    Date selectPlayDetailDate(Map<String, Object> params);
    /**
     * 减播放次数
     * @param params
     * @return
     */
    int updatePlayDetailNumber(Map<String, Object> params);

    /**
     * 播放次数完后修改状态
     * @param params
     * @return
     */
    int updatePlayDetailIsEnd(Map<String, Object> params);

    /**
     * 查询用户是否购买 并且在有效期内
     * @param params
     * @return
     */
    int selectMemberPlay(Map<String, Object> params);

}
