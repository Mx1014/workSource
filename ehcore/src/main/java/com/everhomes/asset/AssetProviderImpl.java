package com.everhomes.asset;

import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.asset.util.TimeUtils;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.order.PaymentAccount;
import com.everhomes.order.PaymentServiceConfig;
import com.everhomes.order.PaymentUser;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.statistic.BuildingStatisticParam;
import com.everhomes.rest.asset.statistic.CommunityStatisticParam;
import com.everhomes.rest.common.AssetModuleNotifyConstants;
import com.everhomes.rest.contract.ContractTemplateStatus;
import com.everhomes.rest.organization.OrganizationAddressStatus;
import com.everhomes.rest.promotion.order.PurchaseOrderPaymentStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.EhAssetBills;
import com.everhomes.server.schema.tables.pojos.EhPaymentBillOrders;
import com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DecimalUtils;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

//import scala.languageFeature.reflectiveCalls;
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

//    @Autowired
//    private ContractProvider contractProvider;
//
//    @Autowired
//    private PayService payService;
    
    @Autowired
    private UserProvider userProvider;

    @Autowired
	private PortalService portalService;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private AssetGroupProvider assetGroupProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;


//    @Override
//    public void creatAssetBill(AssetBill bill) {
//        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetBills.class));
//
//        bill.setId(id);
//        bill.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        bill.setStatus(AssetBillStatus.UNPAID.getCode());
//
//        bill.setUpdateTime(bill.getCreateTime());
//        bill.setUpdateUid(bill.getCreatorUid());
//
//        LOGGER.info("creatAssetBill: " + bill);
//
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBills.class, id));
//        EhAssetBillsDao dao = new EhAssetBillsDao(context.configuration());
//        dao.insert(bill);
//
//        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetBills.class, null);
//    }

//    @Override
//    public void updateAssetBill(AssetBill bill) {
//        assert(bill.getId() != null);
//
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBills.class, bill.getId()));
//        EhAssetBillsDao dao = new EhAssetBillsDao(context.configuration());
//        bill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        dao.update(bill);
//
//        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetBills.class, bill.getId());
//    }

    @Override
    public AssetBill findAssetBill(Long id, Long ownerId, String ownerType, Long targetId, String targetType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);

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

//        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_ASSET_BILL_TEMPLATE_FIELDS.OWNER_TYPE.eq(ownerType));
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

//    @Override
//    public void creatTemplateField(AssetBillTemplateFields field) {
//        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetBillTemplateFields.class));
//
//        field.setId(id);
//
//        LOGGER.info("creatTemplateField: " + field);
//
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBillTemplateFields.class, id));
//        EhAssetBillTemplateFieldsDao dao = new EhAssetBillTemplateFieldsDao(context.configuration());
//        dao.insert(field);
//
//        DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetBillTemplateFields.class, null);
//    }

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

//        if(ownerId != null) {
//            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
//        }
//        if(ownerType != null) {
//            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
//        }
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
//        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));

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

//    @Override
//    public List<AssetBill> listUnpaidBillsGroupByTenant(Long ownerId, String ownerType, Long targetId, String targetType) {
//
//        List<AssetBill> bills = new ArrayList<>();
//
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        SelectQuery<EhAssetBillsRecord> query = context.selectQuery(Tables.EH_ASSET_BILLS);
//
//
////        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
////        query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_ASSET_BILLS.STATUS.eq(AssetBillStatus.UNPAID.getCode()));
//        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_ID.eq(targetId));
//        query.addConditions(Tables.EH_ASSET_BILLS.TARGET_TYPE.eq(targetType));
////
////        query.addGroupBy(Tables.EH_ASSET_BILLS.TENANT_ID, Tables.EH_ASSET_BILLS.TENANT_TYPE);
//
//
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("listUnpaidBillsGroupByTenant, sql=" + query.getSQL());
//            LOGGER.debug("listUnpaidBillsGroupByTenant, bindValues=" + query.getBindValues());
//        }
//
//        query.fetch().map((EhAssetBillsRecord record) -> {
//            bills.add(ConvertHelper.convert(record, AssetBill.class));
//            return null;
//        });
//
//        return bills;
//    }

//    @Override
//    public int countNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType, Timestamp startTime, Timestamp endTime) {
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        SelectQuery<EhAssetBillNotifyRecordsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_NOTIFY_RECORDS);
//
////        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_ID.eq(ownerId));
////        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_ID.eq(targetId));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_TYPE.eq(targetType));
//
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.ge(startTime));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.le(endTime));
//
//        int count = query.fetchCount();
//        return count;
//    }

//    @Override
//    public AssetBillNotifyRecords getLastAssetBillNotifyRecords(Long ownerId, String ownerType, Long targetId, String targetType) {
//        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        SelectQuery<EhAssetBillNotifyRecordsRecord> query = context.selectQuery(Tables.EH_ASSET_BILL_NOTIFY_RECORDS);
//
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_ID.eq(targetId));
//        query.addConditions(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.TARGET_TYPE.eq(targetType));
//
//        query.addOrderBy(Tables.EH_ASSET_BILL_NOTIFY_RECORDS.CREATE_TIME.desc());
//        query.addLimit(1);
//        List<AssetBillNotifyRecords> records = new ArrayList<>();
//        query.fetch().map((EhAssetBillNotifyRecordsRecord record) -> {
//            records.add(ConvertHelper.convert(record, AssetBillNotifyRecords.class));
//            return null;
//        });
//
//        if(records.size() == 0) {
//            return null;
//        }
//        return records.get(0);
//    }

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
//        if(ownerId != null) {
//            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_ID.eq(ownerId));
//        }
//        if(ownerType != null) {
//            query.addConditions(Tables.EH_ASSET_BILLS.OWNER_TYPE.eq(ownerType));
//        }
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
    	LOGGER.info("AssetProviderImpl listBills currentNamespaceId={}, cmd={}", currentNamespaceId, cmd.toString());
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
        // 应用id，多入口区分账单 by wentian 2018/5/25
        Long categoryId = cmd.getCategoryId();
        Integer paymentType = cmd.getPaymentType();
        Byte isUploadCertificate = cmd.getIsUploadCertificate();
        String buildingName = cmd.getBuildingName();//楼栋名称
        String apartmentName = cmd.getApartmentName();//门牌名称
        String customerTel = cmd.getCustomerTel();//客户手机号
        Long targetIdForEnt = cmd.getTargetIdForEnt();//对公转账是根据企业id来查询相关的所有账单，如果是对公转账则不能为空
        Long dueDayCountStart = cmd.getDueDayCountStart();//欠费天数开始范围
        Long dueDayCountEnd = cmd.getDueDayCountEnd();//欠费天数结束范围
        String sourceName = cmd.getSourceName();//新增账单来源信息
        Byte deleteFlag = cmd.getDeleteFlag();//物业缴费V6.0 账单、费项表增加是否删除状态字段

        //卸货结束
        List<ListBillsDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.AMOUNT_OWED,t.AMOUNT_RECEIVED,t.AMOUNT_RECEIVABLE,t.STATUS,t.NOTICETEL,t.NOTICE_TIMES,
                t.DATE_STR,t.TARGET_NAME,t.TARGET_ID,t.TARGET_TYPE,t.OWNER_ID,t.OWNER_TYPE,t.CONTRACT_NUM,t.CONTRACT_ID,t.BILL_GROUP_ID,
                t.INVOICE_NUMBER,t.PAYMENT_TYPE,t.DATE_STR_BEGIN,t.DATE_STR_END,t.CUSTOMER_TEL,
        		DSL.groupConcatDistinct(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME)).as("addresses"),
        		t.DUE_DAY_COUNT,t.SOURCE_TYPE,t.SOURCE_ID,t.SOURCE_NAME,t.CONSUME_USER_ID,t.DELETE_FLAG,t.CAN_DELETE,t.CAN_MODIFY,t.IS_READONLY,
        		t.TAX_AMOUNT);
//        query.addFrom(t, t2);
//        query.addConditions(t.ID.eq(t2.BILL_ID));
        query.addFrom(t);
        query.addJoin(t2, t.ID.eq(t2.BILL_ID));
        		
        query.addConditions(t.NAMESPACE_ID.eq(currentNamespaceId));
        if(!org.springframework.util.StringUtils.isEmpty(ownerType)) {
        	query.addConditions(t.OWNER_TYPE.eq(ownerType));
        }
        if(!org.springframework.util.StringUtils.isEmpty(ownerId)) {
        	query.addConditions(t.OWNER_ID.eq(ownerId));
        }
        
        if(categoryId != null){
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }
        //增加欠费天数查询条件 : 0＜天数≤30,30＜天数≤60
        if(!org.springframework.util.StringUtils.isEmpty(dueDayCountStart)) {
        	query.addConditions(t.DUE_DAY_COUNT.greaterThan(dueDayCountStart));
        }
        if(!org.springframework.util.StringUtils.isEmpty(dueDayCountEnd)) {
        	query.addConditions(t.DUE_DAY_COUNT.lessOrEqual(dueDayCountEnd));
        }

        //status[Byte]:账单属性，0:未出账单;1:已出账单，对应到eh_payment_bills表中的switch字段
        if(!org.springframework.util.StringUtils.isEmpty(status)){
            query.addConditions(t.SWITCH.eq(status));
        }
        //物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单），因为是同步账单，所以已出、未出都要同步
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getSwitchList())){
            query.addConditions(t.SWITCH.in(cmd.getSwitchList()));
        }
        //物业缴费V7.5（中天-资管与财务EAS系统对接）：更新时间，为空全量同步数据，不为空是增量同步（该时间点以后的数据信息），使用1970-01-01 00:00:00开始到现在的毫秒数（时间戳）；
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getUpdateTime())) {
        	try {
        		String updateTime = TimeUtils.timestampToDate(cmd.getUpdateTime());
        		query.addConditions(DSL.cast(t.UPDATE_TIME, String.class).greaterOrEqual(updateTime)
                		.or(DSL.cast(t.CREAT_TIME, String.class).greaterOrEqual(updateTime)));
        	}catch (Exception e) {
        		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
	                    "This updateTime is error, updateTime={" + cmd.getUpdateTime() + "}");
			}
        }
        //物业缴费V7.4(瑞安项目-资产管理对接CM系统) 通过账单内是否包含服务费用（资源预定、云打印）
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getSourceTypeList())){
            query.addConditions(t2.SOURCE_TYPE.in(cmd.getSourceTypeList()));
        }
        //物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步会再次传输
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getThirdSign())){
            query.addConditions(t.THIRD_SIGN.eq(cmd.getThirdSign()).or(t.THIRD_SIGN.isNull()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getContractId())) {
        	query.addConditions(t.CONTRACT_ID.eq(cmd.getContractId()));
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
        //新增账单来源信息查询条件
        if(!org.springframework.util.StringUtils.isEmpty(sourceName)) {
        	query.addConditions(t.SOURCE_NAME.like("%"+sourceName+"%"));
        }
        //物业缴费V6.0 账单、费项表增加是否删除状态字段,0：已删除；1：正常使用
        if(!org.springframework.util.StringUtils.isEmpty(deleteFlag)) {
        	query.addConditions(t.DELETE_FLAG.eq(deleteFlag));
        	query.addConditions(t2.DELETE_FLAG.eq(deleteFlag));
        }
        query.addGroupBy(t.ID);
        //需根据收费项的楼栋门牌进行查询，不能直接根据账单的楼栋门牌进行查询
        if(!org.springframework.util.StringUtils.isEmpty(buildingName) || !org.springframework.util.StringUtils.isEmpty(apartmentName)) {
        	buildingName = buildingName != null ? buildingName : "";
        	apartmentName = apartmentName != null ? apartmentName : "";
        	String queryAddress = buildingName + "/" + apartmentName;
        	query.addHaving(DSL.groupConcatDistinct(DSL.concat(t2.BUILDING_NAME,DSL.val("/"), t2.APARTMENT_NAME)).like("%"+queryAddress+"%"));
        }
        //物业缴费V6.0 账单列表处增加筛选项：欠费金额、应收、已收、待收等排序
        if(cmd.getSorts() != null) {
        	List<ReSortCmd> sorts = cmd.getSorts();
        	if(sorts != null && !sorts.isEmpty()) {
                for(ReSortCmd sort : sorts) {
                    if(sort.getSortField().equals("amount_receivable")) {
                        if(sort.getSortType().equals("desc")) {
                        	query.addOrderBy(t.AMOUNT_RECEIVABLE.desc());
                        } else {
                        	query.addOrderBy(t.AMOUNT_RECEIVABLE.asc());
                        }
                    }else if(sort.getSortField().equals("amount_received")) {
                        if(sort.getSortType().equals("desc")) {
                        	query.addOrderBy(t.AMOUNT_RECEIVED.desc());
                        } else {
                        	query.addOrderBy(t.AMOUNT_RECEIVED.asc());
                        }
                    }else if(sort.getSortField().equals("amount_owed")) {
                        if(sort.getSortType().equals("desc")) {
                        	query.addOrderBy(t.AMOUNT_OWED.desc());
                        } else {
                        	query.addOrderBy(t.AMOUNT_OWED.asc());
                        }
                    }
                }
            }
        }
        if(status!=null && status == 1){
            query.addOrderBy(t.STATUS);
        }
        //瑞安CM对接，如果企业关联的是瑞安那边系统中的资产，则需要将这部分产生的账单查出来
        if(!org.springframework.util.StringUtils.isEmpty(cmd.getIsCheckProperty())) {
			query.addJoin(Tables.EH_ORGANIZATION_ADDRESSES,JoinType.LEFT_OUTER_JOIN,t.TARGET_ID.eq(Tables.EH_ORGANIZATION_ADDRESSES.ORGANIZATION_ID));
			query.addJoin(Tables.EH_ADDRESSES,JoinType.LEFT_OUTER_JOIN,Tables.EH_ORGANIZATION_ADDRESSES.ADDRESS_ID.eq(Tables.EH_ADDRESSES.ID));
			query.addConditions(Tables.EH_ADDRESSES.NAMESPACE_ADDRESS_TYPE.eq("ruian_cm"));
			query.addConditions(Tables.EH_ORGANIZATION_ADDRESSES.STATUS.eq(OrganizationAddressStatus.ACTIVE.getCode()));
			query.addConditions(Tables.EH_ADDRESSES.STATUS.eq(AddressAdminStatus.ACTIVE.getCode()));
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
            dto.setTaxAmount(r.getValue(t.TAX_AMOUNT));
            if(!org.springframework.util.StringUtils.isEmpty(billGroupName)) {
                dto.setBillGroupName(billGroupName);
            }else{
                String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS).where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(r.getValue(t.BILL_GROUP_ID))).fetchOne(0,String.class);
                dto.setBillGroupName(billGroupNameFound);
            }
            dto.setBillId(String.valueOf(r.getValue(t.ID)));
            dto.setBillStatus(r.getValue(t.STATUS));
            //dto.setNoticeTelList(r.getValue(t.NOTICETEL));
            if (r.getValue(t.NOTICETEL) != null) {
            	dto.setNoticeTelList(Arrays.asList((r.getValue(t.NOTICETEL)).split(",")));
			}
            dto.setNoticeTimes(r.getValue(t.NOTICE_TIMES));
            dto.setDateStr(r.getValue(t.DATE_STR));
            dto.setTargetId(r.getValue(t.TARGET_ID, String.class));
            dto.setTargetName(r.getValue(t.TARGET_NAME));
            dto.setTargetType(r.getValue(t.TARGET_TYPE));
            //如果企业客户类型下+客户名称是空+客户ID不为空，那么需根据客户ID找到客户名称
            if(dto.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode()) && dto.getTargetName() == null && dto.getTargetId() != null) {
            	Organization organization = organizationProvider.findOrganizationById(r.getValue(t.TARGET_ID));
            	dto.setTargetName(organization.getName());
            }
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
            //增加欠费天数
            dto.setDueDayCount(r.getValue(t.DUE_DAY_COUNT));
            //新增账单来源信息
            dto.setSourceType(r.getValue(t.SOURCE_TYPE));
            dto.setSourceId(r.getValue(t.SOURCE_ID));
            dto.setSourceName(r.getValue(t.SOURCE_NAME));
            dto.setConsumeUserId(r.getValue(t.CONSUME_USER_ID));
            //物业缴费V6.0 账单、费项表增加是否删除状态字段,0：已删除；1：正常使用
            dto.setDeleteFlag(r.getValue(t.DELETE_FLAG));
            //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
            dto.setCanDelete(r.getValue(t.CAN_DELETE));
            dto.setCanModify(r.getValue(t.CAN_MODIFY));
            //瑞安CM对接 账单、费项表增加是否是只读字段:只读状态：0：非只读；1：只读
            dto.setIsReadOnly(r.getValue(t.IS_READONLY));
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
                .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode())) //物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                    .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode())) //物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode())) //物业缴费V6.0 账单、费项表增加是否删除状态字段
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
        query.addConditions(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段

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
        		,t.ENERGY_CONSUME,t.CHARGING_ITEMS_ID,t.NAMESPACE_ID,t.OWNER_ID,t.OWNER_TYPE,t.CATEGORY_ID)
                .from(t)
                .where(t.BILL_ID.eq(billId))
                .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetch()
                .map(r -> {
                    ShowBillDetailForClientDTO dto = new ShowBillDetailForClientDTO();
                    dto.setAmountOwed(r.getValue(t.AMOUNT_OWED));
                    dto.setBillItemName(r.getValue(t.CHARGING_ITEM_NAME));
                    //APP端也需更新:备注名称改为显示名称
                    Integer namespaceId = r.getValue(t.NAMESPACE_ID);
                    Long ownerId = r.getValue(t.OWNER_ID);
                    String ownerType = r.getValue(t.OWNER_TYPE);
                    Long chargingItemId = r.getValue(t.CHARGING_ITEMS_ID);
                    Long categoryId = r.getValue(t.CATEGORY_ID);
                    String projectChargingItemName = getProjectChargingItemName(namespaceId, ownerId, ownerType, chargingItemId, categoryId);
                    dto.setProjectChargingItemName(projectChargingItemName);
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
                .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
    public ShowCreateBillDTO showCreateBill(Long billGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules rule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("rule");
//        EhPaymentBillGroups bg = Tables.EH_PAYMENT_BILL_GROUPS.as("bg");
        EhPaymentChargingItemScopes ci = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("ci");


        ShowCreateBillDTO response = new ShowCreateBillDTO();
        List<BillItemDTO> list = new ArrayList<>();
        Long categoryId = findCategoryIdFromBillGroup(billGroupId);
        context.select(rule.CHARGING_ITEM_ID,ci.PROJECT_LEVEL_NAME,rule.ID,ci.TAX_RATE)
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
                    dto.setTaxRate(r.getValue(ci.TAX_RATE));//增加税率
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
        query.addConditions(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
            //获取所需要的cmd的数据
            BillGroupDTO billGroupDTO = cmd.getBillGroupDTO();
            Byte isSettled = cmd.getIsSettled();
            //String noticeTel = cmd.getNoticeTel();
            List<String> noticeTelList = cmd.getNoticeTelList();
            String noticeTelListStr = "";
            if (noticeTelList != null) {
                noticeTelListStr = String.join(",", noticeTelList);
            }
            Long ownerId = cmd.getOwnerId();
            String ownerType = cmd.getOwnerType();
            String targetName = cmd.getTargetName();
            Long targetId = cmd.getTargetId();
            String targetType = cmd.getTargetType();
            String contractNum = cmd.getContractNum();
            Long contractId = cmd.getContractId();
            String dateStrBegin = cmd.getDateStrBegin();
            String dateStrEnd = cmd.getDateStrEnd();
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
            if (list1 != null && list1.size() > 0) {
                BillItemDTO itemDTO = list1.get(0);
                apartmentName = itemDTO.getApartmentName();
                buildingName = itemDTO.getBuildingName();
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

            //根据billGroup获得时间，如需重复使用，则请抽象出来
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
            //根据账单组设置生成账单的账期、账单开始时间、账单结束时间、出账单日、最晚还款日
            AssetBillDateDTO assetBillDateDTO = generateBillDate(billGroupId, dateStrBegin, dateStrEnd);
            //检查明细生成日期是否在现有账单周期中，若在则返回该账单ID，否则返回null。
            Long checkedBillId = null;
            if (cmd.getCanMergeBillItem()) {
                try {
                    checkedBillId = checkBillItemIsInBill(assetBillDateDTO, cmd);
                } catch (ParseException parseException) {
                    LOGGER.error("parse date error");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                            "parse date error");
                }
            }
            //nextBillId优先顺序：1.入参billID，2.检查的checkedBillId，3.自增长
            long nextBillId;
            if (billId != null) {
                nextBillId = billId;
            } else if (checkedBillId != null) {
                nextBillId = checkedBillId;
            } else {
                nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
                if (nextBillId == 0) {
                    nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
                }
            }

            if (list2 != null) {
                //bill exemption
                List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
//                long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class), list2.size());
                for (int i = 0; i < list2.size(); i++) {
                    long currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
                    if (currentExemItemSeq == 0) {
                        currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
                    }
                    ExemptionItemDTO exemptionItemDTO = list2.get(i);
                    PaymentExemptionItems exemptionItem = new PaymentExemptionItems();
                    BigDecimal amount = exemptionItemDTO.getAmount();
                    if (amount == null) {
                        continue;
                    }
                    exemptionItem.setAmount(amount);
                    exemptionItem.setBillGroupId(billGroupId);
                    exemptionItem.setBillId(nextBillId);
                    if (cmd.getCanMergeBillItem()){
                        exemptionItem.setMerchantOrderId(Long.valueOf(cmd.getMerchantOrderId()));
                    }
                    exemptionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    exemptionItem.setCreatorUid(UserContext.currentUserId());
                    exemptionItem.setId(currentExemItemSeq);
                    currentExemItemSeq += 1;
                    exemptionItem.setRemarks(exemptionItemDTO.getRemark());
                    if (targetType != null) {
                        exemptionItem.setTargetType(targetType);
                    }
                    if (targetId != null) {
                        exemptionItem.setTargetId(targetId);
                    }
                    exemptionItem.setTargetname(targetName);
                    exemptionItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

                    exemptionItems.add(exemptionItem);

                    if (amount.compareTo(zero) == -1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte) 0)) {
                        amount = amount.multiply(new BigDecimal("-1"));
                        amountExemption = amountExemption.add(amount);
                    } else if (amount.compareTo(zero) == 1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte) 1)) {
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
            if (list1 != null) {
                for (int i = 0; i < list1.size(); i++) {
                    long currentBillItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                    if (currentBillItemSeq == 0) {
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
                    item.setChargingItemsId(dto.getChargingItemsId());
                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    item.setCreatorUid(UserContext.currentUserId());
                    item.setDateStr(assetBillDateDTO.getDateStr());
                    //时间假定
                    item.setDateStrBegin(assetBillDateDTO.getDateStrBegin());
                    item.setDateStrEnd(assetBillDateDTO.getDateStrEnd());
                    item.setId(currentBillItemSeq);
                    item.setNamespaceId(UserContext.getCurrentNamespaceId());
                    item.setOwnerType(ownerType);
                    item.setOwnerId(ownerId);
                    item.setContractId(contractId);
                    item.setContractNum(contractNum);
                    // item 也添加categoryId， 这样费用清单简单些
                    item.setCategoryId(categoryId);
                    if (targetType != null) {
                        item.setTargetType(targetType);
                    }
                    if (targetId != null) {
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
                    item.setTaxRate(dto.getTaxRate());//费项增加税率信息
                    //物业缴费V6.6（对接统一账单） 账单要增加来源
                    item.setSourceId(cmd.getSourceId());
                    item.setSourceType(cmd.getSourceType());
                    item.setSourceName(cmd.getSourceName());
                    item.setConsumeUserId(cmd.getConsumeUserId());
                    item.setConsumeUserName(cmd.getConsumeUserName());
                    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                    item.setCanDelete(cmd.getCanDelete());
                    item.setCanModify(cmd.getCanModify());
                    //物业缴费V6.0 账单、费项表增加是否删除状态字段
                    item.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
                    //瑞安CM对接 账单、费项表增加是否是只读字段
                    item.setIsReadonly((byte) 0);//只读状态：0：非只读；1：只读
                    //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识
                    item.setMerchantOrderId(cmd.getMerchantOrderId());
                    //物业缴费V7.1（企业记账流程打通）: 增加商品信息字段
                    item.setGoodsServeType(dto.getGoodsServeType());
                    item.setGoodsNamespace(dto.getGoodsNamespace());
                    item.setGoodsTag1(dto.getGoodsTag1());
                    item.setGoodsTag2(dto.getGoodsTag2());
                    item.setGoodsTag3(dto.getGoodsTag3());
                    item.setGoodsTag4(dto.getGoodsTag4());
                    item.setGoodsTag5(dto.getGoodsTag5());
                    item.setGoodsServeApplyName(dto.getGoodsServeApplyName());
                    item.setGoodsTag(dto.getGoodsTag());
                    item.setGoodsName(dto.getGoodsName());
                    item.setGoodsDescription(dto.getGoodsDescription());
                    item.setGoodsCounts(dto.getGoodsCounts());
                    item.setGoodsPrice(dto.getGoodsPrice());
                    item.setGoodsTotalprice(dto.getGoodsTotalPrice());
                    billItemsList.add(item);

                    amountReceivable = amountReceivable.add(var1);
                    amountOwed = amountOwed.add(var1);
                    amountReceivableWithoutTax = amountReceivableWithoutTax.add(var2);//增加应收（不含税）
                    amountOwedWithoutTax = amountOwedWithoutTax.add(var2);//增加应收（不含税）
                    if (dto.getTaxAmount() != null) {
                        taxAmount = taxAmount.add(dto.getTaxAmount());//增加税额
                    }
                }
            }

            //增加减免费项
            if (list3 != null) {
                List<com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
                for (int i = 0; i < list3.size(); i++) {
                    long currentSubtractionItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
                    if (currentSubtractionItemSeq == 0) {
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
                    if (subtractionItem != null && subtractionItem.getSubtractionType().equals(AssetSubtractionType.item.getCode())) {
                        if (billItemsList != null) {
                            for (int j = 0; j < billItemsList.size(); j++) {
                                PaymentBillItems item = (PaymentBillItems) billItemsList.get(j);
                                if (item.getChargingItemsId().equals(subtractionItem.getChargingItemId())) {
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
            if (assetPaymentBillAttachmentList != null) {
                List<com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments> paymentBillAttachmentsList = new ArrayList<>();
                for (int i = 0; i < assetPaymentBillAttachmentList.size(); i++) {
                    long currentSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
                    if (currentSeq == 0) {
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
            for (int i = 0; i < billItemsList.size(); i++) {
                billItemsList.get(i).setStatus(billStatus);
            }
            EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
            billItemsDao.insert(billItemsList);

            com.everhomes.server.schema.tables.pojos.EhPaymentBills newBill = new PaymentBills();
            if (billId==null&&checkedBillId==null){
                //  缺少创造者信息，先保存在其他地方，比如持久化日志
                amountOwed = DecimalUtils.negativeValueFilte(amountOwed);
                newBill.setAmountOwed(amountOwed);
                amountOwedWithoutTax = DecimalUtils.negativeValueFilte(amountOwedWithoutTax);//增加待收（不含税）
                newBill.setAmountOwedWithoutTax(amountOwedWithoutTax);//增加待收（不含税）
//                if(amountOwed.compareTo(zero) == 0) {
//                    newBill.setStatus((byte)1);
//                }else{
//                    newBill.setStatus(billStatus);
//                }
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
                newBill.setDateStr(assetBillDateDTO.getDateStr());
                newBill.setDateStrBegin(assetBillDateDTO.getDateStrBegin());
                newBill.setDateStrEnd(assetBillDateDTO.getDateStrEnd());
                newBill.setDateStrDue(assetBillDateDTO.getDateStrDue());//出账单日
                newBill.setDueDayDeadline(assetBillDateDTO.getDueDayDeadline());//最晚还款日
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
                if (!CollectionUtils.isEmpty(noticeTelList)) {
                    newBill.setNoticetel(String.join(",", noticeTelList));
                }
                newBill.setOwnerId(ownerId);
                newBill.setTargetName(targetName);
                newBill.setOwnerType(ownerType);
                newBill.setTargetType(targetType);
                newBill.setTargetId(targetId);
                newBill.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                newBill.setCreatorId(UserContext.currentUserId());
                newBill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                newBill.setNoticeTimes(0);

                if (isOwed == null) {
                    newBill.setChargeStatus((byte) 0);
                } else {
                    newBill.setChargeStatus(isOwed);
                }
                if (isSettled != null) {
                    newBill.setSwitch(isSettled);
                } else {
                    //物业缴费V6.6（对接统一账单）：如果没有传账单是已出或未出，系统根据出账单日和当前日期做比较，判断是已出还是未出
                    try {
                        Date today = new Date();
                        Date billDay = yyyyMMdd.parse(newBill.getDateStrDue());
                        if (today.compareTo(billDay) >= 0) {
                            newBill.setSwitch((byte) 1);
                        } else {
                            newBill.setSwitch((byte) 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                newBill.setContractId(contractId);
                newBill.setContractNum(contractNum);
                //物业缴费V6.6（对接统一账单） 账单要增加来源
                newBill.setSourceId(cmd.getSourceId());
                newBill.setSourceType(cmd.getSourceType());
                newBill.setSourceName(cmd.getSourceName());
                newBill.setConsumeUserId(cmd.getConsumeUserId());
                newBill.setConsumeUserName(cmd.getConsumeUserName());
                //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                newBill.setCanDelete(cmd.getCanDelete());
                newBill.setCanModify(cmd.getCanModify());
                //物业缴费V6.0 账单、费项表增加是否删除状态字段
                newBill.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
                //账单能合并到明细中，不对如下属性设置,该设置在明细中存储
                if (!cmd.getCanMergeBillItem()) {
                    //账单表增加第三方账单唯一标识字段
                    newBill.setThirdBillId(cmd.getThirdBillId());
                    //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识
                    newBill.setMerchantOrderId(cmd.getMerchantOrderId());
                }

                //瑞安CM对接 账单、费项表增加是否是只读字段
                newBill.setIsReadonly((byte) 0);//只读状态：0：非只读；1：只读
                EhPaymentBillsDao billsDao = new EhPaymentBillsDao(context.configuration());
                billsDao.insert(newBill);
            }else{
//                否则更新账单
                reCalBillById(nextBillId);//重新计算账单
                newBill = findPaymentBillById(nextBillId);

            }
            response[0] = ConvertHelper.convert(newBill, ListBillsDTO.class);
            response[0].setBillGroupName(billGroupDTO.getBillGroupName());
            response[0].setBillId(String.valueOf(newBill.getId()));
            response[0].setNoticeTelList(noticeTelList);
            response[0].setBillStatus(newBill.getStatus());
            response[0].setTargetType(targetType);
            response[0].setTargetId(String.valueOf(targetId));
            return null;
        });
        return response[0];
    }

//    @Override
//    public ListBillsDTO creatPropertyBillForCommunity( CreateBillCommand cmd){
//        final ListBillsDTO[] response = {new ListBillsDTO()};
//
//        this.dbProvider.execute((TransactionStatus status) -> {
//            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//            //获取所需要的cmd的数据
//            BillGroupDTO billGroupDTO = cmd.getBillGroupDTO();
//            Byte isSettled = cmd.getIsSettled();
//            //String noticeTel = cmd.getNoticeTel();
//            List<String> noticeTelList = cmd.getNoticeTelList();
//            String noticeTelListStr = "";
//            if (noticeTelList != null) {
//                noticeTelListStr = String.join(",", noticeTelList);
//            }
//            Long ownerId = cmd.getOwnerId();
//            String ownerType = cmd.getOwnerType();
//            String targetName = cmd.getTargetName();
//            Long targetId = cmd.getTargetId();
//            String targetType = cmd.getTargetType();
//            String contractNum = cmd.getContractNum();
//            Long contractId = cmd.getContractId();
//            String dateStrBegin = cmd.getDateStrBegin();
//            String dateStrEnd = cmd.getDateStrEnd();
//            Byte isOwed = cmd.getIsOwed();
//            String customerTel = cmd.getCustomerTel();
//            String invoiceNum = cmd.getInvoiceNum();
//            Long categoryId = cmd.getCategoryId();
//
//            //普通信息卸载
//            Long billGroupId = billGroupDTO.getBillGroupId();
//            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
//            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();
////            服务账单不存在减免费项
////            List<SubItemDTO> list3 = billGroupDTO.getSubItemDTOList();//增加减免费项
//            String apartmentName = null;
//            String buildingName = null;
//            if(list1!=null && list1.size() > 0){
//                BillItemDTO itemDTO = list1.get(0);
//                apartmentName= itemDTO.getApartmentName();
//                buildingName = itemDTO.getBuildingName();
//            }
//            //需要组装的信息
//            BigDecimal amountExemption = new BigDecimal("0");
//            BigDecimal amountSupplement = new BigDecimal("0");
//            BigDecimal amountReceivable = new BigDecimal("0");
//            BigDecimal amountOwed = new BigDecimal("0");
//            BigDecimal zero = new BigDecimal("0");
//            BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;//增加应收（不含税）
//            BigDecimal amountOwedWithoutTax = BigDecimal.ZERO;//增加待收（不含税）
//            BigDecimal taxAmount = BigDecimal.ZERO;//增加税额
//
//            //根据billGroup获得时间，如需重复使用，则请抽象出来
//            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
//            //根据账单组设置生成账单的账期、账单开始时间、账单结束时间、出账单日、最晚还款日
//            AssetBillDateDTO assetBillDateDTO = generateBillDate(billGroupId, dateStrBegin, dateStrEnd);
//            //检查明细生成日期是否在现有账单周期中，若在则返回该账单ID，否则返回null。
//            Long billId;
//            try {
//                billId = checkBillItemIsInBill(assetBillDateDTO,cmd);
//            }catch (ParseException parseException){
//                LOGGER.error("parse date error");
//                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//                        "parse date error");
//            }
//            long nextBillId;
//            if(billId != null) {
//                nextBillId = billId;
//            }else {
//                nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                        com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
//                if(nextBillId == 0){
//                    nextBillId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                            com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));
//                }
//            }
//
//            if(list2!=null) {
//                //bill exemption
//                List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
////                long nextExemItemBlock = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(
////                com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class), list2.size());
//                for(int i = 0; i < list2.size(); i++){
//                    long currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                            com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
//                    if(currentExemItemSeq == 0){
//                        currentExemItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                                com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems.class));
//                    }
//                    ExemptionItemDTO exemptionItemDTO = list2.get(i);
//                    PaymentExemptionItems exemptionItem = new PaymentExemptionItems();
//                    BigDecimal amount = exemptionItemDTO.getAmount();
//                    if(amount == null){
//                        continue;
//                    }
//                    exemptionItem.setAmount(amount);
//                    exemptionItem.setBillGroupId(billGroupId);
//                    exemptionItem.setBillId(nextBillId);
//                    exemptionItem.setMerchantOrderId(Long.valueOf(cmd.getMerchantOrderId()));
//                    exemptionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                    exemptionItem.setCreatorUid(UserContext.currentUserId());
//                    exemptionItem.setId(currentExemItemSeq);
//                    currentExemItemSeq += 1;
//                    exemptionItem.setRemarks(exemptionItemDTO.getRemark());
//                    if(targetType!=null){
//                        exemptionItem.setTargetType(targetType);
//                    }
//                    if(targetId != null) {
//                        exemptionItem.setTargetId(targetId);
//                    }
//                    exemptionItem.setTargetname(targetName);
//                    exemptionItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//
//                    exemptionItems.add(exemptionItem);
//
//                    if(amount.compareTo(zero)==-1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte)0)){
//                        amount = amount.multiply(new BigDecimal("-1"));
//                        amountExemption = amountExemption.add(amount);
//                    }else if(amount.compareTo(zero)==1 || (exemptionItemDTO.getIsPlus() != null && exemptionItemDTO.getIsPlus().byteValue() == (byte)1)){
//                        amountSupplement = amountSupplement.add(amount);
//                    }
//                }
//                //应收否应该计算减免项
////                amountReceivable = amountReceivable.subtract(amountExemption);
////                amountReceivable = amountReceivable.add(amountSupplement);
//                amountOwed = amountOwed.subtract(amountExemption);
//                amountOwed = amountOwed.add(amountSupplement);
//                amountOwedWithoutTax = amountOwedWithoutTax.subtract(amountExemption);//待收（不含税）
//                amountOwedWithoutTax = amountOwedWithoutTax.add(amountSupplement);//待收（不含税）
//                EhPaymentExemptionItemsDao exemptionItemsDao = new EhPaymentExemptionItemsDao(context.configuration());
//                exemptionItemsDao.insert(exemptionItems);
//            }
//            Byte billStatus = 0;
//            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillItems> billItemsList = new ArrayList<>();
//            if(list1!=null){
//                for(int i = 0; i < list1.size() ; i++) {
//                    long currentBillItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                            com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
//                    if(currentBillItemSeq == 0){
//                        this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(
//                                com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
//                    }
//                    BillItemDTO dto = list1.get(i);
//                    PaymentBillItems item = new PaymentBillItems();
//                    item.setBillGroupRuleId(dto.getBillGroupRuleId());
//                    item.setAddressId(dto.getAddressId());
//                    item.setBuildingName(dto.getBuildingName());
//                    item.setApartmentName(dto.getApartmentName());
//                    BigDecimal var1 = dto.getAmountReceivable();
//                    //减免项不覆盖收费项目的收付，暂时
//                    var1 = DecimalUtils.negativeValueFilte(var1);
//                    item.setAmountOwed(var1);
//                    item.setAmountReceivable(var1);
//                    item.setAmountReceived(new BigDecimal("0"));
//                    item.setBillGroupId(billGroupId);
//                    item.setBillId(nextBillId);
//                    item.setChargingItemName(dto.getBillItemName());
//                    item.setChargingItemsId(dto.getChargingItemsId());
//                    item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                    item.setCreatorUid(UserContext.currentUserId());
//                    item.setDateStr(assetBillDateDTO.getDateStr());
//                    //时间假定
//                    item.setDateStrBegin(assetBillDateDTO.getDateStrBegin());
//                    item.setDateStrEnd(assetBillDateDTO.getDateStrEnd());
//                    item.setId(currentBillItemSeq);
//                    item.setNamespaceId(UserContext.getCurrentNamespaceId());
//                    item.setOwnerType(ownerType);
//                    item.setOwnerId(ownerId);
//                    item.setContractId(contractId);
//                    item.setContractNum(contractNum);
//                    // item 也添加categoryId， 这样费用清单简单些
//                    item.setCategoryId(categoryId);
//                    if(targetType!=null){
//                        item.setTargetType(targetType);
//                    }
//                    if(targetId != null) {
//                        item.setTargetId(targetId);
//                    }
//                    item.setTargetName(targetName);
//                    item.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                    item.setEnergyConsume(dto.getEnergyConsume());//增加用量
//                    BigDecimal var2 = dto.getAmountReceivableWithoutTax();
//                    var2 = DecimalUtils.negativeValueFilte(var2);
//                    item.setAmountOwedWithoutTax(var2);//增加待收（不含税）
//                    item.setAmountReceivableWithoutTax(var2);//增加应收（不含税）
//                    item.setAmountReceivedWithoutTax(new BigDecimal("0"));//增加已收（不含税）
//                    item.setTaxAmount(dto.getTaxAmount());//增加税额
//                    item.setTaxRate(dto.getTaxRate());//费项增加税率信息
//                    //物业缴费V6.6（对接统一账单） 账单要增加来源
//                    item.setSourceId(cmd.getSourceId());
//                    item.setSourceType(cmd.getSourceType());
//                    item.setSourceName(cmd.getSourceName());
//                    item.setConsumeUserId(cmd.getConsumeUserId());
//                    item.setConsumeUserName(cmd.getConsumeUserName());
//                    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
//                    item.setCanDelete(cmd.getCanDelete());
//                    item.setCanModify(cmd.getCanModify());
//                    //物业缴费V6.0 账单、费项表增加是否删除状态字段
//                    item.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
//                    //瑞安CM对接 账单、费项表增加是否是只读字段
//                    item.setIsReadonly((byte)0);//只读状态：0：非只读；1：只读
//                    //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识
//                    item.setMerchantOrderId(cmd.getMerchantOrderId());
//                    //物业缴费V7.1（企业记账流程打通）: 增加商品信息字段
//                    item.setGoodsServeType(dto.getGoodsServeType());
//                    item.setGoodsNamespace(dto.getGoodsNamespace());
//                    item.setGoodsTag1(dto.getGoodsTag1());
//                    item.setGoodsTag2(dto.getGoodsTag2());
//                    item.setGoodsTag3(dto.getGoodsTag3());
//                    item.setGoodsTag4(dto.getGoodsTag4());
//                    item.setGoodsTag5(dto.getGoodsTag5());
//                    item.setGoodsServeApplyName(dto.getGoodsServeApplyName());
//                    item.setGoodsTag(dto.getGoodsTag());
//                    item.setGoodsName(dto.getGoodsName());
//                    item.setGoodsDescription(dto.getGoodsDescription());
//                    item.setGoodsCounts(dto.getGoodsCounts());
//                    item.setGoodsPrice(dto.getGoodsPrice());
//                    item.setGoodsTotalprice(dto.getGoodsTotalPrice());
//                    billItemsList.add(item);
//
//                    amountReceivable = amountReceivable.add(var1);
//                    amountOwed = amountOwed.add(var1);
//                    amountReceivableWithoutTax = amountReceivableWithoutTax.add(var2);//增加应收（不含税）
//                    amountOwedWithoutTax = amountOwedWithoutTax.add(var2);//增加应收（不含税）
//                    if(dto.getTaxAmount() != null) {
//                        taxAmount = taxAmount.add(dto.getTaxAmount());//增加税额
//                    }
//                }
//            }
//
//            //服务账单不存在减免费项
//            //增加减免费项
////            if(list3 != null) {
////                List<com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems> subtractionItemsList = new ArrayList<>();
////                for(int i = 0; i < list3.size() ; i++) {
////                    long currentSubtractionItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
////                    if(currentSubtractionItemSeq == 0){
////                        this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentSubtractionItems.class));
////                    }
////                    SubItemDTO dto = list3.get(i);
////                    PaymentSubtractionItem subtractionItem = new PaymentSubtractionItem();
////                    subtractionItem.setId(currentSubtractionItemSeq);
////                    subtractionItem.setNamespaceId(UserContext.getCurrentNamespaceId());
////                    subtractionItem.setCategoryId(categoryId);
////                    subtractionItem.setOwnerId(ownerId);
////                    subtractionItem.setOwnerType(ownerType);
////                    subtractionItem.setBillId(nextBillId);
////                    subtractionItem.setBillGroupId(billGroupId);
////                    subtractionItem.setSubtractionType(dto.getSubtractionType());
////                    subtractionItem.setChargingItemId(dto.getChargingItemId());
////                    subtractionItem.setChargingItemName(dto.getChargingItemName());
////                    subtractionItem.setCreatorUid(UserContext.currentUserId());
////                    subtractionItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
////
////                    subtractionItemsList.add(subtractionItem);
////
////                    //根据减免费项配置重新计费，减免费项类型为eh_payment_bill_items才需要重新计费，减免费项类型为滞纳金新增无需重新计费
////                    if(subtractionItem != null && subtractionItem.getSubtractionType().equals(AssetSubtractionType.item.getCode())) {
////                        if(billItemsList != null){
////                            for(int j = 0; j < billItemsList.size() ; j++) {
////                                PaymentBillItems item = (PaymentBillItems) billItemsList.get(j);
////                                if(item.getChargingItemsId().equals(subtractionItem.getChargingItemId())) {
////                                    //如果收费项明细和减免费项的charginItemId相等，那么该费项金额应该从账单中减掉
////                                    amountOwed = amountOwed.subtract(item.getAmountReceivable());
////                                    amountOwedWithoutTax = amountOwedWithoutTax.subtract(item.getAmountReceivableWithoutTax());//增加待收（不含税）的计算
////                                }
////                            }
////                        }
////                    }
////                }
////                EhPaymentSubtractionItemsDao subtractionItemsDao = new EhPaymentSubtractionItemsDao(context.configuration());
////                subtractionItemsDao.insert(subtractionItemsList);
////            }
//
//            //服务账单不存在附件
//            //新增附件
////            List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList = cmd.getAssetPaymentBillAttachmentList();
////            if(assetPaymentBillAttachmentList != null) {
////                List<com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments> paymentBillAttachmentsList = new ArrayList<>();
////                for(int i = 0; i < assetPaymentBillAttachmentList.size() ; i++) {
////                    long currentSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
////                    if(currentSeq == 0){
////                        this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillAttachments.class));
////                    }
////                    AssetPaymentBillAttachment dto = assetPaymentBillAttachmentList.get(i);
////                    PaymentBillAttachments paymentBillAttachments = new PaymentBillAttachments();
////                    paymentBillAttachments.setId(currentSeq);
////                    paymentBillAttachments.setNamespaceId(UserContext.getCurrentNamespaceId());
////                    paymentBillAttachments.setCategoryId(categoryId);
////                    paymentBillAttachments.setOwnerId(ownerId);
////                    paymentBillAttachments.setOwnerType(ownerType);
////                    paymentBillAttachments.setBillId(nextBillId);
////                    paymentBillAttachments.setBillGroupId(billGroupId);
////                    paymentBillAttachments.setCreatorUid(UserContext.currentUserId());
////                    paymentBillAttachments.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
////                    paymentBillAttachments.setFilename(dto.getFilename());
////                    paymentBillAttachments.setContentUri(dto.getUri());
////                    paymentBillAttachments.setContentUrl(dto.getUrl());
////                    paymentBillAttachments.setContentType(dto.getContentType());
////                    paymentBillAttachmentsList.add(paymentBillAttachments);
////                }
////                EhPaymentBillAttachmentsDao paymentBillAttachmentsDao = new EhPaymentBillAttachmentsDao(context.configuration());
////                paymentBillAttachmentsDao.insert(paymentBillAttachmentsList);
////            }
//
//            //重新判断状态，如果待缴金额为0，则设置为已缴状态
////          if(amountOwed.compareTo(new BigDecimal("0"))!=1){
////               billStatus = 1;
////          }
//            for(int i = 0; i < billItemsList.size(); i++) {
//                billItemsList.get(i).setStatus(billStatus);
//            }
//            EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
//            billItemsDao.insert(billItemsList);
//
//            com.everhomes.server.schema.tables.pojos.EhPaymentBills newBill = new PaymentBills();
////            如果传入的billID是空则执行insert操作，否则执行update操作
//            if (billId==null){
//                //  缺少创造者信息，先保存在其他地方，比如持久化日志
//                amountOwed = DecimalUtils.negativeValueFilte(amountOwed);
//                newBill.setAmountOwed(amountOwed);
//                amountOwedWithoutTax = DecimalUtils.negativeValueFilte(amountOwedWithoutTax);//增加待收（不含税）
//                newBill.setAmountOwedWithoutTax(amountOwedWithoutTax);//增加待收（不含税）
////                            if(amountOwed.compareTo(zero) == 0) {
////                    newBill.setStatus((byte)1);
////                }else{
////                    newBill.setStatus(billStatus);
////                }
//                newBill.setStatus(billStatus);
//                amountReceivable = DecimalUtils.negativeValueFilte(amountReceivable);
//                amountReceivableWithoutTax = DecimalUtils.negativeValueFilte(amountReceivableWithoutTax);//增加应收（不含税）
//                newBill.setAmountReceivable(amountReceivable);
//                newBill.setAmountReceivableWithoutTax(amountReceivableWithoutTax);//增加应收（不含税）
//                newBill.setTaxAmount(taxAmount);//增加税额
//                newBill.setAmountReceived(zero);
//                newBill.setAmountReceivedWithoutTax(zero);//增加已收不含税
//                newBill.setAmountSupplement(amountSupplement);
//                newBill.setAmountExemption(amountExemption);
//                newBill.setBillGroupId(billGroupId);
//                //时间
//                newBill.setDateStr(assetBillDateDTO.getDateStr());
//                newBill.setDateStrBegin(assetBillDateDTO.getDateStrBegin());
//                newBill.setDateStrEnd(assetBillDateDTO.getDateStrEnd());
//                newBill.setDateStrDue(assetBillDateDTO.getDateStrDue());//出账单日
//                newBill.setDueDayDeadline(assetBillDateDTO.getDueDayDeadline());//最晚还款日
//                //新增时只填了一个楼栋门牌，所以也可以放到bill里去 by wentian 2018/4/24
//                newBill.setBuildingName(buildingName);
//                newBill.setApartmentName(apartmentName);
//
//                //添加客户的手机号，用来之后定位用户 by wentian.V.Brytania 2018/4/13
//                newBill.setCustomerTel(customerTel);
//
//                newBill.setInvoiceNumber(invoiceNum);
//
//                // added category Id
//                newBill.setCategoryId(categoryId);
//                newBill.setId(nextBillId);
//                newBill.setNamespaceId(UserContext.getCurrentNamespaceId());
//                if (!CollectionUtils.isEmpty(noticeTelList)) {
//                    newBill.setNoticetel(String.join(",", noticeTelList));
//                }
//                newBill.setOwnerId(ownerId);
//                newBill.setTargetName(targetName);
//                newBill.setOwnerType(ownerType);
//                newBill.setTargetType(targetType);
//                newBill.setTargetId(targetId);
//                newBill.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                newBill.setCreatorId(UserContext.currentUserId());
//                newBill.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                newBill.setNoticeTimes(0);
//
//                if(isOwed == null){
//                    newBill.setChargeStatus((byte)0);
//                }else{
//                    newBill.setChargeStatus(isOwed);
//                }
//                if(isSettled != null) {
//                    newBill.setSwitch(isSettled);
//                }else {
//                    //物业缴费V6.6（对接统一账单）：如果没有传账单是已出或未出，系统根据出账单日和当前日期做比较，判断是已出还是未出
//                    try {
//                        Date today = new Date();
//                        Date billDay = yyyyMMdd.parse(newBill.getDateStrDue());
//                        if(today.compareTo(billDay) >= 0) {
//                            newBill.setSwitch((byte)1);
//                        }else {
//                            newBill.setSwitch((byte)0);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                newBill.setContractId(contractId);
//                newBill.setContractNum(contractNum);
//                //物业缴费V6.6（对接统一账单） 账单要增加来源
//                newBill.setSourceId(cmd.getSourceId());
//                newBill.setSourceType(cmd.getSourceType());
//                newBill.setSourceName(cmd.getSourceName());
//                newBill.setConsumeUserId(cmd.getConsumeUserId());
//                newBill.setConsumeUserName(cmd.getConsumeUserName());
//                //账单表增加第三方账单唯一标识字段
//                newBill.setThirdBillId(cmd.getThirdBillId());
//                //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
//                newBill.setCanDelete(cmd.getCanDelete());
//                newBill.setCanModify(cmd.getCanModify());
//                //物业缴费V6.0 账单、费项表增加是否删除状态字段
//                newBill.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
//                //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识,明细合并setMerchantOrderId存储在billItems里面
////                newBill.setMerchantOrderId(cmd.getMerchantOrderId());
//
//                //瑞安CM对接 账单、费项表增加是否是只读字段
//                newBill.setIsReadonly((byte)0);//只读状态：0：非只读；1：只读
//                EhPaymentBillsDao billsDao = new EhPaymentBillsDao(context.configuration());
//                billsDao.insert(newBill);
//            }else{
////                否则更新账单
//                reCalBillById(billId);//重新计算账单
//                newBill = findPaymentBillById(billId);
//
//            }
//            response[0] = ConvertHelper.convert(newBill, ListBillsDTO.class);
//            response[0].setBillGroupName(billGroupDTO.getBillGroupName());
//            response[0].setBillId(String.valueOf(newBill.getId()));
//            response[0].setNoticeTelList(noticeTelList);
//            response[0].setBillStatus(newBill.getStatus());
//            response[0].setTargetType(targetType);
//            response[0].setTargetId(String.valueOf(targetId));
//            return null;
//        });
//        return response[0];
//    }

    public Long checkBillItemIsInBill(AssetBillDateDTO assetBillDateDTO,CreateBillCommand cmd) throws ParseException{
        Long billId=null;
//        出账单日解析
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        Date dateStrDue = yyyyMMdd.parse(assetBillDateDTO.getDateStrDue());

        PaymentBillsCommand PBCmd = new PaymentBillsCommand();
        PBCmd.setNamespaceId(cmd.getNamespaceId());
        PBCmd.setSourceId(cmd.getSourceId());
        PBCmd.setSourceType(cmd.getSourceType());
        PBCmd.setDateStr(yyyyMM.format(new Date()));
        PBCmd.setTargetId(cmd.getTargetId());
        PBCmd.setTargetType(cmd.getTargetType());
        List<PaymentBills> bills = findPaymentBills(PBCmd);

        if (bills!=null){
            Date createDateStd = new Date(0l);
            for (PaymentBills bill : bills){
                if (dateStrDue.compareTo(yyyyMMdd.parse(bill.getDateStrBegin()))>=0
                    && dateStrDue.compareTo(yyyyMMdd.parse(bill.getDateStrEnd()))<=0){
                    if (bill.getCreatTime().compareTo(createDateStd)>=0){
                        createDateStd = bill.getCreatTime();
                        billId = bill.getId();
                    }
                }
            }
        }

        return billId;
    }

    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    @Override
    public ListBillDetailResponse listBillDetail(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentExemptionItems t = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t");
        EhPaymentChargingItems k = Tables.EH_PAYMENT_CHARGING_ITEMS.as("k");
        EhPaymentLateFine fine = Tables.EH_PAYMENT_LATE_FINE.as("fine");
        EhAddresses t1 = Tables.EH_ADDRESSES.as("t1");
        com.everhomes.server.schema.tables.EhPaymentSubtractionItems t2 = Tables.EH_PAYMENT_SUBTRACTION_ITEMS.as("t2");//增加减免费项
        ListBillDetailResponse vo = new ListBillDetailResponse();
        BillGroupDTO dto = new BillGroupDTO();
        List<BillItemDTO> list1 = new ArrayList<>();
        List<ExemptionItemDTO> list2 = new ArrayList<>();
        List<SubItemDTO> subItemDTOList = new ArrayList<SubItemDTO>();//增加减免费项

        context.select(r.ID,r.TARGET_ID,r.NOTICETEL,r.CUSTOMER_TEL,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM
                , r.INVOICE_NUMBER, r.BUILDING_NAME, r.APARTMENT_NAME, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT, r.STATUS, r.CONTRACT_ID, r.CONTRACT_NUM
                , r.SOURCE_ID, r.SOURCE_TYPE, r.SOURCE_NAME, r.CONSUME_USER_ID, r.THIRD_BILL_ID, r.CAN_DELETE, r.CAN_MODIFY, r.PAYMENT_TYPE, r.MERCHANT_ORDER_ID)
                .from(r)
                .where(r.ID.eq(billId))
                .and(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetch()
                .map(f -> {
                    vo.setBillId(f.getValue(r.ID));
                    vo.setBillGroupId(f.getValue(r.BILL_GROUP_ID));
                    vo.setTargetId(f.getValue(r.TARGET_ID));
                    //vo.setNoticeTel(f.getValue(r.NOTICETEL));//催缴联系号码
                    if (f.getValue(r.NOTICETEL) != null) {
                    	vo.setNoticeTelList(Arrays.asList((f.getValue(r.NOTICETEL)).split(",")));
					}
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
                    //新增账单来源信息
                    vo.setSourceId(f.getValue(r.SOURCE_ID));
                    vo.setSourceType(f.getValue(r.SOURCE_TYPE));
                    vo.setSourceName(f.getValue(r.SOURCE_NAME));
                    vo.setConsumeUserId(f.getValue(r.CONSUME_USER_ID));
                    vo.setThirdBillId(f.getValue(r.THIRD_BILL_ID));
                    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                    vo.setCanDelete(f.getValue(r.CAN_DELETE));
                    vo.setCanModify(f.getValue(r.CAN_MODIFY));
                    //对接统一账单业务线的需求
                    vo.setPaymentType(f.getValue(r.PAYMENT_TYPE));
                    //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识
                    vo.setMerchantOrderId(f.getValue(r.MERCHANT_ORDER_ID));
                    return null;
                });
        context.select(o.CHARGING_ITEM_NAME,o.ID,o.AMOUNT_RECEIVABLE,t1.APARTMENT_NAME,t1.BUILDING_NAME, o.APARTMENT_NAME, o.BUILDING_NAME, o.CHARGING_ITEMS_ID
        		, o.ENERGY_CONSUME,o.AMOUNT_RECEIVABLE_WITHOUT_TAX,o.TAX_AMOUNT,o.TAX_RATE
        		, o.SOURCE_ID, o.SOURCE_TYPE, o.SOURCE_NAME, o.CONSUME_USER_ID, o.CAN_DELETE, o.CAN_MODIFY
        		, o.GOODS_SERVE_APPLY_NAME, o.GOODS_SERVE_TYPE, o.GOODS_NAMESPACE
        		, o.GOODS_TAG1, o.GOODS_TAG2, o.GOODS_TAG3, o.GOODS_TAG4, o.GOODS_TAG5
        		, o.GOODS_TAG, o.GOODS_NAME, o.GOODS_DESCRIPTION, o.GOODS_COUNTS
        		, o.GOODS_COUNTS, o.GOODS_PRICE, o.GOODS_TOTALPRICE, o.OWNER_ID)
                .from(o)
                .leftOuterJoin(k)
                .on(o.CHARGING_ITEMS_ID.eq(k.ID))
                .leftOuterJoin(t1)
                .on(o.ADDRESS_ID.eq(t1.ID))
                .where(o.BILL_ID.eq(billId))
                .and(o.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .orderBy(k.DEFAULT_ORDER)
                .fetch()
                .map(f -> {
                    BillItemDTO itemDTO = new BillItemDTO();
                    itemDTO.setBillId(billId);
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
                    itemDTO.setAmountReceivableWithoutTax(f.getValue(o.AMOUNT_RECEIVABLE_WITHOUT_TAX));//增加应收（不含税）
                    itemDTO.setTaxAmount(f.getValue(o.TAX_AMOUNT));//税额
                    itemDTO.setTaxRate(f.getValue(o.TAX_RATE));//税率
                    //新增账单来源信息
                    itemDTO.setSourceId(f.getValue(o.SOURCE_ID));
                    itemDTO.setSourceType(f.getValue(o.SOURCE_TYPE));
                    itemDTO.setSourceName(f.getValue(o.SOURCE_NAME));
                    itemDTO.setConsumeUserId(f.getValue(o.CONSUME_USER_ID));
                    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                    itemDTO.setCanDelete(f.getValue(o.CAN_DELETE));
                    itemDTO.setCanModify(f.getValue(o.CAN_MODIFY));
                    //物业缴费V7.1（企业记账流程打通）: 增加商品信息字段
                    itemDTO.setGoodsServeApplyName(f.getValue(o.GOODS_SERVE_APPLY_NAME));
                    //物业缴费V7.4 对接最新的统一订单和商户
                    itemDTO.setGoodsServeType(f.getValue(o.GOODS_SERVE_TYPE));
                    itemDTO.setGoodsNamespace(f.getValue(o.GOODS_NAMESPACE));
                    itemDTO.setGoodsTag1(f.getValue(o.GOODS_TAG1));
                    itemDTO.setGoodsTag2(f.getValue(o.GOODS_TAG2));
                    itemDTO.setGoodsTag3(f.getValue(o.GOODS_TAG3));
                    itemDTO.setGoodsTag4(f.getValue(o.GOODS_TAG4));
                    itemDTO.setGoodsTag5(f.getValue(o.GOODS_TAG5));
                    itemDTO.setGoodsTag(f.getValue(o.GOODS_TAG));
                    itemDTO.setGoodsName(f.getValue(o.GOODS_NAME));
                    itemDTO.setGoodsDescription(f.getValue(o.GOODS_DESCRIPTION));
                    itemDTO.setGoodsCounts(f.getValue(o.GOODS_COUNTS));
                    itemDTO.setGoodsPrice(f.getValue(o.GOODS_PRICE));
                    itemDTO.setGoodsTotalPrice(f.getValue(o.GOODS_TOTALPRICE));
                    //物业缴费V8.0（账单对接卡券） -44680
                    itemDTO.setOwnerId(f.getValue(o.OWNER_ID));
                    list1.add(itemDTO);
                    return null;
                });
        // 滞纳金
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
                nitem.setAmountReceivableWithoutTax(n.getAmount());
                nitem.setTaxAmount(BigDecimal.ZERO);
                nitem.setTaxRate(BigDecimal.ZERO);
                nitem.setItemFineType(AssetItemFineType.lateFine.getCode());//增加费项类型字段
                nitem.setItemType(AssetSubtractionType.lateFine.getCode());//费项类型
                //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
                //滞纳金是当成一个费项来展示的，这个会涉及滞纳金的后台计算，应该再区分滞纳金/费项，是滞纳金不允许编辑/删除。
                nitem.setCanDelete((byte)0);
                nitem.setCanModify((byte)0);
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
    
    @Override
    public boolean checkBillByCategory(Long billId, Long categoryId) {
        DSLContext context = getReadOnlyContext();
        Long fetch = context.select(Tables.EH_PAYMENT_BILLS.CATEGORY_ID)
                .from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
        query.addConditions(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                .and(t1.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetch(t1.ID);
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(DSL.sum(o.AMOUNT_RECEIVABLE),DSL.sum(o.AMOUNT_RECEIVED),DSL.sum(o.AMOUNT_OWED),t.NAME,t.ID);
//        query.addFrom(t,o);
        query.addFrom(t);
        query.addJoin(o);
        query.addConditions(o.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段

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

    public String getProjectChargingItemName(Integer namespaceId, Long ownerId, String ownerType, Long chargingItemId, Long categoryId) {
        List<String> names = getReadOnlyContext().select(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME)
                .from(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.CATEGORY_ID.eq(categoryId))
                .fetch(Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.PROJECT_LEVEL_NAME);
        if(names.size() >= 1){
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
        query.addConditions(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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

    public List<ListChargingStandardsDTO> listChargingStandards(String ownerType, Long ownerId, Long chargingItemId, Long categoryId, Long billGroupId) {
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
                .and(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))//issue-35467 【物业缴费6.3】选择账单组的计费周期为“合同月”，选择收费项后，计费周期要过滤掉非“合同月”的计费标准
                .fetch(Tables.EH_PAYMENT_BILL_GROUPS.BALANCE_DATE_TYPE);
        // limiteCycles
        if (limitCyclses.get(0)<11) {
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
        }else {
            //自定义自然周期和自定义合同周期放开限制
            context.select()
                    .from(standardScopeT,standardT)
                    .where(standardT.ID.eq(standardScopeT.CHARGING_STANDARD_ID))
                    .and(standardScopeT.OWNER_ID.eq(ownerId))
                    .and(standardScopeT.OWNER_TYPE.eq(ownerType))
                    .and(standardT.CHARGING_ITEMS_ID.eq(chargingItemId))
                    // add category id constraint
                    .and(standardScopeT.CATEGORY_ID.eq(categoryId))
//                        .and(standardT.BILLING_CYCLE.in(limitCyclses).or(standardT.BILLING_CYCLE.eq(BillingCycle.ONE_DEAL.getCode())))
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
        }
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
                if(varIden.equals("dj") || varIden.equals("gdje")){
                    var.setVariableName(varName+"含税(元)");
                    var.setVariablePlaceholder("请输入"+varName+"(含税)");
                }
                if(varIden.equals("dj")){
                    var.setVariableValue(suggestPrices.get(j)==null?null:new BigDecimal(suggestPrices.get(j)));
                }
                vars.add(var);
                if(varIden.equals("dj") || varIden.equals("gdje")){
                    PaymentVariable varRate = new PaymentVariable();
                    varRate.setVariableIdentifier("taxRate");
                    varRate.setVariableName("税率(%)");
                    vars.add(varRate);
                }
                if(varIden.equals("gdje")){
                    PaymentVariable var1 = new PaymentVariable();
                    var1.setVariableIdentifier("gdjebhs");
                    var1.setVariableName("固定金额不含税(元)");
                    var1.setVariablePlaceholder("请输入固定金额(不含税)");
                    vars.add(var1);
                }
                if(varIden.equals("dj")){
                    PaymentVariable var2 = new PaymentVariable();
                    var2.setVariableIdentifier("djbhs");
                    var2.setVariableName("单价不含税(元)");
                    var2.setVariablePlaceholder("请输入单价(不含税)");
                    vars.add(var2);
                }
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
    	List<String> noticeTelList = cmd.getNoticeTelList();
    	Long categoryId = cmd.getCategoryId();
    	String ownerType = cmd.getOwnerType();
        Long ownerId = cmd.getOwnerId();

        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            EhPaymentExemptionItems t2 = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t2");
            com.everhomes.server.schema.tables.EhPaymentSubtractionItems t3 = Tables.EH_PAYMENT_SUBTRACTION_ITEMS.as("t3");
            Long billGroupId = billGroupDTO.getBillGroupId();
            List<BillItemDTO> list1 = billGroupDTO.getBillItemDTOList();
            List<ExemptionItemDTO> list2 = billGroupDTO.getExemptionItemDTOList();
            List<SubItemDTO> subItemDTOList = billGroupDTO.getSubItemDTOList();//增加减免费项
            //1、更新收费项：更新费项之前先删除原来的（置为已删除状态）
            //2、只对常规费项做编辑操作，对滞纳金费项不做处理（前端已做过滤）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
            	.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())//物业缴费V6.0 账单、费项表增加是否删除状态字段
	            .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
	            .execute();
            //根据账单组设置生成账单的账期、账单开始时间、账单结束时间、出账单日、最晚还款日
            AssetBillDateDTO assetBillDateDTO = generateBillDate(billGroupId, cmd.getDateStrBegin(), cmd.getDateStrEnd());
            if(list1!=null){
                for(int i = 0; i < list1.size() ; i++) {
                    BillItemDTO dto = list1.get(i);
                    Long billItemId = dto.getBillItemId();
                    //如果费项ID不为空，那么是更新操作
                	BigDecimal var1 = dto.getAmountReceivable();
                    //减免项不覆盖收费项目的收付，暂时
                    var1 = DecimalUtils.negativeValueFilte(var1);
                    BigDecimal var2 = dto.getAmountReceivableWithoutTax();
                    var2 = DecimalUtils.negativeValueFilte(var2);
                    //如果费项ID是空，那么是新增
                    if(billItemId == null) {
                    	long currentBillItemSeq = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                        if(currentBillItemSeq == 0){
                            this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));
                        }
                        PaymentBillItems item = new PaymentBillItems();
                        item.setBillGroupRuleId(dto.getBillGroupRuleId());
                        item.setAddressId(dto.getAddressId());
                        item.setBuildingName(dto.getBuildingName());
                        item.setApartmentName(dto.getApartmentName());
                        item.setAmountOwed(var1);
                        item.setAmountReceivable(var1);
                        item.setAmountReceived(new BigDecimal("0"));
                        item.setBillGroupId(billGroupId);
                        item.setBillId(billId);
                        item.setChargingItemName(dto.getBillItemName());
                        item.setChargingItemsId(dto.getChargingItemsId());
                        item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        item.setCreatorUid(UserContext.currentUserId());
                        item.setDateStr(assetBillDateDTO.getDateStr());
                        //时间假定
                        item.setDateStrBegin(assetBillDateDTO.getDateStrBegin());
                        item.setDateStrEnd(assetBillDateDTO.getDateStrEnd());
                        item.setId(currentBillItemSeq);
                        item.setNamespaceId(UserContext.getCurrentNamespaceId());
                        item.setOwnerType(ownerType);
                        item.setOwnerId(ownerId);
                        item.setContractId(cmd.getContractId());
                        item.setContractNum(cmd.getContractNum());
                        //item 也添加categoryId， 这样费用清单简单些
                        item.setCategoryId(categoryId);
                        if(targetType!=null){
                            item.setTargetType(targetType);
                        }
                        if(targetId != null) {
                            item.setTargetId(targetId);
                        }
                        item.setTargetName(targetName);
                        item.setEnergyConsume(dto.getEnergyConsume());//增加用量
                        item.setAmountOwedWithoutTax(var2);//增加待收（不含税）
                        item.setAmountReceivableWithoutTax(var2);//增加应收（不含税）
                        item.setAmountReceivedWithoutTax(new BigDecimal("0"));//增加已收（不含税）
                        item.setTaxAmount(dto.getTaxAmount());//增加税额
                        item.setTaxRate(dto.getTaxRate());//费项增加税率信息
                        //物业缴费V6.6（对接统一账单） 账单要增加来源
                        item.setSourceType(AssetSourceTypeEnum.ASSET_MODULE.getSourceType());
                        item.setSourceId(AssetPaymentBillSourceId.CREATE.getCode());
                    	LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_CREATE_CODE, "zh_CN");
                    	item.setSourceName(localeString.getText());
                    	//物业缴费V6.0 ：手动新增的未出账单及已出未缴账单需支持修改和删除（修改和删除分为：修改和删除整体）
                    	item.setCanDelete((byte)1);
                    	item.setCanModify((byte)1);
                        //物业缴费V6.0 账单、费项表增加是否删除状态字段
                        item.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());

                    	EhPaymentBillItemsDao billItemsDao = new EhPaymentBillItemsDao(context.configuration());
                    	billItemsDao.insert(item);
                    }else {
                    	context.update(Tables.EH_PAYMENT_BILL_ITEMS)
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.VALID.getCode())//物业缴费V6.0 账单、费项表增加是否删除状态字段
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID, dto.getChargingItemsId())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEM_NAME, dto.getBillItemName())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE, var1)
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED, var1)
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED, new BigDecimal("0"))
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE_WITHOUT_TAX, var2)
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED_WITHOUT_TAX, var2)
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED_WITHOUT_TAX, new BigDecimal("0"))
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.TAX_AMOUNT, dto.getTaxAmount())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.BUILDING_NAME, dto.getBuildingName())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.APARTMENT_NAME, dto.getApartmentName())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.ADDRESS_ID, dto.getAddressId())
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
	                    	.set(Tables.EH_PAYMENT_BILL_ITEMS.OPERATOR_UID, UserContext.currentUserId())
	        	            .where(Tables.EH_PAYMENT_BILL_ITEMS.ID.eq(billItemId))
	        	            .execute();
                    }
                }
            }
            List<com.everhomes.server.schema.tables.pojos.EhPaymentExemptionItems> exemptionItems = new ArrayList<>();
            //减免项列表list2
            List<Long> includeExemptionIds = new ArrayList<Long>();
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
            String noticeTelListStr = "";
            if (noticeTelList != null) {
            	noticeTelListStr = String.join(",", noticeTelList);
            }
            context.update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.INVOICE_NUMBER, invoiceNum)
                    //.set(Tables.EH_PAYMENT_BILLS.NOTICETEL, noticeTel)
            		.set(Tables.EH_PAYMENT_BILLS.NOTICETEL, noticeTelListStr)
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
    public void deleteBill(Long billId,String merchantOrderId) {
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //删除账单（置状态）
            context.update(Tables.EH_PAYMENT_BILLS)
            		.set(Tables.EH_PAYMENT_BILLS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
                    .set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                    .execute();
            //删除优惠项
            deletExemptionItem(context,billId,merchantOrderId);
            return null;
        });
    }

    @Override
    public void deleteBill(Long billId){
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //删除账单（置状态）
            context.update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILLS.ID.eq(billId))
                    .execute();
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
                    .set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                    .execute();
            return null;
        });
    }

    @Override
    public void deleteBillItems(Long billId,String merchantOrderId) {
        this.dbProvider.execute((TransactionStatus status) -> {
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
            //删除费项（置状态）
            context.update(Tables.EH_PAYMENT_BILL_ITEMS)
                    .set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
                    .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                    .and(Tables.EH_PAYMENT_BILL_ITEMS.MERCHANT_ORDER_ID.eq(merchantOrderId))
                    .execute();
            //删除优惠项
            deletExemptionItem(context,billId,merchantOrderId);
            return null;
        });
    }

    public void deletExemptionItem(DSLContext context,Long billID,String merchantOrderId) {
        context.delete(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.BILL_ID.eq(billID))
                .and(Tables.EH_PAYMENT_EXEMPTION_ITEMS.MERCHANT_ORDER_ID.eq(Long.valueOf(merchantOrderId)))
                .execute();
    }

    @Override
    public void deleteBillItem(Long billItemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //删除费项（置状态）
        context.update(Tables.EH_PAYMENT_BILL_ITEMS)
    		.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
    		.where(Tables.EH_PAYMENT_BILL_ITEMS.ID.eq(billItemId))
            .execute();
    }
    
    public void deleteBillItemByBillId(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //删除费项（置状态）
        context.update(Tables.EH_PAYMENT_BILL_ITEMS)
    		.set(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG, AssetPaymentBillDeleteFlag.DELETE.getCode())
    		.where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
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

    @SuppressWarnings("rawtypes")
    public List<VariableIdAndValue> findPreInjectedVariablesForCal(Long chargingStandardId,Long ownerId,String ownerType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<VariableIdAndValue> list = new ArrayList<>();
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        String variableJson = context.select(t.VARIABLES_JSON_STRING)
                .from(t)
                .where(t.CHARGING_STANDARDS_ID.eq(chargingStandardId))
                .and(t.OWNERID.eq(ownerId))
                .and(t.OWNERTYPE.eq(ownerType))
                .fetchOne(0, String.class);
        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
		Map<String,String> map = gson.fromJson(variableJson, Map.class);
        for(Map.Entry entry : map.entrySet()){
            VariableIdAndValue vid = new VariableIdAndValue();
            vid.setVariableValue(entry.getValue());
            vid.setVariableId(entry.getKey());
            list.add(vid);
        }
        return list;
    }

//    private void getVaraibleIdenInHashset(String formulaCopy, Set<String> variableIdentifiers, int index) {
//        for(int i = 0; i < formulaCopy.length(); i ++){
//            char c = formulaCopy.charAt(i);
//            if(c == '*' || c == '/' || c == '+' || c == '-' || i == formulaCopy.length()-1){
//                String iden = formulaCopy.substring(index, i == formulaCopy.length() - 1 ? i + 1 : i);
//                if(hasDigit(iden)){
//                    variableIdentifiers.add(iden);
//                }
//                index = i + 1;
//            }
//        }
//    }

    @Override
    public void increaseNoticeTime(List<Long> billIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.NOTICE_TIMES,Tables.EH_PAYMENT_BILLS.NOTICE_TIMES.add(1))
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
    public List<PaymentBillGroupRule> getBillGroupRule(Long chargingItemId, Long chargingStandardId, String ownerType, Long ownerId, Long billGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        List<PaymentBillGroupRule> rules = context.select()
                .from(t)
                .where(t.CHARGING_ITEM_ID.eq(chargingItemId))
                .and(t.OWNERTYPE.eq(ownerType))
                .and(t.OWNERID.eq(ownerId))
                .and(t.BILL_GROUP_ID.eq(billGroupId))//物业缴费V6.3 签合同选择计价条款前，先选择账单组
                .fetch()
                .map(r -> ConvertHelper.convert(r, PaymentBillGroupRule.class));
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
                    .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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

    /**
     * 合同更新/根据合同id,自动刷新合同账单
     * @param contractId
     */
    public void deleteContractPaymentByContractId(Long contractId) {
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentContractReceiver t1 = Tables.EH_PAYMENT_CONTRACT_RECEIVER.as("t1");
        EhPaymentBillItems t2 = Tables.EH_PAYMENT_BILL_ITEMS.as("t2");
        this.coordinationProvider.getNamedLock(contractId.toString()).enter(() -> {
            this.dbProvider.execute((TransactionStatus status) -> {
                DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
                List<Long> billIds = context.select(t.ID)
                        .from(t)
                        .where(t.CONTRACT_ID.eq(contractId))
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
        EhPaymentBillGroups billGroup = Tables.EH_PAYMENT_BILL_GROUPS.as("billGroup");
        Set<PaymentExpectancyDTO> set = new LinkedHashSet<>();
        
        //修复缺陷 #42398 【中天】【合同管理】中天大厦租赁合同，编号为20171024，产生费用清单数据有问题
//        List<Long> fetch = context.select(bill.ID)
//                .from(bill)
//                .where(bill.CONTRACT_NUM.eq(contractNum))
//                .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
//                .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
//                .fetch(bill.ID);
//        context.select(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.DATE_STR_BEGIN,t.DATE_STR_END,t.DATE_STR_DUE,t.AMOUNT_RECEIVABLE,t1.NAME,t1.ID,
//        		t.AMOUNT_RECEIVABLE_WITHOUT_TAX,t.TAX_AMOUNT,billGroup.NAME,t.DELETE_FLAG)
//                .from(t,t1,billGroup)
//                .where(t.BILL_ID.in(fetch))
//                .and(t.BILL_GROUP_ID.eq(billGroup.ID))
//                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
//                .orderBy(t1.NAME,t.DATE_STR)
//                .limit(pageOffset,pageSize+1)
//                .fetch()
//                .map(r -> {
//                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
//                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
//                    dto.setPropertyIdentifier(r.getValue(t.BUILDING_NAME)+r.getValue(t.APARTMENT_NAME));
//                    dto.setDueDateStr(r.getValue(t.DATE_STR_DUE));
//                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
//                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
//                    dto.setChargingItemName(r.getValue(t1.NAME));
//                    dto.setBillItemId(r.getValue(t.ID));
//                    dto.setChargingItemId(r.getValue(t1.ID));
//                    dto.setBillGroupName(r.getValue(billGroup.NAME));//物业缴费V6.3合同概览需要增加账单组名称字段
//                    //物业缴费V6.0 账单、费项表增加是否删除状态字段
//                    dto.setDeleteFlag(r.getValue(t.DELETE_FLAG));
//                    //缺陷 #42852 【中天】【缴费管理】联科园区6栋5-6F合同录入信息无误，但是展示账单数据金额却有小数
//                    BigDecimal taxAmount = r.getValue(t.TAX_AMOUNT);//税额
//                    BigDecimal amountReceivable = r.getValue(t.AMOUNT_RECEIVABLE);//应收金额含税
//                    BigDecimal amountReceivableWithoutTax = r.getValue(t.AMOUNT_RECEIVABLE_WITHOUT_TAX);//应收不含税
//                    dto.setTaxAmount(taxAmount != null ? taxAmount.stripTrailingZeros() : taxAmount);
//                    dto.setAmountReceivable(amountReceivable != null ? amountReceivable.stripTrailingZeros() : amountReceivable);
//                    dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax.stripTrailingZeros() : amountReceivableWithoutTax);
//                    set.add(dto);
//                    return null;
//                });

        List<Long> fetch1 = context.select(bill.ID)
                .from(bill)
                .where(bill.CONTRACT_ID.eq(contractId))
                .and(bill.NAMESPACE_ID.eq(namespaceId))//解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                .fetch(bill.ID);
        context.select(t.ID,t.BUILDING_NAME,t.APARTMENT_NAME,t.DATE_STR_BEGIN,t.DATE_STR_END,t.DATE_STR_DUE,t.AMOUNT_RECEIVABLE,t1.NAME,t1.ID,
        		t.AMOUNT_RECEIVABLE_WITHOUT_TAX,t.TAX_AMOUNT,billGroup.NAME)
                .from(t,t1,billGroup)
                .where(t.BILL_ID.in(fetch1))
                .and(t.BILL_GROUP_ID.eq(billGroup.ID))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .orderBy(t1.NAME,t.DATE_STR)
                .limit(pageOffset,pageSize+1)
                .fetch()
                .map(r -> {
                    PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    //缺陷 #42424 【智谷汇】保证金设置为固定金额，但是实际会以合同签约门牌的数量计价。实际上保证金是按照合同收费，不是按照门牌的数量进行重复计费
                    String buildingName = r.getValue(t.BUILDING_NAME) != null ? r.getValue(t.BUILDING_NAME) : "";
                    String apartmentName = r.getValue(t.APARTMENT_NAME) != null ? r.getValue(t.APARTMENT_NAME) : "";
                    dto.setPropertyIdentifier(buildingName + apartmentName);
                    dto.setDueDateStr(r.getValue(t.DATE_STR_DUE));
                    dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
                    dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
                    dto.setChargingItemName(r.getValue(t1.NAME));
                    dto.setBillItemId(r.getValue(t.ID));
                    dto.setChargingItemId(r.getValue(t1.ID));
                    dto.setBillGroupName(r.getValue(billGroup.NAME));//物业缴费V6.3合同概览需要增加账单组名称字段
                    //缺陷 #42852 【中天】【缴费管理】联科园区6栋5-6F合同录入信息无误，但是展示账单数据金额却有小数
                    BigDecimal taxAmount = r.getValue(t.TAX_AMOUNT);//税额
                    BigDecimal amountReceivable = r.getValue(t.AMOUNT_RECEIVABLE);//应收金额含税
                    BigDecimal amountReceivableWithoutTax = r.getValue(t.AMOUNT_RECEIVABLE_WITHOUT_TAX);//应收不含税
                    dto.setTaxAmount(taxAmount != null ? taxAmount.stripTrailingZeros() : taxAmount);
                    dto.setAmountReceivable(amountReceivable != null ? amountReceivable.stripTrailingZeros() : amountReceivable);
                    dto.setAmountReceivableWithoutTax(amountReceivableWithoutTax != null ? amountReceivableWithoutTax.stripTrailingZeros() : amountReceivableWithoutTax);
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
        //EhPaymentBillGroupsRules t1 = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t1");
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
                //.where(t.DATE_STR.lessThan(billDateStr))
                //修复issue-36575 【新微创源】企业账单：已出账单依旧在未出账单中
                .where(t.DATE_STR_DUE.lessOrEqual(billDateStr))//DATE_STR_DUE：应收日期（出账单日）
                .and(t.SWITCH.eq((byte)0))
                //瑞安CM对接：只读的账单是来源于第三方，不允许从未出转为已出
                .and(t.IS_READONLY.eq((byte)0))//只读状态：0：非只读；1：只读
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
//        HashSet<Long> idSet = new HashSet<>();
//        Long id = null;
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

    public PaymentBills findPaymentBill(Integer namespaceId, String sourceType, Long sourceId, String merchantOrderId) {
    	SelectQuery<EhPaymentBillsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS).getQuery();
		if(!org.springframework.util.StringUtils.isEmpty(namespaceId)){
			query.addConditions(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId));
		}
		if(!org.springframework.util.StringUtils.isEmpty(sourceType)){
			query.addConditions(Tables.EH_PAYMENT_BILLS.SOURCE_TYPE.eq(sourceType));
		}
		if(!org.springframework.util.StringUtils.isEmpty(sourceId)){
			query.addConditions(Tables.EH_PAYMENT_BILLS.SOURCE_ID.eq(sourceId));
		}
		if(!org.springframework.util.StringUtils.isEmpty(merchantOrderId)){
			query.addConditions(Tables.EH_PAYMENT_BILLS.MERCHANT_ORDER_ID.eq(merchantOrderId));
		}
		query.addConditions(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
		List<PaymentBills> list = query.fetchInto(PaymentBills.class);
        if(list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<PaymentBillItems> findPaymentBillItems(Integer namespaceId, String sourceType, Long sourceId, String merchantOrderId){
        SelectQuery<EhPaymentBillItemsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILL_ITEMS).getQuery();
        if(!org.springframework.util.StringUtils.isEmpty(namespaceId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.NAMESPACE_ID.eq(namespaceId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(sourceType)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.SOURCE_TYPE.eq(sourceType));
        }
        if(!org.springframework.util.StringUtils.isEmpty(sourceId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.SOURCE_ID.eq(sourceId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(merchantOrderId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.MERCHANT_ORDER_ID.eq(merchantOrderId));
        }
        query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
        List<PaymentBillItems> list = query.fetchInto(PaymentBillItems.class);
        if(list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    @Override
    public List<PaymentBillItems> findPaymentBillItems(Integer namespaceId,Long ownerId,String ownerType,Long billId){
        SelectQuery<EhPaymentBillItemsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILL_ITEMS).getQuery();
        if(!org.springframework.util.StringUtils.isEmpty(namespaceId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.NAMESPACE_ID.eq(namespaceId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(ownerId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_ID.eq(ownerId));
        }
        if(!org.springframework.util.StringUtils.isEmpty(ownerType)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.OWNER_TYPE.eq(ownerType));
        }
        if(!org.springframework.util.StringUtils.isEmpty(billId)){
            query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId));
        }
        query.addConditions(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
        List<PaymentBillItems> list = query.fetchInto(PaymentBillItems.class);
        if(list.size()>0){
            return list;
        }else{
            return null;
        }
    }
    @Override
    public List<PaymentBills> findPaymentBills(PaymentBillsCommand PBCmd) {
        SelectQuery<EhPaymentBillsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS).getQuery();
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getNamespaceId())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(PBCmd.getNamespaceId()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getSourceType())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.SOURCE_TYPE.eq(PBCmd.getSourceType()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getNamespaceId())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.SOURCE_ID.eq(PBCmd.getSourceId()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getDateStr())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.DATE_STR.eq(PBCmd.getDateStr()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getTargetId())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.TARGET_ID.eq(PBCmd.getTargetId()));
        }
        if(!org.springframework.util.StringUtils.isEmpty(PBCmd.getTargetType())){
            query.addConditions(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(PBCmd.getTargetType()));
        }

        query.addConditions(Tables.EH_PAYMENT_BILLS.SWITCH.eq(BillSwitch.UNSETTLED.getCode()));

        query.addConditions(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));

        query.addConditions(Tables.EH_PAYMENT_BILLS.STATUS.eq(BillStatus.UNFINISHED.getCode()));

        List<PaymentBills> list = query.fetchInto(PaymentBills.class);
        if(list.size()>0){
            return list;
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
        query.addConditions(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        return query.fetch(Tables.EH_PAYMENT_BILLS.ID);
    }

    @Override
    public void createBillOrderMaps(List<PaymentBillOrder> billOrderList) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
        long nextBlockSequence = this.sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPaymentBillOrders.class), billOrderList.size());

        List<EhPaymentBillOrders> orderBills = new ArrayList<>();
        for(PaymentBillOrder billdOrder : billOrderList){
            EhPaymentBillOrders billOrderInDb  = ConvertHelper.convert(billdOrder, EhPaymentBillOrders.class);
            billOrderInDb.setId(nextBlockSequence++);
            orderBills.add(billOrderInDb);
        }

        EhPaymentBillOrdersDao dao = new EhPaymentBillOrdersDao(dslContext.configuration());
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
            //物业缴费V8.0（账单对接卡券） -44680：增加费项ID
            dto.setChargingItemId(rule.getChargingItemId());
            list.add(dto);
        }
        return list;
    }

    public PaymentBillGroup getBillGroup(Integer namespaceId, Long ownerId, String ownerType, Long categoryId, Long brotherGroupId, Byte isDefault) {
    	SelectQuery<EhPaymentBillGroupsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILL_GROUPS).getQuery();
		query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.NAMESPACE_ID.eq(namespaceId));
		if(ownerId != null){
			query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.eq(ownerId));
		}
		if(ownerType != null){
			query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_TYPE.eq(ownerType));
		}
		if(categoryId != null){
			query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(categoryId));
		}
		if(brotherGroupId != null){
			query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID.eq(brotherGroupId));
		}
		if(isDefault != null) {
			query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.IS_DEFAULT.eq(isDefault));
		}
		List<PaymentBillGroup> records = query.fetchInto(PaymentBillGroup.class);
		return records.get(0);
    }

    @Override
    public ListChargingItemDetailForBillGroupDTO listChargingItemDetailForBillGroup(Long billGroupRuleId) {
        DSLContext context = getReadOnlyContext();
        ListChargingItemDetailForBillGroupDTO dto = new ListChargingItemDetailForBillGroupDTO();
        EhPaymentBillGroupsRules t1 = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t1");
        PaymentBillGroupRule rule = context.selectFrom(t1)
                .where(t1.ID.eq(billGroupRuleId))
                .fetchOneInto(PaymentBillGroupRule.class);
        dto.setBillGroupRuleId(rule.getId());
        dto.setChargingItemId(rule.getChargingItemId());
        dto.setDayOffset(rule.getBillItemDayOffset());
        dto.setMonthOffset(rule.getBillItemMonthOffset());
         if(rule.getBillItemMonthOffset() == null){
                dto.setBillItemGenerationType((byte)3);
            }else if(rule.getBillItemDayOffset() == null){
                dto.setBillItemGenerationType((byte)1);
            }else{
                dto.setBillItemGenerationType((byte)2);
            }
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
                //issue-36209 修复“【物业缴费6.3】在物业缴费建3个租金组，分别关联收费项之后，新增计价条款页面，收费项重复显示了3次”的bug
                .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(cmd.getBillGroupId()))
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
    public boolean checkCoupledChargingStandard(Long cid, Long categoryId) {
        DSLContext context = getReadWriteContext();
        List<Long> bros = context.select(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.ID)
                .from(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.OWNER_ID.eq(cid))
                .and(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.CATEGORY_ID.eq(categoryId))
                .fetch(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.ID);
        if(bros.size() < 1){
            return true;
        }
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
    public void autoNoticeConfig(Integer namespaceId, String ownerType, Long ownerId, Long categoryId, List<com.everhomes.server.schema.tables.pojos.EhPaymentNoticeConfig> toSaveConfigs) {
        DSLContext writeContext = getReadWriteContext();
        EhPaymentNoticeConfig noticeConfig = Tables.EH_PAYMENT_NOTICE_CONFIG.as("noticeConfig");
        EhPaymentNoticeConfigDao noticeConfigDao = new EhPaymentNoticeConfigDao(writeContext.configuration());
        this.dbProvider.execute((TransactionStatus status) -> {
            writeContext.delete(noticeConfig)
                    .where(noticeConfig.NAMESPACE_ID.eq(namespaceId))
                    .and(noticeConfig.OWNER_TYPE.eq(ownerType))
                    .and(noticeConfig.OWNER_ID.eq(ownerId))
                    .and(noticeConfig.CATEGORY_ID.eq(categoryId))//修复issue-35688 “【物业缴费】自动催缴设置，没有做多入口”的bug
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
        query.addConditions(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
        query.addConditions(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetchInto(PaymentBills.class);
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
        
      //修复缺陷 #42398 【中天】【合同管理】中天大厦租赁合同，编号为20171024，产生费用清单数据有问题
//        List<Long> fetch = new ArrayList<Long>();
//        if(categoryId != null) {
//        	fetch = context.select(bill.ID)
//                    .from(bill)
//                    .where(bill.CONTRACT_NUM.eq(contractNum))
//                    .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
//                    .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
//                    .and(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
//                    .fetch(bill.ID);
//        }else {
//        	fetch = context.select(bill.ID)
//                    .from(bill)
//                    .where(bill.CONTRACT_NUM.eq(contractNum))
//                    .and(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
//                    .fetch(bill.ID);
//        }
//        context.select(t.ID,t.AMOUNT_RECEIVABLE)
//	        .from(t,t1)
//	        .where(t.BILL_ID.in(fetch))
//	        .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
//	        .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
//	        .orderBy(t1.NAME,t.DATE_STR)
//	        .fetch()
//	        .map(r -> {
//	            PaymentExpectancyDTO dto = new PaymentExpectancyDTO();
//	            dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
//	            dto.setBillItemId(r.getValue(t.ID));
//	            set.add(dto);
//	            return null;
//        });

        List<Long> fetch1 = new ArrayList<Long>();
        if(categoryId != null) {
        	fetch1 = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_ID.eq(contractId))
                    .and(bill.NAMESPACE_ID.eq(namespaceId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .and(bill.CATEGORY_ID.eq(categoryId)) //解决issue-34161 签约一个正常合同，执行“/energy/calculateTaskFeeByTaskId”，会生成3条费用清单   by 杨崇鑫
                    .and(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                    .fetch(bill.ID);
        }else {
        	fetch1 = context.select(bill.ID)
                    .from(bill)
                    .where(bill.CONTRACT_ID.eq(contractId))
                    .and(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                    .fetch(bill.ID);
        }
        context.select(t.ID,t.AMOUNT_RECEIVABLE)
                .from(t,t1)
                .where(t.BILL_ID.in(fetch1))
                .and(t.CHARGING_ITEMS_ID.eq(t1.ID))
                .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
        query.addSelect(bill.SOURCE_TYPE);
        query.addSelect(bill.CONFIRM_FLAG);
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
        query.addConditions(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                    //物业缴费V7.4:若用户在APP一次性全部支付，此时在APP端显示的支付状态是“已支付，待确认”。当财务看到了支付结果，在CM中确认收入以后，CM的账单状态变成“已支付”。下一次同步数据时，APP同步显示为 “已确认”。
                    //账单来源是来源于CM，以及是服务费账单（资源预订、云打印）类型的，那么需要瑞安财务确认支付结果
                    String sourceType = r.getValue(bill.SOURCE_TYPE);
                    if(AssetModuleNotifyConstants.ASSET_CM_MODULE.equals(sourceType) 
                    		|| AssetSourceType.RENTAL_MODULE.equals(sourceType)
                    		|| AssetSourceType.PRINT_MODULE.equals(sourceType)) {
                    	dto.setConfirmFlag(r.getValue(bill.CONFIRM_FLAG) != null ? r.getValue(bill.CONFIRM_FLAG) : (byte)0);
                    }
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetchInto(PaymentBills.class);
    }

    @Override
    public List<PaymentBills> findPaidBillsByIds(List<String> billIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .and(Tables.EH_PAYMENT_BILLS.STATUS.eq((byte)1))
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetchInto(PaymentBills.class);
    }

    @Override
    public void reCalBillById(long billId) {
//        重新计算账单
        final BigDecimal[] amountReceivable = {new BigDecimal("0")};
        final BigDecimal[] amountReceivableWithoutTax = {new BigDecimal("0")};
        final BigDecimal[] amountReceived = {new BigDecimal("0")};
        final BigDecimal[] amountReceivedWithoutTax = {new BigDecimal("0")};
        final BigDecimal[] amountOwed = {new BigDecimal("0")};
        final BigDecimal[] amountOwedWithoutTax = {new BigDecimal("0")};
        final BigDecimal[] amountExempled = {new BigDecimal("0")};
        final BigDecimal[] amountSupplement = {new BigDecimal("0")};
        final BigDecimal[] taxAmount = {new BigDecimal("0")};
        BigDecimal zero = new BigDecimal("0");
        getReadOnlyContext().select(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE,Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED,
        		Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED,Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID,
        		Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE_WITHOUT_TAX,Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED_WITHOUT_TAX,
        		Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED_WITHOUT_TAX,Tables.EH_PAYMENT_BILL_ITEMS.TAX_AMOUNT)
                .from(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetch()
                .forEach(r -> {
                    amountReceivable[0] = amountReceivable[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE));//应收
                    if(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE_WITHOUT_TAX) != null) {
                    	amountReceivableWithoutTax[0] = amountReceivableWithoutTax[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVABLE_WITHOUT_TAX));//应收不含税
                    }
                    amountReceived[0] = amountReceived[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED));//已收
                    if(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED_WITHOUT_TAX) != null) {
                    	amountReceivedWithoutTax[0] = amountReceivedWithoutTax[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_RECEIVED_WITHOUT_TAX));//已收不含税
                    }
                    if(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.TAX_AMOUNT) != null) {
                    	taxAmount[0] = taxAmount[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.TAX_AMOUNT));//税额
                    }
                    //根据减免费项配置重新计算待收金额
                    Long charingItemId = r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.CHARGING_ITEMS_ID);
                    Boolean isConfigSubtraction = isConfigItemSubtraction(billId, charingItemId);//用于判断该费项是否配置了减免费项
                    if(!isConfigSubtraction) {//如果费项没有配置减免费项，那么需要相加到待缴金额中
                    	amountOwed[0] = amountOwed[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED));
                    	if(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED_WITHOUT_TAX) != null) {
                    		amountOwedWithoutTax[0] = amountOwedWithoutTax[0].add(r.getValue(Tables.EH_PAYMENT_BILL_ITEMS.AMOUNT_OWED_WITHOUT_TAX));
                    	}
                    }
                });
        getReadOnlyContext().select(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT)
                .from(Tables.EH_PAYMENT_EXEMPTION_ITEMS)
                .where(Tables.EH_PAYMENT_EXEMPTION_ITEMS.BILL_ID.in(billId))
                .fetch()
                .forEach(r ->{
//                    amountReceivable[0] = amountReceivable[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    amountOwed[0] = amountOwed[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
                    amountOwedWithoutTax[0] = amountOwedWithoutTax[0].add(r.getValue(Tables.EH_PAYMENT_EXEMPTION_ITEMS.AMOUNT));
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
                        	amountOwedWithoutTax[0] = amountOwedWithoutTax[0].add(value);
                        }
                    }
                });
        //修复issue-32525 "导入一个账单，减免大于收费项，列表显示了负数"的bug
        amountOwed[0] = DecimalUtils.negativeValueFilte(amountOwed[0]);
        amountOwedWithoutTax[0] = DecimalUtils.negativeValueFilte(amountOwedWithoutTax[0]);
        //更改金额，但不更改状态
        getReadWriteContext().update(Tables.EH_PAYMENT_BILLS)
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE, amountReceivable[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED, amountOwed[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_SUPPLEMENT, amountSupplement[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_EXEMPTION, amountExempled[0])
                    .set(Tables.EH_PAYMENT_BILLS.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                    .set(Tables.EH_PAYMENT_BILLS.OPERATOR_UID,UserContext.currentUserId())
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVABLE_WITHOUT_TAX, amountReceivableWithoutTax[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_OWED_WITHOUT_TAX, amountOwedWithoutTax[0])
                    .set(Tables.EH_PAYMENT_BILLS.AMOUNT_RECEIVED_WITHOUT_TAX, amountReceivedWithoutTax[0])
                    .set(Tables.EH_PAYMENT_BILLS.TAX_AMOUNT, taxAmount[0])
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                //瑞安CM对接：只读的账单不允许任何修改，要过滤掉
                .and(Tables.EH_PAYMENT_BILLS.IS_READONLY.eq((byte)0))//只读状态：0：非只读；1：只读
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .execute();
    }

    @Override
    public List<PaymentBillItems> getBillItemsByBillIds(List<Long> overdueBillIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.in(overdueBillIds))
                .and(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .execute();
    }

    @Override
    public void linkOrganizationToBill(Long ownerUid, String orgName) {
        DSLContext context = getReadWriteContext();
        context.update(Tables.EH_PAYMENT_BILLS)
                .set(Tables.EH_PAYMENT_BILLS.TARGET_ID, ownerUid)
                .where(Tables.EH_PAYMENT_BILLS.TARGET_NAME.eq(orgName))
                .and(Tables.EH_PAYMENT_BILLS.TARGET_TYPE.eq(AssetTargetType.ORGANIZATION.getCode()))
                .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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
                .and(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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

    public Map<Long,String> getGroupNames(ArrayList<Long> groupIds) {
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
	
	//add by tangcen 2018年6月12日16:18:51
	@Override
	public void deleteUnsettledBillsOnContractId(Long contractId,List<Long> billIdsNotToDelete) {
	    EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        EhPaymentBillItems t1 = Tables.EH_PAYMENT_BILL_ITEMS.as("t1");
        this.coordinationProvider.getNamedLock(contractId.toString()).enter(() -> {
            this.dbProvider.execute((TransactionStatus status) -> {
                DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
                List<Long> billIds = context.select(t.ID)
                        .from(t)
                        .where(t.CONTRACT_ID.eq(contractId))
                        .and(t.SWITCH.eq((byte) 0))
                        .fetch(t.ID);
                //删除时保存最近的一条未出账单信息
                billIds.removeAll(billIdsNotToDelete);
                context.delete(t)
                        .where(t.ID.in(billIds))
                        .execute();
                context.delete(t1)
                        .where(t1.BILL_ID.in(billIds))
                        .execute();
                return null;
            });
            return null;
        });

	}

	@Override
	public PaymentBills getFirstUnsettledBill(Long contractId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
		Record record = context.select()
					            .from(t)
					            .where(t.CONTRACT_ID.eq(contractId))
					            .and(t.SWITCH.eq((byte) 0))
					            .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
					            .orderBy(t.DATE_STR.asc())
					            .limit(1)
					            .fetchOne();
       return ConvertHelper.convert(record, PaymentBills.class);
	}

	@Override
	public List<PaymentBillItems> findBillItemsByBillId(Long billId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
		List<PaymentBillItems> result = new ArrayList<>();
		context.select()
			   .from(t)
			   .where(t.BILL_ID.eq(billId))
			   .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
			   .fetch()
			   .forEach(r->{
				   PaymentBillItems billItem = ConvertHelper.convert(r, PaymentBillItems.class);
				   result.add(billItem);
			   });
		return result;
	}

	@Override
	public void updatePaymentBills(PaymentBills bill) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillsDao dao = new EhPaymentBillsDao(context.configuration());
        dao.update(bill);
	}

	@Override
	public List<PaymentBills> getUnsettledBillBeforeEndtime(Long contractId,String endTimestr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
		List<PaymentBills> result = new ArrayList<>();
		context.select()
			   	.from(t)
			   	.where(t.CONTRACT_ID.eq(contractId))
	            .and(t.SWITCH.eq((byte) 0))
	            .and(t.DATE_STR_BEGIN.le(endTimestr))
	            .and(t.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
	            .orderBy(t.DATE_STR.asc())
	            .fetch()
	            .forEach(r->{
	            	PaymentBills bill = ConvertHelper.convert(r, PaymentBills.class);
	            	result.add(bill);
	            });
		return result;
	}

	@Override
	public void deleteUnsettledBills(Long contractId, String endTimeStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
        EhPaymentBillItems billItems = Tables.EH_PAYMENT_BILL_ITEMS.as("billItems");
        //获取要删除的账单的id
        List<Long> billIds =context.select(billItems.BILL_ID)
					        		.from(billItems)
					        		.where(billItems.CONTRACT_ID.eq(contractId))
					        		.and(billItems.DATE_STR_BEGIN.gt(endTimeStr))
					        		.groupBy(billItems.BILL_ID)
					        		.fetch(billItems.BILL_ID);
        //删除无效的billItems
        context.delete(billItems)
        		.where(billItems.CONTRACT_ID.eq(contractId))
        		.and(billItems.DATE_STR_BEGIN.gt(endTimeStr))
        		.execute();
        //删除无效的账单
        context.delete(bills)
        		.where(bills.ID.in(billIds))
        		.execute();
	}

	@Override
	public PaymentBills findLastBill(Long contractId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
		List<PaymentBills> list = context.select()
										.from(bills)
										.where(bills.CONTRACT_ID.eq(contractId))
										.and(bills.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
										.orderBy(bills.DATE_STR_BEGIN.desc())
										.limit(0,1)
										.fetchInto(PaymentBills.class);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public String findEndTimeByPeriod(String endTimeStr, Long contractId, Long chargingItemId) {
		//SELECT date_str_end FROM eh_payment_bill_items
		//WHERE date_str_begin<"2018-09-10"
		//AND contract_id=7322
		//GROUP BY date_str_end
		//ORDER BY date_str_end DESC
		//LIMIT 0,1
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillItems billItems = Tables.EH_PAYMENT_BILL_ITEMS.as("billItems");
		String endTimeByPeriod = context.select(billItems.DATE_STR_END)
										.from(billItems)
						        		.where(billItems.CONTRACT_ID.eq(contractId))
						        		.and(billItems.DATE_STR_BEGIN.le(endTimeStr))
						        		.and(billItems.CHARGING_ITEMS_ID.eq(chargingItemId))
						        		.and(billItems.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
						        		.groupBy(billItems.DATE_STR_END)
						        		.orderBy(billItems.DATE_STR_END.desc())
						        		.limit(0, 1)
						        		.fetchOne(0,String.class);

        return endTimeByPeriod;
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
        String paymentOrderNum = cmd.getPaymentOrderNum();//订单编号
        //卸货结束
        List<PaymentOrderBillDTO> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        com.everhomes.server.schema.tables.EhPaymentBillOrders t2 = Tables.EH_PAYMENT_BILL_ORDERS.as("t2");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID, t.AMOUNT_RECEIVABLE, t.AMOUNT_RECEIVED, t.DATE_STR_BEGIN, t.DATE_STR_END, t.TARGET_NAME, t.TARGET_TYPE, t.BILL_GROUP_ID,
        		t2.BILL_ID, t2.ORDER_NUMBER, t2.PAYMENT_ORDER_ID, t2.GENERAL_ORDER_ID, t2.PAYMENT_TIME, t2.PAYMENT_TYPE, t2.PAYMENT_CHANNEL, t2.UID);
        query.addFrom(t);
        query.addJoin(t2, t.ID.eq(DSL.cast(t2.BILL_ID, Long.class)));
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
            query.addConditions(DSL.cast(t2.PAYMENT_TIME, String.class).greaterOrEqual(startPayTime + " 00:00:00"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(endPayTime)){
            query.addConditions(DSL.cast(t2.PAYMENT_TIME, String.class).lessOrEqual(endPayTime + " 23:59:59"));
        }
        if(!org.springframework.util.StringUtils.isEmpty(paymentType)){
        	//业务系统：paymentType：支付方式，0:微信，1：支付宝，2：对公转账
            //电商系统：paymentType： 支付类型:1:"微信APP支付",2:"网关支付",7:"微信扫码支付",8:"支付宝扫码支付",9:"微信公众号支付",10:"支付宝JS支付",
            //12:"微信刷卡支付（被扫）",13:"支付宝刷卡支付(被扫)",15:"账户余额",21:"微信公众号js支付"
            if(paymentType.equals(0)) {//微信
            	query.addConditions(t2.PAYMENT_TYPE.eq(1)
            			.or(t2.PAYMENT_TYPE.eq(7))
            			.or(t2.PAYMENT_TYPE.eq(9))
            			.or(t2.PAYMENT_TYPE.eq(12))
            			.or(t2.PAYMENT_TYPE.eq(21))
            	);
            }else if(paymentType.equals(1)) {//支付宝
            	query.addConditions(t2.PAYMENT_TYPE.eq(8)
            			.or(t2.PAYMENT_TYPE.eq(10))
            			.or(t2.PAYMENT_TYPE.eq(13))
            	);
            }else if(paymentType.equals(2)){//对公转账
            	query.addConditions(t2.PAYMENT_TYPE.eq(2));
            }
        }
        if(!org.springframework.util.StringUtils.isEmpty(paymentOrderNum)){
        	query.addConditions(t2.PAYMENT_ORDER_ID.like("%" + paymentOrderNum + "%"));
        }
        query.addConditions(t2.PAYMENT_STATUS.eq(PurchaseOrderPaymentStatus.PAID.getCode()));//eh_payment_bill_orders中的PAYMENT_STATUS=2代表支付成功
        query.addOrderBy(t2.PAYMENT_TIME.desc());
        query.addLimit(pageOffSet,pageSize+1);
        query.fetch().map(r -> {
        	PaymentOrderBillDTO dto = new PaymentOrderBillDTO();
        	dto.setBillId(r.getValue(t.ID));//账单ID
        	dto.setDateStrBegin(r.getValue(t.DATE_STR_BEGIN));
        	dto.setDateStrEnd(r.getValue(t.DATE_STR_END));
        	dto.setTargetName(r.getValue(t.TARGET_NAME));
        	dto.setTargetType(r.getValue(t.TARGET_TYPE));
        	dto.setAmountReceivable(r.getValue(t.AMOUNT_RECEIVABLE));
        	dto.setAmountReceived(r.getValue(t.AMOUNT_RECEIVED));
        	String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS)
            		.where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(r.getValue(t.BILL_GROUP_ID))).fetchOne(0,String.class);
        	dto.setBillGroupName(billGroupNameFound);
            SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //缴费时间
            String payTime = r.getValue(t2.PAYMENT_TIME).toString();
			try {
				payTime = yyyyMMddHHmm.format(yyyyMMddHHmm.parse(payTime));
			}catch (Exception e){
				payTime = null;
                LOGGER.error(e.toString());
            }
            dto.setPayTime(payTime);
            try {
            	Integer queryPaymentType = r.getValue(t2.PAYMENT_TYPE);
                dto.setPaymentType(convertPaymentType(queryPaymentType));//支付方式
            }catch(Exception e){
            	LOGGER.debug("Integer.parseInt, paymentType={}, Exception={}", r.getValue(t2.PAYMENT_TYPE), e);
            }
            dto.setPaymentStatus(1);//1：已完成，0：订单异常
            list.add(dto);
            return null;
        });
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
	
	public PaymentOrderBillDTO listPaymentBillDetail(Long billId) {
		PaymentOrderBillDTO dto = new PaymentOrderBillDTO();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills r = Tables.EH_PAYMENT_BILLS.as("r");
        EhPaymentBillItems o = Tables.EH_PAYMENT_BILL_ITEMS.as("o");
        EhPaymentExemptionItems t = Tables.EH_PAYMENT_EXEMPTION_ITEMS.as("t");
        EhPaymentChargingItems k = Tables.EH_PAYMENT_CHARGING_ITEMS.as("k");
        EhAddresses t1 = Tables.EH_ADDRESSES.as("t1");
        List<BillItemDTO> list1 = new ArrayList<>();
        List<ExemptionItemDTO> list2 = new ArrayList<>();
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(r.ID,r.TARGET_ID,r.NOTICETEL,r.DATE_STR,r.DATE_STR_BEGIN,r.DATE_STR_END,r.TARGET_NAME,r.TARGET_TYPE,r.BILL_GROUP_ID,r.CONTRACT_NUM
        		, r.INVOICE_NUMBER, r.BUILDING_NAME, r.APARTMENT_NAME, r.AMOUNT_RECEIVABLE, r.AMOUNT_RECEIVED, r.AMOUNT_EXEMPTION, r.AMOUNT_SUPPLEMENT
        		,DSL.groupConcatDistinct(DSL.concat(o.BUILDING_NAME,DSL.val("/"), o.APARTMENT_NAME)).as("addresses")
        		);
        query.addFrom(r);
        query.addJoin(o, JoinType.LEFT_OUTER_JOIN, r.ID.eq(o.BILL_ID));
        query.addConditions(r.ID.eq(billId));
        query.addConditions(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));//物业缴费V6.0 账单、费项表增加是否删除状态字段
        query.fetch().map(f -> {
        	dto.setAddresses(f.getValue("addresses", String.class));
        	dto.setBillId(f.getValue(r.ID));
        	dto.setDateStrBegin(f.getValue(r.DATE_STR_BEGIN));//账单开始时间
        	dto.setDateStrEnd(f.getValue(r.DATE_STR_END));//账单结束时间
        	dto.setTargetName(f.getValue(r.TARGET_NAME));
        	dto.setTargetType(f.getValue(r.TARGET_TYPE));
        	dto.setBuildingName(f.getValue(r.BUILDING_NAME));
        	dto.setApartmentName(f.getValue(r.APARTMENT_NAME));
            BigDecimal amountReceivable = f.getValue(r.AMOUNT_RECEIVABLE);//应收
            BigDecimal amoutExemption = f.getValue(r.AMOUNT_EXEMPTION);//减免
            BigDecimal amountSupplement = f.getValue(r.AMOUNT_SUPPLEMENT);//增收
            amountReceivable = amountReceivable.subtract(amoutExemption).add(amountSupplement);//应收=应收-减免+增收
            if(amountReceivable.compareTo(BigDecimal.ZERO) > 0) {
            	dto.setAmountReceivable(amountReceivable);
            }else {
            	dto.setAmountReceivable(BigDecimal.ZERO);
            }
            dto.setAmountReceived(f.getValue(r.AMOUNT_RECEIVED));//实收
            dto.setAmountExemption(amoutExemption);//减免
            dto.setAmountSupplement(amountSupplement);//增收
            String billGroupNameFound = context.select(Tables.EH_PAYMENT_BILL_GROUPS.NAME).from(Tables.EH_PAYMENT_BILL_GROUPS)
            		.where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(f.getValue(r.BILL_GROUP_ID))).fetchOne(0,String.class);
            dto.setBillGroupName(billGroupNameFound);
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
        //组装订单信息
        PaymentOrderBillDTO paymentOrderBillDTO = getPaymentOrderDetailDTO(billId);
        dto.setPayTime(paymentOrderBillDTO.getPayTime());
        dto.setPaymentType(paymentOrderBillDTO.getPaymentType());
        dto.setPaymentStatus(paymentOrderBillDTO.getPaymentStatus());
        dto.setPaymentOrderNum(paymentOrderBillDTO.getPaymentOrderNum());
        dto.setPayerName(paymentOrderBillDTO.getPayerName());
        dto.setPayerTel(paymentOrderBillDTO.getPayerTel());
        return dto;
    }
	
	public PaymentOrderBillDTO getPaymentOrderDetailDTO(Long billId) {
		PaymentOrderBillDTO dto = new PaymentOrderBillDTO();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhPaymentBillOrders t2 = Tables.EH_PAYMENT_BILL_ORDERS.as("t2");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t2.BILL_ID, t2.ORDER_NUMBER, t2.PAYMENT_ORDER_ID, t2.GENERAL_ORDER_ID, t2.PAYMENT_TIME, t2.PAYMENT_TYPE, t2.PAYMENT_CHANNEL, t2.UID);
        query.addFrom(t2);
        query.addConditions(t2.BILL_ID.eq(String.valueOf(billId)));
        query.addConditions(t2.PAYMENT_STATUS.eq(PurchaseOrderPaymentStatus.PAID.getCode()));//eh_payment_bill_orders中的PAYMENT_STATUS=2代表支付成功
        query.addOrderBy(t2.PAYMENT_TIME.desc());
        query.fetch().map(r -> {
        	dto.setPaymentOrderNum(String.valueOf(r.getValue(t2.PAYMENT_ORDER_ID)));//支付订单ID
            SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //缴费时间

            String payTime = r.getValue(t2.PAYMENT_TIME).toString();
			try {
				payTime = yyyyMMddHHmm.format(yyyyMMddHHmm.parse(payTime));
			}catch (Exception e){
				payTime = null;
                LOGGER.error(e.toString());
            }
            dto.setPayTime(payTime);
            //获得付款人员（缴费人名称、缴费人电话）
            User userById = userProvider.findUserById(r.getValue(t2.UID));
            if(userById != null) {
            	dto.setPayerName(userById.getNickName());
            	UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(userById.getId(), UserContext.getCurrentNamespaceId());
            	if(userIdentifier != null) {
            		dto.setPayerTel(userIdentifier.getIdentifierToken());
            	}
            }
            try {
            	Integer queryPaymentType = r.getValue(t2.PAYMENT_TYPE);
                dto.setPaymentType(convertPaymentType(queryPaymentType));//支付方式
            }catch(Exception e){
            	LOGGER.debug("Integer.parseInt, paymentType={}, Exception={}", r.getValue(t2.PAYMENT_TYPE), e);
            }
            dto.setPaymentStatus(1);//1：已完成，0：订单异常
            return null;
        });
        return dto;
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
        query.addConditions(r.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
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
	            .and(Tables.EH_PAYMENT_BILL_ITEMS.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
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

	@Override
    public Double getApartmentInfo(Long addressId, Long contractId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.select(Tables.EH_CONTRACT_BUILDING_MAPPINGS.AREA_SIZE)
                .from(Tables.EH_CONTRACT_BUILDING_MAPPINGS)
                .where(Tables.EH_CONTRACT_BUILDING_MAPPINGS.ADDRESS_ID.eq(addressId))
                .and(Tables.EH_CONTRACT_BUILDING_MAPPINGS.CONTRACT_ID.eq(contractId))
                .fetchOne(0,Double.class);
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


	public GetPayBillsForEntResultResp getPayBillsResultByOrderId(Long orderId) {
		GetPayBillsForEntResultResp response = new GetPayBillsForEntResultResp();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<Record> query = context.selectQuery();
		query.addSelect(Tables.EH_PAYMENT_BILL_ORDERS.PAYMENT_STATUS);
        query.addFrom(Tables.EH_PAYMENT_BILL_ORDERS);
		query.addConditions(Tables.EH_PAYMENT_BILL_ORDERS.PAYMENT_ORDER_ID.eq(orderId));
		query.fetch().map(r -> {
			Integer status = r.getValue(Tables.EH_PAYMENT_BILL_ORDERS.PAYMENT_STATUS);
			if(status != null && status.equals(1)) {
				response.setPayState(1);//支付状态，0-待支付、1-支付成功、2-支付中、5-支付失败
			}else{
				response.setPayState(0);
			}
			return null;
		});
		return response;
	}

	/**
	 * 获取费项配置的税率
	 * @param billGroupId
	 * @return
	 */
	public BigDecimal getBillItemTaxRate(Long billGroupId, Long billItemId) {
		final BigDecimal[] taxRate = {BigDecimal.ZERO};
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillGroupsRules rule = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("rule");
        EhPaymentChargingItemScopes ci = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("ci");
        Long categoryId = findCategoryIdFromBillGroup(billGroupId);
        context.select(rule.CHARGING_ITEM_ID,ci.PROJECT_LEVEL_NAME,rule.ID,ci.TAX_RATE)
                .from(rule,ci)
                .where(rule.CHARGING_ITEM_ID.eq(ci.CHARGING_ITEM_ID))
                .and(rule.OWNERID.eq(ci.OWNER_ID))
                .and(rule.BILL_GROUP_ID.eq(billGroupId))
                .and(ci.CATEGORY_ID.eq(categoryId))
                .and(ci.CHARGING_ITEM_ID.eq(billItemId))
                .fetch()
                .map(r -> {
                    taxRate[0] = r.getValue(ci.TAX_RATE);
                    return null;});
        return taxRate[0];
    }

    public void updateBillDueDayCount(Long billId, Long dueDayCount) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
		dslContext.update(bill)
	        .set(bill.DUE_DAY_COUNT,dueDayCount)
	        .where(bill.ID.eq(billId))
	        .execute();
	}

	@Override
	public PaymentBillItems findFirstBillItemToDelete(Long contractId,String endTimeStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillItems billItems = Tables.EH_PAYMENT_BILL_ITEMS.as("billItems");
        List<PaymentBillItems> list = new ArrayList<>();
        list = context.select()
					.from(billItems)
					.where(billItems.CONTRACT_ID.eq(contractId))
					.and(billItems.DATE_STR_BEGIN.gt(endTimeStr))
					.and(billItems.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
					.orderBy(billItems.DATE_STR_BEGIN.asc())
					.limit(0,1)
					.fetchInto(PaymentBillItems.class);
        if (list.size() > 0) {
        	return list.get(0);
		}
        return null;
	}

	@Override
	public PaymentBills findBillById(Long billId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetBills.class, billId));
        EhPaymentBillsDao dao = new EhPaymentBillsDao(context.configuration());
        com.everhomes.server.schema.tables.pojos.EhPaymentBills r = dao.findById(billId);
        if (r != null) {
			return ConvertHelper.convert(r, PaymentBills.class);
		}
		return null;
	}

	@Override
	public void deleteBillItemsAfterDate(Long contractId, String endTimeStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentBillItems billItems = Tables.EH_PAYMENT_BILL_ITEMS.as("billItems");
        context.delete(billItems)
        		.where(billItems.CONTRACT_ID.eq(contractId))
        		.and(billItems.DATE_STR_BEGIN.gt(endTimeStr))
        		.execute();

	}

	public boolean isInWorkChargingStandard(Integer namespaceId, Long chargingStandardId) {
        DSLContext context = getReadOnlyContext();
        //issue-27671 【合同管理】删除收费项“租金”的标准后，进入修改合同条款信息页面，进入“修改”页面，标准显示了“数字”
        //只要关联了合同（包括草稿合同）就不能删除标准
        //看是否关联了合同
        EhContractChargingItems t = Tables.EH_CONTRACT_CHARGING_ITEMS.as("t");
        EhContracts contracts = Tables.EH_CONTRACTS.as("contracts");
        List<Long> fetch1 = context.select(t.CONTRACT_ID)
              .from(t)
              .leftOuterJoin(contracts)
              .on(t.CONTRACT_ID.eq(contracts.ID))
              .where(t.NAMESPACE_ID.eq(namespaceId))
              .and(t.CHARGING_STANDARD_ID.eq(chargingStandardId))
              .and(contracts.STATUS.ne((byte)0)) //修复缺陷 #43425 【缴费管理】新增收费项计算规则，签草稿合同关联该规则，删掉合同，计算规则删不掉
              .fetch(t.CONTRACT_ID);
        if(fetch1.size()>0){
        	return true;
        }else {
        	return false;
        }
    }

	public List<PaymentBills> findBillsByIds(List<String> billIds) {
        return getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .fetchInto(PaymentBills.class);
    }

	public List<PaymentBillOrder> findPaymentBillOrderRecordByOrderNum(String bizOrderNum) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentBillOrdersRecord>  query = context.selectQuery(Tables.EH_PAYMENT_BILL_ORDERS);
        query.addConditions(Tables.EH_PAYMENT_BILL_ORDERS.ORDER_NUMBER.eq(bizOrderNum));
        return query.fetchInto(PaymentBillOrder.class);
	}

	public void updatePaymentBillOrder(String bizOrderNum, Integer paymentStatus, Integer paymentType, Timestamp payDatetime, Integer paymentChannel) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillOrders t = Tables.EH_PAYMENT_BILL_ORDERS.as("t");
        context.update(t)
                .set(t.PAYMENT_STATUS, paymentStatus)
                .set(t.PAYMENT_TYPE, paymentType)
                .set(t.PAYMENT_TIME, payDatetime)
                .set(t.PAYMENT_CHANNEL, paymentChannel)
                .where(t.ORDER_NUMBER.eq(bizOrderNum))
                .execute();
	}

	public AssetModuleAppMapping insertAppMapping(AssetModuleAppMapping mapping) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhAssetModuleAppMappingsDao dao = new EhAssetModuleAppMappingsDao(dslContext.configuration());
        long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAssetModuleAppMappings.class));
        mapping.setId(nextSequence);
        mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        mapping.setCreateUid(UserContext.currentUserId());
        mapping.setStatus(AppMappingStatus.ACTIVE.getCode());
		dao.insert(mapping);
		return mapping;
	}

//    public boolean checkExistAssetMapContract(Long assetCategoryId) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
//                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
//                .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
//                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
//        return records.size() > 0;
//    }
//
//    public void updateAssetMapContract(AssetModuleAppMapping mapping) {
//    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
//    	dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID, mapping.getSourceId())
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONFIG, mapping.getConfig())
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_UID, UserContext.currentUserId())
//	        .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(mapping.getAssetCategoryId()))
//	        .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
//	        .execute();
//    }

//    public boolean checkExistAssetMapEnergy(Long assetCategoryId) {
//        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
//        List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID)
//                .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//                .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(assetCategoryId))
//                .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType()))
//                .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ID);
//        return records.size() > 0;
//    }
//
//    public void updateAssetMapEnergy(AssetModuleAppMapping mapping) {
//    	DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
//    	dslContext.update(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID, mapping.getSourceId())
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONFIG, mapping.getConfig())
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
//	        .set(Tables.EH_ASSET_MODULE_APP_MAPPINGS.UPDATE_UID, UserContext.currentUserId())
//	        .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(mapping.getAssetCategoryId()))
//	        .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType()))
//	        .execute();
//    }

//	public boolean checkExistGeneralBillAssetMapping(AssetGeneralBillMappingCmd cmd) {
//		List<AssetModuleAppMapping> records = findAssetModuleAppMapping(cmd);
//        return records.size() > 0;
//	}
//
//	public AssetModuleAppMapping updateGeneralBillAssetMapping(AssetModuleAppMapping mapping) {
//		AssetGeneralBillMappingCmd cmd = ConvertHelper.convert(mapping, AssetGeneralBillMappingCmd.class);
//		List<AssetModuleAppMapping> records = findAssetModuleAppMapping(cmd);
//		if(records.size() != 0 && records.get(0) != null) {
//			DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
//			EhAssetModuleAppMappingsDao dao = new EhAssetModuleAppMappingsDao(dslContext.configuration());
//			Long id = records.get(0).getId();
//			mapping.setId(id);
//			mapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			mapping.setUpdateUid(UserContext.currentUserId());
//	        dao.update(mapping);
//	        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetModuleAppMappings.class, id);
//		}
//		return mapping;
//	}

	public boolean checkIsUsedByGeneralBill(Long billGroupId, Long chargingItemId) {
		SelectQuery<EhAssetModuleAppMappingsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_ASSET_MODULE_APP_MAPPINGS).getQuery();
		if(billGroupId != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.BILL_GROUP_ID.eq(billGroupId));
		}
		if(chargingItemId != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CHARGING_ITEM_ID.eq(chargingItemId));
		}
		List<AssetModuleAppMapping> records = query.fetchInto(AssetModuleAppMapping.class);
        return records.size() > 0;
	}

	public List<AssetModuleAppMapping> findAssetModuleAppMapping(AssetGeneralBillMappingCmd cmd) {
		SelectQuery<EhAssetModuleAppMappingsRecord> query = getReadOnlyContext().selectFrom(Tables.EH_ASSET_MODULE_APP_MAPPINGS).getQuery();
		query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
		if(cmd.getOwnerId() != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.OWNER_ID.eq(cmd.getOwnerId()));
		}
		if(cmd.getOwnerType() != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.OWNER_TYPE.eq(cmd.getOwnerType()));
		}
		if(cmd.getSourceId() != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID.eq(cmd.getSourceId()));
		}
		if(cmd.getSourceType() != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(cmd.getSourceType()));
		}
		if(cmd.getAssetCategoryId() != null){
			query.addConditions(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(cmd.getAssetCategoryId()));
		}
		List<AssetModuleAppMapping> records = query.fetchInto(AssetModuleAppMapping.class);
		return records;
	}

    public Long getOriginIdFromMappingApp(Long moduleId, Long originId, long targetModuleId) {
        return getOriginIdFromMappingApp(moduleId, originId, targetModuleId, null);
    }

    public Long getOriginIdFromMappingApp(final Long moduleId, final Long originId, long targetModuleId, Integer namespaceId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
	    if(moduleId == PrivilegeConstants.ENERGY_MODULE && targetModuleId == PrivilegeConstants.ASSET_MODULE_ID){
            List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType()))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONFIG.like("%" + "\"energyFlag\":1" + "%"))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
            if(records.size() > 0) return records.get(0);
            return null;
        }
        if(originId == null) return null;
        Long ret = null;

        if(targetModuleId == PrivilegeConstants.ASSET_MODULE_ID && moduleId == PrivilegeConstants.CONTRACT_MODULE){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID.eq(originId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
                    .fetchOne(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
        }else if(targetModuleId == PrivilegeConstants.CONTRACT_MODULE && moduleId == PrivilegeConstants.ASSET_MODULE_ID){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(originId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
                    .fetchOne(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID);
        }
        return ret;
    }

	public List<Long> getOriginIdFromMappingAppForEnergy(final Long moduleId, final Long originId, long targetModuleId, Integer namespaceId) {
        DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
	    if(moduleId == PrivilegeConstants.ENERGY_MODULE && targetModuleId == PrivilegeConstants.ASSET_MODULE_ID){
	    	List<Long> records = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType()))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.CONFIG.like("%" + "\"energyFlag\":1" + "%"))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
            return records;
        }
        if(originId == null) return null;
        List<Long> ret = null;

        if(targetModuleId == PrivilegeConstants.ASSET_MODULE_ID && moduleId == PrivilegeConstants.CONTRACT_MODULE){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID.eq(originId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID);
        }else if(targetModuleId == PrivilegeConstants.CONTRACT_MODULE && moduleId == PrivilegeConstants.ASSET_MODULE_ID){
            ret = dslContext.select(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID)
                    .from(Tables.EH_ASSET_MODULE_APP_MAPPINGS)
                    .where(Tables.EH_ASSET_MODULE_APP_MAPPINGS.ASSET_CATEGORY_ID.eq(originId))
                    .and(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_TYPE.eq(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType()))
                    .fetch(Tables.EH_ASSET_MODULE_APP_MAPPINGS.SOURCE_ID);
        }
        return ret;
    }

	public List<AppAssetCategory> listAssetAppCategory(Integer namespaceId) {
		DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<AppAssetCategory> list = dslContext.select()
	        .from(Tables.EH_ASSET_APP_CATEGORIES)
	        .where(Tables.EH_ASSET_APP_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
	        .fetchInto(AppAssetCategory.class);
		return list;
	}

    /**
     * 根据账单组设置生成账单的账期、账单开始时间、账单结束时间、出账单日、最晚还款日
     * @param billGroupId
     * @return
     */
    public AssetBillDateDTO generateBillDate(Long billGroupId, String dateStrBegin, String dateStrEnd){
    	AssetBillDateDTO dto = new AssetBillDateDTO();
    	//根据billGroup获得时间，如需重复使用，则请抽象出来
        SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    	//需要billGroup查看生成账单周期
        PaymentBillGroup group = assetGroupProvider.getBillGroupById(billGroupId);
        BillingCycle balanceDateType = BillingCycle.fromCode(group.getBalanceDateType());//生成账单周期：自然月、自然季等
        byte dueDayType = group.getDueDayType();//最晚还款日的单位类型，1:日; 2:月
        Integer dueDay = group.getDueDay();//最晚还款日
        BillsDayType billsDayType = BillsDayType.fromCode(group.getBillsDayType());//出账单日类型，1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期第几日
        Integer billsDay = group.getBillsDay();//出账单日
        Calendar start = Calendar.getInstance();
        try{
            // 如果传递了计费开始时间
            if(dateStrBegin != null){
                start.setTime(yyyyMMdd.parse(dateStrBegin));
                dto.setDateStr(yyyyMM.format(yyyyMM.parse(dateStrBegin)));//账期取的是账单开始时间的yyyy-MM
            }else{
            	dto.setDateStr(yyyyMM.format(start.getTime()));//账期取的是账单开始时间的yyyy-MM
                start.setTime(yyyyMM.parse(dto.getDateStr()));
                //如果没有设置账单的开始时间，那么默认是当前月的第一天
                start.set(Calendar.DAY_OF_MONTH,start.getActualMinimum(Calendar.DAY_OF_MONTH));
            }
            dto.setDateStrBegin(yyyyMMdd.format(start.getTime()));
            int cycle = 0;
            switch(balanceDateType){
                case NATURAL_MONTH:
                    cycle = 1;
                    break;
                case NATURAL_QUARTER:
                    cycle = 3;
                    break;
                case NATURAL_YEAR:
                    cycle = 12;
                    break;
                default:
                	cycle = 0;
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
                dto.setDateStrEnd(dateStrEnd);
            }else{
                dto.setDateStrEnd(yyyyMMdd.format(start.getTime()));
            }
            //计算之后设置出账单日
            //出账单日类型，1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期第几日
            if(billsDayType == null){
                billsDayType = BillsDayType.FIRST_MONTH_NEXT_PERIOD;
            }
            Date dateStart = yyyyMMdd.parse(dto.getDateStrBegin());
            Date dateEnd = yyyyMMdd.parse(dto.getDateStrEnd());
            switch (billsDayType){
                case FIRST_MONTH_NEXT_PERIOD:
                	start.setTime(dateEnd);
                	start.add(Calendar.DAY_OF_MONTH, billsDay);
                    break;
                case BEFORE_THIS_PERIOD:
                	start.setTime(dateStart);
                	start.add(Calendar.DAY_OF_MONTH, -billsDay);
                    break;
                case AFTER_THIS_PERIOD:
                	start.setTime(dateStart);
                	start.add(Calendar.DAY_OF_MONTH, billsDay - 1);
                    break;
                case END_THIS_PERIOD:
                	start.setTime(dateEnd);
                    break;
            }
            dto.setDateStrDue(yyyyMMdd.format(start.getTime()));
            //计算之后设置最晚还款日
            //日
            if(dueDayType == (byte)1){
                start.add(Calendar.DAY_OF_MONTH,dueDay);
            }
            //月
            else if(dueDayType == (byte)2){
                start.add(Calendar.MONTH,dueDay);
            }
            dto.setDueDayDeadline(yyyyMMdd.format(start.getTime()));
        }catch (Exception e){
            LOGGER.error(e.toString());
        }
        return dto;
    }

    public PaymentBills getCMBillByThirdBillId(Integer namespaceId, Long ownerId, String thirdBillId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<PaymentBills> list = context.selectFrom(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.THIRD_BILL_ID.eq(thirdBillId))
                .and(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(ownerId))
                .fetchInto(PaymentBills.class);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public PaymentBillItems getCMBillItemByBillId(Long billId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<PaymentBillItems> list = context.selectFrom(Tables.EH_PAYMENT_BILL_ITEMS)
                .where(Tables.EH_PAYMENT_BILL_ITEMS.BILL_ID.eq(billId))
                .fetchInto(PaymentBillItems.class);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    public Long createCMBill(PaymentBills bill) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBills.class));

        bill.setId(id);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBills.class, id));
        EhPaymentBillsDao dao = new EhPaymentBillsDao(context.configuration());
        dao.insert(bill);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentBills.class, null);

        return id;
    }

    public void createCMBillItem(PaymentBillItems items) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillItems.class));

        items.setId(id);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBillItems.class, id));
        EhPaymentBillItemsDao dao = new EhPaymentBillItemsDao(context.configuration());
        dao.insert(items);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentBillItems.class, null);
    }

    public void updateCMBill(PaymentBills paymentBills) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBills.class, paymentBills.getId()));
        EhPaymentBillsDao dao = new EhPaymentBillsDao(context.configuration());
        paymentBills.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(paymentBills);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPaymentBills.class, paymentBills.getId());
    }

    public void updateCMBillItem(PaymentBillItems items) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBillItems.class, items.getId()));
        EhPaymentBillItemsDao dao = new EhPaymentBillItemsDao(context.configuration());
        items.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(items);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPaymentBillItems.class, items.getId());
    }

    public void createOrUpdateAssetModuleAppMapping(AssetModuleAppMapping mapping) {
		//判断缴费是否已经存在关联的记录
		AssetGeneralBillMappingCmd cmd = ConvertHelper.convert(mapping, AssetGeneralBillMappingCmd.class);
		List<AssetModuleAppMapping> records = findAssetModuleAppMapping(cmd);
		if(records.size() != 0 && records.get(0) != null) {
			DSLContext dslContext = this.dbProvider.getDslContext(AccessSpec.readWrite());
			EhAssetModuleAppMappingsDao dao = new EhAssetModuleAppMappingsDao(dslContext.configuration());
			Long id = records.get(0).getId();
			mapping.setId(id);
			mapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			mapping.setUpdateUid(UserContext.currentUserId());
	        dao.update(mapping);
	        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetModuleAppMappings.class, id);
	        LOGGER.info("update eh_asset_module_app_mappings.id={} success!", id);
		}else {
			insertAppMapping(mapping);
			LOGGER.info("insert eh_asset_module_app_mappings success!");
		}
	}
	
	public List<CommunityStatisticParam> getPaymentBillsDateStr() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBills t = Tables.EH_PAYMENT_BILLS.as("t");
        List<CommunityStatisticParam> list = context.selectDistinct(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.DATE_STR)
				.from(t)
				.orderBy(t.NAMESPACE_ID, t.OWNER_ID, t.DATE_STR)
				.fetchInto(CommunityStatisticParam.class);
		return list;
	}

	public List<CommunityStatisticParam> getStatisticCommunityDateStr() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillStatisticCommunity t = Tables.EH_PAYMENT_BILL_STATISTIC_COMMUNITY.as("t");
        List<CommunityStatisticParam> list = context.selectDistinct(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.DATE_STR)
				.from(t)
				.orderBy(t.NAMESPACE_ID, t.OWNER_ID, t.DATE_STR)
				.fetchInto(CommunityStatisticParam.class);
		return list;
	}

	public List<BuildingStatisticParam> getPaymentBillItemsDateStr() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillItems t = Tables.EH_PAYMENT_BILL_ITEMS.as("t");
        List<BuildingStatisticParam> list = context.selectDistinct(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.BUILDING_NAME, t.DATE_STR)
				.from(t)
				.orderBy(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.BUILDING_NAME, t.DATE_STR)
				.fetchInto(BuildingStatisticParam.class);
		return list;
	}

	public List<BuildingStatisticParam> getStatisticBuildingDateStr() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentBillStatisticBuilding t = Tables.EH_PAYMENT_BILL_STATISTIC_BUILDING.as("t");
        List<BuildingStatisticParam> list = context.selectDistinct(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.BUILDING_NAME, t.DATE_STR)
				.from(t)
				.orderBy(t.NAMESPACE_ID, t.OWNER_ID, t.OWNER_TYPE, t.BUILDING_NAME, t.DATE_STR)
				.fetchInto(BuildingStatisticParam.class);
		return list;
	}
	
    //缴费对接门禁
	@Override
	public AssetDooraccessParam findDoorAccessParamById(Long id) {
		assert (id != null);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.pojos.EhAssetDooraccessParams.class, id));
		EhAssetDooraccessParamsDao dao = new EhAssetDooraccessParamsDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), AssetDooraccessParam.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AssetDooraccessParam findDoorAccessParamByParams(SetDoorAccessParamCommand cmd) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessParams.class));
		EhAssetDooraccessParams t1 = Tables.EH_ASSET_DOORACCESS_PARAMS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.NAMESPACE_ID.eq(cmd.getNamespaceId());
		//cond = cond.and(t1.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));
		cond = cond.and(t1.CATEGORY_ID.eq(cmd.getCategoryId()));
		cond = cond.and(t1.OWNER_ID.eq(cmd.getOwnerId()));
		cond = cond.and(t1.ORG_ID.eq(cmd.getOrgId()));
		query.orderBy(t1.CREATE_TIME.desc());

		List<AssetDooraccessParam> assetDooraccessParams = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), AssetDooraccessParam.class));

		if (assetDooraccessParams.size() > 0 && assetDooraccessParams != null) {
			return assetDooraccessParams.get(0);
		}

		return null;
	}

	@Override
	public void createDoorAccessParam(AssetDooraccessParam assetDooraccessParam) {
		long id = this.dbProvider.allocPojoRecordId(EhAssetDooraccessParams.class);
		assetDooraccessParam.setId(id);
		assetDooraccessParam.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		assetDooraccessParam.setStatus(ContractTemplateStatus.ACTIVE.getCode()); // 有效的状态
		assetDooraccessParam.setCreatorUid(UserContext.currentUserId());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessParams.class, id));
		EhAssetDooraccessParamsDao dao = new EhAssetDooraccessParamsDao(context.configuration());
		dao.insert(assetDooraccessParam);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetDooraccessParams.class, null);
	}

	@Override
	public void updateDoorAccessParam(AssetDooraccessParam assetDooraccessParam) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessParams.class, assetDooraccessParam.getId()));
		EhAssetDooraccessParamsDao dao = new EhAssetDooraccessParamsDao(context.configuration());
		assetDooraccessParam.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		assetDooraccessParam.setOperatorUid(UserContext.currentUserId());
		dao.update(assetDooraccessParam);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetDooraccessParams.class, assetDooraccessParam.getId());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AssetDooraccessParam> listDooraccessParams(GetDoorAccessParamCommand cmd) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessParams.class));
		EhAssetDooraccessParams t1 = Tables.EH_ASSET_DOORACCESS_PARAMS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.NAMESPACE_ID.eq(cmd.getNamespaceId());
		cond = cond.and(t1.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));
		cond = cond.and(t1.CATEGORY_ID.eq(cmd.getCategoryId()));
		if (cmd.getOwnerId() != null) {
			cond = cond.and(t1.OWNER_ID.eq(cmd.getOwnerId()));
		}
		if (cmd.getOwnerId() == null) {
			cond = cond.and(t1.ORG_ID.eq(cmd.getOrgId()));
		}
		query.orderBy(t1.CREATE_TIME.desc());

		List<AssetDooraccessParam> assetDooraccessParams = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), AssetDooraccessParam.class));
		return assetDooraccessParams;
	}

	// 获取eh_asset_dooraccess_params 整表数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AssetDooraccessParam> listDooraccessParamsList(byte status) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessParams.class));
		EhAssetDooraccessParams t1 = Tables.EH_ASSET_DOORACCESS_PARAMS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.STATUS.eq(status);
		query.orderBy(t1.CREATE_TIME.desc());
		List<AssetDooraccessParam> assetDooraccessParams = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), AssetDooraccessParam.class));
		return assetDooraccessParams;
	}

	// 根据eh_asset_dooraccess_params 遍历账单,需要在NamespaceId，due_day_count，owner_id建立索引
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SettledBillRes getAssetDoorAccessBills(int pageSize, long pageAnchor, byte status,
			AssetDooraccessParam doorAccessParam) {
		Long pageOffset = (pageAnchor - 1) * pageSize;
		SettledBillRes res = new SettledBillRes();
		/*
		 * List<PaymentBills> paymentBills =
		 * getReadOnlyContext().selectFrom(Tables.EH_PAYMENT_BILLS)
		 * .where(Tables.EH_PAYMENT_BILLS.SWITCH.eq((byte) 1))
		 * .and(Tables.EH_PAYMENT_BILLS.NAMESPACE_ID.eq(doorAccessParam.
		 * getNamespaceId()))
		 * .and(Tables.EH_PAYMENT_BILLS.OWNER_ID.eq(doorAccessParam.getOwnerId()
		 * )) .and(Tables.EH_PAYMENT_BILLS.STATUS.eq(status))
		 * .and(Tables.EH_PAYMENT_BILLS.DELETE_FLAG.eq(
		 * AssetPaymentBillDeleteFlag.VALID.getCode()))
		 * .and(Tables.EH_PAYMENT_BILLS.DUE_DAY_COUNT.greaterOrEqual(
		 * doorAccessParam.getFreezeDays()))//欠费天数大于等于设置的欠费天数关闭门禁
		 * .and(Tables.EH_PAYMENT_BILLS.TARGET_ID.isNotNull())
		 * .limit(pageOffset.intValue(), pageSize+1)
		 * .fetchInto(PaymentBills.class);
		 */
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBills.class));
		EhPaymentBills t1 = Tables.EH_PAYMENT_BILLS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.NAMESPACE_ID.eq(doorAccessParam.getNamespaceId());
		cond = cond.and(t1.SWITCH.eq((byte) 1));
		cond = cond.and(t1.OWNER_ID.eq(doorAccessParam.getOwnerId()));
		cond = cond.and(t1.STATUS.eq(status));
		cond = cond.and(t1.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
		cond = cond.and(t1.DUE_DAY_COUNT.greaterOrEqual((doorAccessParam.getFreezeDays())));// 欠费天数大于等于设置的欠费天数关闭门禁
		cond = cond.and(t1.TARGET_ID.isNotNull());
		query.limit(pageOffset.intValue(), pageSize + 1);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("listAssetBill, sql=" + query.getSQL());
			LOGGER.debug("listAssetBill, bindValues=" + query.getBindValues());
		}

		List<PaymentBills> paymentBills = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), PaymentBills.class));

		if (paymentBills.size() > pageSize) {
			res.setNextPageAnchor(pageAnchor + 1);
			paymentBills.remove(paymentBills.size() - 1);
		} else {
			res.setNextPageAnchor(null);
		}
		res.setBills(paymentBills);
		return res;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SettledBillRes getAssetDoorAccessBillsUNPAID(int pageSize, long pageAnchor, byte status, AssetDooraccessParam doorAccessParam) {
		Long pageOffset = (pageAnchor - 1) * pageSize;
		SettledBillRes res = new SettledBillRes();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPaymentBills.class));
		EhPaymentBills t1 = Tables.EH_PAYMENT_BILLS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.NAMESPACE_ID.eq(doorAccessParam.getNamespaceId());
		cond = cond.and(t1.SWITCH.eq((byte) 1));
		cond = cond.and(t1.OWNER_ID.eq(doorAccessParam.getOwnerId()));
		cond = cond.and(t1.STATUS.eq(status));
		cond = cond.and(t1.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()));
		cond = cond.and(t1.DUE_DAY_COUNT.lt((doorAccessParam.getFreezeDays())));// 欠费天数小于设置的欠费天数开启门禁（门禁已经关闭过）
		cond = cond.and(t1.TARGET_ID.isNotNull());
		query.limit(pageOffset.intValue(), pageSize + 1);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("listAssetBill, sql=" + query.getSQL());
			LOGGER.debug("listAssetBill, bindValues=" + query.getBindValues());
		}

		List<PaymentBills> paymentBills = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), PaymentBills.class));

		if (paymentBills.size() > pageSize) {
			res.setNextPageAnchor(pageAnchor + 1);
			paymentBills.remove(paymentBills.size() - 1);
		} else {
			res.setNextPageAnchor(null);
		}
		res.setBills(paymentBills);
		return res;
	}

	@Override
	public Long createDoorAccessLog(AssetDooraccessLog assetDooraccessLog) {
		long id = this.dbProvider.allocPojoRecordId(EhAssetDooraccessLogs.class);
		assetDooraccessLog.setId(id);
		assetDooraccessLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		assetDooraccessLog.setStatus(ContractTemplateStatus.ACTIVE.getCode()); // 有效的状态
		assetDooraccessLog.setCreatorUid(UserContext.currentUserId());
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessLogs.class, id));
		EhAssetDooraccessLogsDao dao = new EhAssetDooraccessLogsDao(context.configuration());
		dao.insert(assetDooraccessLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAssetDooraccessLogs.class, null);
		return id;
	}

	@Override
	public void updateDoorAccessLog(AssetDooraccessLog assetDooraccessLog) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessLogs.class, assetDooraccessLog.getId()));
		EhAssetDooraccessLogsDao dao = new EhAssetDooraccessLogsDao(context.configuration());
		assetDooraccessLog.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		assetDooraccessLog.setOperatorUid(UserContext.currentUserId());
		dao.update(assetDooraccessLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAssetDooraccessLogs.class, assetDooraccessLog.getId());
	}

	// 删除门禁相关记录
	@Override
	public void deleteAllDoorAccessLog(AssetDooraccessLog assetDooraccessLog) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhAssetDooraccessLogs t1 = Tables.EH_ASSET_DOORACCESS_LOGS.as("t1");
		UpdateQuery<EhAssetDooraccessLogsRecord> query = context.updateQuery(t1);
		query.addValue(t1.STATUS, assetDooraccessLog.getStatus());
		query.execute();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AssetDooraccessLog getDooraccessLog(AssetDooraccessLog assetDooraccessLog) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessLogs.class));
		EhAssetDooraccessLogs t1 = Tables.EH_ASSET_DOORACCESS_LOGS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.NAMESPACE_ID.eq(assetDooraccessLog.getNamespaceId());
		cond = cond.and(t1.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode()));
		cond = cond.and(t1.OWNER_ID.eq(assetDooraccessLog.getOwnerId()));
		if (assetDooraccessLog.getCategoryId() != null) {
			cond = cond.and(t1.CATEGORY_ID.eq(assetDooraccessLog.getCategoryId()));
		}
		if (assetDooraccessLog.getDooraccessStatus() != null) {
			cond = cond.and(t1.DOORACCESS_STATUS.eq(assetDooraccessLog.getDooraccessStatus()));
		}
		if (assetDooraccessLog.getOwnerType() != null) {
			cond = cond.and(t1.OWNER_TYPE.eq(assetDooraccessLog.getOwnerType()));
		}
		if (assetDooraccessLog.getProjectId() != null) {
			cond = cond.and(t1.PROJECT_ID.eq(assetDooraccessLog.getProjectId()));
		}

		query.orderBy(t1.CREATE_TIME.desc());

		List<AssetDooraccessLog> assetDooraccessParams = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), AssetDooraccessLog.class));

		if (assetDooraccessParams != null && assetDooraccessParams.size() > 0) {
			return assetDooraccessParams.get(0);
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaymentBillOrder getPaymentBillOrderByBillId(String billId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(com.everhomes.server.schema.tables.EhPaymentBillOrders.class));
		com.everhomes.server.schema.tables.EhPaymentBillOrders t1 = Tables.EH_PAYMENT_BILL_ORDERS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.BILL_ID.eq(billId);
		cond = cond.and(t1.PAYMENT_STATUS.eq((int) AssetBillStatusType.PAID.getCode()));
		query.orderBy(t1.CREATE_TIME.desc());
		List<PaymentBillOrder> assetDooraccessParams = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), PaymentBillOrder.class));

		if (assetDooraccessParams != null && assetDooraccessParams.size() > 0) {
			return assetDooraccessParams.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<AssetDooraccessLog> getDooraccessLogInStatus(AssetDooraccessParam doorAccessParamInStatus) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhAssetDooraccessLogs.class));
		EhAssetDooraccessLogs t1 = Tables.EH_ASSET_DOORACCESS_LOGS.as("t1");
		SelectJoinStep<Record> query = context.select(t1.fields()).from(t1);
		Condition cond = t1.STATUS.eq(ContractTemplateStatus.ACTIVE.getCode());
		cond = cond.and(t1.PROJECT_ID.eq(doorAccessParamInStatus.getOwnerId()));
		//query.orderBy(t1.CREATE_TIME.desc());
		List<AssetDooraccessLog> assetDooraccessLogs = query.where(cond).fetch()
				.map(new DefaultRecordMapper(t1.recordType(), AssetDooraccessLog.class));
		return assetDooraccessLogs;
	}

	public List<String> findDateStr(List<String> billIds) {
		return getReadOnlyContext().selectDistinct(Tables.EH_PAYMENT_BILLS.DATE_STR)
				.from(Tables.EH_PAYMENT_BILLS)
                .where(Tables.EH_PAYMENT_BILLS.ID.in(billIds))
                .fetchInto(String.class);
	}
	
	public List<PaymentBillOrder> findPaymentBillOrderByGeneralOrderId(Long merchantOrderId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhPaymentBillOrdersRecord>  query = context.selectQuery(Tables.EH_PAYMENT_BILL_ORDERS);
        query.addConditions(Tables.EH_PAYMENT_BILL_ORDERS.GENERAL_ORDER_ID.eq(merchantOrderId));
        return query.fetchInto(PaymentBillOrder.class);
	}
	
	public void updatePaymentBillOrder(Long merchantOrderId, Integer paymentStatus, Integer paymentType, Timestamp payDatetime, Integer paymentChannel) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhPaymentBillOrders t = Tables.EH_PAYMENT_BILL_ORDERS.as("t");
        context.update(t)
                .set(t.PAYMENT_STATUS, paymentStatus)
                .set(t.PAYMENT_TYPE, paymentType)
                .set(t.PAYMENT_TIME, payDatetime)
                .set(t.PAYMENT_CHANNEL, paymentChannel)
                .where(t.GENERAL_ORDER_ID.eq(merchantOrderId))
                .execute();
	}

}
