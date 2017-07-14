package com.everhomes.authorization;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import com.everhomes.rest.approval.CommonStatus;
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
import com.everhomes.util.StringHelper;

@Component(AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX+"EhNamespaces"+999992)
public class ZJAuthorizationModuleHandler implements AuthorizationModuleHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZJAuthorizationModuleHandler.class);
	   
	
	static final String url = "http://139.129.220.146:3578/openapi/Authenticate";
	
	static final String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";
	
	static final String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";
	
	static final String[] communites = {"深业花园","岭秀名苑","深发花园","风临左岸","深业中心大厦","地王","地王","天天","幸福","屠龙","抗日战争","富人","南领花园","不存在的小区1","不存在的小区2","不存在的小区3","不存在的小区4","不存在的小区5","不存在的小区6"};
	
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
    
    @Autowired
    private AuthorizationThirdPartyFormProvider authorizationThirdPartyFormProvider;
    
	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		getSettinginfo(cmd);
		Map<String, String> params = generalParams(cmd);
		ZjgkJsonEntity<List<ZjgkResponse>> entity = null;
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			//向第三方认证。
			entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_SUCCESS);
			//请求成功，返回承租地址，那么创建家庭。
			AuthorizationThirdPartyRecord record = createFamily(cmd,entity,params,PERSONAL_AUTHORIZATION);
			//创建工作流
			FlowCase flowCase = createWorkFlow(cmd,entity,PERSONAL_AUTHORIZATION,params);
			record.setFlowCaseId(flowCase.getId());
			//记录认证参数和结果，提供给工作流使用
			createRecords(record,PERSONAL_AUTHORIZATION);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processGeneralFormDTO(cmd,entity);
	}
	
	private void getSettinginfo(PostGeneralFormCommand cmd) {
//		List<AuthorizationThirdPartyForm> list = authorizationThirdPartyFormProvider.listFormSourceByOwner(cmd.getOwnerType(), cmd.getOwnerId());
//		if(list!=null && list.size()>0){
//			AuthorizationThirdPartyForm setting = list.get(0);
//			thiurl = setting.getAuthorizationUrl();
//			
//		}
	}

	private void createRecords(AuthorizationThirdPartyRecord record,String type) {
		//记录对同一个用户来说，只有一条有效，其他的则提供给工作流显示而已，设置为无效
		dbProvider.execute(status -> {
			if(record.getErrorCode() == ZjgkJsonEntity.ERRORCODE_SUCCESS){
				authorizationProvider.updateAuthorizationThirdPartyRecordStatusByUseId(UserContext.getCurrentNamespaceId(),UserContext.current().getUser().getId(),type);
				authorizationProvider.createAuthorizationThirdPartyRecord(record);
			}else{
				record.setStatus(CommonStatus.INACTIVE.getCode());
				authorizationProvider.createAuthorizationThirdPartyRecord(record);
			}
			return null;
		});
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

	private AuthorizationThirdPartyRecord createFamily(PostGeneralFormCommand cmd,ZjgkJsonEntity<List<ZjgkResponse>> entity, Map<String, String> params, String authorizationType) {
		List<ZjgkResponse> list = entity.getResponse();
		AuthorizationThirdPartyRecord record = null;
		if(entity.isSuccess() && list != null && list.size() > 0){
			//此处不加，事务，因为调用的方法中存在事务
			//先让用户离开通过第三方认证的家庭
			leaveThirdPartyAuthAddress(authorizationType);
			//加入第三方认证的家庭
			for (ZjgkResponse zjgkResponse : list) {
				zjgkResponse.setCommunityName(communites[(int)(Math.random()*19)]); // TODO
				zjgkResponse.setBuildingName(generateRandomNumber(2)+"-"+generateRandomNumber(3));// TODO
				//生成加入家庭的command
				ClaimAddressCommand claimcmd = generateClaimAddressCommand(cmd,zjgkResponse);
				if(claimcmd == null){
					zjgkResponse.setExistCommunityFlag(ZjgkResponse.NOT_EXIST_COMMUNITY);
					continue;
				}
				if(claimcmd.getCommunityId() == null){
					zjgkResponse.setExistCommunityFlag(ZjgkResponse.MULTI_COMMUNITY);
					continue;
				}
				//加入家庭
				ClaimedAddressInfo addressinfo = addressService.claimAddress(claimcmd);
				zjgkResponse.setAddressId(addressinfo.getAddressId());
				zjgkResponse.setFullAddress(addressinfo.getFullAddress());
				zjgkResponse.setUserCount(addressinfo.getUserCount());
			}
			record = generateUserAuthorizationRecord(cmd,params,authorizationType,entity);
			// 认证状态设置为通过，家庭认证通过。
			familyService.adminBatchApproveMember(generateBatchApproveMemberCommand(record));
		}else{
			record = generateUserAuthorizationRecord(cmd,params,authorizationType,entity);
		}
		return record;
	}
	
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
	}


	//先离开第三方认证的家庭。再次认证才不会出错。
	private void leaveThirdPartyAuthAddress(String authorizationType) {
		//获取用户所有的家庭
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		//认证记录
		AuthorizationThirdPartyRecord record = authorizationProvider.getAuthorizationThirdPartyRecordByUserId(UserContext.getCurrentNamespaceId(),UserContext.current().getUser().getId(),authorizationType);
		//用户所在家庭的地址，在第三方认证记录表中，那么久离开。
		familyList.stream().filter(r ->{
			return isInleaveRecord(record, r.getAddressId());
		}).map(r -> {
			DisclaimAddressCommand cmd = new DisclaimAddressCommand();
			cmd.setAddressId(r.getAddressId());
			addressService.disclaimAddress(cmd);
			return null;
		});
	}

	//生成批量认证的command。
	private BatchApproveMemberCommand generateBatchApproveMemberCommand(AuthorizationThirdPartyRecord record) {
		//获取用户所有的家庭
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		//只有第三方认证的家庭，才做
		BatchApproveMemberCommand batchCmd = new BatchApproveMemberCommand();
		//过滤出在第三方认证的信息，直接认证成功。
		List<ApproveMemberCommand> members = familyList.stream().filter(r ->{
			return isInleaveRecord(record, r.getAddressId());
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
	
	//在记录
	private boolean isInleaveRecord(AuthorizationThirdPartyRecord record, long addressId){
		ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		for (ZjgkResponse zjgkResponse: entity.getResponse()) {
			if(zjgkResponse.getAddressId() != null && zjgkResponse.getAddressId().longValue() == addressId){
				return true;
			}
		}
		return false;
	}

	//调用认证接口，需要生成认证的command
	private ClaimAddressCommand generateClaimAddressCommand(PostGeneralFormCommand cmd, ZjgkResponse zjgkResponse) {
		String communityName = zjgkResponse.getCommunityName();
		List<Community> communities = communityProvider.listCommunityByNamespaceIdAndName(cmd.getNamespaceId(), communityName);
		ClaimAddressCommand claimcmd = new ClaimAddressCommand();
		if(communities == null){
			return null;
		}
		if(communities.size()>1){
			return claimcmd;
		}
		claimcmd.setCommunityId(communities.get(0).getId());
		claimcmd.setApartmentName(zjgkResponse.getApartmentName());
		claimcmd.setBuildingName(zjgkResponse.getBuildingName());
		return claimcmd;
	}

	private AuthorizationThirdPartyRecord generateUserAuthorizationRecord(PostGeneralFormCommand cmd, Map<String, String> params,
			String authorizationType,ZjgkJsonEntity<List<ZjgkResponse>> entity) {
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
		record.setErrorCode(entity.getErrorCode());
//		record.setAddressId(addressinfo.getAddressId());
//		record.setFullAddress(addressinfo.getFullAddress());
//		record.setUserCount(addressinfo.getUserCount());
		record.setResultJson(StringHelper.toJsonString(entity));
		record.setStatus(CommonStatus.ACTIVE.getCode());
		return record;
	}

	private FlowCase createWorkFlow(PostGeneralFormCommand cmd, ZjgkJsonEntity<List<ZjgkResponse>> entity, String authorizationType, Map<String, String> params) {
		
		GeneralModuleInfo ga = new GeneralModuleInfo();

		ga.setNamespaceId(UserContext.getCurrentNamespaceId());
		ga.setOwnerType(cmd.getOwnerType());
		ga.setOwnerId(cmd.getOwnerId());
		ga.setModuleType(FlowModuleType.NO_MODULE.getCode());
		ga.setModuleId(FlowConstants.AUTHORIZATION_MODULE);
		ga.setProjectId(cmd.getOwnerId());
		ga.setProjectType(EntityType.NAMESPACE.getCode());
		ga.setOrganizationId(Long.valueOf(authorizationType));//用来存认证类型type

		
		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setApplyUserId(UserContext.current().getUser().getId());
		createFlowCaseCommand.setReferId(cmd.getOwnerId());
		createFlowCaseCommand.setReferType(cmd.getOwnerType());
		if(authorizationType == PERSONAL_AUTHORIZATION){
			createFlowCaseCommand.setContent("姓名 : "+params.get("name")+"\n证件号码 : "+params.get("certificateNo"));
			createFlowCaseCommand.setTitle("个人认证申请");
		}else{
			createFlowCaseCommand.setContent("组织机构代码 : "+params.get("organizationCode")+"\n企业联系人 : "+params.get("organizationContact"));
			createFlowCaseCommand.setTitle("企业认证申请");
		}
//		createFlowCaseCommand.setProjectId(cmd.getOwnerId());
//		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());
		
		FlowCase flowcase = flowService.createDumpFlowCase(ga, createFlowCaseCommand);
		return flowcase;
	}

	public static String generalContent(ZjgkJsonEntity<List<ZjgkResponse>> entity) {
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
			buffer.append("未定义的返回码 ").append(entity.getErrorCode()).append(" ");
		}
		if(list!=null && list.size()>0){
			for (ZjgkResponse zjgkResponse : list) {
				if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.EXIST_COMMUNITY){
					buffer.append(zjgkResponse.getCommunityName()).append(zjgkResponse.getBuildingName()).append(zjgkResponse.getApartmentName()).append("\n");
				}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.NOT_EXIST_COMMUNITY){
					buffer.append("园区[").append(zjgkResponse.getCommunityName()).append("]不存在\n");
				}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.MULTI_COMMUNITY){
					buffer.append("园区[").append(zjgkResponse.getCommunityName()).append("]名称存在多个\n");
				}
				
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
		getSettinginfo(cmd);
		Map<String, String> params = generalParams(cmd);
		ZjgkJsonEntity<List<ZjgkResponse>> entity = null;
		try {
			String jsonStr = HttpUtils.post(url, params, 10, "UTF-8");
			//向第三方认证。
			entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			//请求成功，返回承租地址，那么创建家庭。
			AuthorizationThirdPartyRecord record = createFamily(cmd,entity,params,ORGANIZATION_AUTHORIZATION);
			//创建工作流
			FlowCase flowCase = createWorkFlow(cmd,entity,ORGANIZATION_AUTHORIZATION,params);
			record.setFlowCaseId(flowCase.getId());
			//记录认证参数和结果，提供给工作流使用
			createRecords(record,ORGANIZATION_AUTHORIZATION);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processGeneralFormDTO(cmd,entity);
	
	}

}
