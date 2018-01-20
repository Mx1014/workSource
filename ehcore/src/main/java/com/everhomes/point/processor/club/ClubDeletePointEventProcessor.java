package com.everhomes.point.processor.club;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.group.Group;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.PointRule;
import com.everhomes.point.PointRuleToEventMapping;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.group.ClubType;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public List<PointRule> getPointRules(LocalEvent localEvent) {
        Byte clubType = getClubType(localEvent);

        String deleteEventName = "";
        String leaveEventName = "";

        if (Objects.equals(clubType, ClubType.NORMAL.getCode())) {
            deleteEventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.NORMAL.getCode());
            leaveEventName = SystemEvent.CLUB_CLUB_LEAVE.suffix(ClubType.NORMAL.getCode());
        } else if (Objects.equals(clubType, ClubType.GUILD.getCode())) {
            deleteEventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.GUILD.getCode());
            leaveEventName = SystemEvent.CLUB_CLUB_LEAVE.suffix(ClubType.GUILD.getCode());
        }

        List<PointRule> pointRules = new ArrayList<>();
        pointRules.addAll(getPointRulesByEventName(deleteEventName));
        pointRules.addAll(getPointRulesByEventName(leaveEventName));
        return pointRules;
    }

    private List<PointRule> getPointRulesByEventName(String leaveEventName) {
        String[] split = leaveEventName.split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String name = StringUtils.join(tokens, ".");

            List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByEventName(name);
            if (mappings != null && mappings.size() > 0) {
                List<Long> ruleIds = mappings.stream().map(PointRuleToEventMapping::getRuleId).collect(Collectors.toList());
                return pointRuleProvider.listPointRuleByIds(ruleIds);
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected String getEventName(LocalEvent localEvent) {
        Byte clubType = getClubType(localEvent);
        String eventName = "";
        if (Objects.equals(clubType, ClubType.NORMAL.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.NORMAL.getCode());
        } else if (Objects.equals(clubType, ClubType.GUILD.getCode())) {
            eventName = SystemEvent.CLUB_CLUB_RELEASE.suffix(ClubType.GUILD.getCode());
        }
        return eventName;
    }

    private Byte getClubType(LocalEvent localEvent) {
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
        return clubType;
    }
}
