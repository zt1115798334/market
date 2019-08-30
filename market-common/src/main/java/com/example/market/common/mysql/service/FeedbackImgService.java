package com.example.market.common.mysql.service;

import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.FeedbackImg;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
public interface FeedbackImgService extends BaseService<FeedbackImg, Long> {

    Map<Long, List<Long>> findByFeedbackId(Long feedbackId);

    Map<Long, List<Long>> findByFeedbackIds(List<Long> feedbackIds);

}
