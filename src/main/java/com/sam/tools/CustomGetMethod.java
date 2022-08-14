package com.sam.tools;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class CustomGetMethod extends GetMethod {

	public CustomGetMethod(String uri) {
		super(uri);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.HttpMethodBase#getResponseCharSet()
	 */
	@Override
	public String getResponseCharSet() {
		String charset = super.getResponseCharSet();
		GZIPInputStream gzin;
		try {
			if (getResponseBody() != null || getResponseStream() != null) {
				if (getResponseHeader("Content-Encoding") != null
						&& getResponseHeader("Content-Encoding").getValue()
								.toLowerCase().indexOf("gzip") > -1) {
					InputStream is = getResponseBodyAsStream();
					gzin = new GZIPInputStream(is);

					InputStreamReader isr = new InputStreamReader(gzin, charset);
					java.io.BufferedReader br = new java.io.BufferedReader(isr);
					String tempbf;
					if(charset==null || charset=="ISO-8859-1"){
						while ((tempbf = br.readLine()) != null) {
							if(tempbf.indexOf("charset=")>0){
								Pattern p = Pattern.compile("charset=[\"|']?(.+?)[$|\"|']");
								Matcher m = p.matcher(tempbf);
								if (m.find()){
									charset = m.group(1);
									if(charset.startsWith("\"")) charset = charset.replace("\"", "");
								}
								break;
							}
						}
						
					}
					isr.close();
					gzin.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtils.isEmpty(charset)?"UTF-8":charset;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.HttpMethodBase#getResponseBodyAsString()
	 */
	@Override
	public String getResponseBodyAsString() {
		GZIPInputStream gzin;
		try {
			if (getResponseBody() != null || getResponseStream() != null) {
				if (getResponseHeader("Content-Encoding") != null
						&& getResponseHeader("Content-Encoding").getValue()
								.toLowerCase().indexOf("gzip") > -1) {
					InputStream is = getResponseBodyAsStream();
					gzin = new GZIPInputStream(is);

					String charset = getResponseCharSet();
					if(charset.indexOf("utf-8") != -1 || charset.indexOf("UTF-8") != -1){
						charset = "UTF-8";
					}
					//gb2312 只包含简体字符 ;  gbk 包含简体字符 繁体字符 兼容 gb2312
					if("gb2312".equals(charset.toLowerCase())) {
	                    charset = "GBK";
	                }
					InputStreamReader isr = new InputStreamReader(gzin, StringUtils.isEmpty(charset)?"ISO-8859-1":charset);
					java.io.BufferedReader br = new java.io.BufferedReader(isr);
					StringBuffer sb = new StringBuffer();
					String tempbf;
					while ((tempbf = br.readLine()) != null) {
						sb.append(tempbf);
						sb.append("\r\n");
					}
					isr.close();
					gzin.close();
					return sb.toString();
				} else {
					return super.getResponseBodyAsString();
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
