package com.fantacg.video.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.video.service.PlayDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname PlayDetailController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/playdetail")
public class PlayDetailController {

    @Autowired
    PlayDetailService playDetailService;

    /**
     * 购买的播放视频详情
     */
    @GetMapping("/{videoId}")
    public Result selectPlayDetailByVideo(@PathVariable("videoId") String videoId) {
        return playDetailService.selectPlayDetailByVideo(videoId);
    }


    /**
     * 查询我的播放视频
     */
    @GetMapping("/page")
    public Result selectPlayDetailList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                       @RequestParam(value = "isEnd", defaultValue = "-1") Integer isEnd) {
        return playDetailService.selectPlayDetailList(page, rows, isEnd);
    }

    /**
     * 查询视频是否有效期
     *
     * @param memberId 用户id
     * @param videoId  视频id
     * @return 是否有效
     */
    @GetMapping("/detail/{memberId}/{videoId}")
    public Result videoPriceDetail(@PathVariable("memberId") Long memberId, @PathVariable("videoId") String videoId) {
        return this.playDetailService.selectOne(memberId, videoId);
    }


    /**
     *  添加视频播放信息
     * @param Id id
     * @param orderNo 订单编号
     * @param memberId 用户id
     * @param number 数量
     * @param type 类型
     * @param videoId 视频id
     * @param endTime 结束时间
     * @return 添加 成功/失败
     */
    @GetMapping("/detail/{Id}/{orderNo}/{memberId}/{number}/{type}/{videoId}/{endTime}")
    public boolean installPriceDetail(
            @PathVariable("Id") Long Id,
            @PathVariable("orderNo") String orderNo,
            @PathVariable("memberId") Long memberId,
            @PathVariable("number") Integer number,
            @PathVariable("type") Integer type,
            @PathVariable("videoId") String videoId,
            @PathVariable("endTime") Date endTime) {
        return this.playDetailService.installPriceDetail(Id, orderNo, memberId, number, type, videoId, endTime);
    }

}
