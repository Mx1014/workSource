package com.everhomes.contract;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhContractChargingChangeAddressesDao;
import com.everhomes.server.schema.tables.pojos.EhContractChargingChangeAddresses;
import com.everhomes.server.schema.tables.records.EhContractChargingChangeAddressesRecord;
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
 * Created by ying.xiong on 2017/10/10.
 */
@Component
public class ContractChargingChangeAddressProviderImpl implements ContractChargingChangeAddressProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractChargingChangeAddressProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public void createContractChargingChangeAddress(ContractChargingChangeAddress address) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhContractChargingChangeAddresses.class));
        address.setId(id);
        address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        address.setCreateUid(UserContext.current().getUser().getId());

        LOGGER.info("createContractChargingChangeAddress: " + address);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChangeAddresses.class, id));
        EhContractChargingChangeAddressesDao dao = new EhContractChargingChangeAddressesDao(context.configuration());
        dao.insert(address);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhContractChargingChangeAddresses.class, null);

    }

    @Override
    public void updateContractChargingChangeAddress(ContractChargingChangeAddress address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChangeAddresses.class, address.getId()));
        EhContractChargingChangeAddressesDao dao = new EhContractChargingChangeAddressesDao(context.configuration());
        dao.update(address);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingChangeAddresses.class, address.getId());

    }

    @Override
    public void deleteContractChargingChangeAddress(ContractChargingChangeAddress address) {
        assert(address.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhContractChargingChangeAddresses.class, address.getId()));
        EhContractChargingChangeAddressesDao dao = new EhContractChargingChangeAddressesDao(context.configuration());
        dao.delete(address);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhContractChargingChangeAddresses.class, address.getId());

    }

    @Override
    public ContractChargingChangeAddress findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhContractChargingChangeAddressesDao dao = new EhContractChargingChangeAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ContractChargingChangeAddress.class);
    }

    @Override
    public List<ContractChargingChangeAddress> findByChangeId(Long changeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhContractChargingChangeAddressesRecord> query = context.selectQuery(Tables.EH_CONTRACT_CHARGING_CHANGE_ADDRESSES);
        query.addConditions(Tables.EH_CONTRACT_CHARGING_CHANGE_ADDRESSES.CHARGING_CHANGE_ID.eq(changeId));
        query.addConditions(Tables.EH_CONTRACT_CHARGING_CHANGE_ADDRESSES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<ContractChargingChangeAddress> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ContractChargingChangeAddress.class));
            return null;
        });

        return result;
    }
}
