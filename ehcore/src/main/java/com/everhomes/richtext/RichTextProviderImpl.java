package com.everhomes.richtext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRichTextsDao;
import com.everhomes.server.schema.tables.pojos.EhRichTexts;
import com.everhomes.server.schema.tables.records.EhRichTextsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.mysql.jdbc.StringUtils;

public class RichTextProviderImpl implements RichTextProvider {
	
	@Autowired
	private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

	@Override
	public RichText findRichText(String ownerType, Long ownerId,
			String resourceType, Integer namespaceId) {
		
		List<RichText> rt = new ArrayList<RichText>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhRichTextsRecord> query = context.selectQuery(Tables.EH_RICH_TEXTS);
 
        if(!StringUtils.isNullOrEmpty(ownerType))
        	query.addConditions(Tables.EH_RICH_TEXTS.OWNER_TYPE.eq(ownerType));
        
        if(null != ownerId)
        	query.addConditions(Tables.EH_RICH_TEXTS.OWNER_ID.eq(ownerId));
        
        if(!StringUtils.isNullOrEmpty(resourceType))
        	query.addConditions(Tables.EH_RICH_TEXTS.RESOURCE_TYPE.eq(resourceType));
        
        if(null != namespaceId)
        	query.addConditions(Tables.EH_RICH_TEXTS.NAMESPACE_ID.eq(namespaceId));
        
        query.fetch().map((r) -> {
        	rt.add(ConvertHelper.convert(r, RichText.class));
            return null;
        });
        
        if(rt == null || rt.size() == 0) {
        	return null;
        }
        
        return rt.get(0);
	}

	@Override
	public RichText getDefaultRichText(String resourceType) {
		List<RichText> rt = new ArrayList<RichText>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        SelectQuery<EhRichTextsRecord> query = context.selectQuery(Tables.EH_RICH_TEXTS);
        if(!StringUtils.isNullOrEmpty(resourceType))
        	query.addConditions(Tables.EH_RICH_TEXTS.RESOURCE_TYPE.eq(resourceType));
        
        query.addConditions(Tables.EH_RICH_TEXTS.NAMESPACE_ID.eq(0));
        query.addConditions(Tables.EH_RICH_TEXTS.OWNER_TYPE.eq("DEFAULT"));
        query.addConditions(Tables.EH_RICH_TEXTS.OWNER_ID.eq(0L));
        
        query.fetch().map((r) -> {
        	rt.add(ConvertHelper.convert(r, RichText.class));
            return null;
        });
        
        if(rt == null || rt.size() == 0) {
        	return null;
        }
        
        return rt.get(0);
	}

	@Override
	public void createRichText(RichText richText) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRichTexts.class));
		richText.setId(id);
		richText.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhRichTextsDao dao = new EhRichTextsDao(context.configuration());
        dao.insert(richText);
	}

	@Override
	public void updateRichText(RichText richText) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRichTextsDao dao = new EhRichTextsDao(context.configuration());
        dao.update(richText);
	}

}
