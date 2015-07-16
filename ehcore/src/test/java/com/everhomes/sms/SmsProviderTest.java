package com.everhomes.sms;


import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.everhomes.junit.TestCaseBase;

public class SmsProviderTest extends TestCaseBase {
    @Autowired
    @Qualifier("smsProvider")
    SmsProvider smsProvider;

    @Ignore @Test
    public void testSendMessageOK() {
        try {
            smsProvider.sendSms(new String[]{"15889660710","18565600064"}, "测试修改后发短信");
        } catch (Throwable e) {
            fail();
        }
    }

}
