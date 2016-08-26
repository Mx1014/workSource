// @formatter:off
package com.everhomes.locale;

public interface LocaleTemplateProvider {
    LocaleTemplate findLocaleTemplateById(long id);
    
    LocaleTemplate findLocaleTemplateByScope(Integer namespaceId, String scope, int code, String locale);
    
    void updateLocaleTemplate(LocaleTemplate template);
    
    void deleteLocaleTemplate(LocaleTemplate template);

	void createLocaleTemplate(LocaleTemplate template);
}
