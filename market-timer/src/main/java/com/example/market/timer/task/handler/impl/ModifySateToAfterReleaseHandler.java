package com.example.market.timer.task.handler.impl;

import com.example.market.common.mysql.entity.User;
import com.example.market.common.mysql.service.*;
import com.example.market.timer.task.handler.UserPageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.IOP.TransactionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/24 15:25
 * description:
 */
@AllArgsConstructor
@Slf4j
@Component("modifySateToAfterReleaseHandler")
public class ModifySateToAfterReleaseHandler extends UserPageHandler {

    private final GrainTransactionService grainTransactionService;

    private final FruitsTransactionService fruitsTransactionService;

    @Override
    protected int handleDataOfPerPage(List<User> list, int pageNumber) {
        List<Long> userIdList = list.parallelStream().map(User::getId).collect(Collectors.toList());
        grainTransactionService.modifyGrainTransactionSateToAfterRelease(userIdList);
        fruitsTransactionService.modifyFruitsTransactionSateToAfterRelease(userIdList);
        return list.size();
    }
}
