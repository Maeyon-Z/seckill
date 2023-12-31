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
package com.zzz.seckill.application.event.handler;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.fastjson.JSON;
import com.zzz.seckill.domain.event.SeckillOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 订单事件处理器
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@EventHandler
public class SeckillOrderEventHandler implements EventHandlerI<Response, SeckillOrderEvent> {
    private final Logger logger = LoggerFactory.getLogger(SeckillOrderEventHandler.class);

    @Override
    public Response execute(SeckillOrderEvent seckillOrderEvent) {
        logger.info("orderEvent|接收订单事件|{}", JSON.toJSON(seckillOrderEvent));
        if (seckillOrderEvent.getId() == null){
            logger.info("orderEvent|订单参数错误");
            return Response.buildSuccess();
        }
        return Response.buildSuccess();
    }
}
