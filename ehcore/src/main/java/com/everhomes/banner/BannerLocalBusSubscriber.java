package com.everhomes.banner;

import com.everhomes.banner.targethandler.BannerTargetActivityDetailHandler;
import com.everhomes.banner.targethandler.BannerTargetPostDetailHandler;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.SystemEvent;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.banner.targetdata.BannerActivityTargetData;
import com.everhomes.rest.banner.targetdata.BannerPostTargetData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by xq.tian on 2018/3/27.
 */
@Component
public class BannerLocalBusSubscriber implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private BannerProvider bannerProvider;

    @Autowired
    private ForumProvider forumProvider;

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        LocalEvent localEvent = (LocalEvent) args;

        Long appId = null;
        Long postId = 0L;
        Long forumId = 0L;

        Post parentPost = (Post) StringHelper.fromJsonString(localEvent.getStringParam("parentPost"), Post.class);
        String postJson = localEvent.getStringParam("post");
        if (postJson != null) {
            Post post = (Post) StringHelper.fromJsonString(postJson, Post.class);
            postId = post.getId();
            forumId = post.getForumId();
            boolean isComment = post.getParentPostId() != null && post.getParentPostId() != 0;
            if (isComment && parentPost != null) {
                appId = parentPost.getEmbeddedAppId();
            } else {
                appId = post.getEmbeddedAppId();
            }
        }

        if (postId == null || forumId == null) {
            return Action.none;
        }

        checkBanner(appId, postId, forumId);
        return Action.none;
    }

    private void checkBanner(Long appId, Long postId, Long forumId) {
        Forum forum = forumProvider.findForumById(forumId);
        List<Banner> banners = bannerProvider.findBannerByNamespeaceId(forum.getNamespaceId());

        // 活动
        if (Objects.equals(appId, AppConstants.APPID_ACTIVITY)) {
            BannerTargetActivityDetailHandler handler = PlatformContext.getComponent(BannerTargetActivityDetailHandler.class);

            BannerActivityTargetData targetData = new BannerActivityTargetData();
            targetData.setEntryId(forumId);
            targetData.setActivityId(postId);

            BannerTargetHandleResult result = handler.evaluate(StringHelper.toJsonString(targetData));

            updateBanner(banners, result);
        } else {
            // 帖子
            BannerTargetPostDetailHandler handler = PlatformContext.getComponent(BannerTargetPostDetailHandler.class);

            BannerPostTargetData targetData = new BannerPostTargetData();
            targetData.setEntryId(forumId);
            targetData.setPostId(postId);

            BannerTargetHandleResult result = handler.evaluate(StringHelper.toJsonString(targetData));

            updateBanner(banners, result);
        }
    }

    private void updateBanner(List<Banner> banners, BannerTargetHandleResult result) {
        for (Banner banner : banners) {
            if (equals(result, banner)) {
                banner.setActionType(ActionType.NONE.getCode());
                banner.setActionData(null);
                bannerProvider.updateBanner(banner);
            }
        }
    }

    private boolean equals(BannerTargetHandleResult result, Banner banner) {
        return Objects.equals(banner.getActionType(), result.getActionType())
                && Objects.equals(banner.getActionData(), result.getActionData());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            LocalEventBus.subscribe(SystemEvent.FORUM_POST_DELETE.name(), this);
        }
    }
}
