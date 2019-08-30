package com.example.market.common.mysql.entity;

import com.example.market.common.base.entity.IdEntity;
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
@Table(name = "t_fruits_type")
public class FruitsType extends IdEntity {
    /**
     *
     */
    private Long id;
    /**
     * 水果
     */
    private String fruits;
    /**
     * 水果分组：{1：浆果类，2：柑橘类，3：核果类，4：仁果类,5:瓜类,6:其他}
     */
    private Short fruitsGroup;
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


}
