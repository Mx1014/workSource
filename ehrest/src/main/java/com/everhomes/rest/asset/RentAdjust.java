//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/18.
 */

public class RentAdjust {
    //时间区间，对应的资产，调组的type，参数
    private Date start;
    private Date end;
    private Byte adjustType;
    private Float separationTime;
    private Byte seperationType;
    private Long adjustAmount;

    @ItemType(String.class)
    private List<ContractProperty> properties;
    private
}
