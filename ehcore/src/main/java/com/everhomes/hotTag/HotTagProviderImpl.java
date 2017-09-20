package com.everhomes.hotTag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.RecordHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.activity.Activity;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.quality.QualityInspectionStandards;
import com.everhomes.quality.QualityInspectionTasks;
import com.everhomes.rest.hotTag.HotTagStatus;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.rest.quality.QualityStandardStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhActivitiesDao;
import com.everhomes.server.schema.tables.daos.EhHotTagsDao;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhHotTags;
import com.everhomes.server.schema.tables.records.EhHotTagsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionStandardsRecord;
import com.everhomes.server.schema.tables.records.EhQualityInspectionTasksRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import scala.Int;

@Component
public class HotTagProviderImpl implements HotTagProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(HotTagProviderImpl.class);
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
	private DbProvider dbProvider;

	@Override
	public List<TagDTO> listHotTag(Integer nameSpaceId, Long categoryId, String serviceType, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhHotTagsRecord> query = context.selectQuery(Tables.EH_HOT_TAGS);
		query.addConditions(Tables.EH_HOT_TAGS.SERVICE_TYPE.eq(serviceType));
		query.addConditions(Tables.EH_HOT_TAGS.NAMESPACE_ID.eq(nameSpaceId));
		if(categoryId != null){
			query.addConditions(Tables.EH_HOT_TAGS.CATEGORY_ID.eq(categoryId));
		}
		query.addConditions(Tables.EH_HOT_TAGS.STATUS.eq(HotTagStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_HOT_TAGS.DEFAULT_ORDER.desc());

		if(pageSize != null){
			query.addLimit(pageSize);
		}

		List<TagDTO> result = new ArrayList<TagDTO>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, TagDTO.class));
			return null;
		});
		
		return result;
	}

	@Override
	public List<TagDTO> listDistinctAllHotTag(String serviceType, Integer pageSize, Integer pageOffset) {
		List<TagDTO> result = new ArrayList<TagDTO>();

		if(pageOffset == null){
			pageOffset = 1;
		}
		Integer offset =  (int) ((pageOffset - 1 ) * (pageSize-1));

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		context.selectDistinct(Tables.EH_HOT_TAGS.NAME)
				.from(Tables.EH_HOT_TAGS)
				.where(Tables.EH_HOT_TAGS.SERVICE_TYPE.eq(serviceType)
						.and(Tables.EH_HOT_TAGS.STATUS.eq(HotTagStatus.ACTIVE.getCode())))
				.limit(offset, pageSize)
				.fetch().map(r ->{
					result.add(RecordHelper.convert(r, TagDTO.class));
					return null;
				});

		return result;
	}

	@Override
	public void updateHotTag(HotTags tag) {
		assert(tag.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhHotTags.class, tag.getId()));
        EhHotTagsDao dao = new EhHotTagsDao(context.configuration());
        dao.update(tag);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhHotTags.class, tag.getId());		
	}

	@Override
	public void createHotTag(HotTags tag) {
		
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhHotTags.class));
		
		tag.setId(id);
		tag.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		tag.setStatus(HotTagStatus.ACTIVE.getCode());
		
		LOGGER.info("createHotTag: " + tag);
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhHotTags.class, id));
        EhHotTagsDao dao = new EhHotTagsDao(context.configuration());
        dao.insert(tag);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhHotTags.class, null);
	}

	@Override
	public HotTags findById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhHotTags.class, id));
		EhHotTagsDao dao = new EhHotTagsDao(context.configuration());
		EhHotTags result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, HotTags.class);
	}

	@Override
	public HotTags findByName(Integer namespaceId, Long categoryId, String serviceType, String name) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhHotTagsRecord> query = context.selectQuery(Tables.EH_HOT_TAGS);
		query.addConditions(Tables.EH_HOT_TAGS.NAME.eq(name));
		query.addConditions(Tables.EH_HOT_TAGS.SERVICE_TYPE.eq(serviceType));
		query.addConditions(Tables.EH_HOT_TAGS.NAMESPACE_ID.eq(namespaceId));

		if(categoryId != null){
			query.addConditions(Tables.EH_HOT_TAGS.CATEGORY_ID.eq(categoryId));
		}

		List<HotTags> result = new ArrayList<HotTags>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, HotTags.class));
			return null;
		});
		if(result.size()==0)
			return null;
		
		return result.get(0);
	}

}
