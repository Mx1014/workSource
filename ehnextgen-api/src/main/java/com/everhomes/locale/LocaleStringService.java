// @formatter:off
package com.everhomes.locale;

import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;

public interface LocaleStringService {
    String getLocalizedString(String scope, String code, String locale, String defaultValue);

	ListLocaleTemplateResponse listLocaleTemplate(ListLocaleTemplateCommand cmd);

	LocaleTemplateDTO updateLocaleTemplate(UpdateLocaleTemplateCommand cmd);
}
