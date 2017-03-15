package com.everhomes.statistics.transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;


import com.everhomes.namespace.Namespace;
import org.apache.http.protocol.HTTP;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;


























import com.everhomes.business.Business;
import com.everhomes.business.BusinessProvider;
import com.everhomes.business.BusinessService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.pmsy.PmsyOrder;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.payment.PaymentCardProvider;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.payment.PaymentCardTransaction;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.address.admin.ListBuildingByCommunityIdsCommand;
import com.everhomes.rest.business.BusinessDTO;
import com.everhomes.rest.business.BusinessTargetType;
import com.everhomes.rest.business.ListBusinessByKeywordCommand;
import com.everhomes.rest.business.ListBusinessByKeywordCommandResponse;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardTransactionStatus;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyOwnerType;
import com.everhomes.rest.statistics.transaction.ListStatServiceSettlementAmountsCommand;
import com.everhomes.rest.statistics.transaction.ListStatShopTransactionsResponse;
import com.everhomes.rest.statistics.transaction.ListStatTransactionCommand;
import com.everhomes.rest.statistics.transaction.PaidChannel;
import com.everhomes.rest.statistics.transaction.SettlementErrorCode;
import com.everhomes.rest.statistics.transaction.SettlementOrderType;
import com.everhomes.rest.statistics.transaction.SettlementResourceType;
import com.everhomes.rest.statistics.transaction.SettlementServiceType;
import com.everhomes.rest.statistics.transaction.SettlementStatOrderStatus;
import com.everhomes.rest.statistics.transaction.SettlementStatTransactionPaidStatus;
import com.everhomes.rest.statistics.transaction.StatPaidOrderStatus;
import com.everhomes.rest.statistics.transaction.StatRefundOrderStatus;
import com.everhomes.rest.statistics.transaction.StatServiceSettlementResultDTO;
import com.everhomes.rest.statistics.transaction.StatTaskLock;
import com.everhomes.rest.statistics.transaction.StatTaskLogDTO;
import com.everhomes.rest.statistics.transaction.StatTaskStatus;
import com.everhomes.rest.statistics.transaction.StatShopTransactionDTO;
import com.everhomes.rest.statistics.transaction.StatWareDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

@Component
public class StatTransactionServiceImpl implements StatTransactionService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StatTransactionServiceImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private StatTransactionProvider statTransactionProvider;
	
	@Autowired
	private PmsyProvider pmsyProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
	private PaymentCardProvider paymentCardProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private BusinessProvider businessProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Autowired
	private BusinessService businessService;
	
	@PostConstruct
	public void setup(){
		String triggerName = StatTransactionScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
		String jobName = triggerName;
		String cronExpression = configurationProvider.getValue(StatTransactionConstant.STAT_CRON_EXPRESSION, StatTransactionScheduleJob.CRON_EXPRESSION);
		//启动定时任务
		scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, StatTransactionScheduleJob.class, null);
	}
	
	@Override
	public List<StatTaskLogDTO> excuteSettlementTask(Long startDate, Long endDate) {
		
		List<StatTaskLog> statTaskLogs = new ArrayList<StatTaskLog>();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		if(null == startDate || null == endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		//如果结束时间大于昨天，结束时间就取昨天的值
		if(DateUtil.dateToStr(new Date(endDate), DateUtil.YMR_SLASH).compareTo(DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH)) > 0){
			endDate = calendar.getTimeInMillis();
		}
		
		// 结束时间大于开始时间
		if(startDate > endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		//获取范围内的所以日期
		List<Date> dDates = DateUtil.getStartToEndDates(new Date(startDate), new Date(endDate));
		
		
		
		for (Date date : dDates) {
			String sDate = DateUtil.dateToStr(date, DateUtil.YMR_SLASH);
			//按日期结算数据
			this.coordinationProvider.getNamedLock(CoordinationLocks.STAT_SETTLEMENT.getCode() + "_" + sDate).enter(()-> {
				StatTaskLog statTaskLog = this.settlementByDate(sDate);
				statTaskLogs.add(statTaskLog);
				return null;
            });
		}
		
		return statTaskLogs.stream().map(r ->{
			return ConvertHelper.convert(r, StatTaskLogDTO.class);
		}).collect(Collectors.toList());
	}
	
	@Override
	public List<StatServiceSettlementResultDTO> listStatServiceSettlementAmountDetails(
			ListStatServiceSettlementAmountsCommand cmd) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Long startDate = cmd.getStartDate();
		Long endDate = cmd.getEndDate();
		if(null == startDate || null == endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		String sStartDate = DateUtil.dateToStr(new Date(startDate), DateUtil.YMR_SLASH);
		String sEndDate = DateUtil.dateToStr(new Date(endDate), DateUtil.YMR_SLASH);
		Condition cond = null;
		if(!StringUtils.isEmpty(cmd.getNamespaceId())){
			cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.NAMESPACE_ID.eq(cmd.getNamespaceId());
		}
		
		if(!StringUtils.isEmpty(cmd.getCommunityId())){
			if(null == cond)
				cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(cmd.getCommunityId());
			else
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
		}
		
		List<StatServiceSettlementResult> results = new ArrayList<StatServiceSettlementResult>();
		
		List<String> serviceTypes = new ArrayList<String>();
		serviceTypes.add(SettlementServiceType.ZUOLIN_SHOP.getCode());
		serviceTypes.add(SettlementServiceType.OTHER_SHOP.getCode());
		serviceTypes.add(SettlementServiceType.COMMUNITY_SERVICE.getCode());
		
		for (String serviceType : serviceTypes) {
			Condition serviceCond = null;
			if(null == cond)
				serviceCond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE.eq(serviceType);
			else
				serviceCond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE.eq(serviceType));
			
			if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.ZUOLIN_SHOP){
				Condition neCond = serviceCond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID.ne("-1"));
				List<StatServiceSettlementResult> shopSettlementResults = statTransactionProvider.listStatServiceSettlementResult(neCond, sStartDate, sEndDate);
				results.addAll(shopSettlementResults);
				
				//乱数据全部统计在临时店铺下面
				//只有0域或者查询全部的时候 才把没有来源的数据查出 add by sfyan 20170309
				if(StringUtils.isEmpty(cmd.getNamespaceId()) || cmd.getNamespaceId() == Namespace.DEFAULT_NAMESPACE){
					Condition eqCond = serviceCond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID.eq("-1"));
					StatServiceSettlementResult result = statTransactionProvider.getStatServiceSettlementResultTotal(eqCond, sStartDate, sEndDate);
					result.setServiceType(serviceType);
					result.setResourceType(SettlementResourceType.SHOP.getCode());
					result.setResourceId("-1");
					results.add(result);
				}

			}else if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.OTHER_SHOP){
				List<StatServiceSettlementResult> shopSettlementResults = statTransactionProvider.listStatServiceSettlementResult(serviceCond, sStartDate, sEndDate);
				results.addAll(shopSettlementResults);
			}else if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.COMMUNITY_SERVICE){
				List<StatService> statServices = null;
				if(!StringUtils.isEmpty(cmd.getNamespaceId())){
					statServices = statTransactionProvider.listStatServices(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
				}else{
					statServices = statTransactionProvider.listStatServiceGroupByServiceTypes();
				}
				for (StatService statService : statServices) {
					Condition resCond = serviceCond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(statService.getServiceType()));
					StatServiceSettlementResult result = statTransactionProvider.getStatServiceSettlementResultTotal(resCond, sStartDate, sEndDate);
					result.setResourceName(statService.getServiceName());
					result.setResourceType(statService.getServiceType());
					result.setServiceType(serviceType);
					results.add(result);
				}
			}
		}
		
		return results.stream().map(r ->{
			return this.convertStatServiceSettlementResultDTO(r);
		}).collect(Collectors.toList());
	}
	

	
	private StatServiceSettlementResultDTO convertStatServiceSettlementResultDTO(StatServiceSettlementResult statServiceSettlementResult){
		StatServiceSettlementResultDTO dto = ConvertHelper.convert(statServiceSettlementResult, StatServiceSettlementResultDTO.class);
		dto.setServiceName(this.getServiceName(dto.getServiceType()));
		if(SettlementResourceType.fromCode(dto.getResourceType()) == SettlementResourceType.SHOP){
			if(!StringUtils.isEmpty(dto.getResourceId()) && dto.getResourceId().equals("-1")){
				dto.setResourceName("临时店铺");
			}else{
				Business business = businessProvider.findBusinessByTargetId(dto.getResourceId());
				if(null != business){
					dto.setResourceName(business.getName());
				}
			}
		}
		
//		dto.setResourceName(this.getResourceName(dto.getResourceType(), dto.getResourceName()));
		
		if(null == dto.getCommunityId()){
			dto.setCommunityName("-");
		}else{
			Community community = communityProvider.findCommunityById(dto.getCommunityId());
			if(null != community){
				dto.setCommunityName(community.getName());
			}else{
				dto.setCommunityName("-");
			}
		}
		
		dto.setAlipayPaidAmount(statServiceSettlementResult.getAlipayPaidAmount().doubleValue());
		dto.setAlipayRefundAmount(statServiceSettlementResult.getAlipayRefundAmount().doubleValue());
		dto.setWechatPaidAmount(statServiceSettlementResult.getWechatPaidAmount().doubleValue());
		dto.setWechatRefundAmount(statServiceSettlementResult.getWechatRefundAmount().doubleValue());
		dto.setPaymentCardPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount().doubleValue());
		dto.setPaymentCardRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount().doubleValue());
		dto.setTotalPaidAmount(statServiceSettlementResult.getTotalPaidAmount().doubleValue());
		dto.setTotalRefundAmount(statServiceSettlementResult.getTotalRefundAmount().doubleValue());
		return dto;
	}
	
	@Override
	public List<StatServiceSettlementResultDTO> listStatServiceSettlementAmounts(
			ListStatServiceSettlementAmountsCommand cmd) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Long startDate = cmd.getStartDate();
		Long endDate = cmd.getEndDate();
		if(null == startDate || null == endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		String sStartDate = DateUtil.dateToStr(new Date(startDate), DateUtil.YMR_SLASH);
		String sEndDate = DateUtil.dateToStr(new Date(endDate), DateUtil.YMR_SLASH);
		
		Condition cond = null;
		if(!StringUtils.isEmpty(cmd.getNamespaceId())){
			cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.NAMESPACE_ID.eq(cmd.getNamespaceId());
		}
		
		if(!StringUtils.isEmpty(cmd.getCommunityId())){
			if(null == cond)
				cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(cmd.getCommunityId());
			else
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
		}
		
		List<StatService> statServices = null;
		if(!StringUtils.isEmpty(cmd.getNamespaceId())){
			statServices = statTransactionProvider.listStatServices(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		}else{
			statServices = statTransactionProvider.listStatServiceGroupByServiceTypes();
		}
		
		Condition servCond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(SettlementResourceType.SHOP.getCode());
		for (StatService statService : statServices) {
			servCond = servCond.or(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(statService.getServiceType()));
		}
		
		cond = cond.and(servCond);
		
		List<StatServiceSettlementResult> results = statTransactionProvider.listCountStatServiceSettlementResultByService(cond, sStartDate, sEndDate);
		
		StatServiceSettlementResult total = statTransactionProvider.getStatServiceSettlementResultTotal(cond, sStartDate, sEndDate);
		
		results = this.processStatServiceSettlementAmounts(results);
		
		List<StatServiceSettlementResultDTO> dtos = results.stream().map(r ->{
			return this.convertStatServiceSettlementResultDTO(r);
		}).collect(Collectors.toList());
		
		
		
		StatServiceSettlementResultDTO totalDTO = new StatServiceSettlementResultDTO();
		if(null != total){
			totalDTO = this.convertStatServiceSettlementResultDTO(total);
		}
		
		totalDTO.setServiceName("总计");
		
		dtos.add(totalDTO);
		
		return dtos;
	}
	
	private List<StatServiceSettlementResult> processStatServiceSettlementAmounts(List<StatServiceSettlementResult> results){
		List<StatServiceSettlementResult> statServiceSettlementResults= new ArrayList<StatServiceSettlementResult>();
		Map<String, StatServiceSettlementResult> resultMap = new HashMap<String, StatServiceSettlementResult>();
		for (StatServiceSettlementResult statServiceSettlementResult : results) {
			resultMap.put(statServiceSettlementResult.getServiceType(), statServiceSettlementResult);
		}
		
		List<String> serviceTypes = new ArrayList<String>();
		serviceTypes.add(SettlementServiceType.ZUOLIN_SHOP.getCode());
		serviceTypes.add(SettlementServiceType.OTHER_SHOP.getCode());
		serviceTypes.add(SettlementServiceType.COMMUNITY_SERVICE.getCode());
		
		for (String serviceType : serviceTypes) {
			StatServiceSettlementResult result = resultMap.get(serviceType);
			if(null == result){
				result = new StatServiceSettlementResult();
				result.setServiceType(serviceType);
				result.setAlipayPaidAmount(new BigDecimal(0.00));
				result.setAlipayRefundAmount(new BigDecimal(0.00));
				result.setWechatPaidAmount(new BigDecimal(0.00));
				result.setWechatRefundAmount(new BigDecimal(0.00));
				result.setPaymentCardPaidAmount(new BigDecimal(0.00));
				result.setPaymentCardRefundAmount(new BigDecimal(0.00));
				result.setTotalPaidAmount(new BigDecimal(0.00));
				result.setTotalRefundAmount(new BigDecimal(0.00));
			}
			statServiceSettlementResults.add(result);
		}
		
		return statServiceSettlementResults;
	}
	
	@Override
	public void exportStatServiceSettlementAmounts(
			ListStatServiceSettlementAmountsCommand cmd, HttpServletResponse response) {
		
		List<StatServiceSettlementResultDTO> statServiceSettlementResultsDTOs = this.listStatServiceSettlementAmounts(cmd);
		
		List<StatServiceSettlementResultDTO> statServiceSettlementResultDetailDTOs = this.listStatServiceSettlementAmountDetails(cmd);
		
		this.exportStatServiceSettlementAmountFile(statServiceSettlementResultDetailDTOs, statServiceSettlementResultsDTOs, response);
	}
	
	private void exportStatServiceSettlementAmountFile(List<StatServiceSettlementResultDTO> statServiceSettlementResultDetailDTOs,List<StatServiceSettlementResultDTO> statServiceSettlementResultsDTOs, HttpServletResponse response){
		XSSFWorkbook wb = new XSSFWorkbook();
		ByteArrayOutputStream out = null;
		try {
			String sheetName = "结算报表";
			XSSFSheet sheet = wb.createSheet(sheetName);
			
			// 创建单元格样式
			XSSFCellStyle style = wb.createCellStyle();// 样式对象
//			style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直  
//	        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平 
//	        
//			// 设置边框
//			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
//			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			
			//设置标题字体格式  
	        Font font = wb.createFont();
	        font.setFontHeightInPoints((short)20);  
	        font.setFontName("Courier New");
	        
	        style.setFont(font);
	        
	        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
	        titleStyle.setFont(font);
	        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
	        
	        int rowNum = 0;
	        
	        XSSFRow row1 = sheet.createRow(rowNum ++);
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
	        XSSFCell cell1 = row1.createCell(0);
	        cell1.setCellStyle(titleStyle);  
	        cell1.setCellValue("项目类型统计总表"); 
	        
	        XSSFRow row2 = sheet.createRow(rowNum ++);
	        row2.setRowStyle(style);
	        row2.createCell(0).setCellValue("项目类型");
	        row2.createCell(1).setCellValue("支付宝实收");
	        row2.createCell(2).setCellValue("支付宝退款");
	        row2.createCell(3).setCellValue("微信实收");
	        row2.createCell(4).setCellValue("微信退款");
//	        row2.createCell(5).setCellValue("一卡通实收");
//	        row2.createCell(6).setCellValue("一卡通退款");
	        row2.createCell(5).setCellValue("实收总计");
	        row2.createCell(6).setCellValue("退款总计");
	        
	        for (StatServiceSettlementResultDTO statServiceSettlementResult : statServiceSettlementResultsDTOs) {
	        	
	        	XSSFRow row = sheet.createRow(rowNum ++);
	        	row.setRowStyle(style);
	        	row.createCell(0).setCellValue(statServiceSettlementResult.getServiceName());
	        	row.createCell(1).setCellValue(statServiceSettlementResult.getAlipayPaidAmount());
	            row.createCell(2).setCellValue(statServiceSettlementResult.getAlipayRefundAmount());
	            row.createCell(3).setCellValue(statServiceSettlementResult.getWechatPaidAmount());
	            row.createCell(4).setCellValue(statServiceSettlementResult.getWechatRefundAmount());
//	            row.createCell(5).setCellValue(statServiceSettlementResult.getPaymentCardPaidAmount());
//	            row.createCell(6).setCellValue(statServiceSettlementResult.getPaymentCardRefundAmount());
	            row.createCell(5).setCellValue(statServiceSettlementResult.getTotalPaidAmount());
	            row.createCell(6).setCellValue(statServiceSettlementResult.getTotalRefundAmount());
			}
	        
	        
	        XSSFRow row3 = sheet.createRow(rowNum);
	        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 10));
	        XSSFCell cell2 = row3.createCell(0);
	        cell2.setCellStyle(titleStyle);
	        cell2.setCellValue("具体项目统计表"); 
	        rowNum ++;
	        XSSFRow row4 = sheet.createRow(rowNum ++);
	        row4.setRowStyle(style);
	        row4.createCell(0).setCellValue("项目类型");
	        row4.createCell(1).setCellValue("社区名称");
	        row4.createCell(2).setCellValue("项目名称");
	        row4.createCell(3).setCellValue("支付宝实收");
	        row4.createCell(4).setCellValue("支付宝退款");
	        row4.createCell(5).setCellValue("微信实收");
	        row4.createCell(6).setCellValue("微信退款");
//	        row4.createCell(7).setCellValue("一卡通实收");
//	        row4.createCell(8).setCellValue("一卡通退款");
	        row4.createCell(7).setCellValue("实收总计");
	        row4.createCell(8).setCellValue("退款总计");
	        
	        for (StatServiceSettlementResultDTO statServiceSettlementResult : statServiceSettlementResultDetailDTOs) {
	        	
	        	org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(rowNum ++);
	        	row.setRowStyle(style);
	        	row.createCell(0).setCellValue(statServiceSettlementResult.getServiceName());
	        	row.createCell(1).setCellValue(statServiceSettlementResult.getCommunityName());
	        	row.createCell(2).setCellValue(statServiceSettlementResult.getResourceName());
	        	row.createCell(3).setCellValue(statServiceSettlementResult.getAlipayPaidAmount());
	            row.createCell(4).setCellValue(statServiceSettlementResult.getAlipayRefundAmount());
	            row.createCell(5).setCellValue(statServiceSettlementResult.getWechatPaidAmount());
	            row.createCell(6).setCellValue(statServiceSettlementResult.getWechatRefundAmount());
//	            row.createCell(7).setCellValue(statServiceSettlementResult.getPaymentCardPaidAmount());
//	            row.createCell(8).setCellValue(statServiceSettlementResult.getPaymentCardRefundAmount());
	            row.createCell(7).setCellValue(statServiceSettlementResult.getTotalPaidAmount());
	            row.createCell(8).setCellValue(statServiceSettlementResult.getTotalRefundAmount());
			}
	        
	        out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtil.download(out, response);
		} catch (Exception e) {
			LOGGER.error("export excel error", e);
		} finally{
			try {
				wb.close();
				out.close();
			} catch (IOException e) {
				LOGGER.error("export excel error", e);
			}
		}
		
	}
	
	@Override
	public ListStatShopTransactionsResponse listStatShopTransactions(
			ListStatTransactionCommand cmd) {
		
		ListStatShopTransactionsResponse response = new ListStatShopTransactionsResponse();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Long startDate = cmd.getStartDate();
		Long endDate = cmd.getEndDate();
		if(null == startDate || null == endDate){
			startDate = calendar.getTimeInMillis();
			endDate = calendar.getTimeInMillis();
		}
		
		String sStartDate = DateUtil.dateToStr(new Date(startDate), DateUtil.YMR_SLASH);
		String sEndDate = DateUtil.dateToStr(new Date(endDate), DateUtil.YMR_SLASH);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<StatShopTransactionDTO> dtos = null;
		if(SettlementOrderType.TRANSACTION == SettlementOrderType.fromCode(cmd.getOrderType())){
			List<StatTransaction> statTransactions = statTransactionProvider.listStatTransactions(locator, pageSize, sStartDate, sEndDate, cmd.getResourceId(), cmd.getResourceType(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getServiceType());
			dtos = convertTransactionDTOByPaid(statTransactions);
		}else{
			List<StatRefund> statRefunds = statTransactionProvider.listStatRefunds(locator, pageSize, sStartDate, sEndDate, cmd.getResourceId(), cmd.getResourceType(), cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getServiceType());
			dtos = convertTransactionDTOByRefund(statRefunds);
		}
		response.setNextPageAnchor(locator.getAnchor());
		response.setDtos(dtos);
		
		return response;
	}
	
	@Override
	public void exportStatShopTransactions(ListStatTransactionCommand cmd,
			HttpServletResponse response) {
		//导出100条
		cmd.setPageSize(100);
		ListStatShopTransactionsResponse res = this.listStatShopTransactions(cmd);
		List<StatShopTransactionDTO> dtos = res.getDtos();
		this.exportStatShopTransactionsFile(dtos, response);
	}
	
	private void exportStatShopTransactionsFile(List<StatShopTransactionDTO> dtos, HttpServletResponse response){
		XSSFWorkbook wb = new XSSFWorkbook();
		ByteArrayOutputStream out = null;
		try {
			String sheetName = "流水数据";
			XSSFSheet sheet = wb.createSheet(sheetName);
			
			// 创建单元格样式
			XSSFCellStyle style = wb.createCellStyle();// 样式对象
			
			//设置标题字体格式  
	        Font font = wb.createFont();
	        font.setFontHeightInPoints((short)20);  
	        font.setFontName("Courier New");
	        
	        style.setFont(font);
	        
	        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
	        titleStyle.setFont(font);
	        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); 
	        
	        int rowNum = 0;
	        XSSFRow row1 = sheet.createRow(rowNum ++);
	        row1.setRowStyle(style);
	        row1.createCell(0).setCellValue("订单编号");
	        row1.createCell(1).setCellValue("支付时间");
	        row1.createCell(2).setCellValue("订单状态");
	        row1.createCell(3).setCellValue("支付金额");
	        row1.createCell(4).setCellValue("支付通道");
	        row1.createCell(5).setCellValue("联系人");
	        row1.createCell(6).setCellValue("联系电话");
	        row1.createCell(7).setCellValue("购买商品");
	        
	        for (StatShopTransactionDTO statShopTransactionDTO : dtos) {
	        	
	        	XSSFRow row = sheet.createRow(rowNum ++);
	        	row.setRowStyle(style);
	        	row.createCell(0).setCellValue(statShopTransactionDTO.getOrderNo());
	        	row.createCell(1).setCellValue(DateUtil.dateToStr(new Date(statShopTransactionDTO.getPaidTime()), DateUtil.DATE_TIME_LINE));
	            row.createCell(2).setCellValue(statShopTransactionDTO.getStatusName());
	            row.createCell(3).setCellValue(statShopTransactionDTO.getPaidAmount());
	            row.createCell(4).setCellValue(statShopTransactionDTO.getPaidChannelName());
	            row.createCell(5).setCellValue(statShopTransactionDTO.getUserName());
	            row.createCell(6).setCellValue(statShopTransactionDTO.getUserPhone());
	            List<StatWareDTO> wareDTOs = statShopTransactionDTO.getWares();
	            String wareInfo = "";
	            for (StatWareDTO statWareDTO : wareDTOs) {
	            	wareInfo += statWareDTO.getWareName() + "X" + statWareDTO.getNumber() + " ";
				}
	            row.createCell(7).setCellValue(wareInfo);
			}
	        out = new ByteArrayOutputStream();
			wb.write(out);
			DownloadUtil.download(out, response);
		} catch (Exception e) {
			LOGGER.error("export excel error", e);
		} finally{
			try {
				wb.close();
				out.close();
			} catch (IOException e) {
				LOGGER.error("export excel error", e);
			}
		}
		
	}
	
	@Override
	public List<BusinessDTO> listZuoLinBusinesses() {
		ListBusinessByKeywordCommand cmd = new ListBusinessByKeywordCommand();
		cmd.setPageOffset(1);
		cmd.setPageSize(1000);
		ListBusinessByKeywordCommandResponse res = businessService.listBusinessByKeyword(cmd);
		List<BusinessDTO> businessDTOs = res.getList();
		List<BusinessDTO> dtos = new ArrayList<BusinessDTO>();
		
		if(null != businessDTOs){
			for (BusinessDTO businessDTO : businessDTOs) {
				if(BusinessTargetType.fromCode(businessDTO.getTargetType()) == BusinessTargetType.ZUOLIN){
					dtos.add(businessDTO);
				}
			}
		}
		//固定添加一个临时店铺 值为-1
		BusinessDTO businessDTO = new BusinessDTO();
		businessDTO.setTargetType(BusinessTargetType.ZUOLIN.getCode());
		businessDTO.setTargetId("-1");
		businessDTO.setName("临时店铺");
		businessDTO.setDisplayName("临时店铺");
		dtos.add(businessDTO);
		return dtos;
	}
	
	private List<StatShopTransactionDTO> convertTransactionDTOByPaid(List<StatTransaction> statTransactions){
		List<StatShopTransactionDTO> dtos = new ArrayList<StatShopTransactionDTO>();
		for (StatTransaction statTransaction : statTransactions) {
			StatShopTransactionDTO dto = ConvertHelper.convert(statTransaction, StatShopTransactionDTO.class);
			dto.setPaidTime(statTransaction.getPaidTime().getTime());
			dto.setPaidAmount(statTransaction.getPaidAmount().doubleValue());
			if(null != statTransaction.getPayerUid()){
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(statTransaction.getPayerUid(), IdentifierType.MOBILE.getCode());
				if(null != userIdentifier){
					dto.setUserPhone(userIdentifier.getIdentifierToken());
				}
				
				User user = userProvider.findUserById(statTransaction.getPayerUid());
				if(null != user){
					dto.setUserName(user.getNickName());
				}
			}
			
			String wareJson = statTransaction.getWareJson();
			List<StatWareDTO> statWareDTOs =  new ArrayList<StatWareDTO>();
			
			if(!StringUtils.isEmpty(wareJson)){
				Ware[] wares = (Ware[])StringHelper.fromJsonString(wareJson, Ware[].class);
				if(null != wares){
					for (Ware ware : wares) {
						StatWareDTO wareDTO = this.getWareInfo(ware.getWareId());
						if(null != wareDTO){
							wareDTO.setNumber(ware.getNumber());
							statWareDTOs.add(wareDTO);
						}
					}
				}
			}
			dto.setWares(statWareDTOs);
			
			StatOrder order = statTransactionProvider.findStatOrderByOrderNoAndResourceType(statTransaction.getOrderNo(), SettlementResourceType.SHOP.getCode());
			
			if(null != order){
				dto.setStatus(order.getStatus());
				dto.setStatusName(this.getPaidStatusName(order.getStatus()));
			}
			dto.setPaidChannelName(this.getPaidChannelName(dto.getPaidChannel()));
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	private String getPaidStatusName(Byte status){
		if(StatPaidOrderStatus.fromCode(status) == StatPaidOrderStatus.WAITING_PAID){
			return "待支付";
		}else if(StatPaidOrderStatus.fromCode(status) == StatPaidOrderStatus.WAITING_DELIVER){
			return "待发货";
		}else if(StatPaidOrderStatus.fromCode(status) == StatPaidOrderStatus.DELIVERED){
			return "已发货";
		}else if(StatPaidOrderStatus.fromCode(status) == StatPaidOrderStatus.FINISH){
			return "已完成";
		}else if(StatPaidOrderStatus.fromCode(status) == StatPaidOrderStatus.CLOSE){
			return "已关闭";
		}
		return "未知";
	}
	
	private String getRefundStatusName(Byte status){
		if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.UN_APPLY){
			return "未申请";
		}else if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.WAITING){
			return "待处理";
		}else if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.REJECT){
			return "已拒绝";
		}else if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.REFUNDING){
			return "退款中";
		}else if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.SUCCESS){
			return "成功";
		}else if(StatRefundOrderStatus.fromCode(status) == StatRefundOrderStatus.CLOSE){
			return "已关闭";
		}
		return "未知";
	}
	
	private String getPaidChannelName(Byte paidChannel){
		if(PaidChannel.fromCode(paidChannel) == PaidChannel.ALIPAY){
			return "支付宝";
		}else if(PaidChannel.fromCode(paidChannel) == PaidChannel.WECHAT){
			return "微信";
		}else if(PaidChannel.fromCode(paidChannel) == PaidChannel.PAYMENT){
			return "一卡通";
		}else if(PaidChannel.fromCode(paidChannel) == PaidChannel.OHTER){
			return "其他";
		}
		return "未知";
	}
	
	private List<StatShopTransactionDTO> convertTransactionDTOByRefund(List<StatRefund> statRefunds){
		List<StatShopTransactionDTO> dtos = new ArrayList<StatShopTransactionDTO>();
		for (StatRefund statRefund : statRefunds) {
			StatShopTransactionDTO dto = ConvertHelper.convert(statRefund, StatShopTransactionDTO.class);
			dto.setPaidAmount(statRefund.getRefundAmount().doubleValue());
			dto.setPaidTime(statRefund.getRefundTime().getTime());
			if(null != statRefund.getPayerUid()){
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(statRefund.getPayerUid(), IdentifierType.MOBILE.getCode());
				if(null != userIdentifier){
					dto.setUserPhone(userIdentifier.getIdentifierToken());
				}
			}
			String wareJson = statRefund.getWareJson();
			List<StatWareDTO> statWareDTOs =  new ArrayList<StatWareDTO>();
			
			if(!StringUtils.isEmpty(wareJson)){
				Ware[] wares = (Ware[])StringHelper.fromJsonString(wareJson, Ware[].class);
				if(null != wares){
					for (Ware ware : wares) {
						StatWareDTO wareDTO = this.getWareInfo(ware.getWareId());
						wareDTO.setNumber(ware.getNumber());
						statWareDTOs.add(wareDTO);
					}
				}
			}
			dto.setWares(statWareDTOs);
			StatOrder order = statTransactionProvider.findStatOrderByOrderNoAndResourceType(statRefund.getOrderNo(), SettlementResourceType.SHOP.getCode());
			if(null != order){
				dto.setStatus(order.getStatus());
				dto.setStatusName(this.getRefundStatusName(order.getStatus()));
			}
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	private StatWareDTO getWareInfo(Long modelId){
		String serverURL = configurationProvider.getValue(StatTransactionConstant.BIZ_SERVER_URL, "");
		if(StringUtils.isEmpty(serverURL)){
			LOGGER.error("biz serverURL not configured, param = {}", StatTransactionConstant.BIZ_SERVER_URL);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz serverURL not configured.");
		}
		
		String paidOrderApi = configurationProvider.getValue(StatTransactionConstant.BIZ_WARE_INFO_API, "");
		
		if(StringUtils.isEmpty(paidOrderApi)){
			LOGGER.error("biz paid ware info api not configured, param = {}", StatTransactionConstant.BIZ_WARE_INFO_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz paid  ware info api not configured.");
		}
		
		try {
			String appKey = configurationProvider.getValue(StatTransactionConstant.BIZ_APPKEY, "");
	        String secretKey = configurationProvider.getValue(StatTransactionConstant.BIZ_SECRET_KEY, "");
	        List<Long> modelIds = new ArrayList<Long>();
	        modelIds.add(modelId);
	        Integer nonce = (int)(Math.random()*1000);
		    Long timestamp = System.currentTimeMillis();       
		    Map<String,Object> params = new HashMap<String, Object>();
		    params.put("nonce", nonce);
		    params.put("timestamp", timestamp);
		    params.put("appKey", appKey);
		    params.put("modelIds", modelIds);
		    Map<String, String> mapForSignature = new HashMap<String, String>();
	        for(Map.Entry<String, Object> entry : params.entrySet()) {
	        	if(!entry.getKey().equals("modelIds")) {
	        		mapForSignature.put(entry.getKey(), entry.getValue().toString());
	        	}
	        }
	        mapForSignature.put("modelIds", org.apache.commons.lang.StringUtils.join(modelIds, ","));
		    String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
		    params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		    String url = serverURL + paidOrderApi;
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("request url = {}, params = {}", url, params);
			}

		    String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, HTTP.UTF_8);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("response result = {}", result);
			}
		    ListModelInfoResponse response = (ListModelInfoResponse)StringHelper.fromJsonString(result, ListModelInfoResponse.class);
		    
		    if(null != response){
		    	List<Model> models = response.getResponse();
		    	if(null != models && 0 < models.size()){
		    		Model model = models.get(0);
		    		StatWareDTO dto = new StatWareDTO();
		    		dto.setWareName(model.getCommoName());
		    		dto.setWareId(model.getCommoNo());
		    		return dto;
		    	}
		    }
		} catch (Exception e) {
			LOGGER.error("Get ware info error e = {}");
		}
		
	    return null;
	}
	
	private String getServiceName(String serviceType){
		if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.COMMUNITY_SERVICE){
			return "社区服务";
		}else if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.ZUOLIN_SHOP){
			return "左邻小站";
		}else if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.OTHER_SHOP){
			return "其他店铺";
		}else if(SettlementServiceType.fromCode(serviceType) == SettlementServiceType.THIRD_SERVICE){
			return "第三方服务";
		}
		return "其他服务";
	}
	
//	private String getResourceName(String resourceType, String resourceName){
//		if(SettlementResourceType.fromCode(resourceType) == SettlementResourceType.SHOP){
//			return resourceName;
//		}else if(SettlementResourceType.fromCode(resourceType) == SettlementResourceType.PARKING_RECHARGE){
//			return "停车充值";
//		}else if(SettlementResourceType.fromCode(resourceType) == SettlementResourceType.PAYMENT_CARD){
//			return "一卡通";
//		}else if(SettlementResourceType.fromCode(resourceType) == SettlementResourceType.PMSY){
//			return "物业缴费";
//		}else if(SettlementResourceType.fromCode(resourceType) == SettlementResourceType.RENTAL_SITE){
//			return "资源预定";
//		}
//		
//		return "其他服务";
//	}
	
	/**
	 * 按天生成结算
	 */
	private StatTaskLog settlementByDate(String date){
		
		LOGGER.debug("start production settlement data. date = {}", date);
		
		StatTaskLog statTaskLog = statTransactionProvider.findStatTaskLog(date);
		
		if(null == statTaskLog){
			statTaskLog = new StatTaskLog();
			statTaskLog.setIslock(StatTaskLock.LOCK.getCode());
			statTaskLog.setStatus(StatTaskStatus.SYNC_SHOP_ORDER.getCode());
			statTaskLog.setTaskNo(date);
			statTransactionProvider.createStatTaskLog(statTaskLog);
		}else{
			//把锁置成无效
//			if(StatTaskLock.fromCode(statTaskLog.getIslock()) == StatTaskLock.LOCK){
//				LOGGER.debug("settlement data task being executed, date = {}, lock = {} ", date, statTaskLog.getIslock());
//				return statTaskLog;
//			}
			//把生成结算数据的任务锁住，不让其他线程执行
			statTaskLog.setIslock(StatTaskLock.LOCK.getCode());
			statTransactionProvider.updateStatTaskLog(statTaskLog);
		}
		
		try {
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_SHOP_ORDER){
				this.syncShopToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PMSY_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PMSY_ORDER){
				this.syncPmsyToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PARKING_RECHARGE_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PARKING_RECHARGE_ORDER){
				this.syncParkingRechargeToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_RENTAL_SITE_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_RENTAL_SITE_ORDER){
				this.syncRentalSiteToStatOrderByDate(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_CARD_ORDER.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}			
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_CARD_ORDER){
//				this.syncPaymentToStatOrderByDate(date);   //屏蔽一卡通
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAID_PLATFORM_TRANSACTION.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAID_PLATFORM_TRANSACTION){
				this.syncPaidPlatformToStatTransaction(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_CARD_TRANSACTION.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_CARD_TRANSACTION){
//				this.syncPaymentToStatTransaction(date);   //屏蔽一卡通
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAID_PLATFORM_REFUND.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAID_PLATFORM_REFUND){
				this.syncPaidPlatformToStatRefund(date);
				statTaskLog.setStatus(StatTaskStatus.SYNC_PAYMENT_REFUND.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.SYNC_PAYMENT_REFUND){
//				this.syncPaymentToStatRefund(date);   //屏蔽一卡通
				statTaskLog.setStatus(StatTaskStatus.GENERATE_SETTLEMENT_DETAIL.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.GENERATE_SETTLEMENT_DETAIL){
				this.generateStatSettlementByDate(date);
				statTaskLog.setStatus(StatTaskStatus.GENERATE_SETTLEMENT_RESULT.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			if(StatTaskStatus.fromCode(statTaskLog.getStatus()) == StatTaskStatus.GENERATE_SETTLEMENT_RESULT){
				this.generateStatServiceSettlementResultByDate(date);
				statTaskLog.setStatus(StatTaskStatus.FINISH.getCode());
				statTransactionProvider.updateStatTaskLog(statTaskLog);
			}
			
			LOGGER.debug("End production settlement data. date = {}", date);
		} catch (Exception e) {
			LOGGER.error("production settlement data error, date = {} error = {}", date, e);
		} finally{
			//执行结束，解锁
			statTaskLog.setIslock(StatTaskLock.UNLOCK.getCode());
			statTransactionProvider.updateStatTaskLog(statTaskLog);
		}
		
		return statTaskLog;
	}
	
	/**
	 * 同步物业缴费订单到结算订单表
	 * @param date
	 */
	private void syncPmsyToStatOrderByDate(String date){

		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPmsyToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PMSY.getCode());
		
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> status = new ArrayList<Byte>();
		status.add(PmsyOrderStatus.UNPAID.getCode());
		status.add(PmsyOrderStatus.PAID.getCode());
		while (true) {
			//物业缴费交易订单数据同步
			List<PmsyOrder> pmsyOrders = pmsyProvider.listPmsyOrders(pageSize, startDate, endDate,status, locator);
			for (PmsyOrder pmsyOrder : pmsyOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(pmsyOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(pmsyOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				statOrder.setNamespaceId(pmsyOrder.getNamespaceId());
				statOrder.setOrderAmount(pmsyOrder.getOrderAmount());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(pmsyOrder.getId().toString());
				statOrder.setOrderTime(pmsyOrder.getCreateTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(pmsyOrder.getCreatorUid());
				statOrder.setResourceType(SettlementResourceType.PMSY.getCode());
				
				//转换成结算订单数据的状态
				if(PmsyOrderStatus.fromCode(pmsyOrder.getStatus()) == PmsyOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(PmsyOrderStatus.fromCode(pmsyOrder.getStatus()) == PmsyOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(pmsyOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//物业缴费退款订单数据同步
			break;
		}
		
	}
	
	/**
	 * 同步电商订单到结算订单表
	 * @param date
	 */
	private void syncShopToStatOrderByDate(String date) throws Exception{
		String serverURL = configurationProvider.getValue(StatTransactionConstant.BIZ_SERVER_URL, "");
		if(StringUtils.isEmpty(serverURL)){
			LOGGER.error("biz serverURL not configured, param = {}", StatTransactionConstant.BIZ_SERVER_URL);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz serverURL not configured.");
		}
		
		String paidOrderApi = configurationProvider.getValue(StatTransactionConstant.BIZ_PAID_ORDER_API, "");
		
		if(StringUtils.isEmpty(paidOrderApi)){
			LOGGER.error("biz paid order api not configured, param = {}", StatTransactionConstant.BIZ_PAID_ORDER_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz paid order api not configured.");
		}
		
		String refundOrderApi = configurationProvider.getValue(StatTransactionConstant.BIZ_REFUND_ORDER_API, "");

		if(StringUtils.isEmpty(refundOrderApi)){
			LOGGER.error("biz refund order api not configured, param = {}", StatTransactionConstant.BIZ_REFUND_ORDER_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"biz refund order api not configured.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.SHOP.getCode());
		
		String url = serverURL + paidOrderApi;
		//设置需要查询的订单的状态集
		List<String> status = new ArrayList<String>();
		status.add("2");
		status.add("3");
		status.add("6");
		status.add("7");
		Map<String, Object> params = this.getParams(date);
		String pageAnchor = null;
		params.put("status", status);
		String appKey = configurationProvider.getValue(StatTransactionConstant.BIZ_APPKEY, "");
        String secretKey = configurationProvider.getValue(StatTransactionConstant.BIZ_SECRET_KEY, "");
        params.put("appKey", appKey);
        
        Map<String, String> mapForSignature = new HashMap<String, String>();
        for(Map.Entry<String, Object> entry : params.entrySet()) {
        	if(!entry.getKey().equals("status")) {
				mapForSignature.put(entry.getKey(), entry.getValue().toString());
			}
        }
        mapForSignature.put("status", org.apache.commons.lang.StringUtils.join(status, ","));
        String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		while (true) {
			String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, null);
			ListPaidOrderResponse response = (ListPaidOrderResponse)StringHelper.fromJsonString(result, ListPaidOrderResponse.class);
			
			this.checkErrorCode(url, StringHelper.toJsonString(params), response.getErrorCode(), result);
			
			if(null != response.getResponse()){
				PaidOrderResponse res = response.getResponse();
				pageAnchor = res.getNextAnchor();
				
				if(null != res.getList()){
					// 批量添加支付订单
					this.batchAddStatPaidOrder(res.getList());
				}
			}
			
			if(StringUtils.isEmpty(pageAnchor)){
				break;
			}
			params.put("pageAnchor", pageAnchor);
			mapForSignature.put("pageAnchor", pageAnchor);
			signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
	        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		}
		
		url = serverURL + refundOrderApi;
		params = this.getParams(date);
		//设置需要查询的订单的状态集
		status = new ArrayList<String>();
		status.add("4");
		status.add("5");
		status.add("6");
		params.put("status", status);
		params.put("appKey", appKey);
		mapForSignature = new HashMap<String, String>();
	    for(Map.Entry<String, Object> entry : params.entrySet()) {
	    	if(!entry.getKey().equals("status")) {
	    		mapForSignature.put(entry.getKey(), entry.getValue().toString());
	    	}
	    }
	    mapForSignature.put("status", org.apache.commons.lang.StringUtils.join(status, ","));
	    signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
	    params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		while (true) {
			String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, null);
			
			ListRefundOrderResponse response = (ListRefundOrderResponse)StringHelper.fromJsonString(result, ListRefundOrderResponse.class);
			
			this.checkErrorCode(url, StringHelper.toJsonString(params), response.getErrorCode(), result);
			
			if(null != response.getResponse()){
				RefundOrderResponse res = response.getResponse();
				pageAnchor = res.getNextAnchor();
				
				if(null != res.getList()){
					// 批量添加退款订单
					this.batchAddStatRefundOrder(res.getList());
				}
			}
			
			if(StringUtils.isEmpty(pageAnchor)){
				
				break;
			}
			
			params.put("pageAnchor", pageAnchor);
			mapForSignature.put("pageAnchor", pageAnchor.toString());
			signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
	        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		}
	}
	
	
	private void batchAddStatPaidOrder(List<BizPaidOrder> orders){
		for (BizPaidOrder bizPaidOrder : orders) {
			StatOrder order = new StatOrder();
			order.setCommunityId(0L);
			order.setNamespaceId(0);
			order.setOrderNo(bizPaidOrder.getOrderNo());
			order.setOrderAmount(bizPaidOrder.getPaidAmount());
			order.setStatus(bizPaidOrder.getStatus());
			order.setOrderType(SettlementOrderType.TRANSACTION.getCode());
			if(1 == bizPaidOrder.getPayType() || 2 == bizPaidOrder.getPayType()){
				Long userId = Long.valueOf(bizPaidOrder.getBuyerUserId());
				User user = userProvider.findUserById(userId);
				order.setPayerUid(userId);
				if(null != user){
					order.setNamespaceId(user.getNamespaceId());
				}
			}else{
				order.setNamespaceId(0);
			}
			order.setShopType(bizPaidOrder.getShopCreateType());
			order.setResourceType(SettlementResourceType.SHOP.getCode());
			order.setResourceId(bizPaidOrder.getShopNo());
			order.setOrderTime(new Timestamp(bizPaidOrder.getPaidTime()));
			order.setOrderDate(DateUtil.dateToStr(new Date(bizPaidOrder.getPaidTime()), DateUtil.YMR_SLASH));
			
			
			List<Ware> wares = new ArrayList<Ware>();
			if(null != bizPaidOrder.getModels() && 0 < bizPaidOrder.getModels().size()){
				for (BizOrderModel model : bizPaidOrder.getModels()) {
					Ware ware = new Ware();
					ware.setWareId(model.getId());
					ware.setNumber(model.getQuantity());
					wares.add(ware);
				}
			}
			order.setWareJson(StringHelper.toJsonString(wares));
			
			statTransactionProvider.createStatOrder(order);
		}
	}
	
	private void batchAddStatRefundOrder(List<BizRefundOrder> orders){
		for (BizRefundOrder bizRefundOrder : orders) {
			StatOrder order = new StatOrder();
			order.setCommunityId(0L);
			order.setNamespaceId(0);
			order.setOrderNo(bizRefundOrder.getRefundOrderNo());
			order.setOrderAmount(bizRefundOrder.getRefundAmount());
			order.setStatus(bizRefundOrder.getRefundStatus());
			order.setOrderType(SettlementOrderType.REFUND.getCode());
			if(1 == bizRefundOrder.getPayType() || 2 == bizRefundOrder.getPayType()){
				Long userId = Long.valueOf(bizRefundOrder.getBuyerUserId());
				User user = userProvider.findUserById(userId);
				order.setPayerUid(userId);
				if(null != user){
					order.setNamespaceId(user.getNamespaceId());
				}
			}else{
				order.setNamespaceId(0);
			}
			order.setShopType(bizRefundOrder.getShopCreateType());
			order.setResourceType(SettlementResourceType.SHOP.getCode());
			order.setResourceId(bizRefundOrder.getShopNo());
			order.setOrderTime(new Timestamp(bizRefundOrder.getRefundTime()));
			order.setOrderDate(DateUtil.dateToStr(new Date(bizRefundOrder.getRefundTime()), DateUtil.YMR_SLASH));
			
			List<Ware> wares = new ArrayList<Ware>();
			Ware ware = new Ware();
			ware.setWareId(bizRefundOrder.getModelId());
			ware.setNumber(bizRefundOrder.getRefundQuantity());
			wares.add(ware);
			order.setWareJson(StringHelper.toJsonString(wares));
			
			statTransactionProvider.createStatOrder(order);
		}
	}
	
	/**
	 * 同步停车充值订单到结算订单表
	 * @param date
	 */
	private void syncParkingRechargeToStatOrderByDate(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncParkingRechargeToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PARKING_RECHARGE.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> status = new ArrayList<Byte>();
		status.add(ParkingRechargeOrderStatus.UNPAID.getCode());
		status.add(ParkingRechargeOrderStatus.PAID.getCode());
		
		while (true) {
			//停车充值交易订单数据同步
			List<ParkingRechargeOrder> parkingRechargeOrders = parkingProvider.listParkingRechargeOrders(pageSize, startDate, endDate,status, locator);
			for (ParkingRechargeOrder parkingRechargeOrder : parkingRechargeOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(parkingRechargeOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(parkingRechargeOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				User user = userProvider.findUserById(parkingRechargeOrder.getPayerUid());
				statOrder.setNamespaceId(user.getNamespaceId());
				statOrder.setOrderAmount(parkingRechargeOrder.getPrice());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(parkingRechargeOrder.getId().toString());
				statOrder.setOrderTime(parkingRechargeOrder.getRechargeTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(parkingRechargeOrder.getPayerUid());
				statOrder.setResourceType(SettlementResourceType.PARKING_RECHARGE.getCode());
				
				//转换成结算订单数据的状态
				if(ParkingRechargeOrderStatus.fromCode(parkingRechargeOrder.getStatus()) == ParkingRechargeOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(ParkingRechargeOrderStatus.fromCode(parkingRechargeOrder.getStatus()) == ParkingRechargeOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(parkingRechargeOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//停车充值退款订单数据同步
			break;
		}
	}
	
	/**
	 * 同步资源预定订单到结算订单表
	 * @param date
	 */
	private void syncRentalSiteToStatOrderByDate(String date){
		
	}
	
	/**
	 * 同步一卡通订单到结算订单表
	 * @param date
	 */
	private void syncPaymentToStatOrderByDate(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPaymentToStatOrderByDate parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatOrderByDate(date, SettlementResourceType.PAYMENT_CARD.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> status = new ArrayList<Byte>();
		status.add(CardOrderStatus.UNPAID.getCode());
		status.add(CardOrderStatus.PAID.getCode());
		
		while (true) {
			//一卡通交易订单数据同步
			List<PaymentCardRechargeOrder> paymentCardRechargeOrders = paymentCardProvider.listPaymentCardRechargeOrders(pageSize, startDate, endDate,status, locator);
			for (PaymentCardRechargeOrder paymentCardRechargeOrder : paymentCardRechargeOrders) {
				StatOrder statOrder = new StatOrder();
				if(PmsyOwnerType.fromCode(paymentCardRechargeOrder.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statOrder.setCommunityId(paymentCardRechargeOrder.getOwnerId());
				}else{
					statOrder.setCommunityId(0L);
				}
				statOrder.setNamespaceId(paymentCardRechargeOrder.getNamespaceId());
				statOrder.setOrderAmount(paymentCardRechargeOrder.getAmount());
				statOrder.setOrderDate(date);
				statOrder.setOrderNo(paymentCardRechargeOrder.getOrderNo().toString());
				statOrder.setOrderTime(paymentCardRechargeOrder.getRechargeTime());
				statOrder.setOrderType(SettlementOrderType.TRANSACTION.getCode());
				statOrder.setPayerUid(paymentCardRechargeOrder.getPayerUid());
				statOrder.setResourceType(SettlementResourceType.PAYMENT_CARD.getCode());
				
				//转换成结算订单数据的状态
				if(CardOrderStatus.fromCode(paymentCardRechargeOrder.getPayStatus()) == CardOrderStatus.PAID){
					statOrder.setStatus(SettlementStatOrderStatus.PAID.getCode());
				}else if(CardOrderStatus.fromCode(paymentCardRechargeOrder.getPayStatus()) == CardOrderStatus.UNPAID){
					statOrder.setStatus(SettlementStatOrderStatus.WAITING.getCode());
				}
				statTransactionProvider.createStatOrder(statOrder);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(paymentCardRechargeOrders.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
		
		while (true) {
			//一卡通退款订单数据同步
			break;
		}
	}
	
	/**
	 * 同步支付平台交易流水到结算交易流水表
	 * @param date
	 */
	private void syncPaidPlatformToStatTransaction(String date) throws Exception{
		String serverURL = configurationProvider.getValue(StatTransactionConstant.PAID_SERVER_URL, "");
		if(StringUtils.isEmpty(serverURL)){
			LOGGER.error("paid serverURL not configured, param = {}", StatTransactionConstant.PAID_SERVER_URL);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"paid serverURL not configured.");
		}
		String paidTransactionApi = configurationProvider.getValue(StatTransactionConstant.PAID_TRANSACTION_API, "");
		if(StringUtils.isEmpty(paidTransactionApi)){
			LOGGER.error("paid transaction api not configured, param = {}", StatTransactionConstant.PAID_TRANSACTION_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"paid transaction api not configured.");
		}
		
		/*根据业务 删除除一卡通的支付流水*/
		SettlementResourceType[] resourceTypes = SettlementResourceType.values();
		for (SettlementResourceType resourceType : resourceTypes) {
			if(SettlementResourceType.PAYMENT_CARD != resourceType){
				statTransactionProvider.deleteStatTransactionByDate(date, resourceType.getCode());
			}
		}
		
		
		String url = serverURL + paidTransactionApi;
		List<String> status = new ArrayList<String>();
		status.add("success");
		Map<String, Object> params = this.getParams(date);
		String pageAnchor = null;
		params.put("status", status);
		String appKey = configurationProvider.getValue(StatTransactionConstant.PAID_APPKEY, "");
        String secretKey = configurationProvider.getValue(StatTransactionConstant.PAID_SECRET_KEY, "");
        params.put("appKey", appKey);
        Map<String, String> mapForSignature = new HashMap<String, String>();
        for(Map.Entry<String, Object> entry : params.entrySet()) {
        	if(!entry.getKey().equals("status")) {
				mapForSignature.put(entry.getKey(), entry.getValue().toString());
			}
        }
        mapForSignature.put("status", org.apache.commons.lang.StringUtils.join(status, ","));
        String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		while (true) {
			String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, null);
			
			ListPaidTransactionResponse response = (ListPaidTransactionResponse)StringHelper.fromJsonString(result, ListPaidTransactionResponse.class);

			this.checkErrorCode(url, StringHelper.toJsonString(params), response.getErrorCode(), result);
			
			if(null != response.getResponse()){
				PaidTransactionResponse res = response.getResponse();
				pageAnchor = res.getNextAnchor();
				if(null != res.getList()){
					// 批量添加支付流水
					this.batchAddStatTransaction(res.getList());
				}
			}

			if(StringUtils.isEmpty(pageAnchor)){
				break;
			}
			
			params.put("pageAnchor", pageAnchor);
			mapForSignature.put("pageAnchor", pageAnchor.toString());
			signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
	        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		}
	}
	
	private void batchAddStatTransaction(List<PaidTransaction> transactions){
		for (PaidTransaction paidTransaction : transactions) {
			StatTransaction statTransaction = new StatTransaction();
			statTransaction.setPaidAmount(paidTransaction.getPaidAmount());
			statTransaction.setFeeRate(BigDecimal.valueOf(0));
			statTransaction.setFeeAmount(statTransaction.getPaidAmount().multiply(statTransaction.getFeeRate()));
			statTransaction.setSettlementAmount(statTransaction.getPaidAmount().subtract(statTransaction.getFeeAmount()));
			statTransaction.setOrderNo(paidTransaction.getOrderNo());
			statTransaction.setPaidChannel(this.getPaidChannel(paidTransaction.getOnlinePayStyleNo()).getCode());
			statTransaction.setPaidStatus(SettlementStatTransactionPaidStatus.PAID.getCode());
			statTransaction.setPaidType(paidTransaction.getPayType());
			statTransaction.setResourceType(this.getResourceType(paidTransaction.getOrderType()).getCode());
			statTransaction.setPaidTime(new Timestamp(paidTransaction.getPaidTime()));
			statTransaction.setTransactionNo(paidTransaction.getPayNo());
			
			StatOrder statOrder = statTransactionProvider.findStatOrderByOrderNoAndResourceType(statTransaction.getOrderNo(), statTransaction.getResourceType());
			
			if(null != statOrder){
				statTransaction.setCommunityId(statOrder.getCommunityId());
				statTransaction.setNamespaceId(statOrder.getNamespaceId());
				statTransaction.setPayerUid(statOrder.getPayerUid());
				if(StatTransactionConstant.PAY_RECORD_ORDER_TYPE_DIANSHANG.equals(paidTransaction.getOrderType())){
					if(statOrder.getShopType() == StatTransactionConstant.PAY_ORDER_SHOP_TYPE_PLATFORM){
						statTransaction.setServiceType(SettlementServiceType.ZUOLIN_SHOP.getCode());
					}else if(statOrder.getShopType() == StatTransactionConstant.PAY_ORDER_SHOP_TYPE_SELF){
						statTransaction.setServiceType(SettlementServiceType.OTHER_SHOP.getCode());
					}
					statTransaction.setResourceId(statOrder.getResourceId());
				}
				statTransaction.setWareJson(statOrder.getWareJson());
			}else{
				statTransaction.setCommunityId(0L);
				statTransaction.setNamespaceId(0);
				statTransaction.setResourceId("-1");
				statTransaction.setPayerUid(0L);
			}
			
			if(StatTransactionConstant.COMMUNITY_SERVICES.contains(paidTransaction.getOrderType())){
				statTransaction.setServiceType(SettlementServiceType.COMMUNITY_SERVICE.getCode());
			}else{
				statTransaction.setServiceType(SettlementServiceType.ZUOLIN_SHOP.getCode());
			}
			
			
			
			statTransaction.setPaidDate(DateUtil.dateToStr(new Date(paidTransaction.getPaidTime()), DateUtil.YMR_SLASH));
			statTransactionProvider.createStatTransaction(statTransaction);
		}
	}
	
	private PaidChannel getPaidChannel(String payType){
		PaidChannel paidChannel = PaidChannel.OHTER;
		switch (payType) {
		case StatTransactionConstant.PAY_RECORD_PAY_TYPE_ALIPAY:
			paidChannel = PaidChannel.ALIPAY;
			break;
		case StatTransactionConstant.PAY_RECORD_PAY_TYPE_WECHAT:
			paidChannel = PaidChannel.WECHAT;
			break;
		default:
			break;
		}
		return paidChannel;
	}
	
	private SettlementResourceType getResourceType(String orderType){
		SettlementResourceType resourceType = SettlementResourceType.OTHER;
		switch (orderType) {
		case StatTransactionConstant.PAY_RECORD_ORDER_TYPE_DIANSHANG:
			resourceType = SettlementResourceType.SHOP;
			break;
		case StatTransactionConstant.PAY_RECORD_ORDER_TYPE_PMSY:
			resourceType = SettlementResourceType.PMSY;
			break;
		case StatTransactionConstant.PAY_RECORD_ORDER_TYPE_PAYMENTCARD:
			resourceType = SettlementResourceType.PAYMENT_CARD;
			break;
		case StatTransactionConstant.PAY_RECORD_ORDER_TYPE_RENTALORDER:
			resourceType = SettlementResourceType.RENTAL_SITE;
			break;
		case StatTransactionConstant.PAY_RECORD_ORDER_TYPE_PARKING:
			resourceType = SettlementResourceType.PARKING_RECHARGE;
			break;
		default:
			break;
		}
		
		return resourceType;
	}
	
	/**
	 * 同步一卡通交易流水到结算交易流水表
	 * @param date
	 */
	private void syncPaymentToStatTransaction(String date){
		if(StringUtils.isEmpty(date)){
			LOGGER.error("syncPaymentToStatTransaction parameter date is null, date = {}", date);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"parameter date is null.");
		}
		
		//先删除数据，以免重复
		statTransactionProvider.deleteStatTransactionByDate(date, SettlementResourceType.PAYMENT_CARD.getCode());
		
		Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		endDate.setDate(endDate.getDate() + 1);
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<Byte> status = new ArrayList<Byte>();
		status.add(CardTransactionStatus.PAIDED.getCode());
		
		while (true) {
			//一卡通交易交易数据同步
			List<PaymentCardTransaction> paymentCardTransactions = paymentCardProvider.listCardTransactions(pageSize, startDate, endDate,status, locator);
			for (PaymentCardTransaction paymentCardTransaction : paymentCardTransactions) {
//				StatOrder statOrder = statTransactionProvider.findStatOrderByOrderNoAndResourceType(paymentCardTransaction.getOrderNo(), SettlementResourceType.PAYMENT_CARD.getCode());
				StatTransaction statTransaction = new StatTransaction();
				if(PmsyOwnerType.fromCode(paymentCardTransaction.getOwnerType()) == PmsyOwnerType.COMMUNITY){
					statTransaction.setCommunityId(paymentCardTransaction.getOwnerId());
				}else{
					statTransaction.setCommunityId(0L);
				}
				statTransaction.setNamespaceId(paymentCardTransaction.getNamespaceId());
				statTransaction.setPayerUid(paymentCardTransaction.getPayerUid());
//				if(null != statOrder){
//					statTransaction.setOrderAmount(statOrder.getOrderAmount());
//					statTransaction.setItemCode(statOrder.getItemCode());
//					statTransaction.setVendorCode(statOrder.getVendorCode());
//					statTransaction.setPayerUid(statOrder.getPayerUid());
//				}
				statTransaction.setResourceType(SettlementResourceType.PAYMENT_CARD.getCode());
				statTransaction.setPaidDate(date);
				statTransaction.setServiceType(SettlementServiceType.COMMUNITY_SERVICE.getCode());
				statTransaction.setOrderNo(paymentCardTransaction.getOrderNo());
				statTransaction.setPaidAmount(paymentCardTransaction.getAmount());
				statTransaction.setTransactionNo(paymentCardTransaction.getTransactionNo());
				statTransaction.setFeeRate(BigDecimal.valueOf(0));
				statTransaction.setFeeAmount(statTransaction.getPaidAmount().multiply(statTransaction.getFeeRate()));
				statTransaction.setSettlementAmount(statTransaction.getPaidAmount().subtract(statTransaction.getFeeAmount()));
				statTransaction.setVendorTransactionNo(paymentCardTransaction.getTransactionNo());
				statTransaction.setPaidStatus(SettlementStatTransactionPaidStatus.PAID.getCode());
				statTransaction.setPaidTime(paymentCardTransaction.getTransactionTime());
				statTransaction.setPaidChannel(PaidChannel.PAYMENT.getCode());
				statTransactionProvider.createStatTransaction(statTransaction);
			}
			
			//没查出记录或者anchor为null 代表已经是尾页了
			if(paymentCardTransactions.size() == 0 || null == locator.getAnchor()){
				break;
			}
		}
	}
	
	/**
	 * 同步支付平台退款流水到结算退款流水表
	 * @param date
	 */
	private void syncPaidPlatformToStatRefund(String date) throws Exception{
		String serverURL = configurationProvider.getValue(StatTransactionConstant.PAID_SERVER_URL, "");
		if(StringUtils.isEmpty(serverURL)){
			LOGGER.error("paid serverURL not configured, param = {}", StatTransactionConstant.PAID_SERVER_URL);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"paid serverURL not configured.");
		}
		
		String paidRefundApi = configurationProvider.getValue(StatTransactionConstant.PAID_REFUND_API, "");
		if(StringUtils.isEmpty(paidRefundApi)){
			LOGGER.error("paid refund api not configured, param = {}", StatTransactionConstant.PAID_REFUND_API);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_PARAMETER_ISNULL,
					"paid refund api not configured.");
		}
		
		/*根据业务 删除除一卡通的退款流水*/
		SettlementResourceType[] resourceTypes = SettlementResourceType.values();
		for (SettlementResourceType resourceType : resourceTypes) {
			if(SettlementResourceType.PAYMENT_CARD != resourceType){
				statTransactionProvider.deleteStatRefundByDate(date, resourceType.getCode());
			}
		}
		
		String url = serverURL + paidRefundApi;
		List<String> status = new ArrayList<String>();
		status.add("success");
		Map<String, Object> params = this.getParams(date);
		String pageAnchor = null;
		params.put("status", status);
		String appKey = configurationProvider.getValue(StatTransactionConstant.PAID_APPKEY, "");
        String secretKey = configurationProvider.getValue(StatTransactionConstant.PAID_SECRET_KEY, "");
        params.put("appKey", appKey);
        Map<String, String> mapForSignature = new HashMap<String, String>();
        for(Map.Entry<String, Object> entry : params.entrySet()) {
        	if(!entry.getKey().equals("status")) {
				mapForSignature.put(entry.getKey(), entry.getValue().toString());
			}
        }
        mapForSignature.put("status", org.apache.commons.lang.StringUtils.join(status, ","));
        String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		while (true) {
			String result = HttpUtils.postJson(url, StringHelper.toJsonString(params), 30, null);
			
			ListPaidRefundResponse response = (ListPaidRefundResponse)StringHelper.fromJsonString(result, ListPaidRefundResponse.class);

			this.checkErrorCode(url, StringHelper.toJsonString(params), response.getErrorCode(), result);
			
			if(null != response.getResponse()){
				PaidRefundResponse res = response.getResponse();
				pageAnchor = res.getNextAnchor();
				if(null != res.getList()){
					// 批量添加退款流水
					this.batchAddStatRefund(res.getList());
				}
			}
			
			if(StringUtils.isEmpty(pageAnchor)){
				break;
			}
			
			params.put("pageAnchor", pageAnchor);
			mapForSignature.put("pageAnchor", pageAnchor.toString());
			signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
	        params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		}
	}
	
	
	private void batchAddStatRefund(List<PaidRefund> refunds){
		for (PaidRefund paidRefund : refunds) {
			StatRefund statRefund = new StatRefund();
			statRefund.setRefundAmount(paidRefund.getRefundAmount());
			statRefund.setFeeRate(BigDecimal.valueOf(0));
			statRefund.setFeeAmount(statRefund.getRefundAmount().multiply(statRefund.getFeeRate()));
			statRefund.setSettlementAmount(statRefund.getRefundAmount().subtract(statRefund.getFeeAmount()));
			statRefund.setOrderNo(paidRefund.getOrderNo());
			statRefund.setPaidChannel(this.getPaidChannel(paidRefund.getOnlinePayStyleNo()).getCode());
			statRefund.setResourceType(this.getResourceType(paidRefund.getOrderType()).getCode());
			statRefund.setRefundTime(new Timestamp(paidRefund.getRefundTime()));
			statRefund.setRefundNo(paidRefund.getPayNo());
			
			StatOrder statOrder = statTransactionProvider.findStatOrderByOrderNoAndResourceType(statRefund.getOrderNo(), statRefund.getResourceType());
			
			
			if(null != statOrder){
				statRefund.setCommunityId(statOrder.getCommunityId());
				statRefund.setNamespaceId(statOrder.getNamespaceId());
				statRefund.setPayerUid(statOrder.getPayerUid());
				statRefund.setResourceId(statOrder.getResourceId());
				if(StatTransactionConstant.PAY_RECORD_ORDER_TYPE_DIANSHANG.equals(paidRefund.getOrderType())){
					if(statOrder.getShopType() == StatTransactionConstant.PAY_ORDER_SHOP_TYPE_PLATFORM){
						statRefund.setServiceType(SettlementServiceType.ZUOLIN_SHOP.getCode());
					}else if(statOrder.getShopType() == StatTransactionConstant.PAY_ORDER_SHOP_TYPE_SELF){
						statRefund.setServiceType(SettlementServiceType.OTHER_SHOP.getCode());
					}
				}
				statRefund.setWareJson(statOrder.getWareJson());
			}else{
				statRefund.setCommunityId(0L);
				statRefund.setNamespaceId(0);
				statRefund.setResourceId("-1");
				statRefund.setPayerUid(0L);
			}
			
			if(StatTransactionConstant.COMMUNITY_SERVICES.contains(paidRefund.getOrderType())){
				statRefund.setServiceType(SettlementServiceType.COMMUNITY_SERVICE.getCode());
			}else{
				statRefund.setServiceType(SettlementServiceType.ZUOLIN_SHOP.getCode());
			}
			statRefund.setRefundDate(DateUtil.dateToStr(new Date(paidRefund.getRefundTime()), DateUtil.YMR_SLASH));
			statTransactionProvider.createStatRefund(statRefund);
		}
	}
	
	/**
	 * 同步一卡通退款流水到结算退款流水表
	 * @param date
	 */
	private void syncPaymentToStatRefund(String date){
		
	}
	
	/**
	 * 生成结算数据
	 * @param date
	 */
	private void generateStatSettlementByDate(String date){
		// 先删除数据，重新生成
		statTransactionProvider.deleteStatSettlementByDate(date);
		
		List<StatSettlement> transactionSettlements = statTransactionProvider.countStatTransactionSettlement(date);
		
		List<StatSettlement> refundSettlements = statTransactionProvider.countStatRefundSettlement(date);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("generate transaction settlement data size = {}, date = {}", transactionSettlements.size(), date);
			LOGGER.debug("generate refund settlement data size = {}, date = {}", refundSettlements.size(), date);
		}
		dbProvider.execute((TransactionStatus status) -> {
			if(transactionSettlements.size() > 0){
				for (StatSettlement statSettlement : transactionSettlements) {
					statTransactionProvider.createStatSettlement(statSettlement);
				}
				
				for (StatSettlement statSettlement : refundSettlements) {
					Condition cond = Tables.EH_STAT_SETTLEMENTS.NAMESPACE_ID.eq(statSettlement.getNamespaceId());
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.COMMUNITY_ID.eq(statSettlement.getCommunityId()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.SERVICE_TYPE.eq(statSettlement.getServiceType()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_TYPE.eq(statSettlement.getResourceType()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.RESOURCE_ID.eq(statSettlement.getResourceId()));
					cond = cond.and(Tables.EH_STAT_SETTLEMENTS.PAID_CHANNEL.eq(statSettlement.getPaidChannel()));
					StatSettlement settlement = statTransactionProvider.findStatSettlementByDate(cond, date);
					
					if(null == settlement){
						statTransactionProvider.createStatSettlement(statSettlement);
					}else{
						settlement.setRefundAmount(statSettlement.getRefundAmount());
						settlement.setRefundCount(statSettlement.getRefundCount());
						settlement.setRefundFeeAmount(statSettlement.getRefundFeeAmount());
						settlement.setRefundFeeRate(statSettlement.getRefundFeeRate());
						settlement.setRefundSettlementAmount(statSettlement.getRefundSettlementAmount());
						statTransactionProvider.updateStatSettlement(settlement);
					}
				}
				
			}else{
				for (StatSettlement statSettlement : refundSettlements) {
					statTransactionProvider.createStatSettlement(statSettlement);
				}
			}
			return null;
		});
	}
	
	/**
	 * 生成结算数据结果
	 * @param date
	 */
	private void generateStatServiceSettlementResultByDate(String date){
		
		//先删除数据，重新生成
		statTransactionProvider.deleteStatServiceSettlementResultByDate(date);
		
		List<StatServiceSettlementResult> alipaySettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.ALIPAY.getCode(), date);
		List<StatServiceSettlementResult> wechatSettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.WECHAT.getCode(), date);
		List<StatServiceSettlementResult> paymentSettlementResults = statTransactionProvider.countStatServiceSettlementResult(PaidChannel.PAYMENT.getCode(), date);

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("generate alipay settlement data size = {}, date = {}", alipaySettlementResults.size(), date);
			LOGGER.debug("generate wechat settlement data size = {}, date = {}", wechatSettlementResults.size(), date);
			LOGGER.debug("generate payment settlement data size = {}, date = {}", paymentSettlementResults.size(), date);
		}
		if(alipaySettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : alipaySettlementResults) {
				statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getAlipayPaidAmount());
				statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getAlipayRefundAmount());
				statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getAlipayPaidCount());
				statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getAlipayRefundCount());
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}

			for (StatServiceSettlementResult statServiceSettlementResult : wechatSettlementResults) {
				Condition cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getWechatPaidAmount());
					statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getWechatRefundAmount());
					statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getWechatPaidCount());
					statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getWechatRefundCount());
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}else{
					settlementResult.setWechatPaidAmount(statServiceSettlementResult.getWechatPaidAmount());
					settlementResult.setWechatRefundAmount(statServiceSettlementResult.getWechatRefundAmount());
					settlementResult.setWechatPaidCount(statServiceSettlementResult.getWechatPaidCount());
					settlementResult.setWechatRefundCount(statServiceSettlementResult.getWechatRefundCount());
					settlementResult.setTotalPaidAmount(settlementResult.getAlipayPaidAmount().add(settlementResult.getWechatPaidAmount()));
					settlementResult.setTotalRefundAmount(settlementResult.getAlipayRefundAmount().add(settlementResult.getWechatRefundAmount()));
					settlementResult.setTotalPaidCount(settlementResult.getAlipayPaidCount() + settlementResult.getWechatPaidCount());
					settlementResult.setTotalRefundCount(settlementResult.getAlipayRefundCount() + settlementResult.getWechatRefundCount());
					statTransactionProvider.updateStatServiceSettlementResult(settlementResult);
				}
			}

			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				Condition cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount());
					statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount());
					statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getPaymentCardPaidCount());
					statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getPaymentCardRefundCount());
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}else{
					settlementResult.setPaymentCardPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount());
					settlementResult.setPaymentCardRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount());
					settlementResult.setPaymentCardPaidCount(statServiceSettlementResult.getPaymentCardPaidCount());
					settlementResult.setPaymentCardRefundCount(statServiceSettlementResult.getPaymentCardRefundCount());
					settlementResult.setTotalPaidAmount(settlementResult.getAlipayPaidAmount().add(settlementResult.getWechatPaidAmount()).add(settlementResult.getPaymentCardPaidAmount()));
					settlementResult.setTotalRefundAmount(settlementResult.getAlipayRefundAmount().add(settlementResult.getWechatRefundAmount()).add(settlementResult.getPaymentCardRefundAmount()));
					settlementResult.setTotalPaidCount(settlementResult.getAlipayPaidCount() + settlementResult.getWechatPaidCount() + settlementResult.getPaymentCardPaidCount());
					settlementResult.setTotalRefundCount(settlementResult.getAlipayRefundCount() + settlementResult.getWechatRefundCount() + settlementResult.getPaymentCardRefundCount());
					statTransactionProvider.updateStatServiceSettlementResult(settlementResult);
				}
			}
		}else if(wechatSettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : wechatSettlementResults) {
				statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getWechatPaidAmount());
				statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getWechatRefundAmount());
				statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getWechatPaidCount());
				statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getWechatRefundCount());
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}

			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				Condition cond = Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.NAMESPACE_ID.eq(statServiceSettlementResult.getNamespaceId());
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.COMMUNITY_ID.eq(statServiceSettlementResult.getCommunityId()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.SERVICE_TYPE.eq(statServiceSettlementResult.getServiceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_TYPE.eq(statServiceSettlementResult.getResourceType()));
				cond = cond.and(Tables.EH_STAT_SERVICE_SETTLEMENT_RESULTS.RESOURCE_ID.eq(statServiceSettlementResult.getResourceId()));
				StatServiceSettlementResult settlementResult = statTransactionProvider.findStatServiceSettlementResultDate(cond, date);
				if(null == settlementResult){
					statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount());
					statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount());
					statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getPaymentCardPaidCount());
					statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getPaymentCardRefundCount());
					statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
				}else{
					settlementResult.setPaymentCardPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount());
					settlementResult.setPaymentCardRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount());
					settlementResult.setPaymentCardPaidCount(statServiceSettlementResult.getPaymentCardPaidCount());
					settlementResult.setPaymentCardRefundCount(statServiceSettlementResult.getPaymentCardRefundCount());
					settlementResult.setTotalPaidAmount(settlementResult.getWechatPaidAmount().add(settlementResult.getPaymentCardPaidAmount()));
					settlementResult.setTotalRefundAmount(settlementResult.getWechatRefundAmount().add(settlementResult.getPaymentCardRefundAmount()));
					settlementResult.setTotalPaidCount(settlementResult.getWechatPaidCount() + settlementResult.getPaymentCardPaidCount());
					settlementResult.setTotalRefundCount(settlementResult.getWechatRefundCount() + settlementResult.getPaymentCardRefundCount());
					statTransactionProvider.updateStatServiceSettlementResult(settlementResult);
				}
			}
		}else if(paymentSettlementResults.size() > 0){
			for (StatServiceSettlementResult statServiceSettlementResult : paymentSettlementResults) {
				statServiceSettlementResult.setTotalPaidAmount(statServiceSettlementResult.getPaymentCardPaidAmount());
				statServiceSettlementResult.setTotalRefundAmount(statServiceSettlementResult.getPaymentCardRefundAmount());
				statServiceSettlementResult.setTotalPaidCount(statServiceSettlementResult.getPaymentCardPaidCount());
				statServiceSettlementResult.setTotalRefundCount(statServiceSettlementResult.getPaymentCardRefundCount());
				statTransactionProvider.createStatServiceSettlementResult(statServiceSettlementResult);
			}
		}
		
		
	}
	
	private Map<String, Object> getParams(String date){
		 Integer nonce = (int)(Math.random()*1000);
	     Long timestamp = System.currentTimeMillis();       
	     
	     int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, 10);
	     
	     Timestamp startDate = Timestamp.valueOf(date + " 00:00:00");
		 Timestamp endDate = Timestamp.valueOf(date + " 00:00:00");
		 endDate.setDate(endDate.getDate() + 1);
	     
	     Map<String,Object> params = new HashMap<String, Object>();
	     params.put("nonce", nonce);
	     params.put("timestamp", timestamp);
	     params.put("pageSize", pageSize);
//	     params.put("pageAnchor", 1464157283126L);
	     params.put("startTime", startDate.getTime());
	     params.put("endTime", endDate.getTime() - 1);     
	     return params;

	}

	private void checkErrorCode(String url, String params, Integer code, String result){
		if(200 != code){
			LOGGER.error("Failed to get data, url = {}, params = {}, result = {}", url, params , result);
			throw RuntimeErrorException.errorWith(SettlementErrorCode.SCOPE, SettlementErrorCode.ERROR_RESPONSE,
					"Failed to get data.");
		}
	}
	
	
	public static void main(String[] args) {
		String json = "{\"version\" : \"1\",\"response\" : {\"nextAnchor\" : \"1\",list:[{\"orderNo\" : \"1\"}]}}";
		ListPaidOrderResponse response = (ListPaidOrderResponse)StringHelper.fromJsonString(json, ListPaidOrderResponse.class);
		PaidOrderResponse res = response.getResponse();
		List<BizPaidOrder> list  = res.getList();
		Integer s = 200;
		System.out.println(Timestamp.valueOf("2016-08-01" + " 00:00:00").getTime());
		
		
		List<Date> dDates = DateUtil.getStartToEndDates(new Date(1422025100000L), new Date(1472065200001L));
		for (Date date : dDates) {
			System.out.println(DateUtil.dateToStr(date, DateUtil.YMR_SLASH));

		}
	}
}
