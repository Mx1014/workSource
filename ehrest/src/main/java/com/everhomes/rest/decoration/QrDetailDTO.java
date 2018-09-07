package com.everhomes.rest.decoration;

import java.util.List;


/**
 * <ul>
 * <li>name</li>
 * <li>workerType</li>
 * <li>phone</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>address：门牌号</li>
 * <li>applyName：申请人姓名</li>
 * <li>applyPhone：申请人电话</li>
 * <li>applyCompany：申请人公司</li>
 * <li>decoratorName：负责人姓名</li>
 * <li>decoratorPhone：负责人电话</li>
 * <li>decoratorCompany：负责人公司</li>
 * <li>flowCasees：工作流 List 参考{@link com.everhomes.rest.decoration.DecorationFlowCaseDTO}</li>
 * </ul>
 */
public class QrDetailDTO {

    private String name;
    private String workerType;
    private String phone;

    private String address;
    private Long startTime;
    private Long endTime;
    private String applyName;
    private String applyPhone;
    private String applyCompany;
    private String decoratorName;
    private String decoratorPhone;
    private String decoratorCompany;
    private String imageUrl;
    private List<DecorationFlowCaseDTO> flowCasees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getDecoratorCompany() {
        return decoratorCompany;
    }

    public void setDecoratorCompany(String decoratorCompany) {
        this.decoratorCompany = decoratorCompany;
    }

    public List<DecorationFlowCaseDTO> getFlowCasees() {
        return flowCasees;
    }

    public void setFlowCasees(List<DecorationFlowCaseDTO> flowCasees) {
        this.flowCasees = flowCasees;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
