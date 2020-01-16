package com.fantacg.video.mapper;

import com.fantacg.common.pojo.video.VideoPrice;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoPriceMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface VideoPriceMapper extends Mapper<VideoPrice> {

    String queryMinPriceByVideoId(String videoId);

    List<HashMap<String, Object>> queryPriceByVideoId(String videoId);

    int updateVideoPrice(VideoPrice videoPrice);

    int deleteVideoPrice(VideoPrice videoPrice);

    HashMap<String,Object> queryPriceById(Long id);


}
