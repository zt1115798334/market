package com.example.market.common.mysql.entity;

import com.example.market.common.base.entity.IdPageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_grain_transaction")
public class GrainTransaction extends IdPageEntity {
    /**
     * 用户id
     */
    private Long userId;
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
    /**
     * 浏览量
     */
    private Long browsingVolume;
    /**
     * 交易状态{inRelease:发布中，newRelease:新发布，afterRelease:发布后,sellOut:卖出,lowerShelf:下架}
     */
    private String state;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public GrainTransaction(Long id,Long grainTypeId, String title, Double price, Short newOldDegree, String particularYear, String brand, String describeContent, String address, Short transactionMode) {
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

    public GrainTransaction(String sortName, String sortOrder, int pageNumber, int pageSize, LocalDateTime startDateTime, LocalDateTime endDateTime, String searchArea, String searchValue) {
        super(sortName, sortOrder, pageNumber, pageSize, startDateTime, endDateTime, searchArea, searchValue);
    }
}
