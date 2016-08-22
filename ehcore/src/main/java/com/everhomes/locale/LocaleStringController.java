package com.everhomes.locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.locale.GetLocalizedStringCommand;
import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;
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
    
    /**
     * 
     * <b>URL: /locale/listLocaleTemplate</b>
     * <p>模板列表</p>
     */
    @RequestMapping("listLocaleTemplate")
    @RestReturn(ListLocaleTemplateResponse.class)
    public RestResponse listLocaleTemplate(ListLocaleTemplateCommand cmd){
    	return new RestResponse(localeService.listLocaleTemplate(cmd));
    }
    
    /**
     * 
     * <b>URL: /locale/updateLocaleTemplate</b>
     * <p>更新模板</p>
     */
    @RequestMapping("listLocaleTemplate")
    @RestReturn(LocaleTemplateDTO.class)
    public RestResponse updateLocaleTemplate(UpdateLocaleTemplateCommand cmd){
    	return new RestResponse(localeService.updateLocaleTemplate(cmd));
    }
}
