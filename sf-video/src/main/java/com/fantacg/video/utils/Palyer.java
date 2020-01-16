package com.fantacg.video.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.fantacg.common.constant.VideoConstant;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname Palyer
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
public class Palyer {
	
	private Palyer(){}
	
	private static Palyer palyer=null;
	
	public static Palyer getInstance() {
		if(palyer==null) {
			palyer=new Palyer();	
		}
		return palyer;
	}
	
	public DefaultAcsClient initVodClient() {
	    //点播服务所在的Region，国内请填cn-shanghai，不要填写别的区域
	    String regionId = "cn-shanghai";
	    DefaultProfile profile = DefaultProfile.getProfile(regionId, VideoConstant.accessKeyId, VideoConstant.accessKeySecret);
	    DefaultAcsClient client = new DefaultAcsClient(profile);
	    return client;
	}
	
	/*获取播放凭证函数*/
	public  GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client,String videoId) throws Exception {
	    GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
	    request.setVideoId(videoId);
	    return client.getAcsResponse(request);
	}
}
