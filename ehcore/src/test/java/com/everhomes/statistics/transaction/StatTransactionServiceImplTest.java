package com.everhomes.statistics.transaction;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.sms.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by xq.tian on 2018/2/8.
 */
public class StatTransactionServiceImplTest extends CoreServerTestCase {

    @Autowired
    private StatTransactionService statTransactionService;

    @Test
    public void syncPaidPlatformToStatTransaction() throws Exception {
        String sDate = DateUtil.dateToStr(Date.valueOf(LocalDate.now()), DateUtil.YMR_SLASH);
        statTransactionService.syncShopToStatOrderByDate(sDate);
        statTransactionService.syncNewPaidPlatformToStatTransaction(sDate);
    }
}