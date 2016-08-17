package com.everhomes.locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.locale.CreateLocaleTemplateCommand;
import com.everhomes.rest.locale.CreateLocaleTemplateResponse;
import com.everhomes.rest.locale.GetLocalizedStringCommand;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/locale")
public class LocaleStringController extends ControllerBase {

    @Autowired
    private LocaleStringService localeService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @RequireAuthentication(false)
    @RequestMapping("getLocalizedString")
    @RestReturn(value=String.class)
    public RestResponse isTrustedApp(@Valid GetLocalizedStringCommand cmd) {
        String locale = cmd.getLocale();
        if(locale == null || locale.isEmpty())
            locale = "en_US";
        
        return new RestResponse(localeService.getLocalizedString(cmd.getScope(), cmd.getCode(), locale, cmd.getDefaultValue()));
    }
    
    /**
     * 
     * <b>URL: /locale/createLocaleTemplate</b>
     * <p>创建一条消息模板</p>
     */
    @RequestMapping("createLocaleTemplate")
    @RestReturn(CreateLocaleTemplateResponse.class)
    public RestResponse createLocaleTemplate(CreateLocaleTemplateCommand cmd){
    	return new RestResponse(localeTemplateService.createLocaleTemplate(cmd));
    }
}
