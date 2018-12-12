package com.everhomes.officecubicle;

import com.everhomes.flow.*;
import com.everhomes.messaging.MessagingService;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
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
    	LOGGER.debug("onFlowCreating");
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
    	LOGGER.debug("listServiceTypes");
    	List<FlowServiceTypeDTO> list = new ArrayList<FlowServiceTypeDTO>();
    	FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
    	dto.setNamespaceId(namespaceId);
    	dto.setServiceName("工位预订");
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
    	LOGGER.debug("onFlowCaseActionFired");
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
    	LOGGER.debug("onFlowCaseBriefRender");
    	return null;
    }
    
    final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        LOGGER.debug("onFlowCaseDetailRender");
        OfficeCubicleOrder order = officeCubicleProvider.getOrderById(flowCase.getReferId());
        if (order == null)
            return null;
        List<FlowCaseEntity> list = new ArrayList<FlowCaseEntity>();
//        OfficeRentType officeRentType = OfficeRentType.fromCode(order.getRentType());
    	list.add(new FlowCaseEntity("预约人姓名", order.getReserverName() , FlowCaseEntityType.MULTI_LINE.getCode()));
    	list.add(new FlowCaseEntity("手机号码", order.getReserveContactToken(), FlowCaseEntityType.MULTI_LINE.getCode()));
    	list.add(new FlowCaseEntity("公司名称", order.getReserveEnterprise(), FlowCaseEntityType.MULTI_LINE.getCode()));
    	list.add(new FlowCaseEntity("预约空间", order.getSpaceName() , FlowCaseEntityType.MULTI_LINE.getCode()));
        list.add(new FlowCaseEntity("参观日期", order.getReserveTime() == null ? null : order.getReserveTime().toLocalDateTime().format(dtf), FlowCaseEntityType.MULTI_LINE.getCode()));
//        list.add(new FlowCaseEntity("工位类型", officeRentType.getMsg(), FlowCaseEntityType.MULTI_LINE.getCode()));
//        if (officeRentType == OfficeRentType.OPENSITE) {
//            list.add(new FlowCaseEntity("预订工位数", order.getPositionNums()+"", FlowCaseEntityType.MULTI_LINE.getCode()));
//        }else if(order.getCategoryName()==null){
//            list.add(new FlowCaseEntity("预订空间", "无", FlowCaseEntityType.MULTI_LINE.getCode()));
//        }else{
//            list.add(new FlowCaseEntity("预订空间", order.getCategoryName(), FlowCaseEntityType.MULTI_LINE.getCode()));
//        }
//        OfficeOrderType officeOrderType = OfficeOrderType.fromCode(order.getOrderType());
//        list.add(new FlowCaseEntity("预订类型", officeOrderType==null?"无":officeOrderType.getMsg(), FlowCaseEntityType.MULTI_LINE.getCode()));
    	list.add(new FlowCaseEntity("公司人数", order.getEmployeeNumber()==null?"无":order.getEmployeeNumber()+"", FlowCaseEntityType.MULTI_LINE.getCode()));
    	if(order.getFinancingFlag()==null){
    		list.add(new FlowCaseEntity("是否已融资","否", FlowCaseEntityType.MULTI_LINE.getCode()));
    	}else{
    		list.add(new FlowCaseEntity("是否已融资",order.getFinancingFlag()==1?"是":"否", FlowCaseEntityType.MULTI_LINE.getCode()));
    	}
    	return list;
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
    public FlowConditionVariable onFlowConditionVariableRender(FlowCaseState ctx, String variable, String entityType, Long entityId, String extra) {
    	LOGGER.debug("onFlowConditionVariableRender");
    	return null;
    }

    @Override
    public List<FlowFormDTO> listFlowForms(Flow flow) {
    	LOGGER.debug("listFlowForms");
    	return null;
    }
}
