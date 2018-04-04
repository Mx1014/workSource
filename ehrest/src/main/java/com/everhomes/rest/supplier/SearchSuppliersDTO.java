//@formatter:off
package com.everhomes.rest.supplier;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>supplierId:供应商id</li>
 * <li>supplierName:供应商名字</li>
 *</ul>
 */
public class SearchSuppliersDTO {
    private Long supplierId;
    private String supplierName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
