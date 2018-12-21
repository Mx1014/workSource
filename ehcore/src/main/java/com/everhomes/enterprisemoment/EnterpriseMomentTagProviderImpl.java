// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.enterprisemoment.MomentTagDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentTagsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentTags;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentTagsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EnterpriseMomentTagProviderImpl implements EnterpriseMomentTagProvider {
	private static final String LIST_ENTERPRISE_MOMENT_TAG = "listEnterpriseMomentTag";
	private static final com.everhomes.server.schema.tables.EhEnterpriseMomentTags tagTable = Tables.EH_ENTERPRISE_MOMENT_TAGS;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = LIST_ENTERPRISE_MOMENT_TAG, key = "{#enterpriseMomentTag.namespaceId, #enterpriseMomentTag.organizationId}")
	public void createEnterpriseMomentTag(EnterpriseMomentTag enterpriseMomentTag) {
		getReadWriteDao().insert(enterpriseMomentTag);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMomentTags.class, null);
	}

	@Override
	@CacheEvict(value = LIST_ENTERPRISE_MOMENT_TAG, key = "{#namespaceId, #organizationId}")
	public List<EnterpriseMomentTag> batchCreateEnterpriseMomentTag(Integer namespaceId, Long creatorUid, Long organizationId, List<MomentTagDTO> tagDTOS) {
		if(tagDTOS == null || tagDTOS.isEmpty()){
			return new ArrayList<>(0);
		}
		List<EnterpriseMomentTag> enterpriseMomentTags = new ArrayList<>();
		for(MomentTagDTO tag : tagDTOS){
			EnterpriseMomentTag newEnterpriseMomentTag = buildEnterpriseMomentTag(namespaceId, organizationId, tag.getName(), creatorUid);
			//为了满足根据标签名字筛选，但为了性能，有想用标签id做索引，做了如下的特殊操作
			//修改创建tag时如果不能找到同名的tag，创建新tag，否则把同名tagId所在的那行数据更新为newEnterpriseMomentTag中的数据
			EnterpriseMomentTag sameNameEnterpriseMomentTag = findEnterpriseMomentTagIncludeDeletedByName(namespaceId, organizationId, tag.getName());
			if(sameNameEnterpriseMomentTag == null){
				createEnterpriseMomentTag(newEnterpriseMomentTag);
				enterpriseMomentTags.add(newEnterpriseMomentTag);
			} else{
				newEnterpriseMomentTag.setId(sameNameEnterpriseMomentTag.getId());
				updateEnterpriseMomentTag(sameNameEnterpriseMomentTag);
				enterpriseMomentTags.add(sameNameEnterpriseMomentTag);
			}
		}
		return enterpriseMomentTags;
	}

	private EnterpriseMomentTag buildEnterpriseMomentTag(Integer namespaceId, Long organizationId, String name, Long creatorUid){
		EnterpriseMomentTag enterpriseMomentTag = new EnterpriseMomentTag();
		enterpriseMomentTag.setId(sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentTags.class)));
		enterpriseMomentTag.setNamespaceId(namespaceId);
		enterpriseMomentTag.setOrganizationId(organizationId);
		enterpriseMomentTag.setName(name);
		enterpriseMomentTag.setCreatorUid(creatorUid);
		enterpriseMomentTag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		return enterpriseMomentTag;
	}

	@Override
	@CacheEvict(value = LIST_ENTERPRISE_MOMENT_TAG, key = "{#namespaceId, #organizationId}")
	public void batchUpdateEnterpriseMomentTag(Integer namespaceId, Long operatorUid, Long organizationId, List<MomentTagDTO> tagDTOS) {
		tagDTOS.forEach(tag -> {
			EnterpriseMomentTag needToUpdateEnterpriseMomentTag = findEnterpriseMomentTagById(tag.getId());
			needToUpdateEnterpriseMomentTag.setName(tag.getName());
			needToUpdateEnterpriseMomentTag.setId(sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentTags.class)));
			needToUpdateEnterpriseMomentTag.setOperatorUid(operatorUid);
			needToUpdateEnterpriseMomentTag.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			//为了满足根据标签名字筛选，但为了性能，又想用标签id做索引，做了如下的特殊操作
			//修改名字时把当前tag标记为删除，如果不能找到同名的tag，把创建新名字的tag，否则把同名tagId所在的那行数据更新为needToUpdateEnterpriseMomentTag中的数据
			EnterpriseMomentTag sameNameEnterpriseMomentTag = findEnterpriseMomentTagIncludeDeletedByName(namespaceId, organizationId, tag.getName());
			batchDeleteEnterpriseMomentTag(namespaceId, operatorUid, organizationId, Arrays.asList(tag));
			if(sameNameEnterpriseMomentTag == null){
				createEnterpriseMomentTag(needToUpdateEnterpriseMomentTag);
			} else {
				needToUpdateEnterpriseMomentTag.setId(sameNameEnterpriseMomentTag.getId());
				updateEnterpriseMomentTag(needToUpdateEnterpriseMomentTag);
			}
		});
	}

	private EnterpriseMomentTag findEnterpriseMomentTagIncludeDeletedByName(Integer namespaceId, Long organizationId, String name){
		Record record = getReadOnlyContext().select().from(tagTable)
				.where(tagTable.NAMESPACE_ID.eq(namespaceId))
				.and(tagTable.ORGANIZATION_ID.eq(organizationId))
				.and(tagTable.NAME.eq(name))
				.fetchOne();
		if(record == null){
			return null;
		}
		return record.map(r -> ConvertHelper.convert(r, EnterpriseMomentTag.class));
	}

	private void updateEnterpriseMomentTag(EnterpriseMomentTag tag){
		getReadWriteDao().update(tag);
	}

	@Override
	@CacheEvict(value = LIST_ENTERPRISE_MOMENT_TAG, key = "{#namespaceId, #organizationId}")
	public void batchDeleteEnterpriseMomentTag(Integer namespaceId, Long deleteUid, Long organizationId, List<MomentTagDTO> tagDTOS) {
		if(tagDTOS == null || tagDTOS.isEmpty()){
			return;
		}
		UpdateQuery<EhEnterpriseMomentTagsRecord> updateQuery = getReadWriteContext().updateQuery(tagTable);
		updateQuery.addValue(tagTable.DELETE_FLAG, (byte)1);
		updateQuery.addValue(tagTable.DELETE_UID, deleteUid);
		updateQuery.addValue(tagTable.DELETE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		ArrayList<Condition> conditions = new ArrayList<>();
		Long[] ids = tagDTOS.stream().map(tag -> tag.getId()).toArray(Long[]::new);
		conditions.add(tagTable.ID.in(ids));
		conditions.add(tagTable.NAMESPACE_ID.eq(namespaceId));
		conditions.add(tagTable.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(conditions);
		updateQuery.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMomentTags.class, null);
	}

	@Override
	public EnterpriseMomentTag findEnterpriseMomentTagById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), EnterpriseMomentTag.class);
	}
	
	@Override
	@Cacheable(value = LIST_ENTERPRISE_MOMENT_TAG, key = "{#namespaceId, #organizationId}", unless = "#result.size() == 0")
	public List<EnterpriseMomentTag> listEnterpriseMomentTag(Integer namespaceId, Long organizationId) {
		return getReadOnlyContext().select().from(tagTable)
				.where(tagTable.NAMESPACE_ID.eq(namespaceId))
				.and(tagTable.ORGANIZATION_ID.eq(organizationId))
				.and(tagTable.DELETE_FLAG.eq((byte)0))
				.orderBy(tagTable.CREATE_TIME)
				.fetch().map(r -> ConvertHelper.convert(r, EnterpriseMomentTag.class));
	}

	@Override
	public boolean isNeedInitTag(Integer namespaceId, Long organizationId) {
		return getReadOnlyContext().select(tagTable.ID).from(tagTable)
				.where(tagTable.NAMESPACE_ID.eq(namespaceId))
				.and(tagTable.ORGANIZATION_ID.eq(organizationId))
				.fetch().size() <= 0;
	}

	private EhEnterpriseMomentTagsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentTagsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentTagsDao getDao(DSLContext context) {
		return new EhEnterpriseMomentTagsDao(context.configuration());
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
