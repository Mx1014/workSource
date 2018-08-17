package com.everhomes.yellowPage.stat;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.namespace.Namespace;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.statistics.event.StatEventCommonStatus;
import com.everhomes.rest.statistics.event.StatEventParamType;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.rest.yellowPage.stat.StatClickOrSortType;
import com.everhomes.statistics.event.StatEvent;
import com.everhomes.statistics.event.StatEventHandler;
import com.everhomes.statistics.event.StatEventLog;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventParamLog;
import com.everhomes.statistics.event.StatEventParamProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;

@Component
public class AllianceClickStatEventHandlerImpl implements StatEventHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllianceClickStatEventHandlerImpl.class);
	
	private final String SERVICE_ALLIANCE_CLICK= "service_alliance_click";
	
	private final String PARAM_FIELD_COMMUNITY_ID = "communityId";
	private final String PARAM_FIELD_SERVICE_ID = "serviceId";
	private final String PARAM_FIELD_CATEGORY_ID = "categoryId";
	private final String PARAM_FIELD_USER_ID = "userId";
	private final String PARAM_FIELD_CLICK_TYPE = "clickType";
	
	@Autowired
	ClickStatDetailProvider detailProvider;
	
    @Autowired
    StatEventParamProvider statEventParamProvider;
    
    @Autowired
    YellowPageProvider yellowPageProvider;

    @Autowired
    UserProvider userProvider;
    
    @Autowired
    OrganizationProvider organizationProvider;
    
    @Autowired
    ClickStatProvider clickStatProvider;
    
	@Override
	public String getEventName() {
		return SERVICE_ALLIANCE_CLICK;
	}

	@Override
	public void processStat(Namespace namespace, StatEvent statEvent, LocalDate statTime,
			StatEventStatTimeInterval interval) {
		// 前一天的最早时间
		Timestamp minTime = Timestamp.valueOf(statTime.atTime(LocalTime.MIN));
		// 前一天的最晚时间
		Timestamp maxTime = Timestamp.valueOf(statTime.atTime(LocalTime.MAX));

//		每次统计前一天的。
		List<Map<String, Object>> ret = detailProvider.countClickDetailsByDate(minTime, maxTime);

		// 创建记录
		Date date = new Date(minTime.getTime());
		for (Map<String, Object> map : ret) {
			ClickStat stat = new ClickStat();
			stat.setNamespaceId((Integer) map.get("namespaceId"));
			stat.setType((Long) map.get("type"));
			stat.setOwnerId((Long) map.get("ownerId"));
			stat.setCategoryId((Long) map.get("categoryId"));
			stat.setServiceId((Long) map.get("serviceId"));
			stat.setClickType((Byte) map.get("clickType"));
			stat.setClickCount(((Integer) map.get("clickCount")).longValue());
			stat.setClickDate(date);
			clickStatProvider.createStat(stat);
		}
		
		LOGGER.info("processStat: maxTime:"+maxTime.toString()+" minTime:"+minTime.toString()+" create count:"+ret.size());
	}

	/* 
	 * @see com.everhomes.statistics.event.StatEventHandler#processEventParamLogs(com.everhomes.statistics.event.StatEventLog, java.util.Map)
	 * 
	 * param：
	    "service_id": 123,  #服务id，非“点击服务类型”事件需传
        "category_id": 123,  #服务类型id
        "user_id": 321 #用户id,
        "click_type": 1 #点击类型枚举值，详见5.1.1
	 */
	@Override
	public List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param) {
		
        ClickStatDetail detail = new ClickStatDetail();
        detail.setNamespaceId(log.getNamespaceId());
        detail.setClickTime(log.getDeviceTime());
		for (Map.Entry<String, String> entry : param.entrySet()) {
			try {
				setClickStatDetailAttribute(entry, detail);
			} catch(Exception e) {
				LOGGER.error("single param not valid key:"+entry.getKey()+" value:"+entry.getValue()+" total:"+param.toString()+" log:"+log.toString());
			}
		}
		
        //检查参数
        boolean isParamOk = checkEventParam(detail);
        if (!isParamOk) {
        	LOGGER.error("param not valid detail:"+param.toString()+" log:"+log.toString());
        } else {
            //业务创建记录
    		reUpdateDetailParam(detail);
            detailProvider.createClickStatDetail(detail);
        }
        
        // 返回日志
        return getParamLogList(log, param);
	}
	
	/**
	 * //更新categoryId, ownerId, type, userName, userPhone
	 * @param detail
	 */
	private void reUpdateDetailParam(ClickStatDetail detail) {

		// 获取用户名和用户手机号
		TargetDTO userDto = organizationProvider.findUserContactByUserId(detail.getNamespaceId() , detail.getUserId());
		if (null != userDto) {
			detail.setUserName(userDto.getTargetName());
			detail.setUserPhone(userDto.getUserIdentifier());
		}

		if (StatClickOrSortType.CLICK_TYPE_SERVICE_TYPE.getCode().equals(detail.getClickType())) {
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(detail.getCategoryId());
			if (null != category) {
				long categoryId = null == category.getParentId() ? 0L : category.getParentId();
				categoryId = 0L == categoryId ? category.getId() : categoryId;
				detail.setType(categoryId);
			} 
			return;
		}

		// 更新为最新categoryid,填写ownerId,type
		ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(detail.getServiceId(), null, null);
		if (null != sa) {
			detail.setType(sa.getParentId());
			detail.setCategoryId(sa.getCategoryId());
		}
	}

	private void setClickStatDetailAttribute(Map.Entry<String, String> entry, ClickStatDetail detail) {
		
		if (PARAM_FIELD_COMMUNITY_ID.equals(entry.getKey())) {
			detail.setOwnerId(Long.parseLong(entry.getValue()));
			return;
		}	
		
		if (PARAM_FIELD_SERVICE_ID.equals(entry.getKey())) {
			detail.setServiceId(Long.parseLong(entry.getValue()));
			return;
		}		
		
		if (PARAM_FIELD_CATEGORY_ID.equals(entry.getKey())) {
			detail.setCategoryId(Long.parseLong(entry.getValue()));;
			return;
		}	
		
		if (PARAM_FIELD_USER_ID.equals(entry.getKey())) {
			detail.setUserId(Long.parseLong(entry.getValue()));
			return;
		}		
		
		if (PARAM_FIELD_CLICK_TYPE.equals(entry.getKey())) {
			detail.setClickType(Byte.parseByte(entry.getValue()));
			return;
		}
	}

	private List<StatEventParamLog> getParamLogList(StatEventLog log, Map<String, String> param) {
		List<StatEventParamLog> paramLogs = new ArrayList<>();
		for (Map.Entry<String, String> entry : param.entrySet()) {
			StatEventParam statEventParam = statEventParamProvider.findStatEventParam(log.getEventName(),
					entry.getKey());
			if (statEventParam != null) {

				StatEventParamLog paramLog = new StatEventParamLog();
				paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
				paramLog.setSessionId(log.getSessionId());
				paramLog.setNamespaceId(log.getNamespaceId());
				paramLog.setEventType(log.getEventType());
				paramLog.setEventName(log.getEventName());
				paramLog.setUid(log.getUid());
				paramLog.setEventLogId(log.getId());
				paramLog.setParamKey(entry.getKey());
				paramLog.setEventVersion(log.getEventVersion());
				paramLog.setUploadTime(log.getUploadTime());
				if (statEventParam.getParamType() == StatEventParamType.NUMBER.getCode()) {
					paramLog.setNumberValue(Integer.valueOf(entry.getValue()));
				} else {
					paramLog.setStringValue(entry.getValue());
				}

				paramLogs.add(paramLog);
			}
		}
		
		return paramLogs;
	}
	
	private boolean checkEventParam(ClickStatDetail detail) {

		if (null == detail 
				|| null == detail.getNamespaceId() 
				|| null == detail.getOwnerId()
				|| null == detail.getCategoryId() 
				|| null == detail.getClickTime() 
				|| null == detail.getClickType()
				|| null == detail.getUserId()) {
			
			return false;
		}

		if (!StatClickOrSortType.CLICK_TYPE_SERVICE_TYPE.getCode().equals(detail.getClickType())
				&& null == detail.getServiceId()) {
			return false;
		}

		return true;
	}
	
}
