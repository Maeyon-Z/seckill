package com.zzz.seckill.application.event.handler;


import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.fastjson.JSON;
import com.zzz.seckill.application.cache.service.goods.SeckillGoodsCacheService;
import com.zzz.seckill.application.cache.service.goods.SeckillGoodsListCacheService;
import com.zzz.seckill.domain.event.SeckillGoodsEvent;
import com.alibaba.cola.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@EventHandler
public class SeckillGoodsEventHandler implements EventHandlerI<Response, SeckillGoodsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SeckillGoodsEventHandler.class);

    @Autowired
    private SeckillGoodsCacheService seckillGoodsCacheService;
    @Autowired
    private SeckillGoodsListCacheService seckillGoodsListCacheService;

    @Override
    public Response execute(SeckillGoodsEvent seckillGoodsEvent) {
        logger.info("goodsEvent|接收秒杀商品事件{}", JSON.toJSON(seckillGoodsEvent));
        if (seckillGoodsEvent.getId() == null){
            logger.info("goodsEvent|接收秒杀品事件参数错误");
            return Response.buildSuccess();
        }
        seckillGoodsCacheService.tryUpdateSeckillGoodsCacheByLock(seckillGoodsEvent.getId(), false);
        seckillGoodsListCacheService.tryUpdateSeckillGoodsCacheByLock(seckillGoodsEvent.getActivityId(), false);
        return Response.buildSuccess();
    }

}
