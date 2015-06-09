// @formatter:off
package com.everhomes.banner;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBannerClicksDao;
import com.everhomes.server.schema.tables.daos.EhBannerOrdersDao;
import com.everhomes.server.schema.tables.daos.EhBannersDao;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhBannerClicks;
import com.everhomes.server.schema.tables.pojos.EhBannerOrders;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhBannersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;


@Component
public class BannerProviderImpl implements BannerProvider {
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createBanner(Banner banner) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        InsertQuery<EhBannersRecord> query = context.insertQuery(Tables.EH_BANNERS);
        banner.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        query.setRecord(ConvertHelper.convert(banner, EhBannersRecord.class));
        query.setReturning(Tables.EH_BORDERS.ID);
        if(query.execute() > 0) {
            banner.setId(query.getReturnedRecord().getId());
        }
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBanners.class, null);
        
    }

    @Override
    public void updateBanner(Banner banner) {
        assert(banner != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBannersDao dao = new EhBannersDao(context.configuration());
        dao.update(banner);
        
    }

    @Override
    public void deleteBanner(Banner banner) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBannersDao dao = new EhBannersDao(context.configuration());
        dao.delete(banner);
        
    }

    @Override
    public void deleteBanner(long id) {
        BannerProvider self = PlatformContext.getComponent(BannerProvider.class);
        Banner banner = self.findBannerById(id);
        if(banner != null)
            self.deleteBanner(banner);
        
    }

    @Override
    public Banner findBannerById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUsersDao dao = new EhUsersDao(context.configuration());
        EhUsers banner = dao.findById(id);
        return ConvertHelper.convert(banner, Banner.class);
    }
    
    @Override
    public List<Banner> listBannersByScopeTypeAndScopeId(String scopeType, long scopeId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_BANNERS);
        Condition condition = Tables.EH_BANNERS.STATUS.eq(BannerStatus.ACTIVE.getCode());
        if(scopeType != null)
            condition = Tables.EH_BANNERS.SCOPE_TYPE.eq(scopeType);
        if(condition != null)
            condition = condition.and(Tables.EH_BANNERS.SCOPE_ID.eq(scopeId));
        else
            condition = Tables.EH_BANNERS.SCOPE_ID.eq(scopeId);
        if(condition != null) {
            step.where(condition);
        }
        List<Banner> result = step.orderBy(Tables.EH_BANNERS.ORDER.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r, Banner.class);});
        return result;
    }
    
    @Override
    public List<Banner> listAllBanners(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_BANNERS);

        List<Banner> result = step.orderBy(Tables.EH_BANNERS.CREATE_TIME.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r, Banner.class);});
        return result;
    }
    
    @Override
    public void createBannerClick(BannerClick bannerClick) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, bannerClick.getUid()));
        
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBannerClicks.class));
        bannerClick.setId(id);
        bannerClick.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhBannerClicksDao dao = new EhBannerClicksDao(context.configuration());
        dao.insert(bannerClick);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBannerClicks.class, null);
    }

    @Override
    public void updateBannerClick(BannerClick bannerClick) {
        assert(bannerClick != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerClick.getUid()));
        EhBannerClicksDao dao = new EhBannerClicksDao(context.configuration());
        dao.update(bannerClick);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerClicks.class, bannerClick.getUid());
    }

    @Override
    public void deleteBannerClick(BannerClick bannerClick) {
        assert(bannerClick != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerClick.getUid()));
        EhBannerClicksDao dao = new EhBannerClicksDao(context.configuration());
        dao.delete(bannerClick);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerClicks.class, bannerClick.getUid());
    }

    @Override
    public void deleteBannerClick(long id) {
        BannerProvider self = PlatformContext.getComponent(BannerProvider.class);
        BannerClick bannerClick = self.findBannerClickById(id);
        if(bannerClick != null)
            self.deleteBannerClick(bannerClick);
        
    }

    @Override
    public BannerClick findBannerClickById(long id) {
        final BannerClick[] result = new BannerClick[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            EhBannerClicksDao dao = new EhBannerClicksDao(context.configuration());
            EhBannerClicks bannerClick = dao.findById(id);
            if(bannerClick != null) {
                result[0] = ConvertHelper.convert(bannerClick, BannerClick.class);
                return false;
            }
            return true;
        });

        return result[0];
    }
    
    @Override
    public void createBannerOrder(BannerOrder bannerOrder) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, bannerOrder.getUid()));
        
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBannerOrders.class));
        bannerOrder.setId(id);
        bannerOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhBannerOrdersDao dao = new EhBannerOrdersDao(context.configuration());
        dao.insert(bannerOrder);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBannerOrders.class, null);
    }

    @Override
    public void updateBannerOrder(BannerOrder bannerOrder) {
        assert(bannerOrder != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerOrder.getUid()));
        EhBannerOrdersDao dao = new EhBannerOrdersDao(context.configuration());
        dao.update(bannerOrder);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerOrders.class, bannerOrder.getUid());
    }

    @Override
    public void deleteBannerOrder(BannerOrder bannerOrder) {
        assert(bannerOrder != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerOrder.getUid()));
        EhBannerOrdersDao dao = new EhBannerOrdersDao(context.configuration());
        dao.delete(bannerOrder);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerClicks.class, bannerOrder.getUid());
    }

    @Override
    public void deleteBannerOrder(long id) {
        BannerProvider self = PlatformContext.getComponent(BannerProvider.class);
        BannerOrder bannerOrder = self.findBannerOrderById(id);
        if(bannerOrder != null)
            self.deleteBannerOrder(bannerOrder);
        
    }

    @Override
    public BannerOrder findBannerOrderById(long id) {
        final BannerOrder[] result = new BannerOrder[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            EhBannerOrdersDao dao = new EhBannerOrdersDao(context.configuration());
            EhBannerOrders bannerOrder = dao.findById(id);
            if(bannerOrder != null) {
                result[0] = ConvertHelper.convert(bannerOrder, BannerOrder.class);
                return false;
            }
            return true;
        });

        return result[0];
    }

    @Override
    public BannerClick findBannerClickByBannerIdAndUserId(long bannerId, long userId) {
        final BannerClick[] result = new BannerClick[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(Tables.EH_BANNER_CLICKS)
            .where(Tables.EH_BANNER_CLICKS.BANNER_ID.eq(bannerId))
            .and(Tables.EH_BANNER_CLICKS.UID.eq(userId))
            .fetch().map((r) ->{
                result[0] = ConvertHelper.convert(r, BannerClick.class);
                return null;
            });
           return true;
        });

        return result[0];
    }


}
