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

import com.zzz.seckill.application.command.SeckillGoodsCommond;
import com.zzz.seckill.domain.model.dto.SeckillGoodsDTO;
import com.zzz.seckill.domain.model.entity.SeckillGoods;

import java.util.List;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 商品
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public interface SeckillGoodsService {

    /**
     * 保存商品信息
     */
    void saveSeckillGoods(SeckillGoodsCommond seckillGoodsCommond);

    /**
     * 根据id获取商品详细信息
     */
    SeckillGoods getSeckillGoodsId(Long id);

    /**
     * 根据活动id获取商品列表
     */
    List<SeckillGoods> getSeckillGoodsByActivityId(Long activityId);

    /**
     * 修改商品状态
     */
    void updateStatus(Integer status, Long id);

    /**
     * 扣减库存
     */
    boolean updateAvailableStock(Integer count, Long id);

    /**
     * 扣减数据库库存
     */
    boolean updateDbAvailableStock(Integer count, Long id);

    /**
     * 获取当前可用库存
     */
    Integer getAvailableStockById(Long id);

    /**
     * 根据活动id从缓存中获取数据
     */
    List<SeckillGoodsDTO> getSeckillGoodsList(Long activityId, Long version);

    /**
     * 根据id获取商品详细信息（带缓存）
     */
    SeckillGoodsDTO getSeckillGoods(Long id, Long version);
}
