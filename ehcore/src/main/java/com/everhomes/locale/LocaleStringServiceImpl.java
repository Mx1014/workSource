package com.everhomes.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocaleStringServiceImpl implements LocaleStringService {

    @Autowired
    private LocaleStringProvider provider;
    
    @Override
    public String getLocalizedString(String scope, String code, String locale, String defaultValue) {
        LocaleString localeText = this.provider.find(scope, code, locale);
        if(localeText != null)
            return localeText.getText();
        
        return defaultValue;
    }
}
