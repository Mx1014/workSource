// @formatter:off
package com.everhomes.poll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
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
    

    @Override
    public void createPoll(Poll poll) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolls.class, poll.getId()));
        EhPollsDao dao = new EhPollsDao(context.configuration());
        dao.insert(poll);

    }

    @Override
    public void createPollItem(List<PollItem> pollItems) {
        pollItems.forEach(item->{
            Long id=shardingProvider.allocShardableContentId(EhPollItems.class).second();
            item.setId(id);
        });
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(PollItem.class));
        EhPollItemsDao dao = new EhPollItemsDao(context.configuration());
        dao.insert(pollItems.stream().map(r->ConvertHelper.convert(r, EhPollItems.class)).collect(Collectors.toList()));
    }

    @Override
    public void createPollVote(PollVote pollVote) {
        Long id = shardingProvider.allocShardableContentId(EhPollVotes.class).second();
        pollVote.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(PollItem.class, id));
        EhPollVotesDao dao = new EhPollVotesDao(context.configuration());
        dao.insert(pollVote);
    }

    @Override
    public List<PollItem> listPollItemByPollId(Long pollId) {
        List<PollItem> values=new ArrayList<PollItem>();
       dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPollItems.class), null, (context,object)->{
           EhPollItemsDao dao=new EhPollItemsDao(context.configuration());
           List<PollItem> result = dao.fetchByPollId(pollId).stream().map(r->ConvertHelper.convert(r, PollItem.class)).collect(Collectors.toList());
           if(CollectionUtils.isNotEmpty(result)){
               values.addAll(result);
           }
           return true;
       });
       return values;
    }

    @Override
    public List<PollVote> listPollVoteByPollId(Long pollId) {
        List<PollVote> pollVotes=new ArrayList<PollVote>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPollVotes.class), null, (context,object)->{
            EhPollVotesDao dao=new EhPollVotesDao(context.configuration());
            dao.fetchByPollId(pollId).forEach(vote->{
                pollVotes.add(ConvertHelper.convert(vote,PollVote.class));
            });
            return true;
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
        PollVote[] pollVote = new PollVote[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhPollVotes.class),pollVote,
                (context, object) -> {
                    EhPollVotesRecord result = (EhPollVotesRecord)context.select().from(Tables.EH_POLL_VOTES).where(Tables.EH_POLL_VOTES.POLL_ID.eq(pollId))
                            .and(Tables.EH_POLL_VOTES.VOTER_UID.eq(uid)).fetchAny();
                    if(result!=null){
                        pollVote[0]=ConvertHelper.convert(result, PollVote.class);
                        return false;
                    }
                    return true;
                });
        return pollVote[0];
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
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhPollItems.class,pollItem.getId()));
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
}
