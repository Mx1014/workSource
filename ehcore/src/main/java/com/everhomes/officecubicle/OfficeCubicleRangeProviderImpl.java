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
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleRangesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRanges;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubicleRangeProviderImpl implements OfficeCubicleRangeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createOfficeCubicleRange(OfficeCubicleRange officeCubicleRange) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleRanges.class));
		officeCubicleRange.setId(id);
		officeCubicleRange.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		officeCubicleRange.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(officeCubicleRange);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleRanges.class, null);
	}

	@Override
	public void deleteRangesBySpaceId(Long rangeId) {
		dbProvider.getDslContext(AccessSpec.readOnly()).delete(Tables.EH_OFFICE_CUBICLE_RANGES)
				.where(Tables.EH_OFFICE_CUBICLE_RANGES.SPACE_ID.equal(rangeId)).execute();
	}

	@Override
	public void updateOfficeCubicleRange(OfficeCubicleRange officeCubicleRange) {
		assert (officeCubicleRange.getId() != null);
		getReadWriteDao().update(officeCubicleRange);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleRanges.class, officeCubicleRange.getId());
	}

	@Override
	public OfficeCubicleRange findOfficeCubicleRangeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), OfficeCubicleRange.class);
	}
	
	@Override
	public List<OfficeCubicleRange> listOfficeCubicleRange() {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_RANGES)
				.orderBy(Tables.EH_OFFICE_CUBICLE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleRange.class));
	}

	@Override
	public List<OfficeCubicleRange> listRangesBySpaceId(Long spaceId) {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_RANGES)
				.where(Tables.EH_OFFICE_CUBICLE_RANGES.SPACE_ID.eq(spaceId))
				.orderBy(Tables.EH_OFFICE_CUBICLE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleRange.class));
	}

	@Override
	public OfficeCubicleRange findOfficeCubicleRangeByOwner(Long ownerId, String ownerType, Long spaceId, Integer namespaceId) {
		List<OfficeCubicleRange> list =  getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_RANGES)
				.where(Tables.EH_OFFICE_CUBICLE_RANGES.SPACE_ID.eq(spaceId))
				.and(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_RANGES.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_OFFICE_CUBICLE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleRange.class));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<OfficeCubicleRange> getOfficeCubicleRangeByOwner(Long ownerId, String ownerType,  Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_OFFICE_CUBICLE_RANGES)
				.where(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_RANGES.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_OFFICE_CUBICLE_RANGES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleRange.class));
	}
	
	private EhOfficeCubicleRangesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhOfficeCubicleRangesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhOfficeCubicleRangesDao getDao(DSLContext context) {
		return new EhOfficeCubicleRangesDao(context.configuration());
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
