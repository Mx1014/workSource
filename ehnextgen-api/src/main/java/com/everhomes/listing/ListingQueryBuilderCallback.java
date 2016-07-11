// @formatter:off
package com.everhomes.listing;

import org.jooq.Record;
import org.jooq.SelectQuery;

public interface ListingQueryBuilderCallback {
    SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query);
}
