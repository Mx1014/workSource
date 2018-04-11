package com.everhomes.dbprovider;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.schema.Tables;
import com.everhomes.schema.tables.records.EhConfigurationsRecord;
import org.jooq.DSLContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2018/4/10.
 */
public class DbProviderTest extends CoreServerTestCase {

    @Autowired
    private DbProvider dbProvider;

    @Test
    public void testDbProvider() {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
                EhConfigurationsRecord any = context.selectFrom(Tables.EH_CONFIGURATIONS).fetchAny();
                Integer id = any.getId();
            }).start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
