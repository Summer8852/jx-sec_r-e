package com.fantacg.project.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.project.filter.LoginInterceptor;
import com.fantacg.project.mapper.PandectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PandectService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class PandectService {

    @Autowired
    PandectMapper pandectMapper;

    /**
     * 查询 项目 班组 培训数
     *
     * @return 项目 班组 培训数
     */
    public Result queryProTmPwCount() {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        Map<String, Object> map = this.pandectMapper.queryProTmPwCount(memberId);
        log.info("项目 班组 培训数:" + map);
        return Result.success(map);
    }


    /**
     * 查询最新 答题列表
     *
     * @param page
     * @return 答题列表
     */
    public Result queryPtList(Integer page) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        PageHelper.startPage(0, page);
        // 查询
        Page<HashMap<String, Object>> pageInfo = this.pandectMapper.queryPtList(memberId);
        // 返回结果
        log.info("答题列表:" + pageInfo);
        return Result.success(pageInfo);
    }

    /**
     * 查询 及格 不及格人数
     *
     * @return 及格 不及格人数
     */
    public Result queryPassRate() {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        Map<String, Object> map = this.pandectMapper.queryPassRate(memberId);
        log.info("查询 及格 不及格人数:" + map);
        return Result.success(map);
    }

    /**
     * 查询一周内每天答题人数
     *
     * @return 一周内每天答题人数
     */
    public Result queryWeekAnswerNum() {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        List<Map<String, Object>> maps = this.pandectMapper.queryWeekAnswerNum(memberId);
        log.info("查询一周内每天答题人数:" + maps);
        return Result.success(maps);
    }

    /**
     * 查询每个工种占比
     *
     * @return 每个工种占比
     */
    public Result queryWorkerProportion() {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        List<Map<String, Long>> maps = this.pandectMapper.queryWorkerProportion(memberId);
        Collections.sort(maps, new Comparator<Map<String, Long>>() {
            @Override
            public int compare(Map<String, Long> o1, Map<String, Long> o2) {
                // TODO Auto-generated method stub
                return o2.get("num").compareTo(o1.get("num"));
            }
        });
        log.info("查询每个工种占比:" + maps);
        return Result.success(maps);
    }

}
