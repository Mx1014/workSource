package com.everhomes.point.processor.club;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.group.Group;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.group.ClubType;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ClubDeletePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.GROUP_GROUP_DELETE.getCode()};
    }

    @Override
    protected String getEventName(LocalEvent localEvent) {
        Byte clubType = null;

        String groupJson = localEvent.getStringParam("group");
        if (groupJson != null) {
            Group group = (Group) StringHelper.fromJsonString(groupJson, Group.class);
            clubType = group.getClubType();

            // --
            localEvent.setTargetUid(group.getCreatorUid());
        } else {
            String groupDtoJson = localEvent.getStringParam("groupDto");
            if (groupDtoJson != null) {
                GroupDTO group = (GroupDTO) StringHelper.fromJsonString(groupDtoJson, GroupDTO.class);
                clubType = group.getClubType();

                // --
                localEvent.setTargetUid(group.getCreatorUid());
            }
        }

        String eventName = "";
        if (Objects.equals(clubType, ClubType.NORMAL.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.NORMAL.getCode());
        } else if (Objects.equals(clubType, ClubType.GUILD.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.GUILD.getCode());
        }
        return eventName;
    }
}
