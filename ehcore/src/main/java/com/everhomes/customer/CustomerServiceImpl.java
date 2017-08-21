package com.everhomes.customer;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.customer.*;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.varField.ModuleName;
import com.everhomes.rest.warehouse.ImportWarehouseMaterialDataDTO;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setNamespaceId(UserContext.getCurrentNamespaceId());
        if(cmd.getCorpEntryDate() != null) {
            customer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        enterpriseCustomerProvider.createEnterpriseCustomer(customer);

        enterpriseCustomerSearcher.feedDoc(customer);
    }

    @Override
    public void updateEnterpriseCustomer(UpdateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        EnterpriseCustomer updateCustomer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        updateCustomer.setNamespaceId(customer.getNamespaceId());
        updateCustomer.setOrganizationId(customer.getOrganizationId());
        updateCustomer.setCreateTime(customer.getCreateTime());
        updateCustomer.setCreatorUid(customer.getCreatorUid());
        if(cmd.getCorpEntryDate() != null) {
            updateCustomer.setCorpEntryDate(new Timestamp(cmd.getCorpEntryDate()));
        }
        enterpriseCustomerProvider.updateEnterpriseCustomer(updateCustomer);

        enterpriseCustomerSearcher.feedDoc(customer);
    }

    @Override
    public void deleteEnterpriseCustomer(DeleteEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = checkEnterpriseCustomer(cmd.getId());
        enterpriseCustomerProvider.deleteEnterpriseCustomer(customer);

        enterpriseCustomerSearcher.feedDoc(customer);
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
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    private List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, List<ImportEnterpriseCustomerDataDTO> list, Long userId){
        List<ImportFileResultLog<ImportEnterpriseCustomerDataDTO>> errorDataLogs = new ArrayList<>();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

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

            ScopeFieldItem scopeCategoryFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getCategoryItemName());
            if(scopeCategoryFieldItem != null) {
                customer.setCategoryItemId(scopeCategoryFieldItem.getItemId());
            }
            ScopeFieldItem scopeLevelFieldItem = fieldProvider.findScopeFieldItemByDisplayName(cmd.getNamespaceId(), ModuleName.ENTERPRISE_CUSTOMER.getName(), str.getLevelItemName());
            if(scopeLevelFieldItem != null) {
                customer.setLevelItemId(scopeLevelFieldItem.getItemId());
            }


            customer.setNamespaceId(namespaceId);
            customer.setCreatorUid(userId);
            enterpriseCustomerProvider.createEnterpriseCustomer(customer);
            enterpriseCustomerSearcher.feedDoc(customer);
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
        EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
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
        enterpriseCustomerProvider.createCustomerCommercial(commercial);
    }

    @Override
    public void createCustomerPatent(CreateCustomerPatentCommand cmd) {
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        enterpriseCustomerProvider.createCustomerPatent(patent);
    }

    @Override
    public void createCustomerTrademark(CreateCustomerTrademarkCommand cmd) {
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
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
        commercial.setCreateTime(exist.getCreateTime());
        commercial.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerCommercial(commercial);
    }

    @Override
    public void updateCustomerPatent(UpdateCustomerPatentCommand cmd) {
        CustomerPatent exist = checkCustomerPatent(cmd.getId(), cmd.getCustomerId());
        CustomerPatent patent = ConvertHelper.convert(cmd, CustomerPatent.class);
        patent.setCreateTime(exist.getCreateTime());
        patent.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerPatent(patent);
    }

    @Override
    public void updateCustomerTrademark(UpdateCustomerTrademarkCommand cmd) {
        CustomerTrademark exist = checkCustomerTrademark(cmd.getId(), cmd.getCustomerId());
        CustomerTrademark trademark = ConvertHelper.convert(cmd, CustomerTrademark.class);
        trademark.setCreateTime(exist.getCreateTime());
        trademark.setCreateUid(exist.getCreateUid());
        enterpriseCustomerProvider.updateCustomerTrademark(trademark);
    }
}
