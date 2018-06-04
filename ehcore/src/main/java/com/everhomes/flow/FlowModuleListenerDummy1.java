package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// @Component
public class FlowModuleListenerDummy1 implements FlowFunctionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowModuleListenerDummy1.class);

    @Autowired
    private FlowService flowService;

    private Long moduleId = 20100L;

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo module = new FlowModuleInfo();
        module.setModuleName("Dummy1");
        module.setModuleId(moduleId);
        return module;
    }

    @ExportFunction(
            desc = @LocaleText(
                    value = "When enter in end node, trigger this function.",
                    scope = FlowServiceErrorCode.SCOPE,
                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
            params = {
                    @FuncParam(
                            name = "vendorName",
                            desc = @LocaleText(
                                    value = "Vendor name",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "vendorURL",
                            desc = @LocaleText(
                                    value = "Vendor server url",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "appKey",
                            desc = @LocaleText(
                                    value = "Vendor app key",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
            },
            id = 1111L,
            version = 1
    )
    public void testExportFlowFunction(FlowCaseState ctx, Map<String, String> param) {
        LOGGER.debug("testExportFlowFunction: param = {}", param);
    }
}
