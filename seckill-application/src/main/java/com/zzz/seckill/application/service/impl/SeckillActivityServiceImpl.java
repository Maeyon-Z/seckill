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

import com.zzz.seckill.application.cache.model.SeckillBusinessCache;
import com.zzz.seckill.application.cache.service.activity.SeckillActivityCacheService;
import com.zzz.seckill.application.cache.service.activity.SeckillActivityListCacheService;
import com.zzz.seckill.application.service.SeckillActivityService;
import com.zzz.seckill.domain.code.HttpCode;
import com.zzz.seckill.domain.model.dto.SeckillActivityDTO;
import com.zzz.seckill.domain.model.enums.SeckillActivityStatus;
import com.zzz.seckill.domain.exception.SeckillException;
import com.zzz.seckill.domain.service.SeckillActivityDomainService;
import com.zzz.seckill.infrastructure.utils.beans.BeanUtil;
import com.zzz.seckill.infrastructure.utils.id.SnowFlakeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.zzz.seckill.domain.model.entity.SeckillActivity;
/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 秒杀活动
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {
    @Autowired
    private SeckillActivityDomainService seckillActivityDomainService;
    @Autowired
    private SeckillActivityListCacheService seckillActivityListCacheService;
    @Autowired
    private SeckillActivityCacheService seckillActivityCacheService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSeckillActivityDTO(SeckillActivityDTO seckillActivityDTO) {
        if (seckillActivityDTO == null){
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtil.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivity.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        seckillActivity.setStatus(SeckillActivityStatus.PUBLISHED.getCode());
        seckillActivityDomainService.saveSeckillActivity(seckillActivity);
    }

    @Override
    public List<SeckillActivity> getSeckillActivityList(Integer status) {
        return seckillActivityDomainService.getSeckillActivityList(status);
    }

    @Override
    public List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status) {
        return seckillActivityDomainService.getSeckillActivityListBetweenStartTimeAndEndTime(currentTime, status);
    }

    @Override
    public SeckillActivity getSeckillActivityById(Long id) {
        return seckillActivityDomainService.getSeckillActivityById(id);
    }

    /**
     * 获取秒杀活动详细信息（使用缓存机制）
     */
    @Override
    public SeckillActivity getSeckillActivityById(Long id, Long version) {
        SeckillBusinessCache<SeckillActivity> seckillActivityCache = seckillActivityCacheService.getCachedActivity(id, version);
        if(!seckillActivityCache.isExist()){
            throw new SeckillException(HttpCode.ACTIVITY_NOT_EXISTS);
        }
        if(seckillActivityCache.isRetryLater()){
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        return seckillActivityCache.getData();
    }

    @Override
    public void updateStatus(Integer status, Long id) {
         seckillActivityDomainService.updateStatus(status, id);
    }

    @Override
    public List<SeckillActivityDTO> getSeckillActivityList(Integer status, Long version) {
        SeckillBusinessCache<List<SeckillActivity>> seckillActivitiyListCache = seckillActivityListCacheService.getCachedActivities(status, version);
        if (!seckillActivitiyListCache.isExist()){
            throw new SeckillException(HttpCode.ACTIVITY_NOT_EXISTS);
        }
        //稍后再试，前端需要对这个状态做特殊处理，即不去刷新数据，静默稍后再试
        if (seckillActivitiyListCache.isRetryLater()){
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        List<SeckillActivityDTO> seckillActivityDTOList = seckillActivitiyListCache.getData().stream().map((seckillActivity) -> {
            SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
            BeanUtil.copyProperties(seckillActivity, seckillActivityDTO);
            seckillActivityDTO.setVersion(seckillActivitiyListCache.getVersion());
            return seckillActivityDTO;
        }).collect(Collectors.toList());
        return seckillActivityDTOList;
    }
}
