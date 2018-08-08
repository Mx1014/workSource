package com.everhomes.flow;

import com.everhomes.rest.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

// @Component
public class FlowModuleListenerDummy3 implements FlowFunctionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowModuleListenerDummy3.class);

    private Long moduleId = 40100L;

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
                            name = "vendorName1",
                            desc = @LocaleText(
                                    value = "Vendor name",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "vendorURL1",
                            desc = @LocaleText(
                                    value = "Vendor server url",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "appKey1",
                            desc = @LocaleText(
                                    value = "Vendor app key",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
            },
            id = 1234567L,
            version = 1
    )
    public void testExportFlowFunction1(FlowCaseState ctx, Map<String, String> param) {
        LOGGER.debug("testExportFlowFunction: param = {}", param);
    }

    @ExportFunction(
            desc = @LocaleText(
                    value = "When enter in end node, trigger this function.",
                    scope = FlowServiceErrorCode.SCOPE,
                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""),
            params = {
                    @FuncParam(
                            name = "vendorName2",
                            desc = @LocaleText(
                                    value = "Vendor name",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "vendorURL2",
                            desc = @LocaleText(
                                    value = "Vendor server url",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + "")),
                    @FuncParam(
                            name = "appKey2",
                            desc = @LocaleText(
                                    value = "Vendor app key",
                                    scope = FlowServiceErrorCode.SCOPE,
                                    code = FlowTemplateCode.COMMENT_STEP_CONTENT_WITH_APPLIER + ""))
            },
            id = 12345672222L,
            version = 1
    )
    public void testExportFlowFunction2(FlowCaseState ctx, Map<String, String> param) {
        LOGGER.debug("testExportFlowFunction: param = {}", param);
    }
}
