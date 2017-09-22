//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/8/30.
 */
/**
 *<ul>
 * <li>contractNum:合同编号</li>
 * <li>pageOffset:偏移</li>
 * <li>pageSize:页大小</li>
 *</ul>
 */
public class ListBillExpectanciesOnContractCommand {
    private String contractNum;
    private Integer pageOffset;
    private Integer pageSize;

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
