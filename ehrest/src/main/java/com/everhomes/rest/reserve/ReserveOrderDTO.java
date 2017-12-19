package com.everhomes.rest.reserve;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 订单id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.reserve.ReserveOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>resourceType: 资源类型</li>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>resourceJson: 资源json数据</li>
 * <li>orderNo: 订单编号</li>
 * <li>reserveStartTime: 预约开始时间</li>
 * <li>reserveEndTime: 预约结束时间</li>
 * <li>reserveCellCount: 预约单元格数量</li>
 * <li>actualStartTime: 实际开始使用时间</li>
 * <li>actualEndTime: 实际结束使用时间</li>
 * <li>applicantEnterpriseId: 预约人公司id</li>
 * <li>applicantEnterpriseName: 预约人公司名称</li>
 * <li>applicantPhone: 预约人手机号</li>
 * <li>applicantName: 预约人名称</li>
 * <li>addressId: 门牌id</li>
 * <li>address: 门牌地址 </li>
 * <li>overTime: 超时时间 </li>
 * <li>payTime: 每页条数</li>
 * <li>payType: 每页条数</li>
 * <li>paidAmount: 每页条数</li>
 * <li>owingAmount: 每页条数</li>
 * <li>totalAmount: 每页条数</li>
 * <li>status: 订单状态 {@link com.everhomes.rest.reserve.ReserveOrderStatus}</li>
 * <li>creatorUid: 每页条数</li>
 * <li>createTime: 每页条数</li>
 * <li>updateUid: 每页条数</li>
 * <li>updateTime: 每页条数</li>
 * <li>cancelUid: 每页条数</li>
 * <li>cancelTime: 每页条数</li>
 * <li>remainPayTime: 剩余支付时间</li>
 * </ul>
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
    private String address;
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

    private Integer remainPayTime;
}
