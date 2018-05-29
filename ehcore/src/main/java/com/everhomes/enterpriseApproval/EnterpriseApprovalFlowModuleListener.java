package com.everhomes.enterpriseApproval;

import com.alibaba.fastjson.JSON;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFieldProcessor;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
    ContentServerService contentServerService;

    @Autowired
    LocaleStringService localeStringService;

    private DecimalFormat decimalFormat = new DecimalFormat("0.000");

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
        return null;
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx){
        FlowCase flowCase = ctx.getRootState().getFlowCase();
        LOGGER.debug("审批被驳回,handler 执行 onFlowCaseAbsorted  userType : " + ctx.getCurrentEvent().getUserType());
        EnterpriseApprovalHandler handler = getEnterpriseApprovalHandler(flowCase.getReferId());
        handler.onFlowCaseAbsorted(ctx);
    }

    private EnterpriseApprovalHandler getEnterpriseApprovalHandler(Long referId) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(referId);
        if(ga!=null){
            EnterpriseApprovalHandler handler = PlatformContext.getComponent(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX
                    + ga.getApprovalAttribute());
            if (handler != null) {
                return handler;
            }
        }
        return PlatformContext.getComponent(GeneralApprovalDefaultHandler.GENERAL_APPROVAL_DEFAULT_HANDLER_NAME);
    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        PostApprovalFormCommand cmd = JSON.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
        StringBuilder content = new StringBuilder(localeStringService.getLocalizedString("general_approval.key", "1", "zh_CN", "Name") + " : " + flowCase.getApplierName() + "\n");
        List<FlowCaseEntity> entities = processEntities(cmd.getValues());

    }

    private List<FlowCaseEntity> processEntities(List<PostApprovalFormItem> values){
        List<FlowCaseEntity> entities = new ArrayList<>();
        for(PostApprovalFormItem value : values){
            FlowCaseEntity e = new FlowCaseEntity();
            e.setKey(value.getFieldDisplayName() == null ? value.getFieldName() : value.getFieldDisplayName());
            //  just process the item which is not default
            if(!DEFAULT_FIELDS.contains(value.getFieldName())){
                switch (GeneralFormFieldType.fromCode(value.getFieldType())){
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
                        processAskForLeaveField(entities, e, value.getFieldValue());
                        break;
                    case BUSINESS_TRIP:
                        //出差
                        processBusinessTripField(entities, e, value.getFieldValue());
                        break;
                    case OVERTIME:
                        //加班
                        processOverTimeField(entities, e, value.getFieldValue());
                        break;
                    case GO_OUT:
                        //外出
                        processGoOutField(entities, e, value.getFieldValue());
                        break;
                    case ABNORMAL_PUNCH:
                        //打卡异常
                        processAbnormalPunchField(entities, e, value.getFieldValue());
                        break;
                }
            }

        }
        return null;
    }

    private void processAskForLeaveField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormAskForLeaveValue leaveValue = JSON.parseObject(jsonVal, PostApprovalFormAskForLeaveValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "1", "zh_CN", "请假类型"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getRestName());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "2", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getStartTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "3", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(leaveValue.getEndTime());
        entities.add(2, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.ask_for_leave.key", "4", "zh_CN", "请假时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(leaveValue.getDuration()) + " 天");
        entities.add(3, e);
    }

    private void processBusinessTripField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormBusinessTripValue tripValue = JSON.parseObject(jsonVal, PostApprovalFormBusinessTripValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(tripValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.business_trip.key", "3", "zh_CN", "出差时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(tripValue.getDuration()) + " 天");
        entities.add(2, e);
    }

    private void processOverTimeField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormOverTimeValue overTimeValue = JSON.parseObject(jsonVal, PostApprovalFormOverTimeValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(overTimeValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.overtime.key", "3", "zh_CN", "加班时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(overTimeValue.getDuration()) + " 天");
        entities.add(2, e);
    }

    private void processGoOutField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormGoOutValue outValue = JSON.parseObject(jsonVal, PostApprovalFormGoOutValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "1", "zh_CN", "开始时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getStartTime());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "2", "zh_CN", "结束时间"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(outValue.getEndTime());
        entities.add(1, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.go_out.key", "3", "zh_CN", "外出时长"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(decimalFormat.format(outValue.getDuration()) + " 天");
        entities.add(2, e);
    }

    private void processAbnormalPunchField(List<FlowCaseEntity> entities, FlowCaseEntity e, String jsonVal) {
        PostApprovalFormAbnormalPunchValue punchValue = JSON.parseObject(jsonVal, PostApprovalFormAbnormalPunchValue.class);
        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.abnormal_punch.key", "1", "zh_CN", "异常日期"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalDate());
        entities.add(0, e);

        e = new FlowCaseEntity();
        e.setKey(localeStringService.getLocalizedString("general_approval.abnormal_punch.key", "2", "zh_CN", "异常班次"));
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setValue(punchValue.getAbnormalItem());
        entities.add(1, e);
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {

    }
}
