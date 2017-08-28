package com.everhomes.general_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralFormGroupsDao;
import com.everhomes.server.schema.tables.daos.EhGeneralFormsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormGroups;
import com.everhomes.server.schema.tables.pojos.EhGeneralForms;
import com.everhomes.server.schema.tables.records.EhGeneralFormGroupsRecord;
import com.everhomes.server.schema.tables.records.EhGeneralFormsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralFormProviderImpl implements GeneralFormProvider {
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ShardingProvider shardingProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public Long createGeneralForm(GeneralForm obj) {
		long id = this.sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhGeneralForms.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralForms.class));
		obj.setId(id);

		prepareObj(obj);
		EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
		dao.insert(obj);
		return id;
	}

	@Override
	public void updateGeneralForm(GeneralForm obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralForms.class));
		EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
		dao.update(obj);
	}

	@Override
	public void deleteGeneralForm(GeneralForm obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralForms.class));
		EhGeneralFormsDao dao = new EhGeneralFormsDao(context.configuration());
		dao.deleteById(obj.getId());
	}

	@Override
	public GeneralForm getGeneralFormById(Long id) {
		try {
			GeneralForm[] result = new GeneralForm[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec
					.readWriteWith(EhGeneralForms.class));

			result[0] = context.select().from(Tables.EH_GENERAL_FORMS)
					.where(Tables.EH_GENERAL_FORMS.ID.eq(id)).fetchAny().map((r) -> {
						return ConvertHelper.convert(r, GeneralForm.class);
					});

			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public List<GeneralForm> queryGeneralForms(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralForms.class));

		SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
		if (queryBuilderCallback != null)
			queryBuilderCallback.buildCondition(locator, query);

		if (locator.getAnchor() != null) {
			query.addConditions(Tables.EH_GENERAL_FORMS.ID.gt(locator.getAnchor()));
		}

		query.addLimit(count);
		List<GeneralForm> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, GeneralForm.class);
		});

		if (objs.size() >= count) {
			locator.setAnchor(objs.get(objs.size() - 1).getId());
		} else {
			locator.setAnchor(null);
		}

		return objs;
	}

	private void prepareObj(GeneralForm obj) {
		Long l2 = DateHelper.currentGMTTime().getTime();
		obj.setCreateTime(new Timestamp(l2));
		if(null == obj.getFormOriginId())
			obj.setFormOriginId(obj.getId());
	}

	@Override
	public GeneralForm getActiveGeneralFormByOriginId(Long formOriginId) {
		try {
			GeneralForm[] result = new GeneralForm[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec
					.readWriteWith(EhGeneralForms.class));
			result[0] = context.select().from(Tables.EH_GENERAL_FORMS)
					.where(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.eq(formOriginId))
					.and(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()))
					.orderBy(Tables.EH_GENERAL_FORMS.FORM_VERSION.desc()).fetchAny().map((r) -> {
						return ConvertHelper.convert(r, GeneralForm.class);
					});
			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public void invalidForms(Long formOriginId) { 
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralForms.class));
		context.update(Tables.EH_GENERAL_FORMS).set(Tables.EH_GENERAL_FORMS.STATUS,GeneralFormStatus.INVALID.getCode()).
		where(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.eq(formOriginId)).execute();
	}

	@Override
	public GeneralForm getActiveGeneralFormByOriginIdAndVersion(Long formOriginId, Long formVersion) {
		try {
			GeneralForm[] result = new GeneralForm[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec
					.readWriteWith(EhGeneralForms.class));

			result[0] = context.select().from(Tables.EH_GENERAL_FORMS)
					.where(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.eq(formOriginId)).and(Tables.EH_GENERAL_FORMS.FORM_VERSION.eq(formVersion)).fetchAny().map((r) -> {
						return ConvertHelper.convert(r, GeneralForm.class);
					});

			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public GeneralFormGroups createGeneralFormGroup(GeneralFormGroups group){
		Long id = this.sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhGeneralFormGroups.class));
		group.setId(id);
		group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhGeneralFormGroupsDao dao = new EhGeneralFormGroupsDao(context.configuration());
		dao.insert(group);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhGeneralFormGroups.class, null);
		return group;
	}

	@Override
	public GeneralFormGroups findGeneralFormGroupById(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhGeneralFormGroupsDao dao = new EhGeneralFormGroupsDao(context.configuration());
		EhGeneralFormGroups group = dao.findById(id);
		return ConvertHelper.convert(group, GeneralFormGroups.class);
	}

	@Override
	public void updateGeneralFormGroup(GeneralFormGroups group){
		group.setOperatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhGeneralFormGroupsDao dao = new EhGeneralFormGroupsDao(context.configuration());
		dao.update(group);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGeneralFormGroups.class, group.getId());
	}

	@Override
	public List<GeneralFormGroups> listGeneralFormGroups(Integer namespaceId, Long organizationId, Long formOriginId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhGeneralFormGroupsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_GROUPS);
		query.addConditions(Tables.EH_GENERAL_FORM_GROUPS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORM_GROUPS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_GENERAL_FORM_GROUPS.FORM_ORIGIN_ID.eq(formOriginId));
		List<GeneralFormGroups> results = new ArrayList<>();
		query.fetch().map(r -> {
			results.add(ConvertHelper.convert(r,GeneralFormGroups.class));
			return null;
		});
		if (null != results && 0 != results.size()) {
			return results;
		}
		return null;
	}
	/*		        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesContactsSticky.class));
        archivesContactsSticky.setId(id);
        archivesContactsSticky.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        archivesContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        archivesContactsSticky.setUpdateTime(archivesContactsSticky.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.insert(archivesContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesContactsSticky.class, null);
    */
}
