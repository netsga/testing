package com.lge.hems.device.exceptions.deviceinstance;

import com.lge.hems.device.model.common.ResultCode;
import com.lge.hems.device.model.controller.response.BaseResponse;
import com.lge.hems.device.model.controller.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by netsga on 2016. 5. 30..
 * 전역 exception handling 처리용
 */
@ControllerAdvice
@RestController
public class InstanceExceptionController {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicateDeviceException.class, NullInstanceException.class, NotRegisteredDeviceException.class})
    public BaseResponse parameterVerificationError(InstanceException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setLogicalDeviceId(e.getLogicalDeviceId());
        errorContent.setErrorMessage(e.getMessage());

        base.setResultCode(ResultCode.PARAMETER_ERROR.getResultCode());
        base.setResult(errorContent);

        return base;
    }


    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DeviceInstanceDataUpdateException.class})
    public BaseResponse instanceDataUpdateError(DeviceInstanceDataUpdateException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setLogicalDeviceId(e.getLogicalDeviceId());
        errorContent.setErrorMessage(e.getMessage());
        errorContent.setErrorKey(e.getKey());

        base.setResultCode(ResultCode.PARAMETER_ERROR.getResultCode());
        base.setResult(errorContent);

        e.printStackTrace();

        return base;
    }


}
