package com.everhomes.activity;

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
    
    private SampleEnum sampleEnum;
    
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
    
    public SampleEnum getSampleEnum() {
        return sampleEnum;
    }

    public void setSampleEnum(SampleEnum sampleEnum) {
        this.sampleEnum = sampleEnum;
    }

    public Map<String, String> getMapValues() {
        return mapValues;
    }

    public void setMapValues(Map<String, String> mapValues) {
        this.mapValues = mapValues;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
