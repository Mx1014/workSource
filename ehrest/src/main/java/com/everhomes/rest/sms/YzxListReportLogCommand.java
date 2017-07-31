// @formatter:off
package com.everhomes.rest.sms;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>mobile: mobile</li>
 *     <li>status: status</li>
 *     <li>failure: failure</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class YzxListReportLogCommand {

    private Integer namespaceId;
    private String mobile;
    private Byte status;
    private Byte failure;
    private Long pageAnchor;
    private Integer pageSize;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getFailure() {
        return failure;
    }

    public void setFailure(Byte failure) {
        this.failure = failure;
    }
}
