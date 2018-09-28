package com.everhomes.rest.user;

import java.util.List;

public class FindUsersByPhonesResponse {
    private List<FindUsersByPhonesDTO> dtos ;
    private String errorMsg ;

    public List<FindUsersByPhonesDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FindUsersByPhonesDTO> dtos) {
        this.dtos = dtos;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
