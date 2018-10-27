package com.everhomes.yellowPage;

import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.UpdateQuery;

public interface DeleteQueryBuilderCallback {
	 void buildCondition(DeleteQuery<? extends Record> query);
}
