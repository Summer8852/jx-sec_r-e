package com.fantacg.user.service;

import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.dto.worker.WorkerDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.pojo.worker.PersonCredential;
import com.fantacg.common.pojo.worker.PersonRegisterInfo;
import com.fantacg.common.pojo.worker.PersonRegisterProfession;
import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.IdWorker;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.user.filter.LoginInterceptor;
import com.fantacg.user.mapper.PersonCredentialMapper;
import com.fantacg.user.mapper.PersonRegisterInfoMapper;
import com.fantacg.user.mapper.PersonRegisterProfessionMapper;
import com.fantacg.user.mapper.WorkerInfoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname WorkerInfoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class WorkerInfoService {

    @Value("${aliyun.idcard.appcode}")
    private String appcode;
    @Autowired
    private WorkerInfoMapper workerInfoMapper;
    @Autowired
    private PersonCredentialMapper personCredentialMapper;
    @Autowired
    private PersonRegisterInfoMapper personRegisterInfoMapper;
    @Autowired
    private PersonRegisterProfessionMapper personRegisterProfessionMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BirthPlaceService birthPlaceService;


    /**
     * 添加人员实人信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void addWorkerInfo(WorkerInfo workerInfo) {
        // 删除redis
        redisTemplate.delete(KeyConstant.WORKINFO_CARD_KEY_PREFIX + workerInfo.getIdCardNumber());
        // 获取籍贯
        String birthPlace = birthPlaceService.queryBirthPlaceById(workerInfo.getIdCardNumber());
        // 查询是否存在实人信息
        Example example = new Example(WorkerInfo.class);
        example.createCriteria()
                .andEqualTo("idCardNumber", workerInfo.getIdCardNumber());
        WorkerInfo info = workerInfoMapper.selectOneByExample(example);
        //  如果不存在工人信息
        if (info == null) {
            //  添加实人员信息
            workerInfo.setBirthPlaceCode(birthPlace);
            workerInfo.setId(new IdWorker().nextId());
            workerInfoMapper.insertSelective(workerInfo);
        } else {
            //  修改实人员信息
            workerInfo.setId(new IdWorker().nextId());
            workerInfoMapper.updateWorkerInfoByCard(workerInfo);
        }
    }

    /**
     * 添加人员资质信息
     *
     * @param workerDto
     * @return
     */
    public Result addWorkerDto(WorkerDto workerDto) {
        int num = 0;
        //判断人员资质数据是否为空 如不为空 则添加人员资质数据
        if (!workerDto.getPersonCredentials().isEmpty()) {
            for (PersonCredential personCredential : workerDto.getPersonCredentials()) {
                personCredential.setName(workerDto.getWorkerInfo().getName());
                personCredential.setIdCardType(workerDto.getWorkerInfo().getIdCardType());
                personCredential.setIdCardNumber(workerDto.getWorkerInfo().getIdCardNumber());
                num = personCredentialMapper.insertSelective(personCredential);
            }
        }
        //判断人员注册信息数据是否为空 如不为空 则添加人员注册信息数据
        if (!workerDto.getPersonRegisterInfos().isEmpty()) {
            for (PersonRegisterInfo personRegisterInfo : workerDto.getPersonRegisterInfos()) {
                personRegisterInfo.setPersonName(workerDto.getWorkerInfo().getName());
                personRegisterInfo.setIdCardType(workerDto.getWorkerInfo().getIdCardType());
                personRegisterInfo.setIdCardNumber(workerDto.getWorkerInfo().getIdCardNumber());
                num = personRegisterInfoMapper.insertSelective(personRegisterInfo);
            }
        }
        //判断人员注册专业数据是否为空 如不为空 则添加人员注册专业数据
        if (!workerDto.getPersonCredentials().isEmpty()) {
            for (PersonRegisterProfession personRegisterProfession : workerDto.getPersonRegisterProfessions()) {
                personRegisterProfession.setPersonName(workerDto.getWorkerInfo().getName());
                personRegisterProfession.setIdCardType(workerDto.getWorkerInfo().getIdCardType());
                personRegisterProfession.setIdCardNumber(workerDto.getWorkerInfo().getIdCardNumber());
                num = personRegisterProfessionMapper.insertSelective(personRegisterProfession);
            }
        }
        if (num > 0) {
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        }
        return Result.success(ResultCode.DATA_ADD_ERROR);
    }


    /**
     * 根据身份证查询实人信息 AES加密身份证号
     *
     * @param workerInfo {"cardNum":"fjqLxkF9w+NwJkw1rQDnmT47Oi/J2U2uxLJE2ysWEVw="}
     * @return
     */
    public Result queryWorkerInfoByCardNum(WorkerInfo workerInfo) {
        try {
            String cardNum = workerInfo.getCardNum();
            String s = null;
            s = redisTemplate.opsForValue().get(KeyConstant.WORKINFO_CARD_KEY_PREFIX + cardNum);
            if (StringUtil.isNotEmpty(s)) {
                workerInfo = objectMapper.readValue(s, WorkerInfo.class);
            } else {
                Example example = new Example(WorkerInfo.class);
                example.createCriteria()
                        .andEqualTo("idCardNumber", AesEncrypt.decryptAES(cardNum));
                workerInfo = this.workerInfoMapper.selectOneByExample(example);
                if (workerInfo != null) {
                    redisTemplate.opsForValue().set(KeyConstant.WORKINFO_CARD_KEY_PREFIX + cardNum, objectMapper.writeValueAsString(workerInfo));
                }
            }
            return Result.success(workerInfo);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 分页筛选人员信息
     *
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public Result queryWorkerByPage(Integer page, Integer rows, String sortBy, Boolean desc) {
        // 开始分页
        PageHelper.startPage(page, rows);
        Map<String, Object> params = new HashMap<>();

        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        params.put("memberId", memberId);

        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            params.put("orderByClause", orderByClause);
        }

        // 查询
        Page<WorkerInfo> pageInfo = (Page<WorkerInfo>) workerInfoMapper.selectWorkerInfoByPage(params);

        // 返回结果
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 批量导入人员信息
     *
     * @param inFile
     * @return
     */
//    public Result multSaveWorkerInfo(MultipartFile inFile) {
//        ArrayList list = new ArrayList();
//        try {
//            File file = null;
//            Path path = Paths.get(System.getProperty("java.io.tmpdir"), inFile.getName() + UUID.randomUUID() + "zip");
//            file = path.toFile();
//            FileUtils.copyInputStreamToFile(inFile.getInputStream(), file);
//            ZipFile zf = new ZipFile(file, Charset.defaultCharset());
//
//            ZipInputStream zin = new ZipInputStream(inFile.getInputStream());
//            ZipEntry ze;
//            while ((ze = zin.getNextEntry()) != null) {
//                if (ze.isDirectory()) {
//                } else {
//                    InputStream inputStream = zf.getInputStream(ze);
//                    Map front = .scanIdCard(inputStream, appcode, "front");
//                    list.add(front);
//                    // TODO: 2019/3/28
//                }
//            }
//            return Result.success(list);
//        } catch (IOException e) {
//            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
//			throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
//        }
//        return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
//    }


}
