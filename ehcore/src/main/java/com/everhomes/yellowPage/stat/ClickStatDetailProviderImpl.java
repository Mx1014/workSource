package com.everhomes.yellowPage.stat;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.yellowPage.stat.StatClickOrSortType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAllianceStatDetails;
import com.everhomes.server.schema.tables.daos.EhAllianceStatDetailsDao;
import com.everhomes.server.schema.tables.records.EhAllianceStatDetailsRecord;
import com.everhomes.sms.DateUtil;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.stat.ClickStatDetail;
import com.everhomes.yellowPage.stat.ClickStatDetailProvider;

@Component
public class ClickStatDetailProviderImpl implements ClickStatDetailProvider{

	private final Class<EhAllianceStatDetails> CLASS = EhAllianceStatDetails.class;

	EhAllianceStatDetails TABLE = Tables.EH_ALLIANCE_STAT_DETAILS;

	@Autowired
	DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;	

	@Override
	public List<ClickStatDetail> listStatDetails(ListingLocator locator, Integer pageSize, Long type, Long ownerId,
			Long categoryId, Long serviceId, Byte clickType, Long userId, String userPhone, Timestamp startTime,
			Timestamp endTime) {

		return listTool(locator, pageSize, (l, q) -> {

			Condition condition = TABLE.TYPE.eq(type).and(TABLE.TYPE.ne(0L));
			if (null != ownerId) {
				condition = condition.and(TABLE.OWNER_ID.eq(ownerId));
			}

			if (null != categoryId) {
				condition = condition.and(TABLE.CATEGORY_ID.eq(categoryId));
			}

			if (null != serviceId) {
				condition = condition.and(TABLE.SERVICE_ID.eq(serviceId));
			}

			if (null != clickType) {
				condition = condition.and(TABLE.CLICK_TYPE.eq(clickType));
			}

			if (null != userId) {
				condition = condition.and(TABLE.USER_ID.eq(userId));
			}

			if (!StringUtils.isBlank(userPhone)) {
				condition = condition.and(TABLE.USER_PHONE.eq(userPhone));
			}

			if (null != startTime) {
				condition = condition.and(TABLE.CLICK_TIME.ge(startTime.getTime()));
			}

			if (null != endTime) {
				condition = condition.and(TABLE.CLICK_TIME.le(endTime.getTime()));
			}

			q.addConditions(condition);
			return q;
		});

	}

	@Override
	public void createClickStatDetail(ClickStatDetail detail) {

		// 设置动态属性 如id，createTime
		detail.setId(getCreateId());
		detail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		// 使用dao方法
		writeDao().insert(detail);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
	}

	private Long getCreateId() {
		return sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
	}

	private EhAllianceStatDetailsDao writeDao() {
		return statDetailsDao(AccessSpec.readWrite());
	}

	private EhAllianceStatDetailsDao statDetailsDao(AccessSpec accessSpec) {
		DSLContext context = dbProvider.getDslContext(accessSpec); 
		return new EhAllianceStatDetailsDao(context.configuration());
	}

	private DSLContext readOnlyContext() {
		return dbProvider.getDslContext(AccessSpec.readOnly());
	}
	
	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}
	
	private SelectQuery<EhAllianceStatDetailsRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}
	
	public List<ClickStatDetail> listTool(ListingLocator locator, Integer pageSize,
			ListingQueryBuilderCallback callback) {

		SelectQuery<EhAllianceStatDetailsRecord> query = selectQuery();

		if (callback != null) {
			callback.buildCondition(null, query);
		}
		
		if (null != locator && null != locator.getAnchor()) {
			query.addConditions(TABLE.CLICK_TIME.le(locator.getAnchor()));
			if (locator.getEntityId() > 0) {
				query.addConditions(TABLE.ID.le(locator.getEntityId())); // 因为单单是时间戳有可能重复，故另增加id作为第二排序条件
			}
		}
		
		if (null != pageSize) {
			query.addLimit(pageSize+1); 
		}

		// 做排序
		query.addOrderBy(TABLE.CLICK_TIME.desc());
		query.addOrderBy(TABLE.ID.desc());
		
		 List<ClickStatDetail> details =  query.fetchInto(ClickStatDetail.class);
		if (null != locator) {
			if (null != pageSize && details.size() > pageSize) {
				ClickStatDetail tmp = details.get(details.size() - 1);
				locator.setAnchor(tmp.getClickTime());
				locator.setEntityId(tmp.getId());
				details.remove(details.size() - 1);
			} else {
				locator.setAnchor(null);
				locator.setEntityId(0L);
			}
		}
		
		return details;
	}
	

	@Override
	public List<Map<String, Object>> countClickDetailsByDate(Integer namespaceId, Timestamp minTime, Timestamp maxTime) {
		return readOnlyContext()
				.select(TABLE.NAMESPACE_ID.as("namespaceId"), 
						TABLE.TYPE.as("type"), 
						TABLE.OWNER_ID.as("ownerId"), 
						TABLE.SERVICE_ID.as("serviceId"), 
						TABLE.CATEGORY_ID.as("categoryId"),
						TABLE.CLICK_TYPE.as("clickType"), 
						DSL.count().as("clickCount"))
				.from(TABLE)
				.where(TABLE.CLICK_TIME.between(minTime.getTime(), maxTime.getTime()).and(TABLE.NAMESPACE_ID.eq(namespaceId)))
				.groupBy(
						TABLE.OWNER_ID,
						TABLE.SERVICE_ID, 
						TABLE.CATEGORY_ID, 
						TABLE.CLICK_TYPE)
				.fetch().intoMaps()
				;
	}

	@Override
	public void deleteStatDetail(Date date) {
		if (null == date) {
			return;
		}
		
		String pattern = "yyyy-MM-dd HH:mm:ss";
		java.util.Date minTime = DateUtil.strToDate(date.toString()+" 00:00:00", pattern);
		java.util.Date maxTime = DateUtil.strToDate(date.toString()+" 23:59:59", pattern);
		readWriteContext()
		.delete(TABLE)
		.where(
				TABLE.CLICK_TIME.between(minTime.getTime(), maxTime.getTime())
				)
		.execute();
	}
}

