package com.everhomes.rest.rentalv2;

public class SiteStructureDTO {
    private Long id;
    private String name;
    private String displayName;
    private String iconUri;
    private String iconUrl;
    private Byte   isSurport;
    private Long defaultOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Byte getIsSurport() {
        return isSurport;
    }

    public void setIsSurport(Byte isSurport) {
        this.isSurport = isSurport;
    }

    public Long getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Long defaultOrder) {
        this.defaultOrder = defaultOrder;
    }
}
