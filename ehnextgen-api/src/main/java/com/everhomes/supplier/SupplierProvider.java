//@formatter:off
package com.everhomes.supplier;

import com.everhomes.rest.supplier.ListSuppliersDTO;
import com.everhomes.rest.supplier.SearchSuppliersDTO;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Wentian on 2018/1/9.
 */
public interface SupplierProvider {
    WarehouseSupplier findSupplierById(Long id);

    void insertSupplier(WarehouseSupplier supplier);

    void updateSupplier(WarehouseSupplier supplier);

    String getNewIdentity();

    void deleteSupplier(Long id);

    TreeMap<Long,ListSuppliersDTO> findSuppliers(String ownerType, Long ownerId, Integer namespaceId, String contactName, String supplierName
            , Long pageAnchor, Integer pageSize, Long communityId);

    String findSupplierNameById(Long supplierId);

    List<SearchSuppliersDTO> findSuppliersByKeyword(String nameKeyword);
}
