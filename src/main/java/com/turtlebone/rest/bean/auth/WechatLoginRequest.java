package com.turtlebone.rest.bean.auth;


import lombok.Data;

@Data
public class WechatLoginRequest {
	protected String nickName;
	protected String avatarUrl;
	protected String code;
	protected Integer app;
}
