package com.everhomes.yellowPage.stat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.GroupField;
import org.jooq.Record4;
import org.jooq.SelectHavingStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAllianceStat;
import com.everhomes.server.schema.tables.daos.EhAllianceStatDao;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.YellowPageService;
import com.everhomes.yellowPage.stat.ClickStat;
import com.everhomes.yellowPage.stat.ClickStatProvider;

@Component
public class ClickStatProviderImpl implements ClickStatProvider{

	private final Class<EhAllianceStat> CLASS = EhAllianceStat.class;

	EhAllianceStat TABLE = Tables.EH_ALLIANCE_STAT;

	@Autowired
	DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;	

	@Override
	public List<ClickStat> listStats(Long type, Long ownerId, Long categoryId, Timestamp startTime, Timestamp endTime,
			Byte searchType) {
		
		Condition condition  =  TABLE.TYPE.eq(type).and(TABLE.TYPE.ne(0L));
		if (null != ownerId) {
			condition = condition.and(TABLE.OWNER_ID.eq(ownerId));
		}
		
		if (null != categoryId) {
			condition = condition.and(TABLE.CATEGORY_ID.eq(categoryId));
		}
		
		if (null != startTime) {
			condition = condition.and(TABLE.CLICK_DATE.ge(new Date(startTime.getTime())));
		}
		
		if (null != endTime) {
			condition = condition.and(TABLE.CLICK_DATE.le(new Date(endTime.getTime())));
		}
		
		GroupField groupField = TABLE.CATEGORY_ID;
		if (AllianceClickStatService.TYPE_STAT_SEARCH_BY_SERVICE.equals(searchType)) {
			groupField = TABLE.SERVICE_ID;
		}
		
		return readOnlyContext()
		.select(TABLE.SERVICE_ID, TABLE.TYPE, TABLE.CATEGORY_ID, TABLE.CLICK_TYPE, DSL.sum(TABLE.CLICK_COUNT).as("clickTotal"))
		.from(TABLE)
		.where(condition)
		.groupBy(groupField, TABLE.CLICK_TYPE)
		.fetch()
		.map(r->{
			ClickStat stat = new ClickStat();
			stat.setType(r.getValue(TABLE.TYPE));
			stat.setServiceId(r.getValue(TABLE.SERVICE_ID));
			stat.setCategoryId(r.getValue(TABLE.CATEGORY_ID));
			stat.setClickType(r.getValue(TABLE.CLICK_TYPE));
			BigDecimal totalCount = r.getValue("clickTotal", BigDecimal.class);
			if (null != totalCount) {
				stat.setClickCount(totalCount.longValue());
			}
			return stat;
		});
	}

	@Override
	public void createStat(ClickStat stat) {

		// 设置动态属性 如id，createTime
		stat.setId(getCreateId());
		stat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		// 使用dao方法
		writeDao().insert(stat);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
	}
	
	@Override
	public void deleteStat(Date date) {
		readWriteContext().delete(TABLE).where(TABLE.CLICK_DATE.eq(date)).execute();
	}

	private Long getCreateId() {
		return sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
	}

	private EhAllianceStatDao writeDao() {
		return statDao(AccessSpec.readWrite());
	}

	private EhAllianceStatDao statDao(AccessSpec accessSpec) {
		DSLContext context = dbProvider.getDslContext(accessSpec); 
		return new EhAllianceStatDao(context.configuration());
	}

	private DSLContext readOnlyContext() {
		return dbProvider.getDslContext(AccessSpec.readOnly());
	}
	
	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}

}

