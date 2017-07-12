package com.everhomes.test.junit.acl;

import com.everhomes.rest.acl.*;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sfyan on 2016/11/4.
 */
public class ListAuthorizationServiceModuleMembersTest extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        logoff();
    }

    @Test
    public void testListAuthorizationServiceModuleMembers(){
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        String commandRelativeUri = "/acl/listAuthorizationServiceModuleMembers";
        ListAuthorizationServiceModuleCommand cmd = new ListAuthorizationServiceModuleCommand();
        cmd.setOrganizationId(1000751L);
        cmd.setOwnerType("EhOrganizations");
        cmd.setOwnerId(1000750L);
        ListAuthorizationServiceModuleMembersRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListAuthorizationServiceModuleMembersRestResponse.class, context);
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" +
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertNotNull(response.getResponse());
        assertEquals(3, response.getResponse().size());
        checkMemberData(response.getResponse());
    }

    private void checkMemberData(List<AuthorizationServiceModuleMembersDTO> dtos){
        List<String> contactTokens = new ArrayList<>();
        contactTokens.add("12000000010");
        contactTokens.add("12000000011");
        contactTokens.add("12000000012");
        for (AuthorizationServiceModuleMembersDTO dto: dtos) {
            assertEquals(true, contactTokens.contains(dto.getContactToken()));
            assertNotNull(dto.getAuthorizationServiceModules());
            checkModuleData(dto.getAuthorizationServiceModules(), dto.getContactToken());
        }
    }

    private void checkModuleData(List<AuthorizationServiceModuleDTO> dtos, String contactToken){
        assertEquals(2, dtos.size());
        List<Long> cIds = new ArrayList<>();
        cIds.add(24210090697425925L);
        cIds.add(24210090697425926L);
        for (AuthorizationServiceModuleDTO dto: dtos) {
            assertEquals(true, cIds.contains(dto.getResourceId()));
            if(24210090697425925L == dto.getResourceId()){
                assertNotNull(dto.getAssignments());
                assertEquals(1, dto.getAssignments().size());
                List<Long> mIds = new ArrayList<>();

                mIds.add(1L);
                for (ServiceModuleAssignmentDTO serviceModuleAssignmentDTO: dto.getAssignments()) {
                    assertEquals(true, mIds.contains(serviceModuleAssignmentDTO.getModuleId()));
                }
            }
            if(24210090697425926L == dto.getResourceId()){
                assertNotNull(dto.getAssignments());
                List<Long> mIds = new ArrayList<>();
                if("12000000010".equals(contactToken)){
                    mIds.add(2L);
                    assertEquals(1, dto.getAssignments().size());
                }else{
                    mIds.add(1L);
                    mIds.add(2L);
                    mIds.add(3L);
                    assertEquals(3, dto.getAssignments().size());
                }
                for (ServiceModuleAssignmentDTO serviceModuleAssignmentDTO: dto.getAssignments()) {
                    assertEquals(true, mIds.contains(serviceModuleAssignmentDTO.getModuleId()));
                }
            }
        }
    }

    protected void initCustomData() {
        String filePath = "data/json/3.4.x-test-data-userinfo_160605.txt";
        String file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/organization-member-test-data_161104.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/service_module_assignments-test-data_161017.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/service_module-test-data_161104txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);

        filePath= "data/json/community-test-data_161104.txt";
        file = dbProvider.getAbsolutePathFromClassPath(filePath);
        dbProvider.loadJsonFileToDatabase(file, false);
    }
}
