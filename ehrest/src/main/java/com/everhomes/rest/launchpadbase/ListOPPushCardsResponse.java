// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>moreCardRouter: 跳转更多路由</li>
 *     <li>cards: 卡片信息{@link OPPushCard} </li>
 * </ul>
 */
public class ListOPPushCardsResponse {

    private String moreCardRouter;
    private List<OPPushCard> cards;

    public String getMoreCardRouter() {
        return moreCardRouter;
    }

    public void setMoreCardRouter(String moreCardRouter) {
        this.moreCardRouter = moreCardRouter;
    }

    public List<OPPushCard> getCards() {
        return cards;
    }

    public void setCards(List<OPPushCard> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
