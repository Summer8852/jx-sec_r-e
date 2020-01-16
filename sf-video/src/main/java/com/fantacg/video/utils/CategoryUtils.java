package com.fantacg.video.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import com.fantacg.common.constant.VideoConstant;
import com.fantacg.common.pojo.video.VideoCategory;

/**
 * 阿里云 媒资分类 工具类
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname CategoryUtils 阿里云 媒资分类 工具类
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class CategoryUtils {


    /**创建分类函数*/
    public static AddCategoryResponse addCategory(VideoCategory category) throws Exception {
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        AddCategoryResponse response = new AddCategoryResponse();
        AddCategoryRequest request = new AddCategoryRequest();
        // 父分类ID，若不填，则默认生成一级分类，根节点分类ID为-1
        request.setParentId(category.getParentId());
        // 分类名称
        request.setCateName(category.getName());
        return client.getAcsResponse(request);
    }


    /**修改分类函数*/
    public static UpdateCategoryResponse updateCategory(VideoCategory category) throws Exception {
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        AddCategoryResponse response = new AddCategoryResponse();
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        // 请设置真实分类ID
        request.setCateId(category.getId());
        // 分类名称
        request.setCateName(category.getName());
        return client.getAcsResponse(request);
    }

    /**删除分类函数*/
    public static DeleteCategoryResponse deleteCategory(Long cateId) throws Exception {
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        AddCategoryResponse response = new AddCategoryResponse();
        DeleteCategoryRequest request = new DeleteCategoryRequest();
        // 请设置待删除分类ID
        request.setCateId(cateId);
        return client.getAcsResponse(request);
    }

    /**查询分类及其子分类函数*/
    public static GetCategoriesResponse getCategories(Long cateId, Long pageNo, Long pageSize) throws Exception {
        DefaultAcsClient client = InitVodClientUtils.initVodClient(VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
        AddCategoryResponse response = new AddCategoryResponse();
        GetCategoriesRequest request = new GetCategoriesRequest();
        request.setCateId(cateId);
        request.setPageNo(pageNo);
        request.setPageSize(pageSize);
        return client.getAcsResponse(request);
    }

}
