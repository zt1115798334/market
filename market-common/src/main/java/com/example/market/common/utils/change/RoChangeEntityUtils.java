package com.example.market.common.utils.change;

import com.example.market.common.base.entity.ro.*;
import com.example.market.common.mysql.entity.*;
import com.example.market.common.utils.DateUtils;
import com.example.market.common.utils.UserUtils;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:08
 * description:
 */
public class RoChangeEntityUtils {

    public static RoUser resultRoUser(User user, UserImg userImgUrl) {
        return new RoUser(
                user.getId(),
                user.getUserName(),
                user.getPersonalSignature(),
                user.getPhone(),
                user.getSex(),
                user.getIntegral(),
                user.getAccountType(),
                new RoImagePath(userImgUrl.getImgId()));
    }

    public static List<RoUser> resultRoUser(List<User> userList, Map<Long, UserImg> userImgMap) {
        return userList.stream().map(user ->
                RoChangeEntityUtils.resultRoUser(user,
                        userImgMap.getOrDefault(user.getId(), UserUtils.getDefaultUserImg())))
                .collect(toList());
    }



    public static RoCommentStatus resultRoCommentStatus(Comment comment,
                                                        Long userId,
                                                        RoTopicCommentMap topicCommentMap,
                                                        Map<Long, Long> commentReplyNumMap,
                                                        Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap) {
        Long commentId = comment.getId();
        Long commentUserId = comment.getUserId();
        return new RoCommentStatus(
                topicCommentMap.getUserMap().getOrDefault(commentUserId, UserUtils.getDefaultRoUser()),
                comment.getState(),
                Objects.equal(commentUserId, userId),
                topicCommentMap.getZanStateMap().containsKey(commentId),
                topicCommentMap.getZanNumMap().getOrDefault(commentId, 0L),
                commentId,
                comment.getTopicId(),
                comment.getContent(),
                DateUtils.formatDate(comment.getCreatedTime()),
                commentReplyNumMap.getOrDefault(commentId,0L),
                roCommentReplyStatusMap.getOrDefault(commentId, Collections.emptyList()));
    }

    public static List<RoCommentStatus> resultRoCommentStatus(List<Comment> commentList,
                                                              Long userId,
                                                              RoTopicCommentMap topicCommentMap,
                                                              Map<Long, Long> commentReplyNumMap,
                                                              Map<Long, List<RoCommentReplyStatus>> roCommentReplyStatusMap) {
        return commentList.stream()
                .map(comment -> resultRoCommentStatus(comment, userId, topicCommentMap, commentReplyNumMap, roCommentReplyStatusMap)).collect(toList());
    }

    public static RoFruitsTransaction resultRoFruitsTransaction(FruitsTransaction fruitsTransaction, Long userId,
                                                    RoTopicMap topicMethod) {
        Long fruitsTransactionId = fruitsTransaction.getId();
        Long fruitsTransactionUserId = fruitsTransaction.getUserId();
        return new RoFruitsTransaction(
                topicMethod.getUserMap().getOrDefault(fruitsTransactionUserId, UserUtils.getDefaultRoUser()),
                fruitsTransaction.getState(),
                Objects.equal(fruitsTransactionUserId, userId),
                topicMethod.getZanStateMap().containsKey(fruitsTransactionId),
                topicMethod.getZanNumMap().getOrDefault(fruitsTransactionId, 0L),
                topicMethod.getZanUserMap().getOrDefault(fruitsTransactionId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(fruitsTransactionId),
                topicMethod.getCommentCountMap().getOrDefault(fruitsTransactionId, 0L),
                fruitsTransaction.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(fruitsTransactionId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                fruitsTransactionId,
                fruitsTransaction.getFruitsTypeId(),
                fruitsTransaction.getTitle(),
                fruitsTransaction.getPrice(),
                DateUtils.formatDateTime(fruitsTransaction.getListingTime()),
                fruitsTransaction.getBrand(),
                fruitsTransaction.getDescribeContent(),
                fruitsTransaction.getAddress(),
                fruitsTransaction.getTransactionMode());
    }

    public static List<RoFruitsTransaction> resultRoFruitsTransaction(List<FruitsTransaction> fruitsTransactionList,
                                                          Long userId,
                                                          RoTopicMap topicMethod) {
        return fruitsTransactionList.stream()
                .map(fruitsTransaction ->
                        resultRoFruitsTransaction(fruitsTransaction, userId, topicMethod)).collect(toList());
    }

    public static RoGrainTransaction resultRoGrainTransaction(GrainTransaction grainTransaction, Long userId,
                                                    RoTopicMap topicMethod) {
        Long grainTransactionId = grainTransaction.getId();
        Long grainTransactionUserId = grainTransaction.getUserId();
        return new RoGrainTransaction(
                topicMethod.getUserMap().getOrDefault(grainTransactionUserId, UserUtils.getDefaultRoUser()),
                grainTransaction.getState(),
                Objects.equal(grainTransactionUserId, userId),
                topicMethod.getZanStateMap().containsKey(grainTransactionId),
                topicMethod.getZanNumMap().getOrDefault(grainTransactionId, 0L),
                topicMethod.getZanUserMap().getOrDefault(grainTransactionId, Collections.emptyList()),
                topicMethod.getCollectionStateMap().containsKey(grainTransactionId),
                topicMethod.getCommentCountMap().getOrDefault(grainTransactionId, 0L),
                grainTransaction.getBrowsingVolume(),
                topicMethod.getTopicImgMap().getOrDefault(grainTransactionId, Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()),
                grainTransactionId,
                grainTransaction.getGrainTypeId(),
                grainTransaction.getTitle(),
                grainTransaction.getPrice(),
                grainTransaction.getNewOldDegree(),
                grainTransaction.getParticularYear(),
                grainTransaction.getBrand(),
                grainTransaction.getDescribeContent(),
                grainTransaction.getAddress(),
                grainTransaction.getTransactionMode());
    }

    public static List<RoGrainTransaction> resultRoGrainTransaction(List<GrainTransaction> grainTransactionList,
                                                          Long userId,
                                                          RoTopicMap topicMethod) {
        return grainTransactionList.stream()
                .map(grainTransaction ->
                        resultRoGrainTransaction(grainTransaction, userId, topicMethod)).collect(toList());
    }



    public static RoFeedback resultRoFeedback(Feedback feedback, Map<Long, List<Long>> feedbackImgMap) {
        return new RoFeedback(
                feedback.getId(),
                feedback.getFeedbackType(),
                feedback.getContent(),
                feedback.getContactMode(),
                feedbackImgMap.getOrDefault(feedback.getId(), Collections.emptyList())
                        .stream()
                        .map(RoImagePath::new)
                        .collect(toList()));
    }

    public static List<RoFeedback> resultRoFeedback(List<Feedback> feedbackList, Map<Long, List<Long>> feedbackImgMap) {
        return feedbackList.stream()
                .map(feedback -> resultRoFeedback(feedback, feedbackImgMap)).collect(toList());
    }
}
