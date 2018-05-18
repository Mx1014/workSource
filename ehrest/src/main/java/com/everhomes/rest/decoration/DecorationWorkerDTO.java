package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>workerType：工种</li>
 * <li>id</li>
 * <li>name</li>
 * <li>phone</li>
 * <li>imageUrl</li>
 * <li>uid</li>
 * <li>imageUri</li>
 * </ul>
 */
public class DecorationWorkerDTO {
    private Long id;
    private Long uid;
    private String name;
    private String phone;
    private String workerType;
    private String imageUrl;
    private String imageUri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkerType() {
        return workerType;
    }

    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
