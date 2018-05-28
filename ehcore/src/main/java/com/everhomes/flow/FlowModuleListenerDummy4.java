package com.everhomes.flow;

import com.everhomes.rest.flow.FlowServiceErrorCode;
import com.everhomes.rest.flow.FlowTemplateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

// @Component
public class FlowModuleListenerDummy4 implements FlowFunctionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowModuleListenerDummy4.class);

    private Long moduleId = 52000L;

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
            id = 1234567L,
            version = 1
    )
    public void testExportFlowFunction(FlowCaseState ctx, Map<String, String> param) {
        LOGGER.debug("testExportFlowFunction: param = {}", param);
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
            id = 1234567111111L,
            version = 1
    )
    public void testExportFlowFunction1(FlowCaseState ctx, Map<String, String> param) {
        LOGGER.debug("testExportFlowFunction: param = {}", param);
    }
}
