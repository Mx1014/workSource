package com.everhomes.configuration.admin;




import java.util.List;

import javax.validation.Valid;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Configuration controller", site="core")
@RestController
@RequestMapping("/admin/configuration")
public class ConfigurationAdminController extends ControllerBase {
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private DbProvider dbProvider;
    /**
     * <b>URL: /admin/configuration/updateConfiguration</b>
     * <p>更新配置</p>
     */
    @RequestMapping("updateBanner")
    @RestReturn(value=String.class)
    public RestResponse updateConfiguration(@Valid UpdateConfigurationAdminCommand cmd) {
        if(cmd.getName() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid configuration name paramter.");
        }
        configurationProvider.setValue(cmd.getName(), cmd.getValue());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/configuration/deleteConfiguration</b>
     * <p>删除配置</p>
     */
    @RequestMapping("deleteConfiguration")
    @RestReturn(value=String.class)
    public RestResponse deleteConfiguration(@Valid DeleteConfigurationAdminCommand cmd) {
        if(cmd.getName() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid configuration name paramter.");
        }
        configurationProvider.deleteValue(cmd.getName());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/configuration/listConfigurations</b>
     * <p>获取配置项信息</p>
     */
    @RequestMapping("listConfigurations")
    @RestReturn(value=ListConfigurationsAdminCommandResponse.class)
    public RestResponse listConfigurations(ListConfigurationsAdminCommand cmd){
        
        final long pageSize = cmd.getPageSize() == null ? this.configurationProvider.getIntValue("pagination.page.size", 
                AppConfig.DEFAULT_PAGINATION_PAGE_SIZE) : cmd.getPageSize();
        long pageOffset = cmd.getPageOffset() == null ? 1L : cmd.getPageOffset();
        long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        
        List<ConfigurationDTO> result = context.select().from("eh_configurations").limit((int)pageSize).offset((int)offset).fetch().map(r ->{
            ConfigurationDTO dto = new ConfigurationDTO();
            dto.setId((Integer)r.getValue("id"));
            dto.setName((String)r.getValue("name"));
            dto.setValue((String)r.getValue("value"));
            dto.setDescription((String)r.getValue("description"));
            return dto;
            
        });
        ListConfigurationsAdminCommandResponse response = new ListConfigurationsAdminCommandResponse();
        if(result != null && result.size() == pageSize){
            response.setNextPageOffset((int)pageOffset + 1);
        }
        response.setRequests(result);
        
        return new RestResponse(response);
    }
}
