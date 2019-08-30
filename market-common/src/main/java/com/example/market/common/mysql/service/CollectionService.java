package com.example.market.common.mysql.service;

import com.example.market.common.base.entity.IdPageEntity;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.Collection;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/19 11:06
 * description:
 */
public interface CollectionService extends BaseService<Collection, Long> {

    void enableOnCollection(Long userId, Long topicId, Short topicType);

    void enableOffCollection(Long userId, Long topicId, Short topicType);

    Map<Long, Long> collectionState(Long userId, List<Long> topicId, Short topicType);

    PageImpl<Long> findCollection(Long userId, Short topicType, IdPageEntity pageEntity);
}
