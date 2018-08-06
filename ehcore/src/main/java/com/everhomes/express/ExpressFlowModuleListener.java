package com.everhomes.express;

import com.everhomes.flow.*;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.officecubicle.OfficeCubicleOrder;
import com.everhomes.officecubicle.OfficeCubicleProvider;
import com.everhomes.rest.express.ExpressOrderStatus;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.officecubicle.OfficeOrderType;
import com.everhomes.rest.officecubicle.OfficeOrderWorkFlowStatus;
import com.everhomes.rest.officecubicle.OfficeRentType;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 快递
 */
@Component
public class ExpressFlowModuleListener implements FlowModuleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressFlowModuleListener.class);
    @Autowired
    public static final long MODULE_ID = 40700L;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;
    @Autowired
    private ExpressOrderProvider expressOrderProvider;

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
    	LOGGER.debug("onFlowCreating");
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
    	LOGGER.debug("listServiceTypes");
    	List<FlowServiceTypeDTO> list = new ArrayList<FlowServiceTypeDTO>();
    	FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
    	dto.setNamespaceId(namespaceId);
    	dto.setServiceName("快递");
    	dto.setModuleId(MODULE_ID);
    	list.add(dto);
        return list;
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
    	LOGGER.debug("onFlowCaseStart");
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
    	LOGGER.debug("onFlowCaseAbsorted");
    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
    	LOGGER.debug("onFlowCaseStateChanged");
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        ExpressOrder expressOrder = expressOrderProvider.findExpressOrderById(flowCase.getReferId());
        FlowCaseStatus flowCaseStatus = FlowCaseStatus.fromCode(flowCase.getStatus());
        switch (flowCaseStatus){
            case PROCESS:expressOrder.setStatus(ExpressOrderStatus.PROCESSING.getCode());break;
            case ABSORTED:expressOrder.setStatus(ExpressOrderStatus.CANCELLED.getCode());break;
            case FINISHED:expressOrder.setStatus(ExpressOrderStatus.FINISHED.getCode());break;
            default:break;
        }
        expressOrderProvider.updateExpressOrder(expressOrder);
    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {
    	LOGGER.debug("onFlowCaseActionFired");
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
    	LOGGER.debug("onFlowCaseBriefRender");
    	return null;
    }
    
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        LOGGER.debug("onFlowCaseDetailRender");
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
    	LOGGER.debug("onFlowButtonFired");
    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
    	LOGGER.debug("onFlowCaseCreating");
    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {
    	LOGGER.debug("onFlowCaseCreated");
    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {
    	LOGGER.debug("onFlowSMSVariableRender");
    }

    @Override
    public void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) {
    	LOGGER.debug("onFlowMessageSend");
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
    	LOGGER.debug("onFlowVariableRender");
    	return null;
    }

    @Override
    public List<FlowConditionVariableDTO> listFlowConditionVariables(Flow flow, FlowEntityType flowEntityType, String ownerType, Long ownerId) {
    	LOGGER.debug("listFlowConditionVariables");
    	return null;
    }

    @Override
    public void onScanQRCode(FlowCase flowCase, QRCodeDTO qrCode, Long currentUserId) {
    	LOGGER.debug("onScanQRCode");
    }

    @Override
    public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String extra) {
    	LOGGER.debug("onFlowConditionVariableRender");
    	return null;
    }

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
    	LOGGER.debug("listFlowForms");
    	return null;
    }
}
