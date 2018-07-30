package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentUser;
import com.everhomes.pay.order.OrderDTO;
import com.everhomes.pay.user.ListBusinessUserByIdsCommand;
import com.everhomes.paySDK.api.PayService;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.EhAssetModuleAppMappings;
import com.everhomes.server.schema.tables.EhAssetPaymentOrder;
import com.everhomes.server.schema.tables.EhCommunities;
import com.everhomes.server.schema.tables.EhOrganizationOwners;
import com.everhomes.server.schema.tables.EhOrganizations;
import com.everhomes.server.schema.tables.EhPaymentAccounts;
import com.everhomes.server.schema.tables.EhPaymentBillCertificate;
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
import com.everhomes.server.schema.tables.EhPaymentLateFine;
import com.everhomes.server.schema.tables.EhPaymentNoticeConfig;
import com.everhomes.server.schema.tables.EhPaymentOrderRecords;
import com.everhomes.server.schema.tables.EhPaymentUsers;
import com.everhomes.server.schema.tables.EhPaymentVariables;
import com.everhomes.server.schema.tables.EhUserIdentifiers;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.EhAssetBillTemplateFields;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments;
import com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
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

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired 
    private PayService payService;
    
    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private AssetService assetService;

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

        if(StringUtils.isNotBlank(dateStr)) {
            query.addConditions(Tables.EH_ASSET_BILLS.ACCOUNT_PERIOD.like(dateStr + "%"));
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("findAssetBill, sql=" + query.getSQL());
            LOGGER.debug("findAssetBill, bindValues=" + query.getBindValues());
        }

        return query.fetchAnyInto(AssetBill.class);
    }

    @Override
    public List<ListBillsDTO> listBills(Integer currentNamespaceId, Integer pageOffSet, Integer pageSize, ListBillsCommand cmd) {
        //卸货
        String contractNum = cmd.getContractNum();
        Long ownerId = cmd.getOwnerId();
        String ownerType = cmd.getOwnerType();
        String billGroupName = cmd.getBillGroupName();
        Long billGroupId = cmd.getBillGroupId();
        Byte billStatus = cmd.getBillStatus();
        String dateStrBegin = cmd.getDateStrBegin();
        String dateStrEnd = cmd.getDateStrEnd();
        String targetName = cmd.getTargetName();
        Byte status = cmd.getStatus();
        String targetType = cmd.getTargetType();
        //应用id，多入口区分账单 by wentian 2018/5/25
        Long categoryId = cmd.getCategoryId();
        Integer paymentType = cmd.getPaymentType();
        Byte isUploadCertificate = cmd.getIsUploadCertificate();
        String buildingName = cmd.getBuildingName();//楼栋名称
        String apartmentName = cmd.getApartmentName();//门牌名称
        String customerTel = cmd.getCustomerTel();//客户手机号               
        Long targetIdForEnt = cmd.getTargetIdForEnt();//对公转账是根据企业id来查询相关的所有账单，如果是对公转账则不能为空

        //卸货结束
        List<ListBillsDTO> list = new ArrayList< >();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.AMOUNT_OWED,t.AMOUNT_RECEIVED,t.AMOUNT_RECEIVABLE,t.STATUS,t.NOTICETEL,t.NOTICE_TIMES,
                t.DATE_STR,t.TARGET_NAME,t.TARGET_ID,t.TARGET_TYPE,t.OWNER_ID,t.OWNER_TYPE,t.CONTRACT_NUM,t.CONTRACT_ID,t.BILL_GROUP_ID,
                t.INVOICE_NUMBER,t.PAYMENT_TYPE,t.DATE_STR_BEGIN,t.DATE_STR_END,t.CUSTOMER_TEL,
        		DSL.groupConcatDistinct(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME)).as("addresses"));
        query.addFrom(t, t2);
        query.addConditions(t.ID.eq(t2.BILL_ID));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));        
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));

        if(categoryId != null){
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }

        //status[Byte]:账单属性，0:未出账单;1:已出账单，对应到eh_payment_bills表中的switch字段
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
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)){
            query.addConditions(t.DATE_STR_BEGIN.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)){
            query.addConditions(t.DATE_STR_END.lessOrEqual(dateStrEnd));
        }
        if(!org.springframework.util.StringUtils.isEmpty(customerTel)){
            query.addConditions(t.CUSTOMER_TEL.like("%"+customerTel+"%"));
        }
        if(paymentType!=null){
            query.addConditions(t.PAYMENT_TYPE.eq(paymentType));
        }
        if(!org.springframework.util.StringUtils.isEmpty(targetIdForEnt)){
        	query.addConditions(t.TARGET_ID.eq(targetIdForEnt));//对公转账是根据企业id来查询相关的所有账单
        }
        //只查询上传了缴费凭证的记录
        if(isUploadCertificate!=null && isUploadCertificate == 1){
        	query.addConditions(t.IS_UPLOAD_CERTIFICATE.eq(isUploadCertificate));
        }
        query.addGroupBy(t.ID);
        //需根据收费项的楼栋门牌进行查询，不能直接根据账单的楼栋门牌进行查询
        if(!org.springframework.util.StringUtils.isEmpty(buildingName) || !org.springframework.util.StringUtils.isEmpty(apartmentName)) {
        	buildingName = buildingName != null ? buildingName : "";
        	apartmentName = apartmentName != null ? apartmentName : "";
        	String queryAddress = buildingName + "/" + apartmentName;
        	query.addHaving(DSL.groupConcatDistinct(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME)).like("%"+queryAddress+"%"));
        }
        if(status!=null && status == 1){
            query.addOrderBy(t.STATUS);
        }
        query.addOrderBy(t.DATE_STR_BEGIN.desc());
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(r -> {
        	ListBillsDTO dto = new ListBillsDTO();
            dto.setAddresses(r.getValue("addresses", String.class));
            dto.setBuildingName(r.getValue(t.BUILDING_NAME));
            dto.setApartmentName(r.getValue(t.APARTMENT_NAME));
            dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
            dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
            dto.setAmountReceived(r.getValue(t.AMOUNT_RECEIVED));
            if(!org.springframework.util.StringUtils.isEmpty(billGroupName)) {
                dto.setBillGroupName(billGroupName);
            }else{
                String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS).where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(r.getValue(t.BILL_GROUP_ID))).fetchOne(0,String.class);
                dto.setBillGroupName(billGroupNameFound);
            }
            dto.setBillId(String.valueOf(r.getValue(t.ID)));
            dto.setBillStatus(r.getValue(t.STATUS));
            dto.setNoticeTel(r.getValue(t.NOTICETEL));
            dto.setNoticeTimes(r.getValue(t.NOTICE_TIMES));
            dto.setDateStr(r.getValue(t.DATE_STR));
            dto.setTargetName(r.getValue(t.TARGET_NAME));
            dto.setTargetId(r.getValue(t.TARGET_ID, String.class));
            dto.setTargetType(r.getValue(t.TARGET_TYPE));
            dto.setOwnerId(r.getValue(t.OWNER_ID, String.class));
            dto.setOwnerType(r.getValue(t.OWNER_TYPE));
            dto.setContractNum(r.getValue(t.CONTRACT_NUM));
            dto.setContractId(r.getValue(t.CONTRACT_ID, String.class));
            // 增加发票编号
            dto.setInvoiceNum(r.getValue(t.INVOICE_NUMBER));
            //添加支付方式
            dto.setPaymentType(r.getValue(t.PAYMENT_TYPE));
            //增加账单时间
            dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
            dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
            //增加客户手机号
            dto.setCustomerTel(r.getValue(t.CUSTOMER_TEL));
            list.add(dto);
            return null;});
        return list;
    }

    @Override
    public List<BillDTO> listBillItems(Long billId, String targetName, int pageNum, Integer pageSize) {
        List<BillDTO> dtos = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhAddresses t2 = Tables.EH_ADDRESSES.as("t2");
        EhPaymentLateFine t3 = Tables.EH_PAYMENT_LATE_FINE.as("t3");
        //先算第一个offset
        int firstOffset = (pageNum - 1) * pageSize;
        String sql = context.select(t.DATE_STR,t.CHARGING_ITEM_NAME,t.AMOUNT_RECEIVABLE,t.AMOUNT_RECEIVED,t.AMOUNT_OWED,t.STATUS,t.ID,t2.APARTMENT_NAME,t2.BUILDING_NAME,t.BILL_GROUP_RULE_ID, t.BUILDING_NAME, t.APARTMENT_NAME)
                .from(t)
                .leftOuterJoin(t2)
                .on(t.ADDRESS_ID.eq(t2.ID))
                .where(t.BILL_ID.eq(billId))
                .limit(firstOffset,pageSize+1)
                .getSQL();
        context.fetch(sql,billId,pageSize + 1,firstOffset)
                .forEach(r ->{
                    BillDTO dto =new BillDTO();
                    dto.setDateStr(r.getValue(t.DATE_STR));
                    dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setAmountReceived(r.getValue(t.AMOUNT_RECEIVED));
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setBillStatus(r.getValue(t.STATUS));
                    dto.setBillItemId(r.getValue(t.ID));
                    dto.setBillGroupRuleId(r.getValue(t.BILL_GROUP_RULE_ID));
                    String addrBuildingName = r.getValue(t2.BUILDING_NAME);
                    String addrAartName = r.getValue(t2.APARTMENT_NAME);
                    if(addrBuildingName!=null || addrAartName!=null){
                        dto.setApartmentName(addrAartName);
                        dto.setBuildingName(addrBuildingName);
                    }else{
                        // 使用t.APARTMENT_NAME仍然识别不了
                        if(r.getValue(11)!=null){
                            try{
                                dto.setApartmentName(r.getValue(11).toString());
                            }catch (Exception e){
                            }
                        }
                        if(r.getValue(10)!=null){
                            try{
                                dto.setBuildingName(r.getValue(10).toString());
                            }catch (Exception e){
                            }
                        }
                    }
                    dtos.add(dto);
                    });
        List<BillDTO> fines = new ArrayList<>();
        for(int i = 0; i < dtos.size(); i ++){
            BillDTO dto = dtos.get(i);
            dto.setTargetName(targetName);
            if(org.apache.commons.lang.StringUtils.isEmpty(dto.getBillItemName())) {
                String projectLevelName = context.select(itemScope.PROJECT_LEVEL_NAME)
                        .from(itemScope, groupRule)
                        .where(groupRule.CHARGING_STANDARDS_ID.eq(itemScope.ID))
                        .and(groupRule.ID.eq(dto.getBillGroupRuleId()))
                        .fetchOne(itemScope.PROJECT_LEVEL_NAME);
                dto.setBillItemName(projectLevelName);
            }
            //查询billItem的滞纳金
            getReadOnlyContext().select(t3.NAME,t3.AMOUNT,t3.AMOUNT,t.DATE_STR,t.STATUS,t3.ID,t.BILL_GROUP_RULE_ID
                    ,t2.APARTMENT_NAME,t2.BUILDING_NAME)
                    .from(t3)
                    .leftOuterJoin(t)
                    .on(t3.BILL_ITEM_ID.eq(t.ID))
                    .leftOuterJoin(t2)
                    .on(t.ADDRESS_ID.eq(t2.ID))
                    .where(t3.BILL_ITEM_ID.eq(dto.getBillItemId()))
                    .fetch()
                    .forEach(r -> {
                        BillDTO fineDTO = (BillDTO)dto.clone();
                        fineDTO.setBillItemName(r.getValue(t3.NAME));
                        fineDTO.setAmountReceivable(r.getValue(t3.AMOUNT));
                        fineDTO.setAmountReceived(new BigDecimal("0"));
                        fineDTO.setAmountOwed(r.getValue(t3.AMOUNT));
                        fineDTO.setDateStr(r.getValue(t.DATE_STR));
                        fineDTO.setBillStatus(r.getValue(t.STATUS));
                        fineDTO.setBillItemId(r.getValue(t3.ID));
                        fineDTO.setBillGroupRuleId(r.getValue(t.BILL_GROUP_RULE_ID));
                        fineDTO.setApartmentName(r.getValue(t2.APARTMENT_NAME));
                        fineDTO.setBuildingName(r.getValue(t2.BUILDING_NAME));
                        fines.add(fineDTO);
                    });
        }
        dtos.addAll(fines);
        return dtos;
    }

    @Override
    public List<NoticeInfo> listNoticeInfoByBillId(List<Long> billIds) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<NoticeInfo> list = new ArrayList<>();
        dslContext.select(Tables.EH_PAYMENT_BILLS.NOTICETEL,Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILLS.AMOUNT_OWED,Tables.EH_PAYMENT_BILLS.TARGET_ID,Tables.EH_PAYMENT_BILLS.TARGET_TYPE,Tables.EH_PAYMENT_BILLS.TARGET_NAME,Tables.EH_PAYMENT_BILLS.DATE_STR, Tables.EH_PAYMENT_BILLS.NAMESPACE_ID)
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
            info.setDateStr(r.getValue(Tables.EH_PAYMENT_BILLS.DATE_STR));
            info.setNamespaceId(r.getValue(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID));
            list.add(info);
            return null;});
        List<String> fetch = dslContext.select(Tables.EH_APP_URLS.NAME)
                .from(Tables.EH_APP_URLS)
                .where(Tables.EH_APP_URLS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetch(Tables.EH_APP_URLS.NAME);
        if(fetch.size()<1){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION, "该域空间没有" +
                    "在app地址表中进行注册，无法获得域空间名称，请联系左邻管理员，域空间id为"+UserContext.getCurrentNamespaceId());
        }
        String appName = fetch.get(0);
        for(int i = 0; i < list.size(); i++){
            list.get(i).setAppName(appName);
        }
        return list;
    }

    @Override
    public List<BillDetailDTO> listBillForClient(Long ownerId, String ownerType, String targetType, Long targetId, Long billGroupId,Byte isOwedBill,Long contractId,String contractNum) {
        List<BillDetailDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        SelectQuery<Record> query = dslContext.selectQuery();
        query.addFrom(t);
        //必要用户参数
        query.addConditions(t.TARGET_TYPE.eq(targetType));
        query.addConditions(t.TARGET_ID.eq(targetId));
        if(ownerType != null){
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
        }
        if(ownerId != null){
            query.addConditions(t.OWNER_ID.eq(ownerId));
        }else{
            LOGGER.error("showBillClient did not send ownerId which is not right, targetId = {},targetType = {}",targetId,targetType);
        }
        //已出账单，排除了未来账单
        query.addConditions(t.SWITCH.eq((byte)1));

        if(billGroupId!=null){
            query.addConditions(t.BILL_GROUP_ID.eq(billGroupId));
        }
        if(contractId!=null){
            query.addConditions(t.CONTRACT_ID.eq(contractId));
        }
        if(contractNum!=null){
            query.addConditions(t.CONTRACT_NUM.eq(contractNum));
        }
        if(isOwedBill==1){
            query.addConditions(t.STATUS.eq((byte)0));
        }
//        List<Byte> list  = new ArrayList<>();
        query.fetch()
                .map(r -> {
                    BillDetailDTO dto = new BillDetailDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setAmountReceviable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setBillId(String.valueOf(r.getValue(t.ID)));
                    dto.setDateStr(r.getValue(t.DATE_STR));

//                    list.add(r.getValue(t.DATE_STR_DUE));
//                    list.add(r.getValue(t.DUE_DAY_DEADLINE));
//                    list.add(r.getValue(t.STATUS));
//                    list. add(r.getValue(t.CHARGE_STATUS));
                    dto.setChargeStatus(r.getValue(t.CHARGE_STATUS));
                    dto.setStatus(r.getValue(t.STATUS));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setDeadline(r.getValue(t.DUE_DAY_DEADLINE));
                    dtos.add(dto);
                    return null;});
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0; i < dtos.size(); i ++){
            //根据t.status 缴纳情况和 t.chargetStatus正常或者欠费来判定三个状态，注意，这里没有未缴状态，因为所有都是已出账单
            BillDetailDTO dto = dtos.get(i);
            if(dto.getChargeStatus()!=null){
                if(dto.getStatus().byteValue() == (byte)0 && dto.getChargeStatus().byteValue() == (byte)1){
                    dto.setStatus((byte)2);
                }else if(dto.getStatus().byteValue() == (byte)0 && dto.getChargeStatus().byteValue() == (byte)0){
                    dto.setStatus((byte)0);
                }
            }else{
                String deadline = dto.getDeadline();
                try{
                    Date dead = sdf.parse(deadline);
                    if(dto.getStatus().byteValue() == (byte)0 && today.compareTo(dead) != -1){
                        dto.setStatus((byte)2);
                    }else if(dto.getStatus().byteValue() == (byte)0 && today.compareTo(dead) != 1){
                        dto.setStatus((byte)0);
                    }
                }catch(Exception e){}
            }
            //这是按照时间来划分状态
//            String due = (String)list.get(3*i);
//            String deadline = (String)list.get(3*i+1);
//            Byte status = (Byte)list.get(3*i+2);
//            if(status != null && status.byteValue() == (byte)1){
//                dto.setStatus((byte)1);
//                continue;
//            }
//            try{
//                if(due!=null && sdf.parse(due).compareTo(today) != 1){
//                    dto.setStatus((byte)0);
//                    //状态进阶为待缴
//                }else if(due!=null && sdf.parse(due).compareTo(today) == 1){
//                    dto.setStatus((byte)3);
//                    //未缴
//                }
//                if(deadline!=null && sdf.parse(deadline).compareTo(today)== -1){
//                    //状态进阶为欠费
//                    dto.setStatus((byte)2);
//                }
//            }catch (Exception e){
//                dto.setStatus((byte)0);
//            }
        }
        for(int i = 0; i < dtos.size(); i++){
            BillDetailDTO dto = dtos.get(i);
            if(isOwedBill.byteValue() == (byte)1 && dto.getStatus().byteValue() == (byte)3){
                dtos.remove(dto);
                i--;
            }
        }
        return dtos;
    }

    @Override
    public ShowBillDetailForClientResponse getBillDetailForClient(Long billId) {
        ShowBillDetailForClientResponse response = new ShowBillDetailForClientResponse();
        final String[] dateStrBegin = {""};
        final String[] dateStrEnd = {""};
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        List<ShowBillDetailForClientDTO> dtos = new ArrayList<>();
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");//账单收费细项
        EhPaymentLateFine fine = Tables.EH_PAYMENT_LATE_FINE.as("fine");//滞纳金
        EhPaymentExemptionItems exemption = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("exemption");//减免/增收项

        dslContext.select(t.AMOUNT_OWED,t.CHARGING_ITEM_NAME,t.DATE_STR,t.APARTMENT_NAME,t.BUILDING_NAME,t.AMOUNT_RECEIVABLE,t.DATE_STR_BEGIN,t.DATE_STR_END
        		,t.ENERGY_CONSUME,t.CHARGING_ITEMS_ID)
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .fetch()
                .map(r -> {
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    String buildingName = r.getValue(t.BUILDING_NAME)==null?"":r.getValue(t.BUILDING_NAME);
                    String apartmentName = r.getValue(t.APARTMENT_NAME)==null?"":r.getValue(t.APARTMENT_NAME);
                    String address = buildingName + apartmentName;
                    dto.setAddressName(org.apache.commons.lang.StringUtils.isEmpty(address)?"":address);
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    //根据减免费项配置重新计算待收金额
                    Long charingItemId = r.getValue(t.CHARGING_ITEMS_ID);
                    Boolean isConfigSubtraction = isConfigItemSubtraction(billId, charingItemId);//用于判断该费项是否配置了减免费项
                    if(!isConfigSubtraction) {//如果费项没有配置减免费项，那么需要相加到待缴金额中
                    	dto.setIsConfigSubtraction((byte)0);
                    	amountOwed[0] = amountOwed[0].add(r.getValue(t.AMOUNT_OWED));
                    }else {
                    	dto.setIsConfigSubtraction((byte)1);
                    }
                    //issue-34165 个人客户，登录查看账单详情页面，用量保留一位小数 杨崇鑫
                    DecimalFormat df = new DecimalFormat("#0.0");
                    String energyConsume = r.getValue(t.ENERGY_CONSUME);
                    if(!org.springframework.util.StringUtils.isEmpty(energyConsume)) {
                    	BigDecimal energyConsumeBigDecimal = new BigDecimal(energyConsume);
                    	dto.setEnergyConsume(df.format(energyConsumeBigDecimal));//增加用量
                    }else {
                    	dto.setEnergyConsume(energyConsume);//增加用量
                    }
                    if(AssetEnergyType.personWaterItem.getCode().equals(r.getValue(t.CHARGING_ITEMS_ID))
                    		|| AssetEnergyType.publicWaterItem.getCode().equals(r.getValue(t.CHARGING_ITEMS_ID))) {
                    	dto.setEnergyUnit("吨");
                    }else if(AssetEnergyType.personElectricItem.getCode().equals(r.getValue(t.CHARGING_ITEMS_ID))
                    		|| AssetEnergyType.publicElectricItem.getCode().equals(r.getValue(t.CHARGING_ITEMS_ID))) {
                    	dto.setEnergyUnit("度");
                    }
                    dtos.add(dto);
                    dateStrBegin[0] = r.getValue(t.DATE_STR_BEGIN);
                    dateStrEnd[0] = r.getValue(t.DATE_STR_END);
                    amountReceivable[0] = amountReceivable[0].add(r.getValue(t.AMOUNT_RECEIVABLE));
                    return null;
                });
        dslContext.select(fine.AMOUNT,fine.NAME,t.DATE_STR,t.APARTMENT_NAME,t.BUILDING_NAME,t.AMOUNT_RECEIVABLE,t.DATE_STR_BEGIN,t.DATE_STR_END,t.AMOUNT_OWED,
        		fine.BILL_ITEM_ID)
                .from(fine,t)
                .where(fine.BILL_ITEM_ID.eq(t.ID))
                .and(fine.BILL_ID.eq(billId))
                .fetch()
                .map(r -> {
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(r.getValue(fine.AMOUNT));
                    dto.setBillItemName(r.getValue(fine.NAME));
                    dto.setAddressName(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
                    dto.setAmountReceivable(r.getValue(fine.AMOUNT));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    Long billItemId = r.getValue(fine.BILL_ITEM_ID);
                	//减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金
                	Long chargingItemId = getPaymentBillItemsChargingItemID(billId,billItemId);
                	//根据减免费项配置重新计算待收金额
                    Boolean isConfigSubtraction = isConfigLateFineSubtraction(billId, chargingItemId);//用于判断该滞纳金是否配置了减免费项
                    if(!isConfigSubtraction) {//如果滞纳金没有配置减免费项，那么需要相加到待缴金额中
                    	dto.setIsConfigSubtraction((byte)0);
                    	amountOwed[0] = amountOwed[0].add(r.getValue(fine.AMOUNT));
                    }else {
                    	dto.setIsConfigSubtraction((byte)1);
                    }  
                    dtos.add(dto);
                    dateStrBegin[0] = r.getValue(t.DATE_STR_BEGIN);
                    dateStrEnd[0] = r.getValue(t.DATE_STR_END);
                    amountReceivable[0] = amountReceivable[0].add(r.getValue(fine.AMOUNT));
                    return null;
                });
        //查询该账单id是否存在对应的缴费凭证记录
        Result<Record> fetch = dslContext.select()
        		.from(Tables.EH_PAYMENT_BILL_CERTIFICATE)
        		.where(Tables.EH_PAYMENT_BILL_CERTIFICATE.BILL_ID.eq(billId))
        		.fetch();
        if(fetch.isNotEmpty()){
        	response.setIsUploadCertificate((byte)1);
        }else{
        	response.setIsUploadCertificate((byte)0);
        }
        
        //后台设置了减免增收后，APP端需作为一个费项展现出来
        dslContext.select(exemption.AMOUNT,exemption.REMARKS)
		    .from(exemption)
		    .where(exemption.BILL_ID.eq(billId))
		    .fetch()
		    .map(r -> {
		        ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
		        dto.setAmountReceivable(r.getValue(exemption.AMOUNT));
		        dto.setBillItemName(r.getValue(exemption.REMARKS));
		        dtos.add(dto);
		        amountOwed[0] = amountOwed[0].add(r.getValue(exemption.AMOUNT));
		        return null;
        });
        response.setAmountReceivable(amountReceivable[0]);
        amountOwed[0] = DecimalUtils.negativeValueFilte(amountOwed[0]);
        response.setAmountOwed(amountOwed[0]);
        response.setDatestr(dateStrBegin[0] + "~" + dateStrEnd[0]);
        response.setShowBillDetailForClientDTOList(dtos);
        //增加附件信息
        List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList = new ArrayList<>();
        dslContext.select(Tables.EH_PAYMENT_BILL_ATTACHMENTS.ID,Tables.EH_PAYMENT_BILL_ATTACHMENTS.FILENAME,Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_URI,
        		Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_URL,Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_TYPE)
		    .from(Tables.EH_PAYMENT_BILL_ATTACHMENTS)
		    .where(Tables.EH_PAYMENT_BILL_ATTACHMENTS.BILL_ID.eq(billId))
		    .fetch()
		    .map(r -> {
		    	AssetPaymentBillAttachment assetPaymentBillAttachment = new AssetPaymentBillAttachment();
		    	assetPaymentBillAttachment.setFilename(r.getValue(Tables.EH_PAYMENT_BILL_ATTACHMENTS.FILENAME));
		    	assetPaymentBillAttachment.setUri(r.getValue(Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_URI));
		    	assetPaymentBillAttachment.setUrl(r.getValue(Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_URL));
		    	assetPaymentBillAttachment.setContentType(r.getValue(Tables.EH_PAYMENT_BILL_ATTACHMENTS.CONTENT_TYPE));
		    	assetPaymentBillAttachmentList.add(assetPaymentBillAttachment);
		        return null;
	    });
        response.setAssetPaymentBillAttachmentList(assetPaymentBillAttachmentList);
        return response;
    }

    @Override
    public List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType, Long categoryId) {
        List<ListBillGroupsDTO> list = new ArrayList<>();
        List<Long> userIds = new ArrayList<Long>();
        ListBusinessUserByIdsCommand cmd = new ListBusinessUserByIdsCommand();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID,t.NAME,t.DEFAULT_ORDER,t.BALANCE_DATE_TYPE,t.BALANCE_DATE_TYPE,t.BILLS_DAY,
        		t.DUE_DAY,t.DUE_DAY_TYPE,t.BILLS_DAY_TYPE,t.BILLS_DAY_TYPE,t.BIZ_PAYEE_TYPE,t.BIZ_PAYEE_ID);
        query.addFrom(t);
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        if(categoryId != null){
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }
        query.addOrderBy(t.DEFAULT_ORDER);
        query.fetch().map(r -> {
        	ListBillGroupsDTO dto = new ListBillGroupsDTO();
            dto.setBillGroupId(r.getValue(t.ID));
            dto.setBillGroupName(r.getValue(t.NAME));
            dto.setDefaultOrder(r.getValue(t.DEFAULT_ORDER)); 
            dto.setBillingCycle(r.getValue(t.BALANCE_DATE_TYPE));
            dto.setBillingDay(r.getValue(t.BILLS_DAY));
            dto.setDueDay(r.getValue(t.DUE_DAY));
            dto.setDueDayType(r.getValue(t.DUE_DAY_TYPE));
            dto.setBillDayType(r.getValue(t.BILLS_DAY_TYPE));
            dto.setBizPayeeType(r.getValue(t.BIZ_PAYEE_TYPE));//收款方账户类型
            dto.setBizPayeeId(r.getValue(t.BIZ_PAYEE_ID));//收款方账户id
            userIds.add(r.getValue(t.BIZ_PAYEE_ID));
            list.add(dto);
            return null;
        });
        //由于收款方账户名称可能存在修改的情况，故重新请求电商
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listBillGroups(request), cmd={}", userIds);
        }
        List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(userIds);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listBillGroups(response), response={}", payUserDTOs);
        }
        if(payUserDTOs != null && payUserDTOs.size() != 0) {
        	for(int i = 0;i < payUserDTOs.size();i++) {
            	for(int j = 0;j < list.size();j++) {
            		if(payUserDTOs.get(i) != null && list.get(j) != null &&
            			payUserDTOs.get(i).getId() != null && list.get(j).getBizPayeeId() != null &&
            			payUserDTOs.get(i).getId().equals(list.get(j).getBizPayeeId())){
            			list.get(j).setAccountName(payUserDTOs.get(i).getRemark());// 用户向支付系统注册帐号时填写的帐号名称
            			list.get(j).setAccountAliasName(payUserDTOs.get(i).getUserAliasName());//企业名称（认证企业）
            			// 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
                        Integer registerStatus = payUserDTOs.get(i).getRegisterStatus();
                        if(registerStatus != null && registerStatus.intValue() == 1) {
                        	list.get(j).setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
                        } else {
                        	list.get(j).setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
                        }
            		}
            	}
            }
        }
        return list;
    }

    @Override
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules rule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("rule");
//        EhPaymentBillGroups bg = Tables.EH_PAYMENT_BILL_GROUPS.as("bg");
        EhPaymentChargingItemScopes ci = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("ci");


        ShowCreateBillDTO response = new ShowCreateBillDTO();
        List<BillItemDTO> list = new ArrayList<>();
        Long categoryId = findCategoryIdFromBillGroup(billGroupId);
        context.select(rule.CHARGING_ITEM_ID,ci.PROJECT_LEVEL_NAME,rule.ID)
                .from(rule,ci)
                .where(rule.CHARGING_ITEM_ID.eq(ci.CHARGING_ITEM_ID))
                .and(rule.OWNERID.eq(ci.OWNER_ID))
                .and(rule.BILL_GROUP_ID.eq(billGroupId))
                .and(ci.CATEGORY_ID.eq(categoryId))
                .fetch()
                .map(r -> {
                    BillItemDTO dto = new BillItemDTO();
                    dto.setBillItemId(r.getValue(rule.CHARGING_ITEM_ID));
                    dto.setBillItemName(r.getValue(ci.PROJECT_LEVEL_NAME));
                    dto.setBillGroupRuleId(r.getValue(rule.ID));
                    list.add(dto);
                    return null;});

        response.setBillGroupId(billGroupId);
        response.setBillItemDTOList(list);
        List<String> fetch = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS.NAME);
        if(fetch.size() > 0){
            response.setBillGroupName(fetch.get(0));
        }
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
                        dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
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
    public ListBillsDTO creatPropertyBill( CreateBillCommand cmd, Long billId) {
        final ListBillsDTO[] response = {new ListBillsDTO()};
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //根据billGroup获得时间，如需重复使用，则请抽象出来
            SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
            //获取所需要的cmd的数据
            BillGroupDTO billGroupDTO = cmd.getBillGroupDTO();
            Byte isSettled = cmd.getIsSettled();
            String noticeTel = cmd.getNoticeTel();
            Long ownerId = cmd.getOwnerId();
            String ownerType = cmd.getOwnerType();
            String targetName = cmd.getTargetName();
            Long targetId = cmd.getTargetId();
            String targetType = cmd.getTargetType();
            String contractNum = cmd.getContractNum();
            Long contractId = cmd.getContractId();
            String dateStrBegin = cmd.getDateStrBegin();
            String dateStrEnd = cmd.getDateStrEnd();
            //账期取的是账单开始时间的yyyy-MM
            String dateStr;
			try {
				dateStr = yyyyMM.format(yyyyMM.parse(dateStrBegin));
			}catch (Exception e){
				dateStr = null;
                LOGGER.error(e.toString());
            }
            Byte isOwed = cmd.getIsOwed();
            String customerTel = cmd.getCustomerTel();
            String invoiceNum = cmd.getInvoiceNum();
            Long categoryId = cmd.getCategoryId();

            //普通信息卸载
            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();
            List<SubItemDTO> list3 = billGroupDTO.getSubItemDTOList();//增加减免费项
            String apartmentName = null;
            String buildingName = null;
            if(list1!=null && list1.size() > 0){
                BillItemDTO itemDTO = list1.get(0);
                apartmentName= itemDTO.getApartmentName();
                buildingName = itemDTO.getBuildingName();
            }
            //需要billGroup查看生成账单周期
            PaymentBillGroup group = getBillGroupById(billGroupId);
            List<String> dates = new ArrayList<>();
            Byte balanceDateType = group.getBalanceDateType();
            byte dueDayType = group.getDueDayType();
            Integer dueDay = group.getDueDay();
            Integer billsDay = group.getBillsDay();
            Calendar start = Calendar.getInstance();
            try{
                // 如果传递了计费开始时间
                if(dateStrBegin != null){
                    start.setTime(yyyyMMdd.parse(dateStrBegin));
                }else{
                    start.setTime(yyyyMM.parse(dateStr));
                    start.set(Calendar.DAY_OF_MONTH,start.getActualMinimum(Calendar.DAY_OF_MONTH));
                }
                dates.add(yyyyMMdd.format(start.getTime()));
                int cycle = 0;
                switch(balanceDateType){
                    case 2:
                        cycle = 1;
                        break;
                    case 3:
                        cycle = 3;
                        break;
                    case 4:
                        cycle = 12;
                        break;
                }
                start.add(Calendar.MONTH,cycle);
                if(cycle == 0){
                    //自然周期
                    start.set(Calendar.DAY_OF_MONTH,start.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                start.add(Calendar.DAY_OF_MONTH,-1);
                // 如果计费结束时间不是null，那么就应该设置为给定的
                if(dateStrEnd != null){
                    dates.add(dateStrEnd);
                }else{
                    dates.add(yyyyMMdd.format(start.getTime()));
                }
                start.add(Calendar.MONTH,1);
                start.set(Calendar.DAY_OF_MONTH,billsDay);
                dates.add(yyyyMMdd.format(start.getTime()));
                if(dueDayType == (byte)1){
                    start.add(Calendar.DAY_OF_MONTH,dueDay);
                }else if(dueDayType == (byte)0){
                    start.add(Calendar.MONTH,dueDay);
                }
                dates.add(yyyyMMdd.format(start.getTime()));
            }catch (Exception e){
                dates.add(null);
                dates.add(null);
                dates.add(null);
                dates.add(null);
                LOGGER.error(e.toString());
            }
            //需要组装的信息
            BigDecimal amountExemption = new BigDecimal("0");
            BigDecimal amountSupplement = new BigDecimal("0");
            BigDecimal amountReceivable = new BigDecimal("0");
            BigDecimal amountOwed = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");
            BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;//增加应收（不含税）
            BigDecimal amountOwedWithoutTax = BigDecimal.ZERO;//增加待收（不含税）
            BigDecimal taxAmount = BigDecimal.ZERO;//增加税额

            long nextBillId;
            if(billId != null) {
            	nextBillId = billId;
            }else {
            	nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
                if(nextBillId == 0){
                    nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
                }
            }

            if(list2!=null) {
                //bill exemption
                List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
//                long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class), list2.size());
                for(int i = 0; i < list2.size(); i++){
                    long currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
                    if(currentExemItemSeq == 0){
                        currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
                    }
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

                    if(amount.compareTo(zero)==-1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte)0)){
                        amount = amount.multiply(new BigDecimal("-1"));
                        amountExemption = amountExemption.add(amount);
                    }else if(amount.compareTo(zero)==1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte)1)){
                        amountSupplement = amountSupplement.add(amount);
                    }
                }
                //应收否应该计算减免项
//                amountReceivable = amountReceivable.subtract(amountExemption);
//                amountReceivable = amountReceivable.add(amountSupplement);
                amountOwed = amountOwed.subtract(amountExemption);
                amountOwed = amountOwed.add(amountSupplement);
                amountOwedWithoutTax = amountOwedWithoutTax.subtract(amountExemption);//待收（不含税）
                amountOwedWithoutTax = amountOwedWithoutTax.add(amountSupplement);//待收（不含税）
                EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
                exemptionItemsDao.insert(exemptionItems);
            }
            Byte billStatus = 0;
            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
            if(list1!=null){                
                for(int i = 0; i < list1.size() ; i++) {
                    long currentBillItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                    if(currentBillItemSeq == 0){
                        this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                    }
                    BillItemDTO dto = list1.get(i);
                    PaymentBillItems item = new PaymentBillItems();
                    item.setBillGroupRuleId(dto.getBillGroupRuleId());
                    item.setAddressId(dto.getAddressId());
                    item.setBuildingName(dto.getBuildingName());
                    item.setApartmentName(dto.getApartmentName());
                    BigDecimal var1 = dto.getAmountReceivable();
                    //减免项不覆盖收费项目的收付，暂时
                    var1 = DecimalUtils.negativeValueFilte(var1);
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
                    //时间假定
                    item.setDateStrBegin(dates.get(0));
                    item.setDateStrEnd(dates.get(1));
                    item.setId(currentBillItemSeq);
                    item.setNamespaceId(UserContext.getCurrentNamespaceId());
                    item.setOwnerType(ownerType);
                    item.setOwnerId(ownerId);
                    item.setContractId(contractId);
                    item.setContractNum(contractNum);
                    // item 也添加categoryId， 这样费用清单简单些
                    item.setCategoryId(categoryId);
                    if(targetType!=null){
                        item.setTargetType(targetType);
                    }
                    if(targetId != null) {
                        item.setTargetId(targetId);
                    }
                    item.setTargetName(targetName);
                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setEnergyConsume(dto.getEnergyConsume());//增加用量
                    BigDecimal var2 = dto.getAmountReceivableWithoutTax();
                    var2 = DecimalUtils.negativeValueFilte(var2);
                    item.setAmountOwedWithoutTax(var2);//增加待收（不含税）
                    item.setAmountReceivableWithoutTax(var2);//增加应收（不含税）
                    item.setAmountReceivedWithoutTax(new BigDecimal("0"));//增加已收（不含税）
                    item.setTaxAmount(dto.getTaxAmount());//增加税额
                    billItemsList.add(item);

                    amountReceivable = amountReceivable.add(var1);
                    amountOwed = amountOwed.add(var1);
                    amountReceivableWithoutTax = amountReceivableWithoutTax.add(var2);//增加应收（不含税）
                    amountOwedWithoutTax = amountOwedWithoutTax.add(var2);//增加应收（不含税）
                    taxAmount = taxAmount.add(dto.getTaxAmount());//增加税额
                }
            }
            
            //增加减免费项
            if(list3 != null) {
            	  List<com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
	              for(int i = 0; i < list3.size() ; i++) {
	                  long currentSubtractionItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
	                  if(currentSubtractionItemSeq == 0){
	                      this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
	                  }
	                  SubItemDTO dto = list3.get(i);
	                  PaymentSubtractionItem subtractionItem = new PaymentSubtractionItem();
	                  subtractionItem.setId(currentSubtractionItemSeq);
	                  subtractionItem.setNamespaceId(UserContext.getCurrentNamespaceId());
	                  subtractionItem.setCategoryId(categoryId);
	                  subtractionItem.setOwnerId(ownerId);
	                  subtractionItem.setOwnerType(ownerType);
	                  subtractionItem.setBillId(nextBillId);
	                  subtractionItem.setBillGroupId(billGroupId);
	                  subtractionItem.setSubtractionType(dto.getSubtractionType());
	                  subtractionItem.setChargingItemId(dto.getChargingItemId());
	                  subtractionItem.setChargingItemName(dto.getChargingItemName());
	                  subtractionItem.setCreatorUid(UserContext.currentUserId());
	                  subtractionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                  
	                  subtractionItemsList.add(subtractionItem);
	
	                  //根据减免费项配置重新计费，减免费项类型为eh_payment_bill_items才需要重新计费，减免费项类型为滞纳金新增无需重新计费
	                  if(subtractionItem != null && subtractionItem.getSubtractionType().equals(AssetSubtractionType.item.getCode())) {
	                	  if(billItemsList != null){
		                	  for(int j = 0; j < billItemsList.size() ; j++) {
		                		  PaymentBillItems item = (PaymentBillItems) billItemsList.get(j);
		                		  if(item.getChargingItemsId().equals(subtractionItem.getChargingItemId())) {
		                			  //如果收费项明细和减免费项的charginItemId相等，那么该费项金额应该从账单中减掉
		                			  amountOwed = amountOwed.subtract(item.getAmountReceivable());
		                			  amountOwedWithoutTax = amountOwedWithoutTax.subtract(item.getAmountReceivableWithoutTax());//增加待收（不含税）的计算
		                		  }
		                	  }
		                  }
	                  }
	              }
	              EhPaymentSubtractionItemsDao subtractionItemsDao = new EhPaymentSubtractionItemsDao(context.configuration());
	              subtractionItemsDao.insert(subtractionItemsList);
            }
            //新增附件
            List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList = cmd.getAssetPaymentBillAttachmentList();
            if(assetPaymentBillAttachmentList != null) {
          	  	  List<com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments> paymentBillAttachmentsList = new ArrayList<>();
	              for(int i = 0; i < assetPaymentBillAttachmentList.size() ; i++) {
	                  long currentSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
	                  if(currentSeq == 0){
	                      this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
	                  }
	                  AssetPaymentBillAttachment dto = assetPaymentBillAttachmentList.get(i);
	                  PaymentBillAttachments paymentBillAttachments = new PaymentBillAttachments();
	                  paymentBillAttachments.setId(currentSeq);
	                  paymentBillAttachments.setNamespaceId(UserContext.getCurrentNamespaceId());
	                  paymentBillAttachments.setCategoryId(categoryId);
	                  paymentBillAttachments.setOwnerId(ownerId);
	                  paymentBillAttachments.setOwnerType(ownerType);
	                  paymentBillAttachments.setBillId(nextBillId);
	                  paymentBillAttachments.setBillGroupId(billGroupId);
	                  paymentBillAttachments.setCreatorUid(UserContext.currentUserId());
	                  paymentBillAttachments.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                  paymentBillAttachments.setFilename(dto.getFilename());
	                  paymentBillAttachments.setContentUri(dto.getUri());
	                  paymentBillAttachments.setContentUrl(dto.getUrl());
	                  paymentBillAttachments.setContentType(dto.getContentType());
	                  paymentBillAttachmentsList.add(paymentBillAttachments);
	              }
	              EhPaymentBillAttachmentsDao paymentBillAttachmentsDao = new EhPaymentBillAttachmentsDao(context.configuration());
	              paymentBillAttachmentsDao.insert(paymentBillAttachmentsList);
            }

			//重新判断状态，如果待缴金额为0，则设置为已缴状态
//          if(amountOwed.compareTo(new BigDecimal("0"))!=1){
//               billStatus = 1;
//          }
            for(int i = 0; i < billItemsList.size(); i++) {
                billItemsList.get(i).setStatus(billStatus);
            }
            EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
            billItemsDao.insert(billItemsList);

            com.everhomes.server.schema.tables.pojos.EhPaymentBills newBill = new PaymentBills();
            //  缺少创造者信息，先保存在其他地方，比如持久化日志
            amountOwed = DecimalUtils.negativeValueFilte(amountOwed);
            newBill.setAmountOwed(amountOwed);
            amountOwedWithoutTax = DecimalUtils.negativeValueFilte(amountOwedWithoutTax);//增加待收（不含税）
            newBill.setAmountOwedWithoutTax(amountOwedWithoutTax);//增加待收（不含税）
//            if(amountOwed.compareTo(zero) == 0) {
//                newBill.setStatus((byte)1);
//            }else{
//                newBill.setStatus(billStatus);
//            }
            newBill.setStatus(billStatus);
            amountReceivable = DecimalUtils.negativeValueFilte(amountReceivable);
            amountReceivableWithoutTax = DecimalUtils.negativeValueFilte(amountReceivableWithoutTax);//增加应收（不含税）
            newBill.setAmountReceivable(amountReceivable);
            newBill.setAmountReceivableWithoutTax(amountReceivableWithoutTax);//增加应收（不含税）
            newBill.setTaxAmount(taxAmount);//增加税额
            newBill.setAmountReceived(zero);
            newBill.setAmountReceivedWithoutTax(zero);//增加已收不含税
            newBill.setAmountSupplement(amountSupplement);
            newBill.setAmountExemption(amountExemption);
            newBill.setBillGroupId(billGroupId);
            //时间
            newBill.setDateStr(dateStr);
            newBill.setDateStrBegin(dates.get(0));
            newBill.setDateStrEnd(dates.get(1));
            newBill.setDateStrDue(dates.get(2));
            newBill.setDueDayDeadline(dates.get(3));
            //新增时只填了一个楼栋门牌，所以也可以放到bill里去 by wentian 2018/4/24
            newBill.setBuildingName(buildingName);
            newBill.setApartmentName(apartmentName);

            //添加客户的手机号，用来之后定位用户 by wentian.V.Brytania 2018/4/13
            newBill.setCustomerTel(customerTel);

            newBill.setInvoiceNumber(invoiceNum);

            // added category Id
            newBill.setCategoryId(categoryId);
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

            if(isOwed == null){
                newBill.setChargeStatus((byte)0);
            }else{
                newBill.setChargeStatus(isOwed);
            }
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
        EhPaymentLateFine fine = Tables.EH_PAYMENT_LATE_FINE.as("fine");
        EhAddresses t1 = Tables.EH_ADDRESSES.as("t1");
        com.everhomes.server.schema.tables.EhPaymentSubtractionItems t2 = Tables.EH_PAYMENT_SUBTRACTION_ITEMS.as("t2");//增加减免费项
        ListBillDetailVO vo = new ListBillDetailVO();
        BillGroupDTO dto = new BillGroupDTO();
        List<BillItemDTO> list1 = new ArrayList<>();
        List<ExemptionItemDTO> list2 = new ArrayList<>();
        List<SubItemDTO> subItemDTOList = new ArrayList<SubItemDTO>();//增加减免费项

        context.select(r.ID,r.TARGET_ID,r.NOTICETEL,r.CUSTOMER_TEL,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM
                , r.INVOICE_NUMBER, r.BUILDING_NAME, r.APARTMENT_NAME, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT, r.STATUS, r.CONTRACT_ID, r.CONTRACT_NUM)
                .from(r)
                .where(r.ID.eq(billId))
                .fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    vo.setNoticeTel(f.getValue(r.NOTICETEL));//催缴联系号码
                    vo.setCustomerTel(f.getValue(r.CUSTOMER_TEL));//客户手机号
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setDateStrBegin(f.getValue(r.DATE_STR_BEGIN));//账单开始时间
                    vo.setDateStrEnd(f.getValue(r.DATE_STR_END));//账单结束时间
                    vo.setTargetName(f.getValue(r.TARGET_NAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
                    vo.setInvoiceNum(f.getValue(r.INVOICE_NUMBER));
//                    bill can has multiple addresses, thus one single address may confuse user
                    // still present addresses
                    vo.setBuildingName(f.getValue(r.BUILDING_NAME));
                    vo.setApartmentName(f.getValue(r.APARTMENT_NAME));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    vo.setAmoutExemption(f.getValue(r.AMOUNT_EXEMPTION));//总减免
                    vo.setAmountSupplement(f.getValue(r.AMOUNT_SUPPLEMENT));//总增收
                    vo.setBillStatus(f.getValue(r.STATUS));
                    vo.setContractId(f.getValue(r.CONTRACT_ID));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    return null;
                });
        context.select(o.CHARGING_ITEM_NAME,o.ID,o.AMOUNT_RECEIVABLE,t1.APARTMENT_NAME,t1.BUILDING_NAME, o.APARTMENT_NAME, o.BUILDING_NAME, o.CHARGING_ITEMS_ID
        		, o.ENERGY_CONSUME)
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
                    String apartFromAddr = f.getValue(t1.APARTMENT_NAME);
                    String buildingFromAddr = f.getValue(t1.BUILDING_NAME);
                    if(!org.jooq.tools.StringUtils.isBlank(apartFromAddr) || !org.jooq.tools.StringUtils.isBlank(buildingFromAddr)){
                        itemDTO.setApartmentName(apartFromAddr);
                        itemDTO.setBuildingName(buildingFromAddr);
                    }else{
                        itemDTO.setApartmentName(f.getValue(o.APARTMENT_NAME));
                        itemDTO.setBuildingName(f.getValue(o.BUILDING_NAME));
                    }
                    itemDTO.setChargingItemsId(f.getValue(o.CHARGING_ITEMS_ID));
                    itemDTO.setEnergyConsume(f.getValue(o.ENERGY_CONSUME));//费项增加用量字段
                    itemDTO.setItemFineType(AssetItemFineType.item.getCode());//增加费项类型字段
                    itemDTO.setItemType(AssetSubtractionType.item.getCode());//增加费项类型字段
                    list1.add(itemDTO);
                    return null;
                });
        //滞纳金
        List<BillItemDTO> fineList = new ArrayList<>();
        for(BillItemDTO item : list1){
            List<PaymentLateFine> fines = context.selectFrom(fine)
                .where(fine.BILL_ITEM_ID.eq(item.getBillItemId()))
                    .fetchInto(PaymentLateFine.class);
            for(PaymentLateFine n : fines){
                BillItemDTO nitem = ConvertHelper.convert(item, BillItemDTO.class);
                // 左邻convert为浅拷贝，第一层字段更改不会影响之前的
                nitem.setBillItemName(n.getName());
                nitem.setAmountReceivable(n.getAmount());
                nitem.setItemFineType(AssetItemFineType.lateFine.getCode());//增加费项类型字段
                nitem.setItemType(AssetSubtractionType.lateFine.getCode());//费项类型
                fineList.add(nitem);
            }
        }
        list1.addAll(fineList);

        context.select(t.AMOUNT,t.ID, t.REMARKS)
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
        //增加减免费项
        context.select(t2.ID,t2.SUBTRACTION_TYPE,t2.CHARGING_ITEM_ID, t2.CHARGING_ITEM_NAME)
	        .from(t2)
	        .where(t2.BILL_ID.eq(billId))
	        .fetch()
	        .map(f -> {
	        	SubItemDTO subItemDTO = new SubItemDTO();
	        	subItemDTO.setSubtractionType(f.getValue(t2.SUBTRACTION_TYPE));
	        	subItemDTO.setChargingItemId(f.getValue(t2.CHARGING_ITEM_ID));
	        	subItemDTO.setChargingItemName(f.getValue(t2.CHARGING_ITEM_NAME));
	        	subItemDTOList.add(subItemDTO);
	            return null;
        });
        
        dto.setBillItemDTOList(list1);
        dto.setExemptionItemDTOList(list2);
        dto.setSubItemDTOList(subItemDTOList);//增加减免费项
        vo.setBillGroupDTO(dto);
        //增加附件信息
        com.everhomes.server.schema.tables.EhPaymentBillAttachments attachments = Tables.EH_PAYMENT_BILL_ATTACHMENTS.as("attachments");
        List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList = new ArrayList<>();
        context.select(attachments.ID, attachments.FILENAME, attachments.CONTENT_URI, attachments.CONTENT_URL)
	        .from(attachments)
	        .where(attachments.BILL_ID.eq(billId))
	        .fetch()
	        .map(f -> {
	        	AssetPaymentBillAttachment assetPaymentBillAttachment = new AssetPaymentBillAttachment();
	        	assetPaymentBillAttachment.setFilename(f.getValue(attachments.FILENAME));
	        	assetPaymentBillAttachment.setUri(f.getValue(attachments.CONTENT_URI));
	        	assetPaymentBillAttachment.setUrl(f.getValue(attachments.CONTENT_URL));
	        	assetPaymentBillAttachmentList.add(assetPaymentBillAttachment);
	            return null;
	        });
        vo.setAssetPaymentBillAttachmentList(assetPaymentBillAttachmentList);
        return vo;
    }
    
    public ListBillDetailVO listBillDetailForPayment(Long billId, ListPaymentBillCmd cmd) {
    	if(cmd.getBillId() != null && !cmd.getBillId().equals(billId)) {
    		return null;
    	}
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
        //根据账单id查找所有的收费细项，并且拼装楼栋门牌
        vo.setAddresses("");//初始化
        EhPaymentBillItems o2 = Tables.EH_PAYMENT_BILL_ITEMS.as("o2");
        SelectQuery<Record> queryAddr = context.selectQuery();
        queryAddr.addSelect(o2.BUILDING_NAME,o2.APARTMENT_NAME);
        queryAddr.addFrom(o2);
        queryAddr.addConditions(o2.BILL_ID.eq(billId));
        queryAddr.fetch()
        	.map(f -> {
        		String newAddr = f.getValue(o2.BUILDING_NAME) + "/" + f.getValue(o2.APARTMENT_NAME);
        		if(f.getValue(o2.BUILDING_NAME) != null && f.getValue(o2.APARTMENT_NAME) != null && !vo.getAddresses().contains(newAddr)) {
        			String addresses = vo.getAddresses() + newAddr + ",";
        			vo.setAddresses(addresses);
        		}
        		return null;
        });
        String addresses = vo.getAddresses();
        if(addresses != null && addresses.length() > 0) {
        	addresses = addresses.substring(0, addresses.length() - 1);//去掉最后一个逗号 
        	vo.setAddresses(addresses);
        }
        //需根据收费项的楼栋门牌进行查询，不能直接根据账单的楼栋门牌进行查询
        String buildingName = cmd.getBuildingName();
        String apartmentName = cmd.getApartmentName();
        if(!org.springframework.util.StringUtils.isEmpty(buildingName) && !org.springframework.util.StringUtils.isEmpty(apartmentName)) {
        	String queryAddress = buildingName + "/" + apartmentName;
        	if(!addresses.contains(queryAddress)) {
        		return null;
        	}
        }
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID,r.TARGET_ID,r.NOTICETEL,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM
        		, r.INVOICE_NUMBER, r.BUILDING_NAME, r.APARTMENT_NAME, r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT);
        query.addFrom(r);
        query.addConditions(r.ID.eq(billId));
        if(cmd.getDateStrBegin() != null) {
        	query.addConditions(r.DATE_STR_BEGIN.greaterOrEqual(cmd.getDateStrBegin()));
        }
        if(cmd.getDateStrEnd() != null) {
        	query.addConditions(r.DATE_STR_END.lessOrEqual(cmd.getDateStrEnd()));
        }
        if(cmd.getTargetName() != null) {
        	query.addConditions(r.TARGET_NAME.like("%"+cmd.getTargetName()+"%"));
        }
        query.fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    vo.setNoticeTel(f.getValue(r.NOTICETEL));
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setDateStrBegin(f.getValue(r.DATE_STR_BEGIN));//账单开始时间
                    vo.setDateStrEnd(f.getValue(r.DATE_STR_END));//账单结束时间
                    vo.setTargetName(f.getValue(r.TARGET_NAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
                    vo.setInvoiceNum(f.getValue(r.INVOICE_NUMBER));
                    vo.setBuildingName(f.getValue(r.BUILDING_NAME));
                    vo.setApartmentName(f.getValue(r.APARTMENT_NAME));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);//应收
                    BigDecimal amoutExemption = f.getValue(r.AMOUNT_EXEMPTION);//减免
                    BigDecimal amountSupplement = f.getValue(r.AMOUNT_SUPPLEMENT);//增收
                    amountReceivable = amountReceivable.subtract(amoutExemption).add(amountSupplement);//应收=应收-减免+增收
                    if(amountReceivable.compareTo(BigDecimal.ZERO) > 0) {
                    	vo.setAmountReceivable(amountReceivable);
                    }else {
                    	vo.setAmountReceivable(BigDecimal.ZERO);
                    }
                    vo.setAmountReceived(f.getValue(r.AMOUNT_RECEIVED));//实收
                    vo.setAmoutExemption(amoutExemption);//减免
                    vo.setAmountSupplement(amountSupplement);//增收
                    String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS)
                    		.where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(f.getValue(r.BILL_GROUP_ID))).fetchOne(0,String.class);
                    vo.setBillGroupName(billGroupNameFound);
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
                    //包括滞纳金
                    getReadOnlyContext().select(Tables.EH_PAYMENT_LATE_FINE.AMOUNT)
                        .from(Tables.EH_PAYMENT_LATE_FINE)
                        .where(Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID.eq(itemDTO.getBillItemId()))
                        .fetch()
                        .forEach(rrr ->{
                            BigDecimal value = rrr.getValue(Tables.EH_PAYMENT_LATE_FINE.AMOUNT);
                            if(value != null){
                            	itemDTO.setLateFineAmount(value);
                            }
                    });
                    if(itemDTO.getLateFineAmount() == null) {
                    	itemDTO.setLateFineAmount(BigDecimal.ZERO);
                    }
                    list1.add(itemDTO);
                    return null;
                });
        context.select(t.AMOUNT,t.ID, t.REMARKS)
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
    public boolean checkBillByCategory(Long billId, Long categoryId) {
        DSLContext context = getReadOnlyContext();
        Long fetch = context.select(Tables.EH_PAYMENT_BILLS.CATEGORY_ID)
                .from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .fetchOne(Tables.EH_PAYMENT_BILLS.CATEGORY_ID);
        if(fetch != null && fetch.longValue() == categoryId.longValue()){
            return true;
        }
        return false;
    }

    public PaymentLateFine findLastedFine(Long id) {
         List<PaymentLateFine> list = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_LATE_FINE)
                .where(Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID.eq(id))
                 .orderBy(Tables.EH_PAYMENT_LATE_FINE.ID.desc())
                .fetchInto(PaymentLateFine.class);
         if(list.size() < 1) return null;
         return list.get(0);
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByDateStrs(String beginLimit, String endLimit, Long ownerId, String ownerType, Long categoryId) {
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
        query.addConditions(r.CATEGORY_ID.eq(categoryId));
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
    public List<BillStaticsDTO> listBillStaticsByChargingItems(String ownerType, Long ownerId,String beginLimit, String endLimit, Long categoryId) {
        List<BillStaticsDTO> list = new ArrayList<>();
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentBills t1 = Tables.EH_PAYMENT_BILLS.as("t1");
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> settledBillIds = context.select(t1.ID)
                .from(t1)
                .where(t1.SWITCH.eq((byte) 1))
                .and(t1.CATEGORY_ID.eq(categoryId))
                .fetch(t1.ID);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(DSL.sum(o.AMOUNT_RECEIVABLE),DSL.sum(o.AMOUNT_RECEIVED),DSL.sum(o.AMOUNT_OWED),t.NAME,t.ID);
//        query.addFrom(t,o);
        query.addFrom(t);
        query.addJoin(o);

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
        List<Long> itemIds = new ArrayList<>();
        query.fetch()
                .map(f -> {
                    BillStaticsDTO dto = new BillStaticsDTO();
                    dto.setAmountOwed(f.getValue(DSL.sum(o.AMOUNT_OWED)));
                    dto.setAmountReceivable(f.getValue(DSL.sum(o.AMOUNT_RECEIVABLE)));
                    dto.setAmountReceived(f.getValue(DSL.sum(o.AMOUNT_RECEIVED)));
                    dto.setValueOfX(f.getValue(t.NAME));
                    itemIds.add(f.getValue(t.ID));
                    list.add(dto);
                    return null;
                });
        for(int i = 0; i < list.size(); i++){
            String projectName = getProjectName(ownerId, ownerType, itemIds.get(i));
            if(projectName != null){
                list.get(i).setValueOfX(projectName);
            }

        }
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

    private String getProjectName(Long ownerId, String ownerType, Long aLong) {
        List<String> names = getReadOnlyContext().select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME)
                .from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.eq(aLong))
                .fetch(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME);
        if(names.size() > 1){
            return names.get(0);
        }
        return null;
    }

    @Override
    public List<BillStaticsDTO> listBillStaticsByCommunities(String dateStrBegin,String dateStrEnd,Integer currentNamespaceId, Long categoryId) {
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
        query.addConditions(t.CATEGORY_ID.eq(categoryId));
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
        //手动调整缴费状态时，需要将PAYMENT_TYPE置为0（线下支付）
        context.update(bill)
                .set(bill.STATUS,(byte)1)
                .set(bill.AMOUNT_RECEIVED,bill.AMOUNT_OWED)
                .set(bill.AMOUNT_OWED,new BigDecimal("0"))
                .set(bill.PAYMENT_TYPE,0)
                .where(bill.ID.eq(billId))
                .execute();
        //bill item
        context.update(item)
                .set(item.STATUS,(byte)1)
                .set(item.AMOUNT_RECEIVED,item.AMOUNT_OWED)
                .set(item.AMOUNT_OWED,new BigDecimal("0"))
                .where(item.BILL_ID.eq(billId))
                .execute();
        //bill exemption已经减到bill中了
    }

    @Override
    public List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId, Long categoryId) {
        List<ListChargingItemsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        List<PaymentChargingItem> items = context.selectFrom(t)
                .fetchInto(PaymentChargingItem.class);

        List<PaymentChargingItemScope> scopes = context.selectFrom(t1)
                .where(t1.OWNER_ID.eq(ownerId))
                .and(t1.OWNER_TYPE.eq(ownerType))
                .and(t1.CATEGORY_ID.eq(categoryId))
                .and(t1.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
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
                    dto.setTaxRate(scope.getTaxRate());//增加税率
                    isSelected = 0;
                }
            }
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId, Long categoryId) {
        List<ListChargingStandardsDTO> list = new ArrayList<>();

        EhPaymentChargingStandardsScopes standardScopeT = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScopeT");
        EhPaymentChargingStandards standardT = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("standardT");
        DSLContext context = getReadOnlyContext();

        List<Long> suggestPrices = new ArrayList<>();
        //tododone 这里来限定billcycle， 收费项目所属的ownerType，ownerId，namespace下的账单组的billcycle，来进行约束
        // 但一次性的可以带出来
        List<Byte> limitCyclses = context.select(Tables.EH_PAYMENT_BILL_GROUPS.BALANCE_DATE_TYPE)
                .from(Tables.EH_PAYMENT_BILL_GROUPS_RULES, Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERTYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID.eq(chargingItemId))
                // add category id to filter the right billing cycle fix #32202
                .and(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(categoryId))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(Tables.EH_PAYMENT_BILL_GROUPS.ID))
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS.BALANCE_DATE_TYPE);
       // limiteCycles
        context.select()
                .from(standardScopeT,standardT)
                .where(standardT.ID.eq(standardScopeT.CHARGING_STANDARD_ID))
                .and(standardScopeT.OWNER_ID.eq(ownerId))
                .and(standardScopeT.OWNER_TYPE.eq(ownerType))
                .and(standardT.CHARGING_ITEMS_ID.eq(chargingItemId))
                // add category id constraint
                .and(standardScopeT.CATEGORY_ID.eq(categoryId))
                .and(standardT.BILLING_CYCLE.in(limitCyclses).or(standardT.BILLING_CYCLE.eq(BillingCycle.ONE_DEAL.getCode())))
                .fetch()
                .map(r -> {
                    ListChargingStandardsDTO dto = new ListChargingStandardsDTO();
                    dto.setChargingStandardName(r.getValue(standardT.NAME));
                    dto.setBillingCycle(r.getValue(standardT.BILLING_CYCLE));
                    dto.setFormula(r.getValue(standardT.FORMULA));
                    dto.setFormulaType(r.getValue(standardT.FORMULA_TYPE));
                    dto.setChargingStandardId(r.getValue(standardT.ID));
                    Byte priceUnitType = r.getValue(standard.PRICE_UNIT_TYPE);
                    if(priceUnitType != null){
                        //fixed as using day unit price
                        dto.setUseUnitPrice((byte)1);
                    }else{
                        dto.setUseUnitPrice((byte)0);
                    }
                    suggestPrices.add(r.getValue(standardT.SUGGEST_UNIT_PRICE)==null?null:r.getValue(standardT.SUGGEST_UNIT_PRICE).longValue());
                    list.add(dto);
                    return null;
                });
        List<List<String>> formus = new ArrayList<>();
        List<List<String>> conditionVarIdens = new ArrayList<>();
        for(int i =0; i < list.size(); i++){
            ListChargingStandardsDTO dto = list.get(i);
            List<String> formu = new ArrayList<>();
            List<String> conditionVarIden = new ArrayList<>();
            context.select(Tables.EH_PAYMENT_FORMULA.FORMULA_JSON,Tables.EH_PAYMENT_FORMULA.CONSTRAINT_VARIABLE_IDENTIFER)
                    .from(Tables.EH_PAYMENT_FORMULA)
                    .where(Tables.EH_PAYMENT_FORMULA.CHARGING_STANDARD_ID.eq(dto.getChargingStandardId()))
                    .fetch()
                    .map(r -> {
                        formu.add(r.getValue(Tables.EH_PAYMENT_FORMULA.FORMULA_JSON));
                        conditionVarIden.add(r.getValue(Tables.EH_PAYMENT_FORMULA.CONSTRAINT_VARIABLE_IDENTIFER));
                        return null;
                    });

            formus.add(formu);
            conditionVarIdens.add(conditionVarIden);
        }
        //从公式中取得参数值
        for(int j = 0; j < formus.size(); j++){
            ListChargingStandardsDTO dto = list.get(j);
            List<String> formulaJsons = formus.get(j);
            Set<String> replaces = new HashSet<>();
            for(int i =0; i < formulaJsons.size();i++){
                String formulaJson = formulaJsons.get(i);
                if(formulaJson!=null){
                    char[] formularChars = formulaJson.toCharArray();
                    int index = 0;
                    int start = 0;
                    while(index < formularChars.length){
                        if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'||formularChars[index]=='/'||index == formularChars.length-1){
                            String substring = formulaJson.substring(start, index == formulaJson.length() - 1 ? index + 1 : index);
                            if(!hasDigit(substring)){
                                replaces.add(substring);
                            }
                            start = index+1;
                        }
                        index++;
                    }
                }
            }
            List<String> conditionVarIden = conditionVarIdens.get(j);
            for(int i = 0; i < conditionVarIden.size(); i ++){
                replaces.add(conditionVarIden.get(i));
            }
            List<PaymentVariable> vars = new ArrayList<>();
            Iterator<String> iterator = replaces.iterator();
            while(iterator.hasNext()){
                PaymentVariable var = new PaymentVariable();
                String varIden = iterator.next();
                if(varIden == null ){
                    continue;
                }
                String varName = getNameByVariableIdenfitier(varIden);
                if(StringUtils.isBlank(varName)){
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_GENERAL_EXCEPTION,"formula variable name does not exist in schema");
                }
                var.setVariableIdentifier(varIden);
                var.setVariableName(varName);
                if(varIden.equals("dj")){
                    var.setVariableValue(suggestPrices.get(j)==null?null:new BigDecimal(suggestPrices.get(j)));
                }
                vars.add(var);
            }
            if(vars.size()>0){
                dto.setVariables(vars);
            }else{
                dto.setVariables(null);
            }
        }
        //把水费和电费的用量干掉
        //现在把公摊/自用水电费干掉，用contains，如果不想用中文，就用variableIdentifier by wentian 2018/4/20
        String itemName = context.select(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME)
                .from(Tables.EH_PAYMENT_CHARGING_ITEMS)
                .where(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(chargingItemId))
                .fetchOne(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME);
        if(itemName.contains(AssetPaymentConstants.CHARGING_ITEM_NAME_WATER) || itemName.contains(AssetPaymentConstants.CHARGING_ITEM_NAME_ELECTRICITY)) {
            for( int i = 0; i < list.size(); i ++){
                ListChargingStandardsDTO dto = list.get(i);
                List<PaymentVariable> variables = dto.getVariables();
                for(int j = 0; j < variables.size(); j ++){
                    PaymentVariable variable = variables.get(j);
                    if(variable.getVariableName().equals(AssetPaymentConstants.VARIABLE_YJ)){
                        variables.remove(j);
                        j--;
                    }
                    //把比例系数干掉
                    if(variable.getVariableName().equals(AssetPaymentConstants.VARIABLE_BLXS)){
                        variables.remove(j);
                        j--;
                    }
                }
            }
        }
        return list;
    }

    private String getNameByVariableIdenfitier(String varIden) {
        DSLContext context = getReadOnlyContext();
        EhPaymentVariables variable = Tables.EH_PAYMENT_VARIABLES.as("variable");
        return context.select(variable.NAME)
                .from(variable)
                .where(variable.IDENTIFIER.eq(varIden))
                .fetchOne(variable.NAME);
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
    public void modifyNotSettledBill(ModifyNotSettledBillCommand cmd) {
    	//卸载参数
    	Long billId = cmd.getBillId();
    	BillGroupDTO billGroupDTO = cmd.getBillGroupDTO();
    	String targetType = cmd.getTargetType();
    	Long targetId = cmd.getTargetId();
    	String targetName = cmd.getTargetName();
    	String invoiceNum = cmd.getInvoiceNum();
    	String noticeTel = cmd.getNoticeTel();
    	Long categoryId = cmd.getCategoryId();
    	String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
            EhPaymentBillItems t1 = Tables.EH_PAYMENT_BILL_ITEMS.as("t1");
            EhPaymentExemptionItems t2 = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t2");
            com.everhomes.server.schema.tables.EhPaymentSubtractionItems t3 = Tables.EH_PAYMENT_SUBTRACTION_ITEMS.as("t3");
            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();
            List<SubItemDTO> subItemDTOList = billGroupDTO.getSubItemDTOList();//增加减免费项
            //需要组装的信息
            BigDecimal amountExemption = new BigDecimal("0");
            BigDecimal amountSupplement = new BigDecimal("0");
            BigDecimal amountReceivable = new BigDecimal("0");
            BigDecimal amountChange = new BigDecimal("0");
            BigDecimal zero = new BigDecimal("0");
            //更新收费项（更新费项之前先删除原来的）
//            context.delete(Tables.EH_PAYMENT_BILL_ITEMS)
//	            .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
//	            .execute();
//            if(list1!=null){
//                for(int i = 0; i < list1.size() ; i++) {
//                    BillItemDTO dto = list1.get(i);
//                    //只对常规费项做新增操作，对滞纳金费项不做处理
//                    if(dto.getItemFineType().equals(AssetItemFineType.item.getCode())) {
//                    	List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
//                    	long currentBillItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
//                        if(currentBillItemSeq == 0){
//                            this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
//                        }
//                        PaymentBillItems item = new PaymentBillItems();
//                        item.setBillGroupRuleId(dto.getBillGroupRuleId());
//                        item.setAddressId(dto.getAddressId());
//                        item.setBuildingName(dto.getBuildingName());
//                        item.setApartmentName(dto.getApartmentName());
//                        BigDecimal var1 = dto.getAmountReceivable();
//                        //减免项不覆盖收费项目的收付，暂时
//                        var1 = DecimalUtils.negativeValueFilte(var1);
//                        item.setAmountOwed(var1);
//                        item.setAmountReceivable(var1);
//                        item.setAmountReceived(new BigDecimal("0"));
//                        item.setBillGroupId(billGroupId);
//                        item.setBillId(billId);
//                        item.setChargingItemName(dto.getBillItemName());
//                        item.setChargingItemsId(dto.getBillItemId());
//                        item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                        item.setCreatorUid(UserContext.currentUserId());
//                        item.setDateStr(dateStr);
//                        //时间假定
//                        item.setDateStrBegin(dates.get(0));
//                        item.setDateStrEnd(dates.get(1));
//                        item.setId(currentBillItemSeq);
//                        item.setNamespaceId(UserContext.getCurrentNamespaceId());
//                        item.setOwnerType(ownerType);
//                        item.setOwnerId(ownerId);
//                        item.setContractId(contractId);
//                        item.setContractNum(contractNum);
//                        //item 也添加categoryId， 这样费用清单简单些
//                        item.setCategoryId(categoryId);
//                        if(targetType!=null){
//                            item.setTargetType(targetType);
//                        }
//                        if(targetId != null) {
//                            item.setTargetId(targetId);
//                        }
//                        item.setTargetName(targetName);
//                        item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                        item.setEnergyConsume(dto.getEnergyConsume());//增加用量
//                        billItemsList.add(item);
//                    	EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
//                    	billItemsDao.insert(billItemsList);
//                    }
//                }
//            }
            List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
            //减免项列表list2
            List<Long> includeExemptionIds = new ArrayList();
            includeExemptionIds.add(-1l);
            if(list2!=null){
                //bill exemption
                for(int i = 0; i < list2.size(); i++){
                    ExemptionItemDTO exemptionItemDTO = list2.get(i);
                    //有id的
                    if(exemptionItemDTO.getExemptionId()!=null){
                        includeExemptionIds.add(exemptionItemDTO.getExemptionId());
                        context.update(t2)
                                .set(t2.AMOUNT,exemptionItemDTO.getAmount()==null?new BigDecimal("0"):exemptionItemDTO.getAmount())
                                .set(t2.REMARKS,exemptionItemDTO.getRemark())
                                .set(t2.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                                .set(t2.OPERATOR_UID,UserContext.currentUserId())
                                .where(t2.BILL_ID.eq(billId))
                                .and(t2.ID.eq(exemptionItemDTO.getExemptionId()))
                                .execute();
                    }else{
                        //没有id的，为新增的
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
                        includeExemptionIds.add(nextId);
                    }
                }
            }
            EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
            exemptionItemsDao.insert(exemptionItems);
            //删除include进来之外的增收减免
            context.delete(t2)
                    .where(t2.ID.notIn(includeExemptionIds))
                    .and(t2.BILL_ID.eq(billId))
                    .execute();
            
            //修改减免费项配置
            //先删除：根据billId删除该账单原来的减免费项配置
            context.delete(t3)
                    .where(t3.BILL_ID.eq(billId))
                    .execute();
            List<com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
            if(subItemDTOList != null) {
                //后插入：新增修改后的配置
            	for(int i = 0; i < subItemDTOList.size(); i++){
            		long currentSubtractionItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
	                if(currentSubtractionItemSeq == 0){
	                   this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
	                }
	                SubItemDTO dto = subItemDTOList.get(i);
            		PaymentSubtractionItem subtractionItem = new PaymentSubtractionItem();
            		subtractionItem.setId(currentSubtractionItemSeq);
            		subtractionItem.setNamespaceId(UserContext.getCurrentNamespaceId());
            		subtractionItem.setCategoryId(categoryId);
            		subtractionItem.setOwnerId(ownerId);
            		subtractionItem.setOwnerType(ownerType);
            		subtractionItem.setBillId(billId);
            		subtractionItem.setBillGroupId(billGroupId);
            		subtractionItem.setSubtractionType(dto.getSubtractionType());
            		subtractionItem.setChargingItemId(dto.getChargingItemId());
            		subtractionItem.setChargingItemName(dto.getChargingItemName());
            		subtractionItem.setCreatorUid(UserContext.currentUserId());
            		subtractionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            		subtractionItemsList.add(subtractionItem);
            	}
            	EhPaymentSubtractionItemsDao subtractionItemsDao = new EhPaymentSubtractionItemsDao(context.configuration());
	            subtractionItemsDao.insert(subtractionItemsList);
            }
            
            //更新附件（先删除原来的附件）
            context.delete(Tables.EH_PAYMENT_BILL_ATTACHMENTS)
	            .where(Tables.EH_PAYMENT_BILL_ATTACHMENTS.BILL_ID.eq(billId))
	            .execute();
            List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList = cmd.getAssetPaymentBillAttachmentList();
            if(assetPaymentBillAttachmentList != null) {
          	  	  List<com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments> paymentBillAttachmentsList = new ArrayList<>();
	              for(int i = 0; i < assetPaymentBillAttachmentList.size() ; i++) {
	                  long currentSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
	                  if(currentSeq == 0){
	                      this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
	                  }
	                  AssetPaymentBillAttachment dto = assetPaymentBillAttachmentList.get(i);
	                  PaymentBillAttachments paymentBillAttachments = new PaymentBillAttachments();
	                  paymentBillAttachments.setId(currentSeq);
	                  paymentBillAttachments.setNamespaceId(UserContext.getCurrentNamespaceId());
	                  paymentBillAttachments.setCategoryId(categoryId);
	                  paymentBillAttachments.setOwnerId(ownerId);
	                  paymentBillAttachments.setOwnerType(ownerType);
	                  paymentBillAttachments.setBillId(billId);
	                  paymentBillAttachments.setBillGroupId(billGroupId);
	                  paymentBillAttachments.setCreatorUid(UserContext.currentUserId());
	                  paymentBillAttachments.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                  paymentBillAttachments.setFilename(dto.getFilename());
	                  paymentBillAttachments.setContentUri(dto.getUri());
	                  paymentBillAttachments.setContentUrl(dto.getUrl());
	                  paymentBillAttachments.setContentType(dto.getContentType());
	                  paymentBillAttachmentsList.add(paymentBillAttachments);
	              }
	              EhPaymentBillAttachmentsDao paymentBillAttachmentsDao = new EhPaymentBillAttachmentsDao(context.configuration());
	              paymentBillAttachmentsDao.insert(paymentBillAttachmentsList);
            }
            
            reCalBillById(billId);//重新计算账单
            // 更新发票
            context.update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.INVOICE_NUMBER, invoiceNum)
                    .set(Tables.EH_PAYMENT_BILLS.NOTICETEL, noticeTel)
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            return null;
        });
    }

    public void modifyBillForImport(Long billId, CreateBillCommand cmd) {
    	deleteBillForImport(billId);//先删除原来的账单
		creatPropertyBill(cmd, billId);//新增一个账单，账单id为原来的账单id
        reCalBillById(billId);//重新计算账单
    }
    
    public void deleteBillForImport(Long billId) {
    	this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            int execute = context.delete(Tables.EH_PAYMENT_BILLS)
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            if(execute == 0){
                throw new RuntimeException("删除账单失败，无法找到此账单");
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
    public List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId,Long ownerId,String ownerType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<VariableIdAndValue> list = new ArrayList<>();
//<<<<<<< HEAD
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        String variableJson = context.select(t.VARIABLES_JSON_STRING)
                .from(t)
                .where(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
                .and(t.OWNERID.eq(ownerId))
                .and(t.OWNERTYPE.eq(ownerType))
                .fetchOne(0, String.class);
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(variableJson, Map.class);
        for(Map.Entry entry : map.entrySet()){
            VariableIdAndValue vid = new VariableIdAndValue();
            vid.setVariableValue(entry.getValue());
            vid.setVariableId(entry.getKey());
            list.add(vid);
//=======
//
//
//        com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards standard = findChargingStandardById(chargingStandardId);
//        String formulaJson = standard.getFormulaJson();
//
//        //获得不重复的字母的变量标识的集合
//
//        String formulaCopy = formulaJson;
//        Set<String> variableIdentifiers = new HashSet<>();
//        int index = 0;
//        getVaraibleIdenInHashset(formulaCopy, variableIdentifiers, index);
//        Iterator<String> iterator = variableIdentifiers.iterator();
//        while(iterator.hasNext()){
//            String iden = iterator.next();
//            com.everhomes.server.schema.tables.pojos.EhPaymentVariables varia = findVariableByIden(iden);
//            VariableIdAndValue viv = new VariableIdAndValue();
//            viv.setVaribleIdentifier(iden);
//            viv.setVariableId(varia.getId());
//            if(viv.equals("dj")){
//                viv.setVariableValue(standard.getSuggestUnitPrice());
//            }
//            list.add(viv);
//>>>>>>> payment_wentian_2.0
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
    public List<PaymentBillGroupRule> getBillGroupRule(Long chargingItemId, Long chargingStandardId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        List<PaymentBillGroupRule> rules = context.select()
                .from(t)
                .where(t.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(t.OWNERTYPE.eq(ownerType))
                .and(t.OWNERID.eq(ownerId))
                .fetch()
                .map(r -> ConvertHelper.convert(r, PaymentBillGroupRule.class));
//        if(rules.size() > 1){
//            List<PaymentBillGroupRule> rules2 = context.select()
//                    .from(t)
//                    .where(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
//                    .and(t.OWNERTYPE.eq(ownerType))
//                    .and(t.OWNERID.eq(ownerId))
//                    .fetch()
//                    .map(r -> ConvertHelper.convert(r, PaymentBillGroupRule.class));
//            return rules2.get(0);
//        }
        return rules;
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
                    .set(t.SWITCH,t.NEXT_SWITCH)
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
        this.coordinationProvider.getNamedLock(contractId.toString()).enter(() -> {
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
                        .execute();
                context.delete(t1)
                        .where(t1.CONTRACT_ID.eq(contractId))
                        .execute();
                return null;
            });
            return null;
        });
    }

    @Override
    public List<PaymentExpectancyDTO> listBillExpectanciesOnContract(String contractNum, Integer pageOffset, Integer pageSize,Long contractId, Long categoryId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
        Set<PaymentExpectancyDTO> set = new LinkedHashSet<>();

        List<Long> fetch = context.select(bill.ID)
                .from(bill)
                .where(bill.CONTRACT_NUM.eq(contractNum))
                .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .fetch(bill.ID);
        context.select(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.DATE_STR_BEGIN,t.DATE_STR_END,t.DATE_STR_DUE,t.AMOUNT_RECEIVABLE,t1.NAME,t1.ID)
                .from(t,t1)
                .where(t.BILL_ID.in(fetch))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.NAME,t.DATE_STR)
                .limit(pageOffset,pageSize+1)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();

                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setPropertyIdentifier(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
                    dto.setDueDateStr(r.getValue(t.DATE_STR_DUE));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setChargingItemName(r.getValue(t1.NAME));
                    dto.setBillItemId(r.getValue(t.ID));
                    dto.setChargingItemId(r.getValue(t1.ID));
                    set.add(dto);
                    return null;
                });

        List<Long> fetch1 = context.select(bill.ID)
                .from(bill)
                .where(bill.CONTRACT_ID.eq(contractId))
                .and(bill.NAMESPACE_ID.eq(namespaceId))//解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .fetch(bill.ID);
        context.select(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.DATE_STR_BEGIN,t.DATE_STR_END,t.DATE_STR_DUE,t.AMOUNT_RECEIVABLE,t1.NAME,t1.ID)
                .from(t,t1)
                .where(t.BILL_ID.in(fetch1))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.NAME,t.DATE_STR)
                .limit(pageOffset,pageSize+1)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setPropertyIdentifier(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
                    dto.setDueDateStr(r.getValue(t.DATE_STR_DUE));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setChargingItemName(r.getValue(t1.NAME));
                    dto.setBillItemId(r.getValue(t.ID));
                    dto.setChargingItemId(r.getValue(t1.ID));
                    set.add(dto);
                    return null;
                });
        return set.stream().collect(Collectors.toList());
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
                .where(t.ID.eq(billId))
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
                .where(t.ID.eq(billId))
                .execute();
        if(amount.compareTo(new BigDecimal("0"))<0){
            context.update(t)
                    .set(t.AMOUNT_EXEMPTION,t.AMOUNT_EXEMPTION.add(amount))
                    .where(t.ID.eq(billId))
                    .execute();
        }else{
            context.update(t)
                    .set(t.AMOUNT_SUPPLEMENT,t.AMOUNT_SUPPLEMENT.sub(amount))
                    .where(t.ID.eq(billId))
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
    public Long saveAnOrderCopy(String payerType, String payerId, String amountOwed, String clientAppName, Long communityId, String contactNum, String openid, String payerName,Long expireTimePeriod,Integer namespaceId,String orderType) {
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
        order.setOrderType(orderType);

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
    public List<AssetPaymentOrder> findAssetOrderByBillId(String billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        HashSet<Long> idSet = new HashSet<>();
        Long id = null;
        return context.select(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.ORDER_ID)
                .from(Tables.EH_ASSET_PAYMENT_ORDER_BILLS)
                .where(Tables.EH_ASSET_PAYMENT_ORDER_BILLS.BILL_ID.eq(billId))
                .fetchInto(AssetPaymentOrder.class);
    }

    @Override
    public PaymentBills findPaymentBillById(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<PaymentBills> list =  context.selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .fetchInto(PaymentBills.class);
        if(list.size()>0){
            return list.get(0);
        }else{
            return null;
        }

    }

    @Override
    public List<Long> findbillIdsByOwner(Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_PAYMENT_BILLS.ID);
        query.addFrom(Tables.EH_PAYMENT_BILLS);
        query.addConditions(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId));
        if(ownerType!=null){
            query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType));
        }
        if(ownerId!=null){
            query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId));
        }
        return query.fetch(Tables.EH_PAYMENT_BILLS.ID);
    }

    @Override
    public void saveOrderBills(List<BillIdAndAmount> bills, Long orderId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long nextBlockSequence = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrderBills.class),bills.size());
        long nextSequence = nextBlockSequence - bills.size()+1;
        List<com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrderBills> orderBills = new ArrayList<>();
        for(int i = 0; i < bills.size(); i ++){
            com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrderBills orderBill  = new com.everhomes.server.schema.tables.pojos.EhAssetPaymentOrderBills();
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
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAssetPaymentOrderBills t = Tables.EH_ASSET_PAYMENT_ORDER_BILLS.as("t");
        return context.selectFrom(t)
                .where(t.ORDER_ID.eq(orderId))
                .fetchInto(AssetPaymentOrderBills.class);
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
    
    public void changeOrderPaymentType(Long orderId, Integer paymentType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhAssetPaymentOrder t = Tables.EH_ASSET_PAYMENT_ORDER.as("t");
        context.update(t)
                .set(t.PAYMENT_TYPE,paymentType != null ? paymentType.toString() : "")
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
/*
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
        //更改金钱
        context.update(t)
                .set(t.AMOUNT_RECEIVED,t.AMOUNT_OWED)
                .set(t.AMOUNT_OWED,new BigDecimal("0"))
                .where(t.ID.in(billIds))
                .execute();
        context.update(t1)
                .set(t1.AMOUNT_RECEIVED,t1.AMOUNT_OWED)
                .set(t1.AMOUNT_OWED,new BigDecimal("0"))
                .where(t1.BILL_ID.in(billIds))
                .execute();
    }
    */
    
    @Override
    public void changeBillStatusAndPaymentTypeOnPaiedOff(List<Long> billIds,Integer paymentType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t1 = Tables.EH_PAYMENT_BILL_ITEMS.as("t1");
        //更新订单状态，记录订单的支付方式
        context.update(t)
                .set(t.STATUS,(byte)1)
                .set(t.PAYMENT_TYPE,paymentType)
                .where(t.ID.in(billIds))
                .execute();
        //更新各收费项的状态
        context.update(t1)
                .set(t1.STATUS,(byte)1)
                .where(t1.BILL_ID.in(billIds))
                .execute();
        //更改金钱
        context.update(t)
                .set(t.AMOUNT_RECEIVED,t.AMOUNT_OWED)
                .set(t.AMOUNT_OWED,BigDecimal.ZERO)
                .where(t.ID.in(billIds))
                .execute();
        context.update(t1)
                .set(t1.AMOUNT_RECEIVED,t1.AMOUNT_OWED)
                .set(t1.AMOUNT_OWED,BigDecimal.ZERO)
                .where(t1.BILL_ID.in(billIds))
                .execute();

    }

    @Override
    public void configChargingItems(ConfigChargingItemsCommand cmd, List<Long> communityIds) {
        //卸载参数
    	List<ConfigChargingItems> configChargingItems = cmd.getChargingItemConfigs();
    	Long communityId = cmd.getOwnerId();
    	String ownerType = cmd.getOwnerType();
    	Integer namespaceId = cmd.getNamespaceId();
    	Long categoryId = cmd.getCategoryId();
    	
    	byte de_coupling = 1;
        if(communityIds!=null && communityIds.size() >1){
            for(int i = 0; i < communityIds.size(); i ++){
                Long cid = communityIds.get(i);
//                只要园区还有自己的scope，且一个scope的独立权得到承认，那么不能修改
                Boolean coupled = true;
                if(cid.longValue() != namespaceId.longValue()){
                    coupled = checkCoupling(cid,ownerType, categoryId);
                }
                if(coupled){
                    de_coupling = 0;
                    configChargingItemForOneCommunity(configChargingItems, cid, ownerType, namespaceId, de_coupling, categoryId);
                }
            }
        }else{
            //只有一个园区,不是list过来的
            configChargingItemForOneCommunity(configChargingItems, communityId, ownerType, namespaceId, de_coupling, categoryId);
        }
    }

    private Boolean checkCoupling(Long communityId, String ownerType, Long categoryId) {
        DSLContext context = getReadOnlyContext();
        List<Byte> flags = context.select(itemScope.DECOUPLING_FLAG)
                .from(itemScope)
                .where(itemScope.OWNER_TYPE.eq(ownerType))
                .and(itemScope.OWNER_ID.eq(communityId))
                .and(itemScope.CATEGORY_ID.eq(categoryId))
                .fetch(itemScope.DECOUPLING_FLAG);
        for(int i = 0; i < flags.size(); i ++){
            if(flags.get(i).byteValue() == (byte)1){
                return false;
            }
        }
        return true;
    }

    private void configChargingItemForOneCommunity(List<ConfigChargingItems> configChargingItems, Long communityId, String ownerType, Integer namespaceId, Byte decouplingFlag, Long categoryId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingItemScopes t = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t");
        EhPaymentChargingItemScopesDao dao = new EhPaymentChargingItemScopesDao(context.configuration());
        List<com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes> list = new ArrayList<>();
        if(configChargingItems == null){
            context.delete(t)
                    .where(t.OWNER_TYPE.eq(ownerType))
                    .and(t.OWNER_ID.eq(communityId))
                    // add categoryId constraint
                    .and(t.CATEGORY_ID.eq(categoryId))
                    .execute();
            return;
        }
        for(int i = 0; i < configChargingItems.size(); i ++) {
            ConfigChargingItems vo = configChargingItems.get(i);
            PaymentChargingItemScope scope = new PaymentChargingItemScope();
            scope.setChargingItemId(vo.getChargingItemId());
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes.class));
            scope.setId(nextSequence);
            scope.setNamespaceId(namespaceId);
            scope.setOwnerId(communityId);
            scope.setOwnerType(ownerType);
            scope.setCategoryId(categoryId);
            scope.setProjectLevelName(vo.getProjectChargingItemName());
            scope.setDecouplingFlag(decouplingFlag);
            scope.setDecouplingFlag(decouplingFlag);
            scope.setTaxRate(vo.getTaxRate());//增加税率
            list.add(scope);
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            context.delete(t)
                    .where(t.OWNER_TYPE.eq(ownerType))
                    .and(t.OWNER_ID.eq(communityId))
                    .and(t.CATEGORY_ID.eq(categoryId))
                    .execute();
            if(list.size()>0){
                dao.insert(list);
            }
            return null;
        });
    }

    @Override
    public void createChargingStandard(com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards c, com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes s, List<com.everhomes.server.schema.tables.pojos.EhPaymentFormula> f) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//        if(c!=null){
            EhPaymentChargingStandardsDao ChargingStandardsDao = new EhPaymentChargingStandardsDao(context.configuration());
            ChargingStandardsDao.insert(c);
//        }
//        if(s != null){
            EhPaymentChargingStandardsScopesDao chargingStandardsScopesDao = new EhPaymentChargingStandardsScopesDao(context.configuration());
            chargingStandardsScopesDao.insert(s);
//        }
//        if(f!=null){
            EhPaymentFormulaDao paymentFormulaDao = new EhPaymentFormulaDao(context.configuration());
            paymentFormulaDao.insert(f);
//        }
    }

    @Override
    public void modifyChargingStandard(Long chargingStandardId,String chargingStandardName,String instruction,byte deCouplingFlag,String ownerType,Long ownerId, Byte useUnitPrice) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
        if(deCouplingFlag == (byte)1){
            //去解耦
            Long nullId = null;
            context.update(t)
                    .set(t.NAME,chargingStandardName)
                    .set(t.INSTRUCTION,instruction)
                    .where(t.ID.eq(chargingStandardId))
                    .execute();
            context.update(standardScope)
                    .set(standardScope.BROTHER_STANDARD_ID,nullId)
                    .where(standardScope.OWNER_TYPE.eq(ownerType))
                    .and(standardScope.OWNER_ID.eq(ownerId))
                    .execute();
        }else if(deCouplingFlag == (byte)0 ) {
            //bro和本人是 chargingStandardId的进行修改
            List<Long> fetch = context.select(standardScope.CHARGING_STANDARD_ID)
                    .from(standardScope)
                    .where(standardScope.BROTHER_STANDARD_ID.eq(chargingStandardId))
                    .or(standardScope.CHARGING_STANDARD_ID.eq(chargingStandardId))//修复issue-29576 收费项计算规则-标准名称不能修改
                    .fetch(standardScope.CHARGING_STANDARD_ID);
            UpdateQuery<EhPaymentChargingStandardsRecord> query = context.updateQuery(t);
            query.addValue(t.NAME, chargingStandardName);
            if(useUnitPrice != null && useUnitPrice.byteValue() == 1){
                query.addValue(t.PRICE_UNIT_TYPE, (byte)1);
            }
            query.addValue(t.INSTRUCTION, instruction);
            query.addConditions(t.ID.in(fetch));
            query.execute();
        }
    }

    @Override
    public GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd) {
        DSLContext context = getReadOnlyContext();
        GetChargingStandardDTO dto = new GetChargingStandardDTO();
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        context.select(t.NAME,t.FORMULA,t.BILLING_CYCLE,t.INSTRUCTION,t.FORMULA_TYPE,t.SUGGEST_UNIT_PRICE,t.AREA_SIZE_TYPE, t.PRICE_UNIT_TYPE)
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
                    Byte priceUnitPrice = r.getValue(t.PRICE_UNIT_TYPE);
                    if(priceUnitPrice != null){
                        dto.setUseUnitPrice((byte)1);
                    }else{
                        dto.setUseUnitPrice((byte)0);
                    }
                    return null;
                });
        return dto;
    }

    @Override
    public void deleteChargingStandard(DeleteChargingStandardCommand cmd, byte deCouplingFlag) {
    	//卸载参数
    	Long chargingStandardId = cmd.getChargingStandardId();
    	Long ownerId = cmd.getOwnerId();
    	String ownerType = cmd.getOwnerType();
    	Long categoryId = cmd.getCategoryId();
    	
        DSLContext context = getReadWriteContext();
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
        com.everhomes.server.schema.tables.EhPaymentFormula formula = Tables.EH_PAYMENT_FORMULA.as("formula");
        Long nullId = null;
        if(deCouplingFlag == (byte)1){
            //去解耦
            context.delete(standard)
                    .where(standard.ID.eq(chargingStandardId))
                    .execute();
//            Long bro = context.select(standardScope.BROTHER_STANDARD_ID)
//                    .from(standardScope)
//                    .where(standardScope.CHARGING_STANDARD_ID.eq(chargingStandardId))
//                    .fetchOne(standardScope.BROTHER_STANDARD_ID);
            //if(bro!=null){
            //issue-34458 在具体项目新增一条标准（自定义的标准），“注：该项目使用默认配置”文案不消失，刷新也不消失
            //只要做了删除动作，那么该项目下的配置全部解耦
                context.update(standardScope)
                        .set(standardScope.BROTHER_STANDARD_ID,nullId)
                        .where(standardScope.OWNER_ID.eq(ownerId))
                        .and(standardScope.OWNER_TYPE.eq(ownerType))
                        .and(standardScope.CATEGORY_ID.eq(categoryId))
                        .execute();
            //}
            context.delete(standardScope)
                    .where(standardScope.CHARGING_STANDARD_ID.eq(chargingStandardId))
                    .execute();
            context.delete(formula)
                    .where(formula.CHARGING_STANDARD_ID.eq(chargingStandardId))
                    .execute();
        }else if(deCouplingFlag == (byte)0){
            //耦合
            List<Long> standardIds = context.select(standardScope.CHARGING_STANDARD_ID)
                    .from(standardScope)
                    .where(standardScope.BROTHER_STANDARD_ID.eq(chargingStandardId))
                    .fetch(standardScope.CHARGING_STANDARD_ID);
            standardIds.add(chargingStandardId);
            context.delete(standard)
                    .where(standard.ID.eq(chargingStandardId))
                    .execute();
            context.delete(standardScope)
                    .where(standardScope.CHARGING_STANDARD_ID.in(standardIds))
                    .execute();
            context.delete(formula)
                    .where(formula.CHARGING_STANDARD_ID.in(standardIds))
                    .execute();
        }
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
    public Long createBillGroup(CreateBillGroupCommand cmd,byte deCouplingFlag,Long brotherGroupId) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        Long nullId = null;
        if(deCouplingFlag == (byte) 1){
            //去解耦
            //添加
            Long nextGroupId = getNextSequence(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups.class);
            InsertBillGroup(cmd, brotherGroupId, context, t, nextGroupId);
            //去解耦同伴
            context.update(t)
                    .set(t.BROTHER_GROUP_ID,nullId)
                    .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .execute();
            return nextGroupId;
        }else if(deCouplingFlag == (byte)0){
        	//添加
            Long nextGroupId = getNextSequence(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups.class);
            if(cmd.getNamespaceId().equals(cmd.getOwnerId().intValue())){//ownerId为-1代表选择的是全部
            	InsertBillGroup(cmd, brotherGroupId, context, t, nextGroupId);
            }else {
            	//全部配置要同步到具体项目的时候，首先要判断一下该项目是否解耦了，如果解耦了，则不需要
            	IsProjectNavigateDefaultCmd isProjectNavigateDefaultCmd = new IsProjectNavigateDefaultCmd();
            	isProjectNavigateDefaultCmd.setModuleType(AssetModuleType.GROUPS.getCode());
            	isProjectNavigateDefaultCmd.setNamespaceId(cmd.getNamespaceId());
            	isProjectNavigateDefaultCmd.setOwnerId(cmd.getOwnerId());
            	isProjectNavigateDefaultCmd.setOwnerType(cmd.getOwnerType());
            	IsProjectNavigateDefaultResp isProjectNavigateDefaultResp = assetService.isProjectNavigateDefault(isProjectNavigateDefaultCmd);
            	if(isProjectNavigateDefaultResp.getDefaultStatus().equals((byte)1)) {//1：代表使用默认配置，那么需要同步
                    InsertBillGroup(cmd, brotherGroupId, context, t, nextGroupId);
            	}
            }
            return nextGroupId;
        }
        return null;
    }

    private void InsertBillGroup(CreateBillGroupCommand cmd, Long brotherGroupId, DSLContext context, EhPaymentBillGroups t, Long nextGroupId) {
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = new PaymentBillGroup();
        group.setId(nextGroupId);
        group.setBalanceDateType(cmd.getBillingCycle());
        group.setBillsDay(cmd.getBillDay());
        group.setBillsDayType(cmd.getBillDayType());
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setCreatorUid(UserContext.currentUserId());
        group.setBrotherGroupId(brotherGroupId);
        Integer nextOrder = context.select(DSL.max(t.DEFAULT_ORDER))
                .from(t)
                .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                .fetchOne(0,Integer.class);
        group.setDefaultOrder(nextOrder==null?1:nextOrder+1);
        group.setDueDay(cmd.getDueDay());
        group.setDueDayType(cmd.getDueDayType());
        group.setName(cmd.getBillGroupName());
        group.setNamespaceId(cmd.getNamespaceId());
        group.setOwnerId(cmd.getOwnerId());
        group.setOwnerType(cmd.getOwnerType());
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setBizPayeeId(cmd.getBizPayeeId());//增加收款方id
        group.setBizPayeeType(cmd.getBizPayeeType());//增加收款方类型
        group.setCategoryId(cmd.getCategoryId());
        EhPaymentBillGroupsDao dao = new EhPaymentBillGroupsDao(context.configuration());
        dao.insert(group);
    }

    @Override
    public void modifyBillGroup(ModifyBillGroupCommand cmd,byte deCouplingFlag) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        Map updates = new HashMap<>();
        updates.put(t.NAME,cmd.getBillGroupName());
        updates.put(t.BILLS_DAY,cmd.getBillDay());
        updates.put(t.BALANCE_DATE_TYPE,cmd.getBillingCycle());
        updates.put(t.DUE_DAY,cmd.getDueDay());
        updates.put(t.DUE_DAY_TYPE,cmd.getDueDayType());
        updates.put(t.BIZ_PAYEE_ID,cmd.getBizPayeeId());//更新收款方账户id
        updates.put(t.BIZ_PAYEE_TYPE,cmd.getBizPayeeType());//更新收款方账户类型
        if(cmd.getBillDayType()!= null){
            updates.put(t.BILLS_DAY_TYPE, cmd.getBillDayType());
        }
        if(deCouplingFlag == (byte)0){
            UpdateQuery<EhPaymentBillGroupsRecord> query = context.updateQuery(t);
            query.addValues(updates);
            query.addConditions(t.ID.eq(cmd.getBillGroupId()).or(t.BROTHER_GROUP_ID.eq(cmd.getBillGroupId())));
            query.execute();
            return;
        }
        Long nullId = null;
        context.update(t)
                .set(t.BROTHER_GROUP_ID,nullId)
                .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                .execute();
        UpdateQuery<EhPaymentBillGroupsRecord> query = context.updateQuery(t);
        query.addValues(updates);
        query.addConditions(t.ID.eq(cmd.getBillGroupId()));
        query.execute();
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
        query.addSelect(t.BILLING_CYCLE,t.ID,t.NAME,t.FORMULA,t.FORMULA_TYPE,t.SUGGEST_UNIT_PRICE,t.AREA_SIZE_TYPE, t.PRICE_UNIT_TYPE);
        query.addFrom(t,t1);
        query.addConditions(t.CHARGING_ITEMS_ID.eq(cmd.getChargingItemId()));
        query.addConditions(t1.CHARGING_STANDARD_ID.eq(t.ID));
        query.addConditions(t1.OWNER_ID.eq(cmd.getOwnerId()));
        query.addConditions(t1.OWNER_TYPE.eq(cmd.getOwnerType()));
        //add category constraint
        query.addConditions(t1.CATEGORY_ID.eq(cmd.getCategoryId()));
        query.addLimit(cmd.getPageAnchor().intValue(),cmd.getPageSize()+1);
        query.fetch().map(r -> {
            ListChargingStandardsDTO dto = new ListChargingStandardsDTO();
            dto.setBillingCycle(r.getValue(t.BILLING_CYCLE));
            dto.setChargingStandardId(r.getValue(t.ID));
            dto.setChargingStandardName(r.getValue(t.NAME));
            dto.setFormula(r.getValue(t.FORMULA));
            dto.setFormulaType(r.getValue(t.FORMULA_TYPE));
            dto.setSuggestUnitPrice(r.getValue(t.SUGGEST_UNIT_PRICE));
            dto.setAreaSizeType(r.getValue(t.AREA_SIZE_TYPE));
            Byte priceUnitType = r.getValue(t.PRICE_UNIT_TYPE);
            if(priceUnitType != null){
                //fixed as using unit price of day
                dto.setUseUnitPrice((byte)1);
            }else{
                dto.setUseUnitPrice((byte)0);
            }
            list.add(dto);
            return null;
        });
        for(int i = 0; i < list.size(); i ++){
            ListChargingStandardsDTO dto = list.get(i);
            Long chargingStandardId = dto.getChargingStandardId();
            //获得标准
            com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards standard = findChargingStandardById(chargingStandardId);
            Set<String> varIdens = new HashSet<>();
            //获得formula的额外内容
            List<PaymentFormula> formulaCondition = null;
            if(standard.getFormulaType()==3 || standard.getFormulaType() == 4){
                formulaCondition = getFormulas(standard.getId());
                for(int m = 0; m < formulaCondition.size(); m ++){
                    varIdens.add(findVariableByIden(formulaCondition.get(m).getConstraintVariableIdentifer()).getName());
                }
            }
            //获得standard公式
            String formula = null;
            if(standard.getFormulaType()==1 || standard.getFormulaType() == 2){
                formulaCondition = getFormulas(standard.getId());
                if(formulaCondition!=null){
                    if(formulaCondition.size()>1){
                        LOGGER.error("普通公式的标准的id为"+standard.getId()+",对应了"+formulaCondition.size()+"条公式!");
                    }
                    PaymentFormula paymentFormula = formulaCondition.get(0);
                    formula = paymentFormula.getFormulaJson();
                }else{
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"找不到公式,标准的id为"+standard.getId()+"");
                }
            }
            if(formula!=null){
                char[] formularChars = formula.toCharArray();
                int index = 0;
                int start = 0;
                while(index < formularChars.length){
                    if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'||formularChars[index]=='/'||index == formularChars.length-1){
                        String var = formula.substring(start,index==formula.length()-1?index+1:index);
                        if(!IntegerUtil.hasDigit(var)){
                            varIdens.add(findVariableByIden(var).getName());
                        }
                        start = index+1;
                    }
                    index++;
                }

            }
            dto.setVariableNames(new ArrayList<>(varIdens));
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
    public List<ListChargingItemsForBillGroupDTO> listChargingItemsForBillGroup(Long billGroupId,Long pageAnchor,Integer pageSize) {
        List<ListChargingItemsForBillGroupDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        EhPaymentChargingItemScopes t4 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t4");

        List<PaymentBillGroupRule> rules = context.selectFrom(t)
                .where(t.BILL_GROUP_ID.eq(billGroupId))
                .limit(pageAnchor.intValue(),pageSize+1)
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
                    .and(t4.CATEGORY_ID.eq(group.getCategoryId()))
                    .fetchOne(t4.PROJECT_LEVEL_NAME);
            if(StringUtils.isBlank(ItemName)){
                ItemName = context.select(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME)
                        .from(Tables.EH_PAYMENT_CHARGING_ITEMS)
                        .where(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(rule.getChargingItemId()))
                        .fetchOne(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME);
            }
            dto.setBillingCycle(group.getBalanceDateType());

//            PaymentChargingStandards standard = context.selectFrom(t3)
//                    .where(t3.ID.eq(rule.getChargingStandardsId()))
//                    .fetchOneInto(PaymentChargingStandards.class);
//            dto.setFormula(standard.getFormula());
            dto.setProjectChargingItemName(ItemName==null?"":ItemName);
//            dto.setChargingStandardName(standard.getName());
            dto.setBillItemGenerationMonth(rule.getBillItemMonthOffset());
            dto.setBillItemGenerationDay(rule.getBillItemDayOffset());
            if(rule.getBillItemMonthOffset() == null){
                dto.setBillItemGenerationType((byte)3);
            }else if(rule.getBillItemDayOffset() == null){
                dto.setBillItemGenerationType((byte)1);
            }else{
                dto.setBillItemGenerationType((byte)2);
            }
            list.add(dto);
        }
        return list;
    }

    @Override
    public Long addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd,Long brotherRuleId,byte deCouplingFlag) {
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups group = Tables.EH_PAYMENT_BILL_GROUPS.as("group");
        DSLContext readOnlyContext = getReadOnlyContext();
        DSLContext writeContext = getReadWriteContext();
        EhPaymentBillGroupsRulesDao dao = new EhPaymentBillGroupsRulesDao(writeContext.configuration());
        //先获得cateogryId
        Long categoryId = readOnlyContext.select(group.CATEGORY_ID).from(group).where(group.ID.eq(cmd.getBillGroupId()))
                .fetchOne(group.CATEGORY_ID);
        if(deCouplingFlag == (byte) 0){
            //耦合中
            return insertOrUpdateBillGroupRule(cmd, brotherRuleId, t, readOnlyContext, dao,deCouplingFlag, categoryId);
        }else if(deCouplingFlag == (byte) 1){
            //解耦合
            insertOrUpdateBillGroupRule(cmd, brotherRuleId, t, readOnlyContext, dao,deCouplingFlag, categoryId);
            Long nullId = null;
            writeContext.update(t)
                    .set(t.BROTHER_RULE_ID,nullId)
                    .where(t.OWNERID.eq(cmd.getOwnerId()))
                    .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                    .execute();
            writeContext.update(group)
                    .set(group.BROTHER_GROUP_ID,nullId)
                    .where(group.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(group.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .execute();
        }
        return null;
//        AddOrModifyRuleForBillGroupResponse response = new AddOrModifyRuleForBillGroupResponse();
//        Long ruleId = cmd.getBillGroupRuleId();
//        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = readOnlyContext.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
//                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(cmd.getBillGroupId())).fetchOneInto(PaymentBillGroup.class);
//
//        List<Long> fetch = readOnlyContext.select(t.CHARGING_ITEM_ID).from(t).where(t.OWNERID.eq(group.getOwnerId()))
//                .and(t.OWNERTYPE.eq(group.getOwnerType()))
//                .fetch(t.CHARGING_ITEM_ID);
//        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = new PaymentBillGroupRule();
//        if(ruleId == null){
//            if(fetch.contains(cmd.getChargingItemId())){
//                response.setFailCause(AssetPaymentConstants.CREATE_CHARGING_ITEM_FAIL);
//                return response;
//            }
//            //新增 一条billGroupRule
//            long nextRuleId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class));
//            ruleId = nextRuleId;
//            rule.setId(nextRuleId);
//            rule.setBillGroupId(cmd.getBillGroupId());
//            rule.setChargingItemId(cmd.getChargingItemId());
//            rule.setChargingItemName(cmd.getGroupChargingItemName());
//            rule.setChargingStandardsId(cmd.getChargingStandardId());
//            rule.setNamespaceId(group.getNamespaceId());
//            rule.setOwnerid(group.getOwnerId());
//            rule.setOwnertype(group.getOwnerType());
//            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
//            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
//            dao.insert(rule);
////            response.setFailCause(AssetPaymentConstants.SAVE_SUCCESS);
//        }else{
//            //拿到正确的rule
////            if(cmd.getOwnerId().longValue() == cmd.getNamespaceId().longValue()){
//            rule = readOnlyContext.selectFrom(t)
//                    .where(t.ID.eq(ruleId))
//                    .fetchOneInto(PaymentBillGroupRule.class);
////            }else{
////                rule = readOnlyContext.selectFrom(t)
////                        .where(t.OWNERID.eq(group.getOwnerId()))
////                        .and(t.OWNERTYPE.eq(group.getOwnerType()))
////                        .and(t.BROTHER_RULE_ID.eq(ruleId))
////                        .fetchOneInto(PaymentBillGroupRule.class);
////            }
//            boolean workFlag = isInWorkGroupRule(rule);
//            if(workFlag){
//                response.setFailCause(AssetPaymentConstants.MODIFY_GROUP_RULE_UNSAFE);
//                return response;
//            }
//            //如果没有关联则不修改
//            rule.setBillGroupId(cmd.getBillGroupId());
//            rule.setChargingItemId(cmd.getChargingItemId());
//            rule.setChargingItemName(cmd.getGroupChargingItemName());
//            rule.setChargingStandardsId(cmd.getChargingStandardId());
//            rule.setNamespaceId(group.getNamespaceId());
//            rule.setOwnerid(group.getOwnerId());
//            rule.setOwnertype(group.getOwnerType());
//            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
//            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
//            dao.update(rule);
////            response.setFailCause(AssetPaymentConstants.MODIFY_SUCCESS);
//        }
//        Long nullId = null;
//        writeContext.update(Tables.EH_PAYMENT_BILL_GROUPS)
//                .set(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID,nullId)
//                .where(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.eq(cmd.getOwnerId()))
//                .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_TYPE.eq(cmd.getOwnerType()))
//                .execute();
//        return response;
    }

    private Long insertOrUpdateBillGroupRule(AddOrModifyRuleForBillGroupCommand cmd, Long brotherRuleId, EhPaymentBillGroupsRules t, DSLContext readOnlyContext, EhPaymentBillGroupsRulesDao dao,byte deCouplingFlag, Long categoryId) {
        Long ruleId = cmd.getBillGroupRuleId();
//        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = readOnlyContext.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
//                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(cmd.getBillGroupId())).fetchOneInto(PaymentBillGroup.class);
//
//        List<Long> fetch = readOnlyContext.select(t.CHARGING_ITEM_ID).from(t).where(t.OWNERID.eq(group.getOwnerId()))
//                .and(t.OWNERTYPE.eq(group.getOwnerType()))
//                .fetch(t.CHARGING_ITEM_ID);
        List<Long> fetch = readOnlyContext.select(t.CHARGING_ITEM_ID).from(t, Tables.EH_PAYMENT_BILL_GROUPS)
                .where(t.OWNERID.eq(cmd.getOwnerId()))
                .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                .and(t.BILL_GROUP_ID.eq(Tables.EH_PAYMENT_BILL_GROUPS.ID))
                .and(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(categoryId))
                .fetch(t.CHARGING_ITEM_ID);
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = new PaymentBillGroupRule();
        if(ruleId == null){
            if(fetch.contains(cmd.getChargingItemId())){
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.UNIQUE_BILL_ITEM_CHECK,"a bill item can only exists in one bill group for a specific community");
            }
            //新增 一条billGroupRule
            long nextRuleId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class));
            ruleId = nextRuleId;
            Long billGroupId = cmd.getBillGroupId();
            //拿到正确的billGroupId
            if(deCouplingFlag == 0 && cmd.getOwnerId().intValue() != cmd.getNamespaceId().intValue()){
                List<Long> groupIds = readOnlyContext.select(Tables.EH_PAYMENT_BILL_GROUPS.ID)
                        .from(Tables.EH_PAYMENT_BILL_GROUPS)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID.eq(cmd.getBillGroupId()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_TYPE.eq(cmd.getOwnerType()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.eq(cmd.getOwnerId()))
                        .fetch(Tables.EH_PAYMENT_BILL_GROUPS.ID);
                if(groupIds.size()<0 || groupIds.size() > 1){
                    LOGGER.error("add group rule for coupled communities, "+groupIds.size()+" brother groups are found, not one which leads to a violation to the law here");
                    return null;
                }
                billGroupId = groupIds.get(0);
            }
            rule.setId(nextRuleId);
            rule.setBillGroupId(billGroupId);
            rule.setChargingItemId(cmd.getChargingItemId());
            rule.setChargingItemName(cmd.getGroupChargingItemName());
            rule.setChargingStandardsId(cmd.getChargingStandardId());
            rule.setNamespaceId(cmd.getNamespaceId());
            rule.setOwnerid(cmd.getOwnerId());
            rule.setOwnertype(cmd.getOwnerType());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            rule.setBrotherRuleId(brotherRuleId);
            dao.insert(rule);
//            response.setFailCause(AssetPaymentConstants.SAVE_SUCCESS);
        }else{
            //拿到正确的rule
            if(cmd.getOwnerId().longValue() == cmd.getNamespaceId().longValue() || deCouplingFlag == 1){
                rule = readOnlyContext.selectFrom(t)
                        .where(t.ID.eq(ruleId))
                        .fetchOneInto(PaymentBillGroupRule.class);
            }else{
                rule = readOnlyContext.selectFrom(t)
                        .where(t.OWNERID.eq(cmd.getOwnerId()))
                        .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                        .and(t.BROTHER_RULE_ID.eq(ruleId))
                        .fetchOneInto(PaymentBillGroupRule.class);
            }
            boolean workFlag = isInWorkGroupRule(rule);
            if(workFlag){
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.CHANGE_SAFE_CHECK,"object is on work, modify or delete is not allowed.");
            }
            //如果没有关联则不修改
//            rule.setBillGroupId(rule.getBillGroupId());
            rule.setChargingItemId(cmd.getChargingItemId());
//            rule.setChargingItemName(cmd.getGroupChargingItemName());
//            rule.setChargingStandardsId(cmd.getChargingStandardId());
//            rule.setNamespaceId(cmd.getNamespaceId());
//            rule.setOwnerid(cmd.getOwnerId());
//            rule.setOwnertype(cmd.getOwnerType());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            rule.setBrotherRuleId(brotherRuleId);
            dao.update(rule);
//            response.setFailCause(AssetPaymentConstants.MODIFY_SUCCESS);
        }
        return ruleId;
    }

    @Override
    public com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules findBillGroupRuleById(Long billGroupRuleId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                .fetchOneInto(PaymentBillGroupRule.class);
    }
    @Override
    public boolean isInWorkGroupRule(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule) {
        // todo change the way to examine bill group validation, if not bill and not valid contract, work flag should be false
        boolean workFlag = true;
        DSLContext context = getReadOnlyContext();
        EhPaymentContractReceiver t = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t");
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
        //看是否关联了合同

        // 合同的状态需要限定 by wentian 2018/5/22
        List<Long> fetch1 = context.select(t.CONTRACT_ID)
                .from(t)
                .where(t.OWNER_ID.eq(rule.getOwnerid()))
                .and(t.OWNER_TYPE.eq(rule.getOwnertype()))
                .and(t.NAMESPACE_ID.eq(rule.getNamespaceId()))
                .and(t.EH_PAYMENT_CHARGING_ITEM_ID.eq(rule.getChargingItemId()))
                .fetch(t.CONTRACT_ID);
        if(fetch1.size()>0){
            for(Long cid : fetch1){
                //合同是否为正常
              if(contractProvider.isNormal(cid)){
                 return true;
              }
            }
            // 和isNOrmal重复了
//            Long correlatedContractId = fetch1.get(0);
//            Byte contractStatus = contractProvider.findContractById(correlatedContractId).getStatus();
//            ContractStatus status = ContractStatus.fromStatus(contractStatus);
//            switch (status){
//                case INVALID:
//                   workFlag = false;
//                case INACTIVE:
//                    workFlag = false;
//                case DENUNCIATION:
//                    workFlag = false;
//                case EXPIRED:
//                    workFlag = false;
//                case HISTORY:
//                    workFlag = false;
//            }
        }else{
            workFlag = false;
        }
        //先看是否产生了账单
        List<Long> fetch = context.select(bills.ID)
                .from(bills)
                .where(bills.BILL_GROUP_ID.eq(rule.getBillGroupId()))
                .and(bills.SWITCH.notEqual((byte)3))
                //todo 限定条件应该排除已经缴纳的账单，但已经缴纳的账单的需要增加一个账单组历史名称,且兼容历史数据，即数据迁移
                .fetch(bills.ID);
        if(fetch.size()>0){
            workFlag = true;
        }else{
            workFlag = false;
        }
        return workFlag;
    }

    @Override
    public DeleteChargingItemForBillGroupResponse deleteBillGroupRuleById(Long billGroupRuleId,byte deCouplingFlag) {
        DeleteChargingItemForBillGroupResponse response = new DeleteChargingItemForBillGroupResponse();
        DSLContext context = getReadWriteContext();
        if(deCouplingFlag == 0){
            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules> rules = context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                    .or(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BROTHER_RULE_ID.eq(billGroupRuleId))
                    .fetchInto(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class);
            rules:for(int i = 0; i < rules.size(); i ++){
                boolean workFlag = isInWorkGroupRule(rules.get(i));
                if(workFlag){
                    continue rules;
                }
                context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(rules.get(i).getId()))
                        .execute();
            }
        }else{
            com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = findBillGroupRuleById(billGroupRuleId);
            boolean workFlag = isInWorkGroupRule(rule);
            if(workFlag){
                response.setFailCause(AssetPaymentConstants.DELETE_GROUP_RULE_UNSAFE);
                return response;
            }
            context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                    .execute();
        }
        return response;
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
        List<PaymentBillGroupRule> rules = context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(billGroupId))
                .fetchInto(PaymentBillGroupRule.class);
        for(int i = 0; i < rules.size(); i ++) {
            boolean inWorkGroupRule = isInWorkGroupRule(rules.get(i));
            if(inWorkGroupRule){
                return true;
            }
        }
        return  false;

    }

    @Override
    public DeleteBillGroupReponse deleteBillGroupAndRules(Long billGroupId,byte deCouplingFlag,String ownerType,Long ownerId) {
        DeleteBillGroupReponse response = new DeleteBillGroupReponse();
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        if(deCouplingFlag == (byte)0){
            List<Long> groupIds = context.select(t1.ID)
                    .from(t1)
                    .where(t1.ID.eq(billGroupId))
                    .or(t1.BROTHER_GROUP_ID.eq(billGroupId))
                    .fetch(t1.ID);
            ids:for( int i = 0; i < groupIds.size(); i ++){
                boolean workFlag = checkBillsByBillGroupId(groupIds.get(i));
                if(workFlag){
//                    response.setFailCause(AssetPaymentConstants.DELTE_GROUP_UNSAFE);
//                    return response;
                    continue ids;
                }
                int finalI = i;
                this.dbProvider.execute((TransactionStatus status) -> {
                    context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                            .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(groupIds.get(finalI)))
                            .execute();

                    context.delete(Tables.EH_PAYMENT_BILL_GROUPS)
                            .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(groupIds.get(finalI)))
                            .execute();
                    return null;
                });
            }
        }else if(deCouplingFlag == (byte)1){
            boolean workFlag = checkBillsByBillGroupId(billGroupId);
            if(workFlag) {
                response.setFailCause(AssetPaymentConstants.DELTE_GROUP_UNSAFE);
                return response;
            }
            //删除会导致其他同伴解耦，解决issue-32616:删除从全部继承过来的账单组，具体显示还是显示了“注：该项目使用默认配置”文案
            Long nullId = null;
            this.dbProvider.execute((TransactionStatus status) -> {
                context.delete(t)
                        .where(t.BILL_GROUP_ID.eq(billGroupId))
                        .execute();
                context.delete(t1)
                        .where(t1.ID.eq(billGroupId))
                        .execute();
                context.update(t1)
                        .set(t1.BROTHER_GROUP_ID,nullId)
                        .where(t1.OWNER_ID.eq(ownerId))
                        .and(t1.OWNER_TYPE.eq(ownerType))
                        .execute();
                return null;
            });
        }
        return response;
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
//        PaymentChargingStandards standard = context.selectFrom(t)
//                .where(t.ID.eq(rule.getChargingStandardsId()))
//                .fetchOneInto(PaymentChargingStandards.class);
//        dto.setBillCycle(standard.getBillingCycle());
        dto.setBillGroupRuleId(rule.getId());
        dto.setChargingItemId(rule.getChargingItemId());
//        dto.setChargingStandardId(standard.getId());
        dto.setDayOffset(rule.getBillItemDayOffset());
        dto.setMonthOffset(rule.getBillItemMonthOffset());
         if(rule.getBillItemMonthOffset() == null){
                dto.setBillItemGenerationType((byte)3);
            }else if(rule.getBillItemDayOffset() == null){
                dto.setBillItemGenerationType((byte)1);
            }else{
                dto.setBillItemGenerationType((byte)2);
            }
//        dto.setFormula(standard.getFormula());
        dto.setGroupChargingItemName(rule.getChargingItemName());
        return dto;
    }

    @Override
    public List<ListChargingItemsDTO> listAvailableChargingItems(OwnerIdentityCommand cmd) {
        List<ListChargingItemsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        List<PaymentChargingItemScope> scopes = context.select(t1.fields())
                .from(Tables.EH_PAYMENT_BILL_GROUPS_RULES,t1, Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID.eq(t1.CHARGING_ITEM_ID))
                .and(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID))
                .and(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(cmd.getCategoryId()))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERTYPE.eq(cmd.getOwnerType()))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERID.eq(cmd.getOwnerId()))
                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t1.OWNER_ID.eq(cmd.getOwnerId()))
                // add category filter by wentian sama
                .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
                .and(t1.CHARGING_ITEM_ID.notEqual(AssetPaymentConstants.LATE_FINE_ID))
                .fetchInto(PaymentChargingItemScope.class);
        for(int j = 0; j < scopes.size(); j ++){
            ListChargingItemsDTO dto = new ListChargingItemsDTO();
            PaymentChargingItemScope scope = scopes.get(j);
            dto.setChargingItemName(scope.getProjectLevelName());
            dto.setChargingItemId(scope.getChargingItemId());
            dto.setTaxRate(scope.getTaxRate());//增加税率
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<PaymentFormula> getFormulas(Long id) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_FORMULA)
                .where(Tables.EH_PAYMENT_FORMULA.CHARGING_STANDARD_ID.eq(id))
                .fetchInto(PaymentFormula.class);
    }

    @Override
    public boolean cheackGroupRuleExistByChargingStandard(Long chargingStandardId,String ownerType,Long ownerId) {
        DSLContext context = getReadOnlyContext();
        Long itemId = context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS.CHARGING_ITEMS_ID)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOne(Tables.EH_PAYMENT_CHARGING_STANDARDS.CHARGING_ITEMS_ID);
        List<Long> fetch = context.select(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID)
                .from(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID.eq(itemId))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERTYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERID.eq(ownerId))
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID);
        if(fetch.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public void setInworkFlagInContractReceiver(Long contractId,String contractNum) {
        DSLContext writeContext = getReadWriteContext();
        PaymentContractReceiver cr = new PaymentContractReceiver();
        long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentContractReceiver.class));
        cr.setId(nextSequence);
        cr.setContractId(contractId);
        cr.setContractNum(contractNum);
        cr.setInWork((byte)1);
        cr.setIsRecorder((byte)1);
        EhPaymentContractReceiverDao dao = new EhPaymentContractReceiverDao(writeContext.configuration());
        dao.insert(cr);
    }

    @Override
    public void setInworkFlagInContractReceiverWell(Long contractId) {
        DSLContext writeContext = getReadWriteContext();
        PaymentContractReceiver cr = new PaymentContractReceiver();
        EhPaymentContractReceiver contract = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("contract");
        writeContext.update(contract)
                .set(contract.IN_WORK,(byte)0)
                .where(contract.CONTRACT_ID.eq(contractId))
                .execute();
    }

    @Override
    public Boolean checkContractInWork(Long contractId,String contractNum) {
        EhPaymentContractReceiver contract = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("contract");
        DSLContext context = getReadOnlyContext();
        List<Byte> aByte = context.select(contract.IN_WORK)
                .from(contract)
                .where(contract.IS_RECORDER.eq((byte) 1))
                .and(contract.CONTRACT_NUM.eq(contractNum))
                .fetch(contract.IN_WORK);
        if( aByte.size() == 0 ){
             aByte = context.select(contract.IN_WORK)
                    .from(contract)
                    .where(contract.IS_RECORDER.eq((byte) 1))
                    .and(contract.CONTRACT_ID.eq(contractId))
                    .fetch(contract.IN_WORK);
        }
//<<<<<<< HEAD
//        if ( aByte == null) {
//        	return false;
//            //throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.FAIL_IN_GENERATION,"mission failed");
        if ( aByte.size() == 0) {
            //throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.FAIL_IN_GENERATION,"mission failed");
        	return false;
        }
        if( aByte.get(aByte.size()-1) == (byte)0){
            return false;
        }
        return true;
    }

//    @Override
//    public List<Long> deleteAllChargingStandardScope(Long ownerId, String ownerType) {
//        DSLContext context = getReadWriteContext();
//        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
//        List<Long> standardIds = context.select(standardScope.CHARGING_STANDARD_ID)
//                .from(standardScope)
//                .where(standardScope.OWNER_TYPE.eq(ownerType))
//                .and(standardScope.OWNER_ID.eq(ownerId))
//                .fetch(standardScope.CHARGING_STANDARD_ID);
//        context.delete(standardScope)
//                .where(standardScope.OWNER_TYPE.eq(ownerType))
//                .and(standardScope.OWNER_ID.eq(ownerId))
//                .execute();
//        return standardIds;
//    }

    @Override
    public void updateChargingStandardByCreating(String standardName,String instruction,Long chargingStandardId, Long ownerId, String ownerType) {
        //个体园区点击修改时的操作
        DSLContext context = getReadWriteContext();
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
        com.everhomes.server.schema.tables.EhPaymentFormula formulaSchema = Tables.EH_PAYMENT_FORMULA.as("formulaSchema");
        //拿到所有的standard的id
        List<Long> standardIds = context.select(standardScope.CHARGING_STANDARD_ID)
                .from(standardScope)
                .where(standardScope.OWNER_TYPE.eq(ownerType))
                .and(standardScope.OWNER_ID.eq(ownerId))
                .fetch(standardScope.CHARGING_STANDARD_ID);
        //拿到原来的所有的standard
        List<com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards> standards = context.selectFrom(standard)
                .where(standard.ID.in(standardIds))
                .fetchInto(PaymentChargingStandards.class);
        //公式和scope都要存一份
        List<com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes> scopes = new ArrayList<>();
        List<com.everhomes.server.schema.tables.pojos.EhPaymentFormula> formulas = new ArrayList<>();
        for(int i = 0; i < standards.size(); i ++){
            com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards origin = standards.get(i);
            //如果是需要修改的那个standard，修改之
            if(origin.getId().longValue() == chargingStandardId.longValue()){
                origin.setName(standardName);
                origin.setInstruction(instruction);
            }
            //standard的公式
            com.everhomes.server.schema.tables.pojos.EhPaymentFormula formula = context.selectFrom(formulaSchema)
                    .where(formulaSchema.CHARGING_STANDARD_ID.eq(origin.getId()))
                    .fetchOneInto(PaymentFormula.class);
            //standard的scope
            com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes scope = context.selectFrom(standardScope).
                    where(standardScope.CHARGING_STANDARD_ID.eq(origin.getId()))
                    .and(standardScope.OWNER_ID.eq(ownerId))
                    .and(standardScope.OWNER_TYPE.eq(ownerType))
                    .fetchOneInto(PaymentChargingStandardScope.class);
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards.class));
            //c，s，f均修改id
            formula.setChargingStandardId(nextSequence);
            formulas.add(formula);
            scope.setChargingStandardId(nextSequence);
            scopes.add(scope);
            origin.setId(nextSequence);
        }
        //删除个体园区所有的scope
        context.delete(standardScope)
                .where(standardScope.OWNER_TYPE.eq(ownerType))
                .and(standardScope.OWNER_ID.eq(ownerId))
                .execute();
        EhPaymentChargingStandardsDao dao = new EhPaymentChargingStandardsDao(context.configuration());
        EhPaymentFormulaDao dao1 = new EhPaymentFormulaDao(context.configuration());
        EhPaymentChargingStandardsScopesDao dao2 = new EhPaymentChargingStandardsScopesDao(context.configuration());
        //插入新的c，s，f
        dao.insert(standards);
        dao1.insert(formulas);
        dao2.insert(scopes);
    }

    @Override
    public boolean checkCoupledChargingStandard(Long cid) {
        DSLContext context = getReadWriteContext();
        List<Long> bros = context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.ID)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.OWNER_ID.eq(cid))
                .fetch(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.ID);
        if(bros.size() < 1){
            return true;
        }
//        Long bro = context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.BROTHER_STANDARD_ID)
//                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES)
//                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.CHARGING_STANDARD_ID.eq(cid))
//                .fetchOne(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.BROTHER_STANDARD_ID);
//        if(bro == null){
//            return false;
//        }else{
//            return true;
//        }
        return false;
    }

    @Override
    public void deCoupledForChargingItem(Long ownerId, String ownerType, Long categoryId) {
        DSLContext context = getReadWriteContext();
        context.update(itemScope)
                .set(itemScope.DECOUPLING_FLAG,(byte)1)
                .where(itemScope.OWNER_TYPE.eq(ownerType))
                .and(itemScope.OWNER_ID.eq(ownerId))
                //added categoryId constraint
                .and(itemScope.CATEGORY_ID.eq(categoryId))
                .execute();
        //issue-34458 在具体项目新增一条标准（自定义的标准），“注：该项目使用默认配置”文案不消失，刷新也不消失
        //只要做了新增动作，那么该项目下的配置全部解耦
        Long nullId = null;
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
            context.update(standardScope)
                    .set(standardScope.BROTHER_STANDARD_ID,nullId)
                    .where(standardScope.OWNER_ID.eq(ownerId))
                    .and(standardScope.OWNER_TYPE.eq(ownerType))
                    .execute();
    }

    @Override
    public List<com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules> getBillGroupRuleByCommunity(Long ownerId, String ownerType) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERID.eq(ownerId))
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERTYPE.eq(ownerType))
                .fetchInto(PaymentBillGroupRule.class);
    }

    @Override
    public PaymentChargingItemScope findChargingItemScope(Long chargingItemId, String ownerType, Long ownerId) {
        DSLContext context = getReadOnlyContext();
        List<PaymentChargingItemScope> scopes = context.selectFrom(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_TYPE.eq(ownerType))
                .fetchInto(PaymentChargingItemScope.class);
        if(scopes.size()>0){
            return scopes.get(0);
        }
        return null;
    }

    @Override
    public List<PaymentNoticeConfig> listAutoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, Long categoryId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_NOTICE_CONFIG)
                .where(Tables.EH_PAYMENT_NOTICE_CONFIG.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_NOTICE_CONFIG.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_NOTICE_CONFIG.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_NOTICE_CONFIG.CATEGORY_ID.eq(categoryId))
                .fetchInto(PaymentNoticeConfig.class);
    }

    @Override
    public void autoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, List<com.everhomes.server.schema.tables.pojos.EhPaymentNoticeConfig> toSaveConfigs) {
        DSLContext writeContext = getReadWriteContext();
        EhPaymentNoticeConfig noticeConfig = Tables.EH_PAYMENT_NOTICE_CONFIG.as("noticeConfig");
        EhPaymentNoticeConfigDao noticeConfigDao = new EhPaymentNoticeConfigDao(writeContext.configuration());
        this.dbProvider.execute((TransactionStatus status) -> {
            writeContext.delete(noticeConfig)
                    .where(noticeConfig.NAMESPACE_ID.eq(namespaceId))
                    .and(noticeConfig.OWNER_TYPE.eq(ownerType))
                    .and(noticeConfig.OWNER_ID.eq(ownerId))
                    .execute();
            noticeConfigDao.insert(toSaveConfigs);
            return null;
        });
    }

    @Override
    public AssetPaymentOrder getOrderById(Long orderId) {
        DSLContext context = getReadOnlyContext();
        List<AssetPaymentOrder> assetPaymentOrders = context.selectFrom(Tables.EH_ASSET_PAYMENT_ORDER)
                .where(Tables.EH_ASSET_PAYMENT_ORDER.ID.eq(orderId))
                .fetchInto(AssetPaymentOrder.class);
        if(assetPaymentOrders.size() > 0) {
            return assetPaymentOrders.get(0);
        }
        return null;
    }

    @Override
    public String getBillSource(String billId) {
        Long id = null;
        try{
            id = Long.parseLong(billId);
        }catch(Exception e){
            return "";
        }
        DSLContext context = getReadOnlyContext();
        PaymentBills bill = context.selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(id))
                .fetchOneInto(PaymentBills.class);
        StringBuilder sb = new StringBuilder();
        sb.append(bill==null?"":bill.getDateStr()==null?"":bill.getDateStr());
        if(bill != null) {
            PaymentBillGroup group = context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(bill.getBillGroupId()))
                    .fetchOneInto(PaymentBillGroup.class);
            sb.append(group == null? "":group.getName()==null?"":group.getName());
        }
        return sb.toString();
    }

    @Override
    public List<PaymentNoticeConfig> listAllNoticeConfigs() {
        DSLContext context = getReadOnlyContext();
        List<PaymentNoticeConfig> list = new ArrayList<>();
        return context.selectFrom(Tables.EH_PAYMENT_NOTICE_CONFIG)
                .fetchInto(PaymentNoticeConfig.class);

    }

    @Override
    public List<PaymentBills> getAllBillsByCommunity(Integer namespaceId, Long key) {
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_PAYMENT_BILLS.fields());
        query.addFrom(Tables.EH_PAYMENT_BILLS);
        query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(key));
        query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq("community"));
        if(namespaceId!=null){
            query.addConditions(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId));
        }
        query.addConditions(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte)1));
        query.addConditions(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte)0));
        return query.fetchInto(PaymentBills.class);
    }

    @Override
    public List<PaymentBills> findAssetArrearage(Integer namespaceId, Long communityId, Long organizationId) {
        SelectQuery<EhPaymentBillsRecord> query = getReadOnlyContext().selectQuery(Tables.EH_PAYMENT_BILLS);
        if (namespaceId != null) query.addConditions(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId));
        if (communityId != null) {
            query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq("community"));
            query.addConditions(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(communityId));
        }

        if (organizationId != null) {
            query.addConditions(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq("eh_organization"));
            query.addConditions(Tables.EH_PAYMENT_BILLS.TARGET_ID.eq(organizationId));
        }
        query.addConditions(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte) 1));
        return query.fetchInto(PaymentBills.class);
    }
    public List<com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules> getBillGroupRuleByCommunityWithBro(Long ownerId, String ownerType, boolean withBro) {
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES);
        query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERID.eq(ownerId));
        query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS_RULES.OWNERTYPE.eq(ownerType));
        if(withBro){
            query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BROTHER_RULE_ID.isNotNull());
        }else{
            query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BROTHER_RULE_ID.isNull());
        }
        return query.fetchInto(PaymentBillGroupRule.class);
    }

    @Override
    public List<PaymentBills> findSettledBillsByContractIds(List<Long> contractIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.CONTRACT_ID.in(contractIds))
                .and(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte)1))
                .fetchInto(PaymentBills.class);
    }

    @Override
    public String getbillGroupNameById(Long billGroupId) {
        return getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_GROUPS.NAME)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOne(Tables.EH_PAYMENT_BILL_GROUPS.NAME);
    }

    @Override
    public Collection<? extends Long> getAddressIdByBillId(Long id) {
        Set<Long> ids = new HashSet<>();
        getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_ITEMS.ADDRESS_ID)
                .from(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(id))
                .fetch()
                .forEach(r -> {
                    ids.add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.ADDRESS_ID));
                });
        return ids;
    }

    @Override
    public String getAddressStrByIds(List<Long> collect) {
        StringBuilder sb = new StringBuilder();
        getReadOnlyContext().select(Tables.EH_ADDRESSES.BUILDING_NAME,Tables.EH_ADDRESSES.APARTMENT_NAME)
                .from(Tables.EH_ADDRESSES)
                .where(Tables.EH_ADDRESSES.ID.in(collect))
                .fetch()
                .forEach(r -> {
                    sb.append(r.getValue(Tables.EH_ADDRESSES.BUILDING_NAME)+r.getValue(Tables.EH_ADDRESSES.APARTMENT_NAME)+",");
                });
        return sb.toString();
    }

    @Override
    public BigDecimal getBillExpectanciesAmountOnContract(String contractNum, Long contractId, Long categoryId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        EhPaymentChargingItems t1 = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t1");
        EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
        HashSet<PaymentExpectancyDTO> set = new HashSet<>();
        List<Long> fetch = new ArrayList<Long>();
        if(categoryId != null) {
        	fetch = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_NUM.eq(contractNum))
                    .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .fetch(bill.ID);
        }else {
        	fetch = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_NUM.eq(contractNum))
                    .fetch(bill.ID);
        }
        context.select(t.ID,t.AMOUNT_RECEIVABLE)
	        .from(t,t1)
	        .where(t.BILL_ID.in(fetch))
	        .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
	        .orderBy(t1.NAME,t.DATE_STR)
	        .fetch()
	        .map(r -> {
	            PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
	            dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
	            dto.setBillItemId(r.getValue(t.ID));
	            set.add(dto);
	            return null;
        });
        
        List<Long> fetch1 = new ArrayList<Long>();
        if(categoryId != null) {
        	fetch1 = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_ID.eq(contractId))
                    .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .fetch(bill.ID);
        }else {
        	fetch1 = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_ID.eq(contractId))
                    .fetch(bill.ID);
        }
        context.select(t.ID,t.AMOUNT_RECEIVABLE)
                .from(t,t1)
                .where(t.BILL_ID.in(fetch1))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.NAME,t.DATE_STR)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
                    dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
                    dto.setBillItemId(r.getValue(t.ID));
                    set.add(dto);
                    return null;
                });
        BigDecimal amount = new BigDecimal("0");
        Iterator<PaymentExpectancyDTO> it = set.iterator();
        while(it.hasNext()){
            amount = amount.add(it.next().getAmountReceivable());
        }
        return amount;
    }

    @Override
    public List<ListAllBillsForClientDTO> listAllBillsForClient(Integer namespaceId, String ownerType, Long ownerId, String targetType, Long targetId, Byte status, Long billGroupId) {
        List<ListAllBillsForClientDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        ArrayList<Long> groupIds = new ArrayList<>();
        EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(bill);
        query.addSelect(bill.BILL_GROUP_ID);
        query.addSelect(bill.AMOUNT_OWED);
        query.addSelect(bill.AMOUNT_RECEIVABLE);
        query.addSelect(bill.DATE_STR_BEGIN);
        query.addSelect(bill.DATE_STR_END);
        query.addSelect(bill.STATUS);
        query.addSelect(bill.ID);
        if(namespaceId!=null){
            query.addConditions(bill.NAMESPACE_ID.eq(namespaceId));
        }
        if(ownerId != null){
            query.addConditions(bill.OWNER_ID.eq(ownerId));
        }
        if(ownerType != null){
            query.addConditions(bill.OWNER_TYPE.eq(ownerType));
        }
        if(status != null){
            query.addConditions(bill.STATUS.eq(status));
        }
        if(billGroupId != null){
            query.addConditions(bill.BILL_GROUP_ID.eq(billGroupId));
        }
        query.addConditions(bill.TARGET_TYPE.eq(targetType));
        //如果是企业客户，可以通过targetId查询
        if(targetType.equals(AssetPaymentStrings.EH_ORGANIZATION)) {
        	query.addConditions(bill.TARGET_ID.eq(targetId));
        }
        query.addConditions(bill.SWITCH.eq((byte)1));
        query.addOrderBy(bill.DATE_STR.desc());

        query.fetch()
                .map(r -> {
                    ListAllBillsForClientDTO dto = new ListAllBillsForClientDTO();
                    groupIds.add(r.getValue(bill.BILL_GROUP_ID));
                    dto.setAmountOwed(r.getValue(bill.AMOUNT_OWED).toString());
                    dto.setAmountReceivable(r.getValue(bill.AMOUNT_RECEIVABLE).toString());
                    dto.setDateStrBegin(r.getValue(bill.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(bill.DATE_STR_END));
                    dto.setChargeStatus(r.getValue(bill.STATUS));
                    dto.setBillId(String.valueOf(r.getValue(bill.ID)));
                    list.add(dto);
                    return null;
                });
        Map<Long,String> groupNames = getGroupNames(groupIds);
        for(int i = 0 ; i < list.size(); i ++){
            list.get(i).setBillGroupName(groupNames.get(groupIds.get(i)));
        }
        return list;
    }

    @Override
    public PaymentServiceConfig findServiceConfig(Integer namespaceId) {
        List<PaymentServiceConfig> configs = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_SERVICE_CONFIGS)
                .where(Tables.EH_PAYMENT_SERVICE_CONFIGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_SERVICE_CONFIGS.NAME.eq("物业缴费"))
                .fetchInto(PaymentServiceConfig.class);
        if(configs == null || configs.size() < 1){
            return null;
        }
        return configs.get(0);
    }

    @Override
    public List<PaymentBills> findSettledBillsByCustomer(String targetType, Long targetId,String ownerType,Long ownerId) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.TARGET_ID.eq(targetId))
                .and(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(targetType))
                .and(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte)1))
                .and(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte)0))
                .and(Tables.EH_PAYMENT_BILLS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                .fetchInto(PaymentBills.class);
    }

    @Override
    public List<PaymentBills> findPaidBillsByIds(List<String> billIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .and(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte)1))
                .fetchInto(PaymentBills.class);
    }

    @Override
    public void reCalBillById(long billId) {
//        重新计算账单
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        final BigDecimal[] amountReceived = {new BigDecimal("0")};
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountExempled = {new BigDecimal("0")};
        final BigDecimal[] amountSupplement = {new BigDecimal("0")};
        BigDecimal zero = new BigDecimal("0");
        getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED,
        		Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED,Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID)
                .from(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                .fetch()
                .forEach(r -> {
                    amountReceivable[0] = amountReceivable[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE));//应收
                    amountReceived[0] = amountReceived[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED));//已收
                    //根据减免费项配置重新计算待收金额
                    Long charingItemId = r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID);
                    Boolean isConfigSubtraction = isConfigItemSubtraction(billId, charingItemId);//用于判断该费项是否配置了减免费项
                    if(!isConfigSubtraction) {//如果费项没有配置减免费项，那么需要相加到待缴金额中
                    	amountOwed[0] = amountOwed[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED));
                    }
                });
        getReadOnlyContext().select(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT)
                .from(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.BILL_ID.in(billId))
                .fetch()
                .forEach(r ->{
//                    amountReceivable[0] = amountReceivable[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    amountOwed[0] = amountOwed[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    if(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT).compareTo(zero) == 1){
                        amountSupplement[0] = amountSupplement[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    }else if (r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT).compareTo(zero) == -1){
                        amountExempled[0] = amountExempled[0].subtract(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    }
                });
        //包括滞纳金
        getReadOnlyContext().select(Tables.EH_PAYMENT_LATE_FINE.AMOUNT,Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID)
                .from(Tables.EH_PAYMENT_LATE_FINE)
                .where(Tables.EH_PAYMENT_LATE_FINE.BILL_ID.eq(billId))
                .fetch()
                .forEach(r ->{
                    BigDecimal value = r.getValue(Tables.EH_PAYMENT_LATE_FINE.AMOUNT);
                    if(value != null){
                    	Long billItemId = r.getValue(Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID);
                    	//减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金
                    	Long chargingItemId = getPaymentBillItemsChargingItemID(billId,billItemId);
                    	//根据减免费项配置重新计算待收金额
                        Boolean isConfigSubtraction = isConfigLateFineSubtraction(billId, chargingItemId);//用于判断该滞纳金是否配置了减免费项
                        if(!isConfigSubtraction) {//如果滞纳金没有配置减免费项，那么需要相加到待缴金额中
                        	amountOwed[0] = amountOwed[0].add(value);
                        }    
                    }
                });
        //修复issue-32525 "导入一个账单，减免大于收费项，列表显示了负数"的bug
        amountOwed[0] = DecimalUtils.negativeValueFilte(amountOwed[0]);
        //更改金额，但不更改状态
        getReadWriteContext().update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE, amountReceivable[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED, amountOwed[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_SUPPLEMENT, amountSupplement[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_EXEMPTION, amountExempled[0])
                    .set(Tables.EH_PAYMENT_BILLS.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                    .set(Tables.EH_PAYMENT_BILLS.OPERATOR_UID,UserContext.currentUserId())
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();

    }

    @Override
    public SettledBillRes getSettledBills(int pageSize, long pageAnchor) {
        Long pageOffset = (pageAnchor - 1 ) * pageSize;
        SettledBillRes res = new SettledBillRes();
        List<PaymentBills> paymentBills = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte) 1))
                .and(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte) 0))
                .limit(pageOffset.intValue(), pageSize+1)
                .fetchInto(PaymentBills.class);
        if(paymentBills.size() > pageSize){
            res.setNextPageAnchor(pageAnchor+1);
            paymentBills.remove(paymentBills.size()-1);
        }else{
            res.setNextPageAnchor(null);
        }
        res.setBills(paymentBills);
        return res;
    }

    @Override
    public void changeBillToDue(Long id) {
        getReadWriteContext().update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.CHARGE_STATUS,(byte)1)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(id))
                .execute();
    }

    @Override
    public List<PaymentBillItems> getBillItemsByBillIds(List<Long> overdueBillIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.in(overdueBillIds))
                .fetchInto(PaymentBillItems.class);
    }

    @Override
    public void updatePaymentItem(PaymentBillItems item) {
        DSLContext con = getReadWriteContext();
        EhPaymentBillItemsDao dao = new EhPaymentBillItemsDao(con.configuration());
        dao.update(item);
    }

    @Override
    public BigDecimal getLateFineAmountByItemId(Long id) {
        final BigDecimal[] res = {new BigDecimal("0")};
        getReadOnlyContext().select(Tables.EH_PAYMENT_LATE_FINE.AMOUNT)
                .from(Tables.EH_PAYMENT_LATE_FINE)
                .where(Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID.eq(id))
                .fetch()
                .forEach(r -> {
                    res[0] = res[0].add(r.getValue(Tables.EH_PAYMENT_LATE_FINE.AMOUNT));
                });
        return res[0];
    }

    @Override
    public void createLateFine(PaymentLateFine fine) {
        DSLContext con = getReadWriteContext();
        EhPaymentLateFineDao dao = new EhPaymentLateFineDao(con.configuration());
        dao.update(fine);
    }

    @Override
    public void updateBillAmountOwedDueToFine(BigDecimal fineAmount, Long billId) {
        DSLContext context = getReadWriteContext();
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED,Tables.EH_PAYMENT_BILLS.AMOUNT_OWED.add(fineAmount))
                .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE.add(fineAmount))
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .execute();
    }

    @Override
    public List<ListLateFineStandardsDTO> listLateFineStandards(Long ownerId, String ownerType, Integer namespaceId, Long categoryId) {
        List<ListLateFineStandardsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingStandardsScopes scope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("scope");
        EhPaymentChargingStandards std = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("std");
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(scope,std);
        query.addConditions(scope.CHARGING_STANDARD_ID.eq(std.ID));
        query.addConditions(scope.OWNER_ID.eq(ownerId));
        query.addConditions(std.CHARGING_ITEMS_ID.eq(AssetPaymentConstants.LATE_FINE_ID));
        if(ownerType!=null){
            query.addConditions(scope.OWNER_TYPE.eq(ownerType));
        }
        if(namespaceId!=null){
            query.addConditions(scope.NAMESPACE_ID.eq(namespaceId));
        }
        if(categoryId != null){
            query.addConditions(scope.CATEGORY_ID.eq(categoryId));
        }
        query.fetch()
                .forEach(r -> {
                    ListLateFineStandardsDTO dto = new ListLateFineStandardsDTO();
                    dto.setLateFeeStandardId(r.getValue(scope.CHARGING_STANDARD_ID));
                    dto.setLateFeeStandardName(r.getValue(std.NAME));
                    dto.setLateFeeStandardFormula(r.getValue(std.FORMULA));
                    list.add(dto);
                });
        return list;
    }

    @Override
    public void updateLateFineAndBill(PaymentLateFine fine, BigDecimal fineAmount, Long billId, boolean isInsert) {
        DSLContext context = getReadWriteContext();
        EhPaymentLateFineDao dao = new EhPaymentLateFineDao(context.configuration());
        this.dbProvider.execute((TransactionStatus status) -> {
            if(isInsert){
                dao.insert(fine);
            }else{
                dao.update(fine);
            }
            context.update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED,Tables.EH_PAYMENT_BILLS.AMOUNT_OWED.add(fineAmount))
//                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE.add(fineAmount))
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            return status;
        });
    }

    @Override
    public PaymentChargingItem getBillItemByName(Integer namespaceId, Long ownerId, String ownerType, Long billGroupId, String projectLevelName) {
        DSLContext context = getReadOnlyContext();
        List<Long> chargingItemIds = context.select(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID)
                .from(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(billGroupId))
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS_RULES.CHARGING_ITEM_ID);
        Long categoryId = findCategoryIdFromBillGroup(billGroupId);
        List<Long> chosenId = context.select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID)
                .from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME.eq(projectLevelName))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.in(chargingItemIds))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CATEGORY_ID.eq(categoryId))
                .fetch(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID);
        if(chosenId.size() != 1){
            return null;
        }
        return context.selectFrom(Tables.EH_PAYMENT_CHARGING_ITEMS)
                .where(Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(chosenId.get(0)))
                .fetchOneInto(PaymentChargingItem.class);
    }

    @Override
    public String findBillGroupNameById(Long billGroupId) {
        DSLContext context = getReadOnlyContext();
        return context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOne(Tables.EH_PAYMENT_BILL_GROUPS.NAME);
    }

    @Override
    public void linkIndividualUserToBill(Long ownerUid, String token) {
        DSLContext context = getReadWriteContext();
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.TARGET_ID, ownerUid)
                .where(Tables.EH_PAYMENT_BILLS.CUSTOMER_TEL.eq(token))
                .and(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(AssetTargetType.USER.getCode()))
                .execute();
    }

    @Override
    public void linkOrganizationToBill(Long ownerUid, String orgName) {
        DSLContext context = getReadWriteContext();
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.TARGET_ID, ownerUid)
                .where(Tables.EH_PAYMENT_BILLS.TARGET_NAME.eq(orgName))
                .and(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(AssetTargetType.ORGANIZATION.getCode()))
                .execute();
    }

    @Override
    public void modifySettledBill(Long billId, String invoiceNum, String noticeTel) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
        // 更新发票，更新催缴手机号码
        dslContext.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.INVOICE_NUMBER, invoiceNum)
                .set(Tables.EH_PAYMENT_BILLS.NOTICETEL, noticeTel)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .execute();
    }

    @Override
    public boolean checkBillExistById(Long billId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> fetch = dslContext.select(Tables.EH_PAYMENT_BILLS.ID)
                .from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .fetch(Tables.EH_PAYMENT_BILLS.ID);
        if(fetch.size() > 0) return true;
        return false;
    }

    @Override
    public String getAddressByBillId(Long id) {
        final String[] ret = {""};
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        dslContext.select(Tables.EH_PAYMENT_BILL_ITEMS.APARTMENT_NAME, Tables.EH_PAYMENT_BILL_ITEMS.BUILDING_NAME)
                .from(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(id))
                .fetch()
                .forEach(r -> {
                    String apartment = r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.APARTMENT_NAME);
                    String building = r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.BUILDING_NAME);
                    ret[0] = building + apartment;
                    return;
                });
        return ret[0];
    }

    @Override
    public List<PaymentAppView> findAppViewsByNamespaceIdOrRemark(Integer namespaceId, Long communityId, String targetType, String ownerType, String billGroupName, String billGroupName1, Boolean[] remarkCheckList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_PAYMENT_APP_VIEWS);
        query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.NAMESPACE_ID.eq(namespaceId));
        if(communityId!=null){
            query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.COMMUNITY_ID.eq(communityId));
        }
        if(remarkCheckList[0]) {
            query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.REMARK1_TYPE.eq(targetType));
            query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.REMARK1_IDENTIFIER.eq(ownerType));
        }
        if(remarkCheckList[1]){
            query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.REMARK2_TYPE.eq(billGroupName));
            query.addConditions(Tables.EH_PAYMENT_APP_VIEWS.REMARK2_IDENTIFIER.eq(billGroupName1));
        }
        return query.fetchInto(PaymentAppView.class);
    }

    @Override
    public List<PaymentNoticeConfig> listAllNoticeConfigsByNameSpaceId(Integer namespaceId) {
        DSLContext context = getReadOnlyContext();
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_PAYMENT_NOTICE_CONFIG);
        if(namespaceId!=null){
            query.addConditions(Tables.EH_PAYMENT_NOTICE_CONFIG.NAMESPACE_ID.eq(namespaceId));
        }
        return query.fetchInto(PaymentNoticeConfig.class);
    }

    @Override
    public Long findCategoryIdFromBillGroup(Long billGroupId) {
        return getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOne(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID);
    }

    @Override
    public void insertAssetCategory(com.everhomes.server.schema.tables.pojos.EhAssetAppCategories c) {
        EhAssetAppCategoriesDao dao = new EhAssetAppCategoriesDao(getReadWriteContext().configuration());
        dao.insert(c);
    }

    private Map<Long,String> getGroupNames(ArrayList<Long> groupIds) {
        Map<Long,String> map = new HashMap<>();
        EhPaymentBillGroups group = Tables.EH_PAYMENT_BILL_GROUPS.as("group");
        for(int i = 0 ; i < groupIds.size(); i++ ){
            Long groupId = groupIds.get(i);
            if(map.containsKey(groupId)) continue;
            map.put(groupId,getReadOnlyContext().select(group.NAME).from(group).where(group.ID.eq(groupId)).fetchOne(group.NAME));
        }
        return map;
    }

    //根据billId查询相应的凭证图片记录
    @Override
	public List<PaymentBillCertificate> listUploadCertificates(Long billId) {
    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
    	SelectQuery<Record> selectQuery = dslContext.selectQuery();
    	selectQuery.addFrom(Tables.EH_PAYMENT_BILL_CERTIFICATE);
    	selectQuery.addConditions(Tables.EH_PAYMENT_BILL_CERTIFICATE.BILL_ID.eq(billId));
    	List<PaymentBillCertificate> paymentBillCertificateList = selectQuery.fetch().map(r->{
    		PaymentBillCertificate paymentBillCertificate = ConvertHelper.convert(r, PaymentBillCertificate.class);
    		return paymentBillCertificate;
    	});
		return paymentBillCertificateList;
	}

    //根据billId查询对应的上传缴费凭证时的留言
    @Override
	public String getCertificateNote(Long billId) {
    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
    	String certificateNote = dslContext.select(Tables.EH_PAYMENT_BILLS.CERTIFICATE_NOTE)
    							.from(Tables.EH_PAYMENT_BILLS)
    							.where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
    							.fetchOne(Tables.EH_PAYMENT_BILLS.CERTIFICATE_NOTE);
		return certificateNote;
	}
    
    //更新上传的缴费凭证的图片信息
    @Override
	public void updatePaymentBillCertificates(Long billId, String certificateNote, List<String> certificateUris) {
    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
    	EhPaymentBillCertificateDao dao = new EhPaymentBillCertificateDao(dslContext.configuration());
    	
    	List<PaymentBillCertificate> list = new ArrayList<>();
    	certificateUris.stream().forEach(r->{
    		PaymentBillCertificate paymentBillCertificate = new PaymentBillCertificate();
    		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentBillCertificate.class));
    		paymentBillCertificate.setId(id);
    		paymentBillCertificate.setBillId(billId);
    		paymentBillCertificate.setCertificateUri(r);
    		list.add(paymentBillCertificate);
    	});
    	
    	this.dbProvider.execute((TransactionStatus status) -> {
    		//更新上传凭证时附带的留言
            dslContext.update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.CERTIFICATE_NOTE, certificateNote)
                    .set(Tables.EH_PAYMENT_BILLS.IS_UPLOAD_CERTIFICATE, (byte)1)
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            //删除之前EH_PAYMENT_BILL_CERTIFICATE表中与该billId相关联的缴费凭证的记录
            dslContext.delete(Tables.EH_PAYMENT_BILL_CERTIFICATE)
            		.where(Tables.EH_PAYMENT_BILL_CERTIFICATE.BILL_ID.eq(billId))
            		.execute();
    		//重新添加缴费凭证图片信息
            list.stream().forEach(r->{
            	dao.insert(r);
            });
            return status;
        });
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
    private static EhPaymentChargingStandards standard = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("standard");
    private static EhPaymentChargingItemScopes itemScope = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("itemScope");
    private static EhPaymentBillGroupsRules groupRule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("groupRule");


    //add by tangcen 
	@Override
	public String findProjectChargingItemNameByCommunityId(Long ownerId, Integer namespaceId, Long categoryId, Long chargingItemId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        String projectChargingItemName = null;
        projectChargingItemName =context.select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME)
                				.from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
                				.where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(ownerId))
                				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.eq(chargingItemId))
                				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CATEGORY_ID.eq(categoryId))
                				.fetchOne(0,String.class);
        if (org.springframework.util.StringUtils.isEmpty(projectChargingItemName)) {
        	projectChargingItemName =context.select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME)
    				.from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
    				.where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
    				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(new Long(Integer.toString(namespaceId))))
    				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.eq(chargingItemId))
    				.and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CATEGORY_ID.eq(categoryId))
    				.fetchOne(0,String.class);
		}
        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("ownerId: {}; namespaceId: {}; chargingItemId: {}; projectChargingItemName: {}",
        			ownerId, namespaceId, chargingItemId,projectChargingItemName);
        }
        return projectChargingItemName;
	}

    //add by steve
	@Override
	public void setRent(Long contractId, BigDecimal rent) {
		 DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
	     // 更新合同中的rent字段
	     dslContext.update(Tables.EH_CONTRACTS)
	                .set(Tables.EH_CONTRACTS.RENT, rent)
	                .where(Tables.EH_CONTRACTS.ID.eq(contractId))
	                .execute();
	}
	
	public List<PaymentOrderBillDTO> listBillsForOrder(Integer currentNamespaceId, Integer pageOffSet, Integer pageSize, ListPaymentBillCmd cmd) {
        //卸货
        Long ownerId = cmd.getCommunityId();
        String dateStrBegin = cmd.getDateStrBegin();
        String dateStrEnd = cmd.getDateStrEnd();
        String startPayTime = cmd.getStartPayTime();
        String endPayTime = cmd.getEndPayTime();
        String targetName = cmd.getTargetName();
        Integer paymentType = cmd.getPaymentType();
        Long billId = cmd.getBillId();
        Long categoryId = cmd.getCategoryId();//增加多入口查询条件
        //卸货结束
        List<PaymentOrderBillDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        com.everhomes.server.schema.tables.EhAssetPaymentOrderBills t2 = Tables.EH_ASSET_PAYMENT_ORDER_BILLS.as("t2");
        EhPaymentOrderRecords t3 = Tables.EH_PAYMENT_ORDER_RECORDS.as("t3");
        EhAssetPaymentOrder t4 = Tables.EH_ASSET_PAYMENT_ORDER.as("t4");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID, t.AMOUNT_RECEIVABLE, t.AMOUNT_RECEIVED, t.DATE_STR_BEGIN, t.DATE_STR_END, 
        		t2.BILL_ID, t3.PAYMENT_ORDER_ID, t3.CREATE_TIME, t4.UID, t4.PAYMENT_TYPE);
        query.addFrom(t);
        query.addJoin(t2, t.ID.eq(DSL.cast(t2.BILL_ID, Long.class)));
        query.addJoin(t3, t2.ORDER_ID.eq(t3.ORDER_ID));
        query.addJoin(t4, t2.ORDER_ID.eq(t4.ID));
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        //status[Byte]:账单属性，0:未出账单;1:已出账单，对应到eh_payment_bills表中的switch字段
        Byte status = new Byte("1");
        query.addConditions(t.SWITCH.eq(status));
        if(!org.springframework.util.StringUtils.isEmpty(categoryId)){
        	query.addConditions(t.CATEGORY_ID.eq(categoryId));//增加多入口查询条件
        }
        if(!org.springframework.util.StringUtils.isEmpty(billId)){
            query.addConditions(t.ID.eq(billId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrBegin)){
            query.addConditions(t.DATE_STR_BEGIN.greaterOrEqual(dateStrBegin));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dateStrEnd)){
            query.addConditions(t.DATE_STR_END.lessOrEqual(dateStrEnd));
        }
        if(!org.springframework.util.StringUtils.isEmpty(targetName)){
        	query.addConditions(t.TARGET_NAME.like("%" + targetName + "%"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(startPayTime)){
            query.addConditions(DSL.cast(t3.CREATE_TIME, String.class).greaterOrEqual(startPayTime + " 00:00:00"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(endPayTime)){
            query.addConditions(DSL.cast(t3.CREATE_TIME, String.class).lessOrEqual(endPayTime + " 23:59:59"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(paymentType)){
        	//业务系统：paymentType：支付方式，0:微信，1：支付宝，2：对公转账
            //电商系统：paymentType： 支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
            //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
            if(paymentType.equals(0)) {//微信
            	query.addConditions(t4.PAYMENT_TYPE.eq("1")
            			.or(t4.PAYMENT_TYPE.eq("7"))
            			.or(t4.PAYMENT_TYPE.eq("9"))
            			.or(t4.PAYMENT_TYPE.eq("12"))
            			.or(t4.PAYMENT_TYPE.eq("21"))
            	);
            }else if(paymentType.equals(1)) {//支付宝
            	query.addConditions(t4.PAYMENT_TYPE.eq("8")
            			.or(t4.PAYMENT_TYPE.eq("10"))
            			.or(t4.PAYMENT_TYPE.eq("13"))
            	);
            }else if(paymentType.equals(2)){//对公转账
            	query.addConditions(t4.PAYMENT_TYPE.eq("2"));
            }
        }
        query.addConditions(t2.STATUS.eq(1));//EhAssetPaymentOrderBills中的status1代表支付成功
        query.addOrderBy(t3.CREATE_TIME.desc());
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(r -> {
        	PaymentOrderBillDTO dto = new PaymentOrderBillDTO();
        	dto.setBillId(r.getValue(t.ID));//账单ID
        	dto.setPaymentOrderNum(r.getValue(t3.PAYMENT_ORDER_ID).toString());//订单ID
        	ListBillDetailVO listBillDetailVO = listBillDetailForPaymentV2(dto.getBillId());
        	dto.setDateStrBegin(listBillDetailVO.getDateStrBegin());
        	dto.setDateStrEnd(listBillDetailVO.getDateStrEnd());
        	dto.setTargetName(listBillDetailVO.getTargetName());
        	dto.setTargetType(listBillDetailVO.getTargetType());
        	dto.setAmountReceivable(listBillDetailVO.getAmountReceivable());
        	dto.setAmountReceived(listBillDetailVO.getAmountReceived());
        	dto.setAmountExemption(listBillDetailVO.getAmoutExemption());
    		dto.setAmountSupplement(listBillDetailVO.getAmountSupplement());
    		dto.setBuildingName(listBillDetailVO.getBuildingName());
    		dto.setApartmentName(listBillDetailVO.getApartmentName());
    		dto.setBillGroupName(listBillDetailVO.getBillGroupName());
    		dto.setAddresses(listBillDetailVO.getAddresses());
    		if(listBillDetailVO.getBillGroupDTO() != null) {
    			dto.setBillItemDTOList(listBillDetailVO.getBillGroupDTO().getBillItemDTOList());
    			dto.setExemptionItemDTOList(listBillDetailVO.getBillGroupDTO().getExemptionItemDTOList());
    		}
            SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //缴费时间
            String payTime = r.getValue(t3.CREATE_TIME).toString();
			try {
				payTime = yyyyMMddHHmm.format(yyyyMMddHHmm.parse(payTime));
			}catch (Exception e){
				payTime = null;
                LOGGER.error(e.toString());
            }
            dto.setPayTime(payTime);
            //获得付款人员（缴费人名称、缴费人电话）
            User userById = userProvider.findUserById(r.getValue(t4.UID));
            if(userById != null) {
            	dto.setPayerName(userById.getNickName());
                UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), cmd.getNamespaceId());
                if(userIdentifier != null) {
                	dto.setPayerTel(userIdentifier.getIdentifierToken());
                }
            }
            try {
            	Integer queryPaymentType = Integer.parseInt(r.getValue(t4.PAYMENT_TYPE));
                dto.setPaymentType(convertPaymentType(queryPaymentType));//支付方式
            }catch(Exception e){
            	LOGGER.debug("Integer.parseInt, paymentType={}, Exception={}", r.getValue(t4.PAYMENT_TYPE), e);
            }
            dto.setPaymentStatus(1);//1：已完成，0：订单异常
            list.add(dto);
            return null;
        });
        //调用支付提供的接口查询订单信息
        List<Long> payOrderIds = new ArrayList<>();
        for(PaymentOrderBillDTO dto : list) {
        	payOrderIds.add(Long.parseLong(dto.getPaymentOrderNum()));
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listPayOrderByIds(request), cmd={}", payOrderIds);
        }
        List<OrderDTO> payOrderDTOs = payServiceV2.listPayOrderByIds(payOrderIds);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listPayOrderByIds(response), response={}", GsonUtil.toJson(payOrderDTOs));
        }
        if(payOrderDTOs != null && payOrderDTOs.size() != 0) {
        	for(int i = 0;i < payOrderDTOs.size();i++) {
            	for(int j = 0;j < list.size();j++) {
            		if(payOrderDTOs.get(i).getId() != null && list.get(j).getPaymentOrderNum() != null &&
            				payOrderDTOs.get(i).getId().equals(Long.parseLong(list.get(j).getPaymentOrderNum()))){
            			PaymentOrderBillDTO dto = list.get(j);
            			OrderDTO orderDTO = payOrderDTOs.get(i);
            			//dto.setAmount(AmountUtil.centToUnit(LongUtil.convert(orderDTO.getAmount())));
            			if(orderDTO.getPaymentType() != null) {
            				dto.setPaymentType(convertPaymentType(orderDTO.getPaymentType()));//支付方式
            			}
            			list.set(j, dto);
            		}
            	}
            }
        }
        return list;
    }
	
	private Integer convertPaymentType(Integer paymentType) {
    	//业务系统：paymentType：支付方式，0:微信，1：支付宝，2：对公转账
        //电商系统：paymentType： 支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
        //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
        if(paymentType.equals(1) || paymentType.equals(7) || paymentType.equals(9) || paymentType.equals(12) || paymentType.equals(21)) {
        	paymentType = 0;//微信
        }else if(paymentType.equals(8) || paymentType.equals(10) || paymentType.equals(13)) {
        	paymentType = 1;//支付宝
        }else {
        	paymentType = 2;//对公转账
        }
        return paymentType;
    }
	
	public ListBillDetailVO listBillDetailForPaymentV2(Long billId) {
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
        //根据账单id查找所有的收费细项，并且拼装楼栋门牌
        vo.setAddresses("");//初始化
        EhPaymentBillItems o2 = Tables.EH_PAYMENT_BILL_ITEMS.as("o2");
        SelectQuery<Record> queryAddr = context.selectQuery();
        queryAddr.addSelect(o2.BUILDING_NAME,o2.APARTMENT_NAME);
        queryAddr.addFrom(o2);
        queryAddr.addConditions(o2.BILL_ID.eq(billId));
        queryAddr.fetch()
        	.map(f -> {
        		String newAddr = f.getValue(o2.BUILDING_NAME) + "/" + f.getValue(o2.APARTMENT_NAME);
        		if(f.getValue(o2.BUILDING_NAME) != null && f.getValue(o2.APARTMENT_NAME) != null && !vo.getAddresses().contains(newAddr)) {
        			String addresses = vo.getAddresses() + newAddr + ",";
        			vo.setAddresses(addresses);
        		}
        		return null;
        });
        String addresses = vo.getAddresses();
        if(addresses != null && addresses.length() > 0) {
        	addresses = addresses.substring(0, addresses.length() - 1);//去掉最后一个逗号 
        	vo.setAddresses(addresses);
        }
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID,r.TARGET_ID,r.NOTICETEL,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM
        		, r.INVOICE_NUMBER, r.BUILDING_NAME, r.APARTMENT_NAME, r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT);
        query.addFrom(r);
        query.addConditions(r.ID.eq(billId));
        query.fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    vo.setNoticeTel(f.getValue(r.NOTICETEL));
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setDateStrBegin(f.getValue(r.DATE_STR_BEGIN));//账单开始时间
                    vo.setDateStrEnd(f.getValue(r.DATE_STR_END));//账单结束时间
                    vo.setTargetName(f.getValue(r.TARGET_NAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
                    vo.setInvoiceNum(f.getValue(r.INVOICE_NUMBER));
                    vo.setBuildingName(f.getValue(r.BUILDING_NAME));
                    vo.setApartmentName(f.getValue(r.APARTMENT_NAME));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);//应收
                    BigDecimal amoutExemption = f.getValue(r.AMOUNT_EXEMPTION);//减免
                    BigDecimal amountSupplement = f.getValue(r.AMOUNT_SUPPLEMENT);//增收
                    amountReceivable = amountReceivable.subtract(amoutExemption).add(amountSupplement);//应收=应收-减免+增收
                    if(amountReceivable.compareTo(BigDecimal.ZERO) > 0) {
                    	vo.setAmountReceivable(amountReceivable);
                    }else {
                    	vo.setAmountReceivable(BigDecimal.ZERO);
                    }
                    vo.setAmountReceived(f.getValue(r.AMOUNT_RECEIVED));//实收
                    vo.setAmoutExemption(amoutExemption);//减免
                    vo.setAmountSupplement(amountSupplement);//增收
                    String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS)
                    		.where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(f.getValue(r.BILL_GROUP_ID))).fetchOne(0,String.class);
                    vo.setBillGroupName(billGroupNameFound);
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
                    //包括滞纳金
                    getReadOnlyContext().select(Tables.EH_PAYMENT_LATE_FINE.AMOUNT)
                        .from(Tables.EH_PAYMENT_LATE_FINE)
                        .where(Tables.EH_PAYMENT_LATE_FINE.BILL_ITEM_ID.eq(itemDTO.getBillItemId()))
                        .fetch()
                        .forEach(rrr ->{
                            BigDecimal value = rrr.getValue(Tables.EH_PAYMENT_LATE_FINE.AMOUNT);
                            if(value != null){
                            	itemDTO.setLateFineAmount(value);
                            }
                    });
                    if(itemDTO.getLateFineAmount() == null) {
                    	itemDTO.setLateFineAmount(BigDecimal.ZERO);
                    }
                    list1.add(itemDTO);
                    return null;
                });
        context.select(t.AMOUNT,t.ID, t.REMARKS)
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
	
	public IsProjectNavigateDefaultResp isChargingItemsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        Byte decouplingFlag = new Byte("1");//用于判断是否是使用默认配置，还是处于解耦状态
        List<PaymentChargingItemScope> scopes = context.selectFrom(t1)
        		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(t1.DECOUPLING_FLAG.eq(decouplingFlag))
                .fetchInto(PaymentChargingItemScope.class);
        if(scopes != null && scopes.size() != 0) {
        	response.setDefaultStatus((byte)0);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        }else {
			response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
		}
        return response;
    }
	
	public IsProjectNavigateDefaultResp isChargingStandardsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		DSLContext context = getReadOnlyContext();
        EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
        List<PaymentChargingStandardsScopes> scopes = context.selectFrom(t1)
        		.where(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .fetchInto(PaymentChargingStandardsScopes.class);
        if(scopes != null && scopes.size() == 0) {//判断是否是初始化的时候，初始化的时候全部里面没配置、项目也没配置，该域空间下没有数据
        	response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        }else {//说明该域空间下已经有数据了
        	scopes = context.selectFrom(t1)
            		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .fetchInto(PaymentChargingStandardsScopes.class);
        	if(scopes.size() > 0 && scopes.get(0).getBrotherStandardId() != null) {
        		response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        	}else {
        		response.setDefaultStatus((byte)0);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        	}
        }
        return response;
	}
	
	public IsProjectNavigateDefaultResp isBillGroupsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		DSLContext context = getReadOnlyContext();
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        List<PaymentBillGroup> scopes = context.selectFrom(t1)
        		.where(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .fetchInto(PaymentBillGroup.class);
        if(scopes != null && scopes.size() == 0) {//判断是否是初始化的时候，初始化的时候全部里面没配置、项目也没配置，该域空间下没有数据
        	response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        }else {//说明该域空间下已经有数据了
        	scopes = context.selectFrom(t1)
            		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .fetchInto(PaymentBillGroup.class);
        	if(scopes.size() > 0 && scopes.get(0).getBrotherGroupId() != null) {
        		response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        	}else {
        		response.setDefaultStatus((byte)0);//1：代表使用的是默认配置，0：代表有做过个性化的修改
        	}
        }
       return response;
	}
	
	public void transferOrderPaymentType() {
		DSLContext writeContext = getReadWriteContext();
		EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
		EhAssetPaymentOrder t4 = Tables.EH_ASSET_PAYMENT_ORDER.as("t4");
		writeContext.update(t4)
	        .set(t4.PAYMENT_TYPE, "8")//由于原来payment_type这个字段没存支付方式，所以都是null，默认全部置为支付宝：8
	        .execute();
		writeContext.update(t)
			.set(t.PAYMENT_TYPE, 8)//由于原来payment_type这个字段没存支付方式，所以都是null，默认全部置为支付宝：8
			.where(t.STATUS.eq((byte)1))//0: upfinished; 1: paid off（已缴）
			.execute();
		//根据支付订单ID列表查询订单信息，如果查的到支付方式，那么刷新支付方式
        List<PaymentOrderBillDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhAssetPaymentOrderBills t2 = Tables.EH_ASSET_PAYMENT_ORDER_BILLS.as("t2");
        EhPaymentOrderRecords t3 = Tables.EH_PAYMENT_ORDER_RECORDS.as("t3");
        //EhAssetPaymentOrder t4 = Tables.EH_ASSET_PAYMENT_ORDER.as("t4");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t3.PAYMENT_ORDER_ID, t4.ID, t.ID);
        query.addFrom(t);
        query.addJoin(t2, t.ID.eq(DSL.cast(t2.BILL_ID, Long.class)));
        query.addJoin(t3, t2.ORDER_ID.eq(t3.ORDER_ID));
        query.addJoin(t4, t2.ORDER_ID.eq(t4.ID));
        query.fetch().map(r -> {
        	PaymentOrderBillDTO dto = new PaymentOrderBillDTO();
        	dto.setUserId(r.getValue(t.ID));//存eh_payment_bills表的id，方便更新eh_payment_bills表的支付方式
        	dto.setBillId(r.getValue(t4.ID));//存EH_ASSET_PAYMENT_ORDER表的id，方便更新EH_ASSET_PAYMENT_ORDER表的支付方式
        	dto.setPaymentOrderNum(r.getValue(t3.PAYMENT_ORDER_ID).toString());//订单ID
            list.add(dto);
            return null;
        });
        //调用支付提供的接口查询订单信息
        List<Long> payOrderIds = new ArrayList<>();
        for(PaymentOrderBillDTO dto : list) {
        	payOrderIds.add(Long.parseLong(dto.getPaymentOrderNum()));
        }
        //支付订单信息一次最多查询100个
        while(payOrderIds.size() >= 99) {
        	List<Long> queryIds = payOrderIds.subList(0, 99); 
        	queryAndTransfer(queryIds, list);
        	payOrderIds.removeAll(queryIds);
        }
        queryAndTransfer(payOrderIds, list);
    }
	
	private void queryAndTransfer(List<Long> queryIds, List<PaymentOrderBillDTO> list) {
		DSLContext writeContext = getReadWriteContext();
		EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
		EhAssetPaymentOrder t4 = Tables.EH_ASSET_PAYMENT_ORDER.as("t4");
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("transferOrderPaymentType(request), cmd={}", queryIds);
        }
        List<OrderDTO> payOrderDTOs = payServiceV2.listPayOrderByIds(queryIds);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("transferOrderPaymentType(response), response={}", GsonUtil.toJson(payOrderDTOs));
        }
        if(payOrderDTOs != null && payOrderDTOs.size() != 0) {
        	for(int i = 0;i < payOrderDTOs.size();i++) {
            	for(int j = 0;j < list.size();j++) {
            		if(payOrderDTOs.get(i).getId() != null && list.get(j).getPaymentOrderNum() != null &&
            				payOrderDTOs.get(i).getId().equals(Long.parseLong(list.get(j).getPaymentOrderNum()))){
            			PaymentOrderBillDTO dto = list.get(j);
            			OrderDTO orderDTO = payOrderDTOs.get(i);
            			if(orderDTO.getPaymentType() != null) {
            				writeContext.update(t4)
	                            .set(t4.PAYMENT_TYPE, orderDTO.getPaymentType().toString())
	                            .where(t4.ID.eq(dto.getBillId()))
	                            .execute();
            				writeContext.update(t)
	                            .set(t.PAYMENT_TYPE, orderDTO.getPaymentType())
	                            .where(t.ID.eq(dto.getBillId()))
	                            .execute();
            			}
            		}
            	}
            }
        }
	}

    @Override
    public Long getOriginIdFromMappingApp(final Long moduleId, final Long originId, long targetModuleId, Integer namespaceId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
	    if(moduleId == PrivilegeConstants.ENERGY_MODULE && targetModuleId == PrivilegeConstants.ASSET_MODULE_ID){
            List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ENERGY_FLAG.eq(AppMappingEnergyFlag.YES.getCode()))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
            if(records.size() > 0) return records.get(0);
            return null;
        }
        if(originId == null) return null;
        Long ret = null;

        if(targetModuleId == PrivilegeConstants.ASSET_MODULE_ID && moduleId == PrivilegeConstants.CONTRACT_MODULE){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(originId))
                    .fetchOne(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
        }else if(targetModuleId == PrivilegeConstants.CONTRACT_MODULE && moduleId == PrivilegeConstants.ASSET_MODULE_ID){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(originId))
                    .fetchOne(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID);
        }
        return ret;
    }

    @Override
    public Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId) {
        return getOriginIdFromMappingApp(moduleId, originId, targetModuleId, null);
    }

    @Override
    public void insertAppMapping(com.everhomes.server.schema.tables.pojos.EhAssetModuleAppMappings relation) {
        EhAssetModuleAppMappingsDao dao = new EhAssetModuleAppMappingsDao(getReadWriteContext().configuration());
        dao.insert(relation);
	}


    @Override
    public void updateAnAppMapping(UpdateAnAppMappingCommand cmd) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        boolean alreadyPaired = isAlreadyPaired(cmd.getAssetCategoryId(), cmd.getContractCategoryId());
//        if(alreadyPaired){
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.SUCCESS, "already paired, no need pair again");
//        }
//        boolean existAsset = checkExistAsset(cmd.getAssetCategoryId());
//        boolean existContract = checkExistContract(cmd.getContractCategoryId());
//        if(existAsset && existContract){
//            Integer namespaceIdAsset = findNamespaceByAsset(cmd.getAssetCategoryId());
//            Integer namespaceIdContract = findNamespaceByContractId(cmd.getContractCategoryId());
//            if(namespaceIdAsset != namespaceIdContract){
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                        "asset app and contract app do not have the same namespaceId");
//            }
//            AssetModuleAppMapping mapping = new AssetModuleAppMapping();
//            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetModuleAppMappings.class));
//            mapping.setId(nextSequence);
//            mapping.setAssetCategoryId(cmd.getAssetCategoryId());
//            mapping.setContractCategoryId(cmd.getContractCategoryId());
//            mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            mapping.setCreateUid(UserContext.currentUserId());
//            mapping.setNamespaceId(namespaceIdAsset);
//            mapping.setStatus(AppMappingStatus.ACTIVE.getCode());
//            mapping.setEnergyFlag(AppMappingEnergyFlag.NO.getCode());
//            this.dbProvider.execute((status) -> {
//                deleteByContractAndAsset(cmd.getContractCategoryId(), cmd.getAssetCategoryId());
//                insertAppMapping(mapping);
//                return null;
//            });
//        }else if(existAsset && !existContract){
//            dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                    .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID, cmd.getContractCategoryId())
//                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(cmd.getAssetCategoryId()));
//        }else if(!existAsset && existContract){
//            dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                    .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID, cmd.getAssetCategoryId())
//                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(cmd.getContractCategoryId()));
//        }else{
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                    "asset category id and contract id does not exist yes");
//        }
    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
    	dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID, cmd.getContractCategoryId())
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_ORIGINID, cmd.getContractOriginId())
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CHANGEFLAG, cmd.getContractChangeFlag())
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ENERGY_FLAG, cmd.getEnergyFlag())
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_UID, UserContext.currentUserId())
	        .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(cmd.getAssetCategoryId()))
	        .execute();
    }

//    private boolean isAlreadyPaired(Long assetCategoryId, Long contractCategoryId) {
//        List<Long> fetch = this.dbProvider.getDslContext(AccessSpec.readOnly()).select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
//                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
//                .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(contractCategoryId))
//                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
//        return fetch.size() > 0;
//    }
//
//    private void deleteByContractAndAsset(Long contractCategoryId, Long assetCategoryId) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        dslContext.delete(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(contractCategoryId))
//                .or(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
//                .execute();
//    }
//
//    private Integer findNamespaceByContractId(Long contractCategoryId) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        Integer namespaceId = null;
//        List<Integer> fetch = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID)
//                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(contractCategoryId))
//                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID);
//        if(fetch.size() > 0) return fetch.get(0);
//        return null;
//    }
//
//    private Integer findNamespaceByAsset(Long assetCategoryId) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        Integer namespaceId = null;
//        List<Integer> fetch = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID)
//                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
//                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID);
//        if(fetch.size() > 0) return fetch.get(0);
//        return null;
//    }

    @Override
    public boolean checkExistAsset(Long assetCategoryId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
        return records.size() > 0;
    }

    @Override
    public boolean checkExistContract(Long contractCategoryId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(contractCategoryId))
                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
        return records.size() > 0;
    }

    @Override
    public Long checkEnergyFlag(Integer namespaceID) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(namespaceID))
                .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ENERGY_FLAG.eq(AppMappingEnergyFlag.YES.getCode()))
                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
	    if(records.size()>0){
	        return records.get(0);
        }
        return null;
    }

    @Override
    public void changeEnergyFlag(Long mappingId, AppMappingEnergyFlag flag) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ENERGY_FLAG, flag.getCode())
                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID.eq(mappingId))
                .execute();
    }
    
    public String getProjectNameByBillID(Long billId) {
		String projectName = getReadOnlyContext().select(Tables.EH_COMMUNITIES.NAME)
	        .from(Tables.EH_COMMUNITIES,Tables.EH_PAYMENT_BILLS)
	        .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
	        .and(Tables.EH_COMMUNITIES.ID.eq(Tables.EH_PAYMENT_BILLS.OWNER_ID))
	        .fetchOne(Tables.EH_COMMUNITIES.NAME);
		return projectName;
	}
	
	public ListBillDetailVO listBillDetailForPaymentForEnt(Long billId, ListPaymentBillCmd cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        ListBillDetailVO vo = new ListBillDetailVO();
        BillGroupDTO dto = new BillGroupDTO();
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID,r.TARGET_ID,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM);
        query.addFrom(r);
        query.addConditions(r.ID.eq(billId));
        query.fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    vo.setDateStr(f.getValue(r.DATE_STR));
                    vo.setDateStrBegin(f.getValue(r.DATE_STR_BEGIN));
                    vo.setDateStrEnd(f.getValue(r.DATE_STR_END));
                    vo.setTargetName(f.getValue(r.TARGET_NAME));
                    vo.setTargetType(f.getValue(r.TARGET_TYPE));
                    vo.setContractNum(f.getValue(r.CONTRACT_NUM));
                    String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS)
                    		.where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(f.getValue(r.BILL_GROUP_ID))).fetchOne(0,String.class);
                    vo.setBillGroupName(billGroupNameFound);
                    return null;
                });
        vo.setBillGroupDTO(dto);
        return vo;
    }
    
    public ShowCreateBillSubItemListDTO showCreateBillSubItemList(ShowCreateBillSubItemListCmd cmd) {
    	//卸载参数
    	Long billGroupId = cmd.getBillGroupId();
    	Long categoryId = cmd.getCategoryId();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules rule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("rule");
        EhPaymentChargingItemScopes ci = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("ci");

        ShowCreateBillSubItemListDTO response = new ShowCreateBillSubItemListDTO();
        List<SubItemDTO> list = new ArrayList<>();

        context.select(rule.CHARGING_ITEM_ID,ci.PROJECT_LEVEL_NAME,rule.ID)
                .from(rule,ci)
                .where(rule.CHARGING_ITEM_ID.eq(ci.CHARGING_ITEM_ID))
                .and(rule.OWNERID.eq(ci.OWNER_ID))
                .and(rule.BILL_GROUP_ID.eq(billGroupId))
                .and(ci.CATEGORY_ID.eq(categoryId))
                .fetch()
                .map(r -> {
                	//减免费项
                    SubItemDTO dto = new SubItemDTO();
                    dto.setSubtractionType(AssetSubtractionType.item.getCode());
                    dto.setChargingItemId(r.getValue(rule.CHARGING_ITEM_ID));
                    dto.setChargingItemName(r.getValue(ci.PROJECT_LEVEL_NAME));
                    list.add(dto);
                    //减免费项对应的滞纳金
                    SubItemDTO dtoLateFine = new SubItemDTO();
                    dtoLateFine.setSubtractionType(AssetSubtractionType.lateFine.getCode());
                    dtoLateFine.setChargingItemId(r.getValue(rule.CHARGING_ITEM_ID));
                    dtoLateFine.setChargingItemName(r.getValue(ci.PROJECT_LEVEL_NAME) + "滞纳金");
                    list.add(dtoLateFine);
                    return null;});
        
        response.setBillGroupId(billGroupId);
        response.setSubItemDTOList(list);
        List<String> fetch = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .and(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(categoryId))
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS.NAME);
        if(fetch.size() > 0){
            response.setBillGroupName(fetch.get(0));
        }
        return response;
	}

	public void batchModifyBillSubItem(BatchModifyBillSubItemCommand cmd) {
		//卸载参数
		Long billGroupId = cmd.getBillGroupId();
		Long categoryId = cmd.getCategoryId();
    	String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();
        List<Long> billIdList = cmd.getBillIdList();
		List<SubItemDTO> subItemDTOList = cmd.getSubItemDTOList();
		
		//修改减免费项配置
		this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            com.everhomes.server.schema.tables.EhPaymentSubtractionItems t3 = Tables.EH_PAYMENT_SUBTRACTION_ITEMS.as("t3");
            List<com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
            if(billIdList != null && subItemDTOList != null ) {
            	//先删除：根据billId删除该账单原来的减免费项配置
                context.delete(t3)
                        .where(t3.BILL_ID.in(billIdList))
                        .execute();
                //后插入：新增修改后的配置
            	for(int i = 0; i < subItemDTOList.size(); i++){
                    SubItemDTO dto = subItemDTOList.get(i);
                    for(int j = 0;j < billIdList.size();j++) {
                    	long currentSubtractionItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
                        if(currentSubtractionItemSeq == 0){
                           this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
                        }
                    	Long billId = billIdList.get(j);
                    	PaymentSubtractionItem subtractionItem = new PaymentSubtractionItem();
                		subtractionItem.setId(currentSubtractionItemSeq);
                		subtractionItem.setNamespaceId(UserContext.getCurrentNamespaceId());
                		subtractionItem.setCategoryId(categoryId);
                		subtractionItem.setOwnerId(ownerId);
                		subtractionItem.setOwnerType(ownerType);
                		subtractionItem.setBillGroupId(billGroupId);
                		subtractionItem.setSubtractionType(dto.getSubtractionType());
                		subtractionItem.setChargingItemId(dto.getChargingItemId());
                		subtractionItem.setChargingItemName(dto.getChargingItemName());
                		subtractionItem.setCreatorUid(UserContext.currentUserId());
                		subtractionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                		subtractionItem.setBillId(billId);
                		subtractionItemsList.add(subtractionItem);
                    }
            	}
            	EhPaymentSubtractionItemsDao subtractionItemsDao = new EhPaymentSubtractionItemsDao(context.configuration());
                subtractionItemsDao.insert(subtractionItemsList);
                
                for(int j = 0;j < billIdList.size();j++) {
                	Long billId = billIdList.get(j);
                	reCalBillById(billId);//重新计算账单
                }
            }
            return null;
        });
	}
	
	//根据billId和billItemId获取费项表的charingItemId
	public Long getPaymentBillItemsChargingItemID(Long billId, Long billItemId) {
		Long chargingItemId = getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID)
	            .from(Tables.EH_PAYMENT_BILL_ITEMS)
	            .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
	            .and(Tables.EH_PAYMENT_BILL_ITEMS.ID.eq(billItemId))
	            .fetchOne(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID);
		return chargingItemId;
	}
	
	//取出所有的减免费项配置
	public List<EhPaymentSubtractionItems> getSubtractionItemsByBillId(Long billId){
		List<EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
		getReadOnlyContext().select(Tables.EH_PAYMENT_SUBTRACTION_ITEMS.SUBTRACTION_TYPE,Tables.EH_PAYMENT_SUBTRACTION_ITEMS.CHARGING_ITEM_ID,
        		Tables.EH_PAYMENT_SUBTRACTION_ITEMS.CHARGING_ITEM_NAME)
	        .from(Tables.EH_PAYMENT_SUBTRACTION_ITEMS)
	        .where(Tables.EH_PAYMENT_SUBTRACTION_ITEMS.BILL_ID.eq(billId))
	        .fetch()
	        .forEach(r -> {
	        	PaymentSubtractionItem subtractionItem = new PaymentSubtractionItem();
	        	subtractionItem.setSubtractionType(r.getValue(Tables.EH_PAYMENT_SUBTRACTION_ITEMS.SUBTRACTION_TYPE));
	        	subtractionItem.setChargingItemId(r.getValue(Tables.EH_PAYMENT_SUBTRACTION_ITEMS.CHARGING_ITEM_ID));
	        	subtractionItem.setChargingItemName(r.getValue(Tables.EH_PAYMENT_SUBTRACTION_ITEMS.CHARGING_ITEM_NAME));
	        	subtractionItemsList.add(subtractionItem);
	        });
		return subtractionItemsList;
	}
	
	//判断收费项是否配置了减免费项
	public Boolean isConfigItemSubtraction(Long billId, Long charingItemId) {
		List<EhPaymentSubtractionItems> subtractionItemsList = getSubtractionItemsByBillId(billId);//取出所有的减免费项配置
		Boolean isConfigSubtraction = false;//用于判断该费项是否配置了减免费项
	    if(subtractionItemsList != null) {
	    	for(int i = 0;i < subtractionItemsList.size();i++) {
	    		PaymentSubtractionItem subtractionItem = (PaymentSubtractionItem) subtractionItemsList.get(i);
	    		//如果减免费项类型是"eh_payment_bill_items"，并且charginItemsId相等
	    		if(subtractionItem != null && subtractionItem.getSubtractionType().equals(AssetSubtractionType.item.getCode())) {
	    			if(charingItemId != null && charingItemId.equals(subtractionItem.getChargingItemId())) {
	    				isConfigSubtraction = true;
	        		}
	            }
	    	}
	    }
	    return isConfigSubtraction;
	}
	
	//判断滞纳金是否配置了减免费项
	public Boolean isConfigLateFineSubtraction(Long billId, Long charingItemId) {
		List<EhPaymentSubtractionItems> subtractionItemsList = getSubtractionItemsByBillId(billId);//取出所有的减免费项配置
		Boolean isConfigSubtraction = false;//用于判断该费项是否配置了减免费项
        if(subtractionItemsList != null) {
        	for(int i = 0;i < subtractionItemsList.size();i++) {
        		PaymentSubtractionItem subtractionItem = (PaymentSubtractionItem) subtractionItemsList.get(i);
        		//如果减免费项类型是"eh_payment_late_fine"，并且charginItemsId相等
        		if(subtractionItem != null && subtractionItem.getSubtractionType().equals(AssetSubtractionType.lateFine.getCode())) {
        			if(charingItemId != null && charingItemId.equals(subtractionItem.getChargingItemId())) {
        				isConfigSubtraction = true;
            		}
                }
        	}
        }
	    return isConfigSubtraction;
	}

	public void updatePaymentBillSwitch(BatchUpdateBillsToSettledCmd cmd) {
		//对应到eh_payment_bills表中的switch字段账单属性，0:未出账单;1:已出账单
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
        dslContext.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.SWITCH, new Byte("1"))
                .where(Tables.EH_PAYMENT_BILLS.ID.in(cmd.getBillIdList()))
                .execute();
	}

	public void updatePaymentBillStatus(BatchUpdateBillsToPaidCmd cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //更新 状态，更新 金钱数目，不可逆
        EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
        EhPaymentBillItems item = Tables.EH_PAYMENT_BILL_ITEMS.as("item");
        //BILL
        //手动调整缴费状态时，需要将PAYMENT_TYPE置为0（线下支付）
        context.update(bill)
                .set(bill.STATUS,(byte)1)//对应到eh_payment_bills表中的status字段账单属性，0:未缴;1:已缴
                .set(bill.AMOUNT_RECEIVED,bill.AMOUNT_OWED)
                .set(bill.AMOUNT_OWED,new BigDecimal("0"))
                .set(bill.PAYMENT_TYPE,0)
                .where(bill.ID.in(cmd.getBillIdList()))
                .execute();
        //bill item
        context.update(item)
                .set(item.STATUS,(byte)1)//对应到eh_payment_bills表中的status字段账单属性，0:未缴;1:已缴
                .set(item.AMOUNT_RECEIVED,item.AMOUNT_OWED)
                .set(item.AMOUNT_OWED,new BigDecimal("0"))
                .where(item.BILL_ID.in(cmd.getBillIdList()))
                .execute();
        //bill exemption已经减到bill中了
	}
	
	public List<Long> getOriginIdFromMappingAppForEnergy(final Long moduleId, final Long originId, long targetModuleId, Integer namespaceId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
	    if(moduleId == PrivilegeConstants.ENERGY_MODULE && targetModuleId == PrivilegeConstants.ASSET_MODULE_ID){
            List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ENERGY_FLAG.eq(AppMappingEnergyFlag.YES.getCode()))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
            return records;
        }
        if(originId == null) return null;
        List<Long> ret = null;

        if(targetModuleId == PrivilegeConstants.ASSET_MODULE_ID && moduleId == PrivilegeConstants.CONTRACT_MODULE){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID.eq(originId))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
        }else if(targetModuleId == PrivilegeConstants.CONTRACT_MODULE && moduleId == PrivilegeConstants.ASSET_MODULE_ID){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(originId))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONTRACT_CATEGORY_ID);
        }
        return ret;
    }
	
}
