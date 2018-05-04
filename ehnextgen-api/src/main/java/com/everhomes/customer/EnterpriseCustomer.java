package com.everhomes.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomers;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by ying.xiong on 2017/8/11.
 */
public class EnterpriseCustomer extends EhEnterpriseCustomers {

    private static final long serialVersionUID = -6128098809813016697L;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> banner;

    public List<AttachmentDescriptor> getBanner() {
        return banner;
    }

    public void setBanner(List<AttachmentDescriptor> banner) {
        this.banner = banner;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
