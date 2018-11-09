package com.everhomes.investment;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.InvitedCustomerStatisticsDTO;
import com.everhomes.rest.investment.InvitedCustomerStatus;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhCustomerTrackers;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.EhCustomerContactsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerCurrentRentsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerRequirementAddressesDao;
import com.everhomes.server.schema.tables.daos.EhCustomerRequirementsDao;
import com.everhomes.server.schema.tables.daos.EhCustomerTrackersDao;
import com.everhomes.server.schema.tables.pojos.EhCustomerContacts;
import com.everhomes.server.schema.tables.pojos.EhCustomerCurrentRents;
import com.everhomes.server.schema.tables.pojos.EhCustomerRequirementAddresses;
import com.everhomes.server.schema.tables.pojos.EhCustomerRequirements;
import com.everhomes.server.schema.tables.records.EhCustomerContactsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerRequirementAddressesRecord;
import com.everhomes.server.schema.tables.records.EhCustomerRequirementsRecord;
import com.everhomes.server.schema.tables.records.EhCustomerTrackersRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InvitedCustomerProviderImpl implements InvitedCustomerProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitedCustomerProviderImpl.class);

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
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerContact.class));
    }

    @Override
    public List<CustomerContact> findContactByCustomerIdAndType(Long customerId, Byte contactType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCustomerContacts.class));

        SelectQuery<EhCustomerContactsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_CONTACTS);
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.CONTACT_TYPE.eq(contactType));
        query.addConditions(Tables.EH_CUSTOMER_CONTACTS.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerContact.class));
    }

    @Override
    public Long createTracker(CustomerTracker tracker) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerTrackers.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerTrackers.class));
        tracker.setId(id);

        User user = UserContext.current().getUser();
        tracker.setOperatorUid(user.getId());
        tracker.setCreatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        tracker.setOperatorTime(new Timestamp(l2));
        tracker.setCreateTime(new Timestamp(l2));
        EhCustomerTrackersDao dao = new EhCustomerTrackersDao(context.configuration());
        dao.insert(tracker);
        return id;
    }

    @Override
    public Long updateTracker(CustomerTracker tracker) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerTrackers.class));

        User user = UserContext.current().getUser();
        tracker.setOperatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        tracker.setOperatorTime(new Timestamp(l2));

        EhCustomerTrackersDao dao = new EhCustomerTrackersDao(context.configuration());
        dao.update(tracker);
        return tracker.getId();
    }

    @Override
    public CustomerTracker findTrackerById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerTrackers.class));

        EhCustomerTrackersDao dao = new EhCustomerTrackersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerTracker.class);    }

    @Override
    public List<CustomerTracker> findTrackerByCustomerId(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCustomerTrackers.class));

        SelectQuery<EhCustomerTrackersRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKERS);
        query.addConditions(Tables.EH_CUSTOMER_TRACKERS.CUSTOMER_ID.eq(customerId));
        query.addConditions(Tables.EH_CUSTOMER_TRACKERS.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerTracker.class));
    }


    @Override
    public Long createRequirement(CustomerRequirement requirement) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerRequirements.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirements.class));
        requirement.setId(id);
        requirement.setVersion(id);

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
                    .and(Tables.EH_CUSTOMER_REQUIREMENTS.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_CUSTOMER_REQUIREMENTS.VERSION.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, CustomerRequirement.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            LOGGER.debug("This Customer don't have NewestRequirement");

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
        currentRent.setVersion(id);

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
                    .and(Tables.EH_CUSTOMER_CURRENT_RENTS.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()))
                    .orderBy(Tables.EH_CUSTOMER_CURRENT_RENTS.ID.desc()).fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, CustomerCurrentRent.class);
                    });
            return result[0];
        } catch (Exception ex) {
            // fetchAny() maybe return null
            LOGGER.info("This Customer don't have NewestCurrentRent");

            return null;
        }
    }

    @Override
    public List<InvitedCustomerStatisticsDTO> getInvitedCustomerStatistics(Boolean isAdmin, String keyWord, BigDecimal startAreaSize,BigDecimal endAreaSize,Set<Long> itemIds, Map<Long, FieldItemDTO> itemsMap, ListingQueryBuilderCallback callback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        List<Long> customerIds = getRelatedStatistics(startAreaSize,endAreaSize,isAdmin);
        List<Long> trackerIds = getPrivilegeControlData(isAdmin);
        List<Long> keyIds = getKeyWordCustomerIds(keyWord);
        Field<?>[] fieldArray = new Field[itemIds.size()+1];
        List<Field<?>> fields = new ArrayList<>();
        itemIds.forEach((itemId) -> {
            // dynamic build stastics decode
            Field<Long> staTmp = DSL.decode().when(customer.LEVEL_ITEM_ID.eq(itemId), itemId);
            fields.add(DSL.count(staTmp).as(itemId.toString()));
        });
        fields.add(DSL.count().as("totalCount"));
        fields.toArray(fieldArray);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(fieldArray);
        query.addConditions(customer.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        // related record searcher
        if (startAreaSize != null || endAreaSize != null) {
            query.addConditions(customer.ID.in(customerIds));
        }
        if (!isAdmin) {
            query.addConditions(customer.ID.in(trackerIds));
        }
        if(StringUtils.isNotBlank(keyWord)){
            query.addConditions(customer.ID.in(keyIds));
        }
        query.addFrom(customer);
        callback.buildCondition(null, query);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("count investment enterprise, sql=" + query.getSQL());
            LOGGER.debug("count investment  enterprise , bindValues=" + query.getBindValues());
        }
        List<InvitedCustomerStatisticsDTO> result = new ArrayList<>();
        query.fetch().map((r) -> {
            for (Long itemId : itemIds) {
                InvitedCustomerStatisticsDTO dto = new InvitedCustomerStatisticsDTO();
                dto.setKey(itemsMap.get(itemId).getItemDisplayName());
                dto.setValue(r.getValue(itemId.toString(), Long.class).toString());
                dto.setItemId(itemId);
                result.add(dto);
            }
            InvitedCustomerStatisticsDTO dto = new InvitedCustomerStatisticsDTO();
            dto.setKey("totalCount");
            dto.setValue(r.getValue("totalCount", Long.class).toString());
            result.add(dto);
            return null;
        });
        return result;
    }

    private List<Long> getKeyWordCustomerIds(String keyWord) {
        List<Long> keyWordIds = new ArrayList<>();
        keyWord = StringUtils.trim(keyWord);
        if (StringUtils.isNotBlank(keyWord)) {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
            com.everhomes.server.schema.tables.EhCustomerTrackers trackers = Tables.EH_CUSTOMER_TRACKERS;
            com.everhomes.server.schema.tables.EhEnterpriseCustomers customers = Tables.EH_ENTERPRISE_CUSTOMERS;
            com.everhomes.server.schema.tables.EhCustomerContacts contacts = Tables.EH_CUSTOMER_CONTACTS;
            com.everhomes.server.schema.tables.EhOrganizationMembers members = Tables.EH_ORGANIZATION_MEMBERS;
            List<Long> memberIds = context.select(members.TARGET_ID)
                    .from(members)
                    .where(members.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                    .and(members.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                    .and(members.CONTACT_NAME.like("%" + keyWord + "%"))
                    .fetchInto(Long.class);
            if(CollectionUtils.isEmpty(memberIds)){
                return new ArrayList<>();
            }
            keyWordIds = context.select(trackers.CUSTOMER_ID)
                    .from(trackers)
                    .where(trackers.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                    .and(trackers.TRACKER_UID.in(memberIds))
                    .union(context.select(contacts.CUSTOMER_ID)
                            .from(contacts)
                            .where(contacts.STATUS.eq(CommonStatus.ACTIVE.getCode()))
                            .and(contacts.NAME.like("%" + keyWord + "%"))
                    ).union(context.select(customers.ID)
                            .from(customers)
                            .where(customers.STATUS.eq(CommonStatus.ACTIVE.getCode())
                                    .and(customers.TRACKING_UID.in(memberIds))))
                    .fetchInto(Long.class);
        }
        return keyWordIds;
    }

    private List<Long> getPrivilegeControlData(Boolean isAdmin) {
        List<Long> trackers = new ArrayList<>();
        if (!isAdmin) {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
            com.everhomes.server.schema.tables.EhCustomerTrackers tracker = Tables.EH_CUSTOMER_TRACKERS;
            SelectQuery<EhCustomerTrackersRecord> query = context.selectQuery(tracker);
            query.addSelect(tracker.CUSTOMER_ID);
            query.addConditions(tracker.STATUS.eq(CommonStatus.ACTIVE.getCode()));
            query.addConditions(tracker.TRACKER_UID.eq(UserContext.currentUserId()));
            trackers = query.fetchInto(Long.class);
        }
        return trackers;
    }

    private List<Long> getRelatedStatistics(BigDecimal startAreaSize, BigDecimal endAreaSize, Boolean isAdmin) {
        List<Long> requires = new ArrayList<>();
        if (startAreaSize != null || endAreaSize != null) {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
            com.everhomes.server.schema.tables.EhCustomerRequirements requirements = Tables.EH_CUSTOMER_REQUIREMENTS;
            SelectQuery<EhCustomerRequirementsRecord> query = context.selectQuery(requirements);
            query.addSelect(requirements.CUSTOMER_ID);
            query.addConditions(requirements.STATUS.eq(CommonStatus.ACTIVE.getCode()));
//            if (startAreaSize != null)
//                query.addConditions(requirements.MIN_AREA.le(startAreaSize));
//            if (endAreaSize != null)
//                query.addConditions(requirements.MAX_AREA.ge(endAreaSize));
            if (startAreaSize != null && endAreaSize != null) {
                query.addConditions(requirements.MIN_AREA.ge(startAreaSize));
                query.addConditions(requirements.MAX_AREA.le(endAreaSize));
            }
            if (startAreaSize != null && endAreaSize == null) {
                query.addConditions(requirements.MIN_AREA.ge(startAreaSize));
            }
            if (startAreaSize == null) {
                query.addConditions(requirements.MAX_AREA.le(endAreaSize));
            }
            requires = query.fetchInto(Long.class);

        }
        return requires;
    }

    @Override
    public void deleteInvitedCustomer(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        context.update(customer)
                .set(customer.STATUS, CommonStatus.INACTIVE.getCode())
                .where(customer.ID.eq(id))
                .execute();
    }

    @Override
    public void deleteCustomerContacts(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhCustomerContacts contact = Tables.EH_CUSTOMER_CONTACTS;
        context.update(contact)
                .set(contact.STATUS, CommonStatus.INACTIVE.getCode())
                .where(contact.CUSTOMER_ID.eq(id))
                .execute();
    }

    @Override
    public void deleteCustomerContactsWithType(Long id, Byte type) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhCustomerContacts contact = Tables.EH_CUSTOMER_CONTACTS;
        context.update(contact)
                .set(contact.STATUS, CommonStatus.INACTIVE.getCode())
                .where(contact.CUSTOMER_ID.eq(id))
                .and(contact.CONTACT_TYPE.eq(type))
                .execute();
    }

    @Override
    public Long createRequirementAddress(CustomerRequirementAddress address) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerRequirementAddresses.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirementAddresses.class));
        address.setId(id);

        User user = UserContext.current().getUser();
        address.setOperatorUid(user.getId());
        address.setCreatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        address.setCreateTime(new Timestamp(l2));
        address.setOperatorTime(new Timestamp(l2));

        EhCustomerRequirementAddressesDao dao = new EhCustomerRequirementAddressesDao(context.configuration());
        dao.insert(address);
        return id;
    }

    @Override
    public Long updateCurrentRent(CustomerRequirementAddress address) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirementAddresses.class));

        User user = UserContext.current().getUser();
        address.setOperatorUid(user.getId());

        Long l2 = DateHelper.currentGMTTime().getTime();
        address.setOperatorTime(new Timestamp(l2));

        EhCustomerRequirementAddressesDao dao = new EhCustomerRequirementAddressesDao(context.configuration());
        dao.update(address);
        return address.getId();
    }

    @Override
    public CustomerRequirementAddress findRequirementAddressById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerRequirementAddresses.class));


        EhCustomerRequirementAddressesDao dao = new EhCustomerRequirementAddressesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), CustomerRequirementAddress.class);    }

    @Override
    public List<CustomerRequirementAddress> findRequirementAddressByRequirementId(Long requirementId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCustomerTrackers.class));

        SelectQuery<EhCustomerRequirementAddressesRecord> query = context.selectQuery(Tables.EH_CUSTOMER_REQUIREMENT_ADDRESSES);
        query.addConditions(Tables.EH_CUSTOMER_REQUIREMENT_ADDRESSES.REQUIREMENT_ID.eq(requirementId));
        query.addConditions(Tables.EH_CUSTOMER_REQUIREMENT_ADDRESSES.STATUS.ne(InvitedCustomerStatus.INVALID.getCode()));
        return query.fetch().map(r -> ConvertHelper.convert(r, CustomerRequirementAddress.class));    }

    public List<EnterpriseCustomer> listCustomersByType(byte code, ListingLocator locator, int pageSize) {
        return null;
    }

    @Override
    public void deleteCustomerTrackersByCustomerId(Long customerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhCustomerTrackers tracker = Tables.EH_CUSTOMER_TRACKERS;
        context.update(tracker)
                .set(tracker.STATUS, CommonStatus.INACTIVE.getCode())
                .where(tracker.CUSTOMER_ID.eq(customerId))
                .execute();
    }


    @Override
    public void updateToEnterpriseCustomerByCustomerId(Long customerId, Long phoneNumber, String contactName){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        context.update(customer)
                .set(customer.CUSTOMER_SOURCE, InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode())
                .set(customer.LEVEL_ITEM_ID, (long)CustomerLevelType.REGISTERED_CUSTOMER.getCode())
                .set(customer.CONTACT_MOBILE, phoneNumber.toString())
                .set(customer.CONTACT_NAME, contactName)
                .where(customer.ID.eq(customerId))
                .execute();
    }

    @Override
    public void getInitCustomerStatus() {

    }
}
