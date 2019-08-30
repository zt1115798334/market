package com.example.market.security.shiro.realm;

import com.example.market.common.constant.properties.AccountProperties;
import com.example.market.common.mysql.service.UserService;
import com.example.market.common.redis.StringRedisService;
import com.example.market.common.service.VerificationCodeService;
import com.example.market.common.utils.JwtUtils;
import com.example.market.security.shiro.token.JwtToken;
import com.example.market.security.shiro.token.PasswordToken;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:26
 * description: realm管理器
 */
@AllArgsConstructor
@Component
public class RealmManager {

    private final UserService userService;

    private final AccountProperties accountProperties;

    private final StringRedisService stringRedisService;

    private final VerificationCodeService verificationCodeService;

    private final JwtUtils jwtUtils;


    public List<Realm> initGetRealm() {
        List<Realm> realmList = Lists.newArrayList();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setAccountProperties(accountProperties);
        passwordRealm.setStringRedisService(stringRedisService);
        passwordRealm.setVerificationCodeService(verificationCodeService);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setUserService(userService);
        jwtRealm.setJwtUtils(jwtUtils);
        jwtRealm.setStringRedisService(stringRedisService);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return Collections.unmodifiableList(realmList);
    }
}
