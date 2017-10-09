package com.everhomes.rest.news;

/**
 * <ul>
 * 参数
 * <li>newsTagId: 标签id</li>
 * <li>name: 标签key</li>
 * <li>value: 标签value</li>
 * </ul>
 */
public class NewsTagValsDTO {
    private Long newsTagId;
    private String name;
    private String value;


    public Long getNewsTagId() {
        return newsTagId;
    }

    public void setNewsTagId(Long newsTagId) {
        this.newsTagId = newsTagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
