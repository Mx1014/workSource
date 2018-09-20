package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceTagDao;
import com.everhomes.server.schema.tables.pojos.EhAllianceTag;
import com.everhomes.server.schema.tables.records.EhAllianceTagRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class AllianceTagProviderImpl implements AllianceTagProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	

	@Override
	public void createAllianceTag(AllianceTag headTag) {

		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAllianceTag.class));
		headTag.setId(id);
		headTag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		headTag.setCreateUid(UserContext.currentUserId());

		// 使用dao方法
		getAllianceTagDao(AccessSpec.readWrite()).insert(headTag);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAllianceTag.class, null);
	}

	@Override
	public void updateAllianceTag(AllianceTag headTag) {
		
		// 使用dao方法
		getAllianceTagDao(AccessSpec.readWrite()).update(headTag);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAllianceTag.class, null);
	}
	
	@Override
	public List<AllianceTag> getAllianceParentTagList(ListingLocator locator, Integer pageSize, Integer namespaceId,String ownerType,Long ownerId,
			Long type) {
		return getAllianceTagList(locator, pageSize, namespaceId, ownerType, ownerId, type, 0L);
	}

	@Override
	public List<AllianceTag> getAllianceChildTagList(Integer namespaceId, Long type, Long parentId) {
		return getAllianceTagList(null, null, namespaceId, null, null, type, parentId);
	}
	
	private List<AllianceTag> getAllianceTagList(ListingLocator locator, Integer pageSize, Integer namespaceId,
			String ownerType, Long ownerId, Long type, Long parentId) {
		
		return listAllianceTags(pageSize, locator, (l, query) -> {

			com.everhomes.server.schema.tables.EhAllianceTag tag = Tables.EH_ALLIANCE_TAG;
			query.addConditions(tag.NAMESPACE_ID.eq(namespaceId));
			if (!StringUtils.isEmpty(ownerType)) {
				query.addConditions(tag.OWNER_TYPE.eq(ownerType));
			}
			
			if (null != ownerId) {
				query.addConditions(tag.OWNER_ID.eq(ownerId));
			}
			
			query.addConditions(tag.TYPE.eq(type));
			query.addConditions(tag.PARENT_ID.eq(parentId));
			return null;
		});
	}
	
	private EhAllianceTagDao getAllianceTagDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceTagDao(context.configuration());
	}

	@Override
	public List<AllianceTag> listAllianceTags(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 1000 : pageSize;
        
        SelectQuery<EhAllianceTagRecord> query = context.selectQuery(Tables.EH_ALLIANCE_TAG);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(Tables.EH_ALLIANCE_TAG.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		// 必须获取未删除的
		query.addConditions(Tables.EH_ALLIANCE_TAG.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));
		
		// 作排序
        query.addOrderBy(Tables.EH_ALLIANCE_TAG.DEFAULT_ORDER.asc()); 
        query.addOrderBy(Tables.EH_ALLIANCE_TAG.ID.asc());
		
        
        List<AllianceTag> list = query.fetchInto(AllianceTag.class);
        
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
	public void deleteProjectTags(Long ownerId, Long type) {
		com.everhomes.server.schema.tables.EhAllianceTag TABLE = Tables.EH_ALLIANCE_TAG;
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(TABLE)
				.where(
						TABLE.OWNER_ID.eq(ownerId)
						.and(TABLE.OWNER_TYPE.eq(ServiceAllianceBelongType.COMMUNITY.getCode()))
						.and(TABLE.TYPE.eq(type)))
				.execute();
	}

}
