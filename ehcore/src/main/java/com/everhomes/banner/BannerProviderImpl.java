// @formatter:off
package com.everhomes.banner;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.ApplyPolicy;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhBannerCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhBannerClicksDao;
import com.everhomes.server.schema.tables.daos.EhBannerOrdersDao;
import com.everhomes.server.schema.tables.daos.EhBannersDao;
import com.everhomes.server.schema.tables.pojos.EhBannerCategories;
import com.everhomes.server.schema.tables.pojos.EhBannerClicks;
import com.everhomes.server.schema.tables.pojos.EhBannerOrders;
import com.everhomes.server.schema.tables.pojos.EhBanners;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhBannersRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.everhomes.server.schema.Tables.EH_BANNERS;


@Component
public class BannerProviderImpl implements BannerProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public long createBanner(Banner banner) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
        InsertQuery<EhBannersRecord> query = context.insertQuery(Tables.EH_BANNERS);
        banner.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        banner.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        query.setRecord(ConvertHelper.convert(banner, EhBannersRecord.class));
        query.setReturning(Tables.EH_BANNERS.ID);
        query.execute();
        Long id = query.getReturnedRecord().getId();

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBanners.class, null);
        return id;
    }
//    @Caching(evict = { @CacheEvict(value="Banner", key="#banner.id"),
//            @CacheEvict(value="BannerList", key="#banner.id") } )
    @Override
    public void updateBanner(Banner banner) {
        assert(banner != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBannersDao dao = new EhBannersDao(context.configuration());
        banner.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(banner);
        
    }
//    @Caching(evict = { @CacheEvict(value="Banner", key="#banner.id"),
//            @CacheEvict(value="BannerList", key="#banner.id") } )
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
    
//    @Cacheable(value="Banner", key="#id", unless="#result == null")
    @Override
    public Banner findBannerById(long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhBannersDao dao = new EhBannersDao(context.configuration());
        EhBanners banner = dao.findById(id);
        return ConvertHelper.convert(banner, Banner.class);
    }
    
//    @Cacheable(value="BannerList", key="{#scopeType,#scopeId}", unless="#result.size() == 0")
    @Override
    public List<Banner> findBannersByTagAndScope(Integer namespaceId, String sceneType, String bannerLocation,String bannerGroup,byte scopeCode, long scopeId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_BANNERS);
        Condition condition = Tables.EH_BANNERS.STATUS.eq(BannerStatus.ACTIVE.getCode());
        if(bannerLocation != null && !bannerLocation.trim().equals("")){
            condition = condition.and(Tables.EH_BANNERS.BANNER_LOCATION.eq(bannerLocation));
        }
        if(bannerGroup != null && !bannerGroup.trim().equals("")){
            condition = condition.and(Tables.EH_BANNERS.BANNER_GROUP.eq(bannerGroup));
        }
        condition = condition.and(Tables.EH_BANNERS.NAMESPACE_ID.eq(namespaceId));
        if (sceneType != null) {
            condition = condition.and(Tables.EH_BANNERS.SCENE_TYPE.eq(sceneType));
        }

        condition = condition.and(Tables.EH_BANNERS.SCOPE_CODE.eq(scopeCode));
        condition = condition.and(Tables.EH_BANNERS.SCOPE_ID.eq(scopeId));
        if(condition != null) {
            step.where(condition);
        }
        
        List<Banner> result = step.orderBy(Tables.EH_BANNERS.ORDER.desc()).
                fetch().map((r) ->{ return ConvertHelper.convert(r, Banner.class);});
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query banners by tag and scope, sql=" + step.getSQL());
            LOGGER.debug("Query banners by tag and scope, bindValues=" + step.getBindValues());
        }
        
        return result;
    }
    
    @Override
    public List<Banner> listBanners(String keyword, long offset, long pageSize){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(Tables.EH_BANNERS);
        Condition condition = Tables.EH_BANNERS.NAME.like("%" + keyword + "%");
        step.where(condition);

        List<Banner> result = step.orderBy(Tables.EH_BANNERS.CREATE_TIME.desc()).limit((int)pageSize).offset((int)offset).
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
    
//    @Caching(evict = { @CacheEvict(value="BannerClick", key="#bannerClick.id"),
//            @CacheEvict(value="BannerClick-userId", key="{#bannerClick.bannerId, #bannerClick.uid}"),
//            @CacheEvict(value="BannerClick-token", key="#bannerClick.uuid")} )
    @Override
    public void updateBannerClick(BannerClick bannerClick) {
        assert(bannerClick != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerClick.getUid()));
        EhBannerClicksDao dao = new EhBannerClicksDao(context.configuration());
        dao.update(bannerClick);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerClicks.class, bannerClick.getUid());
    }
    
//    @Caching(evict = { @CacheEvict(value="BannerClick", key="#bannerClick.id"),
//            @CacheEvict(value="BannerClick-userId", key="{#bannerClick.bannerId, #bannerClick.uid}"),
//            @CacheEvict(value="BannerClick-token", key="#bannerClick.uuid")} )
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

//    @Cacheable(value="BannerClick", key="#id", unless="#result == null")
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
    
//    @Caching(evict = { @CacheEvict(value="BannerOrder", key="#bannerOrder.id")} )
    @Override
    public void updateBannerOrder(BannerOrder bannerOrder) {
        assert(bannerOrder != null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,bannerOrder.getUid()));
        EhBannerOrdersDao dao = new EhBannerOrdersDao(context.configuration());
        dao.update(bannerOrder);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBannerOrders.class, bannerOrder.getUid());
    }
//    @Caching(evict = { @CacheEvict(value="BannerOrder", key="#bannerOrder.id")} )
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
    
//    @Cacheable(value="BannerOrder", key="id", unless="#result == null")
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
    
//    @Cacheable(value="BannerClick-userId", key="{#bannerId, #userId}", unless="#result == null")
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
    
//    @Cacheable(value="BannerClick-token", key="{#token}", unless="#result == null")
    @Override
    public BannerClick findBannerClickByToken(String token){
        final BannerClick[] result = new BannerClick[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), result, (DSLContext context, Object reducingContext) -> {
            context.select().from(Tables.EH_BANNER_CLICKS)
            .where(Tables.EH_BANNER_CLICKS.UUID.eq(token))
            .fetch().map((r) ->{
                result[0] = ConvertHelper.convert(r, BannerClick.class);
                return null;
            });
           return true;
        });

        return result[0];
    }
    
	@Override
	public List<Banner> findBannerByNamespaceId(Integer namespaceId) {
		List<Banner> banners = new ArrayList<>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhBanners.class), namespaceId, (context, reducingContext) -> {
          context.select().from(Tables.EH_BANNERS)
            .where(Tables.EH_BANNERS.NAMESPACE_ID.eq(namespaceId))
            .fetch().map((r) ->{
            	banners.add(ConvertHelper.convert(r, Banner.class));
            	return null;
            });
           return true;
        });
		return banners;
	}
	
	@Override
	public List<BannerDTO> listBannersByOwner(Integer namespaceId, BannerScope scope, String sceneType, Long pageAnchor, Integer pageSize, ApplyPolicy applyPolicy) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		Condition condition = EH_BANNERS.NAMESPACE_ID.eq(namespaceId).and(EH_BANNERS.STATUS.ne(BannerStatus.DELETE.getCode()));
		if(scope != null && scope.getScopeId() != null && scope.getScopeCode() != null) {
			condition = condition.and(EH_BANNERS.SCOPE_CODE.eq(scope.getScopeCode()).and(EH_BANNERS.SCOPE_ID.eq(scope.getScopeId())));
		}
		if (sceneType != null){
		    condition = condition.and(EH_BANNERS.SCENE_TYPE.eq(sceneType));
        }
		if(pageAnchor != null) {
			condition = condition.and(EH_BANNERS.CREATE_TIME.le(new Timestamp(pageAnchor)));
		}
		if(applyPolicy != null) {
			condition = condition.and(EH_BANNERS.APPLY_POLICY.eq(applyPolicy.getCode()));
		}
		
		SelectSeekStep3<EhBannersRecord, Byte, Integer, Timestamp> orderBy = context.selectFrom(EH_BANNERS).where(condition)
			.orderBy(EH_BANNERS.STATUS.asc(), EH_BANNERS.ORDER.desc(), EH_BANNERS.CREATE_TIME.desc());
		
		List<BannerDTO> dtoList;
		if(pageSize != null) {
			dtoList = orderBy.limit(pageSize).fetch().map(r -> ConvertHelper.convert(r, BannerDTO.class));
		} else {
			dtoList = orderBy.fetch().map(r -> ConvertHelper.convert(r, BannerDTO.class));
		}
		return dtoList;
	}
	
	@Override
	public Map<String, Integer> selectCountGroupBySceneType(Integer namespaceId, BannerScope scope, BannerStatus status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		
		Condition condition = EH_BANNERS.NAMESPACE_ID.eq(namespaceId);
		if(scope != null && scope.getScopeId() != null && scope.getScopeCode() != null) {
			condition = condition.and(EH_BANNERS.SCOPE_CODE.eq(scope.getScopeCode()).and(EH_BANNERS.SCOPE_ID.eq(scope.getScopeId())));
		}
		if(status != null) {
			condition = condition.and(EH_BANNERS.STATUS.eq(status.getCode()));
		}
		return context.select(EH_BANNERS.SCENE_TYPE, DSL.count())
				.from(EH_BANNERS).where(condition)
				.groupBy(EH_BANNERS.SCENE_TYPE).fetchMap(EH_BANNERS.SCENE_TYPE, DSL.count());
	}

    @Override
    public Banner findAnyCustomizedBanner(Integer namespaceId, Byte scopeCode, Long scopeId, String sceneType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = EH_BANNERS.NAMESPACE_ID.eq(namespaceId);
        if(scopeCode != null && scopeId != null) {
            condition = condition.and(EH_BANNERS.SCOPE_CODE.eq(scopeCode).and(EH_BANNERS.SCOPE_ID.eq(scopeId)));
        }
        if (sceneType != null){
            condition = condition.and(EH_BANNERS.SCENE_TYPE.eq(sceneType));
        }
        condition = condition.and(EH_BANNERS.APPLY_POLICY.eq(ApplyPolicy.CUSTOMIZED.getCode()));
        return context.selectFrom(Tables.EH_BANNERS).where(condition).fetchAnyInto(Banner.class);
    }

    @Override
    public List<Banner> listBannersByNamespace(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_BANNERS)
                .where(Tables.EH_BANNERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_BANNERS.STATUS.eq(BannerStatus.ACTIVE.getCode()))
                .fetchInto(Banner.class);
    }

    @Override
    public List<Banner> listBannersByCommunityId(Integer namespaceId, Long communityId, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhBanners t = EH_BANNERS;

        SelectQuery<EhBannersRecord> query = context.selectFrom(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        if (categoryId == null) {
            //兼容旧数据
            query.addConditions(t.CATEGORY_ID.eq(0L));
        }else {
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }
        query.addConditions(t.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode()));
        query.addConditions(t.SCOPE_ID.eq(communityId));

        query.addConditions(t.STATUS.eq(BannerStatus.ACTIVE.getCode()));
        query.addOrderBy(t.ORDER);

        return query.fetchInto(Banner.class);
    }

    @Override
    public Integer getMaxOrderByCommunityId(Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhBanners t = EH_BANNERS;

        SelectQuery<Record1<Integer>> query = context.select(DSL.max(t.ORDER)).from(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));

        query.addConditions(t.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode()));
        query.addConditions(t.SCOPE_ID.eq(communityId));

        // query.addConditions(t.STATUS.eq(BannerStatus.ACTIVE.getCode()));
        return query.fetchOneInto(Integer.class);
    }

    @Override
    public Integer getMinOrderByCommunityId(Integer namespaceId, Long scopeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhBanners t = EH_BANNERS;

        SelectQuery<Record1<Integer>> query = context.select(DSL.min(t.ORDER)).from(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));

        query.addConditions(t.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode()));
        query.addConditions(t.SCOPE_ID.eq(scopeId));

        return query.fetchOneInto(Integer.class);
    }

    @Override
    public List<Banner> listBannersByCommunityId(Integer namespaceId, Long communityId, Long categoryId, int pageSize, ListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhBanners t = EH_BANNERS;

        SelectQuery<EhBannersRecord> query = context.selectFrom(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode()));
        query.addConditions(t.STATUS.ne(BannerStatus.DELETE.getCode()));
        if (categoryId == null) {
            //兼容旧数据
            query.addConditions(t.CATEGORY_ID.eq(0L));
        }else {
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }
        query.addOrderBy(t.STATUS);

        if (communityId != null && communityId != 0) {
            query.addConditions(t.SCOPE_ID.eq(communityId));
            query.addOrderBy(t.ORDER);
        }

        query.addOrderBy(t.ID.desc());

        if (pageSize > 0) {
            if (locator.getAnchor() != null) {
                query.addLimit(locator.getAnchor().intValue(), pageSize + 1);
            } else {
                query.addLimit(pageSize + 1);
            }
        }

        List<Banner> list = query.fetchInto(Banner.class);
        if (list.size() > pageSize && pageSize > 0) {
            long anchor = locator.getAnchor() != null ? locator.getAnchor() : 0;
            locator.setAnchor(anchor + pageSize);
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public Map<Long, Integer> countEnabledBannersByScope(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhBanners t = EH_BANNERS;

        return context.select(t.SCOPE_ID, DSL.count(t.ID))
                .from(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode()))
                .and(t.STATUS.eq(BannerStatus.ACTIVE.getCode()))
                .groupBy(t.SCOPE_ID)
                .fetchMap(t.SCOPE_ID, DSL.count(t.ID));
    }

    @Override
    public void createBannerCategory(BannerCategory bannerCategory) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class));

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBannerCategories.class));
        bannerCategory.setId(id);
        EhBannerCategoriesDao dao = new EhBannerCategoriesDao(context.configuration());
        dao.insert(bannerCategory);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhBannerOrders.class, null);
    }

    @Override
    public BannerCategory findBannerCategoryById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));

        assert(id != null);
        EhBannerCategoriesDao dao = new EhBannerCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), BannerCategory.class);
    }

    @Override
    public void updateBannerCategory(BannerCategory bannerCategory) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class));
        EhBannerCategoriesDao dao = new EhBannerCategoriesDao(context.configuration());
        dao.update(bannerCategory);
    }
}
