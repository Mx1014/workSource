package com.everhomes.gogs;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGogsReposDao;
import com.everhomes.server.schema.tables.pojos.EhGogsRepos;
import com.everhomes.server.schema.tables.records.EhGogsReposRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GogsRepoProviderImpl implements GogsRepoProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createRepo(GogsRepo repo) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGogsRepos.class));
        repo.setId(id);
        repo.setCreateTime(DateUtils.currentTimestamp());
        repo.setCreatorUid(UserContext.currentUserId());

        EhGogsReposDao dao = new EhGogsReposDao(dbProvider.getDslContext(AccessSpec.readWrite()).configuration());
        dao.insert(repo);
    }

    @Override
    public List<GogsRepo> listRepos(int pageSize, ListingLocator locator, ListingQueryBuilderCallback callback) {
        return this.query(locator, pageSize, callback);
    }

    @Override
    public GogsRepo findById(Long repoId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhGogsReposDao dao = new EhGogsReposDao(context.configuration());
        return ConvertHelper.convert(dao.findById(repoId), GogsRepo.class);
    }

    @Override
    public GogsRepo getAnyRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId) {
        List<GogsRepo> list = this.query(new ListingLocator(), 1, (locator, query) -> {
            com.everhomes.server.schema.tables.EhGogsRepos t = Tables.EH_GOGS_REPOS;
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(t.MODULE_TYPE.eq(moduleType));
            query.addConditions(t.MODULE_ID.eq(moduleId));
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
            return query;
        });
        if (list.size() > 0) {
            return list.iterator().next();
        }
        return null;
    }

    private List<GogsRepo> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhGogsRepos t = Tables.EH_GOGS_REPOS;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhGogsReposRecord> query = context.selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<GogsRepo> list = query.fetchInto(GogsRepo.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }
}
