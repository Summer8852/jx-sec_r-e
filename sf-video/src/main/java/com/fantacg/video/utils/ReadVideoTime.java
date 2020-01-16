package com.fantacg.video.utils;

import com.fantacg.common.enums.ExceptionEnum;
import com.fantacg.common.exception.JxException;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 获取视频时长工具类
 * @author DUPENGFEI
 */
/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname ReadVideoTime
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@Slf4j
public class ReadVideoTime {

	/**
	 * @param source
	 * @return
	 */
	public Long readVideoTime(File source) {
		Encoder encoder = new Encoder();
		Long ls = null;
		try {
			MultimediaInfo m = encoder.getInfo(source);
			 return m.getDuration() / 1000;
		} catch (Exception e) {
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
			throw new JxException(ExceptionEnum.SYSTEM_INNER_ERROR);
		}

	}
}
