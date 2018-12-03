package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowNodeProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFieldProcessor;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.enterpriseApproval.ComponentAbnormalPunchValue;
import com.everhomes.rest.enterpriseApproval.ComponentAskForLeaveValue;
import com.everhomes.rest.enterpriseApproval.ComponentBusinessTripValue;
import com.everhomes.rest.enterpriseApproval.ComponentDismissApplicationValue;
import com.everhomes.rest.enterpriseApproval.ComponentEmployApplicationValue;
import com.everhomes.rest.enterpriseApproval.ComponentGoOutValue;
import com.everhomes.rest.enterpriseApproval.ComponentOverTimeValue;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalErrorCode;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalStringCode;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowFormDTO;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowNodeLogDTO;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowServiceTypeDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.ListGeneralFormsCommand;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.server.schema.Tables;
import com.everhomes.techpark.punch.utils.PunchDayParseUtils;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EnterpriseApprovalFlowModuleListener implements FlowModuleListener {

    private static List<String> DEFAULT_FIELDS = new ArrayList<String>();

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalFlowModuleListener.class);

    @Autowired
    ServiceModuleProvider serviceModuleProvider;

    @Autowired
    GeneralApprovalProvider generalApprovalProvider;

    @Autowired
    GeneralApprovalFieldProcessor generalApprovalFieldProcessor;

    @Autowired
    GeneralApprovalValProvider generalApprovalValProvider;

    @Autowired
    GeneralFormService generalFormService;

    @Autowired
    GeneralFormProvider generalFormProvider;

    @Autowired
    ContentServerService contentServerService;

    @Autowired
    LocaleStringService localeStringService;
    @Autowired
    FlowService flowService;
    @Autowired
    FlowNodeProvider flowNodeProvider;
    @Autowired
    OrganizationProvider organizationProvider;


    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EnterpriseApprovalFlowModuleListener() {
        for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
            DEFAULT_FIELDS.add(value.getCode());
        }
    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(EnterpriseApprovalController.MODULE_ID);
        moduleInfo.setModuleId(module.getId());
        moduleInfo.setModuleName(module.getName());
        //  (启用工作流中的表单设置)
        moduleInfo.addMeta(FlowModuleInfo.META_KEY_FORM_FLAG, String.valueOf(TrueOrFalseFlag.TRUE.getCode()));
        return moduleInfo;
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getRootState().getFlowCase();
        LOGGER.debug("审批被驳回,handler 执行 onFlowCaseAbsorted  userType : " + ctx.getCurrentEvent().getUserType());
        EnterpriseApprovalHandler handler = getEnterpriseApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseAbsorted(ctx);
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

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        PostApprovalFormCommand cmd = JSON.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
        StringBuilder content = new StringBuilder(localeStringService.getLocalizedString(
                EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPLIER, "zh_CN", "Name") + " : " + flowCase.getApplierName() + "\n");
        List<FlowCaseEntity> entities = processCreatingEntities(cmd.getValues());
        for (int i = 0; i < entities.size(); i++) {
            if (i == 3)
                break;
            String key = entities.get(i).getKey();
            //  将"null"屏蔽为空字符串
            String value = StringUtils.isEmpty(entities.get(i).getValue()) ? "无" : entities.get(i).getValue();
            content.append(key).append(" : ").append(value).append("\n");
        }
        content = new StringBuilder(content.substring(0, content.length() - 1));
        flowCase.setContent(content.toString());
    }

    private List<FlowCaseEntity> processCreatingEntities(List<PostApprovalFormItem> values) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        for (PostApprovalFormItem value : values) {
            FlowCaseEntity e = new FlowCaseEntity();
            e.setKey(value.getFieldDisplayName() == null ? value.getFieldName() : value.getFieldDisplayName());
            //  just process the item which is not default
            if (!DEFAULT_FIELDS.contains(value.getFieldName())) {
                switch (GeneralFormFieldType.fromCode(value.getFieldType())) {
                    case SINGLE_LINE_TEXT:
                    case NUMBER_TEXT:
                    case DATE:
                    case DROP_BOX:
                        generalApprovalFieldProcessor.processDropBoxField(entities, e, value.getFieldValue());
                        break;
                    case MULTI_LINE_TEXT:
                        generalApprovalFieldProcessor.processMultiLineTextField(entities, e, value.getFieldValue());
                        break;
                    case IMAGE:
                        break;
                    case FILE:
                        break;
                    case INTEGER_TEXT:
                        generalApprovalFieldProcessor.processIntegerTextField(entities, e, value.getFieldValue());
                        break;
                    case SUBFORM:
                        break;
                    case CONTACT:
                        //企业联系人
                        generalApprovalFieldProcessor.processContactField(entities, e, value.getFieldValue());
                        break;
                    case ASK_FOR_LEAVE:
                        //请假
                        processAskForLeaveField(entities, value.getFieldValue());
                        break;
                    case BUSINESS_TRIP:
                        //出差
                        processBusinessTripField(entities, value.getFieldValue());
                        break;
                    case OVERTIME:
                        //加班
                        processOverTimeField(entities, value.getFieldValue());
                        break;
                    case GO_OUT:
                        //外出
                        processGoOutField(entities, value.getFieldValue());
                        break;
                    case ABNORMAL_PUNCH:
                        //打卡异常
                        processAbnormalPunchField(entities, value.getFieldValue());
                        break;
                    case EMPLOY_APPLICATION:
                        processEmployApplicationField(entities, value.getFieldValue());
                        break;
                    case DISMISS_APPLICATION:
                        processDismissApplicationField(entities, value.getFieldValue());
                        break;
                    case MULTI_SELECT:
                        //多选　
                        generalApprovalFieldProcessor.processMultiSelectField(entities,e, value.getFieldValue());
                        break;
                }
            }

        }
        return entities;
    }

    private void processAskForLeaveField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentAskForLeaveValue leaveValue = JSON.parseObject(jsonVal, ComponentAskForLeaveValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.ASK_FOR_LEAVE_TYPE, "zh_CN", "Type"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getRestName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.START_TIME, "zh_CN", "Start Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getStartTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.END_TIME, "zh_CN", "End Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getEndTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.ASK_FOR_LEAVE_TIME, "zh_CN", "Total Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithUnit(leaveValue.getDuration(), "天", "小时"));
        entities.add(e);
    }

    private void processBusinessTripField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentBusinessTripValue tripValue = JSON.parseObject(jsonVal, ComponentBusinessTripValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.START_TIME, "zh_CN", "Start Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getStartTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.END_TIME, "zh_CN", "End Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getEndTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.BUSINESS_TIME, "zh_CN", "Total Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(tripValue.getDuration()) + " 天");
        entities.add(e);
    }

    private void processOverTimeField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentOverTimeValue overTimeValue = JSON.parseObject(jsonVal, ComponentOverTimeValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.START_TIME, "zh_CN", "Start Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getStartTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.END_TIME, "zh_CN", "End Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getEndTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.OVERTIME_TIME, "zh_CN", "Total Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        if (overTimeValue.getDurationInMinute() != null) {
            e.setValue(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithUnit(overTimeValue.getDurationInMinute() * 60 * 1000, "小时", "分钟"));
        } else {
            e.setValue(overTimeValue.getDuration() + "天");
        }
        entities.add(e);
    }

    private void processGoOutField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentGoOutValue outValue = JSON.parseObject(jsonVal, ComponentGoOutValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.START_TIME, "zh_CN", "Start Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getStartTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.END_TIME, "zh_CN", "End Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getEndTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.GO_OUT_TIME, "zh_CN", "Total Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(outValue.getDuration()) + " 天");
        entities.add(e);
    }

    private void processAbnormalPunchField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentAbnormalPunchValue punchValue = JSON.parseObject(jsonVal, ComponentAbnormalPunchValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.ABNORMAL_PUNCH_DATE, "zh_CN", "Abnormal Date"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalDate());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.ABNORMAL_PUNCH_CLASS, "zh_CN", "Abnormal Class"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalItem());
        entities.add(e);
    }

    private void processEmployApplicationField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentEmployApplicationValue value = JSON.parseObject(jsonVal, ComponentEmployApplicationValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPLIER_JOB_POSITION, "zh_CN", "Job Position"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getApplierJobPosition());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.CHECK_IN_TIME, "zh_CN", "Check In Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getCheckInTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.EMPLOY_TIME, "zh_CN", "Employ Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getEmploymentTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.EMPLOY_REASON, "zh_CN", "Employ Reason"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getEmploymentReason());
        entities.add(e);
    }

    private void processDismissApplicationField(List<FlowCaseEntity> entities, String jsonVal) {
        ComponentDismissApplicationValue value = JSON.parseObject(jsonVal, ComponentDismissApplicationValue.class);
        FlowCaseEntity e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPLIER_JOB_POSITION, "zh_CN", "Job Position"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getApplierJobPosition());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.DISMISS_TIME, "zh_CN", "Dismiss Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getDismissTime());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.DISMISS_REASON, "zh_CN", "Dismiss Reason"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getDismissReason());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.DISMISS_REMARK, "zh_CN", "Dismiss Remark"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(value.getDismissRemark());
        entities.add(e);
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        // 审批通过 ( 如果 stepType 不是驳回 就是正常结束,进入处理 )
        FlowCase flowCase = ctx.getRootState().getFlowCase();
        EnterpriseApprovalHandler handler;
        //  兼容以前的版本，老版本未使用上 refer_id 故其值为0
        if (flowCase.getReferId() == 0L)
            handler = getEnterpriseApprovalHandler(flowCase.getOwnerId());
        else
            handler = getEnterpriseApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseEnd(flowCase);
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        //  姓名
        FlowCaseEntity e;
        //  approval-1.6 added by R
        EnterpriseApprovalFlowCase gf = ConvertHelper.convert(flowCase, EnterpriseApprovalFlowCase.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPROVAL_NUMBER, "zh_CN", "Approval Number"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getApprovalNo() != null ? gf.getApprovalNo().toString() : null);
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPROVAL_CREATE_TIME, "zh_CN", "Create Time"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(dateFormat.format(gf.getCreateTime().toLocalDateTime()));
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPLIER, "zh_CN", "Applier Name"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getApplierName());
        entities.add(e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString(EnterpriseApprovalStringCode.SCOPE, EnterpriseApprovalStringCode.APPLIER_DEPARTMENT, "zh_CN", "Department"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(gf.getCreatorDepartment());
        entities.add(e);

        entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
        return entities;
    }

    private List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        if (flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())) {
            List<GeneralApprovalVal> vals = this.generalApprovalValProvider
                    .queryGeneralApprovalValsByFlowCaseId(flowCase.getRootFlowCaseId());
            if (vals.size() == 0) {
                vals = this.generalApprovalValProvider
                        .queryGeneralApprovalValsByFlowCaseId(flowCase.getSubFlowRootFlowCaseId());
            }
            if (vals.size() == 0) {
                return new ArrayList<>();
            }
            GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                    vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
            // 模板设定的字段DTOs
            List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                    GeneralFormFieldDTO.class);
            processEntities(entities, vals, fieldDTOs);

            int formCount = 1;
            if (generalApprovalFieldProcessor.sinceVersion510()) {
                List<FlowCase> allFlowCases = flowService.getAllFlowCase(flowCase.getRootFlowCaseId());
                List<FlowNodeLogDTO> flowNodes = flowService.getStepTrackerLogs(allFlowCases);
                // 驳回的情况同一个节点会出现多次，需要去重
                Set<Long> distinctNodeIds = new HashSet<>();
                for (FlowNodeLogDTO flowNode : flowNodes) {
                    if (flowNode.getFormOriginId() == null || flowNode.getFormOriginId() == 0) {
                        continue;
                    }
                    FlowNodeDetailDTO flowNodeDetailDTO = flowService.getFlowNodeDetail(flowNode.getNodeId());
                    if (flowNodeDetailDTO == null || TrueOrFalseFlag.TRUE != TrueOrFalseFlag.fromCode(flowNodeDetailDTO.getFormStatus())) {
                        continue;
                    }
                    if (distinctNodeIds.add(flowNode.getNodeId())) {
                        formCount++;
                        processEntities(entities, flowCase.getOrganizationId(), flowCase.getRootFlowCaseId(), flowNode);
                    }
                }
                // 表单数> 1的要显示表单名称
                if (formCount > 1) {
                    FlowCaseEntity approvalFormName = new FlowCaseEntity();
                    approvalFormName.setKey(form.getFormName());
                    approvalFormName.setValue("");
                    approvalFormName.setEntityType(FlowCaseEntityType.ENTITY_GROUP.getCode());
                    entities.add(0, approvalFormName);
                }
            }
        }
        return entities;
    }

    private void processEntities(List<FlowCaseEntity> entities, Long organizationId, Long rootFlowCaseId, FlowNodeLogDTO flowNode) {
        List<FlowEventLog> flowEventLogs = flowService.getNodeEnterLogsIgnoreCompleteFlag(flowNode.getFlowCaseId(), flowNode.getNodeId());
        GeneralForm flowNodeForm = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(flowNode.getFormOriginId(), flowNode.getFormVersion());
        // 显示表单名称
        FlowCaseEntity flowNodeFormName = new FlowCaseEntity();
        flowNodeFormName.setKey(flowNodeForm.getFormName());
        flowNodeFormName.setValue(getFlowNodeProcessorNameList(flowEventLogs, organizationId));
        flowNodeFormName.setEntityType(FlowCaseEntityType.ENTITY_GROUP.getCode());
        entities.add(flowNodeFormName);
        List<GeneralApprovalVal> flowNodeFormVals = generalApprovalValProvider.getGeneralApprovalVal(flowNode.getFormOriginId(), flowNode.getFormVersion(), rootFlowCaseId, flowNode.getNodeId());
        List<GeneralFormFieldDTO> flowNodeFormFields = JSONObject.parseArray(flowNodeForm.getTemplateText(),
                GeneralFormFieldDTO.class);
        if (CollectionUtils.isEmpty(flowNodeFormVals)) {
            processNullEntities(entities, flowNodeFormFields);
        } else {
            processEntities(entities, flowNodeFormVals, flowNodeFormFields);
        }
    }

    private String getFlowNodeProcessorNameList(List<FlowEventLog> processors, Long organizationId) {
        if (CollectionUtils.isEmpty(processors)) {
            return "";
        }
        StringBuffer s = new StringBuffer();
        List<Long> userIds = processors.stream().map(FlowEventLog::getFlowUserId).distinct().collect(Collectors.toList());
        List<OrganizationMemberDetails> memberDetails = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), organizationId, ((locator, query) -> {
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID.in(userIds));
            return query;
        }));
        if (CollectionUtils.isEmpty(memberDetails)) {
            return "";
        }
        for (OrganizationMemberDetails member : memberDetails) {
            if (StringUtils.isNotBlank(member.getContactName())) {
                s.append(member.getContactName()).append('、');
            }
        }
        if (StringUtils.isBlank(s.toString())) {
            return "";
        }
        return s.substring(0, s.length() - 1);
    }

    private void processEntities(List<FlowCaseEntity> entities, List<GeneralApprovalVal> vals,
                                 List<GeneralFormFieldDTO> fieldDTOs) {

        for (GeneralApprovalVal val : vals) {
            try {
                if (!DEFAULT_FIELDS.contains(val.getFieldName())) {
                    // 不在默认fields的就是自定义字符串，组装这些
                    FlowCaseEntity e = new FlowCaseEntity();
                    GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
                    if (null == dto) {
                        //  just process the subForm value (there is no more subForm)
                        continue;
                    }
                    e.setKey(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
                    switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
                        case SINGLE_LINE_TEXT:
                        case NUMBER_TEXT:
                        case DATE:
                        case DROP_BOX:
                            generalApprovalFieldProcessor.processDropBoxField(entities, e, val.getFieldStr3());
                            break;
                        case MULTI_LINE_TEXT:
                            generalApprovalFieldProcessor.processMultiLineTextField(entities, e, val.getFieldStr3());
                            break;
                        case IMAGE:
                            generalApprovalFieldProcessor.processImageField(entities, e, val.getFieldStr3());
                            break;
                        case FILE:
                            generalApprovalFieldProcessor.processFileField(entities, e, val.getFieldStr3());
                            break;
                        case INTEGER_TEXT:
                            generalApprovalFieldProcessor.processIntegerTextField(entities, e, val.getFieldStr3());
                            break;
                        case SUBFORM:
                            generalApprovalFieldProcessor.processSubFormField4EnterpriseApproval(entities, dto, val.getFieldStr3());
                            break;
                        case CONTACT:
                            //企业联系人
                            generalApprovalFieldProcessor.processContactField(entities, e, val.getFieldStr3());
                            break;
                        case ASK_FOR_LEAVE:
                            //请假
                            processAskForLeaveField(entities, val.getFieldStr3());
                            break;
                        case BUSINESS_TRIP:
                            //出差
                            processBusinessTripField(entities, val.getFieldStr3());
                            break;
                        case OVERTIME:
                            //加班
                            processOverTimeField(entities, val.getFieldStr3());
                            break;
                        case GO_OUT:
                            //外出
                            processGoOutField(entities, val.getFieldStr3());
                            break;
                        case ABNORMAL_PUNCH:
                            //打卡异常
                            processAbnormalPunchField(entities, val.getFieldStr3());
                            break;
                        case EMPLOY_APPLICATION:
                            processEmployApplicationField(entities, val.getFieldStr3());
                            break;
                        case DISMISS_APPLICATION:
                            processDismissApplicationField(entities, val.getFieldStr3());
                            break;
                        case MULTI_SELECT:
                            //多选　
                            generalApprovalFieldProcessor.processMultiSelectField(entities,e, val.getFieldStr3());
                            break;
                    }
                }
            } catch (NullPointerException e) {
                LOGGER.error(" ********** 空指针错误  val = " + JSON.toJSONString(val), e);
            } catch (Exception e) {
                LOGGER.error(" ********** 这是什么错误  = " + JSON.toJSONString(val), e);

            }
        }
    }

    private void processNullEntities(List<FlowCaseEntity> entities, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO fieldDTO : fieldDTOs) {
            if (DEFAULT_FIELDS.contains(fieldDTO.getFieldName())) {
                continue;
            }
            switch (GeneralFormFieldType.fromCode(fieldDTO.getFieldType())) {
                case SUBFORM:
                    generalApprovalFieldProcessor.processNullSubFormField(entities, fieldDTO);
                    break;
                default:
                    FlowCaseEntity e = new FlowCaseEntity();
                    e.setKey(fieldDTO.getFieldDisplayName() == null ? fieldDTO.getFieldName() : fieldDTO.getFieldDisplayName());
                    e.setEntityType(FlowCaseEntityType.TEXT.getCode());
                    e.setValue("");
                    entities.add(e);
                    break;
            }
        }
    }

    private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
        for (GeneralFormFieldDTO val : fieldDTOs) {
            if (val.getFieldName().equals(fieldName))
                return val;
        }
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
        if (FlowStepType.APPROVE_STEP == ctx.getStepType()) {
            Long formOriginId = ctx.getCurrentNode().getFlowNode().getFormOriginId();
            if (formOriginId == null || formOriginId <= 0 || TrueOrFalseFlag.TRUE != TrueOrFalseFlag.fromCode(ctx.getCurrentNode().getFlowNode().getFormStatus())) {
                return;
            }
            Long formVersion = ctx.getCurrentNode().getFlowNode().getFormVersion();
            Long currentFlowNodeId = ctx.getCurrentNode().getFlowNode().getId();
            Long rootFlowCaseId = ctx.getFlowCase().getRootFlowCaseId();
            GeneralForm flowNodeForm = generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(formOriginId, formVersion);
            if (flowNodeForm == null) {
                return;
            }
            List<GeneralFormFieldDTO> flowNodeFormFields = JSONObject.parseArray(flowNodeForm.getTemplateText(), GeneralFormFieldDTO.class);
            if (CollectionUtils.isEmpty(flowNodeFormFields)) {
                return;
            }
            if (!generalApprovalFieldProcessor.requiredFlag(flowNodeFormFields)) {
                return;
            }
            List<GeneralApprovalVal> values = generalApprovalValProvider.getGeneralApprovalVal(formOriginId, formVersion, rootFlowCaseId, currentFlowNodeId);
            if (CollectionUtils.isEmpty(values)) {
                throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_FLOW_CASE_FORM_REQUIRE_SUBMIT, "");
            }
        }
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<GeneralApproval> ga = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(),
                Integer.MAX_VALUE - 1, (locator, query) -> {
                    query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
                    query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(GeneralApprovalStatus.RUNNING.getCode()));
                    query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(EnterpriseApprovalController.MODULE_ID));

                    if (ownerType != null && ownerId != null) {
                        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
                        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
                    }
                    return query;
                });
        if (ga != null) {
            return ga.stream().map(r -> {
                FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
                dto.setId(r.getId());
                dto.setNamespaceId(r.getNamespaceId());
                dto.setServiceName(r.getApprovalName());
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
        List<FlowFormDTO> flowFormDTOS = new ArrayList<>();
        if (flow.getOwnerType().equals(FlowOwnerType.GENERAL_APPROVAL.getCode())) {
            GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(flow.getOwnerId());
            if (approval.getApprovalAttribute().equals(GeneralApprovalAttribute.CUSTOMIZE.getCode())) {
                ListGeneralFormsCommand command = new ListGeneralFormsCommand();
                command.setOwnerId(flow.getOrganizationId());
                command.setOwnerType("EhOrganizations");
                command.setModuleId(EnterpriseApprovalController.MODULE_ID);
                command.setModuleType(EnterpriseApprovalController.MODULE_TYPE);
                ListGeneralFormResponse response = generalFormService.listGeneralForms(command);
                if (response.getForms() != null && response.getForms().size() > 0)
                    response.getForms().forEach(r -> flowFormDTOS.add(getFlowFormDTO(r.getFormName(), r.getFormVersion(), r.getFormOriginId())));
            } else {
                GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(approval.getFormOriginId());
                if (form != null)
                    flowFormDTOS.add(getFlowFormDTO(form.getFormName(), form.getFormVersion(), form.getFormOriginId()));
            }
        }
        return flowFormDTOS;
    }

    private FlowFormDTO getFlowFormDTO(String formName, Long formVersion, Long formOriginId) {
        FlowFormDTO dto = new FlowFormDTO();
        dto.setName(formName);
        dto.setFormVersion(formVersion);
        dto.setFormOriginId(formOriginId);
        return dto;
    }

    @Override
    public void onFlowStateChanging(Flow flow) {
        if(flow.getFormOriginId() == 0)
            throw RuntimeErrorException.errorWith(EnterpriseApprovalErrorCode.SCOPE, EnterpriseApprovalErrorCode.ERROR_DISABLE_APPROVAL_FORM,
                    "Form is empty.");
    }

    public List<FlowCaseEntity> getApprovalDetails(Long flowCaseId) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        FlowCase flowCase = flowService.getFlowCaseById(flowCaseId);
        List<GeneralApprovalVal> vals = this.generalApprovalValProvider.queryGeneralApprovalValsByFlowCaseId(flowCase.getRootFlowCaseId());
        if (vals.size() == 0) {
            vals = this.generalApprovalValProvider.queryGeneralApprovalValsByFlowCaseId(flowCase.getSubFlowRootFlowCaseId());
        }
        if (vals.size() == 0) {
            return new ArrayList<>();
        }
        GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
                vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
        // 模板设定的字段DTOs
        List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
                GeneralFormFieldDTO.class);
        processEntities(entities, vals, fieldDTOs);
        return entities;
    }

}
