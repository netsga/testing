package com.lge.hems.device.utilities.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;

@Component
public class LoggerInjector implements BeanPostProcessor {
//	private static final String STRING = "";

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                // make the field accessible if defined private
                ReflectionUtils.makeAccessible(field);
                if (field.getAnnotation(LoggerImpl.class) != null) {
//					LoggerImpl impl = field.getAnnotation(LoggerImpl.class);
                    Logger log = null;
//					if(!STRING.equals(impl.name())) {
//						log = LoggerFactory.getLogger(impl.name());
//					} else {
                    log = LoggerFactory.getLogger(bean.getClass());
//					}

                    field.set(bean, log);
                }
            }
        });
		return bean;
	}
}