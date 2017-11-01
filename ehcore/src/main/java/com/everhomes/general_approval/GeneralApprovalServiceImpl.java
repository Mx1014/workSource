package com.everhomes.general_approval;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormTemplate;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.YellowPageProvider;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.db.DbProvider;
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
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

import javax.servlet.http.HttpServletResponse;


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

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private GeneralApprovalFlowModuleListener generalApprovalFlowModuleListener;

    @Autowired
    private ContentServerService contentServerService;

    private StringTemplateLoader templateLoader;

    private Configuration templateConfig;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private SimpleDateFormat approvalNoFormat = new SimpleDateFormat("yyyyMMdd");


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
        RedisTemplate template = bigCollectionProvider.getMapAccessor("general_approval_no", "").getTemplate(new StringRedisSerializer());
        ValueOperations op = template.opsForValue();

        // TODO Auto-generated method stub
        return this.dbProvider.execute((TransactionStatus status) -> {
            User user = UserContext.current().getUser();
            GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
                    .getApprovalId());
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
                    .getFormOriginId());

            if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                // 使用表单/审批 注意状态 config
                form.setStatus(GeneralFormStatus.RUNNING.getCode());
                this.generalFormProvider.updateGeneralForm(form);
            }
            Flow flow = null;
            if (FlowModuleType.SERVICE_ALLIANCE.getCode().equals(ga.getModuleType())) {
                flow = flowService.getEnabledFlow(ga.getNamespaceId(), 40500L,
                        FlowModuleType.NO_MODULE.getCode(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());
            } else {
                flow = flowService.getEnabledFlow(ga.getNamespaceId(), ga.getModuleId(),
                        ga.getModuleType(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());
            }

            CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
            cmd21.setApplyUserId(user.getId());
            cmd21.setReferType(FlowReferType.APPROVAL.getCode());
            cmd21.setProjectType(ga.getOwnerType());
            cmd21.setProjectId(ga.getOwnerId());
            // 把command作为json传到content里，给flowcase的listener进行处理
            cmd21.setContent(JSON.toJSONString(cmd));
            cmd21.setCurrentOrganizationId(cmd.getOrganizationId());
            cmd21.setTitle(ga.getApprovalName());

            //  存储更多的信息 added by approval1.6
            GeneralApprovalFlowCaseAdditionalFieldDTO fieldDTO = new GeneralApprovalFlowCaseAdditionalFieldDTO();
            List<OrganizationMember> member = organizationProvider.listOrganizationMembersByUId(user.getId());
            member = member.stream().filter(r -> {
                return r.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode());
            }).collect(Collectors.toList());
            if (member != null && member.size() > 0) {
                Organization department = organizationProvider.findOrganizationById(member.get(0).getOrganizationId());
                //  存储部门 id 及名称
                fieldDTO.setDepartment(department.getName());
                fieldDTO.setDepartmentId(department.getId());
            }
            //  设置审批编号 added by approval1.6
            String countKey = "general_approval_no" + user.getNamespaceId() + cmd.getOrganizationId();
            String count;
            if (op.get(countKey) == null) {
                LocalDate tomorrowStart = LocalDate.now().plusDays(1);
                long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
                op.set(countKey, "1", seconds, TimeUnit.SECONDS);
                count = "1";
            } else {
                count = (String) op.get(countKey);
            }
            String approvalNo = approvalNoFormat.format(new Date());
            for (int i = 0; i < 4 - count.length(); i++)
                approvalNo += "0";
            approvalNo += count;
            op.increment(countKey, 1L);
            fieldDTO.setApprovalNo(Long.valueOf(approvalNo));
            cmd21.setAdditionalFieldDTO(fieldDTO);

            ServiceAllianceCategories category = yellowPageProvider.findCategoryById(ga.getModuleId());
            if (category != null) {
                cmd21.setServiceType(category.getName());
            }
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
    }

    @Override
    public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
        //
        GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
        ga.setNamespaceId(UserContext.getCurrentNamespaceId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        if (cmd.getApprovalAttribute() == null)
            ga.setApprovalAttribute(GeneralApprovalAttribute.CUSTOMIZE.getCode());
        if (cmd.getModifyFlag() == null)
            ga.setModifyFlag(Byte.valueOf("1"));
        if (cmd.getDeleteFlag() == null)
            ga.setDeleteFlag(Byte.valueOf("1"));
        if (cmd.getIconUri() == null)
            ga.setIconUri("cs://1/image/aW1hZ2UvTVRvMU9EVTBNR1psWW1Kak1XSTNZalUwT0RVeVlUQXdOak0zWWpObE1ERmpZUQ");
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
                        if (EntityType.ORGANIZATIONS.getCode().equals(cmd.getOwnerType())
                                && FlowModuleType.SERVICE_ALLIANCE.getCode().equals(cmd.getModuleType())) {
                            communityList = organizationProvider.listOrganizationCommunities(cmd.getOwnerId());
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
                                Condition condition = Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd.getOwnerId()).and(
                                        Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd.getOwnerType())
                                );
                                conditionOR = conditionOR.or(condition);
                                query.addConditions(conditionOR);
                            }
                        } else {
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

                        if (null != cmd.getProjectId())
                            query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd.getProjectId()));
                        if (null != cmd.getProjectType())
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
            if (FlowModuleType.SERVICE_ALLIANCE.getCode().equals(r.getModuleType())) {
                long oid = r.getModuleId();
                r.setModuleId(40500L);
                r.setModuleType(FlowModuleType.NO_MODULE.getCode());
                GeneralApprovalDTO dto = processApproval(r);
                dto.setModuleId(oid);
                dto.setModuleType(FlowModuleType.SERVICE_ALLIANCE.getCode());
                return dto;
            }
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

        //  approval icon
        if (result.getIconUri() != null)
            result.setIconUrl(contentServerService.parserUri(result.getIconUri()));
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
        resp.setForms(forms.stream().map((r) -> {
            return processGeneralFormDTO(r);
        }).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd) {
        return null;
    }


    //  判断是否需要创建模板
    @Override
    public VerifyApprovalTemplatesResponse verifyApprovalTemplates(VerifyApprovalTemplatesCommand cmd) {
        VerifyApprovalTemplatesResponse response = new VerifyApprovalTemplatesResponse();
        response.setResult(1L);
        List<GeneralApprovalTemplate> templates = generalApprovalProvider.listGeneralApprovalTemplateByModuleId(cmd.getModuleId());
        for (GeneralApprovalTemplate template : templates) {
            GeneralApproval ga = generalApprovalProvider.getGeneralApprovalByTemplateId(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(),
                    cmd.getOwnerType(), template.getId());
            if (ga == null) {
                response.setResult(0L);
                break;
            }
        }
        return response;
    }

    //  创建审批模板的接口
    @Override
    public void createApprovalTemplates(CreateApprovalTemplatesCommand cmd) {
        List<GeneralApprovalTemplate> templates = generalApprovalProvider.listGeneralApprovalTemplateByModuleId(cmd.getModuleId());
        //  1.判断审批模板中是否有对应的表单模板
        //  2.没有则直接创建审批
        //  3.有则先创建表单拿去表单 id,在创建审批与生成的 id 关联
        if (templates != null || templates.size() > 0) {
            dbProvider.execute((TransactionStatus status) -> {
                for (GeneralApprovalTemplate approval : templates) {
                    if (approval.getFormTemplateId().longValue() == 0) {
                        //  Create Approvals directly.
                        createGeneralApprovalByTemplate(approval, null, cmd);
                    } else {
                        //  Create Forms before creating approvals.
                        Long formOriginId = createGeneralFormByTemplate(approval, cmd);
                        //  Then, start to create approvals.
                        createGeneralApprovalByTemplate(approval, formOriginId, cmd);
                    }
                }
                return null;
            });
        }
    }

    private void createGeneralApprovalByTemplate(GeneralApprovalTemplate approval, Long formOriginId, CreateApprovalTemplatesCommand cmd) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalByTemplateId(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), approval.getId());
        if (ga != null) {
            ga = convertApprovalFromTemplate(ga, approval, formOriginId, cmd);
            generalApprovalProvider.updateGeneralApproval(ga);
        } else {
            ga = ConvertHelper.convert(approval, GeneralApproval.class);
            ga = convertApprovalFromTemplate(ga, approval, formOriginId, cmd);
            generalApprovalProvider.createGeneralApproval(ga);
        }
    }

    private Long createGeneralFormByTemplate(GeneralApprovalTemplate approval, CreateApprovalTemplatesCommand cmd) {
        GeneralFormTemplate form = generalFormProvider.findGeneralFormTemplateByIdAndModuleId(
                approval.getFormTemplateId(), cmd.getModuleId());
        GeneralForm gf = generalFormProvider.getGeneralFormByTemplateId(cmd.getModuleId(), cmd.getOwnerId(),
                cmd.getOwnerType(), form.getId());
        if (gf != null) {
            gf = convertFormFromTemplate(gf, form, cmd);
            generalFormProvider.updateGeneralForm(gf);
            return gf.getFormOriginId();
        } else {
            gf = ConvertHelper.convert(form, GeneralForm.class);
            gf = convertFormFromTemplate(gf, form, cmd);
            gf.setStatus(GeneralFormStatus.CONFIG.getCode());
            gf.setFormVersion(0L);
            Long formOriginId = generalFormProvider.createGeneralForm(gf);
            return formOriginId;
        }
    }

    private GeneralApproval convertApprovalFromTemplate(GeneralApproval ga, GeneralApprovalTemplate approval, Long formOriginId, CreateApprovalTemplatesCommand cmd) {
        ga.setNamespaceId(UserContext.getCurrentNamespaceId());
        ga.setStatus(GeneralApprovalStatus.INVALID.getCode());
        ga.setOwnerId(cmd.getOwnerId());
        ga.setOwnerType(cmd.getOwnerType());
        ga.setOrganizationId(cmd.getOrganizationId());
        ga.setProjectId(cmd.getProjectId());
        ga.setProjectType(cmd.getProjectType());
        ga.setApprovalTemplateId(approval.getId());
        if (formOriginId != null)
            ga.setFormOriginId(formOriginId);
        ga.setSupportType(cmd.getSupportType());
        return ga;
    }

    private GeneralForm convertFormFromTemplate(GeneralForm gf, GeneralFormTemplate form, CreateApprovalTemplatesCommand cmd) {
        gf.setFormAttribute(GeneralApprovalAttribute.DEFAULT.getCode());
        gf.setNamespaceId(UserContext.getCurrentNamespaceId());
        gf.setFormTemplateId(form.getId());
        gf.setFormTemplateVersion(form.getVersion());
        gf.setOwnerId(cmd.getOwnerId());
        gf.setOwnerId(cmd.getOwnerId());
        gf.setOwnerType(cmd.getOwnerType());
        gf.setOrganizationId(cmd.getOrganizationId());
        return gf;
    }

    @Override
    public GeneralApproval getGeneralApprovalByAttribute(Long ownerId, String attribute){
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalByAttribute(UserContext.getCurrentNamespaceId(), ownerId, attribute);
        return approval;
    }

    @Override
    public ListGeneralApprovalResponse listActiveGeneralApproval(
            ListActiveGeneralApprovalCommand cmd) {
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
        GeneralApproval approval = this.generalApprovalProvider.getGeneralApprovalByName(UserContext.getCurrentNamespaceId(),
                cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getApprovalName());
        if (approval != null)
            return ConvertHelper.convert(approval, GeneralApprovalDTO.class);
        return null;
    }

    @Override
    public ListGeneralApprovalRecordsResponse listGeneralApprovalRecords(ListGeneralApprovalRecordsCommand cmd) {
        ListGeneralApprovalRecordsResponse response = new ListGeneralApprovalRecordsResponse();
        ListingLocator locator = new ListingLocator();
        SearchFlowCaseCommand command = new SearchFlowCaseCommand();

        command.setStartTime(cmd.getStartTime());
        command.setEndTime(cmd.getEndTime());
        command.setOrganizationId(cmd.getOrganizationId());
        command.setModuleId(cmd.getModuleId());
        command.setFlowCaseSearchType(FlowCaseSearchType.ADMIN.getCode());
        command.setNamespaceId(UserContext.getCurrentNamespaceId());
        //  审批状态
        if (cmd.getApprovalStatus() != null)
            command.setFlowCaseStatus(cmd.getApprovalStatus());
        if (cmd.getPageAnchor() != null)
            command.setPageAnchor(cmd.getPageAnchor());

        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        command.setPageSize(count);

        List<FlowCaseDetail> details = flowCaseProvider.findAdminFlowCases(locator, count, command, (locator1, query) -> {
            //  审批类型
            if (cmd.getApprovalType() != null)
                query.addConditions(Tables.EH_FLOW_CASES.TITLE.eq(cmd.getApprovalType()));
            //  申请人姓名
            if (cmd.getCreatorName() != null)
                query.addConditions(Tables.EH_FLOW_CASES.APPLIER_NAME.eq(cmd.getCreatorName()));
            //  审批编号
            if (cmd.getApprovalNo() != null)
                query.addConditions(GeneralApprovalFlowCaseCustomField.APPROVAL_NO.getField().eq(cmd.getApprovalNo()));
            //  申请人部门
            if (cmd.getCreatorDepartmentId() != null)
                query.addConditions(GeneralApprovalFlowCaseCustomField.CREATOR_DEPARTMENT_ID.getField().eq(cmd.getCreatorDepartmentId()));
            return query;
        });
        if (details != null && details.size() > 0) {
            List<GeneralApprovalRecordDTO> results = details.stream().map(r -> {
                GeneralApprovalFlowCase flowCase = ConvertHelper.convert(r, GeneralApprovalFlowCase.class);
                GeneralApprovalRecordDTO dto = new GeneralApprovalRecordDTO();
                dto.setId(r.getId());
                dto.setNamespaceId(r.getNamespaceId());
                dto.setModuleId(r.getModuleId());
                dto.setCreatorName(r.getApplierName());
                dto.setCreatorDepartment(flowCase.getCreatorDepartment());
                dto.setCreatorDepartmentId(flowCase.getCreatorDepartmentId());
                dto.setCreatorMobile(r.getApplierPhone());
                String time = format.format(r.getCreateTime());
                dto.setCreateTime(time);
                dto.setApprovalType(r.getTitle());
                dto.setApprovalNo(flowCase.getApprovalNo());
                dto.setApprovalStatus(r.getStatus());
                dto.setFlowCaseId(flowCase.getId());
                return dto;
            }).collect(Collectors.toList());
            response.setRecords(results);
            response.setNextPageAnchor(locator.getAnchor());

        }
        return response;
    }

    @Override
    public void exportGeneralApprovalRecords(ListGeneralApprovalRecordsCommand cmd, HttpServletResponse httpResponse) {
        cmd.setPageAnchor(null);
        cmd.setPageSize(1000000);
        ListGeneralApprovalRecordsResponse response = listGeneralApprovalRecords(cmd);
        //  1. Set the main title of the sheet
        String mainTitle = "审批记录";
        //  2. Set the subtitle of the sheet
        String subTitle = "申请时间:" + approvalNoFormat.format(cmd.getStartTime()) + " ~ " + approvalNoFormat.format(cmd.getEndTime());
        //  3. Set the title of the approval lists
        List<String> titles = Arrays.asList("审批编号", "提交时间", "申请人", "申请人部门", "表单内容",
                "审批状态", "审批记录", "当前审批人", "督办人");
        //  4. Start to write the excel
        XSSFWorkbook workbook = exportGeneralApprovalRecordsFile(mainTitle, subTitle, titles, response.getRecords());

/*        List<Long> flowCaseIds = response.getRecords().stream().map(r -> {
            return r.getFlowCaseId();
        }).collect(Collectors.toList());*/
        writeExcel(workbook, httpResponse);
    }

    public List<FlowCaseEntity> getApprovalDetails(Long flowCaseId) {
        FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
        return generalApprovalFlowModuleListener.onFlowCaseDetailRender(flowCase, null);
    }

    private XSSFWorkbook exportGeneralApprovalRecordsFile(
            String mainTitle, String subTitle, List<String> titles, List<GeneralApprovalRecordDTO> data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("审批记录");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
        //  1. Write titles
        createGeneralApprovalRecordsFileTitle(workbook, sheet, mainTitle, subTitle, titles);
        //  2. Write data
        if (data != null && data.size() > 0) {
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row dataRow = sheet.createRow(rowIndex + 3);
                createGeneralApprovalRecordsFileData(workbook, dataRow, data.get(rowIndex));
            }
        }
        return workbook;
    }

    private void createGeneralApprovalRecordsFileTitle(XSSFWorkbook workbook, Sheet sheet, String mainTitle, String subTitle, List<String> list) {

        //  1.Set the style(center)
        Row mainTitleRow = sheet.createRow(0);
        XSSFCellStyle mainTitleStyle = workbook.createCellStyle();
        mainTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //  1.Set the value of the main title
        Cell mainTitleCell = mainTitleRow.createCell(0);
        mainTitleCell.setCellStyle(mainTitleStyle);
        mainTitleCell.setCellValue(mainTitle);

        //  2.Set the style(center)
        Row subTitleRow = sheet.createRow(1);
        XSSFCellStyle subTitleStyle = workbook.createCellStyle();
        subTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
        //  2.Set the value of the subtitle
        Cell subTitleCell = subTitleRow.createCell(0);
        subTitleCell.setCellStyle(subTitleStyle);
        subTitleCell.setCellValue(subTitle);

        //  3.Set the title of the approval lists
        Row titleRow = sheet.createRow(2);
        for (int i = 0; i < list.size(); i++) {
            sheet.setColumnWidth(i,15*256);
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(list.get(i));
        }
    }

    private void createGeneralApprovalRecordsFileData(XSSFWorkbook workbook, Row dataRow, GeneralApprovalRecordDTO data) {
        //  1. basic data from flowCases
        dataRow.createCell(0).setCellValue(data.getApprovalNo());
        dataRow.createCell(1).setCellValue(data.getCreateTime());
        dataRow.createCell(2).setCellValue(data.getCreatorName());
        dataRow.createCell(3).setCellValue(data.getCreatorDepartment());

        //  2. data from form
        List<FlowCaseEntity> entitiyLists = getApprovalDetails(data.getFlowCaseId());
        if (entitiyLists != null && entitiyLists.size() > 4) {
            String formLogs = "";
            for (int i = 4; i < entitiyLists.size(); i++) {
                formLogs += entitiyLists.get(i).getKey() + " : " + entitiyLists.get(i).getValue() + "\n";
            }
            dataRow.createCell(4).setCellValue(formLogs);
        }

        //  3. approval status
        if (FlowCaseStatus.PROCESS.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(5).setCellValue("处理中");
        else if (FlowCaseStatus.FINISHED.getCode().equals(data.getApprovalStatus()))
            dataRow.createCell(5).setCellValue("已完成");
        else
            dataRow.createCell(5).setCellValue("已取消");

        //  4. the operator logs of the approval
        SearchFlowOperateLogsCommand logsCommand = new SearchFlowOperateLogsCommand();
        logsCommand.setFlowCaseId(data.getFlowCaseId());
        logsCommand.setPageSize(100000);
        List<FlowOperateLogDTO> operateLogLists = flowService.searchFlowOperateLogs(logsCommand).getLogs();
        if (operateLogLists != null && operateLogLists.size() > 0) {
            String operateLogs = "";
            for (int i = 0; i < operateLogLists.size(); i++) {
                operateLogs += operateLogLists.get(i).getFlowCaseContent() + "\n";
            }
            dataRow.createCell(6).setCellValue(operateLogs);
        }

        //  5. the current operator
        List<UserInfo> processorLists = flowService.getCurrentProcessors(data.getFlowCaseId());
        if (processorLists != null && processorLists.size() > 0) {
            String processors = "";
            for (int i = 0; i < processorLists.size(); i++) {
                processors += processorLists.get(i).getNickName() + ", ";
            }
            processors = processors.substring(0, processors.length()-1);
            dataRow.createCell(7).setCellValue(processors);
        }

        //  6. the current supervisor
        FlowCase flowCase = flowService.getFlowCaseById(data.getFlowCaseId());
        List<UserInfo> supervisorLists = flowService.getSupervisor(flowCase);
        if (supervisorLists != null && supervisorLists.size() > 0) {
            String supervisors = "";
            for (int i = 0; i < processorLists.size(); i++) {
                supervisors += supervisorLists.get(i).getNickName() + ", ";
            }
            supervisors = supervisors.substring(0, supervisors.length()-1);
            dataRow.createCell(8).setCellValue(supervisors);
        }
    }

    private void writeExcel(XSSFWorkbook workbook, HttpServletResponse httpResponse) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            workbook.write(out);
            String fileName = "审批记录.xlsx";
            httpResponse.setContentType("application/msexcel");
            httpResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            //response.addHeader("Content-Length", "" + out.);
            OutputStream excelStream = new BufferedOutputStream(httpResponse.getOutputStream());
            httpResponse.setContentType("application/msexcel");
            excelStream.write(out.toByteArray());
            excelStream.flush();
            excelStream.close();
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally {
            try {
                workbook.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }
}
