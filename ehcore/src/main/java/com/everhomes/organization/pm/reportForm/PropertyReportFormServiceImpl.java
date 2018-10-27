package com.everhomes.organization.pm.reportForm;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.everhomes.asset.statistic.AssetStatisticCommunityExportHandler;
import com.everhomes.asset.statistic.AssetStatisticService;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.namespace.NamespacesProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
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
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.scheduler.EnergyAutoReadingJob;
import com.everhomes.scheduler.EnergyTaskScheduleJob;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PropertyReportFormServiceImpl implements PropertyReportFormService,ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReportFormServiceImpl.class);
	
	@Autowired
	private AssetStatisticService assetStatisticService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private PropertyReportFormProvider propertyReportFormProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private ContractProvider contractProvider;
	
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
		
	    List<Long> communityIds = cmd.getCommunityIds();
	    
		TotalCommunityStaticsDTO result = propertyReportFormProvider.getTotalCommunityStatics(cmd.getNamespaceId(),communityIds,formatDateStr);
		ListBillStatisticByCommunityDTO billTotalStatistic = assetStatisticService.listBillStatisticByCommunityTotalForProperty(cmd.getNamespaceId(), 
				                                                           communityIds, "community", null, formatDateStr);
		
		result.setCommunityCount(communityIds==null ? 0 : communityIds.size());
		result.setAmountReceivable(billTotalStatistic.getAmountReceivable());
		result.setAmountReceived(billTotalStatistic.getAmountReceived());
		result.setAmountOwed(billTotalStatistic.getAmountOwed());
		result.setDueDayCount(billTotalStatistic.getDueDayCount());
		result.setCollectionRate(billTotalStatistic.getCollectionRate());
		
		return result;
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
        
        List<BuildingReportFormDTO> resultList = propertyReportFormProvider.listBuildingReportForm(cmd.getNamespaceId(),cmd.getCommunityId(),cmd.getBuildingIds(),formatDateStr,pageOffSet,pageSize);
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
		String dateStr = cmd.getDateStr();
	    String formatDateStr = formatDateStr(dateStr);
		
	    List<Long> buildingIds = cmd.getBuildingIds();
	    
	    TotalBuildingStaticsDTO result = propertyReportFormProvider.getTotalBuildingStatics(cmd.getNamespaceId(),cmd.getCommunityId(),buildingIds,formatDateStr);
	    
	    List<Building> buildingList = communityProvider.findBuildingsByIds(buildingIds);
        List<String> buildingNameList = new ArrayList<>();
        for (Building building : buildingList) {
        	buildingNameList.add(building.getName());
		}
        ListBillStatisticByBuildingDTO billTotalStatistic = assetStatisticService.listBillStatisticByBuildingTotalForProperty(cmd.getNamespaceId(), 
				                                                           cmd.getCommunityId(), "building", null, formatDateStr,buildingNameList);
		result.setBuildingCount(buildingIds==null ? 0 : buildingIds.size());
        result.setAmountReceivable(billTotalStatistic.getAmountReceivable());
		result.setAmountReceived(billTotalStatistic.getAmountReceived());
		result.setAmountOwed(billTotalStatistic.getAmountOwed());
		result.setDueDayCount(billTotalStatistic.getDueDayCount());
		result.setCollectionRate(billTotalStatistic.getCollectionRate());
		
		return result;
	}
	
	@Override
	public void exportCommunityReportForm(GetTotalCommunityStaticsCommand cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("GetTotalCommunityStaticsCommand", cmd);
		//String exportFileNamePrefix = cmd.getExportFileNamePrefix();
		String fileName = String.format("项目信息汇总表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), CommunityReportFormExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}
	
	@Override
	public OutputStream exportOutputStreamForCommunity(GetTotalCommunityStaticsCommand cmd, Long taskId) {
		OutputStream outputStream = new ByteArrayOutputStream();
    	//每一条数据
		GetCommunityReportFormCommand cmd2 = new GetCommunityReportFormCommand();
		cmd2.setNamespaceId(cmd.getNamespaceId());
		cmd2.setCommunityIds(cmd.getCommunityIds());
		cmd2.setDateStr(cmd.getDateStr());
		cmd2.setPageAnchor(0L);
		cmd2.setPageSize(10000);
		
        ListCommunityReportFormResponse response = getCommunityReportForm(cmd2); 
        List<CommunityReportFormDTO> dtos = response.getResultList();
    
        taskService.updateTaskProcess(taskId, 20);
		Workbook wb = null;
		InputStream in;
		in = this.getClass().getResourceAsStream("/excels/property/communityStatistic.xlsx");
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("exportOutputStreamForCommunity copy inputStream error.");
		}
		Sheet sheet = wb.getSheetAt(0);
		if (null != sheet) {
			Row defaultRow = sheet.getRow(2);
			Cell cell = defaultRow.getCell(0);
			CellStyle style = cell.getCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中   
			int size = 0;
			if(null != dtos){
				size = dtos.size();
				taskService.updateTaskProcess(taskId, 30);
				for(int i = 0;i < size;i++){
					Row tempRow = sheet.createRow(i + 2);
					CommunityReportFormDTO dto = dtos.get(i);
					int orderNum = i + 1;//序号
					boolean isLastRow = false;
					fillRowCellCommunityStatistic(tempRow, style, isLastRow, orderNum, dto);
				}
				taskService.updateTaskProcess(taskId, 70);
				//最后的一行总计
				CellStyle totalStyle = getTotalStyle(wb, style);
				Row tempRow = sheet.createRow(dtos.size() + 2);
				int orderNum = dtos.size() + 1;//序号
				boolean isLastRow = true;
				TotalCommunityStaticsDTO totalCommunityStatics = getTotalCommunityStatics(cmd);
				fillRowCellTotalCommunityStatistic(tempRow, totalStyle, isLastRow, orderNum, totalCommunityStatics);
				taskService.updateTaskProcess(taskId, 80);
				try {
					wb.write(outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return outputStream;
			}else {
				throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
			}
		}
		return outputStream;
	}
	

	@Override
	public void exportBuildingReportForm(GetTotalBuildingStaticsCommand cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("GetTotalBuildingStaticsCommand", cmd);
		//String exportFileNamePrefix = cmd.getExportFileNamePrefix();
		String fileName = String.format("楼宇信息汇总表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), BuildingReportFormExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}
	
	@Override
	public OutputStream exportOutputStreamForBuilding(GetTotalBuildingStaticsCommand cmd, Long taskId) {
		OutputStream outputStream = new ByteArrayOutputStream();
    	//每一条数据
		GetBuildingReportFormCommand cmd2 = new GetBuildingReportFormCommand();
		cmd2.setNamespaceId(cmd.getNamespaceId());
		cmd2.setCommunityId(cmd.getCommunityId());
		cmd2.setBuildingIds(cmd.getBuildingIds());
		cmd2.setDateStr(cmd.getDateStr());
		cmd2.setPageAnchor(0L);
		cmd2.setPageSize(10000);
		
        ListBuildingReportFormResponse response = getBuildingReportForm(cmd2); 
        List<BuildingReportFormDTO> dtos = response.getResults();
    
        taskService.updateTaskProcess(taskId, 20);
		Workbook wb = null;
		InputStream in;
		in = this.getClass().getResourceAsStream("/excels/property/buildingStatistic.xlsx");
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("exportOutputStreamForBuilding copy inputStream error.");
		}
		Sheet sheet = wb.getSheetAt(0);
		if (null != sheet) {
			Row defaultRow = sheet.getRow(2);
			Cell cell = defaultRow.getCell(0);
			CellStyle style = cell.getCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //居中   
			int size = 0;
			if(null != dtos){
				size = dtos.size();
				taskService.updateTaskProcess(taskId, 30);
				for(int i = 0;i < size;i++){
					Row tempRow = sheet.createRow(i + 2);
					BuildingReportFormDTO dto = dtos.get(i);
					int orderNum = i + 1;//序号
					boolean isLastRow = false;
					fillRowCellBuildingStatistic(tempRow, style, isLastRow, orderNum, dto);
				}
				taskService.updateTaskProcess(taskId, 70);
				//最后的一行总计
				CellStyle totalStyle = getTotalStyle(wb, style);
				Row tempRow = sheet.createRow(dtos.size() + 2);
				int orderNum = dtos.size() + 1;//序号
				boolean isLastRow = true;
				TotalBuildingStaticsDTO totalBuildingStatics = getTotalBuildingStatics(cmd);
				fillRowCellTotalBuildingStatistic(tempRow, totalStyle, isLastRow, orderNum, totalBuildingStatics);
				taskService.updateTaskProcess(taskId, 80);
				try {
					wb.write(outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return outputStream;
			}else {
				throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
			}
		}
		return outputStream;
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
	
	@Override
	public Map<String, BigDecimal> getChargeAreaByContractIdAndAddress(List<Long> contractIds,List<String> buildindNames,List<String> apartmentNames){
		Map<String, BigDecimal> resultMap = contractProvider.getChargeAreaByContractIdAndAddress(contractIds, buildindNames, apartmentNames);
		return resultMap;
	}
	
	@Override
	public BigDecimal getTotalChargeArea(List<Long> contractIds,List<String> buildindNames,List<String> apartmentNames){
		return contractProvider.getTotalChargeArea(contractIds, buildindNames, apartmentNames);
	}
	
	private InputStream copyInputStream(InputStream source) {
		if(null == source)
			 return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		  
		byte[] buffer = new byte[1024];  
		int len;  
		try {
			while ((len = source.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}
			baos.flush();  
		} catch (IOException e) {
			LOGGER.error("ExportTasks is fail, cmd={}");
			throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_FLIE_EXPORT_FAIL,
					"ExportTasks is fail.");
		}  
		// 打开一个新的输入流  
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	@SuppressWarnings("deprecation")
	private CellStyle getTotalStyle(Workbook wb, CellStyle style) {
    	//生成一个字体
		Font font = wb.createFont();
		font.setColor(HSSFColor.BLUE.index);//字体颜色
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体增粗
		//把字体应用到当前的样式
		CellStyle totalStyle = wb.createCellStyle();
		totalStyle.cloneStyleFrom(style);
		totalStyle.setFont(font);
		return totalStyle;
    }
	
	/**
     * @param tempRow ： Excel的行
     * @param style 
     * @param isLastRow ： 是否是最后一行
     * @param orderNum ： 序号
     * @param dto
     */
    private void fillRowCellCommunityStatistic(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, CommunityReportFormDTO dto) {
    	//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue(orderNum);
			
		//项目名称
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getCommunityName());
		
		//项目分类
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		cell3.setCellValue(dto.getCategory());
		
		//楼宇总数
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
		
		//房源总数
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getTotalApartmentCount() != null ? dto.getTotalApartmentCount().toString() : "0");
		
		//待租房源数
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getFreeApartmentCount() != null ? dto.getFreeApartmentCount().toString() : "0");	
		
		//已出租房源数
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getRentApartmentCount() != null ? dto.getRentApartmentCount().toString() : "0");
		
		//已占用房源数
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getOccupiedApartmentCount() != null ? dto.getOccupiedApartmentCount().toString() : "0");
		
		//自用房源数
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getLivingApartmentCount() != null ? dto.getLivingApartmentCount().toString() : "0");
		
		//已售房源数
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getSaledApartmentCount() != null ? dto.getSaledApartmentCount().toString() : "0");
		
		//建筑面积
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		
		//在租面积
		Cell cell12 = tempRow.createCell(11);
		cell12.setCellStyle(style);
		cell12.setCellValue(dto.getRentArea() != null ? dto.getRentArea().toString() : "0");
		
		//可招租面积
		Cell cell13 = tempRow.createCell(12);
		cell13.setCellStyle(style);
		cell13.setCellValue(dto.getFreeArea() != null ? dto.getFreeArea().toString() : "0");
		
		//出租率(%)
		Cell cell14 = tempRow.createCell(13);
		cell14.setCellStyle(style);
		cell14.setCellValue((dto.getRentRate() != null ? dto.getRentRate().toString() : "0") + "%");
		
		//空置率(%)
		Cell cell15 = tempRow.createCell(14);
		cell15.setCellStyle(style);
		cell15.setCellValue((dto.getFreeRate() != null ? dto.getFreeRate().toString() : "0") + "%");
		
		//应收含税金额(元)
		Cell cell16 = tempRow.createCell(15);
		cell16.setCellStyle(style);
		cell16.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		
		//已收金额(元)
		Cell cell17 = tempRow.createCell(16);
		cell17.setCellStyle(style);
		cell17.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		
		//待收金额(元)
		Cell cell18 = tempRow.createCell(17);
		cell18.setCellStyle(style);
		cell18.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		
		//总欠费天数(天)
		Cell cell19 = tempRow.createCell(18);
		cell19.setCellStyle(style);
		cell19.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		
		//收缴率(%)
		Cell cell20 = tempRow.createCell(19);
		cell20.setCellStyle(style);
		cell20.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
	}
	
    private void fillRowCellTotalCommunityStatistic(Row tempRow, CellStyle style, boolean isLastRow, int orderNum,
			TotalCommunityStaticsDTO dto) {
		//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("合计");
			
		//项目总数
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getCommunityCount() != null ? dto.getCommunityCount().toString() : "0");
		
		//项目分类
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		cell3.setCellValue("--");
		
		//楼宇总数
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
		
		//房源总数
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getTotalApartmentCount() != null ? dto.getTotalApartmentCount().toString() : "0");
		
		//待租房源数
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getFreeApartmentCount() != null ? dto.getFreeApartmentCount().toString() : "0");	
		
		//已出租房源数
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getRentApartmentCount() != null ? dto.getRentApartmentCount().toString() : "0");
		
		//已占用房源数
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getOccupiedApartmentCount() != null ? dto.getOccupiedApartmentCount().toString() : "0");
		
		//自用房源数
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getLivingApartmentCount() != null ? dto.getLivingApartmentCount().toString() : "0");
		
		//已售房源数
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getSaledApartmentCount() != null ? dto.getSaledApartmentCount().toString() : "0");
		
		//建筑面积
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		
		//在租面积
		Cell cell12 = tempRow.createCell(11);
		cell12.setCellStyle(style);
		cell12.setCellValue(dto.getRentArea() != null ? dto.getRentArea().toString() : "0");
		
		//可招租面积
		Cell cell13 = tempRow.createCell(12);
		cell13.setCellStyle(style);
		cell13.setCellValue(dto.getFreeArea() != null ? dto.getFreeArea().toString() : "0");
		
		//出租率(%)
		Cell cell14 = tempRow.createCell(13);
		cell14.setCellStyle(style);
		cell14.setCellValue((dto.getRentRate() != null ? dto.getRentRate().toString() : "0") + "%");
		
		//空置率(%)
		Cell cell15 = tempRow.createCell(14);
		cell15.setCellStyle(style);
		cell15.setCellValue((dto.getFreeRate() != null ? dto.getFreeRate().toString() : "0") + "%");
		
		//应收含税金额(元)
		Cell cell16 = tempRow.createCell(15);
		cell16.setCellStyle(style);
		cell16.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		
		//已收金额(元)
		Cell cell17 = tempRow.createCell(16);
		cell17.setCellStyle(style);
		cell17.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		
		//待收金额(元)
		Cell cell18 = tempRow.createCell(17);
		cell18.setCellStyle(style);
		cell18.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		
		//总欠费天数(天)
		Cell cell19 = tempRow.createCell(18);
		cell19.setCellStyle(style);
		cell19.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		
		//收缴率(%)
		Cell cell20 = tempRow.createCell(19);
		cell20.setCellStyle(style);
		cell20.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
	}
    
	private void fillRowCellBuildingStatistic(Row tempRow, CellStyle style, boolean isLastRow, int orderNum,
			BuildingReportFormDTO dto) {
		//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue(orderNum);
			
		//楼宇名称
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getBuildingName());
		
		//房源总数
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		cell3.setCellValue(dto.getTotalApartmentCount() != null ? dto.getTotalApartmentCount().toString() : "0");
		
		//待租房源数
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getFreeApartmentCount() != null ? dto.getFreeApartmentCount().toString() : "0");	
		
		//已出租房源数
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getRentApartmentCount() != null ? dto.getRentApartmentCount().toString() : "0");
		
		//已占用房源数
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getOccupiedApartmentCount() != null ? dto.getOccupiedApartmentCount().toString() : "0");
		
		//自用房源数
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getLivingApartmentCount() != null ? dto.getLivingApartmentCount().toString() : "0");
		
		//已售房源数
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getSaledApartmentCount() != null ? dto.getSaledApartmentCount().toString() : "0");
		
		//建筑面积
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		
		//在租面积
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getRentArea() != null ? dto.getRentArea().toString() : "0");
		
		//可招租面积
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getFreeArea() != null ? dto.getFreeArea().toString() : "0");
		
		//出租率(%)
		Cell cell12 = tempRow.createCell(11);
		cell12.setCellStyle(style);
		cell12.setCellValue((dto.getRentRate() != null ? dto.getRentRate().toString() : "0") + "%");
		
		//空置率(%)
		Cell cell13 = tempRow.createCell(12);
		cell13.setCellStyle(style);
		cell13.setCellValue((dto.getFreeRate() != null ? dto.getFreeRate().toString() : "0") + "%");
		
		//应收含税金额(元)
		Cell cell14 = tempRow.createCell(13);
		cell14.setCellStyle(style);
		cell14.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		
		//已收金额(元)
		Cell cell15 = tempRow.createCell(14);
		cell15.setCellStyle(style);
		cell15.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		
		//待收金额(元)
		Cell cell16 = tempRow.createCell(15);
		cell16.setCellStyle(style);
		cell16.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		
		//总欠费天数(天)
		Cell cell17 = tempRow.createCell(16);
		cell17.setCellStyle(style);
		cell17.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		
		//收缴率(%)
		Cell cell18 = tempRow.createCell(17);
		cell18.setCellStyle(style);
		cell18.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
	}
	
	private void fillRowCellTotalBuildingStatistic(Row tempRow, CellStyle style, boolean isLastRow, int orderNum,
			TotalBuildingStaticsDTO dto) {
		//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("合计");
			
		//楼宇名称
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
		
		//房源总数
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		cell3.setCellValue(dto.getTotalApartmentCount() != null ? dto.getTotalApartmentCount().toString() : "0");
		
		//待租房源数
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getFreeApartmentCount() != null ? dto.getFreeApartmentCount().toString() : "0");	
		
		//已出租房源数
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getRentApartmentCount() != null ? dto.getRentApartmentCount().toString() : "0");
		
		//已占用房源数
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getOccupiedApartmentCount() != null ? dto.getOccupiedApartmentCount().toString() : "0");
		
		//自用房源数
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getLivingApartmentCount() != null ? dto.getLivingApartmentCount().toString() : "0");
		
		//已售房源数
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getSaledApartmentCount() != null ? dto.getSaledApartmentCount().toString() : "0");
		
		//建筑面积
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		
		//在租面积
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getRentArea() != null ? dto.getRentArea().toString() : "0");
		
		//可招租面积
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getFreeArea() != null ? dto.getFreeArea().toString() : "0");
		
		//出租率(%)
		Cell cell12 = tempRow.createCell(11);
		cell12.setCellStyle(style);
		cell12.setCellValue((dto.getRentRate() != null ? dto.getRentRate().toString() : "0") + "%");
		
		//空置率(%)
		Cell cell13 = tempRow.createCell(12);
		cell13.setCellStyle(style);
		cell13.setCellValue((dto.getFreeRate() != null ? dto.getFreeRate().toString() : "0") + "%");
		
		//应收含税金额(元)
		Cell cell14 = tempRow.createCell(13);
		cell14.setCellStyle(style);
		cell14.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		
		//已收金额(元)
		Cell cell15 = tempRow.createCell(14);
		cell15.setCellStyle(style);
		cell15.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		
		//待收金额(元)
		Cell cell16 = tempRow.createCell(15);
		cell16.setCellStyle(style);
		cell16.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		
		//总欠费天数(天)
		Cell cell17 = tempRow.createCell(16);
		cell17.setCellStyle(style);
		cell17.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		
		//收缴率(%)
		Cell cell18 = tempRow.createCell(17);
		cell18.setCellStyle(style);
		cell18.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
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
