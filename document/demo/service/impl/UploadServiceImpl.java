package com.fantacg.upload.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.fantacg.auth.utils.JwtUtils;
import com.fantacg.common.constant.ImgConstant;
import com.fantacg.common.constant.MQConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.CookieUtils;
import com.fantacg.common.utils.JsonUtils;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.upload.config.JwtProperties;
import com.fantacg.upload.config.UploadProperties;
import com.fantacg.upload.service.UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author DUPENGFEI
 * @date 2018/9/16
 */
@EnableConfigurationProperties(JwtProperties.class)
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${aliyun.idcard.appcode}")
    private String appcode;
    @Autowired
    UploadProperties prop;
    @Autowired
    FastFileStorageClient storageClient;
    @Autowired
    UploadProperties uploadProperties;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    JwtProperties props;

    /**
     * 上传图片
     *
     * @param file
     * @param url
     * @param fileType
     * @return
     */
    @Override
    public Result uploadImages(MultipartFile file, String url, String fileType) {
        //保存图片
        try {
            // 对文件进行校验对文件格式进行校验
            String contentType = file.getContentType();
            log.info("文件格式：" + file.getContentType());
            if (!fileType.equals(ImgConstant.ATTACHMENT) && !prop.getAllowTypes().contains(contentType)) {
                    return Result.failure(ExceptionEnum.INVALID_FILE_FORMAT.message());
            }
            String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);

            if (fileType.equals(ImgConstant.ID_HAND)) {
                if (StringUtils.isEmpty(url)) {
                    storageClient.deleteFile(storePath.getFullPath());
                    return Result.failure(ResultCode.PARAMETER_NULL_ERROR);
                }

                //人脸 将图片转成base64
                String facepic = OcrIdCardUtil.getImageStr(file.getInputStream());
                //身份证
                String idpic = uploadProperties.getBaseUrl() + url;
                Result result = OcrIdCardUtil.IAcsClient(facepic, idpic);
                if (result.getCode() == 200) {
                    //获取相识度
                    Integer integer = (Integer) result.getData();
                    if (integer < 80) {
                        storageClient.deleteFile(storePath.getFullPath());
                        return Result.failure("请上传正确的手持身份证");
                    }
                } else {
                    storageClient.deleteFile(storePath.getFullPath());
                    return Result.failure(result.getMsg());
                }
            }
            log.info("图片上传成功：" + storePath.getFullPath());
            return Result.success(storePath.getFullPath());
        } catch (Exception e) {
            return Result.failure(ExceptionEnum.UPLOAD_IMAGE_EXCEPTION.message());
        }
    }

    /**
     * 上传试看视频 视频格式必须为mp4
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadVideo(MultipartFile file) {
        try {
            String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            if (!extensionName.equals("mp4")) {
                throw new JxException(ExceptionEnum.INVALID_FILE_FORMAT);
            }
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
            log.info("视频上传成功：" + storePath.getFullPath());
            return Result.success(storePath.getFullPath());
        } catch (Exception e) {
            return Result.failure(ExceptionEnum.UPLOAD_IMAGE_EXCEPTION.message());
        }
    }


    /**
     * 上传身份证正反面识别
     *
     * @param file
     * @param fileType
     * @return
     */
    @Override
    public Result uploadIdCard(MultipartFile file, String fileType) {
        try {
            OcrIdCardUtil ocrIdCardUtil = new OcrIdCardUtil();
            Map<Object, String> map = ocrIdCardUtil.scanIdCard(file.getInputStream(), appcode, fileType);
            if (map != null) {
                if (fileType.equals("front")) {
                    if ( map.get("address").isEmpty() || map.get("nationality").isEmpty() || map.get("num").isEmpty() || map.get("sex").isEmpty() || map.get("name").isEmpty() || map.get("birth").isEmpty()) {
                        return Result.failure("身份证信息无法识别，请重新上传！");
                    }
                }

                if (fileType.equals("reverse")) {
                    if (map.get("start_date").isEmpty() || map.get("issue").isEmpty() || map.get("end_date").isEmpty()) {
                        return Result.failure("身份证信息无法识别，请重新上传！");
                    }
                }
                String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
                StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
                log.info("图片上传成功：" + storePath.getFullPath());
                map.put("url", storePath.getFullPath());
                return Result.success(map);
            }
            return Result.failure("身份证信息无法识别，请重新上传！");
        } catch (Exception e) {
            log.error("uploadIdCard",e);
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 删除不需要要的文件
     *
     * @param
     */
    @Override
    public Result uploadDelImage(ConcurrentMap concurrentMap) {
        try {
            log.info("删除不需要要的文件:" + concurrentMap);
            String path = (String) concurrentMap.get("path");
            if (StringUtil.isNotEmpty(path)) {
                storageClient.deleteFile(path);
            }
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("uploadDelImage",e);
            throw new JxException(ExceptionEnum.INVALID_FILE_NO_NULL);
        }
    }

    /**
     * 视频上传阿里云
     *
     * @param videoDto
     * @param videoFile
     * @param request
     * @return
     */
    @Override
    public Result uploadAliyun(VideoDto videoDto, MultipartFile videoFile, HttpServletRequest request) {
        log.info("上传视频：" + videoDto + "==" + videoFile + "==");
        //用户已登录，获取用户信息
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        if (StringUtils.isBlank(token)) {
            //用户未登录,返回401，拦截
            return Result.failure(ResultCode.LOGIN_TOKEN_EXPIRE_ERROR);
        }
        Claims claims = new JwtUtils().parseJWT(props.getPublicKey(), token);
        if (claims.get("roles").equals(RoleConstant.USER)) {
            Long userId = Long.valueOf(String.valueOf(claims.get("id")));
            // 任何上传方式文件名必须包含扩展名
            String fileName = "*.mp4";
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;
            try {
                if(StringUtils.isEmpty(videoDto.getTemplateGroupId())){
                    videoDto.setTemplateGroupId(VideoConstant.templateGroupId);
                }
                //上传视频到阿里云
                String videoId = UploadVideo.testUploadStream(VideoConstant.accessKeyId, VideoConstant.accessKeySecret, videoDto, fileName,
                        videoFile.getInputStream());
                //创建临时文件，获取视频时长
                String originalFilename = videoFile.getOriginalFilename();
                // 文件名.png
                String fileNameSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                file = File.createTempFile(videoId, fileNameSuffix);
                inputStream = videoFile.getInputStream();
                byte[] buf = new byte[1024];
                int length = 0;
                fileOutputStream = new FileOutputStream(file);
                while ((length = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, length);
                }
                Long readVideoTime = new ReadVideoTime().readVideoTime(file);
                //视频ID
                videoDto.setVidoeId(videoId);
                //视频时间
                videoDto.setReadVideoTime(readVideoTime.intValue());
                //文件后缀名
                videoDto.setSuf(fileNameSuffix);
                videoDto.setUserId(userId);
                amqpTemplate.convertAndSend(MQConstant.VIDEO_EXCHANGE_NAME, MQConstant.VIDEO_ROUTE_KEY, JsonUtils.toString(videoDto));
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            } catch (Exception e) {
                log.error("uploadAliyun()",e);
                //手动开启事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
            } finally {
                //关闭资源
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    file.deleteOnExit();
                } catch (IOException e) {
                    log.error("uploadAliyun()",e);
                    return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
                }
            }
        } else {
            return Result.failure(ResultCode.NO_ACCESS_ERROR);
        }
    }

    /**
     * 上传身份阅读器的图片
     * @param file
     * @return
     */
    @Override
    public Result uploadFile(MultipartFile file) {
        try {
            String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
            return Result.success(storePath.getFullPath());
        } catch (Exception e) {
            log.error("uploadFile()",e);
            return Result.failure(ExceptionEnum.UPLOAD_IMAGE_EXCEPTION.message());
        }
    }


    /**
     * 视频转码
     * @param videoId
     * @return
     */
    @Override
    public Result videoEncryption(String videoId) {
        try {
            DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
            VideoEncryption.submitTranscodeJobs(client, videoId, VideoConstant.templateGroupId);
        } catch (Exception e) {
            log.error("videoEncryption()",e);
        }
        return Result.success();
    }

}
