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
 * <li>start:起计时期</li>
 * <li>end:结束时期</li>
 * <li>adjustType:调整类型；1：按金额递增；2：按金额递减；3：按比例递增；4：按比例递减</li>
 * <li>separationTime:间隔时间</li>
 * <li>seperationType:间隔时间的类型；1：日；2：月：3：年</li>
 * <li>adjustAmplitude:调整幅度</li>
 * <li>properties:应用资源</li>
 *</ul>
 */
public class RentAdjust {
    //时间区间，对应的资产，调组的type，参数
    private Date start;
    private Date end;
    private Byte adjustType;
    private Float separationTime;
    private Byte seperationType;
    private Long adjustAmplitude;

    @ItemType(String.class)
    private List<ContractProperty> properties;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Byte getAdjustType() {
        return adjustType;
    }

    public void setAdjustType(Byte adjustType) {
        this.adjustType = adjustType;
    }

    public Float getSeparationTime() {
        return separationTime;
    }

    public void setSeparationTime(Float separationTime) {
        this.separationTime = separationTime;
    }

    public Byte getSeperationType() {
        return seperationType;
    }

    public void setSeperationType(Byte seperationType) {
        this.seperationType = seperationType;
    }

    public Long getAdjustAmplitude() {
        return adjustAmplitude;
    }

    public void setAdjustAmplitude(Long adjustAmplitude) {
        this.adjustAmplitude = adjustAmplitude;
    }

    public List<ContractProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ContractProperty> properties) {
        this.properties = properties;
    }
}
