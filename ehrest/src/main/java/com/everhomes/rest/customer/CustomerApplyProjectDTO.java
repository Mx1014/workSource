package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>projectName: 项目名称</li>
 *     <li>projectSource: 项目来源id list split by ,</li>
 *     <li>projectSourceNames: 项目来源名称 以逗号分隔</li>
 *     <li>projectAmount: 获批项目金额 万元为单位</li>
 *     <li>projectEstablishDate: 立项日期 时间戳</li>
 *     <li>projectCompleteDate: 完成日期 时间戳</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class CustomerApplyProjectDTO {
    private Long id;
    private Byte customerType;
    private Long customerId;
    private String projectName;
    private String projectSource;
    private String projectSourceNames;
    private Timestamp projectEstablishDate;
    private Timestamp projectCompleteDate;
    private BigDecimal projectAmount;

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

    public Timestamp getProjectCompleteDate() {
        return projectCompleteDate;
    }

    public void setProjectCompleteDate(Timestamp projectCompleteDate) {
        this.projectCompleteDate = projectCompleteDate;
    }

    public Timestamp getProjectEstablishDate() {
        return projectEstablishDate;
    }

    public void setProjectEstablishDate(Timestamp projectEstablishDate) {
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

    public String getProjectSourceNames() {
        return projectSourceNames;
    }

    public void setProjectSourceNames(String projectSourceNames) {
        this.projectSourceNames = projectSourceNames;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
