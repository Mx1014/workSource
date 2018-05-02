package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>persons: 共享日程的同事列表,参考{@link com.everhomes.rest.remind.SharingPersonDTO}</li>
 * </ul>
 */
public class ListSharingPersonsResponse {
    private List<SharingPersonDTO> persons;

    public List<SharingPersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<SharingPersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
