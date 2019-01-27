package com.turtlebone.rest.bean;

import lombok.Data;

@Data
public class CheckinRequest extends BaseRequest {
	protected String type;
}
