package com.everhomes.general_approval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_form.GeneralFormIdCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_form.GeneralFormDTO;
import com.everhomes.rest.general_form.GeneralFormDataSourceType;
import com.everhomes.rest.general_form.GeneralFormDataVisibleType;
import com.everhomes.rest.general_form.GeneralFormFieldDTO;
import com.everhomes.rest.general_form.GeneralFormFieldType;
import com.everhomes.rest.general_form.GeneralFormStatus;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class GeneralApprovalServiceImpl implements GeneralApprovalService {

	@Autowired
	private GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private FlowService flowService;

	@Autowired
	private DbProvider dbProvider;

	@Override
	public GetTemplateByApprovalIdResponse getTemplateByApprovalId(
			GetTemplateByApprovalIdCommand cmd) {
		//
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		GetTemplateByApprovalIdResponse response = ConvertHelper.convert(ga,
				GetTemplateByApprovalIdResponse.class);
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		if(form == null )
			throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE, 
					GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
		form.setFormVersion(form.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		//增加一个隐藏的field 用于存放sourceId
		GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
		sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(sourceIdField);
		
		GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
		organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(organizationIdField);
		
		response.setFormFields(fieldDTOs);
		return response;
	}

	@Override
	public GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd) {

		// TODO Auto-generated method stub
		return this.dbProvider.execute((TransactionStatus status) -> {
			Long userId = UserContext.current().getUser().getId();
			GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
					.getApprovalId());
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
					.getFormOriginId());

			if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
				// 使用表单/审批 注意状态 config
				form.setStatus(GeneralFormStatus.RUNNING.getCode());
				this.generalFormProvider.updateGeneralForm(form);
			}
			Flow flow = flowService.getEnabledFlow(ga.getNamespaceId(), ga.getModuleId(),
					ga.getModuleType(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());

			CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
			cmd21.setApplyUserId(userId);
			// cmd21.setReferId(null);
			cmd21.setReferType(FlowReferType.APPROVAL.getCode());
			cmd21.setProjectId(ga.getProjectId());
			cmd21.setProjectType(ga.getProjectType()); 
			//把command作为json传到content里，给flowcase的listener进行处理
			cmd21.setContent(JSON.toJSONString(cmd));
			// 修改正中会工作流显示名称，暂时写死 add by sw 20170331
			if (UserContext.getCurrentNamespaceId().equals(999983)) {
				cmd21.setTitle("办事指南");
//				cmd21.setTitle(ga.getApprovalName());
			}

			FlowCase flowCase = null;
			if(null == flow) {
				// 给他一个默认哑的flow
				GeneralModuleInfo gm = ConvertHelper.convert(ga, GeneralModuleInfo.class);
				gm.setOwnerId(ga.getId());
				gm.setOwnerType(FlowOwnerType.GENERAL_APPROVAL.getCode());
				flowCase = flowService.createDumpFlowCase(gm, cmd21);
			} else {
				cmd21.setFlowMainId(flow.getFlowMainId());
				cmd21.setFlowVersion(flow.getFlowVersion());
				flowCase = flowService.createFlowCase(cmd21);
			}
			
			// 把values 存起来
			for (PostApprovalFormItem val : cmd.getValues()) {
				GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
				obj.setApprovalId(ga.getId());
				obj.setFormVersion(form.getFormVersion());
				obj.setFlowCaseId(flowCase.getId());
				obj.setFieldName(val.getFieldName());
				obj.setFieldType(val.getFieldType());
				obj.setFieldStr3(val.getFieldValue());
				this.generalApprovalValProvider.createGeneralApprovalVal(obj);
			}
			
			GetTemplateByApprovalIdResponse response =  ConvertHelper.convert(ga, GetTemplateByApprovalIdResponse.class);
			response.setFlowCaseId(flowCase.getId());
			List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
			fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
			response.setFormFields(fieldDTOs);
			response.setValues(cmd.getValues());
			return response;
		});
	}

	public GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public void deleteApprovalFormById(GeneralFormIdCommand cmd) {
		// 删除是状态置为invalid
		this.generalFormProvider.invalidForms(cmd.getFormOriginId());
	}

	@Override
	public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
		//
		GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
		ga.setNamespaceId(UserContext.getCurrentNamespaceId());
		ga.setStatus(GeneralFormStatus.CONFIG.getCode());
		
		//新增加审批的时候可能并为设置 formId
//		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
//				.getFormOriginId());
//		ga.setFormVersion(form.getFormVersion());// 目前这个值并没用到
		
		this.generalApprovalProvider.createGeneralApproval(ga);

		return processApproval(ga);
	}

	@Override
	public GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd) {
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());

		if (null != cmd.getSupportType())
			ga.setSupportType(cmd.getSupportType());
		if (null != cmd.getFormOriginId())
			ga.setFormOriginId(cmd.getFormOriginId());
		if (null != cmd.getApprovalName())
			ga.setApprovalName(cmd.getApprovalName());
		this.generalApprovalProvider.updateGeneralApproval(ga);
		return processApproval(ga);
	}

	@Override
	public ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd) {
		//modify by dengs. 20170428 如果OwnerType是 organaization，则转成所管理的  community做查询
		ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(cmd.getOwnerType());
		List<OrganizationCommunity> communityList = null;
		if(belongType == ServiceAllianceBelongType.ORGANAIZATION){
			 communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
		}
		List<GeneralApproval> gas = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						ServiceAllianceBelongType belongType = ServiceAllianceBelongType.fromCode(cmd.getOwnerType());
						List<OrganizationCommunity> communityList = null;
						
						//modify by dengs. 20170428 如果OwnerType是 organaization，则转成所管理的  community做查询
						if(belongType == ServiceAllianceBelongType.ORGANAIZATION){
							 communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
							 Condition conditionOR = null;
							 for (OrganizationCommunity organizationCommunity : communityList) {
								Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(organizationCommunity.getCommunityId())
										.and(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
								if(conditionOR==null){
									conditionOR = condition;
								}else{
									conditionOR.or(condition);
								}
							 }
							 if(conditionOR!=null)
								 query.addConditions(conditionOR);
						}else{
							query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd
									.getOwnerId()));
							query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd
									.getOwnerType()));
						}
						query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS
								.ne(GeneralFormStatus.INVALID.getCode()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd
								.getModuleId()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd
								.getModuleType()));
						EntityType entityType = EntityType.fromCode(cmd.getProjectType());
						//by dengs, 20170509 如果是园区才匹配查询园区相关信息，如果是公司，则不匹配。
						if(entityType == EntityType.COMMUNITY){
							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd
									.getProjectId()));
							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd
									.getProjectType()));
						}
						return query;
					}
				});

		ListGeneralApprovalResponse resp = new ListGeneralApprovalResponse();
		resp.setDtos(gas.stream().map((r) -> {
			return processApproval(r);
		}).collect(Collectors.toList()));
		return resp;
	}

	private GeneralApprovalDTO processApproval(GeneralApproval r) {
		GeneralApprovalDTO result = ConvertHelper.convert(r, GeneralApprovalDTO.class);
		// form name
		if(r.getFormOriginId() != null && !r.getFormOriginId().equals(0l)) {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r
					.getFormOriginId());
			if(form != null) {
				result.setFormName(form.getFormName());	
			}	
		}
		
		// flow
		Flow flow = flowService.getEnabledFlow(r.getNamespaceId(), r.getModuleId(),
				r.getModuleType(), r.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());

		if (null != flow) {
			result.setFlowName(flow.getFlowName());
		}
		return result;
	}

	@Override
	public void deleteGeneralApproval(GeneralApprovalIdCommand cmd) {

		// 删除是状态置为invalid
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		ga.setStatus(GeneralFormStatus.INVALID.getCode());
		this.generalApprovalProvider.updateGeneralApproval(ga);
	}

}
