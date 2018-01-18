package com.everhomes.openapi;

import com.everhomes.rest.openapi.FunctionCardDto;
import com.everhomes.rest.openapi.FunctionCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GuildFunctionCardHandler implements FunctionCardHandler{
    @Override
    public FunctionCardDto listCards(Integer namespaceId, Long userId) {
        FunctionCardDto dto = new FunctionCardDto();
        dto.setCategoryName(FunctionCategory.GUILD);
        dto.setJsonData(new ArrayList<>());
        return dto;
    }
}
