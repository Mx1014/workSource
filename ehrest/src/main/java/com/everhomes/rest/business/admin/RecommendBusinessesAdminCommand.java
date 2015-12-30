package com.everhomes.rest.business.admin;



import java.util.List;

import com.everhomes.rest.business.BusinessScope;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 店铺id</li>
 * <li>recommendStatus: 是否推荐，参考{@link com.everhomes.rest.business.BusinessRecommendStatus}</li>
 * <li>scopes: 店铺推荐范围列表，参考{@link com.everhomes.rest.business.BusinessScope}</li>
 * </ul>
 */

public class RecommendBusinessesAdminCommand{
    private Long id;
    private Byte recommendStatus;
    @ItemType(BusinessScope.class)
    private List<BusinessScope> scopes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Byte recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public List<BusinessScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<BusinessScope> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
