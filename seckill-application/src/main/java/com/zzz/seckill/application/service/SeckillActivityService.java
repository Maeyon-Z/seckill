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
package com.zzz.seckill.application.service;
import com.zzz.seckill.domain.model.entity.SeckillActivity;

import com.zzz.seckill.domain.model.dto.SeckillActivityDTO;

import java.util.Date;
import java.util.List;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 活动
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public interface SeckillActivityService {

    /**
     * 保存活动信息
     */
    void saveSeckillActivityDTO(SeckillActivityDTO seckillActivityDTO);

    /**
     * 根据状态获取活动列表
     */
    List<SeckillActivity> getSeckillActivityList(Integer status);

    /**
     * 根据时间和状态获取活动列表
     */
    List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status);

    /**
     * 根据id获取活动信息
     */
    SeckillActivity getSeckillActivityById(Long id);

    /**
     * 根据id获取活动信息(使用缓存)
     */
    SeckillActivity getSeckillActivityById(Long id, Long version);

    /**
     * 修改状态
     */
    void updateStatus(Integer status, Long id);

    /**
     * 活动列表
     */
    List<SeckillActivityDTO> getSeckillActivityList(Integer status, Long version);
}
