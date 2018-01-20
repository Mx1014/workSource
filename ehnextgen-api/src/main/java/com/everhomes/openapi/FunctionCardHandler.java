package com.everhomes.openapi;

import com.everhomes.rest.openapi.FunctionCardDto;

import java.util.List;

public interface FunctionCardHandler {
    FunctionCardDto listCards(Integer namespaceId, Long userId);
}
