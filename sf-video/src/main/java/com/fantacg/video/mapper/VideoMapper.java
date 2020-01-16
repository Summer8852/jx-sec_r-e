package com.fantacg.video.mapper;

import com.fantacg.common.pojo.video.Video;
import com.fantacg.common.vo.video.VideoVo;
import com.github.pagehelper.Page;
import feign.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface VideoMapper extends Mapper<Video> {

    /**
     * 查询视频列表
     * @param params
     * @return
     */
    Page<VideoVo> selectAllVideoListByPage(Map<String, Object> params);

    /**
     * Id 查询视频详情
     * @param id 视频id
     * @return
     */
    VideoVo selectVideoDetailById(String id);

    /**
     * 查询我购买的视频
     * @param params
     * @return
     */
    List<Video> selectMyVideoList(Map<String, Object> params);

    /**
     * 分页查询我的视频
     * @param params
     * @return
     */
    List<Video> selectMyVideoListByPage(Map<String, Object> params);

    /**
     * 修改视频封面
     * @param id 视频id
     * @param coverURL 封面url
     * @return 返回是否修改
     */
    int updateVideoCoverUrl(@Param("id") String id, @Param("coverURL")String coverURL);

    /**
     * 查询分类推荐视频
     * @param params
     * @return
     */
    List<HashMap<String,Object>> selectVideoListRand(Map<String, Object> params);

    /**
     * 视频上架
     * @param videoIds
     * @return
     */
    int updateVideoStatus(List<String>videoIds);

    /**
     * 视频下架
     * @param videoIds
     * @return
     */
    int toUndercarriageVideo(List<String> videoIds);

    /**
     * 分类查询视频
     * @param cateId
     * @return
     */
    List<HashMap<String,Object>> queryVideoByCateId(Long cateId);

    /**
     * 查询一级分类所有视频
     * @param list
     * @return
     */
    List<HashMap<String,Object>> queryVideoByCateIds(List<Long> list);

    /**
     * 随机查询视频（首页）
     * @param num
     * @return
     */
    List<Video> queryVideoRandom(Integer num);
}
