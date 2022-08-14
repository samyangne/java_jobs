package com.sam.tools;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @功能描述: 网页数据获取工具类
 * @项目版本: 1.3.1
 * @项目名称: 分布式网络爬虫
 * @相对路径: com.jjshome.spider.tools.HttpUtil.java
 * @创建作者: <a href="mailto:ouyangwenbin2009@live.cn">欧阳文斌</a>
 * @创建日期: 2018年2月2日 下午6:06:56
 */
public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	private static  boolean isPublicNetwork = true;

	public static String getHttpBody(String uri, long time)
			throws  Exception {
		Thread.sleep(time);
		return getHttpBody(uri);
	}



	// String：请求失败的地址 Integer：请求失败得数量
	static Map<String, Integer> failUrlMap = new LinkedHashMap<String, Integer>();

	public static String getHttpBody(String uri) throws Exception {
		HttpClient http = new HttpClient();
		String body = "";
		CustomGetMethod method = null;
		try {
			method = new CustomGetMethod(uri);
		} catch (IllegalArgumentException e) {
			logger.error("异常URL\n" + uri + "\n" + e.toString());
			return body;
		}

		method.setRequestHeader("Accept-Encoding", "gzip, deflate");
		method.setRequestHeader("User-Agent", CommonConstant.UA);

		int statusCode = 400;
		http.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		http.getHttpConnectionManager().getParams().setSoTimeout(60000);
		try {
			statusCode = http.executeMethod(method);
		} catch (UnknownHostException e2) {
			logger.error("异常URL\n" + uri + "\n" + e2.toString());
			return body;
		}
		if ("404".equals(String.valueOf(statusCode))) {
			return body;
		}

		try {
			body = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			failUrlMap.remove(uri);
		}
		return body;
	}



	
	/**
	 * @功能描述:检测是否被反爬 
	 * @使用对象:
	 * @接口版本: 1.1.3
	 * @创建作者: <a href="mailto:yangx@jjshome.com">杨献</a>
	 * @创建日期: 2018年4月10日 下午1:55:14
	 * @param html
	 * @return
	 */
	public static boolean checkStop(String html){
		if(StringUtils.isBlank(html)) return true;
		try {
			//安居反爬
			if(html.contains("访问验证-安居客") || html.contains("系统检测到您正在使用网页抓取工具访问安居客网站")){
				return true;
			}
			//58
			if(html.contains("403 Forbidden")){
				return true;
			}
			if(html.contains("访问过于频繁")){
				return true;
			}
			//房天下反爬
			Document document = Jsoup.parse(html);
			Elements humans = document.getElementsByClass("human");
			if(humans!=null && humans.size()>0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static String postHttpBody(String uri,NameValuePair[] parametersBody) throws Exception{
		HttpClient http = new HttpClient();
		String body = "";
		try {

			CustomPostMethod method = new CustomPostMethod(uri);
			if(parametersBody!=null)
				method.setRequestBody(parametersBody);

			method.setRequestHeader("Accept-Encoding", "gzip, deflate");
			method.setRequestHeader("User-Agent", CommonConstant.UA);
			int statusCode = http.executeMethod(method);
			http.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
			http.getHttpConnectionManager().getParams().setSoTimeout(30000);

			body = method.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}


	public static String postHttpBody(String uri,NameValuePair[] parametersBody,HttpClient http ) throws Exception{
		String body = "";
		try {

			CustomPostMethod method = new CustomPostMethod(uri);
			if(parametersBody!=null)
				method.setRequestBody(parametersBody);
			method.setRequestHeader("Accept-Encoding", "gzip, deflate");
			method.setRequestHeader("User-Agent", CommonConstant.UA);
			int statusCode = http.executeMethod(method);
			http.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
			http.getHttpConnectionManager().getParams().setSoTimeout(30000);

			body = method.getResponseBodyAsString();
			if(StringUtils.isEmpty(body)){
				try{
					Thread.sleep(1000);
					logger.warn("HttpUtil@postHttpBody 重新链接 "+uri);
					body = postHttpBody(uri, parametersBody);
				} catch (Exception e){
					e.printStackTrace();

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}



	


	public static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		strURL = strURL.trim().toLowerCase();
		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				for (int i = 1; i < arrSplit.length; i++) {
					strAllParam = arrSplit[i];
				}
			}
		}
		return strAllParam; 
	}
	public static Map<String, String> splitUrl(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;
		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return mapRequest;
		}
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}
	
	public static String doGet(String strUrl){
		String receive = null;
		GetMethod method = new GetMethod(strUrl);
		try {
			HttpClient client = new HttpClient();
			method.setRequestHeader("Content-type", "application/json; charset=UTF-8");
			method.setRequestHeader("Accept", "application/json; charset=UTF-8");
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			client.executeMethod(method);
			receive = method.getResponseBodyAsString();
			return receive;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return receive;
	
	}
	
	public static String doPost(String strUrl, String postString) {
		String receive = null;
		PostMethod method = new PostMethod(strUrl);
		try {
			HttpClient client = new HttpClient();
			method.setRequestHeader("Content-type","application/json; charset=UTF-8");
			method.setRequestHeader("Accept", "application/json; charset=UTF-8");
			
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			RequestEntity requestEntity = new StringRequestEntity(postString, "text/xml", "iso-8859-1");
			method.setRequestEntity(requestEntity);
			client.executeMethod(method);
			receive = method.getResponseBodyAsString();
			return receive;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return receive;
	}


	/**
	 * 公网请求
	 * @param url
	 * @return
	 */
	public static String requestPublicNetwork(String url){

		String message = "";
		HttpClient httpClient = new HttpClient();
		try {
			GetMethod method = new GetMethod(url);
			httpClient.executeMethod(method);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
			message = method.getResponseBodyAsString();

		} catch (Exception e) {
			logger.error("HttpUtil@requestPublicNetwork", e);
		}
		return message;
	}

	
	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
