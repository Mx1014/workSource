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
import com.everhomes.server.schema.tables.daos.EhPointModulesDao;
import com.everhomes.server.schema.tables.pojos.EhPointModules;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

@Repository
public class PointModuleProviderImpl implements PointModuleProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointModule(PointModule pointModule) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointModules.class));
		pointModule.setId(id);
		pointModule.setCreateTime(DateUtils.currentTimestamp());
		// pointModule.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointModule);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointModules.class, id);
	}

	@Override
	public void updatePointModule(PointModule pointModule) {
		// pointModule.setUpdateTime(DateUtils.currentTimestamp());
		// pointModule.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointModule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointModules.class, pointModule.getId());
	}

	@Override
	public PointModule findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointModule.class);
	}

	private EhPointModulesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointModulesDao(context.configuration());
	}

	private EhPointModulesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointModulesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
