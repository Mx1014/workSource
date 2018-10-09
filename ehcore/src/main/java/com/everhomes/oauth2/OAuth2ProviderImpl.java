package com.everhomes.oauth2;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOauth2CodesDao;
import com.everhomes.server.schema.tables.daos.EhOauth2TokensDao;
import com.everhomes.server.schema.tables.pojos.EhOauth2Codes;
import com.everhomes.server.schema.tables.pojos.EhOauth2Tokens;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IdToken;
import com.everhomes.util.WebTokenGenerator;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OAuth2ProviderImpl implements OAuth2Provider {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createAuthorizationCode(AuthorizationCode code) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOauth2Codes.class));
        code.setId(id);
        code.setCode(WebTokenGenerator.getInstance().toWebToken(new IdToken(id)));

        EhOauth2CodesDao dao = new EhOauth2CodesDao(context.configuration());
        dao.insert(code);
    }

    @Caching(evict = {@CacheEvict(value="OAuth2Code-Id", key="#code.id"),
        @CacheEvict(value="OAuth2Code-Code", key="#code.code")
    })
    @Override
    public void updateAuthorizationCode(AuthorizationCode code) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhOauth2CodesDao dao = new EhOauth2CodesDao(context.configuration());
        dao.update(code);
    }

    @Caching(evict = {@CacheEvict(value="OAuth2Code-Id", key="#code.id"),
        @CacheEvict(value="OAuth2Code-Code", key="#code.code")
    })
    @Override
    public void deleteAuthorizationCode(AuthorizationCode code) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhOauth2CodesDao dao = new EhOauth2CodesDao(context.configuration());
        dao.delete(code);
    }

    @Override
    public void deleteAuthorizationCodeById(long id) {
        OAuth2Provider self = PlatformContext.getComponent(OAuth2Provider.class);

        AuthorizationCode code = self.findAuthorizationCodeById(id);
        if(code != null)
            self.deleteAuthorizationCode(code);
    }

    @Cacheable(value="OAuth2Code-Id", key="#id", unless="#result == null")
    @Override
    public AuthorizationCode findAuthorizationCodeById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AuthorizationCode> codes = context.select().from(Tables.EH_OAUTH2_CODES)
            .where(Tables.EH_OAUTH2_CODES.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, AuthorizationCode.class);
            });
        if(codes.size() > 0)
            return codes.get(0);
        return null;
    }

    @Cacheable(value="OAuth2Code-Code", key="#code", unless="#result == null")
    @Override
    public AuthorizationCode findAuthorizationCodeByCode(String code) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AuthorizationCode> codes = context.select().from(Tables.EH_OAUTH2_CODES)
            .where(Tables.EH_OAUTH2_CODES.CODE.eq(code))
            .fetch().map((record) -> {
                    return ConvertHelper.convert(record, AuthorizationCode.class);
                });
        if(codes.size() > 0)
            return codes.get(0);
        return null;
    }

    @Override
    public void createAccessToken(AccessToken token) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOauth2Tokens.class));
        token.setId(id);
        token.setTokenString(WebTokenGenerator.getInstance().toWebToken(new IdToken(id)));

        EhOauth2TokensDao dao = new EhOauth2TokensDao(context.configuration());
        dao.insert(token);
    }

    @Caching(evict = {@CacheEvict(value="OAuth2Token-Id", key="#token.id"),
        @CacheEvict(value="OAuth2Token-Code", key="#token.tokenString")
    })
    @Override
    public void updateAccessToken(AccessToken token) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhOauth2TokensDao dao = new EhOauth2TokensDao(context.configuration());
        dao.update(token);
    }

    @Caching(evict = {@CacheEvict(value="OAuth2Token-Id", key="#token.id"),
        @CacheEvict(value="OAuth2Token-Code", key="#token.tokenString")
    })
    @Override
    public void deleteAccessToken(AccessToken token) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        EhOauth2TokensDao dao = new EhOauth2TokensDao(context.configuration());
        dao.delete(token);
    }

    @Override
    public void deleteAccessTokenById(long id) {
        OAuth2Provider self = PlatformContext.getComponent(OAuth2Provider.class);
        AccessToken token = self.findAccessTokenById(id);

        if(token != null)
            self.deleteAccessToken(token);
    }

    @Cacheable(value="OAuth2Token-Id", key="#id", unless="#result == null")
    @Override
    public AccessToken findAccessTokenById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AccessToken> tokens = context.select().from(Tables.EH_OAUTH2_TOKENS)
            .where(Tables.EH_OAUTH2_TOKENS.ID.eq(id))
            .fetch().map((record)-> {
                return ConvertHelper.convert(record, AccessToken.class);
            });
        if(tokens.size() > 0)
            return tokens.get(0);
        return null;
    }

    @Cacheable(value="OAuth2Token-Code", key="#tokenString", unless="#result == null")
    @Override
    public AccessToken findAccessTokenByTokenString(String tokenString) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<AccessToken> tokens = context.select().from(Tables.EH_OAUTH2_TOKENS)
            .where(Tables.EH_OAUTH2_TOKENS.TOKEN_STRING.eq(tokenString))
            .fetch().map((record) -> {
                    return ConvertHelper.convert(record, AccessToken.class);
                });
        if(tokens.size() > 0)
            return tokens.get(0);
        return null;
    }

    @Override
    public AccessToken findAccessTokenByAppAndGrantorUid(Long appId, Long grantorUid) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhOauth2Tokens t = Tables.EH_OAUTH2_TOKENS;

        return context.select().from(t)
                .where(t.APP_ID.eq(appId))
                .and(t.GRANTOR_UID.eq(grantorUid))
                .orderBy(t.ID.desc())
                .fetchAnyInto(AccessToken.class);
    }
}
