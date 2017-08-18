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
import com.everhomes.server.schema.tables.EhPaymentBillGroups;
import com.everhomes.server.schema.tables.EhPaymentBillGroupsRules;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.EhPaymentChargingItems;
import com.everhomes.server.schema.tables.EhPaymentExemptionItems;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import com.mysql.jdbc.StringUtils;
import freemarker.core.ArithmeticEngine;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ListBillsDTO> listBills(Integer currentNamespaceId, Long ownerId, String ownerType, String addressName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status) {
        List<ListBillsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<EhPaymentBillsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        query.addConditions(t.SWITCH.eq(status));
        if (addressId != null) {
            Map<Long,String> IdAndTypes = new HashMap<>();
            context.select(Tables.EH_USERS.ID).from(Tables.EH_USERS).where(Tables.EH_USERS.ADDRESS_ID.eq(addressId)).fetch().map(r -> {
                IdAndTypes.put(r.getValue(Tables.EH_USERS.ID),"individual");
                return null;});
            context.select(Tables.EH_ORGANIZATIONS.ID).from(Tables.EH_ORGANIZATIONS).where(Tables.EH_ORGANIZATIONS.ADDRESS_ID.eq(addressId)).fetch().map(r -> {
                IdAndTypes.put(r.getValue(Tables.EH_USERS.ID),"organization");
                return null;
            });
            for(Map.Entry<Long,String> entry : IdAndTypes.entrySet()){
                query.addConditions(t.OWNER_TYPE.eq(entry.getValue()).and(t.OWNER_ID.eq(entry.getKey())));
            }
        }
        if(billGroupId!=null) {
            query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        }
        if(billStatus!=null) {
            query.addConditions(t.STATUS.eq(billStatus));
        }
        if(targetName!=null) {
            query.addConditions(t.TARGETNAME.like("%"+targetName+"%"));
        }
        query.addOrderBy(t.DATE_STR.asc());
        query.addGroupBy(t.TARGETNAME);
        query.addLimit(pageOffSet,pageSize+1);

        List<Object[]> billAddresses = new ArrayList<>();
        query.fetch().map(r -> {
            ListBillsDTO dto = new ListBillsDTO();
            if(addressName!=null){
                dto.setAddressName(addressName);
            }else{
                Object[] billAddress = new Object[2];
                billAddress[0] = r.getValue(t.TARGET_TYPE);
                billAddress[1] = r.getValue(t.TARGET_ID);
                billAddresses.add(billAddress);
            }
            dto.setAmountOwed(r.getAmountOwed());
            dto.setAmountReceivable(r.getAmountReceivable());
            dto.setAmountReceived(r.getAmountReceived());
            if(billGroupName!=null) {
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
            dto.setTargetName(r.getTargetname());
            dto.setTargetId(r.getTargetId());
            dto.setTargetType(r.getTargetType());
            dto.setOwnerId(r.getOwnerId());
            dto.setOwnerType(r.getOwnerType());
            list.add(dto);
            return null;});
        for(int i = 0; i < list.size(); i++) {
            ListBillsDTO dto = list.get(i);
            Object[] objs = billAddresses.get(i);
            String buildingApartmentName = "";
            try {
                String targetType = (String) objs[0];
                Long targetId = (Long) objs[1];
                if(targetType.equals("eh_user")){
                    buildingApartmentName = context.select(Tables.EH_ADDRESSES.ADDRESS).from(Tables.EH_USERS,Tables.EH_ADDRESSES).where(Tables.EH_USERS.ID.eq(targetId))
                            .and(Tables.EH_USERS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID)).fetchOne(0,String.class);
                }else if(targetType.equals("eh_organization")){
                    buildingApartmentName = context.select(Tables.EH_ADDRESSES.ADDRESS).from(Tables.EH_ORGANIZATIONS,Tables.EH_ADDRESSES).where(Tables.EH_ORGANIZATIONS.ID.eq(targetId))
                            .and(Tables.EH_ORGANIZATIONS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID)).fetchOne(0,String.class);
                }
            } catch (Exception e) {

            }
            dto.setAddressName(buildingApartmentName);
        }
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize) {
        List<BillDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        context.select(t.DATE_STR,t.CHARGING_ITEM_NAME,t.AMOUNT_RECEIVABLE,t.AMOUNT_RECEIVED,t.AMOUNT_OWED,t.STATUS)
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
            dtos.add(dto);
            return null;});
        return dtos;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<NoticeInfo> list = new ArrayList<>();
                dslContext.select(Tables.EH_PAYMENT_BILLS.NOTICETEL,Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILLS.AMOUNT_OWED,Tables.EH_APP_URLS.NAME,Tables.EH_PAYMENT_BILLS.TARGET_ID,Tables.EH_PAYMENT_BILLS.TARGET_TYPE,Tables.EH_PAYMENT_BILLS.TARGETNAME)
                .from(Tables.EH_PAYMENT_BILLS,Tables.EH_APP_URLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                        .and(Tables.EH_APP_URLS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetch().map(r -> {
                    NoticeInfo info = new NoticeInfo();
                    info.setPhoneNum(r.getValue(Tables.EH_PAYMENT_BILLS.NOTICETEL));
                    info.setAmountRecevable(r.getValue(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE));
                    info.setAmountOwed(r.getValue(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED));
                    info.setAppName(r.getValue(Tables.EH_APP_URLS.NAME));
                    info.setTargetId(r.getValue(Tables.EH_PAYMENT_BILLS.TARGET_ID));
                    info.setTargetType(r.getValue(Tables.EH_PAYMENT_BILLS.TARGET_TYPE));
                    info.setTargetName(r.getValue(Tables.EH_PAYMENT_BILLS.TARGETNAME));
                    list.add(info);
                    return null;});
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
    public void creatPropertyBill(List<AddressIdAndName> addressIdAndNames, BillGroupDTO billGroupDTO, String dateStr, Byte isSettled, String noticeTel, Long ownerId, String ownerType, String targetName,Long targetId,String targetType) {
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

            //普通信息卸载
            Long addressId = null;
            if(addressIdAndNames!=null && addressIdAndNames.size()>0){
                addressId = addressIdAndNames.get(0).getAddressId();
            }
            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();

            //需要组装的信息
            BigDecimal amountExemption = new BigDecimal("0");
            BigDecimal amountSupplement = new BigDecimal("0");
            BigDecimal amountReceivable = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");

            long nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILLS.getClass()));
            nextBillId = 20l;

            //billItems assemble
            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
            long nextBillItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_BILL_ITEMS.getClass()), billItemsList.size());
            long currentBillItemSeq = nextBillItemBlock - billItemsList.size() + 1;
            currentBillItemSeq=100l;
            for(int i = 0; i < list1.size() ; i++) {
                BillItemDTO dto = list1.get(i);
                PaymentBillItems item = new PaymentBillItems();
                item.setAddressid(addressId);
                BigDecimal var1 = dto.getAmountReceivable();
                item.setAmountOwed(var1);
                item.setAmountReceivable(dto.getAmountReceivable());
                item.setAmountReceived(new BigDecimal("0"));
                item.setBillGroupId(billGroupId);
                item.setBillId(nextBillId);
                item.setChargingItemName(dto.getBillItemName());
                item.setChargingItemsId(dto.getBillItemId());
                item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                item.setCreatorUid(UserContext.currentUserId());
                item.setDateStr(dateStr);
                item.setId(currentBillItemSeq++);
                item.setNamespaceId(UserContext.getCurrentNamespaceId());
                item.setOwnerType(ownerType);
                item.setOwnerId(ownerId);
                if(targetType!=null){
                    item.setTargetType(targetType);
                }
                if(targetId != null) {
                    item.setTargetId(targetId);
                }
                item.setTargetname(targetName);
                item.setStatus(isSettled);
                item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                billItemsList.add(item);

                amountReceivable = amountReceivable.add(var1);
            }
            EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
            billItemsDao.insert(billItemsList);

            //bill exemption
            List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
            long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_EXEMPTION_ITEMS.getClass()), exemptionItems.size());
            long currentExemItemSeq = nextExemItemBlock - exemptionItems.size() + 1;
            currentExemItemSeq = 100l;
            for(int i = 0; i < list2.size(); i++){
                ExemptionItemDTO exemptionItemDTO = list2.get(i);
                PaymentExemptionItems exemptionItem = new PaymentExemptionItems();
                BigDecimal amount = exemptionItemDTO.getAmount();
                exemptionItem.setAmount(amount);
                exemptionItem.setBillGroupId(billGroupId);
                exemptionItem.setBillId(nextBillId);
                exemptionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                exemptionItem.setCreatorUid(UserContext.currentUserId());
                exemptionItem.setId(currentExemItemSeq++);
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
                    amountExemption = amountExemption.add(amount);
                }else if(amount.compareTo(zero)==1){
                    amountSupplement = amountSupplement.add(amount);
                }
            }
            EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
            exemptionItemsDao.insert(exemptionItems);

            com.everhomes.server.schema.tables.pojos.EhPaymentBills newBill = new PaymentBills();
            //  缺少创造者信息，先保存在其他地方，比如持久化日志
            amountReceivable = amountReceivable.add(amountExemption);
            amountReceivable = amountReceivable.add(amountSupplement);
            newBill.setAmountExemption(amountExemption);
            newBill.setAmountOwed(amountReceivable);
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
            newBill.setOwnerType(ownerType);
            newBill.setTargetType(targetType);
            newBill.setTargetId(targetId);
            newBill.setNoticeTimes(0);
            newBill.setStatus((byte)0);
            newBill.setSwitch(isSettled);
            EhPaymentBillsDao billsDao = new EhPaymentBillsDao(context.configuration());
            billsDao.insert(newBill);
            return null;
        });

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
        context.select()
                .from(r,o,t,k)
                .where(r.ID.eq(billId))
                .and(o.BILL_ID.eq(r.ID))
                .and(t.BILL_ID.eq(r.ID))
                .and(k.ID.in(o.CHARGING_ITEMS_ID))
                .orderBy(k.DEFAULT_ORDER)
                .fetch()
                //batch DML操作用lambda这种方式会慢，这里是少量数据多连表DQL操作
                .map(f -> {
                    BillItemDTO itemDTO = new BillItemDTO();
                    ExemptionItemDTO exemDto = new ExemptionItemDTO();
                    itemDTO.setBillItemName(f.getValue(o.CHARGING_ITEM_NAME));
                    itemDTO.setBillItemId(f.getValue(o.ID));
                    itemDTO.setAmountReceivable(f.getValue(o.AMOUNT_RECEIVABLE));
                    list1.add(itemDTO);
                    exemDto.setAmount(f.getValue(t.AMOUNT));
                    exemDto.setExemptionId(f.getValue(t.ID));
                    exemDto.setRemark(f.getValue(t.REMARKS));
                    list2.add(exemDto);
                    vo.setTargetId(f.getValue(r.ID));
                    vo.setNoticeTel(f.getValue(r.NOTICETEL));
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setTargetName(f.getValue(r.TARGETNAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
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
        context.select(r.AMOUNT_RECEIVABLE,r.AMOUNT_RECEIVED,r.AMOUNT_OWED)
                .from(r)
                .where(r.OWNER_TYPE.eq(ownerType))
                .and(r.OWNER_ID.eq(ownerId))
                .and(r.DATE_STR.greaterOrEqual(beginLimit))
                .and(r.DATE_STR.lessOrEqual(endLimit))
                .orderBy(r.DATE_STR)
                .fetchOne()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(r.AMOUNT_OWED));
                    dto.setAmountReceivable(f.getValue(r.AMOUNT_RECEIVABLE));
                    dto.setAmountReceived(f.getValue(r.AMOUNT_RECEIVED));
                    dto.setValueOfX(f.getValue(r.DATE_STR));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.select(r.AMOUNT_RECEIVABLE,r.AMOUNT_RECEIVED,r.AMOUNT_OWED)
                .from(r)
                .where(r.OWNER_TYPE.eq(ownerType))
                .and(r.OWNER_ID.eq(ownerId))
//                .and(r.DATE_STR.greaterOrEqual(beginLimit))
//                .and(r.DATE_STR.lessOrEqual(endLimit))
                .orderBy(r.DATE_STR)
                .fetchOne()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(r.AMOUNT_OWED));
                    dto.setAmountReceivable(f.getValue(r.AMOUNT_RECEIVABLE));
                    dto.setAmountReceived(f.getValue(r.AMOUNT_RECEIVED));
                    dto.setValueOfX(f.getValue(r.DATE_STR));
                    list.add(dto);
                    return null;
                });
        return list;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByCommunities(Integer currentNamespaceId) {
        return null;
    }
}
