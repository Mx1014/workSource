package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterAddresses;
import com.everhomes.server.schema.tables.records.EhEnergyMeterAddressesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/19.
 */
public class EnergyMeterAddressProviderImpl implements EnergyMeterAddressProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Map<Long, EnergyMeterAddress> findByMeterId(Long meterId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnergyMeterAddressesRecord> query = context.selectQuery(Tables.EH_ENERGY_METER_ADDRESSES);
        query.addConditions(Tables.EH_ENERGY_METER_ADDRESSES.METER_ID.eq(meterId));
        query.addConditions(Tables.EH_ENERGY_METER_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        Map<Long, EnergyMeterAddress> results = new HashMap<>();
        query.fetch().map((r) -> {
            results.put(r.getId(), ConvertHelper.convert(r, EnergyMeterAddress.class));
            return null;
        });

        return results;
    }

    @Override
    public void createEnergyMeterAddress(EnergyMeterAddress address) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterAddresses.class));
        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setCreatorUid(UserContext.current().getUser().getId());
        address.setStatus(CommonStatus.ACTIVE.getCode());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyMeterAddresses.class, id));
        EhEnergyMeterAddressesDao dao = new EhEnergyMeterAddressesDao(context.configuration());
        dao.insert(address);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnergyMeterAddresses.class, null);
    }

    @Override
    public void updateEnergyMeterAddress(EnergyMeterAddress address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnergyMeterAddresses.class, address.getId()));
        EhEnergyMeterAddressesDao dao = new EhEnergyMeterAddressesDao(context.configuration());
        address.setOperatorUid(UserContext.current().getUser().getId());
        address.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(address);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnergyMeterAddresses.class, address.getId());
    }
}
