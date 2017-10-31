// @formatter:off
package com.everhomes.test.junit.flow;

import com.everhomes.rest.flow.FireButtonRestResponse;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEvaluateItemStar;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowPostEvaluateCommand;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FlowTest extends BaseLoginAuthTestCase {

    // 按钮触发操作
    private static final String fireButtonURL = "/flow/fireButton";
    // -------------------------------------
    private String userIdentifier = "9201000";
    private String plainTextPwd = "123456";

    // 按钮触发操作
    @Test
    public void testFireButton() {
        FlowFireButtonCommand cmd = new FlowFireButtonCommand();
        cmd.setFlowCaseId(1L);
        cmd.setButtonId(1L);
        cmd.setStepCount(1L);
        cmd.setNextNodeId(1L);
        List<String> imagesList = new ArrayList<>();
        imagesList.add("images");
        cmd.setImages(imagesList);
        cmd.setTitle("title");
        cmd.setContent("content");
        cmd.setEntityId(1L);
        cmd.setFlowEntityType("flowEntityType");
        List<FlowEntitySel> entitySelList = new ArrayList<>();
        FlowEntitySel entitySel = new FlowEntitySel();
        entitySel.setEntityId(1L);
        entitySel.setFlowEntityType("flowEntityType");
        entitySelList.add(entitySel);
        cmd.setEntitySel(entitySelList);
        FlowPostEvaluateCommand evaluate = new FlowPostEvaluateCommand();
        evaluate.setUserId(1L);
        evaluate.setFlowCaseId(1L);
        evaluate.setFlowNodeId(1L);
        evaluate.setStepCount(1L);
        List<FlowEvaluateItemStar> starsList = new ArrayList<>();
        FlowEvaluateItemStar stars = new FlowEvaluateItemStar();
        stars.setItemId(1L);
        stars.setStat((byte) 1);
        stars.setContent("content");
        starsList.add(stars);
        evaluate.setStars(starsList);
        cmd.setEvaluate(evaluate);

        FireButtonRestResponse response = httpClientService.restPost(fireButtonURL, cmd, FireButtonRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull("response should be not null", response.getResponse());
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before public void setUp() {
        super.newSetUp();
        logon();
    }

    @Override protected void initCustomData() {
        // String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        // String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        // jsonFilePath = "data/json/organizationfile-1.0-test-data-170217.json";
        // fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        // dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After public void tearDown() {
        super.tearDown();
        logoff();
    }
}
