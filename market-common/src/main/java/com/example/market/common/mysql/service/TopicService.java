package com.example.market.common.mysql.service;

import com.example.market.common.base.entity.ro.*;
import com.example.market.common.base.service.ConstantService;
import com.example.market.common.mysql.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface TopicService extends ConstantService {

    RoFruitsTransaction resultRoFruitsTransaction(FruitsTransaction fruitsTransaction,
                                      Long userId);

    PageImpl<RoFruitsTransaction> resultRoFruitsTransactionPage(Page<FruitsTransaction> page, Long userId);


    RoGrainTransaction resultRoGrainTransaction(GrainTransaction grainTransaction,
                                      Long userId);

    PageImpl<RoGrainTransaction> resultRoGrainTransactionPage(Page<GrainTransaction> page, Long userId);

}
