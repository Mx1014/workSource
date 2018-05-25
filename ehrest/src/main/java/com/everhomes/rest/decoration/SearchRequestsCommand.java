package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>communityId：项目id</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>buildingName：楼栋名</li>
 * <li>doorPlate：门牌号</li>
 * <li>status：申请状态 参考{@link com.everhomes.rest.decoration.DecorationRequestStatus}</li>
 * <li>cancelFlag：是否取消 0未取消 1取消</li>
 * <li>keyword：关键字</li>
 * <li>cancelFlag：是否取消 </li>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * </ul>
 */
public class SearchRequestsCommand {

    private Long communityId;
    private Long startTime;
    private Long endTime;
    private String buildingName;
    private String doorPlate;
    private Byte status;
    private String keyword;
    private Byte cancelFlag;

    private Long pageAnchor;
    private Integer pageSize;
    private Long currentPMId;
    private Long currentProjectId;
    private Long appId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Byte getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(Byte cancelFlag) {
        this.cancelFlag = cancelFlag;
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

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getDoorPlate() {
        return doorPlate;
    }

    public void setDoorPlate(String doorPlate) {
        this.doorPlate = doorPlate;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
