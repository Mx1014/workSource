package com.everhomes.poll;

import com.everhomes.rest.poll.PollDTO;
import com.everhomes.rest.poll.PollPostCommand;
import com.everhomes.rest.poll.PollShowResultCommand;
import com.everhomes.rest.poll.PollShowResultResponse;
import com.everhomes.rest.poll.PollVoteCommand;

public interface PollService {
    void createPoll(PollPostCommand cmd, Long postId);

    PollDTO createVote(PollVoteCommand cmd);

    PollShowResultResponse showResult(PollShowResultCommand cmd);
    
    PollShowResultResponse showResult(Long postId);
    
    PollDTO showResultBrief(Long postId);
}
