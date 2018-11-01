package com.everhomes.yellowPage.standard;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceServiceCategoryMatchDao;
import com.everhomes.server.schema.tables.pojos.EhAllianceTag;
import com.everhomes.server.schema.tables.records.EhAllianceServiceCategoryMatchRecord;
import com.everhomes.server.schema.tables.records.EhServiceAllianceCategoriesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class ServiceCategoryMatchProviderImpl implements ServiceCategoryMatchProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceServiceCategoryMatch MATCH = Tables.EH_ALLIANCE_SERVICE_CATEGORY_MATCH;
	
	Class<ServiceCategoryMatch> CLASS = ServiceCategoryMatch.class;

	@Override
	public void createMatch(ServiceCategoryMatch match) {

		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		match.setId(id);
		match.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		match.setCreateUid(UserContext.currentUserId());

		// 使用dao方法
		getServiceCategoryMatchDao(AccessSpec.readWrite()).insert(match);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void deleteMathes(String ownerType, Long ownerId, Long type) {
		readWriteContext().delete(MATCH)
				.where(
						MATCH.OWNER_ID.eq(ownerId)
						.and(MATCH.OWNER_TYPE.eq(ownerType))
						.and(MATCH.TYPE.eq(type)))
				.execute();
	}
	
	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}

	@Override
	public List<ServiceCategoryMatch> listMatches(String ownerType, Long owenrId, Long type) {
		return  getServiceCategoryMatchList(null, null, ownerType,  owenrId , type, null);
	}

	private EhAllianceServiceCategoryMatchDao getServiceCategoryMatchDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceServiceCategoryMatchDao(context.configuration());
	}
	
	private List<ServiceCategoryMatch> getServiceCategoryMatchList(ListingLocator locator, Integer pageSize, String ownerType, Long ownerId,
			Long type, Long serviceId) {
		return listServiceCategoryMatchs(pageSize, locator, (l, query) -> {
			query.addConditions(MATCH.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
			query.addConditions(MATCH.TYPE.eq(type));
			query.addConditions(MATCH.OWNER_TYPE.eq(ownerType));
			query.addConditions(MATCH.OWNER_ID.eq(ownerId));
			if (null != serviceId) {
				query.addConditions(MATCH.SERVICE_ID.eq(serviceId));
			}
			
			return null;
		});
	}
	
	
	private List<ServiceCategoryMatch> listServiceCategoryMatchs(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 1000 : pageSize;
        
        SelectQuery<EhAllianceServiceCategoryMatchRecord> query = context.selectQuery(MATCH);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(MATCH.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
        
        List<ServiceCategoryMatch> list = query.fetchInto(CLASS);
        
        // 设置锚点
        if (null != locator && null != list) {
    		if (realPageSize > 0 && list.size() > realPageSize) {
    			locator.setAnchor(list.get(list.size() - 1).getId());
    			list.remove(list.size() - 1);
    		} else {
    			locator.setAnchor(null);
    		}
        }

        return list;
    }

	@Override
	public ServiceCategoryMatch findMatch(String ownerType, Long ownerId, Long type, Long serviceId) {
		List<ServiceCategoryMatch>  matches = getServiceCategoryMatchList(null, null, ownerType,  ownerId , type, serviceId);
		if (CollectionUtils.isEmpty(matches)) {
			return null;
		}
		
		return matches.get(0);
	}

	@Override
	public void updateMatch(ServiceCategoryMatch match) {
		
		match.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		match.setCreateUid(UserContext.currentUserId());
		
		// 使用dao方法
		getServiceCategoryMatchDao(AccessSpec.readWrite()).update(match);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
	}

	@Override
	public void updateMatchCategoryName(Long type, Long categoryId, String categoryName) {
		UpdateQuery<EhAllianceServiceCategoryMatchRecord> updateQuery = readWriteContext().updateQuery(MATCH);
		updateQuery.addValue(MATCH.CATEGORY_NAME, categoryName);
		updateQuery.addConditions(MATCH.TYPE.eq(type));
		updateQuery.addConditions(MATCH.CATEGORY_ID.eq(categoryId));
		updateQuery.execute();
	}
}
