// @formatter:off
package com.everhomes.express;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.express.AddExpressUserCommand;
import com.everhomes.rest.express.CancelExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderCommand;
import com.everhomes.rest.express.CreateExpressOrderResponse;
import com.everhomes.rest.express.CreateExpressUserDTO;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressCommand;
import com.everhomes.rest.express.CreateOrUpdateExpressAddressResponse;
import com.everhomes.rest.express.DeleteExpressAddressCommand;
import com.everhomes.rest.express.DeleteExpressUserCommand;
import com.everhomes.rest.express.ExpressCompanyDTO;
import com.everhomes.rest.express.ExpressOrderDTO;
import com.everhomes.rest.express.ExpressOwner;
import com.everhomes.rest.express.ExpressOwnerType;
import com.everhomes.rest.express.ExpressServiceAddressDTO;
import com.everhomes.rest.express.ExpressUserDTO;
import com.everhomes.rest.express.GetExpressLogisticsDetailCommand;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.rest.express.GetExpressOrderDetailCommand;
import com.everhomes.rest.express.GetExpressOrderDetailResponse;
import com.everhomes.rest.express.ListExpressAddressCommand;
import com.everhomes.rest.express.ListExpressAddressResponse;
import com.everhomes.rest.express.ListExpressCompanyCommand;
import com.everhomes.rest.express.ListExpressCompanyResponse;
import com.everhomes.rest.express.ListExpressOrderCommand;
import com.everhomes.rest.express.ListExpressOrderCondition;
import com.everhomes.rest.express.ListExpressOrderResponse;
import com.everhomes.rest.express.ListExpressQueryHistoryResponse;
import com.everhomes.rest.express.ListExpressUserCommand;
import com.everhomes.rest.express.ListExpressUserCondition;
import com.everhomes.rest.express.ListExpressUserResponse;
import com.everhomes.rest.express.ListPersonalExpressOrderCommand;
import com.everhomes.rest.express.ListPersonalExpressOrderResponse;
import com.everhomes.rest.express.ListServiceAddressCommand;
import com.everhomes.rest.express.ListServiceAddressResponse;
import com.everhomes.rest.express.PrintExpressOrderCommand;
import com.everhomes.rest.express.UpdatePaySummaryCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ExpressServiceImpl implements ExpressService {

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
		return new ExpressOwner(UserContext.getCurrentNamespaceId(), expressOwnerType, ownerId);
	}

	@Override
	public ListExpressCompanyResponse listExpressCompany(ListExpressCompanyCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
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
	public void addExpressUser(AddExpressUserCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		if (cmd.getExpressUsers() != null) {
			cmd.getExpressUsers().forEach(r->addExpressUser(owner, r));
		}
	}

	private void addExpressUser(ExpressOwner owner, CreateExpressUserDTO createExpressUserDTO) {
		ExpressUser expressUser = null;
		if ((expressUser = checkExistsExpressUser(owner, createExpressUserDTO)) != null) {
			expressUser.setStatus(CommonStatus.ACTIVE.getCode());
			expressUserProvider.updateExpressUser(expressUser);
		}else {
			expressUser = new ExpressUser();
			expressUser.setNamespaceId(owner.getNamespaceId());
			expressUser.setOwnerType(owner.getOwnerType().getCode());
			expressUser.setOwnerId(owner.getOwnerId());
			expressUser.setOrganizationId(createExpressUserDTO.getOrganizationId());
			expressUser.setOrganizationMemberId(createExpressUserDTO.getOrganizationMemberId());
			expressUser.setStatus(CommonStatus.ACTIVE.getCode());
			expressUserProvider.createExpressUser(expressUser);
		}
	}
	
	private ExpressUser checkExistsExpressUser(ExpressOwner owner, CreateExpressUserDTO createExpressUserDTO) {
		return expressUserProvider.findExpressUserByOrganizationMember(owner.getNamespaceId(), owner.getOwnerType().getCode(), owner.getOwnerId(), createExpressUserDTO.getOrganizationId(), createExpressUserDTO.getOrganizationMemberId());
	}

	@Override
	public void deleteExpressUser(DeleteExpressUserCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		ExpressUser expressUser = expressUserProvider.findExpressUserById(cmd.getId());
		if (expressUser != null) {
			expressUser.setStatus(CommonStatus.INACTIVE.getCode());
			expressUserProvider.updateExpressUser(expressUser);
		}
	}

	@Override
	public ListExpressOrderResponse listExpressOrder(ListExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
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

	private ExpressOrderDTO convertToExpressOrderDTOForWebList(ExpressOrder expressOrder) {
		ExpressOrderDTO expressOrderDTO = new ExpressOrderDTO();
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
		if (expressCompany != null) {
			return expressCompany.getName();
		}
		return null;
	}
	

	@Override
	public GetExpressOrderDetailResponse getExpressOrderDetail(GetExpressOrderDetailCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
		
		return new GetExpressOrderDetailResponse();
	}

	@Override
	public void updatePaySummary(UpdatePaySummaryCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	

	}

	@Override
	public void printExpressOrder(PrintExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	

	}

	@Override
	public CreateOrUpdateExpressAddressResponse createOrUpdateExpressAddress(CreateOrUpdateExpressAddressCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	
		return new CreateOrUpdateExpressAddressResponse();
	}

	@Override
	public void deleteExpressAddress(DeleteExpressAddressCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	

	}

	@Override
	public ListExpressAddressResponse listExpressAddress(ListExpressAddressCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	
		return new ListExpressAddressResponse();
	}

	@Override
	public CreateExpressOrderResponse createExpressOrder(CreateExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	
		return new CreateExpressOrderResponse();
	}

	@Override
	public ListPersonalExpressOrderResponse listPersonalExpressOrder(ListPersonalExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	
		return new ListPersonalExpressOrderResponse();
	}

	@Override
	public void cancelExpressOrder(CancelExpressOrderCommand cmd) {
		ExpressOwner owner = checkOwner(cmd.getOwnerType(), cmd.getOwnerId());
	

	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(GetExpressLogisticsDetailCommand cmd) {
	
		return new GetExpressLogisticsDetailResponse();
	}

	@Override
	public ListExpressQueryHistoryResponse listExpressQueryHistory() {
	
		return new ListExpressQueryHistoryResponse();
	}

	@Override
	public void clearExpressQueryHistory() {
	

	}

}