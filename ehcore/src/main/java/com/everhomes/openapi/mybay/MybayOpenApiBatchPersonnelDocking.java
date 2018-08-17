package com.everhomes.openapi.mybay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.oauthapi.OAuth2ApiService;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;

/**
 * 用于与携程商旅（以下简称“携程”）的人事信息对接服务
 * @author created by huanglmn
 * 20180619
 */
@Component
public class MybayOpenApiBatchPersonnelDocking {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MybayOpenApiBatchPersonnelDocking.class);
	private  static final int SUCCESS_CODE = 0 ;
	private  static final int MYBAY_NAMESPACE_ID = 999966;
	private  static final String SUCCESS = "Success";
	//下面字段串为对方提供的参数名，如有修改，直接修改此处即可，代码中不用管
	private  static final String BATCH_PRO_URL = "batch_pro_URL";
	private  static final String RES_PRO_URL = "res_pro_URL";
	private  static final String ISOPENEDCARDURL = "IsOpenedCardURL";
	private  static final String EMPLOYEEID = "EmployeeId";
	private  static final String APPKEY = "appKey";
	private  static final String APPSECURITY = "appSecurity";
	private  static final String TICKET = "Ticket";
	private  static final String APPID = "Appid";
	private  static final String LOGONAPPID = "LogonAppid";
	private  static final String CORPID = "CorpID";
	
	@Autowired
    private ConfigurationProvider configurationProvider;
	@Autowired
    private UserProvider userProvider;
	@Autowired
	private OAuth2ApiService oAuth2ApiService;
	
	
	/**
	 * 人事信息对接总方法
	 * @return 
	 */
	public Map<String,String> batchDocking() throws IOException{
		//1.获取所有该对接的配置信息
		Map<String ,String> configurations = getConfigurations(MYBAY_NAMESPACE_ID);
		String getTiketURL = configurations.get(BATCH_PRO_URL);
		String batchURL = configurations.get(RES_PRO_URL);
		Long userId = UserContext.currentUserId();
		if(userId == null ){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because userId is null" );
			return null;
		}
		String EmployeeId= String.valueOf(userId);
		configurations.put(EMPLOYEEID, EmployeeId);
		//2.获取Ticket
		String Ticket = getTicket(configurations.get(APPKEY) , configurations.get(APPSECURITY)  , getTiketURL);
		if(Ticket == null || StringUtils.isBlank(Ticket)){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because Ticket is null");
		}
		configurations.put(TICKET, Ticket);
		//3.组装对接的参数对象
		AuthenticationListRequest requestObj = buildParamsObject(configurations);
		if(requestObj == null){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because AuthenticationListRequest is null");
			return null ;
		}
		//4.创建post 请求
		String requestString = StringHelper.toJsonString(requestObj);
		try {
			String  response = HttpUtils.postJson(batchURL, requestString, 600,HTTP.UTF_8);
			 Map<String,String> resultMap = handleResponse(response);
			 return resultMap ;
		} catch (IOException e) {
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because " +e);
			throw e;
		}
		
	}
	
	
	/*
	 * 判断是否开卡（也就是人员是否在对方那里创建成功，首次创建会有延迟，反面都是覆盖更新会比较快）
	 */
	public boolean isFinishDocking(){
		//1.获取所有该对接的配置信息
		Map<String ,String> configurations = getConfigurations(MYBAY_NAMESPACE_ID);
		String getTiketURL = configurations.get(BATCH_PRO_URL);
		String IsOpenedCardURL = configurations.get(ISOPENEDCARDURL);
		Long userId = UserContext.currentUserId();
		if(userId == null ){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because userId is null" );
			return false;
		}
		String EmployeeId= String.valueOf(userId);
		configurations.put(EMPLOYEEID, EmployeeId);
		//2.获取Ticket
		String Ticket = getTicket(configurations.get(APPKEY) , configurations.get(APPSECURITY)  , getTiketURL);
		if(Ticket == null || StringUtils.isBlank(Ticket)){
		   LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because Ticket is null");
		}		
		//3.判断是否开卡
		Map<String ,String> paramsMap = new HashMap<String ,String>();
		paramsMap.put(TICKET, Ticket);
		paramsMap.put(CORPID, configurations.get(APPID));
		paramsMap.put(EMPLOYEEID, EmployeeId);
		String response = null ;
		try {
			 response = HttpUtils.post(IsOpenedCardURL, paramsMap, 600, false);
			 return handleIsDockingResponse(response);
		} catch (IOException e) {
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because " +e);
			return false;
		}
	}
	/**
	 * 获取Ticket
	 * 拿着对方提供的URL 及 接入账号和密码去获取令牌
	 * @param AppKey  接入账号
	 * @param AppSecurity 接入密码
	 * @param paramsMap
	 * @return 返回空或空字符串则说明获取失败，不过这得在方法调用处判断，这里只管获取
	 */
	private String  getTicket(String AppKey , String AppSecurity , String URL ){
		//必要参数一个都不能少，否则无法获取所要的信息
		if(StringUtils.isBlank(AppKey)){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because {} is null or blank ",APPKEY);
			return null;
		}else if(StringUtils.isBlank(AppSecurity)){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because {} is null or blank",APPSECURITY);
			return null;
		}else if(StringUtils.isBlank(URL)){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because URL of get ticket is null or blank " );
			return null;
		}
		//创建返回值容器
		Map<String ,String> paramsMap = new HashMap<String ,String>();
		paramsMap.put(APPKEY, AppKey);
		paramsMap.put(APPSECURITY, AppSecurity);
		String response = null ;
		try {
			 response = HttpUtils.post(URL, paramsMap, 600, false);
		} catch (IOException e) {
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because " +e);
			return null;
		}
		//返回信息不为空，则认为是其约定的JSON字符串：{"Status":{"Success":true,"ErrorCode":0,"Message":"调用成功。"},"Ticket":"59880d2c90e1d33f68001a2f"}		
		if(response != null ){
			//解析返回信息
			//将字符串转为Map
			@SuppressWarnings("unchecked")
			Map<String ,Object> map = (Map<String, Object>) JSONObject.parse(response);
			if(map!=null ){
				
				@SuppressWarnings("unchecked")
				Map<String,Object> status = (Map<String,Object>)map.get("Status");
				if(status != null){
					//0 成功；1 参数验证失败；2 接口异常；
					Integer Code =  (Integer)status.get("ErrorCode");
					//True：调用成功 False：调用失败
					boolean Success = (boolean)status.get(SUCCESS);
					//接口调用成功并且令牌获取成功
					if(Success && Code.intValue() == SUCCESS_CODE){
						//将返回信息
						return (String) map.get(TICKET);
					}else {
						LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, because Code=" +Code+",Message="+map.get("Message"));
						return null;
					}
				}				
			}			
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
		String LogonAppid = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_BATCH_LOGON_APPID,"");
		
		String batch_pro_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_BATCH_PRODUCTION_GETTICKETURL,"");
		String batch_test_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_BATCH_TEST_GETTICKETURL,"");
		String res_pro_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_RESTFUL_PRODUCTION_BATCHURL,"");
		String soap_pro_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_SOAP_PRODUCTION_BATCHURL,"");
		String res_test_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_RESTFUL_TEST_BATCHURL,"");
		String soap_test_URL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_SOAP_TEST_BATCHURL,"");
		String IsOpenedCardURL = configurationProvider.getValue(MYBAY_NAMESPACE_ID, ConfigConstants.CT_ISOPENEDCARDURL,"");

		
		paramsMap.put(APPKEY, AppKey);		
		paramsMap.put(APPSECURITY, AppSecurity);
		paramsMap.put(APPID, Appid);
		paramsMap.put(LOGONAPPID, LogonAppid);
		
		paramsMap.put(BATCH_PRO_URL, batch_pro_URL);
		paramsMap.put("batch_test_URL", batch_test_URL);
		paramsMap.put(RES_PRO_URL, res_pro_URL);
		paramsMap.put("soap_pro_URL", soap_pro_URL);
		paramsMap.put("res_test_URL", res_test_URL);
		paramsMap.put("soap_test_URL", soap_test_URL);
		paramsMap.put(ISOPENEDCARDURL, IsOpenedCardURL);

		return paramsMap ;
	}
	
	/**
	 * 组装请求参数对象 ,在此过程中顺便检查必填项是否齐全
	 * 必填项为空时，只提示不抛异常
	 * 对象层次:
	 * AuthenticationListRequest{
	 * Appkey,Ticket,CorporationID,
	 * AuthenticationInfoList[
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}},
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}},
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}}
	 * 	...
	 * ]
	 * }
	 * @param configurations
	 * @param resultDTO
	 * @return
	 */
	private AuthenticationListRequest buildParamsObject(Map<String ,String> configurations ){
		//传参为空，那不用做了，直接返回
		if(configurations==null ||configurations.entrySet()==null ||configurations.entrySet().size()<1){
			return null ;
		}
		AuthenticationListRequest request = new AuthenticationListRequest();
		request.setAppkey(configurations.get(LOGONAPPID));
		request.setTicket(configurations.get(TICKET));
		request.setCorporationID(configurations.get(APPID));
		
		List<AuthenticationInfo> InfoList  = buildBatchPersonnelInfo(configurations);
		if(InfoList == null ){
			return null ;
		}
		request.setAuthenticationInfoList(InfoList);
		return request;
	}
	

	/**
	 * 
	 * 暂定前端传进来的是要批量对接的人员的ID(设计只传单个人的信息)，存于configurations的“userID” 中
	 * AuthenticationInfoList[
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}},
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}},
	 * 	{Sequence,Authentication{EmployeeID,Name,MobilePhone,...}}
	 * 	...
	 * ]
	 * @param configurations
	 * @param resultDTO 
	 * @return
	 */
	private List<AuthenticationInfo> buildBatchPersonnelInfo(Map<String ,String> configurations ){
		String userID = configurations.get(EMPLOYEEID);
		if(StringUtils.isBlank(userID)){
			LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, this userId is blank ");
			return null ;
		}			 
			//先检查该用户是否存在于系统中
			 Long userId = Long.valueOf(userID);
			 User user = userProvider.findUserById(userId);
			 if(user == null){
				 LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, this userId:"+userId +"  can not find a corresponding user");
			 }
			 List<AuthenticationInfo> list = new ArrayList<AuthenticationInfo>();
			 AuthenticationInfo info = new AuthenticationInfo();
			 info.setSequence(Integer.valueOf(userID));
			 AuthencationEntity ae = new AuthencationEntity();
			 ae.setEmployeeID(userID);
			 ae.setName(user.getNickName());
			 //因为对方接口会核验手机号邮箱，如为非法或不合格式的会校验不通过，但我们测试的用户有些手机号并非真的，所以自己测试时把这两个字段的值注掉，上线记得开回来
			 UserIdentifier uIdentifier = userProvider.findUserIdentifiersOfUser(userId,user.getNamespaceId());
			 if(uIdentifier !=null){
				 if(0 == uIdentifier.getIdentifierType()){
					 ae.setMobilePhone(uIdentifier.getIdentifierToken());
				 }else if(1 == uIdentifier.getIdentifierType()){
					 ae.setEmail(uIdentifier.getIdentifierToken()); 
				 }				 				 				 
			 }	
			//游客：Shenzhenwan_游客;认证用户：Shenzhenwan,目前只有认证用户
			 ae.setSubAccountName("Shenzhenwan");
			 List<OrganizationMemberDTO> orgList =  oAuth2ApiService.getAuthenticationInfo(userId);
			 if(orgList!=null && orgList.size()>0){
				 OrganizationMemberDTO dto = orgList.get(0);
				 ae.setDept1(dto.getOrganizationName());
			 }
			 /*ae.setValid(valid);
			 ae.setDept1(dept1);
			 ae.setDept2(dept2);			 
			 ae.setIsSendEMail(isSendEMail);*/
			 info.setAuthentication(ae);
			 list.add(info);
				
		return list;
	}
	/**
	 * 处理调用别人接口返回的信息：
	 * {
	 * "Result":"Success"    //接口调用结果Success: 更新成功；Failed：更新失败；Contains errors：部分数据未更新成功；
	 * "ErrorMessageList":[
	 * 		{"Sequence":***,"ErrorCode":***,"Message":***},
	 * 		{"Sequence":***,"ErrorCode":***,"Message":***},
	 * 		{"Sequence":***,"ErrorCode":***,"Message":***},
	 * 		...
	 * 	]
	 * }
	 * @param response
	 * @return
	 */
	private Map<String,String> handleResponse(String response){

		if(StringUtils.isBlank(response)){
			return null;
		}
		Map<String,String> resultMap = new HashMap<String,String>();
		//先将JSON字符串转为mapN对象
		Map<String ,Object> jsonmap  = JSONObject.parseObject(response);
		if(jsonmap != null){
			String result =  (String)jsonmap.get("Result");
			if(SUCCESS.equals(result)){
				resultMap.put("result",SUCCESS);
			}else{
				//更新失败，打印出失败信息方便调试
				resultMap.put("result",result);
				@SuppressWarnings("unchecked")
				List<JSONObject> errorList =  (List<JSONObject>)jsonmap.get("ErrorMessageList");
				if(errorList !=null && errorList.size()>0){
					JSONObject errorMap = errorList.get(0);
					if(errorMap != null){
						resultMap.put("errorMsg",errorMap.get("Message")==null?"":errorMap.get("Message").toString());
						LOGGER.error("MybayOpenApiBatchPersonnelDocking bulid is failed, errorMsg:"+errorMap.get("Message"));
					}
				}
				
			}
		}		
		return resultMap;
	}
	
	/**
	 * 处理是否开卡接口返回的参数
	 * @param response
	 * @return
	 */
	private boolean handleIsDockingResponse(String response){

		if(StringUtils.isBlank(response)){
			return false;
		}
		Map<String,String> resultMap = new HashMap<String,String>();
		//先将JSON字符串转为mapN对象
		Map<String ,Object> jsonmap  = JSONObject.parseObject(response);
		if(jsonmap != null){
			boolean result =  (boolean)jsonmap.get("IsOpened");
			return result;
		}	
		return false ;
	}
}
