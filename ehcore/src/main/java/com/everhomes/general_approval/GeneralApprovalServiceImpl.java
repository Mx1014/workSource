package com.everhomes.general_approval;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseTree;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowNodeProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormFieldValueProcessor;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalErrorCode;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowNodeLogDTO;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowSubjectDTO;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.CreateOrUpdateGeneralFormValuesWithFlowCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormNumDTO;
import com.everhomes.rest.general_approval.GeneralFormRenderType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormSubformDTO;
import com.everhomes.rest.general_approval.GeneralFormTemplateType;
import com.everhomes.rest.general_approval.GetGeneralFormsAndValuesByFlowNodeCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.GetUserRealNameCommand;
import com.everhomes.rest.general_approval.ListActiveGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.OrderGeneralApprovalsCommand;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.QueryGeneralFormByFlowNodeType;
import com.everhomes.rest.general_approval.SetGeneralApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.VerifyApprovalNameCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.server.schema.Tables;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.workReport.WorkReportService;
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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class GeneralApprovalServiceImpl implements GeneralApprovalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalServiceImpl.class);

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private PunchService punchService;

    @Autowired
    private WorkReportService workReportService;
    @Autowired
    private GeneralFormFieldValueProcessor generalFormFieldValueProcessor;
    @Autowired
    private FlowNodeProvider flowNodeProvider;
    @Autowired
    private FlowEventLogProvider flowEventLogProvider;
    @Autowired
    private GeneralApprovalValChangeLogUtils generalApprovalValChangeLogUtils;
    @Autowired
    private DbProvider dbProvider;
    private StringTemplateLoader templateLoader;
    private Configuration templateConfig;

    private static final List<String> NON_EDITABLE_FIELD_TYPES;

    static {
        NON_EDITABLE_FIELD_TYPES = new ArrayList<>();
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.CONTACT.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.ASK_FOR_LEAVE.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.CANCEL_FOR_LEAVE.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.BUSINESS_TRIP.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.OVERTIME.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.GO_OUT.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.ABNORMAL_PUNCH.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.EMPLOY_APPLICATION.getCode());
        NON_EDITABLE_FIELD_TYPES.add(GeneralFormFieldType.DISMISS_APPLICATION.getCode());
    }

    @Override
    public GetTemplateByApprovalIdResponse getTemplateByApprovalId(
            GetTemplateByApprovalIdCommand cmd) {

        //  此接口疑似未使用 23/05/2018

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
        return null;
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

    private ThreadLocal<Map<String, Integer>> topNumFieldNames = new ThreadLocal<Map<String, Integer>>() {
        protected Map<String, Integer> initialValue() {
            return new HashMap<>();
        }
    };

    private ThreadLocal<Map<String, Integer>> allNumFieldNames = new ThreadLocal<Map<String, Integer>>() {
        protected Map<String, Integer> initialValue() {
            return new HashMap<>();
        }
    };

    private GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        List<GeneralFormFieldDTO> fieldDTOs;
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

    /**
     * 找到filedDTOS里面的数字类型的字段名称
     */
    private Map<String, Integer> findTopNumFieldNames(List<GeneralFormFieldDTO> fieldDTOs,
                                                      String superFieldName) {
        Map<String, Integer> fieldNames = new HashMap<>();
        if (null == fieldDTOs)
            return fieldNames;
        for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
            if (fieldDTO.getFieldType().equals(GeneralFormFieldType.NUMBER_TEXT.getCode())) {
                fieldNames.put(superFieldName == null ? fieldDTO.getFieldDisplayName()
                        : (superFieldName + "." + fieldDTO.getFieldDisplayName()), 1);
            }
        }
        return fieldNames;
    }

    /**
     * 找到filedDTOS里面的数字类型的字段名称+子表单类型的内部数字类型名称 现在只支持一层的子表单
     */
    private Map<String, Integer> findAllNumFieldNames(List<GeneralFormFieldDTO> fieldDTOs) {
        Map<String, Integer> fieldNames = new HashMap<>();
        fieldNames.putAll(findTopNumFieldNames(fieldDTOs, null));
        if (null == fieldDTOs)
            return fieldNames;
        for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
            if (fieldDTO.getFieldType().equals(GeneralFormFieldType.SUBFORM.getCode())) {
                GeneralFormSubformDTO subFromExtra = ConvertHelper.convert(
                        fieldDTO.getFieldExtra(), GeneralFormSubformDTO.class);
                fieldNames.putAll(findTopNumFieldNames(subFromExtra.getFormFields(),
                        fieldDTO.getFieldDisplayName()));
            } else if (fieldDTO.getFieldType().equals(GeneralFormFieldType.NUMBER_TEXT.getCode())) {
            }
        }
        return fieldNames;
    }

    /**
     * 检查客户端传的fieldDTO是否合法
     */
    private void checkFieldDTO(GeneralFormFieldDTO fieldDTO, Map<String, Integer> map) {
        switch (GeneralFormFieldType.fromCode(fieldDTO.getFieldType())) {
            case SUBFORM:
                // 对于子表单要检查所有的字段

                GeneralFormSubformDTO subFromExtra = JSONObject.parseObject(fieldDTO.getFieldExtra(),
                        GeneralFormSubformDTO.class);
                LOGGER.debug("FIELD EXTRA" + fieldDTO.getFieldExtra());
                LOGGER.debug("field extra dto " + subFromExtra);
                Map<String, Integer> subNameMap = findTopNumFieldNames(subFromExtra.getFormFields(),
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
                if (!checkNumberDefaultValue(numberExtra.getDefaultValue(), allNumFieldNames.get())) {
                    throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
                            GeneralApprovalServiceErrorCode.ERROR_FORMULA_CHECK, "ERROR_FORMULA_CHECK");
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param defaultValue 公式
     * @param map          允许的参数和参数被replace的值--一般是1
     * @param defaultValue 公式-默认值
     * @param map          合法的变量名map
     * @return 如果校验通过, 返回true, 否则返回 false;
     * 检验数字文本框的默认公式
     * 1. SUM（）里面必须是子表单变量，SUM（变量）算一个变量
     * 2. 两个变量之间必须有+、-、*、/中的一个符号
     * 3. 变量与纯数字之间必须有+、-、*、/中的一个符号
     * 4. 括号必须成对出现
     */
    @Override
    public Boolean checkNumberDefaultValue(String defaultValue, Map<String, Integer> map) {
        // TODO Auto-generated method stub
        //1.去空格
        defaultValue = defaultValue.trim();
        //2验证变量-把变量都置为0
        try {
            Template freeMarkerTemplate = null;
            String templateKey = "templateKey";
            try {
                templateConfig.getTemplate(templateKey, "UTF8");
            } catch (Exception e) {
            }
            String templateText = defaultValue;
            templateLoader.putTemplate(templateKey, templateText);
            freeMarkerTemplate = templateConfig.getTemplate(templateKey, "UTF8");
            if (freeMarkerTemplate != null) {
                defaultValue = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate, map);
            }
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map, e);
                return false;
            }
        }
        //3replace sum
        defaultValue.replace("sum", "");
        //4验证()是成对出现并后接数字
        char[] valueArray = defaultValue.toCharArray();
        int anchor = 0;

        for (int i = 0; i <= defaultValue.length(); i++) {
            if (valueArray[i] == '(') {
                anchor++;
                if (valueArray[i + 1] == '+' || valueArray[i + 1] == '-' || valueArray[i + 1] == '*' || valueArray[i + 1] == '\\') {
                    LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map);
                    return false;
                }
            } else if (valueArray[i] == ')') {
                anchor--;
                if (valueArray[i - 1] == '+' || valueArray[i - 1] == '-' || valueArray[i - 1] == '*' || valueArray[i - 1] == '\\') {
                    LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map);
                    return false;
                }
            }
            if (anchor < 0) {
                LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map);
                return false;
            }
        }
        if (anchor != 0) {
            LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map);
            return false;
        }
        //5 正则表达式验证
        String regex = "^\\d+([\\+\\-\\*\\/]{1}\\d+)*$";
        if (!match(regex, defaultValue)) {
            LOGGER.error("Invalid defaultValue [" + defaultValue + "]or map " + map);
            return false;
        }
        return true;
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
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

        /***    更改与表单相关业务的状态    ***/
        //  流程审批
        disableApprovalByFormOriginId(cmd.getFormOriginId(), 52000L, "any-module");
        //  工作汇报
        workReportService.disableWorkReportByFormOriginId(cmd.getFormOriginId(), 54000L, "any-module");
    }

    @Override
    public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {

        GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
        Long userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        ga.setNamespaceId(namespaceId);
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOwnerId()));
        generalApprovalProvider.createGeneralApproval(ga);

        return processApproval(ga);
    }

    @Override
    public GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd) {

        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if (ga == null)
            return null;
        ga.setApprovalName(cmd.getApprovalName());
        ga.setApprovalRemark(cmd.getApprovalRemark());
        if (null != cmd.getSupportType())
            ga.setSupportType(cmd.getSupportType());
        updateGeneralApproval(ga);

        return processApproval(ga);
    }

    private void updateGeneralApproval(GeneralApproval ga) {
        Long userId = UserContext.currentUserId();
        ga.setOperatorUid(userId);
        ga.setOperatorName(getUserRealName(userId, ga.getOrganizationId()));
        generalApprovalProvider.updateGeneralApproval(ga);
    }

    private String getUserRealName(Long userId, Long ownerId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, ownerId);
        if (member != null)
            return member.getContactName();
        //  若没有真实姓名则返回空
        return null;
    }

    @Override
    public GeneralApprovalDTO setGeneralApprovalForm(SetGeneralApprovalFormCommand cmd) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if (ga == null)
            return null;
        ga.setFormOriginId(cmd.getFormOriginId());
        updateGeneralApproval(ga);
        return processApproval(ga);
    }

    @Override
    public void orderGeneralApprovals(OrderGeneralApprovalsCommand cmd) {
        if (cmd.getDtos() == null || cmd.getDtos().size() == 0)
            return;
        for (GeneralApprovalDTO dto : cmd.getDtos()) {
            GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(dto.getId());
            if (ga == null)
                continue;
            //  update the order
            ga.setDefaultOrder(dto.getDefaultOrder());
            generalApprovalProvider.updateGeneralApproval(ga);
        }
    }

    @Override
    public ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd) {
        ListGeneralApprovalResponse resp = new ListGeneralApprovalResponse();

        List<GeneralApproval> results = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(), Integer.MAX_VALUE - 1, (locator, query) -> {
            //  1-(1)when ownerType is ORGANIZATION then process it like community (dengs at 20170428)
            if (EntityType.ORGANIZATIONS.getCode().equals(cmd.getOwnerType()) && FlowModuleType.SERVICE_ALLIANCE.getCode().equals(cmd.getModuleType())) {
                setServiceAllianceQuery(cmd, query);
            } else {
                //  1-(2)normal operation (nan.rong at 10/16/2017)
                query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId()));
                query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType()));
                query.addOrderBy(Tables.EH_GENERAL_APPROVALS.DEFAULT_ORDER.asc());
                query.addOrderBy(Tables.EH_GENERAL_APPROVALS.ID.desc());
            }
            query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd.getModuleId()));
            query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd.getModuleType()));

            if (null != cmd.getProjectId())
                query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd.getProjectId()));
            if (null != cmd.getProjectType())
                query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd.getProjectType()));
            if (null != cmd.getStatus())
                query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(cmd.getStatus()));
            return query;
        });

        resp.setDtos(results.stream().map((r) -> {
            //  2-(1)service alliance special operation
            if (FlowModuleType.SERVICE_ALLIANCE.getCode().equals(r.getModuleType())) {
                long oid = r.getModuleId();
                r.setModuleId(40500L);
                r.setModuleType(FlowModuleType.NO_MODULE.getCode());
                GeneralApprovalDTO dto = processApproval(r);
                dto.setModuleId(oid);
                dto.setModuleType(FlowModuleType.SERVICE_ALLIANCE.getCode());
                return dto;
            } else {
                //  2-(2)
                GeneralApprovalDTO dto = processApproval(r);
                dto.setScopes(listGeneralApprovalScopes(r.getNamespaceId(), r.getId()));
                return dto;
            }
        }).collect(Collectors.toList()));
        return resp;
    }

    private void setServiceAllianceQuery(ListGeneralApprovalCommand cmd, SelectQuery<? extends Record> query) {
        List<OrganizationCommunity> communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
        Condition conditionOR = null;
        for (OrganizationCommunity organizationCommunity : communityList) {
            Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(organizationCommunity.getCommunityId())
                    .and(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()));
            if (conditionOR == null) {
                conditionOR = condition;
            } else {
                conditionOR = conditionOR.or(condition);
            }
        }
        if (conditionOR != null) {
            Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId())
                    .and(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType())
                    );
            conditionOR = conditionOR.or(condition);
            query.addConditions(conditionOR);
        }

        if (null != cmd.getProjectId())
            query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd.getProjectId()));
        if (null != cmd.getProjectType())
            query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd.getProjectType()));
        if (null != cmd.getStatus())
            query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(cmd.getStatus()));
    }

    private GeneralApprovalDTO processApproval(GeneralApproval r) {
        GeneralApprovalDTO result = ConvertHelper.convert(r, GeneralApprovalDTO.class);
        // form name
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r.getFormOriginId());
        if (form != null) {
            result.setFormName(form.getFormName());
        }

        // flow
        Flow flow = flowService.getEnabledFlow(r.getNamespaceId(), r.getModuleId(), r.getModuleType(), r.getId(),
                FlowOwnerType.GENERAL_APPROVAL.getCode());
        if (null != flow) {
            result.setFlowName(flow.getFlowName());
        }

        //  approval icon
        if (result.getIconUri() != null)
            result.setIconUrl(contentServerService.parserUri(result.getIconUri()));
        return result;
    }

    @Override
    public List<GeneralApprovalScopeMapDTO> listGeneralApprovalScopes(Integer namespaceId, Long approvalId) {
        List<GeneralApprovalScopeMap> results = generalApprovalProvider.listGeneralApprovalScopes(namespaceId, approvalId);
        if (results != null && results.size() > 0) {
            List<GeneralApprovalScopeMapDTO> scopes = results.stream().map(r -> {
                GeneralApprovalScopeMapDTO dto = ConvertHelper.convert(r, GeneralApprovalScopeMapDTO.class);
                return dto;
            }).collect(Collectors.toList());
            return scopes;
        }
        return null;
    }

    @Override
    public void deleteGeneralApproval(GeneralApprovalIdCommand cmd) {

        // change the status
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        ga.setStatus(GeneralApprovalStatus.DELETED.getCode());
        //  delete the approval
        updateGeneralApproval(ga);
    }

    @Override
    public GeneralFormDTO getApprovalForm(ApprovalFormIdCommand cmd) {
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormOriginId());
        return processGeneralFormDTO(form);
    }


    @Override
    public void enableGeneralApproval(GeneralApprovalIdCommand cmd) {
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        ga.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        updateGeneralApproval(ga);
    }

    @Override
    public void disableGeneralApproval(GeneralApprovalIdCommand cmd) {
        GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        updateGeneralApproval(ga);
    }

    /**
     * 取状态不为失效的form
     */
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
        resp.setForms(forms.stream().map(this::processGeneralFormDTO).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd) {
        return null;
    }

    @Override
    public GeneralApproval getGeneralApprovalByAttribute(Long ownerId, String attribute) {
        return generalApprovalProvider.getGeneralApprovalByAttribute(UserContext.getCurrentNamespaceId(), ownerId, attribute);
    }

    @Override
    public ListGeneralApprovalResponse listActiveGeneralApproval(
            ListActiveGeneralApprovalCommand cmd) {
        //  updated by wcg at 11/22/2017. find the top enterprise.
        if (cmd.getOwnerId() != null)
            cmd.setOwnerId(punchService.getTopEnterpriseId(cmd.getOwnerId()));
        ListGeneralApprovalCommand cmd2 = ConvertHelper.convert(cmd, ListGeneralApprovalCommand.class);
        cmd2.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        if (null == cmd2.getModuleType())
            cmd2.setModuleType(FlowModuleType.NO_MODULE.getCode());
        if (null == cmd2.getModuleId())
            cmd2.setModuleId(GeneralApprovalController.MODULE_ID);
        if (null == cmd2.getOwnerType())
            cmd2.setOwnerType("EhOrganizations");


        return listGeneralApproval(cmd2);
    }

    @Override
    public GeneralApprovalDTO verifyApprovalName(VerifyApprovalNameCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        GeneralApproval approval = this.generalApprovalProvider.getGeneralApprovalByName(namespaceId,
                cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getApprovalName());
        if (approval == null)
            return null;
        if (approval.getId().equals(cmd.getApprovalId()))
            return null;
        return ConvertHelper.convert(approval, GeneralApprovalDTO.class);
    }

    @Override
    public void disableApprovalByFormOriginId(Long formOriginId, Long moduleId, String moduleType) {
        generalApprovalProvider.disableApprovalByFormOriginId(formOriginId, moduleId, moduleType);
    }

    @Override
        public ListGeneralApprovalResponse listAvailableGeneralApprovals(ListGeneralApprovalCommand cmd) {
        Long userId = UserContext.currentUserId();
        ListGeneralApprovalResponse res = new ListGeneralApprovalResponse();
        List<GeneralApprovalDTO> results = new ArrayList<>();
        cmd.setStatus(GeneralApprovalStatus.RUNNING.getCode());
        if (null == cmd.getModuleType())
            cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
        if (null == cmd.getModuleId())
            cmd.setModuleId(GeneralApprovalController.MODULE_ID);
//        ListGeneralApprovalResponse response = ;
        List<GeneralApprovalDTO> approvals = listGeneralApproval(cmd).getDtos();
        OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, cmd.getOwnerId());
        if(member == null)
            member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, cmd.getOwnerId());
        if (approvals != null && approvals.size() > 0) {
            for(GeneralApprovalDTO approval : approvals){
                if(checkTheApprovalScope(approval.getScopes(), member))
                    results.add(approval);
            }
        }
        res.setDtos(results);
        return res;
    }

    @Override
    public boolean checkTheApprovalScope(List<GeneralApprovalScopeMapDTO> scopes, OrganizationMember member) {
        if (member == null || scopes == null || scopes.size() == 0)
            return false;
        List<Long> scopeUserIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.MEMBERDETAIL.getCode()))
                .map(GeneralApprovalScopeMapDTO::getSourceId).collect(Collectors.toList());
        List<Long> scopeDepartmentIds = scopes.stream()
                .filter(p1 -> p1.getSourceType().equals(UniongroupTargetType.ORGANIZATION.getCode()))
                .map(GeneralApprovalScopeMapDTO::getSourceId).collect(Collectors.toList());
        if (scopeUserIds.contains(member.getDetailId()))
            return true;
        for (Long departmentId : scopeDepartmentIds)
            if (member.getGroupPath().contains(String.valueOf(departmentId)))
                return true;
        return false;
    }

    @Override
    public String getUserRealName(GetUserRealNameCommand cmd) {
        User user = UserContext.current().getUser();
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(user.getId(), cmd.getOwnerId());
        if (member != null)
            return member.getContactName();
        //  若没有真实姓名则返回昵称
        return user.getNickName();
    }

    private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public void fullGeneralApprovalFormValues(List<GeneralFormFieldDTO> fields, GeneralForm form, List<GeneralApprovalVal> values) {
        if (form == null || CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty(values)) {
            return;
        }
        for (GeneralApprovalVal val : values) {
            //  若没有值则不用填充
            if (StringUtils.isEmpty(val.getFieldStr3())) {
                continue;
            }
            GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fields);
            //  若没有找到字段则跳过
            if (dto == null) {
                continue;
            }

            PostApprovalFormItem formVal = new PostApprovalFormItem();

            GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(val.getFieldType());
            if (null != fieldType) {
                switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                    case SINGLE_LINE_TEXT:
                    case NUMBER_TEXT:
                    case DATE:
                    case DROP_BOX:
                        formVal = generalFormFieldValueProcessor.processDropBoxField(formVal, val.getFieldStr3(), NormalFlag.NEED.getCode());
                        break;
                    case MULTI_LINE_TEXT:
                        formVal = generalFormFieldValueProcessor.processMultiLineTextField(formVal, val.getFieldStr3(), NormalFlag.NEED.getCode());
                        break;
                    case IMAGE:
                        formVal = generalFormFieldValueProcessor.processImageField(formVal, val.getFieldStr3());
                        break;
                    case FILE:
                        formVal = generalFormFieldValueProcessor.processFileField(formVal, val.getFieldStr3());
                        break;
                    case INTEGER_TEXT:
                        formVal = generalFormFieldValueProcessor.processIntegerTextField(formVal, val.getFieldStr3(), NormalFlag.NEED.getCode());
                        break;
                    case SUBFORM:
                        formVal = generalFormFieldValueProcessor.processSubFormField(formVal, dto.getFieldExtra(), val.getFieldStr3(), NormalFlag.NEED.getCode());
                        break;
                    default:
                        formVal.setFieldValue(val.getFieldStr3());
                }
                dto.setFieldValue(formVal.getFieldValue());
            }
        }
    }

    @Override
    public ListGeneralFormResponse getGeneralFormsAndValuesByFlowNode(GetGeneralFormsAndValuesByFlowNodeCommand cmd) {
        FlowCase flowCase = flowService.getFlowCaseById(cmd.getFlowCaseId());
        checkFlowCaseValid(cmd.getFlowCaseId(), cmd.getCurrentFlowNodeId());

        ListGeneralFormResponse response = new ListGeneralFormResponse();
        response.setNonEditableFieldTypes(NON_EDITABLE_FIELD_TYPES);
        response.setForms(new ArrayList<>());

        Map<String, List<GeneralApprovalVal>> map = new HashMap<>();
        List<FlowNode> flowNodes = new ArrayList<>();
        if (QueryGeneralFormByFlowNodeType.BEFORE_AND_CURRENT_NODE == QueryGeneralFormByFlowNodeType.fromCode(cmd.getQueryType())) {
            GeneralFormDTO flowForm = getGeneralApprovalFormAndValues(flowCase.getRootFlowCaseId());
            response.getForms().add(flowForm);
            map = generalApprovalValsMap(queryGeneralApprovalValsExcludeFlowFormVal(flowCase.getRootFlowCaseId(), null));
            flowNodes = getBeforeAndCurrentNodes(flowCase.getRootFlowCaseId(), cmd.getCurrentFlowNodeId());
        } else {
            map = generalApprovalValsMap(queryGeneralApprovalValsExcludeFlowFormVal(flowCase.getRootFlowCaseId(), cmd.getCurrentFlowNodeId()));
            flowNodes.add(flowNodeProvider.getFlowNodeById(cmd.getCurrentFlowNodeId()));
        }


        if (CollectionUtils.isEmpty(flowNodes)) {
            return response;
        }
        for (FlowNode node : flowNodes) {
            if (node.getFormOriginId() == null || node.getFormOriginId() == 0 || TrueOrFalseFlag.TRUE != TrueOrFalseFlag.fromCode(node.getFormStatus())) {
                continue;
            }
            GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(node.getFormOriginId(), node.getFormVersion());
            if (form == null) {
                continue;
            }
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
            GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
            dto.setFlowCaseId(flowCase.getRootFlowCaseId());
            dto.setFlowNodeId(node.getId());
            dto.setFormFields(fieldDTOs);
            String key = String.format("%d-%d-%d", node.getId(), node.getFormOriginId(), node.getFormVersion());
            List<GeneralApprovalVal> values = map.get(key);
            if (!CollectionUtils.isEmpty(values)) {
                fullGeneralApprovalFormValues(fieldDTOs, form, values);
            }
            response.getForms().add(dto);
        }
        return response;
    }

    private Map<String, List<GeneralApprovalVal>> generalApprovalValsMap(List<GeneralApprovalVal> values) {
        if (CollectionUtils.isEmpty(values)) {
            return new HashMap<>();
        }
        Map<String, List<GeneralApprovalVal>> map = new HashMap<>();
        for (GeneralApprovalVal val : values) {
            String key = String.format("%d-%d-%d", val.getFlowNodeId(), val.getFormOriginId(), val.getFormVersion());
            List<GeneralApprovalVal> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(val);
        }
        return map;
    }

    private List<Long> getProcessingFlowNodeIds(Long flowCaseId) {
        FlowCaseTree flowCaseTree = flowService.getProcessingFlowCaseTree(flowCaseId);
        if (flowCaseTree == null || CollectionUtils.isEmpty(flowCaseTree.getLeafNodes())) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>();
        for (FlowCaseTree flowNode : flowCaseTree.getLeafNodes()) {
            if (flowNode.getFlowCase() != null && flowNode.getFlowCase().getCurrentNodeId() != null) {
                ids.add(flowNode.getFlowCase().getCurrentNodeId());
            }
        }
        return ids;
    }

    private List<FlowNode> getBeforeAndCurrentNodes(Long flowCaseId, Long currentNodeId) {
        List<FlowCase> allFlowCases = flowService.getAllFlowCase(flowCaseId);
        List<FlowNodeLogDTO> allFlowNodes = flowService.getStepTrackerLogs(allFlowCases);
        List<Long> currentNodeIds = getProcessingFlowNodeIds(flowCaseId);
        List<Long> beforeAndCurrentNodeIds = new ArrayList<>();
        for (FlowNodeLogDTO node : allFlowNodes) {
            if (currentNodeIds.contains(node.getNodeId()) && Long.compare(currentNodeId, node.getNodeId()) != 0) {
                continue;
            }
            beforeAndCurrentNodeIds.add(node.getNodeId());
        }
        return flowNodeProvider.queryFlowNodes(new ListingLocator(), Integer.MAX_VALUE - 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_NODES.ID.in(beforeAndCurrentNodeIds));
            return query;
        });
    }

    private List<GeneralApprovalVal> queryGeneralApprovalValsExcludeFlowFormVal(Long flowCaseId, Long flowNodeId) {
        List<GeneralApprovalVal> values = generalApprovalValProvider.queryGeneralApprovalVals(new ListingLocator(), Integer.MAX_VALUE, (locator, query) -> {
            query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(flowCaseId));
            if (flowNodeId != null) {
                query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.eq(flowNodeId));
            } else {
                query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.gt(0L));
            }
            query.addOrderBy(Tables.EH_GENERAL_APPROVAL_VALS.CREATE_TIME.asc());
            return query;
        });
        return values;
    }

    @Override
    public void createOrUpdateGeneralFormValuesWithFlow(CreateOrUpdateGeneralFormValuesWithFlowCommand cmd) {
        FlowCase flowCase = flowService.getFlowCaseById(cmd.getFlowCaseId());
        checkFlowCaseValid(cmd.getFlowCaseId(), cmd.getFlowNodeId());

        List<GeneralFormDTO> befores = new ArrayList<>();
        List<GeneralFormDTO> afters = new ArrayList<>();
        dbProvider.execute(transactionStatus -> {
            for (PostGeneralFormValCommand valCommand : cmd.getPostGeneralFormValCommands()) {
                List<GeneralApprovalVal> values = generalApprovalValProvider.getGeneralApprovalVal(valCommand.getFormOriginId(), valCommand.getFormVersion(), flowCase.getRootFlowCaseId(), valCommand.getFlowNodeId());
                GeneralFormDTO before = getGeneralApprovalFormAndValues(valCommand.getFormOriginId(), valCommand.getFormVersion(), flowCase.getRootFlowCaseId(), valCommand.getFlowNodeId(), values);
                for (PostApprovalFormItem item : valCommand.getValues()) {
                    GeneralApprovalVal val = findGeneralApprovalVal(values, item.getFieldName());
                    if (val == null) {
                        val = new GeneralApprovalVal();
                        val.setNamespaceId(UserContext.getCurrentNamespaceId());
                        val.setApprovalId(flowCase.getReferId());
                        val.setFormOriginId(valCommand.getFormOriginId());
                        val.setFormVersion(valCommand.getFormVersion());
                        val.setFlowCaseId(flowCase.getRootFlowCaseId());
                        val.setFlowNodeId(valCommand.getFlowNodeId());
                        val.setFieldName(item.getFieldName());
                        val.setFieldType(item.getFieldType());
                        val.setFieldStr3(item.getFieldValue());
                        this.generalApprovalValProvider.createGeneralApprovalVal(val);
                    } else {
                        val.setFieldStr3(item.getFieldValue());
                        this.generalApprovalValProvider.updateGeneralApprovalVal(val);
                    }
                }
                GeneralFormDTO after = getGeneralApprovalFormAndValues(valCommand.getFormOriginId(), valCommand.getFormVersion(), flowCase.getRootFlowCaseId(), valCommand.getFlowNodeId(), null);
                befores.add(before);
                afters.add(after);
            }
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(UserContext.currentUserId(), cmd.getOrganizationId());
            String contactName = memberDetail != null ? memberDetail.getContactName() : UserContext.current().getUser().getNickName();
            String logContent = generalApprovalValChangeLogUtils.changeLog(befores, afters, contactName);

            trackChangeLog(flowCase.getRootFlowCaseId(), contactName, logContent);
            return null;
        });
    }

    private void trackChangeLog(Long rootFlowCaseId, String flowUserName, String logContent) {
        FlowCase rootFlowCase = flowService.getFlowCaseById(rootFlowCaseId);
        FlowPostSubjectCommand flowPostSubjectCommand = new FlowPostSubjectCommand();
        flowPostSubjectCommand.setFlowEntityId(rootFlowCase.getCurrentNodeId());
        flowPostSubjectCommand.setFlowEntityType(FlowEntityType.FLOW_NODE.getCode());
        flowPostSubjectCommand.setContent(logContent);
        FlowSubjectDTO subject = flowService.postSubject(flowPostSubjectCommand);
        FlowEventLog log = new FlowEventLog();
        log.setId(flowEventLogProvider.getNextId());
        log.setFlowMainId(rootFlowCase.getFlowMainId());
        log.setFlowVersion(rootFlowCase.getFlowVersion());
        log.setNamespaceId(rootFlowCase.getNamespaceId());
        log.setFlowNodeId(rootFlowCase.getCurrentNodeId());
        log.setFlowCaseId(rootFlowCase.getId());
        log.setStepCount(rootFlowCase.getStepCount());
        log.setSubjectId(subject.getId());
        log.setParentId(0L);
        log.setFlowUserId(UserContext.currentUserId());
        log.setFlowUserName(flowUserName);
        log.setLogType(FlowLogType.NODE_TRACKER.getCode());
        log.setButtonFiredStep(FlowStepType.NO_STEP.getCode());
        log.setTrackerApplier(1L); // 申请人可以看到此条log，为0则看不到
        log.setTrackerProcessor(1L);// 处理人可以看到此条log，为0则看不到
        log.setLogContent(flowUserName + " 编辑了表单");
        List<FlowEventLog> logList = new ArrayList<>(1);
        logList.add(log);

        FlowAutoStepDTO flowAutoStepDTO = new FlowAutoStepDTO();
        flowAutoStepDTO.setFlowCaseId(rootFlowCase.getId());
        flowAutoStepDTO.setFlowNodeId(rootFlowCase.getCurrentNodeId());
        flowAutoStepDTO.setStepCount(rootFlowCase.getStepCount());
        flowAutoStepDTO.setAutoStepType(FlowStepType.NO_STEP.getCode());
        flowAutoStepDTO.setEventType(FlowEventType.BUTTON_FIRED.getCode());
        flowAutoStepDTO.setEventLogs(logList);
        flowAutoStepDTO.setOperatorId(UserContext.currentUserId());
        flowService.processAutoStep(flowAutoStepDTO);
    }

    private GeneralApprovalVal findGeneralApprovalVal(List<GeneralApprovalVal> values, String fieldName) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        for (GeneralApprovalVal val : values) {
            if (val.getFieldName().equals(fieldName)) {
                return val;
            }
        }
        return null;
    }

    private GeneralFormDTO getGeneralApprovalFormAndValues(Long flowCaseId) {
        List<GeneralApprovalVal> values = generalApprovalValProvider.queryGeneralApprovalValsByFlowCaseId(flowCaseId);
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(values.get(0).getFormOriginId(), values.get(0).getFormVersion());
        if (form == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_FORM_NOT_FOUND, "form not found");

        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFlowCaseId(flowCaseId);
        dto.setFormFields(fieldDTOs);
        fullGeneralApprovalFormValues(fieldDTOs, form, values);
        return dto;
    }

    private GeneralFormDTO getGeneralApprovalFormAndValues(Long formOriginId, Long formVersion, Long flowCaseId, Long flowNodeId, List<GeneralApprovalVal> values) {
        if (flowNodeId == null) {
            return getGeneralApprovalFormAndValues(flowCaseId);
        }

        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(formOriginId, formVersion);
        if (form == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_FORM_NOT_FOUND, "form not found");

        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFlowCaseId(flowCaseId);
        dto.setFlowNodeId(flowNodeId);
        dto.setFormFields(fieldDTOs);
        List<GeneralApprovalVal> fieldValues = null;
        if (CollectionUtils.isEmpty(values)) {
            fieldValues = generalApprovalValProvider.getGeneralApprovalVal(formOriginId, formVersion, flowCaseId, flowNodeId);
        } else {
            fieldValues = values;
        }

        if (!CollectionUtils.isEmpty(fieldValues)) {
            fullGeneralApprovalFormValues(fieldDTOs, form, fieldValues);
        }
        return dto;
    }

    private void checkFlowCaseValid(Long flowCaseId, Long flowNodeId) {
        List<Long> currentNodeIds = getProcessingFlowNodeIds(flowCaseId);
        if (!currentNodeIds.contains(flowNodeId)) {
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_FLOW_STEP_ERROR, "step busy");
        }
    }
}
