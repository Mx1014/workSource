package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>dismissTimeStart: 离职起始日期</li>
 * <li>dismissTimeEnd: 离职结束日期</li>
 * <li>checkInTimeStart: 入职起始日期</li>
 * <li>checkInTimeEnd: 入职结束日期</li>
 * <li>contractPartyId: 合同主体id</li>
 * <li>dismissType: 离职类型： 0-辞职 1-解雇 2-其他 参考{@link com.everhomes.rest.archives.ArchivesDismissType}</li>
 * <li>dismissReason: 离职原因 参考{@link com.everhomes.rest.archives.ArchivesDismissReason}</li>
 * <li>contactName: 姓名</li>
 * <li>pageAnchor: 页码(不能为null)</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListArchivesDismissEmployeesCommand {

    private Long organizationId;

    private String dismissTimeStart;

    private String dismissTimeEnd;

    private String checkInTimeStart;

    private String checkInTimeEnd;

    private Long contractPartyId;

    private Byte dismissType;

    private Byte dismissReason;

    private String contactName;

    private Integer pageAnchor;

    private Integer pageSize;

    public ListArchivesDismissEmployeesCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getDismissTimeStart() {
        return dismissTimeStart;
    }

    public void setDismissTimeStart(String dismissTimeStart) {
        this.dismissTimeStart = dismissTimeStart;
    }

    public String getDismissTimeEnd() {
        return dismissTimeEnd;
    }

    public void setDismissTimeEnd(String dismissTimeEnd) {
        this.dismissTimeEnd = dismissTimeEnd;
    }

    public String getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(String checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public String getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(String checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public Long getContractPartyId() {
        return contractPartyId;
    }

    public void setContractPartyId(Long contractPartyId) {
        this.contractPartyId = contractPartyId;
    }

    public Byte getDismissType() {
        return dismissType;
    }

    public void setDismissType(Byte dismissType) {
        this.dismissType = dismissType;
    }

    public Byte getDismissReason() {
        return dismissReason;
    }

    public void setDismissReason(Byte dismissReason) {
        this.dismissReason = dismissReason;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Integer pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
