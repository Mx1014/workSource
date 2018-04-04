package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos 参考{@link UserDTO}</li>
 * </ul>
 */
public class SearchUserByIdentifierResponse {

    private List<UserDTO> dtos;

    public List<UserDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<UserDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
