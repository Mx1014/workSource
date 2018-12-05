package com.everhomes.yellowPage.stat;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.lang3.StringUtils;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.yellowPage.IdNameInfoDTO;
import com.everhomes.rest.yellowPage.stat.ServiceAndTypeNameDTO;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;

/**
 * 用于埋点统计 根据serviceId获取服务名称，服务类型名称 或根据categoryId获取服务类型名称。
 * 统计的是旧的数据，可能有名称，类型修改，故将最新数据保存在这个工具，减少数据库交互。
 * 
 * @author huangmingbo
 *
 */
public class ClickStatTool {

	private YellowPageProvider yellowPageProvider = null;
	private Long currentType = null;

	private Map<Long, String> serviceNames = new HashMap<>(100); // 服务名称map key-id value-serviceName
	private Map<Long, String> serviceTypeNames = new HashMap<>(100); // 类型名称map key-categoryId value-serviceTypeName
	private Map<Long, Long> serviceAndTypeRelationMap = new HashMap<>(100); // 服务与类型的匹配表 key-serviceId value-categoryId

	public void build(Long type) {
		if (null == type || type < 1) {
			return;
		}

		this.currentType = type;

		// 获取所有服务类型名称
		List<IdNameInfoDTO> serviceTypeDtos = getYellowPageProvider().listServiceTypeNames(type);

		for (IdNameInfoDTO dto1 : serviceTypeDtos) {
			serviceTypeNames.put(dto1.getId(), dto1.getName());
		}

		// 根据条件获取所有的服务名称
		List<ServiceAndTypeNameDTO> serviceAndTypeDtos = getYellowPageProvider().listServiceNames(type, null, null);
		for (ServiceAndTypeNameDTO dto2 : serviceAndTypeDtos) {
			serviceNames.put(dto2.getId(), dto2.getName());
			serviceAndTypeRelationMap.put(dto2.getId(), dto2.getServiceTypeId());
		}

	}

	private YellowPageProvider getYellowPageProvider() {
		if (null != yellowPageProvider) {
			return yellowPageProvider;
		}

		return yellowPageProvider = PlatformContext.getComponent(YellowPageProvider.class);
	}

	public String getServiceName(Long serviceId) {
		if (null == serviceId) {
			return null;
		}

		String serviceName = serviceNames.get(serviceId);
		if (!StringUtils.isEmpty(serviceName)) {
			return serviceName;
		}

		ServiceAlliances sa = getYellowPageProvider().findServiceAllianceById(serviceId, null, null);

		return null == sa ? null : sa.getName();
	}

	public String getServiceTypeName(Long serviceTypeId) {
		if (null == serviceTypeId) {
			return null;
		}

		return serviceTypeNames.get(serviceTypeId);
	}

	public IdNameInfoDTO getTypeByServiceId(Long serviceId) {
		Long serviceTypeId = serviceAndTypeRelationMap.get(serviceId);
		if (null == serviceTypeId) {
			return null;
		}

		IdNameInfoDTO dto = new IdNameInfoDTO();
		dto.setId(serviceTypeId);
		dto.setName(serviceTypeNames.get(serviceTypeId));
		return dto;
	}

	public String getTypeNameByServiceId(Long serviceId) {
		IdNameInfoDTO dto = getTypeByServiceId(serviceId);
		return null == dto ? null : dto.getName();
	}

	public Long getCurrentType() {
		return currentType;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
