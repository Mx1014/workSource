//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/24.
 */

public class FunctionDisableListDto {
    private Byte hasPay;
    private Byte hasContractView;

    public Byte getHasPay() {
        return hasPay;
    }

    public void setHasPay(Byte hasPay) {
        this.hasPay = hasPay;
    }

    public Byte getHasContractView() {
        return hasContractView;
    }

    public void setHasContractView(Byte hasContractView) {
        this.hasContractView = hasContractView;
    }
}
