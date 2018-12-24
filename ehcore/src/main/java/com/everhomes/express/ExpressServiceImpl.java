// @formatter:off
package com.everhomes.express;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import com.everhomes.order.PayService;
import com.everhomes.parking.handler.Utils;
import com.everhomes.pay.order.CreateOrderCommand;
import com.everhomes.pay.order.OrderCommandResponse;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.pay.order.SourceType;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.print.SiyinPrintOrder;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.express.*;
import com.everhomes.rest.promotion.order.controller.CreatePurchaseOrderRestResponse;
import com.everhomes.rest.promotion.order.BusinessPayerType;
import com.everhomes.rest.promotion.order.CreatePurchaseOrderCommand;
import com.everhomes.rest.promotion.order.OrderErrorCode;
import com.everhomes.rest.promotion.order.PurchaseOrderCommandResponse;
import com.everhomes.rest.order.*;
import com.everhomes.rest.pay.controller.CreateOrderRestResponse;
import com.everhomes.rest.print.PayPrintOrderCommandV2;
import com.everhomes.rest.rentalv2.RentalServiceErrorCode;
import com.everhomes.user.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.asset.PaymentConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.express.guomao.pay.PayResponse;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

@Component
public class ExpressServiceImpl implements ExpressService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressServiceImpl.class);
	
	private static final int ORDER_NO_LENGTH = 18;
	public static final String BIZ_ACCOUNT_PRE = "NS";//账号前缀
	public static final String BIZ_ORDER_NUM_SPILT = "_";//业务订单分隔符
	@Autowired
	private ExpressParamSettingProvider expressParamSettingProvider;

	@Autowired
	private ExpressOrderEmbeddedV2Handler parkingV2EmbeddedV2Handler;
	
	@Autowired
	private ExpressHotlineProvider expressHotlineProvider;
	
	@Autowired
	private ExpressCompanyBusinessProvider expressCompanyBusinessProvider;
	
	@Autowired
	private ExpressServiceAddressProvider expressServiceAddressProvider;
	
	@Autowired
	private ExpressCompanyProvider expressCompanyProvider;
	
	@Autowired
	private ExpressUserProvider expressUserProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ExpressOrderProvider expressOrderProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Autowired
	private ExpressAddressProvider expressAddressProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private ExpressQueryHistoryProvider expressQueryHistoryProvider;
	
	@Autowired
	private ExpressOrderLogProvider expressOrderLogProvider;

	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private OrderUtil orderUtil;
	
	@Autowired
	private LocaleStringService localeStringService;
	
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	
	@Autowired
    private UserActivityProvider userActivityProvider;

	@Autowired
	private UserProvider userProvider;
	@Autowired
    private AppProvider appProvider;

//	@Autowired
//	private PayService payService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserProvider uProvider;

	@Autowired
	public com.everhomes.paySDK.api.PayService sdkPayService;
	@Autowired
	public ExpressPayeeAccountProvider accountProvider;
	
    @Autowired
    protected GeneralOrderService orderService;
	
	@Value("${server.contextPath:}")
	private String contextPath;

	@Override
	public ListServiceAddressResponse listServiceAddress(ListServiceAddressCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		List<ExpressServiceAddress> expressServiceAddresses = expressServiceAddressProvider.listExpressServiceAddresseByOwner(owner);
		return new ListServiceAddressResponse(expressServiceAddresses.stream().map(this::convertToExpressServiceAddressDTO).collect(Collectors.toList()));
	}
	
	private ExpressServiceAddressDTO convertToExpressServiceAddressDTO(ExpressServiceAddress expressServiceAddress) {
		return ConvertHelper.convert(expressServiceAddress, ExpressServiceAddressDTO.class);
	}

	private ExpressOwner checkOwner(String ownerType, Long ownerId) {
		if (StringUtils.isEmpty(ownerType) || ownerId == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwnerType expressOwnerType = ExpressOwnerType.fromCode(ownerType);
		if (expressOwnerType == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		return new ExpressOwner(UserContext.getCurrentNamespaceId(), expressOwnerType, ownerId, UserContext.current().getUser().getId());
	}

	@Override
	public ListExpressCompanyResponse listExpressCompany(ListExpressCompanyCommand cmd) {
		//如果是查快递接口过来的，不需要传owner
		//ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressOwner owner = new ExpressOwner();
		owner.setNamespaceId(UserContext.getCurrentNamespaceId());
		if (!StringUtils.isEmpty(cmd.getOwnerType())) {
			owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		}
		List<ExpressCompany> expressCompanies = expressCompanyProvider.listExpressCompanyByOwner(owner);
		return new ListExpressCompanyResponse(expressCompanies.stream().map(this::convertToExpressCompanyDTO).collect(Collectors.toList()));
	}
	
	private ExpressCompanyDTO convertToExpressCompanyDTO(ExpressCompany expressCompany) {
		return ConvertHelper.convert(expressCompany, ExpressCompanyDTO.class);
	}

	@Override
	public ListExpressUserResponse listExpressUser(ListExpressUserCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		ListExpressUserCondition condition = ConvertHelper.convert(cmd, ListExpressUserCondition.class);
		condition.setPageSize(pageSize);
		condition.setNamespaceId(owner.getNamespaceId());
		List<ExpressUser> expressUsers = expressUserProvider.listExpressUserByCondition(condition);
		Long nextPageAnchor = null;
		if (expressUsers.size() > pageSize) {
			expressUsers.remove(expressUsers.size()-1);
			nextPageAnchor = expressUsers.get(expressUsers.size()-1).getId();
		}
		return new ListExpressUserResponse(nextPageAnchor, expressUsers.stream().map(this::convertToExpressUserDTO).collect(Collectors.toList()));
	}
	private ExpressUserDTO convertToExpressUserDTO(ExpressUser expressUser) {
		ExpressUserDTO expressUserDTO = ConvertHelper.convert(expressUser, ExpressUserDTO.class);
		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberById(expressUser.getOrganizationMemberId());
		if(organizationMember!=null) {
			expressUserDTO.setName(organizationMember.getContactName());
			expressUserDTO.setPhone(organizationMember.getContactToken());
			return expressUserDTO;
		}
		UserIdentifier userIdentifier = userService.getUserIdentifier(expressUser.getUserId());
		if(userIdentifier!=null){
			expressUserDTO.setPhone(userIdentifier.getIdentifierToken());
			User user = uProvider.findUserById(expressUser.getUserId());
			if(user!=null){
				expressUserDTO.setName(user.getNickName());
			}

		}
		return  expressUserDTO;
	}

	@Override
	public RestResponse addExpressUser(AddExpressUserCommand cmd) {
		if (cmd.getExpressCompanyId() == null || cmd.getServiceAddressId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkAdmin(owner);
		RestResponse restResponse = checkExpressUser(cmd.getExpressUsers());
		if (restResponse != null) {
			return restResponse;
		}
		if (cmd.getExpressUsers() != null) {
			dbProvider.execute(s->{
				cmd.getExpressUsers().forEach(r->addExpressUser(owner, r, cmd));
				return null;
			});
		}
		return null;
	}

	private RestResponse checkExpressUser(List<CreateExpressUserDTO> expressUsers) {
		if (expressUsers != null && !expressUsers.isEmpty()) {
			List<String> errors = new ArrayList<>();
			expressUsers.forEach(e->{
				if (e.getUserId() == null || e.getUserId() == 0L) {
					OrganizationMember organizationMember = organizationProvider.findOrganizationMemberById(e.getOrganizationMemberId());
					errors.add(organizationMember.getContactToken());
				}
			});
			if (!errors.isEmpty()) {
				String error = localeStringService.getLocalizedString(ExpressServiceErrorCode.SCOPE, String.valueOf(ExpressServiceErrorCode.NOT_SIGNED_USER_ERROR), UserContext.current().getUser().getLocale(), "");
				String description = String.format(error, String.join("、", errors));
				return new RestResponse(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.NOT_SIGNED_USER_ERROR, description);
			}
		}
		return null;
	}

	private boolean checkAdmin(ExpressOwner owner) {
		if(owner.getUserId()!=null && owner.getUserId() ==1L){
			return true;
		}
		List<OrganizationCommunityDTO> organizationCommunityList = organizationProvider.findOrganizationCommunityByCommunityId(owner.getOwnerId());
		if (organizationCommunityList != null && !organizationCommunityList.isEmpty()) {
			Long organizationId = organizationCommunityList.get(0).getOrganizationId();
			SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
			boolean result = resolver.checkSuperAdmin(owner.getUserId(), organizationId);
			if (result) {
				return result;
			}
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.PRIVILEGE_ERROR, "privilege error, organizationId="+organizationId+", userId="+owner.getUserId());
		}
		throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.PRIVILEGE_ERROR, "privilege error, no organization");
	}
	
	private void addExpressUser(ExpressOwner owner, CreateExpressUserDTO createExpressUserDTO, AddExpressUserCommand cmd) {
		if (createExpressUserDTO.getOrganizationId() == null || createExpressUserDTO.getOrganizationMemberId() == null || createExpressUserDTO.getUserId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_LACK, "invalid parameters");
		}
		ExpressUser expressUser = null;
		if ((expressUser = checkExistsExpressUser(owner, createExpressUserDTO, cmd)) != null) {
			expressUser.setStatus(CommonStatus.ACTIVE.getCode());
			expressUserProvider.updateExpressUser(expressUser);
		}else {
			expressUser = new ExpressUser();
			expressUser.setNamespaceId(owner.getNamespaceId());
			expressUser.setOwnerType(owner.getOwnerType().getCode());
			expressUser.setOwnerId(owner.getOwnerId());
			expressUser.setServiceAddressId(cmd.getServiceAddressId());
			expressUser.setExpressCompanyId(cmd.getExpressCompanyId());
			expressUser.setOrganizationId(createExpressUserDTO.getOrganizationId());
			expressUser.setOrganizationMemberId(createExpressUserDTO.getOrganizationMemberId());
			expressUser.setUserId(createExpressUserDTO.getUserId());
			expressUser.setStatus(CommonStatus.ACTIVE.getCode());
			expressUserProvider.createExpressUser(expressUser);
		}
	}
	
	private ExpressUser checkExistsExpressUser(ExpressOwner owner, CreateExpressUserDTO createExpressUserDTO, AddExpressUserCommand cmd) {
		return expressUserProvider.findExpressUserByUserId(owner.getNamespaceId(), owner.getOwnerType(), owner.getOwnerId(), createExpressUserDTO.getUserId(), cmd.getServiceAddressId(), cmd.getExpressCompanyId());
	}

	@Override
	public void deleteExpressUser(DeleteExpressUserCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_LACK, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkAdmin(owner);
		ExpressUser expressUser = expressUserProvider.findExpressUserById(cmd.getId());
		if (expressUser == null || expressUser.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressUser.getOwnerType().equals(owner.getOwnerType().getCode())
				|| expressUser.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		expressUser.setStatus(CommonStatus.INACTIVE.getCode());
		expressUserProvider.updateExpressUser(expressUser);
	}

	@Override
	public ListExpressOrderResponse listExpressOrder(ListExpressOrderCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4070040720L, cmd.getAppId(), null,cmd.getCurrentProjectId());//参数设置权限
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkPrivilege(owner, cmd.getServiceAddressId(), cmd.getExpressCompanyId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		ListExpressOrderCondition condition = ConvertHelper.convert(cmd, ListExpressOrderCondition.class);
		condition.setPageSize(pageSize);
		condition.setNamespaceId(owner.getNamespaceId());
		List<ExpressOrder> expressOrders = expressOrderProvider.listExpressOrderByCondition(condition);
		Long nextPageAnchor = null;
		if (expressOrders.size() > pageSize) {
			expressOrders.remove(expressOrders.size()-1);
			nextPageAnchor = expressOrders.get(expressOrders.size()-1).getId();
		}
		return new ListExpressOrderResponse(nextPageAnchor, expressOrders.stream().map(this::convertToExpressOrderDTOForWebList).collect(Collectors.toList()));
	}

	private boolean checkPrivilege(ExpressOwner owner, Long serviceAddressId, Long expressCompanyId) {
		//检查该用户是否在权限列表中
//		ExpressUser expressUser = expressUserProvider.findExpressUserByUserId(owner.getNamespaceId(), owner.getOwnerType(), owner.getOwnerId(), owner.getUserId(), serviceAddressId, expressCompanyId);
//		if (expressUser == null || CommonStatus.fromCode(expressUser.getStatus()) != CommonStatus.ACTIVE) {
//			//检查是否为管理员
//			return checkAdmin(owner);
//		}
		return true;
	}

	private ExpressOrderDTO convertToExpressOrderDTOForWebList(ExpressOrder expressOrder) {
		ExpressOrderDTO expressOrderDTO = new ExpressOrderDTO();
		expressOrderDTO.setId(expressOrder.getId());
		expressOrderDTO.setSendName(expressOrder.getSendName());
		expressOrderDTO.setSendPhone(expressOrder.getSendPhone());
		expressOrderDTO.setExpressCompanyName(getExpressCompany(expressOrder.getExpressCompanyId()));
		expressOrderDTO.setBillNo(expressOrder.getBillNo());
		expressOrderDTO.setSendType(expressOrder.getSendType());
//		expressOrderDTO.setPayType(expressOrder.getPayType());
		expressOrderDTO.setStatus(expressOrder.getStatus());
		expressOrderDTO.setPaySummary(expressOrder.getPaySummary());
		expressOrderDTO.setReceiveName(expressOrder.getReceiveName());
		expressOrderDTO.setReceivePhone(expressOrder.getReceivePhone());
		expressOrderDTO.setFlowCaseId(expressOrder.getFlowCaseId());
		expressOrderDTO.setExpressTarget(expressOrder.getExpressTarget());
		expressOrderDTO.setExpressRemark(expressOrder.getExpressRemark());
		expressOrderDTO.setExpressWay(expressOrder.getExpressWay());
		return expressOrderDTO;
	}
	
	private String getExpressCompany(Long id) {
		ExpressCompany expressCompany = expressCompanyProvider.findExpressCompanyById(id);
		return expressCompany == null ||  expressCompany.getStatus() == 1? null : expressCompany.getName();
	}

	@Override
	public GetExpressOrderDetailResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_LACK, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(cmd.getId());
		//by dengs,向中国邮政请求订单状态
		ExpressCompany company = findTopExpressCompany(expressOrder.getExpressCompanyId());
		ExpressHandler handler = getExpressHandler(company.getId());
		handler.getOrderStatus(expressOrder, company);
		if (expressOrder != null) {
			if (expressOrder.getCreatorUid().longValue() == owner.getUserId().longValue() || checkPrivilege(owner, expressOrder.getServiceAddressId(), expressOrder.getExpressCompanyId())) {
				return new GetExpressOrderDetailResponse(convertToExpressOrderDTOForDetail(expressOrder));
			}
		}
		throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.PRIVILEGE_ERROR, "privilege error, no express order, id="+cmd.getId());
	}

	private ExpressOrderDTO convertToExpressOrderDTOForDetail(ExpressOrder expressOrder) {
		ExpressOrderDTO expressOrderDTO = ConvertHelper.convert(expressOrder, ExpressOrderDTO.class);
		expressOrderDTO.setExpressCompanyName(getExpressCompany(expressOrder.getExpressCompanyId()));
		expressOrderDTO.setServiceAddress(getServiceAddress(expressOrder.getServiceAddressId()));
		return expressOrderDTO;
	}

	private String getServiceAddress(Long id) {
		ExpressServiceAddress expressServiceAddress = expressServiceAddressProvider.findExpressServiceAddressById(id);
		return expressServiceAddress == null ? null : expressServiceAddress.getName();
	}

	@Override
	public void updatePaySummary(UpdatePaySummaryCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			if (checkPrivilege(owner, expressOrder.getServiceAddressId(), expressOrder.getExpressCompanyId())) {
				if (ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.WAITING_FOR_PAY || TrueOrFalseFlag.fromCode(expressOrder.getPaidFlag()) == TrueOrFalseFlag.TRUE) {
					throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying");
				}
				ExpressActionEnum action = null;
				if (expressOrder.getPaySummary() == null) {
					action = ExpressActionEnum.CONFIRM_MONEY;
				}else {
					action = ExpressActionEnum.UPDATE_MONEY;
				}
				expressOrder.setPaySummary(cmd.getPaySummary());
				expressOrderProvider.updateExpressOrder(expressOrder);
				createExpressOrderLog(owner, action, expressOrder, expressOrder.getPaySummary().toString());
			}
			return null;
		});
	}

	@Override
	public CommonOrderDTO payExpressOrder(PayExpressOrderCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		return (CommonOrderDTO)coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			ExpressSendType sendType = ExpressSendType.fromCode(expressOrder.getSendType());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue() || expressOrder.getCreatorUid().longValue() != owner.getUserId().longValue()
					|| sendType == ExpressSendType.GUO_MAO_EXPRESS) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_ABNORMAL, "order abnormal");
			}
			if (ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.WAITING_FOR_PAY) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying");
			}
			if (expressOrder.getPaySummary() == null) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying");
			}
			if (TrueOrFalseFlag.fromCode(expressOrder.getPaidFlag()) != TrueOrFalseFlag.TRUE) {
				expressOrder.setPaidFlag(TrueOrFalseFlag.TRUE.getCode());
				expressOrderProvider.updateExpressOrder(expressOrder);
			}
//			// 这里是费代码 by dengs.-------------------正式环境需要注释掉-------------
//			ExpressCompany expressCompany = findTopExpressCompany(expressOrder.getExpressCompanyId());
//			ExpressHandler handler = getExpressHandler(expressCompany.getId());
//			expressOrder.setStatus(ExpressOrderStatus.PAID.getCode());
//			handler.updateOrderStatus(expressOrder, expressCompany);
//			expressOrderProvider.updateExpressOrder(expressOrder);
			
			createExpressOrderLog(owner, ExpressActionEnum.PAYING, expressOrder, null);
			
			//调用统一处理订单接口，返回统一订单格式
			CommonOrderCommand orderCmd = new CommonOrderCommand();
			String body = "";
			if(expressOrder.getSendName()!=null){
				body = expressOrder.getSendName();
			}
			else if(expressOrder.getReceiveName()!=null){
				body = expressOrder.getReceiveName();
			}
			orderCmd.setBody(body);
			orderCmd.setOrderNo(expressOrder.getId().toString());
			orderCmd.setOrderType(OrderType.OrderTypeEnum.EXPRESS_ORDER.getPycode());
			orderCmd.setSubject("快递订单");
			orderCmd.setTotalFee(expressOrder.getPaySummary());
			CommonOrderDTO dto = null;
			try {
				dto = orderUtil.convertToCommonOrderTemplate(orderCmd);
			} catch (Exception e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"convertToCommonOrder is fail.");
			}
			return dto;
		}).first();
	}
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		// 回调是没有UserContext的，这里需要加一个
		UserContext.current().setUser(new User(1L));
		Long orderId = Long.valueOf(cmd.getOrderNo());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + orderId).enter(() -> {
			//为什么用id，我也不知道。参考下单用的id
			ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(orderId);
			if (expressOrder == null) {
				LOGGER.error("not exists order, orderId="+orderId);
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_NOT_EXIST, "invalid parameters");
			}
			
			//加一个开关，方便在beta环境测试
			boolean flag = configProvider.getBooleanValue("beta.express.order.amount", false);
			if (!flag) {
				if (0!=expressOrder.getPaySummary().compareTo(new BigDecimal(cmd.getPayAmount()))) {
					LOGGER.error("order money error, paySummary="+expressOrder.getPaySummary()+", payAmout="+cmd.getPayAmount());
					throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_ABNORMAL, "invalid parameters");
				}
			}

			//modify by dengs,20170817,支付需要干什么，各个快递公司流程不一样
			ExpressCompany expressCompany = findTopExpressCompany(expressOrder.getExpressCompanyId());
			ExpressHandler handler = getExpressHandler(expressCompany.getId());
			expressOrder.setStatus(ExpressOrderStatus.PAID.getCode());
			handler.updateOrderStatus(expressOrder, expressCompany);
			expressOrderProvider.updateExpressOrder(expressOrder);
			
			ExpressOwner owner = new ExpressOwner(expressOrder.getNamespaceId(), ExpressOwnerType.fromCode(expressOrder.getOwnerType()), expressOrder.getOwnerId(), expressOrder.getCreatorUid());
			createExpressOrderLog(owner, ExpressActionEnum.PAID, expressOrder, "pay success: " + StringHelper.toJsonString(cmd));
			return null;
		});
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		// 回调是没有UserContext的，这里需要加一个
		UserContext.current().setUser(new User(1L));
		Long orderId = Long.valueOf(cmd.getOrderNo());
		ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(orderId);
		if (expressOrder == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = new ExpressOwner(expressOrder.getNamespaceId(), ExpressOwnerType.fromCode(expressOrder.getOwnerType()), expressOrder.getOwnerId(), expressOrder.getCreatorUid());
		createExpressOrderLog(owner, ExpressActionEnum.PAYING, expressOrder, "pay fail: " + StringHelper.toJsonString(cmd));
	}
	
	@Override
	public void printExpressOrder(PrintExpressOrderCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_ABNORMAL, "order abnormal");
			}
			if (checkPrivilege(owner, expressOrder.getServiceAddressId(), expressOrder.getExpressCompanyId())) {
				ExpressOrderStatus status = ExpressOrderStatus.fromCode(expressOrder.getStatus());
				if (status != ExpressOrderStatus.PAID && status != ExpressOrderStatus.PRINTED) {
					throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be paid or printed");
				}
				if (expressOrder.getBillNo() == null) {
					ExpressHandler handler = getExpressHandler(expressOrder.getExpressCompanyId());
					if (handler != null) {
						// 每个快递公司返回的快递单号通过各自的handler获取
						expressOrder.setPrintTime(new Timestamp(DateHelper.currentGMTTime().getTime())); //打印时间，需要传给ems
						String billNo = handler.getBillNo(expressOrder);
						expressOrder.setBillNo(billNo);
					}
					expressOrder.setStatus(ExpressOrderStatus.PRINTED.getCode());
					expressOrderProvider.updateExpressOrder(expressOrder);
				}
				createExpressOrderLog(owner, ExpressActionEnum.PRINT, expressOrder, null);
			}
			return null;
		});
	}

	private ExpressHandler getExpressHandler(Long expressCompanyId) {
		ExpressCompany expressCompany = findTopExpressCompany(expressCompanyId);
		return PlatformContext.getComponent(ExpressHandler.EXPRESS_HANDLER_PREFIX+expressCompany.getId());
	}

	private ExpressCompany findTopExpressCompany(Long expressCompanyId) {
		ExpressCompany expressCompany = expressCompanyProvider.findExpressCompanyById(expressCompanyId);
		if (expressCompany.getParentId().longValue() != 0L) {
			return findTopExpressCompany(expressCompany.getParentId());
		}
		return expressCompany;
	}
	
	@Override
	public CreateOrUpdateExpressAddressResponse createOrUpdateExpressAddress(CreateOrUpdateExpressAddressCommand cmd) {
		if (cmd.getCategory() == null || cmd.getUserName() == null || StringUtils.isEmpty(cmd.getPhone()) || StringUtils.isEmpty(cmd.getProvince())
				|| StringUtils.isEmpty(cmd.getCity()) || StringUtils.isEmpty(cmd.getCounty()) || StringUtils.isEmpty(cmd.getDetailAddress()) || cmd.getDefaultFlag() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressAddress address = dbProvider.execute(s->{
			ExpressAddress expressAddress = null;
			if (cmd.getId() == null) {
				expressAddress = createExpressAddress(owner, cmd);
			}else {
				expressAddress = updateExpressAddress(owner, cmd);
			}
			if (TrueOrFalseFlag.fromCode(expressAddress.getDefaultFlag()) == TrueOrFalseFlag.TRUE) {
				updateOtherAddressToNotDefault(owner, expressAddress);
			}
			return expressAddress;
		});
		
		return new CreateOrUpdateExpressAddressResponse(convertToExpressAddressDTO(address));
	}

	private ExpressAddress createExpressAddress(ExpressOwner owner, CreateOrUpdateExpressAddressCommand cmd) {
		ExpressAddress expressAddress = new ExpressAddress();
		expressAddress.setNamespaceId(owner.getNamespaceId());
		expressAddress.setOwnerType(owner.getOwnerType().getCode());
		expressAddress.setOwnerId(owner.getOwnerId());
		expressAddress.setUserName(cmd.getUserName());
		expressAddress.setOrganizationId(cmd.getOrganizationId());
		expressAddress.setOrganizationName(cmd.getOrganizationName());
		expressAddress.setPhone(cmd.getPhone());
		expressAddress.setProvinceId(cmd.getProvinceId());
		expressAddress.setCityId(cmd.getCityId());
		expressAddress.setCountyId(cmd.getCountyId());
		expressAddress.setProvince(cmd.getProvince());
		expressAddress.setCity(cmd.getCity());
		expressAddress.setCounty(cmd.getCounty());
		expressAddress.setDetailAddress(cmd.getDetailAddress());
		expressAddress.setCategory(cmd.getCategory());
		expressAddress.setStatus(CommonStatus.ACTIVE.getCode());
		//第一个添加的地址自动设置为默认地址
		ExpressAddress anyExpressAddress = expressAddressProvider.findAnyExpressAddressByOwner(owner, cmd.getCategory());
		if (anyExpressAddress == null) {
			expressAddress.setDefaultFlag(TrueOrFalseFlag.TRUE.getCode());
		}else {
			expressAddress.setDefaultFlag(cmd.getDefaultFlag());
		}
		expressAddressProvider.createExpressAddress(expressAddress);
		return expressAddress;
	}

	private ExpressAddress updateExpressAddress(ExpressOwner owner, CreateOrUpdateExpressAddressCommand cmd) {
		ExpressAddress expressAddress = expressAddressProvider.findExpressAddressById(cmd.getId());
		if (expressAddress == null || !expressAddress.getOwnerType().equals(owner.getOwnerType().getCode()) || expressAddress.getOwnerId().longValue() != owner.getOwnerId().longValue()
				|| expressAddress.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || expressAddress.getCreatorUid().longValue() != owner.getUserId()
				|| expressAddress.getCategory().byteValue() != cmd.getCategory().byteValue()) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		expressAddress.setUserName(cmd.getUserName());
		expressAddress.setOrganizationId(cmd.getOrganizationId());
		expressAddress.setOrganizationName(cmd.getOrganizationName());
		expressAddress.setPhone(cmd.getPhone());
		expressAddress.setProvinceId(cmd.getProvinceId());
		expressAddress.setCityId(cmd.getCityId());
		expressAddress.setCountyId(cmd.getCountyId());
		expressAddress.setProvince(cmd.getProvince());
		expressAddress.setCity(cmd.getCity());
		expressAddress.setCounty(cmd.getCounty());
		expressAddress.setDetailAddress(cmd.getDetailAddress());
		expressAddress.setCategory(cmd.getCategory());
		expressAddress.setStatus(CommonStatus.ACTIVE.getCode());
		//更新地址时如果当前地址是默认地址，不能手动设置成非默认地址
		if (TrueOrFalseFlag.fromCode(expressAddress.getDefaultFlag()) != TrueOrFalseFlag.TRUE) {
			expressAddress.setDefaultFlag(cmd.getDefaultFlag());
		}
		expressAddressProvider.updateExpressAddress(expressAddress);
		return expressAddress;
	}

	private void updateOtherAddressToNotDefault(ExpressOwner owner, ExpressAddress expressAddress) {
		expressAddressProvider.updateOtherAddressToNotDefault(owner.getNamespaceId(), owner.getOwnerType(), owner.getOwnerId(), owner.getUserId(), expressAddress.getId(), expressAddress.getCategory());
	}

	private ExpressAddressDTO convertToExpressAddressDTO(ExpressAddress expressAddress) {
		return ConvertHelper.convert(expressAddress, ExpressAddressDTO.class);
	}

	@Override
	public void deleteExpressAddress(DeleteExpressAddressCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressAddress expressAddress = expressAddressProvider.findExpressAddressById(cmd.getId());
		if (expressAddress == null || !expressAddress.getOwnerType().equals(owner.getOwnerType().getCode()) || expressAddress.getOwnerId().longValue() != owner.getOwnerId().longValue()
				|| expressAddress.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || expressAddress.getCreatorUid().longValue() != owner.getUserId()) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		dbProvider.execute(s->{
			expressAddress.setStatus(CommonStatus.INACTIVE.getCode());
			expressAddressProvider.updateExpressAddress(expressAddress);
			//如果删除的是默认地址，则自动把后面第一条设置为默认地址
			if (TrueOrFalseFlag.fromCode(expressAddress.getDefaultFlag()) == TrueOrFalseFlag.TRUE) {
				updateFirstToDefault(owner, expressAddress.getCategory());
			}
			return null;
		});
	}

	private void updateFirstToDefault(ExpressOwner owner, Byte category) {
		List<ExpressAddress> expressAddresses = expressAddressProvider.listExpressAddressByOwner(owner, category);
		if (expressAddresses != null && expressAddresses.size() > 0) {
			ExpressAddress expressAddress = expressAddresses.get(0);
			expressAddress.setDefaultFlag(TrueOrFalseFlag.TRUE.getCode());
			expressAddressProvider.updateExpressAddress(expressAddress);
		}
	}

	@Override
	public ListExpressAddressResponse listExpressAddress(ListExpressAddressCommand cmd) {
		if (cmd.getCategory() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		List<ExpressAddress> expressAddresses = expressAddressProvider.listExpressAddressByOwner(owner, cmd.getCategory());
		return new ListExpressAddressResponse(expressAddresses.stream().map(this::convertToExpressAddressDTO).collect(Collectors.toList()));
	}

	@Override
	public CreateExpressOrderResponse createExpressOrder(CreateExpressOrderCommand cmd) {
		checkCreateExpressOrderCommand(cmd);
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkExpressParams(cmd, owner);
		ExpressOrder expressOrder = generateExpressOrder(owner, cmd);
		// by dengs, 创建订单，这里就直接丢给邮政和国贸EMS
		dbProvider.execute(status -> {
			ExpressCompany expressCompany = findTopExpressCompany(cmd.getExpressCompanyId());
			ExpressHandler handler = getExpressHandler(expressCompany.getId());
			handler.createOrder(expressOrder, expressCompany);//同城信筒并不在此给邮政创建订单，而是在支付之后创建订单
			expressOrderProvider.createExpressOrder(expressOrder);
			handler.afterCreateOrder(expressOrder, expressCompany);//同城信筒并不在此给邮政创建订单，而是在支付之后创建订单
			return null;
		});
		createExpressOrderLog(owner, ExpressActionEnum.CREATE, expressOrder, null);
		return new CreateExpressOrderResponse(convertToExpressOrderDTOForDetail(expressOrder));
	}

	private void checkCreateExpressOrderCommand(CreateExpressOrderCommand cmd) {
		ExpressSendType sendType = ExpressSendType.fromCode(cmd.getSendType());
		if(sendType == null){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		//接受地址和寄送地址不校验，因为存在地址为临时使用的情况。
		//通用参数校验
		if (cmd.getExpressCompanyId() == null 
				|| cmd.getSendMode() == null || cmd.getPayType() == null) {
			LOGGER.error("invalid parameters"
					+ " receiveAddressId = null or expressCompanyId = null or sendMode = null or payType = null");
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
					ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		//华润ems快递，参数校验 
		if (sendType == ExpressSendType.STANDARD){
			if(cmd.getServiceAddressId() == null )
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
						ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		//邮政快递包裹 信筒
		if(sendType == ExpressSendType.CHINA_POST_PACKAGE || sendType == ExpressSendType.CITY_EMPTIES){
			ExpressPackageType packageType = ExpressPackageType.fromCode(cmd.getPackageType());
			if(packageType == null){
				LOGGER.error("unknown package type = " + cmd.getPackageType());
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
						ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			ExpressInvoiceFlagType invoiceFlagType = ExpressInvoiceFlagType.fromCode(cmd.getInvoiceFlag());
			if(invoiceFlagType == null){
				LOGGER.error("unknown invoiceFlag Type = " + cmd.getInvoiceFlag());
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
						ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			if(invoiceFlagType == ExpressInvoiceFlagType.NEED_TAX_INVOIE && (cmd.getInvoiceHead() == null || cmd.getInvoiceHead().length() == 0)){
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
						ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
		}
		//国贸ems校验 TODO
	}

	//快递公司对应的业务检查，业务对应的包装类型检查。
	private void checkExpressParams(CreateExpressOrderCommand cmd, ExpressOwner owner) {
		ExpressCompany company = findTopExpressCompany(cmd.getExpressCompanyId());
		if(company == null){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
					ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		List<ExpressCompanyBusiness> list = expressCompanyBusinessProvider.listExpressSendTypesByOwner(owner.getNamespaceId(), EntityType.NAMESPACE.getCode(),
				Long.valueOf(owner.getNamespaceId()), company.getId());
		if(list == null || list.size() == 0){
			LOGGER.error("expressCompany = " + company.getName() + " mismatch sendType = "+cmd.getSendType());
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE,
					ExpressServiceErrorCode.GET_COMPANY_FAILED, "query company failed");
		}
		boolean isInvaildSendType = true;
		ExpressCompanyBusiness business = null;
		for (ExpressCompanyBusiness r : list) {
			if(r.getSendType().longValue() == cmd.getSendType().longValue()){
				isInvaildSendType = false;
				business = r;
				break;
			}
		}
		if(isInvaildSendType){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		List<ExpressPackageTypeDTO> dtos = convertPackageTypesList(business.getPackageTypes());
		if(dtos == null || dtos.size() == 0){
			return ;
		}
		if(!dtos.stream().map(r-> r.getPackageType().longValue()).collect(Collectors.toList()).contains(cmd.getPackageType().longValue())){
			LOGGER.error("sendType = " + cmd.getSendType() + " mismatch packageType = "+cmd.getPackageType());
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
	}

	private void createExpressOrderLog(ExpressOwner owner, ExpressActionEnum action, ExpressOrder expressOrder, String remark) {
		ExpressOrderLog expressOrderLog = new ExpressOrderLog();
		expressOrderLog.setNamespaceId(owner.getNamespaceId());
		expressOrderLog.setOwnerType(owner.getOwnerType().getCode());
		expressOrderLog.setOwnerId(owner.getOwnerId());
		expressOrderLog.setOrderId(expressOrder.getId());
		expressOrderLog.setAction(action.getCode());
		expressOrderLog.setRemark(remark);
		expressOrderLogProvider.createExpressOrderLog(expressOrderLog);
	}
	
	private ExpressOrder generateExpressOrder(ExpressOwner owner, CreateExpressOrderCommand cmd) {
		ExpressOrder expressOrder = new ExpressOrder();
		expressOrder.setNamespaceId(owner.getNamespaceId());
		expressOrder.setOwnerType(owner.getOwnerType().getCode());
		expressOrder.setOwnerId(owner.getOwnerId());
		expressOrder.setOrderNo(getOrderNo(owner.getUserId()));
		if(cmd.getSendAddressId() != null){
			ExpressAddress sendAddress = expressAddressProvider.findExpressAddressById(cmd.getSendAddressId());
			if (sendAddress == null  && cmd.getSendType().intValue() != ExpressSendType.CITY_EMPTIES.getCode().intValue()) {
				LOGGER.error("not exists send address: id="+cmd.getSendAddressId());
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			//同城信筒没有寄件地址
			if(cmd.getSendType().intValue() != ExpressSendType.CITY_EMPTIES.getCode().intValue()){
				expressOrder.setSendName(sendAddress.getUserName());
				expressOrder.setSendPhone(sendAddress.getPhone());
				expressOrder.setSendOrganization(sendAddress.getOrganizationName());
				expressOrder.setSendProvince(sendAddress.getProvince());
				expressOrder.setSendCity(sendAddress.getCity());
				expressOrder.setSendCounty(sendAddress.getCounty());
				expressOrder.setSendDetailAddress(sendAddress.getDetailAddress());
			}
		}else{
			//同城信筒没有寄件地址
			if(cmd.getSendType().intValue() != ExpressSendType.CITY_EMPTIES.getCode().intValue()){
				if(cmd.getSendName() == null || cmd.getSendPhone() == null ||
						cmd.getSendProvince() == null || cmd.getSendCity() == null || 
						cmd.getSendCounty() == null || cmd.getSendDetailAddress() == null){
					throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
				}
				expressOrder.setSendName(cmd.getSendName());
				expressOrder.setSendPhone(cmd.getSendPhone());
				expressOrder.setSendOrganization(cmd.getSendOrganization());
				expressOrder.setSendProvince(cmd.getSendProvince());
				expressOrder.setSendCity(cmd.getSendCity());
				expressOrder.setSendCounty(cmd.getSendCounty());
				expressOrder.setSendDetailAddress(cmd.getSendDetailAddress());
			}
		}
		if(cmd.getReceiveAddressId() != null){
			ExpressAddress receiveAddress = expressAddressProvider.findExpressAddressById(cmd.getReceiveAddressId());
			if (receiveAddress == null) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			expressOrder.setReceiveName(receiveAddress.getUserName());
			expressOrder.setReceivePhone(receiveAddress.getPhone());
			expressOrder.setReceiveOrganization(receiveAddress.getOrganizationName());
			expressOrder.setReceiveProvince(receiveAddress.getProvince());
			expressOrder.setReceiveCity(receiveAddress.getCity());
			expressOrder.setReceiveCounty(receiveAddress.getCounty());
			expressOrder.setReceiveDetailAddress(receiveAddress.getDetailAddress());
		}else{
			if(cmd.getSendType().intValue() != ExpressSendType.GUO_MAO_EXPRESS.getCode().intValue()) {
				if (cmd.getReceiveName() == null || cmd.getReceivePhone() == null ||
						cmd.getReceiveProvince() == null || cmd.getReceiveCity() == null ||
						cmd.getReceiveCounty() == null || cmd.getReceiveDetailAddress() == null) {
					throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
				}
				expressOrder.setReceiveName(cmd.getReceiveName());
				expressOrder.setReceivePhone(cmd.getReceivePhone());
				expressOrder.setReceiveOrganization(cmd.getReceiveOrganization());
				expressOrder.setReceiveProvince(cmd.getReceiveProvince());
				expressOrder.setReceiveCity(cmd.getReceiveCity());
				expressOrder.setReceiveCounty(cmd.getReceiveCounty());
				expressOrder.setReceiveDetailAddress(cmd.getReceiveDetailAddress());
			}
		}
		expressOrder.setServiceAddressId(cmd.getServiceAddressId());
		expressOrder.setExpressCompanyId(cmd.getExpressCompanyId());
		expressOrder.setSendType(cmd.getSendType());
		expressOrder.setSendMode(cmd.getSendMode());
		expressOrder.setPayType(cmd.getPayType());
		expressOrder.setInternal(cmd.getInternal());
		expressOrder.setInsuredPrice(cmd.getInsuredPrice());
		expressOrder.setStatus(ExpressOrderStatus.WAITING_FOR_PAY.getCode());
		expressOrder.setPaidFlag(TrueOrFalseFlag.FALSE.getCode());
		// add by dengs,invoice add!
		expressOrder.setInvoiceFlag(cmd.getInvoiceFlag());
		expressOrder.setInvoiceHead(cmd.getInvoiceHead());
		expressOrder.setPackageType(cmd.getPackageType());
		expressOrder.setExpressRemark(cmd.getExpressRemark());
		expressOrder.setExpressTarget(cmd.getExpressTarget());
		expressOrder.setExpressType(cmd.getExpressType());
		expressOrder.setExpressWay(cmd.getExpressWay());
		return expressOrder;
	}

	private String getOrderNo(Long userId) {
		//订单编号生成规则，时间戳+userId+补全的随机码（3位），总位数大于或等于18位
		StringBuilder sb = new StringBuilder(20);
		sb.append(System.currentTimeMillis()/1000).append(userId);
		int random = ThreadLocalRandom.current().nextInt(100, 1000);
		if (sb.length() > ORDER_NO_LENGTH - 3) {
			sb.append(random);
		}else {
			int randomLength = ORDER_NO_LENGTH - sb.length();
			sb.append(String.format("%0"+randomLength+"d", random));
		}
		return sb.toString();
	}

	@Override
	public ListPersonalExpressOrderResponse listPersonalExpressOrder(ListPersonalExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		ListExpressOrderCondition condition = ConvertHelper.convert(cmd, ListExpressOrderCondition.class);
		condition.setPageSize(pageSize);
		condition.setNamespaceId(owner.getNamespaceId());
		condition.setUserId(owner.getUserId());
		List<ExpressOrder> expressOrders = expressOrderProvider.listExpressOrderByCondition(condition);
		Long nextPageAnchor = null;
		if (expressOrders.size() > pageSize) {
			expressOrders.remove(expressOrders.size()-1);
			nextPageAnchor = expressOrders.get(expressOrders.size()-1).getId();
		}
		return new ListPersonalExpressOrderResponse(nextPageAnchor, expressOrders.stream().map(this::convertToExpressOrderDTOForClientList).collect(Collectors.toList()));
	}

	private ExpressOrderDTO convertToExpressOrderDTOForClientList(ExpressOrder expressOrder) {
		ExpressOrderDTO expressOrderDTO = new ExpressOrderDTO();
		expressOrderDTO.setId(expressOrder.getId());
		expressOrderDTO.setBillNo(expressOrder.getBillNo());
		expressOrderDTO.setSendCity(expressOrder.getSendCity());
		expressOrderDTO.setSendName(expressOrder.getSendName());
		expressOrderDTO.setStatus(expressOrder.getStatus());
		expressOrderDTO.setReceiveCity(expressOrder.getReceiveCity());
		expressOrderDTO.setReceiveName(expressOrder.getReceiveName());
		expressOrderDTO.setCreateTime(expressOrder.getUpdateTime());
		expressOrderDTO.setPaySummary(expressOrder.getPaySummary());
		expressOrderDTO.setSendType(expressOrder.getSendType());//加上业务类别
		expressOrderDTO.setExpressLogoUrl(getUrl(getExpressCompanyLogo(expressOrder.getExpressCompanyId())));
		expressOrderDTO.setFlowCaseId(expressOrder.getFlowCaseId());
		expressOrderDTO.setExpressTarget(expressOrder.getExpressTarget());
		expressOrderDTO.setExpressRemark(expressOrder.getExpressRemark());
		expressOrderDTO.setExpressWay(expressOrder.getExpressWay());
		return expressOrderDTO;
	}
	
	private String getExpressCompanyLogo(Long expressCompanyId) {
		ExpressCompany expressCompany = expressCompanyProvider.findExpressCompanyById(expressCompanyId);
		if (expressCompany != null) {
			String logo = expressCompany.getLogo();
			if (logo != null) {
				return logo;
			}
			if (expressCompany.getParentId().longValue() != 0L) {
				return getExpressCompanyLogo(expressCompany.getParentId());
			}
		}
		return null;
	}

	@Override
	public void cancelExpressOrder(CancelExpressOrderCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue() || expressOrder.getCreatorUid().longValue() != owner.getUserId().longValue()) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_ABNORMAL, "order abnormal");
			}
			if (ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.WAITING_FOR_PAY) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying and express user has not confirmed money");
			}
			expressOrder.setStatus(ExpressOrderStatus.CANCELLED.getCode());
			expressOrder.setStatusDesc(cmd.getStatusDesc());
			ExpressCompany expressCompany = findTopExpressCompany(expressOrder.getExpressCompanyId());
			ExpressHandler handler = getExpressHandler(expressCompany.getId());
			dbProvider.execute(status->{
				handler.updateOrderStatus(expressOrder, expressCompany);
				expressOrderProvider.updateExpressOrder(expressOrder);
				return null;
			});
			createExpressOrderLog(owner, ExpressActionEnum.CANCEL, expressOrder, null);
			return null;
		});
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd) {
		if (cmd.getExpressCompanyId() == null || StringUtils.isEmpty(cmd.getBillNo())) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressCompany expressCompany = findTopExpressCompany(cmd.getExpressCompanyId());
		if (expressCompany.getId().longValue() == cmd.getExpressCompanyId().longValue()) {
			putIntoHistory(cmd.getExpressCompanyId(), cmd.getBillNo());
		}
		ExpressHandler handler = getExpressHandler(expressCompany.getId());
		return handler.getExpressLogisticsDetail(expressCompany, cmd.getBillNo());
	}

	@Override
	public String getUrl(String uri) {
		try {
			return contentServerService.parserUri(uri, "", null);
		} catch (Exception e) {

		}
		return "";
	}
	
	private void putIntoHistory(Long expressCompanyId, String billNo) {
		User user = UserContext.current().getUser();
		if (user != null) {
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			ExpressQueryHistory expressQueryHistory = expressQueryHistoryProvider.findExpressQueryHistory(namespaceId, user.getId(), expressCompanyId, billNo);
			if (expressQueryHistory == null) {
				expressQueryHistory = new ExpressQueryHistory();
				expressQueryHistory.setNamespaceId(namespaceId);
				expressQueryHistory.setExpressCompanyId(expressCompanyId);
				expressQueryHistory.setBillNo(billNo);
				expressQueryHistory.setStatus(CommonStatus.ACTIVE.getCode());
				expressQueryHistoryProvider.createExpressQueryHistory(expressQueryHistory);
			}else {
				expressQueryHistoryProvider.updateExpressQueryHistory(expressQueryHistory);
			}
		}
	}
	
	@Override
	public ListExpressQueryHistoryResponse listExpressQueryHistory(Integer pageSize) {
		User user = UserContext.current().getUser();
		if (user != null) {
			Integer namespaecId = UserContext.getCurrentNamespaceId();
			List<ExpressQueryHistory> expressQueryHistories = expressQueryHistoryProvider.listExpressQueryHistoryByUser(namespaecId, user.getId(), pageSize);
			return new ListExpressQueryHistoryResponse(expressQueryHistories.stream().map(this::convertToExpressQueryHistoryDTO).collect(Collectors.toList()));
		}
		return null;
	}
	
	private ExpressQueryHistoryDTO convertToExpressQueryHistoryDTO(ExpressQueryHistory expressQueryHistory) {
		ExpressQueryHistoryDTO expressQueryHistoryDTO = ConvertHelper.convert(expressQueryHistory, ExpressQueryHistoryDTO.class);
		expressQueryHistoryDTO.setExpressCompany(getUrl(getExpressCompanyLogo(expressQueryHistory.getExpressCompanyId())));
		return expressQueryHistoryDTO;
	}

	@Override
	public void clearExpressQueryHistory() {
		User user = UserContext.current().getUser();
		if (user != null) {
			Integer namespaecId = UserContext.getCurrentNamespaceId();
			expressQueryHistoryProvider.clearExpressQueryHistory(namespaecId, user.getId());
		}
	}

	@Override
	public GetExpressParamSettingResponse getExpressParamSetting() {
		int ownerId = UserContext.getCurrentNamespaceId();
		int namespaceId = ownerId;
		String ownerType = EntityType.NAMESPACE.getCode();
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(namespaceId,ownerType,ownerId);
		return ConvertHelper.convert(setting, GetExpressParamSettingResponse.class);
	}

	@Override
	public GetExpressBusinessNoteResponse getExpressBusinessNote(GetExpressBusinessNoteCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4070040710L, cmd.getAppId(), null,cmd.getCurrentProjectId());//参数设置权限
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(owner.getNamespaceId(),owner.getOwnerType().getCode(),owner.getOwnerId());
		return ConvertHelper.convert(setting, GetExpressBusinessNoteResponse.class);
	}

	@Override
	public void updateExpressBusinessNote(UpdateExpressBusinessNoteCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4070040710L, cmd.getAppId(), null,cmd.getCurrentProjectId());//参数设置权限
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		if(cmd.getBusinessNote() != null){
			expressParamSettingProvider.updateExpressBusinessNoteByOwner(owner,cmd.getBusinessNote());
		}
		if(cmd.getBusinessNoteFlag() != null){
			checkShowFlag(cmd.getBusinessNoteFlag());
			expressParamSettingProvider.updateExpressBusinessNoteFlagByOwner(owner,cmd.getBusinessNoteFlag());
		}
	}

	private void checkShowFlag(Byte showFlag) {
		ExpressShowType showFlagType = ExpressShowType.fromCode(showFlag);
		if(showFlagType == null){
			LOGGER.error("unKnown showFlag = "+showFlag);
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
	}

	@Override
	public ListExpressHotlinesResponse listExpressHotlines(ListExpressHotlinesCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4070040710L, cmd.getAppId(), null,cmd.getCurrentProjectId());//参数设置权限
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<ExpressHotline> hotlineList = expressHotlineProvider.listHotLinesByOwner(owner,pageSize+1,cmd.getPageAnchor());
		Long nextPageAnchor = null;
		if (hotlineList.size() > pageSize) {
			hotlineList.remove(hotlineList.size()-1);
			nextPageAnchor = hotlineList.get(hotlineList.size()-1).getId();
		}
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(owner.getNamespaceId(),owner.getOwnerType().getCode(),owner.getOwnerId());
		return new ListExpressHotlinesResponse(setting.getHotlineFlag(), nextPageAnchor, hotlineList.stream().map(r -> ConvertHelper.convert(r, ExpressHotlineDTO.class)).collect(Collectors.toList()));
	}

	@Override
	public void updateExpressHotlineFlag(UpdateExpressHotlineFlagCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkShowFlag(cmd.getHotlineFlag());
		expressParamSettingProvider.updateExpressHotlineFlagByOwner(owner, cmd.getHotlineFlag());
	}

	@Override
	public CreateOrUpdateExpressHotlineResponse createOrUpdateExpressHotline(CreateOrUpdateExpressHotlineCommand cmd) {
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configProvider.getBooleanValue("privilege.community.checkflag", true)){
//			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4070040710L, cmd.getAppId(), null,cmd.getCurrentProjectId());//参数设置权限
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressHotline hotline = generateHotline(owner,cmd);
		if(cmd.getId() != null){
			expressHotlineProvider.updateExpressHotline(hotline);
		}
		else{
			expressHotlineProvider.createExpressHotline(hotline);
		}
		return ConvertHelper.convert(hotline, CreateOrUpdateExpressHotlineResponse.class);
	}

	private ExpressHotline generateHotline(ExpressOwner owner, CreateOrUpdateExpressHotlineCommand cmd) {
		if(cmd.getId() != null){
			ExpressHotline hotline = expressHotlineProvider.findExpressHotlineById(cmd.getId());
			if(hotline == null){
				LOGGER.error("unKnown hotline id = "+cmd.getId());
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			if(cmd.getOwnerId().longValue() != hotline.getOwnerId().longValue() || !cmd.getOwnerType().equals(hotline.getOwnerType())){
				LOGGER.error("mismatching hotline ownerType = "+cmd.getOwnerType()+","
						+ " or ownerId = "+cmd.getOwnerId());
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
			}
			hotline.setServiceName(cmd.getServiceName());
			hotline.setHotline(cmd.getHotline());
			return hotline;
		}
		ExpressHotline hotline = ConvertHelper.convert(cmd, ExpressHotline.class);
		hotline.setNamespaceId(owner.getNamespaceId());
		hotline.setStatus(CommonStatus.ACTIVE.getCode());
		return hotline;
	}

	@Override
	public void deleteExpressHotline(DeleteExpressHotlineCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		expressHotlineProvider.updateExpressHotlineStatus(owner,cmd.getId());
	}

	@Override
	public ListExpressSendTypesResponse listExpressSendTypes(ListExpressSendTypesCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int namespaceId = UserContext.getCurrentNamespaceId(); // TODO
		String ownerType = EntityType.NAMESPACE.getCode();
		Long ownerId = Long.valueOf(namespaceId);
		//这里需要找到顶层快递公司id
		Long expressCompanyId = cmd.getExpressCompanyId();
		if(expressCompanyId != null){
			expressCompanyId = findTopExpressCompany(expressCompanyId).getId();
		}
//		owner.setNamespaceId(23456);
		List<ExpressCompany> expressCompany = expressCompanyProvider.listNotDeleteExpressCompanyByOwner(owner);
		List<ExpressCompanyBusiness> list = expressCompanyBusinessProvider.listExpressSendTypesByOwner(namespaceId,ownerType,ownerId,expressCompanyId);
		return new ListExpressSendTypesResponse(list.stream().map(r->{
			ExpressSendTypeDTO dto = ConvertHelper.convert(r, ExpressSendTypeDTO.class);
			dto.setExpressCompanyId(cmd.getExpressCompanyId());
			if(cmd.getExpressCompanyId() == null){
				//如果没有传快递公司id，那么需要通过顶层r.getId()和园区所有快递公司的parentId做对比，查出快递公司在园区的id
				ExpressCompany topcompany = getExpressCompany(expressCompany, r);
				dto.setExpressCompanyId(topcompany.getId());
				dto.setExpressCompany(topcompany.getName());
			}
			return dto;
		}).collect(Collectors.toList()));
	}
	/**
	 * expressCompanies:当前园区下的快递公司
	 * business：配置顶层快递公司的业务对象
	 */
	public ExpressCompany getExpressCompany(List<ExpressCompany> expressCompanies,ExpressCompanyBusiness business){
		for (ExpressCompany expressCompany : expressCompanies) {
			ExpressCompany company = findTopExpressCompany(expressCompany.getId());
			if(company.getId().longValue() == business.getExpressCompanyId().longValue()){
				return expressCompany;
			}
		}
		return null;
	}

	@Override
	public GetExpressHotlineAndBusinessNoteFlagResponse getExpressHotlineAndBusinessNoteFlag(
			GetExpressHotlineAndBusinessNoteFlagCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(owner.getNamespaceId(), owner.getOwnerType().getCode(), owner.getOwnerId());
		return ConvertHelper.convert(setting, GetExpressHotlineAndBusinessNoteFlagResponse.class);
	}

	@Override
	public ListExpressSendModesResponse listExpressSendModes(ListExpressSendModesCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ListExpressSendModesResponse response = new ListExpressSendModesResponse(new ArrayList<ExpressSendModeDTO>());
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(UserContext.getCurrentNamespaceId(),
				EntityType.NAMESPACE.getCode(), UserContext.getCurrentNamespaceId());
		response.getExpressSendModeDTO().add(convertSendModeDTO(setting));
		return response;
	}
	
	private ExpressSendModeDTO convertSendModeDTO(ExpressParamSetting setting) {
		ExpressSendModeDTO dto = ConvertHelper.convert(setting, ExpressSendModeDTO.class);
		ExpressSendMode mode = ExpressSendMode.fromCode(dto.getSendMode());
		if(mode != null){
			dto.setSendModeName(mode.getDesc());
		}
		return dto;
	}

	@Override
	public ListExpressPackageTypesResponse listExpressPackageTypes(ListExpressPackageTypesCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int namespaceId = UserContext.getCurrentNamespaceId(); // TODO
		String ownerType = EntityType.NAMESPACE.getCode();
		Long ownerId = Long.valueOf(namespaceId);
		ExpressCompanyBusiness business = expressCompanyBusinessProvider.getExpressCompanyBusinessByOwner(namespaceId,ownerType,ownerId,cmd.getSendType());
		if(business == null || business.getPackageTypes() == null || business.getPackageTypes().length() == 0){
			return new ListExpressPackageTypesResponse();
		}
		List<ExpressPackageTypeDTO> list = convertPackageTypesList(business.getPackageTypes());
		return new ListExpressPackageTypesResponse(list);
	}
	
	private List<ExpressPackageTypeDTO> convertPackageTypesList(String packageTypes){
		return new ArrayList<Object>(Arrays.asList(JSONArray.parseArray(packageTypes).toArray())).stream().map(r->{
			ExpressPackageTypeDTO dto = JSONObject.parseObject(r.toString(), new TypeReference<ExpressPackageTypeDTO>(){});
			ExpressPackageType packageType = ExpressPackageType.fromCode(dto.getPackageType());
			dto.setPackageTypeName(packageType == null? "":packageType.getDescription());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public GetExpressInsuredDocumentsResponse getExpressInsuredDocuments(GetExpressInsuredDocumentsCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		int namespaceId = UserContext.getCurrentNamespaceId(); // TODO
		String ownerType = EntityType.NAMESPACE.getCode();
		Long ownerId = Long.valueOf(namespaceId);
		ExpressCompanyBusiness business = expressCompanyBusinessProvider.getExpressCompanyBusinessByOwner(namespaceId,ownerType,ownerId,cmd.getSendType());
		return new GetExpressInsuredDocumentsResponse(business == null?null:business.getInsuredDocuments());
	}

	@Override
	public ListExpressOrderStatusResponse listExpressOrderStatus() {
		int namespaceId = UserContext.getCurrentNamespaceId(); // TODO
		String ownerType = EntityType.NAMESPACE.getCode();
		Long ownerId = Long.valueOf(namespaceId);
		ExpressCompanyBusiness business = expressCompanyBusinessProvider.getExpressCompanyBusinessByOwner(namespaceId,ownerType,ownerId);
		
		if(business == null || business.getOrderStatusCollections() == null || business.getOrderStatusCollections().length() == 0){
			return new ListExpressOrderStatusResponse();
		}
		List<ExpressOrderStatusDTO> list = new ArrayList<Object>(Arrays.asList(JSONArray.parseArray(business.getOrderStatusCollections()).toArray())).stream().map(r->{
			ExpressOrderStatusDTO dto = JSONObject.parseObject(r.toString(), new TypeReference<ExpressOrderStatusDTO>(){});
			ExpressOrderStatus status = ExpressOrderStatus.fromCode(dto.getStatus());
			dto.setStatusName(status == null? "":status.getDescription());
			return dto;
		}).collect(Collectors.toList());
		return new ListExpressOrderStatusResponse(list);
	}

	@Override
	public Map<String,String> prePayExpressOrder(PrePayExpressOrderCommand cmd) {
		ExpressClientPayType clientPayType = ExpressClientPayType.fromCode(cmd.getClientPayType());
		Map<String,Map<String,String>> params = generatePrePayExpressOrderParams(cmd, clientPayType);
		String url = configProvider.getValue(ExpressServiceErrorCode.PAYSERVER_URL, "http://pay.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/save_payInfo_record");
		//公众号支付
		if(clientPayType == ExpressClientPayType.OFFICIAL_ACCOUNTS){
			url = configProvider.getValue(ExpressServiceErrorCode.OFFICIAL_ACCOUNTS_PAYSERVER_URL, "http://pay.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/createWechatJsPayOrder");
		}
		LOGGER.info("payserver url = {}", url);
		String result = Utils.post(url, JSONObject.parseObject(StringHelper.toJsonString(params)),null,StandardCharsets.UTF_8);
		if(result == null){
			LOGGER.error("prePayFailed, payresponse = "+result);
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_FROM_THIRD, "error from third");
		}
		PayResponse<Map<String,String>> payresponse = JSONObject.parseObject(result, new TypeReference<PayResponse<Map<String,String>>>(){});
		if(clientPayType == ExpressClientPayType.APP || clientPayType == null){
			if(payresponse.getSuccess()){
				return payresponse.getData();
			}
		}else if(clientPayType == ExpressClientPayType.OFFICIAL_ACCOUNTS){
			if(payresponse.getResult()){
				return payresponse.getBody();
			}
		}

		LOGGER.error("prePayFailed, payresponse = "+result);
		throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_FROM_THIRD, "error from third");
		
	}

	private Map<String,Map<String,String>> generatePrePayExpressOrderParams(PrePayExpressOrderCommand cmd,ExpressClientPayType clientPayType) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressOrder order = expressOrderProvider.findExpressOrderById(cmd.getId());
		if(order == null){
			LOGGER.error("unknown order id = "+cmd.getId());
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		CommonOrderDTO dto = payExpressOrder(ConvertHelper.convert(cmd, PayExpressOrderCommand.class));
		Map<String,Map<String,String>> bodyparams = new HashMap<String,Map<String,String>>();
		Map<String,String> params = new HashMap<String,String>();
		params.put("realm","Web_Guomao");
		params.put("orderType",dto.getOrderType());
		params.put("onlinePayStyleNo","wechat");
		params.put("orderNo",dto.getOrderNo());
		params.put("orderAmount",dto.getTotalFee().floatValue()+"");
		params.put("subject",dto.getSubject());
		params.put("body",dto.getBody());
		params.put("appKey",dto.getAppKey());
		params.put("timestamp",dto.getTimestamp()+"");
		params.put("randomNum",dto.getRandomNum()+"");
		if(clientPayType == ExpressClientPayType.OFFICIAL_ACCOUNTS){
			//这里获取用户的微信的openid，在国贸认证的过程中，存在user_profile中的，参考 ExpressThirdCallController.authReq()
			UserProfile userProfile = userActivityProvider.findUserProfileBySpecialKey(UserContext.current().getUser().getId(), ExpressServiceErrorCode.USER_PROFILE_KEY);
			if(userProfile == null){
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.LOGIN_SIGNED_FAILED, "not find user openId");
			}
			params.put("userId", userProfile.getItemValue());
			params.put("realm", "Wechat_Public_Guomao");
			String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
			params.put("appKey",appKey);
			App app = appProvider.findAppByKey(appKey);
			String signature = SignatureHelper.computeSignature(params, app.getSecretKey());
			params.put("signature",URLEncoder.encode(signature));
		}else{
			params.put("signature",dto.getSignature());
		}
		bodyparams.put("body", params);
		LOGGER.info("request payserver params = {}",bodyparams);
		return bodyparams;
	}

	@Override
	public PreOrderDTO payExpressOrderV2(PayExpressOrderCommandV2 cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
		}
		ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(cmd.getId());
		if(expressOrder.getPayDto()!=null && expressOrder.getPayDto().length()>0){
			PreOrderDTO preOrder = (PreOrderDTO)StringHelper.fromJsonString(expressOrder.getPayDto(), PreOrderDTO.class);
			return preOrder;
		}
		ExpressSendType sendType = ExpressSendType.fromCode(expressOrder.getSendType());
		if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
				|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue() || expressOrder.getCreatorUid().longValue() != owner.getUserId().longValue()
				|| sendType == ExpressSendType.GUO_MAO_EXPRESS) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ORDER_ABNORMAL, "order abnormal");
		}
		if (ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.WAITING_FOR_PAY) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying");
		}
		if (expressOrder.getPaySummary() == null) {
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying");
		}
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			if (TrueOrFalseFlag.fromCode(expressOrder.getPaidFlag()) != TrueOrFalseFlag.TRUE) {
				expressOrder.setPaidFlag(TrueOrFalseFlag.TRUE.getCode());
				expressOrderProvider.updateExpressOrder(expressOrder);
			}
			return null;
		});

		createExpressOrderLog(owner, ExpressActionEnum.PAYING, expressOrder, null);
		
		
		// 3、收款方是否有会员，无则报错
		Long bizPayeeId = getOrderPayeeAccount(cmd);
		List<PayUserDTO> payUserDTOs = sdkPayService
				.listPayUsersByIds(Stream.of(bizPayeeId).collect(Collectors.toList()));
		if (payUserDTOs == null || payUserDTOs.size() == 0) {
			LOGGER.error("payeeUserId no find, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 1001, "暂未绑定收款账户");
		}

		// 4、组装报文，发起下单请求
		PurchaseOrderCommandResponse orderCommandResponse = createOrder(cmd, expressOrder, bizPayeeId);

		// 5、组装支付方式
		PreOrderDTO preOrderDTO = orderCommandResponseToDto(orderCommandResponse, cmd);

		// 6、保存订单信息
		expressOrder.setPayDto(StringHelper.toJsonString(preOrderDTO));
		expressOrder.setGeneralOrderId(orderCommandResponse.getPayResponse().getBizOrderNum());
		expressOrderProvider.updateExpressOrder(expressOrder);
	
		return preOrderDTO;
	}
	
	private void oldMethod() {
//
//		Long amount = expressOrder.getPaySummary().multiply(new BigDecimal(100)).longValue();
//		Integer namespaceId = UserContext.getCurrentNamespaceId();
//
//		User user = UserContext.current().getUser();
//		String sNamespaceId = BIZ_ACCOUNT_PRE + UserContext.getCurrentNamespaceId();
//		TargetDTO userTarget = userProvider.findUserTargetById(user.getId());
//		ListBizPayeeAccountDTO payerDto = accountProvider.createPersonalPayUserIfAbsent(user.getId() + "",
//				sNamespaceId, (userTarget == null || userTarget.getUserIdentifier() == null) ? "12000001802" : userTarget.getUserIdentifier(), null, null, null);
//		List<ExpressPayeeAccount> payeeAccounts = accountProvider.findRepeatBusinessPayeeAccounts(null, namespaceId,
//				expressOrder.getOwnerType(), expressOrder.getOwnerId());
//		if (payeeAccounts == null || payeeAccounts.size() == 0) {
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"未设置收款方账号");
//		}
//		CreateOrderCommand createOrderCommand = new CreateOrderCommand();
//		createOrderCommand.setAccountCode(sNamespaceId);
//		createOrderCommand.setBizOrderNum(generateBizOrderNum(sNamespaceId, OrderType.OrderTypeEnum.EXPRESS_ORDER.getPycode(), expressOrder.getId()));
//		createOrderCommand.setClientAppName(cmd.getClientAppName());//todoed
//		createOrderCommand.setPayerUserId(payerDto.getAccountId());
//		createOrderCommand.setPayeeUserId(payeeAccounts.get(0).getPayeeId());
//		createOrderCommand.setAmount(amount);
//		createOrderCommand.setExtendInfo(OrderType.OrderTypeEnum.EXPRESS_ORDER.getMsg());
//		createOrderCommand.setGoodsName(OrderType.OrderTypeEnum.EXPRESS_ORDER.getMsg());
//		createOrderCommand.setSourceType(1);//下单源，参考com.everhomes.pay.order.SourceType，0-表示手机下单，1表示电脑PC下单
//		String homeurl = configProvider.getValue("home.url", "");
//		String callbackurl = String.format(configProvider.getValue("express.pay.callBackUrl", "%s/evh/express/notifyExpressOrderPaymentV2"), homeurl);
//		createOrderCommand.setBackUrl(callbackurl);
//		createOrderCommand.setOrderRemark1(configProvider.getValue("express.pay.OrderRemark1", "快递订单"));
//
//		LOGGER.info("createPurchaseOrder params" + createOrderCommand);
//		CreateOrderRestResponse purchaseOrder = sdkPayService.createPurchaseOrder(createOrderCommand);
//		if (purchaseOrder == null || 200 != purchaseOrder.getErrorCode() || purchaseOrder.getResponse() == null) {
//			LOGGER.info("purchaseOrder " + purchaseOrder);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//					"preorder failed " + StringHelper.toJsonString(purchaseOrder));
//		}
//		OrderCommandResponse response = purchaseOrder.getResponse();
//		PreOrderDTO preDto = ConvertHelper.convert(response, PreOrderDTO.class);
//		preDto.setExpiredIntervalTime(response.getExpirationMillis());
//		List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
//		String format = "{\"getOrderInfoUrl\":\"%s\"}";
//		if (paymentMethods != null) {
//			preDto.setPayMethod(paymentMethods.stream().map(bizPayMethod -> {
//				PayMethodDTO payMethodDTO = ConvertHelper.convert(bizPayMethod, PayMethodDTO.class);
//				payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
//				payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
//				String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
//				payMethodDTO.setPaymentLogo(paymentLogo);
//				payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
//				PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
//				com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
//				if (bizPaymentParamsDTO != null) {
//					paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
//				}
//				payMethodDTO.setPaymentParams(paymentParamsDTO);
//
//				return payMethodDTO;
//			}).collect(Collectors.toList()));
//		}
//		expressOrder.setPayDto(StringHelper.toJsonString(preDto));
//		expressOrderProvider.updateExpressOrder(expressOrder);
	}
	
	private PreOrderDTO orderCommandResponseToDto(PurchaseOrderCommandResponse orderCommandResponse,
			PayExpressOrderCommandV2 cmd) {
		OrderCommandResponse response = orderCommandResponse.getPayResponse();
		PreOrderDTO preDto = ConvertHelper.convert(response, PreOrderDTO.class);
		preDto.setExpiredIntervalTime(response.getExpirationMillis());
		List<com.everhomes.pay.order.PayMethodDTO> paymentMethods = response.getPaymentMethods();
		String format = "{\"getOrderInfoUrl\":\"%s\"}";
		if (paymentMethods != null) {
			preDto.setPayMethod(paymentMethods.stream().map(bizPayMethod -> {
				PayMethodDTO payMethodDTO = ConvertHelper.convert(bizPayMethod, PayMethodDTO.class);
				payMethodDTO.setPaymentName(bizPayMethod.getPaymentName());
				payMethodDTO.setExtendInfo(String.format(format, response.getOrderPaymentStatusQueryUrl()));
				String paymentLogo = contentServerService.parserUri(bizPayMethod.getPaymentLogo());
				payMethodDTO.setPaymentLogo(paymentLogo);
				payMethodDTO.setPaymentType(bizPayMethod.getPaymentType());
				PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
				com.everhomes.pay.order.PaymentParamsDTO bizPaymentParamsDTO = bizPayMethod.getPaymentParams();
				if (bizPaymentParamsDTO != null) {
					paymentParamsDTO.setPayType(bizPaymentParamsDTO.getPayType());
				}
				payMethodDTO.setPaymentParams(paymentParamsDTO);

				return payMethodDTO;
			}).collect(Collectors.toList()));
		}

		return preDto;
	}

	private PurchaseOrderCommandResponse createOrder(PayExpressOrderCommandV2 cmd, ExpressOrder expressOrder, Long bizPayeeId) {
		
		 CreatePurchaseOrderCommand createOrderCommand = preparePaymentBillOrder(cmd, expressOrder, bizPayeeId);
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

	private CreatePurchaseOrderCommand preparePaymentBillOrder(PayExpressOrderCommandV2 cmd, ExpressOrder expressOrder,  Long bizPayeeId) {
		
        CreatePurchaseOrderCommand preOrderCommand = new CreatePurchaseOrderCommand();

        preOrderCommand.setAmount(changePayAmount(expressOrder.getPaySummary()));

        String accountCode = BIZ_ACCOUNT_PRE+UserContext.getCurrentNamespaceId();
        preOrderCommand.setAccountCode(accountCode);
        preOrderCommand.setClientAppName(cmd.getClientAppName());
        preOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.EXPRESS_ORDER.getV2code());
        // 移到统一订单系统完成
        // String BizOrderNum  = getOrderNum(orderId, OrderType.OrderTypeEnum.WUYE_CODE.getPycode());
        BusinessPayerType payerType = BusinessPayerType.USER;
//        preOrderCommand.setBusinessOrderNumber(generateBizOrderNum(accountCode,OrderType.OrderTypeEnum.PRINT_ORDER.getPycode(),order.getOrderNo()));
        preOrderCommand.setBusinessPayerType(payerType.getCode());
        preOrderCommand.setBusinessPayerId(String.valueOf(UserContext.currentUserId()));
        String businessPayerParams = getBusinessPayerParams(cmd);
        preOrderCommand.setBusinessPayerParams(businessPayerParams);

        preOrderCommand.setPaymentPayeeId(bizPayeeId); //不知道填什么
        buildSpecificPayMethod(cmd, preOrderCommand); // 填充微信，支付宝支付时，请求参数

        //preOrderCommand.setExpirationMillis(EXPIRE_TIME_15_MIN_IN_SEC);
        String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"home.url", "");
//        String homeUrl = "http://10.1.110.51:9092";
        String backUri = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.callback.url.express", "/express/notifyExpressOrderPaymentV2");
        String backUrl = homeUrl + contextPath + backUri;
        preOrderCommand.setCallbackUrl(backUrl);
        preOrderCommand.setExtendInfo(OrderType.OrderTypeEnum.EXPRESS_ORDER.getMsg());
        preOrderCommand.setGoodsName("快递订单");
        preOrderCommand.setGoodsDescription(OrderType.OrderTypeEnum.EXPRESS_ORDER.getMsg());
        preOrderCommand.setIndustryName(null);
        preOrderCommand.setIndustryCode(null);
        preOrderCommand.setSourceType(SourceType.PC.getCode());
        preOrderCommand.setOrderRemark1(configProvider.getValue("express.pay.OrderRemark1", "快递订单"));
        //preOrderCommand.setOrderRemark2(String.valueOf(cmd.getOrderId()));
        preOrderCommand.setOrderRemark3(String.valueOf(cmd.getOwnerId()));
        preOrderCommand.setOrderRemark4(null);
        preOrderCommand.setOrderRemark5(null);
        String systemId = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_SYSTEM_ID, "");
        preOrderCommand.setBusinessSystemId(Long.parseLong(systemId));
        LOGGER.info("preOrderCommand:"+StringHelper.toJsonString(preOrderCommand));
        return preOrderCommand;
    }
	
	private void buildSpecificPayMethod(PayExpressOrderCommandV2 inputCmd, CreatePurchaseOrderCommand createPurchaseOrderCommand) {

		Integer paymentType = inputCmd.getPaymentType();
		if (null == paymentType) {
			return;
		}

		String namespaceUserToken = UserContext.current().getUser().getNamespaceUserToken();
		Map<String, String> flattenMap = new HashMap<>();
		flattenMap.put("acct", namespaceUserToken);
		flattenMap.put("payType", "no_credit");

		createPurchaseOrderCommand.setCommitFlag(1);

		// 公众号支付
		if (paymentType == PaymentType.WECHAT_JS_PAY.getCode() || paymentType == PaymentType.WECHAT_JS_ORG_PAY.getCode()) {
			createPurchaseOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			createPurchaseOrderCommand.setPaymentParams(flattenMap);
		} else if (paymentType != null && paymentType == PaymentType.ALI_JS_PAY.getCode()) {
			createPurchaseOrderCommand.setPaymentType(PaymentType.ALI_JS_PAY.getCode());
			createPurchaseOrderCommand.setPaymentParams(flattenMap);
		} else if (paymentType != null && paymentType == PaymentType.WECHAT_SCAN_PAY.getCode()) {
			createPurchaseOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			createPurchaseOrderCommand.setPaymentParams(flattenMap);
		}

	}
	
    private String getBusinessPayerParams(PayExpressOrderCommandV2 cmd) {

        Long businessPayerId = UserContext.currentUserId();


        UserIdentifier buyerIdentifier = userProvider.findUserIdentifiersOfUser(businessPayerId, UserContext.getCurrentNamespaceId());
        String buyerPhone = null;
        if(buyerIdentifier != null) {
            buyerPhone = buyerIdentifier.getIdentifierToken();
        }
        // 找不到手机号则默认一个
        if(buyerPhone == null || buyerPhone.trim().length() == 0) {
            buyerPhone = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), PaymentConstants.KEY_ORDER_DEFAULT_PERSONAL_BIND_PHONE, "");
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

	private Long getOrderPayeeAccount(PayExpressOrderCommandV2 cmd) {
		ExpressPayeeAccount account = accountProvider.getPayeeAccountByOwner(UserContext.getCurrentNamespaceId(),
				cmd.getOwnerType(), cmd.getOwnerId());
		if (account == null) {
			return null;
		}

		return account.getPayeeId();
	}

	private String generateBizOrderNum(String sNamespaceId, String pycode, Long id) {
		return sNamespaceId+BIZ_ORDER_NUM_SPILT+pycode+BIZ_ORDER_NUM_SPILT+id;
	}

	@Override
	public void notifyExpressOrderPaymentV2(OrderPaymentNotificationCommand cmd) {
		//检查签名
		if(!PayUtil.verifyCallbackSignature(cmd)){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.PAY_CHECK_SIGNED_FAILED, "sign verify faild");
		}

		// * RAW(0)：
		// * SUCCESS(1)：支付成功
		// * PENDING(2)：挂起
		// * ERROR(3)：错误
		if(cmd.getPaymentStatus()== null || 1!=cmd.getPaymentStatus()){
			LOGGER.error("invaild paymentstatus,"+cmd.getPaymentStatus());
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.INPUT_PARAM_INVALID, "invalid parameters");
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
			ExpressOrder order = expressOrderProvider.findExpressOrderByBizOrderNum(cmd.getBizOrderNum());
			if(order == null){
				LOGGER.error("the order {} not found.",cmd.getBizOrderNum());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"the order not found.");
			}

			SrvOrderPaymentNotificationCommand notificationCommand  =  new SrvOrderPaymentNotificationCommand();
			notificationCommand.setOrderId(order.getId());
			notificationCommand.setAmount(cmd.getAmount());
			parkingV2EmbeddedV2Handler.paySuccess(notificationCommand);

		}

	}

	private String transferOrderNo(String bizOrderNum) {
		String[] split = bizOrderNum.split(BIZ_ORDER_NUM_SPILT);
		if(split.length==3){
			return split[2];
		}
		return bizOrderNum;
	}

	@Override
	public  List<ListBizPayeeAccountDTO> listPayeeAccount(ListPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getCommunityId());
		ArrayList arrayList = new ArrayList(Arrays.asList("0", cmd.getCommunityId() + ""));
		String key = OwnerType.ORGANIZATION.getCode() + cmd.getOrganizationId();
		LOGGER.info("sdkPayService request params:{} {} ",key,arrayList);
		List<PayUserDTO> payUserList = sdkPayService.getPayUserList(key,arrayList);
		if(payUserList==null || payUserList.size() == 0){
			return null;
		}
		return payUserList.stream().map(r->{
			ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
			dto.setAccountId(r.getId());
			dto.setAccountType(r.getUserType()==2?OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
			dto.setAccountName(r.getUserName());
			dto.setAccountAliasName(r.getUserAliasName());
			dto.setAccountStatus(Byte.valueOf(r.getRegisterStatus()+""));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public void createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		List<ExpressPayeeAccount> accounts = accountProvider.findRepeatBusinessPayeeAccounts
				(cmd.getId(),cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if(accounts!=null && accounts.size()>0){
			throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_CREATE_REPEAT_PAYEE_ACCOUNT,
					"repeat account");
		}
		if(cmd.getId()!=null){
			ExpressPayeeAccount oldPayeeAccount = accountProvider.findExpressPayeeAccountById(cmd.getId());
			if(oldPayeeAccount == null){
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.ERROR_PAYEE_ACCOUNT_ID_NOT_EXIST,
						"unknown payaccountid = "+cmd.getId());
			}
			ExpressPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,ExpressPayeeAccount.class);
			newPayeeAccount.setCreateTime(oldPayeeAccount.getCreateTime());
			newPayeeAccount.setCreatorUid(oldPayeeAccount.getCreatorUid());
			newPayeeAccount.setNamespaceId(oldPayeeAccount.getNamespaceId());
			newPayeeAccount.setOwnerType(oldPayeeAccount.getOwnerType());
			newPayeeAccount.setOwnerId(oldPayeeAccount.getOwnerId());
			accountProvider.updateExpressPayeeAccount(newPayeeAccount);
		}else{
			ExpressPayeeAccount newPayeeAccount = ConvertHelper.convert(cmd,ExpressPayeeAccount.class);
			newPayeeAccount.setStatus((byte)2);
			accountProvider.createExpressPayeeAccount(newPayeeAccount);
		}
	}

	@Override
	public BusinessPayeeAccountDTO getBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());
		ExpressPayeeAccount account = accountProvider
				.getPayeeAccountByOwner(cmd.getNamespaceId(),cmd.getOwnerType(),cmd.getOwnerId());
		if(account==null){
			return null;
		}
		List<PayUserDTO> payUserDTOS = sdkPayService.listPayUsersByIds(new ArrayList<>(Arrays.asList(account.getPayeeId())));
		Map<Long,PayUserDTO> map = payUserDTOS.stream().collect(Collectors.toMap(PayUserDTO::getId,r->r));
		BusinessPayeeAccountDTO convert = ConvertHelper.convert(account, BusinessPayeeAccountDTO.class);
		PayUserDTO payUserDTO = map.get(convert.getPayeeId());
		if(payUserDTO!=null){
			convert.setPayeeUserType(payUserDTO.getUserType());
			convert.setPayeeUserName(payUserDTO.getUserName());
			convert.setPayeeUserAliasName(payUserDTO.getUserAliasName());
			convert.setPayeeAccountCode(payUserDTO.getAccountCode());
			convert.setPayeeRegisterStatus(payUserDTO.getRegisterStatus());
			convert.setPayeeRemark(payUserDTO.getRemark());
		}
		return convert;

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
}