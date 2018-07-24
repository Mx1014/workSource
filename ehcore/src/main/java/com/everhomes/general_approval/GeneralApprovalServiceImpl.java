package com.everhomes.general_approval;

import java.util.*;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
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
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

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

    private StringTemplateLoader templateLoader;

    private Configuration templateConfig;

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
}
