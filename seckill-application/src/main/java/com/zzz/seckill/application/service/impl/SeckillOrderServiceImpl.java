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
package com.zzz.seckill.application.service.impl;
import com.zzz.seckill.domain.model.entity.SeckillOrder;
import com.zzz.seckill.domain.model.entity.SeckillGoods;
import com.zzz.seckill.application.service.SeckillGoodsService;
import com.zzz.seckill.application.service.SeckillOrderService;
import com.zzz.seckill.domain.code.HttpCode;
import com.zzz.seckill.domain.model.dto.SeckillOrderDTO;
import com.zzz.seckill.domain.model.enums.SeckillGoodsStatus;
import com.zzz.seckill.domain.model.enums.SeckillOrderStatus;
import com.zzz.seckill.domain.exception.SeckillException;
import com.zzz.seckill.domain.repository.SeckillOrderRepository;
import com.zzz.seckill.infrastructure.utils.beans.BeanUtil;
import com.zzz.seckill.infrastructure.utils.id.SnowFlakeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zzz.seckill.application.order.place.SeckillPlaceOrderService;
import java.math.BigDecimal;
import java.util.Date;
import com.zzz.seckill.application.command.SeckillOrderCommand;
import java.util.List;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 订单业务
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private SeckillOrderRepository seckillOrderRepository;
    @Autowired
    private SeckillPlaceOrderService seckillPlaceOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveSeckillOrder(Long userId, SeckillOrderCommand seckillOrderCommand) {
        if (seckillOrderCommand == null){
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }

        return seckillPlaceOrderService.placeOrder(userId, seckillOrderCommand);
    }

    @Override
    public List<SeckillOrder> getSeckillOrderByUserId(Long userId) {
        return seckillOrderRepository.getSeckillOrderByUserId(userId);
    }

    @Override
    public List<SeckillOrder> getSeckillOrderByActivityId(Long activityId) {
        return seckillOrderRepository.getSeckillOrderByActivityId(activityId);
    }
}
