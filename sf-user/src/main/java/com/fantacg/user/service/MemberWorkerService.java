package com.fantacg.user.service;

import com.alibaba.fastjson.JSON;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.pojo.user.MemberWorker;
import com.fantacg.common.pojo.user.Worker;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.MemberWorkerMapper;
import com.fantacg.user.mapper.WorkerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 *
 * @author 杜鹏飞
 * @Classname MemberWorkerService
 * @Created by Dupengfei 2019/12/18 16:03
 * @Version 2.0
 */
@Service
@Slf4j
public class MemberWorkerService {
    @Autowired
    MemberWorkerMapper memberWorkerMapper;
    @Autowired
    WorkerService workerService;
    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     * 登录查询关联账号
     *
     * @return
     */
    public Result queryMemberWorker() {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        String aaaaa = redisTemplate.opsForValue().get(KeyConstant.MEMBER_WORKERS + memberId);
        if (StringUtils.isNotEmpty(aaaaa)) {
            List<MemberWorker> memberWorkers = JSON.parseObject(aaaaa, List.class);
            return Result.success(memberWorkers);
        }

        Example example = new Example(MemberWorker.class);
        example.createCriteria()
                .andEqualTo("memberId", memberId)
                .andEqualTo("isDel", 0);
        List<MemberWorker> memberWorkers = this.memberWorkerMapper.selectByExample(example);
        redisTemplate.opsForValue().set(KeyConstant.MEMBER_WORKERS + memberId, JSON.toJSONString(memberWorkers));
        return Result.success(memberWorkers);

    }


    /**
     * 添加主账号与子账号关联
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addMemberWorker(MemberWorker memberWorker) {
        String account = "";
        String password = "";
        try {
            account = AesEncrypt.decryptAES(memberWorker.getAccount());
            password = AesEncrypt.decryptAES(memberWorker.getPassword());
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
                return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_PWD_ERROR);
            }
        } catch (Exception e) {
            return Result.failure(ResultCode.PARAMETER_ERROR);
        }

        //查询子账号是否存在
        Result result = this.workerService.workerByPhoneOrPwd(account, password);
        if (!result.getCode().equals(ResultCode.SUCCESS.code())) {
            return result;
        }
        Worker worker = JSON.parseObject(JSON.toJSONString(result.getData()), Worker.class);

        if (worker != null) {
            //查询是否已关联其他账号
            Example example = new Example(MemberWorker.class);
            example.createCriteria()
                    .andEqualTo("workerId", worker.getId())
                    .andEqualTo("isDel", 0);
            int count = this.memberWorkerMapper.selectCountByExample(example);
            if (count > 0) {
                return Result.failure("已关联其他账号,请解除已关联账号后重新关联！");
            }
            //关联子账号
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            memberWorker.setId(new IdWorker().nextId());
            memberWorker.setMemberId(memberId);
            memberWorker.setWorkerId(worker.getId());
            memberWorker.setWorkerPhone(account);
            memberWorker.setInDate(new Date());
            memberWorker.setIsDel(0);
            memberWorker.setIsAuth(0);
            count = this.memberWorkerMapper.insertSelective(memberWorker);
            if (count > 0) {
                redisTemplate.delete(KeyConstant.MEMBER_WORKERS + memberId);
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            } else {
                return Result.failure(ResultCode.DATA_ADD_ERROR);
            }
        } else {
            return Result.failure(ResultCode.LOGIN_NOT_ACCOUNT_ERROR);
        }

    }

    /**
     * @param memberWorker
     * @return
     */
    public Result delMemberWorker(MemberWorker memberWorker) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        memberWorker.setIsDel(1);
        memberWorker.setDelDate(new Date());
        memberWorker.setIsAuth(1);
        Example example = new Example(MemberWorker.class);
        example.createCriteria()
                .andEqualTo("id", memberWorker.getId())
                .andEqualTo("isDel", 0)
                .andEqualTo("memberId", memberId);
        int i = this.memberWorkerMapper.updateByExample(memberWorker, example);
        if (i > 0){
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }
}
