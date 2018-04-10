package com.everhomes.dbprovider;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.junit.CoreServerTestCase;
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
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());


    }
}
