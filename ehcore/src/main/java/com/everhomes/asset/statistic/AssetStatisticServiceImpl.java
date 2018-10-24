
package com.everhomes.asset.statistic;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.filedownload.TaskService;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressTotalCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingTotalCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityTotalCmd;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.user.UserContext;
import com.everhomes.util.excel.ExcelUtils;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStatisticServiceImpl implements AssetStatisticService {
	
	@Autowired
	private AssetStatisticProvider assetStatisticProvider;
	
	@Autowired
	private TaskService taskService;
	
	//项目维度
	public ListBillStatisticByCommunityResponse listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd) {
		ListBillStatisticByCommunityResponse response = new ListBillStatisticByCommunityResponse();
		Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillStatisticByCommunityDTO> list = assetStatisticProvider.listBillStatisticByCommunity(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
		response.setListBillStatisticByCommunityDTOs(list);
		return response;
	}
	
	//楼宇维度
	public ListBillStatisticByBuildingResponse listBillStatisticByBuilding(ListBillStatisticByBuildingCmd cmd) {
		ListBillStatisticByBuildingResponse response = new ListBillStatisticByBuildingResponse();
		Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillStatisticByBuildingDTO> list = assetStatisticProvider.listBillStatisticByBuilding(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingNameList());
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
		response.setListBillStatisticByBuildingDTOs(list);
		return response;
	}
	
	//房源维度
	public ListBillStatisticByAddressResponse listBillStatisticByAddress(ListBillStatisticByAddressCmd cmd) {
		ListBillStatisticByAddressResponse response = new ListBillStatisticByAddressResponse();
		Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillStatisticByAddressDTO> list = assetStatisticProvider.listBillStatisticByAddress(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingName(), cmd.getApartmentNameList(), cmd.getChargingItemIdList(), cmd.getTargetName());
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
		response.setListBillStatisticByAddressDTOs(list);
		return response;
	}
	
	/**
	 * 提供给资产获取“缴费信息汇总表-项目”列表接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId, List<Long> ownerIdList, 
			String ownerType, String dateStrBegin, String dateStrEnd) {
        List<ListBillStatisticByCommunityDTO> list = assetStatisticProvider.listBillStatisticByCommunityForProperty(
        		namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
		return list;
	}
	
	/**
	 * 提供给资产获取“缴费信息汇总表-楼宇”列表接口
	 * @param namespaceId
	 * @param ownerId
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @param buildingNameList
	 * @return
	 */
	public List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingForProperty(Integer namespaceId, Long ownerId, String ownerType,
			String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		List<ListBillStatisticByBuildingDTO> list = assetStatisticProvider.listBillStatisticByBuildingForProperty(namespaceId, ownerId, 
				ownerType, dateStrBegin, dateStrEnd, buildingNameList);
		return list;
	}

	//项目维度
	public ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(ListBillStatisticByCommunityTotalCmd cmd) {
		ListBillStatisticByCommunityDTO dto = assetStatisticProvider.listBillStatisticByCommunityTotal(
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
		return dto;
	}
	
	//楼宇维度
	public ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotal(ListBillStatisticByBuildingTotalCmd cmd) {
		ListBillStatisticByBuildingDTO dto = assetStatisticProvider.listBillStatisticByBuildingTotal(
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingNameList());
		return dto;
	}
	
	//房源维度
	public ListBillStatisticByAddressDTO listBillStatisticByAddressTotal(ListBillStatisticByAddressTotalCmd cmd) {
		ListBillStatisticByAddressDTO dto = assetStatisticProvider.listBillStatisticByAddressTotal(
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingName(), cmd.getApartmentNameList(), cmd.getChargingItemIdList(), cmd.getTargetName());
		return dto;
	}

	/**
	 * 提供给资产获取“缴费信息汇总表-项目-合计”列表接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	public ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotalForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd) {
		ListBillStatisticByCommunityDTO dto = assetStatisticProvider.listBillStatisticByCommunityTotalForProperty(
				namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
		return dto;
	}

	public ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotalForProperty(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList) {
		ListBillStatisticByBuildingDTO dto = assetStatisticProvider.listBillStatisticByBuildingTotalForProperty(namespaceId, ownerId, 
				ownerType, dateStrBegin, dateStrEnd, buildingNameList);
		return dto;
	}
	
	//缴费管理V7.0（新增缴费相关统计报表）：缴费信息汇总表-项目 (导出对接下载中心)
	@Override
	public void exportBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("ListBillStatisticByCommunityCmd", cmd);
		String exportFileNamePrefix = cmd.getExportFileNamePrefix();
		String fileName = String.format(exportFileNamePrefix+"报表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), AssetStatisticCommunityExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	//缴费管理V7.0（新增缴费相关统计报表）：缴费信息汇总表-项目 (导出对接下载中心)
    public OutputStream exportOutputStreamBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd, Long taskId){
    	//每一条数据
    	Integer pageOffSet = 0;
        Integer pageSize = 100000;
		List<ListBillStatisticByCommunityDTO> dtos = assetStatisticProvider.listBillStatisticByCommunity(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
		taskService.updateTaskProcess(taskId, 20);
		//初始化 字段信息
        List<String> propertyNames = new ArrayList<String>();
        List<String> titleName = new ArrayList<String>();
        List<Integer> titleSize = new ArrayList<Integer>();
        propertyNames.add("orderNum");titleName.add("序号");titleSize.add(20);
        propertyNames.add("projectName");titleName.add("项目名称");titleSize.add(20);
        propertyNames.add("projectClassify");titleName.add("项目分类");titleSize.add(20);
        propertyNames.add("buildingCount");titleName.add("楼宇总数");titleSize.add(20);
        propertyNames.add("areaSize");titleName.add("建筑面积"); titleSize.add(20);
        propertyNames.add("amountReceivable");titleName.add("应收含税金额（元）");titleSize.add(20);
        propertyNames.add("amountReceived");titleName.add("已收含税金额（元）");titleSize.add(20);
        propertyNames.add("amountOwed");titleName.add("待收含税金额（元）");titleSize.add(20);
        propertyNames.add("dueDayCount");titleName.add("总欠费天数（天）");titleSize.add(20);
        propertyNames.add("collectionRate");titleName.add("收缴率（%）");titleSize.add(20);
        propertyNames.add("noticeTimes");titleName.add("催缴次数");titleSize.add(20);
        //组装Excel数据
        List<Map<String, String>> dataList = new ArrayList<>();
        for(int i = 0; i < dtos.size(); i++) {
        	ListBillStatisticByCommunityDTO dto = dtos.get(i);
            Map<String, String> detail = new HashMap<String, String>();
            detail.put("orderNum", i + 1 + "");
            detail.put("projectName", dto.getProjectName());
            detail.put("projectClassify", dto.getProjectClassify());
            detail.put("buildingCount", dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
            detail.put("areaSize", dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
            detail.put("amountReceivable", dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
            detail.put("amountReceived", dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
            detail.put("amountOwed", dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
            detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
            detail.put("collectionRate", (dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
            detail.put("noticeTimes", dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
            dataList.add(detail);
        }
		taskService.updateTaskProcess(taskId, 50);
		
		//最后的一行总计
		ListBillStatisticByCommunityDTO dto = assetStatisticProvider.listBillStatisticByCommunityTotal( 
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
		Map<String, String> detail = new HashMap<String, String>();
        detail.put("orderNum", "合计（总数）");
        detail.put("projectName", String.valueOf(dtos.size()));
        detail.put("projectClassify", "--");
        detail.put("buildingCount", dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
        detail.put("areaSize", dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
        detail.put("amountReceivable", dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
        detail.put("amountReceived", dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
        detail.put("amountOwed", dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
        detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
        detail.put("collectionRate", (dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
        detail.put("noticeTimes", dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
        dataList.add(detail);
        taskService.updateTaskProcess(taskId, 70);
		
		if (dtos != null && dtos.size() > 0) {
			String fileName = String.format(cmd.getExportFileNamePrefix()+"报表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, cmd.getExportFileNamePrefix()+"报表");
			excelUtils.setNeedSequenceColumn(false);//不需要序号列
			taskService.updateTaskProcess(taskId, 80);
			return excelUtils.getOutputStream(propertyNames, titleName, titleSize, dataList);
		}else {
			throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
		}
	}

    //issue-38508 缴费管理V7.0（新增缴费相关统计报表）：缴费信息汇总表-楼宇 (导出对接下载中心)
	public void exportBillStatisticByBuilding(ListBillStatisticByBuildingCmd cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("ListBillStatisticByBuildingCmd", cmd);
		String exportFileNamePrefix = cmd.getExportFileNamePrefix();
		String fileName = String.format(exportFileNamePrefix+"报表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), AssetStatisticBuildingExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	//issue-38508 缴费管理V7.0（新增缴费相关统计报表）：缴费信息汇总表-楼宇 (导出对接下载中心)
	public OutputStream exportOutputStreamBillStatisticByBuilding(ListBillStatisticByBuildingCmd cmd, Long taskId) {
    	//每一条数据
    	Integer pageOffSet = 0;
        Integer pageSize = 100000;
        List<ListBillStatisticByBuildingDTO> dtos = assetStatisticProvider.listBillStatisticByBuilding(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingNameList());
		taskService.updateTaskProcess(taskId, 20);
		//初始化 字段信息
        List<String> propertyNames = new ArrayList<String>();
        List<String> titleName = new ArrayList<String>();
        List<Integer> titleSize = new ArrayList<Integer>();
        propertyNames.add("orderNum");titleName.add("序号");titleSize.add(20);
        propertyNames.add("buildingName");titleName.add("楼宇名称");titleSize.add(20);
        propertyNames.add("addressCount");titleName.add("房源总数");titleSize.add(20);
        propertyNames.add("areaSize");titleName.add("建筑面积"); titleSize.add(20);
        propertyNames.add("amountReceivable");titleName.add("应收含税金额（元）");titleSize.add(20);
        propertyNames.add("amountReceived");titleName.add("已收含税金额（元）");titleSize.add(20);
        propertyNames.add("amountOwed");titleName.add("待收含税金额（元）");titleSize.add(20);
        propertyNames.add("dueDayCount");titleName.add("总欠费天数（天）");titleSize.add(20);
        propertyNames.add("collectionRate");titleName.add("收缴率（%）");titleSize.add(20);
        propertyNames.add("noticeTimes");titleName.add("催缴次数");titleSize.add(20);
        //组装Excel数据
        List<Map<String, String>> dataList = new ArrayList<>();
        for(int i = 0; i < dtos.size(); i++) {
        	ListBillStatisticByBuildingDTO dto = dtos.get(i);
            Map<String, String> detail = new HashMap<String, String>();
            detail.put("orderNum", i + 1 + "");
            detail.put("buildingName", dto.getBuildingName());
            detail.put("addressCount", dto.getAddressCount() != null ? dto.getAddressCount().toString() : "0");
            detail.put("areaSize", dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
            detail.put("amountReceivable", dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
            detail.put("amountReceived", dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
            detail.put("amountOwed", dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
            detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
            detail.put("collectionRate", (dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
            detail.put("noticeTimes", dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
            dataList.add(detail);
        }
		taskService.updateTaskProcess(taskId, 50);
		
		//最后的一行总计
		ListBillStatisticByBuildingDTO dto = assetStatisticProvider.listBillStatisticByBuildingTotal(
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingNameList());
		Map<String, String> detail = new HashMap<String, String>();
        detail.put("orderNum", "合计（总数）");
        detail.put("buildingName", String.valueOf(dtos.size()));
        detail.put("addressCount", dto.getAddressCount() != null ? dto.getAddressCount().toString() : "0");
        detail.put("areaSize", dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
        detail.put("amountReceivable", dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
        detail.put("amountReceived", dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
        detail.put("amountOwed", dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
        detail.put("dueDayCount", dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
        detail.put("collectionRate", (dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
        detail.put("noticeTimes", dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
        dataList.add(detail);
        taskService.updateTaskProcess(taskId, 70);
		
		if (dtos != null && dtos.size() > 0) {
			String fileName = String.format(cmd.getExportFileNamePrefix()+"报表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, cmd.getExportFileNamePrefix()+"报表");
			excelUtils.setNeedSequenceColumn(false);//不需要序号列
			taskService.updateTaskProcess(taskId, 80);
			return excelUtils.getOutputStream(propertyNames, titleName, titleSize, dataList);
		}else {
			throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
		}
	}
	
}