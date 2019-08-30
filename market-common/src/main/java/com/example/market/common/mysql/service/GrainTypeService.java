package com.example.market.common.mysql.service;


import com.alibaba.fastjson.JSONObject;
import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.GrainType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/30 11:46
 * description:
 */
public interface GrainTypeService extends BaseService<GrainType, Long> {

    JSONObject findGrainType();

}
