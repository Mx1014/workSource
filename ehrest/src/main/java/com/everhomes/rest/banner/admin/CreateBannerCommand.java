package com.everhomes.rest.banner.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 命名空间</li>
 *     <li>name: 名称</li>
 *     <li>scopes: 发送范围，小区id列表</li>
 *     <li>posterPath: 图片uri</li>
 *     <li>targetType: 跳转类型 {@link com.everhomes.rest.banner.BannerTargetType}</li>
 *     <li>targetData: 跳转类型对应的data,每种targetType对应的data都不一样,将targetData对象转换成json字符串的形式</li>
 *     <li>categoryId: 应用入口ID</li>
 * </ul>
 */
public class CreateBannerCommand {

    @NotNull
    private Integer namespaceId;
    @NotNull
    private String name;
    @NotNull
    private List<Long> scopes;
    @NotNull
    private String posterPath;
    @NotNull
    private String targetType;
    private String targetData;
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getScopes() {
        return scopes;
    }

    public void setScopes(List<Long> scopes) {
        this.scopes = scopes;
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
