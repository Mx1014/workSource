//@formatter:off
package com.everhomes.supplier;

import com.everhomes.rest.supplier.*;

import java.util.List;

/**
 * Created by Wentian on 2018/1/9.
 */
public interface SupplierService {
    void createOrUpdateOneSupplier(CreateOrUpdateOneSupplierCommand cmd);

    void deleteSupplier(DeleteOneSupplierCommand cmd);

    ListSuppliersResponse listSuppliers(ListSuppliersCommand cmd);

    GetSupplierDetailDTO getSupplierDetail(GetSupplierDetailCommand cmd);

    List<SearchSuppliersDTO> searchSuppliers(String nameKeyword);
}
