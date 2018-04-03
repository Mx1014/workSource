// @formatter:off
package com.everhomes.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.asset.CheckPaymentUserResponse;
import com.everhomes.server.schema.tables.EhPaymentUsers;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.CategoryProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.business.BusinessTargetType;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhBusinessAssignedNamespaces;
import com.everhomes.server.schema.tables.EhBusinessVisibleScopes;
import com.everhomes.server.schema.tables.daos.EhBusinessAssignedNamespacesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessAssignedScopesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessVisibleScopesDao;
import com.everhomes.server.schema.tables.daos.EhBusinessesDao;
import com.everhomes.server.schema.tables.pojos.EhBusinessAssignedScopes;
import com.everhomes.server.schema.tables.pojos.EhBusinessCategories;
import com.everhomes.server.schema.tables.pojos.EhBusinesses;
import com.everhomes.server.schema.tables.records.EhBusinessesRecord;
import com.everhomes.util.ConvertHelper;


@Component
public class BusinessProviderImpl implements BusinessProvider {
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private SequenceProvider sequenceProvider;
	@Autowired
	private CategoryProvider categoryProvider;

	//	@Caching(evict = { @CacheEvict(value="BusinessesByCreatorId", key="#business.creatorUid") })
	@Override
	public void createBusiness(Business business) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessesRecord record = ConvertHelper.convert(business, EhBusinessesRecord.class);
		InsertQuery<EhBusinessesRecord> query = context.insertQuery(Tables.EH_BUSINESSES);
		query.setRecord(record);
		query.setReturning(Tables.EH_BUSINESSES.ID);
		query.execute();
		business.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinesses.class, null); 

	}
	//	@Caching(evict = { @CacheEvict(value="BusinessesByCreatorId", key="#business.creatorUid"),
	//	        @CacheEvict(value="Business", key="#business.id"),@CacheEvict(value="BusinessByTargetId", key="#business.targetId") })
	@Override
	public void updateBusiness(Business business) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
		dao.update(business);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinesses.class, null);

	}
	//	@Caching(evict = { @CacheEvict(value="BusinessesByCreatorId", key="#business.creatorUid"),
	//	        @CacheEvict(value="Business", key="#business.id"),@CacheEvict(value="BusinessByTargetId", key="#business.targetId")})
	@Override
	public void deleteBusiness(Business business) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
		dao.delete(business);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinesses.class, null);
	}

	@Override
	public void deleteBusiness(Long id) {
		BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
		Business business = self.findBusinessById(id);
		if(business != null)
			self.deleteBusiness(business);

	}
	//    @Cacheable(value="Business", key="#id",unless="#result==null")
	@Override
	public Business findBusinessById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessesDao dao = new EhBusinessesDao(context.configuration()); 
		return ConvertHelper.convert(dao.findById(id),Business.class);
	}

	@Override
	public List<Business> findBusinessByIds(List<Long> ids) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<Business> businesses = context.select().from(Tables.EH_BUSINESSES)
				.where(Tables.EH_BUSINESSES.ID.in(ids))
				.fetch().stream().map(r ->ConvertHelper.convert(r, Business.class)).collect(Collectors.toList());

		return businesses;
	}

	@Override
	public List<BusinessCategory> findBusinessCategoriesByCategory(Long category, Integer offset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<BusinessCategory> businesses = new ArrayList<>();
		context.select().from(Tables.EH_BUSINESS_CATEGORIES)
		.where(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.eq(category))
		.limit(pageSize).offset(offset)
		.fetch().map(r ->{
			businesses.add(ConvertHelper.convert(r, BusinessCategory.class));
			return null;
		});
		return businesses;
	}

	//    @Cacheable(value="BusinessVisibleScopesOfScope", key="{#scopeId,#scopeType},unless="#result.size() == 0")
	@Override
	public List<BusinessVisibleScope> findBusinessVisibleScopesByScope(Long scopeId, Byte scopeType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinessVisibleScopes.class));
		List<BusinessVisibleScope> businessVisibleScopes = context.select().from(Tables.EH_BUSINESS_VISIBLE_SCOPES)
				.where(Tables.EH_BUSINESS_VISIBLE_SCOPES.SCOPE_CODE.eq(scopeType))
				.and(Tables.EH_BUSINESS_VISIBLE_SCOPES.SCOPE_ID.eq(scopeId))
				.fetch().map(r ->{
					return ConvertHelper.convert(r, BusinessVisibleScope.class);
				});
		return businessVisibleScopes;
	}

	//    @Caching(evict = { @CacheEvict(value="BusinessVisibleScopesOfScope", key="{#businessVisibleScope.scopeCode,#businessVisibleScope.scopeId}")})
	@Override
	public void createBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessVisibleScopes.class));
		businessVisibleScope.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
		dao.insert(businessVisibleScope); 
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessVisibleScopes.class, null); 
	}

	//    @Caching(evict = { @CacheEvict(value="BusinessVisibleScopesOfScope", key="{#businessVisibleScope.scopeCode,#businessVisibleScope.scopeId}"),
	//            @CacheEvict(value="BusinessVisibleScopesOfScope", key="#businessVisibleScope.id")})
	@Override
	public void updateBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
		dao.update(businessVisibleScope);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);

	}
	//    @Caching(evict = { @CacheEvict(value="BusinessVisibleScopesOfScope", key="{#businessVisibleScope.scopeCode,#businessVisibleScope.scopeId}"),
	//            @CacheEvict(value="BusinessVisibleScopesOfScope", key="#businessVisibleScope.id")})
	@Override
	public void deleteBusinessVisibleScope(BusinessVisibleScope businessVisibleScope) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
		dao.delete(businessVisibleScope);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);

	}

	@Override
	public void deleteBusinessVisibleScope(Long id) {
		BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
		BusinessVisibleScope businessVisibleScope = self.findBusinessVisibleScopeById(id);
		if(businessVisibleScope != null)
			self.deleteBusinessVisibleScope(businessVisibleScope);

	}
	//    @Cacheable(value="BusinessVisibleScope", key="#id",unless="#result==null")
	@Override
	public BusinessVisibleScope findBusinessVisibleScopeById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
		return ConvertHelper.convert(dao.findById(id),BusinessVisibleScope.class);
	}

	@Override
	public void createBusinessCategory(BusinessCategory businessCategory) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessCategories.class));
		businessCategory.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
		dao.insert(businessCategory); 
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessCategories.class, null); 

	}
	//    @Caching(evict = { @CacheEvict(value="BusinessCategory", key="#businessCategory.id")})
	@Override
	public void updateBusinessCategory(BusinessCategory businessCategory) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
		dao.update(businessCategory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessCategories.class, null);

	}
	//    @Caching(evict = { @CacheEvict(value="BusinessCategory", key="#businessCategory.id")})
	@Override
	public void deleteBusinessCategory(BusinessCategory businessCategory) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
		dao.delete(businessCategory);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);

	}

	@Override
	public void deleteBusinessCategory(Long id) {
		BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
		BusinessCategory businessCategory = self.findBusinessCategoryById(id);
		if(businessCategory != null)
			self.deleteBusinessCategory(businessCategory);

	}
	//    @Cacheable(value="BusinessCategory", key="#id",unless="#result==null")
	@Override
	public BusinessCategory findBusinessCategoryById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
		return ConvertHelper.convert(dao.findById(id),BusinessCategory.class);
	}

	@Override
	public void deleteBusinessVisibleScopeByBusiness(Business business) {
		assert(business != null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		//context.delete(Tables.EH_BUSINESS_VISIBLE_SCOPES).where(Tables.EH_BUSINESS_VISIBLE_SCOPES.OWNER_ID.eq(business.getId())).execute();
		EhBusinessVisibleScopesDao dao = new EhBusinessVisibleScopesDao(context.configuration()); 
		List<com.everhomes.server.schema.tables.pojos.EhBusinessVisibleScopes>  scopes = dao.fetchByOwnerId(business.getId());
		if(scopes != null && !scopes.isEmpty()){
			scopes.forEach(r -> deleteBusinessVisibleScope(r.getId()));
		}

		//DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessVisibleScopes.class, null);
	}

	@Override
	public void deleteBusinessCategoryByBusiness(Business business) {
		assert(business != null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessCategoriesDao dao = new EhBusinessCategoriesDao(context.configuration()); 
		List<EhBusinessCategories> categories = dao.fetchByOwnerId(business.getId());
		if(categories != null && !categories.isEmpty()){
			categories.forEach(r -> deleteBusinessCategory(r.getId()));
		}

		//        context.delete(Tables.EH_BUSINESS_CATEGORIES).where(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID.eq(business.getId())).execute();
		//        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessCategories.class, null);

	}
	//    @Cacheable(value="BusinessByTargetId", key="#targetId",unless="#result==null")
	@Override
	public Business findBusinessByTargetId(String targetId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<Business> businesses = context.select().from(Tables.EH_BUSINESSES)
				.where(Tables.EH_BUSINESSES.TARGET_ID.eq(targetId))
				.fetch().map(r ->{
					return ConvertHelper.convert(r, Business.class);
				});
		if(businesses == null || businesses.isEmpty())
			return null;
		return businesses.get(0);
	}
	@Override
	public List<Business> listBusinessByCategroys(List<Long> categoryIds, List<String> geoHashList,List<Long> businessNamespaceOwnerIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		Condition c = null;
		for(String geoHashStr : geoHashList){
			if(c == null) {
				c = Tables.EH_BUSINESSES.GEOHASH.like(geoHashStr + "%");
			} else {
				c = c.or(Tables.EH_BUSINESSES.GEOHASH.like(geoHashStr + "%"));
			}
		}
		c=c.and(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.in(categoryIds));
		if(businessNamespaceOwnerIds!=null&&!businessNamespaceOwnerIds.isEmpty())
			c=c.and(Tables.EH_BUSINESSES.ID.in(businessNamespaceOwnerIds));
		return context.select(Tables.EH_BUSINESSES.fields()).from(Tables.EH_BUSINESSES)
				.rightOuterJoin(Tables.EH_BUSINESS_CATEGORIES).on(Tables.EH_BUSINESSES.ID.eq(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID))
				.where(c)
				.fetch().stream().map(r ->{
					Business b = new Business();
					b.setId(r.getValue(Tables.EH_BUSINESSES.ID));
					b.setAddress(r.getValue(Tables.EH_BUSINESSES.ADDRESS));
					b.setBizOwnerUid(r.getValue(Tables.EH_BUSINESSES.BIZ_OWNER_UID));
					b.setContact(r.getValue(Tables.EH_BUSINESSES.CONTACT));
					b.setCreateTime(r.getValue(Tables.EH_BUSINESSES.CREATE_TIME));
					b.setCreatorUid(r.getValue(Tables.EH_BUSINESSES.CREATOR_UID));
					b.setDescription(r.getValue(Tables.EH_BUSINESSES.DESCRIPTION));
					b.setDisplayName(r.getValue(Tables.EH_BUSINESSES.DISPLAY_NAME));
					b.setGeohash(r.getValue(Tables.EH_BUSINESSES.GEOHASH));
					b.setLatitude(r.getValue(Tables.EH_BUSINESSES.LATITUDE));
					b.setLongitude(r.getValue(Tables.EH_BUSINESSES.LONGITUDE));
					b.setLogoUri(r.getValue(Tables.EH_BUSINESSES.LOGO_URI));
					b.setName(r.getValue(Tables.EH_BUSINESSES.NAME));
					b.setPhone(r.getValue(Tables.EH_BUSINESSES.PHONE));
					b.setTargetId(r.getValue(Tables.EH_BUSINESSES.TARGET_ID));
					b.setTargetType(r.getValue(Tables.EH_BUSINESSES.TARGET_TYPE));
					b.setUrl(r.getValue(Tables.EH_BUSINESSES.URL));
					b.setVisibleDistance(r.getValue(Tables.EH_BUSINESSES.VISIBLE_DISTANCE));
					return b;
				}).collect(Collectors.toList());
	}

	//    @Cacheable(value="BusinessesByCreatorId", key="#userId",unless="#result.size()==0")
	@Override
	public List<Business> findBusinessByCreatorId(Long userId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<Business> businesses = context.select().from(Tables.EH_BUSINESSES)
				.where(Tables.EH_BUSINESSES.CREATOR_UID.eq(userId))
				.fetch().map(r ->{
					return ConvertHelper.convert(r, Business.class);
				});

		return businesses;
	}

	@Override
	public List<Business> listBusinessesByKeyword(String keyword, Integer offset,Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_BUSINESSES);
		Condition condition = null;
		if(keyword != null && !keyword.trim().equals("")){
			condition = Tables.EH_BUSINESSES.NAME.like("%" + keyword + "%");
		}
		if(condition != null)
			selectStep.where(condition);
		selectStep.limit(pageSize).offset(offset);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Business> result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_BUSINESSES.recordType(), Business.class)
				);
		return result;
	}

	@Override
	public void createBusinessAssignedScope(BusinessAssignedScope businessAssignedScope) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessAssignedScopes.class));
		businessAssignedScope.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		dao.insert(businessAssignedScope); 
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessAssignedScopes.class, null); 
	}

	//    @Caching(evict = { @CacheEvict(value="BusinessAssignedScope", key="#businessAssignedScope.id")})
	@Override
	public void updateBusinessAssignedScope(BusinessAssignedScope businessAssignedScope) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		dao.update(businessAssignedScope);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessAssignedScopes.class, null);

	}
	//    @Caching(evict = { @CacheEvict(value="BusinessAssignedScope", key="#businessAssignedScope.id")})
	@Override
	public void deleteBusinessAssignedScope(BusinessAssignedScope businessAssignedScope) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		dao.delete(businessAssignedScope);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessAssignedScopes.class, null);

	}

	@Override
	public void deleteBusinessAssignedScope(Long id) {
		BusinessProvider self = PlatformContext.getComponent(BusinessProvider.class);
		BusinessAssignedScope businessAssignedScope = self.findBusinessAssignedScopeById(id);
		if(businessAssignedScope != null)
			self.deleteBusinessAssignedScope(businessAssignedScope);

	}
	//    @Cacheable(value="BusinessAssignedScope", key="#id",unless="#result==null")
	@Override
	public BusinessAssignedScope findBusinessAssignedScopeById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		return ConvertHelper.convert(dao.findById(id),BusinessAssignedScope.class);
	}

	@Override
	public void deleteBusinessAssignedScopeByBusinessId(Long businessId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		List<EhBusinessAssignedScopes> assignedScopes = dao.fetchByOwnerId(businessId);
		if(assignedScopes != null && !assignedScopes.isEmpty()){
			assignedScopes.forEach(r -> deleteBusinessAssignedScope(r.getId()));
		}
		//context.delete(Tables.EH_BUSINESS_ASSIGNED_SCOPES).where(Tables.EH_BUSINESS_ASSIGNED_SCOPES.OWNER_ID.eq(businessId)).execute();
		//DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessAssignedScopes.class, null);

	}

	@Override
	public List<BusinessAssignedScope> listBusinessAssignedScopeByScope(Long cityId, Long communityId,List<Long> ownerIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		SelectJoinStep<Record> selectStep = context.select().from(Tables.EH_BUSINESS_ASSIGNED_SCOPES);
		Condition condition = Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_CODE.eq(ScopeType.ALL.getCode());
		condition = condition.and(Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_ID.eq(0L));
		if(cityId != null){
			Condition conCity = Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_CODE.eq(ScopeType.CITY.getCode());
			conCity = conCity.and(Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_ID.eq(cityId));
			condition = condition.or(conCity);
		}
		if(communityId != null){
			Condition conCommunity = Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_CODE.eq(ScopeType.COMMUNITY.getCode());
			conCommunity = conCommunity.and(Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_ID.eq(communityId));
			condition = condition.or(conCommunity);
		}
		if(ownerIds!=null&&!ownerIds.isEmpty()){
			Condition conOwnerIds = Tables.EH_BUSINESS_ASSIGNED_SCOPES.OWNER_ID.in(ownerIds);
			condition = condition.and(conOwnerIds);
		}
		selectStep.where(condition);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<BusinessAssignedScope> result = selectStep.fetch().map(
				new DefaultRecordMapper(Tables.EH_BUSINESS_ASSIGNED_SCOPES.recordType(), BusinessAssignedScope.class)
				);
		return result;
	}

	@Override
	public List<BusinessCategory> findBusinessCategoriesByIdAndOwnerIds(Long id, List<Long> recommendBizIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<BusinessCategory> businessCategories = context.select().from(Tables.EH_BUSINESS_CATEGORIES)
				.where(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID.in(recommendBizIds))
				.and(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.eq(id))
				.fetch().stream().map(r ->ConvertHelper.convert(r, BusinessCategory.class)).collect(Collectors.toList());
		return businessCategories;
	}

	@Override
	public List<BusinessAssignedScope> findBusinessAssignedScopesByBusinessId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		EhBusinessAssignedScopesDao dao = new EhBusinessAssignedScopesDao(context.configuration()); 
		return dao.fetchByOwnerId(id).stream().map(r -> 
		ConvertHelper.convert(r, BusinessAssignedScope.class)).collect(Collectors.toList());
	}
	@Override
	public List<BusinessCategory> listBusinessCategoriesByCatPIdAndOwnerIds(
			Long id, List<Long> recommendBizIds) {
		List<Long> categoryIds = this.categoryProvider.getBusinessSubCategories(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		List<BusinessCategory> businessCategories = context.select().from(Tables.EH_BUSINESS_CATEGORIES)
				.where(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID.in(recommendBizIds))
				.and(Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.in(categoryIds))
				.fetch().stream().map(r ->ConvertHelper.convert(r, BusinessCategory.class)).collect(Collectors.toList());
		return businessCategories;
	}
	@Override
	public List<Business> listBusinessByIds(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		return context.select().from(Tables.EH_BUSINESSES).where(Tables.EH_BUSINESSES.ID.in(ids))
				.fetch().stream().map(r->ConvertHelper.convert(r, Business.class))
				.collect(Collectors.toList());
	}
	@Override
	public BusinessAssignedNamespace findBusinessAssignedNamespaceByNamespace(Long ownerId, Integer namespaceId,Byte visibleFlag) {
		Condition condition = Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.OWNER_ID.eq(ownerId)
				.and(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.NAMESPACE_ID.eq(namespaceId));
		if(visibleFlag!=null)
			condition = condition.and(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.VISIBLE_FLAG.eq(visibleFlag));

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinessAssignedNamespaces.class));
		List<BusinessAssignedNamespace> list = context.select().from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES)
				.where(condition)
				.fetch().map(r->{return ConvertHelper.convert(r, BusinessAssignedNamespace.class);});
		if(list==null||list.isEmpty())
			return null;
		return list.get(0);
	}
	@Override
	public void updateBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessAssignedNamespacesDao dao = new EhBusinessAssignedNamespacesDao(context.configuration()); 
		dao.update(businessAssignedNamespace);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessAssignedNamespaces.class, null);
	}

	@Override
	public void createBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessAssignedNamespacesDao dao = new EhBusinessAssignedNamespacesDao(context.configuration()); 
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhBusinessAssignedNamespaces.class));
		businessAssignedNamespace.setId(id);
		dao.insert(businessAssignedNamespace);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhBusinessAssignedNamespaces.class, null);

	}
	@Override
	public void deleteBusinessAssignedNamespace(BusinessAssignedNamespace businessAssignedNamespace) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhBusinessAssignedNamespacesDao dao = new EhBusinessAssignedNamespacesDao(context.configuration()); 
		dao.delete(businessAssignedNamespace);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhBusinessAssignedNamespaces.class, null);
	}
	@Override
	public BusinessAssignedScope findBusinessAssignedScopeByScope(Long ownerId, Byte scopeCode, Long scopeId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<BusinessAssignedScope> list = context.select().from(Tables.EH_BUSINESS_ASSIGNED_SCOPES)
				.where(Tables.EH_BUSINESS_ASSIGNED_SCOPES.OWNER_ID.eq(ownerId)
						.and(Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_CODE.eq(scopeCode))
						.and(Tables.EH_BUSINESS_ASSIGNED_SCOPES.SCOPE_ID.eq(scopeId)))
						.fetch().stream().map(r ->{
							return ConvertHelper.convert(r, BusinessAssignedScope.class);
						}).collect(Collectors.toList());
		if(list==null||list.isEmpty())
			return null;
		return list.get(0);
	}

	@Override
	public List<BusinessAssignedScope> listBusinessAssignedScopeByOwnerId(Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<BusinessAssignedScope> list = context.select().from(Tables.EH_BUSINESS_ASSIGNED_SCOPES)
				.where(Tables.EH_BUSINESS_ASSIGNED_SCOPES.OWNER_ID.eq(ownerId))
				.fetch().stream().map(r->{
					return ConvertHelper.convert(r, BusinessAssignedScope.class);
				}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<BusinessAssignedNamespace> listBusinessAssignedNamespaceByOwnerId(Long ownerId,Byte visibleFlag) {
		Condition condition = Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.OWNER_ID.eq(ownerId);
		if(visibleFlag!=null)
			condition = condition.and(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.VISIBLE_FLAG.eq(visibleFlag));
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<BusinessAssignedNamespace> list = context.select().from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES)
				.where(condition)
				.fetch().stream().map(r->{
					return ConvertHelper.convert(r, BusinessAssignedNamespace.class);
				}).collect(Collectors.toList());
		return list;
	}

	@Override
	public List<BusinessAssignedNamespace> listBusinessAssignedNamespaceByNamespaceId(Integer namespaceId,Byte visibleFlag) {
		Condition condition = Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.NAMESPACE_ID.eq(namespaceId);
		if(visibleFlag!=null)
			condition = condition.and(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES.VISIBLE_FLAG.eq(visibleFlag));
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<BusinessAssignedNamespace> list = context.select().from(Tables.EH_BUSINESS_ASSIGNED_NAMESPACES)
				.where(condition)
				.fetch().stream().map(r->{
					return ConvertHelper.convert(r, BusinessAssignedNamespace.class);
				}).collect(Collectors.toList());
		return list;
	}
	
	@Override
	public List<Business> listBusinessByCategorys(List<Long> categoryIds,List<Long> bizIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhBusinesses.class));
		Condition condition = Tables.EH_BUSINESS_CATEGORIES.CATEGORY_ID.in(categoryIds);
		if(bizIds!=null&&!bizIds.isEmpty())
			condition = condition.and(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID.in(bizIds));
		
		return context.select(Tables.EH_BUSINESSES.fields()).from(Tables.EH_BUSINESSES)
				.rightOuterJoin(Tables.EH_BUSINESS_CATEGORIES).on(Tables.EH_BUSINESSES.ID.eq(Tables.EH_BUSINESS_CATEGORIES.OWNER_ID))
				.where(condition)
				.fetch().stream().map(r ->{
					Business b = new Business();
					b.setId(r.getValue(Tables.EH_BUSINESSES.ID));
					b.setAddress(r.getValue(Tables.EH_BUSINESSES.ADDRESS));
					b.setBizOwnerUid(r.getValue(Tables.EH_BUSINESSES.BIZ_OWNER_UID));
					b.setContact(r.getValue(Tables.EH_BUSINESSES.CONTACT));
					b.setCreateTime(r.getValue(Tables.EH_BUSINESSES.CREATE_TIME));
					b.setCreatorUid(r.getValue(Tables.EH_BUSINESSES.CREATOR_UID));
					b.setDescription(r.getValue(Tables.EH_BUSINESSES.DESCRIPTION));
					b.setDisplayName(r.getValue(Tables.EH_BUSINESSES.DISPLAY_NAME));
					b.setGeohash(r.getValue(Tables.EH_BUSINESSES.GEOHASH));
					b.setLatitude(r.getValue(Tables.EH_BUSINESSES.LATITUDE));
					b.setLongitude(r.getValue(Tables.EH_BUSINESSES.LONGITUDE));
					b.setLogoUri(r.getValue(Tables.EH_BUSINESSES.LOGO_URI));
					b.setName(r.getValue(Tables.EH_BUSINESSES.NAME));
					b.setPhone(r.getValue(Tables.EH_BUSINESSES.PHONE));
					b.setTargetId(r.getValue(Tables.EH_BUSINESSES.TARGET_ID));
					b.setTargetType(r.getValue(Tables.EH_BUSINESSES.TARGET_TYPE));
					b.setUrl(r.getValue(Tables.EH_BUSINESSES.URL));
					b.setVisibleDistance(r.getValue(Tables.EH_BUSINESSES.VISIBLE_DISTANCE));
					return b;
				}).collect(Collectors.toList());
	}

	@Override
	public CheckPaymentUserResponse checkoutPaymentUser(Long userId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhPaymentUsers t = Tables.EH_PAYMENT_USERS.as("t");
        List<CheckPaymentUserResponse> list = new ArrayList<>();
        context.select()
                .from(t)
                .where(t.OWNER_ID.eq(userId))
                .fetch()
                .map(r -> {
                    CheckPaymentUserResponse res = new CheckPaymentUserResponse();
                    res.setCreateTime(r.getValue(t.CREATE_TIME));
                    res.setId(r.getValue(t.ID));
                    res.setOwnerId(userId);
                    res.setOwnerType(r.getValue(t.OWNER_TYPE));
                    res.setPayment_user_id(r.getValue(t.PAYMENT_USER_ID));
                    res.setPaymentUserType(r.getValue(t.PAYMENT_USER_TYPE));
                    list.add(res);
                    return null;
                });
        return list.size()<1?null:list.get(0);
    }
}
