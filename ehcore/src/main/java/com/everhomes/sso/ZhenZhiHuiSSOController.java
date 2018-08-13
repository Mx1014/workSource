// @formatter:off
package com.everhomes.sso;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/zhenzhihuisso")
public class ZhenZhiHuiSSOController extends ControllerBase{
    @Autowired
    private ZhenZhiHuiSSO zhenZhiHuiSSO;
    /**
     * <b>URL: /zhenzhihuisso/sso</b>
     * <p>圳智慧单点登录</p>
     */
    @RequestMapping("sso")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse share(HttpServletRequest request, HttpServletResponse response) {
        zhenZhiHuiSSO.ssoservice(request,response);
        return new RestResponse();
    }
}
