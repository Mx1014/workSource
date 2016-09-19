// @formatter:off
package com.everhomes.locale;

import java.util.List;

import com.everhomes.rest.locale.LocaleTemplateDTO;

public interface LocaleStringProvider {
    LocaleString find(String scope, String code, String locale);
<<<<<<< HEAD
    LocaleString findByText(String scope, String text, String locale);
=======

	List<LocaleTemplateDTO> listLocaleTemplate(int from, int pageSize, Integer namespaceId, String scope, Integer code,
			String keyword);

	LocaleTemplate findTemplateById(Long id);

	void updateLocaleTemplate(LocaleTemplate template);
>>>>>>> 3.9.2
}
