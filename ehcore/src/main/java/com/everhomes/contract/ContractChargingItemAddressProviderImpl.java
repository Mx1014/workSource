package com.everhomes.contract;

import com.everhomes.contract.ContractChargingItemAddress;
import com.everhomes.contract.ContractChargingItemAddressProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhContractChargingItemAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhContractAttachments;
import com.everhomes.server.schema.tables.pojos.EhContractChargingItemAddresses;
import com.everhomes.server.schema.tables.records.EhContractChargingItemAddressesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/16.
 */
@Component
public class ContractChargingItemAddressProviderImpl implements ContractChargingItemAddressProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractChargingItemAddressProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createContractChargingItemAddress(ContractChargingItemAddress address) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractChargingItemAddresses.class));
        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setCreateUid(UserContext.current().getUser().getId());

        LOGGER.info("createContractChargingItemAddress: " + address);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItemAddresses.class, id));
        EhContractChargingItemAddressesDao dao = new EhContractChargingItemAddressesDao(context.configuration());
        dao.insert(address);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractChargingItemAddresses.class, null);
    }

    @Override
    public void updateContractChargingItemAddress(ContractChargingItemAddress address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItemAddresses.class, address.getId()));
        EhContractChargingItemAddressesDao dao = new EhContractChargingItemAddressesDao(context.configuration());
        dao.update(address);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingItemAddresses.class, address.getId());
    }

    @Override
    public void deleteContractChargingItemAddress(ContractChargingItemAddress address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingItemAddresses.class, address.getId()));
        EhContractChargingItemAddressesDao dao = new EhContractChargingItemAddressesDao(context.configuration());
        dao.delete(address);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingItemAddresses.class, address.getId());
    }

    @Override
    public ContractChargingItemAddress findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractChargingItemAddressesDao dao = new EhContractChargingItemAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractChargingItemAddress.class);
    }

    @Override
    public List<ContractChargingItemAddress> findByItemId(Long itemId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractChargingItemAddressesRecord> query = context.selectQuery(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES);
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CONTRACT_CHARGING_ITEM_ID.eq(itemId));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<ContractChargingItemAddress> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractChargingItemAddress.class));
            return null;
        });

        return result;
    }
}
