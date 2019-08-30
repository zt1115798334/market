package com.example.market.timer.task.handler;

import com.example.market.common.mysql.entity.User;
import com.example.market.common.mysql.service.UserService;
import com.example.market.timer.task.page.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/14 16:04
 * description:
 */
@Slf4j
public abstract class UserPageHandler extends PageHandler<User> {

    @Autowired
    private UserService userService;

    protected Page<User> getPageList(int pageNumber) {
        return userService.findPageByEntity(new User());
    }

}
