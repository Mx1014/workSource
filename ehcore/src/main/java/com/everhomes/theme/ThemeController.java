// @formatter:off
package com.everhomes.theme;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.talent.*;
import com.everhomes.rest.theme.GetThemeColorCommand;
import com.everhomes.rest.theme.ThemeColorDTO;
import com.everhomes.talent.TalentService;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/theme")
public class ThemeController extends ControllerBase {
	

	@Autowired
	private ThemeService themeService;

	/**
	 * <p>获取当前域空间的色值</p>
	 * <b>URL: /theme/getThemeColor</b>
	 */
	@RequestMapping("getThemeColor")
	@RestReturn(ThemeColorDTO.class)
	@RequireAuthentication(false)
	public RestResponse getThemeColor(GetThemeColorCommand cmd){
		ThemeColorDTO dto = themeService.getThemeColor(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}