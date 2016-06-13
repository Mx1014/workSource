package com.everhomes.rest.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SampleObject {
    private Byte byteValue;
    private Integer intValue;
    private Long longValue;
    private Float floatValue;
    private Double dblValue;
    private Date javaDate;
    private Timestamp sqlTimestamp;
    
    @ItemType(SampleEmbedded.class)
    private List<SampleEmbedded> embeddedList; 
    
    @ItemType(String.class)
    private Map<String, String> embeddedMap;

    public SampleObject() {
    }

    public Byte getByteValue() {
        return byteValue;
    }

    public void setByteValue(Byte byteValue) {
        this.byteValue = byteValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    public Double getDblValue() {
        return dblValue;
    }

    public void setDblValue(Double dblValue) {
        this.dblValue = dblValue;
    }

    public Date getJavaDate() {
        return javaDate;
    }

    public void setJavaDate(Date javaDate) {
        this.javaDate = javaDate;
    }

    public Timestamp getSqlTimestamp() {
        return sqlTimestamp;
    }

    public void setSqlTimestamp(Timestamp sqlTimestamp) {
        this.sqlTimestamp = sqlTimestamp;
    }

    public List<SampleEmbedded> getEmbeddedList() {
        return embeddedList;
    }

    public void setEmbeddedList(List<SampleEmbedded> embeddedList) {
        this.embeddedList = embeddedList;
    }

    public Map<String, String> getEmbeddedMap() {
        return embeddedMap;
    }

    public void setEmbeddedMap(Map<String, String> embeddedMap) {
        this.embeddedMap = embeddedMap;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
