package com.fantacg.video.controller;

import com.fantacg.common.utils.Result;
import com.fantacg.common.utils.ValidList;
import com.fantacg.common.pojo.video.VideoPrice;
import com.fantacg.video.service.VideoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 开发公司：深圳市九象数字科技有限公司
 * 版权：深圳市九象数字科技有限公司
 * <P>
 * @author 智慧安全云
 * @Classname VideoPriceController
 * @Created by Dupengfei 2019/11/26 9:57
 * @Version 2.0
 */
@RestController
@RequestMapping("/videoprice")
public class VideoPriceController {

    @Autowired
    private VideoPriceService videoPriceService;

    /**
     * 查询视频价格详情
     *
     * @param videoId 视频id
     * @return 价格详情
     */
    @GetMapping("/{videoId}")
    public Result queryVideoPriceByVideoIdList(@PathVariable("videoId") String videoId) {
        return this.videoPriceService.queryVideoPriceByVideoIdList(videoId);
    }

    /**
     * 添加视频购买方式
     *
     * @param videoPrices 价格信息
     * @param result      校验参数
     * @return 添加成功/失败
     */
    @PostMapping
    public Result saveVideoPrice(@Valid @RequestBody @Validated(VideoPrice.class) ValidList<VideoPrice> videoPrices, BindingResult result) {

        if (result.hasErrors()) {
            return Result.failure(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return this.videoPriceService.saveVideoPrice(videoPrices);
    }


    /**
     * 修改视频购买方式
     *
     * @param videoPrices 价格信息
     * @return 修改成功/失败
     */
    @PutMapping
    public Result updateVideoPrice(@RequestBody List<VideoPrice> videoPrices) {
        return this.videoPriceService.updateVideoPrice(videoPrices);
    }

    /**
     * 删除视频购买方式
     *
     * @param id 视频id
     * @return 删除成功/失败
     */
    @DeleteMapping("/{id}")
    public Result deleteVideoPrice(@PathVariable("id") Long id) {
        return this.videoPriceService.deleteVideoPrice(id);
    }


    /**
     * 根据价格id查询详情
     *
     * @param id 价格id
     * @return 价格详情
     */
    @GetMapping("/detail/{id}")
    public HashMap<String, Object> videoPriceDetail(@PathVariable("id") Long id) {
        return this.videoPriceService.videoPriceDetail(id);
    }
}
