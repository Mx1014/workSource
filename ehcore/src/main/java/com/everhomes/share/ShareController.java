package com.everhomes.share;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.share.ShareCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/share")
public class ShareController extends ControllerBase {

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

	@Autowired
	private ShareService shareService;
 
	/**
	 * <b>URL: /share/share</b>
	 * <p>分享成功后告诉后台，后台好做一些操作</p>
	 */
	@RequestMapping("share")
	@RestReturn(String.class)
	public RestResponse share(@Valid ShareCommand cmd) {
        shareService.share(cmd);
        return SUCCESS;
	}
}