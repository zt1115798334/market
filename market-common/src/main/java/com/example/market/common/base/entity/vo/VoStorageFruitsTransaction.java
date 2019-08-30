package com.example.market.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
public class VoStorageFruitsTransaction implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 水果种类id
     */
    @ApiModelProperty(value = "水果种类id")
    private Long fruitsTypeId;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private Double price;
    /**
     * 上市时间
     */
    @ApiModelProperty(value = "上市时间")
    private String listingTime;
    /**
     * 水果品牌
     */
    @ApiModelProperty(value = "水果品牌")
    private String brand;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String describeContent;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 交易方式：{0：主动收购，1：上门送出}
     */
    @ApiModelProperty(value = "交易方式：{0：主动收购，1：上门送出}")
    private Short transactionMode;
}
