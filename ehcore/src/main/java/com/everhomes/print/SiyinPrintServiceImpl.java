// @formatter:off
package com.everhomes.print;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.print.GetPrintLogonUrlCommand;
import com.everhomes.rest.print.GetPrintLogonUrlResponse;
import com.everhomes.rest.print.GetPrintSettingCommand;
import com.everhomes.rest.print.GetPrintSettingResponse;
import com.everhomes.rest.print.GetPrintStatCommand;
import com.everhomes.rest.print.GetPrintStatResponse;
import com.everhomes.rest.print.GetPrintUnpaidOrderCommand;
import com.everhomes.rest.print.GetPrintUnpaidOrderResponse;
import com.everhomes.rest.print.GetPrintUserEmailCommand;
import com.everhomes.rest.print.GetPrintUserEmailResponse;
import com.everhomes.rest.print.InformPrintCommand;
import com.everhomes.rest.print.InformPrintResponse;
import com.everhomes.rest.print.ListPrintJobTypesCommand;
import com.everhomes.rest.print.ListPrintJobTypesResponse;
import com.everhomes.rest.print.ListPrintOrderStatusCommand;
import com.everhomes.rest.print.ListPrintOrderStatusResponse;
import com.everhomes.rest.print.ListPrintOrdersCommand;
import com.everhomes.rest.print.ListPrintOrdersResponse;
import com.everhomes.rest.print.ListPrintRecordsCommand;
import com.everhomes.rest.print.ListPrintRecordsResponse;
import com.everhomes.rest.print.ListPrintUserOrganizationsCommand;
import com.everhomes.rest.print.ListPrintUserOrganizationsResponse;
import com.everhomes.rest.print.ListPrintingJobsCommand;
import com.everhomes.rest.print.ListPrintingJobsResponse;
import com.everhomes.rest.print.LogonPrintCommand;
import com.everhomes.rest.print.LogonPrintResponse;
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PayPrintOrderResponse;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.PrintJobTypeType;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.rest.print.PrintOwnerType;
import com.everhomes.rest.print.PrintPaperSizeType;
import com.everhomes.rest.print.PrintSettingColorTypeDTO;
import com.everhomes.rest.print.PrintSettingPaperSizePriceDTO;
import com.everhomes.rest.print.PrintSettingType;
import com.everhomes.rest.print.PrintStatDTO;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class SiyinPrintServiceImpl implements SiyinPrintService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintServiceImpl.class);
	
	@Autowired
	private SiyinPrintEmailProvider siyinPrintEmailProvider;
	@Autowired
	private SiyinPrintOrderProvider siyinPrintOrderProvider;
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	@Autowired
	private SiyinPrintRecordProvider siyinPrintRecordProvider;
	@Autowired
	private SiyinPrintSettingProvider siyinPrintSettingProvider;
	@Autowired
    private LocaleStringProvider localeStringProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public GetPrintSettingResponse getPrintSetting(GetPrintSettingCommand cmd) {
		//检查参数
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		
		//获取设置对象集合
		List<SiyinPrintSetting> printSettingList = siyinPrintSettingProvider.listSiyinPrintSettingByOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		//集合装返回的response
		return processPrintSettingResponse(printSettingList);
	}


	@Override
	public void updatePrintSetting(UpdatePrintSettingCommand cmd) {
		//检查参数，并生成更新集合对象
		List<SiyinPrintSetting> list = checkUpdatePrintSettingCommand(cmd);
		
		//删除后创建 设置对象，并在一个事物里面
		siyinPrintSettingProvider.createSiyinPrintSettings(list,cmd.getOwnerType(),cmd.getOwnerId());
	}

	@Override
	public GetPrintStatResponse getPrintStat(GetPrintStatCommand cmd) {
		PrintOwnerType printOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		List<String> ownerTypeList = new ArrayList<String>();
		List<Long> ownerIdList = new ArrayList<Long>();
		
		if(printOwnerType == PrintOwnerType.ENTERPRISE){
			List<OrganizationCommunity> list = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
			if(list == null || list.size() == 0){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, organizationId = {}, have empty community",cmd.getOwnerId());
			}
			list.forEach(r -> {
				ownerIdList.add(r.getCommunityId());
				ownerTypeList.add(PrintOwnerType.COMMUNITY.getCode());
			});
		}else{
			ownerTypeList.add(cmd.getOwnerType());
			ownerIdList.add(cmd.getOwnerId());
		}
		
		//查询订单
		List<SiyinPrintOrder> orders = siyinPrintOrderProvider.listSiyinPrintOrder(cmd.getStartTime(), cmd.getEndTime(), ownerTypeList, ownerIdList);
		
		//计算订单
		return processGetPrintStatResponse(orders);
	}

	@Override
	public ListPrintRecordsResponse listPrintRecords(ListPrintRecordsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintJobTypesResponse listPrintJobTypes(ListPrintJobTypesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintOrderStatusResponse listPrintOrderStatus(ListPrintOrderStatusCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintUserOrganizationsResponse listPrintUserOrganizations(ListPrintUserOrganizationsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePrintUserEmail(UpdatePrintUserEmailCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public GetPrintUserEmailResponse getPrintUserEmail(GetPrintUserEmailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPrintLogonUrlResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogonPrintResponse logonPrint(LogonPrintCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InformPrintResponse informPrint(InformPrintCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printImmediately(PrintImmediatelyCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PayPrintOrderResponse payPrintOrder(PayPrintOrderCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private PrintOwnerType checkOwner(String ownerType, Long ownerId) {
		// TODO Auto-generated method stub
		if(ownerId == null || StringUtils.isEmpty(ownerType)){
			Long userId = UserContext.current().getUser().getId();
			StringBuffer stringBuffer = new StringBuffer();
			LOGGER.error(stringBuffer.append("Invalid parameters, operatorId=").append(userId).append(",ownerType=")
					.append(ownerType).append(",ownerId=").append(ownerId).toString());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, ownerType = "+ ownerType +"; ownerId = "+ ownerId + ". ");
		}
		PrintOwnerType printOwnerType = PrintOwnerType.fromCode(ownerType);
		if(printOwnerType == null){
			Long userId = UserContext.current().getUser().getId();
			StringBuffer stringBuffer = new StringBuffer();
			LOGGER.error(stringBuffer.append("Invalid owner type, operatorId=").append(userId).append(", ownerType=").append(ownerType).toString());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, Unknown ownerType = "+ ownerType+". ");
		}
		return printOwnerType;
	}
	
	/**
	 * 产生打印设置 的返回对象
	 */
	private GetPrintSettingResponse processPrintSettingResponse(List<SiyinPrintSetting> printSettingList) {
		GetPrintSettingResponse response = getDefaultSettingResponse();
		for (SiyinPrintSetting siyinPrintSetting : printSettingList) {
			PrintSettingType settingtype = PrintSettingType.fromCode(siyinPrintSetting.getSettingType());
			if(settingtype == PrintSettingType.PRINT_COPY_SCAN){
				//设置价格
				setPrintCopyScanPrice(response,siyinPrintSetting);
			}
			else if(settingtype == PrintSettingType.COURSE_HOTLINE){
				//设置教程/热线
				response.setHotline(siyinPrintSetting.getHotline());
				response.setPrintCourseList(Arrays.asList(siyinPrintSetting.getPrintCourse().split("\\|")));
				response.setScanCopyCourseList(Arrays.asList(siyinPrintSetting.getScanCopyCourse().split("\\|")));
			}
		}
		return response;
	}

	/**
	 * 没有默认设置的情况，设置默认设置
	 */
	private GetPrintSettingResponse getDefaultSettingResponse() {
		GetPrintSettingResponse response = new GetPrintSettingResponse();
		BigDecimal defaultdecimal = new BigDecimal(0.1);
		response.setColorTypeDTO(new PrintSettingColorTypeDTO());
		response.getColorTypeDTO().setBlackWhitePrice(defaultdecimal);
		response.getColorTypeDTO().setBlackWhitePrice(defaultdecimal);
		
		response.setPaperSizePriceDTO(new PrintSettingPaperSizePriceDTO());
		
		response.getPaperSizePriceDTO().setAthreePrice(new PrintSettingColorTypeDTO());
		response.getPaperSizePriceDTO().getAthreePrice().setBlackWhitePrice(defaultdecimal);
		response.getPaperSizePriceDTO().getAthreePrice().setColorPrice(defaultdecimal);
		
		response.getPaperSizePriceDTO().setAfourPrice(new PrintSettingColorTypeDTO());
		response.getPaperSizePriceDTO().getAfourPrice().setBlackWhitePrice(defaultdecimal);
		response.getPaperSizePriceDTO().getAfourPrice().setColorPrice(defaultdecimal);
		
		response.getPaperSizePriceDTO().setAfivePrice(new PrintSettingColorTypeDTO());
		response.getPaperSizePriceDTO().getAfivePrice().setBlackWhitePrice(defaultdecimal);
		response.getPaperSizePriceDTO().getAfivePrice().setColorPrice(defaultdecimal);
		
		response.getPaperSizePriceDTO().setAsixPrice(new PrintSettingColorTypeDTO());
		response.getPaperSizePriceDTO().getAsixPrice().setBlackWhitePrice(defaultdecimal);
		response.getPaperSizePriceDTO().getAsixPrice().setColorPrice(defaultdecimal);
		
		response.setPrintCourseList(Arrays.asList(getLocalActivityString(PrintErrorCode.PRINT_COURSE_LIST).split("\\|")));
		response.setScanCopyCourseList(Arrays.asList(getLocalActivityString(PrintErrorCode.SCAN_COPY_COURSE_LIST).split("\\|")));
		return response;
	}
	
	 private String getLocalActivityString(String code){
		LocaleString localeString = localeStringProvider.find(PrintErrorCode.SCOPE, code, UserContext.current().getUser().getLocale());
		if (localeString != null) {
			return localeString.getText();
		}
		return "";
	 }


	/**
	 * 根据 打印机操作类型(打印/复印/扫描/)设置价格
	 */
	private void setPrintCopyScanPrice(GetPrintSettingResponse response, SiyinPrintSetting siyinPrintSetting) {
		PrintJobTypeType jobType = PrintJobTypeType.fromCode(siyinPrintSetting.getJobType());
		
		switch (jobType) {
		case PRINT:
			setPrintPaperSizePrice(response,siyinPrintSetting);
			break;
		case COPY:
			break;
		case SCAN:
			response.setColorTypeDTO(ConvertHelper.convert(siyinPrintSetting, PrintSettingColorTypeDTO.class));
			break;
	
		default:
			break;
		}
	}

	/**
	 * 根据纸张设置
	 */
	private void setPrintPaperSizePrice(GetPrintSettingResponse response, SiyinPrintSetting siyinPrintSetting) {
		PrintPaperSizeType paperSizeType = PrintPaperSizeType.fromCode(siyinPrintSetting.getPaperSize());
		
		PrintSettingPaperSizePriceDTO dto = response.getPaperSizePriceDTO();
		if(dto == null){
			response.setPaperSizePriceDTO(new PrintSettingPaperSizePriceDTO());
			dto = response.getPaperSizePriceDTO();
		}
		
		switch (paperSizeType) {
		case A3:
			response.getPaperSizePriceDTO().setAthreePrice(ConvertHelper.convert(siyinPrintSetting, PrintSettingColorTypeDTO.class));
			break;
		case A4:
			response.getPaperSizePriceDTO().setAfourPrice(ConvertHelper.convert(siyinPrintSetting, PrintSettingColorTypeDTO.class));
			break;
		case A5:
			response.getPaperSizePriceDTO().setAfivePrice(ConvertHelper.convert(siyinPrintSetting, PrintSettingColorTypeDTO.class));
			break;
		case A6:
			response.getPaperSizePriceDTO().setAsixPrice(ConvertHelper.convert(siyinPrintSetting, PrintSettingColorTypeDTO.class));
			break;
		default:
			break;
		}
	}
	
	private List<SiyinPrintSetting> checkUpdatePrintSettingCommand(UpdatePrintSettingCommand cmd) {
		// TODO Auto-generated method stub
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		List<SiyinPrintSetting> list = checkPaperSizePriceDTO(cmd.getPaperSizePriceDTO(),cmd.getOwnerType(),cmd.getOwnerId());
		list.add(checkColorTypeDTO(cmd.getColorTypeDTO(),cmd.getOwnerType(),cmd.getOwnerId()));
		list.add(checkCourseList(cmd.getScanCopyCourseList(),cmd.getPrintCourseList(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getHotline()));
		return list;
	}

	/**
	 * 检查打印/复印扫描教程并产生实体
	 */
	private SiyinPrintSetting checkCourseList(List<String> printCourseList,List<String> scancopyCourseList, String string, Long long1,String hotline) {
		if(printCourseList == null || printCourseList.size() == 0)
		{
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, printCourseList = " + printCourseList);
		}
		if(printCourseList.size() == 0 || scancopyCourseList.size() == 0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, scancopyCourseList = " + scancopyCourseList);
		}
		
		SiyinPrintSetting setting = new SiyinPrintSetting();
		setting.setScanCopyCourse("");
		setting.setPrintCourse("");
		setting.setOwnerType(string);
		setting.setOwnerId(long1);
		setting.setSettingType(PrintSettingType.COURSE_HOTLINE.getCode());
		setting.setNamespaceId(UserContext.getCurrentNamespaceId());
		for (String string2 : scancopyCourseList) {
			setting.setScanCopyCourse(setting.getScanCopyCourse() +string2+ "|");
		}
		
		for (String string2 : printCourseList) {
			setting.setPrintCourse(setting.getPrintCourse()+string2+"|");
		}
		
		setting.setHotline(hotline);
		return setting;
	}


	/**
	 * 检查打印扫描价格的DTO，并生成实体
	 */
	private List<SiyinPrintSetting> checkPaperSizePriceDTO(PrintSettingPaperSizePriceDTO paperSizePriceDTO, String string, Long long1) {
		// TODO Auto-generated method stub
		checkPrice(paperSizePriceDTO.getAthreePrice());
		checkPrice(paperSizePriceDTO.getAfourPrice());
		checkPrice(paperSizePriceDTO.getAfivePrice());
		checkPrice(paperSizePriceDTO.getAsixPrice());
		
		SiyinPrintSetting settinga3 = ConvertHelper.convert(paperSizePriceDTO.getAthreePrice(), SiyinPrintSetting.class);
		SiyinPrintSetting settinga4 = ConvertHelper.convert(paperSizePriceDTO.getAfourPrice(), SiyinPrintSetting.class);
		SiyinPrintSetting settinga5 = ConvertHelper.convert(paperSizePriceDTO.getAfivePrice(), SiyinPrintSetting.class);
		SiyinPrintSetting settinga6 = ConvertHelper.convert(paperSizePriceDTO.getAsixPrice(), SiyinPrintSetting.class);
		
		settinga3.setOwnerType(string);
		settinga3.setOwnerId(long1);
		settinga4.setOwnerType(string);
		settinga4.setOwnerId(long1);
		settinga5.setOwnerType(string);
		settinga5.setOwnerId(long1);
		settinga6.setOwnerType(string);
		settinga6.setOwnerId(long1);
		
		settinga3.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		settinga4.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		settinga5.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		settinga6.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		
		settinga3.setJobType(PrintJobTypeType.PRINT.getCode());
		settinga4.setJobType(PrintJobTypeType.PRINT.getCode());
		settinga5.setJobType(PrintJobTypeType.PRINT.getCode());
		settinga6.setJobType(PrintJobTypeType.PRINT.getCode());
		
		settinga3.setPaperSize(PrintPaperSizeType.A3.getCode());
		settinga4.setPaperSize(PrintPaperSizeType.A4.getCode());
		settinga5.setPaperSize(PrintPaperSizeType.A5.getCode());
		settinga6.setPaperSize(PrintPaperSizeType.A6.getCode());
		
		settinga3.setNamespaceId(UserContext.getCurrentNamespaceId());
		settinga4.setNamespaceId(UserContext.getCurrentNamespaceId());
		settinga5.setNamespaceId(UserContext.getCurrentNamespaceId());
		settinga6.setNamespaceId(UserContext.getCurrentNamespaceId());
		return new ArrayList<SiyinPrintSetting>(Arrays.asList(new SiyinPrintSetting[]{settinga3,settinga4,settinga5,settinga6}));
	}


	/**
	 * 检查扫描价格，并生成实体
	 */
	private SiyinPrintSetting checkColorTypeDTO(PrintSettingColorTypeDTO colorTypeDTO, String string, Long long1) {
		checkPrice(colorTypeDTO);
		SiyinPrintSetting setting = ConvertHelper.convert(colorTypeDTO, SiyinPrintSetting.class);
		setting.setOwnerType(string);
		setting.setOwnerId(long1);
		setting.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		setting.setJobType(PrintJobTypeType.SCAN.getCode());
		setting.setNamespaceId(UserContext.getCurrentNamespaceId());
		return setting;
	}
	
	/**
	 * 检查价格信息
	 */
	private void checkPrice(PrintSettingColorTypeDTO colorTypeDTO) {
		if(colorTypeDTO.getBlackWhitePrice().compareTo(BigDecimal.ZERO) < 0)
		{
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, blackWhitePrice = " + colorTypeDTO.getBlackWhitePrice());
		}
		if(colorTypeDTO.getColorPrice().compareTo(BigDecimal.ZERO) < 0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, colorPrice = " + colorTypeDTO.getBlackWhitePrice());
		}
	}
	/**
	 *计算订单统计值 
	 */
	private GetPrintStatResponse processGetPrintStatResponse(List<SiyinPrintOrder> orders) {
		GetPrintStatResponse response = new GetPrintStatResponse();
		
		PrintStatDTO printStat = new PrintStatDTO(new BigDecimal(0),new BigDecimal(0),new BigDecimal(0));
		PrintStatDTO copyStat = new PrintStatDTO(new BigDecimal(0),new BigDecimal(0),new BigDecimal(0));
		PrintStatDTO scanStat = new PrintStatDTO(new BigDecimal(0),new BigDecimal(0),new BigDecimal(0));
		PrintStatDTO allStat = new PrintStatDTO(new BigDecimal(0),new BigDecimal(0),new BigDecimal(0));
		
		for (SiyinPrintOrder siyinPrintOrder : orders) {
			PrintJobTypeType jobType = PrintJobTypeType.fromCode(siyinPrintOrder.getJobType());
			PrintOrderStatusType orderStatusType = PrintOrderStatusType.fromCode(siyinPrintOrder.getOrderStatus());
			//总体统计
			addOrderTotalAmountToStat(allStat,siyinPrintOrder.getOrderTotalAmount(),orderStatusType);
			
			switch (jobType) {
			case PRINT:
				addOrderTotalAmountToStat(printStat, siyinPrintOrder.getOrderTotalAmount(), orderStatusType);
				break;
			case COPY:
				addOrderTotalAmountToStat(copyStat, siyinPrintOrder.getOrderTotalAmount(), orderStatusType);
				break;
			case SCAN:
				addOrderTotalAmountToStat(scanStat, siyinPrintOrder.getOrderTotalAmount(), orderStatusType);
				break;

			default:
				break;
			}
		}
		
		response.setAllStat(allStat);
		response.setCopyStat(copyStat);
		response.setPrintStat(printStat);
		response.setScanStat(scanStat);
		return response;
	}


	/**
	 * 加上某个订单的值到统计对象
	 */
	private void addOrderTotalAmountToStat(PrintStatDTO allStat, BigDecimal orderTotalAmount, PrintOrderStatusType orderStatusType) {
		allStat.setAll(allStat.getAll().add(orderTotalAmount));
		if(orderStatusType == PrintOrderStatusType.PAID)
			allStat.setPaid(allStat.getPaid().add(orderTotalAmount));
		else if(orderStatusType == PrintOrderStatusType.UNPAID)
			allStat.setUnpaid(allStat.getUnpaid().add(orderTotalAmount));
	}


}
