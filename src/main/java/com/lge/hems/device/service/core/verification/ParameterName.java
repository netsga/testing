package com.lge.hems.device.service.core.verification;

/**
 * Created by netsga on 2016. 6. 16..
 */
public class ParameterName {
    // Header
    public static final String USER_ID = "user-id";

    /*
                    new SimpleEntry("", requestContent.getDeviceType()),
                new SimpleEntry("", requestContent.getModelName()),
                new SimpleEntry("", requestContent.getVendor()),
                new SimpleEntry("serviceType", requestContent.getServiceType()));

        verificationService.verifyParameters(false, new SimpleEntry("nameTag", requestContent.getNameTag()),
                new SimpleEntry("subNameTag", requestContent.getSubNameTag()),
                new SimpleEntry("subSubNameTag", requestContent.getSubSubNameTag()),
                new SimpleEntry("version", requestContent.getVersion()),
                new SimpleEntry("logicalDeviceId", requestContent.getLogicalDeviceId()));
     */


    // Body
    public static final String LOGICAL_DEVICE_ID = "logicalDeviceId";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_TYPE = "deviceType";
    public static final String MODEL_NAME = "modelName";
    public static final String VENDOR = "vendor";
    public static final String SERVICE_TYPE = "serviceType";
    public static final String NAME_TAG = "nameTag";
    public static final String SUB_NAME_TAG = "subNameTag";
    public static final String SUBSUB_NAME_TAG = "subSubNameTag";
    public static final String VERSION = "version";
}

