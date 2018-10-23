package com.everhomes.organization.pm.reportForm;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticBuildingDao;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhPropertyStatisticBuilding;

@Component
public class PropertyReportFormProviderImpl implements PropertyReportFormProvider{
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createBuildingStatistics(BuildingStatistics buildingStatistics) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(BuildingStatistics.class));
		buildingStatistics.setId(id);
		buildingStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		EhPropertyStatisticBuildingDao dao = new EhPropertyStatisticBuildingDao();
		dao.insert(buildingStatistics);
	}

	@Override
	public void createCommunityStatics(CommunityStatistics communityStatistics) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CommunityStatistics.class));
		communityStatistics.setId(id);
		communityStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		EhPropertyStatisticCommunityDao dao = new EhPropertyStatisticCommunityDao();
		dao.insert(communityStatistics);
	}

	
	public void batchAddBuildingStatistics2(Collection<BuildingStatistics> values) {
		EhPropertyStatisticBuildingDao dao = new EhPropertyStatisticBuildingDao();
		
	}

	@Override
	public void batchAddCommunityStatics(Collection<CommunityStatistics> values) {
		
		
	}

	@Override
	public void batchAddBuildingStatistics(Collection<BuildingStatistics> values) {
		// TODO Auto-generated method stub
		
	}

}
