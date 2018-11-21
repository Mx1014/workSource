package com.everhomes.investment;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.CustomerErrorCode;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.InvitedCustomerStatisticsDTO;
import com.everhomes.rest.investment.InvitedCustomerStatus;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhCustomerTrackers;
import com.everhomes.server.schema.tables.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
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
    public List<EnterpriseCustomer> getInitCustomerStatus(Integer pageSize, Long nextAnchor) {


        List<EnterpriseCustomer> customers = new ArrayList<>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhEnterpriseCustomers customer = Tables.EH_ENTERPRISE_CUSTOMERS;
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(customer);
        query.addSelect(customer.ID, customer.LEVEL_ITEM_ID, customer.CREATE_TIME, customer.NAMESPACE_ID, customer.COMMUNITY_ID);
        query.addConditions(customer.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(customer.LEVEL_ITEM_ID.isNotNull());


        if(nextAnchor != null && nextAnchor != 0){
            query.addConditions(customer.ID.ge(nextAnchor));
        }

        if(pageSize != null && pageSize != 0){
            query.addLimit(pageSize + 1);
        }

        query.addGroupBy(customer.ID);

        customers = query.fetchInto(EnterpriseCustomer.class);


        return customers;

    }

    @Override
    public void createCustomerLevelChangeRecord(CustomerLevelChangeRecord record) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerLevelChangeRecords.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerLevelChangeRecords.class));
        record.setId(id);


        if(record.getChangeDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            record.setChangeDate(new Timestamp(l2));
        }

        EhCustomerLevelChangeRecordsDao dao = new EhCustomerLevelChangeRecordsDao(context.configuration());
        dao.insert(record);
        //return id;
    }

    public List<CustomerLevelChangeRecord> listCustomerLevelChangeRecord(Integer namespaceId, Long communityId,Timestamp queryStartDate, Timestamp queryEndDate){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerLevelChangeRecordsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_LEVEL_CHANGE_RECORDS);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_LEVEL_CHANGE_RECORDS.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_CUSTOMER_LEVEL_CHANGE_RECORDS.COMMUNITY_ID.eq(communityId));
        }
        if(queryEndDate != null){
            query.addConditions(Tables.EH_CUSTOMER_LEVEL_CHANGE_RECORDS.CHANGE_DATE.le(queryEndDate));
        }
        if(queryStartDate != null){
            query.addConditions(Tables.EH_CUSTOMER_LEVEL_CHANGE_RECORDS.CHANGE_DATE.ge(queryStartDate));
        }

        return query.fetchInto(CustomerLevelChangeRecord.class);

    }

    @Override
    public Integer countCustomerNumByCreateDate(Long communityId, Timestamp queryStartDate, Timestamp queryEndDate){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseCustomersRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_CUSTOMERS);


        if(queryEndDate != null){
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CREATE_TIME.le(queryEndDate));
        }
        if(queryStartDate != null){
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.CREATE_TIME.ge(queryStartDate));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        }
        return query.fetchCount();
    }

    @Override
    public Integer countTrackingNumByCreateDate(Long communityId, Timestamp queryStartDate, Timestamp queryEndDate){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhCustomerTrackingsRecord> query = context.selectQuery(Tables.EH_CUSTOMER_TRACKINGS);
        query.addSelect(Tables.EH_CUSTOMER_TRACKINGS.ID);


        query.addJoin(Tables.EH_ENTERPRISE_CUSTOMERS, Tables.EH_ENTERPRISE_CUSTOMERS.ID.eq(Tables.EH_CUSTOMER_TRACKINGS.CUSTOMER_ID));

        if(queryEndDate != null){
            query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.TRACKING_TIME.le(queryEndDate));
        }
        if(queryStartDate != null){
            query.addConditions(Tables.EH_CUSTOMER_TRACKINGS.TRACKING_TIME.ge(queryStartDate));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_ENTERPRISE_CUSTOMERS.COMMUNITY_ID.eq(communityId));
        }
        return query.fetchCount();
    }

    @Override
    public void createCustomerStatisticsDaily(CustomerStatisticDaily daily) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerStatisticsDaily.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsDaily.class));
        daily.setId(id);


        if(daily.getCreateDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            daily.setCreateDate(new Timestamp(l2));
        }

        EhCustomerStatisticsDailyDao dao = new EhCustomerStatisticsDailyDao(context.configuration());
        dao.insert(daily);
        //return id;
    }

    @Override
    public void createCustomerStatisticsDailyTotal(CustomerStatisticDailyTotal dailyTotal) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerStatisticsDailyTotal.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsDailyTotal.class));
        dailyTotal.setId(id);

        if(dailyTotal.getCreateDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            dailyTotal.setCreateDate(new Timestamp(l2));
        }

        EhCustomerStatisticsDailyTotalDao dao = new EhCustomerStatisticsDailyTotalDao(context.configuration());
        dao.insert(dailyTotal);
        //return id;
    }

    @Override
    public CustomerStatisticDaily getCustomerStatisticsDaily(Integer namespaceId, Long communityId, Date date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsDailyRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_DAILY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID.eq(communityId));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID.isNull());
        }
        if(date != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.eq(date));
        }

        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID);

        return query.fetchAnyInto(CustomerStatisticDaily.class);
    }

    @Override
    public List<CustomerStatisticDaily> listCustomerStatisticDaily(Integer namespaceId, List<Long> communityIds, Date startDate, Date endDate, Integer pageSize, Integer offset) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsDailyRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_DAILY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityIds != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID.in(communityIds));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID.isNull());
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.le(endDate));
        }
        if(endDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.ge(startDate));
        }
        if(pageSize != null){
            query.addLimit(offset*pageSize, pageSize + 1);
        }

        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID);

        return query.fetchInto(CustomerStatisticDaily.class);
    }

    @Override
    public List<CustomerStatisticDailyTotal> listCustomerStatisticDailyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate, Integer pageSize, Integer offset) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsDailyTotalRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.NAMESPACE_ID.eq(namespaceId));
        }
        if(organizationId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.ORGANIZATION_ID.eq(organizationId));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.ORGANIZATION_ID.isNull());
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.DATE_STR.le(endDate));
        }
        if(endDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.DATE_STR.ge(startDate));
        }
        if(pageSize != null){
            query.addLimit(offset * pageSize, pageSize + 1);
        }

        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.ORGANIZATION_ID);
        return query.fetchInto(CustomerStatisticDailyTotal.class);
    }


    @Override
    public List<CustomerStatisticMonthlyTotal> listCustomerStatisticMonthlyTotal(Integer namespaceId, Long organizationId, Date startDate, Date endDate, Integer pageSize, Integer offset) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsMonthlyTotalRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.NAMESPACE_ID.eq(namespaceId));
        }
        if(organizationId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.ORGANIZATION_ID.eq(organizationId));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.ORGANIZATION_ID.isNull());
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.DATE_STR.le(endDate));
        }
        if(endDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.DATE_STR.ge(startDate));
        }
        if(pageSize != null){
            query.addLimit(pageSize + 1);
        }

        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.ORGANIZATION_ID);
        return query.fetchInto(CustomerStatisticMonthlyTotal.class);
    }

    @Override
    public void deleteCustomerStatisticDaily(Integer namespaceId, Long communityId, Date startDate){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsDaily.class));
        DeleteQuery<EhCustomerStatisticsDailyRecord> query = context.deleteQuery(Tables.EH_CUSTOMER_STATISTICS_DAILY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.COMMUNITY_ID.eq(communityId));
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY.DATE_STR.eq(startDate));
        }else{
            LOGGER.error("delete date can not be null");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_STATISTIC_DELETE_DATE,
                    "delete date can not be null");
        }
        query.execute();
    }
    @Override
    public void deleteCustomerStatisticDailyTotal(Integer namespaceId, Long organizationId, Date startDate){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsDailyTotal.class));
        DeleteQuery<EhCustomerStatisticsDailyTotalRecord> query = context.deleteQuery(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.NAMESPACE_ID.eq(namespaceId));
        }
        if(organizationId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.ORGANIZATION_ID.eq(organizationId));
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_DAILY_TOTAL.DATE_STR.eq(startDate));
        }else{
            LOGGER.error("delete date can not be null");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_STATISTIC_DELETE_DATE,
                    "delete date can not be null");
        }
        query.execute();
    }

    @Override
    public void deleteCustomerStatisticDaily(Integer namespaceId, Long communityId, Date startDate, Date endDate){

    }

    @Override
    public void deleteCustomerStatisticDailyTotal(Integer namespaceId, Long communityId, Date startDate, Date endDate){

    }

    @Override
    public void deleteCustomerStatisticMonthly(Integer namespaceId, Long communityId, Date startDate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsMonthly.class));
        DeleteQuery<EhCustomerStatisticsMonthlyRecord> query = context.deleteQuery(Tables.EH_CUSTOMER_STATISTICS_MONTHLY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID.eq(communityId));
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.eq(startDate));
        }else{
            LOGGER.error("delete date can not be null");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_STATISTIC_DELETE_DATE,
                    "delete date can not be null");
        }
        query.execute();
    }

    @Override
    public void deleteCustomerStatisticMonthlyTotal(Integer namespaceId, Long organizationId, Date startDate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsMonthlyTotal.class));
        DeleteQuery<EhCustomerStatisticsMonthlyTotalRecord> query = context.deleteQuery(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.NAMESPACE_ID.eq(namespaceId));
        }
        if(organizationId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.ORGANIZATION_ID.eq(organizationId));
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY_TOTAL.DATE_STR.eq(startDate));
        }else{
            LOGGER.error("delete date can not be null");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_STATISTIC_DELETE_DATE,
                    "delete date can not be null");
        }
        query.execute();
    }

    @Override
    public void deleteCustomerStatisticMonthly(Integer namespaceId, Long communityId, Date startDate, Date endDate) {

    }

    @Override
    public void deleteCustomerStatisticMonthlyTotal(Integer namespaceId, Long communityId, Date startDate, Date endDate) {

    }

    @Override
    public void createCustomerStatisticTotal(CustomerStatisticTotal total) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerStatisticsTotal.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsTotal.class));
        total.setId(id);

        if(total.getCreateDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            total.setCreateDate(new Timestamp(l2));
        }

        EhCustomerStatisticsTotalDao dao = new EhCustomerStatisticsTotalDao(context.configuration());
        dao.insert(total);
    }

    @Override
    public void deleteCustomerStatisticTotal(Integer namespaceId, Long organizationId, Date startDate) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsMonthlyTotal.class));
        DeleteQuery<EhCustomerStatisticsTotalRecord> query = context.deleteQuery(Tables.EH_CUSTOMER_STATISTICS_TOTAL);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_TOTAL.NAMESPACE_ID.eq(namespaceId));
        }
        if(organizationId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_TOTAL.ORGANIZATION_ID.eq(organizationId));
        }
        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_TOTAL.DATE_STR.eq(startDate));
        }else{
            LOGGER.error("delete date can not be null");
            throw RuntimeErrorException.errorWith(CustomerErrorCode.SCOPE, CustomerErrorCode.ERROR_CUSTOMER_STATISTIC_DELETE_DATE,
                    "delete date can not be null");
        }
        query.execute();
    }

    @Override
    public CustomerStatisticTotal getCustomerStatisticTotal(Integer namespaceId, Long communityId, Date date) {
        return null;
    }

    @Override
    public void createCustomerStatisticsMonthly(CustomerStatisticMonthly monthly) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerStatisticsMonthly.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsMonthly.class));
        monthly.setId(id);

        if(monthly.getCreateDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            monthly.setCreateDate(new Timestamp(l2));
        }

        EhCustomerStatisticsMonthlyDao dao = new EhCustomerStatisticsMonthlyDao(context.configuration());
        dao.insert(monthly);
    }

    @Override
    public void createCustomerStatisticsMonthlyTotal(CustomerStatisticMonthlyTotal monthlyTotal) {
        long id = this.sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhCustomerStatisticsMonthlyTotal.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhCustomerStatisticsMonthlyTotal.class));
        monthlyTotal.setId(id);

        if(monthlyTotal.getCreateDate() == null) {
            Long l2 = DateHelper.currentGMTTime().getTime();
            monthlyTotal.setCreateDate(new Timestamp(l2));
        }

        EhCustomerStatisticsMonthlyTotalDao dao = new EhCustomerStatisticsMonthlyTotalDao(context.configuration());
        dao.insert(monthlyTotal);
        //return id;
    }

    @Override
    public CustomerStatisticMonthly getCustomerStatisticsMonthly(Integer namespaceId, Long communityId, Date date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsMonthlyRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_MONTHLY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.NAMESPACE_ID.eq(namespaceId));
        }
        if(communityId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID.eq(communityId));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID.isNull());
        }

        if(date != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.eq(date));
        }
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID);
        return query.fetchAnyInto(CustomerStatisticMonthly.class);
    }

    @Override
    public List<CustomerStatisticMonthly> listCustomerStatisticMonthly(Integer namespaceId, List<Long> communityIds, Date startDate, Date endDate, Integer pageSize, Integer offset) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhCustomerStatisticsMonthlyRecord> query = context.selectQuery(Tables.EH_CUSTOMER_STATISTICS_MONTHLY);

        if(namespaceId != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.NAMESPACE_ID.eq(namespaceId));
        }

        if(communityIds != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID.in(communityIds));
        }else{
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID.isNull());
        }

        if(startDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.le(endDate));
        }

        if(endDate != null){
            query.addConditions(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.ge(startDate));
        }

        if(pageSize != null){
            query.addLimit(offset * pageSize,pageSize + 1);
        }

        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.DATE_STR.desc());
        query.addOrderBy(Tables.EH_CUSTOMER_STATISTICS_MONTHLY.COMMUNITY_ID);
        return query.fetchInto(CustomerStatisticMonthly.class);
    }

}
