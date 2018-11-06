package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.chatrecord.AbstractChatRecordService;
import com.everhomes.rest.asset.TargetDTO;

@Component
public class AllianceOnlineServiceImpl extends AbstractChatRecordService implements AllianceOnlineServiceI {
	
	@Autowired
	AllianceOnlineServiceProvider onlineServiceProvider;
	

	@Override
	public void updateAllianceOnlineService(Long serviceAllianceId, Long userId, String userName) {
		
		//1.首先查询是否有该客服。
		AllianceOnlineService onlineService = onlineServiceProvider.getOnlineServiceByUserId(serviceAllianceId, userId);
		if (null != onlineService) {
			
			//有的话就更新
			onlineService.setUserName(userName);
			onlineServiceProvider.updateOnlineService(onlineService);
			return;
		}
		
		//3.没有就创建
		onlineService = new AllianceOnlineService();
		onlineService.setOwnerId(serviceAllianceId);
		onlineService.setUserId(userId);
		onlineService.setUserName(userName);
		onlineServiceProvider.createOnlineService(onlineService);
	}

	@Override
	public List<TargetDTO> listOnlineServiceByServiceAllianceId(Long serviceAllianceId) {
		
		//获取客服列表，以更新时间降序
		List<AllianceOnlineService> onlines = onlineServiceProvider.getOnlineServiceList(serviceAllianceId);
		if (CollectionUtils.isEmpty(onlines)) {
			return null;
		}
		
		List<TargetDTO> dtos = new ArrayList<>(10);
		for (AllianceOnlineService item : onlines) {
			TargetDTO dto = new TargetDTO();
			dto.setTargetId(item.getUserId());
			dto.setTargetName(item.getUserName());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	private Long getServiceAllianceIdFromExtraParam(String extraParam) {
		
		if (null == extraParam) {
			return null;
		}
		
		String idKey = "serviceAllianceId";

		JSONObject json = JSONObject.parseObject(extraParam);
		if (null == json || null == json.getLong(idKey)) {
			return null;
		}
		
		return json.getLong(idKey);
	}

	@Override
	protected TargetDTO getSingleServicer(Integer namespaceId, String ownerType, Long ownerId, Long servicerId, String extraParam) {
		
		Long serviceAllianceId = getServiceAllianceIdFromExtraParam(extraParam);
		if (null == serviceAllianceId) {
			return null;
		}
		
		AllianceOnlineService onlineService = onlineServiceProvider.getOnlineServiceByUserId(serviceAllianceId, servicerId);
		if (null == onlineService) { 
			return null;
		}
		
		TargetDTO dto = new TargetDTO();
		dto.setTargetId(onlineService.getUserId());
		dto.setTargetName(onlineService.getUserName());
		return dto;
	}

	@Override
	protected List<TargetDTO> getAllServicers(Integer namespaceId, String ownerType, Long ownerId, String extraParam) {

		Long serviceAllianceId = getServiceAllianceIdFromExtraParam(extraParam);
		if (null == serviceAllianceId) {
			return null;
		}

		return listOnlineServiceByServiceAllianceId(serviceAllianceId);
	}

	@Override
	protected void checkOnlineServicePrivilege(Long currentOrgId, Long appId, Long checkCommunityId) {
		//这里不做权限限制
		return;
	}
	
}
