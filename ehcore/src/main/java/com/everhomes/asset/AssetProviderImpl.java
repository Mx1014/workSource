package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentUser;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.OrderType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.EhAssetPaymentOrder;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.EhOrganizations;
import com.everhomes.server.schema.tables.EhPaymentAccounts;
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
import com.everhomes.server.schema.tables.EhPaymentUsers;
import com.everhomes.server.schema.tables.EhPaymentVariables;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;

import com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrderBills;

import com.everhomes.server.schema.tables.pojos.EhPaymentFormula;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import freemarker.core.ArithmeticEngine;
import freemarker.core.ReturnInstruction;
import org.apache.log4j.spi.ErrorCode;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import scala.Char;

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
    public List<ListBillsDTO> listBills(String contractNum,Integer currentNamespaceId, Long ownerId, String ownerType,  String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName, Byte status,String targetType) {
        List<ListBillsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<EhPaymentBillsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        if(!org.springframework.util.StringUtils.isEmpty(status)){
            query.addConditions(t.SWITCH.eq(status));
        }
        if(!org.springframework.util.StringUtils.isEmpty(billGroupId)) {
            query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(billStatus)) {
            query.addConditions(t.STATUS.eq(billStatus));
        }
        if(!org.springframework.util.StringUtils.isEmpty(targetName)) {
            query.addConditions(t.TARGET_NAME.like("%"+targetName+"%"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(targetType)){
            query.addConditions(t.TARGET_TYPE.eq(targetType));
        }
        if(!org.springframework.util.StringUtils.isEmpty(contractNum)){
            query.addConditions(t.CONTRACT_NUM.eq(contractNum));
        }
        if(status!=null && status == 1){
            query.addOrderBy(t.STATUS);
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)){
            query.addConditions(t.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)){
            query.addConditions(t.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addOrderBy(t.DATE_STR.desc());
//        query.addGroupBy(t.TARGET_NAME);
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(r -> {
            ListBillsDTO dto = new ListBillsDTO();
            dto.setBuildingName(r.getBuildingName());
            dto.setApartmentName(r.getApartmentName());
            dto.setAmountOwed(r.getAmountOwed());
            dto.setAmountReceivable(r.getAmountReceivable());
            dto.setAmountReceived(r.getAmountReceived());
            if(!org.springframework.util.StringUtils.isEmpty(billGroupName)) {
                dto.setBillGroupName(billGroupName);
            }else{
                String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS).where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(r.getValue(t.BILL_GROUP_ID))).fetchOne(0,String.class);
                dto.setBillGroupName(billGroupNameFound);
            }
            dto.setBillId(String.valueOf(r.getValue(t.ID)));
            dto.setBillStatus(r.getValue(t.STATUS));
            dto.setNoticeTel(r.getValue(t.NOTICETEL));
            dto.setNoticeTimes(r.getNoticeTimes());
            dto.setDateStr(r.getDateStr());
            dto.setTargetName(r.getTargetName());
            dto.setTargetId(String.valueOf(r.getTargetId()));
            dto.setTargetType(r.getTargetType());
            dto.setOwnerId(String.valueOf(r.getOwnerId()));
            dto.setOwnerType(r.getOwnerType());
            dto.setContractNum(r.getContractNum());
            dto.setContractId(String.valueOf(r.getContractId()));
            list.add(dto);
            return null;});
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize) {
        List<BillDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        EhAddresses t2 = Tables.EH_ADDRESSES.as("t2");
        context.select(t.DATE_STR,t.CHARGING_ITEM_NAME,t.AMOUNT_RECEIVABLE,t.AMOUNT_RECEIVED,t.AMOUNT_OWED,t.STATUS,t.ID,t2.APARTMENT_NAME,t2.BUILDING_NAME)
                .from(t)
                .leftOuterJoin(t1)
                .on(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .leftOuterJoin(t2)
                .on(t.ADDRESS_ID.eq(t2.ID))
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
            dto.setApartmentName(r.getValue(t2.APARTMENT_NAME));
            dto.setBuildingName(r.getValue(t2.BUILDING_NAME));
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
                    info.setPhoneNums(r.getValue(Tables.EH_PAYMENT_BILLS.NOTICETEL));
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
    public List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,Long contractId) {
        List<BillDetailDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<Record> query = dslContext.selectQuery();
        query.addFrom(t);
        if(ownerType!=null){
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
        }
        if(ownerId!=null){
            query.addConditions(t.OWNER_ID.eq(ownerId));
        }
        if(targetType!=null){
            query.addConditions(t.TARGET_TYPE.eq(targetType));
        }
        if(targetId!=null){
            query.addConditions(t.TARGET_ID.eq(targetId));
        }
        if(billGroupId!=null){
            query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        }
        query.addConditions(t.SWITCH.eq((byte)1));
        if(contractId!=null){
            query.addConditions(t.CONTRACT_ID.eq(contractId));
        }
        if(isOwedBill==1){
            query.addConditions(t.STATUS.eq((byte)0));
        }
        query.fetch()
                .map(r -> {
                    BillDetailDTO dto = new BillDetailDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setAmountReceviable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setBillId(String.valueOf(r.getValue(t.ID)));
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
        dslContext.select(t.AMOUNT_OWED,t.CHARGING_ITEM_NAME,t.DATE_STR,t.AMOUNT_OWED,t.APARTMENT_NAME,t.BUILDING_NAME)
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .fetch()
                .map(r -> {
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    dto.setAddressName(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
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
                    dto.setBillingCycle(r.getValue(t.BALANCE_DATE_TYPE));
                    dto.setBillingDay(r.getValue(t.BILLS_DAY));
                    dto.setDueDay(r.getValue(t.DUE_DAY));
                    dto.setDueDayType(r.getValue(t.DUE_DAY_TYPE));
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
        EhPaymentChargingItemScopes ci = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("ci");


        ShowCreateBillDTO response = new ShowCreateBillDTO();
        List<BillItemDTO> list = new ArrayList<>();

        context.select(rule.CHARGING_ITEM_ID,ci.PROJECT_LEVEL_NAME)
                .from(rule,ci)
                .where(rule.CHARGING_ITEM_ID.eq(ci.CHARGING_ITEM_ID))
                .and(rule.OWNERID.eq(ci.OWNER_ID))
                .and(rule.BILL_GROUP_ID.eq(billGroupId))
                .fetch()
                .map(r -> {
                    BillItemDTO dto = new BillItemDTO();
                    dto.setBillItemId(r.getValue(rule.CHARGING_ITEM_ID));
                    dto.setBillItemName(r.getValue(ci.PROJECT_LEVEL_NAME));
                    list.add(dto);
                    return null;});

        response.setBillGroupId(billGroupId);
        response.setBillItemDTOList(list);
        return response;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailByDateStr(Byte billStatus,Long ownerId, String ownerType, Long targetId, String targetType, String dateStr,Long contractId) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        List<ShowBillDetailForClientDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");

        SelectQuery<Record> query = dslContext.selectQuery();
        query.addSelect(t.AMOUNT_RECEIVABLE, t.CHARGING_ITEM_NAME, t.DATE_STR, t.AMOUNT_OWED, t.AMOUNT_RECEIVABLE);
        query.addFrom(t);
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.TARGET_TYPE.eq(targetType));
        query.addConditions(t.TARGET_ID.eq(targetId));
        query.addConditions(t.DATE_STR.eq(dateStr));
        query.addConditions(t.CONTRACT_ID.eq(contractId));
        if(billStatus!=null){
            query.addConditions(t.STATUS.eq(billStatus));
        }
        try {
            query.fetch()
                    .map(r -> {
                        ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                        dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
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
    public ListBillsDTO creatPropertyBill( BillGroupDTO billGroupDTO,String dateStr, Byte isSettled, String noticeTel, Long ownerId, String ownerType, String targetName,Long targetId,String targetType,String contractNum,Long contractId) {
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

            long nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
            if(nextBillId == 0){
                nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
            }

            if(list2!=null) {
                //bill exemption
                List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
                long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class), list2.size());
                long currentExemItemSeq = nextExemItemBlock - list2.size() + 1;
                if(currentExemItemSeq == 0){
                    currentExemItemSeq = currentExemItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
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
                //应收否应该计算减免项
//                amountReceivable = amountReceivable.subtract(amountExemption);
//                amountReceivable = amountReceivable.add(amountSupplement);
                amountOwed = amountOwed.subtract(amountExemption);
                amountOwed = amountOwed.add(amountSupplement);
                EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
                exemptionItemsDao.insert(exemptionItems);
            }
            Byte billStatus = 0;
            if(list1!=null){
                //billItems assemble
                List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
                long nextBillItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class), list1.size());
                long currentBillItemSeq = nextBillItemBlock - list1.size() + 1;
                if(currentBillItemSeq == 0){
                    currentBillItemSeq = currentBillItemSeq+1;
                    this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                }

                for(int i = 0; i < list1.size() ; i++) {
                    BillItemDTO dto = list1.get(i);
                    PaymentBillItems item = new PaymentBillItems();
                    item.setAddressId(dto.getAddressId());
                    item.setBuildingName(dto.getBuildingName());
                    item.setApartmentName(dto.getApartmentName());
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
                    item.setContractId(contractId);
                    item.setContractNum(contractNum);
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
            newBill.setContractId(contractId);
            newBill.setContractNum(contractNum);
            EhPaymentBillsDao billsDao = new EhPaymentBillsDao(context.configuration());
            billsDao.insert(newBill);
            response[0] = ConvertHelper.convert(newBill, ListBillsDTO.class);
            response[0].setBillGroupName(billGroupDTO.getBillGroupName());
            response[0].setBillId(String.valueOf(nextBillId));
            response[0].setNoticeTel(noticeTel);
            response[0].setBillStatus(billStatus);
            response[0].setTargetType(targetType);
            response[0].setTargetId(String.valueOf(targetId));
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
        EhAddresses t1 = Tables.EH_ADDRESSES.as("t1");
        ListBillDetailVO vo = new ListBillDetailVO();
        BillGroupDTO dto = new BillGroupDTO();
        List<BillItemDTO> list1 = new ArrayList<>();
        List<ExemptionItemDTO> list2 = new ArrayList<>();

        context.select(r.ID,r.TARGET_ID,r.NOTICETEL,r.DATE_STR,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM)
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
                    //bill can has multiple addresses, thus one single address may confuse user
//                    vo.setBuildingName(f.getValue(r.BUILDING_NAME));
//                    vo.setApartmentName(f.getValue(r.APARTMENT_NAME));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    return null;
                });
        context.select(o.CHARGING_ITEM_NAME,o.ID,o.AMOUNT_RECEIVABLE,t1.APARTMENT_NAME,t1.BUILDING_NAME)
                .from(o)
                .leftOuterJoin(k)
                .on(o.CHARGING_ITEMS_ID.eq(k.ID))
                .leftOuterJoin(t1)
                .on(o.ADDRESS_ID.eq(t1.ID))
                .where(o.BILL_ID.eq(billId))
                .orderBy(k.DEFAULT_ORDER)
                .fetch()
                .map(f -> {
                    BillItemDTO itemDTO = new BillItemDTO();
                    itemDTO.setBillItemName(f.getValue(o.CHARGING_ITEM_NAME));
                    itemDTO.setBillItemId(f.getValue(o.ID));
                    itemDTO.setAmountReceivable(f.getValue(o.AMOUNT_RECEIVABLE));
                    itemDTO.setApartmentName(f.getValue(t1.APARTMENT_NAME));
                    itemDTO.setBuildingName(f.getValue(t1.BUILDING_NAME));
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
        query.addConditions(r.SWITCH.eq((byte)1));
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
    public List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId,String beginLimit, String endLimit) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentBills t1 = Tables.EH_PAYMENT_BILLS.as("t1");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> settledBillIds = context.select(t1.ID)
                .from(t1)
                .where(t1.SWITCH.eq((byte) 1))
                .fetch(t1.ID);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(DSL.sum(o.AMOUNT_RECEIVABLE),DSL.sum(o.AMOUNT_RECEIVED),DSL.sum(o.AMOUNT_OWED),o.CHARGING_ITEM_NAME);
        query.addFrom(t,o);
//        query.addJoin(o);
        if(settledBillIds!=null&& settledBillIds.size()>0){
            query.addConditions(o.BILL_ID.in(settledBillIds));
        }
        query.addConditions(o.OWNER_TYPE.eq(ownerType));
        query.addConditions(o.OWNER_ID.eq(ownerId));
        query.addConditions(o.CHARGING_ITEMS_ID.eq(t.ID));
        if(beginLimit!=null){
            query.addConditions(o.DATE_STR.greaterOrEqual(beginLimit));
        }
        if(endLimit!=null){
            query.addConditions(o.DATE_STR.lessOrEqual(endLimit));
        }
        query.addGroupBy(t.ID);
        query.addOrderBy(t.DEFAULT_ORDER);
        query.fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(o.AMOUNT_OWED)));
                    dto.setAmountReceivable(f.getValue(DSL.sum(o.AMOUNT_RECEIVABLE)));
                    dto.setAmountReceived(f.getValue(DSL.sum(o.AMOUNT_RECEIVED)));
                    dto.setValueOfX(f.getValue(o.CHARGING_ITEM_NAME));
                    list.add(dto);
                    return null;
                });
//        context.select(DSL.sum(o.AMOUNT_RECEIVABLE),DSL.sum(o.AMOUNT_RECEIVED),DSL.sum(o.AMOUNT_OWED),o.CHARGING_ITEM_NAME)
//                .from(o,t)
//                .where(o.OWNER_TYPE.eq(ownerType))
//                .and(o.OWNER_ID.eq(ownerId))
//                .and(o.CHARGING_ITEMS_ID.eq(t.ID))
//                .groupBy(t.ID)
//                .orderBy(t.DEFAULT_ORDER)
//                .fetch()
//                .map(f -> {
//                    BillStaticsDTO dto = new BillStaticsDTO();
//                    dto.setAmountOwed(f.getValue(DSL.sum(o.AMOUNT_OWED)));
//                    dto.setAmountReceivable(f.getValue(DSL.sum(o.AMOUNT_RECEIVABLE)));
//                    dto.setAmountReceived(f.getValue(DSL.sum(o.AMOUNT_RECEIVED)));
//                    dto.setValueOfX(f.getValue(o.CHARGING_ITEM_NAME));
//                    list.add(dto);
//                    return null;
//                });
        return list;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByCommunities(String dateStrBegin,String dateStrEnd,Integer currentNamespaceId) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhCommunities o = Tables.EH_COMMUNITIES.as("o");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(t);
        if(dateStrBegin!=null){
            query.addConditions(t.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(dateStrEnd!=null){
            query.addConditions(t.DATE_STR.lessOrEqual(dateStrEnd));
        }
        query.addConditions(t.SWITCH.eq((byte)1));
        Table<Record> r = query.asTable("r");

        context.select(DSL.sum(r.field(t.AMOUNT_RECEIVED)), DSL.sum(r.field(t.AMOUNT_RECEIVABLE)), DSL.sum(r.field(t.AMOUNT_OWED)), o.NAME)
                .from(r)
                .rightOuterJoin(o)
                .on(r.field(t.OWNER_ID).eq(o.ID))
                .where(o.NAMESPACE_ID.eq(currentNamespaceId))
                .groupBy(o.NAME)
                .fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(r.field(t.AMOUNT_OWED))));
                    dto.setAmountReceivable(f.getValue(DSL.sum(r.field(t.AMOUNT_RECEIVABLE))));
                    dto.setAmountReceived(f.getValue(DSL.sum(r.field(t.AMOUNT_RECEIVED))));
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
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        List<PaymentChargingItem> items = context.selectFrom(t)
                .fetchInto(PaymentChargingItem.class);

        List<PaymentChargingItemScope> scopes = context.selectFrom(t1)
                .where(t1.OWNER_ID.eq(ownerId))
                .and(t1.OWNER_TYPE.eq(ownerType))
                .fetchInto(PaymentChargingItemScope.class);

        Byte isSelected = 0;
        for(int i = 0; i < items.size(); i ++){
            PaymentChargingItem item = items.get(i);
            ListChargingItemsDTO dto = new ListChargingItemsDTO();
            dto.setChargingItemName(item.getName());
            dto.setChargingItemId(item.getId());
            dto.setIsSelected(isSelected);
            for(int j = 0; j < scopes.size(); j ++){
                PaymentChargingItemScope scope = scopes.get(j);
                if(item.getId() == scope.getChargingItemId()){
                    isSelected = 1;
                    dto.setProjectChargingItemName(scope.getProjectLevelName());
                    dto.setIsSelected(isSelected);
                    isSelected = 0;
                }
            }
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId) {
        List<ListChargingStandardsDTO> list = new ArrayList<>();
        List<String> variableInjectionJson = new ArrayList<>();

        List<Set<String>> variableIdentifiersOfMultiStandard = new ArrayList<>();


        List<String> formulas = new ArrayList<>();

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
            //现在不存变量注入的json串，改为从公式json中获得
            formulas.add(r.getValue(t.FORMULA_JSON));
            variableInjectionJson.add(r.getValue(t3.VARIABLES_JSON_STRING));
            list.add(dto);
            return null;
        });
        //获得不重复的字母的变量标识的集合
        for(int j = 0; j < formulas.size(); j ++){
            String formulaCopy = formulas.get(j);
            Set<String> variableIdentifiers = new HashSet<>();
            int index = 0;
            getVaraibleIdenInHashset(formulaCopy, variableIdentifiers, index);
            variableIdentifiersOfMultiStandard.add(variableIdentifiers);
        }
        for(int i = 0; i < list.size(); i++) {
            ListChargingStandardsDTO dto = list.get(i);
            com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards standard = findChargingStandardById(dto.getChargingStandardId());
            Set<String> idens = variableIdentifiersOfMultiStandard.get(i);
            List<PaymentVariable> variableList = new ArrayList<>();
            Iterator<String> iterator = idens.iterator();
            while(iterator.hasNext()){
                PaymentVariable viv = new PaymentVariable();
                String iden = iterator.next();
                com.everhomes.server.schema.tables.pojos.EhPaymentVariables varia = findVariableByIden(iden);
                viv.setVariableIdentifier(iden);
                if(iden.equals("dj")){
                    viv.setVariableValue(standard.getSuggestUnitPrice());
                }
                viv.setVariableName(varia.getName());
                variableList.add(viv);
            }
            dto.setVariables(variableList);
        }
        return list;
    }

    private com.everhomes.server.schema.tables.pojos.EhPaymentVariables findVariableByIden(String iden) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_VARIABLES)
                .where(Tables.EH_PAYMENT_VARIABLES.IDENTIFIER.eq(iden))
                .fetchOneInto(com.everhomes.server.schema.tables.pojos.EhPaymentVariables.class);
    }

    private boolean hasDigit(String iden) {
        char[] chars = iden.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
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

            if(list1!=null){
                for(int i = 0; i < list1.size() ; i++) {
                    BillItemDTO dto = list1.get(i);
                    context.update(t1)
                            .set(t1.AMOUNT_RECEIVABLE,dto.getAmountReceivable()==null?new BigDecimal("0"):dto.getAmountReceivable())
                            .set(t1.AMOUNT_OWED,dto.getAmountReceivable()==null?new BigDecimal("0"):dto.getAmountReceivable())
                            .set(t1.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                            .set(t1.OPERATOR_UID,UserContext.currentUserId())
                            .where(t1.BILL_ID.in(billId))
                            .and(t1.ID.eq(dto.getBillItemId()))
                            .execute();
                    amountReceivable = amountReceivable.add(dto.getAmountReceivable()==null?new BigDecimal("0"):dto.getAmountReceivable());
                }
            }
            List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
            if(list2!=null){
                //bill exemption
                for(int i = 0; i < list2.size(); i++){
                    ExemptionItemDTO exemptionItemDTO = list2.get(i);
                    if(exemptionItemDTO.getExemptionId()!=null){
                        context.update(t2)
                                .set(t2.AMOUNT,exemptionItemDTO.getAmount()==null?new BigDecimal("0"):exemptionItemDTO.getAmount())
                                .set(t2.REMARKS,exemptionItemDTO.getRemark())
                                .set(t2.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                                .set(t2.OPERATOR_UID,UserContext.currentUserId())
                                .where(t2.BILL_ID.eq(billId))
                                .and(t2.ID.eq(exemptionItemDTO.getExemptionId()))
                                .execute();
                    }else{
                        long nextId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
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
                    if(exemptionItemDTO.getAmount()!=null&&exemptionItemDTO.getAmount().compareTo(zero)==-1){
                        amountExemption = amountExemption.add(exemptionItemDTO.getAmount());
                    }else if(exemptionItemDTO.getAmount()!=null&&exemptionItemDTO.getAmount().compareTo(zero)==1){
                        amountSupplement = amountSupplement.add(exemptionItemDTO.getAmount());
                    }
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
    public List<ListBillExemptionItemsDTO> listBillExemptionItems(String billIdstr, int pageOffSet, Integer pageSize, String dateStr, String targetName) {
        Long billId = Long.parseLong(billIdstr);
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


        com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards standard = findChargingStandardById(chargingStandardId);
        String formulaJson = standard.getFormulaJson();

        //获得不重复的字母的变量标识的集合

        String formulaCopy = formulaJson;
        Set<String> variableIdentifiers = new HashSet<>();
        int index = 0;
        getVaraibleIdenInHashset(formulaCopy, variableIdentifiers, index);
        Iterator<String> iterator = variableIdentifiers.iterator();
        while(iterator.hasNext()){
            String iden = iterator.next();
            com.everhomes.server.schema.tables.pojos.EhPaymentVariables varia = findVariableByIden(iden);
            VariableIdAndValue viv = new VariableIdAndValue();
            viv.setVaribleIdentifier(iden);
            viv.setVariableId(varia.getId());
            if(viv.equals("dj")){
                viv.setVariableValue(standard.getSuggestUnitPrice());
            }
            list.add(viv);
        }
        return list;
    }

    private void getVaraibleIdenInHashset(String formulaCopy, Set<String> variableIdentifiers, int index) {
        for(int i = 0; i < formulaCopy.length(); i ++){
            char c = formulaCopy.charAt(i);
            if(c == '*' || c == '/' || c == '+' || c == '-' || i == formulaCopy.length()-1){
                String iden = formulaCopy.substring(index, i == formulaCopy.length() - 1 ? i + 1 : i);
                if(hasDigit(iden)){
                    variableIdentifiers.add(iden);
                }
                index = i + 1;
            }
        }
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
    public PaymentBillGroupRule getBillGroupRule(Long chargingItemId, Long chargingStandardId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        List<PaymentBillGroupRule> rules = context.select()
                .from(t)
                .where(t.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
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
    public void changeBillStatusOnContractSaved(Long contractId) {
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentContractReceiver t1 = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t1");
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            context.update(t)
                    .set(t.SWITCH,(byte)0)
                    .where(t.CONTRACT_ID.eq(contractId))
                    .and(t.SWITCH.eq((byte)3))
                    .execute();
            context.update(t1)
                    .set(t1.STATUS,(byte)1)
                    .where(t1.CONTRACT_ID.eq(contractId))
                    .execute();
            return null;
        });
    }

    @Override
    public void deleteContractPayment(Long contractId) {
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentContractReceiver t1 = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t1");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            List<Long> billIds = context.select(t.ID)
                    .from(t)
                    .where(t.CONTRACT_ID.eq(contractId))
                    .and(t.SWITCH.eq((byte) 3))
                    .fetch(t.ID);
            context.delete(t)
                    .where(t.ID.in(billIds))
                    .execute();
            context.delete(t2)
                    .where(t2.BILL_ID.in(billIds))
                    .or(t2.CONTRACT_ID.eq(contractId))
                    .execute();
            context.delete(t1)
                    .where(t1.CONTRACT_ID.eq(contractId))
                    .execute();
            return null;
        });
    }

    @Override
    public List<PaymentExpectancyDTO> listBillExpectanciesOnContract(String contractNum, Integer pageOffset, Integer pageSize) {
        List<PaymentExpectancyDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        context.select(t.DATE_STR,t.BUILDING_NAME,t.APARTMENT_NAME,t.DATE_STR_BEGIN,t.DATE_STR_END,t.DATE_STR_DUE,t.AMOUNT_RECEIVABLE,t1.NAME)
                .from(t,t1)
                .where(t.CONTRACT_NUM.eq(contractNum))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.NAME,t.DATE_STR)
                .limit(pageOffset,pageSize+1)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
                    dto.setDateStrEnd(r.getValue(t.DATE_STR));
                    dto.setPropertyIdentifier(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
                    dto.setDueDateStr(r.getValue(t.DATE_STR_DUE));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setChargingItemName(r.getValue(t1.NAME));
                    dtos.add(dto);
                    return null;
                });
        return dtos;
    }

    @Override
    public void updateBillsToSettled(Long contractId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        context.update(t)
                .set(t.SWITCH,(byte)1)
                .where(t.CONTRACT_ID.eq(contractId))
                .and(t.OWNER_TYPE.eq(ownerType))
                .and(t.OWNER_ID.eq(ownerId))
                .and(t.SWITCH.eq((byte)0))
                .execute();
    }

    @Override
    public void updatePaymentBill(Long billId, BigDecimal amountReceivable, BigDecimal amountReceived, BigDecimal amountOwed) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        context.update(t)
                .set(t.AMOUNT_RECEIVABLE,t.AMOUNT_RECEIVABLE.sub(amountReceivable))
                .set(t.AMOUNT_RECEIVED,t.AMOUNT_RECEIVED.sub(amountReceived))
                .set(t.AMOUNT_OWED,t.AMOUNT_OWED.sub(amountOwed))
                .execute();
    }

    @Override
    public PaymentBillItems findBillItemById(Long billItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select()
                .from(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.ID.eq(billItemId))
                .fetchOneInto(PaymentBillItems.class);
    }

    @Override
    public PaymentExemptionItems findExemptionItemById(Long exemptionItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select()
                .from(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.ID.eq(exemptionItemId))
                .fetchOneInto(PaymentExemptionItems.class);
    }

    @Override
    public void updatePaymentBillByExemItemChanges(Long billId, BigDecimal amount) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        context.update(t)
                .set(t.AMOUNT_OWED,t.AMOUNT_OWED.sub(amount))
                .execute();
        if(amount.compareTo(new BigDecimal("0"))<0){
            context.update(t)
                    .set(t.AMOUNT_EXEMPTION,t.AMOUNT_EXEMPTION.add(amount))
                    .execute();
        }else{
            context.update(t)
                    .set(t.AMOUNT_SUPPLEMENT,t.AMOUNT_SUPPLEMENT.sub(amount))
                    .execute();
        }
    }

    @Override
    public List<PaymentBillGroup> listAllBillGroups() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        EhPaymentBillGroupsRules t1 = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t1");
        List<PaymentBillGroup> list = new ArrayList<>();
        context.select()
                .from(t)
                .fetch()
                .map(r -> {
                    list.add(ConvertHelper.convert(r, PaymentBillGroup.class));
                    return null;
                });
        return list;
    }

    @Override
    public void updateBillSwitchOnTime(String billDateStr) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        context.update(t)
                .set(t.SWITCH,(byte)1)
                .where(t.DATE_STR.lessThan(billDateStr))
                .and(t.SWITCH.eq((byte)0))
                .execute();
    }

    @Override
    public String findZjgkCommunityIdentifierById(Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhCommunities t = Tables.EH_COMMUNITIES.as("t");
        return context.select(t.NAMESPACE_COMMUNITY_TOKEN)
                .from(t)
                .where(t.ID.eq(ownerId))
                .fetchAny(0,String.class);
    }

    @Override
    public Long findTargetIdByIdentifier(String customerIdentifier) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhUserIdentifiers t1 = Tables.EH_USER_IDENTIFIERS.as("t1");
        EhOrganizationOwners t2 = Tables.EH_ORGANIZATION_OWNERS.as("t2");
        return context.select(t1.OWNER_UID)
                .from(t1,t2)
                .where(t2.CONTACT_TOKEN.eq(t1.IDENTIFIER_TOKEN))
                .and(t2.NAMESPACE_CUSTOMER_TOKEN.eq(customerIdentifier))
                .fetchOne(0,Long.class);
    }

    @Override
    public String findAppName(Integer currentNamespaceId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<String> fetch = dslContext.select(Tables.EH_APP_URLS.NAME)
                .from(Tables.EH_APP_URLS)
                .where(Tables.EH_APP_URLS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetch(Tables.EH_APP_URLS.NAME);
        if(fetch!=null && fetch.size()>0){
            return fetch.get(0);
        }
        return "";
    }

    @Override
    public Long findOrganizationIdByIdentifier(String targetId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhOrganizations t = Tables.EH_ORGANIZATIONS.as("t");
        return context.select(t.ID)
                .from(t)
                .where(t.NAMESPACE_ORGANIZATION_TOKEN.eq(targetId))
                .fetchOne(0,Long.class);
    }

    @Override
    public AssetVendor findAssetVendorByNamespace(Integer namespaceId) {
        List<AssetVendor> list = new ArrayList<>();
        dbProvider.getDslContext(AccessSpec.readOnly()).selectFrom(Tables.EH_ASSET_VENDOR)
                .where(Tables.EH_ASSET_VENDOR.NAMESPACE_ID.eq(namespaceId))
                .fetch()
                .map(r -> {
                    list.add(ConvertHelper.convert(r, AssetVendor.class));
                    return null;
                });
        if(list!=null&&list.size()>0){
            return (AssetVendor)list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public String findIdentifierByUid(Long aLong) {
        DSLContext con = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhUserIdentifiers t = Tables.EH_USER_IDENTIFIERS.as("t");
        return con.select(t.IDENTIFIER_TOKEN)
                .from(t)
                .where(t.OWNER_UID.eq(aLong))
                .fetchOne(0,String.class);
    }


    @Override
    public Long saveAnOrderCopy(String payerType, String payerId, String amountOwed, String clientAppName, Long communityId, String contactNum, String openid, String payerName,Long expireTimePeriod,Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //TO SAVE A PRE ORDER COPY IN THE ORDER TABLE WITH STATUS BEING NOT BEING PAID YET
        long nextOrderId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrder.class));
        AssetPaymentOrder order = new AssetPaymentOrder();
        order.setClientAppName(clientAppName);
        order.setCommunityId(String.valueOf(communityId));
        order.setContractId(contactNum);
        order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        order.setId(nextOrderId);
        order.setNamespaceId(namespaceId);
        // GET THE START TIME AND EXPIRTIME
        Timestamp startTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        Calendar c = Calendar.getInstance();
        //expiretime为妙，所以乘以1000得到milliseconds
        long l = startTime.getTime() + expireTimePeriod*1000l;

        Timestamp endTime = new Timestamp(l);
        order.setOrderStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        order.setOrderExpireTime(endTime);
        order.setOrderType(OrderType.OrderTypeEnum.ZJGK_RENTAL_CODE.getPycode());

        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 17; i++){
               sb.append(r.nextInt(10));
        }
        order.setOrderNo(Long.parseLong(sb.toString()));
        order.setUid(UserContext.currentUserId());
        order.setPayAmount(new BigDecimal(amountOwed));
        order.setPayerType(payerType);
        order.setStatus((byte)0);
        EhAssetPaymentOrderDao dao = new EhAssetPaymentOrderDao(context.configuration());
        dao.insert(order);
        return nextOrderId;
    }


    @Override
    public Long findAssetOrderByBillIds(List<String> billIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        HashSet<Long> idSet = new HashSet<>();
        Long id = null;
        context.select(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.ORDER_ID)
                .from(Tables.EH_ASSET_PAYMENT_ORDER_BILLS)
                .where(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.BILL_ID.in(billIds))
                .fetch()
                .map(r -> {
                    idSet.add(r.getValue(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.ORDER_ID));
                    return null;});
        if(idSet.size()==1){
            id = idSet.iterator().next();
        }
        return id;
    }

    @Override
    public void saveOrderBills(List<BillIdAndAmount> bills, Long orderId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long nextBlockSequence = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhAssetPaymentOrderBills.class),bills.size());
        long nextSequence = nextBlockSequence - bills.size()+1;
        List<EhAssetPaymentOrderBills> orderBills = new ArrayList<>();
        for(int i = 0; i < bills.size(); i ++){
            EhAssetPaymentOrderBills orderBill  = new EhAssetPaymentOrderBills();
            BillIdAndAmount billIdAndAmount = bills.get(i);
            orderBill.setId(nextSequence++);
            orderBill.setAmount(new BigDecimal(billIdAndAmount.getAmountOwed()));
            orderBill.setBillId(billIdAndAmount.getBillId());
            orderBill.setOrderId(orderId);
            orderBill.setStatus(0);
            orderBills.add(orderBill);
        }
        EhAssetPaymentOrderBillsDao dao = new EhAssetPaymentOrderBillsDao(dslContext.configuration());
        dao.insert(orderBills);
    }

    @Override
    public AssetPaymentOrder findAssetPaymentById(Long orderId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhAssetPaymentOrder t = Tables.EH_ASSET_PAYMENT_ORDER.as("t");
        return context.select()
                .from(t)
                .where(t.ID.eq(orderId))
                .fetchOneInto(AssetPaymentOrder.class);
    }

    @Override
    public List<AssetPaymentOrderBills> findBillsById(Long orderId) {
        List<AssetPaymentOrderBills> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAssetPaymentOrderBills t = Tables.EH_ASSET_PAYMENT_ORDER_BILLS.as("t");
        context.select()
                .from(t)
                .where(t.ORDER_ID.eq(orderId))
                .fetch()
                .map(r -> {
                    list.add(ConvertHelper.convert(r,AssetPaymentOrderBills.class));
                    return null;
                });

        return list;
    }

    @Override
    public void changeOrderStaus(Long orderId, Byte finalOrderStatus) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAssetPaymentOrder t = Tables.EH_ASSET_PAYMENT_ORDER.as("t");
        context.update(t)
                .set(t.STATUS,finalOrderStatus)
                .where(t.ID.eq(orderId))
                .execute();
    }

    @Override
    public void changeBillStatusOnOrder(Map<String, Integer> billStatuses,Long orderId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhAssetPaymentOrderBills t = Tables.EH_ASSET_PAYMENT_ORDER_BILLS.as("t");
        for(Map.Entry<String,Integer> entry : billStatuses.entrySet()){
            context.update(t)
                    .set(t.STATUS,entry.getValue())
                    .where(t.BILL_ID.eq(entry.getKey()))
                    .and(t.ORDER_ID.eq(orderId))
                    .execute();
        }
    }

    @Override
    public PaymentUser findByOwner(String userType, Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentUsers t = Tables.EH_PAYMENT_USERS.as("t");
        List<PaymentUser> list = new ArrayList<>();
        context.select()
                .from(t)
                .where(t.OWNER_ID.eq(id))
                .and(t.OWNER_TYPE.eq(userType))
                .fetch()
                .map(r -> {
                    PaymentUser convert = ConvertHelper.convert(r, PaymentUser.class);
                    list.add(convert);
                    return null;
                });
        return list.size()>0?list.get(0):null;
    }

    @Override
    public PaymentAccount findPaymentAccount() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<PaymentAccount> list = new ArrayList<>();
        EhPaymentAccounts t = Tables.EH_PAYMENT_ACCOUNTS.as("t");
        context.select()
                .from(t)
                .fetch()
                .map(r -> {
                    PaymentAccount convert = ConvertHelper.convert(r, PaymentAccount.class);
                    list.add(convert);
                    return null;
                });
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void changeBillStatusOnPaiedOff(List<Long> billIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t1 = Tables.EH_PAYMENT_BILL_ITEMS.as("t1");
        context.update(t)
                .set(t.STATUS,(byte)1)
                .where(t.ID.in(billIds))
                .execute();
        context.update(t1)
                .set(t1.STATUS,(byte)1)
                .where(t1.BILL_ID.in(billIds))
                .execute();

    }

    @Override
    public void configChargingItems(List<ConfigChargingItems> configChargingItems, Long communityId, String ownerType,Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingItemScopes t = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t");
        EhPaymentChargingItemScopesDao dao = new EhPaymentChargingItemScopesDao(context.configuration());
        List<com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes> list = new ArrayList<>();
        for(int i = 0; i < configChargingItems.size(); i ++) {
            ConfigChargingItems vo = configChargingItems.get(i);
            PaymentChargingItemScope scope = new PaymentChargingItemScope();
            scope.setChargingItemId(vo.getChargingItemId());
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes.class));
            scope.setId(nextSequence);
            scope.setNamespaceId(namespaceId);
            scope.setOwnerId(communityId);
            scope.setOwnerType(ownerType);
            scope.setProjectLevelName(vo.getProjectChargingItemName());
            list.add(scope);
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            context.delete(t)
                    .where(t.OWNER_TYPE.eq(ownerType))
                    .and(t.OWNER_ID.eq(communityId))
                    .execute();
            if(list.size()>0){
                dao.insert(list);
            }
            return null;
        });



    }

    @Override
    public void createChargingStandard(com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards c, com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes s, List<EhPaymentFormula> f) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingStandardsDao ChargingStandardsDao = new EhPaymentChargingStandardsDao(context.configuration());
        EhPaymentChargingStandardsScopesDao chargingStandardsScopesDao = new EhPaymentChargingStandardsScopesDao(context.configuration());
        EhPaymentFormulaDao paymentFormulaDao = new EhPaymentFormulaDao(context.configuration());
        ChargingStandardsDao.insert(c);
        chargingStandardsScopesDao.insert(s);
        paymentFormulaDao.insert(f);
    }

    @Override
    public void modifyChargingStandard(ModifyChargingStandardCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        context.update(t)
                .set(t.NAME,cmd.getChargingStandardName())
                .set(t.INSTRUCTION,cmd.getInstruction())
                .set(t.AREA_SIZE_TYPE,cmd.getAreaSizeType())
                .set(t.SUGGEST_UNIT_PRICE,cmd.getSuggestUnitPrice())
                .where(t.ID.eq(cmd.getChargingStandardId()))
                .execute();
    }

    @Override
    public GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd) {
        DSLContext context = getReadOnlyContext();
        GetChargingStandardDTO dto = new GetChargingStandardDTO();
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        context.select(t.NAME,t.FORMULA,t.BILLING_CYCLE,t.INSTRUCTION,t.FORMULA_TYPE,t.SUGGEST_UNIT_PRICE,t.AREA_SIZE_TYPE)
                .from(t)
                .where(t.ID.eq(cmd.getChargingStandardId()))
                .fetch()
                .map(r -> {
                    dto.setChargingStandardName(r.getValue(t.NAME));
                    dto.setFormula(r.getValue(t.FORMULA));
                    dto.setBillingCycle(r.getValue(t.BILLING_CYCLE));
                    dto.setInstruction(r.getValue(t.INSTRUCTION));
                    dto.setFormulaType(r.getValue(t.FORMULA_TYPE));
                    dto.setSuggestUnitPrice(r.getValue(t.SUGGEST_UNIT_PRICE));
                    dto.setAreaSizeType(r.getValue(t.AREA_SIZE_TYPE));
                    return null;
                });
        return dto;
    }

    @Override
    public void deleteChargingStandard(Long chargingStandardId, Long ownerId, String ownerType) {
        DSLContext context = getReadWriteContext();
        EhPaymentChargingStandardsScopes t = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t");
        context.delete(t)
                .where(t.CHARGING_STANDARD_ID.eq(chargingStandardId))
                .and(t.OWNER_ID.eq(ownerId))
                .and(t.OWNER_TYPE.eq(ownerType))
                .execute();
    }

    @Override
    public List<ListAvailableVariablesDTO> listAvailableVariables(ListAvailableVariablesCommand cmd) {
        DSLContext context = getReadOnlyContext();
        List<ListAvailableVariablesDTO> list = new ArrayList<>();
        EhPaymentVariables t = Tables.EH_PAYMENT_VARIABLES.as("t");
        context.select(t.ID,t.NAME,t.IDENTIFIER)
                .from(t)
                .fetch()
                .map(r ->{
                    ListAvailableVariablesDTO dto = new ListAvailableVariablesDTO();
                    dto.setVariableIdentifier(r.getValue(t.IDENTIFIER));
                    dto.setVariableId(r.getValue(t.ID));
                    dto.setVariableName(r.getValue(t.NAME));
                    list.add(dto);
                    return null;});
        return list;

    }

    @Override
    public String getVariableIdenfitierById(Long variableId) {
        DSLContext context = getReadOnlyContext();
        EhPaymentVariables t = Tables.EH_PAYMENT_VARIABLES.as("t");
        return context.select(t.IDENTIFIER)
                .from(t)
                .where(t.ID.eq(variableId))
                .fetchOne(0,String.class);
    }



    @Override
    public String getVariableIdenfitierByName(String targetStr) {
        DSLContext context = getReadOnlyContext();
        EhPaymentVariables t = Tables.EH_PAYMENT_VARIABLES.as("t");
        return context.select(t.IDENTIFIER)
                .from(t)
                .where(t.NAME.eq(targetStr))
                .fetchOne(0,String.class);
    }

    @Override
    public void createBillGroup(CreateBillGroupCommand cmd) {
        DSLContext context = getReadWriteContext();
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = new PaymentBillGroup();
        Long nextGroupId = getNextSequence(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups.class);
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        group.setId(nextGroupId);
        group.setBalanceDateType(cmd.getBillingCycle());
        group.setBillsDay(cmd.getBillDay());
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setCreatorUid(UserContext.currentUserId());
        Integer nextOrder = context.select(DSL.max(t.DEFAULT_ORDER))
                .from(t)
                .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .fetchOne(0,Integer.class);
        group.setDefaultOrder(nextOrder==null?1:nextOrder+1);
        group.setDueDay(cmd.getDueDay());
        group.setDueDayType(cmd.getDueDayType());
        group.setName(cmd.getBillGroupName());
        group.setNamespaceId(cmd.getNamespaceId());
        group.setOwnerId(cmd.getOwnerId());
        group.setOwnerType(cmd.getOwnerType());
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhPaymentBillGroupsDao dao = new EhPaymentBillGroupsDao(context.configuration());
        dao.insert(group);
    }

    @Override
    public void modifyBillGroup(ModifyBillGroupCommand cmd) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        context.update(t)
                .set(t.NAME,cmd.getBillGroupName())
                .set(t.BILLS_DAY,cmd.getBillDay())
                .set(t.BALANCE_DATE_TYPE,cmd.getBillingCycle())
                .set(t.DUE_DAY_TYPE,cmd.getDueDateType())
                .where(t.ID.eq(cmd.getBillGroupId()))
                .execute();
    }

    @Override
    public List<ListChargingStandardsDTO> listOnlyChargingStandards(ListChargingStandardsCommand cmd) {
        List<ListChargingStandardsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        EhPaymentVariables t3 = Tables.EH_PAYMENT_VARIABLES.as("t3");
//        com.everhomes.server.schema.tables.EhPaymentFormula t3 = Tables.EH_PAYMENT_FORMULA.as("t3");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.BILLING_CYCLE,t.ID,t.NAME,t.FORMULA,t.FORMULA_TYPE,t.SUGGEST_UNIT_PRICE,t.AREA_SIZE_TYPE);
        query.addFrom(t,t1);
        query.addConditions(t.CHARGING_ITEMS_ID.eq(cmd.getChargingItemId()));
        query.addConditions(t1.CHARGING_STANDARD_ID.eq(t.ID));
        query.addConditions(t1.OWNER_ID.eq(cmd.getOwnerId()));
        query.addConditions(t1.OWNER_TYPE.eq(cmd.getOwnerType()));
        query.fetch().map(r -> {
            ListChargingStandardsDTO dto = new ListChargingStandardsDTO();
            dto.setBillingCycle(r.getValue(t.BILLING_CYCLE));
            dto.setChargingStandardId(r.getValue(t.ID));
            dto.setChargingStandardName(r.getValue(t.NAME));
            dto.setFormula(r.getValue(t.FORMULA));
            dto.setFormulaType(r.getValue(t.FORMULA_TYPE));
            dto.setSuggestUnitPrice(r.getValue(t.SUGGEST_UNIT_PRICE));
            dto.setAreaSizeType(r.getValue(t.AREA_SIZE_TYPE));
            list.add(dto);
            return null;
        });
        List<String> fetch = context.select(t3.NAME)
                .from(t3)
                .where(t3.CHARGING_ITEMS_ID.eq(cmd.getChargingItemId()))
                .fetch(t3.NAME);
        for(int i = 0; i < list.size(); i ++){
            ListChargingStandardsDTO dto = list.get(i);
            dto.setVariableNames(fetch);
        }
        return list;
    }

    @Override
    public void adjustBillGroupOrder(Long subjectBillGroupId, Long targetBillGroupId) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        Integer subjectDefaultOrder = context.select(t.DEFAULT_ORDER)
                .from(t)
                .where(t.ID.eq(subjectBillGroupId))
                .fetchOne(t.DEFAULT_ORDER);
        Integer targetDefaultOrder = context.select(t.DEFAULT_ORDER)
                .from(t)
                .where(t.ID.eq(targetBillGroupId))
                .fetchOne(t.DEFAULT_ORDER);
        context.update(t)
                .set(t.DEFAULT_ORDER,targetDefaultOrder)
                .where(t.ID.eq(subjectBillGroupId))
                .execute();
        context.update(t)
                .set(t.DEFAULT_ORDER,subjectDefaultOrder)
                .where(t.ID.eq(targetBillGroupId))
                .execute();
    }

    @Override
    public List<ListChargingItemsForBillGroupDTO> listChargingItemsForBillGroup(Long billGroupId) {
        List<ListChargingItemsForBillGroupDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        EhPaymentChargingStandards t3 = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t3");
        EhPaymentChargingItemScopes t4 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t4");

        SelectQuery<Record> query = context.selectQuery();
        List<PaymentBillGroupRule> rules = context.selectFrom(t)
                .where(t.BILL_GROUP_ID.eq(billGroupId))
                .fetchInto(PaymentBillGroupRule.class);
        for(int i = 0 ; i < rules.size(); i ++){
            PaymentBillGroupRule rule = rules.get(i);
            ListChargingItemsForBillGroupDTO dto = new ListChargingItemsForBillGroupDTO();
            dto.setBillGroupRuleId(rule.getId());
            PaymentBillGroup group = context.selectFrom(t1)
                    .where(t1.ID.eq(rule.getBillGroupId()))
                    .fetchOneInto(PaymentBillGroup.class);
            String ItemName = context.select(t4.PROJECT_LEVEL_NAME)
                    .from(t4)
                    .where(t4.CHARGING_ITEM_ID.eq(rule.getChargingItemId()))
                    .and(t4.OWNER_ID.eq(group.getOwnerId()))
                    .and(t4.OWNER_TYPE.eq(group.getOwnerType()))
                    .and(t4.NAMESPACE_ID.eq(group.getNamespaceId()))
                    .fetchOne(t4.PROJECT_LEVEL_NAME);
            if(StringUtils.isNullOrEmpty(ItemName)){
                ItemName = context.select(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME)
                        .from(Tables.EH_PAYMENT_CHARGING_ITEMS)
                        .where(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(rule.getChargingItemId()))
                        .fetchOne(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME);
            }
            dto.setBillingCycle(group.getBalanceDateType());

            PaymentChargingStandards standard = context.selectFrom(t3)
                    .where(t3.ID.eq(rule.getChargingStandardsId()))
                    .fetchOneInto(PaymentChargingStandards.class);
            dto.setFormula(standard.getFormula());
            dto.setProjectChargingItemName(ItemName==null?"":ItemName);
            dto.setChargingStandardName(standard.getName());
            dto.setBillItemGenerationMonth(rule.getBillItemMonthOffset());
            dto.setBillItemGenerationDay(rule.getBillItemDayOffset());
            list.add(dto);
        }
        return list;
    }

    @Override
    public AddOrModifyRuleForBillGroupResponse addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd) {
        AddOrModifyRuleForBillGroupResponse response = new AddOrModifyRuleForBillGroupResponse();
        Long ruleId = cmd.getBillGroupRuleId();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = new PaymentBillGroupRule();
        DSLContext readOnlyContext = getReadOnlyContext();
        DSLContext writeContext = getReadWriteContext();
        EhPaymentBillGroupsRulesDao dao = new EhPaymentBillGroupsRulesDao(writeContext.configuration());
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = readOnlyContext.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(cmd.getBillGroupId())).fetchOneInto(PaymentBillGroup.class);
        List<Long> fetch = readOnlyContext.select(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID).from(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(group.getId())).fetch(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID);
        if(fetch.contains(cmd.getChargingItemId())){
            response.setFailCause("添加失败，在一个账单组里不能重复添加收费项");
            return response;
        }
        if(ruleId == null){
            //新增 一条billGroupRule
            long nextRuleId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class));
            rule.setId(nextRuleId);
            rule.setBillGroupId(cmd.getBillGroupId());
            rule.setChargingItemId(cmd.getChargingItemId());
            rule.setChargingItemName(cmd.getGroupChargingItemName());
            rule.setChargingStandardsId(cmd.getChargingStandardId());
            rule.setNamespaceId(group.getNamespaceId());
            rule.setOwnerid(group.getOwnerId());
            rule.setOwnertype(group.getOwnerType());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            dao.insert(rule);
        }else{
            rule = readOnlyContext.selectFrom(t)
                    .where(t.ID.eq(ruleId))
                    .fetchOneInto(PaymentBillGroupRule.class);
            boolean workFlag = isInWorkGroupRule(rule,false);
            if(workFlag){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_ACCESS_DENIED,"已关联合同，不能修改");
            }
            //如果没有关联则不修改
            rule.setBillGroupId(cmd.getBillGroupId());
            rule.setChargingItemId(cmd.getChargingItemId());
            rule.setChargingItemName(cmd.getGroupChargingItemName());
            rule.setChargingStandardsId(cmd.getChargingStandardId());
            rule.setNamespaceId(group.getNamespaceId());
            rule.setOwnerid(group.getOwnerId());
            rule.setOwnertype(group.getOwnerType());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            dao.insert(rule);

            writeContext.delete(t)
                    .where(t.ID.eq(rule.getId()))
                    .execute();
            dao.insert(rule);
        }
        return response;
    }

    @Override
    public com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules findBillGroupRuleById(Long billGroupRuleId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                .fetchOneInto(PaymentBillGroupRule.class);
    }
    @Override
    public boolean isInWorkGroupRule(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule, boolean b) {
        DSLContext context = getReadOnlyContext();
        EhPaymentContractReceiver t = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t");
        if(!b){
            List<PaymentContractReceiver> list = context.selectFrom(t)
                    .where(t.OWNER_ID.eq(rule.getOwnerid()))
                    .and(t.OWNER_TYPE.eq(rule.getOwnertype()))
                    .and(t.NAMESPACE_ID.eq(rule.getNamespaceId()))
                    .and(t.EH_PAYMENT_CHARGING_ITEM_ID.eq(rule.getChargingItemId()))
                    .and(t.EH_PAYMENT_CHARGING_STANDARD_ID.eq(rule.getChargingStandardsId()))
                    .fetchInto(PaymentContractReceiver.class);
            if(list!=null && list.size()>0){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void deleteBillGroupRuleById(Long billGroupRuleId) {
        DSLContext context = getReadWriteContext();
        context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                .execute();
    }

    @Override
    public com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards findChargingStandardById(Long chargingStandardId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOneInto(PaymentChargingStandards.class);
    }

    @Override
    public PaymentBillGroup getBillGroupById(Long billGroupId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOneInto(PaymentBillGroup.class);
    }

    @Override
    public boolean checkBillsByBillGroupId(Long billGroupId) {
        DSLContext context = getReadOnlyContext();
        List<Long> fetch = context.select(Tables.EH_PAYMENT_BILLS.ID).from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.BILL_GROUP_ID.eq(billGroupId)).fetch(Tables.EH_PAYMENT_BILLS.ID);
        if(fetch.size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void deleteBillGroupAndRules(Long billGroupId) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        this.dbProvider.execute((TransactionStatus status) -> {
            context.delete(t)
                    .where(t.BILL_GROUP_ID.eq(billGroupId))
                    .execute();
            context.delete(t1)
                    .where(t1.ID.eq(billGroupId))
                    .execute();
            return null;
        });

    }

    @Override
    public ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(Long billGroupRuleId) {
        DSLContext context = getReadOnlyContext();
        ListChargingItemDetailForBillGroupDTO dto = new ListChargingItemDetailForBillGroupDTO();
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        EhPaymentBillGroupsRules t1 = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t1");
        PaymentBillGroupRule rule = context.selectFrom(t1)
                .where(t1.ID.eq(billGroupRuleId))
                .fetchOneInto(PaymentBillGroupRule.class);
        PaymentChargingStandards standard = context.selectFrom(t)
                .where(t.ID.eq(rule.getChargingStandardsId()))
                .fetchOneInto(PaymentChargingStandards.class);
        dto.setBillCycle(standard.getBillingCycle());
        dto.setBillGroupRuleId(rule.getId());
        dto.setChargingItemId(rule.getChargingItemId());
        dto.setChargingStandardId(standard.getId());
        dto.setDayOffset(rule.getBillItemDayOffset());
        dto.setMonthOffset(rule.getBillItemMonthOffset());
        dto.setFormula(standard.getFormula());
        dto.setGroupChargingItemName(rule.getChargingItemName());
        return dto;
    }


    private DSLContext getReadOnlyContext(){
       return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    private Long getNextSequence(Class clz){
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(clz));
    }


}
