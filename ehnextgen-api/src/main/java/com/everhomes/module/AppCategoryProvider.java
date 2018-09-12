// @formatter:off
package com.everhomes.module;

import java.util.List;

public interface AppCategoryProvider {
    List<AppCategory> listAppCategories(Byte locationType, Long parentId);

    List<AppCategory> listLeafAppCategories(Byte locationType);

    Long findMaxDefaultOrder(Byte locationType, Long parentId);

    void delete(Long id);

    void create(AppCategory appCategory);

    AppCategory findById(Long id);

    void udpate(AppCategory appCategory);
}
