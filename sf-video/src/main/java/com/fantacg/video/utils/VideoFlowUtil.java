package com.fantacg.video.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DescribeVodDomainBpsDataRequest;
import com.aliyuncs.vod.model.v20170321.DescribeVodDomainBpsDataResponse;
import com.aliyuncs.vod.model.v20170321.DescribeVodDomainTrafficDataRequest;
import com.aliyuncs.vod.model.v20170321.DescribeVodDomainTrafficDataResponse;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoFlowUtil
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class VideoFlowUtil {

    /**
     * 查询流量数据
     */
    public static DescribeVodDomainTrafficDataResponse describeVodDomainTrafficData(String domainName, String startTime, String endTime) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();

        DescribeVodDomainTrafficDataRequest request = new DescribeVodDomainTrafficDataRequest();
        // 设置域名
        request.setDomainName("sfv.fantacg.com");
        // 设置开始时间，请使用UTC格式
        request.setStartTime(startTime);
        // 设置结束时间，请使用UTC格式
        request.setEndTime(endTime);
        // 返回结果
        return client.getAcsResponse(request);
    }

    /**
     * 查询网络带宽
     */
    public static DescribeVodDomainBpsDataResponse describeVodDomainBpsData(String domainName, String startTime, String endTime) throws Exception {
        DefaultAcsClient client = Palyer.getInstance().initVodClient();

        DescribeVodDomainBpsDataRequest request = new DescribeVodDomainBpsDataRequest();
        // 设置域名
        request.setDomainName(domainName);
        // 设置开始时间，请使用UTC格式
        request.setStartTime(startTime);
        // 设置结束时间，请使用UTC格式
        request.setEndTime(endTime);
        // 返回结果
        return client.getAcsResponse(request);
    }
}
