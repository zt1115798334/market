package com.example.market.common.mysql.service.impl;

import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ro.RoFruitsTransaction;
import com.example.market.common.base.service.PageUtils;
import com.example.market.common.base.service.SearchFilter;
import com.example.market.common.exception.custom.OperationException;
import com.example.market.common.mysql.entity.FruitsTransaction;
import com.example.market.common.mysql.repo.FruitsTransactionRepository;
import com.example.market.common.mysql.service.CollectionService;
import com.example.market.common.mysql.service.FruitsTransactionService;
import com.example.market.common.mysql.service.TopicService;
import com.example.market.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.market.common.base.service.SearchFilter.Operator;
import static com.example.market.common.base.service.SearchFilter.bySearchFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class FruitsTransactionServiceImpl implements FruitsTransactionService {

    private final FruitsTransactionRepository fruitsTransactionRepository;

    private final TopicService topicService;

    private final CollectionService collectionService;

    @Override
    public FruitsTransaction save(FruitsTransaction fruitsTransaction) {
        Long id = fruitsTransaction.getId();
        if (id != null && id != 0L) {
            Optional<FruitsTransaction> fruitsTransactionOptional = findByIdNotDelete(id);
            if (fruitsTransactionOptional.isPresent()) {
                FruitsTransaction fruitsTransactionDB = fruitsTransactionOptional.get();
                fruitsTransactionDB.setTitle(fruitsTransaction.getTitle());
                fruitsTransactionDB.setPrice(fruitsTransaction.getPrice());
                fruitsTransactionDB.setDescribeContent(fruitsTransaction.getDescribeContent());
                fruitsTransactionDB.setAddress(fruitsTransaction.getAddress());
                fruitsTransactionDB.setUpdatedTime(DateUtils.currentDateTime());
                return fruitsTransactionRepository.save(fruitsTransactionDB);
            }
            return null;
        } else {
            fruitsTransaction.setBrowsingVolume(0L);
            fruitsTransaction.setState(IN_RELEASE);
            fruitsTransaction.setCreatedTime(currentDateTime());
            fruitsTransaction.setDeleteState(UN_DELETED);
            return fruitsTransactionRepository.save(fruitsTransaction);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        fruitsTransactionRepository.findById(aLong).ifPresent(FruitsTransaction -> {
            FruitsTransaction.setDeleteState(DELETED);
            fruitsTransactionRepository.save(FruitsTransaction);
        });
    }

    @Override
    public Optional<FruitsTransaction> findByIdNotDelete(Long aLong) {
        return fruitsTransactionRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<FruitsTransaction> findByIdsNotDelete(List<Long> id) {
        return fruitsTransactionRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<FruitsTransaction> findPageByEntity(FruitsTransaction FruitsTransaction) {
        return null;
    }

    public RoFruitsTransaction saveFruitsTransaction(FruitsTransaction FruitsTransaction) {
        FruitsTransaction = this.save(FruitsTransaction);
        return topicService.resultRoFruitsTransaction(FruitsTransaction, FruitsTransaction.getUserId());
    }

    @Override
    public void deleteFruitsTransaction(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyFruitsTransactionSateToNewRelease(Long id) {
        fruitsTransactionRepository.findById(id).ifPresent(FruitsTransaction -> {
            FruitsTransaction.setState(NEW_RELEASE);
            fruitsTransactionRepository.save(FruitsTransaction);
        });
    }


    @Override
    public void modifyFruitsTransactionSateToAfterRelease(List<Long> userId) {
        fruitsTransactionRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public void incrementFruitsTransactionBrowsingVolume(Long id) {
        fruitsTransactionRepository.incrementBrowsingVolume(id);
    }

    @Override
    public FruitsTransaction findFruitsTransaction(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoFruitsTransaction findRoFruitsTransaction(Long id, Long userId) {
        FruitsTransaction FruitsTransaction = this.findFruitsTransaction(id);
        this.incrementFruitsTransactionBrowsingVolume(id);
        return topicService.resultRoFruitsTransaction(FruitsTransaction, userId);
    }


    @Override
    public PageImpl<RoFruitsTransaction> findFruitsTransactionEffectivePage(FruitsTransaction fruitsTransaction, Long userId) {
        List<SearchFilter> filters = getFruitsTransactionFilter(getEffectiveState(), fruitsTransaction);
        return getRoFruitsTransactionCustomPage(fruitsTransaction, userId, filters);

    }

    @Override
    public PageImpl<RoFruitsTransaction> findFruitsTransactionUserPage(FruitsTransaction fruitsTransaction, Long userId) {
        List<SearchFilter> filters = getFruitsTransactionFilter(getEffectiveState(), fruitsTransaction);
        filters.add(new SearchFilter("userId", fruitsTransaction.getUserId(), Operator.EQ));
        return getRoFruitsTransactionCustomPage(fruitsTransaction, userId, filters);

    }

    @Override
    public PageImpl<RoFruitsTransaction> findFruitsTransactionCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, FRUITS_TRANSACTION, customPage);
        List<FruitsTransaction> FruitsTransactionList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoFruitsTransactionPage(new PageImpl<>(FruitsTransactionList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoFruitsTransaction> getRoFruitsTransactionCustomPage(FruitsTransaction fruitsTransaction, Long userId, List<SearchFilter> filters) {
        Specification<FruitsTransaction> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(fruitsTransaction);
        Page<FruitsTransaction> page = fruitsTransactionRepository.findAll(specification, pageable);
        return topicService.resultRoFruitsTransactionPage(page, userId);
    }

    private List<SearchFilter> getFruitsTransactionFilter(List<SearchFilter> filters, FruitsTransaction fruitsTransaction) {
        return getTopicFilter(filters, fruitsTransaction.getSearchArea(), fruitsTransaction.getSearchValue(), fruitsTransaction.getStartDateTime(), fruitsTransaction.getEndDateTime());
    }

}
