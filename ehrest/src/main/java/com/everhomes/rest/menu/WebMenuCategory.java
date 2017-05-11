// @formatter:off
package com.everhomes.rest.menu;

/**
 * <ul>
 * <li>CLASSIFY：对应的是菜单的分类</li>
 * <li>MODULE：对应的是模块，菜单最后的点击节点</li>
 * <li>PAGE：菜单展示tab的形式在页面</li>
 * </ul>
 */
public enum WebMenuCategory {

    CLASSIFY("classify"), MODULE("module"), PAGE("page");

    private String code;

    private WebMenuCategory(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static WebMenuCategory fromCode(String code) {
        WebMenuCategory[] values = WebMenuCategory.values();
        for (WebMenuCategory value: values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
}
