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
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @Override
    public void createEnterpriseCustomer(CreateEnterpriseCustomerCommand cmd) {
        EnterpriseCustomer customer = ConvertHelper.convert(cmd, EnterpriseCustomer.class);
        customer.setNamespaceId(UserContext.getCurrentNamespaceId());
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
    public ImportFileTaskDTO importEnterpriseCustomerData(ImportEnterpriseCustomerDataCommand cmd, MultipartFile mfile, Long userId) {

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
//                    List<ImportWarehouseMaterialDataDTO> datas = handleImportWarehouseMaterialsData(resultList);
//                    if(datas.size() > 0){
//                        //设置导出报错的结果excel的标题
//                        response.setTitle(datas.get(0));
//                        datas.remove(0);
//                    }
//                    List<ImportFileResultLog<ImportWarehouseMaterialDataDTO>> results = importWarehouseMaterialsData(cmd, datas, userId);
//                    response.setTotalCount((long)datas.size());
//                    response.setFailCount((long)results.size());
//                    response.setLogs(results);
                    return response;
                }
            }, task);

        } catch (IOException e) {
            LOGGER.error("File can not be resolved...");
            e.printStackTrace();
        }
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
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
}
