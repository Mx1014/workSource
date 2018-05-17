// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>actionType: actionType</li>
 *     <li>instanceConfig: instanceConfig</li>
 *     <li>cards: 卡片信息{@link OPPushCard}</li>
 * </ul>
 */
public class ListOPPushCardsResponse {

    private Long moduleId;
    private Byte actionType;
    private String instanceConfig;
    private List<OPPushCard> cards;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
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
