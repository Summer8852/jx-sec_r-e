package com.fantacg.video.mapper;

import com.fantacg.common.pojo.video.VideoCategory;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoCategoryMapper
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Repository
public interface VideoCategoryMapper extends Mapper<VideoCategory> {


    List<HashMap<String,Object>> selectVideoCategoryList(Long parentId);

}
