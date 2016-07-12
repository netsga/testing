package com.lge.hems.device.service.core.devicerelation;

import com.lge.hems.device.exceptions.UserDeviceMultipleBindingException;
import com.lge.hems.device.model.common.entity.DeviceInstanceInformation;
import com.lge.hems.device.model.common.entity.UserDeviceBindingInfo;
import com.lge.hems.device.service.dao.rds.DeviceInstanceBindingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by netsga on 2016. 6. 11..
 */
@Service
public class UserDeviceRelationService {
    @Autowired
    private DeviceInstanceBindingRepository repository;
    /**
     * Device를 delete 하거나 read 및 check시 해당 user에 등록된 device만 접근이 가능하다.
     * 이를 검증하기 위한 method
     *
     * @param userId
     * @param logicalDeviceId
     * @return
     */
    public Boolean checkUserDeviceMatch(String userId, String logicalDeviceId) throws UserDeviceMultipleBindingException {
        Boolean result = false;
        // option 1
        // user에게 등록된 gateway 받기
        // gateway에 등록된 device list 받기
        // 요청된 logical device id가 포함되는지 확인.

        // option 2
        // database table에 user와 device들간 연결관계가 표현되어 있음. (gateway binding시 기록됨)
        // 여기서 그냥 바로 검색함.

        List<UserDeviceBindingInfo> findResp = repository.findByLogicalDeviceIdAndUserId(logicalDeviceId, userId);
        if(findResp.size() == 1) {
            result = true;
        } else if(findResp.size() > 1) {
            throw new UserDeviceMultipleBindingException(logicalDeviceId, userId);
        }

        return result;
    }

}
