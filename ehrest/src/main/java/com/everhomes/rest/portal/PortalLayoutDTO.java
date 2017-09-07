package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>id: 门户layout id</li>
 * <li>label: 门户layout名称</li>
 * <li>description: 门户layout描述</li>
 * <li>layoutTemplateId: 门户layout的模板id</li>
 * </ul>
 */
public class PortalLayoutDTO {
    private Long id;

    private String label;

    private String description;

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
}
