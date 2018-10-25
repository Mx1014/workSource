package com.everhomes.organization.pm.reportForm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.schedule.AssetSchedule;
import com.everhomes.asset.statistic.AssetStatisticService;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.organization.pm.reportForm.ApartmentReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.BuildingTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityBriefStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityReportFormDTO;
import com.everhomes.rest.organization.pm.reportForm.CommunityTotalStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.GetBuildingReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetCommunityReportFormCommand;
import com.everhomes.rest.organization.pm.reportForm.GetTotalBuildingStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.GetTotalCommunityStaticsCommand;
import com.everhomes.rest.organization.pm.reportForm.ListBuildingReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.ListCommunityReportFormResponse;
import com.everhomes.rest.organization.pm.reportForm.TotalBuildingStaticsDTO;
import com.everhomes.rest.organization.pm.reportForm.TotalCommunityStaticsDTO;
import com.everhomes.scheduler.EnergyAutoReadingJob;
import com.everhomes.scheduler.EnergyTaskScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.DateHelper;

@Component
public class PropertyReportFormServiceImpl implements PropertyReportFormService,ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReportFormServiceImpl.class);
	
	@Autowired
	private AssetStatisticService assetStatisticService;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private PropertyReportFormProvider propertyReportFormProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Value("${equipment.ip}")
    private String equipmentIp;
	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }

    public void init() {
        String cronExpression = configurationProvider.getValue(ConfigConstants.SCHEDULE_PROPERTY_TASK_TIME, "0 30 2 * * ? ");
        String propertyReportFormTrigger = "PropertyReportFormTask " + System.currentTimeMillis();
        String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
        LOGGER.info("=======================PropertyReportFormTaskServer: " + taskServer + ", equipmentIp: " + equipmentIp);
        if (taskServer.equals(equipmentIp)) {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                scheduleProvider.scheduleCronJob(propertyReportFormTrigger, propertyReportFormTrigger,
                        cronExpression, PropertyReportFormJob.class, null);
            }
        }
    }
    
	@Override
	public ListCommunityReportFormResponse getCommunityReportForm(GetCommunityReportFormCommand cmd) {
		ListCommunityReportFormResponse response = new ListCommunityReportFormResponse();
		
		Long pageAnchor = cmd.getPageAnchor();
		if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        
		Integer pageSize = cmd.getPageSize();
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        
        String dateStr = cmd.getDateStr();
        String formatDateStr = formatDateStr(dateStr);
        
        List<CommunityReportFormDTO> resultList = propertyReportFormProvider.listCommunityReportForm(cmd.getNamespaceId(),cmd.getCommunityIds(),formatDateStr,pageOffSet,pageSize);
        if(resultList.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            resultList.remove(resultList.size()-1);
        }
        
        List<ListBillStatisticByCommunityDTO> billStatisticList = assetStatisticService.listBillStatisticByCommunityForProperty(cmd.getNamespaceId(),cmd.getCommunityIds(), "community", null, formatDateStr);
        Map<Long, ListBillStatisticByCommunityDTO> billStatisticMap = new HashMap<>();
        billStatisticList.stream().forEach(b->billStatisticMap.put(b.getOwnerId(), b));
        
        for (CommunityReportFormDTO result : resultList) {
        	ListBillStatisticByCommunityDTO billStatistic = billStatisticMap.get(result.getCommunityId());
        	if (billStatistic != null) {
				result.setAmountReceivable(billStatistic.getAmountReceivable());
				result.setAmountReceived(billStatistic.getAmountReceived());
        		result.setAmountOwed(billStatistic.getAmountOwed());
        		result.setDueDayCount(billStatistic.getDueDayCount());
        		result.setCollectionRate(billStatistic.getCollectionRate());
			}
		}
        response.setResultList(resultList);
		return response;
	}
	
	@Override
	public TotalCommunityStaticsDTO getTotalCommunityStatics(GetTotalCommunityStaticsCommand cmd){
		String dateStr = cmd.getDateStr();
	    String formatDateStr = formatDateStr(dateStr);
		
		TotalCommunityStaticsDTO result = propertyReportFormProvider.getTotalCommunityStatics(cmd.getNamespaceId(),cmd.getCommunityIds(),formatDateStr);
		ListBillStatisticByCommunityDTO billTotalStatistic = assetStatisticService.listBillStatisticByCommunityTotalForProperty(cmd.getNamespaceId(), 
				                                                           cmd.getCommunityIds(), "community", null, formatDateStr);
		
		result.setAmountReceivable(billTotalStatistic.getAmountReceivable());
		result.setAmountReceived(billTotalStatistic.getAmountReceived());
		result.setAmountOwed(billTotalStatistic.getAmountOwed());
		result.setDueDayCount(billTotalStatistic.getDueDayCount());
		result.setCollectionRate(billTotalStatistic.getCollectionRate());
		
		return result;
	}

	@Override
	public void exportCommunityReportForm(GetTotalCommunityStaticsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListBuildingReportFormResponse getBuildingReportForm(GetBuildingReportFormCommand cmd) {
		ListBuildingReportFormResponse response = new ListBuildingReportFormResponse();
		
		Long pageAnchor = cmd.getPageAnchor();
		if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        
		Integer pageSize = cmd.getPageSize();
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        
        String dateStr = cmd.getDateStr();
        String formatDateStr = formatDateStr(dateStr);
        
        List<BuildingReportFormDTO> resultList = null;
        //List<BuildingReportFormDTO> resultList = propertyReportFormProvider.listBuildingReportForm(cmd.getNamespaceId(),cmd.getCommunityId(),cmd.getBuildingIds(),formatDateStr,pageOffSet,pageSize);
        if(resultList.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            resultList.remove(resultList.size()-1);
        }
        
        List<Building> buildingList = communityProvider.findBuildingsByIds(cmd.getBuildingIds());
        List<String> buildingNameList = new ArrayList<>();
        for (Building building : buildingList) {
        	buildingNameList.add(building.getName());
		}
        
        List<ListBillStatisticByBuildingDTO> billStatisticList = assetStatisticService.listBillStatisticByBuildingForProperty(cmd.getNamespaceId(),
        		cmd.getCommunityId(), "building", null, formatDateStr,buildingNameList);
        Map<String, ListBillStatisticByBuildingDTO> billStatisticMap = new HashMap<>();
        billStatisticList.stream().forEach(b->billStatisticMap.put(b.getBuildingName(), b));
        
        for (BuildingReportFormDTO result : resultList) {
        	ListBillStatisticByBuildingDTO billStatistic = billStatisticMap.get(result.getBuildingName());
        	if (billStatistic != null) {
				result.setAmountReceivable(billStatistic.getAmountReceivable());
				result.setAmountReceived(billStatistic.getAmountReceived());
        		result.setAmountOwed(billStatistic.getAmountOwed());
        		result.setDueDayCount(billStatistic.getDueDayCount());
        		result.setCollectionRate(billStatistic.getCollectionRate());
			}
		}
        response.setResults(resultList);
		return response;
	}

	@Override
	public TotalBuildingStaticsDTO getTotalBuildingStatics(GetTotalBuildingStaticsCommand cmd) {
		
		
		
		return null;
	}
	
	@Override
	public void exportBuildingReportForm(GetTotalBuildingStaticsCommand cmd) {
		
		
	}
	
	/**
	 * 提供给缴费报表的项目信息列表的接口
	 * @param namespaceId
	 * @param communityIdList
	 * @param dateStr（年份传2018，或者传月份2018-07）
	 * @return
	 */
	@Override
	public Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList, String dateStr){
		
		String formatDateStr = formatDateStr(dateStr);
		
		Map<Long, CommunityBriefStaticsDTO> result = propertyReportFormProvider.listCommunityBriefStaticsForBill(namespaceId,communityIdList,formatDateStr);
		
		for(Long communityId : communityIdList){
			CommunityBriefStaticsDTO dto = result.get(communityId);
			if (dto != null) {
				String category = communityProvider.findCommunityCategoryByCommunityId(communityId);
				dto.setCategory(category);
			}else {
				CommunityBriefStaticsDTO communityBriefStaticsDTO = new CommunityBriefStaticsDTO();
				String category = communityProvider.findCommunityCategoryByCommunityId(communityId);
				communityBriefStaticsDTO.setCategory(category);
				result.put(communityId, communityBriefStaticsDTO);
			}
		}
		return result;
	}
	
	/**
	 * 提供给缴费报表的项目信息汇总信息的接口
	 * @param namespaceId
	 * @param communityIdList
	 * @param dateStr（年份传2018，或者传月份2018-07）
	 * @return
	 */
	@Override
	public CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList, String dateStr){
		String formatDateStr = formatDateStr(dateStr);
		CommunityTotalStaticsDTO result = propertyReportFormProvider.getTotalCommunityBriefStaticsForBill(namespaceId,communityIdList,formatDateStr);
		result.setCommunityCount(communityIdList.size());
		return result;
	}
	
	/**
	 * 提供给缴费报表的楼宇信息列表的接口
	 * @param namespaceId
	 * @param communityId
	 * @param buildingNameList
	 * @param dateStr（年份传2018，或者传月份2018-07）
	 * @return
	 */
	@Override
	public Map<String,BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId,Long communityId ,List<String> buildingNameList, String dateStr){
		String formatDateStr = formatDateStr(dateStr);
		Map<String,BuildingBriefStaticsDTO> result = propertyReportFormProvider.listBuildingBriefStaticsForBill(namespaceId,communityId,buildingNameList,formatDateStr);
		
		for (String buildingName : buildingNameList) {
			BuildingBriefStaticsDTO dto = result.get(buildingName);
			if (dto == null) {
				BuildingBriefStaticsDTO buildingBriefStaticsDTO = new BuildingBriefStaticsDTO();
				result.put(buildingName, buildingBriefStaticsDTO);
			}
		}
		return result;
	}
	
	/**
	 * 提供给缴费报表的楼宇信息汇总信息的接口
	 * @param namespaceId
	 * @param communityId
	 * @param buildingNameList
	 * @param dateStr（传年份例子：2018，传月份例子：2018-07）
	 * @return
	 */
	@Override
	public BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId,Long communityId,List<String> buildingNameList, String dateStr){
		String formatDateStr = formatDateStr(dateStr);
		BuildingTotalStaticsDTO result = propertyReportFormProvider.getTotalBuildingBriefStaticsForBill(namespaceId,communityId,buildingNameList,formatDateStr);
		result.setBuildindCount(buildingNameList.size());
		return result;
	}
	
	private String getTodayYearStr(){
		Date currentTime = DateHelper.currentGMTTime();
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy");
		String todayDateStr = yyyyMM.format(currentTime);
		return todayDateStr;
	}
	
	private String getTodayMonthStr(){
		Date currentTime = DateHelper.currentGMTTime();
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
		String todayDateStr = yyyyMM.format(currentTime);
		return todayDateStr;
	}
	
	//如果是2018这种年份字符串，那就要处理
	private String formatDateStr(String dateStr){
		String format = null;
		if (dateStr == null) {
			format = getTodayMonthStr();
		}else if (dateStr.matches("\\d{4}")) {
			String todayYearStr = getTodayYearStr();
			if (dateStr.equals(todayYearStr)) {
				format = getTodayMonthStr();
			}else {
				format = dateStr + "-12";
			}
			return format;
		}else if (dateStr.matches("\\d{4}-\\d{2}")) {
			format = dateStr;
			return format;
		}else {
			//抛出异常
		}
		return format;
	}
	
}
