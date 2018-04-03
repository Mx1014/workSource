//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/22.
 */
/**
 *<ul>
 * <li>hasArrearage:是否有欠费的账单，1：是；0：不是</li>
 *</ul>
 */
public class CheckEnterpriseHasArrearageResponse {
    private Byte hasArrearage;

    public Byte getHasArrearage() {
        return hasArrearage;
    }

    public void setHasArrearage(Byte hasArrearage) {
        this.hasArrearage = hasArrearage;
    }
}
