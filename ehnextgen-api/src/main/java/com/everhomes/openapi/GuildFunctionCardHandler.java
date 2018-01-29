package com.everhomes.openapi;

import com.everhomes.group.GroupService;
import com.everhomes.rest.group.ClubType;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.openapi.FunctionCardDto;
import com.everhomes.rest.openapi.FunctionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuildFunctionCardHandler implements FunctionCardHandler{

    @Autowired
    private GroupService groupService;

    @Override
    public FunctionCardDto listCards(Integer namespaceId, Long userId) {
        FunctionCardDto dto = new FunctionCardDto();
        dto.setCategoryName(FunctionCategory.GUILD);

        List<GroupDTO> groupDtos = groupService.listOwnerGroupsByType(ClubType.GUILD.getCode());
        if(groupDtos != null){
            List<String> json = groupDtos.stream().map(group -> group.toString()).collect(Collectors.toList());
            dto.setJsonData(json);
        }
        return dto;
    }
}
