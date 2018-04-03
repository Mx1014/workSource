package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.pm.CreateOrganizationOwnerCommand;

/**
 * <ul>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>enterpriseCustomer: 创建企业用户参数 参考{@link com.everhomes.rest.customer.CreateEnterpriseCustomerCommand}</li>
 *     <li>individualCustomer: 创建个人用户参数 参考{@link com.everhomes.rest.organization.pm.CreateOrganizationOwnerCommand}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/5.
 */
public class CreateCustomerCommand {

    private byte customerType;

    @ItemType(CreateEnterpriseCustomerCommand.class)
    private CreateEnterpriseCustomerCommand enterpriseCustomer;

    @ItemType(CreateOrganizationOwnerCommand.class)
    private CreateOrganizationOwnerCommand individualCustomer;

    public byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(byte customerType) {
        this.customerType = customerType;
    }

    public CreateEnterpriseCustomerCommand getEnterpriseCustomer() {
        return enterpriseCustomer;
    }

    public void setEnterpriseCustomer(CreateEnterpriseCustomerCommand enterpriseCustomer) {
        this.enterpriseCustomer = enterpriseCustomer;
    }

    public CreateOrganizationOwnerCommand getIndividualCustomer() {
        return individualCustomer;
    }

    public void setIndividualCustomer(CreateOrganizationOwnerCommand individualCustomer) {
        this.individualCustomer = individualCustomer;
    }
}
