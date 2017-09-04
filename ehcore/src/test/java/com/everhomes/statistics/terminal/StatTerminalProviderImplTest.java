package com.everhomes.statistics.terminal;

import com.everhomes.junit.CoreServerTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2017/9/1.
 */
public class StatTerminalProviderImplTest extends CoreServerTestCase {

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Test
    public void cleanTerminalAppVersionCumulativeByCondition() throws Exception {
        // statTerminalProvider.cleanTerminalAppVersionCumulativeByCondition(999983);
    }

    @Test
    public void findLastAppVersion() throws Exception {
        AppVersion appVersion = statTerminalProvider.findLastAppVersion(999983);
        System.out.println(appVersion.getName());
    }

}