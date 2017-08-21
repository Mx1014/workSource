package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一页起始锚点</li>
 * <li>contacts: 成员信息，参考{@link ArchivesContactDTO}</li>
 * </ul>
 */
public class ListArchivesContactsResponse {

    private Long nextPageAnchor;

    @ItemType(ArchivesContactDTO.class)
    private List<ArchivesContactDTO> contacts;

    public ListArchivesContactsResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ArchivesContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ArchivesContactDTO> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
