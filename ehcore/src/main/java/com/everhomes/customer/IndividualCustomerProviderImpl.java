package com.everhomes.customer;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.pm.OrganizationOwnerAddress;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationOwnerAddressDao;
import com.everhomes.server.schema.tables.pojos.EhOrganizationOwnerAddress;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnerAddressRecord;
import com.everhomes.server.schema.tables.records.EhOrganizationOwnersRecord;
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
 * Created by ying.xiong on 2017/8/28.
 */
@Component
public class IndividualCustomerProviderImpl implements IndividualCustomerProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndividualCustomerProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public List<OrganizationOwner> listOrganizationOwnerByNamespaceType(Integer namespaceId, String namespaceType, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_CUSTOMER_TYPE.eq(namespaceType));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<OrganizationOwner> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationOwner.class));
            return null;
        });

        return result;
    }

    @Override
    public List<OrganizationOwnerAddress> listOrganizationOwnerAddressByOwnerId(Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnerAddressRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNER_ADDRESS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNER_ADDRESS.ORGANIZATION_OWNER_ID.eq(ownerId));

        List<OrganizationOwnerAddress> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationOwnerAddress.class));
            return null;
        });

        return result;
    }

    @Override
    public void createOrganizationOwnerAddress(OrganizationOwnerAddress address) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOrganizationOwnerAddress.class));
        address.setId(id);

        LOGGER.info("createOrganizationOwnerAddress: " + address);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhOrganizationOwnerAddress.class, id));
        EhOrganizationOwnerAddressDao dao = new EhOrganizationOwnerAddressDao(context.configuration());
        dao.insert(address);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhOrganizationOwnerAddress.class, null);
    }

    @Override
    public void deleteOrganizationOwnerAddress(OrganizationOwnerAddress address) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhOrganizationOwnerAddressDao dao = new EhOrganizationOwnerAddressDao(context.configuration());
        dao.update(address);
    }

    @Override
    public List<OrganizationOwner> listOrganizationOwnerByNamespaceIdAndName(Integer namespaceId, String name) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationOwnersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_OWNERS);
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.CONTACT_NAME.eq(name));
        query.addConditions(Tables.EH_ORGANIZATION_OWNERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<OrganizationOwner> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, OrganizationOwner.class));
            return null;
        });

        return result;
    }
}
