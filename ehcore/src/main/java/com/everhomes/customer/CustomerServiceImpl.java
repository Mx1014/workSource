package com.everhomes.customer;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contract.ContractService;
import com.everhomes.organization.*;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.*;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.openapi.ZJGKOpenServiceImpl;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ModuleName;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;

/**
 * Created by ying.xiong on 2017/8/15.
 */
@Component
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private EnterpriseCustomerSearcher enterpriseCustomerSearcher;

    @Autowired
    private ImportFileService importFileService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ZJGKOpenServiceImpl zjgkOpenService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    
    @Autowired
    private UserService userService;
    
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;

    @Autowired
    private AddressProvider addressProvider;
	
    private static final  String queueDelay = "trackingPlanTaskDelays";
    private static final  String  queueNoDelay = "trackingPlanTaskNoDelays";
    
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private FieldService fieldService;

    private void checkPrivilege(Integer ns) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(ns);
        //产品功能 #20796
//        if(namespaceId == 999971 || namespaceId == 999983) {
//            LOGGER.error("Insufficient privilege");
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }
    }

    private void checkEnterpriseCustomerNumberUnique(Long id, Integer namespaceId, String customerNumber, String customerName) {
        if(StringUtils.isNotBlank(customerNumber)) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndNumber(namespaceId, customerNumber);
            if(customers != null && customers.size() > 0) {
                if(id != null) {
                    for(EnterpriseCustomer customer : customers) {
                        if(id.equals(customer.getId())) {
                            return;
                        }
                    }
                }
                LOGGER.error("customerNumber {} in namespace {} already exist!", customerNumber, namespaceId);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NUMBER_IS_EXIST,
                        "contractNumber is already exist");
            }
        }
        if(StringUtils.isNotBlank(customerName)) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, customerName);
            if(customers != null && customers.size() > 0) {
                if(id != null) {
                    for(EnterpriseCustomer customer : customers) {
                        if(id.equals(customer.getId())) {
                            return;
                        }
                    }
                }
                LOGGER.error("customerName {} in namespace {} already exist!", customerName, namespaceId);
                throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST,
                        "customerName is already exist");
            }
        }

    }

    @Override
    public EnterpriseCustomerDTO createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd) {
        checkPrivilege(cmd.getNamespaceId());
        checkEnterpriseCustomerNumberUnique(null, cmd.getNamespaceId(), cmd.getCustomerNumber(), cmd.getName());
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setNamespaceId((null != cmd.getNamespaceId() ? cmd.getNamespaceId() : UserContext.getCurrentNamespaceId()));
        if(cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        if(cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        customer.setCreatorUid(UserContext.currentUserId());
        if(null != customer.getLongitude() && null != customer.getLatitude()){
        	String geohash  = GeoHashUtils.encode(customer.getLatitude(), customer.getLongitude());
        	customer.setGeohash(geohash);
        }
        if(null != customer && customer.getTrackingUid() != -1){
	        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(customer.getTrackingUid());
	    	if(null != detail && null != detail.getContactName()){
	    		customer.setTrackingName(detail.getContactName());
	    	}
        }
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);
        
        //企业客户新增成功,保存客户事件
        saveCustomerEvent( 1  ,customer ,null);
        
        OrganizationDTO organizationDTO = createOrganization(customer);
        customer.setOrganizationId(organizationDTO.getId());
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);

        return convertToDTO(customer);
    }

    private EnterpriseCustomerDTO convertToDTO(EnterpriseCustomer customer) {
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
//        ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
        ScopeFieldItem categoryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getCategoryItemId());
        if(categoryItem != null) {
            dto.setCategoryItemName(categoryItem.getItemDisplayName());
        }
//        ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
        ScopeFieldItem levelItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getLevelItemId());
        if(levelItem != null) {
            dto.setLevelItemName(levelItem.getItemDisplayName());
        }
        if(null != dto.getCorpIndustryItemId()){
        	ScopeFieldItem corpIndustryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getCorpIndustryItemId());
        	if(null != corpIndustryItem){
        		dto.setCorpIndustryItemName(corpIndustryItem.getItemDisplayName());
        	}
        }
        if(null != dto.getContactGenderItemId()){
        	ScopeFieldItem contactGenderItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getContactGenderItemId());
        	if(null != contactGenderItem){
        		dto.setContactGenderItemName(contactGenderItem.getItemDisplayName());
        	}
        }
        if(dto.getTrackingUid() != null && dto.getTrackingUid() != -1) {
        	dto.setTrackingName(dto.getTrackingName());
        }
        if(null != dto.getPropertyType()){
        	ScopeFieldItem propertyTypeItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getPropertyType());
        	if(null != propertyTypeItem){
        		dto.setPropertyTypeName(propertyTypeItem.getItemDisplayName());
        	}
        }

        if(null != dto.getRegistrationTypeId()){
            ScopeFieldItem registrationTypeItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getRegistrationTypeId());
            if(null != registrationTypeItem){
                dto.setRegistrationTypeName(registrationTypeItem.getItemDisplayName());
            }
        }

        if(null != dto.getTechnicalFieldId()){
            ScopeFieldItem technicalFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getTechnicalFieldId());
            if(null != technicalFieldItem){
                dto.setTechnicalFieldName(technicalFieldItem.getItemDisplayName());
            }
        }

        if(null != dto.getTaxpayerTypeId()){
            ScopeFieldItem taxpayerTypeItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getTaxpayerTypeId());
            if(null != taxpayerTypeItem){
                dto.setTaxpayerTypeName(taxpayerTypeItem.getItemDisplayName());
            }
        }

        if(null != dto.getRelationWillingId()){
            ScopeFieldItem relationWillingItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getRelationWillingId());
            if(null != relationWillingItem){
                dto.setRelationWillingName(relationWillingItem.getItemDisplayName());
            }
        }

        if(null != dto.getHighAndNewTechId()){
            ScopeFieldItem highAndNewTechItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getHighAndNewTechId());
            if(null != highAndNewTechItem){
                dto.setHighAndNewTechName(highAndNewTechItem.getItemDisplayName());
            }
        }

        if(null != dto.getEntrepreneurialCharacteristicsId()){
            ScopeFieldItem entrepreneurialCharacteristicsItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getEntrepreneurialCharacteristicsId());
            if(null != entrepreneurialCharacteristicsItem){
                dto.setEntrepreneurialCharacteristicsName(entrepreneurialCharacteristicsItem.getItemDisplayName());
            }
        }

        if(null != dto.getSerialEntrepreneurId()){
            ScopeFieldItem serialEntrepreneurItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(),customer.getCommunityId(), dto.getSerialEntrepreneurId());
            if(null != serialEntrepreneurItem){
                dto.setSerialEntrepreneurName(serialEntrepreneurItem.getItemDisplayName());
            }
        }

        return dto;
    }

    private OrganizationDTO createOrganization(EnterpriseCustomer customer) {
        Organization org = organizationProvider.findOrganizationByNameAndNamespaceId(customer.getName(), customer.getNamespaceId());
        if(org != null && OrganizationStatus.ACTIVE.equals(OrganizationStatus.fromCode(org.getStatus()))) {
            //已存在则更新 地址、官网地址、企业logo
            org.setWebsite(customer.getCorpWebsite());
            organizationProvider.updateOrganization(org);
            OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(org.getId());
            if(detail != null) {
                detail.setAvatar(customer.getCorpLogoUri());
                detail.setAddress(customer.getContactAddress());
                detail.setLatitude(customer.getLatitude());
                detail.setLongitude(customer.getLongitude());
                organizationProvider.updateOrganizationDetail(detail);
            } else {
                detail = new OrganizationDetail();
                detail.setOrganizationId(org.getId());
                detail.setAddress(customer.getContactAddress());
                detail.setLatitude(customer.getLatitude());
                detail.setLongitude(customer.getLongitude());
                detail.setAvatar(customer.getCorpLogoUri());
                detail.setCreateTime(org.getCreateTime());
                organizationProvider.createOrganizationDetail(detail);
            }
            return ConvertHelper.convert(org, OrganizationDTO.class);
        }
        CreateEnterpriseCommand command = new CreateEnterpriseCommand();
        command.setName(customer.getName());
//        command.setDisplayName(customer.getNickName());
        command.setNamespaceId(customer.getNamespaceId());
        command.setAvatar(customer.getCorpLogoUri());
//        command.setDescription(customer.getCorpDescription());
        command.setCommunityId(customer.getCommunityId());
//        command.setMemberCount(customer.getCorpEmployeeAmount() == null ? 0 : customer.getCorpEmployeeAmount() + 0L);
//        command.setContactor(customer.getContactName());
//        command.setContactsPhone(customer.getContactPhone());
//        command.setEntries(customer.getContactMobile());
        command.setAddress(customer.getContactAddress());
        if(customer.getLatitude() != null) {
            command.setLatitude(customer.getLatitude().toString());
        }
        if(customer.getLongitude() != null) {
            command.setLongitude(customer.getLongitude().toString());
        }
        command.setWebsite(customer.getCorpWebsite());
        return organizationService.createEnterprise(command);
    }

    @Override
    public EnterpriseCustomerDTO updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        checkPrivilege(customer.getNamespaceId());
        //产品功能 #20796 同步过来的客户名称不可改
        if(NamespaceCustomerType.EBEI.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))
                || NamespaceCustomerType.SHENZHOU.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType())) ) {
            if(!customer.getName().equals(cmd.getName())) {
                LOGGER.error("Insufficient privilege");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                        "Insufficient privilege");
            }
        }
        checkEnterpriseCustomerNumberUnique(customer.getId(), customer.getNamespaceId(), cmd.getCustomerNumber(), cmd.getName());
        EnterpriseCustomer updateCustomer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        updateCustomer.setNamespaceId(customer.getNamespaceId());
        updateCustomer.setCommunityId(customer.getCommunityId());
        updateCustomer.setOrganizationId(customer.getOrganizationId());
        updateCustomer.setCreateTime(customer.getCreateTime());
        updateCustomer.setCreatorUid(customer.getCreatorUid());
        if(cmd.getCorpEntryDate() != null) {
            updateCustomer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }

        if(cmd.getFoundingTime() != null) {
            customer.setFoundingTime(new Timestamp(cmd.getFoundingTime()));
        }
        updateCustomer.setStatus(CommonStatus.ACTIVE.getCode());
        //保存经纬度
        if(null != updateCustomer.getLongitude() && null != updateCustomer.getLatitude()){
        	String geohash  = GeoHashUtils.encode(updateCustomer.getLatitude(), updateCustomer.getLongitude());
        	updateCustomer.setGeohash(geohash);
        }
        if(null != updateCustomer && updateCustomer.getTrackingUid() != null && updateCustomer.getTrackingUid() != -1){
	        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(updateCustomer.getTrackingUid());
	    	if(null != detail && null != detail.getContactName()){
	    		updateCustomer.setTrackingName(detail.getContactName());
	    	}
        }
        if(updateCustomer.getTrackingUid() == -1 || updateCustomer.getTrackingUid() == null){
        	updateCustomer.setTrackingName(null);
        }
        enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);
        enterpriseCustomerSearcher.feedDoc(updateCustomer);
        
        //保存客户事件
        saveCustomerEvent( 3  ,updateCustomer ,customer);

        if(customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
            UpdateEnterpriseCommand command = new UpdateEnterpriseCommand();
            command.setId(updateCustomer.getOrganizationId());
            command.setName(updateCustomer.getName());
            command.setDisplayName(updateCustomer.getNickName());
            command.setNamespaceId(updateCustomer.getNamespaceId());
            command.setAvatar(updateCustomer.getCorpLogoUri());
            command.setDescription(updateCustomer.getCorpDescription());
            command.setCommunityId(updateCustomer.getCommunityId());
            command.setMemberCount(updateCustomer.getCorpEmployeeAmount() == null ? 0 : updateCustomer.getCorpEmployeeAmount() + 0L);
            command.setAddress(updateCustomer.getContactAddress());
            command.setLongitude(updateCustomer.getLongitude());
            command.setLatitude(updateCustomer.getLatitude());
            command.setWebsite(updateCustomer.getCorpWebsite());
            organizationService.updateEnterprise(command, false);
        } else {//没有企业的要新增一个
            OrganizationDTO dto = createOrganization(updateCustomer);
            updateCustomer.setOrganizationId(dto.getId());
            enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);
        }

        //修改了客户名称则要同步修改合同里面的客户名称
        if(!customer.getName().equals(cmd.getName())) {
            List<Contract> contracts = contractProvider.listContractByCustomerId(updateCustomer.getCommunityId(), updateCustomer.getId(), CustomerType.ENTERPRISE.getCode());
            if(contracts != null && contracts.size() > 0) {
                contracts.forEach(contract -> {
                    contract.setCustomerName(updateCustomer.getName());
                    contractProvider.updateContract(contract);
                    contractSearcher.feedDoc(contract);
                });
            }
        }
        return convertToDTO(updateCustomer);
    }

    @Override
    public void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        checkPrivilege(customer.getNamespaceId());
        //产品功能 #20796 同步过来的不能删
        if(NamespaceCustomerType.EBEI.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType()))
                || NamespaceCustomerType.SHENZHOU.equals(NamespaceCustomerType.fromCode(customer.getNamespaceCustomerType())) ) {
            LOGGER.error("Insufficient privilege");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }
        List<Contract> contracts = contractProvider.listContractByCustomerId(customer.getCommunityId(),customer.getId(),CustomerType.ENTERPRISE.getCode());
        for(Contract contract : contracts) {
            if(contract.getStatus() == ContractStatus.ACTIVE.getCode() || contract.getStatus() == ContractStatus.WAITING_FOR_LAUNCH.getCode()
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
        
        //企业客户新增成功,保存客户事件
        saveCustomerEvent( 2  ,customer ,null);

        if(customer.getOrganizationId() != null) {
            DeleteOrganizationIdCommand command = new DeleteOrganizationIdCommand();
            command.setId(customer.getOrganizationId());
            organizationService.deleteEnterpriseById(command);
        }

    }

    @Override
    public SearchEnterpriseCustomerResponse searchEnterpriseCustomer(SearchEnterpriseCustomerCommand cmd) {
        return null;
    }

    @Override
    public ImportFileTaskDTO importEnterpriseCustomer(ImportEnterpriseCustomerDataCommand cmd, MultipartFile mfile, Long userId) {
        checkPrivilege(cmd.getNamespaceId());
        ImportFileTask task = new ImportFileTask();
        try {
            //解析excel
            List resultList = PropMrgOwnerHandler.processorExcel(mfile.getInputStream());

            if(null == resultList || resultList.isEmpty()){
                LOGGER.error("File content is empty。userId="+userId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL,
                        localeStringService.getLocalizedString(String.valueOf(UserServiceErrorCode.SCOPE),
                                String.valueOf(UserServiceErrorCode.ERROR_FILE_CONTEXT_ISNULL),
                                UserContext.current().getUser().getLocale(),"File content is empty"));
            }
            task.setOwnerType(EntityType.ORGANIZATIONS.getCode());
            task.setOwnerId(cmd.getOwnerId());
            task.setType(ImportFileTaskType.ENTERPRISE_CUSTOMER.getCode());
            task.setCreatorUid(userId);
            task = importFileService.executeTask(new ExecuteImportTaskCallback() {
                @Override
                public ImportFileResponse importFile() {
                    ImportFileResponse response = new ImportFileResponse();
                    List<ImportEnterpriseCustomerDataDTO> datas = handleImportEnterpriseCustomerData(resultList);
                    if(datas.size() > 0){
                        //设置导出报错的结果excel的标题
                        response.setTitle(datas.get(0));
                        datas.remove(0);
                    }
                    List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> results = importEnterpriseCustomerData(cmd, datas, userId);
                    response.setTotalCount((long)datas.size());
                    response.setFailCount((long)results.size());
                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        LOGGER.info("task: {}",  task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, List<ImportEnterpriseCustomerDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> errorDataLogs = new ArrayList<>();

        for (ImportEnterpriseCustomerDataDTO str : list) {
            ImportFileResultLog<ImportEnterpriseCustomerDataDTO> log = new ImportFileResultLog<>(CustomerErrorCode.SCOPE);
            EnterpriseCustomer customer = new EnterpriseCustomer();

            if(StringUtils.isBlank(str.getName())){
                LOGGER.error("enterpirse customer name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer name is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            List<EnterpriseCustomer> enterpriseCustomers = enterpriseCustomerProvider.listEnterpriseCustomerByNamespaceIdAndName(cmd.getNamespaceId(), str.getName());
            if(enterpriseCustomers != null && enterpriseCustomers.size() > 0) {
                LOGGER.error("enterpirse customer name is already exist, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer name is already exist");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_NAME_IS_EXIST);
                errorDataLogs.add(log);
                continue;
            }
            customer.setName(str.getName());

            if(StringUtils.isBlank(str.getContactName())){
                LOGGER.error("enterpirse customer contact name is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer contact name is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CONTACT_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setContactName(str.getContactName());

            if(StringUtils.isBlank(str.getContactMobile())){
                LOGGER.error("enterpirse customer contact mobile is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer contact mobile is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CONTACT_MOBILE_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setContactMobile(str.getContactMobile());
            customer.setContactPhone(str.getContactPhone());
            customer.setContactAddress(str.getContactAddress());
    //产品期望改为不存在的导入失败 by xiongying20170904
//            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            ScopeFieldItem scopeCategoryFieldItem = fieldService.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            if(scopeCategoryFieldItem == null) {
                LOGGER.error("enterpirse customer category is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer category is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CATEGORY_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
//            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
            ScopeFieldItem scopeLevelFieldItem = fieldService.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), cmd.getCommunityId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
            if(scopeLevelFieldItem == null) {
                LOGGER.error("enterpirse customer level is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer level is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_LEVEL_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setLevelItemId(scopeLevelFieldItem.getItemId());
//            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
//            if(scopeCategoryFieldItem != null) {
//                customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
//            }
//            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
//            if(scopeLevelFieldItem != null) {
//                customer.setLevelItemId(scopeLevelFieldItem.getItemId());
//            }

            customer.setCommunityId(cmd.getCommunityId());
            customer.setNamespaceId(cmd.getNamespaceId());
            customer.setCreatorUid(userId);
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);

            OrganizationDTO organizationDTO = createOrganization(customer);
            customer.setOrganizationId(organizationDTO.getId());
            enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
            //给企业账号添加管理员 默认添加联系人作为管理员 by xiongying20170909
//            Map<Long, List<String>> orgAdminAccounts = new HashMap<>();
//            if (orgAdminAccounts.get(organizationDTO.getId()) == null ||
//                    !orgAdminAccounts.get(organizationDTO.getId()).contains(str.getContactMobile())) {
//                if (!org.springframework.util.StringUtils.isEmpty(str.getContactMobile())) {
//                    CreateOrganizationAdminCommand createOrganizationAdminCommand = new CreateOrganizationAdminCommand();
//                    createOrganizationAdminCommand.setOrganizationId(organizationDTO.getId());
//                    createOrganizationAdminCommand.setContactToken(str.getContactMobile());
//                    createOrganizationAdminCommand.setContactName(str.getContactName());
//                    rolePrivilegeService.createOrganizationAdmin(createOrganizationAdminCommand, cmd.getNamespaceId());
//                }
//                if(orgAdminAccounts.get(organizationDTO.getId()) == null) {
//                    List<String> mobiles = new ArrayList<>();
//                    mobiles.add(str.getContactMobile());
//                    orgAdminAccounts.put(organizationDTO.getId(), mobiles);
//                }
//                orgAdminAccounts.get(organizationDTO.getId()).add(str.getContactMobile());
//            }
        }
        return errorDataLogs;
    }

    private List<ImportEnterpriseCustomerDataDTO> handleImportEnterpriseCustomerData(List list){
        List<ImportEnterpriseCustomerDataDTO> result = new ArrayList<>();
        int row = 1;
//        int i = 1;
        for (Object o : list) {
            if(row < 2){
                row ++;
                continue;
            }

//            if(i > 9 && result.size() < 2) {
//                break;
//            }
//            i++;

            RowResult r = (RowResult)o;
            ImportEnterpriseCustomerDataDTO data = null;
            if(StringUtils.isNotBlank(r.getA())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setName(r.getA().trim());
            }

            if(StringUtils.isNotBlank(r.getB())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setCategoryItemName(r.getB().trim());
            }

            if(StringUtils.isNotBlank(r.getC())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setLevelItemName(r.getC().trim());
            }

            if(StringUtils.isNotBlank(r.getD())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactName(r.getD().trim());
            }

            if(StringUtils.isNotBlank(r.getE())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactMobile(r.getE().trim());
            }

            if(StringUtils.isNotBlank(r.getF())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactPhone(r.getF().trim());
            }

            if(StringUtils.isNotBlank(r.getG())) {
                if(data == null) {
                    data = new ImportEnterpriseCustomerDataDTO();
                }
                data.setContactAddress(r.getG().trim());
            }

            if(data != null) {
                result.add(data);
            }
        }
        LOGGER.info("result size : " + result.size());
        return result;

    }

    @Override
    public EnterpriseCustomerDTO getEnterpriseCustomer(GetEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        EnterpriseCustomerDTO dto = convertToDTO(customer);
        popularCustomerUrl(dto);
        return dto;
    }

    private void popularCustomerUrl(EnterpriseCustomerDTO dto) {
        if(dto.getContactAvatarUri() != null) {
            String contentUrl = contentServerService.parserUri(dto.getContactAvatarUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId());
            dto.setContactAvatarUrl(contentUrl);
        }
        if(dto.getCorpLogoUri() != null) {
            String contentUrl = contentServerService.parserUri(dto.getCorpLogoUri(), EntityType.ENTERPRISE_CUSTOMER.getCode(), dto.getId());
            dto.setCorpLogoUrl(contentUrl);
        }
    }

    private EnterpriseCustomer checkEnterpriseCustomer(Long id) {
        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(id);
        if(customer == null || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(customer.getStatus()))) {
            LOGGER.error("enterprise customer is not exist or active. id: {}, customer: {}", id, customer);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                        "customer is not exist or active");
        }
        return customer;
    }

    @Override
    public void createCustomerTalent(CreateCustomerTalentCommand cmd) {
        CustomerTalent talent = ConvertHelper.convert(cmd, CustomerTalent.class);
        enterpriseCustomerProvider.createCustomerTalent(talent);
    }

    @Override
    public void deleteCustomerTalent(DeleteCustomerTalentCommand cmd) {
        CustomerTalent talent = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTalent(talent);

    }

    @Override
    public CustomerTalentDTO getCustomerTalent(GetCustomerTalentCommand cmd) {
        CustomerTalent talent = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTalentDTO(talent, cmd.getCommunityId());
    }

    private CustomerTalentDTO convertCustomerTalentDTO(CustomerTalent talent, Long communityId) {
        CustomerTalentDTO dto = ConvertHelper.convert(talent, CustomerTalentDTO.class);
        if(dto.getGender() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getGender());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getGender());
            if(scopeFieldItem != null) {
                dto.setGenderName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getReturneeFlag() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getReturneeFlag());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getReturneeFlag());
            if(scopeFieldItem != null) {
                dto.setReturneeFlagName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getAbroadItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getAbroadItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getAbroadItemId());
            if(scopeFieldItem != null) {
                dto.setAbroadItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getDegreeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getDegreeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getDegreeItemId());
            if(scopeFieldItem != null) {
                dto.setDegreeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getNationalityItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getNationalityItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getNationalityItemId());
            if(scopeFieldItem != null) {
                dto.setNationalityItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getIndividualEvaluationItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getIndividualEvaluationItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getIndividualEvaluationItemId());
            if(scopeFieldItem != null) {
                dto.setIndividualEvaluationItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getTechnicalTitleItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getTechnicalTitleItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), communityId, dto.getTechnicalTitleItemId());
            if(scopeFieldItem != null) {
                dto.setTechnicalTitleItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public List<CustomerTalentDTO> listCustomerTalents(ListCustomerTalentsCommand cmd) {
        List<CustomerTalent> talents = enterpriseCustomerProvider.listCustomerTalentsByCustomerId(cmd.getCustomerId());
        if(talents != null && talents.size() > 0) {
            return talents.stream().map(talent -> {
                return convertCustomerTalentDTO(talent, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    private CustomerTalent checkCustomerTalent(Long id, Long customerId) {
        CustomerTalent talent = enterpriseCustomerProvider.findCustomerTalentById(id);
        if(talent == null || !talent.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(talent.getStatus()))) {
            LOGGER.error("enterprise customer talent is not exist or active. id: {}, talent: {}", id, talent);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TALENT_NOT_EXIST,
                    "customer talent is not exist or active");
        }
        return talent;
    }

    @Override
    public void updateCustomerTalent(UpdateCustomerTalentCommand cmd) {
        CustomerTalent exist = checkCustomerTalent(cmd.getId(), cmd.getCustomerId());
        CustomerTalent talent = ConvertHelper.convert(cmd, CustomerTalent.class);
        talent.setCreateTime(exist.getCreateTime());
        talent.setCreatorUid(exist.getCreatorUid());
        enterpriseCustomerProvider.updateCustomerTalent(talent);
    }

    @Override
    public void createCustomerApplyProject(CreateCustomerApplyProjectCommand cmd) {
        CustomerApplyProject project = ConvertHelper.convert(cmd, CustomerApplyProject.class);
        if(cmd.getProjectCompleteDate() != null) {
            project.setProjectCompleteDate(new Timestamp(cmd.getProjectCompleteDate()));
        }
        if(cmd.getProjectEstablishDate() != null) {
            project.setProjectEstablishDate(new Timestamp(cmd.getProjectEstablishDate()));
        }
        enterpriseCustomerProvider.createCustomerApplyProject(project);
    }

    @Override
    public void createCustomerCommercial(CreateCustomerCommercialCommand cmd) {
        CustomerCommercial commercial = ConvertHelper.convert(cmd, CustomerCommercial.class);
        if(cmd.getCancelDate() != null) {
            commercial.setCancelDate(new Timestamp(cmd.getCancelDate()));
        }
        if(cmd.getChangeDate() != null) {
            commercial.setChangeDate(new Timestamp(cmd.getChangeDate()));
        }
        if(cmd.getFoundationDate() != null) {
            commercial.setFoundationDate(new Timestamp(cmd.getFoundationDate()));
        }
        if(cmd.getBusinessLicenceDate() != null) {
            commercial.setBusinessLicenceDate(new Timestamp(cmd.getBusinessLicenceDate()));
        }
        if(cmd.getTaxRegistrationDate() != null) {
            commercial.setTaxRegistrationDate(new Timestamp(cmd.getTaxRegistrationDate()));
        }
        if(cmd.getValidityBeginDate() != null) {
            commercial.setValidityBeginDate(new Timestamp(cmd.getValidityBeginDate()));
        }
        if(cmd.getValidityEndDate() != null) {
            commercial.setValidityEndDate(new Timestamp(cmd.getValidityEndDate()));
        }
        if(cmd.getLiquidationCommitteeRecoredDate() != null) {
            commercial.setLiquidationCommitteeRecoredDate(new Timestamp(cmd.getLiquidationCommitteeRecoredDate()));
        }

        enterpriseCustomerProvider.createCustomerCommercial(commercial);
    }

    @Override
    public void createCustomerPatent(CreateCustomerPatentCommand cmd) {
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        if(cmd.getRegisteDate() != null) {
            patent.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerPatent(patent);
    }

    @Override
    public void createCustomerTrademark(CreateCustomerTrademarkCommand cmd) {
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
        if(cmd.getRegisteDate() != null) {
            trademark.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerTrademark(trademark);
    }

    @Override
    public void deleteCustomerApplyProject(DeleteCustomerApplyProjectCommand cmd) {
        CustomerApplyProject project = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerApplyProject(project);
    }

    private CustomerApplyProject checkCustomerApplyProject(Long id, Long customerId) {
        CustomerApplyProject project = enterpriseCustomerProvider.findCustomerApplyProjectById(id);
        if(project == null || !project.getCustomerId().equals(customerId)
                || CommonStatus.INACTIVE.equals(CommonStatus.fromCode(project.getStatus()))) {
            LOGGER.error("enterprise customer project is not exist or active. id: {}, project: {}", id, project);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_PROJECT_NOT_EXIST,
                    "customer project is not exist or active");
        }
        return project;
    }

    @Override
    public void deleteCustomerCommercial(DeleteCustomerCommercialCommand cmd) {
        CustomerCommercial commercial = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerCommercial(commercial);
    }

    private CustomerCommercial checkCustomerCommercial(Long id, Long customerId) {
        CustomerCommercial commercial = enterpriseCustomerProvider.findCustomerCommercialById(id);
        if(commercial == null || !commercial.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(commercial.getStatus()))) {
            LOGGER.error("enterprise customer commercial is not exist or active. id: {}, commercial: {}", id, commercial);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_COMMERCIAL_NOT_EXIST,
                    "customer commercial is not exist or active");
        }
        return commercial;
    }

    @Override
    public void deleteCustomerPatent(DeleteCustomerPatentCommand cmd) {
        CustomerPatent patent = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerPatent(patent);
    }

    private CustomerPatent checkCustomerPatent(Long id, Long customerId) {
        CustomerPatent patent = enterpriseCustomerProvider.findCustomerPatentById(id);
        if(patent == null || !patent.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(patent.getStatus()))) {
            LOGGER.error("enterprise customer patent is not exist or active. id: {}, patent: {}", id, patent);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_PATENT_NOT_EXIST,
                    "customer patent is not exist or active");
        }
        return patent;
    }

    @Override
    public void deleteCustomerTrademark(DeleteCustomerTrademarkCommand cmd) {
        CustomerTrademark talent = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerTrademark(talent);
    }

    private CustomerTrademark checkCustomerTrademark(Long id, Long customerId) {
        CustomerTrademark trademark = enterpriseCustomerProvider.findCustomerTrademarkById(id);
        if(trademark == null || !trademark.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(trademark.getStatus()))) {
            LOGGER.error("enterprise customer patent is not exist or active. id: {}, trademark: {}", id, trademark);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRADEMARK_NOT_EXIST,
                    "customer trademark is not exist or active");
        }
        return trademark;
    }

    @Override
    public CustomerApplyProjectDTO getCustomerApplyProject(GetCustomerApplyProjectCommand cmd) {
        CustomerApplyProject project = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        return convertCustomerApplyProjectDTO(project, cmd.getCommunityId());
    }

    private CustomerApplyProjectDTO convertCustomerApplyProjectDTO(CustomerApplyProject project, Long communityId) {
        CustomerApplyProjectDTO dto = ConvertHelper.convert(project, CustomerApplyProjectDTO.class);
        if(dto.getStatus() != null) {
            CustomerApplyProjectStatus status = CustomerApplyProjectStatus.fromStatus(dto.getStatus());
            if(status != null) {
                dto.setStatusName(status.getName());
            }
        }
        //PROJECTGSOURCE不是必填项目，这里没有判断 为空字符串
        if(dto.getProjectSource() != null) {
            String[] ids = dto.getProjectSource().split(",");
            LOGGER.info("project source: {}", ids);
            StringBuilder sb = new StringBuilder();
            for(String id : ids) {
//                ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(project.getNamespaceId(), Long.valueOf(id));
                ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(project.getNamespaceId(), communityId, Long.valueOf(id));
                LOGGER.info("project source scopeFieldItem: {}", scopeFieldItem);
                if(scopeFieldItem != null) {
                    if(sb.length() == 0) {
                        sb.append(scopeFieldItem.getItemDisplayName());
                    } else {
                        sb.append(",");
                        sb.append(scopeFieldItem.getItemDisplayName());
                    }
                }
            }
            dto.setProjectSourceNames(sb.toString());

        }
        return dto;
    }

    @Override
    public CustomerCommercialDTO getCustomerCommercial(GetCustomerCommercialCommand cmd) {
        CustomerCommercial commercial = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        return convertCustomerCommercialDTO(commercial, cmd.getCommunityId());
    }

    private CustomerCommercialDTO convertCustomerCommercialDTO(CustomerCommercial commercial, Long communityId) {
        CustomerCommercialDTO dto = ConvertHelper.convert(commercial, CustomerCommercialDTO.class);

        if(dto.getEnterpriseTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getEnterpriseTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), communityId, dto.getEnterpriseTypeItemId());
            if(scopeFieldItem != null) {
                dto.setEnterpriseTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getShareTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getShareTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), communityId, dto.getShareTypeItemId());
            if(scopeFieldItem != null) {
                dto.setShareTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getPropertyType() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getPropertyType());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), communityId, dto.getPropertyType());
            if(scopeFieldItem != null) {
                dto.setPropertyTypeName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerPatentDTO getCustomerPatent(GetCustomerPatentCommand cmd) {
        CustomerPatent patent = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        return convertCustomerPatentDTO(patent, cmd.getCommunityId());
    }

    private CustomerPatentDTO convertCustomerPatentDTO(CustomerPatent patent, Long communityId) {
        CustomerPatentDTO dto = ConvertHelper.convert(patent, CustomerPatentDTO.class);

        if(dto.getPatentStatusItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentStatusItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), communityId, dto.getPatentStatusItemId());
            if(scopeFieldItem != null) {
                dto.setPatentStatusItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getPatentTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), communityId, dto.getPatentTypeItemId());
            if(scopeFieldItem != null) {
                dto.setPatentTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerTrademarkDTO getCustomerTrademark(GetCustomerTrademarkCommand cmd) {
        CustomerTrademark trademark = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTrademarkDTO(trademark, cmd.getCommunityId());
    }

    private CustomerTrademarkDTO convertCustomerTrademarkDTO(CustomerTrademark trademark, Long communityId) {
        CustomerTrademarkDTO dto = ConvertHelper.convert(trademark, CustomerTrademarkDTO.class);

        if(dto.getTrademarkTypeItemId() != null) {
//            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(trademark.getNamespaceId(), dto.getTrademarkTypeItemId());
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(trademark.getNamespaceId(), communityId, dto.getTrademarkTypeItemId());
            if(scopeFieldItem != null) {
                dto.setTrademarkTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public List<CustomerApplyProjectDTO> listCustomerApplyProjects(ListCustomerApplyProjectsCommand cmd) {
        List<CustomerApplyProject> projects = enterpriseCustomerProvider.listCustomerApplyProjectsByCustomerId(cmd.getCustomerId());
        if(projects != null && projects.size() > 0) {
            return projects.stream().map(project -> {
                return convertCustomerApplyProjectDTO(project, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerCommercialDTO> listCustomerCommercials(ListCustomerCommercialsCommand cmd) {
        List<CustomerCommercial> commercials = enterpriseCustomerProvider.listCustomerCommercialsByCustomerId(cmd.getCustomerId());
        if(commercials != null && commercials.size() > 0) {
            return commercials.stream().map(commercial -> {
                return convertCustomerCommercialDTO(commercial, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerPatentDTO> listCustomerPatents(ListCustomerPatentsCommand cmd) {
        List<CustomerPatent> patents = enterpriseCustomerProvider.listCustomerPatentsByCustomerId(cmd.getCustomerId());
        if(patents != null && patents.size() > 0) {
            return patents.stream().map(patent -> {
                return convertCustomerPatentDTO(patent, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerTrademarkDTO> listCustomerTrademarks(ListCustomerTrademarksCommand cmd) {
        List<CustomerTrademark> trademarks = enterpriseCustomerProvider.listCustomerTrademarksByCustomerId(cmd.getCustomerId());
        if(trademarks != null && trademarks.size() > 0) {
            return trademarks.stream().map(trademark -> {
                return convertCustomerTrademarkDTO(trademark, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerApplyProject(UpdateCustomerApplyProjectCommand cmd) {
        CustomerApplyProject exist = checkCustomerApplyProject(cmd.getId(), cmd.getCustomerId());
        CustomerApplyProject project = ConvertHelper.convert(cmd, CustomerApplyProject.class);
        if(cmd.getProjectCompleteDate() != null) {
            project.setProjectCompleteDate(new Timestamp(cmd.getProjectCompleteDate()));
        }
        if(cmd.getProjectEstablishDate() != null) {
            project.setProjectEstablishDate(new Timestamp(cmd.getProjectEstablishDate()));
        }
        project.setCreateTime(exist.getCreateTime());
        project.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerApplyProject(project);
    }

    @Override
    public void updateCustomerCommercial(UpdateCustomerCommercialCommand cmd) {
        CustomerCommercial exist = checkCustomerCommercial(cmd.getId(), cmd.getCustomerId());
        CustomerCommercial commercial = ConvertHelper.convert(cmd, CustomerCommercial.class);

        if(cmd.getCancelDate() != null) {
            commercial.setCancelDate(new Timestamp(cmd.getCancelDate()));
        }
        if(cmd.getChangeDate() != null) {
            commercial.setChangeDate(new Timestamp(cmd.getChangeDate()));
        }
        if(cmd.getFoundationDate() != null) {
            commercial.setFoundationDate(new Timestamp(cmd.getFoundationDate()));
        }
        if(cmd.getBusinessLicenceDate() != null) {
            commercial.setBusinessLicenceDate(new Timestamp(cmd.getBusinessLicenceDate()));
        }
        if(cmd.getTaxRegistrationDate() != null) {
            commercial.setTaxRegistrationDate(new Timestamp(cmd.getTaxRegistrationDate()));
        }
        if(cmd.getValidityBeginDate() != null) {
            commercial.setValidityBeginDate(new Timestamp(cmd.getValidityBeginDate()));
        }
        if(cmd.getValidityEndDate() != null) {
            commercial.setValidityEndDate(new Timestamp(cmd.getValidityEndDate()));
        }
        if(cmd.getLiquidationCommitteeRecoredDate() != null) {
            commercial.setLiquidationCommitteeRecoredDate(new Timestamp(cmd.getLiquidationCommitteeRecoredDate()));
        }
        commercial.setCreateTime(exist.getCreateTime());
        commercial.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerCommercial(commercial);
    }

    @Override
    public void updateCustomerPatent(UpdateCustomerPatentCommand cmd) {
        CustomerPatent exist = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        if(cmd.getRegisteDate() != null) {
            patent.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        patent.setCreateTime(exist.getCreateTime());
        patent.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerPatent(patent);
    }

    @Override
    public void updateCustomerTrademark(UpdateCustomerTrademarkCommand cmd) {
        CustomerTrademark exist = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
        if(cmd.getRegisteDate() != null) {
            trademark.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        trademark.setCreateTime(exist.getCreateTime());
        trademark.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerTrademark(trademark);
    }

    @Override
    public void createCustomerEconomicIndicator(CreateCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator indicator = ConvertHelper.convert(cmd, CustomerEconomicIndicator.class);
        if(cmd.getMonth() != null) {
            indicator.setMonth(new Timestamp(cmd.getMonth()));
        }
        enterpriseCustomerProvider.createCustomerEconomicIndicator(indicator);

        //年月不为null 则填到年月所属对应的年度统计表里
        if(cmd.getMonth() != null) {
            CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), new Timestamp(cmd.getMonth()));
            if(statistic == null) {
                statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Timestamp(cmd.getMonth()));
                statistic.setStartTime(getDayBegin(cal));
                statistic.setEndTime(getDayEnd(cal));
                enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
            } else {
                BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
            }
        }

    }

    @Override
    public void createCustomerInvestment(CreateCustomerInvestmentCommand cmd) {
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
        if(cmd.getInvestmentTime() != null) {
            investment.setInvestmentTime(new Timestamp(cmd.getInvestmentTime()));
        }
        enterpriseCustomerProvider.createCustomerInvestment(investment);
    }

    @Override
    public void deleteCustomerEconomicIndicator(DeleteCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator indicator = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerEconomicIndicator(indicator);

        if(indicator.getMonth() != null) {
            CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
            if(statistic != null) {
                if(statistic.getTaxPayment() != null && indicator.getTaxPayment() != null) {
                    if(statistic.getTaxPayment().compareTo(indicator.getTaxPayment()) > 0) {
                        statistic.setTaxPayment(statistic.getTaxPayment().subtract(indicator.getTaxPayment()));
                    } else {
                        statistic.setTaxPayment(BigDecimal.ZERO);
                    }
                }
                if(statistic.getTurnover() != null && indicator.getTurnover() != null) {
                    if(statistic.getTurnover().compareTo(indicator.getTurnover()) > 0) {
                        statistic.setTurnover(statistic.getTurnover().subtract(indicator.getTurnover()));
                    } else {
                        statistic.setTurnover(BigDecimal.ZERO);
                    }
                }

                if(statistic.getTaxPayment().compareTo(BigDecimal.ZERO) == 0
                        && statistic.getTurnover().compareTo(BigDecimal.ZERO) == 0) {
                    enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                }

            }
        }
    }

    private CustomerEconomicIndicator checkCustomerEconomicIndicator(Long id, Long customerId) {
        CustomerEconomicIndicator indicator = enterpriseCustomerProvider.findCustomerEconomicIndicatorById(id);
        if(indicator == null || !indicator.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(indicator.getStatus()))) {
            LOGGER.error("enterprise customer economic indicator is not exist or active. id: {}, indicator: {}", id, indicator);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_ECONOMIC_INDICATOR_NOT_EXIST,
                    "customer economic indicator is not exist or active");
        }
        return indicator;
    }

    @Override
    public void deleteCustomerInvestment(DeleteCustomerInvestmentCommand cmd) {
        CustomerInvestment investment = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerInvestment(investment);
    }

    private CustomerInvestment checkCustomerInvestment(Long id, Long customerId) {
        CustomerInvestment investment = enterpriseCustomerProvider.findCustomerInvestmentById(id);
        if(investment == null || !investment.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(investment.getStatus()))) {
            LOGGER.error("enterprise customer investment is not exist or active. id: {}, investment: {}", id, investment);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_INVESTMENT_NOT_EXIST,
                    "customer investment is not exist or active");
        }
        return investment;
    }

    @Override
    public CustomerEconomicIndicatorDTO getCustomerEconomicIndicator(GetCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator indicator = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(indicator, CustomerEconomicIndicatorDTO.class);
    }

    @Override
    public CustomerInvestmentDTO getCustomerInvestment(GetCustomerInvestmentCommand cmd) {
        CustomerInvestment investment = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(investment, CustomerInvestmentDTO.class);
    }

    @Override
    public List<CustomerEconomicIndicatorDTO> listCustomerEconomicIndicators(ListCustomerEconomicIndicatorsCommand cmd) {
        List<CustomerEconomicIndicator> indicators = enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerId(cmd.getCustomerId());
        if(indicators != null && indicators.size() > 0) {
            return indicators.stream().map(indicator -> {
                return ConvertHelper.convert(indicator, CustomerEconomicIndicatorDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerInvestmentDTO> listCustomerInvestments(ListCustomerInvestmentsCommand cmd) {
        List<CustomerInvestment> investments = enterpriseCustomerProvider.listCustomerInvestmentsByCustomerId(cmd.getCustomerId());
        if(investments != null && investments.size() > 0) {
            return investments.stream().map(investment -> {
                return ConvertHelper.convert(investment, CustomerInvestmentDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerEconomicIndicator(UpdateCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator exist = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        CustomerEconomicIndicator indicator = ConvertHelper.convert(cmd, CustomerEconomicIndicator.class);
        if(cmd.getMonth() != null) {
            indicator.setMonth(new Timestamp(cmd.getMonth()));
        }
        indicator.setCreateTime(exist.getCreateTime());
        indicator.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerEconomicIndicator(indicator);

        if(isSameYear(indicator.getMonth(), exist.getMonth())) {
            if(indicator.getMonth() != null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), new Timestamp(cmd.getMonth()));
                if(statistic == null) {
                    statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    statistic.setStartTime(getDayBegin(cal));
                    statistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()).subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()).subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if(statistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && statistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                    }
                }
            }
        } else {
            if(indicator.getMonth() == null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), exist.getMonth());
                if(statistic != null) {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if(statistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && statistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(statistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                    }
                }
            } else if(exist.getMonth() == null) {
                CustomerEconomicIndicatorStatistic statistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
                if(statistic == null) {
                    statistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    statistic.setStartTime(getDayBegin(cal));
                    statistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(statistic);
                } else {
                    BigDecimal tax = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(statistic);
                }
            } else {
                CustomerEconomicIndicatorStatistic existStatistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), exist.getMonth());
                if(existStatistic != null) {
                    BigDecimal tax = existStatistic.getTaxPayment() == null ? BigDecimal.ZERO : existStatistic.getTaxPayment();
                    existStatistic.setTaxPayment(tax.subtract(exist.getTaxPayment() == null ? BigDecimal.ZERO : exist.getTaxPayment()));
                    BigDecimal turnover = existStatistic.getTurnover() == null ? BigDecimal.ZERO : existStatistic.getTurnover();
                    existStatistic.setTurnover(turnover.subtract(exist.getTurnover() == null ? BigDecimal.ZERO : exist.getTurnover()));
                    if(existStatistic.getTaxPayment().compareTo(BigDecimal.ZERO) <= 0
                            && existStatistic.getTurnover().compareTo(BigDecimal.ZERO) <= 0) {
                        enterpriseCustomerProvider.deleteCustomerEconomicIndicatorStatistic(existStatistic);
                    } else {
                        enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(existStatistic);
                    }
                }
                CustomerEconomicIndicatorStatistic newStatistic = enterpriseCustomerProvider.listCustomerEconomicIndicatorStatisticsByCustomerIdAndMonth(cmd.getCustomerId(), indicator.getMonth());
                if(newStatistic == null) {
                    newStatistic = ConvertHelper.convert(indicator, CustomerEconomicIndicatorStatistic.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Timestamp(cmd.getMonth()));
                    newStatistic.setStartTime(getDayBegin(cal));
                    newStatistic.setEndTime(getDayEnd(cal));
                    enterpriseCustomerProvider.createCustomerEconomicIndicatorStatistic(newStatistic);
                } else {
                    BigDecimal tax = newStatistic.getTaxPayment() == null ? BigDecimal.ZERO : newStatistic.getTaxPayment();
                    newStatistic.setTaxPayment(tax.add(indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment()));
                    BigDecimal turnover = newStatistic.getTurnover() == null ? BigDecimal.ZERO : newStatistic.getTurnover();
                    newStatistic.setTurnover(turnover.add(indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover()));
                    enterpriseCustomerProvider.updateCustomerEconomicIndicatorStatistic(newStatistic);
                }
            }

        }
    }

    @Override
    public void updateCustomerInvestment(UpdateCustomerInvestmentCommand cmd) {
        CustomerInvestment exist = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
        if(cmd.getInvestmentTime() != null) {
            investment.setInvestmentTime(new Timestamp(cmd.getInvestmentTime()));
        }
        investment.setCreateTime(exist.getCreateTime());
        investment.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerInvestment(investment);
    }

    @Override
    public void createCustomerCertificate(CreateCustomerCertificateCommand cmd) {
        CustomerCertificate certificate = ConvertHelper.convert(cmd, CustomerCertificate.class);
        if(cmd.getRegisteDate() != null) {
            certificate.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        enterpriseCustomerProvider.createCustomerCertificate(certificate);
    }

    @Override
    public void deleteCustomerCertificate(DeleteCustomerCertificateCommand cmd) {
        CustomerCertificate certificate = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerCertificate(certificate);
    }

    private CustomerCertificate checkCustomerCertificate(Long id, Long customerId) {
        CustomerCertificate certificate = enterpriseCustomerProvider.findCustomerCertificateById(id);
        if(certificate == null || !certificate.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(certificate.getStatus()))) {
            LOGGER.error("enterprise customer certificate is not exist or active. id: {}, certificate: {}", id, certificate);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer certificate is not exist or active");
        }
        return certificate;
    }

    @Override
    public CustomerCertificateDTO getCustomerCertificate(GetCustomerCertificateCommand cmd) {
        CustomerCertificate certificate = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        return ConvertHelper.convert(certificate, CustomerCertificateDTO.class);
    }

    @Override
    public List<CustomerCertificateDTO> listCustomerCertificates(ListCustomerCertificatesCommand cmd) {
        List<CustomerCertificate> certificates = enterpriseCustomerProvider.listCustomerCertificatesByCustomerId(cmd.getCustomerId());
        if(certificates != null && certificates.size() > 0) {
            return certificates.stream().map(certificate -> {
                return ConvertHelper.convert(certificate, CustomerCertificateDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerCertificate(UpdateCustomerCertificateCommand cmd) {
        CustomerCertificate exist = checkCustomerCertificate(cmd.getId(), cmd.getCustomerId());
        CustomerCertificate certificate = ConvertHelper.convert(cmd, CustomerCertificate.class);
        if(cmd.getRegisteDate() != null) {
            certificate.setRegisteDate(new Timestamp(cmd.getRegisteDate()));
        }
        certificate.setCreateTime(exist.getCreateTime());
        certificate.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerCertificate(certificate);
    }

    @Override
    public void createCustomerDepartureInfo(CreateCustomerDepartureInfoCommand cmd) {
        CustomerDepartureInfo departureInfo = ConvertHelper.convert(cmd, CustomerDepartureInfo.class);
        if(cmd.getReviewTime() != null) {
            departureInfo.setReviewTime(new Timestamp(cmd.getReviewTime()));
        }
        enterpriseCustomerProvider.createCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void createCustomerEntryInfo(CreateCustomerEntryInfoCommand cmd) {
        CustomerEntryInfo entryInfo = ConvertHelper.convert(cmd, CustomerEntryInfo.class);
        if(cmd.getContractEndDate() != null) {
            entryInfo.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
        }
        if(cmd.getContractStartDate() != null) {
            entryInfo.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
        }
        enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
    }

    @Override
    public void deleteCustomerDepartureInfo(DeleteCustomerDepartureInfoCommand cmd) {
        CustomerDepartureInfo departureInfo = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void deleteCustomerEntryInfo(DeleteCustomerEntryInfoCommand cmd) {
        CustomerEntryInfo entryInfo = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerEntryInfo(entryInfo);
    }

    private CustomerEntryInfo checkCustomerEntryInfo(Long id, Long customerId) {
        CustomerEntryInfo entryInfo = enterpriseCustomerProvider.findCustomerEntryInfoById(id);
        if(entryInfo == null || !entryInfo.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(entryInfo.getStatus()))) {
            LOGGER.error("enterprise customer entryInfo is not exist or active. id: {}, certificate: {}", id, entryInfo);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer entryInfo is not exist or active");
        }
        return entryInfo;
    }

    private CustomerDepartureInfo checkCustomerDepartureInfo(Long id, Long customerId) {
        CustomerDepartureInfo departureInfo = enterpriseCustomerProvider.findCustomerDepartureInfoById(id);
        if(departureInfo == null || !departureInfo.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(departureInfo.getStatus()))) {
            LOGGER.error("enterprise customer departureInfo is not exist or active. id: {}, departureInfo: {}", id, departureInfo);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_CERTIFICATE_NOT_EXIST,
                    "customer departureInfo is not exist or active");
        }
        return departureInfo;
    }

    @Override
    public CustomerDepartureInfoDTO getCustomerDepartureInfo(GetCustomerDepartureInfoCommand cmd) {
        CustomerDepartureInfo departureInfo = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        return convertCustomerDepartureInfoDTO(departureInfo, cmd.getCommunityId());
    }

    @Override
    public CustomerEntryInfoDTO getCustomerEntryInfo(GetCustomerEntryInfoCommand cmd) {
        CustomerEntryInfo entryInfo = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        if(entryInfo.getAddressId() != null) {

        }
        return convertCustomerEntryInfoDTO(entryInfo);
    }

    @Override
    public List<CustomerDepartureInfoDTO> listCustomerDepartureInfos(ListCustomerDepartureInfosCommand cmd) {
        List<CustomerDepartureInfo> departureInfos = enterpriseCustomerProvider.listCustomerDepartureInfos(cmd.getCustomerId());
        if(departureInfos != null && departureInfos.size() > 0) {
            return departureInfos.stream().map(departureInfo -> {
                return convertCustomerDepartureInfoDTO(departureInfo, cmd.getCommunityId());
            }).collect(Collectors.toList());
        }
        return null;
    }

    private CustomerDepartureInfoDTO convertCustomerDepartureInfoDTO(CustomerDepartureInfo departureInfo, Long communityId) {
        CustomerDepartureInfoDTO dto = ConvertHelper.convert(departureInfo, CustomerDepartureInfoDTO.class);

        if(dto.getDepartureDirectionId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(departureInfo.getNamespaceId(), communityId, dto.getDepartureDirectionId());
            if(scopeFieldItem != null) {
                dto.setDepartureDirectionName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getDepartureNatureId() != null) {
            ScopeFieldItem scopeFieldItem = fieldService.findScopeFieldItemByFieldItemId(departureInfo.getNamespaceId(), communityId, dto.getDepartureNatureId());
            if(scopeFieldItem != null) {
                dto.setDepartureNatureName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    private CustomerEntryInfoDTO convertCustomerEntryInfoDTO(CustomerEntryInfo entryInfo) {
        CustomerEntryInfoDTO dto = ConvertHelper.convert(entryInfo, CustomerEntryInfoDTO.class);

        if(dto.getAddressId() != null) {
            Address address = addressProvider.findAddressById(dto.getAddressId());
            if(address != null) {
                dto.setAddressName(address.getAddress());
            }
        }

        return dto;
    }

    @Override
    public List<CustomerEntryInfoDTO> listCustomerEntryInfos(ListCustomerEntryInfosCommand cmd) {
        List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider.listCustomerEntryInfos(cmd.getCustomerId());
        if(entryInfos != null && entryInfos.size() > 0) {
            return entryInfos.stream().map(entryInfo -> {
                return convertCustomerEntryInfoDTO(entryInfo);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public void updateCustomerDepartureInfo(UpdateCustomerDepartureInfoCommand cmd) {
        CustomerDepartureInfo exist = checkCustomerDepartureInfo(cmd.getId(), cmd.getCustomerId());
        CustomerDepartureInfo departureInfo = ConvertHelper.convert(cmd, CustomerDepartureInfo.class);
        if(cmd.getReviewTime() != null) {
            departureInfo.setReviewTime(new Timestamp(cmd.getReviewTime()));
        }
        departureInfo.setCreateTime(exist.getCreateTime());
        departureInfo.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerDepartureInfo(departureInfo);
    }

    @Override
    public void updateCustomerEntryInfo(UpdateCustomerEntryInfoCommand cmd) {
        CustomerEntryInfo exist = checkCustomerEntryInfo(cmd.getId(), cmd.getCustomerId());
        CustomerEntryInfo entryInfo = ConvertHelper.convert(cmd, CustomerEntryInfo.class);
        if(cmd.getContractEndDate() != null) {
            entryInfo.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
        }
        if(cmd.getContractStartDate() != null) {
            entryInfo.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
        }
        entryInfo.setCreateTime(exist.getCreateTime());
        entryInfo.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerEntryInfo(entryInfo);
    }

    @Override
    public CustomerIndustryStatisticsResponse listCustomerIndustryStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        CustomerIndustryStatisticsResponse response = new CustomerIndustryStatisticsResponse();
        List<CustomerIndustryStatisticsDTO> dtos = new ArrayList<>();
        Map<Long, Long> industries = enterpriseCustomerProvider.listEnterpriseCustomerIndustryByCommunityId(cmd.getCommunityId());
        response.setCustomerTotalCount(0L);
        industries.forEach((categoryId, count) -> {
            CustomerIndustryStatisticsDTO dto = new CustomerIndustryStatisticsDTO();
            dto.setCorpIndustryItemId(categoryId);
            dto.setCustomerCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), categoryId);
            if(item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setCustomerTotalCount(response.getCustomerTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerIntellectualPropertyStatisticsResponse listCustomerIntellectualPropertyStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });
        CustomerIntellectualPropertyStatisticsResponse response = new CustomerIntellectualPropertyStatisticsResponse();
        response.setPropertyTotalCount(0L);
        List<CustomerIntellectualPropertyStatisticsDTO> dtos = new ArrayList<>();
        Long trademarks = enterpriseCustomerProvider.countTrademarksByCustomerIds(customerIds);
        if(trademarks != null && trademarks != 0) {
            response.setPropertyTotalCount(response.getPropertyTotalCount() + trademarks);

            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("商标");
            dto.setPropertyCount(trademarks);
            dtos.add(dto);
        }

        Long certificates = enterpriseCustomerProvider.countCertificatesByCustomerIds(customerIds);
        if(certificates != null && certificates != 0) {
            response.setPropertyTotalCount(response.getPropertyTotalCount() + certificates);

            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("证书");
            dto.setPropertyCount(certificates);
            dtos.add(dto);
        }

        Map<Long, Long> properties = enterpriseCustomerProvider.listCustomerPatentsByCustomerIds(customerIds);
        properties.forEach((categoryId, count) -> {
            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), categoryId);
            if(item != null) {
                dto.setPropertyType(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setPropertyTotalCount(response.getPropertyTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerProjectStatisticsResponse listCustomerProjectStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });

        CustomerProjectStatisticsResponse response = new CustomerProjectStatisticsResponse();
        List<CustomerProjectStatisticsDTO> dtos = new ArrayList<>();
        response.setProjectTotalAmount(BigDecimal.ZERO);
        response.setProjectTotalCount(0L);

        Map<Long, CustomerProjectStatisticsDTO> statistics = enterpriseCustomerProvider.listCustomerApplyProjectsByCustomerIds(customerIds);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listCustomerProjectStatistics customer ids : {}, statistics: {}", customerIds, StringHelper.toJsonString(statistics));
        }

        statistics.forEach((itemId, statistic) -> {
            CustomerProjectStatisticsDTO dto = statistic;
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), itemId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), itemId);
            if(item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setProjectTotalAmount(response.getProjectTotalAmount().add(dto.getProjectAmount()));
            response.setProjectTotalCount(response.getProjectTotalCount() + dto.getProjectCount());
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerSourceStatisticsResponse listCustomerSourceStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        CustomerSourceStatisticsResponse response = new CustomerSourceStatisticsResponse();
        List<CustomerSourceStatisticsDTO> dtos = new ArrayList<>();
        Map<Long, Long> sources = enterpriseCustomerProvider.listEnterpriseCustomerSourceByCommunityId(cmd.getCommunityId());
        response.setCustomerTotalCount(0L);
        sources.forEach((categoryId, count) -> {
            CustomerSourceStatisticsDTO dto = new CustomerSourceStatisticsDTO();
            dto.setSourceItemId(categoryId);
            dto.setCustomerCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), categoryId);
            if(item != null) {
                dto.setItemName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setCustomerTotalCount(response.getCustomerTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public CustomerTalentStatisticsResponse listCustomerTalentStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
        });

        CustomerTalentStatisticsResponse response = new CustomerTalentStatisticsResponse();
        Map<Long, Long> talents = enterpriseCustomerProvider.listCustomerTalentCountByCustomerIds(customerIds);
        List<CustomerTalentStatisticsDTO> dtos = new ArrayList<>();
        response.setMemberTotalCount(0L);
        talents.forEach((categoryId, count) -> {
            CustomerTalentStatisticsDTO dto = new CustomerTalentStatisticsDTO();
            dto.setTalentCategoryId(categoryId);
            dto.setCustomerMemberCount(count);
//            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
            ScopeFieldItem item = fieldService.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), cmd.getCommunityId(), categoryId);
            if(item != null) {
                dto.setCategoryName(item.getItemDisplayName());
            }
            dtos.add(dto);
            response.setMemberTotalCount(response.getMemberTotalCount() + count);
        });
        response.setDtos(dtos);
        return response;
    }

    @Override
    public EnterpriseCustomerStatisticsDTO listEnterpriseCustomerStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        EnterpriseCustomerStatisticsDTO dto = new EnterpriseCustomerStatisticsDTO();
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomerByCommunity(cmd.getCommunityId());
        dto.setCustomerCount(customers.size() & 0xFFFFFFFFL);
        dto.setCustomerMemberCount(0L);

        List<Long> customerIds = new ArrayList<>();
        customers.forEach(customer -> {
            customerIds.add(customer.getId());
            int members = customer.getCorpEmployeeAmount() == null ? 0 : customer.getCorpEmployeeAmount();
            dto.setCustomerMemberCount(dto.getCustomerMemberCount() + members);
        });

        List<CustomerEconomicIndicator> indicators =enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerIds(customerIds);
        dto.setTotalTurnover(BigDecimal.ZERO);
        dto.setTotalTaxAmount(BigDecimal.ZERO);
        indicators.forEach(indicator -> {
            BigDecimal turnover = indicator.getTurnover() == null ? BigDecimal.ZERO : indicator.getTurnover();
            BigDecimal taxPayment = indicator.getTaxPayment() == null ? BigDecimal.ZERO : indicator.getTaxPayment();
            dto.setTotalTurnover(dto.getTotalTurnover().add(turnover));
            dto.setTotalTaxAmount(dto.getTotalTaxAmount().add(taxPayment));
        });

        return dto;
    }

    @Override
    public ListCustomerAnnualStatisticsResponse listCustomerAnnualStatistics(ListCustomerAnnualStatisticsCommand cmd) {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        ListCustomerAnnualStatisticsResponse response = new ListCustomerAnnualStatisticsResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if(cmd.getPageAnchor() != null) {
            locator.setAnchor(cmd.getPageAnchor());
        }
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<CustomerAnnualStatisticDTO> dtos = enterpriseCustomerProvider.listCustomerAnnualStatistics(cmd.getCommunityId(), now, locator, pageSize,
                cmd.getTurnoverMinimum(), cmd.getTurnoverMaximum(), cmd.getTaxPaymentMinimum(), cmd.getTaxPaymentMaximum());
        response.setStatisticDTOs(dtos);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    private Boolean isSameYear(Timestamp newMonth, Timestamp oldMonth) {
        if(newMonth == null && oldMonth == null) {
            return true;
        } else {
            if(newMonth != null && oldMonth != null) {
                Calendar newCal = Calendar.getInstance();
                newCal.setTime(newMonth);

                Calendar oldCal = Calendar.getInstance();
                oldCal.setTime(oldMonth);
                if(newCal.get(Calendar.YEAR) == oldCal.get(Calendar.YEAR)) {
                    return true;
                }
            }
        }
        return false;
    }
    private Timestamp getDayBegin(Calendar cal) {
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getDayEnd(Calendar cal) {
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DATE, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getMonth(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }
    @Override
    public ListCustomerAnnualDetailsResponse listCustomerAnnualDetails(ListCustomerAnnualDetailsCommand cmd) {
        ListCustomerAnnualDetailsResponse response = new ListCustomerAnnualDetailsResponse();
        List<CustomerEconomicIndicator> economicIndicators = enterpriseCustomerProvider.listCustomerEconomicIndicatorsByCustomerId(cmd.getCustomerId(), new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime()));
        if(economicIndicators != null && economicIndicators.size() > 0) {
            Map<Timestamp, MonthStatistics> monthStatisticsMap = new HashMap<>();
            economicIndicators.forEach(economicIndicator -> {
                Timestamp month = getMonth(economicIndicator.getMonth());
                MonthStatistics statistic = monthStatisticsMap.get(month);
                if(statistic == null) {
                    statistic = new MonthStatistics();
                    statistic.setMonth(month.getTime());
                    statistic.setTaxPayment(economicIndicator.getTaxPayment());
                    statistic.setTurnover(economicIndicator.getTurnover());
                } else {
                    BigDecimal taxPayment = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    statistic.setTaxPayment(taxPayment.add(economicIndicator.getTaxPayment() == null ? BigDecimal.ZERO : economicIndicator.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    statistic.setTurnover(turnover.add(economicIndicator.getTurnover() == null ? BigDecimal.ZERO : economicIndicator.getTurnover()));
                }
                monthStatisticsMap.put(month, statistic);
            });
            List<MonthStatistics> statistics = new ArrayList<>();
            monthStatisticsMap.forEach((month, statistic) -> {
                statistics.add(statistic);
            });
            response.setStatistics(statistics);

            //季度
            Map<Integer, QuarterStatistics> quarterStatisticsMap = new HashMap<>();
            monthStatisticsMap.forEach((timestamp, statistic) -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(timestamp);
                int month = cal.get(Calendar.MONTH);
                int quarter = 0;
                if(month <= 3) {
                    quarter = YearQuarter.THE_FIRST_QUARTER.getCode();
                } else if(month > 3 && month <= 6) {
                    quarter = YearQuarter.THE_SECOND_QUARTER.getCode();
                } else if(month > 6 && month <= 9) {
                    quarter = YearQuarter.THE_THIRD_QUARTER.getCode();
                } else if(month > 9 && month <= 12) {
                    quarter = YearQuarter.THE_FOURTH_QUARTER.getCode();
                }

                QuarterStatistics qs = quarterStatisticsMap.get(quarter);
                if(qs == null) {
                    qs = new QuarterStatistics();
                    qs.setQuarter(quarter);
                    qs.setTaxPayment(statistic.getTaxPayment());
                    qs.setTurnover(statistic.getTurnover());

                } else {
                    BigDecimal taxPayment = statistic.getTaxPayment() == null ? BigDecimal.ZERO : statistic.getTaxPayment();
                    qs.setTaxPayment(taxPayment.add(qs.getTaxPayment() == null ? BigDecimal.ZERO : qs.getTaxPayment()));
                    BigDecimal turnover = statistic.getTurnover() == null ? BigDecimal.ZERO : statistic.getTurnover();
                    qs.setTurnover(turnover.add(qs.getTurnover() == null ? BigDecimal.ZERO : qs.getTurnover()));
                }
                quarterStatisticsMap.put(quarter, qs);
            });

            List<QuarterStatistics> quarterStatisticses = new ArrayList<>();
            quarterStatisticsMap.forEach((quarter, statistic) -> {
                quarterStatisticses.add(statistic);
            });
            response.setQuarterStatisticses(quarterStatisticses);
        }
        return response;
    }

    @Override
    public void syncEnterpriseCustomers(SyncCustomersCommand cmd) {
        if(cmd.getNamespaceId() == 999971) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId()).tryEnter(()-> {
                ExecutorUtil.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(cmd.getCommunityId() == null) {
                                zjgkOpenService.syncEnterprises("0", null);
                            } else {
                                Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                                if(community != null) {
                                    zjgkOpenService.syncEnterprises("0", community.getNamespaceCommunityToken());
                                }

                            }
                        }catch (Exception e){
                            LOGGER.error("syncEnterpriseCustomers error.", e);
                        }
                    }
                });
            });
        } else {
            this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_ENTERPRISE_CUSTOMER.getCode() + cmd.getNamespaceId() + cmd.getCommunityId()).tryEnter(()-> {
                ExecutorUtil.submit(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                            if(community == null) {
                                return;
                            }
                            String version = enterpriseCustomerProvider.findLastEnterpriseCustomerVersionByCommunity(cmd.getNamespaceId(), community.getId());
                            CustomerHandle customerHandle = PlatformContext.getComponent(CustomerHandle.CUSTOMER_PREFIX + cmd.getNamespaceId());
                            if(customerHandle != null) {
                                customerHandle.syncEnterprises("1", version, community.getNamespaceCommunityToken());
                            }

                        }catch (Exception e){
                            LOGGER.error("syncEnterpriseCustomers error.", e);
                        }
                    }
                });
            });
        }

    }

    @Override
    public void syncIndividualCustomers(SyncCustomersCommand cmd) {
        if(cmd.getNamespaceId() == 999971) {
            ExecutorUtil.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(cmd.getCommunityId() == null) {
                            zjgkOpenService.syncIndividuals("0", null);
                        } else {
                            Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                            if(community != null) {
                                zjgkOpenService.syncIndividuals("0", community.getNamespaceCommunityToken());
                            }
                        }
                    }catch (Exception e){
                        LOGGER.error("syncIndividualCustomers error.", e);
                    }
                }
            });

        }
    }

    
	@Override
	public List<CustomerTrackingDTO> listCustomerTrackings(ListCustomerTrackingsCommand cmd) {
		List<CustomerTracking> trackings = enterpriseCustomerProvider.listCustomerTrackingsByCustomerId(cmd.getCustomerId());
        if(trackings != null && trackings.size() > 0) {
            return trackings.stream().map(tracking -> {
                return convertCustomerTrackingDTO(tracking);
            }).collect(Collectors.toList());
        }
        return null;
	}

	@Override
	public CustomerTrackingDTO getCustomerTracking(GetCustomerTrackingCommand cmd) {
		CustomerTracking tracking = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTrackingDTO(tracking);
	}

	private CustomerTrackingDTO convertCustomerTrackingDTO(CustomerTracking talent) {
		CustomerTrackingDTO dto = ConvertHelper.convert(talent, CustomerTrackingDTO.class);
        if(dto.getTrackingType() != null) {
        	String trackingTypeName = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, Integer.parseInt(dto.getTrackingType().toString()) , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
        	dto.setTrackingTypeName(trackingTypeName);
        }
        if(dto.getTrackingUid() != null) {
        	OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(dto.getTrackingUid());
        	if(null != detail && null != detail.getContactName()){
        		dto.setTrackingUidName(detail.getContactName());
        	}else{
        		UserInfo userInfo = userService.getUserInfo(dto.getTrackingUid());
        		if(userInfo != null){
            		dto.setTrackingUidName(userInfo.getNickName());
            	}
        	}
        }
        List<String> urlList = new ArrayList<>();
        List<String> uriList = new ArrayList<>();
        if(StringUtils.isNotEmpty(talent.getContentImgUri())){
        	String[] uriArray = talent.getContentImgUri().split(",");
        	for(String  uri : uriArray){
        		uriList.add(uri);
        		String contentUrl = contentServerService.parserUri(uri, EntityType.CUSTOMER_TRACKING.getCode(), dto.getId());
        		urlList.add(contentUrl);
        	}
        }
        dto.setContentImgUriList(uriList);
        dto.setContentImgUrlList(urlList);
        return dto;
	}

	@Override
	public CustomerTrackingDTO updateCustomerTracking(UpdateCustomerTrackingCommand cmd) {
		CustomerTracking exist = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
		CustomerTracking tracking = ConvertHelper.convert(cmd, CustomerTracking.class);
		if(cmd.getTrackingTime() != null){
			tracking.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
		}
		tracking.setCreateTime(exist.getCreateTime());
		tracking.setCreatorUid(exist.getCreatorUid());
		if(null != cmd.getContentImgUri()){
			tracking.setContentImgUri(cmd.getContentImgUri());
		}
        enterpriseCustomerProvider.updateCustomerTracking(tracking);
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
        customer.setLastTrackingTime(tracking.getTrackingTime());
        //更细客户表的最后跟进时间
        enterpriseCustomerProvider.updateCustomerLastTrackingTime(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
        return ConvertHelper.convert(tracking, CustomerTrackingDTO.class);
	}

	@Override
	public void deleteCustomerTracking(DeleteCustomerTrackingCommand cmd) {
		CustomerTracking tracking = checkCustomerTracking(cmd.getId(), cmd.getCustomerId());
	    enterpriseCustomerProvider.deleteCustomerTracking(tracking);
	}

	@Override
	public void createCustomerTracking(CreateCustomerTrackingCommand cmd) {
		
		EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getCustomerId());
		CustomerTracking tracking = ConvertHelper.convert(cmd, CustomerTracking.class);
		if(cmd.getTrackingTime() != null){
			tracking.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
		}
        enterpriseCustomerProvider.createCustomerTracking(tracking);
        customer.setLastTrackingTime(tracking.getTrackingTime());
        //更细客户表的最后跟进时间
        enterpriseCustomerProvider.updateCustomerLastTrackingTime(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
	}
	
	 private CustomerTracking checkCustomerTracking(Long id, Long customerId) {
		CustomerTracking tracking = enterpriseCustomerProvider.findCustomerTrackingById(id);
        if(tracking == null || !tracking.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(tracking.getStatus()))) {
            LOGGER.error("enterprise customer tracking is not exist or inactive. id: {}, tracking: {}", id, tracking);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRACKING_NOT_EXIST,
                    "customer tracking is not exist or inactive");
        }
        return tracking;
	}

	@Override
	public List<CustomerTrackingPlanDTO> listCustomerTrackingPlans(ListCustomerTrackingPlansCommand cmd) {
		List<CustomerTrackingPlan> plans = enterpriseCustomerProvider.listCustomerTrackingPlans(cmd.getCustomerId());
        if(plans != null && plans.size() > 0) {
            return plans.stream().map(plan -> {
                return convertCustomerTrackingPlanDTO(plan);
            }).collect(Collectors.toList());
        }
        return null;
	}

	@Override
	public CustomerTrackingPlanDTO getCustomerTrackingPlan(GetCustomerTrackingPlanCommand cmd) {
		CustomerTrackingPlan plan = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
        CustomerTrackingPlanDTO  customerTrackingPlanDTO = convertCustomerTrackingPlanDTO(plan);
        //更新状态为已读
        enterpriseCustomerProvider.updateTrackingPlanReadStatus(cmd.getId());
        return customerTrackingPlanDTO;
	}

	private CustomerTrackingPlanDTO convertCustomerTrackingPlanDTO(CustomerTrackingPlan plan) {
		CustomerTrackingPlanDTO dto = ConvertHelper.convert(plan, CustomerTrackingPlanDTO.class);
        if(dto.getTrackingType() != null) {
        	String trackingTypeName = localeTemplateService.getLocaleTemplateString(CustomerTrackingTemplateCode.SCOPE, Integer.parseInt(dto.getTrackingType().toString()) , UserContext.current().getUser().getLocale(), new HashMap<>(), "");
        	dto.setTrackingTypeName(trackingTypeName);
        }
        return dto;
	}

	@Override
	public CustomerTrackingPlanDTO updateCustomerTrackingPlan(UpdateCustomerTrackingPlanCommand cmd) {
		CustomerTrackingPlan exist = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
		CustomerTrackingPlan plan = ConvertHelper.convert(cmd, CustomerTrackingPlan.class);
		if(cmd.getTrackingTime() != null){
			plan.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
		}
		plan.setNotifyStatus(TrackingPlanNotifyStatus.INVAILD.getCode());
		if(cmd.getNotifyTime() != null){
			plan.setNotifyTime(new Timestamp(cmd.getNotifyTime()));
			plan.setNotifyStatus(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode());
		}
		plan.setCreateTime(exist.getCreateTime());
		plan.setCreatorUid(exist.getCreatorUid());
        enterpriseCustomerProvider.updateCustomerTrackingPlan(plan);
        return ConvertHelper.convert(plan, CustomerTrackingPlanDTO.class);
	}

	@Override
	public void deleteCustomerTrackingPlan(DeleteCustomerTrackingPlanCommand cmd) {
		CustomerTrackingPlan plan = checkCustomerTrackingPlan(cmd.getId(), cmd.getCustomerId());
	    enterpriseCustomerProvider.deleteCustomerTrackingPlan(plan);
	}
	
	 private CustomerTrackingPlan checkCustomerTrackingPlan(Long id, Long customerId) {
		CustomerTrackingPlan plan = enterpriseCustomerProvider.findCustomerTrackingPlanById(id);
        if(plan == null || !plan.getCustomerId().equals(customerId)
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(plan.getStatus()))) {
            LOGGER.error("enterprise customer tracking plan is not exist or inactive. id: {}, plan: {}", id, plan);
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_TRACKING_NOT_EXIST,
                    "customer tracking plan is not exist or inactive");
        }
        return plan;
	}

	@Override
	public void createCustomerTrackingPlan(CreateCustomerTrackingPlanCommand cmd) {
		CustomerTrackingPlan plan = ConvertHelper.convert(cmd, CustomerTrackingPlan.class);
		if(cmd.getTrackingTime() != null){
			plan.setTrackingTime(new Timestamp(cmd.getTrackingTime()));
		}
		plan.setNotifyStatus(TrackingPlanNotifyStatus.INVAILD.getCode());
		if(cmd.getNotifyTime() != null ){
			plan.setNotifyTime(new Timestamp(cmd.getNotifyTime()));
			plan.setNotifyStatus(TrackingPlanNotifyStatus.WAITING_FOR_SEND_OUT.getCode());
		}
		plan.setReadStatus(TrackingPlanReadStatus.UNREAD.getCode());
        enterpriseCustomerProvider.createCustomerTrackingPlan(plan);
	}

	private void saveCustomerEvent(int i,  EnterpriseCustomer customer, EnterpriseCustomer exist) {
		enterpriseCustomerProvider.saveCustomerEvent(i,customer,exist);
	}
	
	@Override
	public List<CustomerEventDTO> listCustomerEvents(ListCustomerEventsCommand cmd) {
		List<CustomerEvent> events = enterpriseCustomerProvider.listCustomerEvents(cmd.getCustomerId());
        if(events != null && events.size() > 0) {
            return events.stream().map(event -> {
                return convertCustomerEventDTO(event);
            }).collect(Collectors.toList());
        }
        return null;
	}

	private CustomerEventDTO convertCustomerEventDTO(CustomerEvent event) {
		CustomerEventDTO dto = ConvertHelper.convert(event, CustomerEventDTO.class);
        if(dto.getCreatorUid() != null) {
        	OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(dto.getCreatorUid());
        	if(null != detail && null != detail.getContactName()){
        		dto.setCreatorName(detail.getContactName());
        	}        	
        }
        return dto;
	}

	@Override
	public void allotEnterpriseCustomer(AllotEnterpriseCustomerCommand cmd) {
		//checkPrivilege();
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        customer.setTrackingUid(cmd.getTrackingUid());
        if(cmd.getTrackingUid() != -1){
	        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(cmd.getTrackingUid());
	    	if(null != detail && null != detail.getContactName()){
	    		customer.setTrackingName(detail.getContactName());
	    	}
        }
        enterpriseCustomerProvider.allotEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
	}

	@Override
	public void giveUpEnterpriseCustomer(GiveUpEnterpriseCustomerCommand cmd) {
		EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
		//查看当前用户是否和跟进人一致
		if(null == customer.getTrackingUid() || !(customer.getTrackingUid().toString()).equals(UserContext.currentUserId()== null ? "" : UserContext.currentUserId().toString())){
			LOGGER.error("enterprise customer do not contains trackingUid or not the same uid. id: {}, customer: {} ,current:{}", cmd.getId(), customer,UserContext.currentUserId());
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_NOT_EXIST,
                        "enterprise customer do not contains trackingUid or not the same uid");
		}
		customer.setTrackingUid(-1l);
		customer.setTrackingName(null);
        enterpriseCustomerProvider.giveUpEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);
	}

	@Override
	public ListNearbyEnterpriseCustomersCommandResponse listNearbyEnterpriseCustomers(ListNearbyEnterpriseCustomersCommand cmd) {
		ListNearbyEnterpriseCustomersCommandResponse resp = new ListNearbyEnterpriseCustomersCommandResponse();
        List<EnterpriseCustomerDTO> results = new ArrayList<EnterpriseCustomerDTO>();

        if (cmd.getLatitude() == null || cmd.getLongitude() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, latitude and longitude have to be both specified or neigher");


        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<EnterpriseCustomerDTO> customers = this.enterpriseCustomerProvider.findEnterpriseCustomersByDistance(cmd, locator, pageSize +1);
        if (customers != null) {
        	for(EnterpriseCustomerDTO customer : customers){
        		results.add(customer);
        	}
        }
        if (results != null && results.size() > pageSize) {
        	resp.setNextPageAnchor(results.get(results.size() - 1).getId());
        	results.remove(results.size() - 1);
        }
        if (results != null) {
            resp.setDtos(results);
        }
        return resp;
	}

	
	//每15分钟执行一次，找出待n~n+15分钟内提醒时间的跟进计划 
	@Scheduled(cron="0 0/15 * * * ?")
	@Override
	public void trackingPlanWarningSchedule() {
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			LOGGER.info("trackingPlanWarningSchedule is running...");
			//使用tryEnter方法可以防止分布式部署时重复执行
			coordinationProvider.getNamedLock(CoordinationLocks.TRACKING_PLAN_WARNING_SCHEDULE.getCode()).tryEnter(() -> {
				Date now = DateHelper.currentGMTTime();
				Timestamp queryStartTime = new Timestamp(now.getTime());
				Timestamp queryEndTime = new Timestamp(now.getTime() + 900 * 1000);
				List<CustomerTrackingPlan> plans = enterpriseCustomerProvider.listWaitNotifyTrackingPlans(queryStartTime,queryEndTime);
				if(null != plans && plans.size() > 0){
					plans.forEach(plan ->{
						pushPlanIntoEnqueue(plan);
					});
				}
			});
		}
	}

	private void pushPlanIntoEnqueue(CustomerTrackingPlan plan) {
		 Map<String, Object> map = new HashMap<>();
         map.put("trackingPlanId", plan.getId());
		if (plan.getNotifyTime().getTime() > (DateHelper.currentGMTTime().getTime() + 10L)) {
            scheduleProvider.scheduleSimpleJob(
                    queueDelay + plan.getId(),
                    queueDelay + plan.getId(),
                    new Date(plan.getNotifyTime().getTime()),
                    TrackingPlanNotifytJob.class,
                    map
            );

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("pushTrackingPlanNotify delayedEnqueue trackingPlan = {}", plan);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("pushTrackingPlanNotify noDelayedenqueue trackingPlan = {}", plan);
            }
            scheduleProvider.scheduleSimpleJob(
                    queueNoDelay + plan.getId(),
                    queueNoDelay + plan.getId(),
                    new Date(System.currentTimeMillis() + 1000),
                    TrackingPlanNotifytJob.class,
                    map
            );
        }
	}

	@Override
	public void processTrackingPlanNotify(CustomerTrackingPlan plan) {
		Long userId = plan.getCreatorUid();
		LOGGER.info("processTrackingPlanNotify userId:{}", userId);
        if(userId != null && null != plan.getTrackingTime()) {
            String taskName = plan.getTitle() == null ? "" : plan.getTitle();
            Timestamp time = plan.getTrackingTime();
            String customerName = plan.getCustomerName();
            int code = TrackingNotifyTemplateCode.TRACKING_NEARLY_REACH_NOTIFY;
            String scope = TrackingNotifyTemplateCode.SCOPE;
            String locale = "zh_CN";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("processPmNotifyRecord, userId={}, recordId={}", userId, plan.getId());
            }
            TrackingNotifyLog log = new TrackingNotifyLog();
            log.setCustomerType(plan.getCustomerType());
            log.setCustomerId(plan.getCustomerId());
            log.setReceiverId(userId);
            String notifyTextForApplicant = getMessage(customerName,taskName, time, scope, locale, code);
            sendMessageToUser(userId, notifyTextForApplicant);
            log.setNotifyText(notifyTextForApplicant);
            enterpriseCustomerProvider.createTrackingNotifyLog(log);
        }
	}

	private String getMessage(String  customerName , String taskName, Timestamp time, String scope, String locale, int code) {
		Map<String, Object> notifyMap = new HashMap<String, Object>();
        notifyMap.put("taskName", taskName);
        notifyMap.put("time", timeToStr(time));
        notifyMap.put("customerName", customerName);
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
        return notifyTextForApplicant;
	}
	
	 private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
	 }
	 
	 private void sendMessageToUser(Long userId, String content) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
	                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	 }

	@Override
	public List<List<CustomerTrackingPlanDTO>> listCustomerTrackingPlansByDate(ListCustomerTrackingPlansByDateCommand cmd) {
		List<List<CustomerTrackingPlanDTO>> planList = new ArrayList<>();
		List<CustomerTrackingPlanDTO> todayPlan = new ArrayList<>();
		List<CustomerTrackingPlanDTO> tomorrowPlan = new ArrayList<>();
		List<CustomerTrackingPlanDTO> passOrFuturePlan = new ArrayList<>();
		List<CustomerTrackingPlan> plans = null;
		Long todayFirst = getTodayFirstTimestamp();
		Long todayLast = getTodayLastTimestamp();
		Long tomorrowLast = getTomorrowLastTimestamp();
		//历史记录
		if(StringUtils.isNotEmpty(cmd.getGetHistory()) && "1".equals(cmd.getGetHistory())){
			plans = enterpriseCustomerProvider.listCustomerTrackingPlansByDate(cmd,todayFirst);
			plans.forEach(plan -> {
				passOrFuturePlan.add(convertCustomerTrackingPlanDTO(plan));
			});
			Collections.sort(passOrFuturePlan);
			planList.add(passOrFuturePlan);
			return planList;
		}
		//今天 & 明天  & 以后
		plans = enterpriseCustomerProvider.listCustomerTrackingPlansByDate(cmd , new Timestamp(todayFirst));
		plans.forEach(plan -> {
			if(null != plan.getTrackingTime()){
				Long trackingTime = plan.getTrackingTime().getTime();
				if(trackingTime <= todayLast && trackingTime >= todayFirst){
					todayPlan.add(convertCustomerTrackingPlanDTO(plan));
				}else if(trackingTime > tomorrowLast){
					passOrFuturePlan.add(convertCustomerTrackingPlanDTO(plan));
				}else{
					tomorrowPlan.add(convertCustomerTrackingPlanDTO(plan));
				}
			}
		});
		Collections.sort(todayPlan);
		Collections.sort(tomorrowPlan);
		Collections.sort(passOrFuturePlan);
		planList.add(todayPlan);
		planList.add(tomorrowPlan);
		planList.add(passOrFuturePlan);
		return planList;
	}

	private Long getTodayFirstTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	private Long getTodayLastTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
	
	private Long getTomorrowLastTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, +1);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
	
	
}
