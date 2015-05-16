// @formatter:off
package com.everhomes.poll;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.poll.PollService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StatusChecker;

@Component
public class PollServiceImpl implements PollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollServiceImpl.class);
    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private ForumService forumService;

    @Autowired
    private PollProvider pollProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createPoll(PollPostCommand cmd,Long postId) {
        User user=UserContext.current().getUser();
     
        Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
        dbProvider.execute((status)->{
            Poll poll=new Poll();
            poll.setPostId(postId);
            poll.setAnonymousFlag((byte)cmd.getAnonymousFlag().intValue());
            poll.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            //family
            if(family!=null)
                poll.setCreatorFamilyId(family.getId());
            poll.setCreatorUid(user.getId());
            poll.setPollCount(0);
            poll.setPostId(postId);
            poll.setStartTime(new Timestamp(cmd.getStartTime()));
            poll.setEndTime(new Timestamp(cmd.getStopTime()));
            poll.setStatus((byte)1);
            pollProvider.createPoll(poll);
            List<PollItem> pollItems = cmd.getItemList().stream().map(r->{
                PollItem item=ConvertHelper.convert(r, PollItem.class);
                item.setPollId(poll.getId());
                item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                item.setId(r.getItemId());
                return item;
            }).collect(Collectors.toList());
            pollProvider.createPollItem(pollItems,poll.getId());
            return status;
        });
    }

    @Override
    public PollDTO createVote(PollVoteCommand cmd) {
        User user = UserContext.current().getUser();
        Poll poll = pollProvider.findPollById(cmd.getPollId());
        if (poll == null) {
            LOGGER.error("handle polling failed.the polling does not exsit.id={}", cmd.getPollId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_ID, "invalid poll id.Id="+cmd.getPollId());
        }
        Long postId = poll.getPostId();
        Post post = forumProvider.findPostById(postId);
        if (post == null) {
            LOGGER.error("handle poll error.postId={}",postId);
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_POST, "invalid poll post.postId="+postId);
        }
        List<PollItem> result = pollProvider.listPollItemByPollId(cmd.getPollId());
        if (CollectionUtils.isEmpty(result)) {
            LOGGER.error("cannot find poll item.pollId={}", cmd.getPollId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_ITEMS, "poll items cannot be empty");
        }
         PollItem matchResult = result.stream().filter(r->result.contains(r)).map(m->ConvertHelper.convert(m, PollItem.class)).findFirst().orElse(null);
        if (matchResult==null) {
            LOGGER.error("cannot find any match item.{}", cmd.getItemIds());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_IMTE, "invalid poll item.item="+cmd.getItemIds());
        }
        PollVote voteResult = pollProvider.findPollVoteByUidAndPollId(user.getId(), cmd.getPollId());
        if (voteResult!=null) {
            LOGGER.error("can not vote again.pollId={}", cmd.getPollId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_DUPLICATE_VOTE, "cannot vote again");
        }
        PollVote pollVote = new PollVote();
        pollVote.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        pollVote.setItemId(matchResult.getId());
        pollVote.setPollId(poll.getId());
        pollVote.setVoterUid(user.getId());
        //if addresses is not empty
        if(user.getAddressId()!=null)
                //ensure all address is ok
            pollVote.setVoterFamilyId(familyProvider.findFamilyByAddressId(user.getAddressId()).getId());
        pollProvider.createPollVote(pollVote);
        matchResult.setVoteCount(matchResult.getVoteCount()+1);
        //update poll item
        pollProvider.updatePollItem(matchResult, cmd.getPollId());
        //update poll
        poll.setPollCount(poll.getPollCount()+1);
        pollProvider.updatePoll(poll);
        PollDTO dto=ConvertHelper.convert(poll, PollDTO.class);
        dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        dto.setProcessStatus(getStatus(poll).getCode());
        return dto;
    }
    
   private ProcessStatus getStatus(Poll poll){
      return StatusChecker.getProcessStatus(poll.getStartTimeMs(), poll.getStartTimeMs());
   }

    @Override
    public PollShowResultResponse showResult(PollShowResultCommand cmd) {
        Poll poll = pollProvider.findPollById(cmd.getPollId());
        if (poll == null) {
            LOGGER.error("handle polling failed.the polling does not exsit.id={}", cmd.getPollId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_ID, "invalid poll id.Id="+cmd.getPollId());
        }
        Long postId = poll.getPostId();
        Post post = forumProvider.findPostById(postId);
        if (post == null) {
            LOGGER.error("handle post error.");
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_POST, "invalid poll post.postId="+postId);
        }
        List<PollItem> result = pollProvider.listPollItemByPollId(cmd.getPollId());
        PollShowResultResponse response = new PollShowResultResponse();
        response.setItems(result.stream().map(r->ConvertHelper.convert(r, PollItemDTO.class)).collect(Collectors.toList()));
        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            //skip
        }
        User user=UserContext.current().getUser();
        PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        dto.setStartTime(poll.getStartTime().getTime());
        dto.setStopTime(poll.getEndTime().getTime());
        dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        if(votes==null){
            dto.setPollVoterStatus(VotedStatus.UNVOTED.getCode());
        }
        dto.setProcessStatus(getStatus(poll).getCode());
        response.setPoll(dto);
        return response;
    }

    @Override
    public PollShowResultResponse showResult(Long postId) {
        Poll poll=pollProvider.findByPostId(postId);
        List<PollItem> result = pollProvider.listPollItemByPollId(poll.getId());
        PollShowResultResponse response = new PollShowResultResponse();
        response.setItems(result.stream().map(r->ConvertHelper.convert(r, PollItemDTO.class)).collect(Collectors.toList()));
        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            //skip
        }
        User user=UserContext.current().getUser();
        PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        dto.setStartTime(poll.getStartTime().getTime());
        dto.setStopTime(poll.getEndTime().getTime());
        dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        if(votes==null){
            dto.setPollVoterStatus(VotedStatus.UNVOTED.getCode());
        }
        dto.setProcessStatus(getStatus(poll).getCode());
        response.setPoll(dto);
        return response;
    }
    
    
}
