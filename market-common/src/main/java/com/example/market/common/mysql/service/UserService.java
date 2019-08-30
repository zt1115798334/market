package com.example.market.common.mysql.service;

import com.example.market.common.base.entity.ro.RoUser;
import com.example.market.common.base.entity.vo.VoUser;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.User;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 17:27
 * description:
 */
public interface UserService extends BaseService<User, Long> {

    void updateLastLoginTime(Long userId);

    void saveUser(String account, String password);

    void saveUser(String account, String password, String accountType);

    RoUser saveUser(User user);

    void modifyPassword(String account, String password);

    void deleteUser(Long userId);

    void normalUser(Long userId);

    void increaseIntegral(Long sellerUserId, Long integral);

    void reduceIntegral(Long buyerUserId, Long integral);

    Optional<User> findOptByUserId(Long userId);

    Optional<User> findOptByAccount(String account);

    User findByAccount(String account);

    User findByUserId(Long userId);

    RoUser findRoUserByUserId(Long userId);

    List<RoUser> findRoUserByUserId(List<Long> userIdList);

    Map<Long, RoUser> findMapRoUserByUserId(List<Long> userIdList);

    PageImpl<RoUser> findRoUser(VoUser voUser);

    void validateAccountByRegister(String account);

    void validateAccountByForget(String account);

}
