package com.everhomes.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.Address;
import com.everhomes.address.AddressService;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.family.ApproveMemberCommand;
import com.everhomes.rest.family.BatchApproveMemberCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.ui.user.GetUserRelatedAddressCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressResponse;
import com.everhomes.user.User;
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
	public FamilyService familyService;
	
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private AuthorizationThirdPartyRecordProvider authorizationProvider;

	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		
		Map<String, String> params = generalParams(cmd);
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			//请求成功，返回承租地址，那么创建家庭。
			if(entity.isSuccess()){
				createFamily(cmd,entity,params,jsonStr,PERSONAL_AUTHORIZATION);
			}
			//创建工作流
			createWorkFlow(cmd,entity,PERSONAL_AUTHORIZATION);
		} catch (Exception e) {
			LOGGER.error(""+e);
		}
		return null;
	}
	
	private void createFamily(PostGeneralFormCommand cmd,ZjgkJsonEntity<List<ZjgkResponse>> entity, Map<String, String> params, String resultJson, String authorizationType) {
		List<ZjgkResponse> list = entity.getResponse();
		List<AuthorizationThirdPartyRecord> recordlist = new ArrayList<AuthorizationThirdPartyRecord>();
		if(list!=null && list.size()>0){
			dbProvider.execute((TransactionStatus status) -> {
				for (ZjgkResponse zjgkResponse : list) {
					zjgkResponse.setCommunityName("科技园"); // TODO
					//生成加入家庭的command
					ClaimAddressCommand claimcmd = generalClaimAddressCommand(cmd,zjgkResponse);
					//加入家庭
					ClaimedAddressInfo addressinfo = addressService.claimAddress(claimcmd);
					//认证记录到list
					recordlist.add(generalUserAuthorizationRecord(cmd,params,addressinfo,authorizationType,entity.getErrorCode(),resultJson));
				}
				// TODO 认证状态设置为通过
				familyService.adminBatchApproveMember(generateBatchApproveMemberCommand());
				// TODO 应该删除之前的认证，再在记录。认证成功，记录到表中
				for (AuthorizationThirdPartyRecord record : recordlist) {
					authorizationProvider.createAuthorizationThirdPartyRecord(record);
				}
				return null;
			});
		}
		
	}
	

	private BatchApproveMemberCommand generateBatchApproveMemberCommand() {
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		BatchApproveMemberCommand batchCmd = new BatchApproveMemberCommand();
		List<ApproveMemberCommand> members = familyList.stream().map(r -> {
			ApproveMemberCommand cmd = new ApproveMemberCommand();
			cmd.setAddressId(r.getAddressId());
			cmd.setId(r.getId());
			User user = UserContext.current().getUser();
		    long userId = user.getId();
			cmd.setMemberUid(userId);
			return cmd;
		}).collect(Collectors.toList());
		batchCmd.setMembers(members);
		return batchCmd;
	}

	//调用认证接口，需要生成认证的command
	private ClaimAddressCommand generalClaimAddressCommand(PostGeneralFormCommand cmd, ZjgkResponse zjgkResponse) {
		String communityName = zjgkResponse.getCommunityName();
		Community community = communityProvider.findCommunityByNamespaceIdAndName(cmd.getNamespaceId(), communityName);
		ClaimAddressCommand claimcmd = new ClaimAddressCommand();
		claimcmd.setCommunityId(community.getId());
		claimcmd.setApartmentName(zjgkResponse.getApartmentName());
		claimcmd.setBuildingName(zjgkResponse.getBuildingName());
		return claimcmd;
	}

	private AuthorizationThirdPartyRecord generalUserAuthorizationRecord(PostGeneralFormCommand cmd, Map<String, String> params,
			ClaimedAddressInfo addressinfo, String authorizationType, int errorCode, String resultJson) {
		AuthorizationThirdPartyRecord record = new AuthorizationThirdPartyRecord();
		record.setNamespaceId(cmd.getNamespaceId());
		record.setOwnerType(cmd.getOwnerType());
		record.setOwnerId(cmd.getOwnerId());
		record.setType(authorizationType);
		record.setPhone(params.get("phone"));
		record.setName(params.get("name"));
		record.setCertificateType(params.get("certificateType"));
		record.setCertificateNo(params.get("certificateNo"));
		record.setOrganizationCode(params.get("organizationCode"));
		record.setOrganizationContact(params.get("organizationContact"));
		record.setOrganizationPhone(params.get("organizationPhone"));
		record.setErrorCode(errorCode);
		record.setAddressId(addressinfo.getAddressId());
		record.setFullAddress(addressinfo.getFullAddress());
		record.setUserCount(addressinfo.getUserCount());
		record.setResultJson(resultJson);
		return record;
	}

	private FlowCase createWorkFlow(PostGeneralFormCommand cmd, ZjgkJsonEntity<List<ZjgkResponse>> entity, String personalAuthorization) {
		
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
