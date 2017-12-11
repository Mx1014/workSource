package com.everhomes.module;

import com.everhomes.portal.ServiceModuleApp;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.server.schema.tables.pojos.EhReflectionServiceModuleApps;
import com.everhomes.techpark.punch.PunchDayLog;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionServiceModuleApp extends EhReflectionServiceModuleApps{
    private static final long serialVersionUID = -4995895050475309252L;

    public ReflectionServiceModuleApp() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    protected static ServiceModuleAppDTO getServiceModuleAppDTO(ReflectionServiceModuleApp app){
        ServiceModuleAppDTO dto = new ServiceModuleAppDTO();
        dto.setId(app.getActiveAppId());
        dto.setName(app.getName());
        dto.setModuleId(app.getModuleId());
        dto.setInstanceConfig(app.getInstanceConfig());
        dto.setMenuId(app.getMenuId());
        return dto;
    }

    protected static ServiceModuleApp getServiceModuleApp(ReflectionServiceModuleApp app){
        ServiceModuleApp dto = ConvertHelper.convert(app, ServiceModuleApp.class);
        dto.setId(app.getActiveAppId());
        return dto;
    }
}
