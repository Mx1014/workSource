package com.everhomes.payment_application;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.requisition.Requisition;
import com.everhomes.requisition.RequisitionProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.ContractErrorCode;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.payment_application.*;
import com.everhomes.search.PaymentApplicationSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.supplier.SupplierProvider;
import com.everhomes.supplier.WarehouseSupplier;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@Component
public class PaymentApplicationServiceImpl implements PaymentApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApplicationServiceImpl.class);

    @Autowired
    private PaymentApplicationProvider paymentApplicationProvider;

    @Autowired
    private PaymentApplicationSearcher paymentApplicationSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private SupplierProvider supplierProvider;

    @Autowired
    private RequisitionProvider requisitionProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private FieldProvider fieldProvider;

    @Override
    public String generatePaymentApplicationNumber() {
        String num = DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH) + (int)((Math.random()*9+1)*1000);
        return num;
    }

    @Override
    public PaymentApplicationDTO createPaymentApplication(CreatePaymentApplicationCommand cmd) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOwnerId(), PrivilegeConstants.PAYMENT_APPLICATION_CREATE, ServiceModuleConstants.PAYMENT_APPLICATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, null,cmd.getCommunityId());
        PaymentApplication application = ConvertHelper.convert(cmd, PaymentApplication.class);
        paymentApplicationProvider.createPaymentApplication(application);
        addToFlowCase(application);
        paymentApplicationSearcher.feedDoc(application);
        return toPaymentApplicationDTO(application);
    }

    @Override
    public PaymentApplicationDTO getPaymentApplication(GetPaymentApplicationCommand cmd) {
        PaymentApplication application = paymentApplicationProvider.findPaymentApplication(cmd.getId());
        application.setOrgId(cmd.getOrgId());
        return toPaymentApplicationDTO(application);
    }

    @Override
    public SearchPaymentApplicationResponse searchPaymentApplications(SearchPaymentApplicationCommand cmd) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOwnerId(), PrivilegeConstants.PAYMENT_APPLICATION_LIST, ServiceModuleConstants.PAYMENT_APPLICATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, null,cmd.getCommunityId());
        return paymentApplicationSearcher.query(cmd);
    }

    @Override
    public ListPaymentApplicationByContractResponse listPaymentApplicationByContract(ListPaymentApplicationByContractCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListPaymentApplicationByContractResponse response = new ListPaymentApplicationByContractResponse();
        List<PaymentApplication> paymentApplicationList = paymentApplicationProvider.listPaymentApplicationsByContractId(locator, pageSize + 1, cmd.getContractId());
        if(paymentApplicationList != null && paymentApplicationList.size() > 0) {
            if(paymentApplicationList.size() > pageSize) {
                paymentApplicationList.remove(paymentApplicationList.size() - 1);
                response.setNextPageAnchor(paymentApplicationList.get(paymentApplicationList.size() - 1).getId());
			}

            List<PaymentApplicationDTO> dtos =  paymentApplicationList.stream().map(paymentApplication -> {
                return toPaymentApplicationDTO(paymentApplication);
            }).collect(Collectors.toList());

            response.setApplicationDTOs(dtos);
        }

        return response;
    }

    private PaymentApplicationDTO toPaymentApplicationDTO(PaymentApplication application) {
        PaymentApplicationDTO dto = ConvertHelper.convert(application, PaymentApplicationDTO.class);

        Contract contract = contractProvider.findContractById(application.getContractId());
        if (contract != null) {
            dto.setContractName(contract.getName());
            dto.setContractNumber(contract.getContractNumber());
            dto.setContractAmount(contract.getRent());
            if(contract.getCategoryItemId() != null) {
                ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(),application.getOrgId(), contract.getCommunityId(), Long.valueOf(contract.getCategoryItemId()));
                if(item != null) {
                    dto.setCategoryItemName(item.getItemDisplayName());
                }
            }

            if(CustomerType.SUPPLIER.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                WarehouseSupplier supplier = supplierProvider.findSupplierById(contract.getCustomerId());
                if(supplier != null) {
                    dto.setCustomerName(supplier.getName());
                }

            }
        }

        if(application.getRequestId() != null) {
            Requisition requisition = requisitionProvider.findRequisitionById(application.getRequestId());
            if(requisition != null) {
                dto.setRequestName(requisition.getTheme());
                dto.setRequestIdentity(requisition.getIdentity());
                dto.setRequestUserName(requisition.getApplicantName());
                dto.setRequestDepartment(requisition.getApplicantDepartment());
            }
        }

        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(application.getApplicantUid(), application.getApplicantOrgId());
        if(member != null) {
            dto.setApplicantName(member.getContactName());
        }

        Organization org = organizationProvider.findOrganizationById(application.getApplicantOrgId());
        if(org != null) {
            dto.setApplicantOrgName(org.getName());
        }
        return dto;
    }

    private void addToFlowCase(PaymentApplication application) {
        Flow flow = flowService.getEnabledFlow(application.getNamespaceId(), FlowConstants.PAYMENT_APPLICATION_MODULE,
                FlowModuleType.NO_MODULE.getCode(), application.getCommunityId(), FlowOwnerType.PAYMENT_APPLICATION.getCode());
        if(null == flow) {
            LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.PAYMENT_APPLICATION_MODULE);
            throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ENABLE_FLOW,
                    localeStringService.getLocalizedString(String.valueOf(ContractErrorCode.SCOPE),
                            String.valueOf(ContractErrorCode.ERROR_ENABLE_FLOW),
                            UserContext.current().getUser().getLocale(),"Enable request flow not found."));
        }
        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        createFlowCaseCommand.setCurrentOrganizationId(application.getOwnerId());
        createFlowCaseCommand.setTitle(application.getTitle() + "付款申请");
        createFlowCaseCommand.setApplyUserId(application.getCreateUid());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
        createFlowCaseCommand.setReferId(application.getId());
        createFlowCaseCommand.setReferType(EntityType.PAYMENT_APPLICATION.getCode());
        createFlowCaseCommand.setContent(application.getApplicationNumber());
        createFlowCaseCommand.setServiceType("付款申请");
        createFlowCaseCommand.setProjectId(application.getCommunityId());
        createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

        flowService.createFlowCase(createFlowCaseCommand);
    }
}
