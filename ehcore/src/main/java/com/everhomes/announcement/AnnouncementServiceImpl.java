// @formatter:off
package com.everhomes.announcement;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.announcement.AnnouncementDTO;
import com.everhomes.rest.announcement.CreateAnnouncementCommand;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.CronDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Override
    public AnnouncementDTO createAnnouncement(CreateAnnouncementCommand cmd) {
        List<Long> communityIds = cmd.getVisibleRegionIds();
        NewTopicCommand newTopicCommand = ConvertHelper.convert(cmd, NewTopicCommand.class);
        PostDTO postDTO = this.organizationService.createTopic(newTopicCommand);
        if (TrueOrFalseFlag.TRUE.getCode().equals(cmd.getNoticeUserFlag())) {
            sendMessageToUserWhenCreateAnnouncement(cmd.getNamespaceId(), communityIds, postDTO);
        }
        return ConvertHelper.convert(postDTO, AnnouncementDTO.class);
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
