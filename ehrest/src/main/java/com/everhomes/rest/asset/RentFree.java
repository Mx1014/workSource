//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/18.
 */
/**
 *<ul>
 * <li>chargingItemId:收费项目id</li>
 * <li>amount:免租金额</li>
 * <li>startDate:起计日期</li>
 * <li>endDate:截止日期</li>
 * <li>properties:应用资源</li>
 * <li>remark:备注</li>
 *</ul>
 */
public class RentFree {
    private Long chargingItemId;
    private BigDecimal amount;
    private Date startDate;
    private Date endDate;
    @ItemType(String.class)
    private List<ContractProperty> properties;
    private String remark;

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ContractProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ContractProperty> properties) {
        this.properties = properties;
    }
}
