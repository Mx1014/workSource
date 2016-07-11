package com.everhomes.poll;

import java.util.List;

public interface PollProvider {
    void createPoll(Poll poll);

    void createPollItem(List<PollItem> pollItems);

    void createPollVote(PollVote pollVote);

    void updatePollItem(PollItem pollItem);

    List<PollItem> listPollItemByPollId(Long pollId);

    List<PollVote> listPollVoteByPollId(Long pollId);

    Poll findPollById(Long pollId);

    PollVote findPollVoteByUidAndPollId(Long uid,Long pollId);

    void updatePoll(Poll poll);

    Poll findByPostId(Long postId);
    
    Poll findPollByUuid(String uuid);
    
    PollItem findPollItemById(Long id);

}
