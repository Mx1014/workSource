package com.everhomes.rest.profile;

/**
 * <ul>
 * <li>organizationId: 节点 Id (根据当前节点置顶)</li>
 * <li>detailId: 用户 detailId</li>
 * <li>stick: 置顶状态: 0-取消置顶 1-置顶</li>
 * </ul>
 */
public class StickProfileContactsCommand {

    private Long organizationId;

    private Long detailId;

    private String stick;

    public StickProfileContactsCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;

    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }
}
