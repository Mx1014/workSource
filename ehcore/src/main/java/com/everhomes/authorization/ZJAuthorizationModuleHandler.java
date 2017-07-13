package com.everhomes.authorization;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.address.DisclaimAddressCommand;
import com.everhomes.rest.family.ApproveMemberCommand;
import com.everhomes.rest.family.BatchApproveMemberCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.ui.user.GetUserRelatedAddressCommand;
import com.everhomes.rest.ui.user.GetUserRelatedAddressResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

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
    
  	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());
  	

	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		
		Map<String, String> params = generalParams(cmd);
		ZjgkJsonEntity<List<ZjgkResponse>> entity = null;
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
//			entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_UNRENT);
			//请求成功，返回承租地址，那么创建家庭。
			if(entity.isSuccess()){
				createFamily(cmd,entity,params,jsonStr,PERSONAL_AUTHORIZATION);
			}
			//创建工作流
			createWorkFlow(cmd,entity,PERSONAL_AUTHORIZATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processGeneralFormDTO(cmd,entity);
	}
	
	private PostGeneralFormDTO processGeneralFormDTO(PostGeneralFormCommand cmd, ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);

        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(generalContent(entity));
        items.add(item);
        dto.getValues().addAll(items);
        return dto;
	}

	private void createFamily(PostGeneralFormCommand cmd,ZjgkJsonEntity<List<ZjgkResponse>> entity, Map<String, String> params, String resultJson, String authorizationType) {
		List<ZjgkResponse> list = entity.getResponse();
		List<AuthorizationThirdPartyRecord> recordlist = new ArrayList<AuthorizationThirdPartyRecord>();
		if(list != null && list.size() > 0){
			//此处不加，事务，因为调用的方法中存在事务
			//先让用户离开通过第三方认证的家庭
			leaveThirdPartyAuthAddress();
			//加入第三方认证的家庭
			for (ZjgkResponse zjgkResponse : list) {
				zjgkResponse.setCommunityName("科技园"); // TODO
				zjgkResponse.setBuildingName("4-4");// TODO
				//生成加入家庭的command
				ClaimAddressCommand claimcmd = generalClaimAddressCommand(cmd,zjgkResponse);
				if(claimcmd == null){
					zjgkResponse.setExistCommunityFlag((byte)0);
					continue;
				}
				//加入家庭
				ClaimedAddressInfo addressinfo = addressService.claimAddress(claimcmd);
				//认证记录到list
				recordlist.add(generalUserAuthorizationRecord(cmd,params,addressinfo,authorizationType,entity.getErrorCode(),resultJson));
			}
			
			// 认证状态设置为通过，家庭认证通过。
			familyService.adminBatchApproveMember(generateBatchApproveMemberCommand(recordlist));
			dbProvider.execute(status -> {
				//删除之前第三方认证记录
				authorizationProvider.removeAuthorizationThirdPartyRecord(UserContext.getCurrentNamespaceId(),UserContext.current().getUser().getId());
				// 认证成功，记录到认证记录表中
				for (AuthorizationThirdPartyRecord record : recordlist) {
					authorizationProvider.createAuthorizationThirdPartyRecord(record);
				}
				return null;
			});
		}
		
	}


	//先离开第三方认证的家庭。再次认证才不会出错。
	private void leaveThirdPartyAuthAddress() {
		//获取用户所有的家庭
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		//认证记录
		List<AuthorizationThirdPartyRecord> listrecords = authorizationProvider.listAuthorizationThirdPartyRecordByUserId(UserContext.getCurrentNamespaceId(),UserContext.current().getUser().getId());
		//用户所在家庭的地址，在第三方认证记录表中，那么久离开。
		familyList.stream().filter(r ->{
			return isInleaveRecord(listrecords, r.getAddressId());
		}).map(r -> {
			DisclaimAddressCommand cmd = new DisclaimAddressCommand();
			cmd.setAddressId(r.getAddressId());
			addressService.disclaimAddress(cmd);
			return null;
		});
	}

	//生成批量认证的command。
	private BatchApproveMemberCommand generateBatchApproveMemberCommand(List<AuthorizationThirdPartyRecord> leavelist) {
		//获取用户所有的家庭
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		//只有第三方认证的家庭，才做
		BatchApproveMemberCommand batchCmd = new BatchApproveMemberCommand();
		//过滤出在第三方认证的信息，直接认证成功。
		List<ApproveMemberCommand> members = familyList.stream().filter(r ->{
			return isInleaveRecord(leavelist, r.getAddressId());
		}).map(r -> {
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
	
	private boolean isInleaveRecord(List<AuthorizationThirdPartyRecord> leavelist, long addressId){
		for (AuthorizationThirdPartyRecord record : leavelist) {
			if(record.getAddressId() != null && record.getAddressId().longValue() == addressId){
				return true;
			}
		}
		return false;
	}

	//调用认证接口，需要生成认证的command
	private ClaimAddressCommand generalClaimAddressCommand(PostGeneralFormCommand cmd, ZjgkResponse zjgkResponse) {
		String communityName = zjgkResponse.getCommunityName();
		Community community = communityProvider.findCommunityByNamespaceIdAndName(cmd.getNamespaceId(), communityName);
		if(community == null){
			return null;
		}
		ClaimAddressCommand claimcmd = new ClaimAddressCommand();
		claimcmd.setCommunityId(community.getId());
		claimcmd.setApartmentName(zjgkResponse.getApartmentName());
		claimcmd.setBuildingName(zjgkResponse.getBuildingName());
		return claimcmd;
	}

	private AuthorizationThirdPartyRecord generalUserAuthorizationRecord(PostGeneralFormCommand cmd, Map<String, String> params,
			ClaimedAddressInfo addressinfo, String authorizationType, int errorCode, String resultJson) {
		AuthorizationThirdPartyRecord record = new AuthorizationThirdPartyRecord();
		record.setNamespaceId(UserContext.getCurrentNamespaceId());
		record.setOwnerType(cmd.getOwnerType());
		record.setOwnerId(Long.valueOf(UserContext.getCurrentNamespaceId()));
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
			buffer.append("很遗憾，您的认证未成功：\n");
			buffer.append("1、请检测信息填写是否正确，如填写有误，请重新提交\n");
			buffer.append("2、如填写无误，请到携带相关证件管理处核对信息\n");
			return buffer.toString();
		}
		if(entity.isUnrent()){
			buffer.append("您已退租，如有疑问请联系客服\n");
			return buffer.toString();
		}
		if(entity.isSuccess())
		{
			buffer.append("恭喜您，成为我们的一员，您承租的地址信息如下：\n");
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
