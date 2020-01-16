package com.fantacg.video.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.common.pojo.video.VideoPrice;
import com.fantacg.video.mapper.VideoPriceMapper;
import com.fantacg.video.filter.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoPriceService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class VideoPriceService {


    @Autowired
    VideoPriceMapper videoPriceMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 添加视频价格属性
     * @param videoPrices
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveVideoPrice(List<VideoPrice> videoPrices){
            //获取用户id
            int i = 0;
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            if (!videoPrices.isEmpty()) {
                for (VideoPrice videoPrice : videoPrices) {
                    if (StringUtil.isEmpty(videoPrice.getVideoId())) {
                        log.error("添加视频价格属性异常：videoId 为空");
                        throw new JxException(ExceptionEnum.VIDEO_ID_NULL);
                    }
                    int count = videoPriceMapper.selectCount(videoPrice);
                    if (count > 0) {
                        throw new JxException(ExceptionEnum.VIDEO_PRICE_COUNT);
                    }
                    videoPrice.setInUserName(memberId);
                    videoPrice.setInDate(new Date());
                    i = videoPriceMapper.insertSelective(videoPrice);
                }
            }
            if (i > 0) {
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            }
            return Result.failure(ResultCode.DATA_ADD_ERROR);

    }

    /**
     * 修改视频价格属性
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateVideoPrice(List<VideoPrice> videoPrices) {
        //获取用户id
        int i = 0;
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        if (!videoPrices.isEmpty()) {
            for (VideoPrice videoPrice : videoPrices) {
                if (StringUtil.isEmpty(videoPrice.getVideoId())) {
                    throw new JxException(ExceptionEnum.VIDEO_ID_NULL);
                }
                if (videoPrice.getId() != null) {
                    videoPrice.setEditUserName(memberId);
                    videoPrice.setEditDate(new Date());
                    i = videoPriceMapper.updateVideoPrice(videoPrice);
                } else {
                    int count = videoPriceMapper.selectCount(videoPrice);
                    if (count > 0) {
                        throw new JxException(ExceptionEnum.VIDEO_PRICE_COUNT);
                    }
                    videoPrice.setInUserName(memberId);
                    videoPrice.setInDate(new Date());
                    i = videoPriceMapper.insertSelective(videoPrice);
                }
            }
        }

        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.success(ResultCode.DATA_ADD_ERROR);
    }




    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteVideoPrice(Long id) {

        if(id == 0){
            return Result.failure(ResultCode.PARAMETER_ERROR);
        }

        //获取用户id
        int i = 0;
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        VideoPrice  videoPrice = new VideoPrice();
        videoPrice.setEditDate(new Date());
        videoPrice.setEditUserName(memberId);
        videoPrice.setId(id);
        i = videoPriceMapper.deleteVideoPrice(videoPrice);

        if (i > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }


    /**
     * 查询视频价格详情
     *
     * @param videoId
     * @return
     */
    public Result queryVideoPriceByVideoIdList(String videoId) {
        List<HashMap<String, Object>> maps = this.videoPriceMapper.queryPriceByVideoId(videoId);
        return Result.success(maps);
    }

    public HashMap<String, Object> videoPriceDetail(Long id) {
        return this.videoPriceMapper.queryPriceById(id);
    }
}


