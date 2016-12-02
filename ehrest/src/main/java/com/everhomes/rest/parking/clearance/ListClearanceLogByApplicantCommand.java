// @formatter:off
package com.everhomes.rest.parking.clearance;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>communityId: 小区id</li>
 *     <li>applicantId: 申请人id</li>
 * </ul>
 */
public class ListClearanceLogByApplicantCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    private Long applicantId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
