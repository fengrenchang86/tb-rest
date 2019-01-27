package com.turtlebone.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.turtlebone.main.model.UserModel;
import com.turtlebone.main.service.UserService;
import com.turtlebone.rest.bean.auth.UserDetails;
import com.turtlebone.rest.bean.auth.WechatLoginRequest;
import com.turtlebone.rest.common.AppConfig;
import com.turtlebone.rest.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth/wechat")
public class AuthWechatController {
	private final String WECHAT_URL = "https://api.weixin.qq.com/sns/jscode2session";
	
	@Autowired
	private RedisService redisService;
	@Reference(check=false)
	private UserService userService;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@RequestMapping(value="/getOpenId", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getOpenId(@RequestBody WechatLoginRequest request) {
		AppConfig appConfig = AppConfig.getAppConfig(request.getApp());
		String appId = appConfig.getAPPID();
		String secret = appConfig.getSECRET();
		log.info("getOpenId:{}", JSON.toJSONString(request));
	
		String code = request.getCode();
		String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", 
				WECHAT_URL, appId, secret, code);
		String result = "{}";
		
		UserDetails userDetails = new UserDetails();
		try {
			result = restTemplate.getForEntity(url, String.class).getBody();
			log.debug("wechat return {}", result);
			JSONObject rs = JSON.parseObject(result);
			String openId = rs.getString("openid");
			
			userDetails.setTokenId(openId);
			userDetails.setAvatarUrl(request.getAvatarUrl());
			userDetails.setNickName(request.getNickName());
			userDetails.setTokenType("WX");
			userDetails.setLoginName(getUsername(openId, request.getNickName()));
			
			//把openId存放到redis中，维持30分钟
			redisService.set(openId, JSON.toJSONString(userDetails));
			redisService.expire(openId, 60 * 30);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(userDetails);
	}
	
	protected String getUsername(String openId, String defaultName) {
		if (StringUtils.isEmpty(openId)) {
			return null;
		}
		UserModel user = userService.queryByToken(openId);
		if (user != null) {
			return user.getLoginName();
		} else {
			return defaultName.replaceAll("[^a-zA-Z0-9\\u4E00-\\u9FA5]", "");
		}
	}
}
