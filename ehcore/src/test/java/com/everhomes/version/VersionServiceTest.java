package com.everhomes.version;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.junit.TestCaseBase;

public class VersionServiceTest extends TestCaseBase {

    @Autowired
    private VersionProvider versionProvider;
    
    @Test
    public void testBasics() {
        
    }
}
