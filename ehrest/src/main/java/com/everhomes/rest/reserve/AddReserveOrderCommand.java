package com.everhomes.rest.reserve;

import java.math.BigDecimal;

/**
 * @author sw on 2017/12/18.
 */
public class AddReserveOrderCommand {
    private String resourceType;
    private Long resourceTypeId;
    private String resourceJson;
    private Long reserveStartTime;
    private Long reserveEndTime;
    private Double reserveCellCount;
    private Long applicantEnterpriseId;
    private String applicantEnterpriseName;
    private String applicantPhone;
    private String applicantName;
    private Long addressId;
    private BigDecimal totalAmount;
}
