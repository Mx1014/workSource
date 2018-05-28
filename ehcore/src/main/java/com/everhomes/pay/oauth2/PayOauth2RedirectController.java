package com.everhomes.pay.oauth2;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pay/oauth2/redirect")
public class PayOauth2RedirectController extends PayOauth2BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PayOauth2RedirectController.class);

    @Autowired
    private PayOauth2Service payOauth2Service;

    @RequestMapping("/logon")
    @ResponseBody
    public Object getLogonToken(
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "state", required = true) String state,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        return payOauth2Service.redirectLogon(code, state);
    }
}
