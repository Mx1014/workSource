// @formatter:off
package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: user id</li>
 *     <li>clubType: clubType  NORMAL-0, Guild(行业协会)-1 参考{@link ClubType}</li>
 * </ul>
 */
public class ListPublicGroupCommand {
    @NotNull
    private Long userId;

    private Byte clubType;

    public ListPublicGroupCommand() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getClubType() {
        return clubType;
    }

    public void setClubType(Byte clubType) {
        this.clubType = clubType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
