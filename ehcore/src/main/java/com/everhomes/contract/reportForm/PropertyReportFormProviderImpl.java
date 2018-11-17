package com.everhomes.contract.reportForm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.pm.reportForm.BuildingStatistics;
import com.everhomes.organization.pm.reportForm.CommunityStatistics;
import com.everhomes.organization.pm.reportForm.PropertyReportFormProvider;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalBuildingStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhBuildings;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhPropertyStatisticBuilding;
import com.everhomes.server.schema.tables.EhPropertyStatisticCommunity;
import com.everhomes.server.schema.tables.daos.EhInvestmentAdvertisementsDao;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticBuildingDao;
import com.everhomes.server.schema.tables.daos.EhPropertyStatisticCommunityDao;
import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisements;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class PropertyReportFormProviderImpl implements PropertyReportFormProvider{

	@Override
	public void createBuildingStatistics(BuildingStatistics buildingStatistics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createCommunityStatics(CommunityStatistics communityStatistics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId,
			List<Long> communityIdList, String dateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId,
			List<Long> communityIdList, String dateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId, Long communityId,
			List<String> buildingNameList, String dateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommunityReportFormDTO> listCommunityReportForm(Integer namespaceId, List<Long> communityIds,
			String dateStr, Integer pageOffSet, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TotalCommunityStaticsDTO getTotalCommunityStatics(Integer namespaceId, List<Long> communityIds,
			String formatDateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BuildingReportFormDTO> listBuildingReportForm(Integer namespaceId, Long communityId,
			List<Long> buildingIds, String formatDateStr, Integer pageOffSet, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TotalBuildingStaticsDTO getTotalBuildingStatics(Integer namespaceId, Long communityId,
			List<Long> buildingIds, String formatDateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBuildingDataByDateStr(String dateStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCommunityDataByDateStr(String dateStr) {
		// TODO Auto-generated method stub
		
	}/*
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private CommunityProvider communityProvider;

	@Override
	public void createBuildingStatistics(BuildingStatistics buildingStatistics) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.pojos.EhPropertyStatisticBuilding.class));
		EhPropertyStatisticBuildingDao dao = new EhPropertyStatisticBuildingDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPropertyStatisticBuilding.class));
		buildingStatistics.setId(id);
		buildingStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		dao.insert(buildingStatistics);
	}

	@Override
	public void createCommunityStatics(CommunityStatistics communityStatistics) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.pojos.EhPropertyStatisticCommunity.class));
		EhPropertyStatisticCommunityDao dao = new EhPropertyStatisticCommunityDao(context.configuration());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPropertyStatisticCommunity.class));
		communityStatistics.setId(id);
		communityStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
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
			BigDecimal buildingCount = r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.BUILDING_COUNT));
			BigDecimal areaSize = r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.AREA_SIZE));
			
			result.setBuildingCount(buildingCount!=null ? buildingCount.intValue() : null);
			result.setAreaSize(areaSize);
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
			BigDecimal totalApartmentCount = r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.TOTAL_APARTMENT_COUNT));
			BigDecimal areaSize = r.getValue(DSL.sum(Tables.EH_PROPERTY_STATISTIC_BUILDING.AREA_SIZE));
			
			result.setTotalApartmentCount(totalApartmentCount!=null ? totalApartmentCount.intValue() : null);
			result.setAreaSize(areaSize);
		});
		return result;
	}

	@Override
	public List<CommunityReportFormDTO> listCommunityReportForm(Integer namespaceId, List<Long> communityIds,
			String dateStr, Integer pageOffSet, Integer pageSize) {
		List<CommunityReportFormDTO> result = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhCommunities a = Tables.EH_COMMUNITIES;
		EhPropertyStatisticCommunity b = Tables.EH_PROPERTY_STATISTIC_COMMUNITY;
		
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
		if (pageOffSet != null && pageSize != null) {
			query.addLimit(pageOffSet, pageSize + 1);
		}
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
	public TotalCommunityStaticsDTO getTotalCommunityStatics(Integer namespaceId, List<Long> communityIds,String dateStr) {
		TotalCommunityStaticsDTO result = new TotalCommunityStaticsDTO();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPropertyStatisticCommunity a = Tables.EH_PROPERTY_STATISTIC_COMMUNITY;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(DSL.countDistinct(a.COMMUNITY_ID),DSL.sum(a.BUILDING_COUNT),DSL.sum(a.TOTAL_APARTMENT_COUNT),DSL.sum(a.FREE_APARTMENT_COUNT),
				DSL.sum(a.RENT_APARTMENT_COUNT),DSL.sum(a.OCCUPIED_APARTMENT_COUNT),DSL.sum(a.LIVING_APARTMENT_COUNT),
				DSL.sum(a.SALED_APARTMENT_COUNT),DSL.sum(a.AREA_SIZE),DSL.sum(a.RENT_AREA),DSL.sum(a.FREE_AREA));
		query.addFrom(a);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(a.COMMUNITY_ID.in(communityIds));
		query.addConditions(a.DATE_STR.eq(dateStr));
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			
			Integer communityCount = r.getValue(DSL.countDistinct(a.COMMUNITY_ID));
			BigDecimal buildingCount = r.getValue(DSL.sum(a.BUILDING_COUNT));
			BigDecimal totalApartmentCount = r.getValue(DSL.sum(a.TOTAL_APARTMENT_COUNT));
			BigDecimal freeApartmentCount = r.getValue(DSL.sum(a.FREE_APARTMENT_COUNT));
			BigDecimal rentApartmentCount = r.getValue(DSL.sum(a.RENT_APARTMENT_COUNT));
			BigDecimal occupiedApartmentCount = r.getValue(DSL.sum(a.OCCUPIED_APARTMENT_COUNT));
			BigDecimal livingApartmentCount = r.getValue(DSL.sum(a.LIVING_APARTMENT_COUNT));
			BigDecimal saledApartmentCount = r.getValue(DSL.sum(a.SALED_APARTMENT_COUNT));
			
			result.setCommunityCount(communityCount!=null ? communityCount : null);
			result.setBuildingCount(buildingCount!=null ? buildingCount.intValue() : null);
			result.setTotalApartmentCount(totalApartmentCount!=null ? totalApartmentCount.intValue() : null);
			result.setFreeApartmentCount(freeApartmentCount!=null ? freeApartmentCount.intValue() : null);
			result.setRentApartmentCount(rentApartmentCount!=null ? rentApartmentCount.intValue() : null);
			result.setOccupiedApartmentCount(occupiedApartmentCount!=null ? occupiedApartmentCount.intValue() : null);
			result.setLivingApartmentCount(livingApartmentCount!=null ? livingApartmentCount.intValue() : null);
			result.setSaledApartmentCount(saledApartmentCount!=null ? saledApartmentCount.intValue() : null);
			result.setAreaSize(r.getValue(DSL.sum(a.AREA_SIZE)));
			result.setRentArea(r.getValue(DSL.sum(a.RENT_AREA)));
			result.setFreeArea(r.getValue(DSL.sum(a.FREE_AREA)));
			
			BigDecimal rentRate;
			BigDecimal freeRate;
			if (result.getAreaSize() == null || result.getAreaSize().compareTo(BigDecimal.ZERO)==0) {
				rentRate = BigDecimal.ZERO;
				freeRate = BigDecimal.ZERO;
			}else {
				rentRate = (result.getRentArea()==null ? BigDecimal.ZERO : result.getRentArea()).divide(result.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				freeRate = (result.getFreeArea()==null ? BigDecimal.ZERO : result.getFreeArea()).divide(result.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
			}
			result.setRentRate(rentRate);
			result.setFreeRate(freeRate);
		});
		return result;
	}

	@Override
	public List<BuildingReportFormDTO> listBuildingReportForm(Integer namespaceId, Long communityId,
			List<Long> buildingIds, String dateStr, Integer pageOffSet, Integer pageSize) {
		List<BuildingReportFormDTO> result = new ArrayList<>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhBuildings a = Tables.EH_BUILDINGS;
		EhPropertyStatisticBuilding b = Tables.EH_PROPERTY_STATISTIC_BUILDING;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(a.ID,a.NAME,a.COMMUNITY_ID,
				b.TOTAL_APARTMENT_COUNT,b.FREE_APARTMENT_COUNT,b.RENT_APARTMENT_COUNT,
				b.OCCUPIED_APARTMENT_COUNT,b.LIVING_APARTMENT_COUNT,b.SALED_APARTMENT_COUNT,
				b.AREA_SIZE,b.RENT_AREA,b.FREE_AREA,b.RENT_RATE,b.FREE_RATE);
		query.addFrom(a);
		query.addJoin(b, JoinType.LEFT_OUTER_JOIN, a.ID.eq(b.BUILDING_ID));
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(a.COMMUNITY_ID.eq(communityId));
		query.addConditions(a.ID.in(buildingIds));
		query.addConditions(b.DATE_STR.eq(dateStr));
		query.addConditions(b.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		if (pageOffSet != null && pageSize != null) {
			query.addLimit(pageOffSet, pageSize + 1);
		}
		query.fetch().forEach(r->{
			BuildingReportFormDTO dto = new BuildingReportFormDTO();
			
			dto.setCommunityId(r.getValue(a.COMMUNITY_ID));
			Community community = communityProvider.findCommunityById(dto.getCommunityId());
			if (community!=null) {
				dto.setCommunityName(community.getName());
			}
			dto.setBuildingId(r.getValue(a.ID));
			dto.setBuildingName(r.getValue(a.NAME));
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
	public TotalBuildingStaticsDTO getTotalBuildingStatics(Integer namespaceId, Long communityId,
			List<Long> buildingIds, String dateStr) {
		TotalBuildingStaticsDTO result = new TotalBuildingStaticsDTO();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPropertyStatisticBuilding a = Tables.EH_PROPERTY_STATISTIC_BUILDING;
		
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(DSL.countDistinct(a.BUILDING_ID),DSL.sum(a.TOTAL_APARTMENT_COUNT),DSL.sum(a.FREE_APARTMENT_COUNT),
				DSL.sum(a.RENT_APARTMENT_COUNT),DSL.sum(a.OCCUPIED_APARTMENT_COUNT),DSL.sum(a.LIVING_APARTMENT_COUNT),
				DSL.sum(a.SALED_APARTMENT_COUNT),DSL.sum(a.AREA_SIZE),DSL.sum(a.RENT_AREA),DSL.sum(a.FREE_AREA));
		query.addFrom(a);
		query.addConditions(a.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(a.COMMUNITY_ID.eq(communityId));
		query.addConditions(a.BUILDING_ID.in(buildingIds));
		query.addConditions(a.DATE_STR.eq(dateStr));
		query.addConditions(a.STATUS.eq(PropertyReportFormStatus.ACTIVE.getCode()));
		query.fetch().forEach(r->{
			
			Integer buildingCount = r.getValue(DSL.countDistinct(a.BUILDING_ID));
			BigDecimal totalApartmentCount = r.getValue(DSL.sum(a.TOTAL_APARTMENT_COUNT));
			BigDecimal freeApartmentCount = r.getValue(DSL.sum(a.FREE_APARTMENT_COUNT));
			BigDecimal rentApartmentCount = r.getValue(DSL.sum(a.RENT_APARTMENT_COUNT));
			BigDecimal occupiedApartmentCount = r.getValue(DSL.sum(a.OCCUPIED_APARTMENT_COUNT));
			BigDecimal livingApartmentCount = r.getValue(DSL.sum(a.LIVING_APARTMENT_COUNT));
			BigDecimal saledApartmentCount = r.getValue(DSL.sum(a.SALED_APARTMENT_COUNT));
			
			result.setBuildingCount(buildingCount!=null ? buildingCount : null);
			result.setTotalApartmentCount(totalApartmentCount!=null ? totalApartmentCount.intValue() : null);
			result.setFreeApartmentCount(freeApartmentCount!=null ? freeApartmentCount.intValue() : null);
			result.setRentApartmentCount(rentApartmentCount!=null ? rentApartmentCount.intValue() : null);
			result.setOccupiedApartmentCount(occupiedApartmentCount!=null ? occupiedApartmentCount.intValue() : null);
			result.setLivingApartmentCount(livingApartmentCount!=null ? livingApartmentCount.intValue() : null);
			result.setSaledApartmentCount(saledApartmentCount!=null ? saledApartmentCount.intValue() : null);
			result.setAreaSize(r.getValue(DSL.sum(a.AREA_SIZE)));
			result.setRentArea(r.getValue(DSL.sum(a.RENT_AREA)));
			result.setFreeArea(r.getValue(DSL.sum(a.FREE_AREA)));
			
			BigDecimal rentRate;
			BigDecimal freeRate;
			if (result.getAreaSize() == null || result.getAreaSize().compareTo(BigDecimal.ZERO)==0) {
				rentRate = BigDecimal.ZERO;
				freeRate = BigDecimal.ZERO;
			}else {
				rentRate = (result.getRentArea()==null ? BigDecimal.ZERO : result.getRentArea()).divide(result.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				freeRate = (result.getFreeArea()==null ? BigDecimal.ZERO : result.getFreeArea()).divide(result.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
			}
			result.setRentRate(rentRate);
			result.setFreeRate(freeRate);
		});
		return result;
	}

	@Override
	public void deleteCommunityDataByDateStr(String dateStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_PROPERTY_STATISTIC_COMMUNITY)
				.where(Tables.EH_PROPERTY_STATISTIC_COMMUNITY.DATE_STR.eq(dateStr))
				.execute();
	}
	
	@Override
	public void deleteBuildingDataByDateStr(String dateStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_PROPERTY_STATISTIC_BUILDING)
				.where(Tables.EH_PROPERTY_STATISTIC_BUILDING.DATE_STR.eq(dateStr))
				.execute();
	}


*/}
