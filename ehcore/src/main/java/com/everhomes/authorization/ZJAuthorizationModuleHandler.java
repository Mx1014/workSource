package com.everhomes.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.address.AddressService;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.http.HttpUtils;
import com.everhomes.locale.LocaleStringService;
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
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.techpark.expansion.ApplyEntryResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

@Component(AuthorizationModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX+"EhNamespaces"+999971)
public class ZJAuthorizationModuleHandler implements AuthorizationModuleHandler {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ZJAuthorizationModuleHandler.class);


	private String url = "http://139.129.220.146:3578/openapi/Authenticate";

	private String appKey = "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21";

	private String secretKey = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";

	private String[] communites = {"人才公寓"};

	private enum CertificateType{
		ID_CARD_NO("身份证", "1");

		private String type;
		private String code;

		public String getType() {
			return type;
		}

		public String getCode() {
			return code;
		}

		CertificateType(String type, String code){
			this.type = type;
			this.code = code;
		}
		public static CertificateType fromType(String type) {
			if (type != null && !type.isEmpty()) {
				for (CertificateType certificateType : CertificateType.values()) {
					if (certificateType.getType().equals(type)) {
						return certificateType;
					}
				}
			}
			return null;
		}

	}


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
	private LocaleStringService localeStringService;

    @Autowired
    private AuthorizationThirdPartyFormProvider authorizationThirdPartyFormProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    private boolean isdebug;

    private int length;

    private boolean isdebug(){
    	return isdebug;
    }
	@Override
	public PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd) {
		return authorization(cmd,PERSONAL_AUTHORIZATION);
	}

	@Override
	public PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd) {
		return authorization(cmd,ORGANIZATION_AUTHORIZATION);
	}

	public PostGeneralFormDTO authorization(PostGeneralFormCommand cmd, String type) {
		getSettinginfo(cmd);
		Map<String, String> params = generateParams(cmd,type);
		if(ORGANIZATION_AUTHORIZATION.equals(type)){
			length = params.get("organizationCode").length();
		}else{
			length = params.get("certificateNo").length();
		}
		ZjgkJsonEntity<List<ZjgkResponse>> entity = new ZjgkJsonEntity<List<ZjgkResponse>>();
		//调试模式
		if(isdebug()){
			generateBebugEntity(entity);
		}
		//正常模式
		else{
			String jsonStr = null;
			try {
				jsonStr = HttpUtils.post(url, params, 20, "UTF-8");
				//向第张江认证。
				entity = JSONObject.parseObject(jsonStr,new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
			} catch (Exception e) {
				e.printStackTrace();
				entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_SEND_REQUEST_EXCEPTION);
				entity.setErrorDescription("请求失败");
			}
		}
		//请求成功，返回承租地址，那么创建家庭。
		AuthorizationThirdPartyRecord record = createFamily(cmd,entity,params,type);
		//创建工作流
		FlowCase flowCase = createWorkFlow(cmd,entity,type,params);
		record.setFlowCaseId(flowCase.getId());
		//记录认证参数和结果，提供给工作流使用
		createRecords(record,type);

		return processGeneralFormDTO(cmd,entity,type,flowCase);
	}

	//生成调试对象。
	private ZjgkJsonEntity<List<ZjgkResponse>> generateBebugEntity(ZjgkJsonEntity<List<ZjgkResponse>> entity) {
		List<ZjgkResponse> responses = new ArrayList<>();
		if(length == 18){
			entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_SUCCESS);
			entity.setErrorDescription("认证成功");
			ZjgkResponse response = new ZjgkResponse();
			response.setCommunityName(communites[(int)(Math.random()*communites.length)]); // TODO
			response.setBuildingName(String.valueOf(generateRandomNumber(2)));// TODO
			response.setApartmentName(String.valueOf(generateRandomNumber(3)));
			response.setAddress(response.getCommunityName()+response.getBuildingName()+"-"+response.getApartmentName());
			responses.add(response);
		}else if(length == 2){
			entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_MISMATCHING);
			entity.setErrorDescription("签名证书非法");
		}else{
			entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_UNRENT);
			entity.setErrorDescription("已退租");
		}
		entity.setResponse(responses);
		return entity;

	}
	//获取对应的对接放的url，appkey,secretkey
	private void getSettinginfo(PostGeneralFormCommand cmd) {
		this.isdebug = configProvider.getBooleanValue("debug.flag",false);
		String stringCommunity = configProvider.getValue("zj_communites","人才公寓");
		this.communites = stringCommunity.split(",");
		AuthorizationThirdPartyForm setting = authorizationThirdPartyFormProvider.getFormSource(UserContext.getCurrentNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(),cmd.getSourceType(),cmd.getSourceId());
		this.url = setting.getAuthorizationUrl();
		this.appKey = setting.getAppKey();
		this.secretKey = setting.getSecretKey();
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

	private PostGeneralFormDTO processGeneralFormDTO(PostGeneralFormCommand cmd, ZjgkJsonEntity<List<ZjgkResponse>> entity,String type, FlowCase flowCase) {
		PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);

		List<PostApprovalFormItem> items = new ArrayList<>();
		PostApprovalFormItem item = new PostApprovalFormItem();
		item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
//        if(PERSONAL_AUTHORIZATION.equals(type)){
//        	String documentflow = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE,
//    				AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL_S);
//    		String[] documentflows = documentflow.split("\\|");
//        	item.setFieldValue(generalContent(entity,documentflows));
//		}else{
//			String documentflow = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE,
//    				AuthorizationErrorCode.ORGANIZATION_BACK_CODE_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.ORGANIZATION_BACK_CODE_DETAIL_S);
//    		String[] documentflows = documentflow.split("\\|");
//        	item.setFieldValue(generalContent(entity,documentflows));
//		}
		ApplyEntryResponse resp = new ApplyEntryResponse();
		resp.setUrl(processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
		item.setFieldValue(StringHelper.toJsonString(resp));
		items.add(item);
		dto.getValues().addAll(items);
		return dto;
	}

	//表单提交，对接工作流之后的url，返回给客户端跳转
	private String processFlowURL(Long flowCaseId, String string, Long moduleId) {
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+string+"&moduleId="+moduleId  ;
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
			BatchApproveMemberCommand batchAppCmd = generateBatchApproveMemberCommand(record);
			try{
				// 认证状态设置为通过，家庭认证通过。
				familyService.adminBatchApproveMember(batchAppCmd);
			}catch(DataAccessException e){
				//开发中，发现如果认证返回的小区，如果在数据库中没有查到和管理公司（eh_organization_communities）的关联,会抛出异常。
				//在此处理为认证失败
				record.setErrorCode(ZjgkJsonEntity.ERRORCODE_NOT_PASS_IN_ZUOLIN);
				entity.setErrorCode(ZjgkJsonEntity.ERRORCODE_NOT_PASS_IN_ZUOLIN);
				entity.setErrorDescription("认证异常");
				entity.getResponse().clear();
				record.setResultJson(StringHelper.toJsonString(entity));
				//用户退出还未认证的家庭。
				for (ApproveMemberCommand r : batchAppCmd.getMembers()) {
					DisclaimAddressCommand disCmd = new DisclaimAddressCommand();
					disCmd.setAddressId(r.getAddressId());
					addressService.disclaimAddress(disCmd);
					e.printStackTrace();
				}
			}
			return record;
		}else if(entity.isUnrent()){//已退租，退出家庭。
			leaveThirdPartyAuthAddress(authorizationType);
		}
		return generateUserAuthorizationRecord(cmd,params,authorizationType,entity);
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
		if(familyList != null)
			for (FamilyDTO r : familyList) {
				if(isInleaveRecord(record, r.getAddressId())){
					DisclaimAddressCommand cmd = new DisclaimAddressCommand();
					cmd.setAddressId(r.getAddressId());
					addressService.disclaimAddress(cmd);
				}
			}
	}

	//生成批量认证的command。
	private BatchApproveMemberCommand generateBatchApproveMemberCommand(AuthorizationThirdPartyRecord record) {
		//获取用户所有的家庭
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		//只有第三方认证的家庭，才做
		BatchApproveMemberCommand batchCmd = new BatchApproveMemberCommand();
		//过滤出在第三方认证的信息，直接认证成功。
		List<ApproveMemberCommand> members = familyList.stream()
				.filter(r ->{
					return isInleaveRecord(record, r.getAddressId());
				})
				.map(r -> {
					ApproveMemberCommand cmd = new ApproveMemberCommand();
					cmd.setAddressId(r.getAddressId());
					cmd.setId(r.getId());
					cmd.setMemberUid(UserContext.current().getUser().getId());
					return cmd;
				}).collect(Collectors.toList());
		batchCmd.setMembers(members);
		return batchCmd;
	}

	//在记录
	private boolean isInleaveRecord(AuthorizationThirdPartyRecord record, long addressId){
		ZjgkJsonEntity<List<ZjgkResponse>> entity = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		if(entity != null &&  entity.getResponse() != null)
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
		if(communities == null || communities.size() == 0){
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
		//设置到resultJson里面了。
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
		String document = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE,
				AuthorizationErrorCode.WORK_FLOW_TITLE, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.WORK_FLOW_TITLE_S);
		String[] documents = document.split("\\|");
		if(authorizationType == PERSONAL_AUTHORIZATION){
			createFlowCaseCommand.setContent(documents[0]+params.get("name")+"\n"+documents[1]+params.get("certificateNo"));
			createFlowCaseCommand.setTitle(documents[2]);
		}else{
			createFlowCaseCommand.setContent(documents[3]+params.get("organizationCode")+"\n"+documents[4]+params.get("organizationContact"));
			createFlowCaseCommand.setTitle(documents[5]);
		}
//		createFlowCaseCommand.setProjectId(cmd.getOwnerId());
//		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

		FlowCase flowcase = flowService.createDumpFlowCase(ga, createFlowCaseCommand);
		return flowcase;
	}

//	public String generalContent(ZjgkJsonEntity<List<ZjgkResponse>> entity, String[] documentflows) {
//		StringBuffer buffer = new StringBuffer();
//		List<ZjgkResponse> list = entity.getResponse();
//		if(entity.isMismatching()){
//			buffer.append("\n")
//			.append(documentflows[0])
//			.append("\n")
//			.append(documentflows[1])
//			.append("\n")
//			.append(documentflows[2])
//			.append("\n");
//			return buffer.toString();
//		}
//		if(entity.isUnrent()){
//			buffer.append("\n")
//			.append(documentflows[3])
//			.append("\n");
//			return buffer.toString();
//		}
//		if(entity.isRequestFail()){
//			buffer.append("\n")
//			.append(documentflows[11])
//			.append("\n");
//			return buffer.toString();
//		}
//		if(entity.isNotPassInZuolin()){
//			buffer.append("\n")
//			.append(documentflows[12])
//			.append("\n");
//			return buffer.toString();
//		}
//		if(entity.isSuccess())
//		{
//			buffer.append("\n").append(documentflows[4]).append("\n");
//		}
//		else{
//			buffer.append("\n").append(documentflows[5]).append(entity.getErrorCode()).append(" ");
//		}
//
//		if(list!=null && list.size()>0){
//			for (ZjgkResponse zjgkResponse : list) {
//				if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.EXIST_COMMUNITY){
//					buffer.append(zjgkResponse.getBuildingName()).append(zjgkResponse.getApartmentName());
//				}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.NOT_EXIST_COMMUNITY){
//					buffer.append(documentflows[6]).append(zjgkResponse.getCommunityName()).append(documentflows[7]).append("\n");
//				}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.MULTI_COMMUNITY){
//					buffer.append(documentflows[8]).append(zjgkResponse.getCommunityName()).append(documentflows[9]).append("\n");
//				}
//
//			}
//		}
//		return buffer.toString();
//	}

	private Map<String, String> generateParams(PostGeneralFormCommand cmd, String type){
		List<PostApprovalFormItem> values = cmd.getValues();
		Map<String, String> params= new HashMap<String,String>();
//		params.put("organizationCode","1232123");
//		params.put("organizationContact","Test");
//		params.put("organizationPhone","1234567890");
//		params.put("phone","18761600673");
//		params.put("name","dsf");
//		params.put("certificateType","1");
//		params.put("certificateNo","321201199307070219");
		for (PostApprovalFormItem item : values) {
			GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(item.getFieldType());
			switch (fieldType) {
				case SINGLE_LINE_TEXT:
				case MULTI_LINE_TEXT:
				case INTEGER_TEXT:
				case NUMBER_TEXT:
				case DROP_BOX:
				case DATE:
					PostApprovalFormTextValue value = JSONObject.parseObject(item.getFieldValue(),PostApprovalFormTextValue.class);
					params.put(item.getFieldName(), value.getText());
					break;
				default:
					break;
			}
			if(item.getFieldName().equals("certificateType")){
//				params.put(item.getFieldName(), "1");
				CertificateType certificateType = CertificateType.fromType(params.get("certificateType"));
				if(certificateType != null) {
					params.put(item.getFieldName(), certificateType.getCode());
				}
			}
		}
		params.put("appKey", appKey);
		params.put("timestamp", ""+System.currentTimeMillis());
		params.put("nonce", ""+(long)(Math.random()*100000));
		params.put("type", type);
		String signature = SignatureHelper.computeSignature(params, secretKey);
		params.put("signature", signature);
		return params;

	}

}
