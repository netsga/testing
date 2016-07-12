package com.lge.hems.device.service.core.devicemodel;

import com.google.gson.JsonSyntaxException;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.customize.JsonConverter;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.exceptions.*;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by netsga on 2016. 5. 24..
 *
 */
@Service
public class DeviceModelService {
    @LoggerImpl
    private Logger logger;

    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;

    @Autowired
    private JsonConverter converter;

    private Map<String, DeviceModelInformation> modelInformationCache = CollectionFactory.newMap();

    // Read all device model from redis repository
    @PostConstruct
    public void init() {
        try {
            readAllDeviceModel();
        } catch (ModelReadFailException e) {
            e.printStackTrace();
        }
    }

    /**
     * Device model ID를 받아 cache Repository로부터 데이터를 검색하고 결과를 응답해 줌.
     * ModelCache는 model ID를 read 할때마다 cache를 Refresh 해주는 것으로 최신 버전을 유지한다.
     *
     * @param modelId modelId
     * @return
     * @throws Exception
     */
    public DeviceModelInformation readDeviceModel(String modelId) throws NullRequestException, NullModelException, ModelReadFailException, ModelDeserializeException {
        DeviceModelInformation result = null;
        if(modelId == null) {
            throw new NullRequestException("Model ID is null.");
        }

        try {
            Object modelStr = cacheRepository.readDeviceModel(modelId);

            if(modelStr != null && !modelStr.toString().isEmpty()) {
                result = converter.fromJson(modelStr.toString(), DeviceModelInformation.class);
                modelInformationCache.put(modelId, result);
            }
        } catch (JsonSyntaxException e) {
            throw new ModelDeserializeException("Cannot convert model", modelId);
        } catch (Exception e) {
            throw new ModelReadFailException("Cannot read model", modelId, e);
        }

        return result;
    }

    /**
     * Device model template을 추가하는 서비스로 model template의 ID와 model 정보를 전달받아 storage에 저장한다.
     * 내부적으로 check existence model method를 호출하여 중복 체크를 하도록 되어있어
     * 호출하는 곳에서 별도로 중복체크를 하지 않아도 된다.
     * model에 현재 생성한 시기를 기록한다.
     *
     * @param modelId modelId
     * @param model model 상세 정보
     * @return successful flag
     *
     * @throws ModelCreateException
     * @throws ModelSerializeException
     * @throws NullRequestException
     * @throws CheckModelExistenceException
     */
    public Boolean addDeviceModel(String modelId, DeviceModelInformation model) throws ModelCreateException, ModelSerializeException, NullRequestException, CheckModelExistenceException {
        String modelStr = null;
        Boolean result = false;

        if(checkExistenceModel(modelId)) {
            throw new ModelCreateException("Model ID already created.", model.getId());
        }

        // add timestamp
        model.setTimestamp(System.currentTimeMillis());

        try {
            modelStr = converter.toJson(model);
        } catch (JsonSyntaxException e) {
            throw new ModelSerializeException("Cannot serialize device model", model.getId());
        }

        try {
            result = cacheRepository.addDeviceModel(modelId, modelStr);
        } catch (Exception e) {
            throw new ModelCreateException("Cannot create device model", model.getId());
        }

        modelInformationCache.put(modelId, model);

        return result;
    }

    /**
     * 현재 model id가 존재하는지 확인하기 위한 것으로 중복 방지를 위해서 사용한다.
     *
     * @param modelId modelId
     * @return
     * @throws CheckModelExistenceException
     * @throws NullRequestException
     */
    public Boolean checkExistenceModel(String modelId) throws CheckModelExistenceException, NullRequestException {
        Boolean result;
        if(modelId == null) {
            throw new NullRequestException("Model ID is null.");
        }

        try {
            result = cacheRepository.checkDeviceModelExistence(modelId);

            if(result) {
                // redis 에는 있지만 local cache에 없는 경우 local cache update
                if(!modelInformationCache.containsKey(modelId)) {
                    this.readDeviceModel(modelId);
                }
            }
        } catch (Exception e) {
            throw new CheckModelExistenceException(modelId);
        }
        return result;
    }

    /**
     * Device model를 하나씩 제거할때 사용되는 method.
     * 없으면 false 던짐.
     *
     * @param modelId
     * @return
     * @throws Exception
     */
    public Boolean deleteSingleDeviceModel(String modelId) throws Exception {
        Boolean result;
        if(modelId != null && !modelId.isEmpty()) {
            result = cacheRepository.deleteSingleDeviceModel(modelId);
            modelInformationCache.remove(modelId);
        } else {
            throw new NullRequestException("Requested modelId is null");
        }
        return result;
    }

    /**
     * 전체 device model을 읽는 것으로 model information까지 함게 가져옴.
     *
     * @return
     * @throws ModelReadFailException
     */
    public List<DeviceModelInformation> readAllDeviceModel() throws ModelReadFailException {
        List<DeviceModelInformation> result = CollectionFactory.newList();
        Map<String, Object> resp;
        try {
            resp = cacheRepository.readAllDeviceModel();
        } catch (Exception e) {
            throw new ModelReadFailException("Read fail to whole device model");
        }

        for(Map.Entry entry:resp.entrySet()) {
            DeviceModelInformation tempModel = null;
            try {
                tempModel = converter.fromJson(entry.getValue().toString(), DeviceModelInformation.class);
            } catch (JsonSyntaxException e) {
                continue;
            }
            result.add(tempModel);
            modelInformationCache.put(tempModel.getId(), tempModel);
        }
        return result;
    }

    /**
     * model list만 가져옴
     * 변경 해야함. model list는 md5라서 알아볼 수 없음.!
     *
     * @return
     * @throws ModelReadFailException
     */
    public Set<String> readAllDeviceModelList() throws ModelReadFailException {
        Set<String> resp;
        try {
            resp = cacheRepository.readAllDeviceModeList();
        } catch (Exception e) {
            throw new ModelReadFailException("Cannot get model list");
        }

         return resp;
    }

    /**
     * 아래 4가지 파라미터를 기반으로 match 되는 것들만 map으로 구성하여 응답해줌.
     *
     * @param deviceType
     * @param vendor
     * @param modelName
     * @param version
     * @return
     * @throws ModelReadFailException
     * @throws NullModelException
     * @throws NullRequestException
     */
    public List<DeviceModelInformation> searchDeviceModel(String deviceType, String vendor, String modelName, String version) throws ModelReadFailException, NullModelException, NullRequestException {
        List<DeviceModelInformation> result = CollectionFactory.newList();
        Map<String, Object> resp;

        if(deviceType == null && vendor == null && modelName == null && version == null) {
            throw new NullRequestException("Search request must have at least one parameter.");
        }

        try {
            resp = cacheRepository.readAllDeviceModel();
        } catch (Exception e) {
            throw new ModelReadFailException("Read fail to whole device model");
        }

        if(resp == null || resp.isEmpty()) {
            throw new NullModelException("Device model storage is empty.");
        }

        for(Map.Entry entry:resp.entrySet()) {
            DeviceModelInformation tempModel = null;
            try {
                tempModel = converter.fromJson(entry.getValue().toString(), DeviceModelInformation.class);
            } catch (JsonSyntaxException e) {
                continue;
            }

            if(deviceType != null && !tempModel.getDeviceType().equals(deviceType)) {
                continue;
            }

            if(vendor != null && !tempModel.getVendor().equals(vendor)) {
                continue;
            }

            if(modelName != null && !tempModel.getModelName().equals(modelName)) {
                continue;
            }

            if(version != null && !tempModel.getVersion().equals(version)) {
                continue;
            }

            result.add(tempModel);
        }

        return result;
    }

    public Map<String, DeviceModelInformation> getModelInformationCache() {
        return modelInformationCache;
    }
}
