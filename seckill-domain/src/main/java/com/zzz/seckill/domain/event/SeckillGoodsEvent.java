package com.zzz.seckill.domain.event;

public class SeckillGoodsEvent extends SeckillBaseEvent {
    private Long activityId;


    public SeckillGoodsEvent(Long id, Long activityId, Integer status) {
        super(id, status);
        this.activityId = activityId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
