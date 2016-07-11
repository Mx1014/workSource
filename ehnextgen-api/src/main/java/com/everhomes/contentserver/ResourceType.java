package com.everhomes.contentserver;

public enum ResourceType {
    image(1), audio(2), video(3), unknown(0);
    private Integer fileType;

    ResourceType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getCode() {
        return fileType;
    }

    public static ResourceType fromStringCode(String fileType) {
        for (ResourceType objType : ResourceType.values()) {
            if (objType.name().equalsIgnoreCase(fileType)) {
                return objType;
            }
        }
        return unknown;
    }
}
