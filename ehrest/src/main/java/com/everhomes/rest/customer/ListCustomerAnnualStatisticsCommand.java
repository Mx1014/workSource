package com.everhomes.rest.customer;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>turnoverMinimum: 营业额最低值</li>
 *     <li>turnoverMaximum: 营业额最高值</li>
 *     <li>taxPaymentMinimum: 纳税额最低值</li>
 *     <li>taxPaymentMaximum: 纳税额最高值</li>
 *     <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 *     <li>pageSize: 每页的数量</li>
 * </ul>
 * Created by ying.xiong on 2017/11/8.
 */
public class ListCustomerAnnualStatisticsCommand {
    private Integer namespaceId;

    private Long communityId;

    private BigDecimal turnoverMinimum;
    private BigDecimal turnoverMaximum;
    private BigDecimal taxPaymentMinimum;
    private BigDecimal taxPaymentMaximum;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public BigDecimal getTaxPaymentMaximum() {
        return taxPaymentMaximum;
    }

    public void setTaxPaymentMaximum(BigDecimal taxPaymentMaximum) {
        this.taxPaymentMaximum = taxPaymentMaximum;
    }

    public BigDecimal getTaxPaymentMinimum() {
        return taxPaymentMinimum;
    }

    public void setTaxPaymentMinimum(BigDecimal taxPaymentMinimum) {
        this.taxPaymentMinimum = taxPaymentMinimum;
    }

    public BigDecimal getTurnoverMaximum() {
        return turnoverMaximum;
    }

    public void setTurnoverMaximum(BigDecimal turnoverMaximum) {
        this.turnoverMaximum = turnoverMaximum;
    }

    public BigDecimal getTurnoverMinimum() {
        return turnoverMinimum;
    }

    public void setTurnoverMinimum(BigDecimal turnoverMinimum) {
        this.turnoverMinimum = turnoverMinimum;
    }
}
