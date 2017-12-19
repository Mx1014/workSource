package com.everhomes.rest.reserve;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveOrderDTO {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String resourceType;
    private Long resourceTypeId;
    private String resourceJson;
    private String orderNo;
    private Timestamp reserveStartTime;
    private Timestamp reserveEndTime;
    private Double reserveCellCount;
    private Timestamp actualStartTime;
    private Timestamp actualEndTime;
    private Long applicantEnterpriseId;
    private String applicantEnterpriseName;
    private String applicantPhone;
    private String applicantName;
    private Long addressId;
    private Long overTime;
    private Timestamp payTime;
    private String payType;
    private BigDecimal paidAmount;
    private BigDecimal owingAmount;
    private BigDecimal totalAmount;
    private Byte status;
    private Long creatorUid;
    private Timestamp createTime;
    private Long updateUid;
    private Timestamp updateTime;
    private Long cancelUid;
    private Timestamp cancelTime;

    private Integer remainTime;
}
