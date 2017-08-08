package com.everhomes.asset;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.equipment.EquipmentInspectionEquipments;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetBillStatus;
import com.everhomes.rest.asset.AssetBillTemplateFieldDTO;
import com.everhomes.rest.asset.ListSettledBillDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.daos.EhAssetBillTemplateFieldsDao;
import com.everhomes.server.schema.tables.daos.EhAssetBillsDao;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import com.mysql.jdbc.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
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
        if(dateStrBegin!=null) {
            query.addConditions(t.DATE_STR.greaterOrEqual(dateStrBegin));
        }
        if(dateStrEnd!=null) {
            query.addConditions(t.DATE_STR.lessOrEqual(dateStrEnd));
        }
        if(targetName!=null) {
            query.addConditions(t.TARGETNAME.like("%"+targetName+"%"));
        }
        query.addOrderBy(t.DATE_STR.asc());
        query.addGroupBy(t.TARGETNAME);
        query.addLimit(pageOffSet,pageSize);
        query.fetch().map(r -> {
            ListSettledBillDTO dto = new ListSettledBillDTO();
            if(addressName!=null){
                dto.setAddressName(addressName);
            }else{
                String buildingApartmentName = "";
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
}
