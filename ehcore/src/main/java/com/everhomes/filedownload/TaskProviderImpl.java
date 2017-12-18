// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhJobsDao;
import com.everhomes.server.schema.tables.pojos.EhJobs;
import com.everhomes.server.schema.tables.records.EhJobsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JobProviderImpl implements JobProvider {

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createJob(Job job) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhJobs.class));
        job.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhJobsDao dao = new EhJobsDao(context.configuration());
        dao.insert(job);
    }

    @Override
    public void updateJob(Job job) {
        assert(job.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhJobsDao dao = new EhJobsDao(context.configuration());
        dao.update(job);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhJobs.class, job.getId());
    }


    @Override
    public Job findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhJobs.class, id));
        EhJobsDao dao = new EhJobsDao(context.configuration());
        EhJobs result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, Job.class);
    }

    @Override
    public List<Job> listJobs(Integer namespaceId, Long communityId, Long orgId, Long userId, Byte type, Byte status, Long pageAnchor, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhJobsRecord> query = context.selectQuery(Tables.EH_JOBS);

        if(namespaceId != null){
            query.addConditions(Tables.EH_JOBS.NAMESPACE_ID.eq(namespaceId));
        }

        if(communityId != null){
            query.addConditions(Tables.EH_JOBS.COMMUNITY_ID.eq(communityId));
        }

        if(orgId != null){
            query.addConditions(Tables.EH_JOBS.ORG_ID.eq(orgId));
        }

        if(userId != null){
            query.addConditions(Tables.EH_JOBS.USER_ID.eq(userId));
        }

        if(type != null){
            query.addConditions(Tables.EH_JOBS.TYPE.eq(type));
        }

        if(status != null){
            query.addConditions(Tables.EH_JOBS.STATUS.eq(status));
        }

        query.addConditions(Tables.EH_JOBS.ID.le(pageAnchor));
        query.addOrderBy(Tables.EH_JOBS.ID.desc());
        query.addLimit(pageSize);

        List<Job> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Job.class));
            return null;
        });

        return result;
    }
}
