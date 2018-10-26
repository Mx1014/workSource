package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID，必填</li>
 * <li>departmentId: 查询的部门，必填</li>
 * <li>appId: 应用id</li>
 * <li>queryByDate: 查询的统计日期，日期时间戳，空值时默认当天，和queryByMonth二选一</li>
 * <li>queryByMonth: 查询的统计月份，格式:yyyyMM ，空值时默认当月，和queryByDate二选一</li>
 * <li>pageOffset: 页码，第一页不填或者1</li>
 * <li>pageSize: 每页返回记录数，（每页40条记录），必填</li>
 * </ul>
 */
public class ListPunchMembersCommand {
    private Long organizationId;
    private Long departmentId;
    private Long queryByDate;
    private Long appId;
    private String queryByMonth;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getQueryByDate() {
        return queryByDate;
    }

    public void setQueryByDate(Long queryByDate) {
        this.queryByDate = queryByDate;
    }

    public String getQueryByMonth() {
        return queryByMonth;
    }

    public void setQueryByMonth(String queryByMonth) {
        this.queryByMonth = queryByMonth;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
}
