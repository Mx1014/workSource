// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>visitorReasonList: (必填)事由列表，{@link com.everhomes.rest.visitorsys.BaseVisitorReasonDTO}</li>
 * </ul>
 */
public class ListVisitReasonsResponse{
    @ItemType(BaseVisitorReasonDTO.class)
    private List<BaseVisitorReasonDTO> visitorReasonList;

    public List<BaseVisitorReasonDTO> getVisitorReasonList() {
        return visitorReasonList;
    }

    public void setVisitorReasonList(List<BaseVisitorReasonDTO> visitorReasonList) {
        this.visitorReasonList = visitorReasonList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}