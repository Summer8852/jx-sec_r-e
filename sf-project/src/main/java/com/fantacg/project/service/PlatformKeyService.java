package com.fantacg.project.service;

import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.project.Platform;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.project.mapper.PlatformKeyMapper;
import com.fantacg.project.mapper.PlatformMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname EquipmentService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class PlatformKeyService {

    @Autowired
    PlatformKeyMapper platformKeyMapper;
    @Autowired
    PlatformMapper platformMapper;

    /**
     * 添加 设备/平台
     * @param platform
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addPlatform(Platform platform) {
        //不为空时 判断是否重复添加
        if(StringUtil.isNotEmpty(platform.getPlatformNo())){
            Example example = new Example(Platform.class);
            example.createCriteria().andEqualTo("platformNo", platform.getPlatformNo());
            int count = platformMapper.selectCountByExample(example);
            if(count>0){
                return Result.failure("重复添加");
            }
        }
        //判断是否有设备编号
        if(StringUtil.isEmpty(platform.getPlatformNo())){
            platform.setPlatformNo(String.valueOf(new IdWorker().nextId()));
        }
        platform.setInDate(new Date());
        int insert = platformMapper.insert(platform);
        if(insert >0){
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.success(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 分页查询 设备/平台
     * @param page
     * @param rows
     * @return
     */
    public Result pagePlatform(Integer page, Integer rows) {
        // 开始分页 字段名1 ASC/DESC
        PageHelper.startPage(page, rows," in_date DESC ");
        Page<Platform> equipments = (Page<Platform>) this.platformMapper.selectAll();
        // 返回结果
        return Result.success(new PageResult<>(equipments.getTotal(), equipments));
    }

    /**
     * 删除 设备/平台
     * @param platform
     * @return
     */
    public Result delPlatform(Platform platform) {
        //判断是否已有对接账号及项目
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("platformNo", platform.getPlatformNo());
        int count = platformKeyMapper.selectCountByExample(example);
        if(count >0){
            return Result.failure("该平台存在账号对接，无法删除！");
        }

        int delete = platformMapper.delete(platform);
        if(delete >0){
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }


    /**
     * 生成数据对接唯一key(账号id,设备/设备id）
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addPlatformKey(PlatformKey platformKey) {
        //判断设备是否重复
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("isDel", 0)
                .andEqualTo("platformNo", platformKey.getPlatformNo())
                .andEqualTo("projectCode", platformKey.getProjectCode())
                .andEqualTo("accountId", platformKey.getAccountId());
        int i = this.platformKeyMapper.selectCountByExample(example);
        if (i > 0) {
            return Result.failure("设备已关联项目,请勿重复添加！");
        }
        String str = platformKey.getAccountId() +","+ platformKey.getPlatformNo() +","+ platformKey.getProjectCode();
        platformKey.setId(new IdWorker().nextId());
        platformKey.setAccessKey(AesEncrypt.encryptAES(str));
        platformKey.setInDate(new Date());
        platformKey.setIsDel(0);
        i = this.platformKeyMapper.insertSelective(platformKey);
        if (i > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_ADD_ERROR);
    }

    /**
     * 查询账号下的设备信息
     *weixin
     * @return
     */
    public Result queryPlatformKey(Integer page, Integer rows) {
        Example example = new Example(PlatformKey.class);
        example.createCriteria().andEqualTo("isDel", 0);
        // 开始分页
        PageHelper.startPage(page, rows);
        Page<PlatformKey> equipments = (Page<PlatformKey>) this.platformKeyMapper.selectByExample(example);
        // 返回结果
        return Result.success(new PageResult<>(equipments.getTotal(), equipments));
    }

    /**
     * 查询账号下的设备信息
     *
     * @param platformKey
     * @return
     */
    public Result queryPlatformKeyByAccount(PlatformKey platformKey) {
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("accountId", platformKey.getAccountId())
                .andEqualTo("isDel", 0);
        List<PlatformKey> equipments = this.platformKeyMapper.selectByExample(example);
        return Result.success(equipments);
    }

    /**
     * 删除
     *
     * @return
     */
    public Result delPlatformKeyByKey(PlatformKey platformKey) {
        //判断设备是否重复
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("accountId", platformKey.getAccountId())
                .andEqualTo("id", platformKey.getId());
        platformKey.setIsDel(1);
        int i = this.platformKeyMapper.updateByExampleSelective(platformKey, example);
        if (i > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }


    /**
     * 通过Key查询设备信息
     *
     * @param platformKey
     * @return
     */
    public PlatformKey queryPlatformKeyByKey(PlatformKey platformKey) {
        Example example = new Example(PlatformKey.class);
        example.createCriteria()
                .andEqualTo("accessKey", platformKey.getAccessKey())
                .andEqualTo("isDel", 0);
        return this.platformKeyMapper.selectOneByExample(example);
    }



}
