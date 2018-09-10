// @formatter:off
package com.everhomes.announcement;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CancelLikeAnnouncementCommand;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.announcement.DeleteAnnouncementCommand;
import com.everhomes.rest.announcement.GetAnnouncementCommand;
import com.everhomes.rest.announcement.LikeAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementCommand;
import com.everhomes.rest.announcement.ListAnnouncementResponse;
import com.everhomes.rest.announcement.QueryAnnouncementCommand;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.CronDateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnouncementServiceImpl implements AnnouncementService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementServiceImpl.class);


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private ForumService forumService;

    @Override
    public AnnouncementDTO createAnnouncement(CreateAnnouncementCommand cmd) {
        List<Long> communityIds = cmd.getVisibleRegionIds();
        NewTopicCommand newTopicCommand = ConvertHelper.convert(cmd, NewTopicCommand.class);
        newTopicCommand.setContentCategory(CategoryConstants.CATEGORY_ID_NOTICE);
        PostDTO postDTO = this.organizationService.createTopic(newTopicCommand);
        if (TrueOrFalseFlag.TRUE.getCode().equals(cmd.getNoticeUserFlag())) {
            sendMessageToUserWhenCreateAnnouncement(cmd.getNamespaceId(), communityIds, postDTO);
        }
        return ConvertHelper.convert(postDTO, AnnouncementDTO.class);
    }

    @Override
    public AnnouncementDTO getAnnouncement(GetAnnouncementCommand cmd) {
        GetTopicCommand getTopicCommand = new GetTopicCommand();
        getTopicCommand.setTopicId(cmd.getAnnouncementId());
        getTopicCommand.setCommunityId(cmd.getCommunityId());
        PostDTO postDTO = this.forumService.getTopic(getTopicCommand);
        return ConvertHelper.convert(postDTO, AnnouncementDTO.class);
    }

    @Override
    public void deleteAnnouncement(DeleteAnnouncementCommand cmd) {
        Forum forum = forumService.findFourmByNamespaceId(UserContext.getCurrentNamespaceId());
        if(forum != null){
            cmd.setForumId(forum.getId());
        }
        this.forumService.deletePost(cmd.getForumId(), cmd.getAnnouncementId(), null, null, null);
    }

    @Override
    public ListAnnouncementResponse listAnnouncement(ListAnnouncementCommand cmd) {
        List<Long> communityIds = new ArrayList<>();
        communityIds.add(cmd.getCommunityId());
        List<Long> organizationIds = new ArrayList<>();
        ListPostCommandResponse listPostCommandResponse = this.forumService.listNoticeTopic(organizationIds, communityIds, cmd.getPublishStatus(), cmd.getPageSize(), cmd.getPageAnchor());
        ListAnnouncementResponse response = ConvertHelper.convert(listPostCommandResponse, ListAnnouncementResponse.class);
        List<AnnouncementDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listPostCommandResponse.getPosts())) {
            listPostCommandResponse.getPosts().stream().forEach(r -> {
                AnnouncementDTO announcementDTO = ConvertHelper.convert(r, AnnouncementDTO.class);
                dtos.add(announcementDTO);
            });
        }
        response.setAnnouncementDTOs(dtos);
        return response;
    }

    @Override
    public void likeAnnouncement(LikeAnnouncementCommand cmd) {
        LikeTopicCommand command = new LikeTopicCommand();
        command.setTopicId(cmd.getAnnouncementId());
        Forum forum = forumService.findFourmByNamespaceId(UserContext.getCurrentNamespaceId());
        if(forum != null){
            command.setForumId(forum.getId());
        }
        this.forumService.likeTopic(command);
    }

    @Override
    public void cancelLikeAnnouncement(CancelLikeAnnouncementCommand cmd) {
        CancelLikeTopicCommand command = new CancelLikeTopicCommand();
        command.setTopicId(cmd.getAnnouncementId());
        Forum forum = forumService.findFourmByNamespaceId(UserContext.getCurrentNamespaceId());
        if(forum != null){
            command.setForumId(forum.getId());
        }
        this.forumService.cancelLikeTopic(command);
    }

    private void sendMessageToUserWhenCreateAnnouncement(Integer namespaceId, List<Long> communityIds, PostDTO postDTO) {
        String triggerName = CoordinationLocks.ANNOUNCEMENT_CREATE_NOTICE_USER.getCode() + System.currentTimeMillis();
        String jobName = CoordinationLocks.ANNOUNCEMENT_CREATE_NOTICE_USER.getCode() + System.currentTimeMillis();
        Long time = System.currentTimeMillis()+2*1000;
        String cronExpression = CronDateUtils.getCron(new Timestamp(time));
        Map param = new HashMap();
        param.put("namespaceId",namespaceId);
        param.put("communityIds", communityIds);
        param.put("postDTO", postDTO);
        LOGGER.info("The AnnouncementJob has been prepared at " + LocalDateTime.now());
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, AnnouncementNoticeJob.class, param);
    }
}
