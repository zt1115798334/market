package com.example.market.common.mysql.service.impl;

import com.example.market.common.base.entity.ro.*;
import com.example.market.common.mysql.entity.*;
import com.example.market.common.mysql.service.*;
import com.example.market.common.utils.change.RoChangeEntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Component
public class TopicServiceImpl implements TopicService {

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final ZanService zanService;

    private final UserService userService;

    private final CollectionService collectionService;

    private RoTopicMap getTopicMethod(List<Long> userIdList, Long userId, List<Long> topicId, Short topicTyp) {
        Map<Long, RoUser> userMap = userService.findMapRoUserByUserId(userIdList);
        Map<Long, List<Long>> topicImgMap = topicImgService.findTopicImgList(topicId, topicTyp);
        Map<Long, Long> zanNumMap = zanService.countZan(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> zanStateMap = zanService.zanState(userId, topicId, topicTyp, ZAN_TOPIC);
        Map<Long, List<RoUser>> zanUserMap = zanService.zanUser(topicId, topicTyp, ZAN_TOPIC);
        Map<Long, Long> commentCountMap = commentService.countComment(topicId, topicTyp);
        Map<Long, Long> collectionStateMap = collectionService.collectionState(userId, topicId, topicTyp);
        return new RoTopicMap(userMap, topicImgMap, zanNumMap, zanStateMap, collectionStateMap, commentCountMap, zanUserMap);
    }


    @Override
    public RoFruitsTransaction resultRoFruitsTransaction(FruitsTransaction fruitsTransaction, Long userId) {
        List<Long> topicId = Collections.singletonList(fruitsTransaction.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(fruitsTransaction.getUserId()), userId, topicId, FRUITS_TRANSACTION);
        return RoChangeEntityUtils.resultRoFruitsTransaction(fruitsTransaction, userId, topicMethod);
    }

    @Override
    public PageImpl<RoFruitsTransaction> resultRoFruitsTransactionPage(Page<FruitsTransaction> page, Long userId) {
        List<Long> topicId = page.stream().map(FruitsTransaction::getId).collect(toList());
        List<Long> userIdList = page.stream().map(FruitsTransaction::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, FRUITS_TRANSACTION);
        List<RoFruitsTransaction> roFruitsTransactionList = RoChangeEntityUtils
                .resultRoFruitsTransaction(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roFruitsTransactionList, page.getPageable(), page.getTotalElements());
    }

    @Override
    public RoGrainTransaction resultRoGrainTransaction(GrainTransaction grainTransaction, Long userId) {
        List<Long> topicId = Collections.singletonList(grainTransaction.getId());
        RoTopicMap topicMethod = getTopicMethod(Collections.singletonList(grainTransaction.getUserId()), userId, topicId, GRAIN_TRANSACTION);
        return RoChangeEntityUtils.resultRoGrainTransaction(grainTransaction, userId, topicMethod);
    }

    @Override
    public PageImpl<RoGrainTransaction> resultRoGrainTransactionPage(Page<GrainTransaction> page, Long userId) {
        List<Long> topicId = page.stream().map(GrainTransaction::getId).collect(toList());
        List<Long> userIdList = page.stream().map(GrainTransaction::getUserId).distinct().collect(toList());
        RoTopicMap topicMethod = getTopicMethod(userIdList, userId, topicId, GRAIN_TRANSACTION);
        List<RoGrainTransaction> roGrainTransactionList = RoChangeEntityUtils
                .resultRoGrainTransaction(page.getContent(), userId, topicMethod);
        return new PageImpl<>(roGrainTransactionList, page.getPageable(), page.getTotalElements());
    }


}
