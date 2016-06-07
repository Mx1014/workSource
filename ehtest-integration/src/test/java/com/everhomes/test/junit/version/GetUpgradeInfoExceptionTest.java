// @formatter:off
package com.everhomes.test.junit.version;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.rest.version.GetUpgradeInfoRestResponse;
import com.everhomes.rest.version.VersionDTO;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.rest.version.VersionRequestCommand;
import com.everhomes.test.core.base.BaseServerTestCase;
import com.everhomes.test.core.http.HttpClientService;

public class GetUpgradeInfoExceptionTest extends BaseServerTestCase {
    @Autowired
    private HttpClientService httpClientService;
    
    @Before
    public void setUp() {
        // 用登录
    }
    
    @Test
    public void testNullRealm() {
        // realm字段为null
        VersionRequestCommand cmd = new VersionRequestCommand();
        
        String commandRelativeUri = "/version/getUpgradeInfo";
        GetUpgradeInfoRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            GetUpgradeInfoRestResponse.class, null);
        
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER
        assertTrue(response.getErrorDetails().contains("Invalid realm parameter in the command"));
    }
    
    @Test
    public void testEmptyRealm() {
        // realm字段为空
        VersionRequestCommand cmd = new VersionRequestCommand();
        cmd.setRealm("");
        
        String commandRelativeUri = "/version/getUpgradeInfo";
        GetUpgradeInfoRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            GetUpgradeInfoRestResponse.class, null);
        
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER
        assertTrue(response.getErrorDetails().contains("Invalid realm parameter in the command"));
    }
    
    @Test
    public void testInvalidVersion() {
        // 有realm字段，但没有版本
        VersionRequestCommand cmd = new VersionRequestCommand();
        cmd.setRealm(VersionRealmType.ANDROID.getCode());
        
        String commandRelativeUri = "/version/getUpgradeInfo";
        GetUpgradeInfoRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            GetUpgradeInfoRestResponse.class, null);
        
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER
        assertTrue(response.getErrorDetails().contains("Invalid current version parameter in the command"));
    }
    
    @Test
    public void testRealmNotFound() {
        // 有realm字段，但找不到记录
        VersionRequestCommand cmd = new VersionRequestCommand();
        cmd.setRealm(VersionRealmType.ANDROID.getCode());
        VersionDTO version = new VersionDTO();
        version.setMajor(3);
        version.setMinor(0);
        version.setRevision(0);
        version.setTag(null);
        cmd.setCurrentVersion(version);
        
        String commandRelativeUri = "/version/getUpgradeInfo";
        GetUpgradeInfoRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, 
            GetUpgradeInfoRestResponse.class, null);
        
        assertNotNull(response);
        assertEquals("general", response.getErrorScope()); // com.everhomes.constants.ErrorCodes.SCOPE_GENERAL
        assertEquals(Integer.valueOf(506), response.getErrorCode()); // com.everhomes.constants.ErrorCodes.ERROR_INVALID_PARAMETER
        assertTrue(response.getErrorDetails().contains("Invalid realm parameter in the command, realm does not exist"));
    }
    
    @After
    public void tearDown() {
        
    }
    
    protected void initCustomData() {
        // 不准备数据
    }
}

