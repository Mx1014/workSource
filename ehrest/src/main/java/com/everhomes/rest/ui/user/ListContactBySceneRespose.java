package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contacts: 联系人列表，{@link com.everhomes.rest.ui.user.SceneContactDTO}</li>
 * </ul>
 */
public class ListContactBySceneRespose {
    @ItemType(SceneContactDTO.class)
    private List<SceneContactDTO> contacts;

    public ListContactBySceneRespose() {
    }

    public ListContactBySceneRespose(List<SceneContactDTO> contacts, Long nextPageAnchor) {
        super();
        this.contacts = contacts;
    }

    public List<SceneContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<SceneContactDTO> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
