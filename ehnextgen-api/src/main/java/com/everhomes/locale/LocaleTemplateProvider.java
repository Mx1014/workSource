// @formatter:off
package com.everhomes.locale;

import java.util.List;

import com.everhomes.listing.ListingLocator;

public interface LocaleTemplateProvider {
    LocaleTemplate findLocaleTemplateById(long id);
    
    LocaleTemplate findLocaleTemplateByScope(Integer namespaceId, String scope, int code, String locale);
    
    void updateLocaleTemplate(LocaleTemplate template);
    
    void deleteLocaleTemplate(LocaleTemplate template);

	void createLocaleTemplate(LocaleTemplate template);
	
	/**
	 * Added by Janson 20161227
	 * @param locator
	 * @param namespaceId
	 * @param scope
	 * @param locale
	 * @param keyword
	 * @param count
	 * @return
	 */
	List<LocaleTemplate> listLocaleTemplatesByScope(ListingLocator locator,
			Integer namespaceId, String scope, String locale, String keyword,
			int count);
}
