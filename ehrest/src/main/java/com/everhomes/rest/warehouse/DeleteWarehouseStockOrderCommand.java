//@formatter:off
package com.everhomes.rest.warehouse;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
/**
 *<ul>
 * <li>id:出入库单的id</li>
 *</ul>
 */
public class DeleteWarehouseStockOrderCommand {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
