package com.everhomes.techpark.rental;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import net.greghaines.jesque.Job;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.techpark.rental.AddItemAdminCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommand;
import com.everhomes.rest.techpark.rental.AddRentalBillItemCommandResponse;
import com.everhomes.rest.techpark.rental.AddRentalSiteCommand;
import com.everhomes.rest.techpark.rental.AddRentalSiteSingleSimpleRule;
import com.everhomes.rest.techpark.rental.AmorpmFlag;
import com.everhomes.rest.techpark.rental.AttachmentDTO;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommand;
import com.everhomes.rest.techpark.rental.BatchCompleteBillCommandResponse;
import com.everhomes.rest.techpark.rental.BatchIncompleteBillCommand;
import com.everhomes.rest.techpark.rental.BillAttachmentDTO;
import com.everhomes.rest.techpark.rental.BillQueryStatus;
import com.everhomes.rest.techpark.rental.CancelRentalBillCommand;
import com.everhomes.rest.techpark.rental.CompleteBillCommand;
import com.everhomes.rest.techpark.rental.ConfirmBillCommand;
import com.everhomes.rest.techpark.rental.DateLength;
import com.everhomes.rest.techpark.rental.DeleteItemAdminCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalBillCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteCommand;
import com.everhomes.rest.techpark.rental.DeleteRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.DisableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.EnableRentalSiteCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteDayStatusResponse;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindAutoAssignRentalSiteWeekStatusResponse;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommand;
import com.everhomes.rest.techpark.rental.FindRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsAndAttachmentsCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteItemsAndAttachmentsResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteRulesCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommand;
import com.everhomes.rest.techpark.rental.FindRentalSiteWeekStatusCommandResponse;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommand;
import com.everhomes.rest.techpark.rental.FindRentalSitesCommandResponse;
import com.everhomes.rest.techpark.rental.GetItemListAdminCommand;
import com.everhomes.rest.techpark.rental.GetItemListCommandResponse;
import com.everhomes.rest.techpark.rental.GetRentalSiteTypeResponse;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommand;
import com.everhomes.rest.techpark.rental.GetRentalTypeRuleCommandResponse;
import com.everhomes.rest.techpark.rental.IncompleteBillCommand;
import com.everhomes.rest.techpark.rental.InvoiceFlag;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillCountCommandResponse;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommand;
import com.everhomes.rest.techpark.rental.ListRentalBillsCommandResponse;
import com.everhomes.rest.techpark.rental.LoopType;
import com.everhomes.rest.techpark.rental.NormalFlag;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommand;
import com.everhomes.rest.techpark.rental.OnlinePayCallbackCommandResponse;
import com.everhomes.rest.techpark.rental.PayZuolinRefundCommand;
import com.everhomes.rest.techpark.rental.PayZuolinRefundResponse;
import com.everhomes.rest.techpark.rental.RentalBillCountDTO;
import com.everhomes.rest.techpark.rental.RentalBillDTO;
import com.everhomes.rest.techpark.rental.RentalItemType;
import com.everhomes.rest.techpark.rental.RentalOwnerType;
import com.everhomes.rest.techpark.rental.RentalServiceErrorCode;
import com.everhomes.rest.techpark.rental.RentalSiteDTO;
import com.everhomes.rest.techpark.rental.RentalSiteDayRulesDTO;
import com.everhomes.rest.techpark.rental.RentalSiteNumberDayRulesDTO;
import com.everhomes.rest.techpark.rental.RentalSiteNumberRuleDTO;
import com.everhomes.rest.techpark.rental.RentalSitePicDTO;
import com.everhomes.rest.techpark.rental.RentalSiteRulesDTO;
import com.everhomes.rest.techpark.rental.RentalSiteStatus;
import com.everhomes.rest.techpark.rental.RentalType;
import com.everhomes.rest.techpark.rental.SiteBillStatus;
import com.everhomes.rest.techpark.rental.SiteItemDTO;
import com.everhomes.rest.techpark.rental.SiteRuleStatus;
import com.everhomes.rest.techpark.rental.UpdateItemAdminCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalRuleCommand;
import com.everhomes.rest.techpark.rental.UpdateRentalSiteCommand;
import com.everhomes.rest.techpark.rental.VisibleFlag;
import com.everhomes.rest.techpark.rental.rentalBillRuleDTO;
import com.everhomes.rest.techpark.rental.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO;
import com.everhomes.rest.techpark.rental.admin.AttachmentType;
import com.everhomes.rest.techpark.rental.admin.DiscountType;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListCommand;
import com.everhomes.rest.techpark.rental.admin.GetRefundOrderListResponse;
import com.everhomes.rest.techpark.rental.admin.GetRefundUrlCommand;
import com.everhomes.rest.techpark.rental.admin.GetRentalBillCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminResponse;
import com.everhomes.rest.techpark.rental.admin.GetResourceTypeListCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceTypeListResponse;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.techpark.rental.admin.RefundOrderDTO;
import com.everhomes.rest.techpark.rental.admin.SiteOwnerDTO;
import com.everhomes.rest.techpark.rental.admin.TimeIntervalDTO;
import com.everhomes.rest.techpark.rental.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateItemsAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteDiscountAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateRentalSiteRulesAdminCommand;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceAdminCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.tables.EhRentalSites;
import com.everhomes.server.schema.tables.pojos.EhRentalDefaultRules;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class RentalServiceImpl implements RentalService {
	final String downloadDir ="\\download\\";

	// N分钟后取消
	private Long cancelTime = 5 * 60 * 1000L;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RentalServiceImpl.class);
	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String queueName = "rentalService";
	
	@Autowired
	ContentServerService contentServerService;
    @Autowired
    WorkerPoolFactory workerPoolFactory;
    
    @PostConstruct
    public void setup() {
        workerPoolFactory.getWorkerPool().addQueue(queueName);
    }

	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private OnlinePayService onlinePayService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	JesqueClientFactory jesqueClientFactory;
	@Autowired
	private CoordinationProvider coordinationProvider; 
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	RentalProvider rentalProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private AppProvider appProvider;
	
	
	private int getPageCount(int totalCount, int pageSize) {
		int pageCount = totalCount / pageSize;

		if (totalCount % pageSize != 0) {
			pageCount++;
		}
		return pageCount;
	}

	private void checkEnterpriseCommunityIdIsNull(Long enterpriseCommunityId) {
		if (null == enterpriseCommunityId || enterpriseCommunityId.equals(0L)) {
			LOGGER.error("Invalid enterpriseCommunityId   parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ownerId   parameter in the command");
		}

	}
	
	
	public Object restCall(String api, Object command, Class<?> responseType) {
		String host = this.configurationProvider.getValue("pay.zuolin.host", "https://pay.zuolin.com");
		return restCall(api, command, responseType, host);
	}
	public Object restCall(String api, Object o, Class<?> responseType,String host) {
		AsyncRestTemplate template = new AsyncRestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter(Charset
				.forName("UTF-8")));
		template.setMessageConverters(messageConverters);
		String[] apis = api.split(" ");
		String method = apis[0];

		String url = host
				+ api.substring(method.length() + 1, api.length()).trim();

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		HttpEntity<String> requestEntity = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Host", host); 
		headers.add("charset", "UTF-8");

		ListenableFuture<ResponseEntity<String>> future = null;

		if (method.equalsIgnoreCase("POST")) {
			requestEntity = new HttpEntity<>(StringHelper.toJsonString(o),
					headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
		} else {
			Map<String, String> params = new HashMap<String, String>();
			StringHelper.toStringMap("", o, params);
			LOGGER.debug("params is :" + params.toString());

			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramMap.add(entry.getKey().substring(1),
						URLEncoder.encode(entry.getValue()));
			}

			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramMap)
					.build().toUriString();
			requestEntity = new HttpEntity<>(null, headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.GET, requestEntity,
					String.class);
		}

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = future.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.info("restCall error " + e.getMessage());
			return null;
		}

		if (responseEntity != null
				&& responseEntity.getStatusCode() == HttpStatus.OK) {

			// String bodyString = new
			// String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8")
			// ;
			String bodyString = responseEntity.getBody();
			LOGGER.debug(bodyString);
			LOGGER.debug("HEADER" + responseEntity.getHeaders());
//			return bodyString;
			return StringHelper.fromJsonString(bodyString, responseType);

		}

		LOGGER.info("restCall error " + responseEntity.getStatusCode());
		return null;

	}
	
	@Override
	public void addDefaultRule( AddDefaultRuleAdminCommand cmd){
		this.dbProvider.execute((TransactionStatus status) -> {
			//default rule
			RentalDefaultRule defaultRule = ConvertHelper.convert(cmd, RentalDefaultRule.class); 
			if(null==defaultRule.getCancelFlag())
				defaultRule.setCancelFlag(NormalFlag.NEED.getCode());
			if(null==cmd.getOpenWeekday()){
				defaultRule.setOpenWeekday("0000000");
			}else{
				int openWorkdayInt=0;
				for(Integer weekdayInteger : cmd.getOpenWeekday())
					openWorkdayInt+=10^weekdayInteger;
				String openWorkday=String.valueOf(openWorkdayInt);
				for( ;openWorkday.length()<=7; ){
					openWorkday ="0"+openWorkday;
				}
			}
			defaultRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			defaultRule.setCreatorUid(UserContext.current().getUser().getId());
			this.rentalProvider.createRentalDefaultRule(defaultRule);
			//time intervals
			if(cmd.getRentalType().equals(RentalType.HOUR.getCode())&& null!=cmd.getTimeIntervals())
				for(TimeIntervalDTO intervalDTO : cmd.getTimeIntervals()){
					RentalTimeInterval timeInterval = ConvertHelper.convert(intervalDTO, RentalTimeInterval.class);
					timeInterval.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					timeInterval.setOwnerId(defaultRule.getId());
					this.rentalProvider.createTimeInterval(timeInterval);
				}
			//close dates
			if(null!=cmd.getCloseDates())
				for(Long closedate:cmd.getCloseDates()){
					RentalCloseDate rcd=new RentalCloseDate();
					rcd.setCloseDate(new Date(closedate));
					rcd.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					rcd.setOwnerId(defaultRule.getId());
					this.rentalProvider.createRentalCloseDate(rcd);
				}
			//config attachments
			if(null!=cmd.getAttachments())
				for(com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO attachmentDTO:cmd.getAttachments()){
					RentalConfigAttachment rca =ConvertHelper.convert(attachmentDTO, RentalConfigAttachment.class);
					rca.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					rca.setOwnerId(defaultRule.getId());
					this.rentalProvider.createRentalConfigAttachment(rca);
				}
			return null;
		});
		
		
	}
	@Override
	public QueryDefaultRuleAdminResponse queryDefaultRule(QueryDefaultRuleAdminCommand cmd){
		RentalDefaultRule defaultRule = this.rentalProvider.getRentalDefaultRule(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getResourceTypeId());
		QueryDefaultRuleAdminResponse response = null;
		if(null==defaultRule){
			AddDefaultRuleAdminCommand addCmd = new AddDefaultRuleAdminCommand();
	        addCmd.setOwnerType(cmd.getOwnerType());
	        addCmd.setOwnerId(cmd.getOwnerId());
	        addCmd.setResourceTypeId(cmd.getResourceTypeId());
	        addCmd.setExclusiveFlag(NormalFlag.NONEED.getCode());
	        addCmd.setUnit(0.5);
	        addCmd.setAutoAssign(NormalFlag.NEED.getCode());
	        addCmd.setMultiUnit(NormalFlag.NEED.getCode());
	        addCmd.setNeedPay(NormalFlag.NEED.getCode());
	        addCmd.setMultiTimeInterval(NormalFlag.NEED.getCode());
	        addCmd.setAttachments(new ArrayList<AttachmentConfigDTO>());
	        AttachmentConfigDTO attachement = new AttachmentConfigDTO();
	        attachement.setAttachmentType(AttachmentType.ATTACHMENT.getCode());
	        attachement.setMustOptions(NormalFlag.NONEED.getCode());
	        addCmd.getAttachments().add(attachement);
	        addCmd.setRentalType(RentalType.DAY.getCode());
	        addCmd.setRentalEndTime(1000*60*60L);
	        addCmd.setRentalStartTime(1000*60*60*24*30L); 			addCmd.setTimeStep(1.0);
//	        cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
	        addCmd.setBeginDate(new java.util.Date().getTime());
	        //当前时间+100天
	        addCmd.setEndDate(new java.util.Date().getTime()+1000*60*60*24*100L);
	        addCmd.setOpenWeekday(new ArrayList<Integer>());
	        addCmd.getOpenWeekday().add(1);
	        addCmd.getOpenWeekday().add(2);
	        addCmd.getOpenWeekday().add(3);
	        addCmd.getOpenWeekday().add(4);
	        addCmd.setCloseDates(null);
	        addCmd.setWorkdayPrice(new BigDecimal(100));
	        addCmd.setWeekendPrice(new BigDecimal(200));
	        addCmd.setSiteCounts(10.0);
	        addCmd.setCancelTime(0L);
	        addCmd.setRefundFlag(NormalFlag.NEED.getCode());
	        addCmd.setRefundRatio(30);
	        this.addDefaultRule(addCmd);

			response = ConvertHelper.convert(addCmd, QueryDefaultRuleAdminResponse.class);
			return response;
		}
		else{
			response = ConvertHelper.convert(defaultRule, QueryDefaultRuleAdminResponse.class);
		}
		List<RentalTimeInterval> timeIntervals = this.rentalProvider.queryRentalTimeIntervalByOwner(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
		if(null!=timeIntervals){
			response.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
			for(RentalTimeInterval timeInterval:timeIntervals){
				response.getTimeIntervals().add(ConvertHelper.convert(timeInterval, TimeIntervalDTO.class));
			}
		}
		List<RentalCloseDate> closeDates=this.rentalProvider.queryRentalCloseDateByOwner(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
		if(null!=closeDates){
			response.setCloseDates(new ArrayList<Long>());
			for(RentalCloseDate single:closeDates){
				try{
					response.getCloseDates().add(single.getCloseDate().getTime());
				}catch(java.lang.NullPointerException e){
					LOGGER.error("why java null point close Date is : ["+single.getCloseDate()+"] response is : "+response.toString());
				}
			}
		}
		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
		if(null!=attachments){
			response.setAttachments(new ArrayList<com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				response.getAttachments().add(ConvertHelper.convert(single, com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO .class));
			}
		}
		return response;
	}

	@Override
	public void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd) { 
		RentalDefaultRule defaultRule = this.rentalProvider.getRentalDefaultRule(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getResourceTypeId());
		if(null==defaultRule){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOTFOUND, "didnt have default rule!");
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalDefaultRule newDefaultRule = ConvertHelper.convert(cmd, RentalDefaultRule.class); 
			if(null==defaultRule.getCancelFlag())
				defaultRule.setCancelFlag(NormalFlag.NEED.getCode());
			if(null==cmd.getOpenWeekday()){
				defaultRule.setOpenWeekday("0000000");
			}else{
				int openWorkdayInt=0;
				for(Integer weekdayInteger : cmd.getOpenWeekday())
					openWorkdayInt+=10^weekdayInteger;
				String openWorkday=String.valueOf(openWorkdayInt);
				for( ;openWorkday.length()<=7; ){
					openWorkday ="0"+openWorkday;
				}
			}
			
			newDefaultRule.setId(defaultRule.getId());
			this.rentalProvider.updateRentalDefaultRule(newDefaultRule);
			this.rentalProvider.deleteTimeIntervalsByOwnerId(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
			this.rentalProvider.deleteRentalCloseDatesByOwnerId(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
			this.rentalProvider.deleteRentalConfigAttachmentsByOwnerId(EhRentalDefaultRules.class.getSimpleName(),defaultRule.getId());
			//time intervals
			if(cmd.getRentalType().equals(RentalType.HOUR.getCode())&& null!=cmd.getTimeIntervals())
				for(TimeIntervalDTO intervalDTO : cmd.getTimeIntervals()){
					RentalTimeInterval timeInterval = ConvertHelper.convert(intervalDTO, RentalTimeInterval.class);
					timeInterval.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					timeInterval.setOwnerId(defaultRule.getId());
					this.rentalProvider.createTimeInterval(timeInterval);
				}
			//close dates
			if(null!=cmd.getCloseDates())
				for(Long closedate:cmd.getCloseDates()){
					RentalCloseDate rcd=new RentalCloseDate();
					rcd.setCloseDate(new Date(closedate));
					rcd.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					rcd.setOwnerId(defaultRule.getId());
					this.rentalProvider.createRentalCloseDate(rcd);
				}
			//config attachments
			if(null!=cmd.getAttachments())
				for(com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO attachmentDTO:cmd.getAttachments()){
					RentalConfigAttachment rca =ConvertHelper.convert(attachmentDTO, RentalConfigAttachment.class);
					rca.setOwnerType(EhRentalDefaultRules.class.getSimpleName());
					rca.setOwnerId(defaultRule.getId());
					this.rentalProvider.createRentalConfigAttachment(rca);
				}
			return null;
		});
	}
 
	
	
	@Override
	public void updateRentalRule(UpdateRentalRuleCommand cmd) {
		
		Long userId = UserContext.current().getUser().getId();
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		RentalRule rentalRuleExist = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		if(null == cmd.getRefundFlag()){
			cmd.setRefundFlag((byte)1);
		}
		if(rentalRuleExist != null) {
//		rentalRule.setOwnerId(cmd.getOwnerId());
//		rentalRule.setOwnerType(cmd.getOwnerType());
//		rentalRule.setContactNum(cmd.getContactNum());
//		rentalRule.setPayEndTime(cmd.getPayEndTime());
//		rentalRule.setPaymentRatio(cmd.getPayRatio());
//		rentalRule.setPayStartTime(cmd.getPayStartTime());
//		rentalRule.setRefundFlag(cmd.getRefundFlag());
//		rentalRule.setRentalType(cmd.getRentalType());
//		rentalRule.setOvertimeTime(cmd.getOvertimeTime());
//		rentalRule.setSiteType(cmd.getSiteType());
//		rentalRule.setRentalEndTime(cmd.getRentalEndTime());
//		rentalRule.setRentalStartTime(cmd.getRentalStartTime());
//		rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
//				.getTime()));
//		rentalRule.setContactAddress(cmd.getContactAddress());
//		rentalRule.setContactName(cmd.getContactName());
//		rentalRule.setOperatorUid(userId);
			RentalRule rentalRule =ConvertHelper.convert(cmd,RentalRule.class  );
			rentalRule.setPaymentRatio(cmd.getPayRatio());
			rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rentalRule.setOperatorUid(userId);
			rentalProvider.updateRentalRule(rentalRule);
		} else {
			RentalRule rentalRule =ConvertHelper.convert(cmd,RentalRule.class  );
			rentalRule.setPaymentRatio(cmd.getPayRatio());
			rentalRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rentalRule.setOperatorUid(userId);
			rentalProvider.createRentalRule(rentalRule);
		}
	}

	@Override
	public Long addRentalSite(AddRentalSiteCommand cmd) {
		RentalSite rentalsite = ConvertHelper.convert(cmd, RentalSite.class);
		rentalsite.setStatus(RentalSiteStatus.NORMAL.getCode());
		rentalsite.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		rentalsite.setCreatorUid( UserContext.current().getUser().getId());
		Long siteId = rentalProvider.createRentalSite(rentalsite);
		if (null != cmd.getSiteItems()
				&& !StringUtils.isEmpty(cmd.getSiteItems())) {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd
					.getSiteItems());
			JSONArray itemValue = (JSONArray) jsonObject.get("siteItems");
			Gson gson = new Gson();
			List<SiteItemDTO> siteItemDTOs = gson.fromJson(
					itemValue.toString(), new TypeToken<List<SiteItemDTO>>() {
					}.getType());
			if(null!=siteItemDTOs)
				for (SiteItemDTO siteItemDTO : siteItemDTOs) {
					RentalSiteItem siteItem =  ConvertHelper.convert(siteItemDTO,RentalSiteItem.class );
					siteItem.setName(siteItemDTO.getItemName());
					siteItem.setPrice(siteItemDTO.getItemPrice());
					rentalProvider.createRentalSiteItem(siteItem);
				}
		}
		return siteId;
	}

	@Override
	public void addItem(AddItemAdminCommand cmd) {
		RentalSiteItem siteItem = ConvertHelper.convert(cmd,RentalSiteItem.class );
		siteItem.setName(cmd.getItemName());
		siteItem.setPrice(cmd.getItemPrice());
		rentalProvider.createRentalSiteItem(siteItem);
	}
//
//	/**
//	 * @param cmd
//	 */
//	@Override
//	public void addRentalSiteRules(AddRentalSiteRulesCommand cmd) {
//		RentalSiteRule rsr = new RentalSiteRule();
//		Calendar start = Calendar.getInstance();
//		Calendar end = Calendar.getInstance();
//		start.setTime(new Date(cmd.getBeginDate()));
//		end.setTime(new Date(cmd.getEndDate()));
//		JSONObject jsonObject = (JSONObject) JSONValue.parse(cmd.getChoosen());
//		JSONArray choosenValue = (JSONArray) jsonObject.get("choosen");
//		Gson gson = new Gson();
//		List<Integer> choosenInts = gson.fromJson(choosenValue.toString(),
//				new TypeToken<List<Integer>>() {
//				}.getType());
//		// String[] arr = cmd.getChoosen().split(",");
//		// List<String> list = new ArrayList<String>(arr);
//		while (start.before(end)) {
//			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
//			if (choosenInts.contains(weekday)) {
//				for (double i = cmd.getBeginTime(); i < cmd.getEndTime();) {
//					rsr.setBeginTime(Timestamp.valueOf(dateSF.format(start
//							.getTime())
//							+ " "
//							+ String.valueOf((int) i / 1)
//							+ ":"
//							+ String.valueOf((int) ((i % 1) * 60))
//							+ ":00"));
//
//					i = i + cmd.getTimeStep();
//					rsr.setEndTime(Timestamp.valueOf(dateSF.format(start
//							.getTime())
//							+ " "
//							+ String.valueOf((int) i / 1)
//							+ ":"
//							+ String.valueOf((int) ((i % 1) * 60))
//							+ ":00"));
//					rsr.setRentalSiteId(cmd.getRentalSiteId());
//					rsr.setRentalType(cmd.getRentalType());
//					rsr.setCounts(cmd.getCounts());
//					rsr.setUnit(cmd.getUnit());
//					rsr.setPrice(cmd.getPrice());
//					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
//							.getTime())));
//					rsr.setStatus(cmd.getStatus());
//					rentalProvider.createRentalSiteRule(rsr);
//
//				}
//			}
//			start.add(Calendar.DAY_OF_MONTH, 1);
//		}
//	}
//
//	@Override
//	public FindRentalSitesStatusCommandResponse findRentalSiteDayStatus(
//			FindRentalSitesStatusCommand cmd) {
//
//		if(null!=cmd.getCommunityId()){
//			cmd.setOwnerId(cmd.getCommunityId());
//			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
//		}
//		java.util.Date reserveTime = new java.util.Date();
//		FindRentalSitesStatusCommandResponse response = new FindRentalSitesStatusCommandResponse();
//		response.setSites(new ArrayList<RentalSiteDTO>());
//		RentalRule rentalRule = rentalProvider.getRentalRule(
//				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
//		response.setContactNum(rentalRule.getContactNum());
//		// 查sites
//		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
//				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType(), null, null, null,null);
//		for (RentalSite rs : rentalSites) {
//			RentalSiteDTO rsDTO = new RentalSiteDTO();
//			rsDTO.setBuildingName(rs.getBuildingName());
//			rsDTO.setSiteName(rs.getSiteName());
//			rsDTO.setContactName(rs.getContactName());
//			rsDTO.setCompanyName(rs.getOwnCompanyName());
//			rsDTO.setSpec(rs.getSpec());
//			rsDTO.setAddress(rs.getAddress());
//			rsDTO.setContactPhonenum(rs.getContactPhonenum());
//			rsDTO.setOwnerId(cmd.getOwnerId());
//			rsDTO.setOwnerType(cmd.getOwnerType());
//			rsDTO.setRentalSiteId(rs.getId());
//			rsDTO.setSiteRules(new ArrayList<RentalSiteRulesDTO>());
//			rsDTO.setSiteItems(new ArrayList<SiteItemDTO>());
//			// 查rules
//
//			java.util.Date nowTime = new java.util.Date();
//
//			Timestamp beginTime = new Timestamp(nowTime.getTime()
//					+ rentalRule.getRentalStartTime());
//			List<RentalSiteRule> rentalSiteRules = rentalProvider
//					.findRentalSiteRules(rsDTO.getRentalSiteId(), dateSF
//							.format(new java.util.Date(cmd.getRuleDate())),
//							beginTime, cmd.getRentalType() ,
//							cmd.getRentalType().equals(RentalType.DAY)?DateLength.DAY.getCode():DateLength.MONTH.getCode());
//			// 查sitebills
//			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
//				for (RentalSiteRule rsr : rentalSiteRules) {
//					RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
//					dto.setId(rsr.getId());
//					dto.setRentalSiteId(rsr.getRentalSiteId());
//					dto.setRentalType(rsr.getRentalType());
//					dto.setRentalStep(rsr.getRentalStep());
//					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
//						dto.setTimeStep(rsr.getTimeStep());
//						dto.setBeginTime(rsr.getBeginTime().getTime());
//						dto.setEndTime(rsr.getEndTime().getTime());
//					} else if (dto.getRentalType().equals(
//							RentalType.HALFDAY.getCode())) {
//						dto.setAmorpm(rsr.getAmorpm());
//					}
//					dto.setUnit(rsr.getUnit());
//					dto.setPrice(rsr.getPrice());
//					dto.setRuleDate(rsr.getSiteRentalDate().getTime());
//					List<RentalSitesBill> rsbs = rentalProvider
//							.findRentalSiteBillBySiteRuleId(rsr.getId());
//					dto.setStatus(SiteRuleStatus.OPEN.getCode());
//					dto.setCounts((double) rsr.getCounts());
//					if (null != rsbs && rsbs.size() > 0) {
//						for (RentalSitesBill rsb : rsbs) {
//							dto.setCounts(dto.getCounts()
//									- rsb.getRentalCount());
//						}
//					}
//					if (dto.getCounts() == 0) {
//						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
//					}
//					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
//						if (reserveTime.before(new java.util.Date(rsr
//								.getBeginTime().getTime()
//								- rentalRule.getRentalStartTime()))) {
//							dto.setStatus(SiteRuleStatus.EARLY.getCode());
//						}
//						if (reserveTime.after(new java.util.Date(rsr
//								.getBeginTime().getTime()
//								- rentalRule.getRentalEndTime()))) {
//							dto.setStatus(SiteRuleStatus.LATE.getCode());
//						}
//					} else {
//						if (reserveTime.before(new java.util.Date(rsr
//								.getSiteRentalDate().getTime()
//								- rentalRule.getRentalStartTime()))) {
//							dto.setStatus(SiteRuleStatus.EARLY.getCode());
//						}
//						if (reserveTime.after(new java.util.Date(rsr
//								.getSiteRentalDate().getTime()
//								- rentalRule.getRentalEndTime()))) {
//							dto.setStatus(SiteRuleStatus.LATE.getCode());
//						}
//					}
//					rsDTO.getSiteRules().add(dto);
//
//				}
//			}
//			response.getSites().add(rsDTO);
//		}
//
//		return response;
//	}

	@Override
	public GetRentalSiteTypeResponse findRentalSiteTypes() {

		GetRentalSiteTypeResponse response = new GetRentalSiteTypeResponse();
		response.setSiteTypes(rentalProvider.findRentalSiteTypes());
		return response;
	}

	@Override
	public FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(
			FindRentalSiteItemsAndAttachmentsCommand cmd) {
		if(null==cmd.getRentalSiteId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter site id can not be null");
		if(null==cmd.getRentalSiteRuleIds() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter rule ids can not be null");
		
		FindRentalSiteItemsAndAttachmentsResponse response = new FindRentalSiteItemsAndAttachmentsResponse();
		response.setSiteItems(new ArrayList<SiteItemDTO>());
		List<RentalSiteItem> rsiSiteItems = rentalProvider.findRentalSiteItems(cmd.getRentalSiteId());
		if(rsiSiteItems!=null && rsiSiteItems.size()>0)
			for (RentalSiteItem rsi : rsiSiteItems) {
				SiteItemDTO dto = convertItem2DTO(rsi);
				//对于租赁型的要计算当前时段该场所已经租赁的物品（购买型记录的库存不用计算）
				if(rsi.getItemType().equals(RentalItemType.RENTAL.getCode())){
					int maxOrder = 0;
					for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
						// 对于每一个物品，通过每一个siteRuleID找到它对应的BillIds
						int ruleOrderSum = 0;
						List<RentalSitesBill> rsbs = rentalProvider
								.findRentalSiteBillBySiteRuleId(siteRuleId);
						// 通过每一个billID找已预订的数量
						if (null == rsbs || rsbs.size() == 0) {
							continue;
						}
						for (RentalSitesBill rsb : rsbs) {
							RentalItemsBill rib = rentalProvider.findRentalItemBill(
									rsb.getRentalBillId(), rsi.getId());
							if (null == rib || null == rib.getRentalCount())
								continue;
							ruleOrderSum += rib.getRentalCount();
						}
						// 获取该物品的最大预订量
						if (ruleOrderSum > maxOrder)
							maxOrder = ruleOrderSum;
					} 
					
					
					dto.setCounts(dto.getCounts() - maxOrder); 
				} 
				
				response.getSiteItems().add(dto);
			}
		
		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalSites.class.getSimpleName(),cmd.getRentalSiteId());
		if(null!=attachments){
			response.setAttachments(new ArrayList<AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				response.getAttachments().add(ConvertHelper.convert(single, AttachmentConfigDTO .class));
			}
		}
		
		return response;
	}

	@Override
	public FindRentalSitesCommandResponse findRentalSites(
			FindRentalSitesCommand cmd) {
		if(null==cmd.getResourceTypeId()||null==cmd.getOwnerId()||null==cmd.getOwnerType())

			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter ResourceTypeId OwnerId OwnerType cant be null");
		FindRentalSitesCommandResponse response = new FindRentalSitesCommandResponse();

		if(cmd.getAnchor() == null)
			cmd.setAnchor(Long.MAX_VALUE); 
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());
		if(null==cmd.getStatus() || cmd.getStatus().size() == 0){
			cmd.setStatus(new ArrayList<Byte>());
			cmd.getStatus().add(RentalSiteStatus.NORMAL.getCode());
		}
		List<RentalSiteOwner> siteOwners = this.rentalProvider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getOwnerType(), cmd.getOwnerId());
		List<Long> siteIds = new ArrayList<>();
		if(siteOwners !=null)
			for(RentalSiteOwner siteOwner : siteOwners){
				siteIds.add(siteOwner.getRentalSiteId());
			}  
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getResourceTypeId(), cmd.getKeyword(),
				locator, pageSize,cmd.getStatus(),siteIds);
		if(null==rentalSites)
			return response;

		Long nextPageAnchor = null;
		if(rentalSites != null && rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(new ArrayList<RentalSiteDTO>());
		
		for (RentalSite rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO =convertRentalSite2DTO(rentalSite);
			
			response.getRentalSites().add(rSiteDTO);
		}
 

		return response;
	}
	private RentalSiteDTO convertRentalSite2DTO(RentalSite rentalSite){
		RentalSiteDTO rSiteDTO =ConvertHelper.convert(rentalSite, RentalSiteDTO.class);
		rSiteDTO.setRentalSiteId(rentalSite.getId());
		rSiteDTO.setCreateTime(rentalSite.getCreateTime().getTime()); 
		rSiteDTO.setCoverUrl(this.contentServerService.parserUri(rSiteDTO.getCoverUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		List<RentalSitePic> pics = this.rentalProvider.findRentalSitePicsByOwnerTypeAndId(EhRentalSites.class.getSimpleName(), rentalSite.getId());
		if(null!=pics){
			rSiteDTO.setSitePics(new ArrayList<>());
			for(RentalSitePic pic:pics){
				RentalSitePicDTO picDTO=ConvertHelper.convert(pic, RentalSitePicDTO.class);
				picDTO.setUrl(this.contentServerService.parserUri(pic.getUri(), 
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				rSiteDTO.getSitePics().add(picDTO);
			}
		}
		List<RentalSiteOwner> owners = this.rentalProvider.findRentalSiteOwnersBySiteId(rentalSite.getId());
		if(null!=owners){

			rSiteDTO.setOwners(new ArrayList<SiteOwnerDTO>());
			for(RentalSiteOwner owner : owners){
				SiteOwnerDTO dto = ConvertHelper.convert(owner, SiteOwnerDTO.class);
				rSiteDTO.getOwners().add(dto);
			}
		} 
		List<RentalSiteItem> items = rentalProvider.findRentalSiteItems(rentalSite.getId());
		if (null!=items){
			rSiteDTO.setSiteItems(new ArrayList<SiteItemDTO>());
			for (RentalSiteItem item : items) {
				SiteItemDTO siteItemDTO =convertItem2DTO(item);
				rSiteDTO.getSiteItems().add(siteItemDTO);
			}
		}


		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalSites.class.getSimpleName(),rentalSite.getId());
		if(null!=attachments){
			rSiteDTO.setAttachments(new ArrayList<AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				rSiteDTO.getAttachments().add(ConvertHelper.convert(single, AttachmentConfigDTO .class));
			}
		}
		return rSiteDTO;
	}
	public SiteItemDTO convertItem2DTO(RentalSiteItem item ){
		SiteItemDTO siteItemDTO = ConvertHelper.convert(item, SiteItemDTO.class); 
		if(item.getItemType().equals(RentalItemType.SALE.getCode())){
			//售卖型的要计算售卖数量su
			Integer sumInteger = this.rentalProvider.countRentalSiteItemSoldCount(item.getId()); 
			siteItemDTO.setSoldCount(sumInteger);
			siteItemDTO.setCounts(item.getCounts()-sumInteger);
		} 
		siteItemDTO.setItemName(item.getName());
		siteItemDTO.setItemPrice(item.getPrice());
		siteItemDTO.setImgUrl(this.contentServerService.parserUri(siteItemDTO.getImgUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		return siteItemDTO;
	}
	
	@Override
	public FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd) {
		FindRentalSiteRulesCommandResponse response = new FindRentalSiteRulesCommandResponse();
		response.setRentalSiteRules(new ArrayList<RentalSiteRulesDTO>());

		List<RentalSiteRule> rentalSiteRules = rentalProvider
				.findRentalSiteRules(cmd.getRentalSiteId(), null, null,
						cmd.getRentalType() ,
						cmd.getRentalType().equals(RentalType.DAY)?DateLength.DAY.getCode():DateLength.MONTH.getCode(),null);
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			for (RentalSiteRule rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
				dto.setId(rsr.getId());
				dto.setRentalSiteId(rsr.getRentalSiteId());
				dto.setRentalType(rsr.getRentalType());
				dto.setRentalStep(rsr.getRentalStep()); 
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
				} else if (dto.getRentalType().equals(
						RentalType.HALFDAY.getCode())) {
					dto.setAmorpm(rsr.getAmorpm());
				}
				dto.setUnit(rsr.getUnit());
				dto.setPrice(rsr.getPrice());
				dto.setRuleDate(rsr.getSiteRentalDate().getTime());
				List<RentalSitesBill> rsbs = rentalProvider
						.findRentalSiteBillBySiteRuleId(rsr.getId());
				dto.setStatus(SiteRuleStatus.OPEN.getCode());
				dto.setCounts((double) rsr.getCounts());
				if (null != rsbs && rsbs.size() > 0) {
					for (RentalSitesBill rsb : rsbs) {
						dto.setCounts(dto.getCounts() - rsb.getRentalCount());
					}
				}
				if (dto.getCounts() == 0) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}

				response.getRentalSiteRules().add(dto);

			}
		}

		return response;
	}
//	@Override
//	public VerifyRentalBillCommandResponse VerifyRentalBill(AddRentalBillCommand cmd){
//		VerifyRentalBillCommandResponse response = new VerifyRentalBillCommandResponse();
//		response.setAddBillCode(AddBillCode.NORMAL.getCode());
//		RentalBillDTO billDTO = new RentalBillDTO();
//		response.setRentalBill(billDTO);
//		Long userId = UserContext.current().getUser().getId();
//		int count = this.rentalProvider.countRentalBills(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(), null, SiteBillStatus.SUCCESS.getCode(), cmd.getStartTime(), cmd.getEndTime(), null,userId);
//		if(count > 0 ){ 
//			RentalBill bill =  this.rentalProvider.listRentalBills(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(), null, SiteBillStatus.SUCCESS.getCode(), 1,10,cmd.getStartTime(), cmd.getEndTime(), null,userId).
//					get(0);
//			mappingRentalBillDTO(billDTO, bill);
//			response.setAddBillCode(AddBillCode.CONFLICT.getCode());
//			return response;
//		}
//		return response;
//	}
	@Override
	public RentalBillDTO addRentalBill(AddRentalBillCommand cmd) {

		if(cmd.getRentalSiteId()==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rental site id");

		if(cmd.getRules()==null||cmd.getRules().size()==0)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rules");
		Long userId = UserContext.current().getUser().getId(); 
		RentalBillDTO billDTO = new RentalBillDTO();
		RentalSite rs =this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
		this.dbProvider.execute((TransactionStatus status) -> {
			java.util.Date reserveTime = new java.util.Date();
			List<RentalSiteRule> rentalSiteRules = new ArrayList<RentalSiteRule>();

			RentalBill rentalBill = ConvertHelper.convert(rs, RentalBill.class);
			rentalBill.setSiteName(rs.getSiteName());
			rentalBill.setRentalSiteId(cmd.getRentalSiteId());
			rentalBill.setRentalUid(userId);
			rentalBill.setInvoiceFlag(InvoiceFlag.NONEED.getCode());
			rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
			this.valiRentalBill(cmd.getRules());
//			rentalBill.setRentalCount(cmd.getRentalCount());
			java.math.BigDecimal siteTotalMoney = new java.math.BigDecimal(0);
			Map<java.sql.Date  , Set<Byte>> dayMap= new HashMap<Date, Set<Byte>>();
			for (rentalBillRuleDTO siteRule : cmd.getRules()) {

				if(siteRule.getRentalCount()==null||siteRule.getRuleId() == null )
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
		                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter siteRule");
				if (null == siteRule)
					continue;
				RentalSiteRule rentalSiteRule = rentalProvider
						.findRentalSiteRuleById(siteRule.getRuleId());
				//TODO:不允许一个用户预约多时段的情况
				
				//不允许一个用户预约一个时段多个资源的情况
				
				
				
				//给半天预定的日期map加入am和pm的byte
				if(rs.getRentalType().equals(RentalType.HALFDAY)||rs.getRentalType().equals(RentalType.THREETIMEADAY)){
					if(null==dayMap.get(rentalSiteRule.getSiteRentalDate()))
						dayMap.put(rentalSiteRule.getSiteRentalDate(), new HashSet<Byte>());
					dayMap.get(rentalSiteRule.getSiteRentalDate()).add(rentalSiteRule.getAmorpm());
				}
				rentalSiteRules.add(rentalSiteRule);
				if (null == rentalSiteRule.getBeginTime()) {
					if (null == rentalBill.getStartTime()
							|| rentalBill.getStartTime().after(
									rentalSiteRule.getSiteRentalDate()))
						rentalBill.setStartTime(new Timestamp(rentalSiteRule
								.getSiteRentalDate().getTime()));
				} else {
					if (null == rentalBill.getStartTime()
							|| rentalBill.getStartTime().after(
									rentalSiteRule.getBeginTime())) {
	
						rentalBill.setStartTime(rentalSiteRule.getBeginTime());
					}
				}
	
				if (null == rentalSiteRule.getEndTime()) {
					if (null == rentalBill.getEndTime()
							|| rentalBill.getEndTime().before(
									rentalSiteRule.getSiteRentalDate()))
						rentalBill.setEndTime(new Timestamp(rentalSiteRule
								.getSiteRentalDate().getTime()));
				} else {
					if (null == rentalBill.getEndTime()
							|| rentalBill.getEndTime().before(
									rentalSiteRule.getEndTime())) {
	
						rentalBill.setEndTime(rentalSiteRule.getEndTime());
					}
				}
				if(rs.getNeedPay().equals(NormalFlag.NEED.getCode())){
					if((siteRule.getRentalCount()-siteRule.getRentalCount().intValue())>0){
						//有半个
						//整数部分计算
						if(siteRule.getRentalCount().intValue()>0)
							siteTotalMoney = siteTotalMoney.add(  (null == rentalSiteRule.getPrice()?new java.math.BigDecimal(0):rentalSiteRule.getPrice()).multiply(
									new   java.math.BigDecimal(siteRule.getRentalCount().intValue() )));
						//小数部分计算
						siteTotalMoney = siteTotalMoney.add(  (null == rentalSiteRule.getHalfsitePrice()?new java.math.BigDecimal(0):rentalSiteRule.getPrice()));
					}
					else{
						siteTotalMoney = siteTotalMoney.add(  (null == rentalSiteRule.getPrice()?new java.math.BigDecimal(0):rentalSiteRule.getPrice()).multiply(
								new   java.math.BigDecimal(siteRule.getRentalCount() )));
			 
					}
				}
			}
			
			if(rs.getNeedPay().equals(NormalFlag.NEED.getCode())){
				//优惠
				if(DiscountType.FULL_MOENY_CUT_MONEY.equals(rs.getDiscountType())){
					//满减优惠
					int multiple =  siteTotalMoney.intValue()/rs.getFullPrice().intValue();
					siteTotalMoney = siteTotalMoney.subtract(rs.getCutPrice().multiply(new BigDecimal(multiple)));
				}
				else if(DiscountType.FULL_DAY_CUT_MONEY.equals(rs.getDiscountType()) ){
					int multiple =0;
					//满天减免
					if(rs.getRentalType().equals(RentalType.HALFDAY)){
						for(Date rentalDate:dayMap.keySet()){
							if(dayMap.get(rentalDate).size()==2)
								multiple++;
						}
					}
					else if (rs.getRentalType().equals(RentalType.THREETIMEADAY)){
						for(Date rentalDate:dayMap.keySet()){
							if(dayMap.get(rentalDate).size()==3)
								multiple++;
						}
					}
					siteTotalMoney = siteTotalMoney.subtract(rs.getCutPrice().multiply(new BigDecimal(multiple)));
				}
			}
			//不可以在开始时间-最多提前时间之前 预定 太早了
			if (reserveTime.before(new java.util.Date(rentalBill.getStartTime().getTime()
					- rs.getRentalStartTime()))) {
				LOGGER.error("reserve Time before reserve start time reserveTime = "+ datetimeSF.format(reserveTime));
				throw RuntimeErrorException 
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY,
										"reserve Time before reserve start time");
			}
			//也不可以在结束时间-最少提前时间之后预定  太迟了
			if (reserveTime.after(new java.util.Date(rentalBill.getEndTime().getTime()
					- rs.getRentalEndTime()))) {
				LOGGER.error("reserve Time after reserve end time  reserveTime = "+ datetimeSF.format(reserveTime));
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
										"reserve Time after reserve end time");
			}
			// for (SiteItemDTO siDto : cmd.getRentalItems()) {
			// totalMoney += siDto.getItemPrice() * siDto.getCounts();
			// }
			rentalBill.setSiteTotalMoney(siteTotalMoney);
			rentalBill.setPayTotalMoney(siteTotalMoney);
	//		rentalBill.setReserveMoney(siteTotalMoney.multiply(
	//				 new java.math.BigDecimal((rs.getPaymentRatio()==null?0:rs.getPaymentRatio())/ 100)));
			rentalBill.setReserveTime(Timestamp.valueOf(datetimeSF
					.format(reserveTime)));
	//		if(rs.getPayStartTime()!=null){
	//			rentalBill.setPayStartTime(new Timestamp(cmd.getStartTime()
	//					- rs.getPayStartTime()));
	//		}
	//		if(rs.getPayEndTime()!=null){
	//			rentalBill.setPayEndTime(new Timestamp(cmd.getStartTime()
	//					- rentalRule.getPayEndTime()));
	//		}
			rentalBill.setPaidMoney(new java.math.BigDecimal (0));
			//
			
	//		if (rentalRule.getPaymentRatio()!=null&&(rentalRule.getPaymentRatio()==null?0:rentalRule.getPaymentRatio()) <100 && reserveTime.before(new java.util.Date(cmd.getStartTime()
	//				- rentalRule.getPayStartTime()))) {
	//			//定金比例在100以内 在支付时间之前 为锁定待支付
	//			rentalBill.setStatus(SiteBillStatus.LOCKED.getCode());
	//
	//		} else {
				// 在支付时间之后 为待支付全款
			rentalBill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
	//		}
	
			rentalBill.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rentalBill.setCreatorUid(userId);
			rentalBill.setVisibleFlag(VisibleFlag.VISIBLE.getCode());
	
			// Long rentalBillId = this.rentalProvider.createRentalBill(rentalBill);
			//用基于服务器平台的锁添加订单（包括验证和添加）
			Tuple<Long, Boolean> tuple = (Tuple<Long, Boolean>) this.coordinationProvider
					.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode())
					.enter(() -> {
						// this.groupProvider.updateGroup(group);
						this.valiRentalBill(cmd.getRules());
						return this.rentalProvider.createRentalBill(rentalBill);
					});
			Long rentalBillId = tuple.first();
//			if (rentalBill.getStatus().equals(SiteBillStatus.LOCKED.getCode())) {
//	//			// 20分钟后，取消状态为锁定的订单
//	//			final Job job1 = new Job(
//	//					CancelLockedRentalBillAction.class.getName(),
//	//					new Object[] { String.valueOf(rentalBillId) });
//	//			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
//	//					System.currentTimeMillis() + cancelTime);
//	//			// 在支付时间开始时，把订单状态更新为待支付全款
//	//			final Job job2 = new Job(
//	//					UpdateRentalBillStatusToPayingFinalAction.class.getName(),
//	//					new Object[] { String.valueOf(rentalBill.getId()) });
//	//			// 20min cancel order if status still is locked or paying
//	//			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job2,
//	//					cmd.getStartTime() - rentalRule.getPayStartTime());
//	//			
//	//
//	//			// 在支付时间截止时，取消未成功的订单
//	//			final Job job3 = new Job(
//	//					CancelUnsuccessRentalBillAction.class.getName(),
//	//					new Object[] { String.valueOf(rentalBill.getId()) });
//	//			// 20min cancel order if status still is locked or paying
//	//			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job3,
//	//					cmd.getStartTime() - rentalRule.getPayEndTime());
//				
//				
//			} else
			if (rentalBill.getStatus().equals(
					SiteBillStatus.PAYINGFINAL.getCode())) {
				// 20分钟后，取消未成功的订单
				final Job job1 = new Job(
						CancelUnsuccessRentalBillAction.class.getName(),
						new Object[] { String.valueOf(rentalBill.getId()) });
	
				jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
						System.currentTimeMillis() + cancelTime);
	
			}
			
			
	//		if(null!=rentalBill.getEndTime()&&null!=rs.getOvertimeTime()){
	//			//超期未确认的置为超时
	//			final Job job1 = new Job(
	//					IncompleteUnsuccessRentalBillAction.class.getName(),
	//					new Object[] { String.valueOf(rentalBill.getId()) });
	//
	//			jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
	//					rentalBill.getEndTime().getTime() + rs.getOvertimeTime());
	//		}
			// 循环存site订单
			for (rentalBillRuleDTO siteRule : cmd.getRules())  {
				BigDecimal money = new BigDecimal(0);
				RentalSiteRule  rsr = rentalProvider.findRentalSiteRuleById(siteRule.getRuleId() );
				if((siteRule.getRentalCount()-siteRule.getRentalCount().intValue())>0){
					//有半个
					//整数部分计算
					if(siteRule.getRentalCount().intValue()>0)
						money = money.add(  (null == rsr.getPrice()?new java.math.BigDecimal(0):rsr.getPrice()).multiply(
								new   java.math.BigDecimal(siteRule.getRentalCount().intValue() )));
					//小数部分计算
					siteTotalMoney = siteTotalMoney.add(  (null == rsr.getHalfsitePrice()?new java.math.BigDecimal(0):rsr.getPrice()));
				}
				else{
					siteTotalMoney = siteTotalMoney.add(  (null == rsr.getPrice()?new java.math.BigDecimal(0):rsr.getPrice()).multiply(
							new   java.math.BigDecimal(siteRule.getRentalCount() )));
		 
				}
				RentalSitesBill rsb = new RentalSitesBill();
				rsb.setRentalBillId(rentalBillId);
				
				rsb.setTotalMoney(  money);
				rsb.setRentalCount(siteRule.getRentalCount());
				rsb.setRentalSiteRuleId(rsr.getId());
				rsb.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				rsb.setCreatorUid(userId);
	
				rentalProvider.createRentalSiteBill(rsb);

//				if(rs.getAutoAssign().equals(NormalFlag.NEED.getCode())){
//					Integer loopCnt = 0; 
////					assignSiteNumber(rsb,rsr,billDTO,loopCnt);
//				}
			}
			//验证site订单是否超过了site数量，如果有，抛异常，回滚操作
			this.valiRentalBill(0.0, cmd.getRules());
			mappingRentalBillDTO(billDTO, rentalBill);
			return billDTO;
		});
		return billDTO;
	}
	
//	private void assignSiteNumber(RentalSitesBill rsb ,RentalSiteRule rsr, RentalBillDTO billDTO,Integer loopCnt) {
//		
//		if(loopCnt++>20){
//			throw RuntimeErrorException
//			.errorWith(
//					RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_LOOP_TOOMUCH,"动态分配循环次数过多 ");
//		}
//		try{
//			this.dbProvider.execute((TransactionStatus status) -> {
//				Map<Integer, Double> siteNumberMap = new HashMap<Integer, Double> ();
//				//查询已预定的number 装入Map
//				List<RentalSitesBillNumber> siteNumbers = this.rentalProvider.findSitesBillNumbersBySiteId(rsb.getRentalSiteRuleId());
//				if(null!=siteNumbers){
//					for(RentalSitesBillNumber siteNumber : siteNumbers){
//						if(null == siteNumberMap.get(siteNumber.getSiteNumber()))
//							siteNumberMap.put(siteNumber.getSiteNumber() , siteNumber.getRentalCount());
//						else
//							siteNumberMap.put(siteNumber.getSiteNumber() , siteNumberMap.get(siteNumber.getSiteNumber())+siteNumber.getRentalCount());
//					}
//				}
//				//资源编号
//				int site_number =1;
//				//分配到第几个资源
//				double siteCount = 1.0;
//				//先给整数个site_rule 分配number
//				for( ;siteCount <=rsb.getRentalCount();siteCount++){
//					while(true){
//						if(null == siteNumberMap.get(site_number))
//							break;
//						else
//							site_number++;
//					}
//					//如果编号超出资源退出循环
//					if(site_number > rsr.getCounts())
//						break;
//					RentalSitesBillNumber sitesBillNumber = new RentalSitesBillNumber();
//					sitesBillNumber.setRentalCount(1.0);
//					sitesBillNumber.setRentalSiteBillId(rsb.getId());
//					sitesBillNumber.setRentalSiteRuleId(rsb.getRentalSiteRuleId());
//					sitesBillNumber.setSiteNumber(site_number);
//					this.rentalProvider.createRentalSitesBillNumber(sitesBillNumber);
//					siteNumberMap.put(site_number, 1.0);		
//				}
//				//分配半个
//				for( siteCount=siteCount-0.5;siteCount <=rsb.getRentalCount();){
//					if (siteCount <rsb.getRentalCount())
//						billDTO.setToastFlag(NormalFlag.NEED.getCode());
//					while(true){
//						if( siteNumberMap.get(site_number)==0.5)
//							break;
//						else
//							site_number++;
//					}
//					//资源编号超出单元格的个数,直接异常-应该不会有这种情况
//					if(site_number > rsr.getCounts())
//						throw RuntimeErrorException
//						.errorWith(
//								RentalServiceErrorCode.SCOPE,
//								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
//								localeStringService.getLocalizedString(
//										String.valueOf(RentalServiceErrorCode.SCOPE),
//										String.valueOf(RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES),
//										UserContext.current().getUser()
//												.getLocale(),
//										" has no enough sites to rental "));
//					RentalSitesBillNumber sitesBillNumber = new RentalSitesBillNumber();
//					sitesBillNumber.setRentalCount(0.5);
//					sitesBillNumber.setRentalSiteBillId(rsb.getId());
//					sitesBillNumber.setRentalSiteRuleId(rsb.getRentalSiteRuleId());
//					sitesBillNumber.setSiteNumber(site_number);
//					this.rentalProvider.createRentalSitesBillNumber(sitesBillNumber);
//					siteNumberMap.put(site_number, 0.5);
//					siteCount= siteCount+0.5;
//				}
//				//验证是否有冲突，如果有抛出异常
//				siteNumbers = this.rentalProvider.findSitesBillNumbersBySiteId(rsb.getRentalSiteRuleId());
//				siteNumberMap = new HashMap<Integer, Double> ();
//				for(RentalSitesBillNumber siteNumber : siteNumbers){
//					if(null == siteNumberMap.get(siteNumber.getSiteNumber()))
//						siteNumberMap.put(siteNumber.getSiteNumber() , siteNumber.getRentalCount());
//					else
//						siteNumberMap.put(siteNumber.getSiteNumber() , siteNumberMap.get(siteNumber.getSiteNumber())+siteNumber.getRentalCount());
//					//如果超过1.1个也就是1.5或者更多,说明冲突，重新分配
//					if(siteNumberMap.get(siteNumber.getSiteNumber())>1.1)
//						throw RuntimeErrorException
//						.errorWith(
//								RentalServiceErrorCode.SCOPE,
//								RentalServiceErrorCode.ERROR_REPEAT_SITE_ASSGIN,"资源编号重复，重新分配资源 ");
//				}
//				return null;
//			});
//		}catch(RuntimeErrorException e){
//			if(e.getErrorCode()==RentalServiceErrorCode.ERROR_REPEAT_SITE_ASSGIN){
//				//如果是分配到重复资源编号的就重新分配
//				LOGGER.info("assign rental site repeat,loop again ;time = "+loopCnt);
//				assignSiteNumber(rsb,rsr,billDTO,loopCnt);
//			}else{
//				throw e;
//			}
//			
//		}
//	
//}

	@Override
	public void valiRentalBill(Double rentalcount, List<rentalBillRuleDTO> ruleDTOs) {
		// 如果有一个规则，剩余的数量少于预定的数量
		for (rentalBillRuleDTO dto  : ruleDTOs) {
			if (dto == null)
				continue;
			Double totalCount = Double.valueOf(this.rentalProvider
					.findRentalSiteRuleById(dto.getRuleId()).getCounts());
			Double rentaledCount = this.rentalProvider
					.sumRentalRuleBillSumCounts(dto.getRuleId());
			if (null == rentaledCount)
				rentaledCount = 0.0;
			if ((totalCount - rentaledCount) < rentalcount) {
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
										" has no enough sites to rental ");
			}
		}

	}

	@Override
	public void valiRentalBill(List<rentalBillRuleDTO> ruleDTOs) {
		// 如果有一个规则，剩余的数量少于预定的数量
		for (rentalBillRuleDTO dto : ruleDTOs) {
			if (dto.getRuleId() == null)
				continue;
			Double totalCount = Double.valueOf(this.rentalProvider
					.findRentalSiteRuleById(dto.getRuleId()).getCounts());
			Double rentaledCount = this.rentalProvider
					.sumRentalRuleBillSumCounts(dto.getRuleId());
			if (null == rentaledCount)
				rentaledCount = 0.0;
			if ((totalCount - rentaledCount) < dto.getRentalCount()) {
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
										" has no enough sites to rental ");
			}
		}

	}

	
	@Override
	public FindRentalBillsCommandResponse findRentalBills(
			FindRentalBillsCommand cmd) { 
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(9223372036854775807L);
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		List<Byte> status = new ArrayList<>();
		if(cmd.getBillStatus().equals(BillQueryStatus.VALID.getCode())){
			status.add(SiteBillStatus.LOCKED.getCode());
			status.add(SiteBillStatus.RESERVED.getCode());
			status.add(SiteBillStatus.SUCCESS.getCode());
			status.add(SiteBillStatus.PAYINGFINAL.getCode()); 
		}
		else if(cmd.getBillStatus().equals(BillQueryStatus.CANCELED.getCode())){
			status.add(SiteBillStatus.FAIL.getCode()); 
			status.add(SiteBillStatus.REFUNDED.getCode()); 
			status.add(SiteBillStatus.REFUNDING.getCode()); 
		}
		else if(cmd.getBillStatus().equals(BillQueryStatus.FINISHED.getCode())){
			status.add(SiteBillStatus.COMPLETE.getCode());
			status.add(SiteBillStatus.OVERTIME.getCode()); 
		}
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalBill> billList = this.rentalProvider.listRentalBills(userId,
				cmd.getResourceTypeId(), locator, pageSize + 1,
				status);
		FindRentalBillsCommandResponse response = new FindRentalBillsCommandResponse();
		response.setRentalBills(new ArrayList<RentalBillDTO>());
		for (RentalBill bill : billList) {
			RentalSite rs = this.rentalProvider.getRentalSiteById(bill.getRentalSiteId());
			RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
			mappingRentalBillDTO(dto, bill); 
			dto.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalItemsBill> rentalSiteItems = rentalProvider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			if(null!= rentalSiteItems)
				for (RentalItemsBill rib : rentalSiteItems) {
					SiteItemDTO siDTO = new SiteItemDTO();
					siDTO.setCounts(rib.getRentalCount());
					RentalSiteItem rsItem = rentalProvider
							.findRentalSiteItemById(rib.getRentalSiteItemId());
					if(rsItem != null) {
						siDTO.setItemName(rsItem.getName());
					}
					siDTO.setItemPrice(rib.getTotalMoney());
					
					
					dto.getSiteItems().add(siDTO);
				}
			  
			response.getRentalBills().add(dto);
		}
		return response;
	}
	@Override
	public void mappingRentalBillDTO(RentalBillDTO dto, RentalBill bill) {
		RentalSite rs = rentalProvider
				.getRentalSiteById(bill.getRentalSiteId());
		if(null== rs){
			LOGGER.debug("RentalSite is null...bill id  = " + bill.getId()+",and site id = "+bill.getRentalSiteId());
			return ;
		}
		// 
//		RentalRule rr=rentalProvider.getRentalRule(bill.getOwnerId(), bill.getOwnerType(), bill.getSiteType());
//		RentalRule rr=null;
//		if(null== rr){
//			LOGGER.debug("RentalRule is null...getOwnerId  = " + bill.getOwnerId()+",and getOwnerType = "+bill.getOwnerType()+",and getSiteType = "+ bill.getSiteType());
//			return ;
//		}
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(bill.getRentalUid(), IdentifierType.MOBILE.getCode()) ;
		if(null == userIdentifier){
			LOGGER.debug("userIdentifier is null...userId = " + bill.getRentalUid());
		}else{
			dto.setUserPhone(userIdentifier.getIdentifierToken());}
		User user = this.userProvider.findUserById(bill.getRentalUid());
		if(null!=user)
			dto.setUserName(user.getNickName());
		else {
			LOGGER.debug("user is null...userId = " + bill.getRentalUid());
			dto.setUserName("用户不在系统中");
			}
		dto.setSiteName(rs.getSiteName());
		dto.setBuildingName(rs.getBuildingName());
		dto.setAddress(rs.getAddress());
		dto.setContactPhonenum(rs.getContactPhonenum());
		
		dto.setSpec(rs.getSpec());
		dto.setCompanyName(rs.getOwnCompanyName());
		dto.setContactName(rs.getContactName()); 
		dto.setNotice(rs.getNotice());
		dto.setIntroduction(rs.getIntroduction());
		dto.setRentalBillId(bill.getId()); 
		dto.setRentalCount(bill.getRentalCount());
		if (null == bill.getStartTime()) {

		} else {
			dto.setStartTime(bill.getStartTime().getTime());
		}
		if (null == bill.getEndTime()) {

		} else {
			dto.setEndTime(bill.getEndTime().getTime());
		}
		dto.setReserveTime(bill.getReserveTime().getTime());
		if (null != bill.getPayStartTime()) {
			dto.setPayStartTime(bill.getPayStartTime().getTime());
		}
		if (null != bill.getPayTime()) {
			dto.setPayTime(bill.getPayTime().getTime());
		}
		if (null != bill.getPayEndTime()) {
			dto.setPayDeadLineTime(bill.getPayEndTime().getTime());
		}
		if (null != bill.getCancelTime()) {
			dto.setCancelTime(bill.getCancelTime().getTime());
		}
		// Integer sitePrice = rentalProvider.getSumSitePrice(dto
		// .getRentalBillId());
		// dto.setSitePrice(sitePrice);
		dto.setTotalPrice(bill.getPayTotalMoney());
		dto.setSitePrice(bill.getSiteTotalMoney());
		dto.setReservePrice(bill.getReserveMoney());
		dto.setPaidPrice(bill.getPaidMoney());
		dto.setUnPayPrice(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
		dto.setInvoiceFlag(bill.getInvoiceFlag());
		dto.setStatus(bill.getStatus());
		dto.setRentalSiteRules(new ArrayList<RentalSiteRulesDTO>());
		//支付方式
		List<RentalBillPaybillMap> billPaybillMaps = this.rentalProvider.findRentalBillPaybillMapByBillId(bill.getId()) ;
		if(null!=billPaybillMaps && billPaybillMaps.size()>0)
			dto.setVendorType(billPaybillMaps.get(0).getVendorType());
		//使用详情
		StringBuffer useDetailSB = new StringBuffer();
		// 订单的rules
		List<RentalSitesBill> rsbs = rentalProvider
				.findRentalSitesBillByBillId(bill.getId());
		for (RentalSitesBill rsb : rsbs) {
			RentalSiteRule rsr = rentalProvider.findRentalSiteRulesByRuleId(rsb.getRentalSiteRuleId());
			RentalSiteRulesDTO ruleDto = new RentalSiteRulesDTO();
			ruleDto.setId(rsr.getId());
			ruleDto.setRentalSiteId(rsr.getRentalSiteId());
			ruleDto.setRentalType(rsr.getRentalType());
			ruleDto.setRentalStep(rsr.getRentalStep()); 
			if (ruleDto.getRentalType().equals(RentalType.HOUR.getCode())) {
				ruleDto.setTimeStep(rsr.getTimeStep());
				ruleDto.setBeginTime(rsr.getBeginTime().getTime());
				ruleDto.setEndTime(rsr.getEndTime().getTime());
			} else if (ruleDto.getRentalType().equals(
					RentalType.HALFDAY.getCode())) {
				ruleDto.setAmorpm(rsr.getAmorpm());
			}
			ruleDto.setUnit(rsr.getUnit());
			ruleDto.setPrice(rsr.getPrice());
			ruleDto.setRuleDate(rsr.getSiteRentalDate().getTime());
//			if(rs.getAutoAssign().equals(NormalFlag.NEED.getCode())){
//
//				List<RentalSitesBillNumber> siteNumbers = this.rentalProvider.findSitesBillNumbersByBillId(rsr.getId()); 
//				ruleDto.setSiteNumber("");
//				if(null == siteNumbers)
//					throw RuntimeErrorException
//					.errorWith(
//							RentalServiceErrorCode.SCOPE,
//							RentalServiceErrorCode.ERROR_SITE_ASSGIN_NULL,
//							localeStringService.getLocalizedString(
//									String.valueOf(RentalServiceErrorCode.SCOPE),
//									String.valueOf(RentalServiceErrorCode.ERROR_SITE_ASSGIN_NULL),
//									UserContext.current().getUser()
//											.getLocale(),
//									" error: no assgined site number  "));
//				for(RentalSitesBillNumber siteNumber : siteNumbers){
//					if(StringUtils.isBlank( ruleDto.getSiteNumber()))
//						ruleDto.setSiteNumber(String.valueOf(siteNumber.getSiteNumber())+"号");
//					else
//						ruleDto.setSiteNumber(ruleDto.getSiteNumber()+","+String.valueOf(siteNumber.getSiteNumber())+"号");
//				}
//				
//			}

			ruleDto.setSiteNumber(String.valueOf(rsr.getSiteNumber())+"号");
			if(rsb.getRentalCount()<1)
				ruleDto.setSiteNumber(ruleDto.getSiteNumber()+"（半场）");
			dto.getRentalSiteRules().add(ruleDto);
			
			if(rsr.getRentalType().equals(RentalType.HOUR.getCode())){
				useDetailSB.append("使用时间:");
				useDetailSB.append("从");
				useDetailSB.append(datetimeSF.format(rsr.getBeginTime()));
				useDetailSB.append("到");
				useDetailSB.append(datetimeSF.format(rsr.getEndTime()));
			}else if(rsr.getRentalType().equals(RentalType.DAY.getCode())){
				useDetailSB.append("使用时间:");
				useDetailSB.append(dateSF.format(rsr.getSiteRentalDate()));
			}else {
				useDetailSB.append("使用时间:");
				useDetailSB.append(dateSF.format(rsr.getSiteRentalDate()));
				if(rsr.getAmorpm().equals(AmorpmFlag.AM))
					useDetailSB.append("早上");
				if(rsr.getAmorpm().equals(AmorpmFlag.PM))
					useDetailSB.append("下午");
				if(rsr.getAmorpm().equals(AmorpmFlag.NIGHT))
					useDetailSB.append("晚上");
			}
			if(rs.getExclusiveFlag().equals(NormalFlag.NEED.getCode())){
			//独占资源 只有时间				 
			}
			else if(rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())){
				//不需要资源编号
				useDetailSB.append(";预约数量:");
				useDetailSB.append(rsb.getRentalCount());
			}
			else {
				//不需要资源编号
				useDetailSB.append(";资源编号:");
				useDetailSB.append(ruleDto.getSiteNumber());
//				useDetailSB.append("号");
			}
			useDetailSB.append("\n");
		}
		
		
		
		
		dto.setUseDetail(useDetailSB.toString());
				
				
		// 订单的附件attachments
		dto.setBillAttachments(new ArrayList<BillAttachmentDTO>());
		List<RentalBillAttachment> attachments = rentalProvider
				.findRentalBillAttachmentByBillId(dto.getRentalBillId());
		for (RentalBillAttachment attachment : attachments) {
			BillAttachmentDTO attachmentDTO = new BillAttachmentDTO();
			attachmentDTO.setAttachmentType(attachment.getAttachmentType());
			attachmentDTO.setBillId(attachment.getRentalBillId());
			attachmentDTO.setContent(attachment.getContent());
			attachmentDTO.setId(attachment.getId());
			dto.getBillAttachments().add(attachmentDTO);
		}
	}

	@Override
	public GetRentalTypeRuleCommandResponse getRentalTypeRule(
			GetRentalTypeRuleCommand cmd) {
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		RentalRule rentalRule = rentalProvider.getRentalRule(
				cmd.getOwnerId(),cmd.getOwnerType(), cmd.getSiteType());
		if(null == rentalRule){
			return new GetRentalTypeRuleCommandResponse();
		}
		GetRentalTypeRuleCommandResponse response = ConvertHelper.convert(rentalRule, GetRentalTypeRuleCommandResponse.class);
		response.setPayRatio(rentalRule.getPaymentRatio());
		return response;
	}

	@Override
	public void addRentalSiteSimpleRules(AddRentalSiteRulesAdminCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			//设置默认规则，删除所有的单元格
			Integer deleteCount = rentalProvider.deleteRentalSiteRules(
					cmd.getRentalSiteId(), null, null);
			LOGGER.debug("delete count = " + String.valueOf(deleteCount)
					+ "  from rental site rules  ");
			RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
			rs.setExclusiveFlag(cmd.getExclusiveFlag());
			if(cmd.getExclusiveFlag().equals(NormalFlag.NEED.getCode())){
				cmd.setUnit(1.0);
				cmd.setAutoAssign(NormalFlag.NONEED.getCode());
				cmd.setMultiUnit(NormalFlag.NONEED.getCode());
				cmd.setSiteCounts(1.0);
			}
			rs.setAutoAssign(cmd.getAutoAssign());
			rs.setMultiUnit(cmd.getMultiUnit());
			rs.setNeedPay(cmd.getNeedPay());
			if(cmd.getNeedPay().equals(NormalFlag.NONEED.getCode())){
				cmd.setWeekendPrice(new BigDecimal(0));
				cmd.setWorkdayPrice(new BigDecimal(0));
			}
			rs.setMultiTimeInterval(cmd.getMultiTimeInterval());
			rs.setRentalType(cmd.getRentalType());
			rs.setRentalEndTime(cmd.getRentalEndTime());
			rs.setRentalStartTime(cmd.getRentalStartTime());
			rs.setTimeStep(cmd.getTimeStep());
			rs.setCancelTime(cmd.getCancelTime());
			rs.setRefundFlag(cmd.getRefundFlag());
			rs.setRefundRatio(cmd.getRefundRatio());
			this.rentalProvider.updateRentalSite(rs);
			this.rentalProvider.deleteRentalConfigAttachmentsByOwnerId(EhRentalSites.class.getSimpleName(), rs.getId());
			if(null!=cmd.getAttachments())
				for(com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO attachmentDTO:cmd.getAttachments()){
					RentalConfigAttachment rca =ConvertHelper.convert(attachmentDTO, RentalConfigAttachment.class);
					rca.setOwnerType(EhRentalSites.class.getSimpleName());
					rca.setOwnerId(rs.getId());
					this.rentalProvider.createRentalConfigAttachment(rca);
				}
			
			
			BigDecimal weekendPrice = cmd.getWeekendPrice() == null ? new BigDecimal(0) : cmd.getWeekendPrice(); 
			BigDecimal workdayPrice = cmd.getWorkdayPrice() == null ? new BigDecimal(0) : cmd.getWorkdayPrice();
			
			if(cmd.getTimeIntervals() != null) {
				Double beginTime = null;
				Double endTime = null;
				for(TimeIntervalDTO timeInterval:cmd.getTimeIntervals()){
					if(timeInterval.getBeginTime() == null || timeInterval.getEndTime()==null)
						continue;
					if(beginTime==null||beginTime>timeInterval.getBeginTime())
						beginTime=timeInterval.getBeginTime();
					if(endTime==null||endTime<timeInterval.getEndTime())
						endTime=timeInterval.getEndTime();
					AddRentalSiteSingleSimpleRule signleCmd=ConvertHelper.convert(cmd, AddRentalSiteSingleSimpleRule.class );
					signleCmd.setBeginTime(timeInterval.getBeginTime());
					signleCmd.setEndTime(timeInterval.getEndTime());
					signleCmd.setWeekendPrice(weekendPrice); 
					signleCmd.setWorkdayPrice(workdayPrice);
					addRentalSiteSingleSimpleRule(signleCmd);
				}
			} else {
				AddRentalSiteSingleSimpleRule signleCmd=ConvertHelper.convert(cmd, AddRentalSiteSingleSimpleRule.class );
				signleCmd.setWeekendPrice(weekendPrice); 
				signleCmd.setWorkdayPrice(workdayPrice);
				addRentalSiteSingleSimpleRule(signleCmd);
			}
			return null;
		});
	}
	
	
	public void addRentalSiteSingleSimpleRule(AddRentalSiteSingleSimpleRule cmd) {
		Long userId = UserContext.current().getUser().getId();
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		
		while (start.before(end)) {
			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
			if (cmd.getOpenWeekday().contains(weekday)) {
				RentalSiteRule rsr =ConvertHelper.convert(cmd, RentalSiteRule.class);
				rsr.setAutoAssign(cmd.getAutoAssign()); 
				if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
					for (double i = cmd.getBeginTime(); i < cmd.getEndTime();) {
						rsr.setBeginTime(Timestamp.valueOf(dateSF.format(start
								.getTime())
								+ " "
								+ String.valueOf((int) i / 1)
								+ ":"
								+ String.valueOf((int) ((i % 1) * 60))
								+ ":00"));

						// i = i + cmd.getTimeStep();
						rsr.setRentalStep(1); 
						rsr.setTimeStep(cmd.getTimeStep());
//						i = i + 0.5;
						i = i + cmd.getTimeStep();
						if(i > cmd.getEndTime())
							continue;
						rsr.setEndTime(Timestamp.valueOf(dateSF.format(start
								.getTime())
								+ " "
								+ String.valueOf((int) i / 1)
								+ ":"
								+ String.valueOf((int) ((i % 1) * 60))
								+ ":00"));
						rsr.setRentalSiteId(cmd.getRentalSiteId());
						rsr.setRentalType(cmd.getRentalType());
						rsr.setCounts(cmd.getSiteCounts());
						rsr.setUnit(cmd.getUnit());
						if (weekday == 1 || weekday == 7) {
							rsr.setPrice(cmd.getWeekendPrice());
							
						} else {
							rsr.setPrice(cmd.getWorkdayPrice());
						}
						if(rsr.getUnit()<1){
							rsr.setHalfsitePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) ); 
						}
						rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
								.getTime())));
						rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
						rsr.setCreateTime(new Timestamp(DateHelper
								.currentGMTTime().getTime()));
						rsr.setCreatorUid(userId);
						

						createRSR(rsr, cmd);
					}
				}
				// 按半日预定
				else if (cmd.getRentalType().equals(
						RentalType.HALFDAY.getCode())||cmd.getRentalType().equals(
								RentalType.THREETIMEADAY.getCode())) {
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getSiteCounts()==null?1:cmd.getSiteCounts());
					rsr.setUnit(cmd.getUnit());
					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
							.getTime())));
					rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rsr.setCreatorUid(userId);
					rsr.setRentalStep(1);
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						if(rsr.getUnit()<1){
							rsr.setHalfsitePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) ); 
						}
						createRSR(rsr, cmd);
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						createRSR(rsr, cmd);
						if(cmd.getRentalType().equals(
								RentalType.THREETIMEADAY.getCode())){
							rsr.setAmorpm(AmorpmFlag.NIGHT.getCode());
							createRSR(rsr, cmd);
						}
							
					} else {
						rsr.setPrice(cmd.getWorkdayPrice());
						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						if(rsr.getUnit()<1){
							rsr.setHalfsitePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) ); 
						}
						createRSR(rsr, cmd);
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						createRSR(rsr, cmd);
						if(cmd.getRentalType().equals(
								RentalType.THREETIMEADAY.getCode())){
							rsr.setAmorpm(AmorpmFlag.NIGHT.getCode());
							createRSR(rsr, cmd);
						}
					}

					
				}
				// 按日预定
				else if (cmd.getRentalType().equals(RentalType.DAY.getCode())) {
					rsr.setRentalSiteId(cmd.getRentalSiteId());
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getSiteCounts());
					rsr.setRentalStep(1);
					rsr.setUnit(cmd.getUnit());
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
					} else {
						rsr.setPrice(cmd.getWorkdayPrice());
					}
					rsr.setSiteRentalDate(Date.valueOf(dateSF.format(start
							.getTime())));
					rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rsr.setCreatorUid(userId);

					createRSR(rsr, cmd);
				}

			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	public void createRSR(RentalSiteRule rsr,AddRentalSiteSingleSimpleRule cmd){
		if(cmd.getAutoAssign().equals(NormalFlag.NEED.getCode())){
			//自动分配sitenumber
			for(int siteNumber = 1;siteNumber<=cmd.getSiteCounts();siteNumber++){
				rsr.setSiteNumber(siteNumber);
				rentalProvider.createRentalSiteRule(rsr);
			}
		}else{
			rentalProvider.createRentalSiteRule(rsr);
		}
	}
	@Override
	public void deleteRentalSiteRules(DeleteRentalSiteRulesCommand cmd) {
		JSONObject jsonObject = (JSONObject) JSONValue
				.parse(cmd.getRuleDates());
		JSONArray choosenValue = (JSONArray) jsonObject.get("ruleDates");
		Gson gson = new Gson();
		List<Long> deleteDates = gson.fromJson(choosenValue.toString(),
				new TypeToken<List<Long>>() {
				}.getType());
		for (Long deleteDate : deleteDates) {
			rentalProvider
					.deleteRentalSiteRules(Long.valueOf(cmd.getRentalSiteId()),
							deleteDate, deleteDate);
		}
	}

	@Override
	public void cancelRentalBill(CancelRentalBillCommand cmd) {
		java.util.Date cancelTime = new java.util.Date();
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		
		RentalSite rs = this.rentalProvider.getRentalSiteById(bill.getRentalSiteId());		
		if (bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())&&cancelTime.after(new java.util.Date(bill.getStartTime().getTime()
				- rs.getCancelTime()))) {
			//当成功预约之后要判断是否过了取消时间
			LOGGER.error("cancel over time");
			throw RuntimeErrorException
					.errorWith(
							RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_CANCEL_OVERTIME,
									"cancel bill over time");
		}else{
			this.dbProvider.execute((TransactionStatus status) -> {
				//默认是已退款
				bill.setStatus(SiteBillStatus.REFUNDED.getCode());
				if (rs.getRefundFlag().equals(NormalFlag.NEED)){ 
					List<RentalBillPaybillMap>  billmaps = this.rentalProvider.findRentalBillPaybillMapByBillId(bill.getId());
					for(RentalBillPaybillMap billMap : billmaps){
						//循环退款
						PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
						String refoundApi =  this.configurationProvider.getValue("pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
						String appKey = configurationProvider.getValue("pay.appKey", "b86ddb3b-ac77-4a65-ae03-7e8482a3db70");
						refundCmd.setAppKey(appKey);
						Long timestamp = System.currentTimeMillis();
						refundCmd.setTimestamp(timestamp);
						Integer randomNum = (int) (Math.random()*1000);
						refundCmd.setNonce(randomNum);
						Long refoundOrderNo = this.onlinePayService.createBillId(DateHelper
								.currentGMTTime().getTime());
						refundCmd.setRefundOrderNo(String.valueOf(refoundOrderNo));
						refundCmd.setOrderNo(String.valueOf(billMap.getOnlinePayBillId()));
						refundCmd.setOnlinePayStyleNo(VendorType.fromCode(billMap.getVendorType()).getStyleNo()); 
						refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALREFUND.getPycode());
						//已付金额乘以退款比例除以100
						refundCmd.setRefundAmount(bill.getPaidMoney().multiply(new BigDecimal(rs.getRefundRatio()/100)));
						refundCmd.setRefundMsg("预订单取消退款");
						this.setSignatureParam(refundCmd);
						RentalRefundOrder rentalRefundOrder = ConvertHelper.convert(refundCmd,RentalRefundOrder.class);
						rentalRefundOrder.setOrderNo(billMap.getOnlinePayBillId());
						rentalRefundOrder.setRefundOrderNo(refoundOrderNo);
						rentalRefundOrder.setRentalBillId(bill.getId()); 
						rentalRefundOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						rentalRefundOrder.setCreatorUid(UserContext.current().getUser().getId());
						rentalRefundOrder.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						rentalRefundOrder.setOperatorUid(UserContext.current().getUser().getId());
						rentalRefundOrder.setResourceTypeId(bill.getResourceTypeId());
						rentalRefundOrder.setAmount(refundCmd.getRefundAmount());
						//微信直接退款，支付宝置为退款中 
						if(billMap.getVendorType().equals(VendorType.WEI_XIN.getVendorType())){
							PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refoundApi, refundCmd, PayZuolinRefundResponse.class);
							if(refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
								//退款成功保存退款单信息，修改bill状态
								rentalRefundOrder.setStatus(SiteBillStatus.REFUNDED.getCode());
								bill.setStatus(SiteBillStatus.REFUNDED.getCode());
							}
							else{
								LOGGER.error("bill id=["+bill.getId()+"] refound error param is "+refundCmd.toString());
								throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
										RentalServiceErrorCode.ERROR_REFOUND_ERROR,
												"bill  refound error"); 
							}	
						}
						else{
							rentalRefundOrder.setStatus(SiteBillStatus.REFUNDING.getCode());
							bill.setStatus(SiteBillStatus.REFUNDING.getCode());
						}
						this.rentalProvider.createRentalRefundOrder(rentalRefundOrder);
					}
				}
				else{
					//如果不需要退款，直接状态为已取消
					bill.setStatus(SiteBillStatus.FAIL.getCode());
				}
				//更新bill状态
				rentalProvider.updateRentalBill(bill); 
				return null;
			});
		}
	}
	
	/***给支付相关的参数签名*/
	private void setSignatureParam(PayZuolinRefundCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",cmd.getAppKey());
		map.put("timestamp",cmd.getTimestamp()+"");
		map.put("nonce",cmd.getNonce()+"");
		map.put("refundOrderNo",cmd.getRefundOrderNo());
		map.put("orderNo", cmd.getOrderNo());
		map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
		map.put("orderType",cmd.getOrderType() );
		map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
		map.put("refundMsg", cmd.getRefundMsg()); 
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		cmd.setSignature(signature);
	}
	@Override
	public void updateRentalSite(UpdateRentalSiteCommand cmd) {
		// 已有未取消的预定，不能修改
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		RentalSite rentalsite = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId()); 
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setSiteName(cmd.getSiteName());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setSpec(cmd.getSpec());
		rentalsite.setOwnCompanyName(cmd.getCompany());
		rentalsite.setContactName(cmd.getContactName());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setIntroduction(cmd.getIntroduction());
		rentalsite.setNotice(cmd.getNotice());
		rentalProvider.updateRentalSite(rentalsite);
	}

	@Override
	public void deleteRentalSite(DeleteRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalProvider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
							"HAS BILL IN YOUR DELETE STUFF");
		}
		rentalProvider.deleteRentalSiteRules(cmd.getRentalSiteId(), null, null);
//		rentalProvider.deleteRentalBillBySiteId(cmd.getRentalSiteId());
		rentalProvider.deleteRentalSite(cmd.getRentalSiteId());
	}
	@Override
	public void disableRentalSite(DisableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能停用
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		rentalProvider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.DISABLE.getCode());
	}
	

	@Override
	public void enableRentalSite(EnableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		rentalProvider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.NORMAL.getCode());
	}
	
	
	@Override
	public void deleteRentalSiteItem(DeleteItemAdminCommand cmd) {
//		Integer billCount = rentalProvider.countRentalSiteItemBills(cmd
//				.getItemId());
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//
//		}
		rentalProvider.deleteRentalSiteItemById(cmd.getItemId());
	}

	@Override
	public GetItemListCommandResponse listRentalSiteItems(
			GetItemListAdminCommand cmd) {
		if(cmd.getRentalSiteId()==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rental site id");
		
		GetItemListCommandResponse response = new GetItemListCommandResponse();
		response.setSiteItems(new ArrayList<SiteItemDTO>());
		List<RentalSiteItem> rsiSiteItems = rentalProvider
				.findRentalSiteItems(cmd.getRentalSiteId());
		for (RentalSiteItem rsi : rsiSiteItems) {
			SiteItemDTO dto = convertItem2DTO(rsi);
			 
			response.getSiteItems().add(dto);
		}
		return response;
	}

	@Override
	public AddRentalBillItemCommandResponse addRentalItemBill(
			AddRentalBillItemCommand cmd) {
 
		// 循环存物品订单
		AddRentalBillItemCommandResponse response = new AddRentalBillItemCommandResponse();
		this.dbProvider.execute((TransactionStatus status) -> {
			response.setName("资源预定订单");
			response.setDescription("资源预定订单");
			response.setOrderType("resourceOrder"); 
			Long userId = UserContext.current().getUser().getId();
	
			RentalBill bill = rentalProvider.findRentalBillById(cmd
					.getRentalBillId());
			if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
				throw RuntimeErrorException
						.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_BILL_OVERTIME, "BILL OVERTIME");
			}
			 
			//2016-6-2 10:32:44 fix bug :当有物品订单（说明是付款失败再次付款），就不再生成物品订单
			if (null != cmd.getRentalItems()&&this.rentalProvider.findRentalItemsBillBySiteBillId(cmd.getRentalBillId())==null) {
				java.math.BigDecimal itemMoney = new java.math.BigDecimal(0);
				for (SiteItemDTO siDto : cmd.getRentalItems()) {
					 
					if(siDto.getId() == null) {
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
			                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of siDto id"+ siDto+".");
					}
					RentalSiteItem rSiteItem = this.rentalProvider.getRentalSiteItemById(siDto.getId());
					if (null == rSiteItem)
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
			                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of siDto id"+ siDto+".");
					
					if(rSiteItem.getRentalSiteId()!=bill.getRentalSiteId())
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
			                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter item id is not this site");
						
					RentalItemsBill rib = new RentalItemsBill();
					rib.setTotalMoney(rSiteItem.getPrice().multiply( new java.math.BigDecimal(siDto.getCounts())));
					rib.setRentalSiteItemId(siDto.getId());
					rib.setRentalCount(siDto.getCounts());
					rib.setRentalBillId(cmd.getRentalBillId());
					rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
							.getTime()));
					rib.setCreatorUid(userId);
					itemMoney  = itemMoney.add(rib.getTotalMoney());
					//用基于服务器平台的锁添加订单（包括验证和添加）
					Tuple<Boolean, Boolean> tuple = (Tuple<Boolean, Boolean>)  this.coordinationProvider
							.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode())
							.enter(() -> {
								//先验证后添加，由于锁机制，可以保证同时只有一个线程验证和添加
								if(this.valiItem(rib))
									return true;
								rentalProvider.createRentalItemBill(rib);
								return false;
							});
					Boolean valiBoolean = tuple.first();
					if(valiBoolean)
						throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_NO_ENOUGH_ITEMS,"no enough items");
					
					
				}
				if (itemMoney.doubleValue() > 0) {
					bill.setPayTotalMoney(bill.getSiteTotalMoney().add(itemMoney));
//					bill.setReserveMoney(bill.getReserveMoney().add(itemMoney));
				}
			}
			int compare = bill.getPayTotalMoney().compareTo(BigDecimal.ZERO);
			
			if (compare == 0) {
				// 总金额为0，直接预订成功状态
				bill.setStatus(SiteBillStatus.SUCCESS.getCode());
			}
//			else if ( bill.getStatus().equals(
//							SiteBillStatus.LOCKED.getCode())) {
//				// 预付金额为0，且状态为locked，直接进入支付定金成功状态
//				if(bill.getReserveMoney().equals(0.0))
//					bill.setStatus(SiteBillStatus.RESERVED.getCode());
//				else if (bill.getReserveMoney().equals(bill.getPayTotalMoney().subtract(bill.getPaidMoney())))
//					bill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
//	
//			}
			rentalProvider.updateRentalBill(bill);
			
	//		switch(cmd.getSiteType()){
	//		case("MEETINGROOM"): 
	//			response.setName("会议室预定订单");
	//			response.setDescription("会议室预定订单");
	//			response.setOrderType("huiyishiorder");
	//			break;
	//		case("VIPPARKING"):
	//			response.setName("VIP车位预定订单");
	//			response.setDescription("VIP车位预定订单");
	//			response.setOrderType("vipcheweiorder");
	//			break;
	//		case("ELECSCREEN"):
	//			response.setName("电子屏预定订单");
	//			response.setDescription("电子屏预定订单");
	//			response.setOrderType("dianzipingorder"); 
	//			break;
	//		}
			Long orderNo = null;
			if (bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())) {
				orderNo = onlinePayService.createBillId(DateHelper
						.currentGMTTime().getTime());
				response.setAmount(bill.getReserveMoney());
				response.setOrderNo(String.valueOf(orderNo));
				
			} else if (bill.getStatus()
					.equals(SiteBillStatus.PAYINGFINAL.getCode())) {
				orderNo = onlinePayService.createBillId(DateHelper
						.currentGMTTime().getTime());
				response.setAmount(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
				response.setOrderNo(String.valueOf(orderNo));
			} else {
				response.setAmount(new java.math.BigDecimal(0));
			}
			// save bill and online pay bill
			RentalBillPaybillMap billmap = new RentalBillPaybillMap();
	 
			billmap.setRentalBillId(cmd.getRentalBillId());
			billmap.setOnlinePayBillId(orderNo);
			billmap.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			billmap.setCreatorUid(userId);
			rentalProvider.createRentalBillPaybillMap(billmap);
			for(AttachmentDTO attachment : cmd.getRentalAttachments()){
				RentalBillAttachment rba = new RentalBillAttachment();
				rba.setRentalBillId(cmd.getRentalBillId());
				rba.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				rba.setCreatorUid(userId);
				rba.setAttachmentType(attachment.getAttachmentType());
				rba.setContent(attachment.getContent());
			} 
			//签名
			this.setSignatureParam(response);
			return null;
		});
		
		// 客户端生成订单
		return response;
	}

	private boolean valiItem(RentalItemsBill rib) {

		RentalSiteItem rSiteItem = this.rentalProvider.getRentalSiteItemById(rib.getRentalSiteItemId()); 
		List<RentalSitesBill>  rentalSitesBills = this.rentalProvider.findRentalSitesBillByBillId(rib.getRentalBillId());
		if(rSiteItem.getItemType().equals(RentalItemType.SALE.getCode())){
			Integer soldSum = this.rentalProvider.countRentalSiteItemSoldCount(rSiteItem.getId());
			//如果订单的商品总数加此次订单的数量超过了商品的总数
			if( rSiteItem.getCounts() < soldSum + rib.getRentalCount())
				return true;
		}
		else if(rSiteItem.getItemType().equals(RentalItemType.RENTAL.getCode())){
			//如果这个租用的 循环每一个单元格 
			for(RentalSitesBill rentalSitesBill : rentalSitesBills){
				//由于商品订单不和单元格关联，所以要找到该单元格的所有订单
				List<Long> rentalBillIds = this.findRentalBillIdsByRuleId(rentalSitesBill.getRentalSiteRuleId());
				//查该单元格所有订单的预定商品总数
				Integer rentalSum = this.rentalProvider.countRentalSiteItemRentalCount(rentalBillIds);
				// 在单元格下租用的所有物品总数+此次订单租赁数，超过商品总数 则报异常
				if( rSiteItem.getCounts() < rentalSum + rib.getRentalCount())
					return true;
			}
		}
		return false;
	}
	
	public List<Long> findRentalBillIdsByRuleId(Long ruleId){
		List<Long> result = new ArrayList<>();
		List<RentalSitesBill>  rentalSitesBills = this.rentalProvider.findRentalSiteBillBySiteRuleId(ruleId);
		for(RentalSitesBill sitesBill : rentalSitesBills){
			result.add(sitesBill.getRentalBillId());
		}
		return result;
		
	}
	
	
	private void setSignatureParam(AddRentalBillItemCommandResponse response) {
		String appKey = configurationProvider.getValue("pay.appKey", "b86ddb3b-ac77-4a65-ae03-7e8482a3db70");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",response.getAmount().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		response.setAppKey(appKey);
		response.setRandomNum(randomNum);
		response.setSignature(URLEncoder.encode(signature));
		response.setTimestamp(timestamp);
	}

	@Override
	public ListRentalBillsCommandResponse listRentalBills(
			ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
//		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
//		int totalCount = rentalProvider.countRentalBills(cmd.getOwnerId(),cmd.getOwnerType(),
//				cmd.getSiteType(), cmd.getRentalSiteId(), cmd.getBillStatus(),
//				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag(),null);
//		if (totalCount == 0)
//			return response;

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize()); 
//		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalBill> bills = rentalProvider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), 
				cmd.getRentalSiteId(), locator, cmd.getBillStatus(), cmd.getVendorType(), pageSize+1, cmd.getStartTime(), cmd.getEndTime(),
				null, null); 
		if(bills != null && bills.size() > pageSize) {
			bills.remove(bills.size() - 1);
			response.setNextPageAnchor( bills.get(bills.size() -1).getId()); 
		}
		
		response.setRentalBills(new ArrayList<RentalBillDTO>());
		for (RentalBill bill : bills) {
			RentalBillDTO dto = new RentalBillDTO();
			mappingRentalBillDTO(dto, bill);
			dto.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalItemsBill> rentalSiteItems = rentalProvider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			if(null!=rentalSiteItems)
				for (RentalItemsBill rib : rentalSiteItems) {
					SiteItemDTO siDTO = new SiteItemDTO();
					siDTO.setCounts(rib.getRentalCount());
					RentalSiteItem rsItem = rentalProvider.findRentalSiteItemById(rib.getRentalSiteItemId());
					if(rsItem != null) {
	    				siDTO.setItemName(rsItem.getName());
	    				siDTO.setItemPrice(rib.getTotalMoney());
	    				dto.getSiteItems().add(siDTO);
					} else {
					    LOGGER.error("Rental site item not found, rentalSiteItemId=" + rib.getRentalSiteItemId() + ", cmd=" + cmd);
					}
				}
			response.getRentalBills().add(dto);
		}
 
		return response;
	}

	@Override
	public void deleteRentalBill(DeleteRentalBillCommand cmd) {

		if(null!=cmd.getCommunityId()){
			cmd.setOwnerId(cmd.getCommunityId());
			cmd.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		}

		rentalProvider.deleteRentalBillById(cmd.getRentalBillId());

	}
 

	@Override
	public OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd) {
		// 
		OnlinePayCallbackCommandResponse response = new OnlinePayCallbackCommandResponse();
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {
			 
			LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
		}
			
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
		{
			RentalBillPaybillMap bpbMap= rentalProvider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
			RentalBill bill = rentalProvider.findRentalBillById(bpbMap.getRentalBillId());
			bill.setPaidMoney(bill.getPaidMoney().add(new java.math.BigDecimal(cmd.getPayAmount())));
			bill.setVendorType(cmd.getVendorType());
			bpbMap.setVendorType(cmd.getVendorType());
			bill.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));

			bpbMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
//			bill.setOperatorUid(UserContext.current().getUser().getId());
			if(bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
				bill.setStatus(SiteBillStatus.RESERVED.getCode());
			}
			else if(bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
				if(bill.getPayTotalMoney().equals(bill.getPaidMoney())){
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
				}
				else{
					LOGGER.error("待付款订单:id ["+bill.getId()+"]付款金额有问题： 应该付款金额："+bill.getPayTotalMoney()+"实际付款金额："+bill.getPaidMoney());
 
				}
			}else if(bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
				LOGGER.error("待付款订单:id ["+bill.getId()+"] 状态已经是成功预约");
			}else{
				LOGGER.error("待付款订单:id ["+bill.getId()+"]状态有问题： 订单状态是："+bill.getStatus());
			}
			rentalProvider.updateRentalBill(bill);
		}
		return response;
	}

	@Override
	public FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(
			FindRentalSiteWeekStatusCommand cmd) {
		 
		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getSiteId());
		FindRentalSiteWeekStatusCommandResponse response = ConvertHelper.convert(rs, FindRentalSiteWeekStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalSitePic> pics = this.rentalProvider.findRentalSitePicsByOwnerTypeAndId(EhRentalSites.class.getSimpleName(), rs.getId());
		if(null!=pics){
			response.setSitePics(new ArrayList<>());
			for(RentalSitePic pic:pics){
				RentalSitePicDTO picDTO=ConvertHelper.convert(pic, RentalSitePicDTO.class);
				picDTO.setUrl(this.contentServerService.parserUri(pic.getUri(), 
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				response.getSitePics().add(picDTO);
			}
		}
		response.setAnchorTime(0L);


		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalSites.class.getSimpleName(),rs.getId());
		if(null!=attachments){
			response.setAttachments(new ArrayList<com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				response.getAttachments().add(ConvertHelper.convert(single, com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO .class));
			}
		}
		// 查rules
		
		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<RentalSiteDayRulesDTO>());
		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			response.getSiteDays().add(dayDto);
			dayDto.setSiteRules(new ArrayList<RentalSiteRulesDTO>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalSiteRule> rentalSiteRules = rentalProvider
					.findRentalSiteRules(cmd.getSiteId(), dateSF.format(new java.util.Date(start.getTimeInMillis())),
							beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),RentalSiteStatus.NORMAL.getCode());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalSiteRule rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setId(rsr.getId()); 
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
						if(response.getAnchorTime().equals(0L)){
							response.setAnchorTime(dto.getBeginTime());
						}else{
							try {
								if(timeSF.parse(timeSF.format(new java.util.Date(response.getAnchorTime()))).after(
										timeSF.parse(timeSF.format(new java.util.Date(dto.getBeginTime()))))){
									response.setAnchorTime(dto.getBeginTime());
								}
							} catch (Exception e) {
								LOGGER.error("anchorTime error  dto = "+ dto );
							}
							
							
						}
					} else if (dto.getRentalType().equals(
							RentalType.HALFDAY.getCode())) {
						dto.setAmorpm(rsr.getAmorpm());
					} 
					dto.setRuleDate(rsr.getSiteRentalDate().getTime());
					List<RentalSitesBill> rsbs = rentalProvider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalSitesBill rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					if (dto.getCounts() == 0) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalEndTime())))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					} else {
						if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rs.getRentalEndTime()))) ){
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}
					dayDto.getSiteRules().add(dto);
	
				}
			}
		}
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteWeekStatusResponse findAutoAssignRentalSiteWeekStatus(
			FindAutoAssignRentalSiteWeekStatusCommand cmd) {

		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getSiteId());
		FindAutoAssignRentalSiteWeekStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteWeekStatusResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalSitePic> pics = this.rentalProvider.findRentalSitePicsByOwnerTypeAndId(EhRentalSites.class.getSimpleName(), rs.getId());
		if(null!=pics){
			response.setSitePics(new ArrayList<>());
			for(RentalSitePic pic:pics){
				RentalSitePicDTO picDTO=ConvertHelper.convert(pic, RentalSitePicDTO.class);
				picDTO.setUrl(this.contentServerService.parserUri(pic.getUri(), 
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				response.getSitePics().add(picDTO);
			}
		}
		response.setAnchorTime(0L);


		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalSites.class.getSimpleName(),rs.getId());
		if(null!=attachments){
			response.setAttachments(new ArrayList<com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				response.getAttachments().add(ConvertHelper.convert(single, com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO .class));
			}
		}
		// 查rules
		
		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<RentalSiteNumberDayRulesDTO>());
		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String,List<RentalSiteRulesDTO>> siteNumberMap=new HashMap<>();
			dayDto.setSiteNumbers(new ArrayList<RentalSiteNumberRuleDTO>()); 
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalSiteRule> rentalSiteRules = rentalProvider
					.findRentalSiteRules(cmd.getSiteId(), dateSF.format(new java.util.Date(start.getTimeInMillis())),
							beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),RentalSiteStatus.NORMAL.getCode());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalSiteRule rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getSiteNumber()));
					dto.setId(rsr.getId()); 
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
						if(response.getAnchorTime().equals(0L)){
							response.setAnchorTime(dto.getBeginTime());
						}else{
							try {
								if(timeSF.parse(timeSF.format(new java.util.Date(response.getAnchorTime()))).after(
										timeSF.parse(timeSF.format(new java.util.Date(dto.getBeginTime()))))){
									response.setAnchorTime(dto.getBeginTime());
								}
							} catch (Exception e) {
								LOGGER.error("anchorTime error  dto = "+ dto );
							}
							
							
						}
					} else if (dto.getRentalType().equals(
							RentalType.HALFDAY.getCode())) {
						dto.setAmorpm(rsr.getAmorpm());
					} 
					dto.setRuleDate(rsr.getSiteRentalDate().getTime());
					List<RentalSitesBill> rsbs = rentalProvider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalSitesBill rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					if (dto.getCounts() == 0) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalEndTime())))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					} else {
						if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
								.getSiteRentalDate().getTime()
								- rs.getRentalEndTime()))) ){
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}
					if(siteNumberMap.get(dto.getSiteNumber())==null)
						siteNumberMap.put(dto.getSiteNumber(), new ArrayList<RentalSiteRulesDTO>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);
				}
			}
			
			//
			for(String siteNumber : siteNumberMap.keySet()){
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}
		}
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteDayStatusResponse findAutoAssignRentalSiteDayStatus(
			FindAutoAssignRentalSiteDayStatusCommand cmd) {
		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getSiteId());
		FindAutoAssignRentalSiteDayStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteDayStatusResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalSitePic> pics = this.rentalProvider.findRentalSitePicsByOwnerTypeAndId(EhRentalSites.class.getSimpleName(), rs.getId());
		if(null!=pics){
			response.setSitePics(new ArrayList<>());
			for(RentalSitePic pic:pics){
				RentalSitePicDTO picDTO=ConvertHelper.convert(pic, RentalSitePicDTO.class);
				picDTO.setUrl(this.contentServerService.parserUri(pic.getUri(), 
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				response.getSitePics().add(picDTO);
			}
		}

		List<RentalConfigAttachment> attachments=this.rentalProvider.queryRentalConfigAttachmentByOwner(EhRentalSites.class.getSimpleName(),rs.getId());
		if(null!=attachments){
			response.setAttachments(new ArrayList<com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO>());
			for(RentalConfigAttachment single:attachments){
				response.getAttachments().add(ConvertHelper.convert(single, com.everhomes.rest.techpark.rental.admin.AttachmentConfigDTO .class));
			}
		}
		response.setAnchorTime(0L);
		//当前时间+预定开始时间 即为可预订开始时间
		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		// 查rules
		 
		    
		Map<String,List<RentalSiteRulesDTO>> siteNumberMap=new HashMap<>();
		response.setSiteNumbers(new ArrayList<RentalSiteNumberRuleDTO>()); 
		 
		List<RentalSiteRule> rentalSiteRules = rentalProvider
				.findRentalSiteRules(cmd.getSiteId(), dateSF.format(new java.util.Date(cmd.getRuleDate() )),
						beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),RentalSiteStatus.NORMAL.getCode());
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			for (RentalSiteRule rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
				dto.setSiteNumber(String.valueOf(rsr.getSiteNumber() ));
				dto.setId(rsr.getId()); 
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
					if(response.getAnchorTime().equals(0L)){
						response.setAnchorTime(dto.getBeginTime());
					}else{
						try {
							if(timeSF.parse(timeSF.format(new java.util.Date(response.getAnchorTime()))).after(
									timeSF.parse(timeSF.format(new java.util.Date(dto.getBeginTime()))))){
								response.setAnchorTime(dto.getBeginTime());
							}
						} catch (Exception e) {
							LOGGER.error("anchorTime error  dto = "+ dto );
						}
						
						
					}
				} else if (dto.getRentalType().equals(
						RentalType.HALFDAY.getCode())) {
					dto.setAmorpm(rsr.getAmorpm());
				} 
				dto.setRuleDate(rsr.getSiteRentalDate().getTime());
				List<RentalSitesBill> rsbs = rentalProvider
						.findRentalSiteBillBySiteRuleId(rsr.getId());
				dto.setStatus(SiteRuleStatus.OPEN.getCode());
				dto.setCounts((double) rsr.getCounts());
				if (null != rsbs && rsbs.size() > 0) {
					for (RentalSitesBill rsb : rsbs) {
						dto.setCounts(dto.getCounts()
								- rsb.getRentalCount());
					}
				}
				if (dto.getCounts() == 0) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
							.getBeginTime().getTime()
							- rs.getRentalStartTime())))) {
						dto.setStatus(SiteRuleStatus.EARLY.getCode());
					}
					if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
							.getBeginTime().getTime()
							- rs.getRentalEndTime())))) {
						dto.setStatus(SiteRuleStatus.LATE.getCode());
					}
				} else {
					if ((null!=rs.getRentalStartTime())&&(reserveTime.before(new java.util.Date(rsr
							.getSiteRentalDate().getTime()
							- rs.getRentalStartTime())))) {
						dto.setStatus(SiteRuleStatus.EARLY.getCode());
					}
					if ((null!=rs.getRentalEndTime())&&(reserveTime.after(new java.util.Date(rsr
							.getSiteRentalDate().getTime()
							- rs.getRentalEndTime()))) ){
						dto.setStatus(SiteRuleStatus.LATE.getCode());
					}
				}
				if(siteNumberMap.get(dto.getSiteNumber())==null)
					siteNumberMap.put(dto.getSiteNumber(), new ArrayList<RentalSiteRulesDTO>());
				siteNumberMap.get(dto.getSiteNumber()).add(dto);
			}
		}
		
		//
		for(String siteNumber : siteNumberMap.keySet()){
			RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
			siteNumberRuleDTO.setSiteNumber(siteNumber);
			siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
			response.getSiteNumbers().add(siteNumberRuleDTO);
		}
		 
		return response;
	}
	@Override
	public RentalBillDTO confirmBill(ConfirmBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
							"too late to order the service"); 
		}
		if (Double.valueOf(0.0).equals(bill.getPayTotalMoney().doubleValue())){
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
			rentalProvider.updateRentalBill(bill);
		}
		else {

			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DID_NOT_PAY,
							"did not pay for the bill ,can not confirm"); 
		}
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO completeBill(CompleteBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (!bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())
				&&!bill.getStatus().equals(SiteBillStatus.OVERTIME.getCode())){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_NOT_SUCCESS, "order is not success order."); 
		} 
		bill.setStatus(SiteBillStatus.COMPLETE.getCode());
		rentalProvider.updateRentalBill(bill);
	 
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO incompleteBill(IncompleteBillCommand cmd) {
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getRentalBillId());
		if (!bill.getStatus().equals(SiteBillStatus.COMPLETE.getCode())){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_NOT_COMPLETE,"order is not complete order."); 
		} 
//		RentalRule rule = this.rentalProvider.getRentalRule(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType());
		RentalSite rs = this.rentalProvider.getRentalSiteById(bill.getRentalSiteId());
		java.util.Date cancelTime = new java.util.Date();
		if (cancelTime.before(new java.util.Date(bill.getEndTime().getTime()))) {
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
		}else{
			bill.setStatus(SiteBillStatus.OVERTIME.getCode());
		}
		
		rentalProvider.updateRentalBill(bill);
	 
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public BatchCompleteBillCommandResponse batchIncompleteBill(BatchIncompleteBillCommand cmd) { 
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<RentalBillDTO>());
		if(null!=cmd.getRentalBillIds())
			for (Long billId : cmd.getRentalBillIds()){
				IncompleteBillCommand cmd2 = ConvertHelper.convert(cmd, IncompleteBillCommand.class);
				cmd2.setRentalBillId(billId);
				response.getBills().add(this.incompleteBill(cmd2));
			}
		return response;
	}

	@Override
	public BatchCompleteBillCommandResponse batchCompleteBill(BatchCompleteBillCommand cmd) {
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<RentalBillDTO>());
		if(null!=cmd.getRentalBillIds())
			for (Long billId : cmd.getRentalBillIds()){
				CompleteBillCommand cmd2 = ConvertHelper.convert(cmd, CompleteBillCommand.class);
				cmd2.setRentalBillId(billId);
				response.getBills().add(this.completeBill(cmd2));
			}
		return response;
	}
	
	@Override
	public ListRentalBillCountCommandResponse listRentalBillCount(ListRentalBillCountCommand cmd) {
		ListRentalBillCountCommandResponse response = new ListRentalBillCountCommandResponse(); 
//		response.setRentalBillCounts(new ArrayList<RentalBillCountDTO>());
//		if(cmd.getRentalSiteId() == null ){
//			List<RentalSite>  sites = this.rentalProvider.findRentalSites(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(),null , null, null,null);
//			for(RentalSite site : sites){
//				response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
//			}
//		}
//		else{
//			RentalSite site = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
//			response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
//			
//		}
		
		return response;
	}
	 

	private void processRentalBillCountDTO(RentalBillCountDTO dto, List<RentalBill> bills ){
		Integer sumCount =0;                    
		Integer completeCount =0;               
		Integer cancelCount=0;                      
		Integer overTimeCount=0;                 
		Integer successCount=0;                  
		for(RentalBill bill : bills){
			sumCount++;
			if(bill.getStatus().equals(SiteBillStatus.COMPLETE.getCode())){
				completeCount++;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.FAIL.getCode())){
				cancelCount ++;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.OVERTIME.getCode())){
				overTimeCount ++ ;
			}
			else 	if(bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
				successCount ++;
			} 
		}
		dto.setSumCount(sumCount);
		dto.setCancelCount(cancelCount);
		dto.setCompleteCount(completeCount);
		dto.setOverTimeCount(overTimeCount);
		dto.setSuccessCount(successCount);
	}

	@Override
	public HttpServletResponse exportRentalBills(ListRentalBillsCommand cmd,
			HttpServletResponse response) {
		
//		int totalCount = rentalProvider.countRentalBills(cmd.getOwnerId(),cmd.getOwnerType(),
//				cmd.getSiteType(), cmd.getRentalSiteId(), cmd.getBillStatus(),
//				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag(),null);
//		if (totalCount == 0)
//			return response;

		Integer pageSize = Integer.MAX_VALUE; 
		List<RentalBill> bills = rentalProvider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), 
				cmd.getRentalSiteId(), new CrossShardListingLocator(), cmd.getBillStatus(), cmd.getVendorType(), pageSize, cmd.getStartTime(), cmd.getEndTime(),
				null, null); 
		if(null == bills){
			bills = new ArrayList<RentalBill>();
		}
		List<RentalBillDTO> dtos = new ArrayList<RentalBillDTO>();
		for (RentalBill bill : bills) {
			RentalBillDTO dto = new RentalBillDTO();
			mappingRentalBillDTO(dto, bill);
			dto.setSiteItems(new ArrayList<SiteItemDTO>());
			List<RentalItemsBill> rentalSiteItems = rentalProvider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			if(null != rentalSiteItems) 
				for (RentalItemsBill rib : rentalSiteItems) {
					SiteItemDTO siDTO = new SiteItemDTO();
					siDTO.setCounts(rib.getRentalCount());
					RentalSiteItem rsItem = rentalProvider
							.findRentalSiteItemById(rib.getRentalSiteItemId());
					if(null != rsItem){
						siDTO.setItemName(rsItem.getName());
					}
					siDTO.setItemPrice(rib.getTotalMoney());
					dto.getSiteItems().add(siDTO);
				}
			
			dtos.add(dto);
		}
		
		URL rootPath = RentalServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "RentalBills"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createRentalBillsBook(filePath, dtos);
		
		return download(filePath,response);
	}
	
	public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            
            // 读取完成删除文件
            if (file.isFile() && file.exists()) {  
                file.delete();  
            } 
        } catch (IOException ex) { 
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
 					RentalServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
 					ex.getLocalizedMessage());
     		 
        }
        return response;
    }
	
	public void createRentalBillsBook(String path,List<RentalBillDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("rentalBill");
		
		this.createRentalBillsBookSheetHead(sheet);
		for (RentalBillDTO dto : dtos ) {
			this.setNewRentalBillsBookRow(sheet, dto);
		}
		
		try {
			FileOutputStream out = new FileOutputStream(path);
			
			wb.write(out);
			wb.close();
			out.close();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}
	}
	
	private void createRentalBillsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("序号");
		row.createCell(++i).setCellValue("名称");
		row.createCell(++i).setCellValue("下单时间");
		row.createCell(++i).setCellValue("使用详情");
		row.createCell(++i).setCellValue("预订人"); 
		row.createCell(++i).setCellValue("总价");
		row.createCell(++i).setCellValue("支付方式");
		row.createCell(++i).setCellValue("订单状态");
	}
	
	private void setNewRentalBillsBookRow(Sheet sheet ,RentalBillDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		//序号
		row.createCell(++i).setCellValue(row.getRowNum());
		//名称 - 资源名称
		row.createCell(++i).setCellValue(dto.getSiteName());
		//下单时间 
		if(null!=dto.getReserveTime())
			row.createCell(++i).setCellValue(datetimeSF.format(new Timestamp(dto.getReserveTime())));
		else 
			row.createCell(++i).setCellValue("");
		//使用详情
		row.createCell(++i).setCellValue(dto.getUseDetail());
		//预约人 
		row.createCell(++i).setCellValue(dto.getUserName());
		//总价 
		if(null != dto.getTotalPrice())
			row.createCell(++i).setCellValue(dto.getTotalPrice().toString());
		else
			row.createCell(++i).setCellValue("0");
		//支付方式
		row.createCell(++i).setCellValue(VendorType.fromCode(dto.getVendorType()).getDescribe());
		//订单状态
		if(dto.getStatus() != null)
			row.createCell(++i).setCellValue(statusToString(dto.getStatus()));
		else
			row.createCell(++i).setCellValue("");
		 
	} 
	private String statusToString(Byte status) {
//		
//		if(status.equals(SiteBillStatus.LOCKED.getCode()))
//			return "待付订金";
//		if(status.equals(SiteBillStatus.RESERVED.getCode()))
//			return "已付定金";
//		if(status.equals(SiteBillStatus.SUCCESS.getCode()))
//			return "已预约";
//		if(status.equals(SiteBillStatus.PAYINGFINAL.getCode()))
//			return "待付款";
//		if(status.equals(SiteBillStatus.FAIL.getCode()))
//			return "已取消";
//		if(status.equals(SiteBillStatus.COMPLETE.getCode()))
//			return "已完成";
//		if(status.equals(SiteBillStatus.OVERTIME.getCode()))
//			return "已过期";
//		if(status.equals(SiteBillStatus.REFUNDED.getCode()))
//			return "已退款";
//		if(status.equals(SiteBillStatus.REFUNDING.getCode()))
//			return "退款中";
		return SiteBillStatus.fromCode(status).getDescribe();
	}

	@Override
	public GetResourceListAdminResponse getResourceList(
			GetResourceListAdminCommand cmd) {
		GetResourceListAdminResponse response = new GetResourceListAdminResponse();
		if(null==cmd.getOrganizationId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid organizationId parameter in the command");
		if(null==cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid launchPadItemId parameter in the command");
		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
//		if(null==cmd.getStatus() || cmd.getStatus().size() == 0){
//			cmd.setStatus(new ArrayList<Byte>());
//			cmd.getStatus().add(RentalSiteStatus.NORMAL.getCode());
//		}
		List<Byte> status = new ArrayList<Byte>();
		status.add(RentalSiteStatus.NORMAL.getCode());
		List<RentalSiteOwner> siteOwners = this.rentalProvider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getOwnerType(), cmd.getOwnerId());
		List<Long> siteIds = new ArrayList<>();
		if(siteOwners !=null)
			for(RentalSiteOwner siteOwner : siteOwners){
				siteIds.add(siteOwner.getRentalSiteId());
			}   
		List<RentalSite> rentalSites = rentalProvider.findRentalSites(
				cmd.getResourceTypeId(), null,
				locator, pageSize,status,siteIds);
		if(null==rentalSites)
			return response;

		Long nextPageAnchor = null;
		if(rentalSites != null && rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(new ArrayList<RentalSiteDTO>());
		
		for (RentalSite rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO =convertRentalSite2DTO(rentalSite);
			
			response.getRentalSites().add(rSiteDTO);
		}
 

		return response;
	}

	@Override
	public void addResource(AddResourceAdminCommand cmd) {
		if(null==cmd.getOrganizationId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid organizationId parameter in the command");
		if(null==cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid launchPadItemId parameter in the command");
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalSite rentalsite = ConvertHelper.convert(cmd, RentalSite.class);
			rentalsite.setStatus(RentalSiteStatus.NORMAL.getCode());
			rentalsite.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rentalsite.setCreatorUid( UserContext.current().getUser().getId());
			Long siteId = rentalProvider.createRentalSite(rentalsite);
			if(cmd.getOwners() != null){
				for(SiteOwnerDTO dto:cmd.getOwners()){
					RentalSiteOwner siteOwner = ConvertHelper.convert(dto, RentalSiteOwner.class);
					siteOwner.setRentalSiteId(siteId);
					this.rentalProvider.createRentalSiteOwner(siteOwner);
				}
			}
			if(cmd.getDetailUris() != null){
				for(String uri:cmd.getDetailUris()){
					RentalSitePic detailPic =new RentalSitePic();
					detailPic.setOwnerType(EhRentalSites.class.getSimpleName());
					detailPic.setOwnerId(siteId);
					detailPic.setUri(uri);
					this.rentalProvider.createRentalSitePic(detailPic);
				}
			}
			return null;
		});
	}

	@Override
	public void updateResource(UpdateResourceAdminCommand cmd) {
		
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalSite rentalsite = this.rentalProvider.getRentalSiteById(cmd.getId()); 
			//TODO: 权限校验
			rentalsite.setSiteName(cmd.getSiteName());
			rentalsite.setSpec(cmd.getSpec());
			rentalsite.setAddress(cmd.getAddress());
			rentalsite.setLatitude(cmd.getLatitude());
			rentalsite.setLongitude(cmd.getLongitude());
			rentalsite.setContactPhonenum(cmd.getContactPhonenum());
			rentalsite.setIntroduction(cmd.getIntroduction());
			rentalsite.setChargeUid(cmd.getChargeUid());
			rentalsite.setCoverUri(cmd.getCoverUri());
			rentalProvider.updateRentalSite(rentalsite);
			this.rentalProvider.deleteRentalSitePicsBySiteId(cmd.getId());
			this.rentalProvider.deleteRentalSiteOwnersBySiteId(cmd.getId());
			if(cmd.getOwners() != null){
				for(SiteOwnerDTO dto:cmd.getOwners()){
					RentalSiteOwner siteOwner = ConvertHelper.convert(dto, RentalSiteOwner.class);
					siteOwner.setRentalSiteId(cmd.getId());
					this.rentalProvider.createRentalSiteOwner(siteOwner);
				}
			}
			if(cmd.getDetailUris() != null){
				for(String uri:cmd.getDetailUris()){
					RentalSitePic detailPic =new RentalSitePic();
					detailPic.setOwnerType(EhRentalSites.class.getSimpleName());
					detailPic.setOwnerId(cmd.getId());
					detailPic.setUri(uri);
					this.rentalProvider.createRentalSitePic(detailPic);
				}
			}
			return null;
		});
	}

	@Override
	public void updateItem(UpdateItemAdminCommand cmd) {
		if(null==cmd.getItemType())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ItemType   parameter in the command : null");
		RentalSiteItem siteItem =this.rentalProvider.getRentalSiteItemById(cmd.getId());
		if(cmd.getItemType().equals(RentalItemType.SALE.getCode()))
			siteItem.setCounts(cmd.getCounts()+siteItem.getCounts());
		else
			siteItem.setCounts(cmd.getCounts());
			
		siteItem.setDefaultOrder(cmd.getDefaultOrder());
		siteItem.setImgUri(cmd.getImgUri());
		siteItem.setItemType(cmd.getItemType());
		siteItem.setName(cmd.getItemName());
		siteItem.setPrice(cmd.getItemPrice());
		rentalProvider.updateRentalSiteItem(siteItem);
	}

	@Override
	public void updateItems(UpdateItemsAdminCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			for(SiteItemDTO dto : cmd.getItemDTOs()){
				UpdateItemAdminCommand cmd2 = ConvertHelper.convert(dto, UpdateItemAdminCommand.class);
				updateItem(cmd2);
			}
			
			return null;
		});
		
	}

	public void updateRSRs(List<RentalSiteRule> changeRentalSiteRules, UpdateRentalSiteRulesAdminCommand cmd){
		if(null==changeRentalSiteRules || changeRentalSiteRules.size()==0)
			return;
		for(RentalSiteRule rsr : changeRentalSiteRules){
			rsr.setPrice(cmd.getPrice());
			rsr.setOriginalPrice(cmd.getOriginalPrice());
			rsr.setHalfsitePrice(cmd.getHalfsitePrice());
			rsr.setHalfsiteOriginalPrice(cmd.getHalfsiteOriginalPrice());
			rsr.setStatus(cmd.getStatus());
			rsr.setCounts(cmd.getCounts());
			this.rentalProvider.updateRentalSiteRule(rsr);
		}
	}
	@Override
	public void updateRentalSiteSimpleRules(
			UpdateRentalSiteRulesAdminCommand cmd) { 

		if(null==cmd.getRuleId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ruleId   parameter in the command");
		RentalSiteRule choseRSR = this.rentalProvider.findRentalSiteRuleById(cmd.getRuleId());
		if(null==choseRSR)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ruleId   parameter in the command");
		
		RentalSite rs=this.rentalProvider.getRentalSiteById(choseRSR.getRentalSiteId());
		if(rs==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"rental resource (site) cannot found ");
		 
		if(null!=rs.getAutoAssign() && rs.getAutoAssign().equals(NormalFlag.NEED)){
			cmd.setCounts(1.0);
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			List<RentalSiteRule> changeRentalSiteRules= null;
			//查询rs，判断是按日按小时还是按半天
			try {
				if(cmd.getLoopType().equals(LoopType.ONLYTHEDAY.getCode())){
				//当天的
				
					if(rs.getRentalType().equals(RentalType.HOUR.getCode()))
						{
							//按小时
							Timestamp beginTime = Timestamp.valueOf(dateSF.format(choseRSR.getBeginTime().getTime())+ " "
									+ String.valueOf( cmd.getBeginTime().intValue())
									+ ":"
									+ String.valueOf((int) ((cmd.getBeginTime() % 1) * 60))
									+ ":00");
							Timestamp endTime = Timestamp.valueOf(dateSF.format(choseRSR.getBeginTime().getTime())+ " "
									+ String.valueOf(cmd.getEndTime().intValue())
									+ ":"
									+ String.valueOf((int) ((cmd.getEndTime() % 1) * 60))
									+ ":00");
							changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),beginTime,endTime,
									null, dateSF.format(choseRSR.getSiteRentalDate()));
						}
					
					else if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())){
						List<Byte> ampmList = new ArrayList<Byte>();
						//0早上1下午2晚上
						if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.AM.getCode());
						if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.PM.getCode());
						
						changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
								ampmList,dateSF.format(choseRSR.getSiteRentalDate()));
					}
					else if(rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
						List<Byte> ampmList = new ArrayList<Byte>();
						//0早上1下午2晚上
						if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.AM.getCode());
						if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.PM.getCode());
						if(AmorpmFlag.NIGHT.getCode()>=cmd.getBeginTime()&&AmorpmFlag.NIGHT.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.NIGHT.getCode());
						changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
								ampmList, dateSF.format(choseRSR.getSiteRentalDate()));
					}
					else if(rs.getRentalType().equals(RentalType.DAY.getCode())){
						 
						changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
								null, dateSF.format(choseRSR.getSiteRentalDate()));
					}
				
					updateRSRs(changeRentalSiteRules, cmd);
				}
			
				else {
					//	每天的
					Calendar chooseCalendar = Calendar.getInstance();
					Calendar start = Calendar.getInstance();
					Calendar end = Calendar.getInstance();
					chooseCalendar.setTime(new Date(choseRSR.getBeginTime().getTime()));
					 
					start.setTime(new Date(cmd.getBeginDate()));
					end.setTime(new Date(cmd.getEndDate()));
					
					while (start.before(end)) {
						Integer weekday = start.get(Calendar.DAY_OF_WEEK);
						Integer monthDay = start.get(Calendar.DAY_OF_MONTH);
						//按周循环的,如果不对就继续循环	
						if(cmd.getLoopType().equals(LoopType.EVERYWEEK.getCode())&&
								!weekday.equals(chooseCalendar.get(Calendar.DAY_OF_WEEK) ))
							continue;
						//按月循环的，如果不对就继续循环
						if(cmd.getLoopType().equals(LoopType.EVERYMONTH.getCode())&&
								!monthDay.equals( chooseCalendar.get(Calendar.DAY_OF_MONTH) ))
							continue;
						//当天的
						if(rs.getRentalType().equals(RentalType.HOUR.getCode())){
							//按小时
							Timestamp beginTime = Timestamp.valueOf(dateSF.format(start.getTime().getTime())+ " "
									+ String.valueOf( cmd.getBeginTime().intValue())
									+ ":"
									+ String.valueOf((int) ((cmd.getBeginTime() % 1) * 60))
									+ ":00");
							Timestamp endTime = Timestamp.valueOf(dateSF.format(start.getTime().getTime())+ " "
									+ String.valueOf(cmd.getEndTime().intValue())
									+ ":"
									+ String.valueOf((int) ((cmd.getEndTime() % 1) * 60))
									+ ":00");
							changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),beginTime,endTime,
									null,null);
						}
						else if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())){
							List<Byte> ampmList = new ArrayList<Byte>();
							//0早上1下午2晚上
							if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
								ampmList.add(AmorpmFlag.AM.getCode());
							if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
								ampmList.add(AmorpmFlag.PM.getCode());
							changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
									ampmList, dateSF.format(new java.util.Date(start.getTimeInMillis())));
						}
						else if(rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
							List<Byte> ampmList = new ArrayList<Byte>();
							//0早上1下午2晚上
							if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
								ampmList.add(AmorpmFlag.AM.getCode());
							if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
								ampmList.add(AmorpmFlag.PM.getCode());
							if(AmorpmFlag.NIGHT.getCode()>=cmd.getBeginTime()&&AmorpmFlag.NIGHT.getCode()<=cmd.getEndTime())
								ampmList.add(AmorpmFlag.NIGHT.getCode());
							changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
									ampmList, dateSF.format(new java.util.Date(start.getTimeInMillis())));
						}
						else if(rs.getRentalType().equals(RentalType.DAY.getCode())){
							 
							changeRentalSiteRules = this.rentalProvider.findRentalSiteRuleByDate(choseRSR.getRentalSiteId(),choseRSR.getSiteNumber(),null,null,
									null, dateSF.format(new java.util.Date(start.getTimeInMillis())));
						}
						updateRSRs(changeRentalSiteRules, cmd);
						start.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
			} catch (java.text.ParseException e) { 
				e.printStackTrace();

				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_SQL_EXCEPTION,
						"error: parse date from string to java.sql.date"); 
			}
			return null;
		});
		
		
	}

	@Override
	public void updateRentalSiteDiscount(
			UpdateRentalSiteDiscountAdminCommand cmd) {
		if(null==cmd.getRentalSiteId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid RentalSiteId   parameter in the command");
		RentalSite rs = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
		if(rs==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"rental resource (site) cannot found ");
		rs.setDiscountType(cmd.getDiscountType());
		rs.setFullPrice(cmd.getFullPrice());
		rs.setCutPrice(cmd.getCutprice());
		this.rentalProvider.updateRentalSite(rs);
	}

	@Override
	public GetRefundOrderListResponse getRefundOrderList(
			GetRefundOrderListCommand cmd) {
 
		GetRefundOrderListResponse response = new GetRefundOrderListResponse();
		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize()); 
//		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<RentalRefundOrder> orders = rentalProvider.getRefundOrderList(cmd.getResourceTypeId(),  
				  locator, cmd.getStatus(), VendorType.fromCode(cmd.getVendorType()).getStyleNo(), pageSize+1, cmd.getStartTime(), cmd.getEndTime()); 
		if(orders==null ||orders.size()==0)
			return response;
		if(orders != null && orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			response.setNextPageAnchor( orders.get(orders.size() -1).getId()); 
		}
		response.setRefundOrders(new ArrayList<RefundOrderDTO>());
		orders.forEach((order)->{
			RefundOrderDTO dto =ConvertHelper.convert(order, RefundOrderDTO.class);
			dto.setVendorType(VendorType.fromStyleNo(order.getOnlinePayStyleNo()).getVendorType());
			User applyer = this.userProvider.findUserById(order.getCreatorUid());
			if(null != applyer){
				dto.setApplyUserName(applyer.getNickName());
				
				UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
				if(null == userIdentifier){
					LOGGER.debug("userIdentifier is null...userId = " + order.getCreatorUid());
				}else{
					dto.setApplyUserContact(userIdentifier.getIdentifierToken());
				}
				 
			}
			else{
				LOGGER.error("user not found userId = " + order.getCreatorUid());
			}
			dto.setApplyTime(order.getCreateTime().getTime());
			response.getRefundOrders().add(dto);
		});
		
		return response;
	}

	@Override
	public RentalBillDTO getRentalBill(GetRentalBillCommand cmd) {
		if(null==cmd.getBillId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : bill id is null");
		RentalBill bill = this.rentalProvider.findRentalBillById(cmd.getBillId());
		if(null==bill)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : bill id can not find bill");
			
		RentalBillDTO dto = new RentalBillDTO();
		mappingRentalBillDTO(dto, bill);
		dto.setSiteItems(new ArrayList<SiteItemDTO>());
		List<RentalItemsBill> rentalSiteItems = rentalProvider
				.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
		if(null!=rentalSiteItems)
			for (RentalItemsBill rib : rentalSiteItems) {
				SiteItemDTO siDTO = new SiteItemDTO();
				siDTO.setCounts(rib.getRentalCount());
				RentalSiteItem rsItem = rentalProvider.findRentalSiteItemById(rib.getRentalSiteItemId());
				if(rsItem != null) {
    				siDTO.setItemName(rsItem.getName());
    				siDTO.setItemPrice(rib.getTotalMoney());
    				dto.getSiteItems().add(siDTO);
				} else {
				    LOGGER.error("Rental site item not found, rentalSiteItemId=" + rib.getRentalSiteItemId() + ", cmd=" + cmd);
				}
			}
		return dto;
	}

	@Override
	public String getRefundUrl(GetRefundUrlCommand cmd) { 
		if(null==cmd.getRefundId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : refund order  id is null"); 
		RentalRefundOrder refundOrder = this.rentalProvider.getRentalRefundOrderById(cmd.getRefundId());
		if(null==refundOrder)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : refund order id  can not find refund order ");
		if(refundOrder.getOnlinePayStyleNo().equals(VendorType.WEI_XIN.getStyleNo()))
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 
					RentalServiceErrorCode.ERROR_REFOUND_ERROR, "refund order is wechat  ");
		PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
		String refoundApi =  this.configurationProvider.getValue("pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
		String appKey = configurationProvider.getValue("pay.appKey", "b86ddb3b-ac77-4a65-ae03-7e8482a3db70");
		refundCmd.setAppKey(appKey);
		Long timestamp = System.currentTimeMillis();
		refundCmd.setTimestamp(timestamp);
		Integer randomNum = (int) (Math.random()*1000);
		refundCmd.setNonce(randomNum); 
		refundCmd.setRefundOrderNo(String.valueOf(refundOrder.getRefundOrderNo()) );
		refundCmd.setOrderNo(String.valueOf(refundOrder.getOrderNo()));
		refundCmd.setOnlinePayStyleNo(refundOrder.getOnlinePayStyleNo()); 
		refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALREFUND.getPycode());
		//已付金额乘以退款比例除以100
		refundCmd.setRefundAmount( refundOrder.getAmount());
		refundCmd.setRefundMsg("预订单取消退款");
		this.setSignatureParam(refundCmd);
		PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refoundApi, refundCmd, PayZuolinRefundResponse.class);
		if(refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
			return refundResponse.getResponse();
		}
		else{
			LOGGER.error("refund order no =["+refundOrder.getRefundOrderNo()+"] refound error param is "+refundCmd.toString());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFOUND_ERROR,
							"bill  refound error"); 
		}	
		 
	}

	@Override
	public GetResourceTypeListResponse getResourceTypeList(
			GetResourceTypeListCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
 
}
