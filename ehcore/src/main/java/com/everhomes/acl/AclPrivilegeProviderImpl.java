package com.everhomes.acl;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.schema.tables.daos.EhAclPrivilegesDao;
import com.everhomes.schema.tables.pojos.EhAclPrivileges;
import com.everhomes.schema.tables.records.EhAclPrivilegesRecord;
import com.everhomes.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class AclPrivilegeProviderImpl implements AclPrivilegeProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createAclPrivilege(AclPrivilege obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAclPrivileges.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclPrivileges.class));
        obj.setId(id);
        prepareObj(obj);
        EhAclPrivilegesDao dao = new EhAclPrivilegesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateAclPrivilege(AclPrivilege obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclPrivileges.class));
        EhAclPrivilegesDao dao = new EhAclPrivilegesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteAclPrivilege(AclPrivilege obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclPrivileges.class));
        EhAclPrivilegesDao dao = new EhAclPrivilegesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public AclPrivilege getAclPrivilegeById(Long id) {
        try {
        AclPrivilege[] result = new AclPrivilege[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclPrivileges.class));

        result[0] = context.select().from(Tables.EH_ACL_PRIVILEGES)
            .where(Tables.EH_ACL_PRIVILEGES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, AclPrivilege.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<AclPrivilege> queryAclPrivileges(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAclPrivileges.class));

        SelectQuery<EhAclPrivilegesRecord> query = context.selectQuery(Tables.EH_ACL_PRIVILEGES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACL_PRIVILEGES.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<AclPrivilege> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, AclPrivilege.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(AclPrivilege obj) {
    }
}
