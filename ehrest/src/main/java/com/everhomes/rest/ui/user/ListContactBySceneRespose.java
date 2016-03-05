package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
/**
 * <ul>
 * <li>contacts : 联系人列表，{@link com.everhomes.rest.user.ContactV2DTO}</li>
 * <li>nextPageAnchor : 下一页页码</li>
 * </ul>
 */
public class ListContactBySceneRespose {
    @ItemType(SceneContactDTO.class)
    private List<SceneContactDTO> contacts;

    private Long nextPageAnchor;

    public ListContactBySceneRespose() {
    }

    public ListContactBySceneRespose(List<SceneContactDTO> contacts, Long nextPageAnchor) {
        super();
        this.contacts = contacts;
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SceneContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<SceneContactDTO> contacts) {
        this.contacts = contacts;
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
