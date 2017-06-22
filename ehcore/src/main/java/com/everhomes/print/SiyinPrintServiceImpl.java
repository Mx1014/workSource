// @formatter:off
package com.everhomes.print;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
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
import com.everhomes.rest.print.PayPrintOrderCommand;
import com.everhomes.rest.print.PayPrintOrderResponse;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.print.PrintImmediatelyCommand;
import com.everhomes.rest.print.PrintJobTypeType;
import com.everhomes.rest.print.PrintLogonStatusType;
import com.everhomes.rest.print.PrintOrderDTO;
import com.everhomes.rest.print.PrintOrderLockType;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.rest.print.PrintOwnerType;
import com.everhomes.rest.print.PrintPaperSizeType;
import com.everhomes.rest.print.PrintSettingColorTypeDTO;
import com.everhomes.rest.print.PrintSettingPaperSizePriceDTO;
import com.everhomes.rest.print.PrintSettingType;
import com.everhomes.rest.print.PrintStatDTO;
import com.everhomes.rest.print.UnlockPrinterCommand;
import com.everhomes.rest.print.UpdatePrintSettingCommand;
import com.everhomes.rest.print.UpdatePrintUserEmailCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.xml.XMLToJSON;

@Component
public class SiyinPrintServiceImpl implements SiyinPrintService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintServiceImpl.class);
	private static final Pattern emailregex = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");    
	private static final String REDIS_PRINT_IDENTIFIER_TOKEN = "print-uid";
	private static final String REDIS_PRINTING_TASK_COUNT = "print-task-count";
	private static final String PRINT_SUBJECT = "print";
	
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
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private BigCollectionProvider bigCollectionProvider;
	
	@Autowired
	private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
	
	@Autowired
	private LocalBus localBus;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrderUtil commonOrderUtil;
	
	@Autowired
	private CoordinationProvider coordinationProvider;

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
		List<PrintJobTypeType> list = new ArrayList<PrintJobTypeType>(Arrays.asList(PrintJobTypeType.values()));
		return new ListPrintJobTypesResponse(list.stream().map(r -> r.getCode()).collect(Collectors.toList()));
	}

	@Override
	public ListPrintOrderStatusResponse listPrintOrderStatus(ListPrintOrderStatusCommand cmd) {
		List<PrintOrderStatusType> list = new ArrayList<PrintOrderStatusType>(Arrays.asList(PrintOrderStatusType.values()));
		return new ListPrintOrderStatusResponse(list.stream().map(r -> r.getCode()).collect(Collectors.toList()));
	
	}

	@Override
	public ListPrintUserOrganizationsResponse listPrintUserOrganizations(ListPrintUserOrganizationsCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		if(cmd.getCreatorUid() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, creatorUid = null");
		}
		ListUserRelatedOrganizationsCommand relatedCmd = new ListUserRelatedOrganizationsCommand();
		User user = new User();
		user.setId(cmd.getCreatorUid());
		List<OrganizationSimpleDTO> list = organizationService.listUserRelateOrgs(relatedCmd, user);
		return new ListPrintUserOrganizationsResponse(list.stream().map(r->r.getName()).collect(Collectors.toList()));
	}

	@Override
	public void updatePrintUserEmail(UpdatePrintUserEmailCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkEmailFormat(cmd.getEmail());
		SiyinPrintEmail siyinPrintEmail = processSiyinPrintEmail(cmd);
		if(cmd.getId() == null){
			siyinPrintEmailProvider.createSiyinPrintEmail(siyinPrintEmail);
		}else{
			siyinPrintEmailProvider.updateSiyinPrintEmail(siyinPrintEmail);
		}
	}

	@Override
	public GetPrintUserEmailResponse getPrintUserEmail(GetPrintUserEmailCommand cmd) {
		return ConvertHelper.convert(siyinPrintEmailProvider.
				findSiyinPrintEmailByUserId(UserContext.current().getUser().getId()),GetPrintUserEmailResponse.class);
	}

	@Override
	public GetPrintLogonUrlResponse getPrintLogonUrl(GetPrintLogonUrlCommand cmd) {
		// TODO 再商议
		//将identifierToken丢到redis
		String identifierToken = UUID.randomUUID().toString();
        String key = REDIS_PRINT_IDENTIFIER_TOKEN + identifierToken;
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        
        String timeunit = configurationProvider.getValue(PrintErrorCode.PRINT_SIYIN_TIMEOUT_UNIT, "MINUTES");
        int timeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_SIYIN_TIMEOUT, 10);
        int scanTimeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_LOGON_SCAN_TIMOUT, 10000);
        TimeUnit unit = getTimeUnit(timeunit);
        
        valueOperations.set(key, identifierToken, timeout, unit);
        
        //将通知登陆的接口返回给前端
        String logonURL = configurationProvider.getValue(PrintErrorCode.PRINT_INFORM_URL, "") + identifierToken;
        GetPrintLogonUrlResponse response = new GetPrintLogonUrlResponse();
        response.setLogonURL(logonURL);
        response.setIdentifierToken(identifierToken);
        response.setScanTimes(timeout*1000*getScale(unit)/scanTimeout);;
        return response;
	}

	/**
	 * 单位转秒
	 */
	private int getScale(TimeUnit unit) {
		switch (unit) {
		case SECONDS:
			return 1;
		case MINUTES:
			return 60;
		case HOURS:
			return 3600;
		}
		return 60;
	}


	private TimeUnit getTimeUnit(String timeunit) {
		//秒 SECONDS/分 MINUTES/小时 HOURS
		timeunit = timeunit.toUpperCase();
		switch (timeunit) {
		case "SECONDS":
			return TimeUnit.SECONDS;
		case "MINUTES":
			return TimeUnit.MINUTES;
		case "HOURS":
			return TimeUnit.HOURS;
		}
		return TimeUnit.MINUTES;
	}


	@Override
	public DeferredResult<RestResponse> logonPrint(LogonPrintCommand cmd) {
		// TODO 
		//不知道这里对不对
		DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
		RestResponse response =  new RestResponse();
		String subject = PRINT_SUBJECT;
		int scanTimeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_LOGON_SCAN_TIMOUT, 10000);
		localBusSubscriberBuilder.build(subject + "." + cmd.getIdentifierToken(), new LocalBusOneshotSubscriber() {
		    @Override
		    public Action onLocalBusMessage(Object sender, String subject,
		                                    Object logonResponse, String path) {
		        //这里可以清掉redis的uid
		    	String key = REDIS_PRINT_IDENTIFIER_TOKEN + cmd.getIdentifierToken();
		    	deleteValueOperations(key);
		    	
//		    	ValueOperations<String, String> valueOperations = getValueOperations(key);
//		    	Object object = valueOperations.get(key);
//		    	
//		    	LOGGER.info("object is {}" + object);
		    	
		        deferredResult.setResult((RestResponse)logonResponse);
		        return null;
		    }
		    @Override
		    public void onLocalBusListeningTimeout() {
		        response.setResponseObject("print logon timed out");
		        response.setErrorCode(408);
		        deferredResult.setResult(response);
		    }
		
		}).setTimeout(scanTimeout).create();
		
		return deferredResult;
	}

	@Override
	public InformPrintResponse informPrint(InformPrintCommand cmd) {
		//验证redis中存的identifierToken
        String key = REDIS_PRINT_IDENTIFIER_TOKEN + cmd.getIdentifierToken();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        
        RestResponse printResponse = new RestResponse();
        User user = UserContext.current().getUser();
        if(null != valueOperations.get(key)){
            printResponse.setResponseObject(user);
            printResponse.setErrorCode(ErrorCodes.SUCCESS);
        }else{
            printResponse.setResponseObject("identifierToken "+cmd.getIdentifierToken()+" time out");
            printResponse.setErrorCode(409);
        }

        String subject = PRINT_SUBJECT;

        // 必须重启一个线程来发布通知，通知二维码扫描成功，跳转到成功页面
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                localBus.publish(null, subject + "." + cmd.getIdentifierToken(), printResponse);
            }
        });

        // TODO 逻辑，通知app
        return new InformPrintResponse(checkUnpaidOrder());
    
	}

	/**
	 * 获取key在redis操作的valueOperations
	 */
	private ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations;
	}
	
	/**
	 * 清除redis中key的缓存
	 */
	private void deleteValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		redisTemplate.delete(key);
	}


	@Override
	public void printImmediately(PrintImmediatelyCommand cmd) {
		Long id = UserContext.current().getUser().getId();
		
		PrintLogonStatusType statusType = PrintLogonStatusType.fromCode(checkUnpaidOrder());
		if(statusType == PrintLogonStatusType.HAVE_UNPAID_ORDER){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "have unpaid orders");
		}
		
		//做计数
        String key = REDIS_PRINTING_TASK_COUNT + id;
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        
        String value = valueOperations.get(key);
        if(value == null){
        	value = "0";
        }
        
        //计算值
        valueOperations.set(key, String.valueOf((Integer.parseInt(value)+1)), 30, TimeUnit.MINUTES);
//        unlockPrinter(cmd.);

	}

	@Override
	public ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd) {
		
		Long userId = UserContext.current().getUser().getId();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<SiyinPrintOrder> printOrdersList = siyinPrintOrderProvider.listSiyinPrintOrderByUserId(userId,pageSize+1,cmd.getPageAnchor());
		
		ListPrintOrdersResponse response = new ListPrintOrdersResponse();
		if(printOrdersList == null)
			return response;
		if(printOrdersList.size() > pageSize){
			response.setNextPageAnchor(printOrdersList.get(printOrdersList.size()-1).getId());
			printOrdersList.remove(printOrdersList.size()-1);
		}
		response.setPrintOrdersList(printOrdersList.stream().map(r->ConvertHelper.convert(r, PrintOrderDTO.class)).collect(Collectors.toList()));
		return response;
	}

	@Override
	public GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd) {
		return new GetPrintUnpaidOrderResponse(checkUnpaidOrder());
	}

	@Override
	public CommonOrderDTO payPrintOrder(PayPrintOrderCommand cmd) {
		//检查订单id是否存在，是否已经是  已支付状态
		SiyinPrintOrder order = checkPrintOrder(cmd.getOrderId());
		
		//检查订单是否被锁定
		//没有被锁定的订单，锁定他
		PrintOrderLockType lockType = PrintOrderLockType.fromCode(order.getLockFlag());
		if(lockType == PrintOrderLockType.UNLOCKED){
			order = lockOrder(cmd.getOrderId());
		}
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(PrintJobTypeType.fromCode(order.getJobType()).toString());
		orderCmd.setOrderNo(order.getOrderNo().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PRINT_ORDER.getPycode());
		orderCmd.setSubject(getLocalActivityString(PrintErrorCode.PRINT_SUBJECT,"打印订单"));
		
		//加一个开关，方便在beta环境测试
		boolean flag = configurationProvider.getBooleanValue("beta.print.order.amount", false);
		if(flag) {
			orderCmd.setTotalFee(new BigDecimal(0.02).setScale(2, RoundingMode.FLOOR));
		} else {
			orderCmd.setTotalFee(order.getOrderTotalAmount());
		}
    	

		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"convertToCommonOrder is fail.");
		}
		
		return dto;
		
	}

	private SiyinPrintOrder lockOrder(Long orderId) {
		//这里必须锁定，因为存在同时去合并打印记录到订单的情况，参考 /siyinprint/jobLogNotification ，
		//此处需要合并打印记录到订单。也需要用到此 CoordinationLocks.PRINT_ORDER_LOCK_FLAG 锁。
		Tuple<SiyinPrintOrder,Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.PRINT_ORDER_LOCK_FLAG.getCode()).enter(new Callable<SiyinPrintOrder>() {
			public SiyinPrintOrder call() throws Exception {
				siyinPrintOrderProvider.updateSiyinPrintOrderLockFlag(orderId,PrintOrderLockType.LOCKED.getCode());
				return siyinPrintOrderProvider.findSiyinPrintOrderById(orderId);
			}
		});
		if(!tuple.second()){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_LOCK_FAILED,
					"paid error, can not lock order, id = "+orderId);
		}
		return tuple.first();
	}


	private SiyinPrintOrder checkPrintOrder(Long orderId) {
		SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderById(orderId);
		if(order == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unknown orderId = "+orderId);
		}
		PrintOrderStatusType orderStatus = PrintOrderStatusType.fromCode(order.getOrderStatus());
		if(orderStatus == PrintOrderStatusType.PAID){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Have paid orderId = "+orderId);
		}
		return order;
	}


	@Override
	public ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd) {
		Long id = UserContext.current().getUser().getId();
		
		//做计数
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        String key = REDIS_PRINTING_TASK_COUNT + id;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        
        String value = valueOperations.get(key);
        if(value == null){
        	value = "0";
        }
		return new ListPrintingJobsResponse(Integer.parseInt(value));
	}
	
	@Override
	public void unlockPrinter(UnlockPrinterCommand cmd) {
		unlockPrinter(cmd.getReaderName(),false);
	}
	
	public void unlockPrinter(String readerName, boolean isDirectPrint) {
        String siyinUrl =  configurationProvider.getValue(PrintErrorCode.PRINT_SIYIN_SERVERURL, "http://siyin.zuolin.com:8119");
        String moduleIp = getSiyinModuleIp(siyinUrl, readerName);
        String loginData = getLoginData(siyinUrl,readerName);
        
        SiyinPrintEmail siyinPrintEmail = siyinPrintEmailProvider.findSiyinPrintEmailByUserId(UserContext.current().getUser().getId());
        
        //用户设置了邮箱，上传给司印服务器
        if(siyinPrintEmail!=null){
        	loginData = loginData.replace("<email_address />", "<email_address>"+siyinPrintEmail.getEmail()+"</email_address>");
        }
        //用户打印，则设置直接打印，上传给司印
        if(isDirectPrint){
        	loginData = loginData.replace("<mfp_direct_print>NO</mfp_direct_print>", "<mfp_direct_print>YES</mfp_direct_print>");
		}
        String resultJson = XMLToJSON.convertStandardJson(loginData);
        LOGGER.info("siyin api:/console/loginListener resultJson:{}", resultJson);

        //根据readName获取到 登录的端口 登录的 上下文地址
        SiyinPrintPrinter printer = siyinPrintPrinterProvider.findSiyinPrintPrinterByReadName(readerName);
        if(printer == null){
        	LOGGER.error("Unknown readerName = {}, register on table eh_siyin_print_printers",readerName);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unknown readerName = "+readerName);
        }
        directLogin(moduleIp,printer,loginData);
	}
	
	/**
	 * 解锁打印机
	 */
	private void directLogin(String moduleIp, SiyinPrintPrinter printer, String loginData) {
		Map<String, String> params = new HashMap<>();
		params.put("reader_name", printer.getReaderName());
		params.put("action", "QueryModule");
		params.put("login_data", loginData);
		String url = "http://" + moduleIp  + ":" + printer.getModulePort() + printer.getLoginContext();
		String result;
		try {
			result = HttpUtils.post(url, params, 30);
			String siyinCode = getSiyinCode(result);
			if(!siyinCode.equals("OK")){
				LOGGER.error("siyin api:"+url+"request failed : "+result);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, printer.getLoginContext()+" request failed, message = "+result);
			}
		} catch (IOException e) {
			LOGGER.error("siyin api:"+url+" request exception : "+e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, printer.getLoginContext()+" return exception, message = "+e.getMessage());
		}
		
	}


	/**
	 * 获取用于解锁登录的xml
	 */
	private String getLoginData(String siyinUrl, String readerName) {
		 User user = UserContext.current().getUser();
		 Map<String, String> params = new HashMap<>();
	     params.put("login_account", user.getId().toString());
//	     params.put("login_password", user.getPasswordHash());
	     params.put("reader_name", readerName);
	     params.put("login_domain", "Sysprint_OAuth");
	     params.put("language", "zh-cn");
	     String result = null;
		try {
			result = HttpUtils.post(siyinUrl + "/console/loginListener", params, 30);
			String siyinCode = getSiyinCode(result);
			if(!siyinCode.equals("OK")){
				LOGGER.error("siyin api:/console/loginListener request failed, result = {} ",result);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/loginListener return failed, result = "+result);
			}
		} catch (IOException e) {
			LOGGER.error("siyin api:/console/loginListener request exception : "+e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/loginListener return exception, message = "+e.getMessage());
		}
         	
         return getSiyinData(result);
	}


	/**
	 *  获取模块ip
	 */
	private String getSiyinModuleIp(String siyinUrl, String readerName) {
        Map<String, String> params = new HashMap<>();
        params.put("reader_name", readerName);
        params.put("action", "QueryModule");
        String result;
		try {
			result = HttpUtils.post(siyinUrl + "/console/mfpModuleManager", params, 30);
			String siyinCode = getSiyinCode(result);
			if(!siyinCode.equals("OK")){
				LOGGER.error("siyin api:/console/mfpModuleManager request failed, result = {} ",result);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/mfpModuleManager return failed, result = "+result);
			}
		} catch (IOException e) {
			LOGGER.error("siyin api:/console/mfpModuleManager request exception : "+e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/mfpModuleManager return exception, message = "+e.getMessage());
			
		}
		  
		return getSiyinData(result);
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
		BigDecimal defaultdecimal = new BigDecimal(configurationProvider.getValue(PrintErrorCode.PRINT_DEFAULT_PRICE,"0.1"));
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
	
	 private String getLocalActivityString(String code,String defaultText){
		LocaleString localeString = localeStringProvider.find(PrintErrorCode.SCOPE, code, UserContext.current().getUser().getLocale());
		if (localeString != null) {
			return localeString.getText();
		}
		return defaultText;
	 }
	 
	 private String getLocalActivityString(String code){
		 return getLocalActivityString(code,"");
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
	
	/**
	 * 邮箱格式校验
	 */
	private void checkEmailFormat(String email) {
		 Matcher matcher = emailregex.matcher(email);    
		 boolean isMatched = matcher.matches();   
		 if(!isMatched){
			 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, email format error, email = "+email);
		 }
	}

	/**
	 * 检查用户存在未支付的订单
	 */
	private Byte checkUnpaidOrder() {
		User user = UserContext.current().getUser();
		
		List<SiyinPrintOrder> list = siyinPrintOrderProvider.listSiyinPrintUnpaidOrderByUserId(user.getId());
		
		if(list == null || list.size() == 0){
			return PrintLogonStatusType.LOGON_SUCCESS.getCode();
		}
		return PrintLogonStatusType.HAVE_UNPAID_ORDER.getCode();
	}
	
	/**
	 * 
	 */
	private SiyinPrintEmail processSiyinPrintEmail(UpdatePrintUserEmailCommand cmd) {
		SiyinPrintEmail siyinPrintEmail = ConvertHelper.convert(cmd, SiyinPrintEmail.class);
		if(cmd.getId()!=null){
			siyinPrintEmail = siyinPrintEmailProvider.findSiyinPrintEmailById(cmd.getId());
			siyinPrintEmail.setEmail(cmd.getEmail());
			siyinPrintEmail.setStatus(CommonStatus.ACTIVE.getCode());
			return siyinPrintEmail;
		}
		siyinPrintEmail.setNamespaceId(UserContext.getCurrentNamespaceId());
		siyinPrintEmail.setUserId(UserContext.current().getUser().getId());
		siyinPrintEmail.setStatus(CommonStatus.ACTIVE.getCode());
		return siyinPrintEmail;
	}
	public String getSiyinCode(String result){
        return result.substring(0, result.indexOf(":"));
    }

    public String getSiyinData(String result){
        return result.substring(result.indexOf(":") + 1);
    }
}
