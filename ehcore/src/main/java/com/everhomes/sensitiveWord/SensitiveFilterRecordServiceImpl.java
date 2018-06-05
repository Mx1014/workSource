// @formatter:off
package com.everhomes.sensitiveWord;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.sensitiveWord.admin.GetSensitiveFilterRecordAdminCommand;
import com.everhomes.rest.sensitiveWord.admin.ListSensitiveFilterRecordAdminCommand;
import com.everhomes.rest.sensitiveWord.admin.ListSensitiveFilterRecordAdminResponse;
import com.everhomes.rest.sensitiveWord.admin.SensitiveFilterRecordAdminDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SensitiveFilterRecordServiceImpl implements SensitiveFilterRecordService{
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilterRecordService.class);

    @Autowired
    private SensitiveFilterRecordProvider sensitiveFilterRecordProvider;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public ListSensitiveFilterRecordAdminResponse listSensitiveRecord(ListSensitiveFilterRecordAdminCommand cmd) {
        if(null == cmd.getNamespaceId()) {
            LOGGER.error("namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId cannot be null.");
        }
        ListSensitiveFilterRecordAdminResponse response = new ListSensitiveFilterRecordAdminResponse();
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
        List<SensitiveFilterRecord> list = sensitiveFilterRecordProvider.listSensitiveFilterRecord(cmd.getNamespaceId(), cmd.getPageAnchor(), cmd.getPageSize());
        int size = list.size();
        if (size >0) {
            if (size <= cmd.getPageSize()) {
                response.setNextPageAnchor(null);
            }else {
                list.remove(size-1);
                response.setNextPageAnchor(list.get(list.size() - 1).getId());
            }
            response.setDtos(list.stream().map(r -> {
                SensitiveFilterRecordAdminDTO sensitiveFilterRecordAdminDTO = ConvertHelper.convert(r, SensitiveFilterRecordAdminDTO.class);
                sensitiveFilterRecordAdminDTO.setPublishTime(r.getPublishTime().toString());
                return  sensitiveFilterRecordAdminDTO;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public SensitiveFilterRecordAdminDTO getSensitiveFilterRecord(GetSensitiveFilterRecordAdminCommand cmd) {
        if (cmd.getId() == null) {
            LOGGER.error("ID cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ID cannot be null.");
        }
        SensitiveFilterRecord sensitiveFilterRecord = this.sensitiveFilterRecordProvider.getSensitiveFilterRecord(cmd.getId());
        SensitiveFilterRecordAdminDTO sensitiveFilterRecordAdminDTO = ConvertHelper.convert(sensitiveFilterRecord,SensitiveFilterRecordAdminDTO.class);
        ServiceModuleApp serviceModuleApp = this.serviceModuleAppService.findReleaseServiceModuleAppByOriginId(sensitiveFilterRecord.getAppId());
        if (serviceModuleApp != null) {
            sensitiveFilterRecordAdminDTO.setAppName(serviceModuleApp.getName());
        }
        return sensitiveFilterRecordAdminDTO;
    }
}
