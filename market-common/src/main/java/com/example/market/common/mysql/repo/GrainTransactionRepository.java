package com.example.market.common.mysql.repo;

import com.example.market.common.mysql.entity.GrainTransaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
public interface GrainTransactionRepository extends CrudRepository<GrainTransaction, Long> ,
        JpaSpecificationExecutor<GrainTransaction> {


    Optional<GrainTransaction> findByIdAndDeleteState(Long id, Short deleteState);

    List<GrainTransaction> findByIdInAndDeleteState(List<Long> id, Short deleteState);

    @Modifying
    @Query(value = "update GrainTransaction set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

    @Modifying
    @Query(value = "update GrainTransaction set browsingVolume = browsingVolume+1 where id =:id")
    @Transactional(rollbackFor = RuntimeException.class)
    void incrementBrowsingVolume(Long id);
}
