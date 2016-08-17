// @formatter:off
package com.everhomes.locale;

import com.everhomes.rest.locale.CreateLocaleTemplateCommand;
import com.everhomes.rest.locale.CreateLocaleTemplateResponse;

public interface LocaleTemplateService {
    LocaleTemplate getLocalizedTemplate(Integer namespaceId, String scope, int code, String locale);
    
    String getLocaleTemplateString(Integer namespaceId, String scope, int code, String locale, Object model, String defaultValue);
    
    String getLocaleTemplateString(String scope, int code, String locale, Object model, String defaultValue);
    
    void updateLocaleTemplate(Integer namespaceId, long id, String description, String templateText);
    
    void deleteLocaleTemplate(Integer namespaceId, long id);
    
    void clearLocaleTemplateCache();

	CreateLocaleTemplateResponse createLocaleTemplate(CreateLocaleTemplateCommand cmd);
}
