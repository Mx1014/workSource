// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOauth2ServersDao;
import com.everhomes.server.schema.tables.pojos.EhOauth2Servers;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

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

    @Override
    public void updateOauth2Server(OAuth2Server oAuth2Server) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOauth2ServersDao dao = new EhOauth2ServersDao(context.configuration());
        dao.update(oAuth2Server);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOauth2Servers.class, oAuth2Server.getId());
    }

    @Override
    public void createOAuth2Server(OAuth2Server oAuth2Server) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOauth2Servers.class));
        oAuth2Server.setId(id);
        oAuth2Server.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOauth2ServersDao dao = new EhOauth2ServersDao(context.configuration());
        dao.insert(oAuth2Server);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOauth2Servers.class, id);
    }

    @Override
    public OAuth2Server findOAuth2ServerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhOauth2ServersDao dao = new EhOauth2ServersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), OAuth2Server.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
