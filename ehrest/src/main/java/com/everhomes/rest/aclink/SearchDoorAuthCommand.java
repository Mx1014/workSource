// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>doorId: 门禁id</li>
 * <li>keyword: 搜索关键字，名称或电话号码模糊查询</li>
 * <li>status: 门禁授权状态，{@link com.everhomes.rest.aclink.DoorAuthStatus}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页数量</li>
 * </ul>
 */
public class SearchDoorAuthCommand {
    private Long pageAnchor;
    
    @NotNull
    private Long doorId;
    
    private Integer pageSize;

    private Timestamp createTimeStart;

    private Timestamp createTimeEnd;

    private Long userId;

//    @NotNull
//    private Integer namespaceId;
    
    private String keyword;
    
    private Byte status;

    public Timestamp getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(Timestamp createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Timestamp getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Timestamp createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
