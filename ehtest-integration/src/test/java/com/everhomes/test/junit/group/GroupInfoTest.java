// @formatter:off
package com.everhomes.test.junit.group;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.group.CreateGroupCommand;
import com.everhomes.rest.group.CreateRestResponse;
import com.everhomes.rest.group.GetGroupCommand;
import com.everhomes.rest.group.GetRestResponse;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class GroupInfoTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetGroupInfo() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        Long groupId = 100597L;
        GetGroupCommand cmd = new GetGroupCommand();
        cmd.setGroupId(groupId);
        String commandRelativeUri = "/group/get";
        GetRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, GetRestResponse.class, this.context);
        
        assertNotNull("The reponse of getting group info may not be null", response);
        assertTrue("The group info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue("Group should be family type", GroupDiscriminator.fromCode(response.getResponse().getDiscriminator()) == GroupDiscriminator.FAMILY);
        
        groupId = 100615L;
        cmd = new GetGroupCommand();
        cmd.setGroupId(groupId);
        commandRelativeUri = "/group/get";
        response = httpClientService.restGet(commandRelativeUri, cmd, GetRestResponse.class, this.context);
        
        assertNotNull("The reponse of getting group info may not be null", response);
        assertTrue("The group info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue("Group should be group type", GroupDiscriminator.fromCode(response.getResponse().getDiscriminator()) == GroupDiscriminator.GROUP);
    }
    
    @Ignore @Test
    public void testCreateGroupInfo() {
        Integer namespaceId = 1000000;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        CreateGroupCommand cmd = new CreateGroupCommand();
        cmd.setName("打羽毛球");
        cmd.setAvatar("cs://1/image/aW1hZ2UvTVRvek5ESXhZV1ZtTnpJMVptUTJNV1kxTkdFMFl6STBZekEzTkdKak16UXhaQQ");
        cmd.setDescription("这是一个描述");
        cmd.setNamespaceId(0);
        cmd.setVisibleRegionType(VisibleRegionType.COMMUNITY.getCode());
        cmd.setVisibleRegionId(0L);
        String commandRelativeUri = "/group/create";
        CreateRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, CreateRestResponse.class, this.context);
        
        assertNotNull("The reponse of creating group info may not be null", response);
        assertTrue("The creating group info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertTrue("Group should be group type", GroupDiscriminator.fromCode(response.getResponse().getDiscriminator()) == GroupDiscriminator.GROUP);
    }
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
        String userInfoFilePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
        String groupInfoFilePath = "data/json/3.7.0-test-data-group_160708.txt";
        filePath = dbProvider.getAbsolutePathFromClassPath(groupInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

