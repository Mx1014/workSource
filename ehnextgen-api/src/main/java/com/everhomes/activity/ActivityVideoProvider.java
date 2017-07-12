package com.everhomes.activity;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface ActivityVideoProvider {

    Long createActivityVideo(ActivityVideo obj);

    void updateActivityVideo(ActivityVideo obj);

    void deleteActivityVideo(ActivityVideo obj);

    ActivityVideo getActivityVideoById(Long id);

    List<ActivityVideo> queryActivityVideos(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    ActivityVideo getActivityVideoByActivityId(Long activityId);

    ActivityVideo getActivityVideoByVid(String vid);

}
