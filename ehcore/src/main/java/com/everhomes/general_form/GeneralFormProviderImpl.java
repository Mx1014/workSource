package com.everhomes.general_form;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormValRequestStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralFormFilterUserMapDao;
import com.everhomes.server.schema.tables.daos.EhGeneralFormValRequestsDao;
import com.everhomes.server.schema.tables.daos.EhGeneralFormPrintTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhGeneralFormTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhGeneralFormsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralForms;
import com.everhomes.server.schema.tables.records.EhGeneralFormTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhGeneralFormsRecord;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormPrintTemplates;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhGeneralFormPrintTemplatesRecord;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneralFormProviderImpl implements GeneralFormProvider {
	@Autowired
	private DbProvider dbProvider;

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
		if(StringUtils.isBlank(obj.getOperatorName())){
			User user = UserContext.current().getUser();
			obj.setOperatorName(user.getNickName());
		}
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralForms.class));
		GeneralForm form = context.select().from(Tables.EH_GENERAL_FORMS)
				.where(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.eq(id))
				.orderBy(Tables.EH_GENERAL_FORMS.FORM_VERSION.desc()).fetchAnyInto(GeneralForm.class);
		if(form != null)
			return form;
		return null;

	}

	@Override
	public GeneralForm findGeneralFormById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralForms.class));
		GeneralForm form = context.select().from(Tables.EH_GENERAL_FORMS)
				.where(Tables.EH_GENERAL_FORMS.ID.eq(id)).fetchAnyInto(GeneralForm.class);
		if(form != null)
			return form;
		return null;
	}


	@Override
	public GeneralForm getGeneralFormByApproval(Long formOriginId, Long formVersion) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralForms.class));
		GeneralForm form = context.select().from(Tables.EH_GENERAL_FORMS)
				.where(Tables.EH_GENERAL_FORMS.FORM_ORIGIN_ID.eq(formOriginId))
				.and(Tables.EH_GENERAL_FORMS.FORM_VERSION.eq(formVersion))
				.fetchAnyInto(GeneralForm.class);
		if(form != null)
			return form;
		return null;

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
        if(StringUtils.isBlank(obj.getOperatorName())){
            User user = UserContext.current().getUser();
            obj.setOperatorName(user.getNickName());
        }
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
	public List<GeneralFormTemplate> listGeneralFormTemplate(Long moduleId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhGeneralFormTemplatesRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_TEMPLATES);
		query.addConditions(Tables.EH_GENERAL_FORM_TEMPLATES.MODULE_ID.eq(moduleId));
		List<GeneralFormTemplate> results = query.fetch().map(r -> {
			return ConvertHelper.convert(r, GeneralFormTemplate.class);
		});
		if (results != null && results.size() > 0)
			return results;
		return null;
	}

	@Override
	public GeneralFormTemplate findGeneralFormTemplateByIdAndModuleId(Long id, Long moduleId){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhGeneralFormTemplatesRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_TEMPLATES);
		query.addConditions(Tables.EH_GENERAL_FORM_TEMPLATES.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORM_TEMPLATES.ID.eq(id));
		return query.fetchOneInto(GeneralFormTemplate.class);
	}

	@Override
	public GeneralForm getActiveGeneralFormByName(Long moduleId, Long ownerId, String ownerType, String formName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readOnly());
		SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
		query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_GENERAL_FORMS.FORM_NAME.eq(formName));
		query.addConditions(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
		return query.fetchAnyInto(GeneralForm.class);
	}

    @Override
    public GeneralForm getGeneralFormByTemplateId(Long moduleId, Long ownerId, String ownerType, Long templateId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
        query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_FORMS.FORM_TEMPLATE_ID.eq(templateId));
        query.addConditions(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
        query.addOrderBy(Tables.EH_GENERAL_FORMS.FORM_VERSION.desc());
        return query.fetchAnyInto(GeneralForm.class);
    }
    
    @Override
    public GeneralForm getGeneralFormByTag1(Integer namespaceId, String moduleType, Long moduleId, String stringTag1) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
		query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORMS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_TYPE.eq(moduleType));
		query.addConditions(Tables.EH_GENERAL_FORMS.STRING_TAG1.eq(stringTag1));
		List<GeneralForm> results = query.fetch().map(r -> {
			return ConvertHelper.convert(r, GeneralForm.class);
		});
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
    }

    @Override
    public List<GeneralForm> listGeneralForm(Integer namespaceId, String moduleType, Long moduleId,
											 String projectType, Long projectId, String ownerType, Long ownerId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhGeneralForms t = Tables.EH_GENERAL_FORMS;
		SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(t);

		query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(t.MODULE_TYPE.eq(moduleType));
		query.addConditions(t.MODULE_ID.eq(moduleId));
		query.addConditions(t.OWNER_TYPE.eq(ownerType));
		query.addConditions(t.OWNER_ID.eq(ownerId));
		query.addConditions(t.PROJECT_TYPE.eq(projectType));
		query.addConditions(t.PROJECT_ID.eq(projectId));

		query.addConditions(t.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
		return query.fetchInto(GeneralForm.class);
    }

	@Override
	public GeneralForm getActiveGeneralFormByName(String projectType, Long projectId, Long moduleId, Long ownerId, String ownerType, String formName) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readOnly());
		SelectQuery<EhGeneralFormsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORMS);
		if (projectType != null) {
			query.addConditions(Tables.EH_GENERAL_FORMS.PROJECT_TYPE.eq(projectType));
		}
		if (projectId != null) {
			query.addConditions(Tables.EH_GENERAL_FORMS.PROJECT_ID.eq(projectId));
		}
		query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_GENERAL_FORMS.FORM_NAME.eq(formName));
		query.addConditions(Tables.EH_GENERAL_FORMS.STATUS.ne(GeneralFormStatus.INVALID.getCode()));
		return query.fetchAnyInto(GeneralForm.class);
	}

	@Override
	public GeneralFormTemplate getDefaultFieldsByModuleId(Long moduleId,Integer namespaceId) {
		try {
			GeneralFormTemplate[] result = new GeneralFormTemplate[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormTemplates.class));
			result[0] = context.select().from(Tables.EH_GENERAL_FORM_TEMPLATES)
					.where(Tables.EH_GENERAL_FORM_TEMPLATES.MODULE_ID.eq(moduleId))
					.and(Tables.EH_GENERAL_FORM_TEMPLATES.NAMESPACE_ID.eq(namespaceId)
							.or(Tables.EH_GENERAL_FORM_TEMPLATES.NAMESPACE_ID.eq(0)))
					.orderBy(Tables.EH_GENERAL_FORM_TEMPLATES.VERSION.desc()).fetchAny().map((r) -> {
						return ConvertHelper.convert(r, GeneralFormTemplate.class);
					});
			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteGeneralFormVal(Integer namespaceId, Long moduleId, Long sourceId){
		try {
			DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
			context.delete(Tables.EH_GENERAL_FORM_VALS)
					.where(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.eq(sourceId))
					.and(Tables.EH_GENERAL_FORM_VALS.MODULE_ID.eq(moduleId))
					.and(Tables.EH_GENERAL_FORM_VALS.NAMESPACE_ID.eq(namespaceId))
					.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public List<GeneralFormVal> getGeneralFormVal(Integer namespaceId, Long sourceId,  Long moduleId, Long ownerId){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormVals.class));

		SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);
		query.addConditions(Tables.EH_GENERAL_FORM_VALS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORM_VALS.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORM_VALS.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.eq(sourceId));
		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormVal.class));
	}

	@Override
	public GeneralFormVal getGeneralFormValByCustomerId(Integer namespaceId, Long customerId,  Long moduleId, Long ownerId){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormVals.class));

		GeneralFormVal[] result = new GeneralFormVal[1];

		try {
			result[0] = context.select().from(Tables.EH_GENERAL_FORM_VALS)
				.where(Tables.EH_GENERAL_FORM_VALS.MODULE_ID.eq(moduleId))
				.and(Tables.EH_GENERAL_FORM_VALS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_GENERAL_FORM_VALS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_GENERAL_FORM_VALS.FIELD_VALUE.eq("{\"customerName\":" + customerId +"}"))
				.and(Tables.EH_GENERAL_FORM_VALS.FIELD_NAME.eq("客户名称")).fetchAny().map((r) -> {
					return ConvertHelper.convert(r, GeneralFormVal.class);
				});
		} catch (Exception ex) {
			// fetchAny() maybe return null
			ex.printStackTrace();
			return null;
		}

		return result[0];
	}



	@Override
	public Long saveGeneralFormValRequest(Integer namespaceId, String moduleType, String ownerType, Long ownerId, Long moduleId, Long investmentAdId,Long formOriginId, Long formVersion){
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralFormValRequests.class));
		GeneralFormValRequest generalFormValRequests = new GeneralFormValRequest();
		generalFormValRequests.setCreatorUid(UserContext.currentUserId());
		Long l2 = DateHelper.currentGMTTime().getTime();
		generalFormValRequests.setCreatedTime(new Timestamp(l2));
		//EhGeneralFormValRequests generalFormValRequests = new EhGeneralFormValRequests();
        generalFormValRequests.setId(id);
        generalFormValRequests.setOwnerId(ownerId);
        generalFormValRequests.setOwnerType(ownerType);
        generalFormValRequests.setNamespaceId(namespaceId);
        generalFormValRequests.setSourceId(moduleId);
        generalFormValRequests.setSourceType(moduleType);
		generalFormValRequests.setFormOriginId(formOriginId);
		generalFormValRequests.setFormVersion(formVersion);
		generalFormValRequests.setApprovalStatus((byte)0);
		generalFormValRequests.setInvestmentAdId(investmentAdId);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGeneralFormValRequestsDao dao = new EhGeneralFormValRequestsDao(context.configuration());

		dao.insert(generalFormValRequests);

		DaoHelper.publishDaoAction(DaoAction.CREATE, GeneralFormValRequest.class, null);

		return id;
	}

	@Override
	public List<GeneralFormValRequest> listGeneralFormValRequest(Integer namespaceId, Long sourceId, Long ownerId){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormValRequests.class));

		SelectQuery<EhGeneralFormValRequestsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VAL_REQUESTS);
		query.addConditions(Tables.EH_GENERAL_FORM_VAL_REQUESTS.SOURCE_ID.eq(sourceId));
		query.addConditions(Tables.EH_GENERAL_FORM_VAL_REQUESTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORM_VAL_REQUESTS.OWNER_ID.eq(ownerId));
		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormValRequest.class));
	}

	@Override
	public List<GeneralFormValRequest> listGeneralFormValRequest(){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormValRequests.class));

		SelectQuery<EhGeneralFormValRequestsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VAL_REQUESTS);
		query.addConditions(Tables.EH_GENERAL_FORM_VAL_REQUESTS.APPROVAL_STATUS.ne(GeneralFormValRequestStatus.DELETE.getCode()));
		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormValRequest.class));
	}

	@Override
	public GeneralFormValRequest getGeneralFormValRequest(Long id){
	    DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhGeneralFormValRequestsDao dao = new EhGeneralFormValRequestsDao(context.configuration());

        return ConvertHelper.convert( dao.findById(id), GeneralFormValRequest.class);
	}

	@Override
	public Long updateGeneralFormValRequestStatus(Long sourceId, Byte status){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGeneralFormValRequestsDao dao = new EhGeneralFormValRequestsDao(context.configuration());
		EhGeneralFormValRequests dto = dao.findById(sourceId);
		dto.setApprovalStatus(status);
		dao.update(dto);
		return dto.getId();
	}

	@Override
	public List<GeneralFormVal> listGeneralFormItemByIds(List<Long> ids){

		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralFormVals.class));

		SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);

		query.addConditions(Tables.EH_GENERAL_FORM_VALS.ID.in(ids));
		query.addOrderBy(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.desc());

		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormVal.class));
	}


	@Override
	public List<GeneralFormVal> listGeneralForm(){
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralFormVals.class));

		SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);

		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormVal.class));

	}


	@Override
	public List<GeneralFormVal> listGeneralForm(CrossShardListingLocator locator, Integer pageSize){
		List<GeneralFormVal> result = new ArrayList<>();
		if(locator.getShardIterator() == null){
			AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGeneralFormVals.class);
			ShardIterator shardIterator = new ShardIterator(accessSpec);
			locator.setShardIterator(shardIterator);
		}


		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {

			SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);

			if(locator.getAnchor() != null && locator.getAnchor() != 0L){
				query.addConditions(Tables.EH_GENERAL_FORM_VALS.ID.lt(locator.getAnchor()));
			}

			Map<Long ,List<GeneralFormVal>> valList = result.stream().collect(Collectors.groupingBy(GeneralFormVal::getSourceId));

			query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.in(DSL.select(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID).from(
					DSL.table(DSL.select(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID).from(Tables.EH_GENERAL_FORM_VALS).groupBy(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID)
									.orderBy(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.desc()).limit(pageSize - valList.size())).as("a")
			)));



			query.fetch().map((r) -> {
				result.add(ConvertHelper.convert(r, GeneralFormVal.class));
				return null;
			});

			if (result.size() >= pageSize) {
				locator.setAnchor(result.get(result.size() - 1).getId());
				return IterationMapReduceCallback.AfterAction.done;
			} else {
				locator.setAnchor(null);
			}
			return IterationMapReduceCallback.AfterAction.next;
		});
		return result;
	}


	@Override
	public void deleteGeneralFormFilter(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType, String userUuid, Long formOriginId, Long formVersion){
	    DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhGeneralFormFilterUserMap.class));
        EhGeneralFormFilterUserMapDao dao = new EhGeneralFormFilterUserMapDao(context.configuration());
        context.delete(Tables.EH_GENERAL_FORM_FILTER_USER_MAP)
        .where(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.NAMESPACE_ID.eq(namespaceId))
        .and(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.FORM_VERSION.eq(formVersion))
        .and(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.OWNER_ID.eq(ownerId))
        .and(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.MODULE_ID.eq(moduleId))
        .and(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.FORM_ORIGIN_ID.eq(formOriginId))
        .execute();
	}

	@Override
	public void saveGeneralFormFilter(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType, String userUuid, Long formOriginId, Long formVersion, String fieldName){

        DSLContext context = this.dbProvider.getDslContext(AccessSpec
                .readWriteWith(EhGeneralFormFilterUserMap.class));
        EhGeneralFormFilterUserMapDao dao = new EhGeneralFormFilterUserMapDao(context.configuration());


        this.dbProvider.execute((status) -> {



			Long id = this.sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhGeneralFormFilterUserMap.class));


			GeneralFormFilterUserMap generalFormFilterUserMap = new GeneralFormFilterUserMap();
			generalFormFilterUserMap.setId(id);
			generalFormFilterUserMap.setNamespaceId(namespaceId);
			generalFormFilterUserMap.setOwnerId(ownerId);
			generalFormFilterUserMap.setOwnerType(ownerType);
			generalFormFilterUserMap.setModuleId(moduleId);
			generalFormFilterUserMap.setModuleType(moduleType);
			generalFormFilterUserMap.setUserUuid(userUuid);
			generalFormFilterUserMap.setFieldName(fieldName);
			generalFormFilterUserMap.setFormOriginId(formOriginId);
			generalFormFilterUserMap.setFormVersion(formVersion);

			dao.insert(generalFormFilterUserMap);
			return null;
		});

	}

	@Override
	public List<GeneralFormFilterUserMap> listGeneralFormFilter(Integer namespaceId, Long moduleId, Long ownerId, String userUuid, Long FormOriginId, Long FormVersion){
		List<GeneralFormVal> result = new ArrayList<>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralFormFilterUserMap.class));

		SelectQuery<EhGeneralFormFilterUserMapRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_FILTER_USER_MAP);

		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.MODULE_ID.eq(moduleId));
		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.USER_UUID.eq(userUuid));
		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.FORM_ORIGIN_ID.eq(FormOriginId));
		query.addConditions(Tables.EH_GENERAL_FORM_FILTER_USER_MAP.FORM_VERSION.eq(FormVersion));

		return query.fetch().map(r -> {return ConvertHelper.convert(r, GeneralFormFilterUserMap.class);});
	}

	@Override
	public void updateGeneralFormApprovalStatusById(Long id, Byte status){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralFormValRequests.class));
		EhGeneralFormValRequestsDao dao = new EhGeneralFormValRequestsDao(context.configuration());
		EhGeneralFormValRequests obj = dao.findById(id);

		obj.setApprovalStatus(status);
		dao.update(obj);
	}



	@Override
	public Long createGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate) {
		long id = this.sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhGeneralFormPrintTemplatesRecord.class));
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralFormPrintTemplates.class));
		generalFormPrintTemplate.setId(id);
		generalFormPrintTemplate.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		generalFormPrintTemplate.setCreatorUid(UserContext.currentUserId());
		EhGeneralFormPrintTemplatesDao dao = new EhGeneralFormPrintTemplatesDao(context.configuration());
		dao.insert(generalFormPrintTemplate);
		return id;
	}

	@Override
	public void updateGeneralFormPrintTemplate(GeneralFormPrintTemplate generalFormPrintTemplate) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec
				.readWriteWith(EhGeneralFormPrintTemplates.class));
		generalFormPrintTemplate.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		generalFormPrintTemplate.setUpdateUid(UserContext.currentUserId());
		EhGeneralFormPrintTemplatesDao dao = new EhGeneralFormPrintTemplatesDao(context.configuration());
		dao.update(generalFormPrintTemplate);
	}

	@Override
	public GeneralFormPrintTemplate getGeneralFormPrintTemplateById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhGeneralFormPrintTemplatesDao dao = new EhGeneralFormPrintTemplatesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), GeneralFormPrintTemplate.class);
	}

	@Override
	public GeneralFormPrintTemplate getGeneralFormPrintTemplate(Integer namespaceId, Long ownerId, String ownerType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhGeneralFormPrintTemplatesRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_PRINT_TEMPLATES);
		query.addConditions(Tables.EH_GENERAL_FORM_PRINT_TEMPLATES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_GENERAL_FORM_PRINT_TEMPLATES.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_GENERAL_FORM_PRINT_TEMPLATES.OWNER_TYPE.eq(ownerType));
		List<GeneralFormPrintTemplate> results = query.fetch().map(r -> {
			return ConvertHelper.convert(r, GeneralFormPrintTemplate.class);
		});
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}

	@Override
	public void updateInvestmentAdApplyTransformStatus(Long id, Long transformStatus) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormValRequests.class));
		context.update(Tables.EH_GENERAL_FORM_VAL_REQUESTS)
			   .set(Tables.EH_GENERAL_FORM_VAL_REQUESTS.INTEGRAL_TAG1,transformStatus)
			   .where(Tables.EH_GENERAL_FORM_VAL_REQUESTS.ID.eq(id))
			   .execute();
	}

	@Override
	public void setInvestmentAdId(Long id, Long investmentAdId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormValRequests.class));
		context.update(Tables.EH_GENERAL_FORM_VAL_REQUESTS)
			   .set(Tables.EH_GENERAL_FORM_VAL_REQUESTS.INTEGRAL_TAG2,investmentAdId)
			   .where(Tables.EH_GENERAL_FORM_VAL_REQUESTS.ID.eq(id))
			   .execute();
		
	}

	@Override
	public List<GeneralFormVal> getGeneralFormValBySourceId(Long sourceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGeneralFormVals.class));
		SelectQuery<EhGeneralFormValsRecord> query = context.selectQuery(Tables.EH_GENERAL_FORM_VALS);
		query.addConditions(Tables.EH_GENERAL_FORM_VALS.SOURCE_ID.eq(sourceId));
		return query.fetch().map(r -> ConvertHelper.convert(r, GeneralFormVal.class));
	}
}
