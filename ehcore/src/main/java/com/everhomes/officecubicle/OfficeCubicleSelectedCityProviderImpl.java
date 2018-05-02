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
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleSelectedCitiesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSelectedCities;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubicleSelectedCityProviderImpl implements OfficeCubicleSelectedCityProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOfficeCubicleSelectedCity(OfficeCubicleSelectedCity officeCubicleSelectedCity) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleSelectedCities.class));
		officeCubicleSelectedCity.setId(id);
		officeCubicleSelectedCity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleSelectedCity.setCreatorUid(UserContext.current().getUser().getId());
		officeCubicleSelectedCity.setOperatorUid(officeCubicleSelectedCity.getCreatorUid());
		officeCubicleSelectedCity.setOperateTime(officeCubicleSelectedCity.getCreateTime());
		getReadWriteDao().insert(officeCubicleSelectedCity);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleSelectedCities.class, null);
	}

	@Override
	public void updateOfficeCubicleSelectedCity(OfficeCubicleSelectedCity officeCubicleSelectedCity) {
		assert (officeCubicleSelectedCity.getId() != null);
		officeCubicleSelectedCity.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(officeCubicleSelectedCity);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleSelectedCities.class, officeCubicleSelectedCity.getId());
	}

	@Override
	public OfficeCubicleSelectedCity findOfficeCubicleSelectedCityById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), OfficeCubicleSelectedCity.class);
	}
	
	@Override
	public List<OfficeCubicleSelectedCity> listOfficeCubicleSelectedCity() {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES)
				.orderBy(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleSelectedCity.class));
	}
	
	private EhOfficeCubicleSelectedCitiesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhOfficeCubicleSelectedCitiesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhOfficeCubicleSelectedCitiesDao getDao(DSLContext context) {
		return new EhOfficeCubicleSelectedCitiesDao(context.configuration());
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

	@Override
	public OfficeCubicleSelectedCity findOfficeCubicleSelectedCityByCreator(Long creator) {
		List<OfficeCubicleSelectedCity> lists = getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES)
				.where(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES.CREATOR_UID.eq(creator))
				.orderBy(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleSelectedCity.class));
		if(lists!=null && lists.size()>0){
			return lists.get(0);
		}
		return null;
	}

	@Override
	public void deleteSelectedCityByCreator(Long creator) {
		getReadOnlyContext().delete(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES)
		.where(Tables.EH_OFFICE_CUBICLE_SELECTED_CITIES.CREATOR_UID.eq(creator)).execute();
	}
}
