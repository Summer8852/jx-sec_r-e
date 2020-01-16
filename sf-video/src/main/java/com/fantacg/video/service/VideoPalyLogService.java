package com.fantacg.video.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.video.Video;
import com.fantacg.common.pojo.video.VideoPalyLog;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.video.filter.LoginInterceptor;
import com.fantacg.video.mapper.VideoMapper;
import com.fantacg.video.mapper.VideoPalyLogMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname VideoPalyLogService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class VideoPalyLogService {

    @Autowired
    VideoPalyLogMapper videoPalyLogMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private VideoMapper videoMapper;

    /**
     * 添加播放记录
     *
     * @param videoPalyLog
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addVideoPalyLog(VideoPalyLog videoPalyLog) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        videoPalyLog.setId(new IdWorker().nextId());
        videoPalyLog.setInDate(new Date());
        videoPalyLog.setMemberId(memberId);
        videoPalyLog.setVideoId(JSON.toJSONString(videoPalyLog.getVideosId()));
        videoPalyLog.setIsConsume(0);
        // 添加播放记录
        int i = videoPalyLogMapper.insertSelective(videoPalyLog);
        if (i > 0) {
            return Result.success(ResultCode.SUCCESS);
        }
        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
    }


    /**
     * 分页查询 视频播放记录使用情况
     *
     * @param page 分页
     * @param rows 分页
     * @return
     */
    public Result queryVideoByPalyLogPage(Integer page, Integer rows) {

        PageHelper.startPage(page, rows);
        StringBuffer sb = new StringBuffer();
        Example example = new Example(Video.class);
        Claims loginClaims = LoginInterceptor.getLoginClaims();
        Long memberId = Long.valueOf((String) loginClaims.get("id"));
        sb.append("1=1");
        if (!loginClaims.get("roles").equals(RoleConstant.USER)) {
            sb.append(" AND member_id = " + memberId);
        }
        example.setOrderByClause("in_date DESC");
        example.createCriteria().andCondition(sb.toString());

        Page<VideoPalyLog> videoPalyLogs = (Page<VideoPalyLog>) videoPalyLogMapper.selectByExample(example);
        for (VideoPalyLog videoPalyLog : videoPalyLogs) {
            videoPalyLog.setSize(videoPalyLog.getSize() / 1024 / 1024);
        }
        return Result.success(new PageResult<>(videoPalyLogs.getTotal(), videoPalyLogs));
    }

    /**
     * 视频播放扣费
     */
    @Transactional(rollbackFor = Exception.class)
    public void videoPlayLogConsume(Integer num) {
        // 查询前10条数据 自动扣费
        PageHelper.startPage(1, num);
        Example example = new Example(Video.class);
        example.createCriteria().andCondition("is_consume = 0");
        Page<VideoPalyLog> videoPalyLogs = (Page<VideoPalyLog>) videoPalyLogMapper.selectByExample(example);
        if (!videoPalyLogs.isEmpty()) {
            for (VideoPalyLog videoPalyLog : videoPalyLogs) {
                //扣费

                //修改记录
                videoPalyLog.setIsConsume(1);
                videoPalyLog.setConsumeDate(new Date());
                videoPalyLogMapper.updateByPrimaryKey(videoPalyLog);
            }
        }


    }
}
