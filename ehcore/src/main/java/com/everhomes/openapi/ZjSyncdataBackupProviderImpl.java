package com.everhomes.openapi;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhZjSyncdataBackupDao;
import com.everhomes.server.schema.tables.pojos.EhZjSyncdataBackup;
import com.everhomes.server.schema.tables.records.EhZjSyncdataBackupRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/8.
 */
@Component
public class ZjSyncdataBackupProviderImpl implements ZjSyncdataBackupProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createZjSyncdataBackup(ZjSyncdataBackup backup) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhZjSyncdataBackup.class));
        backup.setId(id);

        EhZjSyncdataBackupDao dao = new EhZjSyncdataBackupDao(context.configuration());
        dao.insert(backup);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhZjSyncdataBackup.class, null);
    }

    @Override
    public void updateZjSyncdataBackup(ZjSyncdataBackup backup) {
        assert (backup.getId() != null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhZjSyncdataBackup.class, backup.getId()));
        EhZjSyncdataBackupDao dao = new EhZjSyncdataBackupDao(context.configuration());
        dao.update(backup);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhZjSyncdataBackup.class, backup.getId());
    }

    @Override
    public List<ZjSyncdataBackup> listZjSyncdataBackupByParam(Integer namespaceId, Byte dataType) {
        List<ZjSyncdataBackup> backups = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhZjSyncdataBackup.class));

        SelectQuery<EhZjSyncdataBackupRecord> query = context.selectQuery(Tables.EH_ZJ_SYNCDATA_BACKUP);

        query.addConditions(Tables.EH_ZJ_SYNCDATA_BACKUP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ZJ_SYNCDATA_BACKUP.DATA_TYPE.eq(dataType));
        query.addConditions(Tables.EH_ZJ_SYNCDATA_BACKUP.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        query.fetch().map((r) -> {
            backups.add(ConvertHelper.convert(r, ZjSyncdataBackup.class));
            return null;
        });
        return backups;
    }

    @Override
    public void updateZjSyncdataBackupInactive(List<ZjSyncdataBackup> backupList) {
        backupList.forEach(b->{
            b.setStatus(CommonStatus.INACTIVE.getCode());
            b.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            updateZjSyncdataBackup(b);
        });
    }
}
