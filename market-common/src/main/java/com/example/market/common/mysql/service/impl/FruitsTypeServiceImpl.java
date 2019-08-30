package com.example.market.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.constant.CacheKeys;
import com.example.market.common.mysql.entity.FruitsType;
import com.example.market.common.mysql.repo.FruitsTypeRepository;
import com.example.market.common.mysql.service.FruitsTypeService;
import com.example.market.common.redis.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final RedisService redisService;

    @Override
    public List<FruitsType> findAll() {
        return (List<FruitsType>) fruitsTypeRepository.findAll();
    }

    @Override
    public JSONObject findFruitsType() {
        String fruitsTypeKey = CacheKeys.getFruitsTypeKey();
        Optional<JSONObject> optional = redisService.get(fruitsTypeKey);
        return optional.orElseGet(() -> {
            Map<String, List<JSONObject>> fruitsTypeMap = this.findAll().parallelStream()
                    .collect(Collectors.groupingBy(fruitsType -> fruitsType.getFruitsGroup().toString(),
                            Collectors.mapping(fruitsType -> {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("id", fruitsType.getId());
                                jsonObject.put("fruits", fruitsType.getFruits());
                                return jsonObject;
                            }, Collectors.toList())));
            JSONObject fruitsTypeJSONObject = JSONObject.parseObject(JSONObject.toJSONString(fruitsTypeMap));
            redisService.setContainExpire(fruitsTypeKey, fruitsTypeJSONObject);
            return fruitsTypeJSONObject;
        });
    }
}
