package com.example.market.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ResultMessage;
import com.example.market.common.base.entity.ro.RoCommentReplyStatus;
import com.example.market.common.base.entity.ro.RoCommentStatus;
import com.example.market.common.base.entity.ro.RoFruitsTransaction;
import com.example.market.common.base.entity.vo.VoCommentPage;
import com.example.market.common.base.entity.vo.VoPage;
import com.example.market.common.base.entity.vo.VoParams;
import com.example.market.common.base.entity.vo.VoStorageFruitsTransaction;
import com.example.market.common.base.service.ConstantService;
import com.example.market.common.base.web.AbstractController;
import com.example.market.common.mysql.entity.Comment;
import com.example.market.common.mysql.entity.CommentReply;
import com.example.market.common.mysql.entity.FruitsTransaction;
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

import static com.example.market.common.constant.SysConst.TopicType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 17:58
 * description:
 */
@Api(tags = "水果交易")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("app/fruitsTransaction")
public class FruitsTransactionController extends AbstractController implements CurrentUser, ConstantService {

    private final FruitsTypeService fruitsTypeService;

    private final FruitsTransactionService fruitsTransactionService;

    private final TopicImgService topicImgService;

    private final CommentService commentService;

    private final CommentReplyService commentReplyService;

    private final ZanService zanService;

    private final CollectionService collectionService;


    @ApiOperation(value = "查询水果分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsType")
    public ResultMessage findFruitsType() {
        JSONObject fruitsType = fruitsTypeService.findFruitsType();
        return success(fruitsType);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 发布
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFruitsTransaction")
    @SaveLog(desc = "保存水果交易信息")
    @DistributedLock
    public ResultMessage saveFruitsTransaction(@Valid @RequestBody VoStorageFruitsTransaction storageFruitsTransaction) {
        FruitsTransaction fruitsTransaction = VoChangeEntityUtils.changeStorageFruitsTransaction(storageFruitsTransaction);
        fruitsTransaction.setUserId(getCurrentUserId());
        RoFruitsTransaction roFruitsTransaction = fruitsTransactionService.saveFruitsTransaction(fruitsTransaction);
        return success("保存成功", roFruitsTransaction);
    }


    @ApiOperation(value = "保存水果交易图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile"),
            @ApiImplicitParam(paramType = "query", name = "topicId", dataType = "String")
    })
    @PostMapping(value = "saveFruitsTransactionImg")
    @SaveLog(desc = "保存水果交易图片信息")
    @DistributedLock
    public ResultMessage saveFruitsTransactionImg(HttpServletRequest request) {
        Long topicId = Long.valueOf(request.getParameter("topicId"));
        fruitsTransactionService.modifyFruitsTransactionSateToNewRelease(topicId);
        topicImgService.saveTopicImgFile(request, topicId, FRUITS_TRANSACTION);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 删除
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "删除水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "deleteFruitsTransaction")
    @SaveLog(desc = "删除水果交易信息")
    @DistributedLock
    public ResultMessage deleteFruitsTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) {
        fruitsTransactionService.deleteFruitsTransaction(id);
        return success("删除成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 展示
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "查询水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransaction")
    public ResultMessage findFruitsTransaction(@NotNull(message = "id不能为空") @RequestParam Long id) {
        RoFruitsTransaction fruitsTransaction = fruitsTransactionService.findRoFruitsTransaction(id, getCurrentUserId());
        return success(fruitsTransaction);
    }

    @ApiOperation(value = "查询有效的水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionEffective")
    public ResultMessage findFruitsTransactionEffective(@Valid @RequestBody VoParams params) {
        FruitsTransaction fruitsTransaction = VoChangeEntityUtils.changeFruitsTransaction(params);
        PageImpl<RoFruitsTransaction> page = fruitsTransactionService.findFruitsTransactionEffectivePage(fruitsTransaction, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户相关的水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionUser")
    public ResultMessage findFruitsTransactionUser(@Valid @RequestBody VoParams params) {
        FruitsTransaction fruitsTransaction = VoChangeEntityUtils.changeFruitsTransaction(params);
        Long currentUserId = getCurrentUserId();
        fruitsTransaction.setUserId(currentUserId);
        PageImpl<RoFruitsTransaction> page = fruitsTransactionService.findFruitsTransactionUserPage(fruitsTransaction, currentUserId);
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    @ApiOperation(value = "查询用户收藏的水果交易信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionCollection")
    public ResultMessage findFruitsTransactionCollection(@Valid @RequestBody VoPage voPage) {
        CustomPage customPage = VoChangeEntityUtils.changeIdPageEntity(voPage);
        PageImpl<RoFruitsTransaction> page = fruitsTransactionService.findFruitsTransactionCollectionPage(customPage, getCurrentUserId());
        return success(page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements(), page.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存水果交易信息点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })

    @PostMapping(value = "enableFruitsTransactionZanOn")
    @SaveLog(desc = "保存水果交易信息点赞")
    @DistributedLock
    public ResultMessage enableFruitsTransactionZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, FRUITS_TRANSACTION, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存水果交易信息取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableFruitsTransactionZanOff")
    @SaveLog(desc = "保存水果交易信息取消点赞")
    @DistributedLock
    public ResultMessage enableFruitsTransactionZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                 @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, FRUITS_TRANSACTION, ZAN_TOPIC, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 收藏
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存水果交易信息收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableFruitsTransactionCollectionOn")
    @SaveLog(desc = "保存水果交易信息收藏")
    @DistributedLock
    public ResultMessage enableFruitsTransactionCollectionOn(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOnCollection(getCurrentUserId(), id, FRUITS_TRANSACTION);
        return success("保存成功");
    }

    @ApiOperation(value = "保存水果交易信息取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableFruitsTransactionCollectionOff")
    @SaveLog(desc = "保存水果交易信息取消收藏")
    @DistributedLock
    public ResultMessage enableFruitsTransactionCollectionOff(@NotNull(message = "id不能为空") @RequestParam Long id) {
        collectionService.enableOffCollection(getCurrentUserId(), id, FRUITS_TRANSACTION);
        return success("保存成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 查看评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "显示水果交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionComment")
    public ResultMessage findFruitsTransactionComment(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(FRUITS_TRANSACTION);
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    @ApiOperation(value = "显示水果交易信息评论数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionCommentCount")
    public ResultMessage findFruitsTransactionCommentCount(@NotNull(message = "topicId不能为空") @RequestParam Long topicId) {
        JSONObject result = commentService.countComment(topicId, FRUITS_TRANSACTION);
        return success(result);
    }

    @ApiOperation(value = "显示水果交易信息评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionCommentReply")
    public ResultMessage findFruitsTransactionCommentReply(@NotNull(message = "commentId不能为空") @RequestParam Long commentId) {
        List<RoCommentReplyStatus> roCommentStatusList = commentReplyService.findRoCommentReplyStatusList(commentId);
        return success(roCommentStatusList);
    }

    @ApiOperation(value = "显示水果交易信息评论和评论回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "findFruitsTransactionCommentAndReply")
    public ResultMessage findFruitsTransactionCommentAndReply(@RequestBody VoCommentPage voCommentPage) {
        Comment comment = VoChangeEntityUtils.changeComment(voCommentPage);
        comment.setTopicType(FRUITS_TRANSACTION);
        PageImpl<RoCommentStatus> roCommentStatusPage = commentService.findRoCommentAndReplyStatusPage(comment, getCurrentUserId());
        return success(roCommentStatusPage.getPageable().getPageNumber(), roCommentStatusPage.getPageable().getPageSize(), roCommentStatusPage.getTotalElements(), roCommentStatusPage.getContent());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 保存评论
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存水果交易信息评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFruitsTransactionComment")
    @SaveLog(desc = "保存水果交易信息评论")
    @DistributedLock
    public ResultMessage saveFruitsTransactionComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        Comment comment = commentService.saveComment(topicId, FRUITS_TRANSACTION, content, getCurrentUserId(), fromUserId);
        return success("保存成功", comment);
    }

    @ApiOperation(value = "保存水果交易信息回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFruitsTransactionCommentReplyToComment")
    @SaveLog(desc = "保存水果交易信息回复")
    @DistributedLock
    public ResultMessage saveFruitsTransactionCommentReplyToComment(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                              @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                              @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                              @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToComment(topicId, FRUITS_TRANSACTION, commentId, commentId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    @ApiOperation(value = "保存水果交易信息回复的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "saveFruitsTransactionCommentReplyToReply")
    @SaveLog(desc = "保存水果交易信息回复的回复")
    @DistributedLock
    public ResultMessage saveFruitsTransactionCommentReplyToReply(@NotNull(message = "topicId不能为空") @RequestParam Long topicId,
                                                            @NotNull(message = "commentId不能为空") @RequestParam Long commentId,
                                                            @NotNull(message = "replyId不能为空") @RequestParam Long replyId,
                                                            @NotEmpty(message = "content不能为空") @RequestParam String content,
                                                            @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        CommentReply commentReply = commentReplyService.saveCommentReplyToReply(topicId, FRUITS_TRANSACTION, commentId, replyId, content, getCurrentUserId(), fromUserId);
        return success("保存成功", commentReply);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 评论点赞
    ///////////////////////////////////////////////////////////////////////////
    @ApiOperation(value = "保存水果交易信息评论点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableFruitsTransactionCommentZanOn")
    @SaveLog(desc = "保存水果交易信息评论点赞")
    @DistributedLock
    public ResultMessage enableFruitsTransactionCommentZanOn(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                       @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOnZan(id, FRUITS_TRANSACTION, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

    @ApiOperation(value = "保存水果交易信息评论取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "authorization", dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "deviceInfo", dataType = "String", defaultValue = "mobile")
    })
    @PostMapping(value = "enableFruitsTransactionCommentZanOff")
    @SaveLog(desc = "保存水果交易信息点赞")
    @DistributedLock
    public ResultMessage enableFruitsTransactionCommentZanOff(@NotNull(message = "id不能为空") @RequestParam Long id,
                                                        @NotNull(message = "fromUserId不能为空") @RequestParam Long fromUserId) {
        zanService.enableOffZan(id, FRUITS_TRANSACTION, ZAN_COMMENT, getCurrentUserId(), fromUserId);
        return success("保存成功");
    }

}
