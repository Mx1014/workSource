package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import com.everhomes.util.Tuple;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Component
public class FlowModuleListenerDummy1 implements FlowModuleListener {
    @Autowired
    private FlowService flowService;

    private Long moduleId = 40100L;

    @Override
    public List<FlowServiceTypeDTO> listServiceTypes(Integer namespaceId, String ownerType, Long ownerId) {
        return null;
    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        module.setModuleName("Dummy1");
        module.setModuleId(moduleId);
        return module;
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        // TODO Auto-generated method stub
        return "testKey1: testValue1\ntestKey2: testValue2";
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
        List<FlowCaseEntity> entities = new ArrayList<>();
        FlowCaseEntity e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setKey("test-list-key");
        e.setValue("test-list-value");
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.LIST.getCode());
        e.setKey("test-list-key2");
        e.setValue("test-list-value2");
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
        e.setKey("test-multi-key3");
        e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
        entities.add(e);

        e = new FlowCaseEntity();
        e.setEntityType(FlowCaseEntityType.TEXT.getCode());
        e.setKey("test-text-key2");
        e.setValue("test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2test-list-value2");
        entities.add(e);

        return entities;
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onFlowButtonFired(FlowCaseState ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCreating(Flow flow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseCreating(FlowCase flowCase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowCaseCreated(FlowCase flowCase) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
                                        List<Tuple<String, Object>> variables) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, String> onFlowVariableRender(FlowCaseState ctx, List<String> vars) {
        Map<String, String> map = new HashMap<>();
        for (String var : vars) {
            map.put(var, "1");
        }
        return map;
    }
}
