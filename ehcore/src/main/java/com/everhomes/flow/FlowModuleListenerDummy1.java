package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlowModuleListenerDummy1 implements FlowModuleListener, FlowFunctionListener {

    @Autowired
    private FlowService flowService;

    private Long moduleId = 40100L;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        module.setModuleName("Dummy1");
        module.setModuleId(moduleId);
        return module;
    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase, FlowUserType flowUserType) {
        return null;
    }

    @ExportFunction(
            desc = @LocaleText(
                    value = "When enter in end node, trigger this function.",
                    scope = FlowServiceErrorCode.SCOPE,
                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
            params = {
                    @FuncParam(
                            name = @LocaleText(
                                    value = "vendor",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
                            desc = @LocaleText(
                                    value = "Vendor name",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = @LocaleText(
                                    value = "server url",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
                            desc = @LocaleText(
                                    value = "Vendor server url",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = @LocaleText(
                                    value = "vendor key",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
                            desc = @LocaleText(
                                    value = "Vendor app key",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
            },
            version = 1
    )
    public void testExportFlowFunction(
            FlowCaseState ctx,
            @FuncParam(
                    desc = @LocaleText(
                            value = "Vendor app key",
                            scope = FlowServiceErrorCode.SCOPE,
                            code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
                    String vendor,
            @FuncParam(
                    desc = @LocaleText(
                            value = "Vendor server url",
                            scope = FlowServiceErrorCode.SCOPE,
                            code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
                    String vendorServer,
            @FuncParam(
                    desc = @LocaleText(
                            value = "Vendor app key",
                            scope = FlowServiceErrorCode.SCOPE,
                            code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
                    String appKey) {


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
    public void onFlowButtonFired(FlowCaseState ctx) {

    }

    /*@Override
    public Map<String, String> onFlowVariableRender(FlowCaseState ctx, List<String> vars) {
        Map<String, String> map = new HashMap<>();
        for (String var : vars) {
            map.put(var, "1");
        }
        return map;
    }*/
}
