package com.everhomes.point.processor.club;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.group.ClubType;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ClubJoinPointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.GROUP_GROUP_JOIN.getCode(),
                SystemEvent.GROUP_GROUP_JOIN_APPROVAL.getCode(),
        };
    }

    @Override
    protected String getEventName(LocalEvent localEvent) {
        Byte clubType = null;

        String groupJson = localEvent.getStringParam("group");
        if (groupJson != null) {
            Group group = (Group) StringHelper.fromJsonString(groupJson, Group.class);
            clubType = group.getClubType();
        } else {
            String groupDtoJson = localEvent.getStringParam("groupDto");
            if (groupDtoJson != null) {
                GroupDTO group = (GroupDTO) StringHelper.fromJsonString(groupDtoJson, GroupDTO.class);
                clubType = group.getClubType();
            }
        }

        Byte memberStatus = null;

        String memberJson = localEvent.getStringParam("member");
        if (memberJson != null) {
            GroupMember groupMember = (GroupMember) StringHelper.fromJsonString(memberJson, GroupMember.class);
            memberStatus = groupMember.getMemberStatus();

            // --
            localEvent.setTargetUid(groupMember.getMemberId());
        }

        String eventName = "";
        if (Objects.equals(memberStatus, GroupMemberStatus.ACTIVE.getCode())) {
            if (Objects.equals(clubType, ClubType.NORMAL.getCode())) {
                eventName = SystemEvent.CLUB_CLUB_JOIN.suffix(ClubType.NORMAL.getCode());
            } else if (Objects.equals(clubType, ClubType.GUILD.getCode())) {
                eventName = SystemEvent.CLUB_CLUB_JOIN.suffix(ClubType.GUILD.getCode());
            }
        }
        return eventName;
    }
}
