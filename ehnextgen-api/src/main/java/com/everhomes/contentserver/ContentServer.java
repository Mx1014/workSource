package com.everhomes.contentserver;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.server.schema.tables.pojos.EhContentServer;

public class ContentServer extends EhContentServer {

    /**
     * 
     */
    private static final long serialVersionUID = -8311209673921529300L;

    public ContentServer() {
    }

    @Override
    public boolean equals(Object server) {
        return EqualsBuilder.reflectionEquals(this, server);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[] { "description", "name", "id" });
    }

}
