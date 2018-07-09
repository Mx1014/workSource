package com.everhomes.rest.decoration;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>id</li>
 * <li>createTime：创建时间</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>address：门牌号</li>
 * <li>applyName：申请人姓名</li>
 * <li>applyPhone：申请人电话</li>
 * <li>applyCompany：申请人公司</li>
 * <li>status：申请状态 参考{@link com.everhomes.rest.decoration.DecorationRequestStatus}</li>
 * <li>processorType：操作人类型 参考{@link com.everhomes.rest.decoration.ProcessorType}</li>
 * <li>cancelFlag：是否取消 0未取消 1工作流取消 2后台取消</li>
 * <li>decoratorName：负责人姓名</li>
 * <li>decoratorPhone：负责人电话</li>
 * <li>decoratorCompany：负责人公司</li>
 * <li>flowCasees：工作流 参考{@link com.everhomes.rest.decoration.DecorationFlowCaseDTO}</li>
 * <li>decorationFee：装修费用明细</li>
 * <li>totalAmount：装修费用</li>
 * <li>refoundAmount：退款</li>
 * <li>refoundComment：退款备注</li>
 * </ul>
 */
public class DecorationRequestDTO {

    private Long id;
    private  Long createTime;
    private String address;
    private Long startTime;
    private Long endTime;
    private String applyName;
    private String applyPhone;
    private String applyCompany;
    private Byte status;
    private Byte processorType;
    private Byte cancelFlag;
    private String cancelReason;

    private String decoratorName;
    private String decoratorPhone;
    private String decoratorCompany;
    private List<DecorationFlowCaseDTO> flowCasees;
    private List<DecorationFeeDTO> decorationFee;
    private String totalAmount;
    private BigDecimal refoundAmount;
    private String refoundComment;



    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<DecorationFeeDTO> getDecorationFee() {
        return decorationFee;
    }

    public void setDecorationFee(List<DecorationFeeDTO> decorationFee) {
        this.decorationFee = decorationFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRefoundAmount() {
        return refoundAmount;
    }

    public void setRefoundAmount(BigDecimal refoundAmount) {
        this.refoundAmount = refoundAmount;
    }

    public String getRefoundComment() {
        return refoundComment;
    }

    public void setRefoundComment(String refoundComment) {
        this.refoundComment = refoundComment;
    }

    public Byte getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Byte processorType) {
        this.processorType = processorType;
    }

    public Byte getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(Byte cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
