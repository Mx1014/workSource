package com.everhomes.rentalv2;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.rentalv2.PriceRuleType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalv2PricePackagesDao;
import com.everhomes.server.schema.tables.pojos.EhRentalv2PricePackages;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class Rentalv2PricePackageProviderImpl implements  Rentalv2PricePackageProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void deletePricePackageByOwnerId(String ownerType, Long ownerId) {
        getReadWriteContext().delete(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.eq(ownerId))
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
    public List<Rentalv2PricePackage> listPricePackageByOwner(String ownerType, Long ownerId, Byte rentalType) {
        SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_RENTALV2_PRICE_PACKAGES)
                .where(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_RENTALV2_PRICE_PACKAGES.OWNER_ID.eq(ownerId));
        if (rentalType!=null)
            step.and(Tables.EH_RENTALV2_PRICE_PACKAGES.RENTAL_TYPE.eq(rentalType));

        return step.orderBy(Tables.EH_RENTALV2_PRICE_PACKAGES.RENTAL_TYPE.asc())
                .fetch().map(r -> ConvertHelper.convert(r, Rentalv2PricePackage.class));
    }

    @Override
    public Rentalv2PricePackage findPricePackageById(Long id) {
        return ConvertHelper.convert(getReadWriteDao().findById(id),Rentalv2PricePackage.class);

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
}
