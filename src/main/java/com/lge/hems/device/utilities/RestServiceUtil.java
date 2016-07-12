package com.lge.hems.device.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

<<<<<<< HEAD
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
=======
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.Map;
>>>>>>> branch 'master' of https://github.com/netsga/testing.git

/**
 * Created by netsga on 2016. 7. 1..6
 */
@Component
public class RestServiceUtil {
	private final static String HTTPS = "https://";
	private final static String HTTP = "http://";

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		HttpComponentsClientHttpRequestFactory restConfig = new HttpComponentsClientHttpRequestFactory();
		restConfig.setReadTimeout(1000); // milliseconds
		restConfig.setConnectTimeout(1000); // milliseconds
		restConfig.setConnectionRequestTimeout(1000);

		this.restTemplate = new RestTemplate(restConfig);
	}

<<<<<<< HEAD
	public Map.Entry<HttpStatus, String> requestGetMethod(String urlStr, String headerStr, String certiKey,
			String password) {
=======
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
    
    public String callExternalApi(String api_url) throws Exception {
		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		
		try {
			URL url = new URL(api_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String str_buffer = "";
	
			while ((str_buffer = in.readLine()) != null) {
				builder.append(str_buffer + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		return builder.toString();
    }
    
    public String callExternalApi(String api_url) throws Exception {
		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		
		try {
			URL url = new URL(api_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String str_buffer = "";
	
			while ((str_buffer = in.readLine()) != null) {
				builder.append(str_buffer + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		return builder.toString();
    }
    
	public Map.Entry<HttpStatus, String> requestPostMethod(String urlStr, String headerStr, String bodyStr,
			String certiKey, String password) {
		String result = null;
		HttpStatus respStatus = null;
		HttpsURLConnection conn = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(HttpMethod.POST.toString());
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);

			String[] headerStrArr = StringUtils.split(headerStr, "|");
			for (String header : headerStrArr) {
				String[] headerArr = StringUtils.split(header, ":");
				conn.addRequestProperty(headerArr[0].trim(), headerArr[1].trim());
			}

			if (urlStr.toLowerCase().startsWith(HTTPS)) {
				conn.setSSLSocketFactory(getFactory(certiKey, password));
			} else if (urlStr.toLowerCase().startsWith(HTTP)) {

			}
			conn.connect();

			OutputStreamWriter clsOutput = new OutputStreamWriter(conn.getOutputStream());

			clsOutput.write(bodyStr);
			clsOutput.flush();
			BufferedReader clsInput = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = clsInput.readLine()) != null) {
				sb.append(line);
			}

			clsOutput.close();
			clsInput.close();

			result = sb.toString();
			respStatus = HttpStatus.valueOf(conn.getResponseCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return new AbstractMap.SimpleEntry<>(respStatus, result);
	}

	public Map.Entry<HttpStatus, String> requestPatchMethod(String urlStr, String headerStr, String bodyStr, String certiKey, String password) {
		String result = null;
		HttpStatus respStatus = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = createClient(certiKey, password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpPatch httpPatch = null;
		try {
			httpPatch = new HttpPatch(new URI(urlStr));
			// Header parameter needs split for create rest header using "|"
			String[] headerStrArr = StringUtils.split(headerStr, "|");
			for (String header : headerStrArr) {
				String[] headerArr = StringUtils.split(header, ":");
				httpPatch.setHeader(headerArr[0].trim(), headerArr[1].trim());
			}
			
			httpPatch.setEntity(new StringEntity(bodyStr, ContentType.create("application/json", "utf-8")));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			CloseableHttpResponse response = httpClient.execute(httpPatch);
			
			BufferedReader clsInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = clsInput.readLine()) != null) {
				sb.append(line);
			}
			clsInput.close();
			result = sb.toString();
			
			respStatus = HttpStatus.valueOf(response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new AbstractMap.SimpleEntry<>(respStatus, result);
	}
	
    public String callExternalApi(String api_url) throws Exception {
		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		
		try {
			URL url = new URL(api_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String str_buffer = "";
	
			while ((str_buffer = in.readLine()) != null) {
				builder.append(str_buffer + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		return builder.toString();
    }
	
	private CloseableHttpClient createClient (String pKeyFile, String pKeyPassword) throws Exception{
		InputStream keyInput = this.getClass().getClassLoader().getResourceAsStream("certificates/" + pKeyFile);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
		
	    try {
	        HttpClientBuilder builder = HttpClientBuilder.create();
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        
	        // add TrustManager
			X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					try {
						KeyStore trustStore = KeyStore.getInstance("JKS");
						String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts";
						trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray());

						// Get Trust Manager
						TrustManagerFactory tmf = TrustManagerFactory
								.getInstance(TrustManagerFactory.getDefaultAlgorithm());
						tmf.init(trustStore);
						TrustManager[] tms = tmf.getTrustManagers();
						((X509TrustManager) tms[0]).checkServerTrusted(arg0, arg1);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
	        
	        ctx.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{trustManager}, null);
	        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        builder.setSSLSocketFactory(scsf);
	        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("https", scsf)
	                .build();

	        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);

	        builder.setConnectionManager(ccm);
	        return builder.build();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    }
	}
	
	
	
	
	
	
	

	public SSLSocketFactory getFactory(String pKeyFile, String pKeyPassword) throws Exception {
		InputStream keyInput = this.getClass().getClassLoader().getResourceAsStream("certificates/" + pKeyFile);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

		SSLContext context = SSLContext.getInstance("TLS");
		// context.init(keyManagerFactory.getKeyManagers(), null, new
		// SecureRandom());

		// add TrustManager
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				try {
					KeyStore trustStore = KeyStore.getInstance("JKS");
					String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts";
					trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray());

					// Get Trust Manager
					TrustManagerFactory tmf = TrustManagerFactory
							.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					TrustManager[] tms = tmf.getTrustManagers();
					((X509TrustManager) tms[0]).checkServerTrusted(arg0, arg1);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		context.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { trustManager }, new SecureRandom());
		return context.getSocketFactory();
	}

	public void setDefaultSSL(String pKeyFile, String pKeyPassword) throws Exception {
		InputStream keyInput = this.getClass().getClassLoader().getResourceAsStream("certificates/" + pKeyFile);
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(keyInput, pKeyPassword.toCharArray());
		keyInput.close();

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

		// add TrustManager
		X509TrustManager trustManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				try {
					KeyStore trustStore = KeyStore.getInstance("JKS");
					String cacertPath = System.getProperty("java.home") + "/lib/security/cacerts";
					trustStore.load(new FileInputStream(cacertPath), "changeit".toCharArray());

					// Get Trust Manager
					TrustManagerFactory tmf = TrustManagerFactory
							.getInstance(TrustManagerFactory.getDefaultAlgorithm());
					tmf.init(trustStore);
					TrustManager[] tms = tmf.getTrustManagers();
					((X509TrustManager) tms[0]).checkServerTrusted(arg0, arg1);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		// context.init(keyManagerFactory.getKeyManagers(), new TrustManager[] {
		// trustManager }, new SecureRandom());

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { trustManager }, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}
}
