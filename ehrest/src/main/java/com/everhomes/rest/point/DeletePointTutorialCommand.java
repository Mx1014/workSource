package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>tutorialId: tutorialId</li>
 * </ul>
 */
public class DeletePointTutorialCommand {

    @NotNull
    private Long tutorialId;

    public Long getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(Long tutorialId) {
        this.tutorialId = tutorialId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
