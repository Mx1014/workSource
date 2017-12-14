// @formatter:off
package com.everhomes.filedownload;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFileDownloadJobsDao;
import com.everhomes.server.schema.tables.pojos.EhFileDownloadJobs;
import com.everhomes.server.schema.tables.records.EhFileDownloadJobsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FileDownloadProviderImpl implements FileDownloadProvider {

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createFileDownloadJob(FileDownloadJob job) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFileDownloadJobs.class));
        job.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhFileDownloadJobsDao dao = new EhFileDownloadJobsDao(context.configuration());
        dao.insert(job);
    }

    @Override
    public void updateFileDownloadJob(FileDownloadJob job) {
        assert(job.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhFileDownloadJobsDao dao = new EhFileDownloadJobsDao(context.configuration());
        dao.update(job);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFileDownloadJobs.class, job.getId());
    }


    @Override
    public FileDownloadJob findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFileDownloadJobs.class, id));
        EhFileDownloadJobsDao dao = new EhFileDownloadJobsDao(context.configuration());
        EhFileDownloadJobs result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, FileDownloadJob.class);
    }

    @Override
    public List<FileDownloadJob> listFileDownloadJobsByOwnerId(Long ownerId, Long pageAnchor, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhFileDownloadJobsRecord> query = context.selectQuery(Tables.EH_FILE_DOWNLOAD_JOBS);
        query.addConditions(Tables.EH_FILE_DOWNLOAD_JOBS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FILE_DOWNLOAD_JOBS.ID.le(pageAnchor));
        query.addOrderBy(Tables.EH_HOT_TAGS.ID.asc());
        query.addLimit(pageSize);

        List<FileDownloadJob> result = new ArrayList<FileDownloadJob>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, FileDownloadJob.class));
            return null;
        });

        return result;
    }
}
