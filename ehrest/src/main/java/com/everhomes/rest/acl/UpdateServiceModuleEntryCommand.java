package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>entryName: entryName</li>
 *     <li>iconUri: iconUri</li>
 * </ul>
 */
public class UpdateServiceModuleEntryCommand {

    private Long id;
    private String entryName;
    private String iconUri;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }


    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
