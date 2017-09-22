package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>customerType: 所属客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 所属客户id</li>
 *     <li>name: 证书名称</li>
 *     <li>patentName: 专利名称</li>
 *     <li>applicationNumber: 授权号</li>
 *     <li>registeDate: 注册日期 时间戳</li>
 *     <li>patentStatusItemId: 专利状态</li>
 *     <li>patentStatusItemName: 专利状态名</li>
 *     <li>patentTypeItemId: 专利类型</li>
 *     <li>patentTypeItemName: 专利类型名</li>
 * </ul>
 * Created by ying.xiong on 2017/8/19.
 */
public class CustomerPatentDTO {
    private Long id;
    private Long customerId;
    private String name;
    private Timestamp registeDate;
    private Long patentStatusItemId;
    private String patentStatusItemName;
    private Long patentTypeItemId;
    private String patentTypeItemName;
    private String patentName;
    private String applicationNumber;

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public Long getPatentStatusItemId() {
        return patentStatusItemId;
    }

    public void setPatentStatusItemId(Long patentStatusItemId) {
        this.patentStatusItemId = patentStatusItemId;
    }

    public String getPatentStatusItemName() {
        return patentStatusItemName;
    }

    public void setPatentStatusItemName(String patentStatusItemName) {
        this.patentStatusItemName = patentStatusItemName;
    }

    public String getPatentTypeItemName() {
        return patentTypeItemName;
    }

    public void setPatentTypeItemName(String patentTypeItemName) {
        this.patentTypeItemName = patentTypeItemName;
    }

    public Long getPatentTypeItemId() {
        return patentTypeItemId;
    }

    public void setPatentTypeItemId(Long patentTypeItemId) {
        this.patentTypeItemId = patentTypeItemId;
    }

    public Timestamp getRegisteDate() {
        return registeDate;
    }

    public void setRegisteDate(Timestamp registeDate) {
        this.registeDate = registeDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
