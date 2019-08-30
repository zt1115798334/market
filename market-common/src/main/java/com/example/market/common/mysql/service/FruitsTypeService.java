package com.example.market.common.mysql.service;


import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.FruitsType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
public interface FruitsTypeService extends BaseService<FruitsType, Long> {

    JSONObject findFruitsType();

}
