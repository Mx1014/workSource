package com.everhomes.rentalv2;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.rentalv2.MaxMinPrice;
import com.everhomes.rest.rentalv2.PriceRuleType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalv2PricePackagesDao;
import com.everhomes.server.schema.tables.pojos.EhRentalv2PricePackages;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
@Component
public class Rentalv2PricePackageProviderImpl implements  Rentalv2PricePackageProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void deletePricePackageByOwnerId(String resourceType, String ownerType, Long ownerId) {
        getReadWriteContext().delete(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.RESOURCE_TYPE.eq(resourceType))
                .execute();
    }

    @Override
    public void deletePricePackageByRentalTypes(String resourceType, String ownerType, Long ownerId, List<Byte> rentalTypes) {
        getReadWriteContext().delete(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.RESOURCE_TYPE.eq(resourceType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.RENTAL_TYPE.notIn(rentalTypes))
                .execute();
    }

    @Override
    public Long createRentalv2PricePackage(Rentalv2PricePackage rentalv2PricePackage) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2PricePackages.class));
        rentalv2PricePackage.setId(id);
        rentalv2PricePackage.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rentalv2PricePackage.setCreatorUid(UserContext.current().getUser().getId());
        if (rentalv2PricePackage.getCellBeginId() == null) {
            rentalv2PricePackage.setCellBeginId(0L);
        }
        if (rentalv2PricePackage.getCellEndId() == null) {
            rentalv2PricePackage.setCellEndId(0L);
        }
        getReadWriteDao().insert(rentalv2PricePackage);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2PricePackages.class, null);
        return  id;
    }
    @Cacheable(value = "listPricePackageByOwner", key="#ownerId", condition="#ownerType.equals('cell')")
    @Override
    public List<Rentalv2PricePackage> listPricePackageByOwner(String resourceType, String ownerType, Long ownerId,
                                                              Byte rentalType,String packageName) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.RESOURCE_TYPE.eq(resourceType));
        if (rentalType!=null)
            step.and(Tables.EH_RENTALV2_PRICE_PACKAGES.RENTAL_TYPE.eq(rentalType));
        if (packageName!=null)
            step.and(Tables.EH_RENTALV2_PRICE_PACKAGES.NAME.eq(packageName));
        return step.orderBy(Tables.EH_RENTALV2_PRICE_PACKAGES.RENTAL_TYPE.asc())
                .fetch().map(r -> ConvertHelper.convert(r, Rentalv2PricePackage.class));
    }

    @Override
    public Rentalv2PricePackage findPricePackageById(Long id) {
        return ConvertHelper.convert(getReadWriteDao().findById(id),Rentalv2PricePackage.class);

    }

    @Override
    public MaxMinPrice findMaxMinPrice(List<Long> packageIds, Byte rentalType,String packageName) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Record record = null;
       if (StringUtils.isEmpty(packageName))
           record = context.select(DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.PRICE),
                DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.ORG_MEMBER_PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.ORG_MEMBER_PRICE),
                DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.APPROVING_USER_PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.APPROVING_USER_PRICE)
        ).from(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq("cell"))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.in(packageIds))
                .fetchOne();
       else
           record = context.select(DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.PRICE),
                   DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.ORG_MEMBER_PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.ORG_MEMBER_PRICE),
                   DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.APPROVING_USER_PRICE), DSL.min(Tables.EH_RENTALV2_PRICE_PACKAGES.APPROVING_USER_PRICE)
           ).from(Tables.EH_RENTALV2_PRICE_PACKAGES)
                   .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq("cell"))
                   .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.in(packageIds))
                   .and(Tables.EH_RENTALV2_PRICE_PACKAGES.NAME.eq(packageName))
                   .fetchOne();

        if (record != null) {
            BigDecimal maxPrice =record.getValue(DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.PRICE));
            BigDecimal minPrice = maxPrice;
            BigDecimal maxOrgMemberPrice = record.getValue(DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.ORG_MEMBER_PRICE));
            BigDecimal minOrgMemberPrice = maxOrgMemberPrice;
            BigDecimal maxApprovingUserPrice = record.getValue(DSL.max(Tables.EH_RENTALV2_PRICE_PACKAGES.APPROVING_USER_PRICE));
            BigDecimal minApprovingUserPrice = maxApprovingUserPrice;
            return new MaxMinPrice(maxPrice, minPrice, maxOrgMemberPrice, minOrgMemberPrice, maxApprovingUserPrice, minApprovingUserPrice);
        }
        return new MaxMinPrice();
    }

    private DSLContext getReadOnlyContext() {
        return getContext(AccessSpec.readOnly());
    }

    private EhRentalv2PricePackagesDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhRentalv2PricePackagesDao getDao(DSLContext context) {
        return new EhRentalv2PricePackagesDao(context.configuration());
    }

    private DSLContext getReadWriteContext() {
        return getContext(AccessSpec.readWrite());
    }

    private DSLContext getContext(AccessSpec accessSpec) {
        return dbProvider.getDslContext(accessSpec);
    }

    private BigDecimal max(BigDecimal ... b) {
        BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
        for (BigDecimal bigDecimal : b) {
            max = maxBig(max, bigDecimal);
        }
        return max;
    }

    private BigDecimal maxBig(BigDecimal b1, BigDecimal b2) {
        if (b2 != null && b2.compareTo(b1) > 0) {
            return b2;
        }
        return b1;
    }

    private BigDecimal min(BigDecimal ... b) {
        BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
        for (BigDecimal bigDecimal : b) {
            min = minBig(min, bigDecimal);
        }
        return min;
    }

    private BigDecimal minBig(BigDecimal b1, BigDecimal b2) {
        if (b2 != null && b2.compareTo(b1) < 0) {
            return b2;
        }
        return b1;
    }
}
