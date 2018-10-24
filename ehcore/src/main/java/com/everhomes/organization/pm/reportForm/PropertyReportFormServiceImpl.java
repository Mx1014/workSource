package com.everhomes.organization.pm.reportForm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.address.ApartmentDTO;
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
	public List<CommunityReportFormDTO> getCommunityReportForm(GetCommunityReportFormCommand cmd) {
		
		
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
	
	/**
	 * 提供给缴费报表的项目信息列表的接口
	 * @param namespaceId
	 * @param communityIdList
	 * @param dateStr（年份传2018，或者传月份2018-07）
	 * @return
	 */
	@Override
	public Map<Long, CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList, String dateStr){
		
		dateStr = formatDateStr(dateStr);
		
		Map<Long, CommunityBriefStaticsDTO> result = propertyReportFormProvider.listCommunityBriefStaticsForBill(namespaceId,communityIdList,dateStr);
		
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
		dateStr = formatDateStr(dateStr);
		CommunityTotalStaticsDTO result = propertyReportFormProvider.getTotalCommunityBriefStaticsForBill(namespaceId,communityIdList,dateStr);
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
		dateStr = formatDateStr(dateStr);
		Map<String,BuildingBriefStaticsDTO> result = propertyReportFormProvider.listBuildingBriefStaticsForBill(namespaceId,communityId,buildingNameList,dateStr);
		
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
		dateStr = formatDateStr(dateStr);
		BuildingTotalStaticsDTO result = propertyReportFormProvider.getTotalBuildingBriefStaticsForBill(namespaceId,communityId,buildingNameList,dateStr);
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
		if (dateStr.matches("\\d{4}")) {
			String todayYearStr = getTodayYearStr();
			if (dateStr.equals(todayYearStr)) {
				dateStr = getTodayMonthStr();
			}else {
				dateStr = dateStr + "-12";
			}
			return dateStr;
		}else if (dateStr.matches("\\d{4}-\\d{2}")) {
			return dateStr;
		}else {
			//可以抛出异常
		}
		return dateStr;
	}

}
