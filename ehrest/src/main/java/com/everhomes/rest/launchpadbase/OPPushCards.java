// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>moreCardRouter: 更多</li>
 *     <li>cardJsons: cardJsons 具体的业务提供卡片信息，模型由各个业务跟客户端定</li>
 * </ul>
 */
public class OPPushCards {

    private String moreCardRouter;
    private List<String> cardJsons;

    public String getMoreCardRouter() {
        return moreCardRouter;
    }

    public void setMoreCardRouter(String moreCardRouter) {
        this.moreCardRouter = moreCardRouter;
    }

    public List<String> getCardJsons() {
        return cardJsons;
    }

    public void setCardJsons(List<String> cardJsons) {
        this.cardJsons = cardJsons;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
