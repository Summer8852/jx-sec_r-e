package com.fantacg.video.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.fantacg.common.auth.utils.JwtConstans;
import com.fantacg.common.auth.utils.ObjectUtils;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.pojo.PageResult;
import com.fantacg.common.utils.AesEncrypt;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.video.filter.LoginInterceptor;
import com.fantacg.video.mapper.PlayDetailMapper;
import com.fantacg.video.mapper.VideoCategoryMapper;
import com.fantacg.video.mapper.VideoMapper;
import com.fantacg.video.mapper.VideoPriceMapper;
import com.fantacg.common.pojo.video.Video;
import com.fantacg.common.pojo.video.VideoPrice;
import com.fantacg.video.utils.*;
import com.fantacg.common.vo.video.VideoVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class VideoService {

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    VideoPriceMapper videoPriceMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    VideoCategoryService videoCategoryService;
    @Autowired
    PlayDetailMapper playDetailMapper;
    @Autowired
    VideoCategoryMapper videoCategoryMapper;


    /**
     * 非AES生成方式，无需以下参数
     */
    private static String redisKey = "play:token:";


    /**
     * 视频中心
     *
     * @return 返回视频列表
     */
    public Result queryVideoIndexByPage(Integer page, Integer rows, String key, String searchCateId) {
        try {
            String redis = KeyConstant.INDEX_VIDEO_LIST + searchCateId + ":" + page + ":" + rows + ":";
            String s = redisTemplate.opsForValue().get(redis);
            //判断是都有数据
            if (StringUtil.isNotEmpty(s)) {
                PageResult<VideoVo> pageResult = objectMapper.readValue(s, PageResult.class);
                return Result.success(pageResult);
            } else {
                // 开始分页
                PageHelper.startPage(page, rows);
                Map<String, Object> params = new HashMap<>();
                params.put("searchCateId", searchCateId);
                Page<VideoVo> pageInfo = this.videoMapper.selectAllVideoListByPage(params);
                PageResult<VideoVo> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo);
                if (pageInfo.getTotal() != 0) {
                    redisTemplate.opsForValue().set(redis, objectMapper.writeValueAsString(pageResult));
                }
                return Result.success(pageResult);
            }

        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 分页查询视频列表
     *
     * @return 返回视频列表
     */
    public Result queryVideoByPage(Integer page, Integer rows, String key, String searchCateId) {
        try {
            PageHelper.startPage(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotBlank(key)) {
                params.put("title", "%" + key + "%");
            }
            if (StringUtils.isNotBlank(searchCateId)) {
                params.put("searchCateId", searchCateId);
            }
            Page<VideoVo> videoVos = videoMapper.selectAllVideoListByPage(params);
            Iterator<VideoVo> iterator = videoVos.iterator();
            while (iterator.hasNext()) {
                if (StringUtil.isEmpty(iterator.next().getCoverUrl())) {
                    //获取阿里云 视频信息 添加视频封面图
                    GetVideoInfoResponse videoInfo = VideoMediaUtil.getVideoInfo(iterator.next().getId());
                    HashMap<String, Object> coverMap = new HashMap<>();
                    coverMap.put("id", iterator.next().getId());
                    coverMap.put("coverUrl", videoInfo.getVideo().getCoverURL());
                    this.videoMapper.updateVideoCoverUrl(iterator.next().getId(), videoInfo.getVideo().getCoverURL());
                }
            }
            return Result.success(new PageResult<>(videoVos.getTotal(), videoVos));
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 创建项目培训时查询所有视频
     *
     * @return
     */
    public Result queryVideoList() {
        try {
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
            //父级分类
            List<HashMap> listvc;

            String s = redisTemplate.opsForValue().get(KeyConstant.TRAIN_VIDEO_LIST + memberId);
            if (StringUtil.isNotEmpty(s)) {
                listvc = objectMapper.readValue(s, List.class);
                return Result.success(listvc);
            }

            //查询父级分类
            Result result = videoCategoryService.queryCategoryListByParentId(-1L);
            listvc = (List<HashMap>) result.getData();
            Iterator<HashMap> iterator = listvc.iterator();
            while (iterator.hasNext()) {
                List<Long> longs = new ArrayList<>();
                HashMap next = iterator.next();
                longs.add(Long.valueOf(String.valueOf(next.get("id"))));
                //遍历children
                List<HashMap> childrens = (List<HashMap>) next.get("children");
                Iterator<HashMap> children = childrens.iterator();
                while (children.hasNext()) {
                    HashMap next1 = children.next();
                    longs.add(Long.valueOf(String.valueOf(next1.get("id"))));
                }

                List<HashMap<String, Object>> videos = videoMapper.queryVideoByCateIds(longs);
//                for (HashMap<String, Object> video : videos) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("videoId", video.get("videoId"));
//                    params.put("memberId", memberId);
//                    video.put("isPay", true);
//                }
                next.put("children", videos);
            }
            if (!listvc.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.TRAIN_VIDEO_LIST + memberId, JSON.toJSONString(listvc));
            }
            return Result.success(listvc);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 上传视频
     *
     * @param videoDto
     * @param videoFile
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result upload(VideoDto videoDto, MultipartFile videoFile, HttpServletRequest request) {
        log.info("上传视频：" + videoDto + "==" + videoFile + "==");
        if (StringUtils.isBlank(videoDto.getTemplateGroupId())) {
            videoDto.setTemplateGroupId(VideoConstant.templateGroupId);
        }
        // 任何上传方式文件名必须包含扩展名
        String fileName = "a.mp4";
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        String videoId = "";
        try {
            //获取用户id
            Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));

            //上传视频到阿里云
            videoId = UploadVideo.testUploadStream(VideoConstant.accessKeyId, VideoConstant.accessKeySecret, videoDto, fileName,
                    videoFile.getInputStream());
            log.info("阿里云视频Id:" + videoId);
            GetVideoInfoResponse videoInfo = VideoMediaUtil.getVideoInfo(videoId);
            log.info("阿里云视频:" + videoInfo.getVideo().getCoverURL());

            //创建临时文件，获取视频时长
            String originalFilename = videoFile.getOriginalFilename();
            // 图片原名.png
            String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            file = File.createTempFile(videoId, fileNameSuffix);
            inputStream = videoFile.getInputStream();
            byte[] buf = new byte[1024];
            int length = 0;
            fileOutputStream = new FileOutputStream(file);
            while ((length = inputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, length);
            }

            Video video = new Video();
            video.setId(videoId);
            video.setStatus(0);
            video.setTitle(videoDto.getTitle());
            video.setCateId(videoDto.getCateId());
            video.setTags(videoDto.getTags());
            video.setDescription(videoDto.getDescription());
            video.setTemplateGroupId(videoDto.getTemplateGroupId());
            video.setIsUpload(0);
            video.setVideoUrl(videoDto.getVideoUrl());
            video.setCoverUrl(videoInfo.getVideo().getCoverURL());

            //视频时间
            Long readVideoTime = new ReadVideoTime().readVideoTime(file);
            video.setVideoTime(readVideoTime.intValue());
            //文件后缀名
            video.setSuf(fileNameSuffix);
            //创建时间
            video.setCreateTime(new Date());
            videoMapper.insertSelective(video);
            if (StringUtil.isNotEmpty(videoDto.getVideoPrices())) {
                List<HashMap<String, Object>> videoPrices = objectMapper.readValue(videoDto.getVideoPrices(), List.class);
                for (HashMap<String, Object> videoPrice : videoPrices) {
                    //添加视频价格
                    VideoPrice price = new VideoPrice();
                    price.setVideoId(video.getId());
                    price.setNumber((Integer) videoPrice.get("number"));
                    price.setPrice(String.valueOf(videoPrice.get("price")));
                    price.setType((Integer) videoPrice.get("type"));
                    price.setTimeType((Integer) videoPrice.get("timeType"));
                    price.setInUserName(memberId);
                    price.setInDate(new Date());
                    videoPriceMapper.insertSelective(price);
                }
            }
            //创建培训视频
            redisTemplate.delete(redisTemplate.keys(KeyConstant.TRAIN_VIDEO_LIST + "*"));
            return Result.success(ResultCode.DATA_ADD_SUCCESS);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        } finally {
            //关闭资源
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                file.deleteOnExit();
            } catch (IOException e) {
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
        }
    }

    /**
     * 添加视频
     */
    @Transactional(rollbackFor = Exception.class)
    public int addVideo(VideoDto videoDto) {
        try {
            if (StringUtils.isBlank(videoDto.getTemplateGroupId())) {
                videoDto.setTemplateGroupId(VideoConstant.templateGroupId);
            }
            int status = 0;
            Video video = new Video();
            video.setId(videoDto.getVidoeId());
            video.setStatus(status);
            video.setTitle(videoDto.getTitle());
            video.setCateId(videoDto.getCateId());
            video.setTags(videoDto.getTags());
            video.setDescription(videoDto.getDescription());
            video.setTemplateGroupId(videoDto.getTemplateGroupId());
            video.setIsUpload(status);
            video.setVideoUrl(videoDto.getVideoUrl());
            video.setCoverUrl(videoDto.getCoverUrl());
            video.setVideoTime(videoDto.getReadVideoTime());
            video.setSuf(videoDto.getSuf());
            video.setCreateTime(new Date());
            videoMapper.insertSelective(video);
            if (StringUtil.isNotEmpty(videoDto.getVideoPrices())) {
                List<HashMap<String, Object>> videoPrices = objectMapper.readValue(videoDto.getVideoPrices(), List.class);
                for (HashMap<String, Object> videoPrice : videoPrices) {
                    //添加视频价格
                    VideoPrice price = new VideoPrice();
                    price.setVideoId(video.getId());
                    price.setNumber((Integer) videoPrice.get("number"));
                    price.setPrice(String.valueOf(videoPrice.get("price")));
                    price.setType((Integer) videoPrice.get("type"));
                    price.setTimeType((Integer) videoPrice.get("timeType"));
                    price.setInUserName(videoDto.getUserId());
                    price.setInDate(new Date());
                    videoPriceMapper.insertSelective(price);
                }
            }
            redisTemplate.delete(redisTemplate.keys(KeyConstant.INDEX_VIDEO_LIST + "*"));
            //创建培训视频
            redisTemplate.delete(redisTemplate.keys(KeyConstant.TRAIN_VIDEO_LIST + "*"));
            return 1;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }


    /**
     * 查询视频详情
     *
     * @param id 阿里云视频id
     * @return 返回视频详情（带视频价格）
     */
    public Result videoDetail(String id) {
        VideoVo vo = this.videoMapper.selectVideoDetailById(id);
        List<HashMap<String, Object>> prices = videoPriceMapper.queryPriceByVideoId(id);
        vo.setPrices(prices);
        return Result.success(vo);
    }

    /**
     * 查询视频详情
     *
     * @param id 阿里云视频id
     * @return 返回视频详情 （返回视频价格）
     */
    public Result videoDetailById(String id) {
        VideoVo vo = this.videoMapper.selectVideoDetailById(id);
        return Result.success(vo);
    }

    /**
     * 上架视频
     *
     * @param videoIds 阿里云视频id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result toShelfVideo(List<String> videoIds) {
        //模糊删除redis换存
        redisTemplate.delete(redisTemplate.keys(KeyConstant.INDEX_VIDEO_LIST + "*"));
        //创建培训视频
        redisTemplate.delete(redisTemplate.keys(KeyConstant.TRAIN_VIDEO_LIST + "*"));
        int i = 0;
        for (String videoId : videoIds) {
            Video video = new Video();
            video.setId(videoId);
            video = videoMapper.selectOne(video);
            if (StringUtil.isEmpty(video.getTitle()) && StringUtil.isEmpty(video.getCateId()) && StringUtil.isEmpty(video.getVideoUrl())) {
                return Result.failure(ResultCode.VIDEO_DATA_INCOMPLETE);
            }
            List<HashMap<String, Object>> maps = videoPriceMapper.queryPriceByVideoId(videoId);
            for (HashMap<String, Object> map : maps) {
                Integer number = (Integer) map.get("number");
                Integer type = (Integer) map.get("type");
                Integer timeType = (Integer) map.get("timeType");
                String price = (String) map.get("price");
                //1 时长 2 次数 3永久
                if (type == 1) {
                    if (number == null || timeType == null || StringUtil.isEmpty(price)) {
                        throw new JxException(ExceptionEnum.VIDEO_PRICE_DATA_INCOMPLETE);
                    }
                } else if (type == 2) {
                    if (number == null || StringUtil.isEmpty(price)) {
                        throw new JxException(ExceptionEnum.VIDEO_PRICE_DATA_INCOMPLETE);
                    }
                } else {
                    if (StringUtil.isEmpty(price)) {
                        throw new JxException(ExceptionEnum.VIDEO_PRICE_DATA_INCOMPLETE);
                    }
                }
            }
        }
        i = videoMapper.updateVideoStatus(videoIds);
        if (i > 0) {
            return Result.success(ResultCode.VIDEO_TO_SHELF_SUCCESS);
        }
        return Result.failure(ResultCode.VIDEO_TO_SHELF_ERROR);
    }


    /**
     * 删除视频
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delete(String id) {

        try {
            //模糊删除redis换存
            redisTemplate.delete(redisTemplate.keys(KeyConstant.INDEX_VIDEO_LIST + "*"));
            //创建培训视频
            redisTemplate.delete(redisTemplate.keys(KeyConstant.TRAIN_VIDEO_LIST + "*"));
            //删除数据库的视频
            Example example = new Example(Video.class);
            example.createCriteria().andCondition(" id = '" + id + "' AND status = " + 0);
            int i = videoMapper.deleteByExample(example);
            if (i > 0) {
                //删除阿里的视频
                DefaultAcsClient client = Palyer.getInstance().initVodClient();
                DeleteVideoRequest request = new DeleteVideoRequest();
                request.setVideoIds(id);
                DeleteVideoResponse acsResponse = client.getAcsResponse(request);
                return Result.success(ResultCode.DATA_DELETE_SUCCESS);
            } else {
                return Result.failure(ResultCode.DATA_DELETE_ERROR);
            }

        } catch (Exception e) {
            log.error("delete()", e);
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client, String id) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(id);
        //Single（每种清晰度和格式只返回一路最新转码完成的流）
        //Multiple（每种清晰度和格式返回所有转码完成的流）
        request.setResultType("Multiple");
        //视频流类型，多个用逗号分隔。支持类型： video audio
        request.setStreamType("video");
        //播放地址过期时间，单位：秒。
        request.setAuthTimeout(60 * 60L);
        //视频流格式，多个用逗号分隔。支持格式  mp4 m3u8 mp3
        request.setFormats("m3u8");
        return client.getAcsResponse(request);
    }

    /**
     * 获取视频列表信息
     *
     * @param videoIds 视频id
     * @return 返回视频列表
     */
    public Result getVideoByVideoIds(List<String> videoIds) {
        List<Video> list = new ArrayList<>();
        Video video = new Video();
        for (String videoId : videoIds) {
            video.setId(videoId);
            list.add(videoMapper.selectOne(video));
        }
        return Result.success(list);
    }

    /**
     * select * from users order by rand() LIMIT 1
     * 查询推荐的视频（根据用户查看的视频分类id 查询该分类的id）
     */
    public Result selectVideoListRand(String cateId) {
        // 开始分页
        PageHelper.startPage(1, 3);
        Map<String, Object> params = new HashMap<>();
        params.put("cateId", cateId);
        // 查询
        Page<HashMap<String, Object>> pageInfo = (Page<HashMap<String, Object>>) videoMapper.selectVideoListRand(params);
        return Result.success(pageInfo);
        //价格 是否购买
//        for (HashMap<String, Object> map : pageInfo) {
//            String price = videoPriceMapper.queryMinPriceByVideoId((String) map.get("id"));
//            map.put("price", price);
//        }
//        //返回用户购买状态
//        for (HashMap<String, Object> map : pageInfo) {
//            map.put("payStatus", 0);
//        }

    }


    /**
     * 下架视频
     *
     * @param videoIds 视频 id
     * @return 返回状态
     */
    public Result toUndercarriageVideo(List<String> videoIds) {
        int i = 0;
        //模糊删除redis换存
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(KeyConstant.INDEX_VIDEO_LIST + "*")));
        i = videoMapper.toUndercarriageVideo(videoIds);
        if (i > 0) {
            return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
        }
        return Result.success(ResultCode.DATA_UPDATE_ERROR);
    }

    /**
     * 获取播放视频凭证
     *
     * @param id 视频id
     * @return 返回播放凭证
     */
    public synchronized Result getVideoPlayAuthResponse(String id, Integer type) {
        try {
            Claims claims = LoginInterceptor.getLoginClaims();
            if (claims != null) {

                Long memberId = Long.valueOf((String) claims.get("id"));
                //获取播放凭证
                GetVideoPlayAuthResponse videoPlayAuth = VideoPlayUtil.getVideoPlayAuth(id);
                //添加播放记录 添加到Redis
                log.info("获取播放视频凭证：" + memberId + "|"+claims.get("roles") + " | " + id + "|" + videoPlayAuth.getPlayAuth());
                this.addVideoPlayLog(memberId, id, String.valueOf(claims.get("roles")));
                return Result.success(videoPlayAuth.getPlayAuth());
            }
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 添加播放记录
     *
     * @param memberId 用户id
     * @param videoId  视频id
     * @return
     */
    private void addVideoPlayLog(Long memberId, String videoId, String roles) {
        //添加播放记录到redis
        redisTemplate.opsForValue().set(KeyConstant.VIDEO_PLAY_LOG + roles + ":" + memberId +":"+ System.currentTimeMillis(), videoId + ":" + System.currentTimeMillis());
    }


    /**
     * 根据传递的参数生成令牌
     * 说明：
     * 1、参数可以是业务方的用户ID、播放终端类型等信息
     * 2、调用令牌接口时生成令牌Token
     *
     * @return
     */
    private String generateToken(String videoId) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        if (org.apache.commons.lang.StringUtils.isEmpty(videoId)) {
            return null;
        }
        String base = videoId + memberId;
        //设置30S后，该token过期，过期时间可以自行调整
        long expire = System.currentTimeMillis() + 60 * 1000L;
        base += "_" + expire;
        //生成token
        String token = AesEncrypt.encryptAES(base);
        //保存token，用于解密时校验token的有效性，例如：过期时间、token的使用次数
        redisTemplate.opsForValue().set(redisKey + videoId + ":" + memberId, base, 60 * 1000L);
        return token;
    }

    /**
     * 验证token的有效性
     * 说明：
     * 1、解密接口在返回播放秘钥前，需要先校验Token的合法性和有效性
     * 2、强烈建议同时校验Token的过期时间以及Token的有效使用次数
     *
     * @param token token
     * @return 返回 true/false
     */
    public boolean validateToken(String videoId, String token) {
        Long memberId = ObjectUtils.toLong(LoginInterceptor.getLoginClaims().get(JwtConstans.JWT_KEY_ID));
        if (null == token || "".equals(token)) {
            return false;
        }
        //先校验token的有效时间
        String s = redisTemplate.opsForValue().get(redisKey + videoId + ":" + memberId);
        if (org.apache.commons.lang.StringUtils.isEmpty(token)) {
            return false;
        }
        return s != null && s.equals(AesEncrypt.decryptAES(token));
    }


}

