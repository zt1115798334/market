package com.example.market.common.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.market.common.constant.CacheKeys;
import com.example.market.common.mysql.entity.FruitsType;
import com.example.market.common.mysql.entity.GrainType;
import com.example.market.common.mysql.repo.GrainTypeRepository;
import com.example.market.common.mysql.service.GrainTypeService;
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
public class GrainTypeServiceImpl implements GrainTypeService {

    private final GrainTypeRepository grainTypeRepository;

    private final RedisService redisService;

    @Override
    public List<GrainType> findAll() {
        return (List<GrainType>) grainTypeRepository.findAll();
    }

    @Override
    public JSONObject findGrainType() {
        String grainTypeKey = CacheKeys.getGrainTypeKey();
        Optional<JSONObject> optional = redisService.get(grainTypeKey);
        return optional.orElseGet(() -> {
            Map<String, List<JSONObject>> grainTypeMap = this.findAll().parallelStream()
                    .collect(Collectors.groupingBy(grainType -> grainType.getGrainGroup().toString(),
                            Collectors.mapping(grainType -> {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("id", grainType.getId());
                                jsonObject.put("grain", grainType.getGrain());
                                return jsonObject;
                            }, Collectors.toList())));
            JSONObject grainTypeJSONObject = JSONObject.parseObject(JSONObject.toJSONString(grainTypeMap));
            redisService.setContainExpire(grainTypeKey, grainTypeJSONObject);
            return grainTypeJSONObject;
        });
    }
}
