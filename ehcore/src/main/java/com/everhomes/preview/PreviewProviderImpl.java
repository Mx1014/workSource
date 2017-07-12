package com.everhomes.preview;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPollVotesDao;
import com.everhomes.server.schema.tables.daos.EhPreviewsDao;
import com.everhomes.server.schema.tables.pojos.EhPollVotes;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.server.schema.tables.pojos.EhPreviews;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;

@Component
public class PreviewProviderImpl implements PreviewProvider{
	@Autowired
    private DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    
	@Override
	public Preview addPreview(Preview preview) {
		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPreviews.class));
		preview.setId(id);
	    DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
	    EhPreviewsDao dao = new EhPreviewsDao(context.configuration());
	    dao.insert(preview);
	    
	    DaoHelper.publishDaoAction(DaoAction.CREATE, EhPreviews.class, null); 
	    return preview;
	}

	@Override
	public Preview getPreview(Long previewId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhPreviewsDao dao = new EhPreviewsDao(context.configuration());
	    return ConvertHelper.convert(dao.findById(previewId), Preview.class);
	}


}
