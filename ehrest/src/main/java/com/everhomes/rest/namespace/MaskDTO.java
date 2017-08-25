package com.everhomes.rest.namespace;

import com.everhomes.util.StringHelper;

/**
 * <ul> 蒙版信息
 * <li>id: ITEM的id</li>
 * <li>tips:提示</li>
 * <li>itemName: ITEM的名字</li>
 * <li>imageType: ITEM图片的类型(实际上是箭头的类型)</li>
 * </ul>
 */
public class MaskDTO {
    private Long id;
    private String tips;
    private String itemName;
    private Byte imageType;
    private String sceneType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public Byte getImageType() {
        return imageType;
    }

    public void setImageType(Byte imageType) {
        this.imageType = imageType;
    }
}
