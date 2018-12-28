package com.everhomes.contract;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractChargingChangesDao;
import com.everhomes.server.schema.tables.pojos.EhContractChargingChanges;
import com.everhomes.server.schema.tables.records.EhContractChargingChangesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/10/10.
 */
@Component
public class ContractChargingChangeProviderImpl implements ContractChargingChangeProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractChargingChangeProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createContractChargingChange(ContractChargingChange change) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractChargingChanges.class));
        change.setId(id);
        change.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        change.setCreateUid(UserContext.current().getUser().getId());

        LOGGER.info("createContractChargingChange: " + change);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChanges.class, id));
        EhContractChargingChangesDao dao = new EhContractChargingChangesDao(context.configuration());
        dao.insert(change);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractChargingChanges.class, null);
    }

    @Caching(evict = { @CacheEvict(value="listByContract", key="#change.contractId")} )
    @Override
    public void updateContractChargingChange(ContractChargingChange change) {
        assert(change.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChanges.class, change.getId()));
        EhContractChargingChangesDao dao = new EhContractChargingChangesDao(context.configuration());
        change.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        change.setOperatorUid(UserContext.current().getUser().getId());
        dao.update(change);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingChanges.class, change.getId());
    }

    @Caching(evict = { @CacheEvict(value="listByContract", key="#change.contractId")} )
    @Override
    public void deleteContractChargingChange(ContractChargingChange change) {
        assert(change.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChanges.class, change.getId()));
        EhContractChargingChangesDao dao = new EhContractChargingChangesDao(context.configuration());
        dao.delete(change);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingChanges.class, change.getId());
    }

    @Cacheable(value = "listByContractId", key="#contractId", unless="#result.size() == 0")
    @Override
    public List<ContractChargingChange> listByContractId(Long contractId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractChargingChangesRecord> query = context.selectQuery(Tables.EH_CONTRACT_CHARGING_CHANGES);
        query.addConditions(Tables.EH_CONTRACT_CHARGING_CHANGES.CONTRACT_ID.eq(contractId));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_CHANGES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<ContractChargingChange> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractChargingChange.class));
            return null;
        });

        return result;
    }

    @Override
    public ContractChargingChange findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractChargingChangesDao dao = new EhContractChargingChangesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractChargingChange.class);
    }
}
