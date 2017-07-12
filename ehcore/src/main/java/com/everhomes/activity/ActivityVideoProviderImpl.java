// @formatter:off
package com.everhomes.activity;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.activity.VideoState;
import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhActivityVideoDao;
import com.everhomes.server.schema.tables.pojos.EhActivityVideo;
import com.everhomes.server.schema.tables.records.EhActivityVideoRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ActivityVideoProviderImpl implements ActivityVideoProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createActivityVideo(ActivityVideo obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityVideo.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityVideo.class));
        obj.setId(id);
        prepareObj(obj);
        EhActivityVideoDao dao = new EhActivityVideoDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateActivityVideo(ActivityVideo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityVideo.class));
        EhActivityVideoDao dao = new EhActivityVideoDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteActivityVideo(ActivityVideo obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityVideo.class));
        EhActivityVideoDao dao = new EhActivityVideoDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public ActivityVideo getActivityVideoById(Long id) {
        try {
        ActivityVideo[] result = new ActivityVideo[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityVideo.class));

        result[0] = context.select().from(Tables.EH_ACTIVITY_VIDEO)
            .where(Tables.EH_ACTIVITY_VIDEO.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, ActivityVideo.class);
            });

        if(result[0] != null && result[0].getVideoState().equals(VideoState.INVALID.getCode())) {
            //invalid
            return null;
        }
        
        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<ActivityVideo> queryActivityVideos(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhActivityVideo.class));

        SelectQuery<EhActivityVideoRecord> query = context.selectQuery(Tables.EH_ACTIVITY_VIDEO);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_ACTIVITY_VIDEO.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<ActivityVideo> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, ActivityVideo.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }
    
    @Override
    public ActivityVideo getActivityVideoByActivityId(Long activityId) {
        ListingLocator locator = new ListingLocator();
        List<ActivityVideo> videos = this.queryActivityVideos(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ACTIVITY_VIDEO.OWNER_ID.eq(activityId));
                query.addConditions(Tables.EH_ACTIVITY_VIDEO.OWNER_TYPE.eq("activity"));
                query.addConditions(Tables.EH_ACTIVITY_VIDEO.VIDEO_STATE.ne(VideoState.INVALID.getCode()));
                query.addOrderBy(Tables.EH_ACTIVITY_VIDEO.ID.desc());
                return query;
            }
            
        });
        
        if(videos != null && videos.size() > 0) {
            return videos.get(0);
        }
        
        return null;
    }

    @Override
    public ActivityVideo getActivityVideoByVid(String vid) {
        ListingLocator locator = new ListingLocator();
        List<ActivityVideo> videos = this.queryActivityVideos(locator, 1, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_ACTIVITY_VIDEO.VIDEO_SID.eq(vid));
                query.addConditions(Tables.EH_ACTIVITY_VIDEO.VIDEO_STATE.ne(VideoState.INVALID.getCode()));
                query.addOrderBy(Tables.EH_ACTIVITY_VIDEO.ID.desc());
                return query;
            }

        });

        if(videos != null && videos.size() > 0) {
            return videos.get(0);
        }

        return null;
    }

    private void prepareObj(ActivityVideo obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }
}
