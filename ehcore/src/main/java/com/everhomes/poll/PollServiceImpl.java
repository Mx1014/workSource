// @formatter:off
package com.everhomes.poll;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.ForumProvider;
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
    private PollProvider pollProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createPoll(PollPostCommand cmd,Long postId) {
        User user=UserContext.current().getUser();
        Family[] families = new Family[1];
        if(user.getAddressId()!=null)
            families[0] = familyProvider.findFamilyByAddressId(user.getAddressId());
        dbProvider.execute(status->{
            Poll poll=new Poll();
            poll.setPostId(postId);
            if(cmd.getAnonymousFlag()!=null)
                poll.setAnonymousFlag((byte)cmd.getAnonymousFlag().intValue());
            poll.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            if(cmd.getMultiChoiceFlag()!=null)
                poll.setMultiSelectFlag((byte)cmd.getMultiChoiceFlag().intValue());
            //family
            if(families[0]!=null)
                poll.setCreatorFamilyId(families[0].getId());
            poll.setCreatorUid(user.getId());
            poll.setPollCount(0);
            poll.setPostId(postId);
            long startTimeMs=DateHelper.parseDataString(cmd.getStartTime(), "yyyy-mm-dd hh:mm:ss").getTime();
            long endTimeMs=DateHelper.parseDataString(cmd.getStartTime(), "yyyy-mm-dd hh:mm:ss").getTime();
            poll.setStartTime(new Timestamp(startTimeMs));
            poll.setStartTimeMs(startTimeMs);
            poll.setEndTimeMs(endTimeMs);
            poll.setEndTime(new Timestamp(endTimeMs));
            poll.setStatus(PollStatus.Published.getCode());
            pollProvider.createPoll(poll);
            List<PollItem> pollItems = cmd.getItemList().stream().map(r->{
                PollItem item=ConvertHelper.convert(r, PollItem.class);
                item.setPollId(poll.getId());
                item.setResourceUrl(r.getCoverUrl());
                //how set resource id
                item.setResourceId(r.getItemId());
                item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                return item;
            }).collect(Collectors.toList());
            pollProvider.createPollItem(pollItems);
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
        List<PollItem> matchResult = result.stream().filter(r->result.contains(r)).map(m->ConvertHelper.convert(m, PollItem.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(matchResult)) {
            LOGGER.error("cannot find any match item.{}", cmd.getItemIds());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_IMTE, "invalid poll item.item="+cmd.getItemIds());
        }
        PollVote voteResult = pollProvider.findPollVoteByUidAndPollId(user.getId(), cmd.getPollId());
        if (voteResult!=null) {
            LOGGER.error("can not vote again.pollId={}", cmd.getPollId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_DUPLICATE_VOTE, "cannot vote again");
        }
        List<PollVote> votes=new ArrayList<PollVote>();
        //do transaction
        if(Selector.fromCode(poll.getMultiSelectFlag()).equals(Selector.MUTIL_SELECT)){
            dbProvider.execute(status->{ matchResult.forEach(item->{
                PollVote pollVote = new PollVote();
                pollVote.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                pollVote.setItemId(item.getId());
                pollVote.setPollId(poll.getId());
                pollVote.setVoterUid(user.getId());
                votes.add(pollVote);
                //if addresses is not empty
                if(user.getAddressId()!=null)
                        //ensure all address is ok
                    pollVote.setVoterFamilyId(familyProvider.findFamilyByAddressId(user.getAddressId()).getId());
                pollProvider.createPollVote(pollVote);
                item.setVoteCount(item.getVoteCount()+1);
                //update poll item
                pollProvider.updatePollItem(item);
                //update poll
                poll.setPollCount(poll.getPollCount()+1);
                pollProvider.updatePoll(poll);
            });
            return true;
            });
            if(poll.getAnonymousFlag()==null||poll.getAnonymousFlag().longValue()!=1L)
                autoComment(poll,post,votes);
            PollDTO dto=ConvertHelper.convert(poll, PollDTO.class);
            dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
            dto.setProcessStatus(getStatus(poll).getCode());
            dto.setStartTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getStartTimeMs(), "YYYY-MM-DD hh:mm:ss"));
            dto.setStopTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getEndTimeMs(), "YYYY-MM-DD hh:mm:ss"));
            return dto;
        }
        
        //do transaction
        dbProvider.execute(status->{

            PollItem item = matchResult.get(0);
            PollVote pollVote = new PollVote();
            pollVote.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            pollVote.setItemId(item.getId());
            pollVote.setPollId(poll.getId());
            pollVote.setVoterUid(user.getId());
            votes.add(pollVote);
            //if addresses is not empty
            if(user.getAddressId()!=null){
                Family  family=familyProvider.findFamilyByAddressId(user.getAddressId());
                if(family!=null)
                pollVote.setVoterFamilyId(family.getId());
            }
            pollProvider.createPollVote(pollVote);
            item.setVoteCount(item.getVoteCount()+1);
            //update poll item
            pollProvider.updatePollItem(item);
            
        poll.setPollCount(poll.getPollCount()+1);
        pollProvider.updatePoll(poll);
        return status;
        });
        if(poll.getAnonymousFlag()==null||poll.getAnonymousFlag().longValue()!=1L)
            autoComment(poll,post,votes);
        PollDTO dto=ConvertHelper.convert(poll, PollDTO.class);
        dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        dto.setProcessStatus(getStatus(poll).getCode());
        return dto;
    }
    
    
    private void autoComment(Poll poll,Post post,List<PollVote> votes){
        String subject=StringUtils.join(votes.stream().map(r->r.getId()).collect(Collectors.toList()),",");
        Post comment=new Post();
        comment.setSubject(subject);
        comment.setForumId(post.getForumId());
        comment.setParentPostId(post.getId());
        forumProvider.createPost(post);
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
        response.setItems(result.stream().map(r->{
                PollItemDTO pollItem = ConvertHelper.convert(r, PollItemDTO.class);
                pollItem.setCoverUrl(contentServerService.parserUri(r.getResourceUrl(), EntityType.POST.getCode(), postId));
                pollItem.setItemId(r.getResourceId());
                pollItem.setCreateTime(r.getCreateTime().toString());
                return pollItem;
                }).collect(Collectors.toList()));
        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            LOGGER.error("convert bean failed.error={}",e.getMessage());
        }
        User user=UserContext.current().getUser();
        PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        dto.setStartTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getStartTimeMs(), "YYYY-MM-DD hh:mm:ss"));
        dto.setStopTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getEndTimeMs(), "YYYY-MM-DD hh:mm:ss"));
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
        if(poll==null){
            return null;
        }
        List<PollItem> result = pollProvider.listPollItemByPollId(poll.getId());
        PollShowResultResponse response = new PollShowResultResponse();
        response.setItems(result.stream().map(r->{
            PollItemDTO item= ConvertHelper.convert(r, PollItemDTO.class);
            item.setCoverUrl(contentServerService.parserUri(r.getResourceUrl(), EntityType.POST.getCode(), postId));
            item.setItemId(r.getResourceId());
            item.setCreateTime(r.getCreateTime().toString());
            return item;
        }).collect(Collectors.toList()));
        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            //skip
        }
        User user=UserContext.current().getUser();
        PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        dto.setStartTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getStartTimeMs(), "YYYY-MM-DD hh:mm:ss"));
        dto.setStopTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), poll.getEndTimeMs(), "YYYY-MM-DD hh:mm:ss"));
        dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        if(votes==null){
            dto.setPollVoterStatus(VotedStatus.UNVOTED.getCode());
        }
        dto.setProcessStatus(getStatus(poll).getCode());
        response.setPoll(dto);
        return response;
    }
}
