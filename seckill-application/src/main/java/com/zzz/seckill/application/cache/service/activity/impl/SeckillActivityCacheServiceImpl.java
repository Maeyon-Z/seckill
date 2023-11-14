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
package com.zzz.seckill.application.cache.service.activity.impl;

import com.alibaba.fastjson.JSON;
import com.zzz.seckill.application.builder.SeckillActivityBuilder;
import com.zzz.seckill.application.cache.model.SeckillBusinessCache;
import com.zzz.seckill.application.cache.service.activity.SeckillActivityCacheService;
import com.zzz.seckill.domain.constants.SeckillConstants;
import com.zzz.seckill.domain.repository.SeckillActivityRepository;
import com.zzz.seckill.infrastructure.cache.distribute.DistributedCacheService;
import com.zzz.seckill.infrastructure.cache.local.LocalCacheService;
import com.zzz.seckill.infrastructure.lock.DistributedLock;
import com.zzz.seckill.infrastructure.lock.factoty.DistributedLockFactory;
import com.zzz.seckill.infrastructure.utils.string.StringUtil;
import com.zzz.seckill.infrastructure.utils.time.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zzz.seckill.domain.model.entity.SeckillActivity;
@Service
public class SeckillActivityCacheServiceImpl implements SeckillActivityCacheService {

    private final static Logger logger = LoggerFactory.getLogger(SeckillActivityCacheServiceImpl.class);

    private static final String SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY = "SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY_";

    // 本地缓存服务
    @Autowired
    private LocalCacheService<Long, SeckillBusinessCache<SeckillActivity>> localCacheService;
    // 分布式锁工厂类
    @Autowired
    private DistributedLockFactory distributedLockFactory;
    // 本地缓存锁
    private final Lock localCacheUpdateLock = new ReentrantLock();
    //分布式缓存服务
    @Autowired
    private DistributedCacheService distributedCacheService;
    // 秒杀活动持久层
    @Autowired
    private SeckillActivityRepository seckillActivityRepository;

    /**
     * 获取分布式缓存的key值
     */
    @Override
    public String buildCacheKey(Object key) {
        return StringUtil.append(SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY, key);
    }

    /**
     * 获取缓存数据
     */
    @Override
    public SeckillBusinessCache<SeckillActivity> getCachedActivity(Long id, Long version) {
        SeckillBusinessCache<SeckillActivity> cacheData = localCacheService.getIfPresent(id);
        if(cacheData != null){
            if(version == null || version.compareTo(cacheData.getVersion()) <= 0){
                logger.info("SeckillActivityInfo|命中本地缓存{}", id);
                return cacheData;
            }else{
                return getActivityByDistributeCache(id);
            }
        }
        return getActivityByDistributeCache(id);

    }

    private SeckillBusinessCache<SeckillActivity> getActivityByDistributeCache(Long id){
        logger.info("读取分布式缓存获取秒杀活动详情{}", id);
        SeckillBusinessCache<SeckillActivity> seckillActivityCache = SeckillActivityBuilder.getSeckillBusinessCache(distributedCacheService.getObject(buildCacheKey(id)), SeckillActivity.class);
        if(seckillActivityCache == null){
            // 如果分布式缓存未命中，则需要查询数据库来更新分布式缓存，需要确保只能有一个请求操作数据库
            seckillActivityCache = tryUpdateSeckillActivityCacheByLock(id, true);
        }
        // 接下来更新本地缓存，同样需要确保只能有一个请求执行此操作
        // tryUpdateSeckillActivityCacheByLock会返回有效的数据或者返回一个retryLater字段为true的数据，retryLater字段为true表示当前有请求正在更新分布式缓存，客户端稍后重试即可
        if(seckillActivityCache != null && !seckillActivityCache.isRetryLater()){
            if(localCacheUpdateLock.tryLock()){
                try {
                    localCacheService.put(id, seckillActivityCache);
                    logger.info("更新秒杀活动详情{}本地缓存", id);
                }finally {
                    localCacheUpdateLock.unlock();
                }
            }
        }
        return seckillActivityCache;
    }


    @Override
    public SeckillBusinessCache<SeckillActivity> tryUpdateSeckillActivityCacheByLock(Long activityId, boolean doubleCheck) {
        logger.info("SeckillActivityCache|更新分布式缓存|{}", activityId);
        DistributedLock lock = distributedLockFactory.getDistributedLock(SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY.concat(String.valueOf(activityId)));
        try {
            // 尝试获取锁，确保只有一个线程来更新分布式缓存，获取失败的话直接返回retryLater
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if(!isLock){
                return new SeckillBusinessCache<SeckillActivity>().retryLater();
            }
            SeckillBusinessCache<SeckillActivity> cache;
            if (doubleCheck){
                //获取锁成功后，再次从缓存中获取数据，防止高并发下多个线程争抢锁的过程中，后续的线程在等待1秒的过程中，前面的线程释放了锁，后续的线程获取锁成功后再次更新分布式缓存数据
                cache = SeckillActivityBuilder.getSeckillBusinessCache(distributedCacheService.getObject(buildCacheKey(activityId)),  SeckillActivity.class);
                if (cache != null){
                    return cache;
                }
            }
            // 获取到锁之后则查询数据库
            SeckillActivity seckillActivity = seckillActivityRepository.getSeckillActivityById(activityId);
            if(seckillActivity == null){
                cache = new SeckillBusinessCache<SeckillActivity>().notExist();
            }else {
                cache = new SeckillBusinessCache<SeckillActivity>().with(seckillActivity).withVersion(SystemClock.millisClock().now());
            }
            distributedCacheService.put(buildCacheKey(activityId),  JSON.toJSONString(cache), SeckillConstants.FIVE_MINUTES);
            logger.info("SeckillActivityCache|分布式缓存已经更新|{}", activityId);
            return cache;
        }catch (Exception e){
            logger.error("SeckillActivityCache|更新分布式缓存失败|{}", activityId);
            return new SeckillBusinessCache<SeckillActivity>().retryLater();
        }finally {
            lock.unlock();
        }
    }

}
