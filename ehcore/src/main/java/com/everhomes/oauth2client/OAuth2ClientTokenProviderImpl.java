// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.oauth2client.OAuth2ClientTokenType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOauth2ClientTokensDao;
import com.everhomes.server.schema.tables.pojos.EhOauth2ClientTokens;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Created by xq.tian on 2017/3/10.
 */
@Repository
public class OAuth2ClientTokenProviderImpl implements OAuth2ClientTokenProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createToken(OAuth2ClientToken token) {
        long nextId = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOauth2ClientTokens.class));
        token.setId(nextId);
        token.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().insert(token);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOauth2ClientTokens.class, nextId);
    }

    @Override
    public OAuth2ClientToken findLastTokenByUserAndVendor(Long userId, String vendor, OAuth2ClientTokenType tokenType) {
        return context().selectFrom(Tables.EH_OAUTH2_CLIENT_TOKENS)
                .where(Tables.EH_OAUTH2_CLIENT_TOKENS.GRANTOR_UID.eq(userId))
                .and(Tables.EH_OAUTH2_CLIENT_TOKENS.TYPE.eq(tokenType.getCode()))
                .and(Tables.EH_OAUTH2_CLIENT_TOKENS.VENDOR.eq(vendor))
                .orderBy(Tables.EH_OAUTH2_CLIENT_TOKENS.EXPIRATION_TIME.desc())
                .fetchAnyInto(OAuth2ClientToken.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private EhOauth2ClientTokensDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhOauth2ClientTokensDao(context.configuration());
    }
}
