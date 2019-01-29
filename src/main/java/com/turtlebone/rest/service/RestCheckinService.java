package com.turtlebone.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.turtlebone.checkin.common.GroupType;
import com.turtlebone.checkin.model.FavCheckinModel;
import com.turtlebone.checkin.model.GroupConfigModel;
import com.turtlebone.checkin.service.CheckinService;
import com.turtlebone.checkin.service.GroupConfigService;
import com.turtlebone.main.model.ActivityModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestCheckinService {
	@Reference(check = false)
	private GroupConfigService groupConfigService;
	
	@Reference(check = false)
	private CheckinService checkinService;
	
	public List<GroupConfigModel> loadChildren(Integer parentId) {
		List<GroupConfigModel> list = null;
		list = groupConfigService.queryByParent(parentId);
		return list;
	}
	
	public int add(GroupConfigModel gcModel) {
		return groupConfigService.create(gcModel);
	}
	
	public int modify(GroupConfigModel gcModel) {
		return groupConfigService.updateByPrimaryKey(gcModel);
	}
	
	public int delete(int id) {
		return groupConfigService.deleteByPrimaryKey(id);
	}
	
	public int checkin(String type, String username, String remark, String datetime) {
		GroupType groupType = GroupType.findGroupType(type);
		if (groupType == null) {
			log.error("groupType[{}]不存在", type);
			return 0;
		}
		if (groupType.getId() >= 0x20000001 && groupType.getId() <= 0x20000010) {
			username = "FLJ";
		}
		return checkinService.checkin(groupType, username, remark, datetime);
	}
	public List<ActivityModel> queryCheckin(String username) {		
		return checkinService.query(username);
	}
	public List<FavCheckinModel> queryFav(String username) {
		return checkinService.queryFav(username);
	}
}
