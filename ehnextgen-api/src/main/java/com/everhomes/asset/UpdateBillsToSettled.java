//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/9/3.
 */

/**
 *<ul>
 * <li>contractId:合同id</li>
 * <li>ownerType:所属者类型，一般为园区community</li>
 * <li>ownerId:所属者id，一般为园区id</li>
 *</ul>
 */
public class UpdateBillsToSettled {
    private Long contractId;
    private String ownerType;
    private Long ownerId;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
