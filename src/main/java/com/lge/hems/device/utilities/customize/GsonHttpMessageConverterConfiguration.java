package com.lge.hems.device.utilities.customize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by netsga on 2016. 5. 25..
 */
@Configuration
public class GsonHttpMessageConverterConfiguration {
    @Autowired
    private JsonConverter conv;

    @Bean
    public HttpMessageConverters customConverters() {
        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(conv.getGson());
        messageConverters.add(converter);

        return new HttpMessageConverters(true, messageConverters);
    }


}