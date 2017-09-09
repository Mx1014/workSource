//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/1.
 */

/**
 *<ul>
 * <li>contractId:合同id</li>
 * <li>contractNum:合同编号</li>
 *</ul>
 */
public class FindUserInfoForPaymentDTO {
    private String contractId;
    private String contractNum;


    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
