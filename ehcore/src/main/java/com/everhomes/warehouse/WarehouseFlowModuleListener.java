package com.everhomes.warehouse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.flow.*;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.module.ServiceModule;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.warehouse.CreateRequestCommand;
import com.everhomes.rest.warehouse.ReviewResult;
import com.everhomes.search.WarehouseRequestMaterialSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/5/17.
 */
@Component
public class WarehouseFlowModuleListener implements FlowModuleListener {
    private static final long MODULE_ID = 21000;
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseFlowModuleListener.class);
    @Autowired
    private FlowService flowService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private WarehouseRequestMaterialSearcher warehouseRequestMaterialSearcher;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        FlowModuleDTO moduleDTO = flowService.getModuleById(MODULE_ID);
        module.setModuleName(moduleDTO.getDisplayName());
        module.setModuleId(MODULE_ID);
        return module;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return null;
    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {

    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {

    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("step into onFlowCaseEnd, ctx: {}", ctx);
        }
        FlowCase flowCase = ctx.getFlowCase();
        Long operatorId = ctx.getOperator().getId();
        Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
        WarehouseRequests request = warehouseProvider.findWarehouseRequests(flowCase.getReferId(), flowCase.getProjectType(), flowCase.getProjectId());
        if(FlowCaseStatus.ABSORTED.equals(FlowCaseStatus.fromCode(flowCase.getStatus()))) {
            request.setReviewResult(ReviewResult.UNQUALIFIED.getCode());
            request.setUpdateTime(current);
            warehouseProvider.updateWarehouseRequest(request);
            updateWarehouseRequestMaterials(request, operatorId);
        }

        else if(FlowCaseStatus.FINISHED.equals(FlowCaseStatus.fromCode(flowCase.getStatus()))) {
            request.setReviewResult(ReviewResult.QUALIFIED.getCode());
            request.setUpdateTime(current);
            warehouseProvider.updateWarehouseRequest(request);
            updateWarehouseRequestMaterials(request, operatorId);
        }
    }

    private void updateWarehouseRequestMaterials(WarehouseRequests request, Long operatorId) {
        List<WarehouseRequestMaterials> materials = warehouseProvider.listWarehouseRequestMaterials(request.getId(), request.getOwnerType(), request.getOwnerId());
        if(materials != null && materials.size() > 0) {
            Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
            materials.forEach(material -> {
                material.setReviewResult(request.getReviewResult());
                material.setReviewUid(operatorId);
                material.setReviewTime(current);
                warehouseProvider.updateWarehouseRequestMaterial(material);
                warehouseRequestMaterialSearcher.feedDoc(material);
            });
        }
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
    }

    @Override
    public void onFlowCreating(Flow flow) {

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {

    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }
}
