package com.everhomes.rest.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SampleCommand {
    private String command;
    private SampleEmbedded embedded;
    
    @ItemType(SampleEmbedded.class)
    private List<SampleEmbedded> listValues;
    
    @ItemType(String.class)
    private Map<String, String> mapValues;
    
    private Byte sampleEnum;
    
    private Date javaDate = new Date();
    private Timestamp sqlTimestamp = new Timestamp(new Date().getTime());
    
    public SampleCommand() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public SampleEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(SampleEmbedded embedded) {
        this.embedded = embedded;
    }
    
    public List<SampleEmbedded> getListValues() {
        return listValues;
    }

    public void setListValues(List<SampleEmbedded> listValues) {
        this.listValues = listValues;
    }
    
    public Byte getSampleEnum() {
        return sampleEnum;
    }

    public void setSampleEnum(Byte sampleEnum) {
        this.sampleEnum = sampleEnum;
    }

    public Map<String, String> getMapValues() {
        return mapValues;
    }

    public void setMapValues(Map<String, String> mapValues) {
        this.mapValues = mapValues;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
