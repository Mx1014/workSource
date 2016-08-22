package com.everhomes.locale.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

public class LocaleTemplateAdminController extends ControllerBase {

    @Autowired
    private LocaleStringService localeService;
    
    /**
     * 
     * <b>URL: /admin/locale/listLocaleTemplate</b>
     * <p>模板列表</p>
     */
    @RequestMapping("listLocaleTemplate")
    @RestReturn(ListLocaleTemplateResponse.class)
    public RestResponse listLocaleTemplate(ListLocaleTemplateCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(localeService.listLocaleTemplate(cmd));
    }
    
    /**
     * 
     * <b>URL: /admin/locale/updateLocaleTemplate</b>
     * <p>更新模板</p>
     */
    @RequestMapping("listLocaleTemplate")
    @RestReturn(LocaleTemplateDTO.class)
    public RestResponse updateLocaleTemplate(UpdateLocaleTemplateCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
    	return new RestResponse(localeService.updateLocaleTemplate(cmd));
    }
}
