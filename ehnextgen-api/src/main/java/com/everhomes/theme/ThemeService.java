package com.everhomes.theme;


import com.everhomes.rest.theme.GetThemeColorCommand;
import com.everhomes.rest.theme.ThemeColorDTO;

public interface ThemeService {

	ThemeColorDTO getThemeColor(GetThemeColorCommand cmd);
}
