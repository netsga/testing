package com.lge.hems.device.exceptions.controller;

import com.lge.hems.device.exceptions.*;
import com.lge.hems.device.exceptions.base.ModelException;
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
public class ExceptionController {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RequestParameterException.class)
    public BaseResponse parameterVerificationError(RequestParameterException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setErrorKey(e.getParameterName() == null ? null : e.getParameterName());
        errorContent.setErrorReason(e.getReason() == null ? null : e.getReason().toString());
        errorContent.setErrorMessage(e.getMessage() == null ? null : e.getMessage());

        base.setResultCode(ResultCode.PARAMETER_ERROR.getResultCode());
        base.setResult(errorContent);

        return base;
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = NullRequestException.class)
    public BaseResponse nullRequestError(NullRequestException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setErrorMessage(e.getMessage());

        base.setResultCode(ResultCode.PARAMETER_ERROR.getResultCode());
        base.setResult(errorContent);

        return base;
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NullModelException.class, CheckModelExistenceException.class})
    public BaseResponse readNullModelError(ModelException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setErrorId(e.getModelId());
        errorContent.setErrorMessage(e.getMessage());

        base.setResultCode(ResultCode.PARAMETER_ERROR.getResultCode());
        base.setResult(errorContent);

        return base;
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ModelCreateException.class, ModelSerializeException.class, ModelReadFailException.class})
    public BaseResponse createModelError(ModelException e) {
        BaseResponse base = new BaseResponse();
        ErrorResponse errorContent = new ErrorResponse();

        errorContent.setErrorId(e.getModelId());
        errorContent.setErrorMessage(e.getMessage());

//        base.setResultCode(ResultCode.FAIL.getResultCode());
        base.setResult(errorContent);

        return base;
    }
}
