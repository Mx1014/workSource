// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by xq.tian on 2017/3/10.
 */
@Repository
public class OAuth2ServerProviderImpl implements OAuth2ServerProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public OAuth2Server findOAuth2ServerByVendor(String vendor) {
        return context().selectFrom(Tables.EH_OAUTH2_SERVERS)
                .where(Tables.EH_OAUTH2_SERVERS.VENDOR.eq(vendor))
                .fetchAnyInto(OAuth2Server.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
