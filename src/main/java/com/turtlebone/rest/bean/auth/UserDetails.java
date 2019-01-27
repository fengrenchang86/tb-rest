package com.turtlebone.rest.bean.auth;

import lombok.Data;

@Data
public class UserDetails {
	private String tokenId;
	private String tokenType;	//WX - 微信, TB - 用户名密码
	private String nickName;
	
	//for TB
	private String loginName;
	
	//for WX
	private String avatarUrl;
}
