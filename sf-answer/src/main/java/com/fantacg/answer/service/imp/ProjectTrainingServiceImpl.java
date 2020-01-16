package com.fantacg.answer.service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import com.alibaba.fastjson.JSON;
import com.fantacg.common.dto.answer.AddProjectTrainingDto;
import com.fantacg.common.dto.answer.ProjectTrainingDto;
import com.fantacg.common.dto.answer.PtDto;
import com.fantacg.answer.filter.LoginInterceptor;
import com.fantacg.answer.mapper.*;
import com.fantacg.common.dto.answer.SafetyEducation;
import com.fantacg.common.pojo.answer.*;
import com.fantacg.answer.service.AnswerService;
import com.fantacg.answer.service.ProjectTrainingService;
import com.fantacg.common.pojo.project.PlatformKey;
import com.fantacg.common.pojo.worker.WorkerInfo;
import com.fantacg.common.vo.answer.*;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ProjectTrainingServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class ProjectTrainingServiceImpl implements ProjectTrainingService {

    private static final String No = "001";
    @Autowired
    ProjectTrainingMapper projectTrainingMapper;
    @Autowired
    ProjectTrainingVideoMapper projectTrainingVideoMapper;
    @Autowired
    ProjectTrainingAnswerMapper projectTrainingAnswerMapper;
    @Autowired
    ProjectTrainingDetailMapper projectTrainingDetailMapper;
    @Autowired
    ProjectTrainingMemberMapper projectTrainingMemberMapper;
    @Autowired
    AnswerLogMapper answerLogMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AnswerService answerService;
    @Autowired
    ObjectMapper objectMapper;


    /**
     * 获取项目培训编号 yyyyMMddhhmm + memberId + 001递增
     *
     * @return
     */
    @Override
    public Result getSysNo() {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //查询当天最大值
        Long sysNo = projectTrainingMapper.getSysNo(memberId);
        if (sysNo == null) {
            sysNo = Long.valueOf(DateUtil.format(new Date(), "yyyyMMddhhmm") + No);
        } else {
            sysNo += 1L;
        }
        log.info("获取项目培训编号:" + sysNo);
        return Result.success(sysNo);
    }

    /**
     * 分页工人查询项目培训列表
     * trainingStatus 状态 -1删除 0未开始 1进行中 2结束
     * isTraining 0未答题 1已答题
     *
     * @param map {}
     * @return
     */
    @Override
    public Result queryWorkerTrainings(Map<String, Object> map) {
        try {
            log.info("分页工人查询项目培训列表" + map);
            //默认查询进行中的项目培训
            Integer trainingStatus = (Integer) map.get("trainingStatus");
            String cardNum = String.valueOf(map.get("cardNum"));
            Integer isTraining = (Integer) map.get("isTraining");
            if (StringUtil.isEmpty(cardNum)) {
                return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
            }
            if (isTraining == null) {
                map.put("isTraining", 0);
            }
            //解密
            map.put("cardNum", AesEncrypt.decryptAES(cardNum));
            //状态 -1删除 0未开始 1进行中 2结束
            if (trainingStatus == null) {
                map.put("trainingStatus", 1);
            }
            // 查询
            Page<ProjectTrainingVO> pageInfo = this.projectTrainingMapper.queryWorkerTrainings(map);
            return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 查询工人项目培训观看视频
     *
     * @return
     */
    @Override
    public Result queryWorkerTrainingVideos(PtDto dto) {
        try {
            log.info("查询工人项目培训观看视频" + dto);
            List<VideoListVO> videoList = this.videoList(dto.getProjectTrainingId(), dto.getTrainingSysNo());
            return Result.success(videoList);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 查询工人项目培训的试题
     *
     * @return
     */
    @Override
    public Result queryWorkerTrainingAnswers(PtDto dto) {
        try {
            log.info("查询工人项目培训的试题" + dto);

            if (StringUtils.isEmpty(dto.getIdCardNumber())) {
                return Result.failure("无身份证号，无法获取培训试题！");
            }

            //判断工人是否需要培训
            Example example = new Example(ProjectTrainingMember.class);
            example.createCriteria()
                    .andEqualTo("projectTrainingId", dto.getProjectTrainingId())
                    .andEqualTo("trainingSysNo", dto.getTrainingSysNo())
                    .andEqualTo("idCardNumber", AesEncrypt.decryptAES(dto.getIdCardNumber()))
                    .andEqualTo("isTraining", 0);
            int count = this.projectTrainingMemberMapper.selectCountByExample(example);

            if (count == 0) {
                return Result.failure("已完成答题或暂无法培训答题！");
            }

            //查询工人项目培训的试题
            List<Answer> answers = this.answerList(dto.getProjectTrainingId(), dto.getTrainingSysNo());
            return Result.success(answers);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 工人扫码 查询 项目培训的试题
     *
     * @param
     * @return
     */
    @Override
    public Result queryWorkerTrainingAnswersQR(PtDto dto) {
        try {
            log.info("工人扫码 查询 项目培训的试题" + dto);
            if (!IdcardUtil.isValidCard(dto.getIdCardNumber())) {
                return Result.failure("无效的身份证！");
            }

            //判断工人是否需要培训
            Example example = new Example(ProjectTrainingMember.class);
            example.createCriteria()
                    .andEqualTo("projectTrainingId", dto.getProjectTrainingId())
                    .andEqualTo("trainingSysNo", dto.getTrainingSysNo())
                    .andEqualTo("idCardNumber", AesEncrypt.decryptAES(dto.getIdCardNumber()))
                    .andEqualTo("isTraining", 0);
            int count = this.projectTrainingMemberMapper.selectCountByExample(example);
            if (count == 0) {
                return Result.failure("已完成答题或暂无法培训答题！");
            }
            //查询工人项目培训的试题
            List<Answer> answers = this.answerList(dto.getProjectTrainingId(), dto.getTrainingSysNo());
            return Result.success(answers);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 单个删除项目培训
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteProjectTraining(ProjectTrainingDto dto) {
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        dto.setMemberId(memberId);
        //删除培训列表
        int i = this.projectTrainingMapper.deleteProjectTraining(dto);

        if (i > 0) {
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        }

        return Result.failure(ResultCode.DATA_DELETE_ERROR);
    }

    /**
     * 结束项目培训考试
     * trainingStatus 状态 -1删除 0未开始 1进行中 2结束
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result stopProjectTraining(ProjectTrainingDto dto) {

        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        //trainingStatus 状态 -1删除 0未开始 1进行中 2结束
        dto.setTrainingStatus(2);
        dto.setMemberId(memberId);
        int i = this.projectTrainingMapper.stopProjectTraining(dto);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 查看项目培训考试详情
     *
     * @param dto
     * @return
     */
    @Override
    public Result detailProjectTraining(ProjectTrainingDto dto) {

        try {
            //获取用户id
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            //培训试题
            List<Answer> answers = this.answerList(dto.getId(), dto.getSysNo());
            //培训视频
            List<VideoListVO> videos = this.videoList(dto.getId(), dto.getSysNo());
            //培训成绩
            dto.setMemberId(memberId);
            List<ProjectTrainingDetailVO> details = this.projectTrainingDetailMapper.queryProjectTrainingDetails(dto);
            //查询培训附件
            List<FileAttachmentInfoVO> files = this.projectTrainingDetailMapper.queryFileAttachmentInfos(dto);
            ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
            map.put("answers", answers);
            map.put("videos", videos);
            map.put("details", details);
            map.put("file", files);
            return Result.success(map);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 提前开始项目培训培训
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result startProjectTraining(ProjectTrainingDto dto) {
        log.info("提前开始项目培训培训:" + dto);
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        dto.setMemberId(memberId);
        int i = this.projectTrainingMapper.startProjectTraining(dto);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.failure(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 自动结束培训考试
     *
     * @param trainingDate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voluntaryStopProjectTraining(String trainingDate) {
        this.projectTrainingMapper.voluntaryStopProjectTraining(trainingDate);
    }

    /**
     * 创建项目培训基本信息
     * 培训时间 和 培训名称 必填
     *
     * @param addProjectTrainingDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addProjectTraining(AddProjectTrainingDto addProjectTrainingDto) {

        log.info("创建培训项目:" + addProjectTrainingDto);
        int i = 0;
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

        //判断培训时间或名称是否为空
        String trainingName = addProjectTrainingDto.getPt().getTrainingName();
        Date trainingDate = addProjectTrainingDto.getPt().getTrainingDate();
        if (StringUtil.isEmpty(trainingName)) {
            return Result.failure("请填写培训名称！");
        }
        if (trainingDate == null) {
            return Result.failure("请填写培训日期！");
        }

        //添加项目培训基本信息
        addProjectTrainingDto.getPt().setId(new IdWorker().nextId());
        addProjectTrainingDto.getPt().setInUserName(memberId);
        addProjectTrainingDto.getPt().setInDate(new Date());
        i = this.projectTrainingMapper.insertSelective(addProjectTrainingDto.getPt());

        //添加项目培训视频信息
        if (!addProjectTrainingDto.getPtvs().isEmpty()) {
            List<ProjectTrainingVideo> projectTrainingVideos = new ArrayList<>();
            for (String videoId : addProjectTrainingDto.getPtvs()) {
                ProjectTrainingVideo projectTrainingVideo = new ProjectTrainingVideo();
                projectTrainingVideo.setProjectTrainingId(addProjectTrainingDto.getPt().getId());
                projectTrainingVideo.setTrainingSysNo(addProjectTrainingDto.getPt().getSysNo());
                projectTrainingVideo.setVideoId(videoId);
                projectTrainingVideo.setInUserName(memberId);
                projectTrainingVideo.setInDate(new Date());
                projectTrainingVideos.add(projectTrainingVideo);
            }
            i = this.projectTrainingVideoMapper.insertProjectTrainingVideo(projectTrainingVideos);
        }

        //添加项目培训题目信息
        if (!addProjectTrainingDto.getPtas().isEmpty()) {
            List<ProjectTrainingAnswer> projectTrainingAnswers = new ArrayList<>();
            for (Long answerId : addProjectTrainingDto.getPtas()) {
                ProjectTrainingAnswer projectTrainingAnswer = new ProjectTrainingAnswer();
                projectTrainingAnswer.setProjectTrainingId(addProjectTrainingDto.getPt().getId());
                projectTrainingAnswer.setTrainingSysNo(addProjectTrainingDto.getPt().getSysNo());
                projectTrainingAnswer.setAnswerId(answerId);
                projectTrainingAnswer.setInUserName(memberId);
                projectTrainingAnswer.setInDate(new Date());
                projectTrainingAnswers.add(projectTrainingAnswer);
            }
            i = this.projectTrainingAnswerMapper.insertProjectTrainingAnswer(projectTrainingAnswers);
        }

        //添加项目培训题目信息
        if (!addProjectTrainingDto.getPtm().isEmpty()) {
            for (ProjectTrainingMember ptm : addProjectTrainingDto.getPtm()) {
                ptm.setProjectTrainingId(addProjectTrainingDto.getPt().getId());
                ptm.setTrainingSysNo(addProjectTrainingDto.getPt().getSysNo());
                ptm.setIsTraining(0);
            }
            i = this.projectTrainingMemberMapper.insertProjectTrainingMember(addProjectTrainingDto.getPtm());
        }

        if (i > 0) {
            return Result.success(addProjectTrainingDto.getPt());
        }

        return Result.failure(ResultCode.DATA_FOUND_ERROR);
    }

    /**
     * 分页查询项目培训基本信息
     * @param page 分页
     * @param rows 分页
     * @param sortBy 排序
     * @param desc 排序
     * @param searchProjectCode 培训项目编码搜索
     * @param searchTrainingDate 培训日期搜索
     * @param searchTrainingName 课程名称搜索
     * @param trainingStatus 培训状态
     * @param searchTypeCode 培训类型搜索
     * @return
     */
    @Override
    public Result queryProjectTrainingByPage(Integer page, Integer rows, String sortBy, Boolean desc, String searchProjectCode, Date searchTrainingDate, String searchTrainingName, Integer trainingStatus, String searchTypeCode) {
        log.info("分页查询项目培训基本信息");
        //获取用户id
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        // 开始分页
        PageHelper.startPage(page, rows);
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        //培训项目编码搜索
        if (StringUtils.isNoneEmpty(searchProjectCode)) {
            params.put("searchProjectCode", searchProjectCode);
        }
        //培训日期搜索
        if (searchTrainingDate != null) {
            params.put("searchTrainingDate", searchTrainingDate);
        }
        //课程名称搜索
        if (StringUtils.isNoneEmpty(searchTrainingName)) {
            params.put("searchTrainingName", searchTrainingName);
        }
        //培训类型搜索
        if (StringUtils.isNoneEmpty(searchTypeCode)) {
            params.put("searchTypeCode", searchTypeCode);
        }
        //培训状态
        if (trainingStatus != null) {
            params.put("trainingStatus", trainingStatus);
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            params.put("orderByClause", orderByClause);
        }
        Page<ProjectTraining> pageInfo = projectTrainingMapper.selectAllProjectTrainingByPage(params);
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * （历史记录）分页查询项目培训记录
     *
     * @param dto
     * @return
     */
    @Override
    public Result queryWorkerTrainingDetailByCardNum(PtDto dto) {

        if (StringUtil.isEmpty(dto.getIdCardNumber())) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }

        if (dto.getPage() == null || dto.getRows() == null) {
            dto.setPage(1);
            dto.setRows(10);
        }
        // 开始分页
        PageHelper.startPage(dto.getPage(), dto.getRows());
        dto.setIdCardNumber(AesEncrypt.decryptAES(dto.getIdCardNumber()));
        // 查询
        Page<ProjectTrainingVO> pageInfo = this.projectTrainingMapper.queryWorkerTrainingDetailByCardNum(dto);
        log.info("历史记录）分页查询项目培训记录:" + pageInfo);
        return Result.success(new PageResult<>(pageInfo.getTotal(), pageInfo));
    }

    /**
     * 答题详情（已答题）
     *
     * @param dto
     * @return
     */
    @Override
    public Result queryProjectTrainingAnswerDetail(PtDto dto) {

        if (StringUtil.isEmpty(dto.getIdCardNumber())) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
        dto.setIdCardNumber(AesEncrypt.decryptAES(dto.getIdCardNumber()));
        HashMap<String, Object> map = this.projectTrainingMapper.queryProjectTrainingAnswerDetail(dto);
        if (map != null) {
            //查询题目正确答案
            String str = (String) map.get("exactness_answer");
            List<HashMap> maps = JSON.parseArray(str, HashMap.class);
            int size = maps.size();
            for (int i = 0; i < size; i++) {
                HashMap answerMap = maps.get(i);
                Iterator iterator = answerMap.keySet().iterator();
                String key = String.valueOf(iterator.next());
                Answer answer = answerService.queryAnswerById(Long.valueOf(key));
                String value = (String) answerMap.get(key);
                answerMap.put("value", value);
                answerMap.put("rightKey", answer.getRightKey());
            }

            map.put("exactness_answer", maps);
        }
        return Result.success(map);
    }

    /**
     * 自动删除培训 （保存30天）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voluntaryDelProjectTraining() {
        //查询 所有删除的
        LocalDateTime now = LocalDateTime.now().plus(30, ChronoUnit.DAYS);
        String delDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<ProjectTrainingDto> dtos = projectTrainingMapper.selectDelProjectTrainingByDelDate(delDate);
        if (!dtos.isEmpty()) {
            for (ProjectTrainingDto dto : dtos) {
                //删除培训
                //删除培训的视频
                Example example = new Example(ProjectTraining.class);
                example.createCriteria()
                        .andEqualTo("id", dto.getId())
                        .andEqualTo("sysNo", dto.getSysNo());
                this.projectTrainingVideoMapper.deleteByExample(example);

                //删除培训的视频
                example = new Example(ProjectTrainingVideo.class);
                example.createCriteria()
                        .andEqualTo("projectTrainingId", dto.getId())
                        .andEqualTo("trainingSysNo", dto.getSysNo());
                this.projectTrainingVideoMapper.deleteByExample(example);

                //删除培训的题目
                example = new Example(ProjectTrainingAnswer.class);
                example.createCriteria()
                        .andEqualTo("projectTrainingId", dto.getId())
                        .andEqualTo("trainingSysNo", dto.getSysNo());
                this.projectTrainingAnswerMapper.deleteByExample(example);

                //删除 需要培训人
                example = new Example(ProjectTrainingMember.class);
                example.createCriteria()
                        .andEqualTo("projectTrainingId", dto.getId())
                        .andEqualTo("trainingSysNo", dto.getSysNo());
                this.projectTrainingMemberMapper.deleteByExample(example);
            }

        }


    }


    /**
     * 题目列表
     *
     * @param projectTrainingId
     * @param trainingSysNo
     * @return
     * @throws IOException
     */
    private List<Answer> answerList(String projectTrainingId, String trainingSysNo) throws IOException {
        //查询redis 缓存数据
        String s = redisTemplate.opsForValue().get(KeyConstant.TRAINING_ANSWER_KEY_PREFIX + projectTrainingId);
        List<Answer> pta;
        //是否存在数据 存在则返回数据
        if (StringUtil.isNotEmpty(s)) {
            pta = objectMapper.readValue(s, List.class);
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("projectTrainingId", projectTrainingId);
            map.put("trainingSysNo", trainingSysNo);
            //查询数据库数据
            pta = this.projectTrainingAnswerMapper.queryPTAByPTId(map);
            //数据加入redis缓存
            if (null != pta) {
                redisTemplate.opsForValue().set(KeyConstant.TRAINING_ANSWER_KEY_PREFIX + projectTrainingId, objectMapper.writeValueAsString(pta));
            }
        }
        return pta;
    }


    /**
     * 视频列表
     *
     * @param projectTrainingId
     * @param trainingSysNo
     * @return
     * @throws IOException
     */
    private List<VideoListVO> videoList(String projectTrainingId, String trainingSysNo) throws IOException {
        //查询redis 缓存数据
        String s = redisTemplate.opsForValue().get(KeyConstant.TRAINING_VIDEO_KEY_PREFIX + projectTrainingId);
        List<VideoListVO> videoListVOS;
        //是否存在数据 存在则返回数据
        if (StringUtil.isNotEmpty(s)) {
            videoListVOS = objectMapper.readValue(s, List.class);
            return videoListVOS;
        }
        //查询数据库数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectTrainingId", projectTrainingId);
        map.put("trainingSysNo", trainingSysNo);
        videoListVOS = this.projectTrainingVideoMapper.queryPTVByPTId(map);
        //数据加入redis缓存
        if (null != videoListVOS) {
            redisTemplate.opsForValue().set(KeyConstant.TRAINING_VIDEO_KEY_PREFIX + projectTrainingId, objectMapper.writeValueAsString(videoListVOS));
        }
        return videoListVOS;
    }



    /**
     * 接收上报的安全教育数据
     * @param platformKey
     * @return
     */
    @Override
    public Result receiveSafetyEducation(PlatformKey platformKey) {
        String str = redisTemplate.opsForValue().get("safetyEducation:" + platformKey.getAccessKey());
        SafetyEducation safetyEducation = JSON.parseObject(str, SafetyEducation.class);

        ProjectTraining projectTraining = safetyEducation.getProjectTraining();
        List<String> videoIds = safetyEducation.getVideoIds();
        List<AnswerLog> answerLogs = safetyEducation.getAnswerLogs();
        List<Long> answerIds = safetyEducation.getAnswerIds();
        List<ProjectTrainingDetail> projectTrainingDetails = safetyEducation.getProjectTrainingDetails();
        List<WorkerInfo> workerInfos = safetyEducation.getWorkerInfos();
        if (videoIds == null || answerIds == null || workerInfos == null) {
            return Result.failure("数据不全无法上报！");
        }

        // 判断培训时间或名称是否为空
        Long memberId = platformKey.getAccountId();
        String projectCode = platformKey.getProjectCode();
        String trainingName = safetyEducation.getProjectTraining().getTrainingName();
        Date trainingDate = safetyEducation.getProjectTraining().getTrainingDate();
        Long sysNo = projectTraining.getSysNo();
        if (StringUtil.isEmpty(trainingName) || trainingDate == null || StringUtil.isEmpty(projectCode) || sysNo == null) {
            return Result.failure("培训时间或名称,数据不全无法上报！");
        }

        //判断数据是否上传 防止重复上传
        Example example = new Example(ProjectTraining.class);
        example.createCriteria().andEqualTo("sysNo", sysNo).andEqualTo("inUserName", memberId);
        int count = this.projectTrainingMapper.selectCountByExample(example);
        if (count > 0) {
            return Result.failure("数据已上报,请勿重复上传");
        }

        //添加人员信息
        for (WorkerInfo workerInfo : workerInfos) {
            workerInfo.setProjectCode(projectCode);
            workerInfo.setMemberId(memberId);
            redisTemplate.convertAndSend("workerInfo", JSON.toJSONString(workerInfo));
        }

        //添加项目培训基本信息
        projectTraining.setId(new IdWorker().nextId());
        projectTraining.setProjectCode(projectCode);
        projectTraining.setTrainingStatus(2);
        projectTraining.setSysNo(sysNo);
        projectTraining.setInUserName(memberId);
        projectTraining.setInDate(new Date());
        int i = this.projectTrainingMapper.insertSelective(projectTraining);

        //添加项目培训视频信息
        List<ProjectTrainingVideo> projectTrainingVideos = new ArrayList<>();
        for (String videoId : videoIds) {
            ProjectTrainingVideo projectTrainingVideo = new ProjectTrainingVideo();
            projectTrainingVideo.setId(new IdWorker().nextId());
            projectTrainingVideo.setProjectTrainingId(projectTraining.getId());
            projectTrainingVideo.setTrainingSysNo(sysNo);
            projectTrainingVideo.setVideoId(videoId);
            projectTrainingVideo.setInUserName(memberId);
            projectTrainingVideo.setInDate(new Date());
            projectTrainingVideos.add(projectTrainingVideo);
        }
        i = this.projectTrainingVideoMapper.insertProjectTrainingVideo(projectTrainingVideos);

        //添加项目培训题目信息
        List<ProjectTrainingAnswer> projectTrainingAnswers = new ArrayList<>();
        for (Long answerId : answerIds) {
            ProjectTrainingAnswer projectTrainingAnswer = new ProjectTrainingAnswer();
            projectTrainingAnswer.setId(new IdWorker().nextId());
            projectTrainingAnswer.setProjectTrainingId(projectTraining.getId());
            projectTrainingAnswer.setTrainingSysNo(sysNo);
            projectTrainingAnswer.setAnswerId(answerId);
            projectTrainingAnswer.setInUserName(memberId);
            projectTrainingAnswer.setInDate(new Date());
            projectTrainingAnswers.add(projectTrainingAnswer);
        }
        i = this.projectTrainingAnswerMapper.insertProjectTrainingAnswer(projectTrainingAnswers);

        //添加培训结果
        for (ProjectTrainingDetail projectTrainingDetail : projectTrainingDetails) {
            projectTrainingDetail.setId(new IdWorker().nextId());
            projectTrainingDetail.setProjectTrainingId(projectTraining.getId());
            projectTrainingDetail.setTrainingSysNo(sysNo);
            projectTrainingDetail.setInDate(new Date());
        }
        i = this.projectTrainingDetailMapper.insertProjectTrainingDetails(projectTrainingDetails);

        // 添加答题数据
        for (AnswerLog answerLog : answerLogs) {
            answerLog.setId(new IdWorker().nextId());
            answerLog.setProjectTrainingId(projectTraining.getId());
            answerLog.setTrainingSysNo(sysNo);
            answerLog.setExactnessAnswer(JSON.toJSONString(answerLog.getList()));
            answerLog.setInDate(new Date());
        }
        i = this.answerLogMapper.insertanswerLogs(answerLogs);

        if (i > 0) {
            return Result.success("数据上报成功！");
        } else {
            return Result.failure("数据上报失败！");
        }
    }



}
