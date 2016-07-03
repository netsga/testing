package com.lge.hems.device.utilities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Created by netsga on 2016. 7. 1..
 */
@Component
public class RestServiceUtil {
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        HttpComponentsClientHttpRequestFactory restConfig = new HttpComponentsClientHttpRequestFactory();
        restConfig.setReadTimeout(1000); //milliseconds
        restConfig.setConnectTimeout(1000); // milliseconds
        restConfig.setConnectionRequestTimeout(1000);

        this.restTemplate = new RestTemplate(restConfig);
    }

    public Map.Entry<HttpStatus, String> requestGetMethod(String url, String headerStr) {

        HttpHeaders headers = new HttpHeaders();
        // Header parameter needs split for create rest header using "|"
        String[] headerStrArr = StringUtils.split(headerStr, "|");
        for(String header:headerStrArr) {
            String[] headerArr = StringUtils.split(header, ":");
            headers.set(headerArr[0].trim(), headerArr[1].trim());
        }

        HttpEntity<String> entity = new HttpEntity<String>("params",headers);

        ResponseEntity<String> restResp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

//        String respValue = (String) jsonMessageConverter.getValueFromMessage(restResp.getBody(), "result.PSMT_001.ST.LastCommTm.stVal");

        return new AbstractMap.SimpleEntry<>(restResp.getStatusCode(), restResp.getBody());
    }
}
