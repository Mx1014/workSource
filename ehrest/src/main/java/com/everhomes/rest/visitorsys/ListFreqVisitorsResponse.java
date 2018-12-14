package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>visitorDtoList: 访客列表，{@link com.everhomes.rest.visitorsys.BaseVisitorDTO}</li>
 * </ul>
 */
public class ListFreqVisitorsResponse {

    @ItemType(BaseVisitorDTO.class)
    private List<BaseVisitorDTO> visitorDtoList;


    public List<BaseVisitorDTO> getVisitorDtoList() {
        return visitorDtoList;
    }

    public void setVisitorDtoList(List<BaseVisitorDTO> visitorDtoList) {
        this.visitorDtoList = visitorDtoList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
