package com.everhomes.locale;

import java.io.IOException;

import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.namespace.Namespace;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class LocaleTemplateServiceImpl implements LocaleTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleTemplateServiceImpl.class);

    @Autowired
    private LocaleTemplateProvider provider;
    
    private StringTemplateLoader templateLoader;
    
    private Configuration templateConfig;
    
    public LocaleTemplateServiceImpl() {
        templateLoader = new StringTemplateLoader();
        templateConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        templateConfig.setTemplateLoader(templateLoader);
        templateConfig.setTemplateUpdateDelay(0);
    }
    
    @Override
    public LocaleTemplate getLocalizedTemplate(Integer namespaceId, String scope, int code, String locale) {
        if(namespaceId == null) {
            namespaceId = Namespace.DEFAULT_NAMESPACE;
        }
        return this.provider.findLocaleTemplateByScope(namespaceId, scope, code, locale);
    }

    @Override
    public String getLocaleTemplateString(String scope, int code, String locale, Object model, String defaultValue) {
        return getLocaleTemplateString(Namespace.DEFAULT_NAMESPACE, scope, code, locale, model, defaultValue);
    }
    
    @Override
    public String getLocaleTemplateString(Integer namespaceId, String scope, int code, String locale, Object model, String defaultValue) {
        String templateKey = getTemplateKey(scope, code, locale);
        try {
            Template freeMarkerTemplate = null;
            try {
                templateConfig.getTemplate(templateKey, "UTF8");
            }catch(Exception e) {

            }
            if(freeMarkerTemplate == null) {
                LocaleTemplate template = getLocalizedTemplate(namespaceId, scope, code, locale);
                if(template != null) {
                    String templateText = template.getText();
                    templateLoader.putTemplate(templateKey, templateText);
                    freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
                } 
            }
            
            if(freeMarkerTemplate != null) {
                return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, model);
            }
        } catch(Exception e) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Invalid locale template, scope=" + scope + ", code=" + code + ", locale=" + locale, e);
            }
        }
        
        return defaultValue;
    }
    
    public void updateLocaleTemplate(Integer namespaceId, long id, String description, String templateText) {
        User user = UserContext.current().getUser();
        LocaleTemplate template = this.provider.findLocaleTemplateById(id);
        if(template ==  null) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Template not found, userId=" + user.getId() + ", templateId=" + id);
            }
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, template not found");
        }
        
        if(description != null) {
            template.setDescription(description);
        }
        if(templateText != null) {
            template.setText(templateText);
        }
        this.provider.updateLocaleTemplate(template);
        
        if(templateText != null) {
            String templateKey = getTemplateKey(template.getScope(), template.getCode(), template.getLocale());
            templateLoader.putTemplate(templateKey, templateText);
        }
    }
    
    public void deleteLocaleTemplate(Integer namespaceId, long id) {
        User user = UserContext.current().getUser();
        LocaleTemplate template = this.provider.findLocaleTemplateById(id);
        if(template ==  null) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Template not found, userId=" + user.getId() + ", templateId=" + id);
            }
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid parameter, template not found");
        }
        
        this.provider.deleteLocaleTemplate(template);

        try {
            String templateKey = getTemplateKey(template.getScope(), template.getCode(), template.getLocale());
            templateConfig.removeTemplateFromCache(templateKey);
        } catch (IOException e) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to delete template from freemarker's cache, userId=" + user.getId() + ", templateId=" + id);
            }
        }
    }
    
    public void clearLocaleTemplateCache() {
        templateConfig.clearTemplateCache();
    }
    
    private String getTemplateKey(String scope, int code, String locale) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(scope);
        strBuilder.append(".");
        strBuilder.append(locale);
        strBuilder.append(".");
        strBuilder.append(code);
        
        return strBuilder.toString();
    }

}
