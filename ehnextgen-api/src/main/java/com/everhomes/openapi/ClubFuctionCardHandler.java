package com.everhomes.openapi;

import com.everhomes.rest.openapi.FunctionCardDto;
import com.everhomes.rest.openapi.FunctionCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ClubFuctionCardHandler implements FunctionCardHandler{
    @Override
    public FunctionCardDto listCards(Integer namespaceId, Long userId) {
        FunctionCardDto dto = new FunctionCardDto();
        dto.setCategoryName(FunctionCategory.CLUB);
        dto.setJsonData(new ArrayList<>());
        return dto;
    }
}
