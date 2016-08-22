package com.everhomes.locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.locale.GetLocalizedStringCommand;
import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/locale")
public class LocaleStringController extends ControllerBase {

    @Autowired
    private LocaleStringService localeService;
    
    @RequireAuthentication(false)
    @RequestMapping("getLocalizedString")
    @RestReturn(value=String.class)
    public RestResponse isTrustedApp(@Valid GetLocalizedStringCommand cmd) {
        String locale = cmd.getLocale();
        if(locale == null || locale.isEmpty())
            locale = "en_US";
        
        return new RestResponse(localeService.getLocalizedString(cmd.getScope(), cmd.getCode(), locale, cmd.getDefaultValue()));
    }
    
}
