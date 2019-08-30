package com.example.market.common.base.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
public class RoFruitsTransaction extends RoTopic {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 水果种类id
     */
    private Long fruitsTypeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 价格
     */
    private Double price;
    /**
     * 上市时间
     */
    private String listingTime;
    /**
     * 水果品牌
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
    /**
     * 浏览量
     */
    private Long browsingVolume;


    public RoFruitsTransaction(RoUser user, String state, boolean userState, boolean zanState, Long zanNum, List<RoUser> zanUsers, boolean collectionState, Long commentNum, Long browsingVolume, List<RoImagePath> topicImgList,
                               Long id, Long fruitsTypeId, String title, Double price, String listingTime, String brand, String describeContent, String address, Short transactionMode) {
        super(user, state, userState, zanState, zanNum, zanUsers, collectionState, commentNum, browsingVolume, topicImgList);
        this.id = id;
        this.fruitsTypeId = fruitsTypeId;
        this.title = title;
        this.price = price;
        this.listingTime = listingTime;
        this.brand = brand;
        this.describeContent = describeContent;
        this.address = address;
        this.transactionMode = transactionMode;
    }

}
