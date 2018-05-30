package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.*;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalServiceErrorCode;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.general_approval.*;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component(EnterpriseApprovalPostHandler.ENTERPRISE_APPROVAL_POST)
public class EnterpriseApprovalPostHandler implements ApprovalPostValHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalPostHandler.class);

    static final String ENTERPRISE_APPROVAL_POST = ApprovalPostValHandler.APPROVAL_POST_PREFIX + "any-module";

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
    private DbProvider dbProvider;

    static final String ENTERPRISE_APPROVAL_NO = "enterprise_approval_no";

    @Override
    public PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.currentUserId();

         //  1.check the approval is legal(判断审批是否启用
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if (ga == null || GeneralApprovalStatus.RUNNING.getCode() != ga.getStatus())
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE, EnterpriseApprovalServiceErrorCode.ERROR_ILLEGAL_APPROVAL,
                    "The approval's status is not running");
        List<GeneralApprovalScopeMapDTO> scopes = generalApprovalService.listGeneralApprovalScopes(namespaceId, ga.getId());

        //  2.get the form by flow(新版通过工作流拿表单，所以必须有工作流)
        Flow flow = flowService.getEnabledFlow(namespaceId, EnterpriseApprovalController.MODULE_ID,
                EnterpriseApprovalController.MODULE_TYPE, cmd.getApprovalId(), EnterpriseApprovalController.APPROVAL_OWNER_TYPE);
        if(flow == null)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE,
                    EnterpriseApprovalServiceErrorCode.ERROR_DISABLE_APPROVAL_FLOW, "flow not found");


        dbProvider.execute((TransactionStatus status) -> {
            //  3.check the user is in the scope(判断是否在范围内)
            OrganizationMember member = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(userId, cmd.getOrganizationId());
            if (member == null)
                member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, cmd.getOrganizationId());
            if (!generalApprovalService.checkTheApprovalScope(scopes, member))
                throw RuntimeErrorException.errorWith(EnterpriseApprovalServiceErrorCode.SCOPE, EnterpriseApprovalServiceErrorCode.ERROR_ILLEGAL_APPROVAL,
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
            fieldDTO.setApprovalNo(generateApprovalNumber(namespaceId, cmd.getOrganizationId()));
            caseCommand.setAdditionalFieldDTO(fieldDTO);

            Long flowCaseId = flowService.getNextFlowCaseId();

            // 把values 存起来
            for (PostApprovalFormItem val : cmd.getValues()) {
                GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
                obj.setApprovalId(ga.getId());
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

            EnterpriseApprovalHandler handler = getEnterpriseApprovalHandler(flowCase.getReferId());
            LOGGER.debug("建立审批,handler 执行 onApprovalCreated ");
            handler.onApprovalCreated(flowCase);
            return null;
        });

        return null;
        }

    private Long generateApprovalNumber(Integer namespaceId, Long organizationId){
        RedisTemplate template = bigCollectionProvider.getMapAccessor(ENTERPRISE_APPROVAL_NO, "").getTemplate(new StringRedisSerializer());
        ValueOperations op = template.opsForValue();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

        //  approval number added by approval1.6
        String countKey = ENTERPRISE_APPROVAL_NO + namespaceId + organizationId;
        String count;
        if (op.get(countKey) == null) {
            LocalDate tomorrowStart = LocalDate.now().plusDays(1);
            long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
            op.set(countKey, "1", seconds, TimeUnit.SECONDS);
            count = "1";
        } else {
            count = (String) op.get(countKey);
        }
        StringBuilder approvalNo = new StringBuilder(format.format(LocalDate.now()));
        for (int i = 0; i < 4 - count.length(); i++)
            approvalNo.append("0");
        approvalNo.append(count);
        op.increment(countKey, 1L);
        return Long.valueOf(approvalNo.toString());
    }

    private EnterpriseApprovalHandler getEnterpriseApprovalHandler(Long referId) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(referId);
        if (ga != null) {
            EnterpriseApprovalHandler handler = PlatformContext.getComponent(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX
                    + ga.getApprovalAttribute());
            if (handler != null) {
                return handler;
            }
        }
        return PlatformContext.getComponent(EnterpriseApprovalDefaultHandler.ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME);
    }
}
