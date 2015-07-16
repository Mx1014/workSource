package com.everhomes.locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.junit.TestCaseBase;

public class LocaleTest extends TestCaseBase {

    @Autowired
    private LocaleStringProvider localeProvider;
    
    @Test
    public void test() {
        LocaleString localeString = this.localeProvider.find("general", 505, "en_US");
        if(localeString != null)
            System.out.println(localeString.getText());
    }
}
