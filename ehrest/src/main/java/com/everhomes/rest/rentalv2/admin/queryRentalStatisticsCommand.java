package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */
public class queryRentalStatisticsCommand {

    private BigDecimal totalAmount;
    private Integer orderAmount;
    @ItemType(Tuple.class)
    private List<Tuple<String,BigDecimal>> classifyAmount;

}
