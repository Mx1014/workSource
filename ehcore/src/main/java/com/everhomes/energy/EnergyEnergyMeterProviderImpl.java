package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhMetersDao;
import com.everhomes.server.schema.tables.pojos.EhMeters;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

import static com.everhomes.server.schema.Tables.EH_METERS;

/**
 * Created by xq.tian on 2016/10/28.
 */
@Repository
public class EnergyEnergyMeterProviderImpl implements EnergyMeterProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createMeter(Meter meter) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhMeters.class));
        meter.setId(id);
        meter.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meter.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meter.setUpdateUid(UserContext.current().getUser().getId());
        rwDao().insert(meter);
        return id;
    }

    @Override
    public Meter findById(Integer namespaceId, Long meterId) {
        return context().selectFrom(EH_METERS)
                .where(EH_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_METERS.ID.eq(meterId)).fetchOneInto(Meter.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhMetersDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhMetersDao(context.configuration());
    }
}
