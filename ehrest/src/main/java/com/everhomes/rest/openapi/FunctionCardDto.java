package com.everhomes.rest.openapi;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

public class FunctionCardDto {
    private FunctionCategory categoryName;
    private List<String> jsonData;

    public FunctionCategory getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(FunctionCategory categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<String> jsonData) {
        this.jsonData = jsonData;
    }
}
