package com.zzz.seckill.application.command;

import java.io.Serializable;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 订单DTO
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public class SeckillOrderCommand implements Serializable {

    private static final long serialVersionUID = 2150071992328498340L;
    //商品id
    private Long goodsId;
    //购买数量
    private Integer quantity;
    //活动id
    private Long activityId;
    //商品版本号
    private Long version;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
