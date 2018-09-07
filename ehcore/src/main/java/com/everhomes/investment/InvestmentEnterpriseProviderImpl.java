package com.everhomes.investment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.investment.InvestmentEnterpriseStatus;
import com.everhomes.rest.investment.InvestmentStatisticsDTO;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.EhCustomerContactsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerCurrentRentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerRequirementsDao;
import com.everhomes.server.schema.tables.pojos.EhCustomerContacts;
import com.everhomes.server.schema.tables.pojos.EhCustomerCurrentRents;
import com.everhomes.server.schema.tables.pojos.EhCustomerRequirements;
import com.everhomes.server.schema.tables.records.EhCustomerContactsRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InvestmentEnterpriseProviderImpl implements InvestmentEnterpriseProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentEnterpriseProviderImpl.class);

    @Autowired
    SequenceProvider sequenceProvider;
    @Autowired
    DbProvider dbProvider;

    @Override
    public Long createContact(CustomerContact contact) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerContacts.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerContacts.class));
        contact.setId(id);

        User user = UserContext.current().getUser();
        contact.setOperatorUid(user.getId());
        contact.setCreatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        contact.setCreateTime(new Timestamp(l2));
        contact.setOperatorTime(new Timestamp(l2));

        EhCustomerContactsDao dao = new EhCustomerContactsDao(context.configuration());
        dao.insert(contact);
        return id;
    }

    @Override
    public Long updateContact(CustomerContact contact) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerContacts.class));

        User user = UserContext.current().getUser();
        contact.setOperatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        contact.setOperatorTime(new Timestamp(l2));

        EhCustomerContactsDao dao = new EhCustomerContactsDao(context.configuration());
        dao.update(contact);
        return contact.getId();
    }

    @Override
    public CustomerContact findContactById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerContacts.class));

        EhCustomerContactsDao dao = new EhCustomerContactsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerContact.class);
    }

    @Override
    public List<CustomerContact> findContactByCustomerId(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCustomerContacts.class));

        SelectQuery<EhCustomerContactsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_CONTACTS);
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerContact.class));
    }

    @Override
    public List<CustomerContact> findContactByCustomerIdAndType(Long customerId, Byte contactType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCustomerContacts.class));

        SelectQuery<EhCustomerContactsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_CONTACTS);
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.CONTACT_TYPE.eq(contactType));
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerContact.class));
    }

    @Override
    public Long createTracker(CustomerTracker contact) {
        return null;
    }

    @Override
    public Long updateTracker(CustomerTracker contact) {
        return null;
    }

    @Override
    public CustomerTracker findTrackerById(Long id) {
        return null;
    }

    @Override
    public List<CustomerTracker> findTrackerByCustomerId(Long customerId) {
        return null;
    }

    @Override
    public List<CustomerTracker> findTrackerByCustomerIdAndType(Long customerId, Byte type) {
        return null;
    }

    @Override
    public Long createRequirement(CustomerRequirement requirement) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerRequirements.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirements.class));
        requirement.setId(id);

        User user = UserContext.current().getUser();
        requirement.setOperatorUid(user.getId());
        requirement.setCreatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        requirement.setCreateTime(new Timestamp(l2));
        requirement.setOperatorTime(new Timestamp(l2));

        EhCustomerRequirementsDao dao = new EhCustomerRequirementsDao(context.configuration());
        dao.insert(requirement);
        return id;
    }

    @Override
    public Long updateRequirement(CustomerRequirement requirement) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirements.class));
        User user = UserContext.current().getUser();
        requirement.setOperatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        requirement.setOperatorTime(new Timestamp(l2));

        EhCustomerRequirementsDao dao = new EhCustomerRequirementsDao(context.configuration());
        dao.update(requirement);
        return requirement.getId();
    }

    @Override
    public CustomerRequirement findRequirementById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirements.class));

        EhCustomerRequirementsDao dao = new EhCustomerRequirementsDao(context.configuration());

        return ConvertHelper.convert(dao.findById(id), CustomerRequirement.class);
    }

    @Override
    public CustomerRequirement findNewestRequirementByCustoemrId(Long customerId) {
        try {
            CustomerRequirement[] result = new CustomerRequirement[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerRequirements.class));
            result[0] = context.select().from(Tables.EH_CUSTOMER_REQUIREMENTS)
                    .where(Tables.EH_CUSTOMER_REQUIREMENTS.CUSTOMER_ID.eq(customerId))
                    .and(Tables.EH_CUSTOMER_REQUIREMENTS.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_CUSTOMER_REQUIREMENTS.VERSION.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, CustomerRequirement.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Long createCurrentRent(CustomerCurrentRent currentRent) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerCurrentRents.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerCurrentRents.class));
        currentRent.setId(id);

        User user = UserContext.current().getUser();
        currentRent.setOperatorUid(user.getId());
        currentRent.setCreatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        currentRent.setCreateTime(new Timestamp(l2));
        currentRent.setOperatorTime(new Timestamp(l2));

        EhCustomerCurrentRentsDao dao = new EhCustomerCurrentRentsDao(context.configuration());
        dao.insert(currentRent);
        return id;
    }

    @Override
    public Long updateCurrentRent(CustomerCurrentRent currentRent) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerCurrentRents.class));

        User user = UserContext.current().getUser();
        currentRent.setOperatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        currentRent.setOperatorTime(new Timestamp(l2));

        EhCustomerCurrentRentsDao dao = new EhCustomerCurrentRentsDao(context.configuration());
        dao.update(currentRent);
        return currentRent.getId();
    }

    @Override
    public CustomerCurrentRent findCurrentRentById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerCurrentRents.class));


        EhCustomerCurrentRentsDao dao = new EhCustomerCurrentRentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerCurrentRent.class);
    }

    @Override
    public CustomerCurrentRent findNewestCurrentRentByCustomerId(Long customerId) {
        try {
            CustomerCurrentRent[] result = new CustomerCurrentRent[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCustomerCurrentRents.class));
            result[0] = context.select().from(Tables.EH_CUSTOMER_CURRENT_RENTS)
                    .where(Tables.EH_CUSTOMER_CURRENT_RENTS.CUSTOMER_ID.eq(customerId))
                    .and(Tables.EH_CUSTOMER_CURRENT_RENTS.STATUS.ne(InvestmentEnterpriseStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_CUSTOMER_CURRENT_RENTS.ID.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, CustomerCurrentRent.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<InvestmentStatisticsDTO> getInvestmentStatistics(Integer namespaceId, Long communityId, Set<Long> itemIds,Map<Long, FieldItemDTO> itemsMap) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        Field<?>[] fieldArray = new Field[itemIds.size()];
        List<Field<?>> fields = new ArrayList<>();
        itemIds.forEach((itemId) -> {
            // dynamic build stastics decode
            Field<Long> staTmp = DSL.decode().when(customer.LEVEL_ITEM_ID.eq(itemId), itemId);
            fields.add(DSL.count(staTmp).as(itemId.toString()));
        });
        fields.toArray(fieldArray);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fieldArray);
        query.addConditions(customer.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(customer.COMMUNITY_ID.eq(communityId));
        query.addConditions(customer.NAMESPACE_ID.eq(namespaceId));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("count investment enterprise, sql=" + query.getSQL());
            LOGGER.debug("count  investment  enterprise , bindValues=" + query.getBindValues());
        }
        List<InvestmentStatisticsDTO> result = new ArrayList<>();
        query.fetch().map((r) -> {
            for (Long itemId : itemIds) {
                InvestmentStatisticsDTO dto = new InvestmentStatisticsDTO();
//                dto.setKey(itemId.toString());
                dto.setKey(itemsMap.get(itemId).getItemDisplayName());
                dto.setValue(r.getValue(itemId.toString(), Long.class).toString());
                result.add(dto);
            }
            return null;
        });
        return result;
    }

    @Override
    public void deleteInvestment(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        context.update(customer)
                .set(customer.STATUS, CommonStatus.INACTIVE.getCode())
                .where(customer.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteInvestmentContacts(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhCustomerContacts contact = Tables.EH_CUSTOMER_CONTACTS;
        context.update(contact)
                .set(contact.STATUS, CommonStatus.INACTIVE.getCode())
                .where(contact.CUSTOMER_ID.eq(id))
                .execute();
    }
}
