package com.everhomes.openapi.ruian;

import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.openapi.ruian.MallcooAutoLoginCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 */
@RestController
@RequestMapping("/openapi/ruian")
// @RequireAuthentication(false)
public class RuianOpenController extends ControllerBase {

    @Autowired
    private RuianOpenService ruianOpenService;
    
    /**
     * <b>URL: /openapi/ruian/autoLogin2Mallcoo</b>
     * <p>转跳到猫酷并自动登录</p>
     */
    @RequestMapping(value = "autoLogin2Mallcoo")
	public Object autoLogin2Mallcoo(MallcooAutoLoginCommand mallcooAutoLoginCommand) {
    	HttpHeaders httpHeaders = ruianOpenService.buildRedirectHeader(mallcooAutoLoginCommand);
    	return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

}
