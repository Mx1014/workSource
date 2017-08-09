// @formatter:off
package com.everhomes.express;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.CancelExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderResponse;
import com.everhomes.rest.express.CreateExpressUserDTO;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressResponse;
import com.everhomes.rest.express.CreateOrUpdateExpressHotlineCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressHotlineResponse;
import com.everhomes.rest.express.DeleteExpressAddressCommand;
import com.everhomes.rest.express.DeleteExpressHotlineCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.ExpressActionEnum;
import com.everhomes.rest.express.ExpressAddressDTO;
import com.everhomes.rest.express.ExpressCompanyDTO;
import com.everhomes.rest.express.ExpressHotlineDTO;
import com.everhomes.rest.express.ExpressInvoiceFlagType;
import com.everhomes.rest.express.ExpressOrderDTO;
import com.everhomes.rest.express.ExpressOrderStatus;
import com.everhomes.rest.express.ExpressOrderStatusDTO;
import com.everhomes.rest.express.ExpressOwner;
import com.everhomes.rest.express.ExpressOwnerType;
import com.everhomes.rest.express.ExpressPackageType;
import com.everhomes.rest.express.ExpressPackageTypeDTO;
import com.everhomes.rest.express.ExpressQueryHistoryDTO;
import com.everhomes.rest.express.ExpressSendMode;
import com.everhomes.rest.express.ExpressSendModeDTO;
import com.everhomes.rest.express.ExpressSendType;
import com.everhomes.rest.express.ExpressSendTypeDTO;
import com.everhomes.rest.express.ExpressServiceAddressDTO;
import com.everhomes.rest.express.ExpressServiceErrorCode;
import com.everhomes.rest.express.ExpressShowType;
import com.everhomes.rest.express.ExpressUserDTO;
import com.everhomes.rest.express.GetExpressBusinessNoteCommand;
import com.everhomes.rest.express.GetExpressBusinessNoteResponse;
import com.everhomes.rest.express.GetExpressHotlineAndBusinessNoteFlagCommand;
import com.everhomes.rest.express.GetExpressHotlineAndBusinessNoteFlagResponse;
import com.everhomes.rest.express.GetExpressInsuredDocumentsCommand;
import com.everhomes.rest.express.GetExpressInsuredDocumentsResponse;
import com.everhomes.rest.express.GetExpressLogisticsDetailCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.GetExpressParamSettingResponse;
import com.everhomes.rest.express.ListExpressAddressCommand;
import com.everhomes.rest.express.ListExpressAddressResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressHotlinesCommand;
import com.everhomes.rest.express.ListExpressHotlinesResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderCondition;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressOrderStatusResponse;
import com.everhomes.rest.express.ListExpressPackageTypesCommand;
import com.everhomes.rest.express.ListExpressPackageTypesResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryResponse;
import com.everhomes.rest.express.ListExpressSendModesCommand;
import com.everhomes.rest.express.ListExpressSendModesResponse;
import com.everhomes.rest.express.ListExpressSendTypesCommand;
import com.everhomes.rest.express.ListExpressSendTypesResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserCondition;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderCommand;
import com.everhomes.rest.express.ListPersonalExpressOrderResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.PayExpressOrderCommand;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdateExpressBusinessNoteCommand;
import com.everhomes.rest.express.UpdateExpressHotlineFlagCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.organization.OrganizationCommunityDTO;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class ExpressServiceImpl implements ExpressService {
	
	private static final int ORDER_NO_LENGTH = 18;
	
	@Autowired
	private ExpressParamSettingProvider expressParamSettingProvider;
	
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "owner type or owner id cannnot be empty");
		}
		ExpressOwnerType expressOwnerType = ExpressOwnerType.fromCode(ownerType);
		if (expressOwnerType == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "owner type error");
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
		expressUserDTO.setName(organizationMember.getContactName());
		expressUserDTO.setPhone(organizationMember.getContactToken());
		return expressUserDTO; 
	} 

	@Override
	public RestResponse addExpressUser(AddExpressUserCommand cmd) {
		if (cmd.getExpressCompanyId() == null || cmd.getServiceAddressId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		checkAdmin(owner);
		ExpressUser expressUser = expressUserProvider.findExpressUserById(cmd.getId());
		if (expressUser == null || expressUser.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressUser.getOwnerType().equals(owner.getOwnerType().getCode())
				|| expressUser.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		expressUser.setStatus(CommonStatus.INACTIVE.getCode());
		expressUserProvider.updateExpressUser(expressUser);
	}

	@Override
	public ListExpressOrderResponse listExpressOrder(ListExpressOrderCommand cmd) {
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
		ExpressUser expressUser = expressUserProvider.findExpressUserByUserId(owner.getNamespaceId(), owner.getOwnerType(), owner.getOwnerId(), owner.getUserId(), serviceAddressId, expressCompanyId);
		if (expressUser == null || CommonStatus.fromCode(expressUser.getStatus()) != CommonStatus.ACTIVE) {
			//检查是否为管理员
			return checkAdmin(owner);
		}
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
		expressOrderDTO.setPayType(expressOrder.getPayType());
		expressOrderDTO.setStatus(expressOrder.getStatus());
		expressOrderDTO.setPaySummary(expressOrder.getPaySummary());
		return expressOrderDTO;
	}
	
	private String getExpressCompany(Long id) {
		ExpressCompany expressCompany = expressCompanyProvider.findExpressCompanyById(id);
		return expressCompany == null ? null : expressCompany.getName();
	}

	@Override
	public GetExpressOrderDetailResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		return (CommonOrderDTO)coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue() || expressOrder.getCreatorUid().longValue() != owner.getUserId().longValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			//TODO 这里是费代码 by dengs.-------------------正式环境需要注释掉-------------
			ExpressCompany expressCompany = findTopExpressCompany(expressOrder.getExpressCompanyId());
			ExpressHandler handler = getExpressHandler(expressCompany.getId());
			expressOrder.setStatus(ExpressOrderStatus.PAID.getCode());
			handler.updateOrderStatus(expressOrder, expressCompany);
			expressOrderProvider.updateExpressOrder(expressOrder);
			
			createExpressOrderLog(owner, ExpressActionEnum.PAYING, expressOrder, null);
			
			//调用统一处理订单接口，返回统一订单格式
			CommonOrderCommand orderCmd = new CommonOrderCommand();
			orderCmd.setBody(expressOrder.getSendName());
			orderCmd.setOrderNo(expressOrder.getId().toString());
			orderCmd.setOrderType(OrderType.OrderTypeEnum.EXPRESS_ORDER.getPycode());
			orderCmd.setSubject("快递订单简要描述");
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
			ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(orderId);
			if (expressOrder == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "not exists order, orderId="+orderId);
			}
			if (!expressOrder.getPaySummary().equals(new BigDecimal(cmd.getPayAmount()))) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "order money error, paySummary="+expressOrder.getPaySummary()+", payAmout="+cmd.getPayAmount());
			}
			expressOrder.setStatus(ExpressOrderStatus.PAID.getCode());
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "not exists order");
		}
		ExpressOwner owner = new ExpressOwner(expressOrder.getNamespaceId(), ExpressOwnerType.fromCode(expressOrder.getOwnerType()), expressOrder.getOwnerId(), expressOrder.getCreatorUid());
		createExpressOrderLog(owner, ExpressActionEnum.PAYING, expressOrder, "pay fail: " + StringHelper.toJsonString(cmd));
	}
	
	@Override
	public void printExpressOrder(PrintExpressOrderCommand cmd) {
		if (cmd.getId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressAddress expressAddress = expressAddressProvider.findExpressAddressById(cmd.getId());
		if (expressAddress == null || !expressAddress.getOwnerType().equals(owner.getOwnerType().getCode()) || expressAddress.getOwnerId().longValue() != owner.getOwnerId().longValue()
				|| expressAddress.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || expressAddress.getCreatorUid().longValue() != owner.getUserId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameter");
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
			return null;
		});
		createExpressOrderLog(owner, ExpressActionEnum.CREATE, expressOrder, null);
		return new CreateExpressOrderResponse(convertToExpressOrderDTOForDetail(expressOrder));
	}

	private void checkCreateExpressOrderCommand(CreateExpressOrderCommand cmd) {
		ExpressSendType sendType = ExpressSendType.fromCode(cmd.getSendType());
		if(sendType == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid sendType = " + cmd.getSendType());
		}
		//接受地址和寄送地址不校验，因为存在地址为临时使用的情况。
		//通用参数校验
		if (cmd.getExpressCompanyId() == null 
				|| cmd.getSendMode() == null || cmd.getPayType() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters"
					+ " receiveAddressId = null or expressCompanyId = null or sendMode = null or payType = null");
		}
		//华润ems快递，参数校验 
		if (sendType == ExpressSendType.STANDARD){
			if(cmd.getServiceAddressId() == null )
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters serviceAddressId = null");
		}
		//邮政快递包裹 信筒
		if(sendType == ExpressSendType.CHINA_POST_PACKAGE || sendType == ExpressSendType.CITY_EMPTIES){
			ExpressPackageType packageType = ExpressPackageType.fromCode(cmd.getPackageType());
			if(packageType == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknown package type = " + cmd.getPackageType());
			}
			ExpressInvoiceFlagType invoiceFlagType = ExpressInvoiceFlagType.fromCode(cmd.getInvoiceFlag());
			if(invoiceFlagType == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknown invoiceFlag Type = " + cmd.getInvoiceFlag());
			}
			if(invoiceFlagType == ExpressInvoiceFlagType.NEED_TAX_INVOIE && (cmd.getInvoiceHead() == null || cmd.getInvoiceHead().length() == 0)){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invaild parameters invoicehead = null");
			}
		}
		//国贸ems校验 TODO
	}

	//快递公司对应的业务检查，业务对应的包装类型检查。
	private void checkExpressParams(CreateExpressOrderCommand cmd, ExpressOwner owner) {
		ExpressCompany company = findTopExpressCompany(cmd.getExpressCompanyId());
		if(company == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknown expresscompany id = "+cmd.getExpressCompanyId());
		}
		List<ExpressCompanyBusiness> list = expressCompanyBusinessProvider.listExpressSendTypesByOwner(owner.getNamespaceId(), EntityType.NAMESPACE.getCode(),
				Long.valueOf(owner.getNamespaceId()), company.getId());
		if(list == null || list.size() == 0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "expressCompany = " + company.getName() + " mismatch sendType = "+cmd.getSendType());
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "expressCompany = " + company.getName() + " mismatch sendType = "+cmd.getSendType());
		}
		List<ExpressPackageTypeDTO> dtos = convertPackageTypesList(business.getPackageTypes());
		if(dtos == null || dtos.size() == 0){
			return ;
		}
		if(!dtos.stream().map(r-> r.getPackageType().longValue()).collect(Collectors.toList()).contains(cmd.getPackageType().longValue())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "sendType = " + cmd.getSendType() + " mismatch packageType = "+cmd.getPackageType());
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
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "not exists send address: id="+cmd.getSendAddressId());
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
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invaild send address params");
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
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "not exists receive address: id="+cmd.getReceiveAddressId());
			}
			expressOrder.setReceiveName(receiveAddress.getUserName());
			expressOrder.setReceivePhone(receiveAddress.getPhone());
			expressOrder.setReceiveOrganization(receiveAddress.getOrganizationName());
			expressOrder.setReceiveProvince(receiveAddress.getProvince());
			expressOrder.setReceiveCity(receiveAddress.getCity());
			expressOrder.setReceiveCounty(receiveAddress.getCounty());
			expressOrder.setReceiveDetailAddress(receiveAddress.getDetailAddress());
		}else{
			if(cmd.getReceiveName() == null || cmd.getReceivePhone() == null ||
					cmd.getReceiveProvince() == null || cmd.getReceiveCity() == null || 
					cmd.getReceiveCounty() == null || cmd.getReceiveDetailAddress() == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invaild receive address params");
			}
			expressOrder.setReceiveName(cmd.getReceiveName());
			expressOrder.setReceivePhone(cmd.getReceivePhone());
			expressOrder.setReceiveOrganization(cmd.getReceiveOrganization());
			expressOrder.setReceiveProvince(cmd.getReceiveProvince());
			expressOrder.setReceiveCity(cmd.getReceiveCity());
			expressOrder.setReceiveCounty(cmd.getReceiveCounty());
			expressOrder.setReceiveDetailAddress(cmd.getReceiveDetailAddress());
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_EXPRESS_ORDER.getCode() + cmd.getId()).enter(() -> {
			ExpressOrder expressOrder= expressOrderProvider.findExpressOrderById(cmd.getId());
			if (expressOrder == null || expressOrder.getNamespaceId().intValue() != owner.getNamespaceId().intValue() || !expressOrder.getOwnerType().equals(owner.getOwnerType().getCode())
					|| expressOrder.getOwnerId().longValue() != owner.getOwnerId().longValue() || expressOrder.getCreatorUid().longValue() != owner.getUserId().longValue()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
			}
			if (ExpressOrderStatus.fromCode(expressOrder.getStatus()) != ExpressOrderStatus.WAITING_FOR_PAY) {
				throw RuntimeErrorException.errorWith(ExpressServiceErrorCode.SCOPE, ExpressServiceErrorCode.STATUS_ERROR, "order status must be waiting for paying and express user has not confirmed money");
			}
			expressOrder.setStatus(ExpressOrderStatus.CANCELLED.getCode());
			ExpressCompany expressCompany = findTopExpressCompany(expressOrder.getExpressCompanyId());
			ExpressHandler handler = getExpressHandler(expressCompany.getId());
			dbProvider.execute(status->{
				expressOrderProvider.updateExpressOrder(expressOrder);
				handler.updateOrderStatus(expressOrder, expressCompany);
				return null;
			});
			createExpressOrderLog(owner, ExpressActionEnum.CANCEL, expressOrder, null);
			return null;
		});
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd) {
		if (cmd.getExpressCompanyId() == null || StringUtils.isEmpty(cmd.getBillNo())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
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
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressParamSetting setting = expressParamSettingProvider.getExpressParamSettingByOwner(owner.getNamespaceId(),owner.getOwnerType().getCode(),owner.getOwnerId());
		return ConvertHelper.convert(setting, GetExpressBusinessNoteResponse.class);
	}

	@Override
	public void updateExpressBusinessNote(UpdateExpressBusinessNoteCommand cmd) {
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unKnown showFlag = "+showFlag);
		}
	}

	@Override
	public ListExpressHotlinesResponse listExpressHotlines(ListExpressHotlinesCommand cmd) {
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
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unKnown hotline id = "+cmd.getId());
			}
			if(cmd.getOwnerId().longValue() != hotline.getOwnerId().longValue() || !cmd.getOwnerType().equals(hotline.getOwnerType())){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "mismatching hotline ownerType = "+cmd.getOwnerType()+","
						+ " or ownerId = "+cmd.getOwnerId());
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
		List<ExpressCompany> expressCompany = expressCompanyProvider.listExpressCompanyByOwner(owner);
		List<ExpressCompanyBusiness> list = expressCompanyBusinessProvider.listExpressSendTypesByOwner(namespaceId,ownerType,ownerId,expressCompanyId);
		return new ListExpressSendTypesResponse(list.stream().map(r->{
			ExpressSendTypeDTO dto = ConvertHelper.convert(r, ExpressSendTypeDTO.class);
			dto.setExpressCompanyId(cmd.getExpressCompanyId());
			if(cmd.getExpressCompanyId() == null){
				//如果没有传快递公司id，那么需要通过顶层r.getId()和园区所有快递公司的parentId做对比，查出快递公司在园区的id
				dto.setExpressCompanyId(getExpressCompanyId(expressCompany, r));
			}
			return dto;
		}).collect(Collectors.toList()));
	}
	/**
	 * expressCompanies:当前园区下的快递公司
	 * business：配置顶层快递公司的业务对象
	 */
	public Long getExpressCompanyId(List<ExpressCompany> expressCompanies,ExpressCompanyBusiness business){
		for (ExpressCompany expressCompany : expressCompanies) {
			ExpressCompany company = findTopExpressCompany(expressCompany.getId());
			if(company.getId().longValue() == business.getExpressCompanyId().longValue()){
				return expressCompany.getId();
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

}