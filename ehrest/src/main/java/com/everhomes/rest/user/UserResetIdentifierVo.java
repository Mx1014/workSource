// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>uid: user id</li>
 *     <li>oldIdentifier: 旧手机号</li>
 *     <li>oldRegionCode: oldRegionCode</li>
 *     <li>newIdentifier: 新手机号</li>
 *     <li>newRegionCode: newRegionCode</li>
 * </ul>
 */
public class UserResetIdentifierVo {

    private Long uid;
    private String oldIdentifier;
    private Integer oldRegionCode;
    private String newIdentifier;
    private Integer newRegionCode;

    public UserResetIdentifierVo(Long uid, String oldIdentifier, Integer oldRegionCode, String newIdentifier, Integer newRegionCode) {
        this.uid = uid;
        this.oldIdentifier = oldIdentifier;
        this.oldRegionCode = oldRegionCode;
        this.newIdentifier = newIdentifier;
        this.newRegionCode = newRegionCode;
    }

    public UserResetIdentifierVo() {
    }

    public String getOldIdentifier() {
        return oldIdentifier;
    }

    public void setOldIdentifier(String oldIdentifier) {
        this.oldIdentifier = oldIdentifier;
    }

    public Integer getOldRegionCode() {
        return oldRegionCode;
    }

    public void setOldRegionCode(Integer oldRegionCode) {
        this.oldRegionCode = oldRegionCode;
    }

    public String getNewIdentifier() {
        return newIdentifier;
    }

    public void setNewIdentifier(String newIdentifier) {
        this.newIdentifier = newIdentifier;
    }

    public Integer getNewRegionCode() {
        return newRegionCode;
    }

    public void setNewRegionCode(Integer newRegionCode) {
        this.newRegionCode = newRegionCode;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
