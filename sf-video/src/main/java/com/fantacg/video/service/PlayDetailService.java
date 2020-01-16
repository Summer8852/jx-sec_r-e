package com.fantacg.video.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.NumberCostant;
import com.fantacg.common.pojo.video.PlayDetail;
import com.fantacg.common.utils.Result;
import com.fantacg.common.vo.PageResult;
import com.fantacg.video.filter.LoginInterceptor;
import com.fantacg.video.mapper.PlayDetailMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PlayDetailService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class PlayDetailService {

    @Autowired
    PlayDetailMapper playDetailMapper;
    @Autowired
    VideoService videoService;

    /**
     * 查询购买视频的播放详情
     *
     * @param videoId
     * @return
     */
    public Result selectPlayDetailByVideo(String videoId) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        PlayDetail playDetail = new PlayDetail();
        playDetail.setVideoId(videoId);
        playDetail.setMemberId(memberId);
        playDetail.setIsEnd(0);
        PlayDetail playDetail1 = playDetailMapper.selectOne(playDetail);
        if (playDetail1 != null) {
            log.info("查询购买视频的播放详情:" + playDetail1);
            return Result.success(playDetail1);
        }
        return Result.failure("未购买该视频");
    }

    /**
     * 查询我的视频列表
     *
     * @param page
     * @param rows
     * @return
     */
    public Result selectPlayDetailList(Integer page, Integer rows, Integer isEnd) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        PageHelper.startPage(page, rows);
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        if (isEnd != -1) {
            params.put("isEnd", isEnd);
        }
        // 查询
        Page<HashMap<String, Object>> pageInfo = playDetailMapper.selectPlayDetailList(params);
        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    public List<PlayDetail> selectPlayDetailByType() {
        PlayDetail playDetail = new PlayDetail();
        playDetail.setType(1);
        playDetail.setIsEnd(0);
        List<PlayDetail> select = playDetailMapper.select(playDetail);
        return select;
    }


    /**
     * 修改状态未已结束
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePlayDetailIsEnd(String id) {
        PlayDetail playDetail = new PlayDetail();
        playDetail.setIsEnd(1);
        Example example = new Example(PlayDetail.class);
        example.createCriteria().andCondition(" id = " + id);
        playDetailMapper.updateByExampleSelective(playDetail, example);
    }

    /**
     * 修改播放次数
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePlayDetailNumber(Map<String, Object> params) {
        int i = playDetailMapper.updatePlayDetailNumber(params);
        if (i == 0) {
            playDetailMapper.updatePlayDetailIsEnd(params);
        }
    }


    /**
     * 删减播放次数
     *
     * @param id
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean getVideoPlayAuthResponse(String id, Long memberId, Integer type) {
        log.info("获取播放凭证：" + id + "---" + type);
        //判断购买次数的播放次数
        Map<String, Object> map = new HashMap<>();
        map.put("videoId", id);
        map.put("memberId", memberId);
        if (type == 1) {
            Date date = playDetailMapper.selectPlayDetailDate(map);
            long l = date.getTime() - System.currentTimeMillis();
            if (l <= 0) {
                playDetailMapper.updatePlayDetailIsEnd(map);
                return false;
            }
        }
        if (type == 2) {
            Integer i = playDetailMapper.selectPlayDetailNumber(map);
            if (i == 0) {
                playDetailMapper.updatePlayDetailIsEnd(map);
                return false;
            }
        }
        if (type == 3) {
            return true;
        }
        int i = playDetailMapper.updatePlayDetailNumber(map);
        if (i > 0) {
            return true;
        }
        return true;
    }

    /**
     * ResultCode.VIDEO_PLAY_ERROR
     * 判断视频是否还能播放 能播放 无法购买
     *
     * @param memberId
     * @param videoId
     * @return
     */
    public Result selectOne(Long memberId, String videoId) {

        PlayDetail playDetail = new PlayDetail();
        playDetail.setMemberId(memberId);
        playDetail.setVideoId(videoId);
        playDetail.setIsEnd(0);
        playDetail = playDetailMapper.selectOne(playDetail);
        if (playDetail != null) {
            if (NumberCostant.ONE.equals(playDetail.getType())) {
                if (playDetail.getEndTime().getTime() > System.currentTimeMillis()) {
                    return Result.success("false");
                }
            }
            if (NumberCostant.TWO.equals(playDetail.getType())) {
                if (playDetail.getNumber() > 0) {
                    return Result.success("false");
                }
            } else if (NumberCostant.THREE.equals(playDetail.getType())) {
                return Result.success("false");
            }
        }
        return Result.success("true");

    }

    /**
     * 添加视频播放信息
     *
     * @param id
     * @param orderNo
     * @param memberId
     * @param number
     * @param type
     * @param videoId
     * @param endTime
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean installPriceDetail(Long id, String orderNo, Long memberId, Integer number, Integer type, String videoId, Date endTime) {

        log.info("添加视频播放信息");

        //查询视频详情
        Result result = videoService.videoDetailById(videoId);
        HashMap<String, Object> videomap = (HashMap<String, Object>) result.getData();

        PlayDetail pd = new PlayDetail();
        pd.setId(String.valueOf(id));
        pd.setVideoId(videoId);
        pd.setTitle(String.valueOf(videomap.get("title")));
        pd.setCateId(String.valueOf(videomap.get("cateId")));
        pd.setTags(String.valueOf(videomap.get("tags")));
        pd.setDescription(String.valueOf(videomap.get("description")));
        pd.setCoverUrl(String.valueOf(videomap.get("coverUrl")));
        pd.setVideoTime((Integer) videomap.get("videoTime"));
        pd.setVideoUrl(String.valueOf(videomap.get("videoUrl")));
        pd.setOrderNo(orderNo);
        pd.setIsEnd(0);
        pd.setMemberId(memberId);
        pd.setNumber(number);
        pd.setType(type);
        pd.setEndTime(endTime);

        int i = playDetailMapper.insertSelective(pd);
        if (i > 0) {
            return true;
        }
        return false;
    }

}
