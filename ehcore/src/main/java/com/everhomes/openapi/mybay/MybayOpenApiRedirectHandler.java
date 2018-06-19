package com.everhomes.openapi.mybay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

/**携程H5单点登录
*@author created by huanglmn
*@date 2018年6月15日
**/
@Component(OpenApiRedirectHandler.PREFIX + "MYBAY")
public class MybayOpenApiRedirectHandler implements OpenApiRedirectHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MybayOpenApiRedirectHandler.class);
	//获取Ticket
	//private  static final String GETTICKETURL = "https://ct.ctrip.com/corpservice/authorize/getticket";
	private  static final int SUCCESS_CODE = 0 ;
	private  static final int MYBAY_NAMESPACE_ID = 999987;
	@Autowired
    private ConfigurationProvider configurationProvider;
	@Autowired
    private UserProvider userProvider;

	@Override
	public String build(String redirectUrl, Map<String, String[]> parameterMap) {
		
			//1.检验用户是否为“深圳湾科技发展有限公司”这个园区管理方所管理的公司的员工（检验其或空间ID是否为999987）
			Integer namespace  = UserContext.getCurrentNamespaceId();
			if(namespace.intValue() != MYBAY_NAMESPACE_ID){
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because the currentNamespaceId form UserContext is not eq MYBAY_NAMESPACE_ID 999987");
			}		
			//2.获取所有该对接的配置信息
			Map<String ,String> configurations = getConfigurations(MYBAY_NAMESPACE_ID);
			//3.获取Ticket
			String Token = getTicket(configurations.get("AppKey") , configurations.get("AppSecurity")  , configurations.get("getTicketURL"));
			if(Token == null || StringUtils.isBlank(Token)){
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because Token is null");
			}
			configurations.put("Token", Token);
			//4.获取员工编号
			Long userId = UserContext.currentUserId();
			if(userId == null ){
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because userId is null" );
				return null;
			}
			String EmployeeId= String.valueOf(userId);
			configurations.put("EmployeeId", EmployeeId);
			String signInfoURL = configurations.get("signInfoURL");
			//请求URL为空是没法做下去的
			if(StringUtils.isBlank(signInfoURL)){
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because signInfoURL is null" );
				return null;
			}
			//5.md5加密 Signature
			String Signature = encodeMD5Signature(configurations);
			configurations.put("Signature", Signature);
			//6.组装对接所需的纯净参数			
			buildParams(configurations);								
			//7.创建POST请求
			String response = null ;

			try {
				 response = HttpUtils.post(signInfoURL, configurations, 600, false);
				 /*response = UriComponentsBuilder.fromHttpUrl(signInfoURL)
			                .queryParam("AccessUserId",configurations.get("AppKey") )			        		
				 			.queryParam("AppSecurity", configurations.get("AppSecurity"))
				 			.queryParam("AppID", configurations.get("Appid"))
				 			.queryParam("CorpPayType", configurations.get("CorpPayType"))
			        		.queryParam("InitPage", configurations.get("InitPage"))
			        		.queryParam("Callback", configurations.get("Callback"))
			        		.queryParam("CostCenter1", configurations.get("CostCenter1"))
			        		.queryParam("CostCenter2", configurations.get("CostCenter2"))
			        		.queryParam("CostCenter3", configurations.get("CostCenter3"))
			        		.queryParam("EmployeeId", configurations.get("EmployeeId"))
			        		.queryParam("Token", configurations.get("Token"))
			        		.queryParam("Signature", configurations.get("Signature"))
			                .build()
			                .toUriString();*/
				 return response ;
			} catch (IOException e) {
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because " +e);
				return null;
			}
	}

	/**
	 * 获取Ticket
	 * 拿关对方提供的URL 及 接入账号和密码去获取令牌
	 * @param AppKey  接入账号
	 * @param AppSecurity 接入密码
	 * @param paramsMap
	 * @return 返回空或空字符串则说明获取失败，不过这得在方法调用处判断，这里只管获取
	 */
	private String  getTicket(String AppKey , String AppSecurity , String URL ){
		//必要参数一个都不能少，否则无法获取所要的信息
		if(StringUtils.isBlank(AppKey)){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because AppKey is null or blank ");
			return null;
		}else if(StringUtils.isBlank(AppSecurity)){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because AppSecurity is null or blank");
			return null;
		}else if(StringUtils.isBlank(URL)){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because URL of get ticket is null or blank " );
			return null;
		}
		//创建返回值容器
		Map<String ,String> paramsMap = new HashMap<String ,String>();
		paramsMap.put("AppKey", AppKey);
		paramsMap.put("AppSecurity", AppSecurity);
		String response = null ;
		try {
			 response = HttpUtils.post(URL, paramsMap, 600, false);
		} catch (IOException e) {
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because " +e);
			return null;
		}
		//返回信息不为空，则认为是其约定的JSON字符串：{"Token":"5514eea9c3d1b72148000001","Message":"","Code":0,"Success":true}
		if(response != null ){
			//解析返回信息
			//将字符串转为Map
			@SuppressWarnings("unchecked")
			Map<String ,Object> map = (Map<String, Object>) JSONObject.parse(response);
			if(map!=null ){
				//0 成功；1 参数验证失败；2 接口异常；
				Integer Code =  (Integer)map.get("Code");
				//True：调用成功 False：调用失败
				boolean Success = (boolean)map.get("Success");
				//接口调用成功并且令牌获取成功
				if(Success && Code.intValue() == SUCCESS_CODE){
					//将返回信息
					return (String) map.get("Token");
				}else {
					LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because Code=" +Code+",Message="+map.get("Message"));
					return null;
				}
			}			
		}
		return null ;
	}
	
	/**
	 * Signature 字段的值 是多个参数给过MD5加密后的字符串
	 * 签名生成规则说明：
		1.加密方式为MD5，32位小写
		2.加密字段MD5(AccessUserId+EmployeeID+CorpPayType+CostCenter1+CostCenter2+CostCenter3+MD5(AppSecurity))
		示例：
			MD5("obk_test"+"test"+"public"+"costcenter1"+"costcenter2"+"costcenter3"+MD5("obk_test"))
			加密后的值为：b0b9315d9cb290c5e8e5acb1b02cb2b3
		注意：
			1. CorpPayType字段如果不传，在生成MD5签名时需要传public
			2. 字符串拼接必须按照上面的顺序
			3. 参与加密的字段，必须在表单中传入单点登录接口
			4. 加密字段包含中文，需使用UTF-8编码
			5. 加密方法请参考：其他=>代码调用实例=>MD5加密方法
	 * @param paramMap (按理说该MAP应该有加密所要的所有参数)
	 * @return 加密后的字符串
	 */
	private String encodeMD5Signature(Map<String ,String> paramMap){
		if(paramMap == null ){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because the paramMap of encodeMD5Signature is null  ");
			return null;
		}
		String AccessUserId = paramMap.get("AccessUserId");//单点登录接入账号（同AppKey）
		String EmployeeID = paramMap.get("EmployeeID");//员工编号
		String CorpPayType = paramMap.get("CorpPayType");//public（因公）/private（因私）
		String CostCenter1 = paramMap.get("CostCenter1");//成本中心1
		String CostCenter2 = paramMap.get("CostCenter2");//成本中心2
		String CostCenter3 = paramMap.get("CostCenter3");//成本中心3
		String AppSecurity = paramMap.get("AppSecurity");//接入密码（同AppSecurity）
		//必要参数一个都不能少，否则无法获取所要的信息
		if(StringUtils.isBlank(AccessUserId)){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because AppKey(AccessUserId) is null or blank ");
			return null;
		}else if(StringUtils.isBlank(AppSecurity)){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because AppSecurity is null or blank");
			return null;
		}
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");			
			 try {
				md5.update(AppSecurity.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because AppSecurity"+e);
				
			}
			 String md5AppSecurity = StringHelper.toHexString(md5.digest()).toLowerCase();
			 
			 String collectStr = AccessUserId + EmployeeID + 
						CorpPayType + CostCenter1 + CostCenter2 +
						CostCenter3 + md5AppSecurity;
			 
			try {
				md5.update(collectStr.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because collectStr"+e);
			}
			String encodeMD5 = StringHelper.toHexString(md5.digest()).toLowerCase();
			return encodeMD5;
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because "+ e);
			e.printStackTrace();
		}
		return null ;														
	}
	
	/**
	 * 获取配置表中相关配置信息
	 * @param namespace
	 * @param nameList
	 * @return
	 */
	private Map<String ,String> getConfigurations(int namespace ){
		//创建返回值容器
		Map<String ,String> paramsMap = new HashMap<String ,String>();
		String AppKey = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_APPKEY,"");
		String AppSecurity = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_APPSECURITY,"");
		String Appid = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_APPID,"");
		String CorpPayType = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_CORPPAYTYPE,"");
		String InitPage = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_INITPAGE,"");
		String Callback = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_CALLBACK,"");
		String CostCenter1 = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_COSTCENTER1,"");
		String CostCenter2 = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_COSTCENTER2,"");
		String CostCenter3 = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_COSTCENTER3,"");
		String limitNamespaceId = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_LIMITNAMESPACEID,"");
		String getTicketURL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_GETTICKETURL,"");
		String signInfoURL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_SIGNINFOURL,"");
		String OnError = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_ONERROR,"");
		
		paramsMap.put("AppKey", AppKey);
		paramsMap.put("AccessUserId", AppKey);
		
		paramsMap.put("AppSecurity", AppSecurity);
		paramsMap.put("Appid", Appid);
		paramsMap.put("CorpPayType", CorpPayType);
		paramsMap.put("InitPage", InitPage);
		paramsMap.put("Callback", Callback);
		paramsMap.put("CostCenter1", CostCenter1);
		paramsMap.put("CostCenter2", CostCenter2);
		paramsMap.put("CostCenter3", CostCenter3);
		paramsMap.put("limitNamespaceId", limitNamespaceId);
		paramsMap.put("getTicketURL", getTicketURL);
		paramsMap.put("signInfoURL", signInfoURL);
		paramsMap.put("OnError", OnError);
		return paramsMap ;
	}
	
	/**
	 * 组装单点登录对接所需的参数，顺便检查必填字段是否为空(检查有问题只提示，不打断)
	 * AccessUserId(n)、EmployeeId(n)、Token(n)、Appid(n)、Signature(n)、EndorsementID
	 * CorpPayType、InitPage(n)、orderNumber、Callback(n)、CostCenter1、
	 * CostCenter2、CostCenter3、CostCenterCustom1、CostCenterCustom2、
	 * Language、NeedConfirmPerson1、NeedConfirmPerson2、OnError
	 * @param map
	 * @return
	 */
	private void buildParams(Map<String ,String> map){
		//将多余的去掉
		map.remove("signInfoURL");
		map.remove("getTicketURL");
		map.remove("limitNamespaceId");
		map.remove("AppKey");
		//检查必填字段
		if(StringUtils.isBlank(map.get("AccessUserId"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  AccessUserId is null");
		}
		if(StringUtils.isBlank(map.get("EmployeeId"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  EmployeeId is null");
		}
		if(StringUtils.isBlank(map.get("Token"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  Token is null");
		}
		if(StringUtils.isBlank(map.get("Appid"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  Appid is null");
		}		
		if(StringUtils.isBlank(map.get("Signature"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  Signature is null");
		}
		if(StringUtils.isBlank(map.get("InitPage"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  InitPage is null");
		}
		if(StringUtils.isBlank(map.get("Callback"))){
			LOGGER.error("MybayOpenApiRedirectHandler bulid is failed, because  Callback is null");
		}
	}
	
}
