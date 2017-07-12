package com.everhomes.authorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.AddressService;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.user.UserContext;

@Component(AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX+"EhNamespaces"+1000000)
public class ZJAuthorizationModuleHandler implements AuthorizationModuleHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZJAuthorizationModuleHandler.class);
	   
	
	static final String url = "http://139.129.220.146:3578/openapi/Authenticate";
	
	static final String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
	
	static final String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";
	
    @Autowired
    private CommunityProvider communityProvider;
	
	@Autowired
	public FlowService flowService;
	
    @Autowired
    private AddressService addressService;

	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		
		Map<String, String> params = generalParams(cmd);
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			//请求成功，返回承租地址，那么创建家庭。
			if(entity.isSuccess()){
				createFamily(cmd,entity);
			}
			//创建工作流
			createWorkFlow(cmd,entity);
		} catch (Exception e) {
			LOGGER.error(""+e);
		}
		return null;
	}
	
	private void createFamily(PostGeneralFormCommand cmd,ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		List<ZjgkResponse> list = entity.getResponse();
		if(list!=null && list.size()>0){
			for (ZjgkResponse zjgkResponse : list) {
				String communityName = zjgkResponse.getCommunityName();
				Community community = communityProvider.findCommunityByNamespaceIdAndName(cmd.getNamespaceId(), communityName);
				ClaimAddressCommand claimcmd = new ClaimAddressCommand();
				claimcmd.setCommunityId(community.getId());
				claimcmd.setApartmentName(zjgkResponse.getApartmentName());
				claimcmd.setBuildingName(zjgkResponse.getBuildingName());
				ClaimedAddressInfo addressinfo = addressService.claimAddress(claimcmd);
				
			}
		}
		
	}

	private FlowCase createWorkFlow(PostGeneralFormCommand cmd, ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		
		GeneralModuleInfo ga = new GeneralModuleInfo();

		ga.setNamespaceId(UserContext.getCurrentNamespaceId());
		ga.setOwnerType(cmd.getOwnerType());
		ga.setOwnerId(cmd.getOwnerId());
		ga.setModuleType(FlowModuleType.NO_MODULE.getCode());
		ga.setModuleId(FlowConstants.AUTHORIZATION_MODULE);
		ga.setProjectId(cmd.getOwnerId());
		ga.setProjectType(EntityType.NAMESPACE.getCode());

		
		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(UserContext.current().getUser().getId());
		createFlowCaseCommand.setReferId(cmd.getOwnerId());
		createFlowCaseCommand.setReferType(cmd.getOwnerType());
		createFlowCaseCommand.setContent(generalContent(entity));

		createFlowCaseCommand.setProjectId(cmd.getOwnerId());
		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
		
		FlowCase flowcase = flowService.createDumpFlowCase(ga, createFlowCaseCommand);
		return flowcase;
	}

	private String generalContent(ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		StringBuffer buffer = new StringBuffer();
		List<ZjgkResponse> list = entity.getResponse();
		if(entity.isMismatching()){
			buffer.append("提交信息匹配不成功：\n");
			buffer.append("1、请检测信息填写是否正确，如填写有误，请重新提交");
			buffer.append("2、如填写无误，请到携带相关证件管理处核对信息");
			return buffer.toString();
		}
		if(entity.isUnrent()){
			buffer.append("您已退租，如有疑问请联系客服\n");
			return buffer.toString();
		}
		if(entity.isSuccess())
		{
			buffer.append("认证成功！您的地址信息如下：\n");
		}
		else{
			buffer.append("不知道什么情况。\n");
		}
		if(list!=null && list.size()>0){
			for (ZjgkResponse zjgkResponse : list) {
				buffer.append(zjgkResponse.getAddress()).append("\n");
			}
		}
		return buffer.toString();
	}

	private Map<String, String> generalParams(PostGeneralFormCommand cmd){
		List<PostApprovalFormItem> values = cmd.getValues();
		Map<String, String> params= new HashMap<String,String>();
		for (PostApprovalFormItem item : values) {
			params.put(item.getFieldName(), item.getFieldValue());
		}
		params.put("appKey", appKey);
		params.put("timestamp", ""+System.currentTimeMillis());
		params.put("nonce", ""+(long)(Math.random()*100000));
		params.put("type", PERSONAL_AUTHORIZATION);
		String signature = computeSignature(params, secretKey);
		params.put("signature", signature);
		return params;
		
	}
	

	@Override
	public PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd) {
		return null;
	}

}
