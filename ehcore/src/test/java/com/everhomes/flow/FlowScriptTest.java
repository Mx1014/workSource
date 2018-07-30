package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.action.FlowGraphScriptAction;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhFlowScriptsRecord;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.*;

public class FlowScriptTest extends LoginAuthTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowScriptTest.class);

    @Configuration
    @ComponentScan(basePackages = {
            "com.everhomes"
    })
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
    })
    static class ContextConfiguration {
    }

    @Autowired
    public FlowProvider flowProvider;

    @Autowired
    private FlowService flowService;

    @Autowired
    private NashornEngineService nashornEngineService;

    @Autowired
    private DbProvider dbProvider;

    private final static String SCRIPT = "var configService = require(\"configService\");\n" +
            "var apiService = require(\"apiService\");\n" +
            "var fetch = require(\"fetch\");\n" +
            "\n" +
            "var app = {};\n" +
            "\n" +
            "// 导出配置，用于管理员自定义配置\n" +
            "app.config = {\n" +
            "    vendor: {\n" +
            "        description: \"供应商名字\",\n" +
            "        default: \"通联支付\",\n" +
            "        validator: function (value) {\n" +
            "            return value.indexOf(\"支付\") !== -1;\n" +
            "        }\n" +
            "    },\n" +
            "    vendorURL: {\n" +
            "        description: \"供应商服务器地址\",\n" +
            "        default: \"http://zzz.com\"\n" +
            "    }\n" +
            "};\n" +
            "\n" +
            "// 入口函数，工作流后台启用脚本后，一旦满足触发条件，则会调用此函数，主要业务逻辑所在\n" +
            "app.main = function (ctx) {\n" +
            "    // 提供的API用于获取管理员在后台配置的属性值\n" +
            "    var config = configService(ctx.flow.id, ctx.flow.flowVersion);\n" +
            "\n" +
            "    // 通过 apiService 获取 api 提供对象\n" +
            "    var flowService = apiService.get(\"flowService\");\n" +
            "    var moduleService = apiService.get(\"moduleService\");\n" +
            "\n" +
            "    var vendor = config.getString(\"vendor\");\n" +
            "    var vendorURL = config.getObject(\"vendorURL\");\n" +
            "    var vendorKey = config.getInt(\"vendorKey\");\n" +
            "\n" +
            "    fetch(\"http://www.baidu.com\", function (result) {\n" +
            "        print(result);\n" +
            "    }, function (reason) {\n" +
            "        print(reason);\n" +
            "    });\n" +
            "\n" +
            "    print(vendor);\n" +
            "    print(vendorURL);\n" +
            "    print(vendorKey);\n" +
            "\n" +
            "    flowService.testDummyCall();\n" +
            "\n" +
            "    return vendorURL;\n" +
            "};\n" +
            "\n" +
            "module.exports = app;";

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCreateFlowScript() {
        CreateFlowScriptCommand cmd = getCreateFlowScriptCommand();
        FlowScriptDTO flowScript = flowService.createFlowScript(cmd);
        assertNotNull(flowScript);
    }

    private CreateFlowScriptCommand getCreateFlowScriptCommand() {
        CreateFlowScriptCommand cmd = new CreateFlowScriptCommand();
        cmd.setDescription("desc");
        cmd.setFlowId(getFlowGraph().getFlow().getId());
        cmd.setName("script1");
        cmd.setScript(SCRIPT);
        cmd.setScriptType(FlowScriptType.JAVASCRIPT.getCode());
        return cmd;
    }

    @Test
    public void testUpdateFlowScript() {
        CreateFlowScriptCommand createCmd = getCreateFlowScriptCommand();
        FlowScriptDTO flowScript = flowService.createFlowScript(createCmd);
        assertNotNull(flowScript);

        UpdateFlowScriptCommand cmd = new UpdateFlowScriptCommand();
        cmd.setId(flowScript.getId());
        cmd.setDescription("newDesc11111");
        cmd.setScript("var a = 22222;");
        cmd.setName("newName21111");

        FlowScriptDTO dto = flowService.updateFlowScript(cmd);
        assertNotNull(dto);
    }

    @Test
    public void testListFlowScript() {
        ListFlowScriptsCommand cmd = new ListFlowScriptsCommand();
        cmd.setFlowId(getFlowGraph().getFlow().getId());
        cmd.setScriptType(FlowScriptType.JAVASCRIPT.getCode());

        ListFlowScriptsResponse response = flowService.listFlowScripts(cmd);
        assertNotNull(response);
        LOGGER.debug(response.toString());
    }

    @Test
    public void testDeleteFlowScript() {
        DeleteFlowScriptCommand cmd = new DeleteFlowScriptCommand();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        EhFlowScriptsRecord record = context.selectFrom(Tables.EH_FLOW_SCRIPTS).orderBy(Tables.EH_FLOW_SCRIPTS.SCRIPT_VERSION.desc()).fetchAny();
        cmd.setId(record.getId());
        flowService.deleteFlowScript(cmd);
    }

    @Test
    public void testUpdateFlowNode1() {
        FlowGraph flowGraph = getFlowGraph();

        UpdateFlowNodeCommand cmd = new UpdateFlowNodeCommand();
        Long flowNodeId = flowGraph.getIdToNode().entrySet().iterator().next().getKey();
        cmd.setFlowNodeId(flowNodeId);

        FlowActionInfo flowScriptCommand = getFlowScriptCommand(flowGraph.getFlow(), FlowScriptType.JAVA);
        cmd.setEnterScript(flowScriptCommand);

        FlowNodeDTO flowNodeDTO = flowService.updateFlowNode(cmd);
        assertNotNull(flowNodeDTO);

        FlowNodeDetailDTO flowNodeDetail = flowService.getFlowNodeDetail(flowNodeId);
        assertNotNull(flowNodeDetail);
        LOGGER.debug(flowNodeDetail.toString());
    }

    @Test
    public void testListFlowScriptConfig() {
        ListFlowScriptsCommand cmd = new ListFlowScriptsCommand();
        cmd.setFlowId(getFlowGraph().getFlow().getId());
        cmd.setScriptType(FlowScriptType.JAVASCRIPT.getCode());

        ListFlowScriptsResponse response = flowService.listFlowScripts(cmd);

        assertNotNull(response.getDtos());

        for (FlowScriptDTO dto : response.getDtos()) {
            for (FlowScriptConfigDTO configDTO : dto.getConfigs()) {
                LOGGER.debug("FlowScriptConfigDTO: {}", configDTO);
            }
        }
    }

    private FlowGraph getFlowGraph() {
        FlowDTO flow = flowService.getFlowById(3L);
        return flowService.getFlowGraph(flow.getId(), FlowConstants.FLOW_CONFIG_VER);
    }

    @Test
    public void testGetFlowButtonDetail() {
        FlowGraph flowGraph = getFlowGraph();
        FlowButtonDetailDTO detailDTO = flowService.getFlowButtonDetail(flowGraph.getNodes().iterator().next().getProcessorButtons().iterator().next().getFlowButton().getId());
        assertNotNull(detailDTO);
        LOGGER.debug(detailDTO.toString());
    }

    @Test
    public void testUpdateFlowButton() {
        FlowGraph flowGraph = getFlowGraph();

        UpdateFlowButtonCommand cmd = new UpdateFlowButtonCommand();
        cmd.setFlowButtonId(flowGraph.getNodes().iterator().next().getProcessorButtons().iterator().next().getFlowButton().getId());
        FlowActionInfo flowScriptCommand = getFlowScriptCommand(flowGraph.getFlow(), FlowScriptType.JAVA);
        cmd.setScript(flowScriptCommand);

        FlowButtonDetailDTO flowButton = flowService.updateFlowButton(cmd);
        assertNotNull(flowButton);

        LOGGER.debug(flowButton.toString());
    }

    @Test
    public void testUpdateFlowButton1() {
        FlowGraph flowGraph = getFlowGraph();

        UpdateFlowButtonCommand cmd = new UpdateFlowButtonCommand();
        cmd.setFlowButtonId(flowGraph.getNodes().iterator().next().getProcessorButtons().iterator().next().getFlowButton().getId());
        FlowActionInfo flowScriptCommand = getFlowScriptCommand(flowGraph.getFlow(), FlowScriptType.JAVASCRIPT);
        // for (FlowScriptConfigInfo info : flowScriptCommand.getEnterScript().getScriptConfigs().getConfigs()) {
        //     info.setFieldValue("不合法的配置值");
        // }
        cmd.setScript(flowScriptCommand);

        FlowButtonDetailDTO flowButton = flowService.updateFlowButton(cmd);
        assertNotNull(flowButton);

        LOGGER.debug(flowButton.toString());
    }

    private FlowActionInfo getFlowScriptCommand(Flow flow, FlowScriptType scriptType) {
        FlowActionInfo enterScript = new FlowActionInfo();
        enterScript.setEnabled(FlowActionStatus.ENABLED.getCode());

        FlowScriptInfo scriptConfigCmd = new FlowScriptInfo();

        ListFlowScriptsCommand listCmd = new ListFlowScriptsCommand();
        listCmd.setFlowId(flow.getId());
        listCmd.setScriptType(scriptType.getCode());

        ListFlowScriptsResponse response = flowService.listFlowScripts(listCmd);

       response.getDtos().stream().max(Comparator.comparingLong(FlowScriptDTO::getId)).ifPresent(next -> {
            LOGGER.debug("FlowScriptDTO: " + next);
            scriptConfigCmd.setScriptType(next.getScriptType());
            scriptConfigCmd.setId(next.getId());
            scriptConfigCmd.setScriptVersion(next.getScriptVersion());

            List<FlowScriptConfigInfo> configs = new ArrayList<>();
            for (FlowScriptConfigDTO dto : next.getConfigs()) {
                FlowScriptConfigInfo e = new FlowScriptConfigInfo();
                e.setFieldName(dto.getFieldName());
                e.setFieldValue(dto.getFieldValue());
                configs.add(e);
            }
           scriptConfigCmd.setConfigs(configs);
       });

        enterScript.setScript(scriptConfigCmd);
        LOGGER.debug("enterScript: " + enterScript);
        return enterScript;
    }

    // private ListFlowScriptConfigsResponse getFlowScriptConfigs(FlowScriptDTO next) {
    //     ListFlowScriptConfigsCommand listConfigCmd = new ListFlowScriptConfigsCommand();
    //     listConfigCmd.setModuleId(next.getModuleId());
    //     listConfigCmd.setScriptId(next.getId());
    //     listConfigCmd.setScriptType(next.getScriptType());
    //
    //     DeferredResult<RestResponse> deferredResult = flowService.listFlowScriptConfigs(listConfigCmd);
    //     try {
    //         Thread.sleep(3000L);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    //     RestResponse result = (RestResponse) deferredResult.getResult();
    //     return (ListFlowScriptConfigsResponse) result.getResponseObject();
    // }

    @Test
    public void testFireFlowScriptAction() {
        FlowGraph flowGraph = getFlowGraph();

        UpdateFlowNodeCommand cmd = new UpdateFlowNodeCommand();
        Long flowNodeId = flowGraph.getIdToNode().entrySet().iterator().next().getKey();
        cmd.setFlowNodeId(flowNodeId);

        FlowActionInfo flowScriptCommand = getFlowScriptCommand(flowGraph.getFlow(), FlowScriptType.JAVASCRIPT);
        cmd.setEnterScript(flowScriptCommand);

        FlowNodeDTO flowNodeDTO = flowService.updateFlowNode(cmd);
        assertNotNull(flowNodeDTO);

        FlowNodeDetailDTO flowNodeDetail = flowService.getFlowNodeDetail(flowNodeId);
        LOGGER.debug("flowNodeDetail: {}", flowNodeDetail);

        FlowGraphScriptAction scriptAction = new FlowGraphScriptAction();
        FlowAction flowAction = ConvertHelper.convert(flowNodeDetail.getEnterScript().getScript(), FlowAction.class);
        flowAction.setScriptMainId(flowNodeDetail.getEnterScript().getScript().getScriptMainId());
        flowAction.setScriptVersion(flowNodeDetail.getEnterScript().getScript().getScriptVersion());

        LOGGER.debug("flowAction: {}", flowAction);

        FlowCaseState ctx = new FlowCaseState();
        ctx.setFlowGraph(flowGraph);
        FlowCase flowCase = new FlowCase();
        flowCase.setFlowMainId(flowGraph.getFlow().getTopId());
        flowCase.setFlowVersion(flowGraph.getFlow().getFlowVersion());
        FlowModuleInfo module = new FlowModuleInfo();
        module.setModuleId(40100L);
        ctx.setModule(module);
        ctx.setFlowCase(flowCase);
        scriptAction.setFlowAction(flowAction);

        scriptAction.fireAction(ctx, null);
    }

    @Test
    public void testFireFlowScriptAction1() {
        FlowGraph flowGraph = getFlowGraph();

        UpdateFlowNodeCommand cmd = new UpdateFlowNodeCommand();
        Long flowNodeId = flowGraph.getIdToNode().entrySet().iterator().next().getKey();
        cmd.setFlowNodeId(flowNodeId);

        FlowActionInfo flowScriptCommand = getFlowScriptCommand(flowGraph.getFlow(), FlowScriptType.JAVASCRIPT);
        cmd.setEnterScript(flowScriptCommand);

        FlowNodeDTO flowNodeDTO = flowService.updateFlowNode(cmd);
        assertNotNull(flowNodeDTO);

        FlowNodeDetailDTO flowNodeDetail = flowService.getFlowNodeDetail(flowNodeId);
        LOGGER.debug("flowNodeDetail: {}", flowNodeDetail);

        FlowGraphScriptAction scriptAction = new FlowGraphScriptAction();
        FlowAction flowAction = ConvertHelper.convert(flowNodeDetail.getEnterScript().getScript(), FlowAction.class);
        flowAction.setScriptMainId(flowNodeDetail.getEnterScript().getScript().getScriptMainId());
        flowAction.setScriptVersion(flowNodeDetail.getEnterScript().getScript().getScriptVersion());

        LOGGER.debug("flowAction: {}", flowAction);

        FlowCaseState ctx = new FlowCaseState();
        ctx.setFlowGraph(flowGraph);
        FlowCase flowCase = new FlowCase();
        flowCase.setFlowMainId(flowGraph.getFlow().getTopId());
        flowCase.setFlowVersion(flowGraph.getFlow().getFlowVersion());
        FlowModuleInfo module = new FlowModuleInfo();
        module.setModuleId(40100L);
        ctx.setModule(module);
        ctx.setFlowCase(flowCase);
        scriptAction.setFlowAction(flowAction);

        scriptAction.fireAction(ctx, null);
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testScriptMain() {
        FlowAction flowAction = new FlowAction();
        FlowCaseState state = new FlowCaseState();
        state.setFlowCase(new FlowCase());
        FlowGraph flowGraph = new FlowGraph();
        flowGraph.setFlow(new Flow());
        state.setFlowGraph(flowGraph);
        state.setOperator(new UserInfo());

        FlowScript script = new FlowScript();
        script.setScriptMainId(1L);
        script.setScriptVersion(0);

        String js = new Scanner(this.getClass().getResourceAsStream("/flow/demo.js")).useDelimiter("\\A").next();
        script.setScript(js);

        // NashornScriptMain main = new NashornScriptMain(state, script, flowAction);

        // main.process(nashornEngineService);
    }

    @Test
    public void testScriptMapping() {
        FlowScript script = new FlowScript();
        script.setScriptMainId(1L);
        script.setScriptVersion(0);

        String js = new Scanner(this.getClass().getResourceAsStream("/flow/demo.js")).useDelimiter("\\A").next();
        script.setScript(js);

        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("a", new String[]{"b"});


        // Object result = new NashornScriptMappingCall(script, "https", paramMap, "body1", new DeferredResult<>()).process(nashornEngineService);
        // LOGGER.info("script mapping: {}", result);
    }
}
