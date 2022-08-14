package com.sam.tools;

import java.io.File;

public class CommonConstant {
	/** 判断是否是windows系统 */
	public static boolean IS_WINDOWS = System.getProperty("os.name").startsWith("win")||System.getProperty("os.name").startsWith("Win");
	/** 文件存储地址 */
	public static final String SPIDER_PATH = System.getProperty("spider.base")+File.separator;
	/** 文件存储地址 */
	public static final String BIGDATA_PATH = System.getProperty("spider.base")+File.separator+"temp"+File.separator;
	/** FTP存储地址 */
	public static final String FTP_PATH = IS_WINDOWS?BIGDATA_PATH:"/home/bigdata/spider/";
	/** user agent */
	public static final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
	
	public static final String UA1="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
}
