package com.example.market.common.mysql.service;

import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ro.RoFruitsTransaction;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.FruitsTransaction;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
public interface FruitsTransactionService extends BaseService<FruitsTransaction, Long> {

    RoFruitsTransaction saveFruitsTransaction(FruitsTransaction fruitsTransaction);

    void deleteFruitsTransaction(Long id);

    void modifyFruitsTransactionSateToNewRelease(Long id);

    void modifyFruitsTransactionSateToAfterRelease(List<Long> userId);

    void incrementFruitsTransactionBrowsingVolume(Long id);

    FruitsTransaction findFruitsTransaction(Long id);

    RoFruitsTransaction findRoFruitsTransaction(Long id, Long userId);

    PageImpl<RoFruitsTransaction> findFruitsTransactionEffectivePage(FruitsTransaction FruitsTransaction, Long userId);

    PageImpl<RoFruitsTransaction> findFruitsTransactionUserPage(FruitsTransaction FruitsTransaction, Long userId);

    PageImpl<RoFruitsTransaction> findFruitsTransactionCollectionPage(CustomPage customPage, Long userId);


}
