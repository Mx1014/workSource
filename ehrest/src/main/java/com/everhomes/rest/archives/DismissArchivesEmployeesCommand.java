package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>detailIds: (List)员工detailId</li>
 * <li>dismissType: 离职类型: 0-辞职,1-解雇,2-其他 参考{@link com.everhomes.rest.archives.ArchivesDismissType}</li>
 * <li>dismissReason: 离职原因 参考{@link com.everhomes.rest.archives.ArchivesDismissReason}</li>
 * <li>dismissTime: 离职日期</li>
 * <li>dismissRemark: 离职备注</li>
 * <li>logFlag: 0-无需人事记录, 1-需要人事记录</li>
 * </ul>
 */
public class DismissArchivesEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    private Byte dismissType;

    private Byte dismissReason;

    private String dismissTime;

    private String dismissRemark;

    //  同样的操作流程，只是操作类型名称不同时传递次参数 add by ryan.
    private Byte logFlag;


    public DismissArchivesEmployeesCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(String dismissTime) {
        this.dismissTime = dismissTime;
    }

    public String getDismissRemark() {
        return dismissRemark;
    }

    public void setDismissRemark(String dismissRemark) {
        this.dismissRemark = dismissRemark;
    }

    public Byte getLogFlag() {
        return logFlag;
    }

    public void setLogFlag(Byte logFlag) {
        this.logFlag = logFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
