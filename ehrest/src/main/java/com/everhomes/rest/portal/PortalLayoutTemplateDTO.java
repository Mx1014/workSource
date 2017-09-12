package com.everhomes.rest.portal;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id: 门户layout 模板的id</li>
 * <li>label: 门户layout 模板名称</li>
 * <li>showUrl: 模板图片url</li>
 * </ul>
 */
public class PortalLayoutTemplateDTO {

    private Long id;
    private String label;
    private String showUrl;

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

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
