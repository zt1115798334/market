package com.example.market.common.mysql.entity;

import com.example.market.common.base.entity.IdPageEntity;
import com.example.market.common.constant.SysConst;
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
 * date: 2018/12/14 13:16
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user")
public class User extends IdPageEntity {

    /**
     * 账户
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 个人签名
     */
    private String personalSignature;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private Short sex;
    /**
     * 积分
     */
    private Long integral;

    /**
     * 账户状态：{0：冻结,1:正常}
     * {@link SysConst.AccountState}
     */
    private Short accountState;

    /**
     * 账户类型：{admin :管理员用户,studentPresident:学生会用户,student:学生用户}
     * {@link SysConst.AccountType}
     */
    private String accountType;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public User(String account, String password, String salt, String userName, String phone, Short sex, Long integral, Short accountState, String accountType) {
        this.account = account;
        this.password = password;
        this.salt = salt;
        this.userName = userName;
        this.phone = phone;
        this.sex = sex;
        this.integral = integral;
        this.accountState = accountState;
        this.accountType = accountType;
    }

    public User(String userName, String personalSignature, String phone,  Short sex) {
        this.userName = userName;
        this.personalSignature = personalSignature;
        this.phone = phone;
        this.sex = sex;
    }
}