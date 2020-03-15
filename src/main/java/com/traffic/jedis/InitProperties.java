package com.traffic.jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitProperties {
	
	private static String propFileName = "common.properties";
	private static Properties props = null;
	static {
		init();
	}
	private static void init() {
		InputStream inStream = InitProperties.class.getClassLoader().getResourceAsStream(propFileName);
		props = new Properties();
		try {
			props.load(inStream);
		} catch (IOException e) {
			throw new RuntimeException("读取配置文件 " + propFileName +" 出现异常", e);  
		}
	}
	public static Properties getProperties() {
		return props;
	}
	
	public static String getProperty(String key) {
		return getProperties().getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		getProperties().setProperty(key, value);
	}
	
}
