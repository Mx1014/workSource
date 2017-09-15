//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>targetName:客户名称</li>
 * <li>dateStr:账期</li>
 * <li>pageSize: 显示数量</li>
 * <li>pageAnchor: 锚点</li>
 *</ul>
 */
public class listBillExemtionItemsCommand {
    private String billId;
    private String targetName;
    private String dateStr;
    private Integer pageSize;
    private Long pageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getTargetName() {
        return targetName;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public listBillExemtionItemsCommand() {

    }
}
