package com.everhomes.openapi.ruian;

import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.openapi.ruian.MallcooAutoLoginCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 */
@RestController
@RequestMapping("/openapi/ruian")
public class RuianOpenController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuianOpenController.class);

    @Autowired
    private RuianOpenService ruianOpenService;
    
    /**
     * <b>URL: /openapi/ruian/autoLogin2Mallcoo</b>
     * <p>转跳到猫酷并自动登录</p>
     */
    @RequestMapping(value = "autoLogin2Mallcoo", method = RequestMethod.GET)
	public Object autoLogin2Mallcoo(MallcooAutoLoginCommand mallcooAutoLoginCommand, HttpServletRequest request, HttpServletResponse response) {
    	String communityId = mallcooAutoLoginCommand.getCommunityId();
    	assert (communityId != null && !communityId.isEmpty());
    	HttpHeaders httpHeaders = ruianOpenService.buildRedirectHeader(communityId);
    	return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

}
