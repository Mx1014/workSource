package com.everhomes.user;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.server.schema.tables.pojos.EhUserContacts;

public class UserContact extends EhUserContacts {

    /**
     * 
     */
    private static final long serialVersionUID = -6464340944212831274L;

    public UserContact() {
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[] { "createTime", "id", "contactId" });
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EhUserContacts))
            return false;
        return EqualsBuilder.reflectionEquals(this, obj, new String[] { "createTime", "id", "contactId" });
    }

}
