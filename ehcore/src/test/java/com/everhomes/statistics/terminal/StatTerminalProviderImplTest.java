package com.everhomes.statistics.terminal;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.enterprise.VerifyEnterpriseContactDTO;
import com.everhomes.util.WebTokenGenerator;
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
        String token = "LyQubhB9aDEVhdZEie-_fi-pi4jS6eDvTrSfPA_IHfGrnvvb0kqH4VORViOtx6dA8X46I-44FX5kABZY0EyMNYGZlXUPhUQ9VNg_FScmcOjFF2sElf9YapCKmwcXoFoT";
        VerifyEnterpriseContactDTO dto = WebTokenGenerator.getInstance().fromWebToken(token, VerifyEnterpriseContactDTO.class);
        System.out.println(dto);
        assertNotNull(dto);
    }

    @Test
    public void findLastAppVersion() throws Exception {
        AppVersion appVersion = statTerminalProvider.findLastAppVersion(999983);
        System.out.println(appVersion.getName());
    }

}