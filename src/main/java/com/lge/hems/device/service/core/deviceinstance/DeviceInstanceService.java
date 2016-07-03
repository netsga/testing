package com.lge.hems.device.service.core.deviceinstance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.lge.hems.device.exceptions.ModelReadFailException;
import com.lge.hems.device.exceptions.NullModelException;
import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.deviceinstance.*;
import com.lge.hems.device.model.common.DeviceModelInformation;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.common.entity.UserDeviceBindingInfo;
import com.lge.hems.device.service.core.devicemodel.DeviceModelService;
import com.lge.hems.device.service.dao.cache.CacheRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceBindingRepository;
import com.lge.hems.device.service.dao.rds.DeviceInstanceRepository;
import com.lge.hems.device.utilities.CollectionFactory;
import com.lge.hems.device.utilities.logger.LoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by netsga on 2016. 5. 25..
 */
@Service
public class DeviceInstanceService {
    @LoggerImpl
    private Logger logger;

    // beans
    @Autowired
    @Qualifier("redisRepository")
    private CacheRepository cacheRepository;
    @Autowired
    private DeviceInstanceRepository instanceRepository;
    @Autowired
    private DeviceInstanceBindingRepository bindRepository;
    @Autowired
    private DeviceModelService modelService;
    @Autowired
    private MessageCore messageCore;

    // Member variables
    private static final String REGEX = "\\[[0-9]+\\]";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    //////////////////////////////////////////////////////////////////////////////////////
    //                                  PUBLIC                                          //
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * device instance create 하는 method.
     * device model이 단일인 경우는 version이 없더라도 그냥 하나 가져옴.
     * 다만 model version이 여러개인데 version을 넣지 않는 경우는 가장 마지막에 등록된 model을 사용함.
     *
     * @param device
     * @throws NullRequestException
     * @throws ModelReadFailException
     * @throws NullModelException
     */
    @Transactional
    public String createDeviceInstance(DeviceInstanceInformation device, String userId) throws NullRequestException, ModelReadFailException, NullModelException, DuplicateDeviceException, AddDeviceInstanceDataException, InstanceReadFailException {
        // dup check
        String duplLogicalDeviceId= checkDuplication(device);
        if(duplLogicalDeviceId != null) {
            throw new DuplicateDeviceException("Duplicated device", duplLogicalDeviceId);
        }

        // get model;
        DeviceModelInformation model = getSuitableDeviceModel(device);

        // create logical device id
        if(device.getLogicalDeviceId() == null || device.getLogicalDeviceId().isEmpty()){
            device.setLogicalDeviceId(messageCore.createLogicalDeviceId(device.getDeviceId()));
        }
        String logicalDeviceId = device.getLogicalDeviceId();

        // store logical device id and user id
        UserDeviceBindingInfo bindInfo = addBindingInfo(device, userId);

        // add instance to cache
        addDeviceInstanceToCache(model, device);

        // add device instance information to database
        device.setModelId(model.getId());
        device.setVersion(model.getVersion());
        device.setCreateTimestamp(System.currentTimeMillis());
        device.setBindingInfo(bindInfo);

        instanceRepository.save(device);

        return logicalDeviceId;
    }

    /**
     * Device information을 read 하는 기능
     *
     * @param logicalDeviceId
     * @return
     * @throws NullInstanceException
     */
    @Transactional
    public DeviceInstanceInformation readDeviceInstanceInformation(String logicalDeviceId) throws NullInstanceException {
        DeviceInstanceInformation result = instanceRepository.findByLogicalDeviceId(logicalDeviceId);

        if(result == null) {
            throw new NullInstanceException("No device instance in device instance informations.", logicalDeviceId);
        }

        return result;
    }

    /**
     * Device instance를 제거하는 기능으로 logical device id를 사용하여 제거한다.
     * 없는 instance를 제거하면 에러륿 발생시키지 않고 false 를 응답하는데
     * 여기에 대해 논의가 필요할 것 같다.
     *
     * @param logicalDeviceId
     * @return
     * @throws NullInstanceException
     * @throws InstanceDeleteException
     */
    @Transactional
    public Boolean deleteDeviceInstance(String logicalDeviceId, String userId) throws NullInstanceException, InstanceDeleteException {
        Boolean result = false;
        DeviceInstanceInformation instance = instanceRepository.findByLogicalDeviceId(logicalDeviceId);
        if(instance != null) {
            try {
                instanceRepository.delete(instance);
                bindRepository.delete(new UserDeviceBindingInfo(logicalDeviceId, userId));
            } catch (Exception e) {
                throw new InstanceDeleteException(logicalDeviceId, e);
            }

            try {
                if (cacheRepository.deleteDeviceInstance(logicalDeviceId)) {
                    result = true;
                }
            } catch (Exception e) {
                throw new InstanceDeleteException(logicalDeviceId, e);
            }
        }
        return result;
    }

    @Transactional
    public List<DeviceInstanceInformation> searchDeviceInstances(String userId, String serviceType, String deviceType, String modelName, String deviceId) {
        List<DeviceInstanceInformation> findResp = bindRepository.findByUserId(userId);
        List<DeviceInstanceInformation> result = CollectionFactory.newList();

        for(DeviceInstanceInformation info:findResp) {
            if(serviceType != null && !info.getServiceType().equals(serviceType)) {
                continue;
            }

            if(deviceType != null && !info.getDeviceType().equals(deviceType)) {
                continue;
            }

            if(modelName != null && !info.getModelName().equals(modelName)) {
                continue;
            }

            if(deviceId != null && !info.getDeviceId().equals(deviceId)) {
                continue;
            }

            result.add(info);
        }
        return result;

    }

    //////////////////////////////////////////////////////////////////////////////////////
    //                                  PRIVATE                                         //
    //////////////////////////////////////////////////////////////////////////////////////

    // for device instance creation
    private UserDeviceBindingInfo addBindingInfo(DeviceInstanceInformation device, String userId) throws AddDeviceInstanceDataException {

        UserDeviceBindingInfo result = bindRepository.save(new UserDeviceBindingInfo(device.getLogicalDeviceId(), userId));
        if(result == null || !result.getLogicalDeviceId().equals(device.getLogicalDeviceId())) {
            throw new AddDeviceInstanceDataException("Cannot create instance");
        }

//        result.setDeviceInstanceInformation(device);
        return result;
    }

    private void addDeviceInstanceToCache(DeviceModelInformation model, DeviceInstanceInformation device) throws InstanceReadFailException, AddDeviceInstanceDataException {
        // add device instance to cache storage
        Boolean cacheDuplResp;
        String logicalDeviceId = device.getLogicalDeviceId();

        // add device instance logical information
        Set<Map.Entry<String, JsonElement>> modelJson = model.getModel().getAsJsonObject().entrySet();
        JsonObject json = modelJson.iterator().next().getValue().getAsJsonObject();
        JsonObject logicalInfo = new JsonObject();
        logicalInfo.addProperty("logicalId", logicalDeviceId);
        logicalInfo.addProperty("registerDate", device.getCreateTimestamp());
        logicalInfo.addProperty("nameTag", device.getNameTag());
        logicalInfo.addProperty("subNameTag", device.getSubNameTag());
        logicalInfo.addProperty("subSubNameTag", device.getSubSubNameTag());
        logicalInfo.addProperty("location", device.getLocation());
        json.get("DC").getAsJsonObject().add("LogicalInfo", logicalInfo);

        try {
            cacheDuplResp = cacheRepository.checkDeviceInstanceExistence(logicalDeviceId);
        } catch (Exception e) {
            throw new InstanceReadFailException("Device instance read fail from data repository.", logicalDeviceId);
        }

        if (cacheDuplResp) {
            throw new AddDeviceInstanceDataException("Device instance sync is broken.", logicalDeviceId);
        }

        try {
            Map<String, Object> fullModel = parseJson(model.getModel().toString());
            Map<String, Object> instanceModel = CollectionFactory.newMap();
            for(Map.Entry<String, Object> e:fullModel.entrySet()) {
                if(e.getKey().endsWith(".default")) {

                    String insertKey = StringUtils.substring(e.getKey(), 0, e.getKey().lastIndexOf(".default"));

                    if(fullModel.containsKey(e.getKey())) {
                        instanceModel.put(insertKey, fullModel.get(e.getKey()));
                    } else {
                        // default value에 대한 고찰이 필요함.
                        instanceModel.put(insertKey, "");
                    }

                } else {
                    continue;
                }
            }

            if (!cacheRepository.addDeviceInstance(logicalDeviceId, instanceModel)) {
                throw new AddDeviceInstanceDataException("Fail to add device instance data");
            }
        } catch (AddDeviceInstanceDataException e) {
            throw e;
        } catch (Exception e) {
            throw new AddDeviceInstanceDataException("Fail to add device instance data", e);
        }
    }


    private DeviceModelInformation getSuitableDeviceModel(DeviceInstanceInformation device) throws NullRequestException, ModelReadFailException, NullModelException {
        List<DeviceModelInformation> modelResp = modelService.searchDeviceModel(device.getDeviceType(), device.getVendor(), device.getModelName(), device.getVersion());
        DeviceModelInformation model;

        if (modelResp.size() == 1) {
            model = modelResp.get(0);
        } else if (modelResp.size() == 0) {
            throw new NullModelException("No device model");
        } else {
            // timestamp순으로 sorting하여 가장 마지막에 등록된 것을 template으로 삼는다.
            Collections.sort(modelResp);
            model = modelResp.get(modelResp.size() - 1);
        }

        return model;
    }

    private String checkDuplication(DeviceInstanceInformation device) {
        String result = null;
        DeviceInstanceInformation existInstance;
        // Logical device id가 not null이면 이미 gateway에서 생성해서 넘겨준 것이므로
        // duplicate check를 logical device id를 함께 넣어서 check 하자.
        if(device.getLogicalDeviceId() != null) {
            existInstance = instanceRepository.findByDeviceIdAndModelNameAndDeviceTypeAndLogicalDeviceId(device.getDeviceId(), device.getModelName(), device.getDeviceType(), device.getLogicalDeviceId());
        } else {
            // 이미 있는 경우는 create process를 타지않고 그냥 생성된 정보를 return 해준다.
            existInstance = instanceRepository.findByDeviceIdAndModelNameAndDeviceType(device.getDeviceId(), device.getModelName(), device.getDeviceType());
        }

        if(existInstance != null) {
            result = existInstance.getLogicalDeviceId();
        }
        return result;
    }


    private Map<String, Object> parseJson(String json) throws IOException {
        Map<String, Object> result = CollectionFactory.newMap();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        while (true) {
            JsonToken token = reader.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case BEGIN_OBJECT:
                    reader.beginObject();
                    break;
                case END_OBJECT:
                    reader.endObject();
                    break;
                case NAME:
                    reader.nextName();
                    break;
                case STRING:
                    String s = reader.nextString();
                    result.put(createPath(reader.getPath()), s);
                    break;
                case NUMBER:
                    String n = reader.nextString();
                    result.put(createPath(reader.getPath()), n);
                    break;
                case BOOLEAN:
                    Boolean b = reader.nextBoolean();
                    result.put(createPath(reader.getPath()), b.toString());
                    break;
                case NULL:
                    reader.nextNull();
                    break;
                case END_DOCUMENT:
                    return result;
            }
        }
    }


    private static String createPath(String path) {
        path = path.substring(2);
        path = PATTERN.matcher(path).replaceAll("");
        return path;
    }



//    private Boolean deleteRelatedDeviceData(DeviceInstanceInformation requestContent) throws InstanceDeleteException {
//        instanceRepository.delete(requestContent);
//        String logicalDeviceId = requestContent.getLogicalDeviceId();
//        Boolean result;
//        try {
//            DeviceInstanceInformation findResp = instanceRepository.findByLogicalDeviceId(logicalDeviceId);
//            if(findResp != null) {
//                throw new Exception();
//            }
//            result = cacheRepository.deleteDeviceInstance(logicalDeviceId);
//        } catch (Exception e) {
//            throw new InstanceDeleteException("Gateway binding process error: cannot rollback");
//        }
//
//
//        return result;
//    }
}
