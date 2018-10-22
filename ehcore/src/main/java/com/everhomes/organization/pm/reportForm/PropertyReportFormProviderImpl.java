package com.everhomes.organization.pm.reportForm;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticBuildingDao;
import com.everhomes.server.schema.tables.pojos.EhPropertyStatisticBuilding;

@Component
public class PropertyReportFormProviderImpl implements PropertyReportFormProvider{
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createBuildingStatistics(BuildingStatistics buildingStatistics) {
		EhPropertyStatisticBuildingDao dao = new EhPropertyStatisticBuildingDao();
		dao.insert(buildingStatistics);
	}

	@Override
	public void createCommunityStatics(CommunityStatistics communityStatistics) {
		// TODO Auto-generated method stub
	}

	
	public void batchAddBuildingStatistics2(Collection<EhPropertyStatisticBuilding> values) {
		EhPropertyStatisticBuildingDao dao = new EhPropertyStatisticBuildingDao();
		dao.insert(values);
	}

	@Override
	public void batchAddCommunityStatics(Collection<CommunityStatistics> values) {
		
		
	}

	@Override
	public void batchAddBuildingStatistics(Collection<BuildingStatistics> values) {
		// TODO Auto-generated method stub
		
	}

}
