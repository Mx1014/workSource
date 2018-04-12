// @formatter:off
package com.everhomes.salary;

import com.everhomes.junit.CoreServerTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class SalaryServiceTest extends CoreServerTestCase {

    @Autowired
    private SalaryService salaryService;


//    @Ignore
    @Test
    public void testCalculateBonusTax() {
        BigDecimal d = salaryService.calculateBonusTax(new BigDecimal(12000), new BigDecimal(3000));
        System.out.println("年终12000,工资3000的税是" + d);
    }
}

