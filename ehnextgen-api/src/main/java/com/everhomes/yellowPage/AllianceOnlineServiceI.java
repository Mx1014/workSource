package com.everhomes.yellowPage;

import java.util.List;


import com.everhomes.chatrecord.ChatRecordService;
import com.everhomes.rest.asset.TargetDTO;

public interface AllianceOnlineServiceI extends ChatRecordService {
	
	void updateAllianceOnlineService(Long serviceAllianceId, Long userId, String userName); 
	
	//根据服务id获取客服列表
	List<TargetDTO>  listOnlineServiceByServiceAllianceId(Long id);
	
}
