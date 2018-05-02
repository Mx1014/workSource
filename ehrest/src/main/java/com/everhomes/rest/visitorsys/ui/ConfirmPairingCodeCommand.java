// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 *<ul>
 *<li>pairingCode : (必填)配对码</li>
 *</ul>
 */

public class ConfirmPairingCodeCommand {
    private String pairingCode;

    public String getPairingCode() {
        return pairingCode;
    }

    public void setPairingCode(String pairingCode) {
        this.pairingCode = pairingCode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
