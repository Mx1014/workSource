<#if entityName?ends_with("s")>
<#assign MyEntityName=entityName?substring(0,entityName?length-1)>
<#else>
<#assign MyEntityName=entityName>
</#if>
<#if MyEntityName?ends_with("ie")>
<#assign MyEntityName=MyEntityName?substring(0,MyEntityName?length-2)+"y">
</#if>
<#if MyEntityName?ends_with("sse")>
<#assign MyEntityName=MyEntityName?substring(0,MyEntityName?length-1)>
</#if>
<#assign MyEntityName=MyEntityName?cap_first>
<#assign entityName=entityName?cap_first>
// @formatter:off
package ${packageName};

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
import com.everhomes.server.schema.tables.daos.Eh${entityName}Dao;
import com.everhomes.server.schema.tables.pojos.Eh${entityName};
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ${MyEntityName}ProviderImpl implements ${MyEntityName}Provider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void create${MyEntityName}(${MyEntityName} ${MyEntityName?uncap_first}) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Eh${entityName}.class));
		${MyEntityName?uncap_first}.setId(id);
		${MyEntityName?uncap_first}.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		${MyEntityName?uncap_first}.setCreatorUid(UserContext.current().getUser().getId());
		${MyEntityName?uncap_first}.setUpdateTime(${MyEntityName?uncap_first}.getCreateTime());
		${MyEntityName?uncap_first}.setOperatorUid(${MyEntityName?uncap_first}.getCreatorUid());
		getReadWriteDao().insert(${MyEntityName?uncap_first});
		DaoHelper.publishDaoAction(DaoAction.CREATE, Eh${entityName}.class, null);
	}

	@Override
	public void update${MyEntityName}(${MyEntityName} ${MyEntityName?uncap_first}) {
		assert (${MyEntityName?uncap_first}.getId() != null);
		${MyEntityName?uncap_first}.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		${MyEntityName?uncap_first}.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(${MyEntityName?uncap_first});
		DaoHelper.publishDaoAction(DaoAction.MODIFY, Eh${entityName}.class, ${MyEntityName?uncap_first}.getId());
	}

	@Override
	public ${MyEntityName} find${MyEntityName}ById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ${MyEntityName}.class);
	}
	
	@Override
	public List<${MyEntityName}> list${MyEntityName}() {
		return getReadOnlyContext().select().from(Tables.EH_${underlineCapEntityName})
				.orderBy(Tables.EH_${underlineCapEntityName}.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ${MyEntityName}.class));
	}
	
	private Eh${entityName}Dao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private Eh${entityName}Dao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private Eh${entityName}Dao getDao(DSLContext context) {
		return new Eh${entityName}Dao(context.configuration());
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
