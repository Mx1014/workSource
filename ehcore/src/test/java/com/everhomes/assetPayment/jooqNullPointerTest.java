//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.PaymentBillItems;
import com.everhomes.asset.PaymentExemptionItems;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.server.schema.tables.daos.EhPaymentBillItemsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentExemptionItemsDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems;
import com.everhomes.util.DateHelper;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.jooq.DSLContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/18.
 */

public class jooqNullPointerTest extends CoreServerTestCase{
    @Autowired
    private DbProvider dbProvider;
    @Test
    public void fun(){
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            EhPaymentExemptionItemsDao dao = new EhPaymentExemptionItemsDao(context.configuration());
            List<EhPaymentExemptionItems> list = new ArrayList<>();
            PaymentExemptionItems item = new PaymentExemptionItems();
            item.setAmount(new BigDecimal("555"));
            item.setBillGroupId(2l);
            item.setBillId(30l);
            item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            item.setCreatorUid(000000l);
            item.setId(50l);
            item.setRemarks("test insert");
            item.setTargetname("YANYANG");
            list.add(item);
            dao.insert(list);
            System.out.println("succceed");
            return null;
        });
    }
}
