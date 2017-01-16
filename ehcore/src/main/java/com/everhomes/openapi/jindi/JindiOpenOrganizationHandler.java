package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.rest.openapi.jindi.JindiOrganizationDTO;

/**
 * 
 * <ul>
 * 抓取组织数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ORGANIZATION_CODE)
public class JindiOpenOrganizationHandler implements JindiOpenHandler {
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<Organization>() {

			@Override
			public List<Organization> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return organizationProvider.listOrganizationByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<Organization> fetchDataByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
				return organizationProvider.listOrganizationByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, Organization organization) {
				JindiOrganizationDTO jindiOrganization = new JindiOrganizationDTO();
				jindiOrganization.setId(organization.getId());
				jindiOrganization.setName(organization.getName());
				jindiOrganization.setCreateTime(organization.getCreateTime());
				jindiOrganization.setUpdateTime(organization.getUpdateTime());
				
				OrganizationDetail organizationDetail = organizationProvider.findOrganizationDetailByOrganizationId(organization.getId());
				if (organizationDetail != null) {
					jindiOrganization.setAddress(organizationDetail.getAddress());
				}
				
				Integer memberCount = organizationProvider.countOrganizationMembers(organization.getId(), null);
				jindiOrganization.setMemberCount(memberCount);
				
				return jindiOrganization;
			}
		});
	}
	
}
