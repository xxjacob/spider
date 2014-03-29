package com.ideax.spider.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	static Logger logger = LoggerFactory.getLogger(Util.class);

	public static ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
	}

	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static String md5Encoding(String src) {
		try {
			byte[] strTemp = src.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);

			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			return src;
		}
	}

	public static final String AK = "XcMrzzpIgrVlXUOtAEzL3PFz";
	public static final String SK = "BOFBT0GSnoh0cnancoAAhhSTWFVonf8d";
	public static final String bucket = "fmstore";
	public static final String host = "http://bcs.duapp.com";

	/**
	 * 获取前面后pcs地址
	 * 
	 * @param method
	 * @param bucket
	 * @param objName
	 *            前面不要加 “/”
	 * @param time
	 * @return
	 */
	public static String genPcsUrl(String method, String bucket, String objName, int time) {
		if (StringUtils.isBlank(method) && StringUtils.isBlank(bucket) && StringUtils.isBlank(objName)) {
			return null;
		}
		String flag;
		if (time > 0)
			flag = "MBOI";
		else
			flag = "MBO";
		StringBuilder sb = new StringBuilder();
		sb.append(flag).append('\n').append("Method=").append(method).append('\n').append("Bucket=").append(bucket)
				.append('\n').append("Object=/").append(objName).append('\n');

		if (time > 0)
			sb.append("Time=").append(time).append('\n');

		String hmac = doSign(sb.toString(), SK);
		StringBuilder sb2 = new StringBuilder();

		String encodeObjName = null;
		try {
			encodeObjName = URLEncoder.encode(objName, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			encodeObjName = objName.replace("+", "%20");
		}
		sb2.append(host).append("/").append(bucket).append('/').append(encodeObjName).append("?sign=").append(flag)
				.append(':').append(AK).append(':').append(hmac);
		return sb2.toString();
	}

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	@SuppressWarnings("deprecation")
	public static String doSign(String data, String key) {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac;
		byte[] bytes = null;
		try {
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			try {
				bytes = mac.doFinal(data.getBytes("UTF-8"));
			} catch (Exception e1) {
				bytes = mac.doFinal(data.getBytes());
			}

			String base64b = Base64.encodeBase64String(bytes);
			try {
				return URLEncoder.encode(base64b, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				return URLEncoder.encode(base64b);
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace(System.err);
		} catch (InvalidKeyException e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

	/*
	 * --------------
	 */
	private static CloseableHttpClient httpclient;
	static {
		httpclient = HttpClients
				.custom()
				// disable reuse
				.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
				// set agent name
				.setUserAgent(
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
				// disable cookie
				.disableCookieManagement()
				// .disableRedirectHandling()
				// socket config, 10s read timeout
				.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(10000).build()).build();
	}

	public static String getHttpResponseString(String url, Map<String, String> params) {
		return getHttpResponseString(url, "get", params, null);
	}

	/**
	 * util to get http/ https response as utf8 string
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getHttpResponseString(String url, String method, Map<String, String> params,
			Map<String, String> header) {

		HttpRequestBase get = null;
		if (method.equalsIgnoreCase("post")) {
			HttpPost post = new HttpPost(url);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Entry<String, String> a : params.entrySet()) {
				list.add(new BasicNameValuePair(a.getKey(), a.getValue()));
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
				return null;
			}
			get = post;
		} else {
			if (params != null && !params.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (Entry<String, String> a : params.entrySet()) {
					try {
						sb.append('&').append(a.getKey()).append('=').append(URLEncoder.encode(a.getValue(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						sb.append(URLEncoder.encode(a.getValue()));
					}
				}
				sb.setCharAt(0, '?');
				url = url + sb.toString();
			}
			get = new HttpGet(url);
		}
		if (header != null) {
			for (Entry<String, String> en : header.entrySet()) {
				get.setHeader(en.getKey(), en.getValue());
			}
		}
		CloseableHttpResponse resp;
		try {
			resp = httpclient.execute(get);
			if (!(resp.getStatusLine().getStatusCode() == 200)) {
				String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
				logger.error("ERROR : report app xml result , response error : status=[{}] , {}", resp.getStatusLine()
						.getStatusCode(), str);
				return null;
			} else
				return EntityUtils.toString(resp.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static boolean download(String url, Map<String, String> header, File file) {

		HttpRequestBase get = null;
		get = new HttpGet(url);
		if (header != null) {
			for (Entry<String, String> en : header.entrySet()) {
				get.setHeader(en.getKey(), en.getValue());
			}
		}
		CloseableHttpResponse resp;
		OutputStream os = null;
		try {
			resp = httpclient.execute(get);
			if (!(resp.getStatusLine().getStatusCode() == 200)) {
				String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
				logger.error("ERROR : report app xml result , response error : status=[{}] , {}", resp.getStatusLine()
						.getStatusCode(), str);
			} else {
				os =  new FileOutputStream(file);
				IOUtils.copy(resp.getEntity().getContent(), os);
				return true;
			}
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}finally{
			IOUtils.closeQuietly(os);
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		String openidStr = "callback( {\"client_id\":\"101033103\",\"openid\":\"9CA246F8643379AA102C5EA5263ED688\"} );";
		int oi = openidStr.indexOf("\"openid\"");
		if (oi < 0) {
			logger.error("get openid error response:" + oi);
		}
		int start = openidStr.indexOf('"', oi + "\"openid\"".length());
		if (start < 0) {
			logger.error("get openid error response:" + start);
		}
		int end = openidStr.indexOf('"', start + 1);
		if (end <= start) {
			logger.error("get openid error response:" + oi + start + end);
		}
		System.out.println(openidStr.substring(start + 1, end));
		// StringBuilder sb = new StringBuilder();
		// sb.append("MBO").append('\n').append("Method=").append("GET").append('\n').append("Bucket=").append(bucket)
		// .append('\n').append("Object=").append("/断桥残雪 - 许嵩.mp3").append('\n');
		// String hmac = doSign(sb.toString(), SK);
		// System.out.println(sb.toString());
		// System.out.println(hmac);
		//
		// System.out.println(Util.genPcsUrl("GET", "fmstore", "断桥残雪 - 许嵩.mp3",
		// 0));
	}

}
