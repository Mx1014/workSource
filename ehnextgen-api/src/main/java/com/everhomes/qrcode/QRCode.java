// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.server.schema.tables.pojos.EhQrcodes;
import com.everhomes.util.StringHelper;

@Deprecated
public class QRCode extends EhQrcodes {
    private static final long serialVersionUID = 2339714226288344317L;

    public QRCode() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
