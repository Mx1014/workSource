package com.everhomes.test.junit.launchad;

import com.everhomes.rest.launchad.GetLaunchadRestResponse;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xq.tian on 2016/12/13.
 */
public class LaunchAdTest extends BaseLoginAuthTestCase {

    //1. 获取启动广告数据
    private static final String GE_LAUNCH_AD_URL = "/launchad/getLaunchad";

    //1. 获取启动广告数据
    @Test
    public void testGeLaunchAd() {
        GetLaunchadRestResponse response = httpClientService.restPost(GE_LAUNCH_AD_URL, null, GetLaunchadRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        LaunchAdDTO dto = response.getResponse();
        assertNotNull("The launch ad list should be not null.", dto);

        assertTrue("The launch ad content uri should starts with 'cs://'", dto.getContentUri().startsWith("cs://"));

        // assertTrue("The launch ad content url should starts with 'http://'", list.get(0).getContentUrl().startsWith("http://"));
        // assertTrue("The launch ad content url should starts with 'http://'", list.get(1).getContentUrl().startsWith("http://"));
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        /*String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);*/

        String jsonFilePath = "data/json/launchad-1.0-test-data-161213.json";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }
}
