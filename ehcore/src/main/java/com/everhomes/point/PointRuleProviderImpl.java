// @formatter:off
package com.everhomes.point;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPointRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPointRules;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

@Repository
public class PointRuleProviderImpl implements PointRuleProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointRule(PointRule pointRule) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRules.class));
		pointRule.setId(id);
		pointRule.setCreateTime(DateUtils.currentTimestamp());
		// pointRule.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointRule);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRules.class, id);
	}

	@Override
	public void updatePointRule(PointRule pointRule) {
		// pointRule.setUpdateTime(DateUtils.currentTimestamp());
		// pointRule.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRules.class, pointRule.getId());
	}

	@Override
	public PointRule findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointRule.class);
	}

	private EhPointRulesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRulesDao(context.configuration());
	}

	private EhPointRulesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRulesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
