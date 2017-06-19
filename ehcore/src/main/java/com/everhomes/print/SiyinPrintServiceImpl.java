// @formatter:off
package com.everhomes.print;

import java.math.BigDecimal;
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
import com.everhomes.locale.LocaleTemplateService;
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
import com.everhomes.rest.print.PrintOwnerType;
import com.everhomes.rest.print.PrintPaperSizeType;
import com.everhomes.rest.print.PrintSettingColorTypeDTO;
import com.everhomes.rest.print.PrintSettingPaperSizePriceDTO;
import com.everhomes.rest.print.PrintSettingType;
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

	@Override
	public GetPrintSettingResponse getPrintSetting(GetPrintSettingCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		
		List<SiyinPrintSetting> printSettingList = siyinPrintSettingProvider.listSiyinPrintSettingByOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		return processPrintSettingResponse(printSettingList);
	}


	@Override
	public void updatePrintSetting(UpdatePrintSettingCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public GetPrintStatResponse getPrintStat(GetPrintStatCommand cmd) {
		// TODO Auto-generated method stub
		return null;
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
	
	private void checkOwner(String ownerType, Long ownerId) {
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
			else if(settingtype == PrintSettingType.PRINT_COPY_SCAN){
				//设置教程/热线
				response.setHotline(siyinPrintSetting.getHotline());
				response.setPrintCourseList(Arrays.asList(siyinPrintSetting.getPrintCourse().split("|")));
				response.setScanCopyCourseList(Arrays.asList(siyinPrintSetting.getScanCopyCourse().split("|")));
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
		
		response.setPrintCourseList(Arrays.asList(getLocalActivityString(PrintErrorCode.PRINT_COURSE_LIST).split("|")));
		response.setScanCopyCourseList(Arrays.asList(getLocalActivityString(PrintErrorCode.SCAN_COPY_COURSE_LIST).split("|")));
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
}
