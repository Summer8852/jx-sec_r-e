package com.fantacg.upload.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.fantacg.common.auth.utils.JwtUtils;
import com.fantacg.common.constant.ImgConstant;
import com.fantacg.common.constant.MQConstant;
import com.fantacg.common.constant.RoleConstant;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.dto.video.VideoDto;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.CookieUtils;

import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.upload.config.JwtProperties;
import com.fantacg.upload.config.UploadProperties;
import com.fantacg.upload.service.UploadService;
import com.fantacg.upload.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;


/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <p>
 *
 * @author 智慧安全云
 * @Classname UploadServiceImpl
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class UploadServiceImpl implements UploadService {


    @Value("${aliyun.idcard.appcode}")
    private String appcode;

    @Value("${file.path}")
    private String filePath;

    private static final String MP4 = "mp4";

    @Autowired
    UploadProperties prop;
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
            long size = file.getSize();
            System.out.println("文件大小 :" + size);
            DecimalFormat format = new DecimalFormat("0.00");
            String str = String.valueOf(size);
            Double dd = Double.valueOf(str) / 1024 / 1024;
            String sizess = format.format(dd) + "MB";
            System.out.println("文件大小 :" + sizess);
            if (dd > 1.8) {
                return Result.failure("照片文件过大,请上传1.5M一下的图片！");
            }

            //文件大小
            file.getSize();
            // 对文件进行校验对文件格式进行校验
            if (!file.getContentType().equals(ImgConstant.ATTACHMENT) && !prop.getAllowTypes().contains(file.getContentType())) {
                return Result.failure(ExceptionEnum.INVALID_FILE_FORMAT.message());
            }

            //身份证人脸对比认证
            if (fileType.equals(ImgConstant.ID_HAND)) {
                //身份证照片url必传
                if (StringUtils.isEmpty(url)) {
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
                        return Result.failure(ExceptionEnum.UPLOAD_IDCARD_SCAN_ERROR.message());
                    }
                } else {
                    return Result.failure(result.getMsg());
                }
            }
            //上传照片
            Result upload = this.upload(file, fileType);
            return upload;
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
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
            if (!MP4.equals(extensionName)) {
                throw new JxException(ExceptionEnum.INVALID_FILE_FORMAT);
            }
            return this.upload(file, "video");
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
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
                if ("front".equals(fileType)) {
                    if (map.get("address").isEmpty() || map.get("nationality").isEmpty() || map.get("num").isEmpty() || map.get("sex").isEmpty() || map.get("name").isEmpty() || map.get("birth").isEmpty()) {
                        return Result.failure("身份证信息无法识别，请重新上传！");
                    }
                }

                if ("reverse".equals(fileType)) {
                    if (map.get("start_date").isEmpty() || map.get("issue").isEmpty() || map.get("end_date").isEmpty()) {
                        return Result.failure("身份证信息无法识别，请重新上传！");
                    }
                }
                Result result = this.upload(file, "fileType");
                if (ResultCode.SUCCESS.code().equals(result.getCode())) {
                    map.put("url", String.valueOf(result.getData()));
                }
                return Result.success(map);
            }
            return Result.failure("身份证信息无法识别，请重新上传！");
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
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
                this.delFile(path);
            }
            return Result.success(ResultCode.DATA_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
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
            String fileName = "*." + MP4;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            File file = null;
            try {
                if (StringUtils.isEmpty(videoDto.getTemplateGroupId())) {
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
                amqpTemplate.convertAndSend(MQConstant.VIDEO_EXCHANGE_NAME, MQConstant.VIDEO_ROUTE_KEY, JSON.DEFAULT_TYPE_KEY);
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            } catch (Exception e) {
                //手动开启事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
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
                    throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
                }
            }
        } else {
            return Result.failure(ResultCode.NO_ACCESS_ERROR);
        }
    }

    /**
     * 上传身份阅读器的图片
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadFile(MultipartFile file, String cardNum) {
        try {
            if (file.isEmpty()) {
                return Result.failure("上传的文件大小为空,请检查!!");
            }

            // 存储转换后文件名称
            String fileName = cardNum + ".png";
            File dest = new File(filePath + "cardHeadImg" + "/" + fileName);

            //判断父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }

            try {
                file.transferTo(dest);
                return Result.success("/file/cardHeadImg/" + fileName);
            } catch (IOException e) {
                log.error("上传文件过程中发生异常！", e);
            }
            return Result.failure("上传失败");
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 上传模板文档
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadWord(MultipartFile file) {
        return this.upload(file, "word");
    }


    /**
     * 视频转码
     *
     * @param videoId
     * @return
     */
    @Override
    public Result videoEncryption(String videoId) {
        try {
            DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
            VideoEncryption.submitTranscodeJobs(client, videoId, VideoConstant.templateGroupId);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
        return Result.success();
    }

    private Result upload(MultipartFile file, String url) {
        if (file.isEmpty()) {
            return Result.failure("上传的文件大小为空,请检查!!");
        }
        //获取文件名称、后缀名、大小
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        long size = file.getSize();
        log.info("上传的文件名称为：[{}],文件后缀为：[{}],文件大小为：[{}]!!", fileName, suffixName, size);
        // 存储转换后文件名称
        fileName = UUID.randomUUID() + suffixName;
        log.info("转换后的文件名为：[{}]!!", fileName);
        File dest = new File(filePath + url + "/" + fileName);

        //判断父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
            if ("template".equals(url)) {
                return Result.success(url + "/" + fileName);
            }
            return Result.success("/file/" + url + "/" + fileName);
        } catch (IOException e) {
            log.error("上传文件过程中发生异常！", e);
        }
        return Result.failure("上传失败");
    }

    private Integer delFile(String path) {
        Integer integer = null;
        String sb = path.replace("/file/", "");
        sb = filePath + sb;
        log.info(sb);
        File file = new File(sb);
        if (file.exists()) {
            if (file.delete()) {
                integer = 1;
            } else {
                integer = 0;
            }
        } else {
            integer = 0;
        }
        return integer;
    }


}
