package com.example.market.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ResultMessage;
import com.example.market.common.base.entity.ro.RoCommentReplyStatus;
import com.example.market.common.base.entity.ro.RoCommentStatus;
import com.example.market.common.base.entity.ro.RoGrainTransaction;
import com.example.market.common.base.entity.vo.VoCommentPage;
import com.example.market.common.base.entity.vo.VoPage;
import com.example.market.common.base.entity.vo.VoParams;
import com.example.market.common.base.entity.vo.VoStorageGrainTransaction;
import com.example.market.common.base.service.ConstantService;
import com.example.market.common.base.web.AbstractController;
import com.example.market.common.mysql.entity.Comment;
import com.example.market.common.mysql.entity.CommentReply;
import com.example.market.common.mysql.entity.GrainTransaction;
import com.example.market.common.mysql.service.*;
import com.example.market.common.utils.change.VoChangeEntityUtils;
import com.example.market.security.aop.DistributedLock;
import com.example.market.security.aop.SaveLog;
import com.example.market.security.base.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 17:58
 * description:
 */
@Api(tags = "农作物交易")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/grainTransaction")
public class GrainTransactionController extends AbstractController implements CurrentUser, ConstantService {

    private final GrainTypeService grainTypeService;

    private final GrainTransactionService grainTransactionService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;


    @ApiOperation(value = "查询农作物分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainType")
    public ResultMessage findGrainType() {
        JSONObject fruitsType = grainTypeService.findGrainType();
        return success(fruitsType);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveGrainTransaction")
    @SaveLog(desc = "保存农作物交易信息")
    @DistributedLock
    public ResultMessage saveGrainTransaction(@Valid @RequestBody VoStorageGrainTransaction storageGrainTransaction) {
        GrainTransaction grainTransaction = VoChangeEntityUtils.changeStorageGrainTransaction(storageGrainTransaction);
        grainTransaction.setUserId(getCurrentUserId());
        RoGrainTransaction roGrainTransaction = grainTransactionService.saveGrainTransaction(grainTransaction);
        return success("保存成功", roGrainTransaction);
    }


    @ApiOperation(value = "保存农作物交易图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveGrainTransactionImg")
    @SaveLog(desc = "保存农作物交易图片信息")
    @DistributedLock
    public ResultMessage saveGrainTransactionImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        grainTransactionService.modifyGrainTransactionSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, GRAIN_TRANSACTION);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteGrainTransaction")
    @SaveLog(desc = "删除农作物交易信息")
    @DistributedLock
    public ResultMessage deleteGrainTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) {
        grainTransactionService.deleteGrainTransaction(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransaction")
    public ResultMessage findGrainTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoGrainTransaction grainTransaction = grainTransactionService.findRoGrainTransaction(id, getCurrentUserId());
        return success(grainTransaction);
    }

    @ApiOperation(value = "查询有效的农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionEffective")
    public ResultMessage findGrainTransactionEffective(@Valid @RequestBody VoParams params) {
        GrainTransaction grainTransaction = VoChangeEntityUtils.changeGrainTransaction(params);
        PageImpl<RoGrainTransaction> page = grainTransactionService.findGrainTransactionEffectivePage(grainTransaction, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionUser")
    public ResultMessage findGrainTransactionUser(@Valid @RequestBody VoParams params) {
        GrainTransaction grainTransaction = VoChangeEntityUtils.changeGrainTransaction(params);
        Long currentUserId = getCurrentUserId();
        grainTransaction.setUserId(currentUserId);
        PageImpl<RoGrainTransaction> page = grainTransactionService.findGrainTransactionUserPage(grainTransaction, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户收藏的农作物交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionCollection")
    public ResultMessage findGrainTransactionCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoGrainTransaction> page = grainTransactionService.findGrainTransactionCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存农作物交易信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableGrainTransactionZanOn")
    @SaveLog(desc = "保存农作物交易信息点赞")
    @DistributedLock
    public ResultMessage enableGrainTransactionZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                     @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, GRAIN_TRANSACTION, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存农作物交易信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableGrainTransactionZanOff")
    @SaveLog(desc = "保存农作物交易信息取消点赞")
    @DistributedLock
    public ResultMessage enableGrainTransactionZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                      @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, GRAIN_TRANSACTION, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存农作物交易信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableGrainTransactionCollectionOn")
    @SaveLog(desc = "保存农作物交易信息收藏")
    @DistributedLock
    public ResultMessage enableGrainTransactionCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, GRAIN_TRANSACTION);
        return success("保存成功");
    }

    @ApiOperation(value = "保存农作物交易信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableGrainTransactionCollectionOff")
    @SaveLog(desc = "保存农作物交易信息取消收藏")
    @DistributedLock
    public ResultMessage enableGrainTransactionCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, GRAIN_TRANSACTION);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示农作物交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionComment")
    public ResultMessage findGrainTransactionComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(GRAIN_TRANSACTION);
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示农作物交易信息评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionCommentCount")
    public ResultMessage findGrainTransactionCommentCount(@NotNull(message = "topicId不能为空") @RequestParam Long topicId) {
        JSONObject result = commentService.countComment(topicId, GRAIN_TRANSACTION);
        return success(result);
    }

    @ApiOperation(value = "显示农作物交易信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionCommentReply")
    public ResultMessage findGrainTransactionCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示农作物交易信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findGrainTransactionCommentAndReply")
    public ResultMessage findGrainTransactionCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(GRAIN_TRANSACTION);
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存农作物交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveGrainTransactionComment")
    @SaveLog(desc = "保存农作物交易信息评论")
    @DistributedLock
    public ResultMessage saveGrainTransactionComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                     @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                     @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, GRAIN_TRANSACTION, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存农作物交易信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveGrainTransactionCommentReplyToComment")
    @SaveLog(desc = "保存农作物交易信息回复")
    @DistributedLock
    public ResultMessage saveGrainTransactionCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                                   @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                                   @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                                   @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, GRAIN_TRANSACTION, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存农作物交易信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveGrainTransactionCommentReplyToReply")
    @SaveLog(desc = "保存农作物交易信息回复的回复")
    @DistributedLock
    public ResultMessage saveGrainTransactionCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                                 @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                                 @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                                 @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, GRAIN_TRANSACTION, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存农作物交易信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableGrainTransactionCommentZanOn")
    @SaveLog(desc = "保存农作物交易信息评论点赞")
    @DistributedLock
    public ResultMessage enableGrainTransactionCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, GRAIN_TRANSACTION, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存农作物交易信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableGrainTransactionCommentZanOff")
    @SaveLog(desc = "保存农作物交易信息点赞")
    @DistributedLock
    public ResultMessage enableGrainTransactionCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                             @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, GRAIN_TRANSACTION, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

}
