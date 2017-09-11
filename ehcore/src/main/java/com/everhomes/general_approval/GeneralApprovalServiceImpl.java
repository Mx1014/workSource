package com.everhomes.general_approval;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.entity.EntityType;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.rest.general_approval.*;
import com.everhomes.util.DateHelper;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormNumDTO;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormSubformDTO;
import com.everhomes.rest.general_approval.GeneralFormTemplateType;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListActiveGeneralApprovalCommand;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalServiceImpl.class);
	@Autowired
	private GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	private OrganizationProvider organizationProvider;

    private StringTemplateLoader templateLoader;
    
    private Configuration templateConfig;
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
		if (form == null)
			throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
					GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
		form.setFormVersion(form.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		// 增加一个隐藏的field 用于存放sourceId
		GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
		sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		sourceIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
		sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(sourceIdField);

		GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
		organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
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

			cmd21.setProjectType(ga.getOwnerType());
			cmd21.setProjectId(ga.getOwnerId());

			// 把command作为json传到content里，给flowcase的listener进行处理
			cmd21.setContent(JSON.toJSONString(cmd));
			// 修改正中会工作流显示名称，暂时写死 add by sw 20170331
//			if (UserContext.getCurrentNamespaceId().equals(999983)) {
//				cmd21.setTitle("办事指南");
//				// cmd21.setTitle(ga.getApprovalName());
//			}
			cmd21.setCurrentOrganizationId(cmd.getOrganizationId());
			cmd21.setTitle(ga.getApprovalName());
			FlowCase flowCase = null;
			if (null == flow) {
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

			GetTemplateByApprovalIdResponse response = ConvertHelper.convert(ga,
					GetTemplateByApprovalIdResponse.class);
			response.setFlowCaseId(flowCase.getId());
			List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
			fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
			response.setFormFields(fieldDTOs);
			response.setValues(cmd.getValues());
			return response;
		});
	}

	@Override
	public GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd) {

		GeneralForm form = ConvertHelper.convert(cmd, GeneralForm.class);
		form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
		form.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		form.setStatus(GeneralFormStatus.CONFIG.getCode());
		form.setNamespaceId(UserContext.getCurrentNamespaceId());
		form.setFormVersion(0L);
		form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
		this.generalFormProvider.createGeneralForm(form);
		return processGeneralFormDTO(form);
	}

	private ThreadLocal<Map<String,Integer>> topNumFieldNames = new ThreadLocal<Map<String,Integer>>() {
		protected  Map<String,Integer> initialValue() {
			return new HashMap<>();
		}
	};

	private ThreadLocal<Map<String,Integer>> allNumFieldNames = new ThreadLocal<Map<String,Integer>>() {
		protected Map<String,Integer> initialValue() {
			return new HashMap<>();
		}
	};

	public GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		checkFieldDTOs(fieldDTOs);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	private void checkFieldDTOs(List<GeneralFormFieldDTO> fieldDTOs) {
		topNumFieldNames.set(findTopNumFieldNames(fieldDTOs, null));
		allNumFieldNames.set(findAllNumFieldNames(fieldDTOs));
		for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
			checkFieldDTO(fieldDTO, allNumFieldNames.get());
		}
	}

	/** 找到filedDTOS里面的数字类型的字段名称 */
	private Map<String,Integer> findTopNumFieldNames(List<GeneralFormFieldDTO> fieldDTOs,
			String superFieldName) {
		Map<String,Integer> fieldNames = new HashMap<>();
		if(null == fieldDTOs)
			return fieldNames;
		for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
			if (fieldDTO.getFieldType().equals(GeneralFormFieldType.NUMBER_TEXT.getCode())){
				fieldNames.put(superFieldName == null ? fieldDTO.getFieldDisplayName()
						: (superFieldName + "." + fieldDTO.getFieldDisplayName()),1);
				}
		}
		return fieldNames;
	}

	/**
	 * 找到filedDTOS里面的数字类型的字段名称+子表单类型的内部数字类型名称 现在只支持一层的子表单
	 * */
	private Map<String,Integer> findAllNumFieldNames(List<GeneralFormFieldDTO> fieldDTOs) {
		Map<String,Integer> fieldNames = new HashMap<>();
		fieldNames.putAll(findTopNumFieldNames(fieldDTOs, null));
		if(null == fieldDTOs)
			return fieldNames;
		for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
			if (fieldDTO.getFieldType().equals(GeneralFormFieldType.SUBFORM.getCode())) {
				GeneralFormSubformDTO subFromExtra = ConvertHelper.convert(
						fieldDTO.getFieldExtra(), GeneralFormSubformDTO.class);
				fieldNames.putAll(findTopNumFieldNames(subFromExtra.getFormFields(),
						fieldDTO.getFieldDisplayName()));
			}else if (fieldDTO.getFieldType().equals(GeneralFormFieldType.NUMBER_TEXT.getCode())){
			}
		}
		return fieldNames;
	}

	/** 检查客户端传的fieldDTO是否合法 */
	private void checkFieldDTO(GeneralFormFieldDTO fieldDTO, Map<String,Integer> map) {
		switch (GeneralFormFieldType.fromCode(fieldDTO.getFieldType())) {
		case SUBFORM:
			// 对于子表单要检查所有的字段

			GeneralFormSubformDTO subFromExtra = JSONObject.parseObject(fieldDTO.getFieldExtra(),
					GeneralFormSubformDTO.class);
			LOGGER.debug("FIELD EXTRA"+fieldDTO.getFieldExtra());
			LOGGER.debug("field extra dto "+subFromExtra);
			Map<String,Integer> subNameMap = findTopNumFieldNames(subFromExtra.getFormFields(),
					fieldDTO.getFieldDisplayName());
			subNameMap.putAll(topNumFieldNames.get());
			for (GeneralFormFieldDTO subFormFieldDTO : subFromExtra.getFormFields()) {

				checkFieldDTO(subFormFieldDTO, subNameMap);
			}
			break;
		case NUMBER_TEXT:
			// 对于数字要检查默认公式
			GeneralFormNumDTO numberExtra = ConvertHelper.convert(fieldDTO.getFieldExtra(),
					GeneralFormNumDTO.class);
			if (null == numberExtra.getDefaultValue())
				break;
			if(!checkNumberDefaultValue(numberExtra.getDefaultValue(), allNumFieldNames.get())){ 
				throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
						GeneralApprovalServiceErrorCode.ERROR_FORMULA_CHECK, "ERROR_FORMULA_CHECK");	
			}
			break;
		default:
			break;
		}
	}

	/**
	* @param defaultValue
	* 公式
	* @param map
	* 允许的参数和参数被replace的值--一般是1
	* @return 如果校验通过,返回true, 否则返回 false;
	 * 检验数字文本框的默认公式 
	 * 1. SUM（）里面必须是子表单变量，SUM（变量）算一个变量 
	 * 2. 两个变量之间必须有+、-、*、/中的一个符号
	 * 3. 变量与纯数字之间必须有+、-、*、/中的一个符号 
	 * 4. 括号必须成对出现
	 * 
	 * @param defaultValue 公式-默认值
	 * @param map 合法的变量名map
	 */
	@Override
	public Boolean checkNumberDefaultValue(String defaultValue, Map<String,Integer> map) {
		// TODO Auto-generated method stub
		//1.去空格
		defaultValue = defaultValue.trim();
		//2验证变量-把变量都置为0
        try {
            Template freeMarkerTemplate = null;
            String templateKey = "templateKey";
            try {
                templateConfig.getTemplate(templateKey, "UTF8");
            }catch(Exception e) {  
            } 
            String templateText = defaultValue;
            templateLoader.putTemplate(templateKey, templateText);
            freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
            if(freeMarkerTemplate != null) {
            	defaultValue =  FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, map);
            }
        } catch(Exception e) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map, e);
    			return false;
            }
        }
		//3replace sum
        defaultValue.replace("sum", "");
		//4验证()是成对出现并后接数字
        char[] valueArray = defaultValue.toCharArray();
        int anchor = 0;
        
        for(int i = 0 ;i<=defaultValue.length();i++){
        	if(valueArray[i] == '('){
        		anchor++;
        		if(valueArray[i+1] == '+'||valueArray[i+1] == '-'||valueArray[i+1] == '*'||valueArray[i+1] == '\\'){
                    LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map );
                    return false;
        		}
        	}
        	else if(valueArray[i] == ')'){
            	anchor--;
        		if(valueArray[i-1] == '+'||valueArray[i-1] == '-'||valueArray[i-1] == '*'||valueArray[i-1] == '\\'){
                    LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map );
                    return false;
        		}
        	}
        	if(anchor <0){ 
                LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map );
                return false;
        	}  
        }
    	if(anchor != 0){ 
            LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map );
            return false;
    	}  
		//5 正则表达式验证
    	String regex = "^\\d+([\\+\\-\\*\\/]{1}\\d+)*$"; 
    	if(!match(regex, defaultValue)){ 
            LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map );
            return false;
    	}
    	return true;
	}

	/**
	* @param regex
	* 正则表达式字符串
	* @param str
	* 要匹配的字符串
	* @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	*/
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	@Override
	public void deleteApprovalFormById(ApprovalFormIdCommand cmd) {
		// 删除是状态置为invalid
		this.generalFormProvider.invalidForms(cmd.getFormOriginId());

		//  删除与表单相关控件组
		this.generalFormProvider.deleteGeneralFormGroupsByFormOriginId(cmd.getFormOriginId());

	}

	@Override
	public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
		//
		GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
		ga.setNamespaceId(UserContext.getCurrentNamespaceId());
		ga.setStatus(GeneralApprovalStatus.INVALID.getCode());

		// 新增加审批的时候可能并为设置 formId
		// GeneralForm form =
		// this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
		// .getFormOriginId());
		// ga.setFormVersion(form.getFormVersion());// 目前这个值并没用到

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

		List<GeneralApproval> gas = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {

					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						List<OrganizationCommunity> communityList = null;
						
						//modify by dengs. 20170428 如果OwnerType是 organaization，则转成所管理的  community做查询
						if(EntityType.ORGANIZATIONS.getCode().equals(cmd.getOwnerType())){
							 communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
							 Condition conditionOR = null;
							 for (OrganizationCommunity organizationCommunity : communityList) {
								Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(organizationCommunity.getCommunityId())
										.and(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
								if(conditionOR==null){
									conditionOR = condition;
								}else{
									conditionOR = conditionOR.or(condition);
								}
							 }
							 if(conditionOR!=null){
								 Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId()).and(
										 Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType())
								 );
								 conditionOR = conditionOR.or(condition);
								 query.addConditions(conditionOR);
							 }
						}else{
							query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd
									.getOwnerId()));
							query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd
									.getOwnerType()));
						}
						query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS
								.ne(GeneralApprovalStatus.DELETED.getCode()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd
								.getModuleId()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd
								.getModuleType()));

						if(null != cmd.getProjectId())
							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd.getProjectId()));
						if(null != cmd.getProjectType())
							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd.getProjectType()));
						if (null != cmd.getStatus())
							query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(cmd
									.getStatus()));
						//注释掉, 2017年6月16日 不想过滤的条件就不要传好了,不用在这里假先知
//						EntityType entityType = EntityType.fromCode(cmd.getProjectType());
//						//by dengs, 20170509 如果是园区才匹配查询园区相关信息，如果是公司，则不匹配。
//						if(entityType == EntityType.COMMUNITY){
//							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd
//									.getProjectId()));
//							query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd
//									.getProjectType()));
//						}
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
		if (r.getFormOriginId() != null && !r.getFormOriginId().equals(0l)) {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r
					.getFormOriginId());
			if (form != null) {
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

		// 删除是状态置为deleted
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		ga.setStatus(GeneralApprovalStatus.DELETED.getCode());
		this.generalApprovalProvider.updateGeneralApproval(ga);
	}

	@Override
	public GeneralFormDTO getApprovalForm(ApprovalFormIdCommand cmd) {
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
				.getFormOriginId());
		return processGeneralFormDTO(form);
	}

 
	@Override
	public void enableGeneralApproval(GeneralApprovalIdCommand cmd) { 
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		ga.setStatus(GeneralApprovalStatus.RUNNING.getCode());
		this.generalApprovalProvider.updateGeneralApproval(ga);
	}

	@Override
	public void disableGeneralApproval(GeneralApprovalIdCommand cmd) { 
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
		this.generalApprovalProvider.updateGeneralApproval(ga);
	}

	/**
	 * 取状态不为失效的form
	 * */
	@Override
	public ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd) {

		List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
																		SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd
								.getOwnerType()));
						query.addConditions(Tables.EH_GENERAL_FORMS.STATUS
								.ne(GeneralFormStatus.INVALID.getCode()));
						return query;
					}
				});
		ListGeneralFormResponse resp = new ListGeneralFormResponse();
		resp.setForms(forms.stream().map((r) -> {
			return processGeneralFormDTO(r);
		}).collect(Collectors.toList()));
		return resp;
	}

	@Override
	public GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd) {
		return null;
	}

	@Override
	public ListGeneralApprovalResponse listActiveGeneralApproval(
			ListActiveGeneralApprovalCommand cmd) {
		ListGeneralApprovalCommand cmd2 = ConvertHelper.convert(cmd, ListGeneralApprovalCommand.class);
		cmd2.setStatus(GeneralApprovalStatus.RUNNING.getCode());
		if(null == cmd2.getModuleType())
			cmd2.setModuleType(FlowModuleType.NO_MODULE.getCode());
		if(null == cmd2.getModuleId())
			cmd2.setModuleId(GeneralApprovalController.MODULE_ID);
		if(null == cmd2.getOwnerType())
			cmd2.setOwnerType("EhOrganizations"); 
		
		
		return listGeneralApproval(cmd2);
	} 
//	@Override
//	public GetTemplateByApprovalIdResponse getActiveGeneralFormByOriginId(GetActiveGeneralFormByOriginIdCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public GetTemplateByApprovalIdResponse postForm(PostFormCommand cmd) {
//		// TODO Auto-generated method stub
//		return null;
//	} 


}
