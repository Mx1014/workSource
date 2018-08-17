package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>address：门牌号</li>
 * <li>applyName：申请人姓名</li>
 * <li>applyPhone：申请人电话</li>
 * <li>applyCompany：申请人公司</li>
 * <li>decoratorUid：负责人用户id</li>
 * <li>decoratorName：负责人姓名</li>
 * <li>decoratorPhone：负责人电话</li>
 * <li>decoratorCompany：负责人公司</li>
 * <li>decoratorCompanyId：装修公司id</li>
 * <li>communityId：项目id</li>
 * </ul>
 */
public class CreateRequestCommand {

    private String applyName;
    private String applyPhone;
    private String applyCompany;
    private String address;
    private Long startTime;
    private Long endTime;
    private Long communityId;

    private Long decoratorUid;
    private String decoratorName;
    private String decoratorPhone;
    private Long decoratorCompanyId;
    private String decoratorCompany;

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyPhone() {
        return applyPhone;
    }

    public void setApplyPhone(String applyPhone) {
        this.applyPhone = applyPhone;
    }

    public String getApplyCompany() {
        return applyCompany;
    }

    public void setApplyCompany(String applyCompany) {
        this.applyCompany = applyCompany;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Long getDecoratorUid() {
        return decoratorUid;
    }

    public void setDecoratorUid(Long decoratorUid) {
        this.decoratorUid = decoratorUid;
    }

    public String getDecoratorName() {
        return decoratorName;
    }

    public void setDecoratorName(String decoratorName) {
        this.decoratorName = decoratorName;
    }

    public String getDecoratorPhone() {
        return decoratorPhone;
    }

    public void setDecoratorPhone(String decoratorPhone) {
        this.decoratorPhone = decoratorPhone;
    }

    public Long getDecoratorCompanyId() {
        return decoratorCompanyId;
    }

    public void setDecoratorCompanyId(Long decoratorCompanyId) {
        this.decoratorCompanyId = decoratorCompanyId;
    }

    public String getDecoratorCompany() {
        return decoratorCompany;
    }

    public void setDecoratorCompany(String decoratorCompany) {
        this.decoratorCompany = decoratorCompany;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
}
