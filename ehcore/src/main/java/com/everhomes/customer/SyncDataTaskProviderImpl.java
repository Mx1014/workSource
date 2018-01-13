package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhSyncDataTasksDao;
import com.everhomes.server.schema.tables.pojos.EhSyncDataTasks;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public class SyncDataTaskProviderImpl implements SyncDataTaskProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createSyncDataTask(SyncDataTask task) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSyncDataTasks.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        task.setId(id);
        task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(task);
    }

    @Override
    public void updateSyncDataTask(SyncDataTask task) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSyncDataTasks.class));
        EhSyncDataTasksDao dao = new EhSyncDataTasksDao(context.configuration());
        task.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update();

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSyncDataTasks.class, task.getId());
    }
}
