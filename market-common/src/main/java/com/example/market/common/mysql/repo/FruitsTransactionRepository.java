package com.example.market.common.mysql.repo;

import com.example.market.common.mysql.entity.FruitsTransaction;
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
public interface FruitsTransactionRepository extends CrudRepository<FruitsTransaction, Long>,
        JpaSpecificationExecutor<FruitsTransaction> {


    Optional<FruitsTransaction> findByIdAndDeleteState(Long id, Short deleteState);

    List<FruitsTransaction> findByIdInAndDeleteState(List<Long> id, Short deleteState);

    @Modifying
    @Query(value = "update FruitsTransaction set state =:afterState where userId in :userId and state = :beforeState and deleteState =:deleteState ")
    void updateState(List<Long> userId, String beforeState, String afterState, Short deleteState);

    @Modifying
    @Query(value = "update FruitsTransaction set browsingVolume = browsingVolume+1 where id =:id")
    @Transactional(rollbackFor = RuntimeException.class)
    void incrementBrowsingVolume(Long id);
}
