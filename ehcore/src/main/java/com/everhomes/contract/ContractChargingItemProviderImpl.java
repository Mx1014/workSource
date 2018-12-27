package com.everhomes.contract;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractChargingItemsDao;
import com.everhomes.server.schema.tables.pojos.EhContractChargingItems;
import com.everhomes.server.schema.tables.records.EhContractChargingItemsRecord;
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
 * Created by ying.xiong on 2017/8/16.
 */
@Component
public class ContractChargingItemProviderImpl implements ContractChargingItemProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractChargingItemProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createContractChargingItem(ContractChargingItem item) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractChargingItems.class));
        item.setId(id);
        item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        item.setCreateUid(UserContext.current().getUser().getId());

        LOGGER.info("createContractChargingItem: " + item);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItems.class));
        EhContractChargingItemsDao dao = new EhContractChargingItemsDao(context.configuration());
        dao.insert(item);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractChargingItems.class, id);
    }

    @Caching(evict = { @CacheEvict(value="listByContract", key="#item.contractId")} )
    @Override
    public void updateContractChargingItem(ContractChargingItem item) {
        assert(item.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItems.class));
        EhContractChargingItemsDao dao = new EhContractChargingItemsDao(context.configuration());
        item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        item.setOperatorUid(UserContext.current().getUser().getId());
        dao.update(item);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingItems.class, item.getId());
    }

    @Caching(evict = { @CacheEvict(value="listByContract", key="#item.contractId")} )
    @Override
    public void deleteContractChargingItem(ContractChargingItem item) {
        assert(item.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItems.class, item.getId()));
        EhContractChargingItemsDao dao = new EhContractChargingItemsDao(context.configuration());
        dao.delete(item);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingItems.class, item.getId());
    }

    @Cacheable(value = "listByContractId", key="#contractId", unless="#result.size() == 0")
    @Override
    public List<ContractChargingItem> listByContractId(Long contractId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractChargingItemsRecord> query = context.selectQuery(Tables.EH_CONTRACT_CHARGING_ITEMS);
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEMS.CONTRACT_ID.eq(contractId));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<ContractChargingItem> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractChargingItem.class));
            return null;
        });

        return result;
    }

    @Override
    public ContractChargingItem findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractChargingItemsDao dao = new EhContractChargingItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractChargingItem.class);
    }
}
