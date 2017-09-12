package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>projectName: 项目名称</li>
 *     <li>projectSource: id list split by ,</li>
 *     <li>projectAmount: 获批项目金额 万元为单位</li>
 *     <li>projectEstablishDate: 立项日期 时间戳</li>
 *     <li>projectCompleteDate: 完成日期 时间戳</li>
 *     <li>status: 状态</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class UpdateCustomerApplyProjectCommand {
    private Long id;
    private Byte customerType;
    private Long customerId;
    private String projectName;
    private String projectSource;
    private Long projectEstablishDate;
    private Long projectCompleteDate;
    private BigDecimal projectAmount;
    private Byte status;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(BigDecimal projectAmount) {
        this.projectAmount = projectAmount;
    }

    public Long getProjectCompleteDate() {
        return projectCompleteDate;
    }

    public void setProjectCompleteDate(Long projectCompleteDate) {
        this.projectCompleteDate = projectCompleteDate;
    }

    public Long getProjectEstablishDate() {
        return projectEstablishDate;
    }

    public void setProjectEstablishDate(Long projectEstablishDate) {
        this.projectEstablishDate = projectEstablishDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
