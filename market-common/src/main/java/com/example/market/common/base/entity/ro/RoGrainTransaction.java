package com.example.market.common.base.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:03
 * description:
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoGrainTransaction extends RoTopic {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 粮食种类id
     */
    private Long grainTypeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 价格
     */
    private Double price;
    /**
     * 新旧程度：{0：新，1：旧}
     */
    private Short newOldDegree;
    /**
     * 年份
     */
    private String particularYear;
    /**
     * 粮食品牌
     */
    private String brand;
    /**
     * 商品描述
     */
    private String describeContent;
    /**
     * 地址
     */
    private String address;
    /**
     * 交易方式：{0：主动收购，1：上门送出}
     */
    private Short transactionMode;

    public RoGrainTransaction(RoUser user, String state, boolean userState, boolean zanState, Long zanNum, List<RoUser> zanUsers, boolean collectionState, Long commentNum, Long browsingVolume, List<RoImagePath> topicImgList,
                              Long id, Long grainTypeId, String title, Double price, Short newOldDegree, String particularYear, String brand, String describeContent, String address, Short transactionMode) {
        super(user, state, userState, zanState, zanNum, zanUsers, collectionState, commentNum, browsingVolume, topicImgList);
        this.id = id;
        this.grainTypeId = grainTypeId;
        this.title = title;
        this.price = price;
        this.newOldDegree = newOldDegree;
        this.particularYear = particularYear;
        this.brand = brand;
        this.describeContent = describeContent;
        this.address = address;
        this.transactionMode = transactionMode;
    }
}
