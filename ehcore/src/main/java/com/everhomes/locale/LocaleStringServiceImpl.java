package com.everhomes.locale;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocaleStringServiceImpl implements LocaleStringService {

    @Autowired
    private LocaleStringProvider provider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Override
    public String getLocalizedString(String scope, String code, String locale, String defaultValue) {
        LocaleString localeText = this.provider.find(scope, code, locale);
        if(localeText != null)
            return localeText.getText();
        
        return defaultValue;
    }

	@Override
	public ListLocaleTemplateResponse listLocaleTemplate(ListLocaleTemplateCommand cmd) {
		Long page = null;
		if (cmd.getPageAnchor() == null || cmd.getPageAnchor() < 0) {
			page = 0L;
		}else {
			page = cmd.getPageAnchor();
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());;
		int from = Long.valueOf(page * pageSize).intValue();
		
		List<LocaleTemplateDTO> list = provider.listLocaleTemplate(from, pageSize+1, cmd.getNamespaceId(), cmd.getScope(), cmd.getCode(), cmd.getKeyword());
		
		Long nextPageAnchor = null;
		if (list != null && list.size() > pageSize) {
			list.remove(list.size()-1);
			nextPageAnchor = page + 1;
		}
		
		ListLocaleTemplateResponse response = new ListLocaleTemplateResponse();
		response.setNextPageAnchor(nextPageAnchor);
		response.setTemplateList(list);
		return response;
	}

	@Override
	public LocaleTemplateDTO updateLocaleTemplate(UpdateLocaleTemplateCommand cmd) {
		LocaleTemplate template = provider.findTemplateById(cmd.getId());
		if (template == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Not found template: cmd="+cmd);
		}
		template.setDescription(cmd.getDescription());
		template.setText(cmd.getText());
		
		provider.updateLocaleTemplate(template);
		
		return ConvertHelper.convert(template, LocaleTemplateDTO.class);
	}
}
