package com.turtlebone.rest.bean;

import lombok.Data;

@Data
public class CheckinRequest extends BaseRequest {
	protected String type;
	protected String remark;
	protected String date;
	protected String time;
}
