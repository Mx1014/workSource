// @formatter:off
package com.everhomes.print;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.*;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.http.HttpUtils;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.qrcode.QRCodeController;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.gorder.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.gorder.order.BusinessPayerType;
import com.everhomes.rest.gorder.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.gorder.order.OrderErrorCode;
import com.everhomes.rest.gorder.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.order.*;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.print.*;
import com.everhomes.rest.qrcode.GetQRCodeImageCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeHandler;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.util.xml.XMLToJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import com.everhomes.order.PayService;

/**
 * 
 *  @author:dengs 2017年6月22日
 */
@Component
public class SiyinPrintServiceImpl implements SiyinPrintService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintServiceImpl.class);
	private static final Pattern emailregex = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");    
	private static final String REDIS_PRINT_IDENTIFIER_TOKEN = "print-uid";
	private static final String BIZ_ORDER_NUM_SPILT = "_";
	public static final String BIZ_ACCOUNT_PRE = "NS";
	//redis记录用户打印任务的数量
	public static final String REDIS_PRINTING_TASK_COUNT = "print-task-count";
	//redis中存储的验证打印记录的时间点，
	public static final String REDIS_PRINT_JOB_CHECK_TIME = "redis_print_job_check_time";
	private static final String PRINT_SUBJECT = "print";
	//用户登录司印使用的用户id-园区id的分割字符串。
//	public static final String PRINT_LOGON_ACCOUNT_SPLIT = "-";
	public static final String PRINT_LOGON_ACCOUNT_SPLIT = "_";
	public static final String PRINT_COMPANY_SPLIT = ",";
	
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
	private BusBridgeProvider busBridgeProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrderUtil commonOrderUtil;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Autowired
	private SiyinJobValidateServiceImpl siyinJobValidateServiceImpl;

//	@Autowired
//	private PayService payService;
	
	@Autowired
	private SiyinUserPrinterMappingProvider siyinUserPrinterMappingProvider;
	
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private ConfigurationProvider configProvider;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private QRCodeController qrController;
	@Autowired
	private QRCodeService qrcodeService;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	public SiyinPrintBusinessPayeeAccountProvider siyinBusinessPayeeAccountProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	public NamespaceProvider namespaceProvider;
    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    protected GeneralOrderService orderService;
    
    @Value("${server.contextPath:}")
    private String contextPath;
	
	@Override
	public GetPrintSettingResponse getPrintSetting(GetPrintSettingCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4140041430L, cmd.getAppId(), null,cmd.getCurrentProjectId());//打印设置权限
		}
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
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4140041420L, cmd.getAppId(), null,cmd.getCurrentProjectId());//打印统计权限
		}
		PrintOwnerType printOwnerType = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		Map<String,List<Object>> map = getCommunitiesByOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		//查询订单
		Timestamp startTime = cmd.getStartTime() == null?null:new Timestamp(cmd.getStartTime());
		Timestamp endTime = cmd.getEndTime() == null?null:new Timestamp(cmd.getEndTime());
		List<SiyinPrintOrder> orders = siyinPrintOrderProvider.listSiyinPrintOrder(startTime, endTime, map.get("ownerTypeList"), map.get("ownerIdList"));
		
		//计算订单
		return processGetPrintStatResponse(orders);
	}

	@Override
	public ListPrintRecordsResponse listPrintRecords(ListPrintRecordsCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4140041410L, cmd.getAppId(), null,cmd.getCurrentProjectId());//订单记录权限
		}
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		Timestamp starttime =cmd.getStartTime() == null?null:new Timestamp(cmd.getStartTime());
		
		Timestamp endtime = cmd.getEndTime() == null?null:new Timestamp(cmd.getEndTime());
		
		Map<String,List<Object>> map = getCommunitiesByOrg(cmd.getOwnerType(), cmd.getOwnerId());
		
		List<SiyinPrintOrder> printOrdersList = siyinPrintOrderProvider.listSiyinPrintOrderByOwners(map.get("ownerTypeList"),map.get("ownerIdList"),starttime
				,endtime,cmd.getJobType(),cmd.getOrderStatus(),cmd.getKeywords(),cmd.getPageAnchor(),pageSize+1);
		
		ListPrintRecordsResponse response = new ListPrintRecordsResponse();
		if(printOrdersList!=null && printOrdersList.size()>pageSize){
			response.setNextPageAnchor(printOrdersList.get(printOrdersList.size()-1).getId());
			printOrdersList.remove(printOrdersList.size()-1);
		}
		response.setPrintRecordsList(printOrdersList.stream().map(r->{
			PrintRecordDTO dto = ConvertHelper.convert(r, PrintRecordDTO.class);
			dto.setCreatorCompanys(new ArrayList<String>(Arrays.asList(r.getCreatorCompany().split(PRINT_COMPANY_SPLIT))));
			return dto;
		}).collect(Collectors.toList()));
		return response;
	}

	//根据管理公司，获取到园区
	private Map<String, List<Object>> getCommunitiesByOrg(String ownerType,Long ownerId) {
		PrintOwnerType printOwnerType = checkOwner(ownerType, ownerId);
		List<Object> ownerTypeList = new ArrayList<Object>();
		List<Object> ownerIdList = new ArrayList<Object>();
		
		if(printOwnerType == PrintOwnerType.ENTERPRISE){
			List<OrganizationCommunity> list = organizationProvider.listOrganizationCommunities(ownerId);
			if(list == null || list.size() == 0){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, organizationId = {}, have empty community",ownerType);
			}
			list.forEach(r -> {
				ownerIdList.add(r.getCommunityId());
				ownerTypeList.add(PrintOwnerType.COMMUNITY.getCode());
			});
		}else{
			ownerTypeList.add(ownerType);
			ownerIdList.add(ownerId);
		}
		Map<String, List<Object>> map = new HashMap<>();
		map.put("ownerIdList", ownerIdList);
		map.put("ownerTypeList", ownerTypeList);
		return map;
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
        String logonURL = configurationProvider.getValue(PrintErrorCode.PRINT_INFORM_URL, "");
        GetPrintLogonUrlResponse response = new GetPrintLogonUrlResponse();
        response.setIdentifierToken(identifierToken);
        response.setType("pc");
		response.setBase64(Base64.getEncoder().encodeToString(response.toString().getBytes()));
		response.setScanTimes(timeout*1000*getScale(unit)/scanTimeout);
		return response;
	}

	@Override
	public DeferredResult<RestResponse> logonPrint(String identifierToken) {
		//不知道这里对不对
		DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
		RestResponse response =  new RestResponse();
		String subject = PRINT_SUBJECT;
		int scanTimeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_LOGON_SCAN_TIMOUT, 10000);
		localBusSubscriberBuilder.build(subject + "." + identifierToken, new LocalBusOneshotSubscriber() {
		    @Override
		    public Action onLocalBusMessage(Object sender, String subject,
		                                    Object logonResponse, String path) {
		        //这里可以清掉redis的uid
		    	String key = REDIS_PRINT_IDENTIFIER_TOKEN + identifierToken;
		    	deleteValueOperations(key);
				String response = (String)logonResponse;
		        deferredResult.setResult(JSONObject.parseObject(response, RestResponse.class));
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
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		if(PrintLogonStatusType.HAVE_UNPAID_ORDER == checkUnpaidOrder(cmd.getOwnerType(), cmd.getOwnerId())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "have unpaid orders");
		}

		RestResponse printResponse = new RestResponse();
		try {
			//验证redis中存的identifierToken
			String key = REDIS_PRINT_IDENTIFIER_TOKEN + cmd.getIdentifierToken().trim();
			ValueOperations<String, String> valueOperations = getValueOperations(key);

			User user = UserContext.current().getUser();

			if (valueOperations.get(key) != null && valueOperations.get(key).length() > 0) {
				User logonUser = new User();
				//这里设置accoutname 为用户id-namespaceid-拥有者id，因为在jobLogNotification
				//中计算价格的时候，不知道所在的园区，所以只能依靠
				logonUser.setAccountName(user.getId() + PRINT_LOGON_ACCOUNT_SPLIT + cmd.getOwnerId());
				printResponse.setResponseObject(logonUser);
				printResponse.setErrorCode(ErrorCodes.SUCCESS);
				//添加正在打印的数量
				addPrintingTaskCount(cmd);
				return new InformPrintResponse(PrintLogonStatusType.LOGON_SUCCESS.getCode());
			}
			printResponse.setResponseObject("identifierToken " + cmd.getIdentifierToken() + " out of date");
			printResponse.setErrorCode(409);
			return new InformPrintResponse(PrintLogonStatusType.HAVE_UNPAID_ORDER.getCode());
		}finally {
			LOGGER.info("subject = {}.{}, print response = {}", PRINT_SUBJECT, cmd.getIdentifierToken(),JSONObject.toJSONString(printResponse));
			ExecutorUtil.submit(new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
						localBusSubscriber.onLocalBusMessage(null, PRINT_SUBJECT + "." + cmd.getIdentifierToken(), JSONObject.toJSONString(printResponse), null);
					} catch (Exception e) {
						LOGGER.error("submit LocalBusSubscriber {}.{} got excetion", PRINT_SUBJECT, cmd.getIdentifierToken(), e);
					}
					localBus.publish(null, PRINT_SUBJECT + "." + cmd.getIdentifierToken(), JSONObject.toJSONString(printResponse));
				}
			},"subscriberPrint"));
		}

	}

	//正在进行的任务计数
	public void addPrintingTaskCount(InformPrintCommand cmd) {
		Long id = UserContext.current().getUser().getId();
		
		PrintLogonStatusType statusType = checkUnpaidOrder(cmd.getOwnerType(), cmd.getOwnerId());
		if(statusType == PrintLogonStatusType.HAVE_UNPAID_ORDER){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "have unpaid orders");
		}
		
		//做计数
        String key = REDIS_PRINTING_TASK_COUNT + id+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        
        String value = valueOperations.get(key);
        if(value == null){
        	value = "0";
        }
        
        //计算值,10分钟有效
        int timeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_SIYIN_JOB_COUNT_TIMEOUT, 10);
        valueOperations.set(key, String.valueOf((Integer.parseInt(value)+1)), timeout, TimeUnit.MINUTES);

	}

	@Override
	public ListPrintOrdersResponse listPrintOrders(ListPrintOrdersCommand cmd) {
		
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		Long userId = UserContext.current().getUser().getId();
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		
		List<SiyinPrintOrder> printOrdersList = siyinPrintOrderProvider.listSiyinPrintOrderByUserId(userId,pageSize+1,cmd.getPageAnchor(),cmd.getOwnerType(), cmd.getOwnerId());
		
		ListPrintOrdersResponse response = new ListPrintOrdersResponse();
		if(printOrdersList == null)
			return response;
		if(printOrdersList.size() > pageSize){
			response.setNextPageAnchor(printOrdersList.get(printOrdersList.size()-1).getId());
			printOrdersList.remove(printOrdersList.size()-1);
		}
		response.setPrintOrdersList(printOrdersList.stream().map(r->{
			PrintOrderDTO dto = ConvertHelper.convert(r, PrintOrderDTO.class);
			dto.setOrderTotalAmount(r.getOrderTotalFee());
			return dto;
		}).collect(Collectors.toList()));
		return response;
	}

	@Override
	public GetPrintUnpaidOrderResponse getPrintUnpaidOrder(GetPrintUnpaidOrderCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		GetPrintUnpaidOrderResponse r = new GetPrintUnpaidOrderResponse();
		r.setExistFlag(checkUnpaidOrder(cmd.getOwnerType(), cmd.getOwnerId(),r).getCode());
		return r;
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

		//锁定了，金额为0，设置为已支付
		if(order.getOrderTotalFee() == null || order.getOrderTotalFee().compareTo(new BigDecimal(0)) == 0){
			order.setOrderStatus(PrintOrderStatusType.PAID.getCode());
			order.setLockFlag(PrintOrderLockType.LOCKED.getCode());
			siyinPrintOrderProvider.updateSiyinPrintOrder(order);
			return null;
		}

		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(PrintJobTypeType.fromCode(order.getJobType()).getDescribe());
		orderCmd.setOrderNo(order.getOrderNo().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PRINT_ORDER.getPycode());
		orderCmd.setSubject(getLocalActivityString(PrintErrorCode.PRINT_SUBJECT,"打印订单"));
		
		//加一个开关，方便在beta环境测试
		boolean flag = configurationProvider.getBooleanValue("beta.print.order.amount", false);
		if(flag) {
			orderCmd.setTotalFee(new BigDecimal(0.02).setScale(2, RoundingMode.FLOOR));
		} else {
			orderCmd.setTotalFee(order.getOrderTotalFee());
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

	@Override
	public PreOrderDTO payPrintOrderV2(PayPrintOrderCommandV2 cmd) {
		//检查订单id是否存在，是否已经是  已支付状态
		SiyinPrintOrder order = checkPrintOrder(cmd.getOrderId());
		if(order.getPayDto()!=null && order.getPayDto().length()>0){
			PreOrderDTO preOrder = (PreOrderDTO)StringHelper.fromJsonString(order.getPayDto(), PreOrderDTO.class);
			return preOrder;
		}

		//检查订单是否被锁定
		//没有被锁定的订单，锁定他
		PrintOrderLockType lockType = PrintOrderLockType.fromCode(order.getLockFlag());
		if(lockType == PrintOrderLockType.UNLOCKED){
			order = lockOrder(cmd.getOrderId());
		}

		//锁定了，金额为0，设置为已支付
		if(order.getOrderTotalFee() == null || order.getOrderTotalFee().compareTo(new BigDecimal(0)) == 0){
			order.setOrderStatus(PrintOrderStatusType.PAID.getCode());
			order.setLockFlag(PrintOrderLockType.LOCKED.getCode());
			siyinPrintOrderProvider.updateSiyinPrintOrder(order);
			return null;
		}
		
        //3、收款方是否有会员，无则报错
		Long bizPayeeId = getOrderPayeeAccount(cmd);
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(Stream.of(bizPayeeId).collect(Collectors.toList()));
        if (payUserDTOs == null || payUserDTOs.size() == 0){
            LOGGER.error("payeeUserId no find, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001,
                    "暂未绑定收款账户");
        }

        //4、组装报文，发起下单请求
        PurchaseOrderCommandResponse orderCommandResponse = createOrder(cmd, order, bizPayeeId);

        //5、组装支付方式
        PreOrderDTO preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);


        //6、保存订单信息
        order.setPayDto(StringHelper.toJsonString(preOrderDTO));
        order.setGeneralOrderId(orderCommandResponse.getPayResponse().getBizOrderNum());
		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
		
		//oldMethod();
		return preOrderDTO;
	}
	
	private Long getOrderPayeeAccount(PayPrintOrderCommandV2 cmd) {
		SiyinPrintBusinessPayeeAccount account = siyinBusinessPayeeAccountProvider
				.getSiyinPrintBusinessPayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if (null == account) {
			return null;
		}
		
		return account.getPayeeId();
	}


	private void oldMethod() {
//		Long amount = order.getOrderTotalFee().multiply(new BigDecimal(100)).longValue();
//		Integer namespaceId = cmd.getNamespaceId();
//		if(namespaceId == null){
//			namespaceId = UserContext.getCurrentNamespaceId();
//		}
//
//		User user = UserContext.current().getUser();
//		String sNamespaceId = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();		//todoed
//		TargetDTO userTarget = userProvider.findUserTargetById(user.getId());
//		ListBizPayeeAccountDTO payerDto = siyinPrintOrderProvider.createPersonalPayUserIfAbsent(user.getId() + "",
//				sNamespaceId, userTarget.getUserIdentifier(),null, null, null);
//		List<SiyinPrintBusinessPayeeAccount> payeeAccounts = siyinBusinessPayeeAccountProvider.findRepeatBusinessPayeeAccounts(null,namespaceId,
//				order.getOwnerType(), order.getOwnerId());
//		if(payeeAccounts==null || payeeAccounts.size()==0){
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"未设置收款方账号");
//		}
//		CreateOrderCommand createOrderCommand = new CreateOrderCommand();
//		createOrderCommand.setAccountCode(sNamespaceId);
//		createOrderCommand.setBizOrderNum(generateBizOrderNum(sNamespaceId,OrderType.OrderTypeEnum.PRINT_ORDER.getPycode(),order.getOrderNo()));
//		createOrderCommand.setClientAppName(cmd.getClientAppName());//todoed
//		createOrderCommand.setPayerUserId(payerDto.getAccountId());
//		createOrderCommand.setPayeeUserId(payeeAccounts.get(0).getPayeeId());
//		createOrderCommand.setAmount(amount);
//		createOrderCommand.setExtendInfo(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
//		createOrderCommand.setGoodsName(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
//		createOrderCommand.setSourceType(1);//下单源，参考com.everhomes.pay.order.SourceType，0-表示手机下单，1表示电脑PC下单
//		String homeurl = configProvider.getValue("home.url", "");
//		String callbackurl = String.format(configProvider.getValue("siyinprint.pay.callBackUrl", "%s/evh/siyinprint/notifySiyinprintOrderPaymentV2"), homeurl);
//		createOrderCommand.setBackUrl(callbackurl);
//		createOrderCommand.setOrderRemark1(configProvider.getValue("siyinprint.pay.OrderRemark1","云打印"));
//
//		LOGGER.info("createPurchaseOrder params"+createOrderCommand);
//		CreateOrderRestResponse purchaseOrder = sdkPayService.createPurchaseOrder(createOrderCommand);
//		if(purchaseOrder==null || 200!=purchaseOrder.getErrorCode() || purchaseOrder.getResponse()==null){
//			LOGGER.info("purchaseOrder "+purchaseOrder);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"preorder failed "+ StringHelper.toJsonString(purchaseOrder));
//		}
//		OrderCommandResponse response = purchaseOrder.getResponse();
//		PreOrderDTO preDto = ConvertHelper.convert(response,PreOrderDTO.class);
//		preDto.setExpiredIntervalTime(response.getExpirationMillis());
//		List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
//		String format = "{\"getOrderInfoUrl\":\"%s\"}";
//		if(paymentMethods!=null){
//			preDto.setPayMethod(paymentMethods.stream().map(bizPayMethod->{
//				PayMethodDTO payMethodDTO = ConvertHelper.convert(bizPayMethod, PayMethodDTO.class);
//				payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
//				payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
//				String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
//				payMethodDTO.setPaymentLogo(paymentLogo);
//				payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
//				PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
//				com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
//				if(bizPaymentParamsDTO != null) {
//					paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
//				}
//				payMethodDTO.setPaymentParams(paymentParamsDTO);
//
//				return payMethodDTO;
//			}).collect(Collectors.toList()));
//		}
//		order.setPayDto(StringHelper.toJsonString(preDto));
//		siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	}
	


	private PreOrderDTO orderCommandResponseToDto(PurchaseOrderCommandResponse orderCommandResponse,
			PayPrintOrderCommandV2 cmd) {
		
		OrderCommandResponse response = orderCommandResponse.getPayResponse();
		PreOrderDTO preDto = ConvertHelper.convert(response,PreOrderDTO.class);
		preDto.setExpiredIntervalTime(response.getExpirationMillis());
		List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		if(paymentMethods!=null){
			preDto.setPayMethod(paymentMethods.stream().map(bizPayMethod->{
				PayMethodDTO payMethodDTO = ConvertHelper.convert(bizPayMethod, PayMethodDTO.class);
				payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
				payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
				String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
				payMethodDTO.setPaymentLogo(paymentLogo);
				payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
				PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
				com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
				if(bizPaymentParamsDTO != null) {
					paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
				}
				payMethodDTO.setPaymentParams(paymentParamsDTO);

				return payMethodDTO;
			}).collect(Collectors.toList()));
		}

		return preDto;
		
//        OrderCommandResponse response = orderCommandResponse.getPayResponse();
//        PreOrderDTO dto = ConvertHelper.convert(response, PreOrderDTO.class);
//        dto.setAmount(changePayAmount(cmd.getAmount()));
//        List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
//        if (paymentMethods != null)
//             dto.setPayMethod(paymentMethods.stream().map(r->{
//                 PayMethodDTO convert = ConvertHelper.convert(r, PayMethodDTO.class);
//                 convert.setExtendInfo(getPayMethodExtendInfo());
//                 return convert;
//             }).collect(Collectors.toList()));
//        dto.setOrderId(cmd.getOrderId());
//        return dto;
    }


	private PurchaseOrderCommandResponse createOrder(PayPrintOrderCommandV2 cmd, SiyinPrintOrder order, Long bizPayeeId) {
		
		 CreatePurchaseOrderCommand createOrderCommand = preparePaymentBillOrder(cmd, order, bizPayeeId);
	        CreatePurchaseOrderRestResponse createOrderResp = orderService.createPurchaseOrder(createOrderCommand);
	        if(!checkOrderRestResponseIsSuccess(createOrderResp)) {
	            String scope = OrderErrorCode.SCOPE;
	            int code = OrderErrorCode.ERROR_CREATE_ORDER_FAILED;
	            String description = "Failed to create order";
	            if(createOrderResp != null) {
	                code = (createOrderResp.getErrorCode() == null) ? code : createOrderResp.getErrorCode()  ;
	                scope = (createOrderResp.getErrorScope() == null) ? scope : createOrderResp.getErrorScope();
	                description = (createOrderResp.getErrorDescription() == null) ? description : createOrderResp.getErrorDescription();
	            }
	            throw RuntimeErrorException.errorWith(scope, code, description);
	        }

	        PurchaseOrderCommandResponse orderCommandResponse = createOrderResp.getResponse();
	        return orderCommandResponse;
	}


	private CreatePurchaseOrderCommand preparePaymentBillOrder(PayPrintOrderCommandV2 cmd, SiyinPrintOrder order, Long bizPayeeId) {
		
        CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();

        preOrderCommand.setAmount(changePayAmount(order.getOrderTotalFee()));

        String accountCode = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();
        preOrderCommand.setAccountCode(accountCode);
        preOrderCommand.setClientAppName(cmd.getClientAppName());
        preOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.PRINT_ORDER.getV2code());
        // 移到统一订单系统完成
        // String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        BusinessPayerType payerType = BusinessPayerType.USER;
//        preOrderCommand.setBusinessOrderNumber(generateBizOrderNum(accountCode,OrderType.OrderTypeEnum.PRINT_ORDER.getPycode(),order.getOrderNo()));
        preOrderCommand.setBusinessPayerType(payerType.getCode());
        preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
        String businessPayerParams = getBusinessPayerParams(cmd);
        preOrderCommand.setBusinessPayerParams(businessPayerParams);
        

       // preOrderCommand.setPaymentPayeeType(billGroup.getBizPayeeType()); 不填会不会有问题?
        preOrderCommand.setPaymentPayeeId(bizPayeeId); //不知道填什么

//        preOrderCommand.setPaymentParams(flattenMap);
        //preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.siyinprint", "/siyinprint/notifySiyinprintOrderPaymentV2");
        String backUrl = homeUrl + contextPath + backUri;
        preOrderCommand.setCallbackUrl(backUrl);
        preOrderCommand.setExtendInfo(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
        preOrderCommand.setGoodsName("云打印");
        preOrderCommand.setGoodsDescription(OrderType.OrderTypeEnum.PRINT_ORDER.getMsg());
        preOrderCommand.setIndustryName(null);
        preOrderCommand.setIndustryCode(null);
        preOrderCommand.setSourceType(SourceType.PC.getCode());
        preOrderCommand.setOrderRemark1(configProvider.getValue("siyinprint.pay.OrderRemark1","云打印"));
        //preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        preOrderCommand.setOrderRemark3(String.valueOf(cmd.getOwnerId()));
        preOrderCommand.setOrderRemark4(null);
        preOrderCommand.setOrderRemark5(null);
        String systemId = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.system_id", "");
        preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));

        LOGGER.info("preOrderCommand:"+StringHelper.toJsonString(preOrderCommand));
        return preOrderCommand;
    }
	
    private String getBusinessPayerParams(PayPrintOrderCommandV2 cmd) {


        Long businessPayerId = UserContext.currentUserId();


        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, cmd.getNamespaceId());
        String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        }
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "gorder.default.personal_bind_phone", "");
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("businessPayerPhone", buyerPhone);
        return StringHelper.toJsonString(map);
    
	}


	private Long changePayAmount(BigDecimal amount){

        if(amount == null){
            return 0L;
        }
        return  amount.multiply(new BigDecimal(100)).longValue();
    }


	private String generateBizOrderNum(String sNamespaceId, String pyCode, Long orderNo) {
		return sNamespaceId+BIZ_ORDER_NUM_SPILT+pyCode+BIZ_ORDER_NUM_SPILT+orderNo;
	}

	private String transferOrderNo(String bizOrderNum) {
		String[] split = bizOrderNum.split(BIZ_ORDER_NUM_SPILT);
		if(split.length==3){
			return split[2];
		}
		return bizOrderNum;
	}

	public List<PayMethodDTO> getPayMethods(String paymentStatusQueryUrl) {
		List<PayMethodDTO> payMethods = new ArrayList<>();
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		PayMethodDTO alipay = new PayMethodDTO();
		alipay.setPaymentName("支付宝支付");
		PaymentParamsDTO alipayParamsDTO = new PaymentParamsDTO();
		alipayParamsDTO.setPayType("A01");
		alipay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
		String url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveVpEWTNPV0kwWlRJMU0yRTFNakJtWkRCalpETTVaalUzTkdaaFltRmtOZw");
		alipay.setPaymentLogo(url);
		alipay.setPaymentParams(alipayParamsDTO);
		alipay.setPaymentType(8);
		payMethods.add(alipay);

		PayMethodDTO wxpay = new PayMethodDTO();
		wxpay.setPaymentName("微信支付");
		wxpay.setExtendInfo(String.format(format, paymentStatusQueryUrl));
		url = contentServerService.parserUri("cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw");
		wxpay.setPaymentLogo(url);
		PaymentParamsDTO wxParamsDTO = new PaymentParamsDTO();
		wxParamsDTO.setPayType("no_credit");
		wxpay.setPaymentParams(wxParamsDTO);
		wxpay.setPaymentType(1);

		payMethods.add(wxpay);
		return payMethods;
	}

	@Override
	public ListPrintingJobsResponse listPrintingJobs(ListPrintingJobsCommand cmd) {
		Long id = UserContext.current().getUser().getId();
		
		//做计数
        String key = REDIS_PRINTING_TASK_COUNT + id+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId();;
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        
        String value = valueOperations.get(key);
        if(value == null){
        	value = "0";
        }
		return new ListPrintingJobsResponse(Integer.parseInt(value));
	}
	
	@Override
	public UnlockPrinterResponse unlockPrinter(UnlockPrinterCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		//解锁打印机之前检查是否存在未支付订单。
		//前端必须先调用接口 /siyinprint/getPrintUnpaidOrder,如此处存在未支付订单，那么抛出异常
		if(PrintLogonStatusType.HAVE_UNPAID_ORDER == checkUnpaidOrder(cmd.getOwnerType(), cmd.getOwnerId())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Have unpaid order");
		}
//		return unlockPrinter(cmd,false);
		return unlockByOauthLogin(cmd);
	}
	
	private UnlockPrinterResponse unlockByOauthLogin(UnlockPrinterCommand cmd) {
		Map<String, String> params = new HashMap<>();
		User user = UserContext.current().getUser();
		QRCodeDTO dto = qrcodeService.getQRCodeInfoById(cmd.getQrid(), null);
		if(dto==null){
			LOGGER.error("QRCodeDTO is null, qrid = "+cmd.getQrid());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "QRCodeDTO is null, qrid = "+cmd.getQrid());
		
		}
		String loginAccount = user.getId().toString()+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId();
		params.put("user_name", loginAccount);
		params.put("email", "");
		params.put("feature", configurationProvider.getValue("print.siyin.feature","MONOPRINT;COLORPRINT;MONOCOPY;COLORCOPY;SCAN;FAX"));
		params.put("qrcode_param", dto.getExtra());
		params.put("copy_mono_limit", String.valueOf(configurationProvider.getIntValue("print.siyin.copy_mono_limit", 50)));
		params.put("copy_color_limit", String.valueOf(configurationProvider.getIntValue("print.siyin.copy_color_limit", 50)));
		StringBuffer buffer = new StringBuffer();
		String siyinUrl =  getSiyinServerUrl();

		String url = buffer.append(siyinUrl).append("/authagent/oauthLogin").toString();
		try {
			String result = HttpUtils.post(url, params, 30);
			if(!result.equals("OK")){
				LOGGER.error("siyin api:"+url+"request failed : "+result);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, url+" request failed, message = "+result);
			}
		} catch (IOException e) {
			LOGGER.error("siyin api:"+url+" request exception : "+e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, url+" return exception, message = "+e.getMessage());
		}
		
		UnlockPrinterResponse response = new UnlockPrinterResponse();
		try{
			JSONObject jsObject = JSONObject.parseObject(dto.getExtra());
			String readerName = jsObject.getString("readerName");
			if(StringUtils.isEmpty(readerName)){
				response.setSourceType(PrintScanTarget.UL_CLIENT.getCode());
			}else{
				response.setSourceType(PrintScanTarget.UL_PRINTER.getCode());
		        Integer namespaceId = cmd.getNamespaceId();
		        if(namespaceId == null){
		        	namespaceId = UserContext.getCurrentNamespaceId();
				}
		        mappingReaderToUser(readerName,namespaceId);
			}
		}catch (Exception e) {
			LOGGER.error("parse json error, qrcode = {}", dto.getExtra(),e);
		}
		
		return response;
	}


	/**
	 * 整个回调可能频繁发生，由于是非用户登录接口，完全可以放到后台任务中去做。
	 */
	@Override
	public void jobLogNotification(String jobData) {
		siyinJobValidateServiceImpl.jobLogNotification(jobData);
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

	@Deprecated //使用司印二维码定制。，此接口废弃
	private UnlockPrinterResponse unlockPrinter(UnlockPrinterCommand cmd, boolean isDirectPrint) {
        String siyinUrl =  getSiyinServerUrl();
        String moduleIp = getSiyinModuleIp(siyinUrl, cmd.getReaderName());
        String loginData = getLoginData(siyinUrl,cmd);
        
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
        SiyinPrintPrinter printer = siyinPrintPrinterProvider.findSiyinPrintPrinterByReadName(cmd.getReaderName());
        if(printer == null){
        	LOGGER.error("Unknown readerName = {}, register on table eh_siyin_print_printers",cmd.getReaderName());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unknown readerName = "+cmd.getReaderName());
        }
        Integer namespaceId = cmd.getNamespaceId();
        if(namespaceId == null){
        	namespaceId = UserContext.getCurrentNamespaceId();
		}
        mappingReaderToUser(cmd.getReaderName(),namespaceId);
        return directLogin(moduleIp,printer,loginData);
	}
	
	private void mappingReaderToUser(String readerName, Integer namespaceId) {
		//理论上应该给这里加锁，但是解锁打印机应该是低频率，这里上锁。
		SiyinUserPrinterMapping mapping = siyinUserPrinterMappingProvider.findSiyinUserPrinterMappingByUserAndPrinter(UserContext.current().getUser().getId(),readerName);
		if(mapping==null){
			mapping = new SiyinUserPrinterMapping();
			mapping.setUserId(UserContext.current().getUser().getId());
			mapping.setReaderName(readerName);
			mapping.setNamespaceId(namespaceId);
			mapping.setUnlockTimes(1L);
			siyinUserPrinterMappingProvider.createSiyinUserPrinterMapping(mapping);
		}else{
			mapping.setUnlockTimes(mapping.getUnlockTimes()+1);
			siyinUserPrinterMappingProvider.updateSiyinUserPrinterMapping(mapping);
		}
		
	}


	/**
	 * 解锁打印机
	 */
	private UnlockPrinterResponse directLogin(String moduleIp, SiyinPrintPrinter printer, String loginData) {
		Map<String, String> params = new HashMap<>();
		params.put("reader_name", printer.getReaderName());
		params.put("action", "QueryModule");
		params.put("login_data", loginData);
		StringBuffer buffer = new StringBuffer();
		String url = buffer.append("http://").append(moduleIp).append(":").append(printer.getModulePort()).append(printer.getLoginContext()).toString();
		boolean isunlockbg = configurationProvider.getBooleanValue("print.isunlockbg", false);
		if(isunlockbg){
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
			return null;
		}
		
		
//			url = buffer.append("?").append("reader_name=").append(printer.getReaderName())
//					.append("&action=QueryModule&login_data=").append(URLEncoder.encode(StringHelper.toJsonString(loginData),"UTF-8")).toString();
			
			return new UnlockPrinterResponse(url, params);
	}


	/**
	 * 获取用于解锁登录的xml
	 */
	private String getLoginData(String siyinUrl, UnlockPrinterCommand cmd) {
		 User user = UserContext.current().getUser();
		 Map<String, String> params = new HashMap<>();
		//这里设置accoutname 为用户id-园区-拥有者id，因为在jobLogNotification
     	//中计算价格的时候，不知道用户所在的园区，所以只能依靠
		 //信息超长,压缩
		 String loginAccount = user.getId().toString()+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId();
	     params.put("login_account", loginAccount);
//	     params.put("login_password", user.getPasswordHash());
	     params.put("reader_name", cmd.getReaderName());
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
//				response.setPrintCourseList(Arrays.asList(siyinPrintSetting.getPrintCourse().split("\\|")));
//				response.setScanCopyCourseList(Arrays.asList(siyinPrintSetting.getScanCopyCourse().split("\\|")));
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
		response.getColorTypeDTO().setColorPrice(defaultdecimal);
		
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
		LocaleString localeString = localeStringProvider.find(PrintErrorCode.SCOPE, code, "zh_CN");
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
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		List<SiyinPrintSetting> list = checkPaperSizePriceDTO(cmd.getPaperSizePriceDTO(),cmd.getOwnerType(),cmd.getOwnerId(),namespaceId);
		list.add(checkColorTypeDTO(cmd.getColorTypeDTO(),cmd.getOwnerType(),cmd.getOwnerId(),namespaceId));
		list.add(checkCourseList(cmd.getScanCopyCourseList(),cmd.getPrintCourseList(),cmd.getOwnerType(),cmd.getOwnerId(),cmd.getHotline(),namespaceId));
		return list;
	}

	/**
	 * 检查打印/复印扫描教程并产生实体
	 */
	private SiyinPrintSetting checkCourseList(List<String> printCourseList,List<String> scancopyCourseList, String ownerType, Long ownerId,String hotline,Integer namespaceId) {
		SiyinPrintSetting setting = new SiyinPrintSetting();
		setting.setScanCopyCourse("");
		setting.setPrintCourse("");
		setting.setOwnerType(ownerType);
		setting.setOwnerId(ownerId);
		setting.setSettingType(PrintSettingType.COURSE_HOTLINE.getCode());
		setting.setNamespaceId(namespaceId);
		
		setting.setScanCopyCourse(getLocalActivityString(PrintErrorCode.PRINT_COURSE_LIST));
		setting.setPrintCourse(getLocalActivityString(PrintErrorCode.SCAN_COPY_COURSE_LIST));
		setting.setHotline(hotline);
		return setting;
	}


	/**
	 * 检查打印扫描价格的DTO，并生成实体
	 */
	private List<SiyinPrintSetting> checkPaperSizePriceDTO(PrintSettingPaperSizePriceDTO paperSizePriceDTO, String string, Long long1,Integer namespaceId) {
		// TODO Auto-generated method stub
		if(paperSizePriceDTO == null){
			paperSizePriceDTO = new PrintSettingPaperSizePriceDTO();
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, paperSizePriceDTO = " +paperSizePriceDTO);
		}
		paperSizePriceDTO.setAthreePrice(checkPrice(paperSizePriceDTO.getAthreePrice()));
		paperSizePriceDTO.setAfourPrice(checkPrice(paperSizePriceDTO.getAfourPrice()));
		paperSizePriceDTO.setAfivePrice(checkPrice(paperSizePriceDTO.getAfivePrice()));
		paperSizePriceDTO.setAsixPrice(checkPrice(paperSizePriceDTO.getAsixPrice()));
		
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
		
		settinga3.setNamespaceId(namespaceId);
		settinga4.setNamespaceId(namespaceId);
		settinga5.setNamespaceId(namespaceId);
		settinga6.setNamespaceId(namespaceId);
		return new ArrayList<SiyinPrintSetting>(Arrays.asList(new SiyinPrintSetting[]{settinga3,settinga4,settinga5,settinga6}));
	}


	/**
	 * 检查扫描价格，并生成实体
	 */
	private SiyinPrintSetting checkColorTypeDTO(PrintSettingColorTypeDTO colorTypeDTO, String string, Long long1,Integer namespaceId) {
		colorTypeDTO = checkPrice(colorTypeDTO);
		SiyinPrintSetting setting = ConvertHelper.convert(colorTypeDTO, SiyinPrintSetting.class);
		setting.setOwnerType(string);
		setting.setOwnerId(long1);
		setting.setSettingType(PrintSettingType.PRINT_COPY_SCAN.getCode());
		setting.setJobType(PrintJobTypeType.SCAN.getCode());
		setting.setNamespaceId(namespaceId);
		return setting;
	}
	
	/**
	 * 检查价格信息
	 */
	private PrintSettingColorTypeDTO checkPrice(PrintSettingColorTypeDTO colorTypeDTO) {
		if(colorTypeDTO == null){
			colorTypeDTO = new PrintSettingColorTypeDTO();
		}
		if(colorTypeDTO.getBlackWhitePrice() == null){
			colorTypeDTO.setBlackWhitePrice(new BigDecimal(0));
		}
		if(colorTypeDTO.getBlackWhitePrice().compareTo(BigDecimal.ZERO) < 0)
		{
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, blackWhitePrice = " + colorTypeDTO.getBlackWhitePrice());
		}
		if(colorTypeDTO.getColorPrice()== null){
			colorTypeDTO.setColorPrice(new BigDecimal(0));
		}
		if(colorTypeDTO.getColorPrice().compareTo(BigDecimal.ZERO) < 0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, colorPrice = " + colorTypeDTO.getBlackWhitePrice());
		}
		return colorTypeDTO;
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
			addOrderTotalAmountToStat(allStat,siyinPrintOrder.getOrderTotalFee(),orderStatusType);
			
			switch (jobType) {
			case PRINT:
				addOrderTotalAmountToStat(printStat, siyinPrintOrder.getOrderTotalFee(), orderStatusType);
				break;
			case COPY:
				addOrderTotalAmountToStat(copyStat, siyinPrintOrder.getOrderTotalFee(), orderStatusType);
				break;
			case SCAN:
				addOrderTotalAmountToStat(scanStat, siyinPrintOrder.getOrderTotalFee(), orderStatusType);
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
	 * @param ownerId 
	 * @param ownerType 
	 */
	private PrintLogonStatusType checkUnpaidOrder(String ownerType, Long ownerId) {
		return checkUnpaidOrder(ownerType, ownerId, null);
	}
	
	private PrintLogonStatusType checkUnpaidOrder(String ownerType, Long ownerId, GetPrintUnpaidOrderResponse r) {
		User user = UserContext.current().getUser();
		
		List<SiyinPrintOrder> list = siyinPrintOrderProvider.listSiyinPrintUnpaidOrderByUserId(user.getId(),ownerType,ownerId);
		
		if(list == null || list.size() == 0){
			return PrintLogonStatusType.LOGON_SUCCESS;
		}
		if(r != null){
			r.setOrderId(list.get(0).getId());
			r.setTotalFee(list.get(0).getOrderTotalFee());
		}
		return PrintLogonStatusType.HAVE_UNPAID_ORDER;
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
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		siyinPrintEmail.setNamespaceId(namespaceId);
		siyinPrintEmail.setUserId(UserContext.current().getUser().getId());
		siyinPrintEmail.setStatus(CommonStatus.ACTIVE.getCode());
		return siyinPrintEmail;
	}
	private String getSiyinCode(String result){
		LOGGER.info("result = "+result);
        return result.substring(0, result.indexOf(":"));
    }

	private String getSiyinData(String result){
        return result.substring(result.indexOf(":") + 1);
    }


	@Override
	public ListQueueJobsResponse listQueueJobs(ListQueueJobsCommand cmd) {
        String siyinUrl =  getSiyinServerUrl();
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
        List<SiyinUserPrinterMapping> mappings = siyinUserPrinterMappingProvider.listSiyinUserPrinterMappingByUserId(UserContext.current().getUser().getId(), namespaceId);
        List<String> readers = new ArrayList<>(5);
        if(mappings == null || mappings.size() == 0){
        	List<SiyinPrintPrinter> lists = siyinPrintPrinterProvider.listSiyinPrintPrinter();
        	for (SiyinPrintPrinter printer : lists) {
				readers.add(printer.getReaderName());
			}
        }else{
        	for (SiyinUserPrinterMapping mapping : mappings) {
        		readers.add(mapping.getReaderName());
			}
        }
        List<ListQueueJobsDTO> jobs = getQueueJobs(readers,siyinUrl,cmd.getOwnerId());
        return new ListQueueJobsResponse(jobs);
	}


	private List<ListQueueJobsDTO> getQueueJobs(List<String> printers, String siyinUrl, Long communityId) {
		List<ListQueueJobsDTO> list = new ArrayList<>();
		readerLoop:
		for (String printer : printers) {
			Map<String, String> params = new HashMap<>();
	        params.put("host_name", printer);
	        params.put("mode", "USERLIST");
	        params.put("card_id", new StringBuffer().append(UserContext.current().getUser().getId()).append(PRINT_LOGON_ACCOUNT_SPLIT).append(communityId).toString());
//	        params.put("card_id", "310183-240111044331058733");
	        params.put("language", "zh-cn");
	        String result;
			try {
				result = HttpUtils.post(siyinUrl + "/console/cardListener", params, 30);
				String siyinCode = getSiyinCode(result);
				if(siyinCode!=null && siyinCode.startsWith("INF")){
					if(siyinCode.equals("INF0008") || siyinCode.equals("INF0009") || siyinCode.equals("INF0001")){
						continue readerLoop;
					}else{
						LOGGER.error("siyin api:/console/cardListener request failed, result = {} ",result);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/cardListener return failed, result = "+result);
					}
				}else if(siyinCode!=null){
					addjobs(list,result,printer);
				}else{
					LOGGER.error("siyin api:/console/cardListener request failed, result = {} ",result);
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/cardListener return failed, result = "+result);
				}
			} catch (IOException e) {
				LOGGER.error("siyin api:/console/cardListener request exception : "+e.getMessage());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/cardListener return exception, message = "+e.getMessage());
				
			}
		}
		return list;
	}


	private void addjobs(List<ListQueueJobsDTO> list, String result,String printer) {
		Map<String, Object> object = XMLToJSON.convertOriginalMap(result);
		Map<?, ?> data = (Map<?,?>)object.get("brocadesoft");
		Object job_list = data.get("job_list");
		List<Map<?,?>> jobList = new ArrayList<>();
		if(job_list instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String,Map<?,?>> map = (Map<String,Map<?,?>>)job_list;
			jobList.add(map.get("job"));
		}else{
		    jobList = (List<Map<?,?>>)job_list;
		}
		list.addAll(jobList.stream().map(map->{
			ListQueueJobsDTO dto = new ListQueueJobsDTO();
			dto.setJobId(map.get("job_id").toString());
			dto.setJobName(map.get("job_name").toString());
			dto.setPrintTime(map.get("print_time").toString());
			dto.setReaderName(printer);
			dto.setTotalPage(map.get("total_page").toString());
        	return dto;
        }).collect(Collectors.toList()));
	}


	@Override
	public void releaseQueueJobs(ReleaseQueueJobsCommand cmd) {
		checkPrinters(cmd.getJobs());
		Map<String,String> rmjobsMap = generateXmlData(cmd.getJobs());
		releaseQueueJobs(rmjobsMap,cmd.getOwnerId());
	}

	private void releaseQueueJobs(Map<String, String> rmjobsMap, Long communityId) {
		String siyinUrl =  getSiyinServerUrl();
		rmjobsMap.entrySet().forEach(r->{
			Map<String, String> params = new HashMap<>();
	        params.put("host_name", r.getKey());
	        params.put("mode", "USERLIST");
	        params.put("card_id", new StringBuffer().append(UserContext.current().getUser().getId()).append(PRINT_LOGON_ACCOUNT_SPLIT).append(communityId).toString());
//	        params.put("card_id", "310183-240111044331058733");
	        params.put("language", "zh-cn");
	        params.put("data", r.getValue());
			try {
				String result = HttpUtils.post(siyinUrl + "/console/cardListener", params, 30);
			} catch (IOException e) {
				LOGGER.error("siyin api:/console/cardListener request exception : "+e.getMessage());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/cardListener return exception, message = "+e.getMessage());
			}
		});
	}

	static final String rootxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><job_list>#{job}</job_list></xml>";
	static final String jobxml = "<job><job_id>#{job_id}</job_id></job>";
	private Map<String, String> generateXmlData(List<ListQueueJobsDTO> jobs) {
		Map<String, List<ListQueueJobsDTO>> printerMap = new HashMap<>();
		for (ListQueueJobsDTO job : jobs) {
			List<ListQueueJobsDTO> printerJobs = printerMap.get(job.getReaderName());
			if(printerJobs == null){
				printerJobs = new ArrayList<>();
				printerMap.put(job.getReaderName(), printerJobs);
			}
			printerJobs.add(job);
		}
		
		Map<String, String> xmlMap = new HashMap<String,String>();
		printerMap.entrySet().forEach(r->{
			StringBuffer buffer = new StringBuffer();
			r.getValue().forEach(dto->{
				buffer.append(jobxml.replace("#{job_id}", dto.getJobId()));
			});
			xmlMap.put(r.getKey(), rootxml.replace("#{job}", buffer.toString()));
		});
		return xmlMap;
	}


	private void checkPrinters(List<ListQueueJobsDTO> jobs) {
		for (ListQueueJobsDTO job : jobs) {
			if(job.getJobId() == null || job.getReaderName() == null){
				LOGGER.error("jobs data error");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, " job = "+job);
			}
		}
	}


	@Override
	public void deleteQueueJobs(DeleteQueueJobsCommand cmd) {
		checkPrinters(cmd.getJobs());
		String siyinUrl =  getSiyinServerUrl();
		Map<String, String> params = new HashMap<>();
        params.put("format", "String");
        params.put("action", "Cancel");
        StringBuffer buffer = new StringBuffer();
        cmd.getJobs().forEach(r->{
        	buffer.append(r.getJobId()).append(',');
        });
        params.put("job_data", buffer.substring(0,buffer.length()-1));
		try {
			String result = HttpUtils.post(siyinUrl + "/console/jobHandler", params, 30);
		} catch (IOException e) {
			LOGGER.error("siyin api:/console/cardListener request exception : "+e.getMessage());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "/console/jobHandler return exception, message = "+e.getMessage());
		}
	
	}
	
	@Override
	public void getPrintQrcode(HttpServletRequest req, HttpServletResponse rps) {
		GetPrintQrcodeCommand cmd = getParamsFromReq(req);
		NewQRCodeCommand nQRCmd = new NewQRCodeCommand();
		String cloudprinturl = configurationProvider.getValue("print.siyin.actiondata", "{\"url\":\"https://core.zuolin.com/cloud-print/build/index.html#/home#sign_suffix\"}");
		nQRCmd.setActionData(cloudprinturl);
		nQRCmd.setActionType(ActionType.OFFICIAL_URL.getCode());
		nQRCmd.setDescription("cloud print");
		nQRCmd.setExtra(cmd.getData());
		nQRCmd.setHandler(QRCodeHandler.PRINT.getCode());

		QRCodeDTO dto = qrcodeService.createQRCode(nQRCmd);

		GetQRCodeImageCommand gQRcmd = new GetQRCodeImageCommand();
		gQRcmd.setHeight(cmd.getHeight()==null?300:cmd.getHeight());
		gQRcmd.setWidth(cmd.getWidth()==null?300:cmd.getWidth());
		gQRcmd.setQrid(dto.getQrid());
		try {
			qrController.getQRCodeImage(gQRcmd, req, rps);
		} catch (Exception e) {
			LOGGER.error("e",e);
		}
	}


	private GetPrintQrcodeCommand getParamsFromReq(HttpServletRequest req) {
		GetPrintQrcodeCommand cmd = new GetPrintQrcodeCommand();
		Object object = req.getParameter("data");
		if(object==null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknow param data=null");
		}
		cmd.setData(object.toString());
		Object width = req.getParameter("width");
		Object height = req.getParameter("height");
		if(width!=null && height!=null){
			cmd.setHeight(Integer.valueOf(height.toString()));
			cmd.setWidth(Integer.valueOf(width.toString()));
		}
		return cmd;
	}

	@Override
	public void mfpLogNotification(MfpLogNotificationCommand cmd) {
		boolean isOk = siyinJobValidateServiceImpl.mfpLogNotification(cmd.getJobData());
		if (!isOk) {
			LOGGER.error("jobData:"+cmd.getJobData());
		}
	}

	@Override
	public List<ListBizPayeeAccountDTO> listPayeeAccount(ListPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getCommunityId());
		ArrayList arrayList = new ArrayList(Arrays.asList("0", cmd.getCommunityId() + ""));
		String key = OwnerType.ORGANIZATION.getCode() + cmd.getOrganizationId();
		LOGGER.info("sdkPayService request params:{} {} ",key,arrayList);
		List<PayUserDTO> payUserList = payServiceV2.getPayUserList(key,arrayList);
		if(payUserList==null || payUserList.size() == 0){
			return null;
		}
 		return payUserList.stream().map(r->{
			ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
			dto.setAccountId(r.getId());
			dto.setAccountType(r.getUserType()==2?OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
			dto.setAccountName(r.getRemark());
			dto.setAccountAliasName(r.getUserAliasName());
	        if (r.getRegisterStatus() != null && r.getRegisterStatus().intValue() == 1) {
	            dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
	        } else {
	            dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
	        }
			
			return dto;
		}).collect(Collectors.toList());
		
		
	}

	@Override
	public void createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		List<SiyinPrintBusinessPayeeAccount> accounts = siyinBusinessPayeeAccountProvider.findRepeatBusinessPayeeAccounts
				(cmd.getId(),cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if(accounts!=null && accounts.size()>0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"repeat account");
		}
		if(cmd.getId()!=null){
			SiyinPrintBusinessPayeeAccount oldPayeeAccount = siyinBusinessPayeeAccountProvider.findSiyinPrintBusinessPayeeAccountById(cmd.getId());
			if(oldPayeeAccount == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"unknown payaccountid = "+cmd.getId());
			}
			SiyinPrintBusinessPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,SiyinPrintBusinessPayeeAccount.class);
			newPayeeAccount.setCreateTime(oldPayeeAccount.getCreateTime());
			newPayeeAccount.setCreatorUid(oldPayeeAccount.getCreatorUid());
			newPayeeAccount.setNamespaceId(oldPayeeAccount.getNamespaceId());
			newPayeeAccount.setOwnerType(oldPayeeAccount.getOwnerType());
			newPayeeAccount.setOwnerId(oldPayeeAccount.getOwnerId());
			siyinBusinessPayeeAccountProvider.updateSiyinPrintBusinessPayeeAccount(newPayeeAccount);
		}else{
			SiyinPrintBusinessPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,SiyinPrintBusinessPayeeAccount.class);
			newPayeeAccount.setStatus((byte)2);
			siyinBusinessPayeeAccountProvider.createSiyinPrintBusinessPayeeAccount(newPayeeAccount);
		}
	}

	@Override
	public BusinessPayeeAccountDTO getBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		SiyinPrintBusinessPayeeAccount account = siyinBusinessPayeeAccountProvider
				.getSiyinPrintBusinessPayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if(account==null){
			return null;
		}
		List<PayUserDTO> payUserDTOS = payServiceV2.listPayUsersByIds(new ArrayList<>(Arrays.asList(account.getPayeeId())));
		Map<Long,PayUserDTO> map = payUserDTOS.stream().collect(Collectors.toMap(PayUserDTO::getId,r->r));
		BusinessPayeeAccountDTO convert = ConvertHelper.convert(account, BusinessPayeeAccountDTO.class);
		PayUserDTO payUserDTO = map.get(convert.getPayeeId());
		if(payUserDTO!=null){
			convert.setPayeeUserType(payUserDTO.getUserType());
			convert.setPayeeUserName(payUserDTO.getRemark());
			convert.setPayeeUserAliasName(payUserDTO.getUserAliasName());
			convert.setPayeeAccountCode(payUserDTO.getAccountCode());
			convert.setPayeeRegisterStatus(payUserDTO.getRegisterStatus());
			convert.setPayeeRemark(payUserDTO.getRemark());
		}
		return convert;

	}
	private JSONArray getNewsFromExcel(MultipartFile[] files) {
		List<RowResult> resultList = null;
		try {
			resultList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			LOGGER.error("processStat Excel error");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION, "processStat Excel error");
		}

		if (resultList != null && resultList.size() > 0) {
			final JSONArray array = new JSONArray();
			for (int i = 1, len = resultList.size(); i < len; i++) {
				RowResult result = resultList.get(i);
				String name = RowResult.trimString(result.getA());
				if(name==null || !name.contains("打印")){
					continue;
				}
				String namespaceId = RowResult.trimString(result.getB());
				Namespace namespace = namespaceProvider.findNamespaceById(Integer.valueOf(namespaceId));
				if(namespace==null){
					continue;
				}
				String organizationType = RowResult.trimString(result.getC());
				String organizationId = RowResult.trimString(result.getD());
				String payType = RowResult.trimString(result.getE());
				String payUserId = RowResult.trimString(result.getF());
				List<OrganizationCommunity> communities = organizationProvider.listOrganizationCommunities(Long.valueOf(organizationId));
				if(communities==null || communities.size()==0){
					continue;
				}
				for (OrganizationCommunity community : communities) {
					JSONObject account = new JSONObject();
					account.put("namespaceId",namespaceId);
					account.put("ownerType",PrintOwnerType.COMMUNITY.getCode());
					account.put("ownerId",community.getCommunityId());
					account.put("payeeId",payUserId);
					account.put("payeeUserType",OwnerType.ORGANIZATION.getCode());
					array.add(account);
				}
			}
			return array;
		}
		return null;
	}
	@Override
	public void initPayeeAccount(MultipartFile[] files) {
		User user = UserContext.current().getUser();
		if(user.getId()!=1){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error person, must system user 1");
		}
		JSONArray accounts = getNewsFromExcel(files);
		if(accounts==null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"error json");
		}

		for (Object object : accounts) {
			JSONObject account = JSONObject.parseObject(object.toString());
			if(account==null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"error account");
			}
			Integer namespaceId = account.getInteger("namespaceId"); if(namespaceId==null){throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"empty namespaceId");}
			String ownerType = account.getString("ownerType");if(ownerType==null){throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"empty ownerType");}
			Long ownerId = account.getLong("ownerId");if(ownerId==null){throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"empty ownerId");}
			List<SiyinPrintBusinessPayeeAccount> oldaccounts = siyinBusinessPayeeAccountProvider.findRepeatBusinessPayeeAccounts(null, namespaceId,ownerType, ownerId);
			Long payeeId = account.getLong("payeeId");if(payeeId==null){throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"empty payeeId");}
			String payeeUserType = account.getString("payeeUserType");if(payeeUserType==null){throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"empty payeeUserType");}
			if(oldaccounts!=null && oldaccounts.size()>0){
				SiyinPrintBusinessPayeeAccount payeeAccount = oldaccounts.get(0);
				payeeAccount.setPayeeId(payeeId);
				payeeAccount.setPayeeUserType(payeeUserType);
				siyinBusinessPayeeAccountProvider.updateSiyinPrintBusinessPayeeAccount(payeeAccount);
			}else{
				SiyinPrintBusinessPayeeAccount payeeAccount = new SiyinPrintBusinessPayeeAccount();
				payeeAccount.setNamespaceId(namespaceId);
				payeeAccount.setOwnerType(ownerType);
				payeeAccount.setOwnerId(ownerId);
				payeeAccount.setPayeeId(payeeId);
				payeeAccount.setPayeeUserType(payeeUserType);
				payeeAccount.setStatus((byte)2);
				siyinBusinessPayeeAccountProvider.createSiyinPrintBusinessPayeeAccount(payeeAccount);
			}
		}
	}

	@Override
	public void mfpLogNotificationV2(MfpLogNotificationV2Command cmd, HttpServletResponse response) {
		if(cmd.getJob_id()==null){
			cmd.setJob_id(UUID.randomUUID().toString());
		}
		if(cmd.getJob_name()==null){
			cmd.setJob_name("jobname.txt");
		}
		if(cmd.getPrint_time()==null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			cmd.setPrint_time(format.format(new Date()));
		}
		if(cmd.getLocation()==null){
			cmd.setLocation("");
		}
		if(cmd.getUser_name()==null){
			UserIdentifier uIdentifier = userProvider.findClaimedIdentifierByToken(cmd.getNamespaceId(), cmd.getPhone() + "");
			if(uIdentifier!=null){
				cmd.setUser_name(uIdentifier.getOwnerUid()+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId());
				cmd.setUser_id(uIdentifier.getOwnerUid()+PRINT_LOGON_ACCOUNT_SPLIT+cmd.getOwnerId());
			}
		}
		
		try {
			response.getOutputStream().write("OK".getBytes());
		} catch (IOException e) {
			LOGGER.info("return ok failed");
		}
		
		MfpLogNotificationCommand cmd2 = new MfpLogNotificationCommand();
		cmd2.setJobData(StringHelper.toJsonString(cmd));
		mfpLogNotification(cmd2);
	}


	@Override
	public void notifySiyinprintOrderPaymentV2(OrderPaymentNotificationCommand cmd) {
			//检查签名
			if(!PayUtil.verifyCallbackSignature(cmd)){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"sign verify faild");
			}

			// * RAW(0)：
			// * SUCCESS(1)：支付成功
			// * PENDING(2)：挂起
			// * ERROR(3)：错误
			if(cmd.getPaymentStatus()== null || 1!=cmd.getPaymentStatus()){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"invaild paymentstatus,"+cmd.getPaymentStatus());
			}//检查状态

			//检查orderType
			//RECHARGE(1), WITHDRAW(2), PURCHACE(3), REFUND(4);
			//充值，体现，支付，退款
			if(cmd.getOrderType()==null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"invaild ordertype,"+cmd.getOrderType());
			}
			if(cmd.getOrderType() == 3) {
				
				//根据统一订单生成的支付编号获得记录
				SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByBizOrderNum(cmd.getBizOrderNum());
				if(order == null){
					LOGGER.error("the order {} not found.",cmd.getBizOrderNum());
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"the order not found.");
				}
				
//				Long orderNo = Long.parseLong(transferOrderNo(cmd.getBizOrderNum()));
//				SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByOrderNo(orderNo);
	
				BigDecimal payAmount = new BigDecimal(cmd.getAmount()).divide(new BigDecimal(100));

				//加一个开关，方便在beta环境测试
				boolean flag = configProvider.getBooleanValue("beta.print.order.amount", false);
				if (!flag) {
					if (0 != order.getOrderTotalFee().compareTo(payAmount)) {
						LOGGER.error("Order amount is not equal to payAmount, cmd={}, order={}", cmd, order);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
								"Order amount is not equal to payAmount.");
					}
				}
				Long payTime = System.currentTimeMillis();
				Timestamp payTimeStamp = new Timestamp(payTime);
				if(order.getOrderStatus().byteValue() == PrintOrderStatusType.UNPAID.getCode()) {
					order.setOrderStatus(PrintOrderStatusType.PAID.getCode());
					order.setLockFlag(PrintOrderLockType.LOCKED.getCode());
					order.setPaidTime(payTimeStamp);
					order.setPayOrderNo(cmd.getOrderId()+"");
					siyinPrintOrderProvider.updateSiyinPrintOrder(order);
				}

			}
	}
	
    /*
     * 由于从支付系统里回来的CreateOrderRestResponse有可能没有errorScope，故不能直接使用CreateOrderRestResponse.isSuccess()来判断，
       CreateOrderRestResponse.isSuccess()里会对errorScope进行比较
     */
    private boolean checkOrderRestResponseIsSuccess(CreatePurchaseOrderRestResponse response){
        if(response != null && response.getErrorCode() != null
                && (response.getErrorCode().intValue() == 200 || response.getErrorCode().intValue() == 201))
            return true;
        return false;
    }
    
    @Override
	public String getSiyinServerUrl() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(),
				PrintErrorCode.PRINT_SIYIN_SERVER_URL, "http://siyin.zuolin.com:8119");
	}
}
