package com.lge.hems.device.utilities.customize;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface GsonExclude {
}
