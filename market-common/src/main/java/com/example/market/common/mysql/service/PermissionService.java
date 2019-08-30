package com.example.market.common.mysql.service;

import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.Permission;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/15 17:20
 * description:
 */
public interface PermissionService extends BaseService<Permission, Long> {

    void saveSysSystemPermission(Long userId, String accountType);

    List<Permission> findAdminPermission();

    List<Permission> findUserPermission();

    List<Permission> findByUserId(Long userId);

}
