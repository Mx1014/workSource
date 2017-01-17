package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>utn：用户总数</li>
 * <li>autn：已授权总用户数</li>
 * <li>uautn：未授权总用户数</li>
 * <li>acutn：已授权认证用户数</li>
 * <li>aucutn：已授权未认证用户数</li>
 * <li>uacutn：未授权认证用户数</li>
 * <li>uaucutn：未授权未认证用户数</li>
 * </ul>
 */
public class DoorAuthStatisticsDTO {

    private Long utn;

    private Long autn;

    private Long uautn;

    private Long acutn;

    private Long aucutn;

    private Long uacutn;

    private Long uaucutn;

    public Long getUtn() {
        return utn;
    }

    public void setUtn(Long utn) {
        this.utn = utn;
    }

    public Long getAutn() {
        return autn;
    }

    public void setAutn(Long autn) {
        this.autn = autn;
    }

    public Long getUautn() {
        return uautn;
    }

    public void setUautn(Long uautn) {
        this.uautn = uautn;
    }

    public Long getAcutn() {
        return acutn;
    }

    public void setAcutn(Long acutn) {
        this.acutn = acutn;
    }

    public Long getAucutn() {
        return aucutn;
    }

    public void setAucutn(Long aucutn) {
        this.aucutn = aucutn;
    }

    public Long getUacutn() {
        return uacutn;
    }

    public void setUacutn(Long uacutn) {
        this.uacutn = uacutn;
    }

    public Long getUaucutn() {
        return uaucutn;
    }

    public void setUaucutn(Long uaucutn) {
        this.uaucutn = uaucutn;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
