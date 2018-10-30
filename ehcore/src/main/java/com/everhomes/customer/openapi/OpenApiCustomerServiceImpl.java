package com.everhomes.customer.openapi;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.customer.CustomerAdminRecord;
import com.everhomes.customer.CustomerEntryInfo;
import com.everhomes.customer.CustomerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.address.ListPropApartmentsResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.community.BuildingServiceErrorCode;
import com.everhomes.rest.community.BuildingStatus;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByCategoryCommand;
import com.everhomes.rest.community.ListCommunitiesByKeywordResponse;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.CustomerErrorCode;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.openapi.BuildingDTO;
import com.everhomes.rest.customer.openapi.CommunityDTO;
import com.everhomes.rest.customer.openapi.CommunityResponse;
import com.everhomes.rest.customer.openapi.DeleteEnterpriseCommand;
import com.everhomes.rest.customer.openapi.ListBuildingResponse;
import com.everhomes.rest.customer.openapi.OpenApiUpdateCustomerCommand;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.organization.pm.PropFamilyDTO;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Component
public class OpenApiCustomerServiceImpl implements OpenApiCustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOpenApiController.class);

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private AddressProvider addressProvider;

    @Autowired
    private CommunityService communityService;

    @Override
    public EnterpriseCustomerDTO createEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setName(cmd.getCompanyName());
        checkEnterpriseCustomerUnique(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getCompanyName());
        checkEnterpriseCustomerAddress(cmd.getAddresses());
        customer.setNamespaceId((null != cmd.getNamespaceId() ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId()));
        if (cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        if (cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        dbProvider.execute((TransactionStatus status) -> {
        //create
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);
        //save operate event
        customerService.saveCustomerEvent(1, customer, null, (byte)0);
        //entry infos
        if (cmd.getAddresses()!=null && cmd.getAddresses().size()>0) {
            OrganizationDTO organizationDTO = customerService.createOrganization(customer);
            customer.setOrganizationId(organizationDTO.getId());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            cmd.getAddresses().forEach((a)->{
                Address address=  addressProvider.findActiveAddressByBuildingApartmentName(customer.getNamespaceId(), customer.getCommunityId(), a.getBuildingName(), a.getApartmentName());
                Building building = communityProvider.findBuildingByCommunityIdAndName(customer.getCommunityId(), a.getBuildingName());
                if(building!=null && address!=null){
                    CustomerEntryInfo entryInfo = new CustomerEntryInfo();
                    entryInfo.setNamespaceId(customer.getNamespaceId());
                    entryInfo.setAddress(address.getAddress());
                    entryInfo.setCustomerName(customer.getName());
                    entryInfo.setCustomerId(customer.getId());
                    entryInfo.setBuildingId(building.getId());
                    entryInfo.setAddressId(address.getId());
                    enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
                    customerService.updateOrganizationAddress(customer.getOrganizationId(),a.getBuildingId(),address.getId());
                }else {
                    throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ADDRESS_NOT_EXIST,
                            (String.format("address is not exist,buildingName = %s ,addressName = %s",a.getBuildingName(),a.getApartmentName())));
                }
            });
        }
        enterpriseCustomerSearcher.feedDoc(customer);
            return null;
        });
        EnterpriseCustomerDTO dto =  ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
        dto.setEnterpriseId(dto.getId());
        return dto;
    }

    private void checkEnterpriseCustomerAddress(List<AddressDTO> addresses) {
        if(addresses==null || addresses.size()==0){
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ADDRESS_ISNULL,
                    "address is null");
        }
    }

    private void checkEnterpriseCustomerUnique( Integer namespaceId,  Long commuityId, String companyName) {
        if (StringUtils.isNotBlank(companyName)) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, commuityId, companyName);
            if (customers != null && customers.size() > 0) {
                LOGGER.error("customerName {} in namespace {} already exist!", companyName, namespaceId);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST,
                        "customerName is already exist");
            }
        }
    }

    @Override
    public EnterpriseCustomerDTO updateEnterpriseCustomer(OpenApiUpdateCustomerCommand cmd) {
        EnterpriseCustomer customer =  checkEnterpriseCustomer(cmd.getEnterpriseId());
        checkEnterpriseCustomerAddress(cmd.getAddresses());
        customer.setName(cmd.getCompanyName());
        customer.setContactName(cmd.getContactName());
        customer.setCorpBusinessLicense(cmd.getCorpBusinessLicense());
        customer.setCorpDescription(cmd.getCorpDescription());
        customer.setHotline(cmd.getHotline());
        customer.setRemark(cmd.getRemark());
        customer.setName(cmd.getCompanyName());
//        customer.setNamespaceId((null != cmd.getNamespaceId() ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId()));
        if (cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        if (cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        //create
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        //save operate event
        customerService.saveCustomerEvent(3, customer, customer, (byte)0);
        //entry infos
        enterpriseCustomerProvider.deleteAllCustomerEntryInfo(customer.getId());
        if (cmd.getAddresses()!=null && cmd.getAddresses().size()>0) {
            if(customer.getOrganizationId()==null || customer.getOrganizationId()==0L){
                OrganizationDTO organizationDTO = customerService.createOrganization(customer);
                customer.setOrganizationId(organizationDTO.getId());
                enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            }
            cmd.getAddresses().forEach((a)->{
                Address address=  addressProvider.findActiveAddressByBuildingApartmentName(customer.getNamespaceId(), customer.getCommunityId(), a.getBuildingName(), a.getApartmentName());
                Building building = communityProvider.findBuildingByCommunityIdAndName(customer.getCommunityId(), a.getBuildingName());
                if(building!=null && address!=null){
                    CustomerEntryInfo entryInfo = new CustomerEntryInfo();
                    entryInfo.setNamespaceId(customer.getNamespaceId());
                    entryInfo.setAddress(address.getAddress());
                    entryInfo.setCustomerName(customer.getName());
                    entryInfo.setCustomerId(customer.getId());
                    entryInfo.setBuildingId(building.getId());
                    entryInfo.setAddressId(address.getId());
                    enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
                    customerService.updateOrganizationAddress(customer.getOrganizationId(),a.getBuildingId(),address.getId());
                }else {
                    throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ADDRESS_NOT_EXIST,
                            (String.format("address is not exist,buildingName = %s ,addressName = %s",a.getBuildingName(),a.getApartmentName())));
                }
            });
        }
        enterpriseCustomerSearcher.feedDoc(customer);
        return ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
    }

    @Override
    public EnterpriseCustomerDTO deleteEnterpriseCustomer(DeleteEnterpriseCommand cmd) {
       EnterpriseCustomer customer =  checkEnterpriseCustomer(cmd.getEnterpriseId());
        List<Contract> contracts = contractProvider.listContractByCustomerId(customer.getCommunityId(), customer.getId(), CustomerType.ENTERPRISE.getCode());
        for (Contract contract : contracts) {
            if (contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
                    || contract.getStatus() == ContractStatus.WAITING_FOR_APPROVAL.getCode() || contract.getStatus() == ContractStatus.APPROVE_QUALITIED.getCode()
                    || contract.getStatus() == ContractStatus.EXPIRING.getCode() || contract.getStatus() == ContractStatus.DRAFT.getCode()) {
                LOGGER.error("enterprise customer has contract. customer: {}", customer);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_HAS_CONTRACT,
                        "enterprise customer has contract");
            }
        }
        customer.setStatus(CommonStatus.INACTIVE.getCode());
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        Organization org = organizationProvider.findOrganizationById(customer.getOrganizationId());
        if (org != null && org.getId() != null) {
            DeleteOrganizationIdCommand command = new DeleteOrganizationIdCommand();
            command.setId(customer.getOrganizationId());
            organizationService.deleteEnterpriseById(command, false);
        }
        return ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
    }

    @Override
    public void createEnterpriseAdmin(DeleteEnterpriseCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getEnterpriseId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(customer.getNamespaceId(), cmd.getAdminToken());
        String contactType = null;
        if (null != userIdentifier) {
            contactType = OrganizationMemberTargetType.USER.getCode();
        } else {
            contactType = OrganizationMemberTargetType.UNTRACK.getCode();
        }
        if (customer != null && customer.getOrganizationId() != null && customer.getOrganizationId() != 0) {
            CreateOrganizationAdminCommand createAdminCmd = new CreateOrganizationAdminCommand();
            createAdminCmd.setContactToken(cmd.getAdminToken());
            createAdminCmd.setContactName(cmd.getAdminName());
            createAdminCmd.setOrganizationId(customer.getOrganizationId());
            createAdminCmd.setOwnerId(customer.getOwnerId());
            createAdminCmd.setNamespaceId(customer.getNamespaceId());
            createAdminCmd.setOwnerType("EhOrganization");
            rolePrivilegeService.createOrganizationAdmin(createAdminCmd);
            List<CustomerAdminRecord> records = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecordsByToken(customer.getId(), cmd.getAdminToken());
            if (records == null || records.size() == 0)
            enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getEnterpriseId(), cmd.getAdminName(), contactType, cmd.getAdminToken(),customer.getNamespaceId());
        } else if (customer != null) {
            //如果属于未认证的 只记录下管理员信息  在添加楼栋门牌和签约的时候激活管理员即可
            // 旧版的模式为新建客户则关联企业客户中organizationId，现在为激活才增加organizationId
            List<CustomerAdminRecord> records = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecordsByToken(customer.getId(), cmd.getAdminToken());
            if (records == null || records.size() == 0)
            enterpriseCustomerProvider.createEnterpriseCustomerAdminRecord(cmd.getEnterpriseId(), cmd.getAdminName(), contactType, cmd.getAdminToken(),customer.getNamespaceId());
        }
        if (customer != null) {
            customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
        }
    }

    @Override
    public void deleteEnterpriseAdmin(DeleteEnterpriseCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getEnterpriseId());
        dbProvider.execute((TransactionStatus status)->{
            //删除客户管理中的管理员记录
            enterpriseCustomerProvider.deleteEnterpriseCustomerAdminRecord(cmd.getEnterpriseId(), cmd.getAdminToken());
            if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0) {
                //删除企业管理中的管理员权限
                DeleteOrganizationAdminCommand deleteOrganizationAdminCmd = new DeleteOrganizationAdminCommand();
                deleteOrganizationAdminCmd.setContactToken(cmd.getAdminToken());
                deleteOrganizationAdminCmd.setOwnerType("EhOrganizations");
                deleteOrganizationAdminCmd.setOrganizationId(customer.getOrganizationId());
                List<Organization> organizations =  organizationProvider.listPMOrganizations(customer.getNamespaceId());
                deleteOrganizationAdminCmd.setOwnerId(organizations.get(0).getId());
                rolePrivilegeService.deleteOrganizationAdministrators(deleteOrganizationAdminCmd);
            }
            return null;
        });

        List<CustomerAdminRecord> customerAdminRecords = enterpriseCustomerProvider.listEnterpriseCustomerAdminRecords(cmd.getEnterpriseId(), null);
        if (customerAdminRecords != null && customerAdminRecords.size() > 0) {
            customer.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        } else {
            customer.setAdminFlag(TrueOrFalseFlag.FALSE.getCode());
        }
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
    }

    private EnterpriseCustomer checkEnterpriseCustomer(Long id) {
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(id);
        if (customer == null || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(customer.getStatus()))) {
            LOGGER.error("enterprise customer is not exist or active. id: {}, customer: {}", id, customer);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                    "customer is not exist or active");
        }
        return customer;
    }

    @Override
    public List<PropFamilyDTO> listAddresses(Long buildingId) {
        Building building = communityProvider.findBuildingById(buildingId);
        if (building == null) {
            LOGGER.error("building is not exist，id = {}", buildingId);
            LOGGER.error("Building not found");
            throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE,
                    BuildingServiceErrorCode.ERROR_BUILDING_NOT_FOUND, "Building not found");

        } else {
            if (BuildingStatus.ACTIVE != BuildingStatus.fromCode(building.getStatus())) {
                LOGGER.error("Building already deleted");
                throw RuntimeErrorException.errorWith(BuildingServiceErrorCode.SCOPE,
                        BuildingServiceErrorCode.ERROR_BUILDING_DELETED, "Building already deleted");
            }
        }
        ListPropApartmentsByKeywordCommand cmd = new ListPropApartmentsByKeywordCommand();
        cmd.setBuildingName(building.getName());
        cmd.setCommunityId(building.getCommunityId());
        cmd.setNamespaceId(building.getNamespaceId());
        ListPropApartmentsResponse response =  propertyMgrService.listNewPropApartmentsByKeyword(cmd);
        return response.getResultList();
    }

    @Override
    public CommunityResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd) {
        ListCommunitiesByKeywordResponse response =  communityService.listCommunitiesByCategory(cmd);
        CommunityResponse communityResponse = new CommunityResponse();
        communityResponse.setNextPageAnchor(response.getNextPageAnchor());
        List<CommunityDTO> communityDTOS = new ArrayList<>();
        if(response.getRequests()!=null && response.getRequests().size()>0){
            response.getRequests().forEach((c) -> communityDTOS.add(ConvertHelper.convert(c, CommunityDTO.class)));
        }
        communityResponse.setRequests(communityDTOS);
        return communityResponse;
    }

    @Override
    public ListBuildingResponse listBuildings(ListBuildingCommand cmd) {
        ListBuildingCommandResponse response = communityService.listBuildings(cmd);
        ListBuildingResponse buildingResponse = new ListBuildingResponse();
        buildingResponse.setNextPageAnchor(response.getNextPageAnchor());
        List<BuildingDTO> buildingDTOS = new ArrayList<>();
        if(response.getBuildings()!=null && response.getBuildings().size()>0){
            response.getBuildings().forEach((c) -> buildingDTOS.add(ConvertHelper.convert(c, BuildingDTO.class)));
        }
        buildingResponse.setBuildings(buildingDTOS);
        return buildingResponse;
    }
}
