package com.everhomes.junit;

import com.everhomes.uniongroup.UniongroupConfigureProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class Test extends CoreServerTestCase {

    @Autowired
    private UniongroupConfigureProvider uniongroupConfigureProvider;

    @org.junit.Test
    public void test() {
//        uniongroupConfigureProvider.cloneGroupTypeDataToVersion(null,null,null,null, null);
    }
}
