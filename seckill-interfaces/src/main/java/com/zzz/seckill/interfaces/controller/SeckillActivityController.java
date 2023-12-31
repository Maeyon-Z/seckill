/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzz.seckill.interfaces.controller;

import com.zzz.seckill.application.service.SeckillActivityService;
import com.zzz.seckill.domain.code.HttpCode;
import com.zzz.seckill.domain.model.dto.SeckillActivityDTO;
import com.zzz.seckill.domain.response.ResponseMessage;
import com.zzz.seckill.domain.response.ResponseMessageBuilder;
import com.zzz.seckill.infrastructure.utils.date.JodaDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zzz.seckill.domain.model.entity.SeckillActivity;
import java.util.List;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 活动Controller
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@RestController
@RequestMapping(value = "/activity")
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;
    /**
     * 保存秒杀活动
     */
    @RequestMapping(value = "/saveSeckillActivity", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<String> saveSeckillActivityDTO(@RequestBody SeckillActivityDTO seckillActivityDTO){
        seckillActivityService.saveSeckillActivityDTO(seckillActivityDTO);
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode());
    }

    /**
     * 根据状态获取活动列表
     */
    @RequestMapping(value = "/getSeckillActivityList", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<List<SeckillActivity>> getSeckillActivityList(@RequestParam(value = "status", required = false) Integer status){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityList(status));
    }

    /**
     * 获取秒杀活动列表(使用缓存机制)
     */
    @RequestMapping(value = "/seckillActivityList", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<List<SeckillActivityDTO>> getSeckillActivityList(@RequestParam(value = "status", required = false) Integer status,
                                                                            @RequestParam(value = "version", required = false) Long version){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityList(status, version));
    }

    /**
     * 根据时间和状态获取活动列表
     */
    @RequestMapping(value = "/getSeckillActivityListBetweenStartTimeAndEndTime", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<List<SeckillActivity>> getSeckillActivityListBetweenStartTimeAndEndTime(@RequestParam(value = "currentTime", required = false) String currentTime,
                                                                                                   @RequestParam(value = "status", required = false)Integer status){
        List<SeckillActivity> seckillActivityList = seckillActivityService.getSeckillActivityListBetweenStartTimeAndEndTime(JodaDateTimeUtils.parseStringToDate(currentTime, JodaDateTimeUtils.DATE_TIME_FORMAT), status);
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillActivityList);
    }

    /**
     * 获取id获取秒杀活动详情
     */
    @RequestMapping(value = "/getSeckillActivityById", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<SeckillActivity> getSeckillActivityById(@RequestParam(value = "id", required = false) Long id){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityById(id));
    }

    /**
     * 获取id获取秒杀活动详情(使用缓存)
     */
    @RequestMapping(value = "/seckillActivity", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<SeckillActivity> getSeckillActivityById(@RequestParam(value = "id", required = false) Long id,
                                                                   @RequestParam(value = "version", required = false) Long version){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityById(id, version));
    }

    /**
     * 更新活动的状态
     */
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseMessage<String> updateStatus(@RequestParam(value = "status", required = false) Integer status,
                                                @RequestParam(value = "id", required = false) Long id){
        seckillActivityService.updateStatus(status, id);
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode());
    }

}
