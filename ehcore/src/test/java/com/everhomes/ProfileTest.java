package com.everhomes;

import com.everhomes.rest.profile.DismissReason;
import com.everhomes.rest.profile.DismissType;
import org.junit.Test;

public class ProfileTest {
    @Test
    public void test(){
        System.out.println(DismissReason.CAREERDEVELOPMENT.getCode());
    }
}
