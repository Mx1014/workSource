// @formatter:off
package com.everhomes.test.junit.flow.admin;

import com.everhomes.rest.flow.CreateFlowGraphJsonCommand;
import com.everhomes.rest.flow.FlowIdCommand;
import com.everhomes.rest.flow.ListPredefinedParamCommand;
import com.everhomes.rest.flow.admin.FlowCreateOrUpdateFlowGraphRestResponse;
import com.everhomes.rest.flow.admin.FlowGetFlowGraphRestResponse;
import com.everhomes.rest.flow.admin.FlowListPredefinedParamRestResponse;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FlowAdminTest extends BaseLoginAuthTestCase {

    // 获取工作流流程图
    private static final String getFlowGraphURL = "/admin/flow/getFlowGraph";
    // 保存工作流流程图
    private static final String createOrUpdateFlowGraphURL = "/admin/flow/createOrUpdateFlowGraph";
    // 预定义的参数列表
    private static final String listPredefinedParamURL = "/admin/flow/listPredefinedParam";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";


    // 获取工作流流程图
    @Test
    public void testGetFlowGraph() {
        testCreateOrUpdateFlowGraph();

        long flowId = 801L;
        FlowIdCommand cmd = new FlowIdCommand();
        cmd.setFlowId(flowId);

        FlowGetFlowGraphRestResponse response = httpClientService.restPost(getFlowGraphURL, cmd, FlowGetFlowGraphRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());

        assertTrue(response.getResponse().getFlowId() == flowId);
        assertNotNull(response.getResponse().getConditions());
        assertNotNull(response.getResponse().getLanes());
        assertNotNull(response.getResponse().getLinks());
        assertNotNull(response.getResponse().getNodes());
        System.out.println(response.getResponse());
    }

    // 保存工作流流程图
    @Test
    public void testCreateOrUpdateFlowGraph() {
        String json = "{\"flowId\":801,\"nodes\":[{\"nodeLevel\":1,\"nodeName\":\"nodeName\",\"flowLaneLevel\":1,\"nodeType\":\"start\"},{\"nodeLevel\":2,\"nodeName\":\"nodeName\",\"nodeType\":\"normal\",\"flowLaneLevel\":2},{\"nodeLevel\":3,\"nodeName\":\"nodeName\",\"nodeType\":\"condition_front\",\"flowLaneLevel\":3,\"branch\":{\"branchDecider\":\"condition\",\"processMode\":\"single\",\"originalNodeLevel\":3,\"convergenceNodeLevel\":12}},{\"nodeLevel\":4,\"nodeName\":\"nodeName\",\"nodeType\":\"normal\",\"flowLaneLevel\":3},{\"nodeLevel\":5,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"normal\"},{\"nodeLevel\":6,\"nodeName\":\"nodeName\",\"nodeType\":\"normal\",\"flowLaneLevel\":3},{\"nodeLevel\":7,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"condition_front\",\"branch\":{\"branchDecider\":\"processor\",\"processMode\":\"concurrent\",\"originalNodeLevel\":7,\"convergenceNodeLevel\":12}},{\"nodeLevel\":8,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"normal\"},{\"nodeLevel\":9,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"normal\"},{\"nodeLevel\":10,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"normal\"},{\"nodeLevel\":11,\"nodeName\":\"nodeName\",\"flowLaneLevel\":3,\"nodeType\":\"normal\"},{\"nodeLevel\":12,\"nodeName\":\"nodeName\",\"flowLaneLevel\":4,\"nodeType\":\"normal\"},{\"nodeLevel\":13,\"nodeName\":\"nodeName\",\"flowLaneLevel\":5,\"nodeType\":\"end\"}],\"links\":[{\"linkLevel\":1,\"fromNodeLevel\":1,\"toNodeLevel\":2},{\"fromNodeLevel\":2,\"linkLevel\":2,\"toNodeLevel\":3},{\"fromNodeLevel\":3,\"linkLevel\":3,\"toNodeLevel\":4},{\"fromNodeLevel\":3,\"linkLevel\":4,\"toNodeLevel\":5},{\"fromNodeLevel\":4,\"linkLevel\":5,\"toNodeLevel\":6},{\"fromNodeLevel\":6,\"linkLevel\":6,\"toNodeLevel\":7},{\"fromNodeLevel\":7,\"linkLevel\":7,\"toNodeLevel\":8},{\"fromNodeLevel\":7,\"linkLevel\":8,\"toNodeLevel\":9},{\"fromNodeLevel\":8,\"linkLevel\":9,\"toNodeLevel\":10},{\"fromNodeLevel\":10,\"linkLevel\":10,\"toNodeLevel\":11},{\"fromNodeLevel\":9,\"linkLevel\":11,\"toNodeLevel\":11},{\"fromNodeLevel\":11,\"linkLevel\":12,\"toNodeLevel\":12},{\"fromNodeLevel\":12,\"linkLevel\":13,\"toNodeLevel\":13}],\"lanes\":[{\"displayName\":\"displayName\",\"laneLevel\":1},{\"displayName\":\"displayName\",\"laneLevel\":2},{\"displayName\":\"displayName\",\"laneLevel\":3},{\"displayName\":\"displayName\",\"laneLevel\":4},{\"displayName\":\"displayName\",\"laneLevel\":5}],\"conditions\":[{\"conditionLevel\":1,\"nodeLevel\":1,\"nextNodeLevel\":2,\"flowLinkLevel\":3,\"expressions\":[{\"logicOperator\":\"&&\",\"relationalOperator\":\">=\",\"variableType1\":1,\"variable1\":\"${pa1}\",\"variableType2\":1,\"variable2\":\"${pa2}\"}]}]}";
        CreateFlowGraphJsonCommand cmd = new CreateFlowGraphJsonCommand();
        cmd.setBody(json);

        FlowCreateOrUpdateFlowGraphRestResponse response = httpClientService.restPost(createOrUpdateFlowGraphURL, cmd, FlowCreateOrUpdateFlowGraphRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());
    }

    // 预定义的参数列表
    @Test
    public void testListPredefinedParam() {
        ListPredefinedParamCommand cmd = new ListPredefinedParamCommand();
        cmd.setModuleType("any-module");
        cmd.setModuleId(1L);
        cmd.setOwnerType("EhFlows");
        cmd.setOwnerId(1L);
        cmd.setEntityType("flow_button");

        FlowListPredefinedParamRestResponse response = httpClientService.restPost(listPredefinedParamURL, cmd, FlowListPredefinedParamRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());
        assertTrue(response.getResponse().getParams().size() == 1);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before
    public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/flow-2.0-test-data-170911.json";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
}
