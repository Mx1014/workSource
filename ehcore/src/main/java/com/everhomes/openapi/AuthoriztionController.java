package com.everhomes.openapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.authorization.AuthorizationThirdPartyRecord;
import com.everhomes.authorization.AuthorizationThirdPartyRecordProvider;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.DisclaimAddressCommand;
import com.everhomes.rest.user.UnrentFeedbackCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;

@RestDoc(value="authorization open Controller", site="core")
@RestController
@RequestMapping("/openapi/user")
public class AuthoriztionController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthoriztionController.class);

	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;
	
	@Autowired
	private AuthorizationThirdPartyRecordProvider authorizationThirdPartyRecordProvider;

	@Autowired
	private AppProvider appProvider;
	
    @Autowired
    private AddressService addressService;

	/**
	 * <b>URL: /openapi/unrentFeedback</b>
     * <p>退租调用接口</p>
	 */
	@RequestMapping("cancelAuthFeedback")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse unrentFeedback(@Valid UnrentFeedbackCommand cmd,HttpServletRequest request, HttpServletResponse response) {
		checkCmd(cmd);
		
		AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
		
		if(mapping == null || mapping.getNamespaceId() == null){
			LOGGER.error("appkey not mapping to namespace, mapping = {}, appKey = {}", mapping, cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}
		
		AuthorizationThirdPartyRecord record = authorizationThirdPartyRecordProvider.findAuthorizationThirdPartyRecordByPhone(cmd.getPhone(),cmd.getType(),mapping.getNamespaceId());
		
		UserContext.current().setUser(new User());
		UserContext.current().getUser().setId(record.getCreatorUid());
		UserContext.current().setNamespaceId(mapping.getNamespaceId());
		if(record != null && record.getResultJson() != null){
			ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			List<ZjgkResponse> list = entity.getResponse();
			if(list!=null){
				try{
					for (ZjgkResponse r : list) {
						//依次退租
						DisclaimAddressCommand disCmd = new DisclaimAddressCommand();
						disCmd.setAddressId(r.getAddressId());
						addressService.disclaimAddress(disCmd);
					}
				}catch(RuntimeErrorException e){
					LOGGER.error("disclaim address error, e = {}", e);
					throw RuntimeErrorException.errorWith("asset", 201,
							"退出失败,"+e.getMessage());
				}
			}
		}
		RestResponse r = new RestResponse();
		r.setErrorScope("asset");
		r.setErrorDetails("OK");
		r.setErrorDescription("OK");
		return r;
	}

	private void checkCmd(UnrentFeedbackCommand cmd) {
		if(cmd.getAppKey() == null
				|| cmd.getNonce() == null
				|| cmd.getPhone() == null
				|| cmd.getSignature() == null
				|| cmd.getTimestamp() == null
				|| cmd.getType() == null
				|| cmd.getCrypto() == null){
			LOGGER.error("cmd params = {}", cmd);
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}
		validateSign(cmd);
	}
	
	private void validateSign(UnrentFeedbackCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());
		if(app == null){
			LOGGER.error("app not found.key=" + cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"非法的appKey!"+cmd.getAppKey());
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("appKey", cmd.getAppKey());
		params.put("timestamp", cmd.getTimestamp()+"");
		params.put("nonce", cmd.getNonce()+"");
		params.put("crypto", cmd.getCrypto()+"");
		params.put("type", cmd.getType()+"");
		params.put("phone", cmd.getPhone());
		String signature = cmd.getSignature();
		boolean nSign = SignatureHelper.verifySignature(params, app.getSecretKey(),signature);
		if(!nSign){
			LOGGER.error("no oauth to access.nSign="+nSign+",sign="+signature+",key="+cmd.getAppKey());
			throw RuntimeErrorException.errorWith("asset", 201,
					"提交信息不匹配!"+cmd.getSignature());
		}
	}


}
