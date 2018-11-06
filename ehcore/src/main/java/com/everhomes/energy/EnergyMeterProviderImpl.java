package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyAutoReadingFlag;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhEnergyMetersDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeters;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
    public long createEnergyMeter(EnergyMeter meter) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeters.class));
        meter.setId(id);
        meter.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        meter.setCreatorUid(UserContext.current().getUser().getId());
        rwDao().insert(meter);
        return id;
    }

    @Override
    public EnergyMeter findById(Integer namespaceId, Long meterId) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METERS.ID.eq(meterId)).fetchOneInto(EnergyMeter.class);
    }

    @Override
    public void updateEnergyMeter(EnergyMeter meter) {
        meter.setUpdateUid(UserContext.current().getUser().getId());
        meter.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().update(meter);
    }

    @Override
    public List<EnergyMeter> listByIds(Integer namespaceId, List<Long> ids) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METERS.ID.in(ids))
                .orderBy(EH_ENERGY_METERS.STATUS.asc(), EH_ENERGY_METERS.CREATE_TIME.desc())
                .fetchInto(EnergyMeter.class);
    }

    @Override
    public List<EnergyMeter> listEnergyMeters(Long pageAnchor, Integer pageSize) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.ID.ge(pageAnchor))
                .and(EH_ENERGY_METERS.STATUS.eq(EnergyMeterStatus.ACTIVE.getCode()))
                .orderBy(EH_ENERGY_METERS.ID)
                .limit(pageSize).fetchInto(EnergyMeter.class);
    }

    @Override
    public List<EnergyMeter> listEnergyMeters() {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.STATUS.eq(EnergyMeterStatus.ACTIVE.getCode()))
                .fetchInto(EnergyMeter.class);
    }

    @Override
    public EnergyMeter findAnyByCategoryId(Integer namespaceId, Long categoryId) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METERS.STATUS.ne(EnergyMeterStatus.INACTIVE.getCode()))
                .and(EH_ENERGY_METERS.BILL_CATEGORY_ID.eq(categoryId).or(EH_ENERGY_METERS.SERVICE_CATEGORY_ID.eq(categoryId)))
                .fetchAnyInto(EnergyMeter.class);
    }

    @Override
    public EnergyMeter findAnyByCategoryId(Integer namespaceId, Long communityId, Long categoryId) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METERS.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METERS.STATUS.ne(EnergyMeterStatus.INACTIVE.getCode()))
                .and(EH_ENERGY_METERS.BILL_CATEGORY_ID.eq(categoryId).or(EH_ENERGY_METERS.SERVICE_CATEGORY_ID.eq(categoryId)))
                .fetchAnyInto(EnergyMeter.class);
    }

    @Override
    public EnergyMeter findByName(Long communityId, String name) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METERS.STATUS.ne(EnergyMeterStatus.INACTIVE.getCode()))
                .and(EH_ENERGY_METERS.NAME.eq(name))
                .fetchAnyInto(EnergyMeter.class);
    }

    @Override
    public EnergyMeter findByNumber(Long communityId, String number) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METERS.STATUS.ne(EnergyMeterStatus.INACTIVE.getCode()))
                .and(EH_ENERGY_METERS.METER_NUMBER.eq(number))
                .fetchAnyInto(EnergyMeter.class);
    }

    @Override
    public List<EnergyMeter> listAutoReadingMeters() {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.AUTO_FLAG.eq(EnergyAutoReadingFlag.TURE.getCode()))
                .and(EH_ENERGY_METERS.STATUS.eq(EnergyMeterStatus.ACTIVE.getCode()))
                .fetchInto(EnergyMeter.class);
    }

    @Override
    public List<EnergyMeter> listAutoReadingMetersByCommunityId(Long communityId) {
        return context().selectFrom(EH_ENERGY_METERS)
                .where(EH_ENERGY_METERS.AUTO_FLAG.eq(EnergyAutoReadingFlag.TURE.getCode()))
                .and(EH_ENERGY_METERS.STATUS.eq(EnergyMeterStatus.ACTIVE.getCode()))
                .and(EH_ENERGY_METERS.COMMUNITY_ID.eq(communityId))
                .fetchInto(EnergyMeter.class);
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
