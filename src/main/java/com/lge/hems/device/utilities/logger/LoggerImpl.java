package com.lge.hems.device.utilities.logger;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface LoggerImpl {
	// for slf4j
	String name() default "";
}
