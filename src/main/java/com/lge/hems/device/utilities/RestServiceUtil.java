package com.lge.hems.device.utilities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

//        HttpHeaders headers = new HttpHeaders();
//        // Header parameter needs split for create rest header using "|"
//        String[] headerStrArr = StringUtils.split(headerStr, "|");
//        for(String header:headerStrArr) {
//            String[] headerArr = StringUtils.split(header, ":");
//            headers.set(headerArr[0].trim(), headerArr[1].trim());
//        }
//
//        HttpEntity<String> entity = new HttpEntity<String>("params",headers);
//
//        ResponseEntity<String> restResp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    	String result = callGetActionToKiwigrid(url, headerStr);
//        String respValue = (String) jsonMessageConverter.getValueFromMessage(restResp.getBody(), "result.PSMT_001.ST.LastCommTm.stVal");

        return new AbstractMap.SimpleEntry<>(HttpStatus.OK, result);
    }
    
    private String callGetActionToKiwigrid(String api_url, String headerStr) {
		String pingUrl = api_url;
		String result = null;
		try {
			URL url = new URL(pingUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			
			String[] headerStrArr = StringUtils.split(headerStr, "|");
	        for(String header:headerStrArr) {
	            String[] headerArr = StringUtils.split(header, ":");
//	            headers.set(headerArr[0].trim(), headerArr[1].trim());
	            conn.addRequestProperty(headerArr[0].trim(), headerArr[1].trim());
	        }
			
			conn.setSSLSocketFactory(getFactory("ad4-lg.p12", "kiwigrid"));		//AppDev
			//conn.setSSLSocketFactory(getFactory("/pilot-telstra.p12", "kiwigrid"));		//pilot-telstra
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			in.close();
			conn.disconnect();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return result;
	}

	public SSLSocketFactory getFactory(String pKeyFile, String pKeyPassword) throws Exception {
		InputStream keyInput = this.getClass().getClassLoader().getResourceAsStream("certificates/" + pKeyFile);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

		SSLContext context = SSLContext.getInstance("TLS");
		// context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

		// add TrustManager
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				try {
					KeyStore trustStore = KeyStore.getInstance("JKS");
					String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts"; // Trust store path should be different by system platform.
					trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray()); // Use default certification validation

					// Get Trust Manager
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					TrustManager[] tms = tmf.getTrustManagers();
					((X509TrustManager) tms[0]).checkServerTrusted(arg0, arg1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		context.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { trustManager }, new SecureRandom());
		return context.getSocketFactory();
	}
}
