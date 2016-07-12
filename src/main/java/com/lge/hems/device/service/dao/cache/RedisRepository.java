package com.lge.hems.device.service.dao.cache;

import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.customize.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class using redistemplate
 *
 * Created by netsga on 2016. 5. 31..
 */
@SuppressWarnings("unchecked")
@Repository
public class RedisRepository implements CacheRepository{
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    ///////////////////////////////////////////////////////////////////////////////
    // Device Model Management Section
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public Boolean checkDeviceModelExistence(String modelId) throws Exception{
        return redisTemplate.opsForHash().hasKey("MODEL", modelId);
    }

    @Override
    public Boolean addDeviceModel(String modelId, String modelStr) throws Exception {
        //execute a transaction
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().put("MODEL", modelId, modelStr);
                return operations.exec();
            }
        });

        return (Boolean)txResults.get(0);
    }

    @Override
    public Object readDeviceModel(String key) throws Exception {
        return redisTemplate.opsForHash().get("MODEL", key);
    }

    @Override
    public void deleteDeviceModel(String... keys) throws Exception {
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().delete("MODEL", keys);
                return operations.exec();
            }
        });
    }

    @Override
    public Boolean deleteSingleDeviceModel(String key) throws Exception {
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().delete("MODEL", key);
                return operations.exec();
            }
        });

        return (Long)txResults.get(0) == 1;
    }

    @Override
    public Map<String, Object> readAllDeviceModel() throws Exception {
        return redisTemplate.opsForHash().entries("MODEL");
    }

    @Override
    public Set<String> readAllDeviceModeList() throws Exception {
        return redisTemplate.opsForHash().keys("MODEL");
    }


    ///////////////////////////////////////////////////////////////////////////////
    // Device Instance Management Section
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public Boolean addDeviceInstance(String logicalDeviceId, Map<String, Object> map) throws Exception {
        //execute a transaction
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().putAll("INSTANCE:" + logicalDeviceId, map);
                operations.hasKey("INSTANCE:" + logicalDeviceId);
                return operations.exec();
            }
        });

        return (Boolean)txResults.get(0);
    }

    @Override
    public Set<String> readDeviceInstanceDataList(String logicalDeviceId) throws Exception {
        return redisTemplate.opsForHash().keys("INSTANCE:" + logicalDeviceId);
    }

    @Override
    public Boolean checkDeviceInstanceExistence(String logicalDeviceId) throws Exception {
        return redisTemplate.hasKey("INSTANCE:" + logicalDeviceId);
    }

    @Override
    public Boolean deleteDeviceInstance(String logicalDeviceId) throws Exception{
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.delete("INSTANCE:" + logicalDeviceId);
                return operations.exec();
            }
        });
        return (Long)txResults.get(0) == 1;
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Device Instance Data Management Section
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public Map<String, Object> readDeviceInstance(String logicalDeviceId) throws Exception {
        return redisTemplate.opsForHash().entries("INSTANCE:" + logicalDeviceId);
    }

    @Override
    public Map<String, Object> readDeviceInstanceData(String logicalDeviceId, List<String> keys) throws Exception{
        Map<String, Object> resp = CollectionFactory.newMap();

        List<Object> txResults = redisTemplate.opsForHash().multiGet("INSTANCE:" + logicalDeviceId, keys);

        Iterator<String> keysIterator = keys.iterator();
        Iterator<Object> objIterator = txResults.iterator();

        while (keysIterator.hasNext() && objIterator.hasNext()) {
            resp.put(keysIterator.next(), objIterator.next());
        }

        return resp;
    }

    @Override
    public Object readDeviceInstanceData(String logicalDeviceId, String key) throws Exception{

        return redisTemplate.opsForHash().get("INSTANCE:" + logicalDeviceId, key);
    }

    @Override
    public Boolean updateDeviceInstanceData(String logicalDeviceId, Map<String, Object> data) throws Exception{
        //execute a transaction
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                for(Map.Entry<String, Object> e:data.entrySet()) {
                    operations.opsForHash().put("INSTANCE:" + logicalDeviceId, e.getKey(), String.valueOf(e.getValue()));
                }
//                operations.opsForHash().putAll("INSTANCE:" + logicalDeviceId, data);
                operations.opsForHash().multiGet("INSTANCE:" + logicalDeviceId, data.keySet());
                return operations.exec();
            }
        });

        return data.values().toString().equals(txResults.get(txResults.size() - 1).toString());
    }

    @Override
    public Boolean updateDeviceInstanceData(String logicalDeviceId, String key, Object data) throws Exception{
        //execute a transaction
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().put("INSTANCE:" + logicalDeviceId, key, String.valueOf(data));
                operations.opsForHash().get("INSTANCE:" + logicalDeviceId, key);
                return operations.exec();
            }
        });

        return data.toString().equals(txResults.get(1).toString());
    }
}
