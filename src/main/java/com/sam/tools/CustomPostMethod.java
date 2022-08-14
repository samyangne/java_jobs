package com.sam.tools;

import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;


public class CustomPostMethod extends PostMethod {

	public CustomPostMethod(String uri) {
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
					while ((tempbf = br.readLine()) != null) {
						if(tempbf.indexOf("charset=")>0){
							try{
								charset = tempbf.substring(tempbf.indexOf("charset=")+8, 
										tempbf.substring(tempbf.indexOf("charset=")+8).indexOf("\""));
								break;
							}catch(Exception e){
								
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
		return "utf-8";
	}
	
	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.HttpMethodBase#getResponseBodyAsString()
	 */
	@Override
	public String getResponseBodyAsString() throws IOException {
		GZIPInputStream gzin;
		if (getResponseBody() != null || getResponseStream() != null) {
			if (getResponseHeader("Content-Encoding") != null
					&& getResponseHeader("Content-Encoding").getValue()
							.toLowerCase().indexOf("gzip") > -1) {
				InputStream is = getResponseBodyAsStream();
				gzin = new GZIPInputStream(is);

				InputStreamReader isr = new InputStreamReader(gzin, getResponseCharSet());
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
	}

}
