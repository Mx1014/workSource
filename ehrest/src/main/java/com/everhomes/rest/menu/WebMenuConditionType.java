// @formatter:off
package com.everhomes.rest.menu;

/**
 * <ul>
 * <li>SYSTEM：跟系统直接相关不需要条件（不需要有筛选条件）</li>
 * <li>NAMESPACE：前提必须要有域空间（有域空间的筛选条件），才能查看到内容</li>
 * <li>PROJECT：前提条件是项目，而查询项目的前提条件要有域空间（有域空间的筛选条件，联动出项目筛选条件）</li>
 * <li>ORGANIZATION：前提条件是机构，机构的前提条件要有域空间（有域空间的筛选条件，联动出机构的筛选条件）</li>
 * </ul>
 */
public enum WebMenuConditionType {

    SYSTEM("system"), NAMESPACE("namespace"), PROJECT("project"), ORGANIZATION("organization");

    private String code;

    private WebMenuConditionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static WebMenuConditionType fromCode(String code) {
        WebMenuConditionType[] values = WebMenuConditionType.values();
        for (WebMenuConditionType value: values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
}
