package com.everhomes.rest.rentalv2.admin;

public class UpdateStructureAdminCommand {
    private Long id;
    private String displayName;
    private String iconUri;
    private Byte isSurport;
    private Long defaultOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
