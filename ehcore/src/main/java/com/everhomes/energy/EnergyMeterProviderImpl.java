package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMetersDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METERS;

/**
 * Created by xq.tian on 2016/10/28.
 */
@Repository
public class EnergyMeterProviderImpl implements EnergyMeterProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createMeter(EnergyMeter meter) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeters.class));
        meter.setId(id);
        meter.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meter.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meter.setUpdateUid(UserContext.current().getUser().getId());
        rwDao().insert(meter);
        return id;
    }

    @Override
    public EnergyMeter findById(Integer namespaceId, Long meterId) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METERS.ID.eq(meterId)).fetchOneInto(EnergyMeter.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMetersDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMetersDao(context.configuration());
    }
}
