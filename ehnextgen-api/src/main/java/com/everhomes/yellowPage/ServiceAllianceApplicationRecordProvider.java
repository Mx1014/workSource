// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface ServiceAllianceApplicationRecordProvider {

	void createServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	void updateServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordById(Long id);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(Long pageAnchor, Integer pageSize);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecordByEnterpriseId(Long enterpriseId,
			Long pageAnchor, Integer pageSize);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordByFlowCaseId(Long flowCaseId);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(AllianceCommonCommand cmd,
			Integer pageSize, ListingLocator locator, List<Byte> workFlowStatusList);

}