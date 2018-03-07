package com.everhomes.rest.banner.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: banner id</li>
 *     <li>name: name</li>
 *     <li>posterPath: 图片路径</li>
 *     <li>targetType: 跳转类型 {@link com.everhomes.rest.banner.BannerTargetType}</li>
 *     <li>targetData: 跳转类型对应的data,每种targetType对应的data都不一样,将targetData对象转换成json字符串的形式</li>
 * </ul>
 */
public class UpdateBannerCommand {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String posterPath;
    @NotNull
    private String targetType;
    private String targetData;

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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
