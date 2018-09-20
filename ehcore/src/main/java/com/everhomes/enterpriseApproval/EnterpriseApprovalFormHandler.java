package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowCaseAdditionalFieldDTO;
import com.everhomes.general_approval.GeneralApprovalFormDataVerifyHandler;
import com.everhomes.general_approval.GeneralApprovalFormHandler;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalErrorCode;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GeneralFormRenderType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component(EnterpriseApprovalFormHandler.ENTERPRISE_APPROVAL_FORM)
public class EnterpriseApprovalFormHandler implements GeneralApprovalFormHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalFormHandler.class);

    static final String ENTERPRISE_APPROVAL_FORM = GeneralApprovalFormHandler.GENERAL_APPROVAL_FORM_PREFIX + "any-module";

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private GeneralApprovalService generalApprovalService;

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    private GeneralFormProvider generalFormProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private DbProvider dbProvider;

    static final String ENTERPRISE_APPROVAL_NO = "enterprise_approval_no";

    @Override
    public PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();

        //  1.check the approval is legal(判断审批是否启用)
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if (ga == null || GeneralApprovalStatus.RUNNING.getCode() != ga.getStatus())
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_APPROVAL_NOT_RUNNING, "The approval's status is not running");
        List<GeneralApprovalScopeMapDTO> scopes = generalApprovalService.listGeneralApprovalScopes(namespaceId, ga.getId());

        //  2.get the form by flow(新版通过工作流拿表单，所以必须有工作流)
        Flow flow = flowService.getEnabledFlow(namespaceId, EnterpriseApprovalController.MODULE_ID,
                EnterpriseApprovalController.MODULE_TYPE, cmd.getApprovalId(), EnterpriseApprovalController.APPROVAL_OWNER_TYPE);
        if (flow == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_DISABLE_APPROVAL_FLOW, "flow not found");


        GetTemplateByApprovalIdResponse res = dbProvider.execute((TransactionStatus status) -> {
            //  3.check the user is in the scope(判断是否在范围内)
            OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, cmd.getOrganizationId());
            if (member == null)
                member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, cmd.getOrganizationId());
            if (!generalApprovalService.checkTheApprovalScope(scopes, member))
                throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_APPROVAL_NO_ACCESS,
                        "The user is not in the approval scope");

            //  4.compatible with the previous data(兼容旧数据)
            Long formOriginId = flow.getFormOriginId();
            if (0 == formOriginId)
                formOriginId = ga.getFormOriginId();
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(formOriginId);
            if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
                // change the status
                form.setStatus(GeneralFormStatus.RUNNING.getCode());
                this.generalFormProvider.updateGeneralForm(form);
            }

            // 数据前置校验
            GeneralApprovalFormDataVerifyHandler dataVerifyHandler = getGeneralApprovalFormDataVerifyHandler(ga);
            dataVerifyHandler.verify(cmd);

            CreateFlowCaseCommand caseCommand = new CreateFlowCaseCommand();
            caseCommand.setApplyUserId(userId);
            caseCommand.setReferType(FlowReferType.APPROVAL.getCode());
            caseCommand.setReferId(ga.getId());
            caseCommand.setProjectType(ga.getOwnerType());
            caseCommand.setProjectId(ga.getOwnerId());
            caseCommand.setContent(JSON.toJSONString(cmd)); // 把command作为json传到content里，给flowCase的listener进行处理
            caseCommand.setCurrentOrganizationId(cmd.getOrganizationId());
            caseCommand.setApplierOrganizationId(cmd.getOrganizationId());
            caseCommand.setTitle(ga.getApprovalName());
            //  5.additional info set(部门&编号)
            GeneralApprovalFlowCaseAdditionalFieldDTO fieldDTO = new GeneralApprovalFlowCaseAdditionalFieldDTO();
            if (member != null) {
                Organization department = organizationProvider.findOrganizationById(member.getOrganizationId());
                fieldDTO.setDepartment(department.getName());
                fieldDTO.setDepartmentId(department.getId());
            }
            //  approval number added by approval1.6
            fieldDTO.setApprovalNo(generateApprovalNumber(namespaceId, cmd.getOrganizationId()));
            caseCommand.setAdditionalFieldDTO(fieldDTO);

            Long flowCaseId = flowService.getNextFlowCaseId();

            // 6.save approval value
            for (PostApprovalFormItem val : cmd.getValues()) {
                GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
                obj.setApprovalId(ga.getId());
                obj.setFormOriginId(form.getFormOriginId());    // the originId is in the flow
                obj.setFormVersion(form.getFormVersion());
                obj.setFlowCaseId(flowCaseId);
                obj.setFieldName(val.getFieldName());
                obj.setFieldType(val.getFieldType());
                obj.setFieldStr3(val.getFieldValue());
                this.generalApprovalValProvider.createGeneralApprovalVal(obj);
            }

            caseCommand.setFlowMainId(flow.getFlowMainId());
            caseCommand.setFlowVersion(flow.getFlowVersion());
            caseCommand.setFlowCaseId(flowCaseId);
            FlowCase flowCase = flowService.createFlowCase(caseCommand);


            //added by wh 建立了审批单之后

            EnterpriseApprovalHandler handler = EnterpriseApprovalHandlerUtils.getEnterpriseApprovalHandler(ga);
            LOGGER.debug("建立审批,handler 执行 onApprovalCreated ");
            handler.onApprovalCreated(flowCase);

            GetTemplateByApprovalIdResponse response = new GetTemplateByApprovalIdResponse();
            response.setFlowCaseId(flowCase.getId());
            return response;
        });

        PostGeneralFormDTO dto = new PostGeneralFormDTO();
        List<PostApprovalFormItem> items = new ArrayList<>();
        PostApprovalFormItem item = new PostApprovalFormItem();
        item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
        item.setFieldValue(JSONObject.toJSONString(res));
        items.add(item);
        dto.setValues(items);
        return dto;
    }

    /**
     * 根据域空间.公司.日期来生成对应的审批编号
     */
    private Long generateApprovalNumber(Integer namespaceId, Long organizationId) {
        RedisTemplate template = bigCollectionProvider.getMapAccessor(ENTERPRISE_APPROVAL_NO, "").getTemplate(new StringRedisSerializer());
        ValueOperations op = template.opsForValue();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

        String opKey = ENTERPRISE_APPROVAL_NO + namespaceId + organizationId + LocalDate.now().toString();
        StringBuilder result = new StringBuilder(format.format(LocalDate.now()));

        Long increment = op.increment(opKey, 1L);
        op.getOperations().expire(opKey, 24, TimeUnit.HOURS);
        String number = String.valueOf(increment);
        for (int i = 0; i < 4 - number.length(); i++)
            result.append("0");

        result.append(number);
        return Long.valueOf(result.toString());
    }

    private GeneralApprovalFormDataVerifyHandler getGeneralApprovalFormDataVerifyHandler(GeneralApproval ga) {
        if (ga != null) {
            GeneralApprovalFormDataVerifyHandler handler = PlatformContext.getComponent(GeneralApprovalFormDataVerifyHandler.GENERAL_APPROVAL_FORM_DATA_VERIFY_PREFIX
                    + ga.getApprovalAttribute());
            if (handler != null) {
                return handler;
            }
        }
        return PlatformContext.getComponent(EnterpriseApprovalFormDataVerifyDefaultHandler.ENTERPRISE_APPROVAL_FORM_DATA_VERIFY);
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        //  get the basic data
        Long approvalId = cmd.getSourceId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Flow flow = flowService.getEnabledFlow(namespaceId, EnterpriseApprovalController.MODULE_ID,
                EnterpriseApprovalController.MODULE_TYPE, approvalId, EnterpriseApprovalController.APPROVAL_OWNER_TYPE);
        if (flow == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_DISABLE_APPROVAL_FLOW, "flow not found");
        Long formOriginId = flow.getFormOriginId();
        //  compatible with the previous data
        if (0 == formOriginId) {
            GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
            formOriginId = ga.getFormOriginId();
        }
        GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(formOriginId);
        if (form == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE,
                    EnterpriseApprovalErrorCode.ERROR_FORM_NOT_FOUND, "form not found");

        //  extra process
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(approvalId);
        EnterpriseApprovalHandler handler = EnterpriseApprovalHandlerUtils.getEnterpriseApprovalHandler(ga);
        handler.processFormFields(fieldDTOs, cmd);


        //  organizationId is still needed
        GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
        organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
        organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
        organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
        organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
        organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
        organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
        fieldDTOs.add(organizationIdField);

        GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
        dto.setFormFields(fieldDTOs);
        return dto;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
        EnterpriseApprovalHandler handler = EnterpriseApprovalHandlerUtils.getEnterpriseApprovalHandler(ga);
        return handler.getGeneralFormReminder(cmd);
    }

}
