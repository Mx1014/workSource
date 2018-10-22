package com.everhomes.yellowPage.standard;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceConfigStateDao;
import com.everhomes.server.schema.tables.records.EhAllianceConfigStateRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.AllianceConfigState;
import com.everhomes.yellowPage.AllianceConfigStateProvider;

@Component
public class AllianceConfigStateProviderImpl implements AllianceConfigStateProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceConfigState STATE = Tables.EH_ALLIANCE_CONFIG_STATE;
	
	Class<AllianceConfigState> CLASS = AllianceConfigState.class;

	@Override
	public void createAllianceConfigState(AllianceConfigState state) {

		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		state.setId(id);
		state.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		state.setCreateUid(UserContext.currentUserId());

		// 使用dao方法
		getAllianceConfigStateDao(AccessSpec.readWrite()).insert(state);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateAllianceConfigState(AllianceConfigState state) {
		
		// 使用dao方法
		getAllianceConfigStateDao(AccessSpec.readWrite()).update(state);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
		
	}

	@Override
	public AllianceConfigState findConfigState(Long type, Long projectId) {
		List<AllianceConfigState> states = getAllianceConfigStateList(null, null, type, projectId);
		if (CollectionUtils.isEmpty(states)) {
			return null;
		}
		
		return states.get(0);
	}

	private EhAllianceConfigStateDao getAllianceConfigStateDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceConfigStateDao(context.configuration());
	}
	
	private List<AllianceConfigState> getAllianceConfigStateList(ListingLocator locator, Integer pageSize,
			Long type, Long projectId) {
		return listAllianceConfigStates(pageSize, locator, (l, query) -> {
			query.addConditions(STATE.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()));
			query.addConditions(STATE.TYPE.eq(type));
			query.addConditions(STATE.PROJECT_ID.eq(projectId));
			return null;
		});
	}
	
	
	private List<AllianceConfigState> listAllianceConfigStates(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 1000 : pageSize;
        
        SelectQuery<EhAllianceConfigStateRecord> query = context.selectQuery(STATE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(STATE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
        
        List<AllianceConfigState> list = query.fetchInto(CLASS);
        
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

}
