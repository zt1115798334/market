package com.example.market.common.mysql.service;


import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ro.RoGrainTransaction;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.GrainTransaction;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
public interface GrainTransactionService extends BaseService<GrainTransaction, Long> {

    RoGrainTransaction saveGrainTransaction(GrainTransaction grainTransaction);

    void deleteGrainTransaction(Long id);

    void modifyGrainTransactionSateToNewRelease(Long id);

    void modifyGrainTransactionSateToAfterRelease(List<Long> userId);

    void incrementGrainTransactionBrowsingVolume(Long id);

    GrainTransaction findGrainTransaction(Long id);

    RoGrainTransaction findRoGrainTransaction(Long id, Long userId);

    PageImpl<RoGrainTransaction> findGrainTransactionEffectivePage(GrainTransaction GrainTransaction, Long userId);

    PageImpl<RoGrainTransaction> findGrainTransactionUserPage(GrainTransaction GrainTransaction, Long userId);

    PageImpl<RoGrainTransaction> findGrainTransactionCollectionPage(CustomPage customPage, Long userId);

}
