package com.lge.hems.device.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.lge.hems.device.exceptions.NullRequestException;
import com.lge.hems.device.exceptions.RestRequestException;

/**
 * Created by netsga on 2016. 7. 1..6
 */
@Component
public class RestServiceUtil {
	
	@PostConstruct
	private void init() {
		
		List<String> filenames = CollectionFactory.newList();

	    try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("certificates/");
	      BufferedReader br = new BufferedReader( new InputStreamReader( in ) ) ) {
	      String resource;

	      while( (resource = br.readLine()) != null ) {
	        filenames.add( resource );
	      }
	    } catch (IOException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		}
	    
	    
		
		System.out.println(filenames.toString());
		
		
	}

	public Map.Entry<HttpStatus, String> requestPostMethod(String urlStr, String headerStr, String bodyStr, String certiKey, String password) throws NullRequestException, RestRequestException {
		HttpUriRequest request = null;
		
		try {
			HttpPost httpPost = new HttpPost(new URI(urlStr));
			// Header parameter needs split for create rest header using "|"
			String[] headerStrArr = StringUtils.split(headerStr, "|");
			for (String header : headerStrArr) {
				String[] headerArr = StringUtils.split(header, ":");
				httpPost.setHeader(headerArr[0].trim(), headerArr[1].trim());
			}

			httpPost.setEntity(new StringEntity(bodyStr, ContentType.create("application/json", "utf-8")));
			request = httpPost;
		} catch (URISyntaxException e) {
			throw new RestRequestException ("Cannot call POST method", urlStr);
		}
		
		return requestRestCall(request, certiKey, password);
	}
	
	public Map.Entry<HttpStatus, String> requestGetMethod(String urlStr, String headerStr, String certiKey, String password) throws RestRequestException, NullRequestException {
		HttpUriRequest request = null;
		
		try {
			// For url encoding. sometimes kiwigrid api needs reserved characters.
			String[] urlArray = StringUtils.split(urlStr, "?");
			
			if(urlArray.length > 1) {
				String queryString = URLEncoder.encode(urlArray[1], StandardCharsets.UTF_8.toString());
				StringBuilder sb = new StringBuilder(urlArray[0]).append("?").append(queryString);
				urlStr = sb.toString();
			}
			
			HttpGet httpGet = new HttpGet(new URI(urlStr));

			// Header parameter needs split for create rest header using "|"
			String[] headerStrArr = StringUtils.split(headerStr, "|");
			for (String header : headerStrArr) {
				String[] headerArr = StringUtils.split(header, ":");
				httpGet.setHeader(headerArr[0].trim(), headerArr[1].trim());
			}
			request = httpGet;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RestRequestException ("Cannot call GET method", urlStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RestRequestException ("Cannot call GET method", urlStr);
		}
		
		return requestRestCall(request, certiKey, password);
	}


	public Map.Entry<HttpStatus, String> requestPatchMethod(String urlStr, String headerStr, String bodyStr, String certiKey, String password) throws NullRequestException, RestRequestException {
		HttpUriRequest request = null;
		
		try {
			HttpPatch httpPatch = new HttpPatch(new URI(urlStr));
			// Header parameter needs split for create rest header using "|"
			String[] headerStrArr = StringUtils.split(headerStr, "|");
			for (String header : headerStrArr) {
				String[] headerArr = StringUtils.split(header, ":");
				httpPatch.setHeader(headerArr[0].trim(), headerArr[1].trim());
			}

			httpPatch.setEntity(new StringEntity(bodyStr, ContentType.create("application/json", "utf-8")));
			request = httpPatch;
		} catch (URISyntaxException e) {
			throw new RestRequestException ("Cannot call PATCH method", urlStr);
		}
		
		return requestRestCall(request, certiKey, password);
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
	
	////////////////////////
	
	private Map.Entry<HttpStatus, String> requestRestCall(HttpUriRequest request, String certiKey, String password) throws NullRequestException {
		CloseableHttpClient httpClient = null;
		Map.Entry<HttpStatus, String> result = null;
		
		if(request == null) {
			throw new NullRequestException("HttpUriRequest is null");
		}
		try {
			httpClient = createClient(certiKey, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			BufferedReader clsInput = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = clsInput.readLine()) != null) {
				sb.append(line);
			}
			clsInput.close();
			result = new AbstractMap.SimpleEntry<>(HttpStatus.valueOf(response.getStatusLine().getStatusCode()), sb.toString());
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private SSLContext getSSLContext(String pKeyFile, String pKeyPassword)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException, KeyManagementException {
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
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
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

		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { trustManager }, null);

		return ctx;
	}

	private CloseableHttpClient createClient(String pKeyFile, String pKeyPassword) throws Exception {

		try {
			HttpClientBuilder builder = HttpClientBuilder.create();
			SSLContext ctx = getSSLContext(pKeyFile, pKeyPassword);

			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(ctx,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			builder.setSSLSocketFactory(scsf);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", scsf).build();

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
}
