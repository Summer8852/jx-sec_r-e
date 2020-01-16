package com.fantacg.user.service;

import com.fantacg.common.pojo.user.Member;
import com.fantacg.common.pojo.user.RealNameAuthLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.Result;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.MemberMapper;
import com.fantacg.user.mapper.RealNameAuthLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PlayDetailApi
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
public class RealNameAuthLogService {

    @Autowired
    private RealNameAuthLogMapper realNameAuthLogMapper;
    @Autowired
    private MemberMapper memberMapper;

    public PageResult<RealNameAuthLog> queryRealNameApplyPage(Integer page, Integer rows, String sortBy, Boolean desc, String username, Integer status) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Map<String,Object> map=new HashMap<>();
        map.put("username",username);
        map.put("status",status);
        map.put("sortBy",sortBy + ( desc?" DESC ":" ASC "));
        // 查询
        Page<RealNameAuthLog> pageInfo = (Page<RealNameAuthLog>) realNameAuthLogMapper.queryRealNameApplyPage(map);
        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public Result getRealNameAuthLogDetail(Long id) {
        RealNameAuthLog realNameAuthLog=new RealNameAuthLog();
        realNameAuthLog.setId(id);
        realNameAuthLog = realNameAuthLogMapper.selectByPrimaryKey(realNameAuthLog);

        Member member=new Member();
        member.setId(realNameAuthLog.getMemberId());
        return Result.success(memberMapper.selectByPrimaryKey(member));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result checkRealNameAuth(RealNameAuthLog realNameAuthLog) {
        Long userId= ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        realNameAuthLog.setUserId(userId);
        realNameAuthLogMapper.updateByPrimaryKeySelective(realNameAuthLog);
        return Result.success();
    }

    public Result getMyRealNameAuthLog() {
        Long id= ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        Example e=new Example(RealNameAuthLog.class);
        e.createCriteria().andCondition(" member_id= " + id);
        List<RealNameAuthLog> realNameAuthLogs = realNameAuthLogMapper.selectByExample(e);
        //判断用户是否提交数据 未提交返回null
        if(CollectionUtils.isEmpty(realNameAuthLogs)){
            return Result.success();
        }

        return Result.success(realNameAuthLogs.get(0));
    }
}
