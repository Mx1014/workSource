package com.everhomes.filedownload;

/**
 * <ul>
 *     <li>name: Excel表中列名</li>
 *     <li>key: object对应的属性名，用户反射获取数据</li>
 *     <li>length: Excel表中列的长度</li>
 * </ul>
 */
public class CellMapper {
    String name;
    String key;
    Long length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
