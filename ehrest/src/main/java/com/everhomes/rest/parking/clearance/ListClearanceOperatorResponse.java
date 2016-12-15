// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Collections;
import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>operators: 人员列表 {@link com.everhomes.rest.parking.clearance.ParkingClearanceOperatorDTO}</li>
 * </ul>
 */
public class ListClearanceOperatorResponse {

    private Long nextPageAnchor;
    @ItemType(ParkingClearanceOperatorDTO.class)
    private List<ParkingClearanceOperatorDTO> operators;

    public ListClearanceOperatorResponse() {
        operators = Collections.emptyList();
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ParkingClearanceOperatorDTO> getOperators() {
        return operators;
    }

    public void setOperators(List<ParkingClearanceOperatorDTO> operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
