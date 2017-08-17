package com.everhomes.asset;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.equipment.EquipmentInspectionEquipments;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.recommend.RecommendationService;
import com.everhomes.rest.asset.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.daos.EhAssetBillTemplateFieldsDao;
import com.everhomes.server.schema.tables.daos.EhAssetBillsDao;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import com.mysql.jdbc.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<ListSettledBillDTO> listSettledBill(Integer currentNamespaceId, Long ownerId, String ownerType, String addressName, Long addressId, String billGroupName, Long billGroupId, Byte billStatus, String dateStrBegin, String dateStrEnd, int pageOffSet, Integer pageSize, String targetName) {
        List<ListSettledBillDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<EhPaymentBillsRecord> query = context.selectQuery(t);
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
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
//        if(dateStrBegin!=null) {
//            //这里应该预防dateStr的不符合规则，所以要有正则表达式，先不加
//            query.addConditions(t.DATE_STR.greaterOrEqual(dateStrBegin));
//        }
//        if(dateStrEnd!=null) {
//            query.addConditions(t.DATE_STR.lessOrEqual(dateStrEnd));
//        }
        if(targetName!=null) {
            query.addConditions(t.TARGETNAME.like("%"+targetName+"%"));
        }
        query.addOrderBy(t.DATE_STR.asc());
        query.addGroupBy(t.TARGETNAME);
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(r -> {
            ListSettledBillDTO dto = new ListSettledBillDTO();
            if(addressName!=null){
                dto.setAddressName(addressName);
            }else{
                String buildingApartmentName = "";
                //这个时候 r.getValue(t.TARGET_TYPE)还是空值，可以使用条件语句空值多表关联
                if (r.getValue(t.TARGET_TYPE).equals("individual")){
                    buildingApartmentName = context.select(Tables.EH_ADDRESSES.BUILDING_NAME,Tables.EH_ADDRESSES.APARTMENT_NAME).from(Tables.EH_USERS,Tables.EH_ADDRESSES).where(Tables.EH_USERS.ID.eq(r.getValue(t.TARGET_ID)))
                            .and(Tables.EH_USERS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID)).fetchOne(0,String.class);
                } else if (r.getValue(t.TARGET_TYPE).equals("organization")) {
                    buildingApartmentName = context.select(Tables.EH_ORGANIZATIONS.ADDRESS_ID).from(Tables.EH_ORGANIZATIONS,Tables.EH_ADDRESSES).where(Tables.EH_ORGANIZATIONS.ID.eq(r.getValue(t.TARGET_ID)))
                            .and(Tables.EH_ORGANIZATIONS.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID)).fetchOne(0,String.class);
                }
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
        return list;
    }

    @Override
    public List<SettledBillDTO> listSettledBillItems(Long billId, String targetName, int pageOffSet, Integer pageSize) {
        List<SettledBillDTO> dtos = new ArrayList<>();
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
            SettledBillDTO dto =new SettledBillDTO();
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
}
