package com.example.market.common.mysql.service;

import com.example.market.common.base.entity.ro.RoCommentReplyStatus;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.CommentReply;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 18:44
 * description:
 */
public interface CommentReplyService extends BaseService<CommentReply, Long> {

    CommentReply saveCommentReplyToComment(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    CommentReply saveCommentReplyToReply(Long topicId, Short topicType, Long commentId, Long replyId, String content, Long toUserId, Long fromUserId);

    List<CommentReply> findCommentReplyList(Long commentId);

    List<CommentReply> findCommentReplyList(List<Long> commentIdList);

    List<RoCommentReplyStatus> findRoCommentReplyStatusList(Long commentId);

    List<RoCommentReplyStatus> findRoCommentReplyStatusList(List<Long> commentIdList);

}
