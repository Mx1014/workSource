
package com.everhomes.asset.statistic;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStatisticServiceImpl implements AssetStatisticService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetStatisticServiceImpl.class);
	
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
    @SuppressWarnings("deprecation")
	public OutputStream exportOutputStreamBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd, Long taskId){
    	OutputStream outputStream = new ByteArrayOutputStream();
    	//每一条数据
    	Integer pageOffSet = 0;
        Integer pageSize = 100000;
		List<ListBillStatisticByCommunityDTO> dtos = assetStatisticProvider.listBillStatisticByCommunity(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
		taskService.updateTaskProcess(taskId, 20);
		Workbook wb = null;
		InputStream in;
		in = this.getClass().getResourceAsStream("/excels/asset/statisticByCommunity.xlsx");
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("exportOutputStreamBillStatisticByCommunity copy inputStream error.");
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
					ListBillStatisticByCommunityDTO dto = dtos.get(i);
					int orderNum = i + 1;//序号
					boolean isLastRow = false;
					fillRowCellBillStatisticByCommunity(tempRow, style, isLastRow, orderNum, dto);
				}
				taskService.updateTaskProcess(taskId, 70);
				//最后的一行总计
				CellStyle totalStyle = getTotalStyle(wb, style);
				Row tempRow = sheet.createRow(dtos.size() + 2);
				int orderNum = dtos.size() + 1;//序号
				boolean isLastRow = true;
				ListBillStatisticByCommunityDTO totalDTO = assetStatisticProvider.listBillStatisticByCommunityTotal( 
		        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
				fillRowCellBillStatisticByCommunity(tempRow, totalStyle, isLastRow, orderNum, totalDTO);
				taskService.updateTaskProcess(taskId, 80);
				//设置自适应列宽
//				for(int i = 0; i < 50;i++) {
//					sheet.autoSizeColumn(i);
//				}
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
    private void fillRowCellBillStatisticByCommunity(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, ListBillStatisticByCommunityDTO dto) {
    	//序号
		Cell cell1 = tempRow.createCell(0);
		cell1.setCellStyle(style);
		if(isLastRow) {
			cell1.setCellValue("合计");
		}else {
			cell1.setCellValue(orderNum);
		}
		//项目名称
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getProjectName());
		//项目分类
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		if(isLastRow) {
			cell3.setCellValue("--");
		}else {
			cell3.setCellValue(dto.getProjectClassify() != null ? dto.getProjectClassify().toString() : "");
		}
		//楼宇总数
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getBuildingCount() != null ? dto.getBuildingCount().toString() : "0");
		//建筑面积
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		//应收含税金额(元)
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		//已收金额(元)
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		//待收金额(元)
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		//总欠费天数(天)
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		//收缴率(%)
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
		//催缴次数
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
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
	@SuppressWarnings("deprecation")
	public OutputStream exportOutputStreamBillStatisticByBuilding(ListBillStatisticByBuildingCmd cmd, Long taskId) {
		OutputStream outputStream = new ByteArrayOutputStream();
    	//每一条数据
    	Integer pageOffSet = 0;
        Integer pageSize = 100000;
        List<ListBillStatisticByBuildingDTO> dtos = assetStatisticProvider.listBillStatisticByBuilding(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingNameList());
		taskService.updateTaskProcess(taskId, 20);
		Workbook wb = null;
		InputStream in;
		in = this.getClass().getResourceAsStream("/excels/asset/statisticByBuilding.xlsx");
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("exportOutputStreamBillStatisticByBuilding copy inputStream error.");
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
					ListBillStatisticByBuildingDTO dto = dtos.get(i);
					int orderNum = i + 1;//序号
					boolean isLastRow = false;
					fillRowCellBillStatisticByBuilding(tempRow, style, isLastRow, orderNum, dto);
				}
				taskService.updateTaskProcess(taskId, 70);
				//最后的一行总计
				CellStyle totalStyle = getTotalStyle(wb, style);
				Row tempRow = sheet.createRow(dtos.size() + 2);
				int orderNum = dtos.size() + 1;//序号
				boolean isLastRow = true;
				ListBillStatisticByBuildingDTO totalDTO = assetStatisticProvider.listBillStatisticByBuildingTotal(
		        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd(),
		        		cmd.getBuildingNameList());
				fillRowCellBillStatisticByBuilding(tempRow, totalStyle, isLastRow, orderNum, totalDTO);
				taskService.updateTaskProcess(taskId, 80);
				//设置自适应列宽
//				for(int i = 0; i < 50;i++) {
//					sheet.autoSizeColumn(i);
//				}
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
     * @param tempRow ： Excel的行
     * @param style 
     * @param isLastRow ： 是否是最后一行
     * @param orderNum ： 序号
     * @param dto
     */
    private void fillRowCellBillStatisticByBuilding(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, ListBillStatisticByBuildingDTO dto) {
    	//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		if(isLastRow) {
			cell0.setCellValue("合计");
		}else {
			cell0.setCellValue(orderNum);
		}
		//楼宇名称
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		cell2.setCellValue(dto.getBuildingName());
		//房源总数
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		cell3.setCellValue(dto.getAddressCount() != null ? dto.getAddressCount().toString() : "");
		//建筑面积
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		cell4.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		//应收含税金额(元)
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		//已收金额(元)
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		cell6.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		//待收金额(元)
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		cell7.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		//总欠费天数(天)
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		//收缴率(%)
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
		//催缴次数
		Cell cell10= tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
	}

	//缴费信息汇总表-房源 (导出对接下载中心)
	public void exportBillStatisticByAddress(ListBillStatisticByAddressCmd cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("ListBillStatisticByAddressCmd", cmd);
		String exportFileNamePrefix = cmd.getExportFileNamePrefix();
		String fileName = String.format(exportFileNamePrefix+"报表", com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH))+ ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), AssetStatisticAddressExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	//缴费信息汇总表-房源 (导出对接下载中心)
	@SuppressWarnings("deprecation")
	public OutputStream exportOutputStreamBillStatisticByAddress(ListBillStatisticByAddressCmd cmd, Long taskId) {
		OutputStream outputStream = new ByteArrayOutputStream();
    	//每一条数据
    	Integer pageOffSet = 0;
        Integer pageSize = 100000;
        List<ListBillStatisticByAddressDTO> dtos = assetStatisticProvider.listBillStatisticByAddress(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
        		cmd.getBuildingName(), cmd.getApartmentNameList(), cmd.getChargingItemIdList(), cmd.getTargetName());
        taskService.updateTaskProcess(taskId, 20);
		Workbook wb = null;
		InputStream in;
		in = this.getClass().getResourceAsStream("/excels/asset/statisticByAddress.xlsx");
		try {
			wb = new XSSFWorkbook(copyInputStream(in));
		} catch (IOException e) {
			LOGGER.error("exportOutputStreamBillStatisticByAddress copy inputStream error.");
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
					ListBillStatisticByAddressDTO dto = dtos.get(i);
					int orderNum = i + 1;//序号
					boolean isLastRow = false;
					fillRowCellBillStatisticByAddress(tempRow, style, isLastRow, orderNum, dto);
				}
				taskService.updateTaskProcess(taskId, 70);
				//最后的一行总计
				CellStyle totalStyle = getTotalStyle(wb, style);
				Row tempRow = sheet.createRow(dtos.size() + 2);
				int orderNum = dtos.size() + 1;//序号
				boolean isLastRow = true;
				ListBillStatisticByAddressDTO totalDTO = assetStatisticProvider.listBillStatisticByAddressTotal(
						cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getDateStrBegin(), cmd.getDateStrEnd(),
		        		cmd.getBuildingName(), cmd.getApartmentNameList(), cmd.getChargingItemIdList(), cmd.getTargetName());
				fillRowCellBillStatisticByAddress(tempRow, totalStyle, isLastRow, orderNum, totalDTO);
				taskService.updateTaskProcess(taskId, 80);
				//设置自适应列宽
//				for(int i = 0; i < 50;i++) {
//					sheet.autoSizeColumn(i);
//				}
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
     * @param tempRow ： Excel的行
     * @param style 
     * @param isLastRow ： 是否是最后一行
     * @param orderNum ： 序号
     * @param dto
     */
    private void fillRowCellBillStatisticByAddress(Row tempRow, CellStyle style, boolean isLastRow, int orderNum, ListBillStatisticByAddressDTO dto) {
    	//序号
		Cell cell0 = tempRow.createCell(0);
		cell0.setCellStyle(style);
		if(isLastRow) {
			cell0.setCellValue("合计");
		}else {
			cell0.setCellValue(orderNum);
		}
		//楼宇房源
		Cell cell2 = tempRow.createCell(1);
		cell2.setCellStyle(style);
		if(isLastRow) {
			cell2.setCellValue("--");
		}else {
			cell2.setCellValue(dto.getAddressName());
		}
		//客户名称
		Cell cell3 = tempRow.createCell(2);
		cell3.setCellStyle(style);
		if(isLastRow) {
			cell3.setCellValue("--");
		}else {
			cell3.setCellValue(dto.getTargetName());
		}
		//催缴手机号码
		Cell cell4 = tempRow.createCell(3);
		cell4.setCellStyle(style);
		if(isLastRow) {
			cell4.setCellValue("--");
		}else {
			cell4.setCellValue(dto.getNoticeTel() != null ? dto.getAreaSize().toString() : "0");
		}
		//收费面积
		Cell cell5 = tempRow.createCell(4);
		cell5.setCellStyle(style);
		cell5.setCellValue(dto.getAreaSize() != null ? dto.getAreaSize().toString() : "0");
		//账单时间
		Cell cell6 = tempRow.createCell(5);
		cell6.setCellStyle(style);
		if(isLastRow) {
			cell6.setCellValue("--");	
		}else {
			cell6.setCellValue(dto.getDateStrBegin() + "~" + dto.getDateStrEnd());	
		}
		//收费项目
		Cell cell7 = tempRow.createCell(6);
		cell7.setCellStyle(style);
		if(isLastRow) {
			cell7.setCellValue("--");	
		}else {
			cell7.setCellValue(dto.getProjectChargingItemName());	
		}
		//应收含税金额(元)
		Cell cell8 = tempRow.createCell(7);
		cell8.setCellStyle(style);
		cell8.setCellValue(dto.getAmountReceivable() != null ? dto.getAmountReceivable().toString() : "0");
		//已收金额(元)
		Cell cell9 = tempRow.createCell(8);
		cell9.setCellStyle(style);
		cell9.setCellValue(dto.getAmountReceived() != null ? dto.getAmountReceived().toString() : "0");
		//待收金额(元)
		Cell cell10 = tempRow.createCell(9);
		cell10.setCellStyle(style);
		cell10.setCellValue(dto.getAmountOwed() != null ? dto.getAmountOwed().toString() : "0");
		//总欠费天数(天)
		Cell cell11 = tempRow.createCell(10);
		cell11.setCellStyle(style);
		cell11.setCellValue(dto.getDueDayCount() != null ? dto.getDueDayCount().toString() : "0");
		//收缴率(%)
		Cell cell12 = tempRow.createCell(11);
		cell12.setCellStyle(style);
		cell12.setCellValue((dto.getCollectionRate() != null ? dto.getCollectionRate().toString() : "0") + "%");
		//催缴次数
//		Cell cell13= tempRow.createCell(12);
//		cell13.setCellStyle(style);
//		cell13.setCellValue(dto.getNoticeTimes() != null ? dto.getNoticeTimes().toString() : "0");
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
	
}