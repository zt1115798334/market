package com.example.market.common.mysql.service.impl;

import com.example.market.common.base.entity.ro.RoFeedback;
import com.example.market.common.mysql.entity.Feedback;
import com.example.market.common.mysql.entity.FeedbackImg;
import com.example.market.common.mysql.entity.FileInfo;
import com.example.market.common.mysql.repo.FeedbackRepository;
import com.example.market.common.mysql.service.FeedbackImgService;
import com.example.market.common.mysql.service.FeedbackService;
import com.example.market.common.mysql.service.FileInfoService;
import com.example.market.common.utils.FileUtils;
import com.example.market.common.utils.change.RoChangeEntityUtils;
import com.example.market.common.utils.module.UploadFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@AllArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {


    private final FeedbackRepository feedbackRepository;

    private final FeedbackImgService feedbackImgService;

    private final FileInfoService fileInfoService;

    @Override
    public Feedback save(Feedback feedback) {
        feedback.setDeleteState(UN_DELETED);
        feedback.setCreatedTime(currentDateTime());
        return feedbackRepository.save(feedback);
    }

    @Override
    public RoFeedback saveFeedback(Feedback feedback) {
        feedback = this.save(feedback);
        return resultRoFeedback(feedback);
    }

    @Override
    public void saveFeedbackImg(HttpServletRequest request, Long feedbackId) {
        String folderPath = FileUtils.getFolderPath(FOLDER_FEEDBACK_IMG);
        List<UploadFile> uploadFile = FileUtils.batchUploadFile(request, folderPath);
        List<FileInfo> fileInfoList = fileInfoService.saveFileInfo(uploadFile);
        List<FeedbackImg> feedbackImgList = fileInfoList.stream().map(FileInfo::getId)
                .map(fileInfoId ->
                        new FeedbackImg(feedbackId, fileInfoId, currentDateTime(), UN_DELETED))
                .collect(toList());
        feedbackImgService.saveAll(feedbackImgList);
    }

    private RoFeedback resultRoFeedback(Feedback feedback) {
        Map<Long, List<Long>> feedbackImgMap = feedbackImgService.findByFeedbackId(feedback.getId());
        return RoChangeEntityUtils.resultRoFeedback(feedback, feedbackImgMap);
    }
}
