package com.everhomes.decoration;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.decoration.DecorationIllustrationDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhDecorationSetting;
import com.everhomes.server.schema.tables.records.EhDecorationSettingRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

public class DecorationProviderImpl implements  DecorationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createDecorationSetting(EhDecorationSetting setting) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationSetting.class));
        setting.setId(id);
        setting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationSettingRecord record = ConvertHelper.convert(setting,EhDecorationSettingRecord.class);
        InsertQuery<EhDecorationSettingRecord> query = context
                .insertQuery(Tables.EH_DECORATION_SETTING);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationSetting.class, null);
        return null;
    }
}
