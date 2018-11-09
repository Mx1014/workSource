// @formatter:off
package com.everhomes.poll;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.rest.poll.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.forum.PostContentType;

import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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

    @Autowired
    private CoordinationProvider coordinationProvider;

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
            // 当没有填开始时间或截止时间时会报空指针，故加上判断 by lqs 20151216
            if(cmd.getStartTime() != null) {
                long startTimeMs=convert(cmd.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime();
                poll.setStartTime(new Timestamp(startTimeMs));
                poll.setStartTimeMs(startTimeMs);
            }
            if(cmd.getStopTime() != null) {
                long endTimeMs=convert(cmd.getStopTime(), "yyyy-MM-dd HH:mm:ss").getTime();
                poll.setEndTimeMs(endTimeMs);
                poll.setEndTime(new Timestamp(endTimeMs));
            }
            poll.setStatus(PollStatus.Published.getCode());
            poll.setId(cmd.getId());

            // 添加tag标签   add by yanjun 20170613
            poll.setTag(cmd.getTag());

            // 增加是否支持重复投票，以及重复投票的间隔   add by yanjun 20170825
            poll.setRepeatFlag(cmd.getRepeatFlag());
            poll.setRepeatPeriod(cmd.getRepeatPeriod());
            poll.setWechatPoll(cmd.getWechatPoll());

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
        List<PollItem> matchResult = result.stream().filter(r->cmd.getItemIds().contains(r.getResourceId())).map(m->ConvertHelper.convert(m, PollItem.class)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(matchResult)) {
            LOGGER.error("cannot find any match item.{}", cmd.getItemIds());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_INVALID_POLL_IMTE, "invalid poll item.item="+cmd.getItemIds());
        }

        //检查是否重复投票，1、不支持重复投票，2、支持重复投票，时间间隔未过
        boolean repeat = checkValidPoll(poll, user.getId());
        if(repeat){
            LOGGER.error("can not vote again.pollId={}", poll.getId());
            throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_DUPLICATE_VOTE, "cannot vote again");
        }


        Tuple<PollDTO, Boolean> enter = this.coordinationProvider.getNamedLock(CoordinationLocks.POLL_VOTE.getCode() + user.getId()).enter(() -> {


            //检查是否重复投票，1、不支持重复投票，2、支持重复投票，时间间隔未过
            //锁内部再检查一遍
            boolean repeat1 = checkValidPoll(poll, user.getId());
            if(repeat1){
                LOGGER.error("can not vote again.pollId={}", poll.getId());
                throw RuntimeErrorException.errorWith(PollServiceErrorCode.SCOPE, PollServiceErrorCode.ERROR_DUPLICATE_VOTE, "cannot vote again");
            }

            List<PollVote> votes = new ArrayList<PollVote>();
            //do transaction
            if (Selector.fromCode(poll.getMultiSelectFlag()).equals(Selector.MUTIL_SELECT)) {
                dbProvider.execute(status -> {
                    matchResult.forEach(item -> {
                        PollVote pollVote = new PollVote();
                        pollVote.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        pollVote.setItemId(item.getId());
                        pollVote.setPollId(poll.getId());
                        pollVote.setVoterUid(user.getId());
                        votes.add(pollVote);
                        //if addresses is not empty
                        if (user.getAddressId() != null) {
                            //ensure all address is ok
                            Family family = familyProvider.findFamilyByAddressId(user.getAddressId());

                            //family要存在
                            if (family != null) {
                                pollVote.setVoterFamilyId(family.getId());
                            }

                        }

                        pollProvider.createPollVote(pollVote);
                        item.setVoteCount(item.getVoteCount() + 1);
                        //update poll item
                        pollProvider.updatePollItem(item);

                    });

                    //将pollCount放在forEach外面，因为一个人投票多个选项，仍然算是一个人投票   edit by yanjun 20170728
                    //update poll
                    poll.setPollCount(poll.getPollCount() + 1);
                    pollProvider.updatePoll(poll);
                    return true;
                });
                if (poll.getAnonymousFlag() == null || poll.getAnonymousFlag().longValue() != 1L)
                    autoComment(poll, post, votes, result);
                PollDTO dto = ConvertHelper.convert(poll, PollDTO.class);
                dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
                dto.setProcessStatus(getStatus(poll).getCode());
                dto.setAnonymousFlag(poll.getAnonymousFlag() == null ? 0 : poll.getAnonymousFlag().intValue());
                dto.setMultiChoiceFlag(poll.getMultiSelectFlag() == null ? 0 : poll.getMultiSelectFlag().intValue());
                dto.setStartTime(poll.getStartTime().toString());
                dto.setStopTime(poll.getEndTime().toString());
                return dto;
            } else {
                //do transaction
                dbProvider.execute(status -> {

                    PollItem item = matchResult.get(0);
                    PollVote pollVote = new PollVote();
                    pollVote.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    pollVote.setItemId(item.getId());
                    pollVote.setPollId(poll.getId());
                    pollVote.setVoterUid(user.getId());
                    votes.add(pollVote);
                    //if addresses is not empty
                    if (user.getAddressId() != null) {
                        Family family = familyProvider.findFamilyByAddressId(user.getAddressId());
                        if (family != null)
                            pollVote.setVoterFamilyId(family.getId());
                    }
                    pollProvider.createPollVote(pollVote);
                    item.setVoteCount(item.getVoteCount() + 1);
                    //update poll item
                    pollProvider.updatePollItem(item);

                    poll.setPollCount(poll.getPollCount() + 1);
                    pollProvider.updatePoll(poll);
                    return status;
                });
                if (poll.getAnonymousFlag() == null || poll.getAnonymousFlag().longValue() != 1L)
                    autoComment(poll, post, votes, result);
                PollDTO dto = ConvertHelper.convert(poll, PollDTO.class);
                dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
                dto.setProcessStatus(getStatus(poll).getCode());
                dto.setAnonymousFlag(poll.getAnonymousFlag() == null ? 0 : poll.getAnonymousFlag().intValue());
                dto.setMultiChoiceFlag(poll.getMultiSelectFlag() == null ? 0 : poll.getMultiSelectFlag().intValue());

                //投票 add by yanjun 20171211
                voteEvents(post);
                return dto;
            }
        });


        return enter.first();
    }

    private void voteEvents(Post post) {
        Long  userId = UserContext.currentUserId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(userId);
            context.setNamespaceId(namespaceId);
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(post.getId());
            Long embeddedAppId = post.getEmbeddedAppId() != null ? post.getEmbeddedAppId() : 0;
            event.setEventName(SystemEvent.FORUM_POST_VOTE.suffix(
                    post.getModuleType(), post.getModuleCategoryId(), embeddedAppId));

            event.addParam("embeddedAppId", String.valueOf(embeddedAppId));
            event.addParam("post", StringHelper.toJsonString(post));
        });
    }


    //检查有有效投票
    private boolean checkValidPoll(Poll poll, Long userId){
        PollVote voteResult = pollProvider.findPollVoteByUidAndPollId(userId, poll.getId());

        //投过票
        if (voteResult == null) {
            return false;
        }

        //检查该投票是否超过时间间隔
        boolean flag =checkVoteExpirePeriod(poll, voteResult);

        //过期--没有有效投票，未过期--有有效投票
        return !flag;

//            //1、不支持重复投票
//            if (poll.getRepeatFlag() == null || poll.getRepeatFlag().byteValue() == RepeatFlag.NO.getCode()) {
//                return true;
//            }
//
//            //2、支持重复投票
//            if(poll.getRepeatFlag().byteValue() == RepeatFlag.YES.getCode() && poll.getRepeatPeriod() != null && voteResult.getCreateTime() !=null){
//
//                java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
//
//                java.sql.Date voteDate = new java.sql.Date(voteResult.getCreateTime().getTime());
//
//                //如果当前日期在下次投票日期之前则报异常
//                if(now.toLocalDate().isBefore(voteDate.toLocalDate().plusDays(poll.getRepeatPeriod()))){
//                    return true;
//                }
//            }
//
//        }
//        return false;
    }
    
    
    private void autoComment(Poll poll,Post post,List<PollVote> votes, List<PollItem> result){
        String subject="";
        if(result.size()==1){
            subject=poll.getSubject()==null?"1":poll.getSubject();
        }else{
        	List<Long> pollItemsId = votes.stream().map(r->r.getItemId()).collect(Collectors.toList());
        	List<PollItem> pollItems = pollItemsId.stream().map(r->{
        		PollItem item = pollProvider.findPollItemById(r);
				return item;
        	}).collect(Collectors.toList());
        	subject = StringUtils.join(pollItems.stream().map(r->r.getSubject()).collect(Collectors.toList()),",");
 //           subject=StringUtils.join(votes.stream().map(r->r.getItemId()).collect(Collectors.toList()),",");
        }
        Post comment=new Post();
        User user = UserContext.current().getUser();
        comment.setContent("我已投 ”"+subject+"“!");
        comment.setCreatorUid(user.getId());
        comment.setForumId(post.getForumId());
        comment.setParentPostId(post.getId());
        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        comment.setContentType(PostContentType.TEXT.getCode());
        forumProvider.createPost(comment);
    }
    
   private ProcessStatus getStatus(Poll poll){
      return StatusChecker.getProcessStatus(poll.getStartTimeMs(), poll.getEndTimeMs());
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


        User user=UserContext.current().getUser();
        //对选项进行遍历，填充选项是否被选择
        populateItemVoteStatus(poll, response.getItems(), user.getId());

        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            LOGGER.error("convert bean failed.error={}",e.getMessage());
        }

        PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        dto.setStartTime(poll.getStartTime().toString());
        dto.setPollId(poll.getId());
        dto.setStopTime(poll.getEndTime().toString());
        dto.setAnonymousFlag(poll.getAnonymousFlag()==null?0:poll.getAnonymousFlag().intValue());
        dto.setMultiChoiceFlag(poll.getMultiSelectFlag()==null?0:poll.getMultiSelectFlag().intValue());
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
        //PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        if(poll.getStartTime() != null)
        	dto.setStartTime(poll.getStartTime().toString());
        
        if(poll.getEndTime() != null)
        	dto.setStopTime(poll.getEndTime().toString());
        
        dto.setAnonymousFlag(poll.getAnonymousFlag()==null?0:poll.getAnonymousFlag().intValue());
        dto.setMultiChoiceFlag(poll.getMultiSelectFlag()==null?0:poll.getMultiSelectFlag().intValue());
        dto.setPollId(poll.getId());

        //检查是否重复投票  edit by yanjun 20170825
        boolean haveValidFlag = checkValidPoll(poll, user.getId());
        if(haveValidFlag){
            dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
            //对选项进行遍历，填充选项是否被选择
            populateItemVoteStatus(poll, response.getItems(), user.getId());

        }else {
            dto.setPollVoterStatus(VotedStatus.UNVOTED.getCode());
        }

        dto.setProcessStatus(getStatus(poll).getCode());
        response.setPoll(dto);
        return response;
    }

    private void populateItemVoteStatus(Poll poll, List<PollItemDTO> items, Long userId){

        List<PollVote> pollVotes = pollProvider.listPollVoteByUidAndPollId(userId, poll.getId());

        //对选项进行遍历，找到对应的不过期的投票则将该选项改成“已选择”
        if(pollVotes != null){
            for(PollItemDTO itemDTO: items){
                for (PollVote vo: pollVotes){
                    if(itemDTO.getId().longValue() == vo.getItemId()){
                        //已经投过票，检查投票是否超过重复投票间隔
                        boolean flag = checkVoteExpirePeriod(poll, vo);
                        //未超过时间间隔
                        if(!flag){
                            itemDTO.setPollVoterStatus(PollVoterStatus.YES.getCode());
                            break;
                        }
                    }
                }


            }
        }


    }


    /**
     * 已经投过票，检查投票是否超过重复投票间隔
     * @param poll
     * @param vo
     * @return
     */
    private boolean checkVoteExpirePeriod(Poll poll, PollVote vo){

        //不支持重复投票，未过期
        if(poll.getRepeatFlag() == null || poll.getRepeatFlag().byteValue() == RepeatFlag.NO.getCode()){
            return false;
        }

        //支持重复投票，并且 “当前日期” > “上次投票日” + “投票间隔”，此时上次投票已经过期
        if(poll.getRepeatFlag() != null && poll.getRepeatFlag().byteValue() == RepeatFlag.YES.getCode() && poll.getRepeatPeriod() != null && vo.getCreateTime() !=null){

            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

            java.sql.Date voteDate = new java.sql.Date(vo.getCreateTime().getTime());

            //如果当前日期在下次投票日期之前则报异常
            if(now.toLocalDate().isAfter(voteDate.toLocalDate().plusDays(poll.getRepeatPeriod() - 1))){
                return true;
            }
        }

        return false;

    }

    private static Date convert(String time,String format){
        SimpleDateFormat f=new SimpleDateFormat(format);
        try {
            return f.parse(time);
        } catch (Exception e) {
            return new Date();
        }
    }

	@Override
	public PollDTO showResultBrief(Long postId) {
		SimpleDateFormat fommat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Poll poll=pollProvider.findByPostId(postId);
        if(poll==null){
            return null;
        }
        PollDTO dto=new PollDTO();
        try{
            BeanUtils.copyProperties(poll, dto);
        }catch(Exception e){
            //skip
        }
        User user=UserContext.current().getUser();
        //PollVote votes = pollProvider.findPollVoteByUidAndPollId(user.getId(), poll.getId());
        if(poll.getStartTime() != null) {
            dto.setStartTime(fommat.format(new Date(poll.getStartTime().getTime())).toString());
        }
        if(poll.getEndTime() != null) {
            dto.setStopTime(fommat.format(new Date(poll.getEndTime().getTime())).toString());
        }
        dto.setAnonymousFlag(poll.getAnonymousFlag() == null ? 0 : poll.getAnonymousFlag().intValue());
        dto.setMultiChoiceFlag(poll.getMultiSelectFlag() == null ? 0 : poll.getMultiSelectFlag().intValue());
        //dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        dto.setPollId(poll.getId());


        //检查是否重复投票  edit by yanjun 20170825
        boolean repeat = checkValidPoll(poll, user.getId());
        if(repeat){
            dto.setPollVoterStatus(VotedStatus.VOTED.getCode());
        }else {
            dto.setPollVoterStatus(VotedStatus.UNVOTED.getCode());
        }
        dto.setProcessStatus(getStatus(poll).getCode());
        return dto;
	}
}
