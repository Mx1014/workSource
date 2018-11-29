package com.everhomes.asset.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportExcelnitUtil {

    //表头信息集合
    private List<String> propertyNames = new ArrayList<String>();
    private List<String> titleName = new ArrayList<String>();
    private List<Integer> titleSize = new ArrayList<Integer>();

    //数据中文存储
    private Map<String,String> dataTag = new HashMap<String,String>();

    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public List<String> getTitleName() {
        return titleName;
    }

    public void setTitleName(List<String> titleName) {
        this.titleName = titleName;
    }

    public List<Integer> getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(List<Integer> titleSize) {
        this.titleSize = titleSize;
    }

    public Map<String, String> getDataTag() {
        return dataTag;
    }

    public void setDataTag(Map<String, String> dataTag) {
        this.dataTag = dataTag;
    }
}
