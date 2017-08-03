package com.everhomes.rest.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by ying.xiong on 2017/8/3.
 */
public class ContractDetailDTO {
    private Long parentId;
    private Long rootParentId;
    private Long customerId;
    private String customerName;
    private String contractNumber;
    private Timestamp contractEndDate;
    private Timestamp contractStartDate;
    private String name;
    private Byte contractType;
    private Byte partyAType;
    private Long partyAId;
    private Byte customerType;
    private String contractSituation;
    private Long categoryItemId;
    private String filingPlace;
    private String recordNumber;
    private String invalidReason;
    private Timestamp signedTime;
    private Double rentSize;
    private BigDecimal rent;
    private BigDecimal downpayment;
    private Timestamp downpaymentTime;
    private BigDecimal deposit;
    private Timestamp depositTime;
    private BigDecimal contractualPenalty;
    private String penaltyRemark;
    private BigDecimal commission;
    private String paidType;
    private Integer freeDays;
    private Integer freeParkingSpace;
    private Timestamp decorateBeginDate;
    private Timestamp decorateEndDate;
    private String signedPurpose;
    private String denunciationReason;
}
