package com.example.market.common.base.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
public class VoStorageGrainTransaction implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 粮食种类id
     */
    @ApiModelProperty(value = "粮食种类id")
    private Long grainTypeId;
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
     * 新旧程度：{0：新，1：旧}
     */
    @ApiModelProperty(value = "新旧程度：{0：新，1：旧}")
    private Short newOldDegree;
    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private String particularYear;
    /**
     * 粮食品牌
     */
    @ApiModelProperty(value = "粮食品牌")
    private String brand;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String describeContent;
    /**
     * 地址
     */
    @ApiModelProperty(value = "id")
    private String address;
    /**
     * 交易方式：{0：主动收购，1：上门送出}
     */
    @ApiModelProperty(value = "交易方式：{0：主动收购，1：上门送出}")
    private Short transactionMode;
}
