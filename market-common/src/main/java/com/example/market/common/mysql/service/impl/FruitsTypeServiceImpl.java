package com.example.market.common.mysql.service.impl;

import com.example.market.common.mysql.repo.FruitsTypeRepository;
import com.example.market.common.mysql.service.FruitsTypeService;
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
public class FruitsTypeServiceImpl implements FruitsTypeService {


    private final FruitsTypeRepository fruitsTypeRepository;

}
