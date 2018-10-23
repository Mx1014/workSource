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
	
	/**
	 * 提供给缴费报表的项目信息列表的接口
	 * @param namespaceId
	 * @param communityIdList
	 * @param dateStr
	 * @return
	 */
	@Override
	public List<CommunityBriefStaticsDTO> listCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList, String dateStr){
		return null;
	}
	
	/**
	 * 提供给缴费报表的项目信息汇总信息的接口
	 * @param namespaceId
	 * @param communityIdList
	 * @param dateStr
	 * @return
	 */
	@Override
	public CommunityTotalStaticsDTO getTotalCommunityBriefStaticsForBill(Integer namespaceId, List<Long> communityIdList, String dateStr){
		return null;
	}
	
	/**
	 * 提供给缴费报表的楼宇信息列表的接口
	 * @param namespaceId
	 * @param communityId
	 * @param buildingNameList
	 * @param dateStr
	 * @return
	 */
	@Override
	public List<BuildingBriefStaticsDTO> listBuildingBriefStaticsForBill(Integer namespaceId,Long communityId ,List<String> buildingNameList, String dateStr){
		return null;
	}
	
	/**
	 * 提供给缴费报表的楼宇信息汇总信息的接口
	 * @param namespaceId
	 * @param communityId
	 * @param buildingNameList
	 * @param dateStr
	 * @return
	 */
	@Override
	public BuildingTotalStaticsDTO getTotalBuildingBriefStaticsForBill(Integer namespaceId,Long communityId,List<String> buildingNameList, String dateStr){
		return null;
	}

}
