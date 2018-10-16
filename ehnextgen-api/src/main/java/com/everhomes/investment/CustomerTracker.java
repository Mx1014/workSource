package com.everhomes.investment;

        import com.everhomes.server.schema.tables.pojos.EhCustomerTrackers;
        import com.everhomes.util.StringHelper;

public class CustomerTracker extends EhCustomerTrackers {

    private static final long serialVersionUID = -3230993663167759962L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
