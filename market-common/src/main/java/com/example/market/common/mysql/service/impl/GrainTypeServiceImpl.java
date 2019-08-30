package com.example.market.common.mysql.service.impl;

import com.example.market.common.mysql.repo.GrainTypeRepository;
import com.example.market.common.mysql.service.GrainTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
public class GrainTypeServiceImpl implements GrainTypeService {


    private final GrainTypeRepository grainTypeRepository;

}
