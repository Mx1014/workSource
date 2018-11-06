package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>id: 门户layout id</li>
 *     <li>label: 门户layout名称</li>
 *     <li>description: 门户layout描述</li>
 *     <li>type: layout类型，1-首页，2-自定义门户，3-分页签门户，参考{@link PortalLayoutType}</li>
 *     <li>indexFlag: 0-否，1-是，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>bgColor: 背景颜色</li>
 * </ul>
 */
public class PortalLayoutDTO {
    private Long id;

    private String label;

    private String description;

    private Byte type;

    private Byte indexFlag;

    private Long bgColor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getBgColor() {
        return bgColor;
    }

    public void setBgColor(Long bgColor) {
        this.bgColor = bgColor;
    }

    public Byte getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Byte indexFlag) {
        this.indexFlag = indexFlag;
    }
}
