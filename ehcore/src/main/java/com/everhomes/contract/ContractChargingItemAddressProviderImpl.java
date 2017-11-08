package com.everhomes.contract;

import com.everhomes.contract.ContractChargingItemAddress;
import com.everhomes.contract.ContractChargingItemAddressProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.energy.EnergyMeterType;
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
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ContractChargingItemAddress> findByAddressId(Long addressId, Byte meterType, Timestamp startDate, Timestamp endDate) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ID,Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.NAMESPACE_ID,
                Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CONTRACT_CHARGING_ITEM_ID, Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ADDRESS_ID,
                Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.STATUS, Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CREATE_UID,
                Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CREATE_TIME);
        query.addFrom(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES);
        query.addJoin(Tables.EH_CONTRACT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_CONTRACT_CHARGING_ITEMS.ID.eq(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CONTRACT_CHARGING_ITEM_ID));
        query.addJoin(Tables.EH_CONTRACTS, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_CONTRACTS.ID.eq(Tables.EH_CONTRACT_CHARGING_ITEMS.CONTRACT_ID));
        query.addConditions(Tables.EH_CONTRACTS.STATUS.eq(ContractStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CONTRACTS.CONTRACT_START_DATE.le(startDate));
        query.addConditions(Tables.EH_CONTRACTS.CONTRACT_END_DATE.ge(endDate));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ADDRESS_ID.eq(addressId));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        if(meterType != null) {
            if(EnergyMeterType.ELECTRIC.equals(EnergyMeterType.fromCode(meterType))) {
                query.addJoin(Tables.EH_PAYMENT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                        Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(Tables.EH_CONTRACT_CHARGING_ITEMS.CHARGING_ITEM_ID));
                query.addConditions(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME.eq("电费"));
            } else if(EnergyMeterType.WATER.equals(EnergyMeterType.fromCode(meterType))) {
                query.addJoin(Tables.EH_PAYMENT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                        Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(Tables.EH_CONTRACT_CHARGING_ITEMS.CHARGING_ITEM_ID));
                query.addConditions(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME.eq("水费"));
            }
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("findByAddressId, sql=" + query.getSQL());
            LOGGER.debug("findByAddressId, bindValues=" + query.getBindValues());
        }
        List<ContractChargingItemAddress> result = new ArrayList<>();
        query.fetch().map((r) -> {
            ContractChargingItemAddress itemAddress = new ContractChargingItemAddress();
            itemAddress.setId(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ID));
            itemAddress.setNamespaceId(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.NAMESPACE_ID));
            itemAddress.setContractChargingItemId(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CONTRACT_CHARGING_ITEM_ID));
            itemAddress.setAddressId(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.ADDRESS_ID));
            itemAddress.setStatus(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.STATUS));
            itemAddress.setCreateTime(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CREATE_TIME));
            itemAddress.setCreateUid(r.getValue(Tables.EH_CONTRACT_CHARGING_ITEM_ADDRESSES.CREATE_UID));
            result.add(itemAddress);
            return null;
        });
        return result;
    }
}
