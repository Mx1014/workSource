package com.everhomes.organization.pm.reportForm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhPropertyStatisticCommunity;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticBuildingDao;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticCommunityDao;

@Component
public class PropertyReportFormProviderImpl implements PropertyReportFormProvider{
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private DbProvider dbProvider;

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

	@Override
	public Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId,
			List<Long> communityIdList, String dateStr) {
		Map<Long, CommunityBriefStaticsDTO> result = new HashMap<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		
		query.addFrom(Tables.EH_PROPERTY_STATISTIC_COMMUNITY);
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.COMMUNITY_ID.in(communityIdList));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.DATE_STR.eq(dateStr));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().map(r->{
			CommunityBriefStaticsDTO dto = new CommunityBriefStaticsDTO();
			
			dto.setCommunityId(r.getValue(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.COMMUNITY_ID));
			dto.setCommunityName(r.getValue(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.COMMUNITY_NAME));
			dto.setBuildingCount(r.getValue(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.BUILDING_COUNT));
			dto.setAreaSize(r.getValue(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.AREA_SIZE));
			
			result.put(dto.getCommunityId(), dto);
			return null;
		});
		return result;
	}

	@Override
	public CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId,
			List<Long> communityIdList, String dateStr) {
		CommunityTotalStaticsDTO result = new CommunityTotalStaticsDTO();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		
		query.addSelect(DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.BUILDING_COUNT),
				DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.AREA_SIZE));
		query.addFrom(Tables.EH_PROPERTY_STATISTIC_COMMUNITY);
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.COMMUNITY_ID.in(communityIdList));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.DATE_STR.eq(dateStr));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			result.setBuildingCount(r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.BUILDING_COUNT)).intValue());
			result.setAreaSize(r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.AREA_SIZE)));
		});
		return result;
	}

	@Override
	public Map<String, BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr) {
		Map<String, BuildingBriefStaticsDTO> result = new HashMap<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		
		query.addFrom(Tables.EH_PROPERTY_STATISTIC_BUILDING);
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.COMMUNITY_ID.eq(communityId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.BUILDING_NAME.in(buildingNameList));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.DATE_STR.eq(dateStr));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			BuildingBriefStaticsDTO dto = new BuildingBriefStaticsDTO();
			
			dto.setBuildingName(r.getValue(Tables.EH_PROPERTY_STATISTIC_BUILDING.BUILDING_NAME));
			dto.setAreaSize(r.getValue(Tables.EH_PROPERTY_STATISTIC_BUILDING.AREA_SIZE));
			dto.setTotalApartmentCount(r.getValue(Tables.EH_PROPERTY_STATISTIC_BUILDING.TOTAL_APARTMENT_COUNT));
			
			result.put(dto.getBuildingName(), dto);
		});
		return result;
	}

	@Override
	public BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr) {
		BuildingTotalStaticsDTO result = new BuildingTotalStaticsDTO();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		
		query.addSelect(DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.TOTAL_APARTMENT_COUNT),
				DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.AREA_SIZE));
		query.addFrom(Tables.EH_PROPERTY_STATISTIC_BUILDING);
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.COMMUNITY_ID.eq(communityId));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.BUILDING_NAME.in(buildingNameList));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.DATE_STR.eq(dateStr));
		query.addConditions(Tables.EH_PROPERTY_STATISTIC_BUILDING.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			result.setTotalApartmentCount(r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.TOTAL_APARTMENT_COUNT)).intValue());
			result.setAreaSize(r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.AREA_SIZE)));
		});
		return result;
	}

	@Override
	public List<CommunityReportFormDTO> listCommunityReportForm(Integer namespaceId, List<Long> communityIds,
			String dateStr, Integer pageOffSet, Integer pageSize) {
		List<CommunityReportFormDTO> result = new ArrayList<>();
		
		EhCommunities a = Tables.EH_COMMUNITIES;
		EhPropertyStatisticCommunity b = Tables.EH_PROPERTY_STATISTIC_COMMUNITY;
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(a.ID,a.NAME,
				b.BUILDING_COUNT,b.TOTAL_APARTMENT_COUNT,b.FREE_APARTMENT_COUNT,b.RENT_APARTMENT_COUNT,
				b.OCCUPIED_APARTMENT_COUNT,b.LIVING_APARTMENT_COUNT,b.SALED_APARTMENT_COUNT,
				b.AREA_SIZE,b.RENT_AREA,b.FREE_AREA,
				b.RENT_RATE,b.FREE_RATE);
		query.addFrom(a);
		query.addJoin(b, JoinType.LEFT_OUTER_JOIN, a.ID.eq(b.COMMUNITY_ID));
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(a.ID.in(communityIds));
		query.addConditions(b.DATE_STR.eq(dateStr));
		query.addConditions(b.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.addLimit(pageOffSet, pageSize + 1);
		query.fetch().forEach(r->{
			CommunityReportFormDTO dto = new CommunityReportFormDTO();
			
			dto.setCommunityId(r.getValue(a.ID));
			dto.setCommunityName(r.getValue(a.NAME));
			dto.setBuildingCount(r.getValue(b.BUILDING_COUNT));
			dto.setTotalApartmentCount(r.getValue(b.TOTAL_APARTMENT_COUNT));
			dto.setFreeApartmentCount(r.getValue(b.FREE_APARTMENT_COUNT));
			dto.setRentApartmentCount(r.getValue(b.RENT_APARTMENT_COUNT));
			dto.setOccupiedApartmentCount(r.getValue(b.OCCUPIED_APARTMENT_COUNT));
			dto.setLivingApartmentCount(r.getValue(b.LIVING_APARTMENT_COUNT));
			dto.setSaledApartmentCount(r.getValue(b.SALED_APARTMENT_COUNT));
			dto.setAreaSize(r.getValue(b.AREA_SIZE));
			dto.setRentArea(r.getValue(b.RENT_AREA));
			dto.setFreeArea(r.getValue(b.FREE_AREA));
			dto.setRentRate(r.getValue(b.RENT_RATE));
			dto.setFreeRate(r.getValue(b.FREE_RATE));
			
			result.add(dto);
		});
		return result;
	}

	@Override
	public TotalCommunityStaticsDTO getTotalCommunityStatics(Integer namespaceId, List<Long> communityIds,
			String formatDateStr) {
		EhPropertyStatisticCommunity a = Tables.EH_PROPERTY_STATISTIC_COMMUNITY;
		
		
		
		
		
		return null;
	}
	
	


}
