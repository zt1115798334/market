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
@Table(name = "t_grain_type")
public class GrainType extends IdEntity {

    /**
     * 粮食
     */
    private String grain;
    /**
     * 粮食分组：{1：麦类，2：稻类，3：粗粮类，4：其他}
     */
    private Short grainGroup;
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
