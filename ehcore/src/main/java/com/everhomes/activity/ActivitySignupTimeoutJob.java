// @formatter:off
package com.everhomes.activity;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.flow.FlowServiceImpl;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ActivitySignupTimeoutJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitySignupTimeoutJob.class);

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ActivitySignupTimeoutService activitySignupTimeoutService;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private ActivityProivider activityProivider;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
                Long postId = jobExecutionContext.getMergedJobDataMap().getLongValue("postId");
                Post post = this.forumProvider.findPostById(postId);
                if (post != null && post.getEmbeddedAppId() != null && post.getEmbeddedAppId() == AppConstants.APPID_ACTIVITY) {
                    Activity activity = this.activityProivider.findSnapshotByPostId(post.getId());
                    if (activity != null) {
                        List<ActivityRoster> rosters = activityProivider.listRosters(activity.getId(), ActivityRosterStatus.NORMAL);
                        if (rosters != null && post.getMinQuantity() != null && rosters.size() < post.getMinQuantity()) {
                            LocalDateTime nowDateTime = LocalDateTime.now();
                            coordinationProvider.getNamedLock(CoordinationLocks.ACTIVITY_SIGNUP_TIMEOUT.getCode()).tryEnter(() -> {
                                LOGGER.info("ActivitySignupTimeoutJob has been started at " + nowDateTime);
                                activitySignupTimeoutService.cancelTimeoutActivity(postId);
                                LOGGER.info("ActivitySignupTimeoutJob has been ended at " + nowDateTime);
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("ActivitySignupTimeoutJob Failed!", e);
        }
    }
}
