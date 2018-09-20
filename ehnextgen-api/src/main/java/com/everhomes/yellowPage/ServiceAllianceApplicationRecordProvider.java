// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

public interface ServiceAllianceApplicationRecordProvider {

	void createServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	void updateServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordById(Long id);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(Long pageAnchor, Integer pageSize);

	List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecordByEnterpriseId(Long enterpriseId,
			Long pageAnchor, Integer pageSize);

	ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordByFlowCaseId(Long flowCaseId);

}