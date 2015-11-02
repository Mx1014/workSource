package com.everhomes.enterprise;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;

import com.everhomes.server.schema.tables.records.EhGroupsRecord;

public class EnterpriseRecordMapper implements RecordMapper<Record, EhGroupsRecord> {

    @SuppressWarnings("unchecked")
    @Override
    public EhGroupsRecord map(Record r) {
        EhGroupsRecord o = new EhGroupsRecord();
        
        o.setId(r.getValue((Field<Long>)r.field("id")));
        o.setUuid(r.getValue((Field<String>)r.field("uuid")));
        o.setNamespaceId(r.getValue((Field<Integer>)r.field("namespace_id")));
        o.setName(r.getValue((Field<String>)r.field("name")));
        o.setDisplayName(r.getValue((Field<String>)r.field("display_name")));
        o.setAvatar(r.getValue((Field<String>)r.field("avatar")));
        o.setDescription(r.getValue((Field<String>)r.field("description")));
        o.setCreatorUid(r.getValue((Field<Long>)r.field("creator_uid")));
        o.setPrivateFlag(r.getValue((Field<Byte>)r.field("private_flag")));
        o.setJoinPolicy(r.getValue((Field<Integer>)r.field("join_policy")));
        o.setDiscriminator(r.getValue((Field<String>)r.field("discriminator")));
        o.setVisibilityScope(r.getValue((Field<Byte>)r.field("visibility_scope")));
        o.setVisibilityScopeId(r.getValue((Field<Long>)r.field("visibility_scope_id")));
        o.setCategoryId(r.getValue((Field<Long>)r.field("category_id")));
        o.setCategoryPath(r.getValue((Field<String>)r.field("category_path")));
        o.setStatus(r.getValue((Field<Byte>)r.field("status")));
        o.setMemberCount(r.getValue((Field<Long>)r.field("member_count")));
        o.setShareCount(r.getValue((Field<Long>)r.field("share_count")));
        o.setPostFlag(r.getValue((Field<Byte>)r.field("post_flag")));
        o.setTag(r.getValue((Field<String>)r.field("tag")));
        o.setIntegralTag1(r.getValue((Field<Long>)r.field("integral_tag")));
        o.setIntegralTag2(r.getValue((Field<Long>)r.field("integral_tag")));
        o.setIntegralTag3(r.getValue((Field<Long>)r.field("integral_tag")));
        o.setIntegralTag4(r.getValue((Field<Long>)r.field("integral_tag")));
        o.setIntegralTag5(r.getValue((Field<Long>)r.field("integral_tag")));
        o.setStringTag1(r.getValue((Field<String>)r.field("string_tag")));
        o.setStringTag2(r.getValue((Field<String>)r.field("string_tag")));
        o.setStringTag3(r.getValue((Field<String>)r.field("string_tag")));
        o.setStringTag4(r.getValue((Field<String>)r.field("string_tag")));
        o.setStringTag5(r.getValue((Field<String>)r.field("string_tag")));
        o.setUpdateTime(r.getValue((Field<Timestamp>)r.field("update_time")));
        o.setCreateTime(r.getValue((Field<Timestamp>)r.field("create_time")));
        o.setDeleteTime(r.getValue((Field<Timestamp>)r.field("delete_time")));
        
        return o;
    }

}
