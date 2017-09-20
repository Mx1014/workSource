package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>detailIds: (List)员工detailId</li>
 * <li>dismissType: 离职类型: 0-辞职,1-解雇,2-其他 参考{@link com.everhomes.rest.archives.ArchivesDismissType}</li>
 * <li>dismissReason: 离职原因 参考{@link com.everhomes.rest.archives.ArchivesDismissReason}</li>
 * <li>dismissTime: 离职日期</li>
 * <li>dismissRemark: 离职备注</li>
 * </ul>
 */
public class DismissArchivesEmployeesCommand {

    private Long organizationId;

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Byte dismissType;

    private Byte dismissReason;

    private Date dismissTime;

    private String dismissRemark;

    public DismissArchivesEmployeesCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
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

    public Date getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(String dismissTime) {
        this.dismissTime = ArchivesUtil.parseDate(dismissTime);
    }

    public void setDismissTime(Date dismissTime) {
        this.dismissTime = dismissTime;
    }

    public String getDismissRemark() {
        return dismissRemark;
    }

    public void setDismissRemark(String dismissRemark) {
        this.dismissRemark = dismissRemark;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
