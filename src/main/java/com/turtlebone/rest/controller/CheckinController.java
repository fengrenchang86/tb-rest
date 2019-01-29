package com.turtlebone.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.turtlebone.checkin.model.FavCheckinModel;
import com.turtlebone.checkin.model.GroupConfigModel;
import com.turtlebone.main.model.ActivityModel;
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
	
	@RequestMapping(value="/getFav", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getFav(@RequestBody CheckinRequest request) {
		String username = request.getUsername();
		List<FavCheckinModel> list = restCheckinService.queryFav(username);
		return ResponseEntity.ok(list);
	}
	
	@RequestMapping(value="/query", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> query(@RequestBody CheckinRequest request) {
		String username = request.getUsername();
		String type = request.getType();
		String remark = request.getRemark();
		String date = request.getDate();
		String time = request.getTime();
		String datetime = null;
		List<ActivityModel> list = new ArrayList<>();
		List<ActivityModel> list1 = restCheckinService.queryCheckin(username);
		List<ActivityModel> list2 = restCheckinService.queryCheckin("FLJ");
		if (list1 != null) {
			list.addAll(list1);
		}
		if (list2 != null) {
			list.addAll(list2);
		}
		Collections.sort(list, new Comparator<ActivityModel>() {

			@Override
			public int compare(ActivityModel x, ActivityModel y) {
				return y.getIdactivity() - x.getIdactivity();
			}
			
		});
		return ResponseEntity.ok(list);
	}
}
