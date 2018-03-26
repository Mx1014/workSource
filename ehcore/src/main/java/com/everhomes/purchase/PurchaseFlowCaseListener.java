//@formatter:off
package com.everhomes.purchase;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/2/6.
 */
@Component
public class PurchaseFlowCaseListener extends AbstractFlowModuleListener {
    @Autowired
    private FlowService flowService;
    @Autowired
    private PurchaseProvider purchaseProvider;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(FlowConstants.PURCHASE_MODULE);
        module.setModuleName(moduleDTO.getDisplayName());
        module.setModuleId(FlowConstants.PURCHASE_MODULE);
        return module;
    }

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        List<FlowServiceTypeDTO> result = new ArrayList<>();
        FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
        dto.setNamespaceId(namespaceId);
        dto.setServiceName("采购申请");
        return result;
    }
    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        purchaseProvider.changePurchaseOrderStatus2Target(PurchaseOrderStatus.HANDLING.getCode(),referId);
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        purchaseProvider.changePurchaseOrderStatus2Target(PurchaseOrderStatus.FINISH.getCode(),referId);
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        FlowCase flowCase = ctx.getFlowCase();
        Long referId = flowCase.getReferId();
        purchaseProvider.changePurchaseOrderStatus2Target(PurchaseOrderStatus.CANCELED.getCode(),referId);
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
