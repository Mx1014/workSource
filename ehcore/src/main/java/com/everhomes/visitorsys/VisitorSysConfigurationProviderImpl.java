// @formatter:off
package com.everhomes.visitorsys;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem;
import com.everhomes.rest.visitorsys.VisitorsysBaseConfig;
import com.everhomes.rest.visitorsys.VisitorsysPassCardConfig;
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
import com.everhomes.server.schema.tables.daos.EhVisitorSysConfigurationsDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysConfigurations;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class VisitorSysConfigurationProviderImpl implements VisitorSysConfigurationProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createVisitorSysConfiguration(VisitorSysConfiguration visitorSysConfiguration) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysConfigurations.class));
		visitorSysConfiguration.setId(id);
		beforeCreateOrUpdate(visitorSysConfiguration);
		visitorSysConfiguration.setConfigVersion(System.currentTimeMillis());
		visitorSysConfiguration.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysConfiguration.setCreatorUid(UserContext.current().getUser().getId());
		visitorSysConfiguration.setOperateTime(visitorSysConfiguration.getCreateTime());
		visitorSysConfiguration.setOperatorUid(visitorSysConfiguration.getCreatorUid());
		getReadWriteDao().insert(visitorSysConfiguration);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhVisitorSysConfigurations.class, null);
	}

	private void beforeCreateOrUpdate(VisitorSysConfiguration visitorSysConfiguration) {
		if(visitorSysConfiguration.getBaseConfig()!=null) {
			visitorSysConfiguration.setConfigJson(String.valueOf(visitorSysConfiguration.getBaseConfig()));
		}else{
			visitorSysConfiguration.setConfigJson(null);
		}
		if(visitorSysConfiguration.getFormConfig()!=null) {
			visitorSysConfiguration.setConfigFormJson(String.valueOf(visitorSysConfiguration.getFormConfig()));
		}else {
			visitorSysConfiguration.setConfigFormJson(null);
		}
		if(visitorSysConfiguration.getPassCardConfig()!=null) {
			visitorSysConfiguration.setConfigPassCardJson(String.valueOf(visitorSysConfiguration.getPassCardConfig()));
		}else {
			visitorSysConfiguration.setConfigPassCardJson(null);
		}

	}

	@Override
	public void updateVisitorSysConfiguration(VisitorSysConfiguration visitorSysConfiguration) {
		assert (visitorSysConfiguration.getId() != null);
		beforeCreateOrUpdate(visitorSysConfiguration);
		visitorSysConfiguration.setConfigVersion(System.currentTimeMillis());
		visitorSysConfiguration.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		visitorSysConfiguration.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(visitorSysConfiguration);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVisitorSysConfigurations.class, visitorSysConfiguration.getId());
	}

	@Override
	public VisitorSysConfiguration findVisitorSysConfigurationById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), VisitorSysConfiguration.class);
	}
	
	@Override
	public List<VisitorSysConfiguration> listVisitorSysConfiguration() {
		return getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CONFIGURATIONS)
				.orderBy(Tables.EH_VISITOR_SYS_CONFIGURATIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysConfiguration.class));
	}

	@Override
	public VisitorSysConfiguration findVisitorSysConfigurationByOwner(Integer namespaceId, String ownerType, Long ownerId) {
		List<VisitorSysConfiguration> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CONFIGURATIONS)
				.where(Tables.EH_VISITOR_SYS_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_VISITOR_SYS_CONFIGURATIONS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_VISITOR_SYS_CONFIGURATIONS.OWNER_ID.eq(ownerId))
				.orderBy(Tables.EH_VISITOR_SYS_CONFIGURATIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysConfiguration.class));
		if(list==null||list.size()==0){
			return null;
		}
		VisitorSysConfiguration configuration = list.get(0);
		afterQuery(configuration);
		return configuration;
	}

	@Override
	public VisitorSysConfiguration findVisitorSysConfigurationByOwnerToken(String ownerToken) {
		List<VisitorSysConfiguration> list = getReadOnlyContext().select().from(Tables.EH_VISITOR_SYS_CONFIGURATIONS)
				.where(Tables.EH_VISITOR_SYS_CONFIGURATIONS.OWNER_TOKEN.eq(ownerToken))
				.orderBy(Tables.EH_VISITOR_SYS_CONFIGURATIONS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, VisitorSysConfiguration.class));
		if(list==null||list.size()==0){
			return null;
		}
		VisitorSysConfiguration configuration = list.get(0);
		afterQuery(configuration);
		return configuration;
	}

	private void afterQuery(VisitorSysConfiguration configuration) {
		if(configuration.getConfigJson()!=null) {
			configuration.setBaseConfig(JSONObject.parseObject(configuration.getConfigJson(), VisitorsysBaseConfig.class));
		}
		if(configuration.getConfigFormJson()!=null) {
			List<VisitorsysApprovalFormItem> forms = JSONObject.parseObject(configuration.getConfigFormJson(), new TypeReference<List<VisitorsysApprovalFormItem>>(){});
			for (VisitorsysApprovalFormItem form : forms){
				if("invalidTime".equals(form.getFieldName())){
					forms.remove(form);
					break;
				}
			}
			configuration.setFormConfig(forms);
		}
		if(configuration.getConfigPassCardJson()!=null) {
			configuration.setPassCardConfig(JSONObject.parseObject(configuration.getConfigPassCardJson(), VisitorsysPassCardConfig.class));
		}
	}

	private EhVisitorSysConfigurationsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhVisitorSysConfigurationsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhVisitorSysConfigurationsDao getDao(DSLContext context) {
		return new EhVisitorSysConfigurationsDao(context.configuration());
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
