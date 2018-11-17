package com.everhomes.contract.reportForm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.indices.recovery.RecoveryState.Start;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.address.AddressProvider;
import com.everhomes.asset.schedule.DoorAccessSchedule;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.reportForm.ApartmentReportFormDTO;
import com.everhomes.scheduler.AssetDoorAccessJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.util.DateHelper;

/**
 * 合同报表定时任务
 * 
 * @author created by djm
 */
@Component
public class ContractReportFormSchedule implements ApplicationListener<ContextRefreshedEvent> {

		private static final Logger LOGGER = LoggerFactory.getLogger(ContractReportFormSchedule.class);

		@Value("${equipment.ip}")
		private String equipmentIp;

		@Autowired
		private ConfigurationProvider configurationProvider;

		@Autowired
		private ScheduleProvider scheduleProvider;

		//static final String cronExpression = "0/10 * *  * * ?";
		static final String cronExpression = "0 0 3,5 * * ?";
		//final String cronExpression = configurationProvider.getValue("contract.contractreportform.cronexpression", "");

		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			if (event.getApplicationContext().getParent() == null) {
				init();
			}
		}

		public void init() {
			//String cronExpression = configurationProvider.getValue("contract.contractreportform.cronexpression", "");
			String autoReading = "ContractReportFormSchedule " + System.currentTimeMillis();
			String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
			LOGGER.info("================================================contractTaskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
			
			if (taskServer.equals(equipmentIp)) {
				if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
					try {
						scheduleProvider.scheduleCronJob(autoReading, autoReading, cronExpression, ContractReportFormJob.class, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	/*@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private PropertyReportFormProvider propertyReportFormProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private PropertyReportFormService propertyReportFormService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(LOGGER.isInfoEnabled()) {
	        LOGGER.info("start PropertyReportFormJob at " + new Timestamp(DateHelper.currentGMTTime().getTime()));
	    }
	    //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
        	generateReportFormStatics();
        }
	}
	
	//定时统计园区数据的接口
	public void generateReportFormStatics() {
		
		//开事务
		dbProvider.execute((TransactionStatus status) -> {
			//先删掉这个月的的统计数据
			String todayDateStr = getTodayDateStr();
			propertyReportFormProvider.deleteCommunityDataByDateStr(todayDateStr);
			propertyReportFormProvider.deleteBuildingDataByDateStr(todayDateStr);
			
			//开始遍历，进行数据统计
			int pageSize = 5000;
			int totalCount = addressProvider.getTotalApartmentCount();
			int totalPage = 0;
			if (totalCount%pageSize == 0) {
				totalPage = totalCount/pageSize;
			}else {
				totalPage = totalCount/pageSize + 1;
			}
			
			Map<Long, CommunityStatistics> communityResultMap = new HashMap<>();
			Map<Long, BuildingStatistics> buildingResultMap = new HashMap<>();
			//分页遍历开始
			for (int currentPage = 0; currentPage <= totalPage; currentPage++) {
				long startTime = System.currentTimeMillis();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Start PropertyReportFormJob for "+ (currentPage+1) +" time.........");
				}
				
				int startIndex = currentPage * pageSize;
				List<ApartmentReportFormDTO> apartments = addressProvider.findActiveApartments(startIndex,pageSize);
				
				//插入上一页统计得到的数据
				if (currentPage != 0 ) {
					//插入园区信息统计数据
					if (apartments != null && apartments.size() > 0 && communityResultMap.containsKey(apartments.get(0).getCommunityId())) {
						CommunityStatistics remove = communityResultMap.remove(apartments.get(0).getCommunityId());
						createCommunityStatics(communityResultMap);
						communityResultMap.put(remove.getCommunityId(), remove);
					}else{
						createCommunityStatics(communityResultMap);
					}
					//插入楼宇信息统计数据
					if (apartments != null && apartments.size() > 0 && buildingResultMap.containsKey(apartments.get(0).getBuildingId())) {
						BuildingStatistics remove = buildingResultMap.remove(apartments.get(0).getBuildingId());
						createBuildingStatics(buildingResultMap);
						buildingResultMap.put(remove.getBuildingId(), remove);
					}else{
						createBuildingStatics(buildingResultMap);
					}
				}
				
				//生成统计数据
				for (ApartmentReportFormDTO apartment : apartments) {
					//园区信息统计数据
					if (communityResultMap.containsKey(apartment.getCommunityId())) {
						CommunityStatistics communityStatistics = communityResultMap.get(apartment.getCommunityId());
						countApartmentsForCommunity(communityStatistics,apartment.getLivingStatus());
					}else{
						Community community = communityProvider.findCommunityById(apartment.getCommunityId());
						if (community != null) {
							CommunityStatistics communityStatistics = initCommunityStatistics(apartment.getCommunityId());
							communityResultMap.put(apartment.getCommunityId(), communityStatistics);
							countApartmentsForCommunity(communityStatistics,apartment.getLivingStatus());
						}else {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("community is null !!! communityId is " + apartment.getCommunityId());
							}
						}
					}
					//楼宇信息统计数据
					if (buildingResultMap.containsKey(apartment.getBuildingId())) {
						if (apartment.getBuildingId() != null) {
							BuildingStatistics buildingStatistics = buildingResultMap.get(apartment.getBuildingId());
							countApartmentsForBuilding(buildingStatistics,apartment.getLivingStatus());
						}
					}else{
						if(apartment.getBuildingId() != null){
							Building building = communityProvider.findBuildingById(apartment.getBuildingId());
							if (building != null) {
								BuildingStatistics buildingStatistics = initBuildingStatistics(apartment.getBuildingId());
								buildingResultMap.put(apartment.getBuildingId(), buildingStatistics);
								countApartmentsForBuilding(buildingStatistics,apartment.getLivingStatus());
							}else {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("building is null !!! buildingId is " + apartment.getBuildingId());
								}
							}
						}
					}
				}
				
				long endTime = System.currentTimeMillis();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("End PropertyReportFormJob for "+ (currentPage+1) +" time.........");
					LOGGER.debug("PropertyReportFormJob progress for " + (currentPage+1) +" time spend " + (endTime-startTime) + "ms");
				}
				
			}
			return null;
		});
	}

	private void createBuildingStatics(Map<Long, BuildingStatistics> buildingResultMap) {
		Collection<BuildingStatistics> values = buildingResultMap.values();
		for (BuildingStatistics buildingStatistics : values) {
			propertyReportFormProvider.createBuildingStatistics(buildingStatistics);
		}
		buildingResultMap.clear();
	}

	private void createCommunityStatics(Map<Long, CommunityStatistics> communityResultMap) {
		Collection<CommunityStatistics> values = communityResultMap.values();
		for (CommunityStatistics communityStatistics : values) {
			propertyReportFormProvider.createCommunityStatics(communityStatistics);
		}
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
		
		if (community != null) {
			communityStatistics.setNamespaceId(community.getNamespaceId());
			communityStatistics.setCommunityId(communityId);
			communityStatistics.setCommunityName(community.getName());
			
			communityStatistics.setDateStr(getTodayDateStr());
			
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
			if (communityStatistics.getAreaSize() == null || communityStatistics.getAreaSize().compareTo(BigDecimal.ZERO)==0) {
				rentRate = BigDecimal.ZERO;
				freeRate = BigDecimal.ZERO;
			}else {
				rentRate = communityStatistics.getRentArea().divide(communityStatistics.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				freeRate = communityStatistics.getFreeArea().divide(communityStatistics.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
			}
			communityStatistics.setRentRate(rentRate);
			communityStatistics.setFreeRate(freeRate);
			
			communityStatistics.setStatus(PropertyReportFormStatus.ACTIVE.getCode());
		}
		return communityStatistics;
	}
	
	private BuildingStatistics initBuildingStatistics(Long buildingId) {
		BuildingStatistics buildingStatistics = new BuildingStatistics();
		Building building = communityProvider.findBuildingById(buildingId);
		
		if (building != null) {
			buildingStatistics.setNamespaceId(building.getNamespaceId());
			buildingStatistics.setCommunityId(building.getCommunityId());
			buildingStatistics.setBuildingId(buildingId);
			buildingStatistics.setBuildingName(building.getName());
			
			buildingStatistics.setDateStr(getTodayDateStr());
			
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
				rentRate = buildingStatistics.getRentArea().divide(buildingStatistics.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
				freeRate = buildingStatistics.getFreeArea().divide(buildingStatistics.getAreaSize(),2,RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
			}
			buildingStatistics.setRentRate(rentRate);
			buildingStatistics.setFreeRate(freeRate);
			
			buildingStatistics.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		return buildingStatistics;
	}*/
	
/*	private String getTodayDateStr(){
		Date currentTime = DateHelper.currentGMTTime();
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
		String todayDateStr = yyyyMM.format(currentTime);
		return todayDateStr;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}

}
*/