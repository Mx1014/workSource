package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.officecubicle.OfficeCubicleOrder;
import com.everhomes.officecubicle.OfficeCubicleProvider;
import com.everhomes.rest.openapi.jindi.JindiActionStationRentalDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

/**
 * 
 * <ul>
 * 抓取工位预订数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_STATION_RENTAL_CODE)
public class JindiOpenActionStationRentalHandler implements JindiOpenHandler {
	
	@Autowired
	private OfficeCubicleProvider officeCubicleProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<OfficeCubicleOrder>() {
			
			@Override
			public List<OfficeCubicleOrder> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return officeCubicleProvider.listStationByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<OfficeCubicleOrder> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return officeCubicleProvider.listStationByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, OfficeCubicleOrder src) {
				JindiActionStationRentalDTO data = new JindiActionStationRentalDTO();
				data.setId(src.getId());
				data.setUserId(src.getReserverUid());
				data.setUserName(src.getReserverName()==null?getUser(src.getReserverUid()).getNickName():src.getReserverName());
				data.setPhone(src.getReserveContactToken());
				data.setReserveTime(src.getReserveTime());
				data.setSpaceName(src.getSpaceName());
				data.setStatus(src.getStatus());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getUpdateTime());
				return data;
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
