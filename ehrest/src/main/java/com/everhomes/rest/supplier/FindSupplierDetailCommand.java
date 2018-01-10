//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>supplierId:供应商id</li>
 *</ul>
 */
public class FindSupplierDetailCommand {
    private String supplierId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
