// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.faq.GetLatestServiceStateCommand;

public interface ServiceAllianceApplicationRecordProvider {

	void createServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	void updateServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordById(Long id);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(Long pageAnchor, Integer pageSize);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecordByEnterpriseId(Long enterpriseId,
			Long pageAnchor, Integer pageSize);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordByFlowCaseId(Long flowCaseId);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(AllianceCommonCommand cmd,
			Integer pageSize, ListingLocator locator, Long userId, List<Byte> workFlowStatusList);

	Integer listRecordCounts(AllianceCommonCommand cmd, Long userId, List<Byte> workFlowStatusList);

	ServiceAllianceApplicationRecord listLatestRecord(GetLatestServiceStateCommand cmd, Long userId, List<Byte> workFlowStatusList);

}