package com.everhomes.asset;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhPaymentBillGroups;
import com.everhomes.server.schema.tables.EhPaymentBillGroupsRules;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.EhPaymentChargingItemScopes;
import com.everhomes.server.schema.tables.EhPaymentChargingItems;
import com.everhomes.server.schema.tables.EhPaymentChargingStandards;
import com.everhomes.server.schema.tables.EhPaymentChargingStandardsScopes;
import com.everhomes.server.schema.tables.EhPaymentContractReceiver;
import com.everhomes.server.schema.tables.EhPaymentExemptionItems;
import com.everhomes.server.schema.tables.EhPaymentVariables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import freemarker.core.ArithmeticEngine;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Administrator on 2017/2/20.
 */
@Component
public class AssetProviderImpl implements AssetProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void creatAssetBill(AssetBill bill) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetBills.class));

        bill.setId(id);
        bill.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        bill.setStatus(AssetBillStatus.UNPAID.getCode());

        bill.setUpdateTime(bill.getCreateTime());
        bill.setUpdateUid(bill.getCreatorUid());

        LOGGER.info("creatAssetBill: " + bill);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBills.class, id));
        EhAssetBillsDao dao = new EhAssetBillsDao(context.configuration());
        dao.insert(bill);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetBills.class, null);
    }

    @Override
    public void updateAssetBill(AssetBill bill) {
        assert(bill.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBills.class, bill.getId()));
        EhAssetBillsDao dao = new EhAssetBillsDao(context.configuration());
        bill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(bill);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetBills.class, bill.getId());
    }

    @Override
    public AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);

        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_ASSET_BILLS.ID.eq(id));

        List<AssetBill> bills = new ArrayList<>();
        query.fetch().map((EhAssetBillsRecord record) -> {
            bills.add(ConvertHelper.convert(record, AssetBill.class));
            return null;
        });

        if(bills.size() == 0) {
            return null;
        }
        return bills.get(0);
    }

    @Override
    public List<AssetBillTemplateFieldDTO> findTemplateFieldByTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType, Long templateVersion) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillTemplateFieldsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS);

        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TARGET_TYPE.eq(targetType));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TEMPLATE_VERSION.eq(templateVersion));

        query.addOrderBy(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.SELECTED_FLAG.desc(), Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.ID.asc());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("findTemplateFieldByTemplateVersion, sql=" + query.getSQL());
            LOGGER.debug("findTemplateFieldByTemplateVersion, bindValues=" + query.getBindValues());
        }
       
        List<AssetBillTemplateFieldDTO> templateVersions = new ArrayList<>();
        query.fetch().map((EhAssetBillTemplateFieldsRecord record) -> {
            templateVersions.add(ConvertHelper.convert(record, AssetBillTemplateFieldDTO.class));
            return null;
        });

        return templateVersions;
    }

    @Override
    public Long getTemplateVersion(Long ownerId, String ownerType, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillTemplateFieldsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS);

        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TARGET_TYPE.eq(targetType));

        query.addOrderBy(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.TEMPLATE_VERSION.desc());
        query.addLimit(1);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTemplateVersion, sql=" + query.getSQL());
            LOGGER.debug("getTemplateVersion, bindValues=" + query.getBindValues());
        }

        List<Long> templateVersions = new ArrayList<>();
        query.fetch().map((EhAssetBillTemplateFieldsRecord record) -> {
            templateVersions.add(record.getTemplateVersion());
            return null;
        });

        if(templateVersions.size() == 0) {
            templateVersions.add(0L);
        }

        return templateVersions.get(0);
    }

    @Override
    public void creatTemplateField(AssetBillTemplateFields field) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetBillTemplateFields.class));

        field.setId(id);

        LOGGER.info("creatTemplateField: " + field);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBillTemplateFields.class, id));
        EhAssetBillTemplateFieldsDao dao = new EhAssetBillTemplateFieldsDao(context.configuration());
        dao.insert(field);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetBillTemplateFields.class, null);
    }

    @Override
    public List<AssetBill> listAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, List<Long> tenantIds, String tenantType,
                                         Long addressId, Byte status, Long startTime, Long endTime, CrossShardListingLocator locator, Integer pageSize) {
        List<AssetBill> bills = new ArrayList<>();

        long pageOffset = 0L;
        if (locator.getAnchor() == null || locator.getAnchor() == 0L){
            locator.setAnchor(0L);
            pageOffset = 1L;
        }
        if(locator.getAnchor() != 0L){
            pageOffset = locator.getAnchor();
        }
        Integer offset =  (int) ((pageOffset - 1 ) * pageSize);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);

        query.addConditions(Tables.EH_ASSET_BILLS.STATUS.ne(AssetBillStatus.INACTIVE.getCode()));

        if(ownerId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
        }
        if(ownerType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
        }
        if(targetId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
        }
        if(targetType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));
        }

        if(tenantIds != null && tenantIds.size() > 0) {
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_ID.in(tenantIds));
        }
        if(tenantType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_TYPE.eq(tenantType));
        }

        if(addressId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.ADDRESS_ID.eq(addressId));
        }

        if(status !=null) {
            query.addConditions(Tables.EH_ASSET_BILLS.STATUS.eq(status));
        }

        if(startTime != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.ge(new Timestamp(startTime)));
        }

        if(endTime != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.le(new Timestamp(endTime)));
        }
        query.addOrderBy(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.desc());
        query.addLimit(offset, pageSize);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listAssetBill, sql=" + query.getSQL());
            LOGGER.debug("listAssetBill, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhAssetBillsRecord record) -> {
            bills.add(ConvertHelper.convert(record, AssetBill.class));
            return null;
        });

        if (bills.size() >= pageSize) {
            locator.setAnchor(pageOffset+1);
        }

        return bills;
    }

    @Override
    public List<BigDecimal> listPeriodUnpaidAccountAmount(Long ownerId, String ownerType, Long targetId, String targetType,
                                Long addressId, String tenantType, Long tenantId, Timestamp currentAccountPeriod) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);
        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));

        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));

        query.addConditions(Tables.EH_ASSET_BILLS.TENANT_ID.eq(tenantId));
        query.addConditions(Tables.EH_ASSET_BILLS.TENANT_TYPE.eq(tenantType));

        query.addConditions(Tables.EH_ASSET_BILLS.ADDRESS_ID.eq(addressId));

        query.addConditions(Tables.EH_ASSET_BILLS.STATUS.eq(AssetBillStatus.UNPAID.getCode()));
        query.addConditions(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.lt(currentAccountPeriod));

        List<BigDecimal> accountAmounts = new ArrayList<>();

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listPeriodUnpaidAccountAmount, sql=" + query.getSQL());
            LOGGER.debug("listPeriodUnpaidAccountAmount, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhAssetBillsRecord record) -> {
            accountAmounts.add(record.getPeriodUnpaidAccountAmount());
            return null;
        });

        return accountAmounts;
    }

    @Override
    public List<AssetBill> listUnpaidBillsGroupByTenant(Long ownerId, String ownerType, Long targetId, String targetType) {

        List<AssetBill> bills = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);


        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILLS.STATUS.eq(AssetBillStatus.UNPAID.getCode()));
        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));
//
//        query.addGroupBy(Tables.EH_ASSET_BILLS.TENANT_ID, Tables.EH_ASSET_BILLS.TENANT_TYPE);


        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listUnpaidBillsGroupByTenant, sql=" + query.getSQL());
            LOGGER.debug("listUnpaidBillsGroupByTenant, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhAssetBillsRecord record) -> {
            bills.add(ConvertHelper.convert(record, AssetBill.class));
            return null;
        });

        return bills;
    }

    @Override
    public int countNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType, Timestamp startTime, Timestamp endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillNotifyRecordsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_NOTIFY_RECORDS);

        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_TYPE.eq(targetType));

        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.ge(startTime));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.le(endTime));

        int count = query.fetchCount();
        return count;
    }

    @Override
    public AssetBillNotifyRecords getLastAssetBillNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillNotifyRecordsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_NOTIFY_RECORDS);

        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_ID.eq(targetId));
        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_TYPE.eq(targetType));

        query.addOrderBy(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.desc());
        query.addLimit(1);
        List<AssetBillNotifyRecords> records = new ArrayList<>();
        query.fetch().map((EhAssetBillNotifyRecordsRecord record) -> {
            records.add(ConvertHelper.convert(record, AssetBillNotifyRecords.class));
            return null;
        });

        if(records.size() == 0) {
            return null;
        }
        return records.get(0);
    }

    @Override
    public AssetVendor findAssetVendorByOwner(String ownerType, Long ownerId) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).selectFrom(Tables.EH_ASSET_VENDOR)
                .where(Tables.EH_ASSET_VENDOR.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_ASSET_VENDOR.OWNER_ID.eq(ownerId)).fetchOneInto(AssetVendor.class);

    }

    @Override
    public List<AssetBill> listUnpaidBills(String tenantType, Long tenantId, Long addressId) {
        List<AssetBill> bills = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);

        query.addConditions(Tables.EH_ASSET_BILLS.STATUS.eq(AssetBillStatus.UNPAID.getCode()));
        if(addressId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.ADDRESS_ID.eq(addressId));
        }

        if(tenantId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_ID.eq(tenantId));
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_TYPE.eq(tenantType));
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listUnpaidBills, sql=" + query.getSQL());
            LOGGER.debug("listUnpaidBills, bindValues=" + query.getBindValues());
        }

        query.fetch().map((EhAssetBillsRecord record) -> {
            bills.add(ConvertHelper.convert(record, AssetBill.class));
            return null;
        });

        return bills;
    }

    @Override
    public AssetBill findAssetBill(Long ownerId, String ownerType, Long targetId, String targetType, String dateStr, Long tenantId, String tenantType, Long addressId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);
        if(ownerId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
        }
        if(ownerType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
        }
        if(targetId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
        }
        if(targetType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));
        }

        if(tenantId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_ID.eq(tenantId));
        }
        if(tenantType != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.TENANT_TYPE.eq(tenantType));
        }

        if(addressId != null) {
            query.addConditions(Tables.EH_ASSET_BILLS.ADDRESS_ID.eq(addressId));
        }

        if(!StringUtils.isNullOrEmpty(dateStr)) {
            query.addConditions(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.like(dateStr + "%"));
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("findAssetBill, sql=" + query.getSQL());
            LOGGER.debug("findAssetBill, bindValues=" + query.getBindValues());
        }

        return query.fetchAnyInto(AssetBill.class);
    }

    @Override
    public List<ListBillsDTO> listBills(Integer currentNamespaceId, Long ownerId, String ownerType, String buildingName,String apartmentName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status) {
        List<ListBillsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<EhPaymentBillsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        if(status!=null){
            query.addConditions(t.SWITCH.eq(status));
        }
//        if (addressId != null) {
//            Map<Long,String> IdAndTypes = new HashMap<>();
//            context.select(Tables.EH_USERS.ID).from(Tables.EH_USERS).where(Tables.EH_USERS.ADDRESS_ID.eq(addressId)).fetch().map(r -> {
//                IdAndTypes.put(r.getValue(Tables.EH_USERS.ID),"individual");
//                return null;});
//            context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.ADDRESS_ID.eq(addressId)).fetch().map(r -> {
//                IdAndTypes.put(r.getValue(Tables.EH_USERS.ID),"organization");
//                return null;
//            });
//            for(Map.Entry<Long,String> entry : IdAndTypes.entrySet()){
//                query.addConditions(t.OWNER_TYPE.eq(entry.getValue()).and(t.OWNER_ID.eq(entry.getKey())));
//            }
//        }
        if(billGroupId!=null) {
            query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        }
        if(billStatus!=null) {
            query.addConditions(t.STATUS.eq(billStatus));
        }
        if(!org.springframework.util.StringUtils.isEmpty(targetName)) {
            query.addConditions(t.TARGET_NAME.like("%"+targetName+"%"));
        }
        if(status!=null && status == 1){
            query.addOrderBy(t.STATUS);
        }
        query.addOrderBy(t.DATE_STR.desc());
        query.addGroupBy(t.TARGET_NAME);
        query.addLimit(pageOffSet,pageSize+1);
        if(!org.springframework.util.StringUtils.isEmpty(buildingName)){
            query.addConditions(t.BUILDING_NAME.eq(buildingName));
        }
        if(!org.springframework.util.StringUtils.isEmpty(apartmentName)){
            query.addConditions(t.APARTMENT_NAME.eq(apartmentName));
        }
//        List<Object[]> billAddresses = new ArrayList<>();
//        final String[] buildingNameSelected = {""};
//        final String[] apartmentSelected = {""};
        query.fetch().map(r -> {
            ListBillsDTO dto = new ListBillsDTO();
//            if(buildingName!=null && apartmentName!=null){
//                dto.setBuildingName(buildingName);
//                dto.setApartmentName(apartmentName);
//            }else{
//                Object[] billAddress = new Object[2];
//                billAddress[0] = r.getValue(t.TARGET_TYPE);
//                billAddress[1] = r.getValue(t.TARGET_ID);
//                billAddresses.add(billAddress);
//                dto.setApartmentName(r.getApartmentName());
//                dto.setBuildingName(r.getBuildingName());
//            }
            dto.setBuildingName(r.getBuildingName());
            dto.setApartmentName(r.getApartmentName());
            dto.setAmountOwed(r.getAmountOwed());
            dto.setAmountReceivable(r.getAmountReceivable());
            dto.setAmountReceived(r.getAmountReceived());
            if(!org.springframework.util.StringUtils.isEmpty(buildingName)) {
                dto.setBillGroupName(billGroupName);
            }else{
                String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS).where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(r.getValue(t.BILL_GROUP_ID))).fetchOne(0,String.class);
                dto.setBillGroupName(billGroupNameFound);
            }
            dto.setBillId(r.getValue(t.ID));
            dto.setBillStatus(r.getValue(t.STATUS));
            dto.setNoticeTel(r.getValue(t.NOTICETEL));
            dto.setNoticeTimes(r.getNoticeTimes());
            dto.setDateStr(r.getDateStr());
            dto.setTargetName(r.getTargetName());
            dto.setTargetId(r.getTargetId());
            dto.setTargetType(r.getTargetType());
            dto.setOwnerId(r.getOwnerId());
            dto.setOwnerType(r.getOwnerType());
            list.add(dto);
            return null;});
//        for(int i = 0; i < billAddresses.size(); i++) {
//            ListBillsDTO dto = list.get(i);
//            Object[] objs = billAddresses.get(i);
//            final String[] buildingNameFound = {""};
//            final String[] apartmentNameFound = {""};
//            try {
//                String targetType = (String) objs[0];
//                Long targetId = (Long) objs[1];
//                if(targetType.equals("eh_user")){
//                    context.select(Tables.EH_ADDRESSES.BUILDING_NAME,Tables.EH_ADDRESSES.APARTMENT_NAME).from(Tables.EH_USERS,Tables.EH_ADDRESSES)
//                            .where(Tables.EH_USERS.ID.eq(targetId)).and(Tables.EH_USERS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID))
//                            .fetch().map(r -> {
//                                buildingNameFound[0] = r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME);
//                                apartmentNameFound[0] = r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME);
//                                return null;
//                            });
//            } else if(targetType.equals("eh_organization")){
//                    context.select(Tables.EH_ADDRESSES.BUILDING_NAME,Tables.EH_ADDRESSES.APARTMENT_NAME).from(Tables.EH_ORGANIZATIONS,Tables.EH_ADDRESSES)
//                            .where(Tables.EH_ORGANIZATIONS.ID.eq(targetId)).and(Tables.EH_ORGANIZATIONS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID))
//                            .fetch().map(r -> {
//                                buildingNameFound[0] = r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME);
//                                apartmentNameFound[0] = r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME);
//                                return null;});
//                }
//            } catch (Exception e) {
//
//            }
//            if(buildingNameSelected[0]!=null && !buildingNameSelected[0].equals("")){
//                dto.setBuildingName(buildingNameSelected[0]);
//            }else{
//                dto.setBuildingName(buildingNameFound[0]);
//            }
//            if(apartmentSelected[0]!=null && !apartmentSelected[0].equals("")){
//                dto.setApartmentName(apartmentSelected[0]);
//            }else{
//                dto.setApartmentName(apartmentNameFound[0]);
//            }
//        }
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize) {
        List<BillDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        context.select(t.DATE_STR,t.CHARGING_ITEM_NAME,t.AMOUNT_RECEIVABLE,t.AMOUNT_RECEIVED,t.AMOUNT_OWED,t.STATUS,t.ID)
                .from(t)
                .leftOuterJoin(t1)
                .on(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .where(t.BILL_ID.eq(billId))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.DEFAULT_ORDER)
                .limit(pageOffSet,pageSize+1)
                .fetch()
                .map(r ->{
            BillDTO dto =new BillDTO();
            dto.setTargetName(targetName);
            dto.setDateStr(r.getValue(t.DATE_STR));
            dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
            dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
            dto.setAmountReceived(r.getValue(t.AMOUNT_RECEIVED));
            dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
            dto.setBillStatus(r.getValue(t.STATUS));
            dto.setBillItemId(r.getValue(t.ID));
            dtos.add(dto);
            return null;});
        return dtos;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<NoticeInfo> list = new ArrayList<>();
                dslContext.select(Tables.EH_PAYMENT_BILLS.NOTICETEL,Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILLS.AMOUNT_OWED,Tables.EH_PAYMENT_BILLS.TARGET_ID,Tables.EH_PAYMENT_BILLS.TARGET_TYPE,Tables.EH_PAYMENT_BILLS.TARGET_NAME)
                .from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .fetch().map(r -> {
                    NoticeInfo info = new NoticeInfo();
                    info.setPhoneNum(r.getValue(Tables.EH_PAYMENT_BILLS.NOTICETEL));
                    info.setAmountRecevable(r.getValue(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE));
                    info.setAmountOwed(r.getValue(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED));
                    info.setTargetId(r.getValue(Tables.EH_PAYMENT_BILLS.TARGET_ID));
                    info.setTargetType(r.getValue(Tables.EH_PAYMENT_BILLS.TARGET_TYPE));
                    info.setTargetName(r.getValue(Tables.EH_PAYMENT_BILLS.TARGET_NAME));
                    list.add(info);
                    return null;});
        List<String> fetch = dslContext.select(Tables.EH_APP_URLS.NAME)
                .from(Tables.EH_APP_URLS)
                .where(Tables.EH_APP_URLS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetch(Tables.EH_APP_URLS.NAME);
        String appName = fetch.get(0);
        for(int i = 0; i < list.size(); i++){
            list.get(i).setAppName(appName);
        }
        return list;
    }

    @Override
    public List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill) {
        List<BillDetailDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<Record> query = dslContext.selectQuery();
        query.addFrom(t);
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.TARGET_TYPE.eq(targetType));
        query.addConditions(t.TARGET_ID.eq(targetId));
        query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        if(isOwedBill==1){
            query.addConditions(t.STATUS.eq((byte)0));
        }
        query.fetch()
                .map(r -> {
                    BillDetailDTO dto = new BillDetailDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setAmountReceviable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setBillId(r.getValue(t.ID));
                    dto.setDateStr(r.getValue(t.DATE_STR));
                    dto.setStatus(r.getValue(t.STATUS));
                    dtos.add(dto);
                    return null;});
        return dtos;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long billId) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        final String[] dateStr = {""};
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        List<ShowBillDetailForClientDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        dslContext.select(t.AMOUNT_RECEIVABLE,t.CHARGING_ITEM_NAME,t.DATE_STR,t.AMOUNT_OWED,t.AMOUNT_RECEIVABLE)
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .fetch()
                .map(r -> {
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    dtos.add(dto);
                    dateStr[0] = r.getValue(t.DATE_STR);
                    amountOwed[0] = amountOwed[0].add(r.getValue(t.AMOUNT_OWED));
                    amountReceivable[0] = amountReceivable[0].add(r.getValue(t.AMOUNT_RECEIVABLE));
                    return null;
                });
        response.setAmountReceivable(amountReceivable[0]);
        response.setAmountOwed(amountOwed[0]);
        response.setDatestr(dateStr[0]);
        response.setShowBillDetailForClientDTOList(dtos);
        return response;
    }

    @Override
    public List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType) {
        List<ListBillGroupsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        context.select()
                .from(t)
                .where(t.OWNER_ID.eq(ownerId))
                .and(t.OWNER_TYPE.eq(ownerType))
                .orderBy(t.DEFAULT_ORDER)
                .fetch()
                .map(r -> {
                    ListBillGroupsDTO dto = new ListBillGroupsDTO();
                    dto.setBillGroupId(r.getValue(t.ID));
                    dto.setBillGroupName(r.getValue(t.NAME));
                    dto.setDefaultOrder(r.getValue(t.DEFAULT_ORDER));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules rule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("rule");
        EhPaymentBillGroups bg = Tables.EH_PAYMENT_BILL_GROUPS.as("bg");
        EhPaymentChargingItems ci = Tables.EH_PAYMENT_CHARGING_ITEMS.as("ci");
        ShowCreateBillDTO response = new ShowCreateBillDTO();
        List<BillItemDTO> list = new ArrayList<>();
        final String[] billRuleName = {""};
        context.select(rule.ID,rule.CHARGING_ITEM_NAME,bg.NAME)
                .from(bg,rule,ci)
                .where(bg.ID.eq(rule.BILL_GROUP_ID))
                .and(rule.CHARGING_ITEM_ID.eq(ci.ID))
                .and(bg.ID.eq(billGroupId))
                .orderBy(ci.DEFAULT_ORDER)
                .fetch()
                .map(r -> {
                    BillItemDTO dto = new BillItemDTO();
                    dto.setBillItemId(r.getValue(rule.ID));
                    dto.setBillItemName(r.getValue(rule.CHARGING_ITEM_NAME));
                    billRuleName[0] = r.getValue(bg.NAME);
                    list.add(dto);
                    return null;});
        response.setBillGroupId(billGroupId);
        response.setBillGroupName(billRuleName[0]);
        response.setBillItemDTOList(list);
        return response;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailByDateStr(Long ownerId, String ownerType, Long targetId, String targetType, String dateStr) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        List<ShowBillDetailForClientDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        try {
            dslContext.select(t.AMOUNT_RECEIVABLE, t.CHARGING_ITEM_NAME, t.DATE_STR, t.AMOUNT_OWED, t.AMOUNT_RECEIVABLE)
                    .from(t)
                    .where(t.OWNER_TYPE.eq(ownerType))
                    .and(t.OWNER_ID.eq(ownerId))
                    .and(t.TARGET_TYPE.eq(targetType))
                    .and(t.TARGET_ID.eq(targetId))
                    .and(t.DATE_STR.eq(dateStr))
                    .fetch()
                    .map(r -> {
                        ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                        dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                        dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                        dtos.add(dto);
                        amountOwed[0] = amountOwed[0].add(r.getValue(t.AMOUNT_OWED));
                        amountReceivable[0] = amountReceivable[0].add(r.getValue(t.AMOUNT_RECEIVABLE));
                        return null;
                    });
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        response.setAmountReceivable(amountReceivable[0]);
        response.setAmountOwed(amountOwed[0]);
        response.setDatestr(dateStr);
        response.setShowBillDetailForClientDTOList(dtos);
        return response;
    }

    @Override
    public ListBillsDTO creatPropertyBill(Long addressId, BillGroupDTO billGroupDTO,String dateStr, Byte isSettled, String noticeTel, Long ownerId, String ownerType, String targetName,Long targetId,String targetType,String buildingName,String apartmentName) {
        final ListBillsDTO[] response = {new ListBillsDTO()};
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            //普通信息卸载

            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();

            //需要组装的信息
            BigDecimal amountExemption = new BigDecimal("0");
            BigDecimal amountSupplement = new BigDecimal("0");
            BigDecimal amountReceivable = new BigDecimal("0");
            BigDecimal amountOwed = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");

            long nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
            if(nextBillId == 0){
                nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
            }

            if(list2!=null) {
                //bill exemption
                List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
                long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_EXEMPTION_ITEMS.getClass()), list2.size());
                long currentExemItemSeq = nextExemItemBlock - list2.size() + 1;
                if(currentExemItemSeq == 0){
                    currentExemItemSeq = currentExemItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_EXEMPTION_ITEMS.getClass()));
                }
                for(int i = 0; i < list2.size(); i++){
                    ExemptionItemDTO exemptionItemDTO = list2.get(i);
                    PaymentExemptionItems exemptionItem = new PaymentExemptionItems();
                    BigDecimal amount = exemptionItemDTO.getAmount();
                    if(amount == null){
                        continue;
                    }
                    exemptionItem.setAmount(amount);
                    exemptionItem.setBillGroupId(billGroupId);
                    exemptionItem.setBillId(nextBillId);
                    exemptionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    exemptionItem.setCreatorUid(UserContext.currentUserId());
                    exemptionItem.setId(currentExemItemSeq);
                    currentExemItemSeq += 1;
                    exemptionItem.setRemarks(exemptionItemDTO.getRemark());
                    if(targetType!=null){
                        exemptionItem.setTargetType(targetType);
                    }
                    if(targetId != null) {
                        exemptionItem.setTargetId(targetId);
                    }
                    exemptionItem.setTargetname(targetName);
                    exemptionItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                    exemptionItems.add(exemptionItem);

                    if(amount.compareTo(zero)==-1){
                        amount = amount.multiply(new BigDecimal("-1"));
                        amountExemption = amountExemption.add(amount);
                    }else if(amount.compareTo(zero)==1){
                        amountSupplement = amountSupplement.add(amount);
                    }
                }
                //应收是否应该计算减免项
                amountReceivable = amountReceivable.subtract(amountExemption);
                amountReceivable = amountReceivable.add(amountSupplement);
                amountOwed = amountOwed.subtract(amountExemption);
                amountOwed = amountOwed.add(amountSupplement);
                EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
                exemptionItemsDao.insert(exemptionItems);
            }
            Byte billStatus = 0;
            if(list1!=null){
                //billItems assemble
                List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
                long nextBillItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()), list1.size());
                long currentBillItemSeq = nextBillItemBlock - list1.size() + 1;
                if(currentBillItemSeq == 0){
                    currentBillItemSeq = currentBillItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()));
                }

                for(int i = 0; i < list1.size() ; i++) {
                    BillItemDTO dto = list1.get(i);
                    PaymentBillItems item = new PaymentBillItems();
                    item.setAddressId(addressId);
                    BigDecimal var1 = dto.getAmountReceivable();
                    //减免项不覆盖收费项目的收付，暂时
                    if(var1==null){
                        var1 = new BigDecimal("0");
                    }
                    item.setAmountOwed(var1);
                    item.setAmountReceivable(var1);
                    item.setAmountReceived(new BigDecimal("0"));
                    item.setBillGroupId(billGroupId);
                    item.setBillId(nextBillId);
                    item.setChargingItemName(dto.getBillItemName());
                    item.setChargingItemsId(dto.getBillItemId());
                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setCreatorUid(UserContext.currentUserId());
                    item.setDateStr(dateStr);
                    item.setId(currentBillItemSeq);
                    currentBillItemSeq += 1;
                    item.setNamespaceId(UserContext.getCurrentNamespaceId());
                    item.setOwnerType(ownerType);
                    item.setOwnerId(ownerId);
                    if(targetType!=null){
                        item.setTargetType(targetType);
                    }
                    if(targetId != null) {
                        item.setTargetId(targetId);
                    }
                    item.setTargetName(targetName);
                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    billItemsList.add(item);

                    amountReceivable = amountReceivable.add(var1);
                    amountOwed = amountOwed.add(var1);
                }

                if(amountOwed.compareTo(new BigDecimal("0"))!=1){
                    billStatus = 1;
                }
                for(int i = 0; i < billItemsList.size(); i++) {
                    billItemsList.get(i).setStatus(billStatus);
                }
                EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
                billItemsDao.insert(billItemsList);
            }


            com.everhomes.server.schema.tables.pojos.EhPaymentBills newBill = new PaymentBills();
            //  缺少创造者信息，先保存在其他地方，比如持久化日志

            newBill.setAddressId(addressId);
            newBill.setBuildingName(buildingName);
            newBill.setApartmentName(apartmentName);
            newBill.setAmountOwed(amountOwed);
            newBill.setAmountReceivable(amountReceivable);
            newBill.setAmountReceived(zero);
            newBill.setAmountSupplement(amountSupplement);
            newBill.setAmountExemption(amountExemption);
            newBill.setBillGroupId(billGroupId);
            newBill.setDateStr(dateStr);
            newBill.setId(nextBillId);
            newBill.setNamespaceId(UserContext.getCurrentNamespaceId());
            newBill.setNoticetel(noticeTel);
            newBill.setOwnerId(ownerId);
            newBill.setTargetName(targetName);
            newBill.setOwnerType(ownerType);
            newBill.setTargetType(targetType);
            newBill.setTargetId(targetId);
            newBill.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            newBill.setCreatorId(UserContext.currentUserId());
            newBill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            newBill.setNoticeTimes(0);
            newBill.setStatus(billStatus);
            newBill.setSwitch(isSettled);
            EhPaymentBillsDao billsDao = new EhPaymentBillsDao(context.configuration());
            billsDao.insert(newBill);
            response[0] = ConvertHelper.convert(newBill, ListBillsDTO.class);
            response[0].setBillGroupName(billGroupDTO.getBillGroupName());
            response[0].setBillId(nextBillId);
            response[0].setNoticeTel(noticeTel);
            response[0].setBillStatus(billStatus);
            response[0].setTargetType(targetType);
            response[0].setTargetId(targetId);
            return null;
        });
        return response[0];
    }

    @Override
    public ListBillDetailVO listBillDetail(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentExemptionItems t = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t");
        EhPaymentChargingItems k = Tables.EH_PAYMENT_CHARGING_ITEMS.as("k");
        ListBillDetailVO vo = new ListBillDetailVO();
        BillGroupDTO dto = new BillGroupDTO();
        List<BillItemDTO> list1 = new ArrayList<>();
        List<ExemptionItemDTO> list2 = new ArrayList<>();

        context.select(r.ID,r.TARGET_ID,r.NOTICETEL,r.DATE_STR,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.BUILDING_NAME,r.APARTMENT_NAME)
                .from(r)
                .where(r.ID.eq(billId))
                .fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    vo.setNoticeTel(f.getValue(r.NOTICETEL));
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setTargetName(f.getValue(r.TARGET_NAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
                    vo.setBuildingName(f.getValue(r.BUILDING_NAME));
                    vo.setApartmentName(f.getValue(r.APARTMENT_NAME));
                    return null;
                });
        context.select(o.CHARGING_ITEM_NAME,o.ID,o.AMOUNT_RECEIVABLE)
                .from(o)
                .leftOuterJoin(k)
                .on(o.CHARGING_ITEMS_ID.eq(k.ID))
                .where(o.BILL_ID.eq(billId))
                .orderBy(k.DEFAULT_ORDER)
                .fetch()
                .map(f -> {
                    BillItemDTO itemDTO = new BillItemDTO();
                    itemDTO.setBillItemName(f.getValue(o.CHARGING_ITEM_NAME));
                    itemDTO.setBillItemId(f.getValue(o.ID));
                    itemDTO.setAmountReceivable(f.getValue(o.AMOUNT_RECEIVABLE));
                    list1.add(itemDTO);
                    return null;
                });
        context.select()
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .fetch()
                .map(f -> {
                    ExemptionItemDTO exemDto = new ExemptionItemDTO();
                    exemDto.setAmount(f.getValue(t.AMOUNT));
                    exemDto.setExemptionId(f.getValue(t.ID));
                    exemDto.setRemark(f.getValue(t.REMARKS));
                    list2.add(exemDto);
                    return null;
                });

        dto.setBillItemDTOList(list1);
        dto.setExemptionItemDTOList(list2);
        vo.setBillGroupDTO(dto);
        return vo;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByDateStrs(String beginLimit, String endLimit, Long ownerId, String ownerType) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(r);
        query.addSelect(DSL.sum(r.AMOUNT_RECEIVABLE),DSL.sum(r.AMOUNT_RECEIVED),DSL.sum(r.AMOUNT_OWED),r.DATE_STR);
        if(beginLimit!=null) {
            query.addConditions(r.DATE_STR.greaterOrEqual(beginLimit));
        }
        if(endLimit!=null) {
            query.addConditions(r.DATE_STR.lessOrEqual(endLimit));
        }
        query.addConditions(r.OWNER_ID.eq(ownerId));
        query.addConditions(r.OWNER_TYPE.eq(ownerType));
        query.addGroupBy(r.DATE_STR);
        query.addOrderBy(r.DATE_STR);
        query.fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(r.AMOUNT_OWED)));
                    dto.setAmountReceivable(f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE)));
                    dto.setAmountReceived(f.getValue(DSL.sum(r.AMOUNT_RECEIVED)));
                    dto.setValueOfX(f.getValue(r.DATE_STR));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.select(DSL.sum(o.AMOUNT_RECEIVABLE),DSL.sum(o.AMOUNT_RECEIVED),DSL.sum(o.AMOUNT_OWED),o.CHARGING_ITEM_NAME)
                .from(o,t)
                .where(o.OWNER_TYPE.eq(ownerType))
                .and(o.OWNER_ID.eq(ownerId))
                .and(o.CHARGING_ITEMS_ID.eq(t.ID))
                .groupBy(o.CHARGING_ITEMS_ID)
                .orderBy(t.DEFAULT_ORDER)
                .fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(o.AMOUNT_OWED)));
                    dto.setAmountReceivable(f.getValue(DSL.sum(o.AMOUNT_RECEIVABLE)));
                    dto.setAmountReceived(f.getValue(DSL.sum(o.AMOUNT_RECEIVED)));
                    dto.setValueOfX(f.getValue(o.CHARGING_ITEM_NAME));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByCommunities(Integer currentNamespaceId) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        EhCommunities o = Tables.EH_COMMUNITIES.as("o");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.select(DSL.sum(r.AMOUNT_RECEIVABLE),DSL.sum(r.AMOUNT_RECEIVED),DSL.sum(r.AMOUNT_OWED),o.NAME)
                .from(r)
                .leftOuterJoin(o)
                .on(r.NAMESPACE_ID.eq(currentNamespaceId))
                .and(r.OWNER_ID.eq(o.ID))
                .groupBy(r.OWNER_ID,r.OWNER_TYPE)
                .fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(r.AMOUNT_OWED)));
                    dto.setAmountReceivable(f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE)));
                    dto.setAmountReceived(f.getValue(DSL.sum(r.AMOUNT_RECEIVABLE)));
                    dto.setValueOfX(f.getValue(o.NAME));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public void modifyBillStatus(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //更新 状态，更新 金钱数目，不可逆
        EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
        EhPaymentBillItems item = Tables.EH_PAYMENT_BILL_ITEMS.as("item");
        //BILL
        context.update(bill)
                .set(bill.STATUS,(byte)1)
                .set(bill.AMOUNT_RECEIVED,bill.AMOUNT_RECEIVABLE)
                .set(bill.AMOUNT_OWED,new BigDecimal("0"))
                .where(bill.ID.eq(billId))
                .execute();
        //bill item
        context.update(item)
                .set(item.STATUS,(byte)1)
                .set(item.AMOUNT_RECEIVED,item.AMOUNT_RECEIVABLE)
                .set(item.AMOUNT_OWED,new BigDecimal("0"))
                .where(item.BILL_ID.eq(billId))
                .execute();
        //bill exemption已经减到bill中了
    }

    @Override
    public List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId) {
        List<ListChargingItemsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID);
        query.addSelect(t.NAME);
        query.addFrom(t);
        query.addFrom(t1);
        query.addConditions(t1.OWNER_ID.eq(ownerId));
        query.addConditions(t1.OWNER_TYPE.eq(ownerType));
        query.addConditions(t1.CHARGING_ITEM_ID.eq(t.ID));
        query.fetch().map(r -> {
            ListChargingItemsDTO dto = new ListChargingItemsDTO();
            dto.setChargingItemId(r.getValue(t.ID));
            dto.setChargingItemName(r.getValue(t.NAME));
            list.add(dto);
            return null;
        });
        return list;
    }

    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId) {
        List<ListChargingStandardsDTO> list = new ArrayList<>();
        List<String> variableInjectionJson = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
        EhPaymentVariables t2 = Tables.EH_PAYMENT_VARIABLES.as("t2");
        EhPaymentBillGroupsRules t3 = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t3");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.BILLING_CYCLE,t.ID,t.NAME,t.FORMULA,t3.VARIABLES_JSON_STRING,t.FORMULA_TYPE);
        query.addFrom(t,t1,t3);
        query.addConditions(t.CHARGING_ITEMS_ID.eq(chargingItemId));
        query.addConditions(t1.CHARGING_STANDARD_ID.eq(t.ID));
        query.addConditions(t3.CHARGING_STANDARDS_ID.eq(t.ID));
        query.addConditions(t1.OWNER_ID.eq(ownerId));
        query.addConditions(t1.OWNER_TYPE.eq(ownerType));
        query.fetch().map(r -> {
            ListChargingStandardsDTO dto = new ListChargingStandardsDTO();
            dto.setBillingCycle(r.getValue(t.BILLING_CYCLE));
            Long chargingStandardId = r.getValue(t.ID);
            dto.setChargingStandardId(chargingStandardId);
            dto.setChargingStandardName(r.getValue(t.NAME));
            dto.setFormula(r.getValue(t.FORMULA));
            dto.setFormulaType(r.getValue(t.FORMULA_TYPE));
            variableInjectionJson.add(r.getValue(t3.VARIABLES_JSON_STRING));
            list.add(dto);
            return null;
        });
        for(int i = 0; i < list.size(); i++) {
            String json = variableInjectionJson.get(i);
            ListChargingStandardsDTO dto = list.get(i);
            Gson gson = new Gson();
            List<VariableIdAndValue> idAndValues = new ArrayList<>();
            Map<String,String> map = gson.fromJson(json, Map.class);
            for(Map.Entry<?,?> entry : map.entrySet()){
                VariableIdAndValue var = new VariableIdAndValue();
                var.setVariableId(entry.getKey());
                var.setVariableValue(entry.getValue());
                idAndValues.add(var);
            }
            List<PaymentVariable> variables = new ArrayList<>();
            for(int j = 0; j < idAndValues.size(); j++) {
                PaymentVariable variable = new PaymentVariable();
                VariableIdAndValue idAndValue = idAndValues.get(j);
                String variableIdentifier = ((String)idAndValue.getVariableId());
                double variableValueDouble = Double.parseDouble((String)idAndValue.getVariableValue());
                BigDecimal variableValue = new BigDecimal(variableValueDouble);
                variableValue = variableValue.setScale(2);
                String variableName = context.select(t2.NAME)
                        .from(t2)
                        .where(t2.IDENTIFIER.eq(variableIdentifier))
                        .fetchOne(0, String.class);
                variable.setVariableIdentifier(variableIdentifier);
                variable.setVariableName(variableName);
                variable.setVariableValue(variableValue);
                variables.add(variable);
            }
            list.get(i).setVariables(variables);
        }
        return list;
    }

    @Override
    public void modifyNotSettledBill(Long billId, BillGroupDTO billGroupDTO,String targetType,Long targetId,String targetName) {
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
            EhPaymentBillItems t1 = Tables.EH_PAYMENT_BILL_ITEMS.as("t1");
            EhPaymentExemptionItems t2 = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t2");
            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();

            //需要组装的信息
            BigDecimal amountExemption = new BigDecimal("0");
            BigDecimal amountSupplement = new BigDecimal("0");
            BigDecimal amountReceivable = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");


            for(int i = 0; i < list1.size() ; i++) {
                BillItemDTO dto = list1.get(i);
                context.update(t1)
                        .set(t1.AMOUNT_RECEIVABLE,dto.getAmountReceivable())
                        .set(t1.AMOUNT_OWED,dto.getAmountReceivable())
                        .set(t1.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                        .set(t1.OPERATOR_UID,UserContext.currentUserId())
                        .where(t1.BILL_ID.in(billId))
                        .and(t1.ID.eq(dto.getBillItemId()))
                        .execute();
                amountReceivable = amountReceivable.add(dto.getAmountReceivable());
            }
            //bill exemption
            List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
            for(int i = 0; i < list2.size(); i++){
                ExemptionItemDTO exemptionItemDTO = list2.get(i);
                if(exemptionItemDTO.getExemptionId()!=null){
                    context.update(t2)
                            .set(t2.AMOUNT,exemptionItemDTO.getAmount())
                            .set(t2.REMARKS,exemptionItemDTO.getRemark())
                            .set(t2.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                            .set(t2.OPERATOR_UID,UserContext.currentUserId())
                            .where(t2.BILL_ID.eq(billId))
                            .and(t2.ID.eq(exemptionItemDTO.getExemptionId()))
                            .execute();
                }else{
                    long nextId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_EXEMPTION_ITEMS.getClass()));
                    PaymentExemptionItems exemptionItem = new PaymentExemptionItems();
                    BigDecimal amount = exemptionItemDTO.getAmount();
                    exemptionItem.setAmount(amount);
                    exemptionItem.setBillGroupId(billGroupId);
                    exemptionItem.setBillId(billId);
                    exemptionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    exemptionItem.setCreatorUid(UserContext.currentUserId());
                    exemptionItem.setId(nextId);
                    exemptionItem.setRemarks(exemptionItemDTO.getRemark());
                    exemptionItem.setTargetType(targetType);
                    exemptionItem.setTargetId(targetId);
                    exemptionItem.setTargetname(targetName);
                    exemptionItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    exemptionItems.add(exemptionItem);
                }

                if(exemptionItemDTO.getAmount().compareTo(zero)==-1){
                    amountExemption = amountExemption.add(exemptionItemDTO.getAmount());
                }else if(exemptionItemDTO.getAmount().compareTo(zero)==1){
                    amountSupplement = amountSupplement.add(exemptionItemDTO.getAmount());
                }
            }
            EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
            exemptionItemsDao.insert(exemptionItems);

            //  缺少创造者信息，先保存在其他地方，比如持久化日志
            amountReceivable = amountReceivable.add(amountExemption);
            amountReceivable = amountReceivable.add(amountSupplement);
            context.update(t)
                    .set(t.AMOUNT_RECEIVABLE,amountReceivable)
                    .set(t.AMOUNT_OWED,amountReceivable)
                    .set(t.AMOUNT_SUPPLEMENT,amountSupplement)
                    .set(t.AMOUNT_EXEMPTION,amountExemption)
                    .set(t.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                    .set(t.OPERATOR_UID,UserContext.currentUserId())
                    .where(t.ID.eq(billId))
                    .execute();
            return null;
        });
    }

    @Override
    public List<ListBillExemptionItemsDTO> listBillExemptionItems(Long billId, int pageOffSet, Integer pageSize, String dateStr, String targetName) {
        List<ListBillExemptionItemsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentExemptionItems t = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t");
        EhPaymentBills t1 = Tables.EH_PAYMENT_BILLS.as("t1");
        dateStr = context.select(t1.DATE_STR)
                .from(t1)
                .where(t1.ID.eq(billId))
                .fetchOne(0,String.class);
        String finalDateStr = dateStr;
        context.select(t.AMOUNT,t.ID,t.REMARKS)
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .fetch()
                .map(r -> {
                    ListBillExemptionItemsDTO dto = new ListBillExemptionItemsDTO();
                    dto.setAmount(r.getValue(t.AMOUNT));
                    dto.setDateStr(finalDateStr);
                    dto.setExemptionId(r.getValue(t.ID));
                    dto.setRemark(r.getValue(t.REMARKS));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public void deleteBill(Long billId) {
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            int execute = context.delete(Tables.EH_PAYMENT_BILLS)
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .and(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte) 0))
                    .execute();
            if(execute == 0){
                throw new RuntimeException("删除账单失败，账单已出或者无法找到此账单");
            }
            context.delete(Tables.EH_PAYMENT_BILL_ITEMS)
                    .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                    .execute();
            context.delete(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                    .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.BILL_ID.eq(billId))
                    .execute();
            return null;
        });

    }

    @Override
    public void deleteBillItem(Long billItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.ID.eq(billItemId))
                .execute();
    }

    @Override
    public void deletExemptionItem(Long exemptionItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.ID.eq(exemptionItemId))
                .execute();
    }

    @Override
    public String findFormulaByChargingStandardId(Long chargingStandardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS.FORMULA_JSON)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOne(0,String.class);
    }

    @Override
    public String findChargingItemNameById(Long chargingItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME)
                .from(Tables.EH_PAYMENT_CHARGING_ITEMS)
                .where(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(chargingItemId))
                .fetchOne(0,String.class);
    }

    @Override
    public void saveContractVariables(List<com.everhomes.server.schema.tables.pojos.EhPaymentContractReceiver> contractDateList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentContractReceiverDao dao = new EhPaymentContractReceiverDao(context.configuration());
        dao.insert(contractDateList);
    }

    @Override
    public List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<VariableIdAndValue> list = new ArrayList<>();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        String variableJson = context.select(t.VARIABLES_JSON_STRING)
                .from(t)
                .where(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
                .fetchOne(0, String.class);
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(variableJson, Map.class);
        for(Map.Entry entry : map.entrySet()){
            VariableIdAndValue vid = new VariableIdAndValue();
            vid.setVariableValue(entry.getValue());
            vid.setVariableId(entry.getKey());
            list.add(vid);
        }
        return list;
    }

    @Override
    public void increaseNoticeTime(List<Long> billIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.NOTICE_TIMES,Tables.EH_PAYMENT_BILLS.NOTICE_TIMES.add(1))
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .execute();
    }

    @Override
    public List<PaymentContractReceiver> findContractReceiverByContractNumAndTimeLimit(String contractNum) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_PAYMENT_CONTRACT_RECEIVER)
                .where(Tables.EH_PAYMENT_CONTRACT_RECEIVER.CONTRACT_NUM.eq(contractNum))
                .fetch().map(r -> ConvertHelper.convert(r, PaymentContractReceiver.class));
    }

    @Override
    public String getStandardNameById(Long chargingStandardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS.NAME)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOne(0,String.class);
    }

    @Override
    public List<Object> getBillDayAndCycleByChargingItemId(Long chargingStandardId,Long chargingItemId,String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        final Integer[] billDay = new Integer[1];
        final Byte[] balanceType = new Byte[1];
        context.select(t1.BILLS_DAY,t1.BALANCE_DATE_TYPE)
                .from(t,t1)
                .where(t1.ID.eq(t.BILL_GROUP_ID))
                .and(t.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
                .and(t.OWNERID.eq(ownerId))
                .and(t.OWNERTYPE.eq(ownerType))
                .fetch()
                .map(r -> {
                    billDay[0] = r.getValue(t1.BILLS_DAY);
                    balanceType[0] = r.getValue(t1.BALANCE_DATE_TYPE);
                    return null;
                });
        List<Object> list = new ArrayList<>();
        list.add(billDay[0]);
        list.add(balanceType[0]);
        return list;
    }

    @Override
    public PaymentBillGroupRule getBillGroupRule(Long chargingStandardId, Long chargingStandardId1, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        List<PaymentBillGroupRule> rules = context.select()
                .from(t)
                .fetch()
                .map(r -> ConvertHelper.convert(r, PaymentBillGroupRule.class));
        return rules.get(0);
    }

    @Override
    public void saveBillItems(List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillItemsDao dao = new EhPaymentBillItemsDao(context.configuration());
        dao.insert(billItemsList);
    }

    @Override
    public void saveBills(List<com.everhomes.server.schema.tables.pojos.EhPaymentBills> billList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillsDao dao = new EhPaymentBillsDao(context.configuration());
        dao.insert(billList);
    }

    @Override
    public Byte findBillyCycleById(Long chargingStandardId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS.BILLING_CYCLE)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOne(0,Byte.class);
    }

    @Override
    public void changeBillStatusOnContractSaved(String contractNum) {
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentContractReceiver t1 = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t1");
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            context.update(Tables.EH_PAYMENT_BILLS)
                    .set(t.SWITCH,(byte)0)
                    .where(t.CONTRACT_NUM.eq(contractNum))
                    .and(t.SWITCH.eq((byte)3))
                    .execute();
            context.update(t1)
                    .set(t1.STATUS,(byte)1)
                    .where(t1.CONTRACT_NUM.eq(contractNum))
                    .execute();
            return null;
        });
    }

    @Override
    public void deleteContractPayment(String contractNum) {
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentContractReceiver t1 = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t1");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            List<Long> billIds = context.select(t.ID)
                    .where(t.CONTRACT_NUM.eq(contractNum))
                    .and(t.SWITCH.eq((byte) 3))
                    .fetch(t.ID);
            context.delete(t)
                    .where(t.ID.in(billIds))
                    .execute();
            context.delete(t2)
                    .where(t2.BILL_ID.in(billIds))
                    .execute();
            context.delete(t1)
                    .where(t1.CONTRACT_NUM.eq(contractNum))
                    .execute();
            return null;
        });
    }

    @Override
    public List<PaymentExpectancyDTO> listBillExpectanciesOnContract(String contractNum, Integer pageOffset, Integer pageSize) {
        List<PaymentExpectancyDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        context.select(t.DATE_STR,t.PROPERTY_IDENTIFER,t.DATA_STR_END,t.DATE_STR_DUE,t.CHARGING_ITEM_NAME,t.AMOUNT_RECEIVABLE)
                .from(t)
                .where(t.CONTRACT_NUM.eq(contractNum))
                .limit(pageOffset,pageSize+1)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
                    dto.setDateStrEnd(r.getValue(t.DATE_STR));
                    dto.setPropertyIdentifier(r.getValue(t.PROPERTY_IDENTIFER));
                    dto.setDueDateStr(r.getValue(t.DATA_STR_END));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_DUE));
                    dto.setChargingItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dtos.add(dto);
                    return null;
                });
        return dtos;
    }

}
