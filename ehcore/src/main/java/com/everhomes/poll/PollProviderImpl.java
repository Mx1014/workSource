// @formatter:off
package com.everhomes.poll;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.everhomes.server.schema.tables.records.EhPollItemsRecord;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPollItemsDao;
import com.everhomes.server.schema.tables.daos.EhPollVotesDao;
import com.everhomes.server.schema.tables.daos.EhPollsDao;
import com.everhomes.server.schema.tables.pojos.EhPollItems;
import com.everhomes.server.schema.tables.pojos.EhPollVotes;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.server.schema.tables.records.EhPollVotesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
//poll data small enough ,so remove all cache
@Component
public class PollProviderImpl implements PollProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    

    @Override
    public void createPoll(Poll poll) {
        if(poll.getUuid() == null) {
            poll.setUuid(UUID.randomUUID().toString());
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, poll.getId()));
        EhPollsDao dao = new EhPollsDao(context.configuration());
        dao.insert(poll);

    }

    @Override
    public void createPollItem(List<PollItem> pollItems) {
        pollItems.forEach(item->{
            Long id=sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPollItems.class));
            item.setId(id);
        });
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class));
        EhPollItemsDao dao = new EhPollItemsDao(context.configuration());
        dao.insert(pollItems.stream().map(r->ConvertHelper.convert(r, EhPollItems.class)).collect(Collectors.toList()));
    }

    @Override
    public void createPollVote(PollVote pollVote) {
        Long id=sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPollVotes.class));
        pollVote.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, pollVote.getPollId()));
        EhPollVotesDao dao = new EhPollVotesDao(context.configuration());
        dao.insert(pollVote);
    }

    @Override
    public List<PollItem> listPollItemByPollId(Long pollId) {
        //List<PollItem> values=new ArrayList<PollItem>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, pollId));
//        EhPollItemsDao dao=new EhPollItemsDao(context.configuration());
//        List<PollItem> result = dao.fetchByPollId(pollId).stream().map(r->ConvertHelper.convert(r, PollItem.class)).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(result)){
//            values.addAll(result);
//        }

        //增加排序功能按照resourceId升序排列  edit by yanjun 20170803
        SelectQuery<EhPollItemsRecord> query = context.selectQuery(Tables.EH_POLL_ITEMS);
        query.addConditions(Tables.EH_POLL_ITEMS.POLL_ID.eq(pollId));
        query.addOrderBy(Tables.EH_POLL_ITEMS.RESOURCE_ID.asc());

        return query.fetch().stream().map(r->ConvertHelper.convert(r, PollItem.class)).collect(Collectors.toList());
    }

    @Override
    public List<PollVote> listPollVoteByPollId(Long pollId) {
        List<PollVote> pollVotes=new ArrayList<PollVote>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, pollId));
        EhPollVotesDao dao=new EhPollVotesDao(context.configuration());
        dao.fetchByPollId(pollId).forEach(vote->{
            pollVotes.add(ConvertHelper.convert(vote,PollVote.class));
        });
        return pollVotes;
    }

    @Override
    public Poll findPollById(Long pollId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPolls.class, pollId));
        EhPollsDao dao = new EhPollsDao(context.configuration());
        EhPolls poll = dao.findById(pollId);
        if (poll == null) {
            return null;
        }
        
        return ConvertHelper.convert(poll, Poll.class);
    }

    @Override
    public PollVote findPollVoteByUidAndPollId(Long uid, Long pollId) {
        PollVote pollVote =null;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, pollId));
        EhPollVotesRecord result = (EhPollVotesRecord)context.select().from(Tables.EH_POLL_VOTES).where(Tables.EH_POLL_VOTES.POLL_ID.eq(pollId))
                .and(Tables.EH_POLL_VOTES.VOTER_UID.eq(uid)).orderBy(Tables.EH_POLL_VOTES.CREATE_TIME.desc()).fetchAny();
        if(result!=null){
            pollVote=ConvertHelper.convert(result, PollVote.class);
        }
        return pollVote;
    }

    @Override
    public void updatePoll(Poll poll) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPolls.class, poll.getId()));
        EhPollsDao dao = new EhPollsDao(context.configuration());
        dao.update(poll);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPolls.class, poll.getId());
    }

    @Override
    public Poll findByPostId(Long postId) {
        Poll[] polls=new Poll[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPolls.class), null, (context,object)->{
            EhPollsDao dao=new EhPollsDao(context.configuration());
            dao.fetchByPostId(postId).forEach(r->{
                polls[0]=ConvertHelper.convert(r,Poll.class);
            });
            if(polls[0]!=null) return false;
            return true;
        });
        return polls[0];
    }

    @Override
    public void updatePollItem(PollItem pollItem) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class,pollItem.getPollId()));
        EhPollItemsDao dao=new EhPollItemsDao(cxt.configuration());
        dao.update(pollItem);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPollItems.class, pollItem.getId());
    }

    @Override
    public Poll findPollByUuid(String uuid) {
        Poll[] polls=new Poll[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPolls.class), null, (context,obj)->{
            return true;
        });
        return polls[0];
    }

	@Override
	public PollItem findPollItemById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPollItems.class, id));
        EhPollItemsDao dao=new EhPollItemsDao(context.configuration());
        PollItem result = ConvertHelper.convert(dao.fetchOneById(id), PollItem.class);
		
		return result;
	}
}
