package com.turtlebone.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.turtlebone.checkin.model.GroupConfigModel;
import com.turtlebone.rest.bean.CheckinRequest;
import com.turtlebone.rest.service.RestCheckinService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.util.StringUtil;

@RestController
@Slf4j
@RequestMapping("/checkin")
public class CheckinController {
	
	@Autowired
	private RestCheckinService restCheckinService;
	
	@RequestMapping(value="/loadChildren/{parentId}", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> loadChildren(@PathVariable Integer parentId) {
		List<GroupConfigModel> list = restCheckinService.loadChildren(parentId);
		return ResponseEntity.ok(list);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> add(@RequestBody GroupConfigModel gcModel) {
		int rt = restCheckinService.add(gcModel);
		return rt == 1 ? ResponseEntity.ok("OK") : ResponseEntity.ok("FAIL");
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> modify(@RequestBody GroupConfigModel gcModel) {
		int rt = restCheckinService.modify(gcModel);
		return rt == 1 ? ResponseEntity.ok("OK") : ResponseEntity.ok("FAIL");
	}
	
	@RequestMapping(value="/checkin", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> checkin(@RequestBody CheckinRequest request) {
		String username = request.getUsername();
		String type = request.getType();
		String remark = request.getRemark();
		String date = request.getDate();
		String time = request.getTime();
		String datetime = null;
		log.debug("Request:{}", JSONObject.toJSONString(request));
		if (!StringUtils.isEmpty(date)) {
			datetime = date + " ";
			if (!StringUtils.isEmpty(time)) {
				datetime += time;
			} else {
				datetime += "00:00";
			}
		}
		int rt = restCheckinService.checkin(type, username, remark, datetime);
		return rt == 1 ? ResponseEntity.ok("OK") : ResponseEntity.ok("FAIL");
	}
}
