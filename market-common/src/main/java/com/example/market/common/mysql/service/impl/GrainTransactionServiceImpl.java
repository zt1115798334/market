package com.example.market.common.mysql.service.impl;

import com.example.market.common.base.entity.CustomPage;
import com.example.market.common.base.entity.ro.RoGrainTransaction;
import com.example.market.common.base.service.PageUtils;
import com.example.market.common.base.service.SearchFilter;
import com.example.market.common.exception.custom.OperationException;
import com.example.market.common.mysql.entity.GrainTransaction;
import com.example.market.common.mysql.repo.GrainTransactionRepository;
import com.example.market.common.mysql.service.CollectionService;
import com.example.market.common.mysql.service.GrainTransactionService;
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
public class GrainTransactionServiceImpl implements GrainTransactionService {

    private final GrainTransactionRepository grainTransactionRepository;

    private final TopicService topicService;

    private final CollectionService collectionService;

    @Override
    public GrainTransaction save(GrainTransaction grainTransaction) {
        Long id = grainTransaction.getId();
        if (id != null && id != 0L) {
            Optional<GrainTransaction> grainTransactionOptional = findByIdNotDelete(id);
            if (grainTransactionOptional.isPresent()) {
                GrainTransaction grainTransactionDB = grainTransactionOptional.get();
                grainTransactionDB.setTitle(grainTransaction.getTitle());
                grainTransactionDB.setPrice(grainTransaction.getPrice());
                grainTransactionDB.setDescribeContent(grainTransaction.getDescribeContent());
                grainTransactionDB.setAddress(grainTransaction.getAddress());
                grainTransactionDB.setUpdatedTime(DateUtils.currentDateTime());
                return grainTransactionRepository.save(grainTransactionDB);
            }
            return null;
        } else {
            grainTransaction.setBrowsingVolume(0L);
            grainTransaction.setState(IN_RELEASE);
            grainTransaction.setCreatedTime(currentDateTime());
            grainTransaction.setDeleteState(UN_DELETED);
            return grainTransactionRepository.save(grainTransaction);
        }
    }

    @Override
    public void deleteById(Long aLong) {
        grainTransactionRepository.findById(aLong).ifPresent(GrainTransaction -> {
            GrainTransaction.setDeleteState(DELETED);
            grainTransactionRepository.save(GrainTransaction);
        });
    }

    @Override
    public Optional<GrainTransaction> findByIdNotDelete(Long aLong) {
        return grainTransactionRepository.findByIdAndDeleteState(aLong, UN_DELETED);
    }

    @Override
    public List<GrainTransaction> findByIdsNotDelete(List<Long> id) {
        return grainTransactionRepository.findByIdInAndDeleteState(id, UN_DELETED);
    }

    @Override
    public Page<GrainTransaction> findPageByEntity(GrainTransaction GrainTransaction) {
        return null;
    }

    public RoGrainTransaction saveGrainTransaction(GrainTransaction GrainTransaction) {
        GrainTransaction = this.save(GrainTransaction);
        return topicService.resultRoGrainTransaction(GrainTransaction, GrainTransaction.getUserId());
    }

    @Override
    public void deleteGrainTransaction(Long id) {
        this.deleteById(id);
    }

    @Override
    public void modifyGrainTransactionSateToNewRelease(Long id) {
        grainTransactionRepository.findById(id).ifPresent(GrainTransaction -> {
            GrainTransaction.setState(NEW_RELEASE);
            grainTransactionRepository.save(GrainTransaction);
        });
    }


    @Override
    public void modifyGrainTransactionSateToAfterRelease(List<Long> userId) {
        grainTransactionRepository.updateState(userId, NEW_RELEASE, AFTER_RELEASE, UN_DELETED);
    }

    @Override
    public void incrementGrainTransactionBrowsingVolume(Long id) {
        grainTransactionRepository.incrementBrowsingVolume(id);
    }

    @Override
    public GrainTransaction findGrainTransaction(Long id) {
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public RoGrainTransaction findRoGrainTransaction(Long id, Long userId) {
        GrainTransaction GrainTransaction = this.findGrainTransaction(id);
        this.incrementGrainTransactionBrowsingVolume(id);
        return topicService.resultRoGrainTransaction(GrainTransaction, userId);
    }


    @Override
    public PageImpl<RoGrainTransaction> findGrainTransactionEffectivePage(GrainTransaction grainTransaction, Long userId) {
        List<SearchFilter> filters = getGrainTransactionFilter(getEffectiveState(), grainTransaction);
        return getRoGrainTransactionCustomPage(grainTransaction, userId, filters);

    }

    @Override
    public PageImpl<RoGrainTransaction> findGrainTransactionUserPage(GrainTransaction grainTransaction, Long userId) {
        List<SearchFilter> filters = getGrainTransactionFilter(getEffectiveState(), grainTransaction);
        filters.add(new SearchFilter("userId", grainTransaction.getUserId(), Operator.EQ));
        return getRoGrainTransactionCustomPage(grainTransaction, userId, filters);

    }

    @Override
    public PageImpl<RoGrainTransaction> findGrainTransactionCollectionPage(CustomPage customPage, Long userId) {
        PageImpl<Long> topicIdPage = collectionService.findCollection(userId, GRAIN_TRANSACTION, customPage);
        List<GrainTransaction> GrainTransactionList = this.findByIdsNotDelete(topicIdPage.getContent());
        return topicService.resultRoGrainTransactionPage(new PageImpl<>(GrainTransactionList, topicIdPage.getPageable(), topicIdPage.getTotalElements()),
                userId);
    }

    private PageImpl<RoGrainTransaction> getRoGrainTransactionCustomPage(GrainTransaction grainTransaction, Long userId, List<SearchFilter> filters) {
        Specification<GrainTransaction> specification = bySearchFilter(filters);
        Pageable pageable = PageUtils.buildPageRequest(grainTransaction);
        Page<GrainTransaction> page = grainTransactionRepository.findAll(specification, pageable);
        return topicService.resultRoGrainTransactionPage(page, userId);
    }

    private List<SearchFilter> getGrainTransactionFilter(List<SearchFilter> filters, GrainTransaction grainTransaction) {
        return getTopicFilter(filters, grainTransaction.getSearchArea(), grainTransaction.getSearchValue(), grainTransaction.getStartDateTime(), grainTransaction.getEndDateTime());

    }

}
