package com.everhomes.locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.junit.CoreServerTestCase;

public class LocaleTest extends CoreServerTestCase {

    @Autowired
    private LocaleStringProvider localeProvider;
    
    @Test
    public void test() {
        LocaleString localeString = this.localeProvider.find("general", "505", "en_US");
        if(localeString != null)
            System.out.println(localeString.getText());
    }
}
