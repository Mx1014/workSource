package com.everhomes.organization.pm.reportForm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.schedule.AssetSchedule;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.reportForm.ApartmentReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.DateHelper;

@Component
public class PropertyReportFormServiceImpl implements PropertyReportFormService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReportFormServiceImpl.class);
	
	@Autowired
	private NamespacesProvider namespacesProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private PropertyReportFormProvider assetReportFormProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public List<CommunityReportFormDTO> getCommunityReportForm(GetCommunityReportFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportCommunityReportForm(GetCommunityReportFormCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BuildingReportFormDTO> getBuildingReportForm(GetBuildingReportFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportBuildingReportForm(GetBuildingReportFormCommand cmd) {
		// TODO Auto-generated method stub
		
	}
	
	//定时统计园区数据的接口
	public void generateReportFormStatics() {
		int pageSize = 5000;
		int totalCount = addressProvider.getTotalApartmentCount();
		int totalPage = 0;
		if (totalCount%pageSize==0) {
			totalPage = totalCount/pageSize;
		}else {
			totalPage = totalCount/pageSize + 1;
		}
		
		Map<Long, CommunityStatistics> communityResultMap = new HashMap<>();
		Map<Long, BuildingStatistics> buildingResultMap = new HashMap<>();
		
		for (int currentPage = 0; currentPage < totalPage; currentPage++) {
			int startIndex = currentPage * pageSize;
			List<ApartmentReportFormDTO> apartments = addressProvider.findActiveApartments(startIndex,pageSize);
			
			if (currentPage != 0 && apartments != null && apartments.size() > 0) {
				//插入园区信息统计数据
				if (communityResultMap.containsKey(apartments.get(0).getCommunityId())) {
					CommunityStatistics remove = communityResultMap.remove(apartments.get(0).getCommunityId());
					createCommunityStatics(communityResultMap);
					communityResultMap.put(remove.getCommunityId(), remove);
				}else{
					createCommunityStatics(communityResultMap);
				}
				//插入楼宇信息统计数据
				if (buildingResultMap.containsKey(apartments.get(0).getBuildingId())) {
					BuildingStatistics remove = buildingResultMap.remove(apartments.get(0).getBuildingId());
					createBuildingStatics(buildingResultMap);
					buildingResultMap.put(remove.getBuildingId(), remove);
				}else{
					createBuildingStatics(buildingResultMap);
				}
			}
			
			for (ApartmentReportFormDTO apartment : apartments) {
				//园区信息统计数据
				if (communityResultMap.containsKey(apartment.getCommunityId())) {
					CommunityStatistics communityStatistics = communityResultMap.get(apartment.getCommunityId());
					countApartmentsForCommunity(communityStatistics,apartment.getLivingStatus());
				}else{
					CommunityStatistics communityStatistics = initCommunityStatistics(apartment.getCommunityId());
					countApartmentsForCommunity(communityStatistics,apartment.getLivingStatus());
				}
				//楼宇信息统计数据
				if (buildingResultMap.containsKey(apartment.getBuildingId())) {
					BuildingStatistics buildingStatistics = buildingResultMap.get(apartment.getBuildingId());
					countApartmentsForBuilding(buildingStatistics,apartment.getLivingStatus());
				}else{
					BuildingStatistics buildingStatistics = initBuildingStatistics(apartment.getBuildingId());
					countApartmentsForBuilding(buildingStatistics,apartment.getLivingStatus());
				}
			}
		}
	}

	private void createBuildingStatics(Map<Long, BuildingStatistics> buildingResultMap) {
		Collection<BuildingStatistics> values = buildingResultMap.values();
		assetReportFormProvider.batchAddBuildingStatistics(values);
		buildingResultMap.clear();
	}

	private void createCommunityStatics(Map<Long, CommunityStatistics> communityResultMap) {
		Collection<CommunityStatistics> values = communityResultMap.values();
		assetReportFormProvider.batchAddCommunityStatics(values);
		communityResultMap.clear();
	}

	private void countApartmentsForBuilding(BuildingStatistics buildingStatistics, Byte livingStatus) {
		buildingStatistics.setTotalApartmentCount(buildingStatistics.getTotalApartmentCount() + 1);
		
		AddressMappingStatus addressMappingStatus = AddressMappingStatus.fromCode(livingStatus);
		if (addressMappingStatus == null) {
			buildingStatistics.setLivingApartmentCount(buildingStatistics.getLivingApartmentCount() + 1);
		}else {
			switch (addressMappingStatus) {
				case LIVING:
					buildingStatistics.setLivingApartmentCount(buildingStatistics.getLivingApartmentCount() + 1);
					break;
				case RENT:
					buildingStatistics.setRentApartmentCount(buildingStatistics.getRentApartmentCount() + 1);
					break;
				case FREE:
					buildingStatistics.setFreeApartmentCount(buildingStatistics.getFreeApartmentCount() + 1);
					break;
				case SALED:
					buildingStatistics.setSaledApartmentCount(buildingStatistics.getSaledApartmentCount() + 1);
					break;	
				case OCCUPIED:
					buildingStatistics.setOccupiedApartmentCount(buildingStatistics.getOccupiedApartmentCount() + 1);
					break;		
				default:
					break;
			}
		}
	}

	private void countApartmentsForCommunity(CommunityStatistics communityStatistics, Byte livingStatus) {
		communityStatistics.setTotalApartmentCount(communityStatistics.getTotalApartmentCount() + 1);
		
		AddressMappingStatus addressMappingStatus = AddressMappingStatus.fromCode(livingStatus);
		if (addressMappingStatus == null) {
			communityStatistics.setLivingApartmentCount(communityStatistics.getLivingApartmentCount() + 1);
		}else {
			switch (addressMappingStatus) {
				case LIVING:
					communityStatistics.setLivingApartmentCount(communityStatistics.getLivingApartmentCount() + 1);
					break;
				case RENT:
					communityStatistics.setRentApartmentCount(communityStatistics.getRentApartmentCount() + 1);
					break;
				case FREE:
					communityStatistics.setFreeApartmentCount(communityStatistics.getFreeApartmentCount() + 1);
					break;
				case SALED:
					communityStatistics.setSaledApartmentCount(communityStatistics.getSaledApartmentCount() + 1);
					break;	
				case OCCUPIED:
					communityStatistics.setOccupiedApartmentCount(communityStatistics.getOccupiedApartmentCount() + 1);
					break;		
				default:
					break;
			}
		}
	}

	private CommunityStatistics initCommunityStatistics(Long communityId) {
		CommunityStatistics communityStatistics = new CommunityStatistics();
		Community community = communityProvider.findCommunityById(communityId);
		
		communityStatistics.setCommunityId(communityId);
		communityStatistics.setCommunityName(community.getName());
		
		Integer buildingCount = communityProvider.countActiveBuildingsByCommunityId(community.getId());
		communityStatistics.setBuildingCount(buildingCount);
		
		communityStatistics.setTotalApartmentCount(0);
		communityStatistics.setFreeApartmentCount(0);
		communityStatistics.setRentApartmentCount(0);
		communityStatistics.setOccupiedApartmentCount(0);
		communityStatistics.setLivingApartmentCount(0);
		communityStatistics.setSaledApartmentCount(0);
		
		communityStatistics.setAreaSize(BigDecimal.valueOf(community.getAreaSize()!=null?community.getAreaSize():0.0));
		communityStatistics.setRentArea(BigDecimal.valueOf(community.getRentArea()!=null?community.getRentArea():0.0));
		communityStatistics.setFreeArea(BigDecimal.valueOf(community.getFreeArea()!=null?community.getFreeArea():0.0));
		
		BigDecimal rentRate;
		BigDecimal freeRate;
		if (communityStatistics.getAreaSize().compareTo(BigDecimal.ZERO)==0) {
			rentRate = BigDecimal.ZERO;
			freeRate = BigDecimal.ZERO;
		}else {
			rentRate = communityStatistics.getRentArea().divide(communityStatistics.getAreaSize()).multiply(new BigDecimal("100"));
			freeRate = communityStatistics.getFreeArea().divide(communityStatistics.getAreaSize()).multiply(new BigDecimal("100"));
		}
		communityStatistics.setRentRate(rentRate);
		communityStatistics.setFreeRate(freeRate);
		
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CommunityStatistics.class));
		communityStatistics.setId(id);
		communityStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		communityStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		return communityStatistics;
	}
	
	private BuildingStatistics initBuildingStatistics(Long buildingId) {
		BuildingStatistics buildingStatistics = new BuildingStatistics();
		Building building = communityProvider.findBuildingById(buildingId);
		
		buildingStatistics.setBuildingId(buildingId);
		buildingStatistics.setBuildingName(building.getName());
		
		buildingStatistics.setTotalApartmentCount(0);
		buildingStatistics.setFreeApartmentCount(0);
		buildingStatistics.setRentApartmentCount(0);
		buildingStatistics.setOccupiedApartmentCount(0);
		buildingStatistics.setLivingApartmentCount(0);
		buildingStatistics.setSaledApartmentCount(0);
		
		buildingStatistics.setAreaSize(BigDecimal.valueOf(building.getAreaSize() != null ? building.getAreaSize():0.0));
		buildingStatistics.setRentArea(BigDecimal.valueOf(building.getRentArea() != null ? building.getRentArea():0.0));
		buildingStatistics.setFreeArea(BigDecimal.valueOf(building.getFreeArea() != null ? building.getFreeArea():0.0));
		
		BigDecimal rentRate;
		BigDecimal freeRate;
		if (buildingStatistics.getAreaSize().compareTo(BigDecimal.ZERO)==0) {
			rentRate = BigDecimal.ZERO;
			freeRate = BigDecimal.ZERO;
		}else {
			rentRate = buildingStatistics.getRentArea().divide(buildingStatistics.getAreaSize()).multiply(new BigDecimal("100"));
			freeRate = buildingStatistics.getFreeArea().divide(buildingStatistics.getAreaSize()).multiply(new BigDecimal("100"));
		}
		buildingStatistics.setRentRate(rentRate);
		buildingStatistics.setFreeRate(freeRate);
		
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(BuildingStatistics.class));
		buildingStatistics.setId(id);
		buildingStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		buildingStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		return buildingStatistics;
	}
	

}
