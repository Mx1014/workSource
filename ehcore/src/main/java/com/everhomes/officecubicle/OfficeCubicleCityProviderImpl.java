// @formatter:off
package com.everhomes.officecubicle;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleCitiesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleCities;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubicleCityProviderImpl implements OfficeCubicleCityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOfficeCubicleCity(OfficeCubicleCity officeCubicleCity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleCities.class));
		officeCubicleCity.setId(id);
		officeCubicleCity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleCity.setCreatorUid(UserContext.current().getUser().getId());
		officeCubicleCity.setUpdateTime(officeCubicleCity.getCreateTime());
		officeCubicleCity.setOperatorUid(officeCubicleCity.getCreatorUid());
		getReadWriteDao().insert(officeCubicleCity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleCities.class, null);
	}

	@Override
	public void updateOfficeCubicleCity(OfficeCubicleCity officeCubicleCity) {
		assert (officeCubicleCity.getId() != null);
		officeCubicleCity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleCity.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(officeCubicleCity);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleCities.class, officeCubicleCity.getId());
	}

	@Override
	public OfficeCubicleCity findOfficeCubicleCityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), OfficeCubicleCity.class);
	}
	
	@Override
	public List<OfficeCubicleCity> listOfficeCubicleCity() {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_CITIES)
				.orderBy(Tables.EH_OFFICE_CUBICLE_CITIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleCity.class));
	}
	
	private EhOfficeCubicleCitiesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhOfficeCubicleCitiesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhOfficeCubicleCitiesDao getDao(DSLContext context) {
		return new EhOfficeCubicleCitiesDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
