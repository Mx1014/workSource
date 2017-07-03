// @formatter:off
package com.everhomes.forum;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhForumPostsRecord;

/**
 * Map type-erased Record to EntityProfileItem object
 * 
 * @author Kelven Yang
 *
 */
public class EhForumPostsRecordMapper implements RecordMapper<Record, EhForumPostsRecord> {    
    @SuppressWarnings("unchecked")
    @Override
    public EhForumPostsRecord map(Record r) {
        EhForumPostsRecord post = new EhForumPostsRecord();
        
        post.setId(r.getValue((Field<Long>)r.field("id")));
        post.setUuid(r.getValue((Field<String>)r.field("uuid")));
        post.setAppId(r.getValue((Field<Long>)r.field("app_id")));
        post.setForumId(r.getValue((Field<Long>)r.field("forum_id")));
        post.setParentPostId(r.getValue((Field<Long>)r.field("parent_post_id")));
        post.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
        post.setCreatorTag(r.getValue((Field<String>)r.field("creator_tag")));
        post.setTargetTag(r.getValue((Field<String>)r.field("target_tag")));
        post.setLongitude(r.getValue((Field<Double>)r.field("longitude")));
        post.setLatitude(r.getValue((Field<Double>)r.field("latitude")));
        post.setGeohash(r.getValue((Field<String>)r.field("geohash")));
        post.setVisibleRegionType(r.getValue((Field<Byte>)r.field("visible_region_type")));
        post.setVisibleRegionId(r.getValue((Field<Long>)r.field("visible_region_id")));
        post.setVisibleRegionPath(r.getValue((Field<String>)r.field("visible_region_path")));
        post.setCategoryId(r.getValue((Field<Long>)r.field("category_id")));
        post.setCategoryPath(r.getValue((Field<String>)r.field("category_path")));
        post.setModifySeq(r.getValue((Field<Long>)r.field("modify_seq")));
        post.setChildCount(r.getValue((Field<Long>)r.field("child_count")));
        post.setForwardCount(r.getValue((Field<Long>)r.field("forward_count")));
        post.setLikeCount(r.getValue((Field<Long>)r.field("like_count")));
        //post.setDislikeCount(r.getValue((Field<Long>)r.field("dislike_count")));
        post.setViewCount(r.getValue((Field<Long>)r.field("view_count")));
        post.setSubject(r.getValue((Field<String>)r.field("subject")));
        post.setContentType(r.getValue((Field<String>)r.field("content_type")));
        post.setContent(r.getValue((Field<String>)r.field("content")));
        post.setContentAbstract(r.getValue((Field<String>)r.field("content_abstract")));
        post.setEmbeddedAppId(r.getValue((Field<Long>)r.field("embedded_app_id")));
        post.setEmbeddedId(r.getValue((Field<Long>)r.field("embedded_id")));
        post.setEmbeddedJson(r.getValue((Field<String>)r.field("embedded_json")));
        post.setEmbeddedVersion(r.getValue((Field<Integer>)r.field("embedded_version")));
        post.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag1")));
        post.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag2")));
        post.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag3")));
        post.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag4")));
        post.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag5")));
        post.setStringTag1(r.getValue((Field<String>)r.field("string_tag1")));
        post.setStringTag2(r.getValue((Field<String>)r.field("string_tag2")));
        post.setStringTag3(r.getValue((Field<String>)r.field("string_tag3")));
        post.setStringTag4(r.getValue((Field<String>)r.field("string_tag4")));
        post.setStringTag5(r.getValue((Field<String>)r.field("string_tag5")));
        post.setPrivateFlag(r.getValue((Field<Byte>)r.field("private_flag")));
        post.setAssignedFlag(r.getValue((Field<Byte>)r.field("assigned_flag")));
        post.setFloorNumber(r.getValue((Field<Long>)r.field("floor_number")));
        post.setStatus(r.getValue((Field<Byte>)r.field("status")));
        post.setUpdateTime(r.getValue((Field<Timestamp>)r.field("update_time")));
        post.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
        post.setDeleterUid(r.getValue((Field<Long>)r.field("deleter_uid")));
        post.setDeleteTime(r.getValue((Field<Timestamp>)r.field("delete_time")));
        post.setStartTime(r.getValue((Field<Timestamp>)r.field("start_time")));     
        post.setEndTime(r.getValue((Field<Timestamp>)r.field("end_time")));  
        post.setOfficialFlag(r.getValue((Field<Byte>)r.field("official_flag")));
        post.setMediaDisplayFlag(r.getValue((Field<Byte>)r.field("media_display_flag")));
        post.setTag(r.getValue((Field<String>)r.field("tag")));
        return post;
    }
}
