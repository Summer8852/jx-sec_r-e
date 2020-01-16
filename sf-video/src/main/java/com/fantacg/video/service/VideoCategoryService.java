package com.fantacg.video.service;

import com.aliyuncs.vod.model.v20170321.AddCategoryResponse;
import com.fantacg.common.constant.KeyConstant;
import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ResultCode;
import com.fantacg.video.mapper.VideoCategoryMapper;
import com.fantacg.video.mapper.VideoMapper;
import com.fantacg.common.pojo.video.VideoCategory;
import com.fantacg.video.utils.CategoryUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoCategoryService
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Service
@Slf4j
public class VideoCategoryService {

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    VideoCategoryMapper videoCategoryMapper;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 根据parentId查询子类目
     *
     * @param pid
     * @return
     */
    public Result queryCategoryListByParentId(Long pid) {

        try {

            String s = redisTemplate.opsForValue().get(KeyConstant.VIDEO_CATEGORY_LIST);
            if (!StringUtils.isEmpty(s)) {
                List<VideoCategory> videoCategories = objectMapper.readValue(s, List.class);
                return Result.success(videoCategories);
            }

            Example example = new Example(VideoCategory.class);
            example.createCriteria().andCondition(" parent_id = " + pid);
            List<VideoCategory> videoCategories = this.videoCategoryMapper.selectByExample(example);
            if (!videoCategories.isEmpty()) {
                for (int i = 0; i < videoCategories.size(); i++) {

                    VideoCategory category = videoCategories.get(i);

                    example = new Example(VideoCategory.class);
                    example.createCriteria().andCondition(" parent_id = " + category.getId());
                    List<VideoCategory> videoCategories1 = this.videoCategoryMapper.selectByExample(example);

                    category.setValue(category.getId());
                    category.setLabel(category.getName());
                    category.setChildren(videoCategories1);

                    if (!videoCategories1.isEmpty()) {
                        for (int j = 0; j < videoCategories1.size(); j++) {

                            VideoCategory category1 = videoCategories1.get(j);

                            example = new Example(VideoCategory.class);
                            example.createCriteria().andCondition(" parent_id = " + category1.getId());
                            List<VideoCategory> videoCategories2 = this.videoCategoryMapper.selectByExample(example);

                            category1.setValue(category1.getId());
                            category1.setLabel(category1.getName());
                            category1.setChildren(videoCategories2);

                            if (!videoCategories2.isEmpty()) {
                                for (int z = 0; z < videoCategories2.size(); z++) {
                                    VideoCategory category2 = videoCategories2.get(z);
                                    category2.setValue(category2.getId());
                                    category2.setLabel(category2.getName());
                                }
                            }
                        }
                    }
                }


            }

            if (!videoCategories.isEmpty()) {
                redisTemplate.opsForValue().set(KeyConstant.VIDEO_CATEGORY_LIST, objectMapper.writeValueAsString(videoCategories));
            }
            return Result.success(videoCategories);
        } catch (Exception e) {
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteById(Long id) {
        try {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.VIDEO_CATEGORY_LIST + "*"));
            //判断子分类是否还存在
            VideoCategory c = new VideoCategory();
            c.setParentId(id);
            int count = videoCategoryMapper.selectCount(c);
            if (count > 0) {
                return Result.failure(ResultCode.DATA_DELETE_CATEGORY_CHILDREN_FIRST);
            }
            //判断分类下是否有视频
            List<HashMap<String, Object>> list = videoMapper.queryVideoByCateId(id);
            if (!list.isEmpty()) {
                return Result.failure(ResultCode.DATA_DELETE_CATEGORY_VIDEO_FIRST);
            }
            c = new VideoCategory();
            c.setId(id);
            int i = this.videoCategoryMapper.delete(c);
            if (i > 0) {
                CategoryUtils.deleteCategory(id);
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            }
            return Result.failure(ResultCode.DATA_ADD_ERROR);
        } catch (Exception e) {
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * 添加分类
     *
     * @param videoCategory
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveVideoCategory(VideoCategory videoCategory) {
        try {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.VIDEO_CATEGORY_LIST + "*"));
            AddCategoryResponse addCategoryResponse = CategoryUtils.addCategory(videoCategory);
            videoCategory.setParentId(addCategoryResponse.getCategory().getParentId());
            videoCategory.setName(addCategoryResponse.getCategory().getCateName());
            videoCategory.setId(addCategoryResponse.getCategory().getCateId());
            videoCategory.setLevel(addCategoryResponse.getCategory().getLevel());
            int i = this.videoCategoryMapper.insertSelective(videoCategory);
            if (i > 0) {
                return Result.success(ResultCode.DATA_ADD_SUCCESS);
            }
            return Result.failure(ResultCode.DATA_ADD_ERROR);
        } catch (Exception e) {
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }

    }

    /**
     * 修改分类名称
     *
     * @param videoCategory
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result update(VideoCategory videoCategory) {

        try {
            redisTemplate.delete(redisTemplate.keys(KeyConstant.VIDEO_CATEGORY_LIST + "*"));
            Example example = new Example(VideoCategory.class);
            example.createCriteria().andCondition(" id = " + videoCategory.getId());
            int i = this.videoCategoryMapper.updateByExampleSelective(videoCategory, example);
            if (i > 0) {
                CategoryUtils.updateCategory(videoCategory);
                return Result.success(ResultCode.DATA_UPDATE_SUCCESS);
            }
            return Result.failure(ResultCode.DATA_UPDATE_ERROR);
        } catch (Exception e) {
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
        }
    }
}
