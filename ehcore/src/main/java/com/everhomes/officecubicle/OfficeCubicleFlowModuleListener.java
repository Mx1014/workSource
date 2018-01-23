package com.everhomes.officecubicle;

import com.everhomes.flow.*;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.officecubicle.OfficeOrderWorkFlowStatus;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfficeCubicleFlowModuleListener implements FlowModuleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleFlowModuleListener.class);
    @Autowired
    public static final long MODULE_ID = 40200L;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private OfficeCubicleProvider officeCubicleProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(MODULE_ID);
        return moduleInfo;
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        return null;
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        OfficeCubicleOrder order = officeCubicleProvider.getOrderById(flowCase.getReferId());
        OfficeOrderWorkFlowStatus wfStatus = OfficeOrderWorkFlowStatus.fromType(flowCase.getStatus());
        if(wfStatus!=null){
            order.setWorkFlowStatus(wfStatus.getCode());
            officeCubicleProvider.updateOrder(order);
        }else {
            LOGGER.info("flowCase id = {}, status = {}", flowCase.getId(), flowCase.getStatus());
        }
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

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

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {

    }

    @Override
    public void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) {

    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }

    @Override
    public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        return null;
    }

    @Override
    public void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) {

    }

    @Override
    public List<FlowPredefinedParamDTO> listFlowPredefinedParam(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
        return null;
    }

    @Override
    public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) {
        return null;
    }

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
        return null;
    }
}
