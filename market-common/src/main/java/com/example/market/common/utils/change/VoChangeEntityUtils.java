package com.example.market.common.utils.change;

import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.vo.*;
import com.example.market.common.mysql.entity.*;
import com.example.market.common.utils.DateUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/19 18:04
 * description:
 */
public class VoChangeEntityUtils {

    public static CustomPage changeIdPageEntity(VoPage page) {
        return new CustomPage(page.getPageNumber(), page.getPageSize());
    }

    public static User changeUser(VoStorageUser voStorageUser) {
        return new User(voStorageUser.getUserName(),
                voStorageUser.getPersonalSignature(),
                voStorageUser.getPhone(),
                voStorageUser.getSex());
    }

    public static Comment changeComment(VoCommentPage commentPage) {
        return new Comment(commentPage.getSortName(),
                commentPage.getSortOrder(),
                commentPage.getPageNumber(),
                commentPage.getPageSize(),
                commentPage.getTopicId());
    }

    public static CommentReply changeCommentReply(VoCommentReplyPage commentReplyPage) {
        return new CommentReply(commentReplyPage.getSortName(),
                commentReplyPage.getSortOrder(),
                commentReplyPage.getPageNumber(),
                commentReplyPage.getPageSize(),
                commentReplyPage.getCommentId());
    }

    public static FruitsTransaction changeFruitsTransaction(VoParams voParams) {
        DateUtils.DateRange dateTimeRange = DateUtils.findDateTimeRange(voParams.getTimeType(), voParams.getStartDateTime(), voParams.getEndDateTime());
        return new FruitsTransaction(voParams.getSortName(),
                voParams.getSortOrder(),
                voParams.getPageNumber(),
                voParams.getPageSize(),
                dateTimeRange.getStartDateTime(),
                dateTimeRange.getEndDateTime(),
                voParams.getSearchArea(),
                voParams.getSearchValue());
    }

    public static FruitsTransaction changeStorageFruitsTransaction(VoStorageFruitsTransaction storageFruitsTransaction) {
        return new FruitsTransaction(storageFruitsTransaction.getId(),
                storageFruitsTransaction.getFruitsTypeId(),
                storageFruitsTransaction.getTitle(),
                storageFruitsTransaction.getPrice(),
                DateUtils.parseDateTime(storageFruitsTransaction.getListingTime()),
                storageFruitsTransaction.getBrand(),
                storageFruitsTransaction.getDescribeContent(),
                storageFruitsTransaction.getAddress(),
                storageFruitsTransaction.getTransactionMode());
    }


    public static GrainTransaction changeGrainTransaction(VoParams voParams) {
        DateUtils.DateRange dateTimeRange = DateUtils.findDateTimeRange(voParams.getTimeType(), voParams.getStartDateTime(), voParams.getEndDateTime());
        return new GrainTransaction(voParams.getSortName(),
                voParams.getSortOrder(),
                voParams.getPageNumber(),
                voParams.getPageSize(),
                dateTimeRange.getStartDateTime(),
                dateTimeRange.getEndDateTime(),
                voParams.getSearchArea(),
                voParams.getSearchValue());
    }

    public static GrainTransaction changeStorageGrainTransaction(VoStorageGrainTransaction storageGrainTransaction) {
        return new GrainTransaction(storageGrainTransaction.getId(),
                storageGrainTransaction.getGrainTypeId(),
                storageGrainTransaction.getTitle(),
                storageGrainTransaction.getPrice(),
                storageGrainTransaction.getNewOldDegree(),
                storageGrainTransaction.getParticularYear(),
                storageGrainTransaction.getBrand(),
                storageGrainTransaction.getDescribeContent(),
                storageGrainTransaction.getAddress(),
                storageGrainTransaction.getTransactionMode());
    }


    public static Feedback changeStorageFeedback(VoStorageFeedback storageFeedback) {
        return new Feedback(storageFeedback.getFeedbackType(),
                storageFeedback.getContent(),
                storageFeedback.getContactMode());
    }
}
