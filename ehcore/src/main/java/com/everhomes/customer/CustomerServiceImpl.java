package com.everhomes.customer;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contract.ContractService;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.openapi.ZJGKOpenServiceImpl;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListEnterpriseCustomerContractsCommand;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ModuleName;
import com.everhomes.rest.warehouse.ImportWarehouseMaterialDataDTO;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeFieldItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public EnterpriseCustomerDTO createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setNamespaceId(UserContext.getCurrentNamespaceId());
        if(cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);

        OrganizationDTO organizationDTO = createOrganization(customer);
        customer.setOrganizationId(organizationDTO.getId());
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);

        return convertToDTO(customer);
    }

    private EnterpriseCustomerDTO convertToDTO(EnterpriseCustomer customer) {
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
        ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
        if(categoryItem != null) {
            dto.setCategoryItemName(categoryItem.getItemDisplayName());
        }
        ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
        if(levelItem != null) {
            dto.setLevelItemName(levelItem.getItemDisplayName());
        }

        return dto;
    }

    private OrganizationDTO createOrganization(EnterpriseCustomer customer) {
        CreateEnterpriseCommand command = new CreateEnterpriseCommand();
        command.setName(customer.getName());
        command.setDisplayName(customer.getNickName());
        command.setNamespaceId(customer.getNamespaceId());
        command.setAvatar(customer.getCorpLogoUri());
        command.setDescription(customer.getCorpDescription());
        command.setCommunityId(customer.getCommunityId());
        command.setMemberCount(customer.getCorpEmployeeAmount() == null ? 0 : customer.getCorpEmployeeAmount() + 0L);
        command.setContactor(customer.getContactName());
        command.setContactsPhone(customer.getContactPhone());
        command.setEntries(customer.getContactMobile());
        command.setAddress(customer.getContactAddress());
        return organizationService.createEnterprise(command);
    }

    @Override
    public EnterpriseCustomerDTO updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        EnterpriseCustomer updateCustomer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        updateCustomer.setNamespaceId(customer.getNamespaceId());
        updateCustomer.setCommunityId(customer.getCommunityId());
        updateCustomer.setOrganizationId(customer.getOrganizationId());
        updateCustomer.setCreateTime(customer.getCreateTime());
        updateCustomer.setCreatorUid(customer.getCreatorUid());
        if(cmd.getCorpEntryDate() != null) {
            updateCustomer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        updateCustomer.setStatus(CommonStatus.ACTIVE.getCode());
        enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);
        enterpriseCustomerSearcher.feedDoc(customer);

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
            command.setContactor(updateCustomer.getContactName());
            command.setContactsPhone(updateCustomer.getContactPhone());
            command.setEntries(updateCustomer.getContactMobile());
            command.setAddress(updateCustomer.getContactAddress());
            organizationService.updateEnterprise(command, false);
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
        customer.setStatus(CommonStatus.INACTIVE.getCode());
        enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
        enterpriseCustomerSearcher.feedDoc(customer);

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
            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            if(scopeCategoryFieldItem == null) {
                LOGGER.error("enterpirse customer category is null, data = {}", str);
                log.setData(str);
                log.setErrorLog("enterpirse customer category is null");
                log.setCode(CustomerErrorCode.ERROR_CUSTOMER_CATEGORY_IS_NULL);
                errorDataLogs.add(log);
                continue;
            }
            customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
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
            Map<Long, List<String>> orgAdminAccounts = new HashMap<>();
            if (!orgAdminAccounts.get(organizationDTO.getId()).contains(str.getContactMobile())) {
                if (!org.springframework.util.StringUtils.isEmpty(str.getContactMobile())) {
                    CreateOrganizationAdminCommand createOrganizationAdminCommand = new CreateOrganizationAdminCommand();
                    createOrganizationAdminCommand.setOrganizationId(organizationDTO.getId());
                    createOrganizationAdminCommand.setContactToken(str.getContactMobile());
                    createOrganizationAdminCommand.setContactName(str.getContactName());
                    rolePrivilegeService.createOrganizationAdmin(createOrganizationAdminCommand, cmd.getNamespaceId());
                }
                orgAdminAccounts.get(organizationDTO.getId()).add(str.getContactMobile());
            }
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
        return convertCustomerTalentDTO(talent);
    }

    private CustomerTalentDTO convertCustomerTalentDTO(CustomerTalent talent) {
        CustomerTalentDTO dto = ConvertHelper.convert(talent, CustomerTalentDTO.class);
        if(dto.getGender() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getGender());
            if(scopeFieldItem != null) {
                dto.setGenderName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getReturneeFlag() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getReturneeFlag());
            if(scopeFieldItem != null) {
                dto.setReturneeFlagName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getAbroadItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getAbroadItemId());
            if(scopeFieldItem != null) {
                dto.setAbroadItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getDegreeItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getDegreeItemId());
            if(scopeFieldItem != null) {
                dto.setDegreeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getNationalityItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getNationalityItemId());
            if(scopeFieldItem != null) {
                dto.setNationalityItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getIndividualEvaluationItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getIndividualEvaluationItemId());
            if(scopeFieldItem != null) {
                dto.setIndividualEvaluationItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getTechnicalTitleItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(talent.getNamespaceId(), dto.getTechnicalTitleItemId());
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
                return convertCustomerTalentDTO(talent);
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
                || !CommonStatus.ACTIVE.equals(CommonStatus.fromCode(project.getStatus()))) {
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
        return convertCustomerApplyProjectDTO(project);
    }

    private CustomerApplyProjectDTO convertCustomerApplyProjectDTO(CustomerApplyProject project) {
        CustomerApplyProjectDTO dto = ConvertHelper.convert(project, CustomerApplyProjectDTO.class);

        if(dto.getProjectSource() != null) {
            String[] ids = dto.getProjectSource().split(",");
            LOGGER.info("project source: {}", ids);
            StringBuilder sb = new StringBuilder();
            for(String id : ids) {
                ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(project.getNamespaceId(), Long.valueOf(id));
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
        return convertCustomerCommercialDTO(commercial);
    }

    private CustomerCommercialDTO convertCustomerCommercialDTO(CustomerCommercial commercial) {
        CustomerCommercialDTO dto = ConvertHelper.convert(commercial, CustomerCommercialDTO.class);

        if(dto.getEnterpriseTypeItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getEnterpriseTypeItemId());
            if(scopeFieldItem != null) {
                dto.setEnterpriseTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getShareTypeItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getShareTypeItemId());
            if(scopeFieldItem != null) {
                dto.setShareTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getPropertyType() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(commercial.getNamespaceId(), dto.getPropertyType());
            if(scopeFieldItem != null) {
                dto.setPropertyTypeName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerPatentDTO getCustomerPatent(GetCustomerPatentCommand cmd) {
        CustomerPatent patent = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        return convertCustomerPatentDTO(patent);
    }

    private CustomerPatentDTO convertCustomerPatentDTO(CustomerPatent patent) {
        CustomerPatentDTO dto = ConvertHelper.convert(patent, CustomerPatentDTO.class);

        if(dto.getPatentStatusItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentStatusItemId());
            if(scopeFieldItem != null) {
                dto.setPatentStatusItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        if(dto.getPatentTypeItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(patent.getNamespaceId(), dto.getPatentTypeItemId());
            if(scopeFieldItem != null) {
                dto.setPatentTypeItemName(scopeFieldItem.getItemDisplayName());
            }
        }
        return dto;
    }

    @Override
    public CustomerTrademarkDTO getCustomerTrademark(GetCustomerTrademarkCommand cmd) {
        CustomerTrademark trademark = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        return convertCustomerTrademarkDTO(trademark);
    }

    private CustomerTrademarkDTO convertCustomerTrademarkDTO(CustomerTrademark trademark) {
        CustomerTrademarkDTO dto = ConvertHelper.convert(trademark, CustomerTrademarkDTO.class);

        if(dto.getTrademarkTypeItemId() != null) {
            ScopeFieldItem scopeFieldItem = fieldProvider.findScopeFieldItemByFieldItemId(trademark.getNamespaceId(), dto.getTrademarkTypeItemId());
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
                return convertCustomerApplyProjectDTO(project);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerCommercialDTO> listCustomerCommercials(ListCustomerCommercialsCommand cmd) {
        List<CustomerCommercial> commercials = enterpriseCustomerProvider.listCustomerCommercialsByCustomerId(cmd.getCustomerId());
        if(commercials != null && commercials.size() > 0) {
            return commercials.stream().map(commercial -> {
                return convertCustomerCommercialDTO(commercial);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerPatentDTO> listCustomerPatents(ListCustomerPatentsCommand cmd) {
        List<CustomerPatent> patents = enterpriseCustomerProvider.listCustomerPatentsByCustomerId(cmd.getCustomerId());
        if(patents != null && patents.size() > 0) {
            return patents.stream().map(patent -> {
                return convertCustomerPatentDTO(patent);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CustomerTrademarkDTO> listCustomerTrademarks(ListCustomerTrademarksCommand cmd) {
        List<CustomerTrademark> trademarks = enterpriseCustomerProvider.listCustomerTrademarksByCustomerId(cmd.getCustomerId());
        if(trademarks != null && trademarks.size() > 0) {
            return trademarks.stream().map(trademark -> {
                return convertCustomerTrademarkDTO(trademark);
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
        enterpriseCustomerProvider.createCustomerEconomicIndicator(indicator);
    }

    @Override
    public void createCustomerInvestment(CreateCustomerInvestmentCommand cmd) {
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
        enterpriseCustomerProvider.createCustomerInvestment(investment);
    }

    @Override
    public void deleteCustomerEconomicIndicator(DeleteCustomerEconomicIndicatorCommand cmd) {
        CustomerEconomicIndicator indicator = checkCustomerEconomicIndicator(cmd.getId(), cmd.getCustomerId());
        enterpriseCustomerProvider.deleteCustomerEconomicIndicator(indicator);
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
        indicator.setCreateTime(exist.getCreateTime());
        indicator.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerEconomicIndicator(indicator);
    }

    @Override
    public void updateCustomerInvestment(UpdateCustomerInvestmentCommand cmd) {
        CustomerInvestment exist = checkCustomerInvestment(cmd.getId(), cmd.getCustomerId());
        CustomerInvestment investment = ConvertHelper.convert(cmd, CustomerInvestment.class);
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
    public CustomerIndustryStatisticsResponse listCustomerIndustryStatistics(ListEnterpriseCustomerStatisticsCommand cmd) {
        CustomerIndustryStatisticsResponse response = new CustomerIndustryStatisticsResponse();
        List<CustomerIndustryStatisticsDTO> dtos = new ArrayList<>();
        Map<Long, Long> industries = enterpriseCustomerProvider.listEnterpriseCustomerIndustryByCommunityId(cmd.getCommunityId());
        response.setCustomerTotalCount(0L);
        industries.forEach((categoryId, count) -> {
            CustomerIndustryStatisticsDTO dto = new CustomerIndustryStatisticsDTO();
            dto.setCorpIndustryItemId(categoryId);
            dto.setCustomerCount(count);
            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
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
        if(trademarks != null) {
            response.setPropertyTotalCount(response.getPropertyTotalCount() + trademarks);

            CustomerIntellectualPropertyStatisticsDTO dto = new CustomerIntellectualPropertyStatisticsDTO();
            dto.setPropertyType("商标");
            dto.setPropertyCount(trademarks);
            dtos.add(dto);
        }

        Long certificates = enterpriseCustomerProvider.countCertificatesByCustomerIds(customerIds);
        if(certificates != null) {
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
            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
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
            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), itemId);
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
            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
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
            ScopeFieldItem item = fieldProvider.findScopeFieldItemByFieldItemId(cmd.getNamespaceId(), categoryId);
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
            BigDecimal taxAmount = indicator.getTotalTaxAmount() == null ? BigDecimal.ZERO : indicator.getTotalTaxAmount();
            dto.setTotalTurnover(dto.getTotalTurnover().add(turnover));
            dto.setTotalTaxAmount(dto.getTotalTaxAmount().add(taxAmount));
        });

        return dto;
    }

    @Override
    public void syncEnterpriseCustomers(SyncCustomersCommand cmd) {
        if(cmd.getNamespaceId() == 999971) {
            if(cmd.getCommunityId() == null) {
                zjgkOpenService.syncEnterprises("0", null);
            } else {
                Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                if(community != null) {
                    zjgkOpenService.syncEnterprises("0", community.getNamespaceCommunityToken());
                }

            }
        }

    }

    @Override
    public void syncIndividualCustomers(SyncCustomersCommand cmd) {
        if(cmd.getNamespaceId() == 999971) {
            if(cmd.getCommunityId() == null) {
                zjgkOpenService.syncIndividuals("0", null);
            } else {
                Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                if(community != null) {
                    zjgkOpenService.syncIndividuals("0", community.getNamespaceCommunityToken());
                }
            }
        }
    }
}
