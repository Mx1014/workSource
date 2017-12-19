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
 * 完善用户信息事件处理器
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ClubCreatePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.GROUP_GROUP_CREATE.getCode(),
                SystemEvent.GROUP_GROUP_APPROVAL.getCode()
        };
    }

    @Override
    protected String getEventName(LocalEvent localEvent, String subscriptionPath) {
        Byte clubType = null;

        String groupJson = localEvent.getParam("group");
        if (groupJson != null) {
            Group group = (Group) StringHelper.fromJsonString(groupJson, Group.class);
            clubType = group.getClubType();
        } else {
            String groupDtoJson = localEvent.getParam("groupDto");
            if (groupDtoJson != null) {
                GroupDTO group = (GroupDTO) StringHelper.fromJsonString(groupDtoJson, GroupDTO.class);
                clubType = group.getClubType();
            }
        }

        String eventName = "";
        if (Objects.equals(clubType, ClubType.NORMAL.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_CREATE.suffix(ClubType.NORMAL.getCode());
        } else if (Objects.equals(clubType, ClubType.GUILD.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_CREATE.suffix(ClubType.GUILD.getCode());
        }
        return eventName;
    }
}
