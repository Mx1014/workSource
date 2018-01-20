package com.everhomes.rest.openapi;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

public class FunctionCardDto {
<<<<<<< HEAD
    private String name;
    private byte  type;
    private list<string> giinfo;

=======
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
>>>>>>> 26140dd856e44df0657bb940ac0b9e5c122e7806
}

public class FuctionCardCategory{
    private String name;
    private List<FunctionCardDto> dtos;
}