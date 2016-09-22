package com.everhomes.wx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.util.RequireAuthentication;

/**
 * 由于要使拦截器工作，URL必须有serverContenxt(如evh），为了符合此规则，需要为微信申请授权及授权回调定义两个接口；
 * 配置了拦截器WebRequestWeixinInterceptor来拦截这两个接口，它的实现逻辑在拦截器中完成。而在controller定义
 * 这两个接口纯属是为了能够在日志中打印出符合标准 的日志，没有其它用途。
 * 
 * 没有特殊原因，不要在此controller增加其它接口。
 *
 */
@RestDoc(value = "WX Auth Controller", site = "core")
@RestController
@RequestMapping("/wxauth")
public class WXAuthController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(WXAuthController.class);
	/**
	 * <b>URL: /wxauth/authReq</b>
	 * <p>请求微信授权，逻辑由拦截器完成。定义此接口目的是为了打印日志。</p>
	 */
	@RequestMapping("authReq")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public String authReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return "Auth request OK";
	}
	/**
	 * <b>URL: /wx/authCallback</b>
	 * <p>微信授权后回调API，逻辑由拦截器完成。定义此接口目的是为了打印日志。</p>
	 */
	@RequestMapping("authCallback")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public String wxRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return "Auth callback OK";
	}
}
