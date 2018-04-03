//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/8.
 */
/**
 *<ul>
 * <li>billId:合同编号</li>
 * <li>targetType:客户类型，个人eh_user;企业：eh_organization</li>
 *</ul>
 */
public class BillIdAndType {
    private String billId;
    private String targetType;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
