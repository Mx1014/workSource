// @formatter:off
package com.everhomes.activity;

import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.activity.ActivityRosterStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ActivitySignupTimeoutAction implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(ActivitySignupTimeoutAction.class);


    @Autowired
    ScheduleProvider scheduleProvider;

    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private ActivityProivider activityProivider;

    @Autowired
    private ActivitySignupTimeoutService activitySignupTimeoutService;

    private Long postId;

    @Override
    public void run() {
        log.info("ActivitySignupTimeoutAction start ! postId=" + postId);
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
            Post post = this.forumProvider.findPostById(postId);
            if (post != null && post.getEmbeddedAppId() != null && post.getEmbeddedAppId() == AppConstants.APPID_ACTIVITY) {
                Activity activity = this.activityProivider.findSnapshotByPostId(post.getId());
                if (activity != null) {
                   List<ActivityRoster> rosters = activityProivider.listRosters(activity.getId(), ActivityRosterStatus.NORMAL);
                   if (rosters != null && post.getMinQuantity() != null && rosters.size() < post.getMinQuantity()) {
                       this.activitySignupTimeoutService.cancelTimeoutActivity(postId);
                   }
                }
            }
        }

        log.info("ActivitySignupTimeoutAction end ! postId=" + postId);
    }

    public ActivitySignupTimeoutAction(final String postId) {
        this.postId = Long.parseLong(postId);
    }
}
