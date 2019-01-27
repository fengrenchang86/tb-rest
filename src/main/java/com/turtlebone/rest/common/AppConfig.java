package com.turtlebone.rest.common;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class AppConfig {
	protected String APPID;
	protected String SECRET;
	
	public AppConfig(String appId, String secret) {
		this.APPID = appId;
		this.SECRET = secret;
	}
	
	private static Map<Integer, AppConfig> map;
	
	static {
		map = new HashMap<>();
		map.put(0, new AppConfig("wxd18588d3eefb71e2", "750a90ba0151d3164185389a883f2f21"));
		map.put(300, new AppConfig("wx40ec130187bae75d", "153635e280c3a911ccbbfcfacf55aac2"));//scorpiowf
	}
	
	public static AppConfig getAppConfig(Integer app) {
		return map.get(app);
	}
}
