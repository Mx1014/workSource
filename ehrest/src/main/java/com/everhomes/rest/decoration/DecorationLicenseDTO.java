package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>workerType：工种</li>
 * <li>qrUrl：二维码地址</li>
 * <li>name</li>
 * </ul>
 */
public class DecorationLicenseDTO {

    private String name;
    private String workerType;
    private String qrUrl;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
