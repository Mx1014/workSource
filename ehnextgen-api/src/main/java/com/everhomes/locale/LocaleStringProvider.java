// @formatter:off
package com.everhomes.locale;

import com.everhomes.rest.locale.LocaleTemplateDTO;

import java.util.List;

public interface LocaleStringProvider {
    LocaleString find(String scope, String code, String locale);

    LocaleString findByText(String scope, String text, String locale);

	List<LocaleTemplateDTO> listLocaleTemplate(int from, int pageSize, Integer namespaceId, String scope, Integer code,
			String keyword);

	LocaleTemplate findTemplateById(Long id);

	void updateLocaleTemplate(LocaleTemplate template);

}
